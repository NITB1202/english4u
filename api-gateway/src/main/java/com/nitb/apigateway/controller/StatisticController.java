package com.nitb.apigateway.controller;

import com.nitb.apigateway.dto.Statistic.AdminsPaginationResponseDto;
import com.nitb.apigateway.dto.Statistic.LearnersPaginationResponseDto;
import com.nitb.apigateway.service.Statistic.StatisticService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statistic")
public class StatisticController {
    private final StatisticService statisticService;

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMIN', 'SYSTEM_ADMIN')")
    @Operation(summary = "Get admin statistics.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<AdminsPaginationResponseDto>> getAdminsStatistics(@Positive(message = "Page must be positive") @RequestParam int page,
                                                                                 @RequestParam(defaultValue = "10") int size) {
        return statisticService.getAdminsStatistics(page, size).map(ResponseEntity::ok);
    }

    @GetMapping("/admin/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'SYSTEM_ADMIN')")
    @Operation(summary = "Search for a admin statistic by email.")
    @ApiResponse(responseCode = "200", description = "Search successfully.")
    public Mono<ResponseEntity<AdminsPaginationResponseDto>> searchAdminStatisticByEmail(@RequestParam String keyword,
                                                                                         @Positive(message = "Page must be positive") @RequestParam int page,
                                                                                         @RequestParam(defaultValue = "10") int size) {
        return statisticService.searchAdminStatisticByEmail(keyword, page, size).map(ResponseEntity::ok);
    }

    @GetMapping("/learner")
    @PreAuthorize("hasAnyRole('ADMIN', 'SYSTEM_ADMIN')")
    @Operation(summary = "Get learner statistics.")
    @ApiResponse(responseCode = "200", description = "Get successfully.")
    public Mono<ResponseEntity<LearnersPaginationResponseDto>> getLearnersStatistics(@Positive(message = "Page must be positive") @RequestParam int page,
                                                                                     @RequestParam(defaultValue = "10") int size) {
        return statisticService.getLearnersStatistics(page, size).map(ResponseEntity::ok);
    }

    @GetMapping("/learner/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'SYSTEM_ADMIN')")
    @Operation(summary = "Search for a learner statistic by email.")
    @ApiResponse(responseCode = "200", description = "Search successfully.")
    public Mono<ResponseEntity<LearnersPaginationResponseDto>> searchLearnersStatisticByEmail(@RequestParam String keyword,
                                                                                              @Positive(message = "Page must be positive") @RequestParam int page,
                                                                                              @RequestParam(defaultValue = "10") int size){
        return statisticService.searchLearnerStatisticByEmail(keyword, page, size).map(ResponseEntity::ok);
    }
}
