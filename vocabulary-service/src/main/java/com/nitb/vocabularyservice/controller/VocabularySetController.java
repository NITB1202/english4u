package com.nitb.vocabularyservice.controller;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import com.nitb.vocabularyservice.grpc.*;

@GrpcService
@RequiredArgsConstructor
public class VocabularySetController extends VocabularyServiceGrpc.VocabularyServiceImplBase {

    @Override
    public void createVocabularySet(CreateVocabularySetRequest request, StreamObserver<VocabularySetResponse> streamObserver) {

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
