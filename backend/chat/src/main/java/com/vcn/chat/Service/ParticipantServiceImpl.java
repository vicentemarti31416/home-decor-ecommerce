package com.vcn.chat.Service;

import com.vcn.chat.exception.NotFoundException;
import com.vcn.chat.model.Participant;
import com.vcn.chat.model.ParticipantDTO;
import com.vcn.chat.repository.ParticipantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantRepository participantRepository;

    public ParticipantServiceImpl(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @Override
    public List<ParticipantDTO> findAll() {
        List<Participant> participants = participantRepository.findAll();
        return listParticipantToListParticipantDTO(participants);
    }

    @Override
    public Participant findByUsername(String username) {
        return participantRepository
                .findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Contacto no encontrado"));
    }

    @Override
    public Participant save(Participant participant) {
        return participantRepository.save(participant);
    }

    private ParticipantDTO participantToParticipantDTO(Participant participant) {
        return ParticipantDTO
                .builder()
                .id(participant.getId())
                .username(participant.getUsername())
                .photo(participant.getPhoto())
                .build();
    }

    private List<ParticipantDTO> listParticipantToListParticipantDTO(List<Participant> participants) {
        return participants.stream().map(this::participantToParticipantDTO).toList();
    }
}
