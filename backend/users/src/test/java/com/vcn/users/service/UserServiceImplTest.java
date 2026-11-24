package com.vcn.users.service;

import com.vcn.users.client.ProductClient;
import com.vcn.users.exception.NotFoundException;
import com.vcn.users.exception.UserAlreadyExistsException;
import com.vcn.users.model.*;
import com.vcn.users.publisher.Publisher;
import com.vcn.users.repository.SecurityTokenRepository;
import com.vcn.users.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Con @ExtendWith(MockitoExtension.class) + @Mock + @InjectMocks se crean el service con los 4 mocks por constructor
 * , sin levantar Spring.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private SecurityTokenRepository securityTokenRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Publisher publisher;
    @Mock
    private ProductClient productClient;

    @InjectMocks
    private UserServiceImpl userService;

    /**
     * ReflectionTestUtils.setField hace falta porque los nombres de las colas de rabbitmq van con @Value en campos.
     * Sin Spring quedan a null
     */
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userService, "enableAccountQueue", "enable_account");
        ReflectionTestUtils.setField(userService, "resetPasswordQueue", "reset_password");
        ReflectionTestUtils.setField(userService, "createParticipantQueue", "create_participant");
    }

    /**
     * Para testear el register se ha de leer el SecurityContextHolder y hacer una autenticación a mano.
     * Se limpia el contexto para que una autenticación anterior no afecte a la siguiente
     */
    @AfterEach
    void cleaningContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void register_withValidData_savesUserAndPublishesMessages() {
        authenticateAs("ROLE_ANONYMOUS");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.existsById("vicente@mail.com")).thenReturn(false);
        when(passwordEncoder.encode("1234")).thenReturn("password-1234");
        userService.register(registerRequest("vicente@mail.com", null));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User user = userCaptor.getValue();
        assertThat(user.getUsername()).isEqualTo("vicente@mail.com");
        assertThat(user.getPassword()).isEqualTo("password-1234");
        assertThat(user.isEnabled()).isFalse();
        assertThat(user.getCustomer().getName()).isEqualTo("Vicente");
        assertThat(user.getAuthorities())
                .extracting(Authority::getRole)
                .containsExactly(Role.ROLE_USER);
        ArgumentCaptor<SecurityToken> tokenCaptor = ArgumentCaptor.forClass(SecurityToken.class);
        verify(securityTokenRepository).save(tokenCaptor.capture());
        ArgumentCaptor<EmailRequest> emailCaptor = ArgumentCaptor.forClass(EmailRequest.class);
        verify(publisher).send(eq("enable_account"), emailCaptor.capture());
        assertThat(emailCaptor.getValue().recipient()).isEqualTo("vicente@mail.com");
        assertThat(emailCaptor.getValue().message()).isEqualTo(tokenCaptor.getValue().getToken());
        verify(publisher).send("create_participant", "vicente@mail.com");
    }

    @Test
    void register_beingAdmin_adminCreatingAnotherAdmin() {
        authenticateAs("ROLE_ADMIN");
        when(userRepository.existsById("admin2@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("1234")).thenReturn("encoded-1234");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        userService.register(registerRequest("admin2@gmail.com", List.of(Role.ROLE_ADMIN)));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User user = userCaptor.getValue();
        assertThat(user.getAuthorities()).extracting(Authority::getRole).containsExactly(Role.ROLE_ADMIN);
    }

    @Test
    void register_requestingAdminWithoutBeingAdmin_throwsAccessDenied() {
        authenticateAs("ROLE_USER");
        assertThatThrownBy(() -> userService
                .register(registerRequest("vicente@mail.com", List.of(Role.ROLE_ADMIN))))
                .isInstanceOf(AccessDeniedException.class);
        verifyNoInteractions(userRepository, securityTokenRepository, publisher);
    }

    @Test
    void register_existingUser_throwsUserAlreadyExists() {
        authenticateAs("ROLE_ANONYMOUS");
        when(userRepository.existsById("vicente@mail.com")).thenReturn(true);
        assertThatThrownBy(() -> userService
                .register(registerRequest("vicente@mail.com", null)))
                .isInstanceOf(UserAlreadyExistsException.class);
        verify(userRepository, never()).save(any());
        verifyNoInteractions(publisher);
    }

    @Test
    void sendCode_withValidEmail_savesSecurityTokenAndSendsCode() {
        userService.sendCode("vicente@gmail.com");
        ArgumentCaptor<SecurityToken> tokenCaptor = ArgumentCaptor.forClass(SecurityToken.class);
        verify(securityTokenRepository).save(tokenCaptor.capture());
        SecurityToken securityToken = tokenCaptor.getValue();
        assertThat(securityToken.getUserId()).isEqualTo("vicente@gmail.com");
        ArgumentCaptor<EmailRequest> emailCaptor = ArgumentCaptor.forClass(EmailRequest.class);
        verify(publisher).send(eq("reset_password"), emailCaptor.capture());
        EmailRequest emailRequest = emailCaptor.getValue();
        assertThat(emailRequest.recipient()).isEqualTo("vicente@gmail.com");
        assertThat(emailRequest.subject()).isEqualTo("Código de recuperación recibido");
        assertThat(emailRequest.message()).isEqualTo(securityToken.getToken());
    }

    @Test
    void enableEmail_withValidToken_setsEnabledSavesAndDeletesToken() {
        // Creamos el token y el user porque no se crean dentro de la función a testear
        SecurityToken securityToken = new SecurityToken(UUID.randomUUID(), "vicente@gmail.com", "token");
        User user = getUser("vicente@gmail.com");
        // Los recuperamos con when para tener su referencia
        when(securityTokenRepository.findByToken("token")).thenReturn(Optional.of(securityToken));
        when(userRepository.findById("vicente@gmail.com")).thenReturn(Optional.of(user));
        // Sin esta sentencia el repository devuelve el user, toCustomerResponse recibe un objeto válido
        // y construye el CustomerResponse. Sin ella devolvería null
        when(userRepository.save(user)).thenReturn(user);
        // Llama a la función del service
        userService.enableEmail("token");
        assertThat(user.isEnabled()).isTrue();
        // Verificamos las llamadas a las funciones de los mock
        verify(userRepository).save(user);
        verify(securityTokenRepository).deleteById(securityToken.getId());
    }

    @Test
    void saveShipmentDetails_withValidData_savesData() {
        ShipmentDetailsRequest shipmentDetailsRequest = new ShipmentDetailsRequest(
                "Calle Rue del percebe 13",
                "Barakaldo",
                "46156",
                "1717525"
        );
        User user = getUser("vicente@gmail.com");
        when(userRepository.findByUsername("vicente@gmail.com")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        userService.saveShipmentDetails(shipmentDetailsRequest, getToken("vicente@gmail.com"));
        assertThat(user.getCustomer().getAddress()).isEqualTo("Calle Rue del percebe 13");
        assertThat(user.getCustomer().getCity()).isEqualTo("Barakaldo");
        assertThat(user.getCustomer().getPostalCode()).isEqualTo("46156");
        assertThat(user.getCustomer().getPhone()).isEqualTo("1717525");
        verify(userRepository).save(user);
    }

    @Test
    void addFavorite_withExistingProduct_addsProductIdToCustomer() {
        User user = getUser("vicente@mail.com");
        when(userRepository.findByUsername("vicente@mail.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        userService.addFavorite(5L, getToken("vicente@mail.com"));
        assertThat(user.getCustomer().getFavoriteIds()).containsExactly(5L);
        verify(productClient).findProductById(eq(5L), any(Jwt.class));
    }

    @Test
    void addFavorite_withNonExistingProduct_throwsNotFound() {
        doThrow(new NotFoundException("Producto no encontrado"))
                .when(productClient).findProductById(eq(3L), any(Jwt.class));
        assertThatThrownBy(() -> userService.addFavorite(3L, getToken("vicente@gmail.com")))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Producto no encontrado");
        verifyNoInteractions(userRepository);
    }

    @Test
    void deleteFavorite_existingFavorite_removesItFromCustomer() {
        User user = getUser("vicente@gmail.com");
        when(userRepository.findByUsername("vicente@gmail.com")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        user.getCustomer().getFavoriteIds().add(1L);
        user.getCustomer().getFavoriteIds().add(2L);
        userService.deleteFavorite(1L, getToken("vicente@gmail.com"));
        assertThat(user.getCustomer().getFavoriteIds()).containsExactly(2L);
    }

    @Test
    void deleteFavorite_nonExistingFavorite_throwsNotFound() {
        User user = getUser("vicente@gmail.com");
        user.getCustomer().getFavoriteIds().add(1L);
        when(userRepository.findByUsername("vicente@gmail.com")).thenReturn(Optional.of(user));
        assertThatThrownBy(() -> userService
                .deleteFavorite(2L, getToken("vicente@gmail.com")))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("No tiene guardado ese favorito");
        verify(userRepository, never()).save(any());
    }

    @Test
    void me_userRegistered_toCostumerResponse() {
        User user = getUser("vicente@gmail.com");
        user.getCustomer().setName("Vicente");
        user.getCustomer().setCity("Murcia");
        when(userRepository.findByUsername("vicente@gmail.com")).thenReturn(Optional.of(user));
        CustomerResponse customerResponse = userService.me(getToken("vicente@gmail.com"));
        assertThat(customerResponse.username()).isEqualTo("vicente@gmail.com");
        assertThat(customerResponse.name()).isEqualTo("Vicente");
        assertThat(customerResponse.city()).isEqualTo("Murcia");
    }

    @Test
    void me_userNotRegistered_throwsNotFound() {
        when(userRepository.findByUsername("vicente@mail.com")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService
                .me(getToken("vicente@mail.com")))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void resetPassword_withValidData_savesPasswordAndSendsEmail() {
        SecurityToken securityToken = new SecurityToken(UUID.randomUUID(), "vicente@gmail.com", "token");
        User user = getUser("vicente@gmail.com");
        when(securityTokenRepository.findByToken("token")).thenReturn(Optional.of(securityToken));
        when(userRepository.findByUsername("vicente@gmail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("new-password")).thenReturn("encoded-password");
        when(userRepository.save(user)).thenReturn(user);
        userService.resetPassword(new ResetPassword("token", "new-password"));
    }

    @Test
    void getCustomerName_withValidUsername_returnsCustomerName() {
        User user = getUser("vicente@gmail.com");
        user.getCustomer().setName("Vicente");
        when(userRepository.findByUsername("vicente@gmail.com")).thenReturn(Optional.of(user));
        String username = userService.getCustomerName("vicente@gmail.com");
        assertThat(username).isEqualTo("Vicente");
    }

    @Test
    void getCustomerName_withNonexistingUsername_throwsNotFound() {
        when(userRepository.findByUsername("v")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService
                .getCustomerName("v"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Usuario no encontrado");
    }

    private void authenticateAs(String role) {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("caller", null, role));
    }

    private RegisterRequest registerRequest(String username, List<Role> roles) {
        return new RegisterRequest(username, "1234", "Vicente", "Martí", roles);
    }

    private User getUser(String username) {
        Customer customer = new Customer();
        customer.setEmail(username);
        User user = new User();
        user.setUsername(username);
        user.setCustomer(customer);
        customer.setUser(user);
        return user;
    }

    private Jwt getToken(String subject) {
        return Jwt.withTokenValue("token").header("alg", "none").subject(subject).build();
    }
}
