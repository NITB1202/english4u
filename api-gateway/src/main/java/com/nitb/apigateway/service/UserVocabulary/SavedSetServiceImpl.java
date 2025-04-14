package com.nitb.apigateway.service.UserVocabulary;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.request.CreateSavedSetRequestDto;
import com.nitb.apigateway.dto.UserVocabulary.request.UpdateSavedSetRequestDto;
import com.nitb.apigateway.dto.UserVocabulary.response.SavedSetDetailResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.response.SavedSetResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.response.SavedSetStateStatisticResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.response.SavedSetsPaginationResponseDto;
import com.nitb.apigateway.grpc.UserVocabularyServiceGrpcClient;
import com.nitb.apigateway.grpc.VocabularyServiceGrpcClient;
import com.nitb.apigateway.mapper.ActionMapper;
import com.nitb.apigateway.mapper.SavedSetMapper;
import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.grpc.ActionResponse;
import com.nitb.uservocabularyservice.grpc.SavedSetResponse;
import com.nitb.uservocabularyservice.grpc.SavedSetsPaginationResponse;
import com.nitb.uservocabularyservice.grpc.SavedSetsResponse;
import com.nitb.vocabularyservice.grpc.VocabularySetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SavedSetServiceImpl implements SavedSetService{
    private final UserVocabularyServiceGrpcClient userVocabularyGrpc;
    private final VocabularyServiceGrpcClient vocabularyGrpc;

    @Override
    public Mono<SavedSetResponseDto> createSavedSet(UUID userId, CreateSavedSetRequestDto request) {
        return Mono.fromCallable(()->{
            VocabularySetResponse set = vocabularyGrpc.getVocabularySetById(request.getSetId());

            if(set == null) {
                throw new BusinessException("Set not found.");
            }

            if(request.getLearnedWords() > set.getWordCount()){
                throw new BusinessException("Learned words exceeds word count.");
            }

            SavedSetResponse response = userVocabularyGrpc.createSavedSet(userId, request);
            return SavedSetMapper.toSavedSetResponseDto(response);

        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<SavedSetsPaginationResponseDto> getSavedSets(UUID userId, int page, int size) {
        return Mono.fromCallable(()->{
            SavedSetsPaginationResponse paginationSets = userVocabularyGrpc.getSavedSets(userId, page, size);

            List<SavedSetDetailResponseDto> response = new ArrayList<>();

            for(SavedSetResponse savedSet : paginationSets.getSetsList()){
                VocabularySetResponse set = vocabularyGrpc.getVocabularySetById(UUID.fromString(savedSet.getSetId()));
                SavedSetDetailResponseDto detail = SavedSetMapper.toSavedSetDetailResponseDto(savedSet, set);
                response.add(detail);
            }

            return SavedSetsPaginationResponseDto.builder()
                    .sets(response)
                    .totalItems(paginationSets.getTotalItems())
                    .totalPages(paginationSets.getTotalPages())
                    .build();

        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<SavedSetsPaginationResponseDto> searchSavedSets(UUID userId, String keyword, int page, int size) {
        return Mono.fromCallable(()->{
            String handledKeyword = keyword.toLowerCase().trim();
            SavedSetsResponse allSavedSets = userVocabularyGrpc.getAllSavedSets(userId);
            List<SavedSetDetailResponseDto> acceptedSets = new ArrayList<>();

            for(SavedSetResponse savedSet : allSavedSets.getSetsList()){
                VocabularySetResponse set = vocabularyGrpc.getVocabularySetById(UUID.fromString(savedSet.getSetId()));
                if(set.getName().toLowerCase().contains(handledKeyword)) {
                    SavedSetDetailResponseDto detail = SavedSetMapper.toSavedSetDetailResponseDto(savedSet, set);
                    acceptedSets.add(detail);
                }
            }

            int totalItems =  acceptedSets.size();
            int totalPages =  (int)Math.ceil((double)totalItems/size);

            int zeroBasedPage = page > 0 ? page - 1 : 0;
            int fromIndex = Math.min(zeroBasedPage * size, totalItems);
            int toIndex = Math.min(fromIndex + size, totalItems);

            List<SavedSetDetailResponseDto> paginated = acceptedSets.subList(fromIndex, toIndex);

            return SavedSetsPaginationResponseDto.builder()
                    .sets(paginated)
                    .totalItems((long)totalItems)
                    .totalPages(totalPages)
                    .build();

        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<SavedSetResponseDto> updateSavedSet(UUID id, UpdateSavedSetRequestDto request) {
        return Mono.fromCallable(()->{
            SavedSetResponse savedSet = userVocabularyGrpc.getSavedSetById(id);

            if(savedSet == null) {
                throw new BusinessException("Saved set not found.");
            }

            VocabularySetResponse set = vocabularyGrpc.getVocabularySetById(UUID.fromString(savedSet.getSetId()));

            if(request.getLearnedWords() > set.getWordCount()){
                throw new BusinessException("Learned words exceeds word count.");
            }

            SavedSetResponse updatedSet = userVocabularyGrpc.updateSavedSet(id, request);
            return SavedSetMapper.toSavedSetResponseDto(updatedSet);

        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> deleteSavedSet(UUID id) {
        return Mono.fromCallable(()->{
            ActionResponse response = userVocabularyGrpc.deleteSavedSet(id);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<SavedSetStateStatisticResponseDto> getSavedSetStateStatistic(UUID userId) {
        return Mono.fromCallable(()->{
            SavedSetsResponse allSavedSets = userVocabularyGrpc.getAllSavedSets(userId);

            int notLearned = 0;
            int learning = 0;
            int learned = 0;

            for(SavedSetResponse savedSet : allSavedSets.getSetsList()){
                if(savedSet.getLearnedWords() == 0){
                    notLearned++;
                    continue;
                }

                VocabularySetResponse set = vocabularyGrpc.getVocabularySetById(UUID.fromString(savedSet.getSetId()));

                if(savedSet.getLearnedWords() < set.getWordCount()){
                    learning++;
                    continue;
                }

                learned++;
            }

            return SavedSetStateStatisticResponseDto.builder()
                    .notLearned(notLearned)
                    .learning(learning)
                    .learned(learned)
                    .build();

        }).subscribeOn(Schedulers.boundedElastic());
    }
}
