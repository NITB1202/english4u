package com.nitb.apigateway.service.Vocabulary;

import com.nitb.apigateway.dto.Action.ActionResponseDto;
import com.nitb.apigateway.dto.Vocabulary.request.CreateVocabularyWordRequestDto;
import com.nitb.apigateway.dto.Vocabulary.request.UpdateVocabularyWordRequestDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularyWordResponseDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularyWordsPaginationResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public class VocabularyWordServiceImpl implements VocabularyWordService {
    @Override
    public Flux<VocabularyWordResponseDto> addWordsToSet(UUID setId, UUID userId, List<CreateVocabularyWordRequestDto> request) {
        return null;
    }

    @Override
    public Mono<VocabularyWordsPaginationResponseDto> getVocabularyWords(UUID setId, int page, int size) {
        return null;
    }

    @Override
    public Mono<VocabularyWordsPaginationResponseDto> searchVocabularyWordByWord(UUID setId, String keyword, int page, int size) {
        return null;
    }

    @Override
    public Mono<VocabularyWordResponseDto> updateVocabularyWord(UUID id, UUID userId, UpdateVocabularyWordRequestDto request) {
        return null;
    }

    @Override
    public Mono<ActionResponseDto> switchWordPosition(UUID userId, UUID word1Id, UUID word2Id) {
        return null;
    }

    @Override
    public Mono<ActionResponseDto> deleteVocabularyWords(UUID setId, UUID userId, List<UUID> ids) {
        return null;
    }

    @Override
    public void uploadVocabularyWordImage(UUID id, UUID userId, String imageUrl) {

    }
}
