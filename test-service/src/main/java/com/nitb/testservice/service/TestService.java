package com.nitb.testservice.service;

import com.nitb.testservice.dto.TestStatisticDto;
import com.nitb.testservice.entity.Test;
import com.nitb.testservice.grpc.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface TestService {
    Test createTest(CreateTestRequest request);
    Test getTestById(GetTestByIdRequest request);
    String getTestNameById(GetTestByIdRequest request);
    Page<Test> getTests(GetTestsRequest request);
    Page<Test> getDeletedTests(GetDeletedTestsRequest request);
    Page<Test> searchTestByName(SearchTestByNameRequest request);
    Page<Test> searchDeletedTestByName(SearchDeletedTestByNameRequest request);
    void validateUpdateTest(ValidateUpdateTestRequest request);
    Test updateTestNameAndTopic(UpdateTestNameAndTopicRequest request);
    Test updateTest(UpdateTestRequest request);
    Test deleteTest(DeleteTestRequest request);
    Test restoreTest(RestoreTestRequest request);
    List<TestStatisticDto> getPublishedTestStatistics(GetPublishedTestStatisticsRequest request);

    void updatePartCount(UUID testId, int count);
    void validateTestId(String testIdStr);
}
