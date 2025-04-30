package com.nitb.testservice.mapper;

import com.nitb.testservice.entity.Question;
import com.nitb.testservice.grpc.GetAllQuestionsForPartResponse;
import com.nitb.testservice.grpc.QuestionResponse;
import com.nitb.testservice.grpc.QuestionSummaryResponse;
import com.nitb.testservice.grpc.QuestionsResponse;

import java.util.List;

public class QuestionMapper {
    private QuestionMapper() {}

    public static QuestionResponse toQuestionResponse(Question question) {
        return QuestionResponse.newBuilder()
                .setId(question.getId().toString())
                .setPosition(question.getPosition())
                .setContent(question.getContent())
                .setAnswers(question.getAnswers())
                .setCorrectAnswer(question.getCorrectAnswer())
                .setExplanation(question.getExplanation())
                .build();
    }

    public static QuestionsResponse toQuestionsResponse(List<Question> questions) {
        List<QuestionResponse> questionResponses = questions.stream()
                .map(QuestionMapper::toQuestionResponse)
                .toList();

        return QuestionsResponse.newBuilder()
                .addAllResponse(questionResponses)
                .build();
    }

    public static QuestionSummaryResponse toQuestionSummaryResponse(Question question) {
        return QuestionSummaryResponse.newBuilder()
                .setId(question.getId().toString())
                .setPosition(question.getPosition())
                .setContent(question.getContent())
                .setAnswers(question.getAnswers())
                .build();
    }

    public static GetAllQuestionsForPartResponse toGetAllQuestionsForPartResponse(List<Question> questions) {
        List<QuestionSummaryResponse> questionResponses = questions.stream()
                .map(QuestionMapper::toQuestionSummaryResponse)
                .toList();

        return GetAllQuestionsForPartResponse.newBuilder()
                .addAllQuestions(questionResponses)
                .build();
    }
}
