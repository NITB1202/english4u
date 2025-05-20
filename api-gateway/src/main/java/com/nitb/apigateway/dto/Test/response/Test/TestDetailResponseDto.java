package com.nitb.apigateway.dto.Test.response.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestDetailResponseDto {
    private UUID id;

    private UUID createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    private String name;

    private Integer minutes;

    private String topic;

    private Integer partCount;

    private Integer questionCount;

    private Long completedUsers;

    private UUID updatedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;

    private Boolean isDeleted;
}
