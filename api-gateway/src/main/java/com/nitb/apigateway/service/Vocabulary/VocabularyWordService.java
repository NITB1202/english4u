package com.nitb.apigateway.service.Vocabulary;

import com.nitb.apigateway.dto.Vocabulary.response.VocabularyWordsPaginationResponseDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface VocabularyWordService {
    Mono<VocabularyWordsPaginationResponseDto> getVocabularyWords(UUID setId, int page, int size);
    Mono<VocabularyWordsPaginationResponseDto> searchVocabularyWordByWord(UUID setId, String keyword, int page, int size);
}
