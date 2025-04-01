package com.nitb.vocabularyservice.controller;

import com.nitb.common.grpc.ActionResponse;
import com.nitb.vocabularyservice.entity.VocabularyWord;
import com.nitb.vocabularyservice.grpc.*;
import com.nitb.vocabularyservice.mapper.VocabularyWordMapper;
import com.nitb.vocabularyservice.service.VocabularyWordService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class VocabularyWordController extends VocabularyServiceGrpc.VocabularyServiceImplBase {
    private final VocabularyWordService vocabularyWordService;

    @Override
    public void createVocabularyWords(CreateVocabularyWordsRequest request, StreamObserver<VocabularyWordsResponse> streamObserver){
        List<VocabularyWord> words = vocabularyWordService.createVocabularyWords(request);
        List<VocabularyWordResponse> wordResponses = words.stream()
                .map(VocabularyWordMapper::toVocabularyWordResponse)
                .toList();

        VocabularyWordsResponse response = VocabularyWordsResponse.newBuilder()
                .addAllWords(wordResponses)
                .build();

        streamObserver.onNext(response);
        streamObserver.onCompleted();
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
