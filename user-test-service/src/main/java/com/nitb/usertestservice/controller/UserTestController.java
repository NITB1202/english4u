package com.nitb.usertestservice.controller;

import com.google.protobuf.Empty;
import com.nitb.usertestservice.dto.ResultStatisticDto;
import com.nitb.usertestservice.entity.Result;
import com.nitb.usertestservice.entity.ResultDetail;
import com.nitb.usertestservice.grpc.*;
import com.nitb.usertestservice.mapper.ResultMapper;
import com.nitb.usertestservice.service.ResultDetailService;
import com.nitb.usertestservice.service.ResultService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class UserTestController extends UserTestServiceGrpc.UserTestServiceImplBase {
    private final ResultService resultService;
    private final ResultDetailService resultDetailService;

    @Override
    public void createResult(CreateResultRequest request, StreamObserver<Empty> responseObserver) {
        Result result = resultService.createResult(request);
        resultDetailService.createResultDetails(result.getId(), request.getDetailsList());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void getResultById(GetResultByIdRequest request, StreamObserver<ResultDetailsResponse> responseObserver) {
        Result result = resultService.getResultById(request);
        List<ResultDetail> details = resultDetailService.getAllResultDetailsForResult(result.getId());
        ResultDetailsResponse response = ResultMapper.toResultDetailsResponse(result, details);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getResults(GetResultsRequest request, StreamObserver<ResultsResponse> responseObserver) {
        Page<Result> results = resultService.getResults(request);
        ResultsResponse response = ResultMapper.toResultsResponse(results);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getResultStatistics(GetResultStatisticsRequest request, StreamObserver<GetResultStatisticsResponse> responseObserver) {
        List<ResultStatisticDto> statistics = resultService.getResultStatistics(request);
        GetResultStatisticsResponse response = ResultMapper.toGetResultStatisticsResponse(statistics);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
