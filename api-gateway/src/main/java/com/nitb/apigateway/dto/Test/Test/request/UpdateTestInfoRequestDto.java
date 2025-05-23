package com.nitb.apigateway.dto.Test.Test.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTestInfoRequestDto {
    private String name;

    private String topic;
}
