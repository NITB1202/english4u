package com.nitb.testservice.repository;

import com.nitb.testservice.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TestRepository extends JpaRepository<Test, UUID> {
}