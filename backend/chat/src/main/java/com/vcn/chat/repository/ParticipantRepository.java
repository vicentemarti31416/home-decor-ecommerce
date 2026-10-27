package com.vcn.chat.repository;

import com.vcn.chat.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, String> {

    public Optional<Participant> findByUsername(String username);
}
