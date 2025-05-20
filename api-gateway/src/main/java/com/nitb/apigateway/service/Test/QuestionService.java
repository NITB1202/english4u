package com.nitb.apigateway.service.Test;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.Test.request.Question.AddQuestionsToPartRequestDto;
import com.nitb.apigateway.dto.Test.request.Question.UpdateQuestionRequestDto;
import com.nitb.apigateway.dto.Test.response.Question.QuestionResponseDto;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface QuestionService {
    Mono<List<QuestionResponseDto>> addQuestionsToPart(UUID userId, AddQuestionsToPartRequestDto request);
    Mono<QuestionResponseDto> getQuestionById(UUID id);
    Mono<QuestionResponseDto> updateQuestion(UUID id, UUID userId, UpdateQuestionRequestDto request);
    Mono<ActionResponseDto> swapQuestionsPosition(UUID userId, UUID question1Id, UUID question2Id);
}
