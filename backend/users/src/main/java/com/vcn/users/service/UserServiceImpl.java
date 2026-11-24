package com.vcn.users.service;

import com.vcn.users.client.ProductClient;
import com.vcn.users.exception.NotFoundException;
import com.vcn.users.exception.UserAlreadyExistsException;
import com.vcn.users.model.*;
import com.vcn.users.publisher.Publisher;
import com.vcn.users.repository.SecurityTokenRepository;
import com.vcn.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Value("${rabbitmq.queue.users-enable_account}")
    private String enableAccountQueue;

    @Value("${rabbitmq.queue.users-reset_password}")
    private String resetPasswordQueue;

    @Value("${rabbitmq.queue.users-password_changed}")
    private String passwordChangedQueue;

    @Value("${rabbitmq.queue.users-create_participant}")
    private String createParticipantQueue;

    private final UserRepository userRepository;
    private final SecurityTokenRepository securityTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final Publisher publisher;
    private final ProductClient productClient;

    public UserServiceImpl(UserRepository userRepository,
                           SecurityTokenRepository securityTokenRepository,
                           PasswordEncoder passwordEncoder,
                           Publisher publisher,
                           ProductClient productClient) {
        this.userRepository = userRepository;
        this.securityTokenRepository = securityTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.publisher = publisher;
        this.productClient = productClient;
    }

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        // Solo un admin autenticado puede asignar ROLE_ADMIN
        boolean requestingAdmin = request.roles().contains(Role.ROLE_ADMIN);
        boolean callerIsAdmin = Objects.requireNonNull(SecurityContextHolder.getContext()
                        .getAuthentication()).getAuthorities().stream()
                .anyMatch(authority -> Objects.equals(authority.getAuthority(), "ROLE_ADMIN"));
        if (requestingAdmin && !callerIsAdmin) {
            throw new AccessDeniedException("No puedes asignarte ROLE_ADMIN");
        }

        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new UserAlreadyExistsException("El usuario ya existe");
        }
        Customer customer = new Customer();
        customer.setEmail(request.username());
        customer.setName(request.name());
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEnabled(false);
        customer.setUser(user);
        user.setCustomer(customer);
        request.roles().forEach(role -> {
            Authority authority = new Authority();
            authority.setRole(role);
            authority.setUser(user);
            user.getAuthorities().add(authority);
        });
        User userDB = userRepository.save(user);
        SecurityToken securityToken = new SecurityToken(null, userDB.getId(), createSecureRandom());
        securityTokenRepository.save(securityToken);
        String subject = "Mensaje de confirmación de email";
        publisher.send(enableAccountQueue, new EmailRequest(user.getUsername(), subject, securityToken.getToken()));
        publisher.send(createParticipantQueue, user.getUsername());
    }

    @Override
    public void sendCode(String email) {
        String code = createSecureRandom();
        SecurityToken securityToken = new SecurityToken(null, email, code);
        securityTokenRepository.save(securityToken);
        String subject = "Código de recuperación recibido";
        publisher.send(resetPasswordQueue, new EmailRequest(email, subject, code));
    }

    @Override
    @Transactional
    public void enableEmail(String token) {
        SecurityToken securityTokenDB = findByToken(token);
        User userDB = userRepository
                .findById(securityTokenDB.getUserId())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        userDB.setEnabled(true);
        userRepository.save(userDB);
        securityTokenRepository.deleteById(securityTokenDB.getId());
    }

    @Override
    @Transactional
    public CustomerResponse saveShipmentDetails(ShipmentDetailsRequest shipmentDetailsRequest, Jwt jwt) {
        User userDB = findByUsername(jwt.getSubject());
        userDB.getCustomer().setAddress(shipmentDetailsRequest.address());
        userDB.getCustomer().setCity(shipmentDetailsRequest.city());
        userDB.getCustomer().setPostalCode(shipmentDetailsRequest.postalCode());
        userDB.getCustomer().setPhone(shipmentDetailsRequest.phone());
        return save(userDB);
    }

    @Override
    @Transactional
    public CustomerResponse addFavorite(Long productId, Jwt jwt) {
        productClient.findProductById(productId, jwt);
        User userDB = findByUsername(jwt.getSubject());
        userDB.getCustomer().getFavoriteIds().add(productId);
        return save(userDB);
    }

    @Override
    @Transactional
    public CustomerResponse deleteFavorite(Long productId, Jwt jwt) {
        User userDB = findByUsername(jwt.getSubject());
        if (!userDB.getCustomer().getFavoriteIds().remove(productId))
            throw new NotFoundException("No tiene guardado ese favorito");
        return save(userDB);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse me(Jwt jwt) {
        User userDB = findByUsername(jwt.getSubject());
        return toCustomerResponse(userDB);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPassword resetPassword) {
        SecurityToken securityTokenDB = findByToken(resetPassword.code());
        User userDB = findByUsername(securityTokenDB.getUserId());
        userDB.setPassword(passwordEncoder.encode(resetPassword.password()));
        userRepository.save(userDB);
        securityTokenRepository.deleteById(securityTokenDB.getId());
        publisher.send(passwordChangedQueue,
                new EmailRequest(
                        userDB.getUsername(),
                        "Contraseña cambiada",
                        "Se ha actualizado la contraseña"));
    }

    @Override
    public String getCustomerName(String username) {
        User userDB = findByUsername(username);
        return userDB.getCustomer().getName();
    }

    private String createSecureRandom() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private CustomerResponse save(User user) {
        return toCustomerResponse(userRepository.save(user));
    }

    private User findByUsername(String username) {
        return this.userRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

    private SecurityToken findByToken(String token) {
        return securityTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new NotFoundException("Security Token no encontrado"));
    }

    private CustomerResponse toCustomerResponse(User user) {
        List<String> roles = user.getAuthorities().stream()
                .map(authority -> authority.getRole().name())
                .toList();
        return CustomerResponse.builder()
                .name(user.getCustomer().getName())
                .lastName(user.getCustomer().getLastName())
                .username(user.getUsername())
                .roles(roles)
                .address(user.getCustomer().getAddress())
                .city(user.getCustomer().getCity())
                .postalCode(user.getCustomer().getPostalCode())
                .phone(user.getCustomer().getPhone())
                .photo(user.getCustomer().getPhoto())
                .favoriteIds(user.getCustomer().getFavoriteIds())
                .build();
    }

}
