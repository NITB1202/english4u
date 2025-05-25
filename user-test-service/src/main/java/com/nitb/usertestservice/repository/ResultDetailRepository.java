package com.nitb.usertestservice.repository;

import com.nitb.usertestservice.entity.ResultDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ResultDetailRepository extends JpaRepository<ResultDetail, UUID> {
    List<ResultDetail> findByResultId(UUID resultId);
}