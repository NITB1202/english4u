package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.Statistic.AdminStatisticResponseDto;
import com.nitb.apigateway.dto.Statistic.AdminsPaginationResponseDto;
import com.nitb.apigateway.dto.Statistic.LearnerStatisticResponseDto;
import com.nitb.apigateway.dto.Statistic.LearnersPaginationResponseDto;
import com.nitb.authservice.grpc.AccountResponse;
import com.nitb.authservice.grpc.AccountsResponse;
import com.nitb.testservice.grpc.AdminTestStatisticResponse;
import com.nitb.testservice.grpc.AdminTestStatisticsResponse;
import com.nitb.userservice.grpc.UserDetailResponse;
import com.nitb.userservice.grpc.UsersResponse;
import com.nitb.usertestservice.grpc.LearnerTestStatisticResponse;
import com.nitb.usertestservice.grpc.LearnerTestStatisticsResponse;
import com.nitb.vocabularyservice.grpc.AdminSetStatisticResponse;
import com.nitb.vocabularyservice.grpc.AdminSetStatisticsResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StatisticMapper {
    private StatisticMapper() {}

    public static AdminStatisticResponseDto toAdminStatisticResponseDto(AccountResponse account, UserDetailResponse user,
                                                                        AdminTestStatisticResponse test, AdminSetStatisticResponse set) {
        return AdminStatisticResponseDto.builder()
                .userId(UUID.fromString(user.getId()))
                .name(user.getName())
                .email(account.getEmail())
                .joinAt(LocalDateTime.parse(user.getJoinAt()))
                .totalTestsPublished(test.getTotalPublishedTests())
                .totalSetsPublished(set.getTotalPublishedSets())
                .build();
    }

    public static AdminsPaginationResponseDto toAdminsPaginationResponseDto(AccountsResponse accounts, UsersResponse users,
                                                                            AdminTestStatisticsResponse tests, AdminSetStatisticsResponse sets) {
        List<AdminStatisticResponseDto> admins = new ArrayList<>();

        for(int i = 0; i < users.getUsersCount(); i++) {
            AccountResponse account = accounts.getAccounts(i);
            UserDetailResponse user = users.getUsers(i);
            AdminTestStatisticResponse test = tests.getStatistics(i);
            AdminSetStatisticResponse set = sets.getStatistics(i);

            admins.add(toAdminStatisticResponseDto(account, user, test, set));
        }

        return AdminsPaginationResponseDto.builder()
                .admins(admins)
                .totalItems(accounts.getTotalItems())
                .totalPages(accounts.getTotalPages())
                .build();
    }

    public static LearnerStatisticResponseDto toLearnerStatisticResponseDto(AccountResponse account, UserDetailResponse user,
                                                                            LearnerTestStatisticResponse test) {
        return LearnerStatisticResponseDto.builder()
                .userId(UUID.fromString(user.getId()))
                .name(user.getName())
                .email(account.getEmail())
                .joinAt(LocalDateTime.parse(user.getJoinAt()))
                .totalTestsTaken(test.getTotalTestsTaken())
                .avgScore(test.getAvgScore())
                .build();
    }

    public static LearnersPaginationResponseDto toLearnersPaginationResponseDto(AccountsResponse accounts, UsersResponse users,
                                                                                LearnerTestStatisticsResponse tests) {
        List<LearnerStatisticResponseDto> learners = new ArrayList<>();

        for(int i = 0; i < users.getUsersCount(); i++) {
            AccountResponse account = accounts.getAccounts(i);
            UserDetailResponse user = users.getUsers(i);
            LearnerTestStatisticResponse test = tests.getStatistics(i);

            learners.add(toLearnerStatisticResponseDto(account, user, test));
        }

        return LearnersPaginationResponseDto.builder()
                .learners(learners)
                .totalItems(accounts.getTotalItems())
                .totalPages(accounts.getTotalPages())
                .build();
    }
}
