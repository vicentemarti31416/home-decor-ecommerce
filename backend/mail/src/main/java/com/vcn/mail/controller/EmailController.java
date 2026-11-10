package com.vcn.mail.controller;

import com.vcn.mail.model.Email;
import com.vcn.mail.model.EmailFile;
import com.vcn.mail.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/mail")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/sendMessage")
    public ResponseEntity<?> receiveRequestEmail(@RequestBody Email email){
        emailService.sendEmail(email.getRecipient(), email.getSubject(), email.getMessage());
        return ResponseEntity.ok(Map.of("estado", "Enviado"));
    }


    @PostMapping("/sendMessageFile")
    public ResponseEntity<?> receiveRequestEmailWithFile(@ModelAttribute EmailFile emailFile) throws IOException {
        return ResponseEntity.ok(emailService.receiveRequestEmailWithFile(emailFile));
    }
}
