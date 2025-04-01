package com.nitb.vocabularyservice.service;

import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.exceptions.NotFoundException;
import com.nitb.vocabularyservice.entity.VocabularySet;
import com.nitb.vocabularyservice.entity.VocabularyWord;
import com.nitb.vocabularyservice.grpc.*;
import com.nitb.vocabularyservice.repository.VocabularySetRepository;
import com.nitb.vocabularyservice.repository.VocabularyWordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class VocabularyWordService {
    private final VocabularyWordRepository vocabularyWordRepository;
    private final VocabularySetRepository vocabularySetRepository;

    private final int DEFAULT_SIZE = 10;

    public List<VocabularyWord> createVocabularyWords(CreateVocabularyWordsRequest request) {
        List<CreateVocabularyWordRequest> wordRequests = request.getWordsList();
        UUID setId = UUID.fromString(request.getSetId());

        VocabularySet set = vocabularySetRepository.findById(setId).orElseThrow(
                () -> new NotFoundException("Vocabulary set not found.")
        );

        int position = set.getWordCount();
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
                    .pronunciation(wordRequest.getPronun())
                    .translation(wordRequest.getTrans())
                    .example(wordRequest.getEx())
                    .build();

            result.add(word);
        }

        //Update set
        set.setWordCount(position);
        set.setUpdatedBy(UUID.fromString(request.getUserId()));
        set.setUpdatedAt(LocalDateTime.now());

        vocabularySetRepository.save(set);

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
        String word = vocabularyWord.getWord();
        String pronun = vocabularyWord.getPronunciation();
        String trans = vocabularyWord.getTranslation();
        String ex = vocabularyWord.getExample();

        if(!word.isBlank()){
            if(vocabularyWordRepository.existsByWordAndSetId(word, vocabularyWord.getSetId()))
                throw new BusinessException("The word '" + word +"' already exists in this set.");
            else
                vocabularyWord.setWord(word);
        }

        if(!pronun.isBlank()){
            vocabularyWord.setPronunciation(pronun);
        }

        if(!trans.isBlank()){
            vocabularyWord.setTranslation(trans);
        }

        if(!ex.isBlank()){
            vocabularyWord.setExample(ex);
        }

        //Update set
        updateLastModified(vocabularyWord.getSetId(), UUID.fromString(request.getUserId()));

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
        updateLastModified(firstWord.getSetId(), UUID.fromString(request.getUserId()));
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
        updateLastModified(word.getSetId(), UUID.fromString(request.getUserId()));
    }

    public void deleteVocabularyWords(DeleteVocabularyWordsRequest request){
        UUID setId = UUID.fromString(request.getSetId());

        //Update word
        List<String> idsString = request.getIdsList();
        List<UUID> ids = idsString.stream().map(UUID::fromString).toList();

        vocabularyWordRepository.deleteAllById(ids);
        List<VocabularyWord> remainingWords = vocabularyWordRepository.findBySetIdOrderByPositionAsc(setId);
        IntStream.range(0, remainingWords.size()).forEach(i -> remainingWords.get(i).setPosition(i + 1));

        //Update set
        VocabularySet set = vocabularySetRepository.findById(setId).orElseThrow(
                () -> new NotFoundException("Vocabulary set not found.")
        );

        set.setWordCount(remainingWords.size());
        set.setUpdatedBy(UUID.fromString(request.getUserId()));
        set.setUpdatedAt(LocalDateTime.now());

        vocabularySetRepository.save(set);
    }

    private void updateLastModified(UUID setId, UUID userId){
        VocabularySet set = vocabularySetRepository.findById(setId).orElseThrow(
                () -> new NotFoundException("Vocabulary set not found.")
        );

        set.setUpdatedBy(userId);
        set.setUpdatedAt(LocalDateTime.now());

        vocabularySetRepository.save(set);
    }
}
