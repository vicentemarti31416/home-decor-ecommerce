package com.vcn.payments.service;

import com.stripe.exception.SignatureVerificationException;
import com.vcn.payments.model.IntentRequest;
import com.vcn.payments.model.IntentResponse;

public interface PaymentsService {

    IntentResponse intentPayment(IntentRequest paymentRequest);
    void handleWebhook(String payload, String signature) throws SignatureVerificationException;
}
