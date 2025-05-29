package com.nitb.apigateway.dto.Auth.Auth.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OAuthUserInfo {
    private String id;

    private String name;

    private String email;

    private String picture;
}
