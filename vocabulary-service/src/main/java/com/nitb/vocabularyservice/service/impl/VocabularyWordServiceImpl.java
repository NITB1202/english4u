package com.nitb.vocabularyservice.service.impl;

import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.exceptions.NotFoundException;
import com.nitb.vocabularyservice.entity.VocabularyWord;
import com.nitb.vocabularyservice.grpc.*;
import com.nitb.vocabularyservice.repository.VocabularyWordRepository;
import com.nitb.vocabularyservice.service.VocabularySetService;
import com.nitb.vocabularyservice.service.VocabularyWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VocabularyWordServiceImpl implements VocabularyWordService {
    private final VocabularyWordRepository vocabularyWordRepository;
    private final VocabularySetService vocabularySetService;
    private final int DEFAULT_SIZE = 10;

    @Override
    public List<VocabularyWord> createVocabularyWords(CreateVocabularyWordsRequest request) {
        List<CreateVocabularyWordRequest> wordRequests = request.getWordsList();
        UUID setId = UUID.fromString(request.getSetId());
        UUID userId = UUID.fromString(request.getUserId());

        int position = vocabularyWordRepository.countBySetId(setId);
        List<VocabularyWord> result = new ArrayList<>();

        //Create words
        for (CreateVocabularyWordRequest wordRequest : wordRequests) {
            if(vocabularyWordRepository.existsByWordAndSetId(wordRequest.getWord(), setId)){
                throw new BusinessException("The word '" + wordRequest.getWord() +"' already exists in this set.");
            }

            position++;

            VocabularyWord word = VocabularyWord.builder()
                    .setId(setId)
                    .position(position)
                    .word(wordRequest.getWord())
                    .pronunciation(wordRequest.getPronunciation())
                    .translation(wordRequest.getTranslation())
                    .example(wordRequest.getExample())
                    .build();

            result.add(word);
        }

        //Update set
        vocabularySetService.updateWordCount(setId, position, userId);

        return vocabularyWordRepository.saveAll(result);
    }

    @Override
    public Page<VocabularyWord> getVocabularyWords(GetVocabularyWordsRequest request) {
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return vocabularyWordRepository.findBySetId(UUID.fromString(request.getSetId()), PageRequest.of(page, size));
    }

    @Override
    public Page<VocabularyWord> searchVocabularyWordByWord(SearchVocabularyWordByWordRequest request){
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return vocabularyWordRepository.findByWordContainingIgnoreCaseAndSetId(
                request.getKeyword(),
                UUID.fromString(request.getSetId()),
                PageRequest.of(page, size)
        );
    }

    @Override
    public void uploadVocabularyWordImage(UploadVocabularyWordImageRequest request){
        UUID wordId = UUID.fromString(request.getId());

        //Update word
        VocabularyWord word = vocabularyWordRepository.findById(wordId).orElseThrow(
                () -> new NotFoundException("Vocabulary word not found.")
        );

        word.setImageUrl(request.getImageUrl());
        vocabularyWordRepository.save(word);
    }

    @Override
    public void ensureWordInSet(EnsureWordInSetRequest request) {
        UUID wordId = UUID.fromString(request.getWordId());
        UUID setId = UUID.fromString(request.getSetId());

        if(!vocabularyWordRepository.existsByIdAndSetId(wordId, setId)){
            throw new NotFoundException("Incorrect word id or set id.");
        }
    }
}
