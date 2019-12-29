package com.moodle.analytics.entity;

public enum SyncJob {

    ONE_MINUTE(1, "1 minute"),
    FIVE_MINUTES(5, "5 minutes"),
    TEN_MINUTES(10, "10 minutes"),
    THIRTY_MINUTES(30, "30 minute"),
    ONE_HOUR(1, "1 hour"),
    TWO_HOURS(2, "2 hours"),
    SIX_HOURS(6, "6 hours"),
    TWELVE_HOURS(12, "12 hours"),
    ONE_DAY(1, "1 day");

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

    public SyncJob fromValue(String value) {
        for (SyncJob syncJob : values()) {
            if (syncJob.getValue().equals(value)) {
                return syncJob;
            }
        }
        throw new IllegalArgumentException("Invalid value for the synchronization job " + value);
    }
}
