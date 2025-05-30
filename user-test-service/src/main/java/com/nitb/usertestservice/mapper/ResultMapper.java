package com.nitb.usertestservice.mapper;

import com.nitb.usertestservice.entity.Result;
import com.nitb.usertestservice.entity.ResultDetail;
import com.nitb.usertestservice.grpc.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public class ResultMapper {
    private ResultMapper() {}

    public static ResultDetailsResponse toResultDetailsResponse(Result result, List<ResultDetail> details) {
        List<ResultDetailResponse> detailResponses = details.stream()
                .map(ResultDetailMapper::toResultDetailResponse)
                .toList();

        return ResultDetailsResponse.newBuilder()
                .setId(result.getId().toString())
                .setTestId(result.getTestId().toString())
                .setSubmitTime(result.getSubmitTime().toString())
                .setSecondsSpent(result.getSecondsSpent())
                .setScore(result.getScore())
                .setAccuracy(result.getAccuracy())
                .addAllDetails(detailResponses)
                .build();
    }

    public static ResultSummaryResponse toResultSummaryResponse(Result result) {
        return ResultSummaryResponse.newBuilder()
                .setId(result.getId().toString())
                .setTestId(result.getTestId().toString())
                .setSubmitTime(result.getSubmitTime().toString())
                .setSecondsSpent(result.getSecondsSpent())
                .setScore(result.getScore())
                .build();
    }

    public static ResultsResponse toResultsResponse(Page<Result> results) {
        List<ResultSummaryResponse> summaries = results.getContent().stream()
                .map(ResultMapper::toResultSummaryResponse)
                .toList();

        return ResultsResponse.newBuilder()
                .addAllResults(summaries)
                .setTotalItems(results.getTotalElements())
                .setTotalPages(results.getTotalPages())
                .build();
    }

    public static ResultStatisticResponse toResultStatisticResponse(String time, long resultCount, long avgSecondsSpent, double avgAccuracy) {
        return ResultStatisticResponse.newBuilder()
                .setTime(time)
                .setResultCount(resultCount)
                .setAvgSecondsSpent(avgSecondsSpent)
                .setAvgAccuracy(avgAccuracy)
                .build();
    }

    public static GetResultStatisticsResponse toGetResultStatisticsResponse(List<ResultStatisticResponse> statistics) {
        return GetResultStatisticsResponse.newBuilder()
                .addAllStatistics(statistics)
                .build();
    }

    public static LearnerTestStatisticResponse toLearnerTestStatisticResponse(UUID userId, long totalTestsTaken, double avgScore) {
        return LearnerTestStatisticResponse.newBuilder()
                .setUserId(userId.toString())
                .setTotalTestsTaken(totalTestsTaken)
                .setAvgScore(avgScore)
                .build();
    }

    public static LearnerTestsStatisticsResponse toLearnerTestsStatisticsResponse(List<LearnerTestStatisticResponse> statistics) {
        return LearnerTestsStatisticsResponse.newBuilder()
                .addAllStatistics(statistics)
                .build();
    }
}
