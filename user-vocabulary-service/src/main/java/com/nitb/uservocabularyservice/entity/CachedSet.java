package com.nitb.uservocabularyservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_cached_sets", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId", "setId"})
})
public class CachedSet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "set_id", nullable = false)
    private UUID setId;

    @Column(name = "learned_words")
    private Integer learnedWords;

    @Column(name = "last_access", nullable = false)
    private LocalDateTime lastAccess;
}
