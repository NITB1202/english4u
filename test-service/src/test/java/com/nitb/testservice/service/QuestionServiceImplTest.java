package com.nitb.testservice.service;

import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.exceptions.NotFoundException;
import com.nitb.testservice.entity.Question;
import com.nitb.testservice.grpc.CreateQuestionRequest;
import com.nitb.testservice.grpc.GetQuestionByIdRequest;
import com.nitb.testservice.repository.QuestionRepository;
import com.nitb.testservice.service.impl.QuestionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionServiceImpl questionService;

    private UUID partId;

    @BeforeEach
    void setup() {
        partId = UUID.randomUUID();
    }

    @Test
    void createQuestions_validRequest_shouldSaveAll() {
        // Given
        List<CreateQuestionRequest> requests = List.of(
                CreateQuestionRequest.newBuilder()
                        .setContent("Question 1?")
                        .setAnswers("A,B,C,D")
                        .setCorrectAnswer("A")
                        .setExplanation("Explanation 1")
                        .build(),
                CreateQuestionRequest.newBuilder()
                        .setContent("Question 2?")
                        .setAnswers("True,False")
                        .setCorrectAnswer("B")
                        .setExplanation("Explanation 2")
                        .build()
        );

        // When
        questionService.createQuestions(partId, requests);

        // Then
        verify(questionRepository, times(1)).saveAll(anyList());
    }

    @Test
    void createQuestions_emptyContent_shouldThrow() {
        List<CreateQuestionRequest> requests = List.of(
                CreateQuestionRequest.newBuilder()
                        .setContent("")
                        .setAnswers("A,B,C,D")
                        .setCorrectAnswer("A")
                        .setExplanation("...")
                        .build()
        );

        BusinessException exception = assertThrows(BusinessException.class,
                () -> questionService.createQuestions(partId, requests));

        assertEquals("Content is empty.", exception.getMessage());
        verify(questionRepository, never()).saveAll(any());
    }

    @Test
    void createQuestions_emptyAnswers_shouldThrow() {
        List<CreateQuestionRequest> requests = List.of(
                CreateQuestionRequest.newBuilder()
                        .setContent("Question?")
                        .setAnswers("")
                        .setCorrectAnswer("A")
                        .setExplanation("...")
                        .build()
        );

        BusinessException exception = assertThrows(BusinessException.class,
                () -> questionService.createQuestions(partId, requests));

        assertEquals("Answers list is empty.", exception.getMessage());
        verify(questionRepository, never()).saveAll(any());
    }

    @Test
    void createQuestions_emptyCorrectAnswer_shouldThrow() {
        List<CreateQuestionRequest> requests = List.of(
                CreateQuestionRequest.newBuilder()
                        .setContent("Question?")
                        .setAnswers("A,B")
                        .setCorrectAnswer("")
                        .setExplanation("...")
                        .build()
        );

        BusinessException exception = assertThrows(BusinessException.class,
                () -> questionService.createQuestions(partId, requests));

        assertEquals("Correct answer is empty.", exception.getMessage());
        verify(questionRepository, never()).saveAll(any());
    }

    @Test
    void createQuestions_invalidCorrectAnswerLength_shouldThrow() {
        List<CreateQuestionRequest> requests = List.of(
                CreateQuestionRequest.newBuilder()
                        .setContent("Question?")
                        .setAnswers("A,B")
                        .setCorrectAnswer("AB")
                        .setExplanation("...")
                        .build()
        );

        BusinessException exception = assertThrows(BusinessException.class,
                () -> questionService.createQuestions(partId, requests));

        assertEquals("Correct answer must be a letter.", exception.getMessage());
        verify(questionRepository, never()).saveAll(any());
    }

    @Test
    void getQuestionById_existingId_shouldReturnQuestion() {
        // Given
        UUID id = UUID.randomUUID();
        GetQuestionByIdRequest request = GetQuestionByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        Question mockQuestion = Question.builder()
                .id(id)
                .content("What is the capital of France?")
                .build();

        when(questionRepository.findById(id)).thenReturn(Optional.of(mockQuestion));

        // When
        Question result = questionService.getQuestionById(request);

        // Then
        assertNotNull(result);
        assertEquals("What is the capital of France?", result.getContent());
        verify(questionRepository, times(1)).findById(id);
    }

    @Test
    void getQuestionById_nonExistentId_shouldThrowNotFoundException() {
        // Given
        UUID id = UUID.randomUUID();
        GetQuestionByIdRequest request = GetQuestionByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        when(questionRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> questionService.getQuestionById(request));

        assertEquals("Question not found.", exception.getMessage());
        verify(questionRepository, times(1)).findById(id);
    }

    @Test
    void getAllQuestionsForPart_shouldReturnListOfQuestions() {
        // Given
        UUID partId = UUID.randomUUID();

        List<Question> mockQuestions = List.of(
                Question.builder().id(UUID.randomUUID()).partId(partId).content("Question 1").build(),
                Question.builder().id(UUID.randomUUID()).partId(partId).content("Question 2").build()
        );

        when(questionRepository.findByPartId(partId)).thenReturn(mockQuestions);

        // When
        List<Question> result = questionService.getAllQuestionsForPart(partId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Question 1", result.get(0).getContent());
        assertEquals("Question 2", result.get(1).getContent());
        verify(questionRepository, times(1)).findByPartId(partId);
    }

    @Test
    void getAllQuestionsForPart_shouldReturnEmptyListIfNoQuestions() {
        // Given
        UUID partId = UUID.randomUUID();

        when(questionRepository.findByPartId(partId)).thenReturn(List.of());

        // When
        List<Question> result = questionService.getAllQuestionsForPart(partId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(questionRepository, times(1)).findByPartId(partId);
    }
}
