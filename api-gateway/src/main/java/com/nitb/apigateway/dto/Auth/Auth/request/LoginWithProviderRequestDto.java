package com.nitb.apigateway.dto.Auth.Auth.request;

import com.nitb.common.enums.Provider;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginWithProviderRequestDto {
    @NotNull(message = "Provider is required.")
    private Provider provider;

    @NotEmpty(message = "Provider id is required.")
    private String providerId;
}
