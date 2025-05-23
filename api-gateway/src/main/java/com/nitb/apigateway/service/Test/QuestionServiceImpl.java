package com.nitb.apigateway.service.Test;

import com.nitb.apigateway.dto.Test.response.Question.QuestionResponseDto;
import com.nitb.apigateway.grpc.TestServiceGrpcClient;
import com.nitb.apigateway.mapper.QuestionMapper;
import com.nitb.testservice.grpc.QuestionDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final TestServiceGrpcClient testGrpc;


    @Override
    public Mono<QuestionResponseDto> getQuestionById(UUID id) {
        return Mono.fromCallable(()->{
            QuestionDetailResponse response = testGrpc.getQuestionById(id);
            return QuestionMapper.toQuestionResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
