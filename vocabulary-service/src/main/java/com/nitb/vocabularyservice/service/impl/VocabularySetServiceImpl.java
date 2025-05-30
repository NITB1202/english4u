package com.nitb.vocabularyservice.service.impl;

import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.exceptions.NotFoundException;
import com.nitb.vocabularyservice.dto.VocabularySetStatisticProjection;
import com.nitb.vocabularyservice.entity.VocabularySet;
import com.nitb.vocabularyservice.grpc.*;
import com.nitb.vocabularyservice.mapper.VocabularySetMapper;
import com.nitb.vocabularyservice.repository.VocabularySetRepository;
import com.nitb.vocabularyservice.service.VocabularySetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VocabularySetServiceImpl implements VocabularySetService {
    private final VocabularySetRepository vocabularySetRepository;
    private final int DEFAULT_SIZE = 10;
    private final int MAX_VERSION = 5;

    @Override
    public VocabularySet createVocabularySet(CreateVocabularySetRequest request) {
        if(vocabularySetRepository.existsByNameAndIsDeletedFalse(request.getName())) {
            throw new BusinessException("This name has been used already.");
        }

        UUID userId = UUID.fromString(request.getUserId());

        VocabularySet vocabularySet = VocabularySet.builder()
                .name(request.getName())
                .version(1)
                .createdBy(userId)
                .createdAt(LocalDateTime.now())
                .updatedBy(userId)
                .updatedAt(LocalDateTime.now())
                .wordCount(0)
                .isDeleted(false)
                .build();

        return vocabularySetRepository.save(vocabularySet);
    }

    @Override
    public VocabularySet getVocabularySetById(GetVocabularySetByIdRequest request) {
        UUID id  = UUID.fromString(request.getId());
        return vocabularySetRepository.findById(id).orElseThrow(
                () -> new BusinessException("No vocabulary set found with id: " + id)
        );
    }

    @Override
    public Page<VocabularySet> getVocabularySets(GetVocabularySetsRequest request) {
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return vocabularySetRepository.findAllByIsDeletedFalse(PageRequest.of(page, size));
    }

    @Override
    public Page<VocabularySet> getDeletedVocabularySets(GetVocabularySetsRequest request) {
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return vocabularySetRepository.findAllByIsDeletedTrue(PageRequest.of(page, size));
    }

    @Override
    public Page<VocabularySet> searchVocabularySetByName(SearchVocabularySetByNameRequest request){
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return vocabularySetRepository.findByNameContainingIgnoreCaseAndIsDeletedFalse(request.getKeyword(), PageRequest.of(page, size));
    }

    @Override
    public Page<VocabularySet> searchDeletedVocabularySetByName(SearchVocabularySetByNameRequest request) {
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return vocabularySetRepository.findByNameContainingIgnoreCaseAndIsDeletedTrue(request.getKeyword(), PageRequest.of(page, size));
    }

    @Override
    public void validateUpdateVocabularySet(ValidateUpdateVocabularySetRequest request) {
        UUID id = UUID.fromString(request.getId());
        VocabularySet set = vocabularySetRepository.findById(id).orElseThrow(
                () -> new BusinessException("No vocabulary set found with id: " + id)
        );

        if(vocabularySetRepository.countByName(set.getName()) >= MAX_VERSION) {
            throw new BusinessException("Version limit reached for this vocabulary set. Please create a new set to continue editing.");
        }
    }

    @Override
    public VocabularySet updateVocabularySetName(UpdateVocabularySetNameRequest request) {
        UUID setId = UUID.fromString(request.getId());

        VocabularySet set = vocabularySetRepository.findById(setId).orElseThrow(
                () -> new BusinessException("No vocabulary set found with id: " + request.getId())
        );

        if(set.getIsDeleted()) {
            throw new BusinessException("This vocabulary set has been deleted. Please restore it before updating.");
        }

        if(vocabularySetRepository.existsByNameAndIsDeletedFalse(request.getName())) {
            throw new BusinessException("This name has been used already.");
        }

        set.setName(request.getName());
        set.setVersion(1);
        set.setUpdatedBy(UUID.fromString(request.getUserId()));
        set.setUpdatedAt(LocalDateTime.now());

        return vocabularySetRepository.save(set);
    }

    @Override
    public VocabularySet updateVocabularySet(UpdateVocabularySetRequest request) {
        UUID oldId = UUID.fromString(request.getOldId());
        VocabularySet oldSet = vocabularySetRepository.findById(oldId).orElseThrow(
                () -> new BusinessException("No vocabulary set found with id: " + oldId)
        );

        UUID newId = UUID.fromString(request.getNewId());
        VocabularySet set = vocabularySetRepository.findById(newId).orElseThrow(
                () -> new BusinessException("No vocabulary set found with id: " + newId)
        );

        int latestVersion = vocabularySetRepository.getLatestVersion(oldSet.getName());

        set.setVersion(latestVersion + 1);
        set.setCreatedBy(oldSet.getCreatedBy());
        set.setCreatedAt(oldSet.getCreatedAt());

        return vocabularySetRepository.save(set);
    }

    @Override
    public VocabularySet deleteVocabularySet(DeleteVocabularySetRequest request){
        UUID setId = UUID.fromString(request.getId());

        VocabularySet set = vocabularySetRepository.findById(setId).orElseThrow(
                () -> new BusinessException("No vocabulary set found with id: " + request.getId())
        );

        set.setIsDeleted(true);
        set.setUpdatedBy(UUID.fromString(request.getUserId()));
        set.setUpdatedAt(LocalDateTime.now());

        return vocabularySetRepository.save(set);
    }

    @Override
    public VocabularySet restoreVocabularySet(RestoreVocabularySetRequest request) {
        UUID setId = UUID.fromString(request.getId());
        UUID userId = UUID.fromString(request.getUserId());
        LocalDateTime now = LocalDateTime.now();

        VocabularySet set = vocabularySetRepository.findById(setId).orElseThrow(
                () -> new BusinessException("No vocabulary set found with id: " + request.getId())
        );

        //If the current version of this set exists, delete it.
        VocabularySet currentSet = vocabularySetRepository.findByNameAndIsDeletedFalse(set.getName());
        if(currentSet != null) {
            currentSet.setIsDeleted(true);
            currentSet.setUpdatedBy(userId);
            currentSet.setUpdatedAt(now);
            vocabularySetRepository.save(currentSet);
        }

        set.setIsDeleted(false);
        set.setUpdatedBy(userId);
        set.setUpdatedAt(now);

        return vocabularySetRepository.save(set);
    }

    @Override
    public List<VocabularySetStatisticResponse> countPublishedVocabularySets(CountPublishedVocabularySetsRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        LocalDateTime from = LocalDate.parse(request.getFrom()).atStartOfDay();
        LocalDateTime to = LocalDate.parse(request.getTo()).atTime(23, 59, 59);

        List<VocabularySetStatisticProjection> result = new ArrayList<>();

        switch (request.getGroupBy()){
            case WEEK -> result.addAll(vocabularySetRepository.countByWeek(userId, from, to));
            case MONTH -> result.addAll(vocabularySetRepository.countByMonth(userId, from, to));
            case YEAR -> result.addAll(vocabularySetRepository.countByYear(userId, from, to));
            default -> throw new BusinessException("Invalid group by.");
        }

        return result.stream()
                .map(p -> VocabularySetMapper.toVocabularySetStatisticResponse(p.getTime(), p.getCount()))
                .toList();
    }

    @Override
    public List<AdminSetStatisticResponse> getAdminSetStatistics(GetAdminSetStatisticsRequest request) {
        List<UUID> userIds = request.getUserIdList().stream()
                .map(UUID::fromString)
                .toList();

        List<AdminSetStatisticResponse> result = new ArrayList<>();

        for(UUID userId : userIds) {
            long totalPublishedSets = vocabularySetRepository.countByCreatedByAndIsDeletedFalse(userId);
            AdminSetStatisticResponse statistic = VocabularySetMapper.toAdminSetStatisticResponse(userId, totalPublishedSets);
            result.add(statistic);
        }

        return result;
    }

    @Override
    public void updateWordCount(UUID setId, int count, UUID userId) {
        VocabularySet set = vocabularySetRepository.findById(setId).orElseThrow(
                () -> new NotFoundException("Vocabulary set not found.")
        );

        set.setWordCount(count);
        set.setUpdatedBy(userId);
        set.setUpdatedAt(LocalDateTime.now());

        vocabularySetRepository.save(set);
    }
}
