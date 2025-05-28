package com.nitb.apigateway.service.Vocabulary;

import com.nitb.apigateway.dto.Vocabulary.request.CreateVocabularySetRequestDto;
import com.nitb.apigateway.dto.Vocabulary.request.UpdateVocabularySetRequestDto;
import com.nitb.apigateway.dto.Vocabulary.response.*;
import com.nitb.apigateway.grpc.UserServiceGrpcClient;
import com.nitb.apigateway.grpc.VocabularyServiceGrpcClient;
import com.nitb.apigateway.mapper.VocabularySetMapper;
import com.nitb.common.enums.GroupBy;
import com.nitb.userservice.grpc.UserResponse;
import com.nitb.vocabularyservice.grpc.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VocabularySetServiceImpl implements VocabularySetService {
    private final VocabularyServiceGrpcClient grpcClient;
    private final UserServiceGrpcClient userClient;

    @Override
    public Mono<CreateVocabularySetResponseDto> createVocabularySet(UUID userId, CreateVocabularySetRequestDto request) {
        return Mono.fromCallable(()->{
            //Check user permission
            userClient.checkCanPerformAction(userId);

            CreateVocabularySetResponse set = grpcClient.createVocabularySet(userId, request.getName());

            UUID setId = UUID.fromString(set.getId());
            VocabularyWordsResponse words = grpcClient.createVocabularyWords(setId, userId, request.getWords());

            return VocabularySetMapper.toCreateVocabularySetResponseDto(set, words);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<VocabularySetDetailResponseDto> getVocabularySetById(UUID id) {
        return Mono.fromCallable(()->{
            VocabularySetDetailResponse set = grpcClient.getVocabularySetById(id);

            UUID createdById = UUID.fromString(set.getCreatedBy());
            UUID updatedById = UUID.fromString(set.getUpdatedBy());

            UserResponse createdBy = userClient.getUserById(createdById);
            UserResponse updatedBy = userClient.getUserById(updatedById);

            return VocabularySetMapper.toVocabularySetDetailResponseDto(set, createdBy, updatedBy);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<VocabularySetsPaginationResponseDto> getVocabularySets(int page, int size) {
        return Mono.fromCallable(()->{
            VocabularySetsResponse sets = grpcClient.getVocabularySets(page, size);
            return VocabularySetMapper.toVocabularySetsPaginationResponseDto(sets);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<VocabularySetsPaginationResponseDto> getDeletedVocabularySets(int page, int size) {
        return Mono.fromCallable(()->{
            VocabularySetsResponse sets = grpcClient.getDeletedVocabularySets(page, size);
            return VocabularySetMapper.toVocabularySetsPaginationResponseDto(sets);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<VocabularySetsPaginationResponseDto> searchVocabularySetByName(String keyword, int page, int size) {
        return Mono.fromCallable(()-> {
            VocabularySetsResponse sets = grpcClient.searchVocabularySetByName(keyword, page, size);
            return VocabularySetMapper.toVocabularySetsPaginationResponseDto(sets);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<VocabularySetsPaginationResponseDto> searchDeletedVocabularySetByName(String keyword, int page, int size) {
        return Mono.fromCallable(()->{
            VocabularySetsResponse sets = grpcClient.searchDeletedVocabularySets(keyword, page, size);
            return VocabularySetMapper.toVocabularySetsPaginationResponseDto(sets);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UpdateVocabularySetNameResponseDto> updateVocabularySetName(UUID id, UUID userId, String name) {
        return Mono.fromCallable(()->{
            userClient.checkCanPerformAction(userId);
            UpdateVocabularySetResponse response = grpcClient.updateVocabularySetName(id, userId, name);
            return VocabularySetMapper.toUpdateVocabularySetNameResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UpdateVocabularySetResponseDto> updateVocabularySet(UUID id, UUID userId, UpdateVocabularySetRequestDto request) {
        return Mono.fromCallable(()->{
            //Check user permission
            userClient.checkCanPerformAction(userId);

            //Check if the vocabulary set has reached the max version.
            grpcClient.validateUpdateVocabularySet(id);

            //Soft-delete the current version
            VocabularySetDetailResponse setDetail = grpcClient.getVocabularySetById(id);
            grpcClient.deleteVocabularySet(id, userId);

            //Create a new version
            CreateVocabularySetResponse updatedSet = grpcClient.createVocabularySet(userId, setDetail.getName());
            UUID updatedSetId = UUID.fromString(updatedSet.getId());
            VocabularyWordsResponse words = grpcClient.createVocabularyWords(updatedSetId, userId, request.getWords());

            //Preserve the original creation info when creating a new version.
            UpdateVocabularySetResponse set = grpcClient.updateVocabularySet(id, updatedSetId);
            return VocabularySetMapper.toUpdateVocabularySetResponseDto(set, words);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<DeleteVocabularySetResponseDto> deleteVocabularySet(UUID id, UUID userId) {
        return Mono.fromCallable(()->{
            userClient.checkCanPerformAction(userId);
            DeleteVocabularySetResponse set = grpcClient.deleteVocabularySet(id, userId);
            return VocabularySetMapper.toDeleteVocabularySetResponseDto(set);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<DeleteVocabularySetResponseDto> restoreVocabularySet(UUID id, UUID userId) {
        return Mono.fromCallable(()->{
            userClient.checkCanPerformAction(userId);
            DeleteVocabularySetResponse set = grpcClient.restoreVocabularySet(id, userId);
            return VocabularySetMapper.toDeleteVocabularySetResponseDto(set);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<VocabularySetStatisticsResponseDto> getVocabularySetStatistics(UUID userId, LocalDate from , LocalDate to, GroupBy groupBy) {
        return Mono.fromCallable(()->{
            CountPublishedVocabularySetsResponse response = grpcClient.countPublishedVocabularySets(userId, from, to, groupBy);
            return VocabularySetMapper.toVocabularySetStatisticsResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
