package com.nitb.apigateway.service.Test;

import com.nitb.apigateway.dto.Test.request.Test.CreateTestRequestDto;
import com.nitb.apigateway.dto.Test.request.Test.UpdateTestRequestDto;
import com.nitb.apigateway.dto.Test.response.Test.*;
import com.nitb.common.enums.GroupBy;

import java.time.LocalDate;
import java.util.UUID;

public interface TestService {
    CreateTestResponseDto createTest(UUID userId, CreateTestRequestDto request);
    TestDetailResponseDto getTestById(UUID id);
    TestPaginationResponseDto getTests(int page, int size);
    TestPaginationResponseDto getDeletedTests(int page, int size);
    TestPaginationResponseDto searchForTestByName(String keyword, int page, int size);
    UpdateTestResponseDto updateTest(UUID userId, UUID id, UpdateTestRequestDto request);
    DeleteTestResponseDto deleteTest(UUID userId, UUID id);
    DeleteTestResponseDto restoreTest(UUID userId, UUID id);
    GetPublishedTestStatisticsResponseDto getPublishedTestStatistics(UUID userId, LocalDate from, LocalDate to, GroupBy groupBy);
}
