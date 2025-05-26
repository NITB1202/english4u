package com.nitb.testservice.mapper;

import com.nitb.testservice.entity.Question;
import com.nitb.testservice.grpc.QuestionAnswersResponse;
import com.nitb.testservice.grpc.QuestionDetailResponse;
import com.nitb.testservice.grpc.QuestionPositionsResponse;
import com.nitb.testservice.grpc.QuestionResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public static QuestionAnswersResponse toQuestionAnswersResponse(List<Question> questions) {
        Map<String, String> answers = questions.stream()
                .collect(Collectors.toMap(
                        q -> q.getId().toString(),
                        Question::getCorrectAnswer
                ));

        return QuestionAnswersResponse.newBuilder()
                .putAllAnswers(answers)
                .build();
    }

    public static QuestionPositionsResponse toQuestionPositionsResponse(List<Question> questions) {
        Map<String, Integer> positions = questions.stream()
                .collect(Collectors.toMap(
                   q -> q.getId().toString(),
                   Question::getPosition
                ));

        return QuestionPositionsResponse.newBuilder()
                .putAllPositions(positions)
                .build();
    }
}
