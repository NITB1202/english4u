package com.nitb.apigateway.service.Vocabulary;

import com.nitb.apigateway.dto.Vocabulary.request.CreateVocabularySetRequestDto;
import com.nitb.apigateway.dto.Vocabulary.request.UpdateVocabularySetRequestDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularySetDetailResponseDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularySetSummaryResponse;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularySetsPaginationResponseDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularyWordResponseDto;
import com.nitb.apigateway.grpc.VocabularyServiceGrpcClient;
import com.nitb.apigateway.mapper.VocabularyWordMapper;
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
    public Mono<VocabularySetDetailResponseDto> createVocabularySet(UUID userId, CreateVocabularySetRequestDto request) {
        return Mono.fromCallable(()->{
            VocabularySetResponse set = grpcClient.createVocabularySet(userId, request.getName());
            VocabularySetSummaryResponse setResponse = VocabularySetMapper.toVocabularySetResponseDto(set);

            List<VocabularyWordResponseDto> wordsResponse = null;

            if(request.getWords() != null) {
                VocabularyWordsResponse words = grpcClient.createVocabularyWords(UUID.fromString(set.getId()), userId, request.getWords());
                wordsResponse = words.getWordsList().stream()
                        .map(VocabularyWordMapper::toVocabularyWordResponseDto)
                        .toList();

                setResponse.setWordCount(wordsResponse.size());
            }

            return VocabularySetDetailResponseDto.builder()
                    .set(setResponse)
                    .words(wordsResponse)
                    .build();

        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<VocabularySetSummaryResponse> getVocabularySetById(UUID id) {
        return Mono.fromCallable(()->{
            VocabularySetResponse set = grpcClient.getVocabularySetById(id);
            return VocabularySetMapper.toVocabularySetResponseDto(set);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<VocabularySetsPaginationResponseDto> getVocabularySets(int page, int size) {
        return Mono.fromCallable(()->{
            VocabularySetsResponse sets = grpcClient.getVocabularySets(page, size);
            List<VocabularySetSummaryResponse> setsResponse = sets.getSetsList()
                    .stream()
                    .map(VocabularySetMapper::toVocabularySetResponseDto)
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
            List<VocabularySetSummaryResponse> setsResponse = sets.getSetsList()
                    .stream()
                    .map(VocabularySetMapper::toVocabularySetResponseDto)
                    .toList();

            return VocabularySetsPaginationResponseDto.builder()
                    .sets(setsResponse)
                    .totalItems(sets.getTotalItems())
                    .totalPages(sets.getTotalPages())
                    .build();

        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<VocabularySetSummaryResponse> updateVocabularySet(UUID id, UUID userId, UpdateVocabularySetRequestDto request) {
        return Mono.fromCallable(()->{
            VocabularySetResponse set = grpcClient.updateVocabularySet(id, userId, request);
            return VocabularySetMapper.toVocabularySetResponseDto(set);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<VocabularySetSummaryResponse> deleteVocabularySet(UUID id, UUID userId) {
        return Mono.fromCallable(()->{
            VocabularySetResponse set = grpcClient.deleteVocabularySet(id, userId);
            return VocabularySetMapper.toVocabularySetResponseDto(set);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
