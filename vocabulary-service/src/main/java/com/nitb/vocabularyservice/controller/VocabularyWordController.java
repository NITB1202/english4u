package com.nitb.vocabularyservice.controller;

import com.nitb.common.grpc.ActionResponse;
import com.nitb.vocabularyservice.grpc.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class VocabularyWordController extends VocabularyServiceGrpc.VocabularyServiceImplBase {

    @Override
    public void createVocabularyWords(CreateVocabularyWordsRequest request, StreamObserver<VocabularyWordsResponse> streamObserver){

    }

    @Override
    public void getVocabularyWords(GetVocabularyWordsRequest request, StreamObserver<VocabularyWordsPaginationResponse> streamObserver){

    }

    @Override
    public void searchVocabularyWordByWord(SearchVocabularyWordByWordRequest request, StreamObserver<VocabularyWordsPaginationResponse> streamObserver){

    }

    @Override
    public void updateVocabularyWord(UpdateVocabularyWordRequest request, StreamObserver<VocabularyWordResponse> streamObserver){

    }

    @Override
    public void switchWordPosition(SwitchWordPositionRequest request, StreamObserver<ActionResponse> streamObserver){

    }

    @Override
    public void uploadVocabularyWordImage(UploadVocabularyWordImageRequest request, StreamObserver<ActionResponse> streamObserver){

    }

    @Override
    public void deleteVocabularyWords(DeleteVocabularyWordsRequest request, StreamObserver<ActionResponse> streamObserver){

    }
}
