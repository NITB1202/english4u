package com.nitb.uservocabularyservice.controller;

import com.nitb.common.grpc.ActionResponse;
import com.nitb.uservocabularyservice.entity.CachedSet;
import com.nitb.uservocabularyservice.entity.SavedSet;
import com.nitb.uservocabularyservice.grpc.*;
import com.nitb.uservocabularyservice.mapper.CachedSetMapper;
import com.nitb.uservocabularyservice.mapper.SavedSetMapper;
import com.nitb.uservocabularyservice.service.CachedSetService;
import com.nitb.uservocabularyservice.service.SavedSetService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class UserVocabularyController extends UserVocabularyServiceGrpc.UserVocabularyServiceImplBase {
    private final SavedSetService savedSetService;
    private final CachedSetService cachedSetService;

    //Saved sets
    @Override
    public void createSavedSet(CreateSavedSetRequest request, StreamObserver<SavedSetResponse> responseObserver){
        SavedSet set = savedSetService.createSavedSet(request);
        SavedSetResponse response = SavedSetMapper.toSavedSetResponse(set);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getSavedSetById(GetSavedSetByIdRequest request, StreamObserver<SavedSetResponse> responseObserver){
        SavedSet set = savedSetService.getSavedSetById(request);
        SavedSetResponse response = SavedSetMapper.toSavedSetResponse(set);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getSavedSets(GetSavedSetsRequest request, StreamObserver<SavedSetsPaginationResponse> responseObserver){
        Page<SavedSet> sets = savedSetService.getSavedSets(request);
        SavedSetsPaginationResponse response = SavedSetMapper.toSavedSetsPaginationResponse(sets);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllSavedSets(GetAllSavedSetsRequest request, StreamObserver<SavedSetsResponse> responseObserver){
        List<SavedSet> sets = savedSetService.getAllSavedSets(request);

        List<SavedSetResponse> setsResponse = sets.stream()
                .map(SavedSetMapper::toSavedSetResponse)
                .toList();

        SavedSetsResponse response = SavedSetsResponse.newBuilder()
                .addAllSets(setsResponse)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateSavedSet(UpdateSavedSetRequest request, StreamObserver<SavedSetResponse> responseObserver){
        SavedSet set = savedSetService.updateSavedSet(request);
        SavedSetResponse response = SavedSetMapper.toSavedSetResponse(set);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteSavedSet(DeleteSavedSetRequest request, StreamObserver<ActionResponse> responseObserver){
        savedSetService.deleteSavedSet(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Delete success.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    //Cached sets
    @Override
    public void createCachedSet(CreateCachedSetRequest request, StreamObserver<CachedSetResponse> responseObserver){
        CachedSet cachedSet = cachedSetService.createCachedSet(request);
        CachedSetResponse response = CachedSetMapper.toCachedSetResponse(cachedSet);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllCachedSets(GetAllCachedSetsRequest request, StreamObserver<CachedSetsResponse> responseObserver){
        List<CachedSet> sets = cachedSetService.getAllCachedSets(request);

        List<CachedSetSummaryResponse> setsResponse = sets.stream()
                .map(CachedSetMapper::toCachedSetSummaryResponse)
                .toList();

        CachedSetsResponse response = CachedSetsResponse.newBuilder()
                .addAllSets(setsResponse)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteCachedSetIfExists(DeleteCachedSetIfExistsRequest request, StreamObserver<ActionResponse> responseObserver){
        boolean success = cachedSetService.deleteCachedSetIfExists(request);
        String message = success ? "Delete success." : "Cached set does not exist.";

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(success)
                .setMessage(message)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
