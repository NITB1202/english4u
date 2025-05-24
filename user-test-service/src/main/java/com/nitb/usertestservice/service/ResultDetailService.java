package com.nitb.usertestservice.service;

import com.nitb.usertestservice.entity.ResultDetail;
import com.nitb.usertestservice.grpc.CreateResultDetailRequest;

import java.util.List;
import java.util.UUID;

public interface ResultDetailService {
    void createResultDetails(UUID resultId, List<CreateResultDetailRequest> requests);
    List<ResultDetail> getAllResultDetailsForResult(UUID resultId);
}
