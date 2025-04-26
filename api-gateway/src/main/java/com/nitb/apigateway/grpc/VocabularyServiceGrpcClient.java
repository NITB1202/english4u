package com.nitb.apigateway.grpc;

import com.nitb.apigateway.dto.Vocabulary.request.CreateVocabularyWordRequestDto;
import com.nitb.apigateway.dto.Vocabulary.request.UpdateVocabularySetRequestDto;
import com.nitb.apigateway.dto.Vocabulary.request.UpdateVocabularyWordRequestDto;
import com.nitb.apigateway.mapper.VocabularyWordMapper;
import com.nitb.common.enums.GroupBy;
import com.nitb.common.grpc.ActionResponse;
import com.nitb.common.mappers.GroupByMapper;
import com.nitb.vocabularyservice.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VocabularyServiceGrpcClient {
    @GrpcClient("vocabulary-service")
    private VocabularyServiceGrpc.VocabularyServiceBlockingStub blockingStub;

    public CreateVocabularySetResponse createVocabularySet(UUID userId, String name) {
        CreateVocabularySetRequest request = CreateVocabularySetRequest.newBuilder()
                .setUserId(userId.toString())
                .setName(name)
                .build();

        return blockingStub.createVocabularySet(request);
    }

    public VocabularyWordsResponse createVocabularyWords(UUID setId, UUID userId, List<CreateVocabularyWordRequestDto> words) {
        List<CreateVocabularyWordRequest> wordRequests = words.stream()
                .map(VocabularyWordMapper::toCreateVocabularyWordRequest)
                .toList();

        CreateVocabularyWordsRequest request =CreateVocabularyWordsRequest.newBuilder()
                .setSetId(setId.toString())
                .setUserId(userId.toString())
                .addAllWords(wordRequests)
                .build();

        return blockingStub.createVocabularyWords(request);
    }

    public VocabularySetDetailResponse getVocabularySetById(UUID id) {
        GetVocabularySetByIdRequest request = GetVocabularySetByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        return blockingStub.getVocabularySetById(request);
    }

    public VocabularySetsResponse getVocabularySets(int page, int size) {
        GetVocabularySetsRequest request = GetVocabularySetsRequest.newBuilder()
                .setPage(page)
                .setSize(size)
                .build();

        return blockingStub.getVocabularySets(request);
    }

    public VocabularySetsResponse getDeletedVocabularySets(int page, int size) {
        GetVocabularySetsRequest request = GetVocabularySetsRequest.newBuilder()
                .setPage(page)
                .setSize(size)
                .build();

        return blockingStub.getDeletedVocabularySets(request);
    }

    public VocabularySetsResponse searchVocabularySetByName(String keyword, int page, int size) {
        SearchVocabularySetByNameRequest request = SearchVocabularySetByNameRequest.newBuilder()
                .setKeyword(keyword)
                .setPage(page)
                .setSize(size)
                .build();

        return blockingStub.searchVocabularySetByName(request);
    }

    public UpdateVocabularySetResponse updateVocabularySet(UUID id, UUID userId, UpdateVocabularySetRequestDto dto) {
        UpdateVocabularySetRequest request = UpdateVocabularySetRequest.newBuilder()
                .setId(id.toString())
                .setUserId(userId.toString())
                .setName(dto.getName())
                .build();

        return blockingStub.updateVocabularySet(request);
    }

    public DeleteVocabularySetResponse deleteVocabularySet(UUID id, UUID userId) {
        DeleteVocabularySetRequest request = DeleteVocabularySetRequest.newBuilder()
                .setId(id.toString())
                .setUserId(userId.toString())
                .build();

        return blockingStub.deleteVocabularySet(request);
    }

    public DeleteVocabularySetResponse restoreVocabularySet(UUID id, UUID userId) {
        RestoreVocabularySetRequest request = RestoreVocabularySetRequest.newBuilder()
                .setId(id.toString())
                .setUserId(userId.toString())
                .build();

        return blockingStub.restoreVocabularySet(request);
    }

    public CountPublishedVocabularySetsResponse countPublishedVocabularySets(UUID userId, LocalDate from, LocalDate to, GroupBy groupBy) {
        CountPublishedVocabularySetsRequest request = CountPublishedVocabularySetsRequest.newBuilder()
                .setUserId(userId.toString())
                .setFrom(from.toString())
                .setTo(to.toString())
                .setGroupBy(GroupByMapper.toGrpcEnum(groupBy))
                .build();

        return blockingStub.countPublishedVocabularySets(request);
    }

    public VocabularyWordsPaginationResponse getVocabularyWords(UUID setId, int page, int size) {
        GetVocabularyWordsRequest request = GetVocabularyWordsRequest.newBuilder()
                .setSetId(setId.toString())
                .setPage(page)
                .setSize(size)
                .build();

        return blockingStub.getVocabularyWords(request);
    }

    public VocabularyWordsPaginationResponse searchVocabularyWordByWord(String keyword, UUID setId, int page, int size) {
        SearchVocabularyWordByWordRequest request = SearchVocabularyWordByWordRequest.newBuilder()
                .setSetId(setId.toString())
                .setKeyword(keyword)
                .setPage(page)
                .setSize(size)
                .build();

        return blockingStub.searchVocabularyWordByWord(request);
    }

    public VocabularyWordResponse updateVocabularyWord(UUID id, UUID userId, UpdateVocabularyWordRequestDto dto){
        UpdateVocabularyWordRequest request = UpdateVocabularyWordRequest.newBuilder()
                .setId(id.toString())
                .setUserId(userId.toString())
                .setWord(Optional.ofNullable(dto.getWord()).orElse(""))
                .setPronunciation(Optional.ofNullable(dto.getPronunciation()).orElse(""))
                .setTranslation(Optional.ofNullable(dto.getTranslation()).orElse(""))
                .setExample(Optional.ofNullable(dto.getExample()).orElse(""))
                .build();

        return blockingStub.updateVocabularyWord(request);
    }

    public ActionResponse switchWordPosition(UUID word1Id, UUID word2Id, UUID userId){
        SwitchWordPositionRequest request = SwitchWordPositionRequest.newBuilder()
                .setWord1Id(word1Id.toString())
                .setWord2Id(word2Id.toString())
                .setUserId(userId.toString())
                .build();

        return blockingStub.switchWordPosition(request);
    }

    public ActionResponse uploadVocabularyWordImage(UUID id, UUID userId, String imageUrl){
        UploadVocabularyWordImageRequest request = UploadVocabularyWordImageRequest.newBuilder()
                .setId(id.toString())
                .setUserId(userId.toString())
                .setImageUrl(imageUrl)
                .build();

        return blockingStub.uploadVocabularyWordImage(request);
    }
}
