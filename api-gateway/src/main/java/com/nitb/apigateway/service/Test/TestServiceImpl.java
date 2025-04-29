package com.nitb.apigateway.service.Test;

import com.nitb.apigateway.dto.Test.request.Test.CreateTestRequestDto;
import com.nitb.apigateway.dto.Test.request.Test.UpdateTestRequestDto;
import com.nitb.apigateway.dto.Test.response.Test.*;
import com.nitb.common.enums.GroupBy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    @Override
    public CreateTestResponseDto createTest(UUID userId, CreateTestRequestDto request) {
        return null;
    }

    @Override
    public TestDetailResponseDto getTestById(UUID id) {
        return null;
    }

    @Override
    public TestPaginationResponseDto getTests(int page, int size) {
        return null;
    }

    @Override
    public TestPaginationResponseDto getDeletedTests(int page, int size) {
        return null;
    }

    @Override
    public TestPaginationResponseDto searchForTestByName(String keyword, int page, int size) {
        return null;
    }

    @Override
    public UpdateTestResponseDto updateTest(UUID userId, UUID id, UpdateTestRequestDto request) {
        return null;
    }

    @Override
    public DeleteTestResponseDto deleteTest(UUID userId, UUID id) {
        return null;
    }

    @Override
    public DeleteTestResponseDto restoreTest(UUID userId, UUID id) {
        return null;
    }

    @Override
    public GetPublishedTestStatisticsResponseDto getPublishedTestStatistics(UUID userId, LocalDate from, LocalDate to, GroupBy groupBy) {
        return null;
    }
}
