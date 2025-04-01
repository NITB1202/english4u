package com.nitb.apigateway.service.Vocabulary;

import com.nitb.apigateway.dto.Vocabulary.request.CreateVocabularySetRequestDto;
import com.nitb.apigateway.dto.Vocabulary.request.UpdateVocabularySetRequestDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularySetDetailResponseDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularySetResponseDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularySetsPaginationResponseDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularyWordResponseDto;
import com.nitb.apigateway.grpc.VocabularyServiceGrpcClient;
import com.nitb.apigateway.mapper.VocabularyMapper;
import com.nitb.vocabularyservice.grpc.VocabularySetResponse;
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
    public Mono<VocabularySetDetailResponseDto> createVocabularySet(CreateVocabularySetRequestDto request) {
        return Mono.fromCallable(()->{
            VocabularySetResponse set = grpcClient.createVocabularySet(request.getUserId(), request.getName());
            VocabularyWordsResponse words = grpcClient.createVocabularyWords(UUID.fromString(set.getId()), request.getUserId(), request.getWords());

            VocabularySetResponseDto setResponse = VocabularyMapper.toVocabularySetResponseDto(set);
            List<VocabularyWordResponseDto> wordsResponse = words.getWordsList().stream()
                    .map(VocabularyMapper::toVocabularyWordResponseDto)
                    .toList();

            return VocabularySetDetailResponseDto.builder()
                    .set(setResponse)
                    .words(wordsResponse)
                    .build();

        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<VocabularySetDetailResponseDto> getVocabularySetById(UUID id) {
        return null;
    }

    @Override
    public Mono<VocabularySetsPaginationResponseDto> getVocabularySets(int page, int size) {
        return null;
    }

    @Override
    public Mono<VocabularySetsPaginationResponseDto> searchVocabularySetByName(String keyword, int page, int size) {
        return null;
    }

    @Override
    public Mono<VocabularySetResponseDto> updateVocabularySet(UUID id, UpdateVocabularySetRequestDto request) {
        return null;
    }

    @Override
    public Mono<VocabularySetResponseDto> deleteVocabularySet(UUID id) {
        return null;
    }
}
