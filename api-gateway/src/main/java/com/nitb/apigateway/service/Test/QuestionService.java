package com.nitb.apigateway.service.Test;

import com.nitb.apigateway.dto.Test.Question.response.QuestionDetailResponseDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface QuestionService {
    Mono<QuestionDetailResponseDto> getQuestionById(UUID id);
}
