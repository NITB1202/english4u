package com.nitb.apigateway.service.Test;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.Test.request.Part.CreatePartRequestDto;
import com.nitb.apigateway.dto.Test.request.Part.UpdatePartRequestDto;
import com.nitb.apigateway.dto.Test.response.Part.PartDetailResponseDto;
import com.nitb.apigateway.dto.Test.response.Part.PartResponseDto;
import com.nitb.apigateway.dto.Test.response.Part.PartSummaryResponseDto;
import com.nitb.apigateway.grpc.TestServiceGrpcClient;
import com.nitb.apigateway.mapper.ActionMapper;
import com.nitb.apigateway.mapper.PartMapper;
import com.nitb.common.grpc.ActionResponse;
import com.nitb.testservice.grpc.GetAllPartsForTestResponse;
import com.nitb.testservice.grpc.PartResponse;
import com.nitb.testservice.grpc.QuestionSummaryResponse;
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
    public Mono<PartResponseDto> addPartToTest(UUID userId, UUID testId, CreatePartRequestDto request) {
        return Mono.fromCallable(()->{
            PartResponse part = testGrpc.createPart(userId, testId, request);
            UUID partId = UUID.fromString(part.getId());

            testGrpc.createQuestions(userId, partId, request.getQuestions());

            int questionCount = request.getQuestions().size();
            PartResponseDto response = PartMapper.toPartResponseDto(part);
            response.setQuestionCount(questionCount);

            return response;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<PartDetailResponseDto> getPartDetailById(UUID id) {
        return Mono.fromCallable(()->{
          String content = testGrpc.getPartContentById(id).getContent();
          List<QuestionSummaryResponse> questions = testGrpc.getAllQuestionsForPart(id).getQuestionsList();
          return PartMapper.toPartDetailResponseDto(id, content, questions);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<List<PartSummaryResponseDto>> getAllPartsForTest(UUID testId) {
        return Mono.fromCallable(()->{
            GetAllPartsForTestResponse response = testGrpc.getAllPartsForTest(testId);
            return response.getPartsList().stream().map(PartMapper::toPartSummaryResponseDto).toList();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> updatePart(UUID id, UUID userId, UpdatePartRequestDto request) {
        return Mono.fromCallable(()->{
            ActionResponse response = testGrpc.updatePart(id, userId, request);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> swapPartsPosition(UUID userId, UUID part1Id, UUID part2Id) {
        return Mono.fromCallable(()->{
            ActionResponse response = testGrpc.swapPartsPosition(userId, part1Id, part2Id);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
