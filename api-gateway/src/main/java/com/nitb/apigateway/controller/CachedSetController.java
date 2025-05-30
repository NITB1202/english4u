package com.nitb.apigateway.controller;

import com.nitb.apigateway.dto.General.DataWithMessageResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.request.CreateCachedSetRequestDto;
import com.nitb.apigateway.dto.UserVocabulary.response.CachedSetSummaryResponseDto;
import com.nitb.apigateway.exception.ErrorResponse;
import com.nitb.apigateway.service.UserVocabulary.CachedSetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cached-sets")
public class CachedSetController {
    private final CachedSetService cachedSetService;

    @PostMapping
    @PreAuthorize("hasRole('LEARNER')")
    @Operation(summary = "Cache a vocabulary set.")
    @ApiResponse(responseCode = "200", description = "Cache successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<DataWithMessageResponseDto>> cachedSet(@AuthenticationPrincipal UUID userId,
                                                                      @Valid @RequestBody CreateCachedSetRequestDto request) {
        return cachedSetService.cacheSet(userId, request).map(ResponseEntity::ok);
    }

    @GetMapping
    @PreAuthorize("hasRole('LEARNER')")
    @Operation(summary = "Get all user's cached sets.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<List<CachedSetSummaryResponseDto>>> getAllCachedSets(@AuthenticationPrincipal UUID userId) {
        return cachedSetService.getAllCachedSets(userId).map(ResponseEntity::ok);
    }
}
