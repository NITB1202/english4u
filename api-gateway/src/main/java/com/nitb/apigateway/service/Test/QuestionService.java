package com.nitb.apigateway.service.Test;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.Test.request.Question.AddQuestionsToPartRequestDto;
import com.nitb.apigateway.dto.Test.request.Question.UpdateQuestionRequestDto;
import com.nitb.apigateway.dto.Test.response.Question.QuestionResponseDto;

import java.util.List;
import java.util.UUID;

public interface QuestionService {
    List<QuestionResponseDto> addQuestionsToPart(UUID userId, AddQuestionsToPartRequestDto request);
    QuestionResponseDto getQuestionById(UUID id);
    QuestionResponseDto updateQuestion(UUID id, UUID userId, UpdateQuestionRequestDto request);
    ActionResponseDto swapQuestionsPosition(UUID userId, UUID question1Id, UUID question2Id);
}
