package com.moodle.analytics.entity;

public enum SyncJob {

    ONE_MINUTE(60_000, "1 minute"),
    FIVE_MINUTES(300_000, "5 minutes"),
    TEN_MINUTES(600_000, "10 minutes"),
    THIRTY_MINUTES(1_800_000, "30 minute"),
    ONE_HOUR(3_600_000, "1 hour"),
    TWO_HOURS(7_200_000, "2 hours"),
    SIX_HOURS(21_600_000, "6 hours"),
    TWELVE_HOURS(43_200_000, "12 hours"),
    ONE_DAY(86_400_000, "1 day");

    private int key;
    private String value;

    SyncJob(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
