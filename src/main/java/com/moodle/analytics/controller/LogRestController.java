package com.moodle.analytics.controller;

import com.moodle.analytics.constant.LineChartReportType;
import com.moodle.analytics.constant.PieChartReportType;
import com.moodle.analytics.entity.LogRecord;
import com.moodle.analytics.error.exception.BadRequestException;
import com.moodle.analytics.repository.LogRecordRepository;
import com.moodle.analytics.resource.LogRecordByDate;
import com.moodle.analytics.resource.PieLogRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LogRestController {

    @Autowired
    private LogRecordRepository logRecordRepository;

    @GetMapping("/logRecord/lineChart")
    public List<LogRecordByDate> lineChart(@RequestParam LineChartReportType reportType,
                                           @RequestParam(required = false) Long fromDate,
                                           @RequestParam(required = false) Long toDate) {
        if (reportType == LineChartReportType.CUSTOM && fromDate == null && toDate == null) {
            throw BadRequestException.builder().build();
        }
        return logRecordRepository.getLogRecordsByDate(reportType, fromDate, toDate);
    }

    @GetMapping("/logRecord/pieChart")
    public List<PieLogRecord> pieChart(@RequestParam PieChartReportType reportType) {
        return logRecordRepository.getLogRecordsForPieChart(reportType);
    }

}
