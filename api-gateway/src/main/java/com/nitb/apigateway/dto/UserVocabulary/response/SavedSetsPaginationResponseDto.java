package com.nitb.apigateway.dto.UserVocabulary.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedSetsPaginationResponseDto {
    private List<SavedSetDetailResponseDto> sets;

    private Long totalItems;

    private Integer totalPages;
}
