package com.nitb.apigateway.grpc;

import com.nitb.apigateway.dto.Vocabulary.request.CreateVocabularyWordRequestDto;
import com.nitb.apigateway.dto.Vocabulary.request.UpdateVocabularySetRequestDto;
import com.nitb.apigateway.dto.Vocabulary.request.UpdateVocabularyWordRequestDto;
import com.nitb.apigateway.mapper.VocabularyWordMapper;
import com.nitb.common.grpc.ActionResponse;
import com.nitb.vocabularyservice.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VocabularyServiceGrpcClient {
    @GrpcClient("vocabulary-service")
    private VocabularyServiceGrpc.VocabularyServiceBlockingStub blockingStub;

    public VocabularySetResponse createVocabularySet(UUID userId, String name) {
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

    public VocabularySetResponse getVocabularySetById(UUID id) {
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

    public VocabularySetsResponse searchVocabularySetByName(String keyword, int page, int size) {
        SearchVocabularySetByNameRequest request = SearchVocabularySetByNameRequest.newBuilder()
                .setKeyword(keyword)
                .setPage(page)
                .setSize(size)
                .build();

        return blockingStub.searchVocabularySetByName(request);
    }

    public VocabularySetResponse updateVocabularySet(UUID id, UUID userId, UpdateVocabularySetRequestDto dto) {
        UpdateVocabularySetRequest request = UpdateVocabularySetRequest.newBuilder()
                .setId(id.toString())
                .setUserId(userId.toString())
                .setName(dto.getName())
                .build();

        return blockingStub.updateVocabularySet(request);
    }

    public VocabularySetResponse deleteVocabularySet(UUID id, UUID userId) {
        DeleteVocabularySetRequest request = DeleteVocabularySetRequest.newBuilder()
                .setId(id.toString())
                .setUserId(userId.toString())
                .build();

        return blockingStub.deleteVocabularySet(request);
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
                .setPronun(Optional.ofNullable(dto.getPronunciation()).orElse(""))
                .setTrans(Optional.ofNullable(dto.getTranslation()).orElse(""))
                .setEx(Optional.ofNullable(dto.getExample()).orElse(""))
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

    public ActionResponse deleteVocabularyWords(UUID setId, UUID userId, List<UUID> ids){
        List<String> idsString = ids.stream().map(UUID::toString).toList();

        DeleteVocabularyWordsRequest request = DeleteVocabularyWordsRequest.newBuilder()
                .setSetId(setId.toString())
                .setUserId(userId.toString())
                .addAllIds(idsString)
                .build();

        return blockingStub.deleteVocabularyWords(request);
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
