package com.vcn.chat.controller;

import com.vcn.chat.Service.ParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ParticipantController {

    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    // TODO: Añadir comprobación de seguridad antes de devolver la información
    @GetMapping("/participants")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(participantService.findAll());
    }
}
