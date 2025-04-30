package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.Test.request.Question.CreateQuestionRequestDto;
import com.nitb.testservice.grpc.CreateQuestionRequest;

public class QuestionMapper {
    private QuestionMapper() {}

    public static CreateQuestionRequest toCreateQuestionRequest(CreateQuestionRequestDto dto) {
        String explanation = dto.getExplanation() != null ? dto.getExplanation() : "";

        return CreateQuestionRequest.newBuilder()
                .setContent(dto.getContent())
                .setAnswers(dto.getAnswers().toString())
                .setCorrectAnswer(dto.getCorrectAnswer().toString())
                .setExplanation(explanation)
                .build();

    }
}
