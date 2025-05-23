package com.nitb.apigateway.dto.Test.request.Test;

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
