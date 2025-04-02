package com.nitb.apigateway.controller;

import com.nitb.apigateway.dto.Action.ActionResponseDto;
import com.nitb.apigateway.dto.Vocabulary.request.AddVocabularyWordsRequestDto;
import com.nitb.apigateway.dto.Vocabulary.request.DeleteVocabularyWordsRequestDto;
import com.nitb.apigateway.dto.Vocabulary.request.UpdateVocabularyWordRequestDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularyWordResponseDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularyWordsPaginationResponseDto;
import com.nitb.apigateway.service.Vocabulary.VocabularyWordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/words")
public class VocabularyWordController {
    private final VocabularyWordService vocabularyWordService;

    @PostMapping
    @Operation(summary = "Add vocabulary words to a set.")
    @ApiResponse(responseCode = "200", description = "Add successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Flux<ResponseEntity<VocabularyWordResponseDto>> addWordsToSet(@RequestParam UUID userId,
                                                                         @Valid @RequestBody AddVocabularyWordsRequestDto request){
        return vocabularyWordService.addWordsToSet(userId, request).map(ResponseEntity::ok);
    }


    @GetMapping
    @Operation(summary = "Get paginated vocabulary words.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<VocabularyWordsPaginationResponseDto>> getVocabularyWords(@RequestParam UUID setId,
                                                                                         @Positive @RequestParam int page,
                                                                                         @RequestParam(defaultValue = "10") int size) {
        return vocabularyWordService.getVocabularyWords(setId, page, size).map(ResponseEntity::ok);
    }

    @GetMapping("/search")
    @Operation(summary = "Search for a vocabulary word.")
    @ApiResponse(responseCode = "200", description = "Search successfully.")
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<VocabularyWordsPaginationResponseDto>> searchVocabularyWordByWord(@RequestParam UUID setId,
                                                                                                 @RequestParam String keyword,
                                                                                                 @Positive @RequestParam int page,
                                                                                                 @RequestParam(defaultValue = "10") int size){
        return vocabularyWordService.searchVocabularyWordByWord(setId, keyword, page, size).map(ResponseEntity::ok);
    }

    @PutMapping
    @Operation(summary = "Update a vocabulary word.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<VocabularyWordResponseDto>> updateVocabularyWord(@RequestParam UUID id,
                                                                                @RequestParam UUID userId,
                                                                                @Valid @RequestBody UpdateVocabularyWordRequestDto request) {
        return vocabularyWordService.updateVocabularyWord(id, userId, request).map(ResponseEntity::ok);
    }

    @PutMapping("/switch")
    @Operation(summary = "Switch the positions of two words within the same set.")
    @ApiResponse(responseCode = "200", description = "Switch successfully.")
    public Mono<ResponseEntity<ActionResponseDto>> switchWordPosition(@RequestParam UUID userId,
                                                                      @RequestParam UUID word1,
                                                                      @RequestParam UUID word2) {
        return vocabularyWordService.switchWordPosition(userId, word1, word2).map(ResponseEntity::ok);
    }

    @DeleteMapping
    @Operation(summary = "Delete vocabulary words.")
    @ApiResponse(responseCode = "200", description = "Delete successfully.")
    public Mono<ResponseEntity<ActionResponseDto>> deleteVocabularyWords(@RequestParam UUID userId,
                                                                         @Valid @RequestBody DeleteVocabularyWordsRequestDto request) {
        return vocabularyWordService.deleteVocabularyWords(userId, request).map(ResponseEntity::ok);
    }
}
