package com.vcn.chat.Service;

import com.vcn.chat.exception.NotFoundException;
import com.vcn.chat.model.Participant;
import com.vcn.chat.model.ParticipantDTO;
import com.vcn.chat.repository.ParticipantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantRepository participantRepository;

    public ParticipantServiceImpl(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipantDTO> findAll() {
        List<Participant> participants = participantRepository.findAll();
        return listParticipantToListParticipantDTO(participants);
    }

    @Override
    @Transactional(readOnly = true)
    public Participant findByUsername(String username) {
        return participantRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Contacto no encontrado"));
    }

    @Override
    @Transactional
    public Participant save(Participant participant) {
        return participantRepository.save(participant);
    }

    public ParticipantDTO participantToParticipantDTO(Participant participant) {
        return new ParticipantDTO(
                participant.getId(),
                participant.getUsername(),
                participant.getPhoto()
        );
    }

    public List<ParticipantDTO> listParticipantToListParticipantDTO(List<Participant> participants) {
        return participants.stream().map(this::participantToParticipantDTO).toList();
    }
}
