package com.nitb.testservice.controller;

import com.nitb.common.grpc.ActionResponse;
import com.nitb.testservice.entity.Part;
import com.nitb.testservice.entity.Question;
import com.nitb.testservice.entity.Test;
import com.nitb.testservice.grpc.*;
import com.nitb.testservice.mapper.PartMapper;
import com.nitb.testservice.mapper.QuestionMapper;
import com.nitb.testservice.mapper.TestMapper;
import com.nitb.testservice.service.PartService;
import com.nitb.testservice.service.QuestionService;
import com.nitb.testservice.service.TestService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class TestController extends TestServiceGrpc.TestServiceImplBase {
    private final TestService testService;
    private final PartService partService;
    private final QuestionService questionService;

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

        int questionCount = partService.getTotalQuestion(test.getId());
        TestDetailResponse response = TestMapper.toTestDetailResponse(test, questionCount);

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

    private TestsPaginationResponse paginateTest(Page<Test> tests) {
        List<TestSummaryResponse> summaryResponses = new ArrayList<>();
        for (Test test : tests.getContent()) {
            int questionCount = partService.getTotalQuestion(test.getId());
            TestSummaryResponse response = TestMapper.toTestSummaryResponse(test, questionCount);
            summaryResponses.add(response);
        }

        return TestMapper.toTestsPaginationResponse(tests, summaryResponses);
    }


    //Parts
    @Override
    public void createPart(CreatePartRequest request, StreamObserver<PartResponse> streamObserver) {
        Part part = partService.createPart(request);
        PartResponse response = PartMapper.toPartResponse(part);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void getPartContentById(GetPartContentByIdRequest request, StreamObserver<GetPartContentByIdResponse> streamObserver) {
        String content = partService.getPartContentById(request);
        GetPartContentByIdResponse response = GetPartContentByIdResponse.newBuilder()
                .setContent(content)
                .build();
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void getAllPartsForTest(GetAllPartsForTestRequest request, StreamObserver<GetAllPartsForTestResponse> streamObserver) {
        List<Part> parts = partService.getAllPartsForTest(request);
        GetAllPartsForTestResponse response = PartMapper.toGetAllPartsForTestResponse(parts);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void updatePart(UpdatePartRequest request, StreamObserver<ActionResponse> streamObserver) {
        partService.updatePart(request);
        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Update successfully.")
                .build();
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void swapPartsPosition(SwapPartsPositionRequest request, StreamObserver<ActionResponse> streamObserver) {
        partService.swapPartsPosition(request);
        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Swap successfully.")
                .build();
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }


    //Questions
    @Override
    public void createQuestions(CreateQuestionsRequest request, StreamObserver<QuestionsResponse> streamObserver) {
        List<Question> questions = questionService.createQuestions(request);
        QuestionsResponse response = QuestionMapper.toQuestionsResponse(questions);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void getQuestionById(GetQuestionByIdRequest request, StreamObserver<QuestionResponse> streamObserver) {
        Question question = questionService.getQuestionById(request);
        QuestionResponse response = QuestionMapper.toQuestionResponse(question);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void getAllQuestionsForPart(GetAllQuestionsForPartRequest request, StreamObserver<GetAllQuestionForPartResponse> streamObserver) {
        List<Question> questions = questionService.getAllQuestionsForPart(request);
        GetAllQuestionForPartResponse response = QuestionMapper.toGetAllQuestionForPartResponse(questions);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void updateQuestion(UpdateQuestionRequest request, StreamObserver<QuestionResponse> streamObserver) {
        Question question = questionService.updateQuestion(request);
        QuestionResponse response = QuestionMapper.toQuestionResponse(question);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void swapQuestionsPosition(SwapQuestionsPositionRequest request, StreamObserver<ActionResponse> streamObserver) {
        questionService.swapQuestionsPosition(request);
        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Swap successfully.")
                .build();
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }
}
