package com.vcn.payments.service;

import com.stripe.StripeClient;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import com.vcn.payments.client.OrderClient;
import com.vcn.payments.model.IntentRequest;
import com.vcn.payments.model.IntentResponse;
import com.vcn.payments.model.StatusRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentsServiceImpl implements PaymentsService {

    @Value("${stripe.webhook.secret}")
    private String secret;

    private final StripeClient stripeClient;
    private final OrderClient orderClient;

    public PaymentsServiceImpl(StripeClient stripeClient, OrderClient orderClient) {
        this.stripeClient = stripeClient;
        this.orderClient = orderClient;
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
        Event event;
        try {
            event = Webhook.constructEvent(payload, signature, secret);
        } catch (Exception e) {
            System.out.println("ERROR WEBHOOK: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            throw e;
        }
        switch (event.getType()) {
            case "payment_intent.succeeded" -> handlePaymentSucceeded(event);
            case "payment_intent.payment_failed" -> handlePaymentFailed(event);
            default -> System.out.println("Evento no manejado: " + event.getType());
        }
    }

    private void handlePaymentSucceeded(Event event) {
        event.getDataObjectDeserializer()
                .getObject()
                .ifPresent(stripeObject -> {
                    PaymentIntent intent = (PaymentIntent) stripeObject;
                    String orderId = intent.getMetadata().get("orderId");
                    orderClient.updateOrderStatus(new StatusRequest(orderId, "PAID"));
                });
    }

    private void handlePaymentFailed(Event event) {
        event.getDataObjectDeserializer()
                .getObject()
                .ifPresent(stripeObject -> {
                    PaymentIntent intent = (PaymentIntent) stripeObject;
                    String orderId = intent.getMetadata().get("orderId");
                    orderClient.updateOrderStatus(new StatusRequest(orderId, "CANCELLED"));
                });
    }
}
