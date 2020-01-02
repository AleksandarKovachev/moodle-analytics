package com.moodle.analytics.repository;

import com.moodle.analytics.resource.LogRecordByDate;

import java.util.List;

public interface LogRecordCustomRepository {

	List<LogRecordByDate> getLogRecordsByDate();

}
