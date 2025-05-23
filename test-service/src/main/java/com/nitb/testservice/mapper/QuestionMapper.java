package com.nitb.testservice.mapper;

import com.nitb.testservice.entity.Question;
import com.nitb.testservice.grpc.QuestionDetailResponse;
import com.nitb.testservice.grpc.QuestionResponse;

public class QuestionMapper {
    private QuestionMapper() {}

    public static QuestionResponse toQuestionResponse(Question question) {
        return QuestionResponse.newBuilder()
                .setId(question.getId().toString())
                .setPosition(question.getPosition())
                .setContent(question.getContent())
                .setAnswers(question.getAnswers())
                .build();
    }

    public static QuestionDetailResponse toQuestionDetailResponse(String partContent, Question question) {
        return QuestionDetailResponse.newBuilder()
                .setId(question.getId().toString())
                .setPosition(question.getPosition())
                .setContent(question.getContent())
                .setAnswers(question.getAnswers())
                .setCorrectAnswer(question.getCorrectAnswer())
                .setExplanation(question.getExplanation())
                .setPartContent(partContent)
                .build();
    }
}
