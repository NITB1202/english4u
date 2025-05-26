package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.UserTest.response.*;
import com.nitb.usertestservice.grpc.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ResultMapper {
    private ResultMapper() {}

    public static SavedResultResponseDto toSavedResultResponseDto(CreateResultResponse result) {
        return SavedResultResponseDto.builder()
                .id(UUID.fromString(result.getId()))
                .build();
    }

    public static ResultDetailsResponseDto toResultDetailsResponseDto(String testName, Map<String, Integer> questionPositions, ResultDetailsResponse result) {
        int correctAnswer = 0;
        int incorrectAnswer = 0;
        int emptyAnswer = 0;

        List<ResultDetailResponseDto> details = new ArrayList<>();

        for(ResultDetailResponse detail : result.getDetailsList()) {
            switch (detail.getState()) {
                case CORRECT: correctAnswer++; break;
                case INCORRECT: incorrectAnswer++; break;
                case EMPTY: emptyAnswer++; break;
            }

            Integer position = questionPositions.get(detail.getQuestionId());
            ResultDetailResponseDto dto = ResultDetailMapper.toResultDetailResponseDto(position, detail);

            details.add(dto);
        }

        return ResultDetailsResponseDto.builder()
                .id(UUID.fromString(result.getId()))
                .testName(testName)
                .correctAnswers(correctAnswer)
                .incorrectAnswers(incorrectAnswer)
                .emptyAnswers(emptyAnswer)
                .score(result.getScore())
                .accuracy(result.getAccuracy())
                .secondsSpent(result.getSecondsSpent())
                .details(details)
                .build();
    }

    public static ResultSummaryResponseDto toResultSummaryResponseDto(String testName, ResultSummaryResponse result) {
        return ResultSummaryResponseDto.builder()
                .id(UUID.fromString(result.getId()))
                .testName(testName)
                .submitDate(LocalDate.parse(result.getSubmitDate()))
                .score(result.getScore())
                .secondsSpent(result.getSecondsSpent())
                .build();
    }

    public static ResultsResponseDto toResultsResponseDto(List<ResultSummaryResponseDto> summaries, ResultsResponse results) {
        return ResultsResponseDto.builder()
                .results(summaries)
                .totalItems(results.getTotalItems())
                .totalPages(results.getTotalPages())
                .build();
    }

    public static ResultStatisticResponseDto toResultStatisticResponseDto(ResultStatisticResponse statistic) {
        return ResultStatisticResponseDto.builder()
                .time(statistic.getTime())
                .resultCount(statistic.getResultCount())
                .avgSecondsSpent(statistic.getAvgSecondsSpent())
                .avgAccuracy(statistic.getAvgAccuracy())
                .build();
    }
}
