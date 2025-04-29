package com.nitb.vocabularyservice.service;

import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.exceptions.NotFoundException;
import com.nitb.vocabularyservice.dto.VocabularySetStatisticDto;
import com.nitb.vocabularyservice.dto.VocabularySetStatisticProjection;
import com.nitb.vocabularyservice.entity.VocabularySet;
import com.nitb.vocabularyservice.grpc.*;
import com.nitb.vocabularyservice.repository.VocabularySetRepository;
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
public class VocabularySetService {
    private final VocabularySetRepository vocabularySetRepository;
    private final int DEFAULT_SIZE = 10;

    public VocabularySet createVocabularySet(CreateVocabularySetRequest request) {
        if(vocabularySetRepository.existsByNameAndIsDeletedFalse(request.getName())) {
            throw new BusinessException("This name has been used already.");
        }

        UUID userId = UUID.fromString(request.getUserId());

        VocabularySet vocabularySet = VocabularySet.builder()
                .name(request.getName())
                .createdBy(userId)
                .createdAt(LocalDateTime.now())
                .updatedBy(userId)
                .updatedAt(LocalDateTime.now())
                .wordCount(0)
                .isDeleted(false)
                .build();

        return vocabularySetRepository.save(vocabularySet);
    }

    public VocabularySet getVocabularySetById(GetVocabularySetByIdRequest request) {
        UUID id  = UUID.fromString(request.getId());
        return vocabularySetRepository.findById(id).orElseThrow(
                () -> new BusinessException("No vocabulary set found with id: " + id)
        );
    }

    public Page<VocabularySet> getVocabularySets(GetVocabularySetsRequest request) {
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return vocabularySetRepository.findAllByIsDeletedFalse(PageRequest.of(page, size));
    }

    public Page<VocabularySet> getDeletedVocabularySets(GetVocabularySetsRequest request) {
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return vocabularySetRepository.findAllByIsDeletedTrue(PageRequest.of(page, size));
    }

    public Page<VocabularySet> searchVocabularySetByName(SearchVocabularySetByNameRequest request){
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return vocabularySetRepository.findByNameContainingIgnoreCaseAndIsDeletedFalse(request.getKeyword(), PageRequest.of(page, size));
    }

    public VocabularySet updateVocabularySet(UpdateVocabularySetRequest request) {
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
        set.setUpdatedBy(UUID.fromString(request.getUserId()));
        set.setUpdatedAt(LocalDateTime.now());

        return vocabularySetRepository.save(set);

    }

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

    public VocabularySet restoreVocabularySet(RestoreVocabularySetRequest request) {
        UUID setId = UUID.fromString(request.getId());

        VocabularySet set = vocabularySetRepository.findById(setId).orElseThrow(
                () -> new BusinessException("No vocabulary set found with id: " + request.getId())
        );

        set.setIsDeleted(false);
        set.setUpdatedBy(UUID.fromString(request.getUserId()));
        set.setUpdatedAt(LocalDateTime.now());

        return vocabularySetRepository.save(set);
    }

    public List<VocabularySetStatisticDto> countPublishedVocabularySets(CountPublishedVocabularySetsRequest request) {
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
                .map(p -> new VocabularySetStatisticDto(p.getTime(), p.getCount()))
                .toList();
    }

    public void updateWordCount(UUID setId, int count, UUID userId) {
        VocabularySet set = vocabularySetRepository.findById(setId).orElseThrow(
                () -> new NotFoundException("Vocabulary set not found.")
        );

        set.setWordCount(count);
        set.setUpdatedBy(userId);
        set.setUpdatedAt(LocalDateTime.now());

        vocabularySetRepository.save(set);
    }

    public void updateLastModified(UUID setId, UUID userId){
        VocabularySet set = vocabularySetRepository.findById(setId).orElseThrow(
                () -> new NotFoundException("Vocabulary set not found.")
        );

        set.setUpdatedBy(userId);
        set.setUpdatedAt(LocalDateTime.now());

        vocabularySetRepository.save(set);
    }
}
