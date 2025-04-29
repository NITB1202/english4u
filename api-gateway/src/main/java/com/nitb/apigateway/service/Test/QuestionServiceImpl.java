package com.nitb.apigateway.service.Test;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.Test.request.Question.AddQuestionsToPartRequestDto;
import com.nitb.apigateway.dto.Test.request.Question.UpdateQuestionRequestDto;
import com.nitb.apigateway.dto.Test.response.Question.QuestionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    @Override
    public List<QuestionResponseDto> addQuestionsToPart(UUID userId, AddQuestionsToPartRequestDto request) {
        return List.of();
    }

    @Override
    public QuestionResponseDto getQuestionById(UUID id) {
        return null;
    }

    @Override
    public QuestionResponseDto updateQuestion(UUID id, UUID userId, UpdateQuestionRequestDto request) {
        return null;
    }

    @Override
    public ActionResponseDto swapQuestionsPosition(UUID userId, UUID question1Id, UUID question2Id) {
        return null;
    }
}
