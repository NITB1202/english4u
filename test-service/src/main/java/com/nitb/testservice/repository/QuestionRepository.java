package com.nitb.testservice.repository;

import com.nitb.testservice.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
    int countByPartId(UUID partId);
    List<Question> findByPartId(UUID partId);
}