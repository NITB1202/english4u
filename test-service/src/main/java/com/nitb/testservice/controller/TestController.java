package com.nitb.testservice.controller;

import com.google.protobuf.Empty;
import com.nitb.testservice.dto.TestStatisticDto;
import com.nitb.testservice.entity.Part;
import com.nitb.testservice.entity.Question;
import com.nitb.testservice.entity.Test;
import com.nitb.testservice.grpc.*;
import com.nitb.testservice.mapper.FileMapper;
import com.nitb.testservice.mapper.PartMapper;
import com.nitb.testservice.mapper.QuestionMapper;
import com.nitb.testservice.mapper.TestMapper;
import com.nitb.testservice.service.PartService;
import com.nitb.testservice.service.QuestionService;
import com.nitb.testservice.service.TestService;
import com.nitb.testservice.service.FileService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class TestController extends TestServiceGrpc.TestServiceImplBase {
    private final TestService testService;
    private final PartService partService;
    private final QuestionService questionService;
    private final FileService fileService;

    //Tests
    @Override
    public void createTest(CreateTestRequest request, StreamObserver<CreateTestResponse> streamObserver) {
        Test test = testService.createTest(request);
        CreateTestResponse response = TestMapper.toCreateTestResponse(test);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void getTestById(GetTestByIdRequest request, StreamObserver<TestDetailResponse> streamObserver) {
        Test test = testService.getTestById(request);

        int questionCount = partService.getTotalQuestionCount(test.getId());
        TestDetailResponse response = TestMapper.toTestDetailResponse(test, questionCount);

        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void getTestNameById(GetTestByIdRequest request, StreamObserver<TestNameResponse> streamObserver) {
        String name = testService.getTestNameById(request);

        TestNameResponse response = TestNameResponse.newBuilder()
                .setName(name)
                .build();

        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void getTests(GetTestsRequest request, StreamObserver<TestsPaginationResponse> streamObserver) {
        Page<Test> tests = testService.getTests(request);
        TestsPaginationResponse response = paginateTest(tests);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void getDeletedTests(GetDeletedTestsRequest request, StreamObserver<TestsPaginationResponse> streamObserver) {
        Page<Test> tests = testService.getDeletedTests(request);
        TestsPaginationResponse response = paginateTest(tests);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void searchTestByName(SearchTestByNameRequest request, StreamObserver<TestsPaginationResponse> streamObserver) {
        Page<Test> tests = testService.searchTestByName(request);
        TestsPaginationResponse response = paginateTest(tests);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void searchDeletedTestByName(SearchDeletedTestByNameRequest request, StreamObserver<TestsPaginationResponse> streamObserver) {
        Page<Test> tests = testService.searchDeletedTestByName(request);
        TestsPaginationResponse response = paginateTest(tests);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void validateUpdateTest(ValidateUpdateTestRequest request, StreamObserver<Empty> streamObserver) {
        testService.validateUpdateTest(request);
        streamObserver.onNext(Empty.getDefaultInstance());
        streamObserver.onCompleted();
    }

    @Override
    public void updateTestNameAndTopic(UpdateTestNameAndTopicRequest request, StreamObserver<UpdateTestResponse> streamObserver) {
        Test test = testService.updateTestNameAndTopic(request);
        UpdateTestResponse response = TestMapper.toUpdateTestResponse(test);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void updateTest(UpdateTestRequest request, StreamObserver<UpdateTestResponse> streamObserver) {
        Test test = testService.updateTest(request);
        UpdateTestResponse response = TestMapper.toUpdateTestResponse(test);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void deleteTest(DeleteTestRequest request, StreamObserver<DeleteTestResponse> streamObserver) {
        Test test = testService.deleteTest(request);
        DeleteTestResponse response = TestMapper.toDeleteTestResponse(test);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void restoreTest(RestoreTestRequest request, StreamObserver<DeleteTestResponse> streamObserver) {
        Test test = testService.restoreTest(request);
        DeleteTestResponse response = TestMapper.toDeleteTestResponse(test);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void getPublishedTestStatistics(GetPublishedTestStatisticsRequest request, StreamObserver<GetPublishedTestStatisticsResponse> streamObserver) {
        List<TestStatisticDto> statistics = testService.getPublishedTestStatistics(request);
        GetPublishedTestStatisticsResponse response = TestMapper.toGetPublishedTestStatisticsResponse(statistics);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void generateTestTemplate(Empty request, StreamObserver<TestTemplateResponse> streamObserver) {
        byte[] data = fileService.generateTestTemplate();
        TestTemplateResponse response = FileMapper.toTestTemplateResponse(data);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void uploadTestTemplate(UploadTestTemplateRequest request, StreamObserver<CreateTestResponse> streamObserver) {
        Test test = fileService.uploadTestTemplate(request);
        CreateTestResponse response = TestMapper.toCreateTestResponse(test);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    private TestsPaginationResponse paginateTest(Page<Test> tests) {
        List<TestSummaryResponse> summaryResponses = new ArrayList<>();
        for (Test test : tests.getContent()) {
            int questionCount = partService.getTotalQuestionCount(test.getId());
            TestSummaryResponse response = TestMapper.toTestSummaryResponse(test, questionCount);
            summaryResponses.add(response);
        }

        return TestMapper.toTestsPaginationResponse(tests, summaryResponses);
    }

    //Parts
    @Override
    public void createParts(CreatePartsRequest request, StreamObserver<Empty> streamObserver) {
        UUID testId = UUID.fromString(request.getTestId());
        int partCount = partService.getPartCount(testId);

        for(int i = partCount; i < request.getPartsCount(); i++) {
            CreatePartRequest partRequest = request.getParts(i);

            Part part = partService.createPart(testId, i + 1, partRequest.getContent());
            List<CreateQuestionRequest> questions = partRequest.getQuestionsList();

            questionService.createQuestions(part.getId(), questions);
            partService.updateQuestionCount(part.getId(), questions.size());
        }

        testService.updatePartCount(testId, request.getPartsCount());

        streamObserver.onNext(Empty.getDefaultInstance());
        streamObserver.onCompleted();
    }

    @Override
    public void getAllPartsForTest(GetAllPartsForTestRequest request, StreamObserver<PartsResponse> streamObserver) {
        List<Part> parts = partService.getAllPartsForTest(request);

        List<PartResponse> partsResponse = new ArrayList<>();
        for(Part part : parts) {
            List<Question> questions = questionService.getAllQuestionsForPart(part.getId());
            PartResponse partResponse = PartMapper.toPartResponse(part, questions);

            partsResponse.add(partResponse);
        }

        PartsResponse response = PartMapper.toPartsResponse(partsResponse);

        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    //Question
    @Override
    public void getQuestionById(GetQuestionByIdRequest request, StreamObserver<QuestionDetailResponse> streamObserver) {
        Question question = questionService.getQuestionById(request);
        String partContent = partService.getPartContent(question.getPartId());

        QuestionDetailResponse response = QuestionMapper.toQuestionDetailResponse(partContent, question);

        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void getQuestionAnswers(GetQuestionAnswersRequest request, StreamObserver<QuestionAnswersResponse> streamObserver) {
        testService.validateTestId(request.getTestId());

        List<Question> questions = getAllQuestionsInTest(request.getTestId());
        QuestionAnswersResponse response = QuestionMapper.toQuestionAnswersResponse(questions);

        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void getQuestionPositions(GetQuestionPositionsRequest request, StreamObserver<QuestionPositionsResponse> streamObserver) {
        testService.validateTestId(request.getTestId());

        List<Question> questions = getAllQuestionsInTest(request.getTestId());
        QuestionPositionsResponse response = QuestionMapper.toQuestionPositionsResponse(questions);

        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    private List<Question> getAllQuestionsInTest(String testIdStr) {
        UUID testId = UUID.fromString(testIdStr);
        List<UUID> partIds = partService.getAllPartIdsForTest(testId);

        List<Question> questions = new ArrayList<>();

        for(UUID partId : partIds) {
            List<Question> partQuestions = questionService.getAllQuestionsForPart(partId);
            questions.addAll(partQuestions);
        }

        return questions;
    }
}
