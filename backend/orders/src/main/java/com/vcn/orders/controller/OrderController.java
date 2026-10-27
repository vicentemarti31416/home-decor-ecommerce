package com.vcn.orders.controller;

import com.vcn.orders.model.*;
import com.vcn.orders.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<?> findAllByUsername(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(orderService.findAllByUsername(jwt.getSubject()));
    }

    /**
     * Sirve para obtener todas las Order de un Customer con sus Product, la Photo principal y los datos principales
     * Se usa para el listado de orders de la vista
     */
    @GetMapping("/order-response")
    public ResponseEntity<?> findAllByUsernameAndIds(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(orderService.findAllByUsernameAndIds(jwt));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    /**
     * Aquí se obtiene la Order mediante el status
     * Si no existe Order se crea una nueva con el status CART
     * El frontend la solicita nada más arrancar la aplicación
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<?> findByStatusAndUsername(@PathVariable String status, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(orderService.findByStatusAndUsername(OrderStatus.valueOf(status), jwt.getSubject()));
    }

    @PostMapping("/cart")
    public ResponseEntity<?> saveCart(@Validated @RequestBody Order order, @AuthenticationPrincipal Jwt jwt, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return validate(bindingResult);
        return ResponseEntity.ok(orderService.save(order));
    }

    /**
     * Añade los items seleccionados en la vista a la Order con status CART
     * También comprueba el stock y el precio actualizado del Product
     */
    @PutMapping("/update-cart")
    public ResponseEntity<?> updateOrder(@Validated @RequestBody Order order, @AuthenticationPrincipal Jwt jwt, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return validate(bindingResult);
        return ResponseEntity.ok(orderService.updateOrder(order, jwt));
    }

    /**
     * Se encarga de realizar el intent desde el cart.
     * También comprueba el stock y precio de los items.
     * Actualiza el status del order en la base de datos.
     * */
    @PostMapping("/payment")
    public ResponseEntity<?> paymentIntent(@Validated @RequestBody Order order, @AuthenticationPrincipal Jwt jwt, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return validate(bindingResult);
        return ResponseEntity.ok(orderService.paymentIntentHelper(order, jwt));
    }

    /**
     * Se encarga de actualizar el status de una Order después de hacer el pago
     */
    @PatchMapping("/restclient/update-status")
    public ResponseEntity<?> updateOrderStatus(@Validated @RequestBody StatusRequest statusRequest, @AuthenticationPrincipal Jwt jwt, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return validate(bindingResult);
        return ResponseEntity.ok(orderService.updateOrderStatus(statusRequest, jwt));
    }

    /**
     * Elimina una Order por su id, primero comprueba si existe dicha order
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        orderService.findById(id);
        orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    protected ResponseEntity<?> validate(BindingResult bindingResult) {
        Map<String, Object> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(),
                        "Field: ".concat(error.getField()).concat(" : ").concat(Objects.requireNonNull(error.getDefaultMessage()))));
        return ResponseEntity.badRequest().body(errors);
    }
}
