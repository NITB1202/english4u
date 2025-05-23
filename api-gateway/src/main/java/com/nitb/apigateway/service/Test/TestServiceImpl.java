package com.nitb.apigateway.service.Test;

import com.nitb.apigateway.dto.Test.Test.response.*;
import com.nitb.apigateway.dto.Test.Part.request.CreatePartRequestDto;
import com.nitb.apigateway.dto.Test.Test.request.CreateTestRequestDto;
import com.nitb.apigateway.dto.Test.Test.request.UpdateTestRequestDto;
import com.nitb.apigateway.grpc.TestServiceGrpcClient;
import com.nitb.apigateway.mapper.TestMapper;
import com.nitb.common.enums.GroupBy;
import com.nitb.testservice.grpc.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final TestServiceGrpcClient testGrpc;

    @Override
    public Mono<CreateTestResponseDto> createTest(UUID userId, CreateTestRequestDto request) {
        return Mono.fromCallable(()->{
            //Create test
            CreateTestResponse test = testGrpc.createTest(userId, request);
            UUID testId = UUID.fromString(test.getId());

            //Create parts
            for(CreatePartRequestDto part : request.getParts() ) {
                PartResponse savedPart = testGrpc.createPart(userId, testId, part);
                UUID savedPartId = UUID.fromString(savedPart.getId());

                //Create questions
                testGrpc.createQuestions(userId, savedPartId, part.getQuestions());
            }

            return TestMapper.toCreateTestResponseDto(test);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<TestDetailResponseDto> getTestById(UUID id) {
        return Mono.fromCallable(()->{
            TestDetailResponse response = testGrpc.getTestById(id);
            return TestMapper.toTestDetailResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<TestsPaginationResponseDto> getTests(int page, int size) {
        return Mono.fromCallable(()->{
            TestsPaginationResponse response = testGrpc.getTests(page, size);
            return TestMapper.toTestsPaginationResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<TestsPaginationResponseDto> getDeletedTests(int page, int size) {
        return Mono.fromCallable(()->{
            TestsPaginationResponse response = testGrpc.getDeletedTests(page, size);
            return TestMapper.toTestsPaginationResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<TestsPaginationResponseDto> searchTestByName(String keyword, int page, int size) {
        return Mono.fromCallable(()->{
            TestsPaginationResponse response = testGrpc.searchTestByName(keyword, page, size);
            return TestMapper.toTestsPaginationResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UpdateTestResponseDto> updateTest(UUID userId, UUID id, UpdateTestRequestDto request) {
        return Mono.fromCallable(()->{
            UpdateTestResponse response = testGrpc.updateTest(userId, id, request);
            return TestMapper.toUpdateTestResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<DeleteTestResponseDto> deleteTest(UUID userId, UUID id) {
        return Mono.fromCallable(()->{
            DeleteTestResponse response = testGrpc.deleteTest(userId, id);
            return TestMapper.toDeleteTestResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<DeleteTestResponseDto> restoreTest(UUID userId, UUID id) {
        return Mono.fromCallable(()->{
            DeleteTestResponse response = testGrpc.restoreTest(userId, id);
            return TestMapper.toDeleteTestResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<GetPublishedTestStatisticsResponseDto> getPublishedTestStatistics(UUID userId, LocalDate from, LocalDate to, GroupBy groupBy) {
        return Mono.fromCallable(()->{
            GetPublishedTestStatisticsResponse response = testGrpc.getPublishedTestStatistics(userId, from, to, groupBy);
            return TestMapper.toGetPublishedTestStatisticsResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
