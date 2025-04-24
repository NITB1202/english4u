package com.nitb.apigateway.service.UserVocabulary;

import com.nitb.apigateway.dto.General.DataWithMessageResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.request.CreateCachedSetRequestDto;
import com.nitb.apigateway.dto.UserVocabulary.response.CachedSetSummaryResponseDto;
import com.nitb.apigateway.grpc.UserVocabularyServiceGrpcClient;
import com.nitb.apigateway.grpc.VocabularyServiceGrpcClient;
import com.nitb.apigateway.mapper.CachedSetMapper;
import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.exceptions.NotFoundException;
import com.nitb.uservocabularyservice.grpc.CachedSetResponse;
import com.nitb.uservocabularyservice.grpc.CachedSetSummaryResponse;
import com.nitb.uservocabularyservice.grpc.CachedSetsResponse;
import com.nitb.vocabularyservice.grpc.VocabularySetDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CachedSetServiceImpl implements CachedSetService{
    private final UserVocabularyServiceGrpcClient userVocabularyGrpc;
    private final VocabularyServiceGrpcClient vocabularyGrpc;

    @Override
    public Mono<DataWithMessageResponseDto> cacheSet(UUID userId, CreateCachedSetRequestDto request) {
        return Mono.fromCallable(()->{
            VocabularySetDetailResponse set = vocabularyGrpc.getVocabularySetById(request.getSetId());

            if(set == null) {
                throw new NotFoundException("Set not found.");
            }

            if(request.getLearnedWords() > set.getWordCount()){
                throw new BusinessException("Learned words exceeds maximum number of words.");
            }

            //Done study -> Not cache, delete
            if(request.getLearnedWords() == set.getWordCount()){
                userVocabularyGrpc.deleteCachedSetIfExists(userId, request.getSetId());

                return DataWithMessageResponseDto.builder()
                        .message("The learned words have reached the maximum count, so the set no longer needs to be cached.")
                        .data(null)
                        .build();
            }

            CachedSetResponse response = userVocabularyGrpc.createCachedSet(userId, request);

            return DataWithMessageResponseDto.builder()
                    .message("Cache successfully.")
                    .data(CachedSetMapper.toCachedSetResponseDto(response))
                    .build();

        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<List<CachedSetSummaryResponseDto>> getAllCachedSets(UUID userId) {
        return Mono.fromCallable(()->{
            CachedSetsResponse cachedSets = userVocabularyGrpc.getAllCachedSets(userId);

            List<CachedSetSummaryResponseDto> responses = new ArrayList<>();

            for(CachedSetSummaryResponse cachedSet : cachedSets.getSetsList()){
                VocabularySetDetailResponse set = vocabularyGrpc.getVocabularySetById(UUID.fromString(cachedSet.getSetId()));
                CachedSetSummaryResponseDto response = CachedSetMapper.toCachedSetSummaryResponseDto(cachedSet, set);
                responses.add(response);
            }

            return responses;

        }).subscribeOn(Schedulers.boundedElastic());
    }
}
