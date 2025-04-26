package com.nitb.apigateway.service.Vocabulary;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.Vocabulary.request.AddVocabularyWordsRequestDto;
import com.nitb.apigateway.dto.Vocabulary.request.UpdateVocabularyWordRequestDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularyWordResponseDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularyWordsPaginationResponseDto;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface VocabularyWordService {
    Mono<List<VocabularyWordResponseDto>> addWordsToSet(UUID userId, AddVocabularyWordsRequestDto request);
    Mono<VocabularyWordsPaginationResponseDto> getVocabularyWords(UUID setId, int page, int size);
    Mono<VocabularyWordsPaginationResponseDto> searchVocabularyWordByWord(UUID setId, String keyword, int page, int size);
    Mono<VocabularyWordResponseDto> updateVocabularyWord(UUID id, UUID userId, UpdateVocabularyWordRequestDto request);
    Mono<ActionResponseDto> switchWordPosition(UUID userId, UUID word1Id, UUID word2Id);
}
