package com.moodle.analytics.repository;

import com.moodle.analytics.constant.ColumnChartReportType;
import com.moodle.analytics.constant.LineChartReportType;
import com.moodle.analytics.constant.PieChartReportType;
import com.moodle.analytics.resource.LogRecordByDate;
import com.moodle.analytics.resource.LogRecordTerm;

import java.util.List;
import java.util.Locale;

public interface LogRecordCustomRepository {

    List<LogRecordByDate> getLogRecordsForLineChart(LineChartReportType lineChartReportType, Long fromDate, Long toDate);

    List<LogRecordTerm> getLogRecordsForPieChart(PieChartReportType reportType);

    List<LogRecordTerm> getLogRecordsForColumnChart(ColumnChartReportType reportType, Locale locale);

}
