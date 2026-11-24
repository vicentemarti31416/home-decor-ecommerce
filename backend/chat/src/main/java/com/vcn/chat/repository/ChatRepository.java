package com.vcn.chat.repository;

import com.vcn.chat.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("""
            SELECT c
            FROM Chat c
            JOIN c.participants p1
            JOIN c.participants p2
            WHERE c.chatRoomType = 'PRIVATE'
            AND p1.username = :sender
            AND p2.username = :receiver
            """)
    Optional<Chat> findPrivateChat(@Param("sender") String sender, @Param("receiver") String receiver);
}
