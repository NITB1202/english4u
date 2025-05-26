package com.nitb.apigateway.service.UserTest;

import com.nitb.apigateway.dto.UserTest.request.SaveResultRequestDto;
import com.nitb.apigateway.dto.UserTest.response.ResultDetailsResponseDto;
import com.nitb.apigateway.dto.UserTest.response.ResultStatisticResponseDto;
import com.nitb.apigateway.dto.UserTest.response.ResultsResponseDto;
import com.nitb.apigateway.dto.UserTest.response.SavedResultResponseDto;
import com.nitb.common.enums.GroupBy;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class UserTestServiceImpl implements UserTestService {
    @Override
    public SavedResultResponseDto saveResult(UUID userId, SaveResultRequestDto request) {
        return null;
    }

    @Override
    public ResultDetailsResponseDto getResultById(UUID id) {
        return null;
    }

    @Override
    public ResultsResponseDto getResults(UUID userId, int page, int size) {
        return null;
    }

    @Override
    public List<ResultStatisticResponseDto> getResultStatistics(UUID userId, LocalDate from, LocalDate to, GroupBy groupBy) {
        return List.of();
    }
}
