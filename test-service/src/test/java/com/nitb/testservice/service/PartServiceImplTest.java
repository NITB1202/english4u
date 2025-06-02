package com.nitb.testservice.service;

import com.nitb.common.exceptions.NotFoundException;
import com.nitb.testservice.entity.Part;
import com.nitb.testservice.grpc.GetAllPartsForTestRequest;
import com.nitb.testservice.repository.PartRepository;
import com.nitb.testservice.service.impl.PartServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PartServiceImplTest {

    @Mock
    private PartRepository partRepository;

    @InjectMocks
    private PartServiceImpl partService;

    @Test
    void createPart_shouldSaveAndReturnPart() {
        UUID testId = UUID.randomUUID();
        int position = 1;
        String content = "Part content";

        Part savedPart = Part.builder()
                .testId(testId)
                .position(position)
                .content(content)
                .questionCount(0)
                .build();

        when(partRepository.save(any(Part.class))).thenReturn(savedPart);

        Part result = partService.createPart(testId, position, content);

        assertNotNull(result);
        assertEquals(testId, result.getTestId());
        assertEquals(position, result.getPosition());
        assertEquals(content, result.getContent());
        assertEquals(0, result.getQuestionCount());

        verify(partRepository, times(1)).save(any(Part.class));
    }

    @Test
    void getAllPartsForTest_shouldReturnPartsList() {
        String testIdStr = UUID.randomUUID().toString();
        UUID testId = UUID.fromString(testIdStr);

        GetAllPartsForTestRequest request = GetAllPartsForTestRequest.newBuilder()
                .setTestId(testIdStr)
                .build();

        List<Part> mockParts = List.of(
                Part.builder().testId(testId).position(1).content("Part 1").build(),
                Part.builder().testId(testId).position(2).content("Part 2").build()
        );

        when(partRepository.getAllByTestId(testId)).thenReturn(mockParts);

        List<Part> result = partService.getAllPartsForTest(request);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Part 1", result.get(0).getContent());
        assertEquals("Part 2", result.get(1).getContent());

        verify(partRepository, times(1)).getAllByTestId(testId);
    }

    @Test
    void getTotalQuestionCount_shouldReturnCorrectSum_whenPartsExist() {
        // Arrange
        UUID testId = UUID.randomUUID();
        List<Part> parts = List.of(
                Part.builder().testId(testId).questionCount(3).build(),
                Part.builder().testId(testId).questionCount(5).build()
        );
        when(partRepository.getAllByTestId(testId)).thenReturn(parts);

        // Act
        int result = partService.getTotalQuestionCount(testId);

        // Assert
        assertEquals(8, result);
        verify(partRepository, times(1)).getAllByTestId(testId);
    }

    @Test
    void getTotalQuestionCount_shouldReturnZero_whenNoPartsExist() {
        // Arrange
        UUID testId = UUID.randomUUID();
        when(partRepository.getAllByTestId(testId)).thenReturn(List.of());

        // Act
        int result = partService.getTotalQuestionCount(testId);

        // Assert
        assertEquals(0, result);
        verify(partRepository, times(1)).getAllByTestId(testId);
    }

    @Test
    void getPartContent_shouldReturnContent_whenPartExists() {
        // Arrange
        UUID partId = UUID.randomUUID();
        Part part = Part.builder()
                .id(partId)
                .content("Part Content Sample")
                .build();
        when(partRepository.findById(partId)).thenReturn(Optional.of(part));

        // Act
        String content = partService.getPartContent(partId);

        // Assert
        assertEquals("Part Content Sample", content);
        verify(partRepository, times(1)).findById(partId);
    }

    @Test
    void getPartContent_shouldThrowNotFoundException_whenPartDoesNotExist() {
        // Arrange
        UUID partId = UUID.randomUUID();
        when(partRepository.findById(partId)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            partService.getPartContent(partId);
        });

        assertTrue(exception.getMessage().contains(partId.toString()));
        verify(partRepository, times(1)).findById(partId);
    }

    @Test
    void getPartCount_shouldReturnCorrectCount() {
        // Arrange
        UUID testId = UUID.randomUUID();
        int expectedCount = 5;
        when(partRepository.countByTestId(testId)).thenReturn(expectedCount);

        // Act
        int actualCount = partService.getPartCount(testId);

        // Assert
        assertEquals(expectedCount, actualCount);
        verify(partRepository, times(1)).countByTestId(testId);
    }

    @Test
    void getAllPartIdsForTest_shouldReturnCorrectListOfIds() {
        // Arrange
        UUID testId = UUID.randomUUID();
        UUID partId1 = UUID.randomUUID();
        UUID partId2 = UUID.randomUUID();

        Part part1 = mock(Part.class);
        Part part2 = mock(Part.class);

        when(part1.getId()).thenReturn(partId1);
        when(part2.getId()).thenReturn(partId2);
        when(partRepository.getAllByTestId(testId)).thenReturn(List.of(part1, part2));

        // Act
        List<UUID> result = partService.getAllPartIdsForTest(testId);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(partId1));
        assertTrue(result.contains(partId2));
        verify(partRepository, times(1)).getAllByTestId(testId);
    }

    @Test
    void getAllPartIdsForTest_shouldReturnEmptyList_whenNoParts() {
        // Arrange
        UUID testId = UUID.randomUUID();
        when(partRepository.getAllByTestId(testId)).thenReturn(List.of());

        // Act
        List<UUID> result = partService.getAllPartIdsForTest(testId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(partRepository, times(1)).getAllByTestId(testId);
    }

    @Test
    void updateQuestionCount_shouldUpdateAndSavePart_whenPartExists() {
        // Arrange
        UUID partId = UUID.randomUUID();
        Part part = new Part();
        part.setId(partId);
        part.setQuestionCount(0);

        when(partRepository.findById(partId)).thenReturn(Optional.of(part));

        // Act
        partService.updateQuestionCount(partId, 5);

        // Assert
        assertEquals(5, part.getQuestionCount());
        verify(partRepository, times(1)).findById(partId);
        verify(partRepository, times(1)).save(part);
    }

    @Test
    void updateQuestionCount_shouldThrowNotFoundException_whenPartDoesNotExist() {
        // Arrange
        UUID partId = UUID.randomUUID();
        when(partRepository.findById(partId)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                partService.updateQuestionCount(partId, 5));

        assertEquals("Part not found.", exception.getMessage());
        verify(partRepository, times(1)).findById(partId);
        verify(partRepository, never()).save(any());
    }
}
