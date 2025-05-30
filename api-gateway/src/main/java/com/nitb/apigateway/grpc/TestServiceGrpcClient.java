package com.nitb.apigateway.grpc;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import com.nitb.apigateway.dto.Test.Comment.request.PostCommentRequestDto;
import com.nitb.apigateway.dto.Test.Comment.request.ReplyCommentRequestDto;
import com.nitb.apigateway.dto.Test.Part.request.CreatePartRequestDto;
import com.nitb.apigateway.dto.Test.Test.request.CreateTestRequestDto;
import com.nitb.apigateway.dto.Test.Test.request.UpdateTestInfoRequestDto;
import com.nitb.apigateway.mapper.PartMapper;
import com.nitb.common.enums.GroupBy;
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

    //Test
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

    public String getTestNameById(UUID id) {
        GetTestByIdRequest request = GetTestByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        return blockingStub.getTestNameById(request).getName();
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

    public TestsPaginationResponse searchDeletedTestByName(String keyword, int page, int size) {
        SearchDeletedTestByNameRequest request = SearchDeletedTestByNameRequest.newBuilder()
                .setKeyword(keyword)
                .setPage(page)
                .setSize(size)
                .build();

        return blockingStub.searchDeletedTestByName(request);
    }

    public Empty validateUpdateTest(UUID id) {
        ValidateUpdateTestRequest request = ValidateUpdateTestRequest.newBuilder()
                .setId(id.toString())
                .build();

        return blockingStub.validateUpdateTest(request);
    }

    public UpdateTestResponse updateTestNameAndTopic(UUID id, UUID userId, UpdateTestInfoRequestDto dto) {
        String name = dto.getName() != null ? dto.getName().trim() : "";
        String topic = dto.getTopic() != null ? dto.getTopic().trim() : "";

        UpdateTestNameAndTopicRequest request = UpdateTestNameAndTopicRequest.newBuilder()
                .setId(id.toString())
                .setUserId(userId.toString())
                .setName(name)
                .setTopic(topic)
                .build();

        return blockingStub.updateTestNameAndTopic(request);
    }

    public UpdateTestResponse updateTest(UUID oldId, UUID newId) {
        UpdateTestRequest request = UpdateTestRequest.newBuilder()
                .setOldId(oldId.toString())
                .setNewId(newId.toString())
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

    public AdminTestStatisticsResponse getAdminTestStatistics(List<String> userIds) {
        GetAdminTestStatisticsRequest request = GetAdminTestStatisticsRequest.newBuilder()
                .addAllUserId(userIds)
                .build();

        return blockingStub.getAdminTestStatistics(request);
    }

    public TestTemplateResponse generateTestTemplate() {
        return blockingStub.generateTestTemplate(Empty.getDefaultInstance());
    }

    public CreateTestResponse uploadTestTemplate(UUID userId, byte[] fileContent) {
        ByteString byteString = ByteString.copyFrom(fileContent);

        UploadTestTemplateRequest request = UploadTestTemplateRequest.newBuilder()
                .setUserId(userId.toString())
                .setFileContent(byteString)
                .build();

        return blockingStub.uploadTestTemplate(request);
    }

    //Part
    public Empty createParts(UUID userId, UUID testId, List<CreatePartRequestDto> dto) {
        List<CreatePartRequest> parts =  dto.stream().map(PartMapper::toCreatePartRequest).toList();

        CreatePartsRequest request = CreatePartsRequest.newBuilder()
                .setTestId(testId.toString())
                .setUserId(userId.toString())
                .addAllParts(parts)
                .build();

        return blockingStub.createParts(request);
    }

    public PartsResponse getAllPartsForTest(UUID testId) {
        GetAllPartsForTestRequest request = GetAllPartsForTestRequest.newBuilder()
                .setTestId(testId.toString())
                .build();

        return blockingStub.getAllPartsForTest(request);
    }

    //Question
    public QuestionDetailResponse getQuestionById(UUID id) {
        GetQuestionByIdRequest request = GetQuestionByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        return blockingStub.getQuestionById(request);
    }

    public QuestionAnswersResponse getQuestionAnswers(UUID testId) {
        GetQuestionAnswersRequest request = GetQuestionAnswersRequest.newBuilder()
                .setTestId(testId.toString())
                .build();

        return blockingStub.getQuestionAnswers(request);
    }

    public QuestionPositionsResponse getQuestionPositions(UUID testId) {
        GetQuestionPositionsRequest request = GetQuestionPositionsRequest.newBuilder()
                .setTestId(testId.toString())
                .build();

        return blockingStub.getQuestionPositions(request);
    }

    //Comment
    public CommentResponse postComment(UUID userId, PostCommentRequestDto dto) {
        PostCommentRequest request = PostCommentRequest.newBuilder()
                .setUserId(userId.toString())
                .setTestId(dto.getTestId().toString())
                .setContent(dto.getContent())
                .build();

        return blockingStub.postComment(request);
    }

    public CommentResponse replyComment(UUID userId, ReplyCommentRequestDto dto) {
        ReplyCommentRequest request = ReplyCommentRequest.newBuilder()
                .setUserId(userId.toString())
                .setParentId(dto.getParentId().toString())
                .setContent(dto.getContent())
                .build();

        return blockingStub.replyComment(request);
    }

    public CommentsResponse getComments(UUID testId, int page, int size) {
        GetCommentsRequest request = GetCommentsRequest.newBuilder()
                .setTestId(testId.toString())
                .setPage(page)
                .setSize(size)
                .build();

        return blockingStub.getComments(request);
    }
}
