package com.vcn.chat.Service;

import com.vcn.chat.model.Participant;
import com.vcn.chat.model.ParticipantDTO;

import java.util.List;

public interface ParticipantService {

    List<ParticipantDTO> findAll();
    Participant findByUsername(String username);
    Participant save(Participant participant);
}
