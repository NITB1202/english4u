package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.Test.Test.response.*;
import com.nitb.testservice.grpc.*;
import com.nitb.userservice.grpc.UserResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TestMapper {
    private TestMapper() {}

    public static CreateTestResponseDto toCreateTestResponseDto(CreateTestResponse test) {
        return CreateTestResponseDto.builder()
                .id(UUID.fromString(test.getId()))
                .name(test.getName())
                .minutes(test.getMinutes())
                .topic(test.getTopic())
                .createdBy(UUID.fromString(test.getCreatedBy()))
                .createdAt(LocalDateTime.parse(test.getCreateAt()))
                .build();
    }

    public static TestDetailResponseDto toTestDetailResponseDto(TestDetailResponse test, UserResponse createdBy, UserResponse updatedBy) {
        return TestDetailResponseDto.builder()
                .id(UUID.fromString(test.getId()))
                .name(test.getName())
                .version(test.getVersion())
                .minutes(test.getMinutes())
                .topic(test.getTopic())
                .createdByName(createdBy.getName())
                .createByAvatarUrl(createdBy.getAvatarUrl())
                .createdAt(LocalDateTime.parse(test.getCreateAt()))
                .completedUsers(test.getCompletedUsers())
                .partCount(test.getPartCount())
                .questionCount(test.getQuestionCount())
                .updatedByName(updatedBy.getName())
                .updatedByAvatarUrl(updatedBy.getAvatarUrl())
                .updatedAt(LocalDateTime.parse(test.getUpdateAt()))
                .isDeleted(test.getIsDeleted())
                .build();
    }

    public static TestSummaryResponseDto toTestSummaryResponseDto(TestSummaryResponse test) {
        return TestSummaryResponseDto.builder()
                .id(UUID.fromString(test.getId()))
                .name(test.getName())
                .version(test.getVersion())
                .minutes(test.getMinutes())
                .topic(test.getTopic())
                .questionCount(test.getQuestionCount())
                .build();
    }

    public static TestsPaginationResponseDto toTestsPaginationResponseDto(TestsPaginationResponse tests) {
        List<TestSummaryResponseDto> dto = tests.getTestsList().stream()
                .map(TestMapper::toTestSummaryResponseDto)
                .toList();

        return TestsPaginationResponseDto.builder()
                .totalItems(tests.getTotalItems())
                .totalPages(tests.getTotalPages())
                .tests(dto)
                .build();
    }

    public static UpdateTestResponseDto toUpdateTestResponseDto(UpdateTestResponse test) {
        return UpdateTestResponseDto.builder()
                .id(UUID.fromString(test.getId()))
                .name(test.getName())
                .version(test.getVersion())
                .minutes(test.getMinutes())
                .topic(test.getTopic())
                .updatedBy(UUID.fromString(test.getUpdatedBy()))
                .updatedAt(LocalDateTime.parse(test.getUpdateAt()))
                .build();
    }

    public static DeleteTestResponseDto toDeleteTestResponseDto(DeleteTestResponse test) {
        return DeleteTestResponseDto.builder()
                .id(UUID.fromString(test.getId()))
                .updatedAt(LocalDateTime.parse(test.getUpdateAt()))
                .updatedBy(UUID.fromString(test.getUpdatedBy()))
                .isDeleted(test.getIsDeleted())
                .build();
    }

    public static TestStatisticResponseDto toTestStatisticResponseDto(TestStatisticResponse stat) {
        return TestStatisticResponseDto.builder()
                .testCount(stat.getTestCount())
                .completedUsers(stat.getCompletedUsers())
                .time(stat.getTime())
                .build();
    }

    public static GetPublishedTestStatisticsResponseDto toGetPublishedTestStatisticsResponseDto(GetPublishedTestStatisticsResponse stats) {
        List<TestStatisticResponseDto> dto = stats.getStatisticsList().stream()
                .map(TestMapper::toTestStatisticResponseDto)
                .toList();

        return GetPublishedTestStatisticsResponseDto.builder()
                .statistics(dto)
                .build();
    }
}
