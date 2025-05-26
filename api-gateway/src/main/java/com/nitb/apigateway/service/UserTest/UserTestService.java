package com.nitb.apigateway.service.UserTest;

import com.nitb.apigateway.dto.UserTest.request.SaveResultRequestDto;
import com.nitb.apigateway.dto.UserTest.response.ResultDetailsResponseDto;
import com.nitb.apigateway.dto.UserTest.response.ResultStatisticResponseDto;
import com.nitb.apigateway.dto.UserTest.response.ResultsResponseDto;
import com.nitb.apigateway.dto.UserTest.response.SavedResultResponseDto;
import com.nitb.common.enums.GroupBy;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface UserTestService {
    Mono<SavedResultResponseDto> saveResult(UUID userId, SaveResultRequestDto request);
    Mono<ResultDetailsResponseDto> getResultById(UUID id);
    Mono<ResultsResponseDto> getResults(UUID userId, int page, int size);
    Mono<List<ResultStatisticResponseDto>> getResultStatistics(UUID userId, LocalDate from, LocalDate to, GroupBy groupBy);
}
