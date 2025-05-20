package com.nitb.apigateway.grpc;

import com.nitb.apigateway.dto.Test.request.Part.CreatePartRequestDto;
import com.nitb.apigateway.dto.Test.request.Part.UpdatePartRequestDto;
import com.nitb.apigateway.dto.Test.request.Question.CreateQuestionRequestDto;
import com.nitb.apigateway.dto.Test.request.Question.UpdateQuestionRequestDto;
import com.nitb.apigateway.dto.Test.request.Test.CreateTestRequestDto;
import com.nitb.apigateway.dto.Test.request.Test.UpdateTestRequestDto;
import com.nitb.apigateway.mapper.QuestionMapper;
import com.nitb.common.enums.GroupBy;
import com.nitb.common.grpc.ActionResponse;
import com.nitb.common.mappers.GroupByMapper;
import com.nitb.testservice.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TestServiceGrpcClient {
    @GrpcClient("test-service")
    private TestServiceGrpc.TestServiceBlockingStub blockingStub;

    //Tests
    public CreateTestResponse createTest(UUID userId, CreateTestRequestDto dto) {
        String topic = dto.getTopic() != null ? dto.getTopic().trim() : "";

        CreateTestRequest request = CreateTestRequest.newBuilder()
                .setUserId(userId.toString())
                .setName(dto.getName().trim())
                .setMinutes(dto.getMinutes())
                .setTopic(topic)
                .build();

        return blockingStub.createTest(request);
    }

    public TestDetailResponse getTestById(UUID id) {
        GetTestByIdRequest request = GetTestByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        return blockingStub.getTestById(request);
    }

    public TestsPaginationResponse getTests(int page, int size) {
        GetTestsRequest request = GetTestsRequest.newBuilder()
                .setPage(page)
                .setSize(size)
                .build();

        return blockingStub.getTests(request);
    }

    public TestsPaginationResponse getDeletedTests(int page, int size) {
        GetDeletedTestsRequest request = GetDeletedTestsRequest.newBuilder()
                .setPage(page)
                .setSize(size)
                .build();

        return blockingStub.getDeletedTests(request);
    }

    public TestsPaginationResponse searchTestByName(String keyword, int page, int size) {
        SearchTestByNameRequest request = SearchTestByNameRequest.newBuilder()
                .setKeyword(keyword)
                .setPage(page)
                .setSize(size)
                .build();

        return blockingStub.searchTestByName(request);
    }

    public UpdateTestResponse updateTest(UUID id, UUID userId, UpdateTestRequestDto dto) {
        String name = dto.getName() != null ? dto.getName().trim() : "";
        int minutes = dto.getMinutes() != null ? dto.getMinutes() : 0;
        String topic = dto.getTopic() != null ? dto.getTopic().trim() : "";

        UpdateTestRequest request = UpdateTestRequest.newBuilder()
                .setId(id.toString())
                .setUserId(userId.toString())
                .setName(name)
                .setMinutes(minutes)
                .setTopic(topic)
                .build();

        return blockingStub.updateTest(request);
    }

    public DeleteTestResponse deleteTest(UUID id, UUID userId) {
        DeleteTestRequest request = DeleteTestRequest.newBuilder()
                .setId(id.toString())
                .setUserId(userId.toString())
                .build();

        return blockingStub.deleteTest(request);
    }

    public DeleteTestResponse restoreTest(UUID id, UUID userId) {
        RestoreTestRequest request = RestoreTestRequest.newBuilder()
                .setId(id.toString())
                .setUserId(userId.toString())
                .build();

        return blockingStub.restoreTest(request);
    }

    public GetPublishedTestStatisticsResponse getPublishedTestStatistics(UUID userId, LocalDate from, LocalDate to, GroupBy groupBy) {
        GetPublishedTestStatisticsRequest request = GetPublishedTestStatisticsRequest.newBuilder()
                .setUserId(userId.toString())
                .setFrom(from.toString())
                .setTo(to.toString())
                .setGroupBy(GroupByMapper.toGrpcEnum(groupBy))
                .build();

        return blockingStub.getPublishedTestStatistics(request);
    }

    //Parts
    public PartResponse createPart(UUID userId, UUID testId, CreatePartRequestDto dto) {
        String content = dto.getContent() != null ? dto.getContent().trim() : "";

        CreatePartRequest request = CreatePartRequest.newBuilder()
                .setTestId(testId.toString())
                .setUserId(userId.toString())
                .setContent(content)
                .build();

        return blockingStub.createPart(request);
    }

    public GetPartContentByIdResponse getPartContentById(UUID id) {
        GetPartContentByIdRequest request = GetPartContentByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        return blockingStub.getPartContentById(request);
    }

    public GetAllPartsForTestResponse getAllPartsForTest(UUID testId) {
        GetAllPartsForTestRequest request = GetAllPartsForTestRequest.newBuilder()
                .setTestId(testId.toString())
                .build();

        return blockingStub.getAllPartsForTest(request);
    }

    public ActionResponse updatePart(UUID id, UUID userId, UpdatePartRequestDto dto) {
        UpdatePartRequest request = UpdatePartRequest.newBuilder()
                .setId(id.toString())
                .setUserId(userId.toString())
                .setContent(dto.getContent())
                .build();

        return blockingStub.updatePart(request);
    }

    public ActionResponse swapPartsPosition(UUID userId, UUID part1Id, UUID part2Id) {
        SwapPartsPositionRequest request = SwapPartsPositionRequest.newBuilder()
                .setUserId(userId.toString())
                .setPart1Id(part1Id.toString())
                .setPart2Id(part2Id.toString())
                .build();

        return blockingStub.swapPartsPosition(request);
    }

    //Questions
    public QuestionsResponse createQuestions(UUID userId, UUID partId, List<CreateQuestionRequestDto> dto) {
        List<CreateQuestionRequest> questions = dto.stream()
                .map(QuestionMapper::toCreateQuestionRequest)
                .toList();

        CreateQuestionsRequest request = CreateQuestionsRequest.newBuilder()
                .setUserId(userId.toString())
                .setPartId(partId.toString())
                .addAllRequests(questions)
                .build();

        return blockingStub.createQuestions(request);
    }

    public QuestionResponse getQuestionById(UUID id) {
        GetQuestionByIdRequest request = GetQuestionByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        return blockingStub.getQuestionById(request);
    }

    public GetAllQuestionsForPartResponse getAllQuestionsForPart(UUID partId) {
        GetAllQuestionsForPartRequest request = GetAllQuestionsForPartRequest.newBuilder()
                .setPartId(partId.toString())
                .build();

        return blockingStub.getAllQuestionsForPart(request);
    }

    public QuestionResponse updateQuestion(UUID id, UUID userId, UpdateQuestionRequestDto dto) {
        String content = dto.getContent() != null ? dto.getContent() : "";
        String answers = dto.getAnswers() != null && !dto.getAnswers().isEmpty() ?
                dto.getAnswers().toString() : "";
        String correctAnswer = dto.getCorrectAnswer() != null ? dto.getCorrectAnswer().toString() : "";
        String explanation = dto.getExplanation() != null ? dto.getExplanation() : "";

        UpdateQuestionRequest request = UpdateQuestionRequest.newBuilder()
                .setId(id.toString())
                .setUserId(userId.toString())
                .setContent(content)
                .setAnswers(answers)
                .setCorrectAnswer(correctAnswer)
                .setExplanation(explanation)
                .build();

        return blockingStub.updateQuestion(request);
    }

    public ActionResponse swapQuestionsPosition(UUID userId, UUID question1Id, UUID question2Id) {
        SwapQuestionsPositionRequest request = SwapQuestionsPositionRequest.newBuilder()
                .setUserId(userId.toString())
                .setQuestion1Id(question1Id.toString())
                .setQuestion2Id(question2Id.toString())
                .build();

        return blockingStub.swapQuestionsPosition(request);
    }
}
