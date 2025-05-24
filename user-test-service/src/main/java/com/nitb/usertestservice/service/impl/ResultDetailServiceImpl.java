package com.nitb.usertestservice.service.impl;

import com.nitb.usertestservice.entity.ResultDetail;
import com.nitb.usertestservice.grpc.CreateResultDetailRequest;
import com.nitb.usertestservice.repository.ResultDetailRepository;
import com.nitb.usertestservice.service.ResultDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResultDetailServiceImpl implements ResultDetailService {
    private final ResultDetailRepository resultDetailRepository;

    @Override
    public void createResultDetails(UUID resultId, List<CreateResultDetailRequest> requests) {

    }

    @Override
    public List<ResultDetail> getAllResultDetailsForResult(UUID resultId) {
        return List.of();
    }
}
