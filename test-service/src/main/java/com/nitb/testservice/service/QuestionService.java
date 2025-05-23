package com.nitb.testservice.service;

import com.nitb.testservice.entity.Question;
import com.nitb.testservice.grpc.CreateQuestionRequest;
import com.nitb.testservice.grpc.GetQuestionByIdRequest;

import java.util.List;
import java.util.UUID;

public interface QuestionService {
    void createQuestions(UUID partId, List<CreateQuestionRequest> requests);
    Question getQuestionById(GetQuestionByIdRequest request);
    List<Question> getAllQuestionsForPart(UUID partId);
}
