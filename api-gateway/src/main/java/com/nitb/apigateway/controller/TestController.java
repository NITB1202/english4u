package com.nitb.apigateway.controller;

import com.nitb.apigateway.dto.Test.Test.request.CreateTestRequestDto;
import com.nitb.apigateway.dto.Test.Test.request.UpdateTestInfoRequestDto;
import com.nitb.apigateway.dto.Test.Test.request.UpdateTestRequestDto;
import com.nitb.apigateway.dto.Test.Test.response.*;
import com.nitb.apigateway.exception.ErrorResponse;
import com.nitb.apigateway.service.Test.TestService;
import com.nitb.common.enums.GroupBy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tests")
public class TestController {
    private final TestService testService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SYSTEM_ADMIN')")
    @Operation(summary = "Create a new test.")
    @ApiResponse(responseCode = "200", description = "Create successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<CreateTestResponseDto>> createTest(@AuthenticationPrincipal UUID userId,
                                                                  @Valid @RequestBody CreateTestRequestDto request) {
        return testService.createTest(userId, request).map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get details of a test by id.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    @ApiResponse(responseCode = "404", description = "Not found.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<TestDetailResponseDto>> getTestById(@PathVariable UUID id) {
        return testService.getTestById(id).map(ResponseEntity::ok);
    }

    @GetMapping("/all")
    @Operation(summary = "Get paginated list of tests.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<TestsPaginationResponseDto>> getTests(@Positive(message = "Page must be positive") @RequestParam int page,
                                                                     @RequestParam(defaultValue = "10") int size) {
        return testService.getTests(page, size).map(ResponseEntity::ok);
    }

    @GetMapping("/deleted")
    @PreAuthorize("hasAnyRole('ADMIN', 'SYSTEM_ADMIN')")
    @Operation(summary = "Get paginated list of deleted tests.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<TestsPaginationResponseDto>> getDeletedTests(@Positive(message = "Page must be positive") @RequestParam int page,
                                                                            @RequestParam(defaultValue = "10") int size) {
        return testService.getDeletedTests(page, size).map(ResponseEntity::ok);
    }

    @GetMapping("/search")
    @Operation(summary = "Search for a test by name.")
    @ApiResponse(responseCode = "200", description = "Search successfully.")
    public Mono<ResponseEntity<TestsPaginationResponseDto>> searchTestByName(@RequestParam String keyword,
                                                                             @Positive(message = "Page must be positive") @RequestParam int page,
                                                                             @RequestParam(defaultValue = "10") int size) {
        return testService.searchTestByName(keyword, page, size).map(ResponseEntity::ok);
    }

    @GetMapping("/search/deleted")
    @PreAuthorize("hasAnyRole('ADMIN', 'SYSTEM_ADMIN')")
    @Operation(summary = "Search for a deleted test by name.")
    @ApiResponse(responseCode = "200", description = "Search successfully.")
    public Mono<ResponseEntity<TestsPaginationResponseDto>> searchDeletedTestByName(@RequestParam String keyword,
                                                                                    @Positive(message = "Page must be positive") @RequestParam int page,
                                                                                    @RequestParam(defaultValue = "10") int size) {
        return testService.searchDeletedTestByName(keyword, page, size).map(ResponseEntity::ok);
    }

    @PatchMapping("/{id}/general")
    @PreAuthorize("hasAnyRole('ADMIN', 'SYSTEM_ADMIN')")
    @Operation(summary = "Update the name and topic of a test.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<UpdateTestResponseDto>> updateTestNameAndTopic(@AuthenticationPrincipal UUID userId,
                                                                              @PathVariable UUID id,
                                                                              @Valid @RequestBody UpdateTestInfoRequestDto request) {
        return testService.updateTestNameAndTopic(userId, id, request).map(ResponseEntity::ok);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SYSTEM_ADMIN')")
    @Operation(summary = "Update a test.")
    @ApiResponse(responseCode = "200", description = "Update successfully.")
    @ApiResponse(responseCode = "400", description = "Invalid request body.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<UpdateTestResponseDto>> updateTest(@AuthenticationPrincipal UUID userId,
                                                                  @PathVariable UUID id,
                                                                  @Valid @RequestBody UpdateTestRequestDto request) {
        return testService.updateTest(userId, id, request).map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SYSTEM_ADMIN')")
    @Operation(summary = "Delete a test.")
    @ApiResponse(responseCode = "200", description = "Delete successfully.")
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<DeleteTestResponseDto>> deleteTest(@AuthenticationPrincipal UUID userId,
                                                                  @PathVariable UUID id) {
        return testService.deleteTest(userId, id).map(ResponseEntity::ok);
    }

    @PatchMapping("/restore/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SYSTEM_ADMIN')")
    @Operation(summary = "Restore a deleted test.")
    @ApiResponse(responseCode = "200", description = "Restore successfully.")
    @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public Mono<ResponseEntity<DeleteTestResponseDto>> restoreTest(@AuthenticationPrincipal UUID userId,
                                                                   @PathVariable UUID id) {
        return testService.restoreTest(userId, id).map(ResponseEntity::ok);
    }

    @GetMapping("/statistic")
    @PreAuthorize("hasAnyRole('ADMIN', 'SYSTEM_ADMIN')")
    @Operation(summary = "Get published test statistics.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<GetPublishedTestStatisticsResponseDto>> getPublishedTestStatistics(@AuthenticationPrincipal UUID userId,
                                                                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                                                                                                  @RequestParam GroupBy groupBy) {
        return testService.getPublishedTestStatistics(userId, from, to, groupBy).map(ResponseEntity::ok);
    }

    @GetMapping("/template")
    @PreAuthorize("hasAnyRole('ADMIN', 'SYSTEM_ADMIN')")
    @Operation(summary = "Download test template as an Excel file")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<byte[]>> downloadTestTemplate() {
        return testService.generateTestTemplate()
                .map(response -> ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + response.getFileName())
                        .body(response.getFileContent().toByteArray())
                )
                .subscribeOn(Schedulers.boundedElastic());
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'SYSTEM_ADMIN')")
    @Operation(summary = "Upload test Excel file and create test")
    @ApiResponse(responseCode = "200", description = "Upload successfully.")
    public Mono<ResponseEntity<CreateTestResponseDto>> uploadTestTemplate(@AuthenticationPrincipal UUID userId,
                                                                          @RequestPart("file") FilePart file) {
        return testService.uploadTestTemplate(userId, file).map(ResponseEntity::ok);
    }
}
