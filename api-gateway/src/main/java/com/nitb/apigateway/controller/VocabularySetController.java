package com.nitb.apigateway.controller;

import com.nitb.apigateway.dto.Vocabulary.request.CreateVocabularySetRequestDto;
import com.nitb.apigateway.dto.Vocabulary.request.UpdateVocabularySetRequestDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularySetDetailResponseDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularySetResponseDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularySetsPaginationResponseDto;
import com.nitb.apigateway.exception.ErrorResponse;
import com.nitb.apigateway.service.Vocabulary.VocabularySetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
@RequestMapping("/api/sets")
public class VocabularySetController {
    private final VocabularySetService vocabularySetService;

    @PostMapping
    @Operation(summary = "Create a new vocabulary set.")
    @ApiResponse(responseCode = "200", description = "Create successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<VocabularySetDetailResponseDto>> createVocabularySet(@RequestParam UUID userId,
                                                                                    @Valid @RequestBody CreateVocabularySetRequestDto request) {
        return vocabularySetService.createVocabularySet(userId, request).map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a vocabulary set by id.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<VocabularySetResponseDto>> getVocabularySetById(@PathVariable UUID id) {
        return vocabularySetService.getVocabularySetById(id).map(ResponseEntity::ok);
    }

    @GetMapping
    @Operation(summary = "Get paginated vocabulary sets.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<VocabularySetsPaginationResponseDto>> getVocabularySets(@Positive @RequestParam int page,
                                                                                       @RequestParam(defaultValue = "10") int size){
        return vocabularySetService.getVocabularySets(page, size).map(ResponseEntity::ok);
    }

    @GetMapping("/search")
    @Operation(summary = "Search for a vocabulary set by name.")
    @ApiResponse(responseCode = "200", description = "Search successfully.")
    public Mono<ResponseEntity<VocabularySetsPaginationResponseDto>> searchVocabularySetByName(@RequestParam String keyword,
                                                                                               @Positive @RequestParam int page,
                                                                                               @RequestParam(defaultValue = "10") int size){
        return vocabularySetService.searchVocabularySetByName(keyword, page, size).map(ResponseEntity::ok);
    }

    @PatchMapping
    @Operation(summary = "Update a vocabulary set.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<VocabularySetResponseDto>> updateVocabularySet(@RequestParam UUID id,
                                                                              @RequestParam UUID userId,
                                                                              @Valid @RequestBody UpdateVocabularySetRequestDto request) {
        return vocabularySetService.updateVocabularySet(id, userId, request).map(ResponseEntity::ok);
    }

    @DeleteMapping
    @Operation(summary = "Soft delete a vocabulary set.")
    @ApiResponse(responseCode = "200", description = "Delete successfully.")
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<VocabularySetResponseDto>> deleteVocabularySet(@RequestParam UUID id,
                                                                              @RequestParam UUID userId) {
        return vocabularySetService.deleteVocabularySet(id, userId).map(ResponseEntity::ok);
    }
}
