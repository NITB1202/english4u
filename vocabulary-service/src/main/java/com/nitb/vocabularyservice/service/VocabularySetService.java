package com.nitb.vocabularyservice.service;

import com.nitb.vocabularyservice.dto.VocabularySetStatisticDto;
import com.nitb.vocabularyservice.entity.VocabularySet;
import com.nitb.vocabularyservice.grpc.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface VocabularySetService {
    VocabularySet createVocabularySet(CreateVocabularySetRequest request);
    VocabularySet getVocabularySetById(GetVocabularySetByIdRequest request);
    Page<VocabularySet> getVocabularySets(GetVocabularySetsRequest request);
    Page<VocabularySet> getDeletedVocabularySets(GetVocabularySetsRequest request);
    Page<VocabularySet> searchVocabularySetByName(SearchVocabularySetByNameRequest request);
    Page<VocabularySet> searchDeletedVocabularySetByName(SearchVocabularySetByNameRequest request);
    void validateUpdateVocabularySet(ValidateUpdateVocabularySetRequest request);
    VocabularySet updateVocabularySetName(UpdateVocabularySetNameRequest request);
    VocabularySet updateVocabularySet(UpdateVocabularySetRequest request);
    VocabularySet deleteVocabularySet(DeleteVocabularySetRequest request);
    VocabularySet restoreVocabularySet(RestoreVocabularySetRequest request);
    List<VocabularySetStatisticDto> countPublishedVocabularySets(CountPublishedVocabularySetsRequest request);

    void updateWordCount(UUID setId, int count, UUID userId);
}
