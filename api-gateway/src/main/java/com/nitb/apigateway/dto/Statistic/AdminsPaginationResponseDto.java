package com.nitb.apigateway.dto.Statistic;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminsPaginationResponseDto {
    private List<AdminStatisticResponseDto> admins;

    private long totalItems;

    private int totalPages;
}
