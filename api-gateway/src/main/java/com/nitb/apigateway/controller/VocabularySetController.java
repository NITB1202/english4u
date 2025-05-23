package com.nitb.apigateway.controller;

import com.nitb.apigateway.dto.Vocabulary.request.CreateVocabularySetRequestDto;
import com.nitb.apigateway.dto.Vocabulary.request.UpdateVocabularySetRequestDto;
import com.nitb.apigateway.dto.Vocabulary.response.*;
import com.nitb.apigateway.exception.ErrorResponse;
import com.nitb.apigateway.service.Vocabulary.VocabularySetService;
import com.nitb.common.enums.GroupBy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sets")
public class VocabularySetController {
    private final VocabularySetService vocabularySetService;

    @PostMapping
    @Operation(summary = "Create a new vocabulary set.")
    @ApiResponse(responseCode = "200", description = "Create successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<CreateVocabularySetResponseDto>> createVocabularySet(@RequestParam UUID userId,
                                                                                       @Valid @RequestBody CreateVocabularySetRequestDto request) {
        return vocabularySetService.createVocabularySet(userId, request).map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get details of a vocabulary set by id.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<VocabularySetDetailResponseDto>> getVocabularySetById(@PathVariable UUID id) {
        return vocabularySetService.getVocabularySetById(id).map(ResponseEntity::ok);
    }

    @GetMapping
    @Operation(summary = "Get paginated vocabulary sets.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<VocabularySetsPaginationResponseDto>> getVocabularySets(@Positive(message = "Page must be positive") @RequestParam int page,
                                                                                       @RequestParam(defaultValue = "10") int size){
        return vocabularySetService.getVocabularySets(page, size).map(ResponseEntity::ok);
    }

    @GetMapping("/deleted")
    @Operation(summary = "Get paginated list of deleted vocabulary sets.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<VocabularySetsPaginationResponseDto>> getDeletedVocabularySets(@Positive(message = "Page must be positive") @RequestParam int page,
                                                                                              @RequestParam(defaultValue = "10") int size){
        return vocabularySetService.getDeletedVocabularySets(page, size).map(ResponseEntity::ok);
    }

    @GetMapping("/search")
    @Operation(summary = "Search for a vocabulary set by name.")
    @ApiResponse(responseCode = "200", description = "Search successfully.")
    public Mono<ResponseEntity<VocabularySetsPaginationResponseDto>> searchVocabularySetByName(@RequestParam String keyword,
                                                                                               @Positive(message = "Page must be positive") @RequestParam int page,
                                                                                               @RequestParam(defaultValue = "10") int size){
        return vocabularySetService.searchVocabularySetByName(keyword, page, size).map(ResponseEntity::ok);
    }

    @GetMapping("/search/deleted")
    @Operation(summary = "Search for a deleted vocabulary set by name.")
    @ApiResponse(responseCode = "200", description = "Search successfully.")
    public Mono<ResponseEntity<VocabularySetsPaginationResponseDto>> searchDeletedVocabularySetsByName(@RequestParam String keyword,
                                                                                                       @Positive(message = "Page must be positive") @RequestParam int page,
                                                                                                       @RequestParam(defaultValue = "10") int size) {
        return vocabularySetService.searchDeletedVocabularySetByName(keyword, page, size).map(ResponseEntity::ok);
    }

    @PatchMapping("/{id}/name")
    @Operation(summary = "Update a vocabulary set's name.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<UpdateVocabularySetResponseDto>> updateVocabularySetName(@PathVariable UUID id,
                                                                                        @RequestParam UUID userId,
                                                                                        @RequestParam String name) {
        return vocabularySetService.updateVocabularySetName(id, userId, name).map(ResponseEntity::ok);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a vocabulary set by versioning it.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<UpdateVocabularySetResponseDto>> updateVocabularySet(@PathVariable UUID id,
                                                                                    @RequestParam UUID userId,
                                                                                    @Valid @RequestBody UpdateVocabularySetRequestDto request) {
        return vocabularySetService.updateVocabularySet(id, userId, request).map(ResponseEntity::ok);
    }

    @DeleteMapping
    @Operation(summary = "Soft delete a vocabulary set.")
    @ApiResponse(responseCode = "200", description = "Delete successfully.")
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<DeleteVocabularySetResponseDto>> deleteVocabularySet(@RequestParam UUID id,
                                                                                    @RequestParam UUID userId) {
        return vocabularySetService.deleteVocabularySet(id, userId).map(ResponseEntity::ok);
    }

    @PatchMapping("/restore")
    @Operation(summary = "Restore a deleted vocabulary set.")
    @ApiResponse(responseCode = "200", description = "Delete successfully.")
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<DeleteVocabularySetResponseDto>> restoreVocabularySet(@RequestParam UUID id,
                                                                                     @RequestParam UUID userId) {
        return vocabularySetService.restoreVocabularySet(id, userId).map(ResponseEntity::ok);
    }

    @GetMapping("/statistic")
    @Operation(summary = "Get statistical summary of vocabulary sets.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<VocabularySetStatisticsResponseDto>> getVocabularySetStatistics(@RequestParam UUID userId,
                                                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                                                                                               @RequestParam GroupBy groupBy) {
        return vocabularySetService.getVocabularySetStatistics(userId, from, to, groupBy).map(ResponseEntity::ok);
    }
}
