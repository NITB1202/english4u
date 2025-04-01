package com.nitb.apigateway.grpc;

import com.nitb.apigateway.dto.Vocabulary.request.CreateVocabularyWordRequestDto;
import com.nitb.apigateway.mapper.VocabularyMapper;
import com.nitb.vocabularyservice.grpc.*;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VocabularyServiceGrpcClient {
    @GrpcService("vocabulary-service")
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
}
