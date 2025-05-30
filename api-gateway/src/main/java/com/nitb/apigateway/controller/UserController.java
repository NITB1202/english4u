package com.nitb.apigateway.controller;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.User.request.UpdateUserRequestDto;
import com.nitb.apigateway.dto.User.response.UpdateUserResponseDto;
import com.nitb.apigateway.dto.User.response.UserLockedResponseDto;
import com.nitb.apigateway.dto.User.response.UserResponseDto;
import com.nitb.apigateway.exception.ErrorResponse;
import com.nitb.apigateway.service.User.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get the logged-in user's basic information.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<UserResponseDto>> getUserById(@AuthenticationPrincipal UUID userId) {
        return userService.getUserById(userId).map(ResponseEntity::ok);
    }

    @PatchMapping
    @Operation(summary = "Update user's profile.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<UpdateUserResponseDto>> updateUser(@AuthenticationPrincipal UUID userId,
                                                                  @Valid @RequestBody UpdateUserRequestDto request) {
        return userService.updateUser(userId, request).map(ResponseEntity::ok);
    }

    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload user's avatar.")
    @ApiResponse(responseCode = "200", description = "Upload successfully.")
    public Mono<ResponseEntity<ActionResponseDto>> uploadUserAvatar(@AuthenticationPrincipal UUID userId,
                                                                    @RequestPart("file") FilePart file) {
        return userService.uploadUserAvatar(userId, file).map(ResponseEntity::ok);
    }

    @PatchMapping("/lock")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @Operation(summary = "Lock/unlock a user")
    @ApiResponse(responseCode = "200", description = "Lock/unlock successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<UserLockedResponseDto>> setUserLocked(@RequestParam UUID id,
                                                                     @RequestParam boolean isLocked) {
        return userService.setUserLocked(id, isLocked).map(ResponseEntity::ok);
    }
}
