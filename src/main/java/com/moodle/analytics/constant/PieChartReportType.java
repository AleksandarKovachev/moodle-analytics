package com.moodle.analytics.constant;

public enum PieChartReportType {

    EVENT_CONTEXT("eventContext"),
    COMPONENT("component"),
    EVENT_NAME("eventName"),
    DESCRIPTION("description"),
    ORIGIN("origin"),
    IP_ADDRESS("ipAddress"),
    FILE("fileName");

    private String field;

    PieChartReportType(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
