package com.nitb.apigateway.controller;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.User.request.UpdateUserRequestDto;
import com.nitb.apigateway.dto.User.response.UserResponseDto;
import com.nitb.apigateway.exception.ErrorResponse;
import com.nitb.apigateway.service.User.UserService;
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
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Get details of a user by id.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<UserResponseDto>> getUserById(@PathVariable UUID id) {
        return userService.getUserById(id).map(ResponseEntity::ok);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a user profile.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> updateUser(@PathVariable UUID id,
                                                              @Valid @RequestBody UpdateUserRequestDto request) {
        return userService.updateUser(id, request).map(ResponseEntity::ok);
    }

    @PatchMapping("/lock")
    @Operation(summary = "Lock/unlock a user")
    @ApiResponse(responseCode = "200", description = "Lock/unlock successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> setUserLocked(@RequestParam UUID id,
                                                                 @RequestParam boolean isLocked) {
        return userService.setUserLocked(id, isLocked).map(ResponseEntity::ok);
    }
}
