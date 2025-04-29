package com.nitb.testservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestStatisticDto {
    private String time;

    private Integer testCount;

    private Long completedUsers;
}
