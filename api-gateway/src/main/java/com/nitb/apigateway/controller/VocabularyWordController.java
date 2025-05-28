package com.nitb.apigateway.controller;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularyWordsPaginationResponseDto;
import com.nitb.apigateway.service.Vocabulary.VocabularyWordService;
import com.nitb.apigateway.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/words")
public class VocabularyWordController {
    private final VocabularyWordService vocabularyWordService;

    @GetMapping
    @Operation(summary = "Get paginated vocabulary words.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<VocabularyWordsPaginationResponseDto>> getVocabularyWords(@RequestParam UUID setId,
                                                                                         @Positive(message = "Page must be positive") @RequestParam int page,
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
                                                                                                 @Positive(message = "Page must be positive") @RequestParam int page,
                                                                                                 @RequestParam(defaultValue = "10") int size){
        return vocabularyWordService.searchVocabularyWordByWord(setId, keyword, page, size).map(ResponseEntity::ok);
    }

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload vocabulary word's image.")
    @ApiResponse(responseCode = "200", description = "Upload successfully.")
    public Mono<ResponseEntity<ActionResponseDto>> uploadVocabularyWordImage(@RequestParam UUID setId,
                                                                             @RequestParam UUID wordId,
                                                                             @RequestPart("file") FilePart file) {
        return vocabularyWordService.uploadVocabularyWordImage(setId, wordId, file).map(ResponseEntity::ok);
    }
}
