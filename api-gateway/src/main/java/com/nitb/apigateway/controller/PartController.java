package com.nitb.apigateway.controller;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.Test.request.Part.CreatePartRequestDto;
import com.nitb.apigateway.dto.Test.request.Part.UpdatePartRequestDto;
import com.nitb.apigateway.dto.Test.response.Part.PartDetailResponseDto;
import com.nitb.apigateway.dto.Test.response.Part.PartResponseDto;
import com.nitb.apigateway.dto.Test.response.Part.PartSummaryResponseDto;
import com.nitb.apigateway.exception.ErrorResponse;
import com.nitb.apigateway.service.Test.PartService;
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
@RequestMapping("/api/parts")
public class PartController {
    private final PartService partService;

    @PostMapping("/{testId}")
    @Operation(summary = "Add a part to the test.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<PartResponseDto>> addPartToTest(@RequestParam UUID userId,
                                                               @PathVariable UUID testId,
                                                               @Valid @RequestBody CreatePartRequestDto request) {
        return partService.addPartToTest(userId, testId, request).map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get details of a part by id.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<PartDetailResponseDto>> getPartDetailById(@PathVariable UUID id) {
        return partService.getPartDetailById(id).map(ResponseEntity::ok);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all parts summaries for a test.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<List<PartSummaryResponseDto>>> getAllPartsForTest(@RequestParam UUID testId) {
        return partService.getAllPartsForTest(testId).map(ResponseEntity::ok);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a part.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> updatePart(@PathVariable UUID id,
                                                              @RequestParam UUID userId,
                                                              @Valid @RequestBody UpdatePartRequestDto request) {
        return partService.updatePart(id, userId, request).map(ResponseEntity::ok);
    }

    @PatchMapping("/swap")
    @Operation(summary = "Swap the positions of two parts within the same test.")
    @ApiResponse(responseCode = "200", description = "Swap successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<ActionResponseDto>> swapPartsPosition(@RequestParam UUID userId,
                                                                     @RequestParam UUID part1Id,
                                                                     @RequestParam UUID part2Id) {
        return partService.swapPartsPosition(userId, part1Id, part2Id).map(ResponseEntity::ok);
    }
}
