package com.moodle.analytics.repository;

import com.moodle.analytics.constant.LineChartReportType;
import com.moodle.analytics.constant.PieChartReportType;
import com.moodle.analytics.resource.LogRecordByDate;
import com.moodle.analytics.resource.PieLogRecord;

import java.util.List;

public interface LogRecordCustomRepository {

	List<LogRecordByDate> getLogRecordsByDate(LineChartReportType lineChartReportType, Long fromDate, Long toDate);

	List<PieLogRecord> getLogRecordsForPieChart(PieChartReportType reportType);

}
