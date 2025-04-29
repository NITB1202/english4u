package com.nitb.testservice.service;

import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.exceptions.NotFoundException;
import com.nitb.testservice.dto.TestStatisticDto;
import com.nitb.testservice.dto.TestStatisticProjection;
import com.nitb.testservice.entity.Test;
import com.nitb.testservice.grpc.*;
import com.nitb.testservice.repository.TestRepository;
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
public class TestService {
    private final TestRepository testRepository;
    private final int DEFAULT_SIZE = 10;

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

    public Test getTestById(GetTestByIdRequest request){
        UUID id = UUID.fromString(request.getId());
        return testRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Test not found.")
        );
    }

    public Page<Test> getTests(GetTestsRequest request){
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return testRepository.findByIsDeletedFalse(PageRequest.of(page, size));
    }

    public Page<Test> getDeletedTests(GetDeletedTestsRequest request){
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return testRepository.findByIsDeletedTrue(PageRequest.of(page, size));
    }

    public Page<Test> searchTestByName(SearchTestByNameRequest request){
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return testRepository.findByNameContainingIgnoreCaseAndIsDeletedFalse(request.getKeyword(), PageRequest.of(page, size));
    }

    public Test updateTest(UpdateTestRequest request){
        UUID id = UUID.fromString(request.getId());

        Test test = testRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Test not found.")
        );

        if(!request.getName().isEmpty()){
            test.setName(request.getName());
        }

        if(request.getMinutes() > 0){
            test.setMinutes(request.getMinutes());
        }

        if(!request.getTopic().isEmpty()){
            test.setTopic(request.getTopic());
        }

        test.setUpdatedBy(UUID.fromString(request.getUserId()));
        test.setUpdatedAt(LocalDateTime.now());

        return testRepository.save(test);
    }

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

    public Test restoreTest(RestoreTestRequest request){
        UUID id = UUID.fromString(request.getId());
        Test test = testRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Test not found.")
        );

        test.setIsDeleted(false);
        test.setUpdatedBy(UUID.fromString(request.getUserId()));
        test.setUpdatedAt(LocalDateTime.now());

        return testRepository.save(test);
    }

    public List<TestStatisticDto> getPublishedTestStatistics(GetPublishedTestStatisticsRequest request){
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
                .map(t -> new TestStatisticDto(t.getTime(), t.getTestCount(), t.getCompletedUsers()))
                .toList();
    }

    public void increasePartCount(UUID testId, UUID userId) {
        Test test = testRepository.findById(testId).orElseThrow(
                () -> new NotFoundException("Test not found.")
        );

        test.setPartCount(test.getPartCount() + 1);
        test.setUpdatedBy(userId);
        test.setUpdatedAt(LocalDateTime.now());
        testRepository.save(test);
    }

    public void updateLastModified(UUID testId, UUID userId) {
        Test test = testRepository.findById(testId).orElseThrow(
                () -> new NotFoundException("Test not found.")
        );

        test.setUpdatedBy(userId);
        test.setUpdatedAt(LocalDateTime.now());
        testRepository.save(test);
    }
}
