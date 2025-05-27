package com.nitb.apigateway.dto.Vocabulary.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VocabularySetDetailResponseDto {
    private UUID id;

    private String createdByName;

    private String createdByAvatarUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private String name;

    private int version;

    private Integer wordCount;

    private String updatedByName;

    private String updatedByAvatarUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private Boolean isDeleted;
}
