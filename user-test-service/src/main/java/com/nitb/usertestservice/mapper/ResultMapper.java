package com.nitb.usertestservice.mapper;

import com.nitb.usertestservice.dto.ResultStatisticDto;
import com.nitb.usertestservice.entity.Result;
import com.nitb.usertestservice.entity.ResultDetail;
import com.nitb.usertestservice.grpc.*;
import com.nitb.usertestservice.util.DurationUtils;
import org.springframework.data.domain.Page;

import java.util.List;

public class ResultMapper {
    private ResultMapper() {}

    public static ResultDetailsResponse toResultDetailsResponse(Result result, List<ResultDetail> details) {
        String timeSpent = DurationUtils.parseToString(result.getTimeSpent());
        List<ResultDetailResponse> detailResponses = details.stream()
                .map(ResultDetailMapper::toResultDetailResponse)
                .toList();

        return ResultDetailsResponse.newBuilder()
                .setId(result.getId().toString())
                .setTestId(result.getTestId().toString())
                .setSubmitDate(result.getSubmitDate().toString())
                .setTimeSpent(timeSpent)
                .setScore(result.getScore())
                .setAccuracy(result.getAccuracy())
                .addAllDetails(detailResponses)
                .build();
    }

    public static ResultSummaryResponse toResultSummaryResponse(Result result) {
        return ResultSummaryResponse.newBuilder()
                .setId(result.getId().toString())
                .setTestId(result.getTestId().toString())
                .setSubmitDate(result.getSubmitDate().toString())
                .setTimeSpent(DurationUtils.parseToString(result.getTimeSpent()))
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
                .setTimeSpentSeconds(statistic.getTimeSpentSeconds())
                .setAccuracy(statistic.getAccuracy())
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
