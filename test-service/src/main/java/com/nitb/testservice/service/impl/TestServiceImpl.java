package com.nitb.testservice.service.impl;

import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.exceptions.NotFoundException;
import com.nitb.testservice.dto.TestStatisticProjection;
import com.nitb.testservice.entity.Test;
import com.nitb.testservice.grpc.*;
import com.nitb.testservice.mapper.TestMapper;
import com.nitb.testservice.repository.TestRepository;
import com.nitb.testservice.service.TestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;
    private final int DEFAULT_SIZE = 10;
    private final int MAX_VERSION = 5;

    @Override
    @Transactional
    public Test createTest(CreateTestRequest request){
        if(testRepository.existsByNameAndIsDeletedFalse(request.getName())){
            throw new BusinessException("Test already exists.");
        }

        if(request.getMinutes() <= 0){
            throw new BusinessException("Minutes must be greater than 0.");
        }

        UUID userId = UUID.fromString(request.getUserId());

        Test test = Test.builder()
                .createdBy(userId)
                .createdAt(LocalDateTime.now())
                .name(request.getName())
                .version(1)
                .minutes(request.getMinutes())
                .topic(request.getTopic())
                .partCount(0)
                .completedUsers(0L)
                .updatedBy(userId)
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        return testRepository.save(test);
    }

    @Override
    public Test getTestById(GetTestByIdRequest request){
        UUID id = UUID.fromString(request.getId());
        return testRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Test not found.")
        );
    }

    @Override
    public String getTestNameById(GetTestByIdRequest request) {
        Test test = testRepository.findById(UUID.fromString(request.getId())).orElseThrow(
                () -> new NotFoundException("Test not found.")
        );

        return test.getName();
    }

    @Override
    public Page<Test> getTests(GetTestsRequest request){
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return testRepository.findByIsDeletedFalse(PageRequest.of(page, size));
    }

    @Override
    public Page<Test> getDeletedTests(GetDeletedTestsRequest request){
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return testRepository.findByIsDeletedTrue(PageRequest.of(page, size));
    }

    @Override
    public Page<Test> searchTestByName(SearchTestByNameRequest request){
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return testRepository.findByNameContainingIgnoreCaseAndIsDeletedFalse(request.getKeyword(), PageRequest.of(page, size));
    }

    @Override
    public Page<Test> searchDeletedTestByName(SearchDeletedTestByNameRequest request){
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return testRepository.findByNameContainingIgnoreCaseAndIsDeletedTrue(request.getKeyword(), PageRequest.of(page, size));
    }

    @Override
    public void validateUpdateTest(ValidateUpdateTestRequest request) {
        UUID id = UUID.fromString(request.getId());
        Test test = testRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Test not found.")
        );

        if(testRepository.countByName(test.getName()) >= MAX_VERSION){
            throw new BusinessException("This test has already reached its maximum version.");
        }
    }

    @Override
    public Test updateTestNameAndTopic(UpdateTestNameAndTopicRequest request) {
        UUID id = UUID.fromString(request.getId());

        Test test = testRepository.findById(id).orElseThrow(
                ()-> new NotFoundException("Test not found.")
        );

        if(!request.getName().isEmpty() && !testRepository.existsByNameAndIsDeletedFalse(request.getName())){
            test.setName(request.getName());
            test.setVersion(1);
        }

        if(!request.getTopic().isEmpty()){
            test.setTopic(request.getTopic());
        }

        return testRepository.save(test);
    }

    @Override
    public Test updateTest(UpdateTestRequest request){
        UUID oldId = UUID.fromString(request.getOldId());
        UUID newId = UUID.fromString(request.getNewId());

        Test oldTest = testRepository.findById(oldId).orElseThrow(
                () -> new NotFoundException("Test with id " + oldId + " not found.")
        );

        Test newTest = testRepository.findById(newId).orElseThrow(
                () -> new NotFoundException("Test with id " + newId + " not found.")
        );

        int latestVersion = testRepository.findMaxVersion(oldTest.getName());

        newTest.setVersion(latestVersion + 1);
        newTest.setCreatedBy(oldTest.getCreatedBy());
        newTest.setCreatedAt(oldTest.getCreatedAt());

        return testRepository.save(newTest);
    }

    @Override
    public Test deleteTest(DeleteTestRequest request){
        UUID id = UUID.fromString(request.getId());
        Test test = testRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Test not found.")
        );

        test.setIsDeleted(true);
        test.setUpdatedBy(UUID.fromString(request.getUserId()));
        test.setUpdatedAt(LocalDateTime.now());

        return testRepository.save(test);
    }

    @Override
    public Test restoreTest(RestoreTestRequest request){
        UUID id = UUID.fromString(request.getId());
        UUID userId = UUID.fromString(request.getUserId());
        LocalDateTime now = LocalDateTime.now();

        Test test = testRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Test not found.")
        );

        //If the current version of this test exists, delete it.
        Test currentVersion = testRepository.findByNameAndIsDeletedFalse(test.getName());
        if(currentVersion != null) {
            currentVersion.setUpdatedBy(userId);
            currentVersion.setUpdatedAt(now);
            currentVersion.setIsDeleted(true);
            testRepository.save(currentVersion);
        }

        test.setUpdatedBy(userId);
        test.setUpdatedAt(now);
        test.setIsDeleted(false);

        return testRepository.save(test);
    }

    @Override
    public List<TestStatisticResponse> getPublishedTestStatistics(GetPublishedTestStatisticsRequest request){
        UUID userId = UUID.fromString(request.getUserId());
        LocalDateTime from = LocalDate.parse(request.getFrom()).atStartOfDay();
        LocalDateTime to = LocalDate.parse(request.getTo()).atTime(23, 59, 59);

        List<TestStatisticProjection> result = new ArrayList<>();

        switch(request.getGroupBy()) {
            case WEEK -> result.addAll(testRepository.getStatsByWeek(userId, from, to));
            case MONTH -> result.addAll(testRepository.getStatsByMonth(userId, from, to));
            case YEAR -> result.addAll(testRepository.getStatsByYear(userId, from, to));
            default -> throw new BusinessException("Invalid group by.");
        }

        return result.stream()
                .map(t -> TestMapper.toTestStatisticResponse(t.getTime(), t.getTestCount(), t.getCompletedUsers()))
                .toList();
    }

    @Override
    public List<AdminTestStatisticResponse> getAdminTestStatistics(GetAdminTestStatisticsRequest request) {
        List<AdminTestStatisticResponse> result = new ArrayList<>();

        for(String idStr : request.getUserIdList()) {
            UUID userId = UUID.fromString(idStr);
            long totalPublishedTests = testRepository.countByCreatedByAndIsDeletedFalse(userId);
            AdminTestStatisticResponse statistic = TestMapper.toAdminTestStatisticResponse(userId, totalPublishedTests);
            result.add(statistic);
        }

        return result;
    }

    @Override
    public void updatePartCount(UUID testId, int count) {
        Test test = testRepository.findById(testId).orElseThrow(
                ()-> new NotFoundException("Test not found.")
        );

        test.setPartCount(count);
        testRepository.save(test);
    }

    @Override
    public void validateTestId(String testIdStr) {
        UUID testId = UUID.fromString(testIdStr);

        if(!testRepository.existsById(testId)) {
            throw new NotFoundException("Test not found.");
        }
    }
}
