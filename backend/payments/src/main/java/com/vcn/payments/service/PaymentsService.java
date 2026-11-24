package com.vcn.payments.service;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.vcn.payments.model.IntentRequest;
import com.vcn.payments.model.IntentResponse;
import com.vcn.payments.model.StatusRequest;

public interface PaymentsService {

    IntentResponse intentPayment(IntentRequest paymentRequest);
    void handleWebhook(String payload, String signature) throws SignatureVerificationException;
    void handlePaymentSucceeded(Event event);
    void handlePaymentFailed(Event event);
    void updateOrderStatus(StatusRequest statusRequest);
}
