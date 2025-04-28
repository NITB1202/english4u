package com.nitb.testservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id",nullable = false)
    private UUID id;

    @Column(name = "part_id", nullable = false)
    private UUID partId;

    @Column(name = "order", nullable = false)
    private Integer order;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "answers", nullable = false)
    private String answers;

    @Column(name = "correct_answer", nullable = false)
    private String correctAnswer;

    @Column(name = "explanation")
    private String explanation;
}
