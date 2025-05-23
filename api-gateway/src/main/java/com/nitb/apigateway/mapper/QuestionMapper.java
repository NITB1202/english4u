package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.Test.Question.request.CreateQuestionRequestDto;
import com.nitb.apigateway.dto.Test.Question.response.QuestionDetailResponseDto;
import com.nitb.apigateway.dto.Test.Question.response.QuestionSummaryResponseDto;
import com.nitb.testservice.grpc.CreateQuestionRequest;
import com.nitb.testservice.grpc.QuestionDetailResponse;
import com.nitb.testservice.grpc.QuestionResponse;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class QuestionMapper {
    private QuestionMapper() {}

    public static CreateQuestionRequest toCreateQuestionRequest(CreateQuestionRequestDto dto) {
        String explanation = dto.getExplanation() != null ? dto.getExplanation().trim() : "";

        return CreateQuestionRequest.newBuilder()
                .setContent(dto.getContent())
                .setAnswers(String.join(",", dto.getAnswers()))
                .setCorrectAnswer(dto.getCorrectAnswer().toString())
                .setExplanation(explanation)
                .build();

    }

    public static QuestionSummaryResponseDto toQuestionSummaryResponseDto(QuestionResponse question) {
        List<String> answers = Arrays.asList(question.getAnswers().split(","));

        return QuestionSummaryResponseDto.builder()
                .id(UUID.fromString(question.getId()))
                .position(question.getPosition())
                .content(question.getContent())
                .answers(answers)
                .build();
    }

    public static QuestionDetailResponseDto toQuestionResponseDto(QuestionDetailResponse question) {
        List<String> answers = Arrays.asList(question.getAnswers().split(","));
        char correctAnswer = question.getCorrectAnswer().charAt(0);

        return QuestionDetailResponseDto.builder()
                .id(UUID.fromString(question.getId()))
                .position(question.getPosition())
                .content(question.getContent())
                .answers(answers)
                .correctAnswer(correctAnswer)
                .explanation(question.getExplanation())
                .partContent(question.getPartContent())
                .build();
    }
}
