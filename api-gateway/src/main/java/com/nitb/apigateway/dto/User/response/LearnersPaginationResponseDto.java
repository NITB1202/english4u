package com.nitb.apigateway.dto.User.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LearnersPaginationResponseDto {
    private List<LearnerStatisticResponseDto> learners;

    private long totalItems;

    private int totalPages;
}
