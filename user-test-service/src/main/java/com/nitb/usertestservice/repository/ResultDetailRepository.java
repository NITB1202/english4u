package com.nitb.usertestservice.repository;

import com.nitb.usertestservice.entity.ResultDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ResultDetailRepository extends JpaRepository<ResultDetail, UUID> {
}