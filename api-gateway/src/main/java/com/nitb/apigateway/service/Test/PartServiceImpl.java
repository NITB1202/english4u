package com.nitb.apigateway.service.Test;

import com.nitb.apigateway.dto.Test.response.Part.PartDetailResponseDto;
import com.nitb.apigateway.grpc.TestServiceGrpcClient;
import com.nitb.apigateway.mapper.PartMapper;
import com.nitb.testservice.grpc.PartsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PartServiceImpl implements PartService {
    private final TestServiceGrpcClient testGrpc;

    @Override
    public Mono<List<PartDetailResponseDto>> getAllPartsForTest(UUID testId) {
        return Mono.fromCallable(()->{
            PartsResponse response = testGrpc.getAllPartsForTest(testId);
            return response.getPartsList().stream().map(PartMapper::toPartSummaryResponseDto).toList();
        }).subscribeOn(Schedulers.boundedElastic());
    }

}
