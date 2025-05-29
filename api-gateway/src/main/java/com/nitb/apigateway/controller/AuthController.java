package com.nitb.apigateway.controller;

import com.nitb.apigateway.dto.Auth.Account.request.CreateAdminAccountRequestDto;
import com.nitb.apigateway.dto.Auth.Auth.request.*;
import com.nitb.apigateway.dto.Auth.Auth.response.LoginResponseDto;
import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.exception.ErrorResponse;
import com.nitb.apigateway.service.Auth.AuthService;
import com.nitb.common.enums.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/cred")
    @Operation(summary = "Login with credentials.")
    @ApiResponse(responseCode = "200", description = "Login successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<LoginResponseDto>> loginWithCredentials(@Valid @RequestBody LoginWithCredentialsRequestDto request) {
        return authService.loginWithCredentials(request).map(ResponseEntity::ok);
    }

    @PostMapping("/prov")
    @Operation(summary = "Login with OAuth provider (Google, Facebook).")
    @ApiResponse(responseCode = "200", description = "Login successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<LoginResponseDto>> loginWithProvider(@Valid @RequestBody LoginWithProviderRequestDto request) {
        return authService.loginWithProvider(request).map(ResponseEntity::ok);
    }

    @PostMapping("/validate")
    @Operation(summary = "Validate account information before registering.")
    @ApiResponse(responseCode = "200", description = "Validate successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> validateRegisterInfo(@Valid @RequestBody ValidateRegisterInfoRequestDto request) {
        return authService.validateRegisterInfo(request).map(ResponseEntity::ok);
    }

    @PostMapping("/register")
    @Operation(summary = "Register.")
    @ApiResponse(responseCode = "200", description = "Register successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> registerWithCredentials(@Valid @RequestBody RegisterWithCredentialsRequestDto request) {
        return authService.registerWithCredentials(request).map(ResponseEntity::ok);
    }

    @GetMapping("/reset")
    @Operation(summary = "Send a verification code to the email address to reset the password.")
    @ApiResponse(responseCode = "200", description = "Send successfully.")
    public Mono<ResponseEntity<ActionResponseDto>> sendResetPasswordCode(@RequestParam String email) {
        return authService.sendResetPasswordCode(email).map(ResponseEntity::ok);
    }

    @PostMapping("/reset")
    @Operation(summary = "Reset the password.")
    @ApiResponse(responseCode = "200", description = "Reset successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> resetPassword(@Valid @RequestBody ResetPasswordRequestDto request) {
        return authService.resetPassword(request).map(ResponseEntity::ok);
    }

    @PostMapping("/admin")
    @Operation(summary = "Create a new admin account.")
    @ApiResponse(responseCode = "200", description = "Create successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> createAdminAccount(@Valid @RequestBody CreateAdminAccountRequestDto request) {
        return authService.createAdminAccount(request).map(ResponseEntity::ok);
    }

    @PatchMapping("/role/{id}")
    @Operation(summary = "Update the user's role and notify them via email.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> updateRole(@PathVariable UUID id, @RequestParam UserRole role) {
        return authService.updateRole(id, role).map(ResponseEntity::ok);
    }
}
