package com.nitb.vocabularyservice.controller;

import com.nitb.vocabularyservice.entity.VocabularySet;
import com.nitb.vocabularyservice.mapper.VocabularySetMapper;
import com.nitb.vocabularyservice.service.VocabularySetService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import com.nitb.vocabularyservice.grpc.*;

@GrpcService
@RequiredArgsConstructor
public class VocabularySetController extends VocabularyServiceGrpc.VocabularyServiceImplBase {
    private final VocabularySetService vocabularySetService;

    @Override
    public void createVocabularySet(CreateVocabularySetRequest request, StreamObserver<VocabularySetResponse> streamObserver) {
        VocabularySet set = vocabularySetService.createVocabularySet(request);
        VocabularySetResponse response = VocabularySetMapper.toVocabularySetResponse(set);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void getVocabularySetById(GetVocabularySetByIdRequest request, StreamObserver<VocabularySetResponse> streamObserver) {

    }

    @Override
    public void getVocabularySets(GetVocabularySetsRequest request, StreamObserver<VocabularySetsResponse> streamObserver){

    }

    @Override
    public void searchVocabularySetByName(SearchVocabularySetByNameRequest request, StreamObserver<VocabularySetsResponse> streamObserver){

    }

    @Override
    public void updateVocabularySet(UpdateVocabularySetRequest request, StreamObserver<VocabularySetResponse> streamObserver){

    }

    @Override
    public void deleteVocabularySet(DeleteVocabularySetRequest request, StreamObserver<VocabularySetResponse> streamObserver){

    }
}
