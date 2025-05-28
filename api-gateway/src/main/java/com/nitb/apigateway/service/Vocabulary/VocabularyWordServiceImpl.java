package com.nitb.apigateway.service.Vocabulary;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.Vocabulary.request.RemoveVocabularyWordImageRequestDto;
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
    private final String WORD_TEMP_FOLDER = "words/temp";

    @Override
    public Mono<VocabularyWordsPaginationResponseDto> getVocabularyWords(UUID setId, int page, int size) {
        return Mono.fromCallable(()->{
            VocabularyWordsPaginationResponse pagination = grpcClient.getVocabularyWords(setId, page, size);
            return VocabularyWordMapper.vocabularyWordsPaginationResponseDto(pagination);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<VocabularyWordsPaginationResponseDto> searchVocabularyWordByWord(UUID setId, String keyword, int page, int size) {
        return Mono.fromCallable(()->{
            VocabularyWordsPaginationResponse pagination = grpcClient.searchVocabularyWordByWord(keyword, setId, page, size);
            return VocabularyWordMapper.vocabularyWordsPaginationResponseDto(pagination);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> uploadVocabularyWordImage(FilePart file) {
        if(!FileUtils.isImage(file)) {
            throw new BusinessException("Invalid image file.");
        }

        return DataBufferUtils.join(file.content())
                .flatMap(buffer -> {
                    byte[] bytes = new byte[buffer.readableByteCount()];
                    buffer.read(bytes);
                    DataBufferUtils.release(buffer);

                    FileResponse tempFile = fileClient.uploadFile(WORD_TEMP_FOLDER, null, bytes);
                    ActionResponseDto response = ActionResponseDto.builder()
                            .success(true)
                            .message(tempFile.getUrl())
                            .build();

                    return Mono.fromCallable(() -> response)
                            .subscribeOn(Schedulers.boundedElastic());
                });
    }

    @Override
    public Mono<ActionResponseDto> removeVocabularyWordImage(RemoveVocabularyWordImageRequestDto dto) {
        return Mono.fromCallable(()->{
            String publicId = FileUtils.extractPublicIdFromUrl(dto.getUrl());

            if(isNotTempFile(publicId)) {
                throw new BusinessException("Only temp file can be removed.");
            }

            ActionResponse response = fileClient.deleteFile(publicId);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private boolean isNotTempFile(String publicId) {
        try {
            UUID.fromString(publicId);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
