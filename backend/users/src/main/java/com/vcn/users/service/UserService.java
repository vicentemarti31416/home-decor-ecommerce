package com.vcn.users.service;

import com.vcn.users.model.*;
import org.springframework.security.oauth2.jwt.Jwt;

public interface UserService {

    void register(RegisterRequest register);
    void sendCode(String email);
    void enableEmail(String token);
    CustomerResponse saveShipmentDetails(ShipmentDetailsRequest shipmentDetailsRequest, Jwt jwt);
    CustomerResponse addFavorite(Long productId, Jwt jwt);
    CustomerResponse deleteFavorite(Long productId, Jwt jwt);
    CustomerResponse me(Jwt jwt);
    void resetPassword(ResetPassword resetPassword);
    String getCustomerName(String username);
}
