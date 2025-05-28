package com.nitb.apigateway.service.Vocabulary;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularyWordsPaginationResponseDto;
import com.nitb.apigateway.grpc.FileServiceGrpcClient;
import com.nitb.apigateway.grpc.VocabularyServiceGrpcClient;
import com.nitb.apigateway.mapper.ActionMapper;
import com.nitb.apigateway.mapper.VocabularyWordMapper;
import com.nitb.apigateway.util.FileUtils;
import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.grpc.ActionResponse;
import com.nitb.fileservice.grpc.FileResponse;
import com.nitb.vocabularyservice.grpc.VocabularyWordsPaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VocabularyWordServiceImpl implements VocabularyWordService {
    private final VocabularyServiceGrpcClient grpcClient;
    private final FileServiceGrpcClient fileClient;
    private final String WORD_FOLDER = "words";

    @Override
    public Mono<VocabularyWordsPaginationResponseDto> getVocabularyWords(UUID setId, int page, int size) {
        return Mono.fromCallable(()->{
            VocabularyWordsPaginationResponse pagination = grpcClient.getVocabularyWords(setId, page, size);
            return VocabularyWordMapper.toVocabularyWordsPaginationResponseDto(pagination);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<VocabularyWordsPaginationResponseDto> searchVocabularyWordByWord(UUID setId, String keyword, int page, int size) {
        return Mono.fromCallable(()->{
            VocabularyWordsPaginationResponse pagination = grpcClient.searchVocabularyWordByWord(keyword, setId, page, size);
            return VocabularyWordMapper.toVocabularyWordsPaginationResponseDto(pagination);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> uploadVocabularyWordImage(UUID setId, UUID wordId, FilePart file) {
        if(!FileUtils.isImage(file)) {
            throw new BusinessException("Invalid image file.");
        }

        grpcClient.ensureWordInSet(wordId, setId);

        return DataBufferUtils.join(file.content())
                .flatMap(buffer -> {
                    byte[] bytes = new byte[buffer.readableByteCount()];
                    buffer.read(bytes);
                    DataBufferUtils.release(buffer);

                    String folderPath = WORD_FOLDER + "/" + setId;
                    FileResponse image = fileClient.uploadFile(folderPath, wordId.toString(), bytes);

                    ActionResponse response = grpcClient.uploadVocabularyWordImage(wordId, image.getUrl());

                    return Mono.fromCallable(() -> ActionMapper.toResponseDto(response))
                            .subscribeOn(Schedulers.boundedElastic());
                });
    }
}
