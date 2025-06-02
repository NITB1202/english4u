package com.nitb.vocabularyservice.service;

import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.exceptions.NotFoundException;
import com.nitb.common.grpc.GroupBy;
import com.nitb.vocabularyservice.dto.VocabularySetStatisticProjection;
import com.nitb.vocabularyservice.entity.VocabularySet;
import com.nitb.vocabularyservice.grpc.*;
import com.nitb.vocabularyservice.repository.VocabularySetRepository;
import com.nitb.vocabularyservice.service.impl.VocabularySetServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VocabularySetServiceImplTest {

    @Mock
    private VocabularySetRepository vocabularySetRepository;

    @InjectMocks
    private VocabularySetServiceImpl vocabularySetService;

    @Test
    void testCreateVocabularySet_success() {
        // Given
        UUID userId = UUID.randomUUID();
        String name = "My Vocabulary Set";

        CreateVocabularySetRequest request = CreateVocabularySetRequest.newBuilder()
                .setName(name)
                .setUserId(userId.toString())
                .build();

        when(vocabularySetRepository.existsByNameAndIsDeletedFalse(name))
                .thenReturn(false);

        VocabularySet savedVocabularySet = VocabularySet.builder()
                .id(UUID.randomUUID())
                .name(name)
                .version(1)
                .createdBy(userId)
                .createdAt(LocalDateTime.now())
                .updatedBy(userId)
                .updatedAt(LocalDateTime.now())
                .wordCount(0)
                .isDeleted(false)
                .build();

        when(vocabularySetRepository.save(any(VocabularySet.class)))
                .thenReturn(savedVocabularySet);

        // When
        VocabularySet result = vocabularySetService.createVocabularySet(request);

        // Then
        assertEquals(name, result.getName());
        assertEquals(userId, result.getCreatedBy());
        assertEquals(0, result.getWordCount());
        assertFalse(result.getIsDeleted());
        assertEquals(1, result.getVersion());

        verify(vocabularySetRepository).existsByNameAndIsDeletedFalse(name);
        verify(vocabularySetRepository).save(any(VocabularySet.class));
    }

    @Test
    void testCreateVocabularySet_nameAlreadyExists_shouldThrowException() {
        // Given
        UUID userId = UUID.randomUUID();
        String name = "Duplicate Set";

        CreateVocabularySetRequest request = CreateVocabularySetRequest.newBuilder()
                .setName(name)
                .setUserId(userId.toString())
                .build();

        when(vocabularySetRepository.existsByNameAndIsDeletedFalse(name))
                .thenReturn(true);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            vocabularySetService.createVocabularySet(request);
        });

        assertEquals("This name has been used already.", exception.getMessage());
        verify(vocabularySetRepository).existsByNameAndIsDeletedFalse(name);
        verify(vocabularySetRepository, never()).save(any());
    }

    @Test
    void testGetVocabularySetById_success() {
        // Given
        UUID id = UUID.randomUUID();

        GetVocabularySetByIdRequest request = GetVocabularySetByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        VocabularySet vocabularySet = VocabularySet.builder()
                .id(id)
                .name("Sample Set")
                .version(1)
                .createdBy(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .updatedBy(UUID.randomUUID())
                .updatedAt(LocalDateTime.now())
                .wordCount(10)
                .isDeleted(false)
                .build();

        when(vocabularySetRepository.findById(id)).thenReturn(Optional.of(vocabularySet));

        // When
        VocabularySet result = vocabularySetService.getVocabularySetById(request);

        // Then
        assertEquals(vocabularySet, result);
        verify(vocabularySetRepository).findById(id);
    }

    @Test
    void testGetVocabularySetById_notFound() {
        // Given
        UUID id = UUID.randomUUID();

        GetVocabularySetByIdRequest request = GetVocabularySetByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        when(vocabularySetRepository.findById(id)).thenReturn(Optional.empty());

        // When + Then
        BusinessException ex = assertThrows(BusinessException.class, () ->
                vocabularySetService.getVocabularySetById(request));

        assertEquals("No vocabulary set found with id: " + id, ex.getMessage());
        verify(vocabularySetRepository).findById(id);
    }

    @Test
    void testGetVocabularySets_success_withValidPageAndSize() {
        // Given
        int page = 2;
        int size = 5;

        GetVocabularySetsRequest request = GetVocabularySetsRequest.newBuilder()
                .setPage(page)
                .setSize(size)
                .build();

        List<VocabularySet> content = List.of(
                VocabularySet.builder().name("Set A").build(),
                VocabularySet.builder().name("Set B").build()
        );

        Page<VocabularySet> mockPage = new PageImpl<>(content);

        when(vocabularySetRepository.findAllByIsDeletedFalse(PageRequest.of(page - 1, size)))
                .thenReturn(mockPage);

        // When
        Page<VocabularySet> result = vocabularySetService.getVocabularySets(request);

        // Then
        assertEquals(2, result.getContent().size());
        verify(vocabularySetRepository).findAllByIsDeletedFalse(PageRequest.of(page - 1, size));
    }

    @Test
    void testGetVocabularySets_withDefaultPageAndSize() {
        // Given
        int defaultSize = 10; // từ service

        GetVocabularySetsRequest request = GetVocabularySetsRequest.newBuilder()
                .setPage(0)
                .setSize(0)
                .build();

        List<VocabularySet> content = List.of(
                VocabularySet.builder().name("Default Set").build()
        );

        Page<VocabularySet> mockPage = new PageImpl<>(content);

        when(vocabularySetRepository.findAllByIsDeletedFalse(PageRequest.of(0, defaultSize)))
                .thenReturn(mockPage);

        // When
        Page<VocabularySet> result = vocabularySetService.getVocabularySets(request);

        // Then
        assertEquals(1, result.getContent().size());
        verify(vocabularySetRepository).findAllByIsDeletedFalse(PageRequest.of(0, defaultSize));
    }

    @Test
    void testGetDeletedVocabularySets_withValidPageAndSize() {
        // Given
        int page = 1;
        int size = 5;

        GetVocabularySetsRequest request = GetVocabularySetsRequest.newBuilder()
                .setPage(page)
                .setSize(size)
                .build();

        List<VocabularySet> deletedSets = List.of(
                VocabularySet.builder().name("Deleted Set 1").isDeleted(true).build(),
                VocabularySet.builder().name("Deleted Set 2").isDeleted(true).build()
        );

        Page<VocabularySet> mockPage = new PageImpl<>(deletedSets);

        when(vocabularySetRepository.findAllByIsDeletedTrue(PageRequest.of(page - 1, size)))
                .thenReturn(mockPage);

        // When
        Page<VocabularySet> result = vocabularySetService.getDeletedVocabularySets(request);

        // Then
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().stream().allMatch(VocabularySet::getIsDeleted));
        verify(vocabularySetRepository).findAllByIsDeletedTrue(PageRequest.of(page - 1, size));
    }

    @Test
    void testGetDeletedVocabularySets_withInvalidPageAndSize_defaultsUsed() {
        // Given
        int defaultSize = 10; // giả định giá trị DEFAULT_SIZE là 10

        GetVocabularySetsRequest request = GetVocabularySetsRequest.newBuilder()
                .setPage(0)
                .setSize(0)
                .build();

        List<VocabularySet> deletedSets = List.of(
                VocabularySet.builder().name("Old Deleted Set").isDeleted(true).build()
        );

        Page<VocabularySet> mockPage = new PageImpl<>(deletedSets);

        when(vocabularySetRepository.findAllByIsDeletedTrue(PageRequest.of(0, defaultSize)))
                .thenReturn(mockPage);

        // When
        Page<VocabularySet> result = vocabularySetService.getDeletedVocabularySets(request);

        // Then
        assertEquals(1, result.getContent().size());
        assertTrue(result.getContent().get(0).getIsDeleted());
        verify(vocabularySetRepository).findAllByIsDeletedTrue(PageRequest.of(0, defaultSize));
    }

    @Test
    void testSearchVocabularySetByName_withValidKeywordAndPageSize() {
        // Given
        String keyword = "english";
        int page = 2;
        int size = 5;

        SearchVocabularySetByNameRequest request = SearchVocabularySetByNameRequest.newBuilder()
                .setKeyword(keyword)
                .setPage(page)
                .setSize(size)
                .build();

        List<VocabularySet> matchingSets = List.of(
                VocabularySet.builder().name("English Basics").isDeleted(false).build(),
                VocabularySet.builder().name("Advanced English").isDeleted(false).build()
        );

        Page<VocabularySet> mockPage = new PageImpl<>(matchingSets);

        when(vocabularySetRepository.findByNameContainingIgnoreCaseAndIsDeletedFalse(
                eq(keyword),
                eq(PageRequest.of(page - 1, size))
        )).thenReturn(mockPage);

        // When
        Page<VocabularySet> result = vocabularySetService.searchVocabularySetByName(request);

        // Then
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().stream().allMatch(set -> set.getName().toLowerCase().contains("english")));
        verify(vocabularySetRepository).findByNameContainingIgnoreCaseAndIsDeletedFalse(keyword, PageRequest.of(page - 1, size));
    }

    @Test
    void testSearchVocabularySetByName_withInvalidPageAndSize_defaultsUsed() {
        // Given
        String keyword = "toeic";
        int defaultSize = 10; // Giả sử DEFAULT_SIZE = 10

        SearchVocabularySetByNameRequest request = SearchVocabularySetByNameRequest.newBuilder()
                .setKeyword(keyword)
                .setPage(0)
                .setSize(0)
                .build();

        List<VocabularySet> matchingSets = List.of(
                VocabularySet.builder().name("TOEIC Set").isDeleted(false).build()
        );

        Page<VocabularySet> mockPage = new PageImpl<>(matchingSets);

        when(vocabularySetRepository.findByNameContainingIgnoreCaseAndIsDeletedFalse(
                eq(keyword),
                eq(PageRequest.of(0, defaultSize))
        )).thenReturn(mockPage);

        // When
        Page<VocabularySet> result = vocabularySetService.searchVocabularySetByName(request);

        // Then
        assertEquals(1, result.getContent().size());
        assertEquals("TOEIC Set", result.getContent().get(0).getName());
        verify(vocabularySetRepository).findByNameContainingIgnoreCaseAndIsDeletedFalse(keyword, PageRequest.of(0, defaultSize));
    }

    @Test
    void testSearchDeletedVocabularySetByName_withValidKeywordAndPaging() {
        // Given
        String keyword = "english";
        int page = 2;
        int size = 5;

        SearchVocabularySetByNameRequest request = SearchVocabularySetByNameRequest.newBuilder()
                .setKeyword(keyword)
                .setPage(page)
                .setSize(size)
                .build();

        List<VocabularySet> deletedSets = List.of(
                VocabularySet.builder().name("English - Old Set").isDeleted(true).build(),
                VocabularySet.builder().name("Advanced English Removed").isDeleted(true).build()
        );

        Page<VocabularySet> mockPage = new PageImpl<>(deletedSets);

        when(vocabularySetRepository.findByNameContainingIgnoreCaseAndIsDeletedTrue(
                eq(keyword),
                eq(PageRequest.of(page - 1, size))
        )).thenReturn(mockPage);

        // When
        Page<VocabularySet> result = vocabularySetService.searchDeletedVocabularySetByName(request);

        // Then
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().stream().allMatch(VocabularySet::getIsDeleted));
        verify(vocabularySetRepository).findByNameContainingIgnoreCaseAndIsDeletedTrue(
                keyword, PageRequest.of(page - 1, size)
        );
    }

    @Test
    void testSearchDeletedVocabularySetByName_withDefaultPaging() {
        // Given
        String keyword = "toeic";
        int defaultSize = 10; // Nếu DEFAULT_SIZE = 10

        SearchVocabularySetByNameRequest request = SearchVocabularySetByNameRequest.newBuilder()
                .setKeyword(keyword)
                .setPage(0) // invalid
                .setSize(0) // invalid
                .build();

        List<VocabularySet> deletedSets = List.of(
                VocabularySet.builder().name("TOEIC Archived Set").isDeleted(true).build()
        );

        Page<VocabularySet> mockPage = new PageImpl<>(deletedSets);

        when(vocabularySetRepository.findByNameContainingIgnoreCaseAndIsDeletedTrue(
                eq(keyword),
                eq(PageRequest.of(0, defaultSize))
        )).thenReturn(mockPage);

        // When
        Page<VocabularySet> result = vocabularySetService.searchDeletedVocabularySetByName(request);

        // Then
        assertEquals(1, result.getContent().size());
        assertTrue(result.getContent().get(0).getName().contains("TOEIC"));
        assertTrue(result.getContent().get(0).getIsDeleted());

        verify(vocabularySetRepository).findByNameContainingIgnoreCaseAndIsDeletedTrue(
                keyword, PageRequest.of(0, defaultSize)
        );
    }

    @Test
    void testValidateUpdateVocabularySet_validCase() {
        // Given
        UUID id = UUID.randomUUID();
        String name = "My Set";

        ValidateUpdateVocabularySetRequest request = ValidateUpdateVocabularySetRequest.newBuilder()
                .setId(id.toString())
                .build();

        VocabularySet set = VocabularySet.builder()
                .id(id)
                .name(name)
                .build();

        when(vocabularySetRepository.findById(id)).thenReturn(Optional.of(set));
        when(vocabularySetRepository.countByName(name)).thenReturn(2); // < MAX_VERSION giả sử = 5

        // When + Then
        assertDoesNotThrow(() -> vocabularySetService.validateUpdateVocabularySet(request));

        verify(vocabularySetRepository).findById(id);
        verify(vocabularySetRepository).countByName(name);
    }

    @Test
    void testValidateUpdateVocabularySet_vocabularySetNotFound() {
        // Given
        UUID id = UUID.randomUUID();

        ValidateUpdateVocabularySetRequest request = ValidateUpdateVocabularySetRequest.newBuilder()
                .setId(id.toString())
                .build();

        when(vocabularySetRepository.findById(id)).thenReturn(Optional.empty());

        // When + Then
        BusinessException ex = assertThrows(BusinessException.class,
                () -> vocabularySetService.validateUpdateVocabularySet(request));

        assertTrue(ex.getMessage().contains("No vocabulary set found with id"));
        verify(vocabularySetRepository).findById(id);
        verify(vocabularySetRepository, never()).countByName(any());
    }

    @Test
    void testValidateUpdateVocabularySet_versionLimitReached() {
        // Given
        UUID id = UUID.randomUUID();
        String name = "Limited Set";

        ValidateUpdateVocabularySetRequest request = ValidateUpdateVocabularySetRequest.newBuilder()
                .setId(id.toString())
                .build();

        VocabularySet set = VocabularySet.builder()
                .id(id)
                .name(name)
                .build();

        when(vocabularySetRepository.findById(id)).thenReturn(Optional.of(set));
        when(vocabularySetRepository.countByName(name)).thenReturn(5); // MAX_VERSION giả sử = 5

        // When + Then
        BusinessException ex = assertThrows(BusinessException.class,
                () -> vocabularySetService.validateUpdateVocabularySet(request));

        assertEquals("Version limit reached for this vocabulary set. Please create a new set to continue editing.", ex.getMessage());

        verify(vocabularySetRepository).findById(id);
        verify(vocabularySetRepository).countByName(name);
    }

    @Test
    void testUpdateVocabularySetName_success() {
        // Given
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String oldName = "Old Name";
        String newName = "New Name";

        UpdateVocabularySetNameRequest request = UpdateVocabularySetNameRequest.newBuilder()
                .setId(id.toString())
                .setUserId(userId.toString())
                .setName(newName)
                .build();

        VocabularySet existingSet = VocabularySet.builder()
                .id(id)
                .name(oldName)
                .isDeleted(false)
                .build();

        when(vocabularySetRepository.findById(id)).thenReturn(Optional.of(existingSet));
        when(vocabularySetRepository.existsByNameAndIsDeletedFalse(newName)).thenReturn(false);
        when(vocabularySetRepository.save(any(VocabularySet.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // return updated set

        // When
        VocabularySet updatedSet = vocabularySetService.updateVocabularySetName(request);

        // Then
        assertEquals(newName, updatedSet.getName());
        assertEquals(1, updatedSet.getVersion());
        assertEquals(userId, updatedSet.getUpdatedBy());
        assertNotNull(updatedSet.getUpdatedAt());

        verify(vocabularySetRepository).findById(id);
        verify(vocabularySetRepository).existsByNameAndIsDeletedFalse(newName);
        verify(vocabularySetRepository).save(existingSet);
    }

    @Test
    void testUpdateVocabularySetName_vocabularySetNotFound() {
        // Given
        UUID id = UUID.randomUUID();
        UpdateVocabularySetNameRequest request = UpdateVocabularySetNameRequest.newBuilder()
                .setId(id.toString())
                .setUserId(UUID.randomUUID().toString())
                .setName("New Name")
                .build();

        when(vocabularySetRepository.findById(id)).thenReturn(Optional.empty());

        // When + Then
        BusinessException ex = assertThrows(BusinessException.class, () ->
                vocabularySetService.updateVocabularySetName(request));

        assertTrue(ex.getMessage().contains("No vocabulary set found with id"));

        verify(vocabularySetRepository).findById(id);
        verify(vocabularySetRepository, never()).save(any());
    }

    @Test
    void testUpdateVocabularySetName_vocabularySetIsDeleted() {
        // Given
        UUID id = UUID.randomUUID();

        UpdateVocabularySetNameRequest request = UpdateVocabularySetNameRequest.newBuilder()
                .setId(id.toString())
                .setUserId(UUID.randomUUID().toString())
                .setName("New Name")
                .build();

        VocabularySet deletedSet = VocabularySet.builder()
                .id(id)
                .name("Old Name")
                .isDeleted(true)
                .build();

        when(vocabularySetRepository.findById(id)).thenReturn(Optional.of(deletedSet));

        // When + Then
        BusinessException ex = assertThrows(BusinessException.class, () ->
                vocabularySetService.updateVocabularySetName(request));

        assertEquals("This vocabulary set has been deleted. Please restore it before updating.", ex.getMessage());

        verify(vocabularySetRepository).findById(id);
        verify(vocabularySetRepository, never()).save(any());
    }

    @Test
    void testUpdateVocabularySetName_nameAlreadyExists() {
        // Given
        UUID id = UUID.randomUUID();
        String newName = "Existing Name";

        UpdateVocabularySetNameRequest request = UpdateVocabularySetNameRequest.newBuilder()
                .setId(id.toString())
                .setUserId(UUID.randomUUID().toString())
                .setName(newName)
                .build();

        VocabularySet existingSet = VocabularySet.builder()
                .id(id)
                .name("Old Name")
                .isDeleted(false)
                .build();

        when(vocabularySetRepository.findById(id)).thenReturn(Optional.of(existingSet));
        when(vocabularySetRepository.existsByNameAndIsDeletedFalse(newName)).thenReturn(true);

        // When + Then
        BusinessException ex = assertThrows(BusinessException.class, () ->
                vocabularySetService.updateVocabularySetName(request));

        assertEquals("This name has been used already.", ex.getMessage());

        verify(vocabularySetRepository).findById(id);
        verify(vocabularySetRepository).existsByNameAndIsDeletedFalse(newName);
        verify(vocabularySetRepository, never()).save(any());
    }

    @Test
    void testUpdateVocabularySet_success() {
        // Given
        UUID oldId = UUID.randomUUID();
        UUID newId = UUID.randomUUID();
        UUID createdBy = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        int latestVersion = 3;

        UpdateVocabularySetRequest request = UpdateVocabularySetRequest.newBuilder()
                .setOldId(oldId.toString())
                .setNewId(newId.toString())
                .build();

        VocabularySet oldSet = VocabularySet.builder()
                .id(oldId)
                .name("My Set")
                .createdBy(createdBy)
                .createdAt(createdAt)
                .build();

        VocabularySet newSet = VocabularySet.builder()
                .id(newId)
                .name("My Set")
                .build();

        when(vocabularySetRepository.findById(oldId)).thenReturn(Optional.of(oldSet));
        when(vocabularySetRepository.findById(newId)).thenReturn(Optional.of(newSet));
        when(vocabularySetRepository.getLatestVersion("My Set")).thenReturn(latestVersion);
        when(vocabularySetRepository.save(any(VocabularySet.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        VocabularySet result = vocabularySetService.updateVocabularySet(request);

        // Then
        assertEquals(latestVersion + 1, result.getVersion());
        assertEquals(createdBy, result.getCreatedBy());
        assertEquals(createdAt, result.getCreatedAt());

        verify(vocabularySetRepository).findById(oldId);
        verify(vocabularySetRepository).findById(newId);
        verify(vocabularySetRepository).getLatestVersion("My Set");
        verify(vocabularySetRepository).save(newSet);
    }

    @Test
    void testUpdateVocabularySet_oldSetNotFound() {
        // Given
        UUID oldId = UUID.randomUUID();
        UUID newId = UUID.randomUUID();

        UpdateVocabularySetRequest request = UpdateVocabularySetRequest.newBuilder()
                .setOldId(oldId.toString())
                .setNewId(newId.toString())
                .build();

        when(vocabularySetRepository.findById(oldId)).thenReturn(Optional.empty());

        // When + Then
        BusinessException ex = assertThrows(BusinessException.class, () ->
                vocabularySetService.updateVocabularySet(request));

        assertEquals("No vocabulary set found with id: " + oldId, ex.getMessage());

        verify(vocabularySetRepository).findById(oldId);
        verify(vocabularySetRepository, never()).findById(newId);
        verify(vocabularySetRepository, never()).save(any());
    }

    @Test
    void testUpdateVocabularySet_newSetNotFound() {
        // Given
        UUID oldId = UUID.randomUUID();
        UUID newId = UUID.randomUUID();

        UpdateVocabularySetRequest request = UpdateVocabularySetRequest.newBuilder()
                .setOldId(oldId.toString())
                .setNewId(newId.toString())
                .build();

        VocabularySet oldSet = VocabularySet.builder()
                .id(oldId)
                .name("My Set")
                .createdBy(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .build();

        when(vocabularySetRepository.findById(oldId)).thenReturn(Optional.of(oldSet));
        when(vocabularySetRepository.findById(newId)).thenReturn(Optional.empty());

        // When + Then
        BusinessException ex = assertThrows(BusinessException.class, () ->
                vocabularySetService.updateVocabularySet(request));

        assertEquals("No vocabulary set found with id: " + newId, ex.getMessage());

        verify(vocabularySetRepository).findById(oldId);
        verify(vocabularySetRepository).findById(newId);
        verify(vocabularySetRepository, never()).save(any());
    }

    @Test
    void testDeleteVocabularySet_success() {
        // Given
        UUID setId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        DeleteVocabularySetRequest request = DeleteVocabularySetRequest.newBuilder()
                .setId(setId.toString())
                .setUserId(userId.toString())
                .build();

        VocabularySet set = VocabularySet.builder()
                .id(setId)
                .isDeleted(false)
                .build();

        when(vocabularySetRepository.findById(setId)).thenReturn(Optional.of(set));
        when(vocabularySetRepository.save(any(VocabularySet.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        VocabularySet result = vocabularySetService.deleteVocabularySet(request);

        // Then
        assertTrue(result.getIsDeleted());
        assertEquals(userId, result.getUpdatedBy());
        assertNotNull(result.getUpdatedAt());

        verify(vocabularySetRepository).findById(setId);
        verify(vocabularySetRepository).save(set);
    }

    @Test
    void testDeleteVocabularySet_notFound() {
        // Given
        UUID setId = UUID.randomUUID();

        DeleteVocabularySetRequest request = DeleteVocabularySetRequest.newBuilder()
                .setId(setId.toString())
                .setUserId(UUID.randomUUID().toString())
                .build();

        when(vocabularySetRepository.findById(setId)).thenReturn(Optional.empty());

        // When + Then
        BusinessException ex = assertThrows(BusinessException.class, () ->
                vocabularySetService.deleteVocabularySet(request));

        assertEquals("No vocabulary set found with id: " + setId, ex.getMessage());

        verify(vocabularySetRepository).findById(setId);
        verify(vocabularySetRepository, never()).save(any());
    }

    @Test
    void testRestoreVocabularySet_success_withCurrentSet() {
        // Given
        UUID setId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDateTime beforeTest = LocalDateTime.now();

        RestoreVocabularySetRequest request = RestoreVocabularySetRequest.newBuilder()
                .setId(setId.toString())
                .setUserId(userId.toString())
                .build();

        VocabularySet setToRestore = VocabularySet.builder()
                .id(setId)
                .name("TestSet")
                .isDeleted(true)
                .build();

        VocabularySet currentSet = VocabularySet.builder()
                .id(UUID.randomUUID())
                .name("TestSet")
                .isDeleted(false)
                .build();

        when(vocabularySetRepository.findById(setId)).thenReturn(Optional.of(setToRestore));
        when(vocabularySetRepository.findByNameAndIsDeletedFalse("TestSet")).thenReturn(currentSet);
        when(vocabularySetRepository.save(any(VocabularySet.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        VocabularySet result = vocabularySetService.restoreVocabularySet(request);

        // Then
        // Current set must be marked deleted and updated
        assertTrue(currentSet.getIsDeleted());
        assertEquals(userId, currentSet.getUpdatedBy());
        assertNotNull(currentSet.getUpdatedAt());
        assertTrue(currentSet.getUpdatedAt().isAfter(beforeTest) || currentSet.getUpdatedAt().isEqual(beforeTest));

        // Restored set must be marked not deleted and updated
        assertFalse(result.getIsDeleted());
        assertEquals(userId, result.getUpdatedBy());
        assertNotNull(result.getUpdatedAt());
        assertTrue(result.getUpdatedAt().isAfter(beforeTest) || result.getUpdatedAt().isEqual(beforeTest));

        verify(vocabularySetRepository).findById(setId);
        verify(vocabularySetRepository).findByNameAndIsDeletedFalse("TestSet");
        verify(vocabularySetRepository).save(currentSet);
        verify(vocabularySetRepository).save(setToRestore);
    }

    @Test
    void testRestoreVocabularySet_success_withoutCurrentSet() {
        // Given
        UUID setId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDateTime beforeTest = LocalDateTime.now();

        RestoreVocabularySetRequest request = RestoreVocabularySetRequest.newBuilder()
                .setId(setId.toString())
                .setUserId(userId.toString())
                .build();

        VocabularySet setToRestore = VocabularySet.builder()
                .id(setId)
                .name("TestSet")
                .isDeleted(true)
                .build();

        when(vocabularySetRepository.findById(setId)).thenReturn(Optional.of(setToRestore));
        when(vocabularySetRepository.findByNameAndIsDeletedFalse("TestSet")).thenReturn(null);
        when(vocabularySetRepository.save(any(VocabularySet.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        VocabularySet result = vocabularySetService.restoreVocabularySet(request);

        // Then
        assertFalse(result.getIsDeleted());
        assertEquals(userId, result.getUpdatedBy());
        assertNotNull(result.getUpdatedAt());
        assertTrue(result.getUpdatedAt().isAfter(beforeTest) || result.getUpdatedAt().isEqual(beforeTest));

        verify(vocabularySetRepository).findById(setId);
        verify(vocabularySetRepository).findByNameAndIsDeletedFalse("TestSet");
        verify(vocabularySetRepository, never()).save(argThat(v -> v != setToRestore)); // no save for currentSet because null
        verify(vocabularySetRepository).save(setToRestore);
    }

    @Test
    void testRestoreVocabularySet_notFound() {
        // Given
        UUID setId = UUID.randomUUID();
        RestoreVocabularySetRequest request = RestoreVocabularySetRequest.newBuilder()
                .setId(setId.toString())
                .setUserId(UUID.randomUUID().toString())
                .build();

        when(vocabularySetRepository.findById(setId)).thenReturn(Optional.empty());

        // When + Then
        BusinessException ex = assertThrows(BusinessException.class, () ->
                vocabularySetService.restoreVocabularySet(request));

        assertEquals("No vocabulary set found with id: " + setId, ex.getMessage());

        verify(vocabularySetRepository).findById(setId);
        verify(vocabularySetRepository, never()).findByNameAndIsDeletedFalse(any());
        verify(vocabularySetRepository, never()).save(any());
    }

    @Test
    void testCountPublishedVocabularySets_groupByWeek() {
        // Given
        UUID userId = UUID.randomUUID();
        String from = "2025-01-01";
        String to = "2025-01-31";
        CountPublishedVocabularySetsRequest request = CountPublishedVocabularySetsRequest.newBuilder()
                .setUserId(userId.toString())
                .setFrom(from)
                .setTo(to)
                .setGroupBy(GroupBy.WEEK)
                .build();

        List<VocabularySetStatisticProjection> mockProjections = List.of(
                new VocabularySetStatisticProjection() {
                    @Override
                    public String getTime() { return LocalDate.of(2025, 1, 1).toString(); }
                    @Override
                    public Long getCount() { return 5L; }
                },
                new VocabularySetStatisticProjection() {
                    @Override
                    public String getTime() { return LocalDate.of(2025, 1, 8).toString(); }
                    @Override
                    public Long getCount() { return 3L; }
                }
        );

        when(vocabularySetRepository.countByWeek(
                eq(userId),
                eq(LocalDate.parse(from).atStartOfDay()),
                eq(LocalDate.parse(to).atTime(23, 59, 59))))
                .thenReturn(mockProjections);

        // When
        List<VocabularySetStatisticResponse> responses = vocabularySetService.countPublishedVocabularySets(request);

        // Then
        assertEquals(2, responses.size());
        assertEquals(LocalDate.of(2025, 1, 1).toString(), responses.get(0).getTime());
        assertEquals(5, responses.get(0).getCount());
        assertEquals(LocalDate.of(2025, 1, 8).toString(), responses.get(1).getTime());
        assertEquals(3, responses.get(1).getCount());

        verify(vocabularySetRepository).countByWeek(any(), any(), any());
    }

    @Test
    void testCountPublishedVocabularySets_groupByMonth() {
        // Given
        UUID userId = UUID.randomUUID();
        String from = "2025-01-01";
        String to = "2025-12-31";
        CountPublishedVocabularySetsRequest request = CountPublishedVocabularySetsRequest.newBuilder()
                .setUserId(userId.toString())
                .setFrom(from)
                .setTo(to)
                .setGroupBy(GroupBy.MONTH)
                .build();

        List<VocabularySetStatisticProjection> mockProjections = List.of(
                new VocabularySetStatisticProjection() {
                    @Override
                    public String getTime() { return LocalDate.of(2025, 1, 1).toString(); }
                    @Override
                    public Long getCount() { return 10L; }
                },
                new VocabularySetStatisticProjection() {
                    @Override
                    public String getTime() { return LocalDate.of(2025, 2, 1).toString(); }
                    @Override
                    public Long getCount() { return 15L; }
                }
        );

        when(vocabularySetRepository.countByMonth(
                eq(userId),
                eq(LocalDate.parse(from).atStartOfDay()),
                eq(LocalDate.parse(to).atTime(23, 59, 59))))
                .thenReturn(mockProjections);

        // When
        List<VocabularySetStatisticResponse> responses = vocabularySetService.countPublishedVocabularySets(request);

        // Then
        assertEquals(2, responses.size());
        assertEquals(LocalDate.of(2025, 1, 1).toString(), responses.get(0).getTime());
        assertEquals(10, responses.get(0).getCount());
        assertEquals(LocalDate.of(2025, 2, 1).toString(), responses.get(1).getTime());
        assertEquals(15, responses.get(1).getCount());

        verify(vocabularySetRepository).countByMonth(any(), any(), any());
    }

    @Test
    void testCountPublishedVocabularySets_groupByYear() {
        // Given
        UUID userId = UUID.randomUUID();
        String from = "2020-01-01";
        String to = "2025-12-31";
        CountPublishedVocabularySetsRequest request = CountPublishedVocabularySetsRequest.newBuilder()
                .setUserId(userId.toString())
                .setFrom(from)
                .setTo(to)
                .setGroupBy(GroupBy.YEAR)
                .build();

        List<VocabularySetStatisticProjection> mockProjections = List.of(
                new VocabularySetStatisticProjection() {
                    @Override
                    public String getTime() { return LocalDate.of(2020, 1, 1).toString(); }
                    @Override
                    public Long getCount() { return 50L; }
                },
                new VocabularySetStatisticProjection() {
                    @Override
                    public String getTime() { return LocalDate.of(2021, 1, 1).toString(); }
                    @Override
                    public Long getCount() { return 70L; }
                }
        );

        when(vocabularySetRepository.countByYear(
                eq(userId),
                eq(LocalDate.parse(from).atStartOfDay()),
                eq(LocalDate.parse(to).atTime(23, 59, 59))))
                .thenReturn(mockProjections);

        // When
        List<VocabularySetStatisticResponse> responses = vocabularySetService.countPublishedVocabularySets(request);

        // Then
        assertEquals(2, responses.size());
        assertEquals(LocalDate.of(2020, 1, 1).toString(), responses.get(0).getTime());
        assertEquals(50, responses.get(0).getCount());
        assertEquals(LocalDate.of(2021, 1, 1).toString(), responses.get(1).getTime());
        assertEquals(70, responses.get(1).getCount());

        verify(vocabularySetRepository).countByYear(any(), any(), any());
    }

    @Test
    void testCountPublishedVocabularySets_invalidGroupBy() {
        // Given
        UUID userId = UUID.randomUUID();
        CountPublishedVocabularySetsRequest request = CountPublishedVocabularySetsRequest.newBuilder()
                .setUserId(userId.toString())
                .setFrom("2025-01-01")
                .setTo("2025-12-31")
                .setGroupBy(GroupBy.GROUP_BY_UNSPECIFIED) // invalid groupBy
                .build();

        // When + Then
        BusinessException ex = assertThrows(BusinessException.class, () -> {
            vocabularySetService.countPublishedVocabularySets(request);
        });

        assertEquals("Invalid group by.", ex.getMessage());
    }

    @Test
    void testGetAdminSetStatistics() {
        // Given
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();

        GetAdminSetStatisticsRequest request = GetAdminSetStatisticsRequest.newBuilder()
                .addAllUserId(List.of(userId1.toString(), userId2.toString()))
                .build();

        when(vocabularySetRepository.countByCreatedByAndIsDeletedFalse(userId1)).thenReturn(5L);
        when(vocabularySetRepository.countByCreatedByAndIsDeletedFalse(userId2)).thenReturn(10L);

        // When
        List<AdminSetStatisticResponse> responses = vocabularySetService.getAdminSetStatistics(request);

        // Then
        assertEquals(2, responses.size());

        AdminSetStatisticResponse resp1 = responses.get(0);
        assertEquals(userId1.toString(), resp1.getUserId());
        assertEquals(5L, resp1.getTotalPublishedSets());

        AdminSetStatisticResponse resp2 = responses.get(1);
        assertEquals(userId2.toString(), resp2.getUserId());
        assertEquals(10L, resp2.getTotalPublishedSets());

        verify(vocabularySetRepository).countByCreatedByAndIsDeletedFalse(userId1);
        verify(vocabularySetRepository).countByCreatedByAndIsDeletedFalse(userId2);
    }

    @Test
    void testUpdateWordCount_success() {
        // Given
        UUID setId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        int newCount = 42;

        VocabularySet existingSet = VocabularySet.builder()
                .id(setId)
                .wordCount(10)
                .updatedBy(UUID.randomUUID())
                .updatedAt(LocalDateTime.now().minusDays(1))
                .build();

        when(vocabularySetRepository.findById(setId)).thenReturn(Optional.of(existingSet));
        when(vocabularySetRepository.save(any(VocabularySet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        vocabularySetService.updateWordCount(setId, newCount, userId);

        // Then
        assertEquals(newCount, existingSet.getWordCount());
        assertEquals(userId, existingSet.getUpdatedBy());
        assertNotNull(existingSet.getUpdatedAt());

        verify(vocabularySetRepository).findById(setId);
        verify(vocabularySetRepository).save(existingSet);
    }

    @Test
    void testUpdateWordCount_setNotFound() {
        // Given
        UUID setId = UUID.randomUUID();

        when(vocabularySetRepository.findById(setId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                vocabularySetService.updateWordCount(setId, 10, UUID.randomUUID())
        );

        assertEquals("Vocabulary set not found.", exception.getMessage());

        verify(vocabularySetRepository).findById(setId);
        verify(vocabularySetRepository, never()).save(any());
    }
}

