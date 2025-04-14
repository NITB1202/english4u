package com.nitb.apigateway.service.Vocabulary;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.Vocabulary.request.AddVocabularyWordsRequestDto;
import com.nitb.apigateway.dto.Vocabulary.request.DeleteVocabularyWordsRequestDto;
import com.nitb.apigateway.dto.Vocabulary.request.UpdateVocabularyWordRequestDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularyWordResponseDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularyWordsPaginationResponseDto;
import com.nitb.apigateway.grpc.VocabularyServiceGrpcClient;
import com.nitb.apigateway.mapper.ActionMapper;
import com.nitb.apigateway.mapper.VocabularyWordMapper;
import com.nitb.common.grpc.ActionResponse;
import com.nitb.vocabularyservice.grpc.VocabularyWordResponse;
import com.nitb.vocabularyservice.grpc.VocabularyWordsPaginationResponse;
import com.nitb.vocabularyservice.grpc.VocabularyWordsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VocabularyWordServiceImpl implements VocabularyWordService {
    private final VocabularyServiceGrpcClient grpcClient;

    @Override
    public Mono<List<VocabularyWordResponseDto>> addWordsToSet(UUID userId, AddVocabularyWordsRequestDto dto) {
        return Mono.fromCallable(() -> {
            VocabularyWordsResponse response = grpcClient.createVocabularyWords(dto.getSetId(), userId, dto.getWords());
            return response.getWordsList().stream()
                    .map(VocabularyWordMapper::toVocabularyWordResponseDto)
                    .toList();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<VocabularyWordsPaginationResponseDto> getVocabularyWords(UUID setId, int page, int size) {
        return Mono.fromCallable(()->{
            VocabularyWordsPaginationResponse response = grpcClient.getVocabularyWords(setId, page, size);
            List<VocabularyWordResponseDto> words = response.getWordsList().stream()
                    .map(VocabularyWordMapper::toVocabularyWordResponseDto)
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
                    .map(VocabularyWordMapper::toVocabularyWordResponseDto)
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
            return VocabularyWordMapper.toVocabularyWordResponseDto(word);
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
    public Mono<ActionResponseDto> deleteVocabularyWords(UUID userId, DeleteVocabularyWordsRequestDto dto) {
        return Mono.fromCallable(()->{
            ActionResponse response = grpcClient.deleteVocabularyWords(dto.getSetId(), userId, dto.getIds());
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public boolean uploadVocabularyWordImage(UUID id, UUID userId, String imageUrl) {
        ActionResponse response = grpcClient.uploadVocabularyWordImage(id, userId, imageUrl);
        return response.getSuccess();
    }
}
