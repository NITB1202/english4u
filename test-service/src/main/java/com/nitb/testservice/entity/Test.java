package com.nitb.testservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tests")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "minutes", nullable = false)
    private Integer minutes;

    @Column(name = "topic")
    private String topic;

    @Column(name = "part_count", nullable = false)
    private Integer partCount;

    @Column(name = "completed_users", nullable = false)
    private Long completedUsers;

    @Column(name = "updated_by", nullable = false)
    private UUID updatedBy;

    @Column(name = "update_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
