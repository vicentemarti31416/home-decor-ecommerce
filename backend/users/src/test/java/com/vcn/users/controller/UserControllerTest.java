package com.vcn.users.controller;

import com.vcn.users.config.SecurityConfig;
import com.vcn.users.exception.NotFoundException;
import com.vcn.users.exception.UserAlreadyExistsException;
import com.vcn.users.model.CustomerResponse;
import com.vcn.users.model.RegisterRequest;
import com.vcn.users.model.ResetPassword;
import com.vcn.users.model.ShipmentDetailsRequest;
import com.vcn.users.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;

    @Test
    void register_validRequest_returns201() throws Exception {
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "username": "vicente@gmail.com",
                                    "password": "1234",
                                    "name": "Vicente",
                                    "lastName": "Martí"
                                    }
                                """))
                .andExpect(status().isCreated());
        verify(userService).register(new RegisterRequest("vicente@gmail.com", "1234", "Vicente", "Martí", null));
    }

    @Test
    void register_invalidRequest_returns400() throws Exception {
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "username": "",
                                "password": "",
                                "name": "",
                                "lastName": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").value(startsWith("Field: username :")))
                .andExpect(jsonPath("$.password").value(startsWith("Field: password :")));
        verifyNoInteractions(userService);
    }

    @Test
    void register_withExistingUser_returnsUserAlreadyExists() throws Exception {
        doThrow(new UserAlreadyExistsException("El usuario ya existe"))
                .when(userService).register(any(RegisterRequest.class));
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                    {
                                    "username": "vicente@gmail.com",
                                    "password": "1234",
                                    "name": "Vicente",
                                    "lastName": "Martí"
                                    }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("El usuario ya existe"));
        verify(userService).register(any(RegisterRequest.class));
    }

    @Test
    void sendCode_withValidEmail_returns200() throws Exception {
        mockMvc.perform(post("/users/send-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("vicente@gmail.com"))
                .andExpect(status().isOk());
        verify(userService).sendCode("vicente@gmail.com");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "v", "v@"})
    void sendCode_withInvalidEmail_returns400(String email) throws Exception {
        mockMvc.perform(post("/users/send-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(email))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(userService);
    }

    @Test
    void resetPassword_withValidData_returns200() throws Exception {
        mockMvc.perform(post("/users/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "code": "code",
                                    "password": "password"
                                    }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Contraseña actualizada correctamente"));
        verify(userService).resetPassword(new ResetPassword("code", "password"));
    }

    @Test
    void resetPassword_withInvalidData_returns400() throws Exception {
        mockMvc.perform(post("/users/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "code": "",
                                    "password": ""
                                    }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(startsWith("Field: code :")))
                .andExpect(jsonPath("$.password").value(startsWith("Field: password :")));
        verifyNoInteractions(userService);
    }

    @Test
    void resetPassword_withBlankData_returns400() throws Exception {
        mockMvc.perform(post("/users/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "code": "",
                                    "password": ""
                                    }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.password").exists());
        verifyNoInteractions(userService);
    }

    @Test
    void enableEmail_withValidData_returns200() throws Exception {
        mockMvc.perform(get("/users/enable")
                        .param("token", "token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Cuenta activada correctamente"));
        verify(userService).enableEmail("token");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void enableEmail_withInvalidData_returns400(String token) throws Exception {
        mockMvc.perform(get("/users/enable")
                        .param("token", token))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(userService);
    }

    @Test
    void enableEmail_withoutData_returns400() throws Exception {
        mockMvc.perform(get("/users/enable"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(userService);
    }

    @Test
    void saveShipmentDetails_withValidData_returns200() throws Exception {
        mockMvc.perform(patch("/users/shipment-details")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "address": "Calle Rue del Percebe, 13",
                                    "city": "Sevilla",
                                    "postalCode": "17962",
                                    "phone": "1717525"
                                    }
                                """))
                .andExpect(status().isOk());
        verify(userService).saveShipmentDetails(eq(
                new ShipmentDetailsRequest("Calle Rue del Percebe, 13", "Sevilla", "17962", "1717525")
        ), any(Jwt.class));
    }

    @Test
    void saveShipmentDetails_withoutAuthentication_returns401() throws Exception {
        mockMvc.perform(patch("/users/shipment-details")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "address": "Calle Rue del Percebe, 13",
                                    "city": "Sevilla",
                                    "postalCode": "17962",
                                    "phone": "1717525"
                                    }
                                """))
                .andExpect(status().isUnauthorized());
        verifyNoInteractions(userService);
    }

    @Test
    void addFavorite_withExistingProduct() throws Exception {
        when(userService.addFavorite(eq(1L), any(Jwt.class)))
                .thenReturn(CustomerResponse.builder().favoriteIds(Set.of(1L)).build());
        mockMvc.perform(post("/users/favorite/{productId}", 1L)
                        .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favoriteIds[0]").value(1L));
        verify(userService).addFavorite(eq(1L), any(Jwt.class));
    }

    @Test
    void addFavorite_withNonExistingProduct_throwsNotFound() throws Exception {
        when(userService
                .addFavorite(eq(1L), any(Jwt.class)))
                .thenThrow(new NotFoundException("Producto no encontrado"));
        mockMvc.perform(post("/users/favorite/{productId}", 1L)
                        .with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Producto no encontrado"));
        verify(userService).addFavorite(eq(1L), any(Jwt.class));
    }

    @Test
    void addFavorite_withoutAuthentication_returns401() throws Exception {
        mockMvc.perform(post("/users/favorite/{productId}", 1L))
                .andExpect(status().isUnauthorized());
        verifyNoInteractions(userService);
    }

    @Test
    void deleteFavorite_withExistingProduct() throws Exception {
        when(userService.deleteFavorite(eq(1L), any(Jwt.class)))
                .thenReturn(CustomerResponse.builder().favoriteIds(Set.of(3L)).build());
        mockMvc.perform(delete("/users/favorite/{productId}", 1L)
                        .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favoriteIds", not(hasItem(1))))
                .andExpect(jsonPath("$.favoriteIds", hasItem(3)));
        verify(userService).deleteFavorite(eq(1L), any(Jwt.class));
    }

    @Test
    void deleteFavorite_withNonSavedProduct_throwsNotFound() throws Exception {
        when(userService.deleteFavorite(eq(1L), any(Jwt.class)))
                .thenThrow(new NotFoundException("No tiene guardado ese favorito"));
        mockMvc.perform(delete("/users/favorite/{productId}", 1L)
                        .with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No tiene guardado ese favorito"));
        verify(userService).deleteFavorite(eq(1L), any(Jwt.class));
    }

    @Test
    void deleteFavorite_withoutAuthentication_returns401() throws Exception {
        mockMvc.perform(delete("/users/favorite/{productId}", 1L))
                .andExpect(status().isUnauthorized());
        verifyNoInteractions(userService);
    }

    @Test
    void getCustomerName_withAuthentication_returnsName() throws Exception {
        when(userService.getCustomerName("vicente@gmail.com")).thenReturn("vicente");
        mockMvc.perform(get("/users/restclient/customer")
                        .with(jwt().jwt(builder -> builder.subject("vicente@gmail.com"))))
                .andExpect(status().isOk())
                .andExpect(content().string("vicente"));
        verify(userService).getCustomerName("vicente@gmail.com");
    }

    @Test
    void getCustomerName_withoutAuthentication_returns401() throws Exception {
        mockMvc.perform(get("/users/restclient/customer"))
                .andExpect(status().isUnauthorized());
        verifyNoInteractions(userService);
    }

    @Test
    void me_withAuthentication_returnsCustomerResponse() throws Exception {
        when(userService.me(any(Jwt.class)))
                .thenReturn(CustomerResponse.builder().name("vicente").build());
        mockMvc.perform(get("/users/me")
                        .with(jwt().jwt(builder -> builder.subject("vicente@gmail.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("vicente"));
        verify(userService).me(any(Jwt.class));
    }

    @Test
    void me_withoutAuthentication_returns401() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());
        verifyNoInteractions(userService);
    }
}
