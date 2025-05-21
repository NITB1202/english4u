package com.nitb.vocabularyservice.service;

import com.nitb.vocabularyservice.entity.VocabularyWord;
import com.nitb.vocabularyservice.grpc.CreateVocabularyWordsRequest;
import com.nitb.vocabularyservice.grpc.GetVocabularyWordsRequest;
import com.nitb.vocabularyservice.grpc.SearchVocabularyWordByWordRequest;
import com.nitb.vocabularyservice.grpc.UploadVocabularyWordImageRequest;
import org.springframework.data.domain.Page;

public interface VocabularyWordService {
    void createVocabularyWords(CreateVocabularyWordsRequest request);
    Page<VocabularyWord> getVocabularyWords(GetVocabularyWordsRequest request);
    Page<VocabularyWord> searchVocabularyWordByWord(SearchVocabularyWordByWordRequest request);
    void uploadVocabularyWordImage(UploadVocabularyWordImageRequest request);
}
