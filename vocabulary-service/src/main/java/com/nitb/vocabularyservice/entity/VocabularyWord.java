package com.nitb.vocabularyservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "vocabulary_words")
public class VocabularyWord {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "set_id", nullable = false)
    private UUID setId;

    @Column(name = "position", nullable = false)
    private Integer position;

    @Column(name = "word", nullable = false)
    private String word;

    @Column(name = "pronunciation", nullable = false)
    private String pronunciation;

    @Column(name = "ex", nullable = false)
    private String example;

    @Column(name = "trans", nullable = false)
    private String translation;

    @Column(name = "image_url")
    private String imageUrl;
}
