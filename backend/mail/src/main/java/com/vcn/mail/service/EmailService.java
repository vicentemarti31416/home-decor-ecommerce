package com.vcn.mail.service;

import com.vcn.mail.model.EmailFile;
import com.vcn.mail.model.OrderResponse;

import java.io.IOException;
import java.util.Map;

public interface EmailService {

    Map<String, String> receiveRequestEmailWithFile(EmailFile emailFile) throws IOException;
    void sendEmail(String recipient, String subject, String message);
    void sendOrderConfirmation(String recipient, String subject, OrderResponse orderResponse);
    void sendEnableAccount(String recipient, String subject, String token);
    void sendResetPassword(String recipient, String subject, String message);
    void sendPasswordChanged(String recipient, String subject, String message);
}
