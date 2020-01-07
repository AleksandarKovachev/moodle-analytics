package com.moodle.analytics.constant;

public enum ColumnChartReportType {

    DAY_OF_WEEK("doc['recordDate'].date.getDayOfWeek()"), HOUR_OF_DAY("doc['recordDate'].date.getHourOfDay()");

    private String script;

    ColumnChartReportType(String script) {
        this.script = script;
    }

    public String getScript() {
        return script;
    }
}
