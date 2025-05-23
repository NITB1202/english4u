package com.nitb.apigateway.grpc;

import com.google.protobuf.Empty;
import com.nitb.apigateway.dto.Vocabulary.request.CreateVocabularyWordRequestDto;
import com.nitb.apigateway.mapper.VocabularyWordMapper;
import com.nitb.common.enums.GroupBy;
import com.nitb.common.grpc.ActionResponse;
import com.nitb.common.mappers.GroupByMapper;
import com.nitb.vocabularyservice.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class VocabularyServiceGrpcClient {
    @GrpcClient("vocabulary-service")
    private VocabularyServiceGrpc.VocabularyServiceBlockingStub blockingStub;

    //Set
    public CreateVocabularySetResponse createVocabularySet(UUID userId, String name) {
        CreateVocabularySetRequest request = CreateVocabularySetRequest.newBuilder()
                .setUserId(userId.toString())
                .setName(name.trim())
                .build();

        return blockingStub.createVocabularySet(request);
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

    public VocabularySetsResponse searchDeletedVocabularySets(String keyword, int page, int size) {
        SearchVocabularySetByNameRequest request = SearchVocabularySetByNameRequest.newBuilder()
                .setKeyword(keyword)
                .setPage(page)
                .setSize(size)
                .build();

        return blockingStub.searchDeletedVocabularySetByName(request);
    }

    public Empty validateUpdateVocabularySet(UUID id) {
        ValidateUpdateVocabularySetRequest request = ValidateUpdateVocabularySetRequest.newBuilder()
                .setId(id.toString())
                .build();

        return blockingStub.validateUpdateVocabularySet(request);
    }

    public UpdateVocabularySetResponse updateVocabularySetName(UUID id, UUID userId, String name) {
        UpdateVocabularySetNameRequest request = UpdateVocabularySetNameRequest.newBuilder()
                .setId(id.toString())
                .setUserId(userId.toString())
                .setName(name.trim())
                .build();

        return blockingStub.updateVocabularySetName(request);
    }

    public UpdateVocabularySetResponse updateVocabularySet(UUID oldId, UUID newId, UUID createdBy, LocalDateTime createAt) {
        UpdateVocabularySetRequest request = UpdateVocabularySetRequest.newBuilder()
                .setOldId(oldId.toString())
                .setNewId(newId.toString())
                .setCreatedBy(createdBy.toString())
                .setCreateAt(createAt.toString())
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

    //Word
    public Empty createVocabularyWords(UUID setId, UUID userId, List<CreateVocabularyWordRequestDto> words) {
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

    public ActionResponse uploadVocabularyWordImage(UUID id, UUID userId, String imageUrl){
        UploadVocabularyWordImageRequest request = UploadVocabularyWordImageRequest.newBuilder()
                .setId(id.toString())
                .setUserId(userId.toString())
                .setImageUrl(imageUrl)
                .build();

        return blockingStub.uploadVocabularyWordImage(request);
    }
}
