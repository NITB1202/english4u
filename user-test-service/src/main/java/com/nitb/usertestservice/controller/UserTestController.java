package com.nitb.usertestservice.controller;

import com.google.protobuf.Empty;
import com.nitb.usertestservice.grpc.*;
import com.nitb.usertestservice.service.ResultDetailService;
import com.nitb.usertestservice.service.ResultService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class UserTestController extends UserTestServiceGrpc.UserTestServiceImplBase {
    private final ResultService resultService;
    private final ResultDetailService resultDetailService;

    @Override
    public void createResult(CreateResultRequest request, StreamObserver<Empty> responseObserver) {

    }

    @Override
    public void getResultById(GetResultByIdRequest request, StreamObserver<ResultDetailsResponse> responseObserver) {

    }

    @Override
    public void getResults(GetResultsRequest request, StreamObserver<ResultsResponse> responseObserver) {

    }

    @Override
    public void getResultStatistics(GetResultStatisticsRequest request, StreamObserver<GetResultStatisticsResponse> responseObserver) {

    }

}
