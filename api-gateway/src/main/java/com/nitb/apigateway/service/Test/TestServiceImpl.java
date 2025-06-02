package com.nitb.apigateway.service.Test;

import com.nitb.apigateway.dto.Test.Test.request.UpdateTestInfoRequestDto;
import com.nitb.apigateway.dto.Test.Test.response.*;
import com.nitb.apigateway.dto.Test.Test.request.CreateTestRequestDto;
import com.nitb.apigateway.dto.Test.Test.request.UpdateTestRequestDto;
import com.nitb.apigateway.grpc.TestServiceGrpcClient;
import com.nitb.apigateway.grpc.UserServiceGrpcClient;
import com.nitb.apigateway.mapper.TestMapper;
import com.nitb.apigateway.util.FileUtils;
import com.nitb.common.enums.GroupBy;
import com.nitb.common.exceptions.BusinessException;
import com.nitb.testservice.grpc.*;
import com.nitb.userservice.grpc.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final TestServiceGrpcClient testGrpc;
    private final UserServiceGrpcClient userGrpc;

    @Override
    public Mono<CreateTestResponseDto> createTest(UUID userId, CreateTestRequestDto request) {
        return Mono.fromCallable(()->{
            //Check user permission
            userGrpc.checkCanPerformAction(userId);

            //Create test
            CreateTestResponse test = testGrpc.createTest(userId, request);
            UUID testId = UUID.fromString(test.getId());

            //Create parts
            testGrpc.createParts(userId, testId, request.getParts());

            return TestMapper.toCreateTestResponseDto(test);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<TestDetailResponseDto> getTestById(UUID id) {
        return Mono.fromCallable(()->{
            TestDetailResponse response = testGrpc.getTestById(id);

            UUID createdById = UUID.fromString(response.getCreatedBy());
            UUID updatedById = UUID.fromString(response.getUpdatedBy());

            UserResponse createdBy = userGrpc.getUserById(createdById);
            UserResponse updatedBy = userGrpc.getUserById(updatedById);

            return TestMapper.toTestDetailResponseDto(response, createdBy, updatedBy);
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
    public Mono<TestsPaginationResponseDto> searchDeletedTestByName(String keyword, int page, int size) {
        return Mono.fromCallable(()->{
            TestsPaginationResponse response = testGrpc.searchDeletedTestByName(keyword, page, size);
            return TestMapper.toTestsPaginationResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UpdateTestResponseDto> updateTestNameAndTopic(UUID userId, UUID id, UpdateTestInfoRequestDto request) {
        return Mono.fromCallable(()->{
            userGrpc.checkCanPerformAction(userId);
            UpdateTestResponse response = testGrpc.updateTestNameAndTopic(id, userId, request);
            return TestMapper.toUpdateTestResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UpdateTestResponseDto> updateTest(UUID userId, UUID id, UpdateTestRequestDto request) {
        return Mono.fromCallable(()->{
            //Check user permission
            userGrpc.checkCanPerformAction(userId);

            //Validate update request
            testGrpc.validateUpdateTest(id);

            //Soft-delete the current version
            TestDetailResponse oldTest = testGrpc.getTestById(id);
            testGrpc.deleteTest(id, userId);

            //Create a new version
            int minute = request.getMinutes() != null ? request.getMinutes() : oldTest.getMinutes();
            CreateTestRequestDto testDto = CreateTestRequestDto.builder()
                    .name(oldTest.getName())
                    .topic(oldTest.getTopic())
                    .minutes(minute)
                    .parts(request.getParts())
                    .build();

            CreateTestResponse newTest = testGrpc.createTest(userId, testDto);
            UUID newId = UUID.fromString(newTest.getId());
            testGrpc.createParts(userId, newId, request.getParts());

            UpdateTestResponse response = testGrpc.updateTest(id, newId);
            return TestMapper.toUpdateTestResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<DeleteTestResponseDto> deleteTest(UUID userId, UUID id) {
        return Mono.fromCallable(()->{
            userGrpc.checkCanPerformAction(userId);
            DeleteTestResponse response = testGrpc.deleteTest(id, userId);
            return TestMapper.toDeleteTestResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<DeleteTestResponseDto> restoreTest(UUID userId, UUID id) {
        return Mono.fromCallable(()->{
            userGrpc.checkCanPerformAction(userId);
            DeleteTestResponse response = testGrpc.restoreTest(id, userId);
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

    @Override
    public Mono<TestTemplateResponse> generateTestTemplate() {
        return Mono.fromCallable(testGrpc::generateTestTemplate).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<CreateTestResponseDto> uploadTestTemplate(UUID userId, FilePart file) {
        //Check user permission
        userGrpc.checkCanPerformAction(userId);

        // Check file extension or MIME type
        if (!FileUtils.isExcelFile(file)) {
            throw new BusinessException("Invalid file format. Only .xlsx files are supported.");
        }

        return DataBufferUtils.join(file.content())
                .flatMap(buffer -> {
                    byte[] bytes = new byte[buffer.readableByteCount()];
                    buffer.read(bytes);
                    DataBufferUtils.release(buffer);

                    return Mono.fromCallable(() -> {
                                CreateTestResponse response = testGrpc.uploadTestTemplate(userId, bytes);
                                return TestMapper.toCreateTestResponseDto(response);
                            })
                            .subscribeOn(Schedulers.boundedElastic());
                });
    }

}
