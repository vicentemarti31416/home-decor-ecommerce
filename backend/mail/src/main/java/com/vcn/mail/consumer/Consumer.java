package com.vcn.mail.consumer;

import com.vcn.mail.model.EmailRequest;
import com.vcn.mail.model.OrderConfirmationRequest;
import com.vcn.mail.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    private final EmailService emailService;

    public Consumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = {"${rabbitmq.queue.orders.order_confirmation}"})
    public void rendOrderConfirmation(@Payload OrderConfirmationRequest orderConfirmationRequest) {
        emailService.sendOrderConfirmation(
                orderConfirmationRequest.recipient(),
                orderConfirmationRequest.subject(),
                orderConfirmationRequest.orderResponse()
        );
    }

    @RabbitListener(queues = {"${rabbitmq.queue.users-enable_account}"})
    public void sendEnableAccount(@Payload EmailRequest emailRequest) {
        emailService.sendEnableAccount(
                emailRequest.recipient(),
                emailRequest.subject(),
                emailRequest.message()
        );
    }

    @RabbitListener(queues = {"${rabbitmq.queue.users-reset_password}"})
    public void sendResetPassword(@Payload EmailRequest emailRequest) {
        emailService.sendResetPassword(
                emailRequest.recipient(),
                emailRequest.subject(),
                emailRequest.message()
        );
    }

    @RabbitListener(queues = {"${rabbitmq.queue.users-password_changed}"})
    public void sendPasswordChanged(@Payload EmailRequest emailRequest) {
        emailService.sendPasswordChanged(
                emailRequest.recipient(),
                emailRequest.subject(),
                emailRequest.message()
        );
    }
}
