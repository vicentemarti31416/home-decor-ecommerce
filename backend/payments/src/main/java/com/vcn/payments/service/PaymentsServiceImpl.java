package com.vcn.payments.service;

import com.stripe.StripeClient;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import com.vcn.payments.model.IntentRequest;
import com.vcn.payments.model.IntentResponse;
import com.vcn.payments.model.StatusRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PaymentsServiceImpl implements PaymentsService {

    @Value("${stripe.webhook.secret}")
    private String secret;

    private final StripeClient stripeClient;
    private final RestClient restClient;

    public PaymentsServiceImpl(StripeClient stripeClient, RestClient restClient) {
        this.stripeClient = stripeClient;
        this.restClient = restClient;
    }

    @Override
    public IntentResponse intentPayment(IntentRequest paymentRequest) {
        PaymentIntent intent;
        try {
            intent = stripeClient.paymentIntents().create(
                    PaymentIntentCreateParams.builder()
                            .setAmount(paymentRequest.amount())
                            .setCurrency("eur")
                            .addPaymentMethodType("card")
                            .putMetadata("orderId", paymentRequest.metadata().get("orderId"))
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new IntentResponse(intent.getClientSecret());
    }

    @Override
    public void handleWebhook(String payload, String signature) throws SignatureVerificationException {
        Event event = Webhook.constructEvent(payload, signature, secret);
        switch (event.getType()) {
            case "payment_intent.succeeded" -> handlePaymentSucceeded(event);
            case "payment_intent.payment_failed" -> handlePaymentFailed(event);
            default -> System.out.println("Evento no manejado: " + event.getType());
        }
    }

    @Override
    public void handlePaymentSucceeded(Event event) {
        event.getDataObjectDeserializer()
                .getObject()
                .ifPresent(stripeObject -> {
                    PaymentIntent intent = (PaymentIntent) stripeObject;
                    String orderId = intent.getMetadata().get("orderId");
                    updateOrderStatus(new StatusRequest(orderId, "PAID"));
                });
    }

    @Override
    public void handlePaymentFailed(Event event) {
        event.getDataObjectDeserializer()
                .getObject()
                .ifPresent(stripeObject -> {
                    PaymentIntent intent = (PaymentIntent) stripeObject;
                    String orderId = intent.getMetadata().get("orderId");
                    updateOrderStatus(new StatusRequest(orderId, "CANCELLED"));
                });
    }

    @Override
    public void updateOrderStatus(StatusRequest statusRequest) {
        restClient
                .patch()
                .uri("http://localhost:8096/orders/restclient/update-status")
                .body(statusRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                            throw new RuntimeException("Pedido no encontrado");
                        }
                )
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                            throw new RuntimeException("Servidor no disponible");
                        }
                )
                .toBodilessEntity();
    }
}
