package com.moodle.analytics.repository;

import com.moodle.analytics.constant.ReportType;
import com.moodle.analytics.resource.LogRecordByDate;

import java.util.List;

public interface LogRecordCustomRepository {

	List<LogRecordByDate> getLogRecordsByDate(ReportType reportType, Long fromDate, Long toDate);

}
