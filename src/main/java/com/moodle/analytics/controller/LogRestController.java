package com.moodle.analytics.controller;

import com.moodle.analytics.constant.ReportType;
import com.moodle.analytics.error.exception.BadRequestException;
import com.moodle.analytics.repository.LogRecordRepository;
import com.moodle.analytics.resource.LogRecordByDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class LogRestController {

    @Autowired
    private LogRecordRepository logRecordRepository;

    @GetMapping("/logRecord")
    public List<LogRecordByDate> logRecords(@RequestParam ReportType reportType,
                                            @RequestParam(required = false) Long fromDate,
                                            @RequestParam(required = false) Long toDate) {
        if (reportType == ReportType.CUSTOM && fromDate == null && toDate == null) {
            throw BadRequestException.builder().build();
        }
        return logRecordRepository.getLogRecordsByDate(reportType, fromDate, toDate);
    }

}
