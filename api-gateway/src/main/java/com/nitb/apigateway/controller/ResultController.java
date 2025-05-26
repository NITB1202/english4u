package com.nitb.apigateway.controller;

import com.nitb.apigateway.dto.UserTest.request.SaveResultRequestDto;
import com.nitb.apigateway.dto.UserTest.response.ResultDetailsResponseDto;
import com.nitb.apigateway.dto.UserTest.response.ResultStatisticResponseDto;
import com.nitb.apigateway.dto.UserTest.response.ResultsResponseDto;
import com.nitb.apigateway.dto.UserTest.response.SavedResultResponseDto;
import com.nitb.apigateway.exception.ErrorResponse;
import com.nitb.apigateway.service.UserTest.ResultService;
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
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/results")
public class ResultController {
    private final ResultService resultService;

    @PostMapping
    @Operation(summary = "Save a result.")
    @ApiResponse(responseCode = "200", description = "Save successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<SavedResultResponseDto>> saveResult(@RequestParam UUID userId,
                                                                   @Valid @RequestBody SaveResultRequestDto request) {
        return resultService.saveResult(userId, request).map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get details of a result by id.")
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ResultDetailsResponseDto>> getResultById(@PathVariable UUID id) {
        return resultService.getResultById(id).map(ResponseEntity::ok);
    }

    @GetMapping
    @Operation(summary = "Get a paginated list of user's results.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResultsResponseDto> getResults(@RequestParam UUID userId,
                                               @Positive(message = "Page must be positive") @RequestParam int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return resultService.getResults(userId, page, size);
    }

    @GetMapping("/statistic")
    @Operation(summary = "Get result statistics.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<List<ResultStatisticResponseDto>> getResultStatistics(@RequestParam UUID userId,
                                                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                                                                      @RequestParam GroupBy groupBy) {
        return resultService.getResultStatistics(userId, from, to, groupBy);
    }
}
