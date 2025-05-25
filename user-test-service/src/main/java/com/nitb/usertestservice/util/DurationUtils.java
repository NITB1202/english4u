package com.nitb.usertestservice.util;

import java.time.Duration;

public class DurationUtils {
    public static Duration parseToDuration(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);
        return Duration.ofHours(hours)
                .plusMinutes(minutes)
                .plusSeconds(seconds);
    }

    public static String parseToString(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        long hours = absSeconds / 3600;
        long minutes = (absSeconds % 3600) / 60;
        long secs = absSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }
}
