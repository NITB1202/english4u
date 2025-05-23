package com.nitb.apigateway.controller;

import com.nitb.apigateway.dto.Test.response.Part.PartSummaryResponseDto;
import com.nitb.apigateway.exception.ErrorResponse;
import com.nitb.apigateway.service.Test.PartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/parts")
public class PartController {
    private final PartService partService;

    @GetMapping("/all")
    @Operation(summary = "Get all parts for a test.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<List<PartSummaryResponseDto>>> getAllPartsForTest(@RequestParam UUID testId) {
        return partService.getAllPartsForTest(testId).map(ResponseEntity::ok);
    }
}
