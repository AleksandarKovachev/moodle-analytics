package com.moodle.analytics.controller;

import com.moodle.analytics.constant.ColumnChartReportType;
import com.moodle.analytics.constant.LineChartReportType;
import com.moodle.analytics.constant.PieChartReportType;
import com.moodle.analytics.error.exception.BadRequestException;
import com.moodle.analytics.repository.LogRecordRepository;
import com.moodle.analytics.resource.LogRecordByDate;
import com.moodle.analytics.resource.LogRecordTerm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

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
        return logRecordRepository.getLogRecordsForLineChart(reportType, fromDate, toDate);
    }

    @GetMapping("/logRecord/pieChart")
    public List<LogRecordTerm> pieChart(@RequestParam PieChartReportType reportType) {
        return logRecordRepository.getLogRecordsForPieChart(reportType);
    }

    @GetMapping("/logRecord/columnChart")
    public List<LogRecordTerm> columnChart(@RequestParam ColumnChartReportType reportType, Locale locale) {
        return logRecordRepository.getLogRecordsForColumnChart(reportType, locale);
    }

}
