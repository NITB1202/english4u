package com.nitb.apigateway.dto.Test.response.Test;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestStatisticResponseDto {
    private String time;

    private Integer testCount;

    private Long completedUsers;
}
