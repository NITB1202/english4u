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
@Table(name = "parts")
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "test_id", nullable = false)
    private UUID testId;

    @Column(name = "order", nullable = false)
    private Integer order;

    @Column(name = "content")
    private String content;

    @Column(name = "question_count", nullable = false)
    private Integer questionCount;
}
