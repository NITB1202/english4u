package com.nitb.vocabularyservice.repository;

import com.nitb.vocabularyservice.entity.VocabularyWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VocabularyWordRepository extends JpaRepository<VocabularyWord, UUID> {
}