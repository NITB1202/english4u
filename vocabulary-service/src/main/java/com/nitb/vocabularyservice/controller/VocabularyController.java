package com.nitb.vocabularyservice.controller;

import com.google.protobuf.Empty;
import com.nitb.common.grpc.ActionResponse;
import com.nitb.vocabularyservice.entity.VocabularySet;
import com.nitb.vocabularyservice.entity.VocabularyWord;
import com.nitb.vocabularyservice.grpc.*;
import com.nitb.vocabularyservice.mapper.VocabularySetMapper;
import com.nitb.vocabularyservice.mapper.VocabularyWordMapper;
import com.nitb.vocabularyservice.service.VocabularySetService;
import com.nitb.vocabularyservice.service.VocabularyWordService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class VocabularyController extends VocabularyServiceGrpc.VocabularyServiceImplBase {
    private final VocabularyWordService vocabularyWordService;
    private final VocabularySetService vocabularySetService;

    //Set section
    @Override
    public void createVocabularySet(CreateVocabularySetRequest request, StreamObserver<CreateVocabularySetResponse> streamObserver) {
        VocabularySet set = vocabularySetService.createVocabularySet(request);
        CreateVocabularySetResponse response = VocabularySetMapper.toCreateVocabularySetResponse(set);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void getVocabularySetById(GetVocabularySetByIdRequest request, StreamObserver<VocabularySetDetailResponse> streamObserver) {
        VocabularySet set = vocabularySetService.getVocabularySetById(request);
        VocabularySetDetailResponse response = VocabularySetMapper.toVocabularySetDetailResponse(set);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void getVocabularySets(GetVocabularySetsRequest request, StreamObserver<VocabularySetsResponse> streamObserver){
        Page<VocabularySet> sets = vocabularySetService.getVocabularySets(request);
        VocabularySetsResponse response = VocabularySetMapper.toVocabularySetsResponse(sets);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void getDeletedVocabularySets(GetVocabularySetsRequest request, StreamObserver<VocabularySetsResponse> streamObserver) {
        Page<VocabularySet> sets = vocabularySetService.getDeletedVocabularySets(request);
        VocabularySetsResponse response = VocabularySetMapper.toVocabularySetsResponse(sets);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void searchVocabularySetByName(SearchVocabularySetByNameRequest request, StreamObserver<VocabularySetsResponse> streamObserver){
        Page<VocabularySet> sets = vocabularySetService.searchVocabularySetByName(request);
        VocabularySetsResponse response = VocabularySetMapper.toVocabularySetsResponse(sets);
        streamObserver.onNext(response);
        streamObserver.onCompleted();

    }

    @Override
    public void searchDeletedVocabularySetByName(SearchVocabularySetByNameRequest request, StreamObserver<VocabularySetsResponse> streamObserver) {
        Page<VocabularySet> sets = vocabularySetService.searchDeletedVocabularySetByName(request);
        VocabularySetsResponse response = VocabularySetMapper.toVocabularySetsResponse(sets);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void validateUpdateVocabularySet(ValidateUpdateVocabularySetRequest request, StreamObserver<Empty> streamObserver) {
        vocabularySetService.validateUpdateVocabularySet(request);
        streamObserver.onNext(Empty.getDefaultInstance());
        streamObserver.onCompleted();
    }

    @Override
    public void updateVocabularySet(UpdateVocabularySetRequest request, StreamObserver<UpdateVocabularySetResponse> streamObserver){
        VocabularySet set = vocabularySetService.updateVocabularySet(request);
        UpdateVocabularySetResponse response = VocabularySetMapper.toUpdateVocabularySetResponse(set);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void updateVocabularySetName(UpdateVocabularySetNameRequest request, StreamObserver<UpdateVocabularySetResponse> streamObserver) {
        VocabularySet set = vocabularySetService.updateVocabularySetName(request);
        UpdateVocabularySetResponse response = VocabularySetMapper.toUpdateVocabularySetResponse(set);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void deleteVocabularySet(DeleteVocabularySetRequest request, StreamObserver<DeleteVocabularySetResponse> streamObserver){
        VocabularySet set = vocabularySetService.deleteVocabularySet(request);
        DeleteVocabularySetResponse response = VocabularySetMapper.toDeleteVocabularySetResponse(set);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void restoreVocabularySet(RestoreVocabularySetRequest request, StreamObserver<DeleteVocabularySetResponse> streamObserver){
        VocabularySet set = vocabularySetService.restoreVocabularySet(request);
        DeleteVocabularySetResponse response = VocabularySetMapper.toDeleteVocabularySetResponse(set);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void countPublishedVocabularySets(CountPublishedVocabularySetsRequest request, StreamObserver<CountPublishedVocabularySetsResponse> streamObserver){
        List<VocabularySetStatisticResponse> statistics = vocabularySetService.countPublishedVocabularySets(request);
        CountPublishedVocabularySetsResponse response = VocabularySetMapper.toCountPublishedVocabularySetsResponse(statistics);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void getAdminSetStatistics(GetAdminSetStatisticsRequest request, StreamObserver<AdminSetStatisticsResponse> streamObserver) {
        List<AdminSetStatisticResponse> statistics = vocabularySetService.getAdminSetStatistics(request);
        AdminSetStatisticsResponse response = VocabularySetMapper.toAdminSetStatisticsResponse(statistics);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    //Word section
    @Override
    public void createVocabularyWords(CreateVocabularyWordsRequest request, StreamObserver<VocabularyWordsResponse> streamObserver){
        List<VocabularyWord> words = vocabularyWordService.createVocabularyWords(request);
        VocabularyWordsResponse response = VocabularyWordMapper.toVocabularyWordsResponse(words);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void getVocabularyWords(GetVocabularyWordsRequest request, StreamObserver<VocabularyWordsPaginationResponse> streamObserver){
        Page<VocabularyWord> words = vocabularyWordService.getVocabularyWords(request);
        VocabularyWordsPaginationResponse response = VocabularyWordMapper.toVocabularyWordsPaginationResponse(words);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void searchVocabularyWordByWord(SearchVocabularyWordByWordRequest request, StreamObserver<VocabularyWordsPaginationResponse> streamObserver){
        Page<VocabularyWord> words = vocabularyWordService.searchVocabularyWordByWord(request);
        VocabularyWordsPaginationResponse response = VocabularyWordMapper.toVocabularyWordsPaginationResponse(words);
        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void uploadVocabularyWordImage(UploadVocabularyWordImageRequest request, StreamObserver<ActionResponse> streamObserver){
        String url = vocabularyWordService.uploadVocabularyWordImage(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage(url)
                .build();

        streamObserver.onNext(response);
        streamObserver.onCompleted();
    }

    @Override
    public void ensureWordInSet(EnsureWordInSetRequest request, StreamObserver<Empty> streamObserver){
        vocabularyWordService.ensureWordInSet(request);
        streamObserver.onNext(Empty.getDefaultInstance());
        streamObserver.onCompleted();
    }
}
