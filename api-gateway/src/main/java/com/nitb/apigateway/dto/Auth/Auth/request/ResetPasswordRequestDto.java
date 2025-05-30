package com.nitb.apigateway.dto.Auth.Auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetPasswordRequestDto {
    @NotEmpty(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotEmpty(message = "New password is required.")
    private String newPassword;

    @NotEmpty(message = "Verification code is required.")
    private String verificationCode;
}
