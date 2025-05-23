package com.nitb.uservocabularyservice.service;

import com.nitb.uservocabularyservice.entity.SavedSet;
import com.nitb.uservocabularyservice.grpc.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SavedSetService {
    SavedSet createSavedSet(CreateSavedSetRequest request);
    SavedSet getSavedSetById(GetSavedSetByIdRequest request);
    Page<SavedSet> getSavedSets(GetSavedSetsRequest request);
    List<SavedSet> getAllSavedSets(GetAllSavedSetsRequest request);
    SavedSet updateSavedSet(UpdateSavedSetRequest request);
    void deleteSavedSet(DeleteSavedSetRequest request);
}
