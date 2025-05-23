package com.nitb.testservice.service.impl;

import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.exceptions.NotFoundException;
import com.nitb.testservice.entity.Question;
import com.nitb.testservice.grpc.*;
import com.nitb.testservice.repository.QuestionRepository;
import com.nitb.testservice.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;

    @Override
    public void createQuestions(UUID partId, List<CreateQuestionRequest> requests) {
        List<Question> savedQuestions = new ArrayList<>();

        for(int i = 0; i < requests.size(); i++) {
            CreateQuestionRequest request = requests.get(i);

            if(request.getContent().isEmpty()){
                throw new BusinessException("Content is empty.");
            }

            if(request.getAnswers().isEmpty()){
                throw new BusinessException("Answers list is empty.");
            }

            if(request.getCorrectAnswer().isEmpty()){
                throw new BusinessException("Correct answer is empty.");
            }

            Question question = Question.builder()
                    .partId(partId)
                    .position(i + 1)
                    .content(request.getContent())
                    .answers(request.getAnswers())
                    .correctAnswer(request.getCorrectAnswer())
                    .explanation(request.getExplanation())
                    .build();

            savedQuestions.add(question);
        }

        questionRepository.saveAll(savedQuestions);
    }

    @Override
    public Question getQuestionById(GetQuestionByIdRequest request) {
        UUID id = UUID.fromString(request.getId());
        return questionRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Question not found.")
        );
    }

    @Override
    public List<Question> getAllQuestionsForPart(UUID partId) {
        return questionRepository.findByPartId(partId);
    }
}
