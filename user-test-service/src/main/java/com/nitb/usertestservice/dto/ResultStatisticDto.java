package com.nitb.usertestservice.dto;

import lombok.*;

import java.time.Duration;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultStatisticDto {
    private String time;

    private Integer resultCount;

    private Duration timeSpent;

    private Float accuracy;
}
