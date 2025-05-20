package com.nitb.apigateway.service.Vocabulary;

import com.nitb.apigateway.dto.Vocabulary.response.VocabularyWordsPaginationResponseDto;
import com.nitb.apigateway.grpc.VocabularyServiceGrpcClient;
import com.nitb.apigateway.mapper.VocabularyWordMapper;
import com.nitb.vocabularyservice.grpc.VocabularyWordsPaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VocabularyWordServiceImpl implements VocabularyWordService {
    private final VocabularyServiceGrpcClient grpcClient;

    @Override
    public Mono<VocabularyWordsPaginationResponseDto> getVocabularyWords(UUID setId, int page, int size) {
        return Mono.fromCallable(()->{
            VocabularyWordsPaginationResponse pagination = grpcClient.getVocabularyWords(setId, page, size);
            return VocabularyWordMapper.vocabularyWordsPaginationResponseDto(pagination);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<VocabularyWordsPaginationResponseDto> searchVocabularyWordByWord(UUID setId, String keyword, int page, int size) {
        return Mono.fromCallable(()->{
            VocabularyWordsPaginationResponse pagination = grpcClient.searchVocabularyWordByWord(keyword, setId, page, size);
            return VocabularyWordMapper.vocabularyWordsPaginationResponseDto(pagination);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
