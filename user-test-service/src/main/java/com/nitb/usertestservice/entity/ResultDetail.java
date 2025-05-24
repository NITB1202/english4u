package com.nitb.usertestservice.entity;

import com.nitb.common.enums.AnswerState;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "result_details")
public class ResultDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "result_id", nullable = false)
    private UUID resultId;

    @Column(name = "question_id", nullable = false)
    private UUID questionId;

    @Column(name = "user_answer", nullable = false)
    private String userAnswer;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private AnswerState state;
}
