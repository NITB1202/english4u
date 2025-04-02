package com.nitb.apigateway.dto.Vocabulary.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteVocabularyWordsRequestDto {
    @NotNull(message = "Set id is required.")
    private UUID setId;

    @NotNull(message = "Word ids are required.")
    private List<UUID> ids;
}
