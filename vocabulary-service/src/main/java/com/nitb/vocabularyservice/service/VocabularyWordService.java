package com.nitb.vocabularyservice.service;

import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.exceptions.NotFoundException;
import com.nitb.vocabularyservice.entity.VocabularyWord;
import com.nitb.vocabularyservice.grpc.*;
import com.nitb.vocabularyservice.repository.VocabularyWordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VocabularyWordService {
    private final VocabularyWordRepository vocabularyWordRepository;
    private final VocabularySetService vocabularySetService;
    private final int DEFAULT_SIZE = 10;

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

    public Page<VocabularyWord> getVocabularyWords(GetVocabularyWordsRequest request) {
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return vocabularyWordRepository.findBySetId(UUID.fromString(request.getSetId()), PageRequest.of(page, size));
    }

    public Page<VocabularyWord> searchVocabularyWordByWord(SearchVocabularyWordByWordRequest request){
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return vocabularyWordRepository.findByWordContainingIgnoreCaseAndSetId(
                request.getKeyword(),
                UUID.fromString(request.getSetId()),
                PageRequest.of(page, size)
        );
    }

    public VocabularyWord updateVocabularyWord(UpdateVocabularyWordRequest request){
        UUID wordId = UUID.fromString(request.getId());

        VocabularyWord vocabularyWord = vocabularyWordRepository.findById(wordId).orElseThrow(
                () -> new NotFoundException("Vocabulary word not found.")
        );

        //Update word
        String word = request.getWord();
        String pronunciation = request.getPronunciation();
        String translation = request.getTranslation();
        String example = request.getExample();

        if(!word.isBlank()){
            if(vocabularyWordRepository.existsByWordAndSetId(word, vocabularyWord.getSetId()))
                throw new BusinessException("The word '" + word +"' already exists in this set.");
            else
                vocabularyWord.setWord(word);
        }

        if(!pronunciation.isBlank()){
            vocabularyWord.setPronunciation(pronunciation);
        }

        if(!translation.isBlank()){
            vocabularyWord.setTranslation(translation);
        }

        if(!example.isBlank()){
            vocabularyWord.setExample(example);
        }

        //Update set
        vocabularySetService.updateLastModified(vocabularyWord.getSetId(), UUID.fromString(request.getUserId()));

        return vocabularyWordRepository.save(vocabularyWord);
    }

    public void switchWordPosition(SwitchWordPositionRequest request){
        //Update word
        UUID firstWordId = UUID.fromString(request.getWord1Id());
        UUID secondWordId = UUID.fromString(request.getWord2Id());

        VocabularyWord firstWord = vocabularyWordRepository.findById(firstWordId).orElseThrow(
                () -> new NotFoundException("Vocabulary word with id " + firstWordId + " not found.")
        );

        VocabularyWord secondWord = vocabularyWordRepository.findById(secondWordId).orElseThrow(
                () -> new NotFoundException("Vocabulary word with id " + secondWordId + " not found.")
        );

        if(!firstWord.getSetId().equals(secondWord.getSetId())){
            throw new BusinessException("Two words are not in the same set.");
        }

        int temp = firstWord.getPosition();
        firstWord.setPosition(secondWord.getPosition());
        secondWord.setPosition(temp);

        vocabularyWordRepository.save(firstWord);
        vocabularyWordRepository.save(secondWord);

        //Update set
        vocabularySetService.updateLastModified(firstWord.getSetId(), UUID.fromString(request.getUserId()));
    }

    public void uploadVocabularyWordImage(UploadVocabularyWordImageRequest request){
        //Update word
        VocabularyWord word = vocabularyWordRepository.findById(UUID.fromString(request.getId())).orElseThrow(
                () -> new NotFoundException("Vocabulary word not found.")
        );

        if(request.getImageUrl().isBlank()){
            throw new BusinessException("Vocabulary word image url is empty.");
        }

        word.setImageUrl(request.getImageUrl());
        vocabularyWordRepository.save(word);

        //Update set
        vocabularySetService.updateLastModified(word.getSetId(), UUID.fromString(request.getUserId()));
    }
}
