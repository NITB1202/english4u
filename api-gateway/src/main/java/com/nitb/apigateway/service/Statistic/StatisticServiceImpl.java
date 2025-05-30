package com.nitb.apigateway.service.Statistic;

import com.nitb.apigateway.dto.Statistic.AdminsPaginationResponseDto;
import com.nitb.apigateway.dto.Statistic.LearnersPaginationResponseDto;
import com.nitb.apigateway.grpc.AuthServiceGrpcClient;
import com.nitb.apigateway.grpc.TestServiceGrpcClient;
import com.nitb.apigateway.grpc.UserServiceGrpcClient;
import com.nitb.apigateway.grpc.VocabularyServiceGrpcClient;
import com.nitb.apigateway.mapper.StatisticMapper;
import com.nitb.authservice.grpc.AccountResponse;
import com.nitb.authservice.grpc.AccountsResponse;
import com.nitb.testservice.grpc.AdminTestStatisticsResponse;
import com.nitb.userservice.grpc.UsersResponse;
import com.nitb.vocabularyservice.grpc.AdminSetStatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService{
    private final AuthServiceGrpcClient authGrpc;
    private final UserServiceGrpcClient userGrpc;
    private final TestServiceGrpcClient testGrpc;
    private final VocabularyServiceGrpcClient vocabularyGrpc;

    @Override
    public Mono<LearnersPaginationResponseDto> getLearnersStatistics(int page, int size) {
        return null;
    }

    @Override
    public Mono<AdminsPaginationResponseDto> getAdminsStatistics(int page, int size) {
        return Mono.fromCallable(()->{
            AccountsResponse accounts = authGrpc.getAdmins(page, size);

            List<String> userIds = accounts.getAccountsList().stream()
                    .map(AccountResponse::getUserId)
                    .toList();

            UsersResponse users = userGrpc.getUsersByListOfIds(userIds);
            AdminTestStatisticsResponse testStatistics = testGrpc.getAdminTestStatistics(userIds);
            AdminSetStatisticsResponse setStatistics = vocabularyGrpc.getAdminSetStatistics(userIds);

            return StatisticMapper.toAdminsPaginationResponseDto(accounts, users, testStatistics, setStatistics);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<LearnersPaginationResponseDto> searchLearnerStatisticByEmail(String keyword, int page, int size) {
        return null;
    }

    @Override
    public Mono<AdminsPaginationResponseDto> searchAdminStatisticByEmail(String keyword, int page, int size) {
        return Mono.fromCallable(()->{
            AccountsResponse accounts = authGrpc.searchAdminByEmail(keyword, page, size);

            List<String> userIds = accounts.getAccountsList().stream()
                    .map(AccountResponse::getUserId)
                    .toList();

            UsersResponse users = userGrpc.getUsersByListOfIds(userIds);
            AdminTestStatisticsResponse testStatistics = testGrpc.getAdminTestStatistics(userIds);
            AdminSetStatisticsResponse setStatistics = vocabularyGrpc.getAdminSetStatistics(userIds);

            return StatisticMapper.toAdminsPaginationResponseDto(accounts, users, testStatistics, setStatistics);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
