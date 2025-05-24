package com.nitb.usertestservice.dto;

import java.time.Duration;

public interface ResultStatisticProjection {
    String getTime();
    Integer getResultCount();
    Duration getTimeSpent();
    Float getAccuracy();
}
