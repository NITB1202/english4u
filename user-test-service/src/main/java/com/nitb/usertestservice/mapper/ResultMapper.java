package com.nitb.usertestservice.mapper;

import com.nitb.usertestservice.dto.ResultStatisticDto;
import com.nitb.usertestservice.entity.Result;
import com.nitb.usertestservice.entity.ResultDetail;
import com.nitb.usertestservice.grpc.*;
import org.springframework.data.domain.Page;

import java.util.List;

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

    public static ResultStatisticResponse toResultStatisticResponse(ResultStatisticDto statistic) {
        return ResultStatisticResponse.newBuilder()
                .setTime(statistic.getTime())
                .setResultCount(statistic.getResultCount())
                .setAvgSecondsSpent(statistic.getAvgSecondsSpent())
                .setAvgAccuracy(statistic.getAvgAccuracy())
                .build();
    }

    public static GetResultStatisticsResponse toGetResultStatisticsResponse(List<ResultStatisticDto> statistics) {
        List<ResultStatisticResponse> responses = statistics.stream()
                .map(ResultMapper::toResultStatisticResponse)
                .toList();

        return GetResultStatisticsResponse.newBuilder()
                .addAllStatistics(responses)
                .build();
    }
}
