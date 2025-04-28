package com.nitb.testservice.repository;

import com.nitb.testservice.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PartRepository extends JpaRepository<Part, UUID> {
}