package com.nitb.apigateway.service.UserVocabulary;

import com.nitb.apigateway.dto.UserVocabulary.request.CreateCachedSetRequestDto;
import com.nitb.apigateway.dto.UserVocabulary.response.CachedSetDetailResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.response.CachedSetResponseDto;
import com.nitb.apigateway.grpc.UserVocabularyServiceGrpcClient;
import com.nitb.apigateway.grpc.VocabularyServiceGrpcClient;
import com.nitb.apigateway.mapper.CachedSetMapper;
import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.exceptions.NotFoundException;
import com.nitb.uservocabularyservice.grpc.CachedSetResponse;
import com.nitb.uservocabularyservice.grpc.CachedSetsResponse;
import com.nitb.vocabularyservice.grpc.VocabularySetResponse;
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
    public Mono<CachedSetResponseDto> cacheSet(UUID userId, CreateCachedSetRequestDto request) {
        return Mono.fromCallable(()->{
            VocabularySetResponse set = vocabularyGrpc.getVocabularySetById(request.getSetId());

            if(set == null) {
                throw new NotFoundException("Set not found.");
            }

            if(request.getLearnedWords() > set.getWordCount()){
                throw new BusinessException("Learned words exceeds maximum number of words.");
            }

            if(request.getLearnedWords() == set.getWordCount()){
                CachedSetResponse cachedSet = userVocabularyGrpc.getCachedSetByUserIdAndSetId(userId, request.getSetId());

                if(cachedSet != null) {
                    userVocabularyGrpc.deleteCachedSet(UUID.fromString(cachedSet.getId()));
                }

                return null;
            }

            CachedSetResponse response = userVocabularyGrpc.createCachedSet(userId, request);
            return CachedSetMapper.toCachedSetResponseDto(response);

        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<List<CachedSetDetailResponseDto>> getAllCachedSets(UUID userId) {
        return Mono.fromCallable(()->{
            CachedSetsResponse cachedSets = userVocabularyGrpc.getAllCachedSets(userId);

            List<CachedSetDetailResponseDto> responses = new ArrayList<>();

            for(CachedSetResponse cachedSet : cachedSets.getSetsList()){
                VocabularySetResponse set = vocabularyGrpc.getVocabularySetById(UUID.fromString(cachedSet.getSetId()));
                CachedSetDetailResponseDto response = CachedSetMapper.toCachedSetDetailResponseDto(cachedSet, set);
                responses.add(response);
            }

            return responses;

        }).subscribeOn(Schedulers.boundedElastic());
    }
}
