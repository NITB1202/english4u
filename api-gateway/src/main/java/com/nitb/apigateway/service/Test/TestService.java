package com.nitb.apigateway.service.Test;

import com.nitb.apigateway.dto.Test.request.Test.CreateTestRequestDto;
import com.nitb.apigateway.dto.Test.request.Test.UpdateTestInfoRequestDto;
import com.nitb.apigateway.dto.Test.request.Test.UpdateTestRequestDto;
import com.nitb.apigateway.dto.Test.response.Test.*;
import com.nitb.common.enums.GroupBy;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

public interface TestService {
    Mono<CreateTestResponseDto> createTest(UUID userId, CreateTestRequestDto request);
    Mono<TestDetailResponseDto> getTestById(UUID id);
    Mono<TestsPaginationResponseDto> getTests(int page, int size);
    Mono<TestsPaginationResponseDto> getDeletedTests(int page, int size);
    Mono<TestsPaginationResponseDto> searchTestByName(String keyword, int page, int size);
    Mono<TestsPaginationResponseDto> searchDeletedTestByName(String keyword, int page, int size);
    Mono<UpdateTestResponseDto> updateTestNameAndTopic(UUID userId, UUID id, UpdateTestInfoRequestDto request);
    Mono<UpdateTestResponseDto> updateTest(UUID userId, UUID id, UpdateTestRequestDto request);
    Mono<DeleteTestResponseDto> deleteTest(UUID userId, UUID id);
    Mono<DeleteTestResponseDto> restoreTest(UUID userId, UUID id);
    Mono<GetPublishedTestStatisticsResponseDto> getPublishedTestStatistics(UUID userId, LocalDate from, LocalDate to, GroupBy groupBy);
}
