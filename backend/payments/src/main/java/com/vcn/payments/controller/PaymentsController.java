package com.vcn.payments.controller;

import com.stripe.StripeClient;
import com.stripe.exception.SignatureVerificationException;
import com.vcn.payments.model.IntentRequest;
import com.vcn.payments.service.PaymentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/payments")
public class PaymentsController {

    public final PaymentsService paymentsService;
    public final StripeClient stripeClient;

    public PaymentsController(PaymentsService paymentsService, StripeClient stripeClient) {
        this.paymentsService = paymentsService;
        this.stripeClient = stripeClient;
    }

    @PostMapping("/restclient/intent-payment")
    public ResponseEntity<?> intentPayments(@Validated @RequestBody IntentRequest intentRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return validate(bindingResult);
        return ResponseEntity.ok(paymentsService.intentPayment(intentRequest));
    }

    /**
     * Se encarga de manejar el webhook para validar el pago realizado
     * */
    // Está mostrando el default después de realizar un pago correctamente
    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhook(@Validated @RequestBody String payload, @RequestHeader("Stripe-Signature") String signature) throws SignatureVerificationException {
        paymentsService.handleWebhook(payload, signature);
        return ResponseEntity.ok("ok");
    }

    protected ResponseEntity<?> validate(BindingResult bindingResult) {
        Map<String, Object> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(error ->
                errors.put(error.getField(),
                        "Field: ".concat(error.getField()).concat(" : ").concat(Objects.requireNonNull(error.getDefaultMessage()))));
        return ResponseEntity.badRequest().body(errors);
    }
}
