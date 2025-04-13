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

        List<SavedSetResponse> setsResponse = sets.getContent().stream()
                .map(SavedSetMapper::toSavedSetResponse)
                .toList();

        SavedSetsPaginationResponse response = SavedSetsPaginationResponse.newBuilder()
                .addAllSets(setsResponse)
                .setTotalItems(sets.getTotalElements())
                .setTotalPages(sets.getTotalPages())
                .build();

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
    public void getCachedSetById(GetCachedSetByIdRequest request, StreamObserver<CachedSetResponse> responseObserver){
        CachedSet set = cachedSetService.getCachedSetById(request);
        CachedSetResponse response = CachedSetMapper.toCachedSetResponse(set);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllCachedSets(GetAllCachedSetsRequest request, StreamObserver<CachedSetsResponse> responseObserver){
        List<CachedSet> sets = cachedSetService.getAllCachedSets(request);

        List<CachedSetResponse> setsResponse = sets.stream()
                .map(CachedSetMapper::toCachedSetResponse)
                .toList();

        CachedSetsResponse response = CachedSetsResponse.newBuilder()
                .addAllSets(setsResponse)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateCachedSet(UpdateCachedSetRequest request, StreamObserver<CachedSetResponse> responseObserver){
        CachedSet set = cachedSetService.updateCachedSet(request);
        CachedSetResponse response = CachedSetMapper.toCachedSetResponse(set);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteCachedSet(DeleteCachedSetRequest request, StreamObserver<ActionResponse> responseObserver){
        cachedSetService.deleteCachedSet(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Delete success.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
