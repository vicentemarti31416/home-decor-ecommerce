package com.vcn.mail.service;

import com.vcn.mail.exception.NotFoundException;
import com.vcn.mail.model.EmailFile;
import com.vcn.mail.model.OrderResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${email.sender}")
    private String emailUser;

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public Map<String, String> receiveRequestEmailWithFile(EmailFile emailFile) throws IOException {
        String fileName = emailFile.getFile().getOriginalFilename();
        if (fileName == null) throw new NotFoundException("File Not Found");
        Path path = Paths.get("src/mail/resources/files/" + fileName);
        Files.createDirectories(path.getParent());
        Files.copy(emailFile.getFile().getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        File file = path.toFile();
        sendEmailWithFile(emailFile.getRecipient(), emailFile.getSubject(), emailFile.getMessage(), file);
        return Map.of(
                "estado", "Enviado",
                "archivo", fileName);
    }

    @Override
    public void sendOrderConfirmation(String recipient, String subject, OrderResponse orderResponse) {

        try {
            Context context = new Context();
            context.setVariable("orderResponse", orderResponse);
            String htmlContent = templateEngine.process("order-confirmation", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(recipient);
            helper.setFrom(emailUser);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendEnableAccount(String recipient, String subject, String token) {

        try {
            Context context = new Context();
            context.setVariable("emailConfirmationToken", token);
            String htmlContent = templateEngine.process("email-confirmation", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(recipient);
            helper.setFrom(emailUser);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendResetPassword(String recipient, String subject, String message) {

        try {
            Context context = new Context();
            context.setVariable("message", message);
            String htmlContent = templateEngine.process("reset-password", context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(recipient);
            helper.setFrom(emailUser);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendPasswordChanged(String recipient, String subject, String message) {

        try {
            Context context = new Context();
            context.setVariable("message", message);
            String htmlContent = templateEngine.process("password-changed", context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(recipient);
            helper.setFrom(emailUser);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendEmail(String recipient, String subject, String message) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(recipient);
        mailMessage.setFrom(emailUser);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }


    @Override
    public void sendEmailWithFile(String recipient, String subject, String message, File file) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

            mimeMessageHelper.setTo(recipient);
            mimeMessageHelper.setFrom(emailUser);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(message);
            mimeMessageHelper.addAttachment(file.getName(), file);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
