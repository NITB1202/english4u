package com.nitb.testservice.repository;

import com.nitb.testservice.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Page<Comment> findByTestIdAndParentIdIsNullOrderByCreateAtDesc(UUID testId, Pageable pageable);
}