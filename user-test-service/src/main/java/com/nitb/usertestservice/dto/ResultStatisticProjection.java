package com.nitb.usertestservice.dto;

public interface ResultStatisticProjection {
    String getTime();
    Long getResultCount();
    Long getTimeSpentSeconds();
    Double getAccuracy();
}
