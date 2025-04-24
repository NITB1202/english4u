package com.nitb.apigateway.service.Vocabulary;

import com.nitb.apigateway.dto.Vocabulary.request.CreateVocabularySetRequestDto;
import com.nitb.apigateway.dto.Vocabulary.request.UpdateVocabularySetRequestDto;
import com.nitb.apigateway.dto.Vocabulary.response.*;
import com.nitb.apigateway.grpc.VocabularyServiceGrpcClient;
import com.nitb.apigateway.mapper.VocabularySetMapper;
import com.nitb.vocabularyservice.grpc.VocabularySetResponse;
import com.nitb.vocabularyservice.grpc.VocabularySetsResponse;
import com.nitb.vocabularyservice.grpc.VocabularyWordsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VocabularySetServiceImpl implements VocabularySetService {
    private final VocabularyServiceGrpcClient grpcClient;

    @Override
    public Mono<VocabularySetWithWordsResponseDto> createVocabularySet(UUID userId, CreateVocabularySetRequestDto request) {
        return Mono.fromCallable(()->{
            VocabularySetResponse set = grpcClient.createVocabularySet(userId, request.getName());
            VocabularyWordsResponse words = request.getWords() != null ?
                    grpcClient.createVocabularyWords(UUID.fromString(set.getId()), userId, request.getWords())
                    : null;

            return VocabularySetMapper.toVocabularySetWithWordsResponse(set, words);

        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<VocabularySetDetailResponseDto> getVocabularySetById(UUID id) {
        return Mono.fromCallable(()->{
            VocabularySetResponse set = grpcClient.getVocabularySetById(id);
            return VocabularySetMapper.toVocabularySetDetailResponseDto(set);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<VocabularySetsPaginationResponseDto> getVocabularySets(int page, int size) {
        return Mono.fromCallable(()->{
            VocabularySetsResponse sets = grpcClient.getVocabularySets(page, size);
            List<VocabularySetSummaryResponseDto> setsResponse = sets.getSetsList()
                    .stream()
                    .map(VocabularySetMapper::toVocabularySetSummaryResponseDto)
                    .toList();

            return VocabularySetsPaginationResponseDto.builder()
                    .sets(setsResponse)
                    .totalItems(sets.getTotalItems())
                    .totalPages(sets.getTotalPages())
                    .build();

        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<VocabularySetsPaginationResponseDto> searchVocabularySetByName(String keyword, int page, int size) {
        return Mono.fromCallable(()-> {
            VocabularySetsResponse sets = grpcClient.searchVocabularySetByName(keyword, page, size);
            List<VocabularySetSummaryResponseDto> setsResponse = sets.getSetsList()
                    .stream()
                    .map(VocabularySetMapper::toVocabularySetSummaryResponseDto)
                    .toList();

            return VocabularySetsPaginationResponseDto.builder()
                    .sets(setsResponse)
                    .totalItems(sets.getTotalItems())
                    .totalPages(sets.getTotalPages())
                    .build();

        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UpdateVocabularySetResponseDto> updateVocabularySet(UUID id, UUID userId, UpdateVocabularySetRequestDto request) {
        return Mono.fromCallable(()->{
            VocabularySetResponse set = grpcClient.updateVocabularySet(id, userId, request);
            return VocabularySetMapper.toUpdateVocabularySetResponseDto(set);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<DeleteVocabularySetResponseDto> deleteVocabularySet(UUID id, UUID userId) {
        return Mono.fromCallable(()->{
            VocabularySetResponse set = grpcClient.deleteVocabularySet(id, userId);
            return VocabularySetMapper.toDeleteVocabularySetResponseDto(set);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
