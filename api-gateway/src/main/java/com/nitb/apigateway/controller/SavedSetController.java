package com.nitb.apigateway.controller;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.request.CreateSavedSetRequestDto;
import com.nitb.apigateway.dto.UserVocabulary.request.UpdateSavedSetRequestDto;
import com.nitb.apigateway.dto.UserVocabulary.response.SavedSetResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.response.SavedSetStateStatisticResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.response.SavedSetsPaginationResponseDto;
import com.nitb.apigateway.exception.ErrorResponse;
import com.nitb.apigateway.service.UserVocabulary.SavedSetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/saved-sets")
public class SavedSetController {
    private final SavedSetService savedSetService;

    @PostMapping
    @Operation(summary = "Save a vocabulary set.")
    @ApiResponse(responseCode = "200", description = "Save successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<SavedSetResponseDto>> createSavedSet(@RequestParam UUID userId,
                                                                    @Valid @RequestBody CreateSavedSetRequestDto request) {
        return savedSetService.createSavedSet(userId, request).map(ResponseEntity::ok);
    }

    @GetMapping
    @Operation(summary = "Get paginated sets saved by the user.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<SavedSetsPaginationResponseDto>> getSavedSets(@RequestParam UUID userId,
                                                                             @Positive(message = "Page must be positive") @RequestParam int page,
                                                                             @RequestParam(defaultValue = "10") int size) {
        return savedSetService.getSavedSets(userId, page, size).map(ResponseEntity::ok);
    }

    @GetMapping("/search")
    @Operation(summary = "Search for a saved set.")
    @ApiResponse(responseCode = "200", description = "Search successfully.")
    public Mono<ResponseEntity<SavedSetsPaginationResponseDto>> searchSavedSets(@RequestParam UUID userId,
                                                                                @RequestParam String keyword,
                                                                                @Positive(message = "Page must be positive") @RequestParam int page,
                                                                                @RequestParam(defaultValue = "10") int size){
        return savedSetService.searchSavedSets(userId, keyword, page, size).map(ResponseEntity::ok);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a saved set.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<SavedSetResponseDto>> updateSavedSet(@PathVariable UUID id,
                                                                    @Valid @RequestBody UpdateSavedSetRequestDto request) {
        return savedSetService.updateSavedSet(id, request).map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a saved set.")
    @ApiResponse(responseCode = "200", description = "Delete successfully.")
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> deleteSavedSet(@PathVariable UUID id) {
        return savedSetService.deleteSavedSet(id).map(ResponseEntity::ok);
    }

    @GetMapping("/state")
    @Operation(summary = "Get the statistics of the user's saved sets state.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<SavedSetStateStatisticResponseDto>> getSavedSetStateStatistic(@RequestParam UUID userId){
        return savedSetService.getSavedSetStateStatistic(userId).map(ResponseEntity::ok);
    }
}
