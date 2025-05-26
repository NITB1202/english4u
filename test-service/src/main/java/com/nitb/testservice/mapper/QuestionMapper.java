package com.nitb.testservice.mapper;

import com.nitb.testservice.entity.Question;
import com.nitb.testservice.grpc.AnswerResponse;
import com.nitb.testservice.grpc.AnswersResponse;
import com.nitb.testservice.grpc.QuestionDetailResponse;
import com.nitb.testservice.grpc.QuestionResponse;

import java.util.List;

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

    public static AnswerResponse toAnswerResponse(Question question) {
        return AnswerResponse.newBuilder()
                .setQuestionId(question.getId().toString())
                .setPosition(question.getPosition())
                .setCorrectAnswer(question.getCorrectAnswer())
                .build();
    }

    public static AnswersResponse toAnswersResponse(List<Question> questions) {
        List<AnswerResponse> answers = questions.stream().map(QuestionMapper::toAnswerResponse).toList();

        return AnswersResponse.newBuilder()
                .addAllAnswers(answers)
                .build();
    }
}
