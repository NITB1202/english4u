package com.nitb.testservice.mapper;

import com.nitb.testservice.entity.Test;
import com.nitb.testservice.grpc.*;
import org.springframework.data.domain.Page;

import java.util.List;

public class TestMapper {
    private TestMapper() {}

    public static CreateTestResponse toCreateTestResponse(Test test) {
        return CreateTestResponse.newBuilder()
                .setId(test.getId().toString())
                .setCreatedBy(test.getCreatedBy().toString())
                .setCreateAt(test.getCreatedAt().toString())
                .setName(test.getName())
                .setMinutes(test.getMinutes())
                .setTopic(test.getTopic())
                .build();
    }

    public static TestDetailResponse toTestDetailResponse(Test test, int questionCount) {
        return TestDetailResponse.newBuilder()
                .setId(test.getId().toString())
                .setCreatedBy(test.getCreatedBy().toString())
                .setCreateAt(test.getCreatedAt().toString())
                .setName(test.getName())
                .setMinutes(test.getMinutes())
                .setTopic(test.getTopic())
                .setPartCount(test.getPartCount())
                .setQuestionCount(questionCount)
                .setCompletedUsers(test.getCompletedUsers())
                .setUpdatedBy(test.getUpdatedBy().toString())
                .setUpdateAt(test.getUpdatedAt().toString())
                .setIsDeleted(test.getIsDeleted())
                .build();
    }

    public static TestSummaryResponse toTestSummaryResponse(Test test, int questionCount) {
        return TestSummaryResponse.newBuilder()
                .setId(test.getId().toString())
                .setName(test.getName())
                .setMinutes(test.getMinutes())
                .setTopic(test.getTopic())
                .setQuestionCount(questionCount)
                .build();
    }

    public static TestsPaginationResponse toTestsPaginationResponse(Page<Test> tests, List<TestSummaryResponse> summaryResponses) {
        return TestsPaginationResponse.newBuilder()
                .addAllTests(summaryResponses)
                .setTotalItems(tests.getTotalElements())
                .setTotalPages(tests.getTotalPages())
                .build();
    }

    public static UpdateTestResponse toUpdateTestResponse(Test test) {
        return UpdateTestResponse.newBuilder()
                .setId(test.getId().toString())
                .setUpdatedBy(test.getUpdatedBy().toString())
                .setUpdateAt(test.getUpdatedAt().toString())
                .setName(test.getName())
                .setMinutes(test.getMinutes())
                .setTopic(test.getTopic())
                .build();
    }

    public static DeleteTestResponse toDeleteTestResponse(Test test) {
        return DeleteTestResponse.newBuilder()
                .setId(test.getId().toString())
                .setUpdatedBy(test.getUpdatedBy().toString())
                .setUpdateAt(test.getUpdatedAt().toString())
                .setIsDeleted(test.getIsDeleted())
                .build();
    }
}
