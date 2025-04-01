package com.nitb.vocabularyservice.repository;

import com.nitb.vocabularyservice.entity.VocabularyWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VocabularyWordRepository extends JpaRepository<VocabularyWord, UUID> {
    boolean existByWordAndSetId(String word, UUID id);
    Page<VocabularyWord> findBySetId(UUID id, Pageable pageable);
    Page<VocabularyWord> findByWordContainingIgnoreCaseAndSetId(String word, UUID id, Pageable pageable);
    List<VocabularyWord> findBySetIdOrderByPositionAsc(UUID id);
}