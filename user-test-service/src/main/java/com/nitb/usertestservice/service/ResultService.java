package com.nitb.usertestservice.service;

import com.nitb.usertestservice.dto.ResultStatisticDto;
import com.nitb.usertestservice.entity.Result;
import com.nitb.usertestservice.grpc.CreateResultRequest;
import com.nitb.usertestservice.grpc.GetResultByIdRequest;
import com.nitb.usertestservice.grpc.GetResultStatisticsRequest;
import com.nitb.usertestservice.grpc.GetResultsRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ResultService {
    Result createResult(CreateResultRequest request);
    Result getResultById(GetResultByIdRequest request);
    Page<Result> getResults(GetResultsRequest request);
    List<ResultStatisticDto> getResultStatistics(GetResultStatisticsRequest request);
}
