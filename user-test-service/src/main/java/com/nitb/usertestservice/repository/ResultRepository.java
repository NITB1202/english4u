package com.nitb.usertestservice.repository;

import com.nitb.usertestservice.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ResultRepository extends JpaRepository<Result, UUID> {
}