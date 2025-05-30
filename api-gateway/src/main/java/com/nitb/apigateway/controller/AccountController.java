package com.nitb.apigateway.controller;

import com.nitb.apigateway.dto.Auth.Account.request.CreateAdminAccountRequestDto;
import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.exception.ErrorResponse;
import com.nitb.apigateway.service.Auth.AccountService;
import com.nitb.common.enums.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/admin")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @Operation(summary = "Create a new admin account.")
    @ApiResponse(responseCode = "200", description = "Create successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> createAdminAccount(@Valid @RequestBody CreateAdminAccountRequestDto request) {
        return accountService.createAdminAccount(request).map(ResponseEntity::ok);
    }

    @PatchMapping("/role/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @Operation(summary = "Update the user's role and notify them via email.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> updateRole(@PathVariable UUID id, @RequestParam UserRole role) {
        return accountService.updateRole(id, role).map(ResponseEntity::ok);
    }
}
