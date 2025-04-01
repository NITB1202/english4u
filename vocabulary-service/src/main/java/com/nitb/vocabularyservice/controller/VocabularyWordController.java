package com.nitb.vocabularyservice.controller;

import com.nitb.common.grpc.ActionResponse;
import com.nitb.vocabularyservice.entity.VocabularyWord;
import com.nitb.vocabularyservice.grpc.*;
import com.nitb.vocabularyservice.mapper.VocabularyWordMapper;
import com.nitb.vocabularyservice.service.VocabularyWordService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;

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
        Page<VocabularyWord> words = vocabularyWordService.getVocabularyWords(request);
        List<VocabularyWordResponse> wordResponses = words.stream()
                .map(VocabularyWordMapper::toVocabularyWordResponse)
                .toList();

        VocabularyWordsPaginationResponse response = VocabularyWordsPaginationResponse.newBuilder()
                .addAllWords(wordResponses)
                .setTotalItems(words.getTotalElements())
                .setTotalPage(words.getTotalPages())
                .build();

        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void searchVocabularyWordByWord(SearchVocabularyWordByWordRequest request, StreamObserver<VocabularyWordsPaginationResponse> streamObserver){
        Page<VocabularyWord> words = vocabularyWordService.searchVocabularyWordByWord(request);
        List<VocabularyWordResponse> wordResponses = words.stream()
                .map(VocabularyWordMapper::toVocabularyWordResponse)
                .toList();

        VocabularyWordsPaginationResponse response = VocabularyWordsPaginationResponse.newBuilder()
                .addAllWords(wordResponses)
                .setTotalItems(words.getTotalElements())
                .setTotalPage(words.getTotalPages())
                .build();

        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void updateVocabularyWord(UpdateVocabularyWordRequest request, StreamObserver<VocabularyWordResponse> streamObserver){
        VocabularyWord word = vocabularyWordService.updateVocabularyWord(request);
        VocabularyWordResponse response = VocabularyWordMapper.toVocabularyWordResponse(word);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void switchWordPosition(SwitchWordPositionRequest request, StreamObserver<ActionResponse> streamObserver){
        vocabularyWordService.switchWordPosition(request);
        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Switch successfully.")
                .build();

        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void uploadVocabularyWordImage(UploadVocabularyWordImageRequest request, StreamObserver<ActionResponse> streamObserver){
        vocabularyWordService.uploadVocabularyWordImage(request);
        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Upload successfully.")
                .build();

        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void deleteVocabularyWords(DeleteVocabularyWordsRequest request, StreamObserver<ActionResponse> streamObserver){
        vocabularyWordService.deleteVocabularyWords(request);
        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Delete successfully.")
                .build();

        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }
}
