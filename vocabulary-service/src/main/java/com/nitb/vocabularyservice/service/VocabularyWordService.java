package com.nitb.vocabularyservice.service;

import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.exceptions.NotFoundException;
import com.nitb.vocabularyservice.entity.VocabularySet;
import com.nitb.vocabularyservice.entity.VocabularyWord;
import com.nitb.vocabularyservice.grpc.CreateVocabularyWordRequest;
import com.nitb.vocabularyservice.grpc.CreateVocabularyWordsRequest;
import com.nitb.vocabularyservice.repository.VocabularySetRepository;
import com.nitb.vocabularyservice.repository.VocabularyWordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VocabularyWordService {
    private final VocabularyWordRepository vocabularyWordRepository;
    private final VocabularySetRepository vocabularySetRepository;

    public List<VocabularyWord> createVocabularyWords(CreateVocabularyWordsRequest request) {
        List<CreateVocabularyWordRequest> wordRequests = request.getWordsList();
        UUID setId = UUID.fromString(request.getSetId());

        VocabularySet set = vocabularySetRepository.findById(setId).orElseThrow(
                () -> new NotFoundException("Vocabulary set not found")
        );

        int position = set.getWordCount();
        List<VocabularyWord> result = new ArrayList<>();

        //Create words
        for (CreateVocabularyWordRequest wordRequest : wordRequests) {
            if(vocabularyWordRepository.existByWordAndSetId(wordRequest.getWord(), setId)){
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
}
