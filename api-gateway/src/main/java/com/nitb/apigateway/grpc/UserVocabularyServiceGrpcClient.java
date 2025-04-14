package com.nitb.apigateway.grpc;

import com.nitb.apigateway.dto.UserVocabulary.request.CreateCachedSetRequestDto;
import com.nitb.apigateway.dto.UserVocabulary.request.CreateSavedSetRequestDto;
import com.nitb.apigateway.dto.UserVocabulary.request.UpdateSavedSetRequestDto;
import com.nitb.common.grpc.ActionResponse;
import com.nitb.uservocabularyservice.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserVocabularyServiceGrpcClient {
    @GrpcClient("user-vocabulary-service")
    private UserVocabularyServiceGrpc.UserVocabularyServiceBlockingStub blockingStub;

    //Saved sets
    public SavedSetResponse createSavedSet(UUID userId, CreateSavedSetRequestDto dto) {
        CreateSavedSetRequest request = CreateSavedSetRequest.newBuilder()
                .setUserId(userId.toString())
                .setSetId(dto.getSetId().toString())
                .setLearnedWords(dto.getLearnedWords())
                .build();

        return blockingStub.createSavedSet(request);
    }

    public SavedSetResponse getSavedSetById(UUID id) {
        GetSavedSetByIdRequest request = GetSavedSetByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        return blockingStub.getSavedSetById(request);
    }

    public SavedSetsPaginationResponse getSavedSets(UUID userId, int page, int size) {
        GetSavedSetsRequest request = GetSavedSetsRequest.newBuilder()
                .setUserId(userId.toString())
                .setPage(page)
                .setSize(size)
                .build();

        return blockingStub.getSavedSets(request);
    }

    public SavedSetsResponse getAllSavedSets(UUID userId) {
        GetAllSavedSetsRequest request = GetAllSavedSetsRequest.newBuilder()
                .setUserId(userId.toString())
                .build();

        return blockingStub.getAllSavedSets(request);
    }

    public SavedSetResponse updateSavedSet(UUID id, UpdateSavedSetRequestDto dto) {
        UpdateSavedSetRequest request = UpdateSavedSetRequest.newBuilder()
                .setId(id.toString())
                .setLearnedWords(dto.getLearnedWords())
                .build();

        return blockingStub.updateSavedSet(request);
    }

    public ActionResponse deleteSavedSet(UUID id) {
        DeleteSavedSetRequest request = DeleteSavedSetRequest.newBuilder()
                .setId(id.toString())
                .build();

        return blockingStub.deleteSavedSet(request);
    }

    //Cached sets
    public CachedSetResponse createCachedSet(UUID userId, CreateCachedSetRequestDto dto) {
        CreateCachedSetRequest request = CreateCachedSetRequest.newBuilder()
                .setUserId(userId.toString())
                .setSetId(dto.getSetId().toString())
                .setLearnedWords(dto.getLearnedWords())
                .build();

        return blockingStub.createCachedSet(request);
    }

    public CachedSetsResponse getAllCachedSets(UUID userId) {
        GetAllCachedSetsRequest request = GetAllCachedSetsRequest.newBuilder()
                .setUserId(userId.toString())
                .build();

        return blockingStub.getAllCachedSets(request);
    }

    public ActionResponse deleteCachedSetIfExists(UUID userId, UUID setId) {
        DeleteCachedSetIfExistsRequest request = DeleteCachedSetIfExistsRequest.newBuilder()
                .setUserId(userId.toString())
                .setSetId(setId.toString())
                .build();

        return blockingStub.deleteCachedSetIfExists(request);
    }
}
