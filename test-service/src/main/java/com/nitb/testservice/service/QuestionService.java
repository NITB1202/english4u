package com.nitb.testservice.service;

import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.exceptions.NotFoundException;
import com.nitb.testservice.entity.Question;
import com.nitb.testservice.grpc.*;
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
    private final PartService partService;

    public List<Question> createQuestions(CreateQuestionsRequest requests) {
        UUID partId = UUID.fromString(requests.getPartId());
        UUID userId = UUID.fromString(requests.getUserId());

        //Create questions
        List<Question> savedQuestions = new ArrayList<>();
        int position = questionRepository.countByPartId(partId);

        for(CreateQuestionRequest request : requests.getRequestsList()){
            if(request.getContent().isEmpty()){
                throw new BusinessException("Content is empty.");
            }

            if(request.getAnswers().isEmpty()){
                throw new BusinessException("Answers list is empty.");
            }

            if(request.getCorrectAnswer().isEmpty()){
                throw new BusinessException("Correct answer is empty.");
            }

            position++;

            Question question = Question.builder()
                    .partId(partId)
                    .position(position)
                    .content(request.getContent())
                    .answers(request.getAnswers())
                    .correctAnswer(request.getCorrectAnswer())
                    .explanation(request.getExplanation())
                    .build();

            savedQuestions.add(question);

        }

        //Update part
        partService.updateQuestionCount(partId, position, userId);

        return questionRepository.saveAll(savedQuestions);
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

        if(!request.getAnswers().isEmpty()){
            question.setAnswers(request.getAnswers());
        }

        if(!request.getCorrectAnswer().isEmpty()){
            question.setCorrectAnswer(request.getCorrectAnswer());
        }

        if(!request.getExplanation().isEmpty()){
            question.setExplanation(request.getExplanation());
        }

        //Update part
        partService.updateLastModified(question.getPartId(), UUID.fromString(request.getUserId()));

        return questionRepository.save(question);
    }

    public void swapQuestionsPosition(SwapQuestionsPositionRequest request) {
        Question firstQuestion = questionRepository.findById(UUID.fromString(request.getQuestion1Id())).orElseThrow(
                () -> new NotFoundException("First question not found.")
        );

        Question secondQuestion = questionRepository.findById(UUID.fromString(request.getQuestion2Id())).orElseThrow(
                () -> new NotFoundException("Second question not found.")
        );

        if(!firstQuestion.getPartId().equals(secondQuestion.getPartId())){
            throw new BusinessException("First and second questions are not in the same part.");
        }

        int temp = firstQuestion.getPosition();
        firstQuestion.setPosition(secondQuestion.getPosition());
        secondQuestion.setPosition(temp);

        questionRepository.save(firstQuestion);
        questionRepository.save(secondQuestion);

        //Update part
        partService.updateLastModified(firstQuestion.getPartId(), UUID.fromString(request.getUserId()));
    }
}
