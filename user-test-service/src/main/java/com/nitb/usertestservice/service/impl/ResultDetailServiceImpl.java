package com.nitb.usertestservice.service.impl;

import com.nitb.common.enums.AnswerState;
import com.nitb.common.mappers.AnswerStateMapper;
import com.nitb.usertestservice.entity.ResultDetail;
import com.nitb.usertestservice.grpc.CreateResultDetailRequest;
import com.nitb.usertestservice.repository.ResultDetailRepository;
import com.nitb.usertestservice.service.ResultDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResultDetailServiceImpl implements ResultDetailService {
    private final ResultDetailRepository resultDetailRepository;

    @Override
    public void createResultDetails(UUID resultId, List<CreateResultDetailRequest> requests) {
        List<ResultDetail> resultDetails = new ArrayList<>();

        for(CreateResultDetailRequest request : requests) {
            UUID questionId = UUID.fromString(request.getQuestionId());
            AnswerState state = AnswerStateMapper.toEnum(request.getState());

            ResultDetail detail = ResultDetail.builder()
                    .resultId(resultId)
                    .questionId(questionId)
                    .userAnswer(request.getUserAnswer())
                    .state(state)
                    .build();

            resultDetails.add(detail);
        }

        resultDetailRepository.saveAll(resultDetails);
    }

    @Override
    public List<ResultDetail> getAllResultDetailsForResult(UUID resultId) {
        return resultDetailRepository.findByResultId(resultId);
    }
}
