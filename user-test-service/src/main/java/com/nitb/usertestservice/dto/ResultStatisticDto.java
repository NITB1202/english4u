package com.nitb.usertestservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultStatisticDto {
    private String time;

    private Long resultCount;

    private Long timeSpentSeconds;

    private Double accuracy;
}
