package com.nitb.apigateway.service.Vocabulary;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularyWordsPaginationResponseDto;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface VocabularyWordService {
    Mono<VocabularyWordsPaginationResponseDto> getVocabularyWords(UUID setId, int page, int size);
    Mono<VocabularyWordsPaginationResponseDto> searchVocabularyWordByWord(UUID setId, String keyword, int page, int size);
    Mono<ActionResponseDto> uploadVocabularyWordImage(UUID setId, UUID wordId, FilePart file);
}
