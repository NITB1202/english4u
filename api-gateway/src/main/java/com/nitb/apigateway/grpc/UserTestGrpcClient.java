package com.nitb.apigateway.grpc;

import com.google.protobuf.Empty;
import com.nitb.apigateway.dto.UserTest.request.CreateResultDetailRequestDto;
import com.nitb.apigateway.dto.UserTest.request.CreateResultRequestDto;
import com.nitb.apigateway.mapper.ResultDetailMapper;
import com.nitb.common.enums.GroupBy;
import com.nitb.common.mappers.GroupByMapper;
import com.nitb.usertestservice.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class UserTestGrpcClient {
    @GrpcClient("user-test-service")
    private UserTestServiceGrpc.UserTestServiceBlockingStub blockingStub;

    public Empty createResult(CreateResultRequestDto result, List<CreateResultDetailRequestDto> details) {
        List<CreateResultDetailRequest> detailRequests = details.stream().map(ResultDetailMapper::toCreateResultDetailRequest).toList();

        CreateResultRequest request = CreateResultRequest.newBuilder()
                .setUserId(result.getUserId().toString())
                .setTestId(result.getTestId().toString())
                .setSecondsSpent(result.getSecondsSpent())
                .setScore(result.getScore())
                .setAccuracy(result.getAccuracy())
                .addAllDetails(detailRequests)
                .build();

        return blockingStub.createResult(request);
    }

    public ResultDetailsResponse getResultById(UUID id) {
        GetResultByIdRequest request = GetResultByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        return blockingStub.getResultById(request);
    }

    public ResultsResponse getResults(UUID userId, int page, int size) {
        GetResultsRequest request = GetResultsRequest.newBuilder()
                .setUserId(userId.toString())
                .setPage(page)
                .setSize(size)
                .build();

        return blockingStub.getResults(request);
    }

    public GetResultStatisticsResponse getResultStatistics(UUID userId, LocalDate from, LocalDate to, GroupBy groupBy) {
        GetResultStatisticsRequest request = GetResultStatisticsRequest.newBuilder()
                .setUserId(userId.toString())
                .setFrom(from.toString())
                .setTo(to.toString())
                .setGroupBy(GroupByMapper.toGrpcEnum(groupBy))
                .build();

        return blockingStub.getResultStatistics(request);
    }
}
