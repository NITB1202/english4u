package com.nitb.common.mappers;

import com.nitb.common.grpc.GroupBy;

public class GroupByMapper {
    private GroupByMapper() {}

    public static GroupBy toGrpcEnum(com.nitb.common.enums.GroupBy groupBy) {
        return switch (groupBy) {
            case WEEK -> GroupBy.WEEK;
            case MONTH -> GroupBy.MONTH;
            case YEAR -> GroupBy.YEAR;
        };
    }
}
