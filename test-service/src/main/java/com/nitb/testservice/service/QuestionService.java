package com.nitb.testservice.service;

import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.exceptions.NotFoundException;
import com.nitb.testservice.entity.Question;
import com.nitb.testservice.grpc.*;
import com.nitb.testservice.repository.PartRepository;
import com.nitb.testservice.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final PartRepository partRepository;

    public List<Question> createQuestions(CreateQuestionsRequest requests) {
        UUID partId = UUID.fromString(requests.getPartId());

        if(!partRepository.existsById(partId)) {
            throw new NotFoundException("Part not found.");
        }

        List<Question> savedQuestions = new ArrayList<>();
        int order = questionRepository.countByPartId(partId) + 1;

        for(CreateQuestionRequest request : requests.getRequestsList()){
            if(request.getContent().isEmpty()){
                throw new BusinessException("Content is empty.");
            }

            if(request.getAnswersList().isEmpty()){
                throw new BusinessException("Answers list is empty.");
            }

            if(request.getCorrectAnswer().isEmpty()){
                throw new BusinessException("Correct answer is empty.");
            }

            Question question = Question.builder()
                    .order(order)
                    .content(request.getContent())
                    .answers(request.getAnswersList().toString())
                    .correctAnswer(request.getCorrectAnswer())
                    .explanation(request.getExplanation())
                    .build();

            savedQuestions.add(question);
            order++;
        }

        questionRepository.saveAll(savedQuestions);

        return savedQuestions;
    }

    public Question getQuestionById(GetQuestionByIdRequest request) {
        UUID id = UUID.fromString(request.getId());
        return questionRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Question not found.")
        );
    }

    public List<Question> getAllQuestionsForPart(GetAllQuestionsForPartRequest request) {
        UUID partId = UUID.fromString(request.getPartId());
        return questionRepository.findByPartId(partId);
    }

    public Question updateQuestion(UpdateQuestionRequest request) {
        UUID id = UUID.fromString(request.getId());
        Question question = questionRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Question not found.")
        );

        if(!request.getContent().isEmpty()){
            question.setContent(request.getContent());
        }

        if(!request.getAnswersList().isEmpty()){
            question.setAnswers(request.getAnswersList().toString());
        }

        if(!request.getCorrectAnswer().isEmpty()){
            question.setCorrectAnswer(request.getCorrectAnswer());
        }

        if(!request.getExplanation().isEmpty()){
            question.setExplanation(request.getExplanation());
        }

        return questionRepository.save(question);
    }

    public void swapQuestionsPosition(SwapQuestionsPositionRequest request) {
        Question firstQuestion = questionRepository.findById(UUID.fromString(request.getQuestion1Id())).orElseThrow(
                () -> new NotFoundException("First question not found.")
        );

        Question secondQuestion = questionRepository.findById(UUID.fromString(request.getQuestion2Id())).orElseThrow(
                () -> new NotFoundException("Second question not found.")
        );

        int temp = firstQuestion.getOrder();
        firstQuestion.setOrder(secondQuestion.getOrder());
        secondQuestion.setOrder(temp);

        questionRepository.save(firstQuestion);
        questionRepository.save(secondQuestion);
    }
}
