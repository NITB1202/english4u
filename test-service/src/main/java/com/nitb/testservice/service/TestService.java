package com.nitb.testservice.service;

import com.nitb.common.exceptions.NotFoundException;
import com.nitb.testservice.entity.Test;
import com.nitb.testservice.grpc.*;
import com.nitb.testservice.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;
    private final int DEFAULT_SIZE = 10;

    public Test createTest(CreateTestRequest request){
        if(testRepository.existsByNameAndIsDeletedFalse(request.getName())){
            throw new RuntimeException("Test already exists.");
        }

        UUID userId = UUID.fromString(request.getUserId());

        Test test = Test.builder()
                .createdBy(userId)
                .createdAt(LocalDateTime.now())
                .name(request.getName())
                .minutes(request.getMinutes())
                .topic(request.getTopic())
                .partCount(0)
                .questionCount(0)
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
}
