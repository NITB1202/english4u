package com.nitb.uservocabularyservice.repository;

import com.nitb.uservocabularyservice.entity.SavedSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SavedSetRepository extends JpaRepository<SavedSet, UUID> {
}