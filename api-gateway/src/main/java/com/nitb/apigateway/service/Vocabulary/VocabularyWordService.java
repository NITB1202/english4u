package com.nitb.apigateway.service.Vocabulary;

import com.nitb.apigateway.dto.Action.ActionResponseDto;
import com.nitb.apigateway.dto.Vocabulary.request.AddVocabularyWordsRequestDto;
import com.nitb.apigateway.dto.Vocabulary.request.DeleteVocabularyWordsRequestDto;
import com.nitb.apigateway.dto.Vocabulary.request.UpdateVocabularyWordRequestDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularyWordResponseDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularyWordsPaginationResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface VocabularyWordService {
    Flux<VocabularyWordResponseDto> addWordsToSet(UUID userId, AddVocabularyWordsRequestDto request);
    Mono<VocabularyWordsPaginationResponseDto> getVocabularyWords(UUID setId, int page, int size);
    Mono<VocabularyWordsPaginationResponseDto> searchVocabularyWordByWord(UUID setId, String keyword, int page, int size);
    Mono<VocabularyWordResponseDto> updateVocabularyWord(UUID id, UUID userId, UpdateVocabularyWordRequestDto request);
    Mono<ActionResponseDto> switchWordPosition(UUID userId, UUID word1Id, UUID word2Id);
    Mono<ActionResponseDto> deleteVocabularyWords(UUID userId, DeleteVocabularyWordsRequestDto request);

    boolean uploadVocabularyWordImage(UUID id, UUID userId, String imageUrl);
}
