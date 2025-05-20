package com.nitb.apigateway.controller;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.Test.request.Question.AddQuestionsToPartRequestDto;
import com.nitb.apigateway.dto.Test.request.Question.UpdateQuestionRequestDto;
import com.nitb.apigateway.dto.Test.response.Question.QuestionResponseDto;
import com.nitb.apigateway.exception.ErrorResponse;
import com.nitb.apigateway.service.Test.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping
    @Operation(summary = "Add questions to a specific part.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<List<QuestionResponseDto>>> addQuestionsToPart(@RequestParam UUID userId,
                                                                              @Valid @RequestBody AddQuestionsToPartRequestDto request) {
        return questionService.addQuestionsToPart(userId, request).map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get details of the question by id.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<QuestionResponseDto>> getQuestionById(@PathVariable UUID id) {
        return questionService.getQuestionById(id).map(ResponseEntity::ok);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a question.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<QuestionResponseDto>> updateQuestion(@PathVariable UUID id,
                                                                    @RequestParam UUID userId,
                                                                    @Valid @RequestBody UpdateQuestionRequestDto request) {
        return questionService.updateQuestion(id, userId, request).map(ResponseEntity::ok);
    }

    @PatchMapping("/swap")
    @Operation(summary = "Swap the positions of two questions within the same part.")
    @ApiResponse(responseCode = "200", description = "Swap successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> swapQuestionsPosition(UUID userId, UUID question1Id, UUID question2Id) {
        return questionService.swapQuestionsPosition(userId, question1Id, question2Id).map(ResponseEntity::ok);
    }
}
