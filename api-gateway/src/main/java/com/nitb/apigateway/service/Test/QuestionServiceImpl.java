package com.nitb.apigateway.service.Test;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.Test.request.Question.AddQuestionsToPartRequestDto;
import com.nitb.apigateway.dto.Test.request.Question.UpdateQuestionRequestDto;
import com.nitb.apigateway.dto.Test.response.Question.QuestionResponseDto;
import com.nitb.apigateway.grpc.TestServiceGrpcClient;
import com.nitb.apigateway.mapper.ActionMapper;
import com.nitb.apigateway.mapper.QuestionMapper;
import com.nitb.common.grpc.ActionResponse;
import com.nitb.testservice.grpc.QuestionResponse;
import com.nitb.testservice.grpc.QuestionsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final TestServiceGrpcClient testGrpc;

    @Override
    public Mono<List<QuestionResponseDto>> addQuestionsToPart(UUID userId, AddQuestionsToPartRequestDto request) {
        return Mono.fromCallable(()->{
            QuestionsResponse response = testGrpc.createQuestions(userId, request.getPartId(), request.getQuestions());
            return response.getResponseList().stream().map(QuestionMapper::toQuestionResponseDto).toList();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<QuestionResponseDto> getQuestionById(UUID id) {
        return Mono.fromCallable(()->{
            QuestionResponse response = testGrpc.getQuestionById(id);
            return QuestionMapper.toQuestionResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<QuestionResponseDto> updateQuestion(UUID id, UUID userId, UpdateQuestionRequestDto request) {
        return Mono.fromCallable(()->{
            QuestionResponse response =  testGrpc.updateQuestion(id, userId, request);
            return QuestionMapper.toQuestionResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> swapQuestionsPosition(UUID userId, UUID question1Id, UUID question2Id) {
        return Mono.fromCallable(()->{
            ActionResponse response = testGrpc.swapQuestionsPosition(userId, question1Id, question2Id);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
