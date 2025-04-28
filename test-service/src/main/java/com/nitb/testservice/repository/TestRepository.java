package com.nitb.testservice.repository;

import com.nitb.testservice.entity.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TestRepository extends JpaRepository<Test, UUID> {
    boolean existsByNameAndIsDeletedFalse(String name);
    Page<Test> findByIsDeletedFalse(Pageable pageable);
    Page<Test> findByIsDeletedTrue(Pageable pageable);
    Page<Test> findByNameContainingIgnoreCaseAndIsDeletedFalse(String name, Pageable pageable);
}