package com.nitb.usertestservice.service.impl;

import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.exceptions.NotFoundException;
import com.nitb.usertestservice.dto.ResultStatisticDto;
import com.nitb.usertestservice.dto.ResultStatisticProjection;
import com.nitb.usertestservice.entity.Result;
import com.nitb.usertestservice.grpc.CreateResultRequest;
import com.nitb.usertestservice.grpc.GetResultByIdRequest;
import com.nitb.usertestservice.grpc.GetResultStatisticsRequest;
import com.nitb.usertestservice.grpc.GetResultsRequest;
import com.nitb.usertestservice.repository.ResultRepository;
import com.nitb.usertestservice.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {
    private final ResultRepository resultRepository;
    private final int DEFAULT_SIZE = 10;

    @Override
    public Result createResult(CreateResultRequest request) {
        if(request.getSecondsSpent() < 0) {
            throw new BusinessException("Seconds spent must be greater than or equal to zero.");
        }

        Result result = Result.builder()
                .testId(UUID.fromString(request.getTestId()))
                .userId(UUID.fromString(request.getUserId()))
                .submitTime(LocalDateTime.now())
                .secondsSpent(request.getSecondsSpent())
                .score(request.getScore())
                .accuracy(request.getAccuracy())
                .build();

        return resultRepository.save(result);
    }

    @Override
    public Result getResultById(GetResultByIdRequest request) {
        return resultRepository.findById(UUID.fromString(request.getId())).orElseThrow(
                () -> new NotFoundException("Result not found.")
        );
    }

    @Override
    public Page<Result> getResults(GetResultsRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return resultRepository.findByUserId(userId, PageRequest.of(page, size));
    }

    @Override
    public List<ResultStatisticDto> getResultStatistics(GetResultStatisticsRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        LocalDateTime from = LocalDate.parse(request.getFrom()).atStartOfDay();
        LocalDateTime to = LocalDate.parse(request.getTo()).atTime(23, 59, 59);

        List<ResultStatisticProjection> statistics = new ArrayList<>();

        switch(request.getGroupBy()) {
            case WEEK -> statistics.addAll(resultRepository.getStatsByWeek(userId, from, to));
            case MONTH -> statistics.addAll(resultRepository.getStatsByMonth(userId, from, to));
            case YEAR -> statistics.addAll(resultRepository.getStatsByYear(userId, from, to));
        }

        return statistics.stream()
                .map(r -> new ResultStatisticDto(r.getTime(), r.getResultCount(),
                        r.getAvgSecondsSpent(), r.getAvgAccuracy()))
                .toList();
    }
}
