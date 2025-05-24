package com.nitb.usertestservice.service.impl;

import com.nitb.usertestservice.dto.ResultStatisticDto;
import com.nitb.usertestservice.entity.Result;
import com.nitb.usertestservice.grpc.CreateResultRequest;
import com.nitb.usertestservice.grpc.GetResultByIdRequest;
import com.nitb.usertestservice.grpc.GetResultStatisticsRequest;
import com.nitb.usertestservice.grpc.GetResultsRequest;
import com.nitb.usertestservice.repository.ResultRepository;
import com.nitb.usertestservice.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {
    private final ResultRepository resultRepository;

    @Override
    public Result createResult(CreateResultRequest request) {
        return null;
    }

    @Override
    public Result getResultById(GetResultByIdRequest request) {
        return null;
    }

    @Override
    public Page<Result> getResults(GetResultsRequest request) {
        return null;
    }

    @Override
    public List<ResultStatisticDto> getResultStatistics(GetResultStatisticsRequest request) {
        return List.of();
    }
}
