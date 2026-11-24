package com.vcn.users.integration;

import com.vcn.users.client.ProductClient;
import com.vcn.users.exception.NotFoundException;
import com.vcn.users.model.Customer;
import com.vcn.users.model.SecurityToken;
import com.vcn.users.model.User;
import com.vcn.users.repository.SecurityTokenRepository;
import com.vcn.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mariadb.MariaDBContainer;
import org.testcontainers.rabbitmq.RabbitMQContainer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
public class UserIntegrationTest {

    @Container
    @ServiceConnection
    static MariaDBContainer mariaDBContainer = new MariaDBContainer("mariadb:10.11");

    @Container
    @ServiceConnection
    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:4-management");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityTokenRepository securityTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private ProductClient productClient;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
        securityTokenRepository.deleteAll();
    }

    @Test
    void register_withValidData_returnsCreated() throws Exception {
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "vicente@gmail.com",
                        "password": "password",
                        "name": "name",
                        "lastName": "lastName",
                        "roles": null
                        }
                        """)).andExpect(status().isCreated());
        Optional<User> userOptional = userRepository.findByUsername("vicente@gmail.com");
        assertThat(userOptional).isPresent();
        assertThat(userOptional.get().isEnabled()).isFalse();
        assertThat(userOptional.get().getPassword()).isNotEqualTo("password");
    }

    @Test
    void register_withInvalidData_returns400() throws Exception {
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username": "",
                                "password": "",
                                "name": "",
                                "lastName": "",
                                "roles": null
                                }
                                """))
                .andExpect(status().isBadRequest());
        assertThat(userRepository.count()).isZero();
    }

    @Test
    void register_withExistingUser_throws409() throws Exception {
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username": "vicente@gmail.com",
                                "password": "password",
                                "name": "name",
                                "lastName": "lastName",
                                "roles": null
                                }
                                """))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username": "vicente@gmail.com",
                                "password": "password",
                                "name": "name",
                                "lastName": "lastName",
                                "roles": null
                                }
                                """))
                .andExpect(status().isConflict());
        assertThat(userRepository.count()).isOne();
    }

    @Test
    void sendCode_withValidEmail_returns200() throws Exception {
        mockMvc.perform(post("/users/send-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("vicente@gmail.com"))
                .andExpect(status().isOk());
        List<SecurityToken> tokenList = securityTokenRepository.findAll();
        assertThat(securityTokenRepository.count()).isOne();
        assertThat(tokenList).hasSize(1);
        SecurityToken securityToken = tokenList.getFirst();
        assertThat(securityToken.getUserId()).isEqualTo("vicente@gmail.com");
        assertThat(securityToken.getToken()).isNotBlank();
    }

    @Test
    void sendCode_withInValidEmail_returns400() throws Exception {
        mockMvc.perform(post("/users/send-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("12345"))
                .andExpect(status().isBadRequest());
        assertThat(securityTokenRepository.count()).isZero();
    }

    @Test
    void resetPassword_withValidData_returns200() throws Exception {
        registerUser();
        securityTokenRepository.save(new SecurityToken(null, "vicente@gmail.com", "12345"));
        mockMvc.perform(post("/users/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "code": "12345",
                                "password": "new-password"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Contraseña actualizada correctamente"));
        String newPassword = userRepository.findByUsername("vicente@gmail.com").get().getPassword();
        assertThat(passwordEncoder.matches("new-password", newPassword)).isTrue();
        assertThat(securityTokenRepository.findByToken("12345").isEmpty()).isTrue();
    }

    @Test
    void resetPassword_withInvalidData_returns400() throws Exception {
        registerUser();
        securityTokenRepository.save(new SecurityToken(null, "vicente@gmail.com", "12345"));
        mockMvc.perform(post("/users/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "code": "",
                                "password": ""
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void enableMail_withValidData_returns200() throws Exception {
        registerUser();
        String token = securityTokenRepository.findAll().getFirst().getToken();
        mockMvc.perform(get("/users/enable")
                        .param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Cuenta activada correctamente"));
        User user = userRepository.findByUsername("vicente@gmail.com").orElseThrow();
        assertThat(user.isEnabled()).isTrue();
        assertThat(securityTokenRepository.findByToken(token).isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void enableMail_withInvalidData_returns400(String token) throws Exception {
        mockMvc.perform(get("/users/enable")
                        .param("token", token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void enableMail_withNullToken_returns400() throws Exception {
        mockMvc.perform(get("/users/enable"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveShipmentDetails_withValidData_returns200() throws Exception {
        registerUser();
        mockMvc.perform(patch("/users/shipment-details")
                        .with(jwt().jwt(builder -> builder.subject("vicente@gmail.com")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "address": "Calle Rue del percebe 13",
                                "city": "Pamplona",
                                "postalCode": "52360",
                                "phone": "725412368"
                                }
                                """))
                .andExpect(status().isOk());
        User user = userRepository.findByUsername("vicente@gmail.com").orElseThrow();
        Customer customer = user.getCustomer();
        assertThat(customer.getAddress()).isEqualTo("Calle Rue del percebe 13");
        assertThat(customer.getCity()).isEqualTo("Pamplona");
        assertThat(customer.getPostalCode()).isEqualTo("52360");
        assertThat(customer.getPhone()).isEqualTo("725412368");
    }

    @Test
    void saveShipmentDetails_withInvalidData_returns400() throws Exception {
        registerUser();
        mockMvc.perform(patch("/users/shipment-details")
                        .with(jwt().jwt(builder -> builder.subject("vicente@gmail.com")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "address": "",
                                "city": "",
                                "postalCode": "",
                                "phone": ""
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveShipmentDetails_withoutAuthentication_returns401() throws Exception {
        mockMvc.perform(patch("/users/shipment-details"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void addFavorite_withExistingProduct_returns200() throws Exception {
        registerUser();
        mockMvc.perform(post("/users/favorite/{productId}", 1L)
                        .with(jwt().jwt(builder -> builder.subject("vicente@gmail.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favoriteIds", hasItem(1)));
        User user = userRepository.findByUsername("vicente@gmail.com").orElseThrow();
        assertThat(user.getCustomer().getFavoriteIds()).contains(1L);
    }

    @Test
    void addFavorite_withNonExistingProduct_returns404() throws Exception {
        doThrow(new NotFoundException("Producto no encontrado"))
                .when(productClient).findProductById(eq(1L), any(Jwt.class));
        registerUser();
        mockMvc.perform(post("/users/favorite/{productId}", 1L)
                        .with(jwt().jwt(builder -> builder.subject("vicente@gmail.com"))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Producto no encontrado"));
        mockMvc.perform(get("/users/me")
                        .with(jwt().jwt(builder -> builder.subject("vicente@gmail.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favoriteIds").isEmpty());
        User user = userRepository.findByUsername("vicente@gmail.com").orElseThrow();
        assertThat(user.getCustomer().getFavoriteIds()).doesNotContain(1L);
    }

    @Test
    void addFavorite_withoutAuthentication_returns401() throws Exception {
        mockMvc.perform(post("/users/favorite/{productId}", 1L))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteFavorite_withExistingFavorite_returns200() throws Exception {
        registerUser();
        mockMvc.perform(post("/users/favorite/{productId}", 1L)
                        .with(jwt().jwt(builder -> builder.subject("vicente@gmail.com"))))
                .andExpect(status().isOk());
        mockMvc.perform(post("/users/favorite/{productId}", 3L)
                        .with(jwt().jwt(builder -> builder.subject("vicente@gmail.com"))))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/users/favorite/{productId}", 1L)
                        .with(jwt().jwt(builder -> builder.subject("vicente@gmail.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favoriteIds", not(hasItem(1))))
                .andExpect(jsonPath("$.favoriteIds", hasItem(3)));
    }

    @Test
    void deleteFavorite_withNonExistingFavorite_returns404() throws Exception {
        registerUser();
        mockMvc.perform(delete("/users/favorite/{productId}", 1L)
                        .with(jwt().jwt(builder -> builder.subject("vicente@gmail.com"))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No tiene guardado ese favorito"));
    }

    @Test
    void deleteFavorite_withoutAuthentication_returns401() throws Exception {
        registerUser();
        mockMvc.perform(delete("/users/favorite/{productId}", 1L))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getCustomerName_withExistingUser_returnsString() throws Exception {
        registerUser();
        mockMvc.perform(get("/users/restclient/customer")
                        .with(jwt().jwt(builder -> builder.subject("vicente@gmail.com"))))
                .andExpect(status().isOk())
                .andExpect(content().string("vicente"));
    }

    @Test
    void getCustomerName_withoutAuthentication_returns401() throws Exception {
        mockMvc.perform(get("/users/restclient/customer"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findByUsername_withExistingUser_returns200() throws Exception {
        registerUser();
        mockMvc.perform(get("/users/me")
                        .with(jwt().jwt(builder -> builder.subject("vicente@gmail.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("vicente@gmail.com"));
    }

    @Test
    void findByUsername_withNonExistingUser_returns404() throws Exception {
        mockMvc.perform(get("/users/me")
                .with(jwt().jwt(builder -> builder.subject("vicente@gmail.com"))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Usuario no encontrado"));
    }

    @Test
    void findByUsername_withoutAuthentication_returns401() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());
    }

    private void registerUser() throws Exception {
        assert mockMvc != null;
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username": "vicente@gmail.com",
                                "password": "12345",
                                "name": "vicente",
                                "lastName": "martí",
                                "roles": null
                                }
                                """))
                .andExpect(status().isCreated());
    }
}
