package com.nitb.usertestservice.service;

import com.nitb.usertestservice.entity.Result;
import com.nitb.usertestservice.grpc.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ResultService {
    Result createResult(CreateResultRequest request);
    Result getResultById(GetResultByIdRequest request);
    Page<Result> getResults(GetResultsRequest request);
    List<ResultStatisticResponse> getResultStatistics(GetResultStatisticsRequest request);
    List<LearnerTestStatisticResponse> getLearnerTestStatistics(GetLearnerTestStatisticsRequest request);
}
