package com.nitb.apigateway.dto.Vocabulary.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyWordsPaginationResponseDto {
    private List<VocabularyWordResponseDto> words;

    private Integer totalItems;

    private Integer totalPages;
}
