package com.nitb.apigateway.controller;

import com.nitb.apigateway.dto.Test.response.Question.QuestionResponseDto;
import com.nitb.apigateway.exception.ErrorResponse;
import com.nitb.apigateway.service.Test.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/{id}")
    @Operation(summary = "Get details of the question by id.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<QuestionResponseDto>> getQuestionById(@PathVariable UUID id) {
        return questionService.getQuestionById(id).map(ResponseEntity::ok);
    }
}
