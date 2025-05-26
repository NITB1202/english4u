package com.nitb.apigateway.service.UserTest;

import com.nitb.apigateway.dto.UserTest.request.CreateResultDetailRequestDto;
import com.nitb.apigateway.dto.UserTest.request.CreateResultRequestDto;
import com.nitb.apigateway.dto.UserTest.request.SaveResultRequestDto;
import com.nitb.apigateway.dto.UserTest.response.*;
import com.nitb.apigateway.grpc.TestServiceGrpcClient;
import com.nitb.apigateway.grpc.UserTestGrpcClient;
import com.nitb.apigateway.mapper.ResultMapper;
import com.nitb.common.enums.AnswerState;
import com.nitb.common.enums.GroupBy;
import com.nitb.testservice.grpc.QuestionAnswersResponse;
import com.nitb.testservice.grpc.QuestionPositionsResponse;
import com.nitb.usertestservice.grpc.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {
    private final UserTestGrpcClient userTestGrpc;
    private final TestServiceGrpcClient testGrpc;

    @Override
    public Mono<SavedResultResponseDto> saveResult(UUID userId, SaveResultRequestDto request) {
        return Mono.fromCallable(()-> {
            QuestionAnswersResponse testAnswers = testGrpc.getQuestionAnswers(request.getTestId());

            int correctAnswer = 0;
            List<CreateResultDetailRequestDto> details = new ArrayList<>();

            for (Map.Entry<String, String> questionAnswer : testAnswers.getAnswersMap().entrySet()) {
                UUID questionId = UUID.fromString(questionAnswer.getKey());
                String userAnswer = request.getUserAnswers().get(questionId);

                AnswerState state = AnswerState.EMPTY;

                if(userAnswer != null && !userAnswer.isEmpty()) {
                    String handledAnswer = userAnswer.trim().toLowerCase();

                    if(handledAnswer.equals(questionAnswer.getValue().toLowerCase())) {
                        state = AnswerState.CORRECT;
                        correctAnswer++;
                    }
                    else {
                        state = AnswerState.INCORRECT;
                    }
                }

                CreateResultDetailRequestDto detail = CreateResultDetailRequestDto.builder()
                        .questionId(questionId)
                        .userAnswer(userAnswer)
                        .state(state)
                        .build();

                details.add(detail);
            }

            float accuracy = (float) correctAnswer / details.size();

            CreateResultRequestDto result = CreateResultRequestDto.builder()
                    .userId(userId)
                    .testId(request.getTestId())
                    .secondsSpent(request.getSecondsSpent())
                    .score(correctAnswer)
                    .accuracy(Math.round(accuracy * 100) / 100f)
                    .build();

            CreateResultResponse response = userTestGrpc.createResult(result, details);

            return ResultMapper.toSavedResultResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ResultDetailsResponseDto> getResultById(UUID id) {
        return Mono.fromCallable(()->{
            ResultDetailsResponse result = userTestGrpc.getResultById(id);

            UUID testId = UUID.fromString(result.getTestId());
            String testName = testGrpc.getTestNameById(testId);
            QuestionPositionsResponse positions = testGrpc.getQuestionPositions(testId);

            return ResultMapper.toResultDetailsResponseDto(testName, positions.getPositionsMap(), result);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ResultsResponseDto> getResults(UUID userId, int page, int size) {
        return Mono.fromCallable(()->{
            ResultsResponse results = userTestGrpc.getResults(userId, page, size);

            List<ResultSummaryResponseDto> summaries = new ArrayList<>();

            for(ResultSummaryResponse summary : results.getResultsList()) {
                UUID testId = UUID.fromString(summary.getTestId());
                String testName = testGrpc.getTestNameById(testId);
                ResultSummaryResponseDto dto = ResultMapper.toResultSummaryResponseDto(testName, summary);
                summaries.add(dto);
            }

            return ResultMapper.toResultsResponseDto(summaries, results);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<List<ResultStatisticResponseDto>> getResultStatistics(UUID userId, LocalDate from, LocalDate to, GroupBy groupBy) {
        return Mono.fromCallable(()->{
            GetResultStatisticsResponse statistics = userTestGrpc.getResultStatistics(userId, from, to, groupBy);
            return statistics.getStatisticsList().stream()
                    .map(ResultMapper::toResultStatisticResponseDto)
                    .toList();
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
