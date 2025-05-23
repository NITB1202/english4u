package com.nitb.apigateway.service.Test;

import com.nitb.apigateway.dto.Test.response.Question.QuestionResponseDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface QuestionService {
    Mono<QuestionResponseDto> getQuestionById(UUID id);
}
