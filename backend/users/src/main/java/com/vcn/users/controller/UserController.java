package com.vcn.users.controller;

import com.vcn.users.exception.UserAlreadyExistsException;
import com.vcn.users.model.*;
import com.vcn.users.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Registra un nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody RegisterRequest registerRequest, BindingResult bindingResult) throws UserAlreadyExistsException {
        if (bindingResult.hasErrors()) return validate(bindingResult);
        userService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Envía el código de recuperación al email para resetear la contraseña
    @PostMapping("/send-code")
    public ResponseEntity<?> sendCode(@Validated @RequestBody String email, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return validate(bindingResult);
        userService.sendCode(email);
        return ResponseEntity.ok().build();
    }

    // Resetea el password
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Validated @RequestBody ResetPassword resetPassword, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return validate(bindingResult);
        userService.resetPassword(resetPassword);
        // El motivo del Map es evitar crear una clase DTO para una respuesta de un solo campo.
        // Si devolvieras el String pelado, Spring respondería Cuenta activada correctamente como text/plain,
        // que es incómodo de consumir desde Angular: tendrías que configurar responseType: 'text'
        // en el HttpClient en lugar del parseo JSON por defecto que devuelve de esta manera.
        return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente"));
    }

    // Valida el email cuando se crea una nueva cuenta
    @GetMapping("/enable")
    public ResponseEntity<?> enableEmail(@RequestParam String token) {
        userService.enableEmail(token);
        // El motivo del Map es evitar crear una clase DTO para una respuesta de un solo campo.
        // Si devolvieras el String pelado, Spring respondería Cuenta activada correctamente como text/plain,
        // que es incómodo de consumir desde Angular: tendrías que configurar responseType: 'text'
        // en el HttpClient en lugar del parseo JSON por defecto que devuelve de esta manera.
        return ResponseEntity.ok(Map.of("message", "Cuenta activada correctamente"));
    }

    @PatchMapping("/shipmentDetails")
    public ResponseEntity<?> saveShipmentDetails(@Validated @RequestBody RegisterRequest registerRequest, @AuthenticationPrincipal Jwt jwt, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return validate(bindingResult);
        return ResponseEntity.ok(userService.saveShipmentDetails(registerRequest, jwt));
    }

    /**
     * Añade el producto a la lista de favoritos mediante el productId que recibe
     */
    @PostMapping("/favorite/{productId}")
    public ResponseEntity<?> addFavorite(@PathVariable Long productId, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(userService.addFavorite(productId, jwt));
    }

    /**
     * Elimina el producto a la lista de favoritos mediante el productId que recibe
     */
    @DeleteMapping("/favorite/{productId}")
    public ResponseEntity<?> deleteFavorite(@PathVariable Long productId, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(userService.deleteFavorite(productId, jwt));
    }

    @PostMapping("/webclient/customer")
    public String getCustomerName(@AuthenticationPrincipal Jwt jwt) {
        return userService.getCustomerName(jwt.getSubject());
    }

    @GetMapping("/me")
    public ResponseEntity<?> findByUsername(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(userService.me(jwt));
    }

    protected ResponseEntity<?> validate(BindingResult bindingResult) {
        Map<String, Object> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(),
                        "Field: ".concat(error.getField()).concat(" : ").concat(Objects.requireNonNull(error.getDefaultMessage()))));
        return ResponseEntity.badRequest().body(errors);
    }
}
