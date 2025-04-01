package com.nitb.apigateway.service.Vocabulary;

import com.nitb.apigateway.dto.Action.ActionResponseDto;
import com.nitb.apigateway.dto.Vocabulary.request.CreateVocabularyWordRequestDto;
import com.nitb.apigateway.dto.Vocabulary.request.UpdateVocabularyWordRequestDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularyWordResponseDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularyWordsPaginationResponseDto;
import com.nitb.apigateway.grpc.VocabularyServiceGrpcClient;
import com.nitb.apigateway.mapper.ActionMapper;
import com.nitb.apigateway.mapper.VocabularyMapper;
import com.nitb.common.grpc.ActionResponse;
import com.nitb.vocabularyservice.grpc.VocabularyWordResponse;
import com.nitb.vocabularyservice.grpc.VocabularyWordsPaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VocabularyWordServiceImpl implements VocabularyWordService {
    private final VocabularyServiceGrpcClient grpcClient;

    @Override
    public Flux<VocabularyWordResponseDto> addWordsToSet(UUID setId, UUID userId, List<CreateVocabularyWordRequestDto> request) {
        return Mono.fromCallable(() -> grpcClient.createVocabularyWords(setId, userId, request))
                .flatMapMany(response -> Flux.fromIterable(response.getWordsList()))
                .map(VocabularyMapper::toVocabularyWordResponseDto)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<VocabularyWordsPaginationResponseDto> getVocabularyWords(UUID setId, int page, int size) {
        return Mono.fromCallable(()->{
            VocabularyWordsPaginationResponse response = grpcClient.getVocabularyWords(setId, page, size);
            List<VocabularyWordResponseDto> words = response.getWordsList().stream()
                    .map(VocabularyMapper::toVocabularyWordResponseDto)
                    .toList();

            return VocabularyWordsPaginationResponseDto.builder()
                    .words(words)
                    .totalItems(response.getTotalItems())
                    .totalPages(response.getTotalPage())
                    .build();

        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<VocabularyWordsPaginationResponseDto> searchVocabularyWordByWord(UUID setId, String keyword, int page, int size) {
        return Mono.fromCallable(()->{
            VocabularyWordsPaginationResponse response = grpcClient.searchVocabularyWordByWord(keyword, setId, page, size);
            List<VocabularyWordResponseDto> words = response.getWordsList().stream()
                    .map(VocabularyMapper::toVocabularyWordResponseDto)
                    .toList();

            return VocabularyWordsPaginationResponseDto.builder()
                    .words(words)
                    .totalItems(response.getTotalItems())
                    .totalPages(response.getTotalPage())
                    .build();

        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<VocabularyWordResponseDto> updateVocabularyWord(UUID id, UUID userId, UpdateVocabularyWordRequestDto request) {
        return Mono.fromCallable(()->{
            VocabularyWordResponse word = grpcClient.updateVocabularyWord(id, userId, request);
            return VocabularyMapper.toVocabularyWordResponseDto(word);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> switchWordPosition(UUID userId, UUID word1Id, UUID word2Id) {
        return Mono.fromCallable(()->{
            ActionResponse response = grpcClient.switchWordPosition(word1Id, word2Id, userId);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> deleteVocabularyWords(UUID setId, UUID userId, List<UUID> ids) {
        return Mono.fromCallable(()->{
            ActionResponse response = grpcClient.deleteVocabularyWords(setId, userId, ids);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public boolean uploadVocabularyWordImage(UUID id, UUID userId, String imageUrl) {
        ActionResponse response = grpcClient.uploadVocabularyWordImage(id, userId, imageUrl);
        return response.getSuccess();
    }
}
