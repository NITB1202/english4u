package com.nitb.testservice.service;

import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.exceptions.NotFoundException;
import com.nitb.common.grpc.GroupBy;
import com.nitb.testservice.dto.TestStatisticProjection;
import com.nitb.testservice.entity.Test;
import com.nitb.testservice.grpc.*;
import com.nitb.testservice.mapper.TestMapper;
import com.nitb.testservice.repository.TestRepository;
import com.nitb.testservice.service.impl.TestServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    @Mock
    private TestRepository testRepository;

    @InjectMocks
    private TestServiceImpl testService;

    static class TestStatisticProjectionImpl implements TestStatisticProjection {
        private final String time;
        private final Integer testCount;
        private final Long completedUsers;

        TestStatisticProjectionImpl(String time, Integer testCount, Long completedUsers) {
            this.time = time;
            this.testCount = testCount;
            this.completedUsers = completedUsers;
        }

        @Override public String getTime() { return time; }
        @Override public Integer getTestCount() { return testCount; }
        @Override public Long getCompletedUsers() { return completedUsers; }
    }

    @org.junit.jupiter.api.Test
    void createTest_success() {
        // Given
        CreateTestRequest request = CreateTestRequest.newBuilder()
                .setUserId(UUID.randomUUID().toString())
                .setName("Test 1")
                .setMinutes(30)
                .setTopic("Topic A")
                .build();

        when(testRepository.existsByNameAndIsDeletedFalse(request.getName())).thenReturn(false);

        // Giả lập repository save trả về đối tượng Test đã set id...
        when(testRepository.save(any(Test.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Test result = testService.createTest(request);

        // Then
        assertNotNull(result);
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getMinutes(), result.getMinutes());
        assertEquals(request.getTopic(), result.getTopic());
        assertEquals(1, result.getVersion());
        assertFalse(result.getIsDeleted());
        assertEquals(UUID.fromString(request.getUserId()), result.getCreatedBy());
        verify(testRepository).existsByNameAndIsDeletedFalse(request.getName());
        verify(testRepository).save(any(Test.class));
    }

    @org.junit.jupiter.api.Test
    void createTest_throwsBusinessException_whenNameExists() {
        // Given
        CreateTestRequest request = CreateTestRequest.newBuilder()
                .setUserId(UUID.randomUUID().toString())
                .setName("DuplicateTest")
                .setMinutes(15)
                .setTopic("Topic A")
                .build();

        when(testRepository.existsByNameAndIsDeletedFalse(request.getName())).thenReturn(true);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> testService.createTest(request));

        assertEquals("Test already exists.", exception.getMessage());
        verify(testRepository).existsByNameAndIsDeletedFalse(request.getName());
        verify(testRepository, never()).save(any());
    }

    @org.junit.jupiter.api.Test
    void createTest_throwsBusinessException_whenMinutesInvalid() {
        // Given
        CreateTestRequest request = CreateTestRequest.newBuilder()
                .setUserId(UUID.randomUUID().toString())
                .setName("Test Minutes Invalid")
                .setMinutes(0)
                .setTopic("Topic A")
                .build();

        when(testRepository.existsByNameAndIsDeletedFalse(request.getName())).thenReturn(false);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> testService.createTest(request));

        assertEquals("Minutes must be greater than 0.", exception.getMessage());
        verify(testRepository).existsByNameAndIsDeletedFalse(request.getName());
        verify(testRepository, never()).save(any());
    }

    @org.junit.jupiter.api.Test
    void getTestById_success() {
        // Given
        UUID id = UUID.randomUUID();
        GetTestByIdRequest request = GetTestByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        Test expectedTest = Test.builder()
                .id(id)
                .name("Sample Test")
                .build();

        when(testRepository.findById(id)).thenReturn(Optional.of(expectedTest));

        // When
        Test actualTest = testService.getTestById(request);

        // Then
        assertNotNull(actualTest);
        assertEquals(expectedTest.getId(), actualTest.getId());
        assertEquals(expectedTest.getName(), actualTest.getName());
        verify(testRepository).findById(id);
    }

    @org.junit.jupiter.api.Test
    void getTestById_throwsNotFoundException() {
        // Given
        UUID id = UUID.randomUUID();
        GetTestByIdRequest request = GetTestByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        when(testRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> testService.getTestById(request));
        assertEquals("Test not found.", ex.getMessage());

        verify(testRepository).findById(id);
    }

    @org.junit.jupiter.api.Test
    void getTestNameById_success() {
        // Given
        UUID id = UUID.randomUUID();
        GetTestByIdRequest request = GetTestByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        Test test = Test.builder()
                .id(id)
                .name("Test Name")
                .build();

        when(testRepository.findById(id)).thenReturn(Optional.of(test));

        // When
        String testName = testService.getTestNameById(request);

        // Then
        assertEquals("Test Name", testName);
        verify(testRepository).findById(id);
    }

    @org.junit.jupiter.api.Test
    void getTestNameById_throwsNotFoundException() {
        // Given
        UUID id = UUID.randomUUID();
        GetTestByIdRequest request = GetTestByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        when(testRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> testService.getTestNameById(request));
        assertEquals("Test not found.", ex.getMessage());

        verify(testRepository).findById(id);
    }

    @org.junit.jupiter.api.Test
    void getTests_returnsPageOfTests() {
        // Given
        GetTestsRequest request = GetTestsRequest.newBuilder()
                .setPage(2)
                .setSize(5)
                .build();

        int expectedPage = 1; // vì page = request.getPage() - 1
        int expectedSize = 5;

        List<Test> tests = List.of(
                Test.builder().id(UUID.randomUUID()).name("Test1").build(),
                Test.builder().id(UUID.randomUUID()).name("Test2").build()
        );
        Page<Test> pageResult = new PageImpl<>(tests);

        when(testRepository.findByIsDeletedFalse(PageRequest.of(expectedPage, expectedSize)))
                .thenReturn(pageResult);

        // When
        Page<Test> result = testService.getTests(request);

        // Then
        assertNotNull(result);
        assertEquals(tests.size(), result.getContent().size());
        verify(testRepository).findByIsDeletedFalse(PageRequest.of(expectedPage, expectedSize));
    }

    @org.junit.jupiter.api.Test
    void getTests_pageOrSizeInvalid_defaultsUsed() {
        // Given
        GetTestsRequest request = GetTestsRequest.newBuilder()
                .setPage(0)  // invalid page, default to 0
                .setSize(0)  // invalid size, default to DEFAULT_SIZE (10)
                .build();

        int expectedPage = 0;
        int expectedSize = 10;

        Page<Test> pageResult = Page.empty();

        when(testRepository.findByIsDeletedFalse(PageRequest.of(expectedPage, expectedSize)))
                .thenReturn(pageResult);

        // When
        Page<Test> result = testService.getTests(request);

        // Then
        assertNotNull(result);
        verify(testRepository).findByIsDeletedFalse(PageRequest.of(expectedPage, expectedSize));
    }

    @org.junit.jupiter.api.Test
    void getDeletedTests_returnsPageOfDeletedTests() {
        // Given
        GetDeletedTestsRequest request = GetDeletedTestsRequest.newBuilder()
                .setPage(3)
                .setSize(2)
                .build();

        int expectedPage = 2; // page - 1
        int expectedSize = 2;

        List<Test> deletedTests = List.of(
                Test.builder().id(UUID.randomUUID()).name("Deleted Test 1").build()
        );
        Page<Test> pageResult = new PageImpl<>(deletedTests);

        when(testRepository.findByIsDeletedTrue(PageRequest.of(expectedPage, expectedSize)))
                .thenReturn(pageResult);

        // When
        Page<Test> result = testService.getDeletedTests(request);

        // Then
        assertNotNull(result);
        assertEquals(deletedTests.size(), result.getContent().size());
        verify(testRepository).findByIsDeletedTrue(PageRequest.of(expectedPage, expectedSize));
    }

    @org.junit.jupiter.api.Test
    void getDeletedTests_pageOrSizeInvalid_defaultsUsed() {
        // Given
        GetDeletedTestsRequest request = GetDeletedTestsRequest.newBuilder()
                .setPage(-1)  // invalid page
                .setSize(-5)  // invalid size
                .build();

        int expectedPage = 0;
        int expectedSize = 10;

        Page<Test> pageResult = Page.empty();

        when(testRepository.findByIsDeletedTrue(PageRequest.of(expectedPage, expectedSize)))
                .thenReturn(pageResult);

        // When
        Page<Test> result = testService.getDeletedTests(request);

        // Then
        assertNotNull(result);
        verify(testRepository).findByIsDeletedTrue(PageRequest.of(expectedPage, expectedSize));
    }

    @org.junit.jupiter.api.Test
    void searchTestByName_returnsPageOfTests() {
        // Given
        SearchTestByNameRequest request = SearchTestByNameRequest.newBuilder()
                .setPage(2)
                .setSize(4)
                .setKeyword("sample")
                .build();

        int expectedPage = 1; // 2 - 1
        int expectedSize = 4;

        List<Test> tests = List.of(
                Test.builder().id(UUID.randomUUID()).name("Sample Test 1").build(),
                Test.builder().id(UUID.randomUUID()).name("Sample Test 2").build()
        );
        Page<Test> pageResult = new PageImpl<>(tests);

        when(testRepository.findByNameContainingIgnoreCaseAndIsDeletedFalse("sample", PageRequest.of(expectedPage, expectedSize)))
                .thenReturn(pageResult);

        // When
        Page<Test> result = testService.searchTestByName(request);

        // Then
        assertNotNull(result);
        assertEquals(tests.size(), result.getContent().size());
        verify(testRepository).findByNameContainingIgnoreCaseAndIsDeletedFalse("sample", PageRequest.of(expectedPage, expectedSize));
    }

    @org.junit.jupiter.api.Test
    void searchTestByName_pageOrSizeInvalid_defaultsUsed() {
        // Given
        SearchTestByNameRequest request = SearchTestByNameRequest.newBuilder()
                .setPage(0)
                .setSize(0)
                .setKeyword("test")
                .build();

        int expectedPage = 0;
        int expectedSize = 10; // DEFAULT_SIZE

        Page<Test> pageResult = Page.empty();

        when(testRepository.findByNameContainingIgnoreCaseAndIsDeletedFalse("test", PageRequest.of(expectedPage, expectedSize)))
                .thenReturn(pageResult);

        // When
        Page<Test> result = testService.searchTestByName(request);

        // Then
        assertNotNull(result);
        verify(testRepository).findByNameContainingIgnoreCaseAndIsDeletedFalse("test", PageRequest.of(expectedPage, expectedSize));
    }

    @org.junit.jupiter.api.Test
    void searchDeletedTestByName_returnsPageOfDeletedTests() {
        // Given
        SearchDeletedTestByNameRequest request = SearchDeletedTestByNameRequest.newBuilder()
                .setPage(3)
                .setSize(3)
                .setKeyword("deleted")
                .build();

        int expectedPage = 2;
        int expectedSize = 3;

        List<Test> deletedTests = List.of(
                Test.builder().id(UUID.randomUUID()).name("Deleted Test 1").build()
        );
        Page<Test> pageResult = new PageImpl<>(deletedTests);

        when(testRepository.findByNameContainingIgnoreCaseAndIsDeletedTrue("deleted", PageRequest.of(expectedPage, expectedSize)))
                .thenReturn(pageResult);

        // When
        Page<Test> result = testService.searchDeletedTestByName(request);

        // Then
        assertNotNull(result);
        assertEquals(deletedTests.size(), result.getContent().size());
        verify(testRepository).findByNameContainingIgnoreCaseAndIsDeletedTrue("deleted", PageRequest.of(expectedPage, expectedSize));
    }

    @org.junit.jupiter.api.Test
    void searchDeletedTestByName_pageOrSizeInvalid_defaultsUsed() {
        // Given
        SearchDeletedTestByNameRequest request = SearchDeletedTestByNameRequest.newBuilder()
                .setPage(-1)
                .setSize(-10)
                .setKeyword("deleted")
                .build();

        int expectedPage = 0;
        int expectedSize = 10;

        Page<Test> pageResult = Page.empty();

        when(testRepository.findByNameContainingIgnoreCaseAndIsDeletedTrue("deleted", PageRequest.of(expectedPage, expectedSize)))
                .thenReturn(pageResult);

        // When
        Page<Test> result = testService.searchDeletedTestByName(request);

        // Then
        assertNotNull(result);
        verify(testRepository).findByNameContainingIgnoreCaseAndIsDeletedTrue("deleted", PageRequest.of(expectedPage, expectedSize));
    }

    @org.junit.jupiter.api.Test
    void validateUpdateTest_success_whenVersionBelowMax() {
        // Given
        UUID testId = UUID.randomUUID();
        ValidateUpdateTestRequest request = ValidateUpdateTestRequest.newBuilder()
                .setId(testId.toString())
                .build();

        Test existingTest = Test.builder()
                .id(testId)
                .name("TestName")
                .build();

        when(testRepository.findById(testId)).thenReturn(Optional.of(existingTest));
        when(testRepository.countByName(existingTest.getName())).thenReturn(3); // dưới max version

        // When & Then
        assertDoesNotThrow(() -> testService.validateUpdateTest(request));

        verify(testRepository).findById(testId);
        verify(testRepository).countByName(existingTest.getName());
    }

    @org.junit.jupiter.api.Test
    void validateUpdateTest_throwsNotFoundException_whenTestNotFound() {
        // Given
        UUID testId = UUID.randomUUID();
        ValidateUpdateTestRequest request = ValidateUpdateTestRequest.newBuilder()
                .setId(testId.toString())
                .build();

        when(testRepository.findById(testId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException ex = assertThrows(NotFoundException.class, () -> testService.validateUpdateTest(request));
        assertEquals("Test not found.", ex.getMessage());

        verify(testRepository).findById(testId);
        verify(testRepository, never()).countByName(anyString());
    }

    @org.junit.jupiter.api.Test
    void validateUpdateTest_throwsBusinessException_whenVersionAtMax() {
        // Given
        UUID testId = UUID.randomUUID();
        ValidateUpdateTestRequest request = ValidateUpdateTestRequest.newBuilder()
                .setId(testId.toString())
                .build();

        Test existingTest = Test.builder()
                .id(testId)
                .name("TestName")
                .build();

        when(testRepository.findById(testId)).thenReturn(Optional.of(existingTest));
        when(testRepository.countByName(existingTest.getName())).thenReturn(5); // đúng max version

        // When & Then
        BusinessException ex = assertThrows(BusinessException.class, () -> testService.validateUpdateTest(request));
        assertEquals("This test has already reached its maximum version.", ex.getMessage());

        verify(testRepository).findById(testId);
        verify(testRepository).countByName(existingTest.getName());
    }

    @org.junit.jupiter.api.Test
    void updateTestNameAndTopic_success_updateNameAndTopic() {
        // Given
        UUID testId = UUID.randomUUID();
        UpdateTestNameAndTopicRequest request = UpdateTestNameAndTopicRequest.newBuilder()
                .setId(testId.toString())
                .setName("New Test Name")
                .setTopic("New Topic")
                .build();

        Test existingTest = Test.builder()
                .id(testId)
                .name("Old Name")
                .topic("Old Topic")
                .version(2)
                .build();

        when(testRepository.findById(testId)).thenReturn(Optional.of(existingTest));
        when(testRepository.existsByNameAndIsDeletedFalse(request.getName())).thenReturn(false);
        when(testRepository.save(any(Test.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Test updatedTest = testService.updateTestNameAndTopic(request);

        // Then
        assertNotNull(updatedTest);
        assertEquals("New Test Name", updatedTest.getName());
        assertEquals(1, updatedTest.getVersion());  // version reset
        assertEquals("New Topic", updatedTest.getTopic());

        verify(testRepository).findById(testId);
        verify(testRepository).existsByNameAndIsDeletedFalse(request.getName());
        verify(testRepository).save(existingTest);
    }

    @org.junit.jupiter.api.Test
    void updateTestNameAndTopic_success_updateTopicOnly() {
        // Given
        UUID testId = UUID.randomUUID();
        UpdateTestNameAndTopicRequest request = UpdateTestNameAndTopicRequest.newBuilder()
                .setId(testId.toString())
                .setName("")
                .setTopic("Updated Topic")
                .build();

        Test existingTest = Test.builder()
                .id(testId)
                .name("Current Name")
                .topic("Old Topic")
                .version(3)
                .build();

        when(testRepository.findById(testId)).thenReturn(Optional.of(existingTest));
        when(testRepository.save(any(Test.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Test updatedTest = testService.updateTestNameAndTopic(request);

        // Then
        assertNotNull(updatedTest);
        assertEquals("Current Name", updatedTest.getName());
        assertEquals(3, updatedTest.getVersion());
        assertEquals("Updated Topic", updatedTest.getTopic());

        verify(testRepository).findById(testId);
        verify(testRepository, never()).existsByNameAndIsDeletedFalse(anyString());
        verify(testRepository).save(existingTest);
    }

    @org.junit.jupiter.api.Test
    void updateTestNameAndTopic_noChange_whenNameExists() {
        // Given
        UUID testId = UUID.randomUUID();
        UpdateTestNameAndTopicRequest request = UpdateTestNameAndTopicRequest.newBuilder()
                .setId(testId.toString())
                .setName("Existing Name")
                .setTopic("Updated Topic")
                .build();

        Test existingTest = Test.builder()
                .id(testId)
                .name("Old Name")
                .topic("Old Topic")
                .version(2)
                .build();

        when(testRepository.findById(testId)).thenReturn(Optional.of(existingTest));
        when(testRepository.existsByNameAndIsDeletedFalse(request.getName())).thenReturn(true);
        when(testRepository.save(any(Test.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Test updatedTest = testService.updateTestNameAndTopic(request);

        // Then
        assertNotNull(updatedTest);
        assertEquals("Old Name", updatedTest.getName());
        assertEquals(2, updatedTest.getVersion());
        assertEquals("Updated Topic", updatedTest.getTopic());

        verify(testRepository).findById(testId);
        verify(testRepository).existsByNameAndIsDeletedFalse(request.getName());
        verify(testRepository).save(existingTest);
    }

    @org.junit.jupiter.api.Test
    void updateTestNameAndTopic_throwsNotFoundException_whenTestNotFound() {
        // Given
        UUID testId = UUID.randomUUID();
        UpdateTestNameAndTopicRequest request = UpdateTestNameAndTopicRequest.newBuilder()
                .setId(testId.toString())
                .setName("Any Name")
                .setTopic("Any Topic")
                .build();

        when(testRepository.findById(testId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException ex = assertThrows(NotFoundException.class, () -> testService.updateTestNameAndTopic(request));
        assertEquals("Test not found.", ex.getMessage());

        verify(testRepository).findById(testId);
        verify(testRepository, never()).existsByNameAndIsDeletedFalse(anyString());
        verify(testRepository, never()).save(any(Test.class));
    }

    @org.junit.jupiter.api.Test
    void updateTest_success() {
        // Given
        UUID oldId = UUID.randomUUID();
        UUID newId = UUID.randomUUID();

        UpdateTestRequest request = UpdateTestRequest.newBuilder()
                .setOldId(oldId.toString())
                .setNewId(newId.toString())
                .build();

        Test oldTest = Test.builder()
                .id(oldId)
                .name("Old Test")
                .version(2)
                .createdBy(UUID.randomUUID())
                .createdAt(LocalDateTime.now().minusDays(10))
                .build();

        Test newTest = Test.builder()
                .id(newId)
                .name("New Test")
                .version(1)
                .build();

        when(testRepository.findById(oldId)).thenReturn(Optional.of(oldTest));
        when(testRepository.findById(newId)).thenReturn(Optional.of(newTest));
        when(testRepository.findMaxVersion(oldTest.getName())).thenReturn(3);
        when(testRepository.save(any(Test.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Test result = testService.updateTest(request);

        // Then
        assertNotNull(result);
        assertEquals(4, result.getVersion());  // latestVersion + 1 = 3 + 1 = 4
        assertEquals(oldTest.getCreatedBy(), result.getCreatedBy());
        assertEquals(oldTest.getCreatedAt(), result.getCreatedAt());
        assertEquals(newTest.getId(), result.getId());

        verify(testRepository).findById(oldId);
        verify(testRepository).findById(newId);
        verify(testRepository).findMaxVersion(oldTest.getName());
        verify(testRepository).save(any(Test.class));
    }

    @org.junit.jupiter.api.Test
    void updateTest_throwsNotFoundException_whenOldTestNotFound() {
        UUID oldId = UUID.randomUUID();
        UUID newId = UUID.randomUUID();

        UpdateTestRequest request = UpdateTestRequest.newBuilder()
                .setOldId(oldId.toString())
                .setNewId(newId.toString())
                .build();

        when(testRepository.findById(oldId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> testService.updateTest(request));
        assertEquals("Test with id " + oldId + " not found.", ex.getMessage());

        verify(testRepository).findById(oldId);
        verify(testRepository, never()).findById(newId);
        verify(testRepository, never()).save(any());
    }

    @org.junit.jupiter.api.Test
    void updateTest_throwsNotFoundException_whenNewTestNotFound() {
        UUID oldId = UUID.randomUUID();
        UUID newId = UUID.randomUUID();

        UpdateTestRequest request = UpdateTestRequest.newBuilder()
                .setOldId(oldId.toString())
                .setNewId(newId.toString())
                .build();

        Test oldTest = Test.builder()
                .id(oldId)
                .name("Old Test")
                .version(2)
                .build();

        when(testRepository.findById(oldId)).thenReturn(Optional.of(oldTest));
        when(testRepository.findById(newId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> testService.updateTest(request));
        assertEquals("Test with id " + newId + " not found.", ex.getMessage());

        verify(testRepository).findById(oldId);
        verify(testRepository).findById(newId);
        verify(testRepository, never()).save(any());
    }

    @org.junit.jupiter.api.Test
    void deleteTest_success() {
        // Given
        UUID testId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        DeleteTestRequest request = DeleteTestRequest.newBuilder()
                .setId(testId.toString())
                .setUserId(userId.toString())
                .build();

        Test test = Test.builder()
                .id(testId)
                .isDeleted(false)
                .build();

        when(testRepository.findById(testId)).thenReturn(Optional.of(test));
        when(testRepository.save(any(Test.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Test result = testService.deleteTest(request);

        // Then
        assertNotNull(result);
        assertTrue(result.getIsDeleted());
        assertEquals(userId, result.getUpdatedBy());
        assertNotNull(result.getUpdatedAt());
        assertEquals(testId, result.getId());

        verify(testRepository).findById(testId);
        verify(testRepository).save(test);
    }

    @org.junit.jupiter.api.Test
    void deleteTest_throwsNotFoundException_whenTestNotFound() {
        // Given
        UUID testId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        DeleteTestRequest request = DeleteTestRequest.newBuilder()
                .setId(testId.toString())
                .setUserId(userId.toString())
                .build();

        when(testRepository.findById(testId)).thenReturn(Optional.empty());

        // When + Then
        NotFoundException ex = assertThrows(NotFoundException.class, () -> testService.deleteTest(request));
        assertEquals("Test not found.", ex.getMessage());

        verify(testRepository).findById(testId);
        verify(testRepository, never()).save(any());
    }

    @org.junit.jupiter.api.Test
    void restoreTest_success_withExistingCurrentVersion() {
        // Given
        UUID testId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String testName = "Vocabulary Test";

        RestoreTestRequest request = RestoreTestRequest.newBuilder()
                .setId(testId.toString())
                .setUserId(userId.toString())
                .build();

        Test deletedTest = Test.builder()
                .id(testId)
                .name(testName)
                .isDeleted(true)
                .build();

        Test currentVersion = Test.builder()
                .id(UUID.randomUUID())
                .name(testName)
                .isDeleted(false)
                .build();

        when(testRepository.findById(testId)).thenReturn(Optional.of(deletedTest));
        when(testRepository.findByNameAndIsDeletedFalse(testName)).thenReturn(currentVersion);
        when(testRepository.save(any(Test.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Test result = testService.restoreTest(request);

        // Then
        assertNotNull(result);
        assertFalse(result.getIsDeleted());
        assertEquals(userId, result.getUpdatedBy());
        assertNotNull(result.getUpdatedAt());

        // Verify current version was marked as deleted
        assertTrue(currentVersion.getIsDeleted());
        assertEquals(userId, currentVersion.getUpdatedBy());
        assertNotNull(currentVersion.getUpdatedAt());

        verify(testRepository).findById(testId);
        verify(testRepository).findByNameAndIsDeletedFalse(testName);
        verify(testRepository, times(2)).save(any(Test.class)); // currentVersion + restoredTest
    }

    @org.junit.jupiter.api.Test
    void restoreTest_success_withoutExistingCurrentVersion() {
        // Given
        UUID testId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String testName = "Grammar Test";

        RestoreTestRequest request = RestoreTestRequest.newBuilder()
                .setId(testId.toString())
                .setUserId(userId.toString())
                .build();

        Test deletedTest = Test.builder()
                .id(testId)
                .name(testName)
                .isDeleted(true)
                .build();

        when(testRepository.findById(testId)).thenReturn(Optional.of(deletedTest));
        when(testRepository.findByNameAndIsDeletedFalse(testName)).thenReturn(null);
        when(testRepository.save(any(Test.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Test result = testService.restoreTest(request);

        // Then
        assertNotNull(result);
        assertFalse(result.getIsDeleted());
        assertEquals(userId, result.getUpdatedBy());

        verify(testRepository).findById(testId);
        verify(testRepository).findByNameAndIsDeletedFalse(testName);
        verify(testRepository).save(deletedTest);
    }

    @org.junit.jupiter.api.Test
    void restoreTest_throwsNotFoundException_whenTestNotFound() {
        // Given
        UUID testId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        RestoreTestRequest request = RestoreTestRequest.newBuilder()
                .setId(testId.toString())
                .setUserId(userId.toString())
                .build();

        when(testRepository.findById(testId)).thenReturn(Optional.empty());

        // When + Then
        NotFoundException ex = assertThrows(NotFoundException.class, () -> testService.restoreTest(request));
        assertEquals("Test not found.", ex.getMessage());

        verify(testRepository).findById(testId);
        verify(testRepository, never()).findByNameAndIsDeletedFalse(anyString());
        verify(testRepository, never()).save(any());
    }

    @org.junit.jupiter.api.Test
    void getPublishedTestStatistics_success_weekGroup() {
        // Given
        UUID userId = UUID.randomUUID();
        String from = "2024-06-01";
        String to = "2024-06-30";

        GetPublishedTestStatisticsRequest request = GetPublishedTestStatisticsRequest.newBuilder()
                .setUserId(userId.toString())
                .setFrom(from)
                .setTo(to)
                .setGroupBy(GroupBy.WEEK)
                .build();

        TestStatisticProjection projection = new TestStatisticProjectionImpl("2024-W23", 5, 10L);

        when(testRepository.getStatsByWeek(eq(userId), any(), any()))
                .thenReturn(List.of(projection));

        // When
        List<TestStatisticResponse> result = testService.getPublishedTestStatistics(request);

        // Then
        assertEquals(1, result.size());
        TestStatisticResponse response = result.get(0);
        assertEquals("2024-W23", response.getTime());
        assertEquals(5L, response.getTestCount());
        assertEquals(10L, response.getCompletedUsers());

        verify(testRepository).getStatsByWeek(eq(userId), any(), any());
    }

    @org.junit.jupiter.api.Test
    void getPublishedTestStatistics_success_monthGroup() {
        UUID userId = UUID.randomUUID();

        GetPublishedTestStatisticsRequest request = GetPublishedTestStatisticsRequest.newBuilder()
                .setUserId(userId.toString())
                .setFrom("2024-01-01")
                .setTo("2024-12-31")
                .setGroupBy(GroupBy.MONTH)
                .build();

        when(testRepository.getStatsByMonth(any(), any(), any()))
                .thenReturn(List.of(new TestStatisticProjectionImpl("2024-01", 3, 8L)));

        List<TestStatisticResponse> result = testService.getPublishedTestStatistics(request);

        assertEquals(1, result.size());
        assertEquals("2024-01", result.get(0).getTime());
        assertEquals(3L, result.get(0).getTestCount());
        assertEquals(8L, result.get(0).getCompletedUsers());

        verify(testRepository).getStatsByMonth(any(), any(), any());
    }

    @org.junit.jupiter.api.Test
    void getPublishedTestStatistics_success_yearGroup() {
        UUID userId = UUID.randomUUID();

        GetPublishedTestStatisticsRequest request = GetPublishedTestStatisticsRequest.newBuilder()
                .setUserId(userId.toString())
                .setFrom("2022-01-01")
                .setTo("2024-12-31")
                .setGroupBy(GroupBy.YEAR)
                .build();

        when(testRepository.getStatsByYear(any(), any(), any()))
                .thenReturn(List.of(new TestStatisticProjectionImpl("2023", 12, 25L)));

        List<TestStatisticResponse> result = testService.getPublishedTestStatistics(request);

        assertEquals(1, result.size());
        assertEquals("2023", result.get(0).getTime());
        assertEquals(12L, result.get(0).getTestCount());
        assertEquals(25L, result.get(0).getCompletedUsers());

        verify(testRepository).getStatsByYear(any(), any(), any());
    }

    @org.junit.jupiter.api.Test
    void getPublishedTestStatistics_invalidGroupBy_shouldThrowException() {
        // Given
        GetPublishedTestStatisticsRequest request = GetPublishedTestStatisticsRequest.newBuilder()
                .setUserId(UUID.randomUUID().toString())
                .setFrom("2024-01-01")
                .setTo("2024-12-31")
                .setGroupBy(GroupBy.GROUP_BY_UNSPECIFIED)
                .build();

        // When + Then
        BusinessException ex = assertThrows(BusinessException.class,
                () -> testService.getPublishedTestStatistics(request));
        assertEquals("Invalid group by.", ex.getMessage());

        verify(testRepository, never()).getStatsByWeek(any(), any(), any());
        verify(testRepository, never()).getStatsByMonth(any(), any(), any());
        verify(testRepository, never()).getStatsByYear(any(), any(), any());
    }

    @org.junit.jupiter.api.Test
    void getAdminTestStatistics_success() {
        // Given
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();

        List<String> userIdList = List.of(userId1.toString(), userId2.toString());

        GetAdminTestStatisticsRequest request = GetAdminTestStatisticsRequest.newBuilder()
                .addAllUserId(userIdList)
                .build();

        when(testRepository.countByCreatedByAndIsDeletedFalse(userId1)).thenReturn(3L);
        when(testRepository.countByCreatedByAndIsDeletedFalse(userId2)).thenReturn(5L);

        // Mock static method in TestMapper using Mockito (optional, or assert directly)
        AdminTestStatisticResponse res1 = AdminTestStatisticResponse.newBuilder()
                .setUserId(userId1.toString())
                .setTotalPublishedTests(3L)
                .build();
        AdminTestStatisticResponse res2 = AdminTestStatisticResponse.newBuilder()
                .setUserId(userId2.toString())
                .setTotalPublishedTests(5L)
                .build();

        try (var mockedMapper = mockStatic(TestMapper.class)) {
            mockedMapper.when(() -> TestMapper.toAdminTestStatisticResponse(userId1, 3L)).thenReturn(res1);
            mockedMapper.when(() -> TestMapper.toAdminTestStatisticResponse(userId2, 5L)).thenReturn(res2);

            // When
            List<AdminTestStatisticResponse> result = testService.getAdminTestStatistics(request);

            // Then
            assertEquals(2, result.size());

            AdminTestStatisticResponse response1 = result.get(0);
            assertEquals(userId1.toString(), response1.getUserId());
            assertEquals(3L, response1.getTotalPublishedTests());

            AdminTestStatisticResponse response2 = result.get(1);
            assertEquals(userId2.toString(), response2.getUserId());
            assertEquals(5L, response2.getTotalPublishedTests());

            verify(testRepository).countByCreatedByAndIsDeletedFalse(userId1);
            verify(testRepository).countByCreatedByAndIsDeletedFalse(userId2);
        }
    }

    @org.junit.jupiter.api.Test
    void getAdminTestStatistics_emptyList_shouldReturnEmpty() {
        // Given
        GetAdminTestStatisticsRequest request = GetAdminTestStatisticsRequest.newBuilder().build();

        // When
        List<AdminTestStatisticResponse> result = testService.getAdminTestStatistics(request);

        // Then
        assertTrue(result.isEmpty());
        verifyNoInteractions(testRepository);
    }

    @org.junit.jupiter.api.Test
    void updatePartCount_success() {
        // Given
        UUID testId = UUID.randomUUID();
        int partCount = 5;

        Test test = new Test();
        test.setId(testId);
        test.setPartCount(0); // giá trị ban đầu

        when(testRepository.findById(testId)).thenReturn(Optional.of(test));
        when(testRepository.save(any(Test.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        testService.updatePartCount(testId, partCount);

        // Then
        assertEquals(partCount, test.getPartCount());
        verify(testRepository).findById(testId);
        verify(testRepository).save(test);
    }

    @org.junit.jupiter.api.Test
    void updatePartCount_testNotFound_shouldThrowException() {
        // Given
        UUID testId = UUID.randomUUID();
        int partCount = 5;

        when(testRepository.findById(testId)).thenReturn(Optional.empty());

        // When + Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                testService.updatePartCount(testId, partCount));

        assertEquals("Test not found.", exception.getMessage());
        verify(testRepository).findById(testId);
        verify(testRepository, never()).save(any());
    }

    @org.junit.jupiter.api.Test
    void validateTestId_validId_exists_shouldPass() {
        // Given
        UUID testId = UUID.randomUUID();
        String testIdStr = testId.toString();
        when(testRepository.existsById(testId)).thenReturn(true);

        // When & Then
        assertDoesNotThrow(() -> testService.validateTestId(testIdStr));
        verify(testRepository).existsById(testId);
    }

    @org.junit.jupiter.api.Test
    void validateTestId_validId_notExists_shouldThrowNotFoundException() {
        // Given
        UUID testId = UUID.randomUUID();
        String testIdStr = testId.toString();
        when(testRepository.existsById(testId)).thenReturn(false);

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> testService.validateTestId(testIdStr));

        assertEquals("Test not found.", exception.getMessage());
        verify(testRepository).existsById(testId);
    }

    @org.junit.jupiter.api.Test
    void validateTestId_invalidUUIDFormat_shouldThrowIllegalArgumentException() {
        // Given
        String invalidUUID = "not-a-valid-uuid";

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> testService.validateTestId(invalidUUID));

        verify(testRepository, never()).existsById(any());
    }
}

