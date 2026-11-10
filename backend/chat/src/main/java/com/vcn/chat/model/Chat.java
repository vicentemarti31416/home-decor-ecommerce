package com.vcn.chat.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;
    @Enumerated(EnumType.STRING)
    private ChatRoomType chatRoomType;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant created;
    @ManyToMany(mappedBy = "chats")
    @JsonManagedReference
    @Builder.Default
    private Set<Participant> participants = new HashSet<>();
}
