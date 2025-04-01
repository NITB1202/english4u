package com.nitb.apigateway.grpc;

import com.nitb.apigateway.dto.Vocabulary.request.CreateVocabularyWordRequestDto;
import com.nitb.apigateway.dto.Vocabulary.request.UpdateVocabularySetRequestDto;
import com.nitb.apigateway.mapper.VocabularyMapper;
import com.nitb.vocabularyservice.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
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
                .map(VocabularyMapper::toCreateVocabularyWordRequest)
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
}
