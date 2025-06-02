package com.nitb.vocabularyservice.service;

import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.exceptions.NotFoundException;
import com.nitb.vocabularyservice.entity.VocabularyWord;
import com.nitb.vocabularyservice.grpc.*;
import com.nitb.vocabularyservice.repository.VocabularyWordRepository;
import com.nitb.vocabularyservice.service.impl.VocabularyWordServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VocabularyWordServiceImplTest {

    @Mock
    private VocabularyWordRepository vocabularyWordRepository;

    @Mock
    private VocabularySetService vocabularySetService;

    @InjectMocks
    private VocabularyWordServiceImpl vocabularyWordService;

    @Test
    void testCreateVocabularyWords_success() {
        UUID setId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CreateVocabularyWordRequest wordRequest = CreateVocabularyWordRequest.newBuilder()
                .setWord("apple")
                .setPronunciation("ˈæpəl")
                .setTranslation("quả táo")
                .setExample("I eat an apple.")
                .build();

        CreateVocabularyWordsRequest request = CreateVocabularyWordsRequest.newBuilder()
                .setSetId(setId.toString())
                .setUserId(userId.toString())
                .addWords(wordRequest)
                .build();

        when(vocabularyWordRepository.countBySetId(setId)).thenReturn(0);
        when(vocabularyWordRepository.existsByWordAndSetId("apple", setId)).thenReturn(false);
        when(vocabularyWordRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        List<VocabularyWord> result = vocabularyWordService.createVocabularyWords(request);

        assertEquals(1, result.size());
        assertEquals("apple", result.get(0).getWord());

        verify(vocabularySetService).updateWordCount(eq(setId), eq(1), eq(userId));
    }

    @Test
    void testCreateVocabularyWords_wordAlreadyExists_throwsException() {
        // Given
        UUID setId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        CreateVocabularyWordRequest word1 = CreateVocabularyWordRequest.newBuilder()
                .setWord("apple")
                .setPronunciation("ˈæpəl")
                .setTranslation("quả táo")
                .setExample("I eat an apple.")
                .build();

        CreateVocabularyWordsRequest request = CreateVocabularyWordsRequest.newBuilder()
                .setSetId(setId.toString())
                .setUserId(userId.toString())
                .addWords(word1)
                .build();

        when(vocabularyWordRepository.countBySetId(setId)).thenReturn(0);
        when(vocabularyWordRepository.existsByWordAndSetId("apple", setId)).thenReturn(true);

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> vocabularyWordService.createVocabularyWords(request));

        assertEquals("The word 'apple' already exists in this set.", exception.getMessage());
        verify(vocabularyWordRepository, never()).saveAll(any());
        verify(vocabularySetService, never()).updateWordCount(any(), anyInt(), any());
    }

    @Test
    void testGetVocabularyWords_success() {
        UUID setId = UUID.randomUUID();
        GetVocabularyWordsRequest request = GetVocabularyWordsRequest.newBuilder()
                .setSetId(setId.toString())
                .setPage(1)
                .setSize(5)
                .build();

        Page<VocabularyWord> mockPage = new PageImpl<>(List.of(new VocabularyWord()));
        when(vocabularyWordRepository.findBySetId(eq(setId), any())).thenReturn(mockPage);

        Page<VocabularyWord> result = vocabularyWordService.getVocabularyWords(request);

        assertEquals(1, result.getContent().size());
        verify(vocabularyWordRepository).findBySetId(eq(setId), any());
    }

    @Test
    void testGetVocabularyWords_defaultPageAndSize() {
        // Given
        UUID setId = UUID.randomUUID();
        int DEFAULT_SIZE = 10;

        GetVocabularyWordsRequest request = GetVocabularyWordsRequest.newBuilder()
                .setSetId(setId.toString())
                .setPage(0)
                .setSize(0)
                .build();

        Page<VocabularyWord> mockPage = new PageImpl<>(List.of());

        when(vocabularyWordRepository.findBySetId(eq(setId), eq(PageRequest.of(0, DEFAULT_SIZE))))
                .thenReturn(mockPage);

        // When
        Page<VocabularyWord> result = vocabularyWordService.getVocabularyWords(request);

        // Then
        assertTrue(result.getContent().isEmpty());
        verify(vocabularyWordRepository).findBySetId(setId, PageRequest.of(0, DEFAULT_SIZE));
    }

    @Test
    void testSearchVocabularyWordByWord_success() {
        // Given
        UUID setId = UUID.randomUUID();
        String keyword = "ap";
        int page = 1;
        int size = 5;

        SearchVocabularyWordByWordRequest request = SearchVocabularyWordByWordRequest.newBuilder()
                .setSetId(setId.toString())
                .setKeyword(keyword)
                .setPage(page)
                .setSize(size)
                .build();

        Page<VocabularyWord> mockPage = new PageImpl<>(List.of(
                VocabularyWord.builder().word("apple").build(),
                VocabularyWord.builder().word("application").build()
        ));

        when(vocabularyWordRepository.findByWordContainingIgnoreCaseAndSetId(
                eq(keyword),
                eq(setId),
                eq(PageRequest.of(0, size))
        )).thenReturn(mockPage);

        // When
        Page<VocabularyWord> result = vocabularyWordService.searchVocabularyWordByWord(request);

        // Then
        assertEquals(2, result.getContent().size());
        assertEquals("apple", result.getContent().get(0).getWord());
        assertEquals("application", result.getContent().get(1).getWord());

        verify(vocabularyWordRepository).findByWordContainingIgnoreCaseAndSetId(
                eq(keyword),
                eq(setId),
                eq(PageRequest.of(0, size))
        );
    }

    @Test
    void testSearchVocabularyWordByWord_defaultPageAndSize() {
        // Given
        UUID setId = UUID.randomUUID();
        String keyword = "banana";
        int DEFAULT_SIZE = 10;

        SearchVocabularyWordByWordRequest request = SearchVocabularyWordByWordRequest.newBuilder()
                .setSetId(setId.toString())
                .setKeyword(keyword)
                .setPage(0)
                .setSize(0)
                .build();

        Page<VocabularyWord> mockPage = new PageImpl<>(List.of());

        when(vocabularyWordRepository.findByWordContainingIgnoreCaseAndSetId(
                eq(keyword),
                eq(setId),
                eq(PageRequest.of(0, DEFAULT_SIZE))
        )).thenReturn(mockPage);

        // When
        Page<VocabularyWord> result = vocabularyWordService.searchVocabularyWordByWord(request);

        // Then
        assertTrue(result.isEmpty());
        verify(vocabularyWordRepository).findByWordContainingIgnoreCaseAndSetId(
                eq(keyword),
                eq(setId),
                eq(PageRequest.of(0, DEFAULT_SIZE))
        );
    }

    @Test
    void testUploadVocabularyWordImage_success() {
        UUID wordId = UUID.randomUUID();
        UploadVocabularyWordImageRequest request = UploadVocabularyWordImageRequest.newBuilder()
                .setId(wordId.toString())
                .setImageUrl("https://image.com/apple.png")
                .build();

        VocabularyWord word = new VocabularyWord();
        word.setId(wordId);

        when(vocabularyWordRepository.findById(wordId)).thenReturn(Optional.of(word));
        when(vocabularyWordRepository.save(any())).thenReturn(word);

        String url = vocabularyWordService.uploadVocabularyWordImage(request);

        assertEquals("https://image.com/apple.png", url);
        assertEquals(url, word.getImageUrl());
    }

    @Test
    void testUploadVocabularyWordImage_wordNotFound() {
        // Given
        UUID wordId = UUID.randomUUID();
        String imageUrl = "https://example.com/image.png";

        UploadVocabularyWordImageRequest request = UploadVocabularyWordImageRequest.newBuilder()
                .setId(wordId.toString())
                .setImageUrl(imageUrl)
                .build();

        when(vocabularyWordRepository.findById(wordId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> vocabularyWordService.uploadVocabularyWordImage(request));

        assertEquals("Vocabulary word not found.", exception.getMessage());
        verify(vocabularyWordRepository).findById(wordId);
        verify(vocabularyWordRepository, never()).save(any());
    }

    @Test
    void testEnsureWordInSet_success() {
        UUID wordId = UUID.randomUUID();
        UUID setId = UUID.randomUUID();

        EnsureWordInSetRequest request = EnsureWordInSetRequest.newBuilder()
                .setWordId(wordId.toString())
                .setSetId(setId.toString())
                .build();

        when(vocabularyWordRepository.existsByIdAndSetId(wordId, setId)).thenReturn(true);

        assertDoesNotThrow(() -> vocabularyWordService.ensureWordInSet(request));
    }

    @Test
    void testEnsureWordInSet_notFound() {
        UUID wordId = UUID.randomUUID();
        UUID setId = UUID.randomUUID();

        EnsureWordInSetRequest request = EnsureWordInSetRequest.newBuilder()
                .setWordId(wordId.toString())
                .setSetId(setId.toString())
                .build();

        when(vocabularyWordRepository.existsByIdAndSetId(wordId, setId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> vocabularyWordService.ensureWordInSet(request));
    }
}
