package com.vcn.users.service;

import com.vcn.users.model.*;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

public interface UserService {

    void register(RegisterRequest register);
    void sendCode(String email);
    void enableEmail(String token);
    void sendEmail(EmailRequest emailRequest);
    CustomerResponse saveShipmentDetails(RegisterRequest registerRequest, Jwt jwt);
    CustomerResponse addFavorite(Long productId, Jwt jwt);
    CustomerResponse deleteFavorite(Long productId, Jwt jwt);
    CustomerResponse me(Jwt jwt);
    void resetPassword(ResetPassword resetPassword);
    String createSecureRandom();
    String getCustomerName(String username);
    CustomerResponse save(User user);
    User findByUsername(String username);
    User findById(String id);
    SecurityToken findByToken(String token);
    void deleteTokenById(UUID id);
    CustomerResponse toCustomerResponse(User user);
}
