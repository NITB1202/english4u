package com.nitb.apigateway.service.Statistic;

import com.nitb.apigateway.dto.User.response.AdminsPaginationResponseDto;
import com.nitb.apigateway.dto.User.response.LearnersPaginationResponseDto;
import reactor.core.publisher.Mono;

public interface StatisticService {
    Mono<LearnersPaginationResponseDto> getLearnersStatistics(int page, int size);
    Mono<AdminsPaginationResponseDto> getAdminsStatistics(int page, int size);
    Mono<LearnersPaginationResponseDto> searchLearnerStatisticByEmail(String keyword, int page, int size);
    Mono<AdminsPaginationResponseDto> searchAdminStatisticByEmail(String keyword, int page, int size);
}
