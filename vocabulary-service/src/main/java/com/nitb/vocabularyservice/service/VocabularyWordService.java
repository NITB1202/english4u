package com.nitb.vocabularyservice.service;

import com.nitb.vocabularyservice.entity.VocabularyWord;
import com.nitb.vocabularyservice.grpc.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VocabularyWordService {
    List<VocabularyWord> createVocabularyWords(CreateVocabularyWordsRequest request);
    Page<VocabularyWord> getVocabularyWords(GetVocabularyWordsRequest request);
    Page<VocabularyWord> searchVocabularyWordByWord(SearchVocabularyWordByWordRequest request);
    String uploadVocabularyWordImage(UploadVocabularyWordImageRequest request);
    void ensureWordInSet(EnsureWordInSetRequest request);
}
