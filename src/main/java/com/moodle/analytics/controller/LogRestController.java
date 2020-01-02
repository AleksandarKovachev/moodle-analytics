package com.moodle.analytics.controller;

import com.moodle.analytics.repository.LogRecordRepository;
import com.moodle.analytics.resource.LogRecordByDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LogRestController {

    @Autowired
    private LogRecordRepository logRecordRepository;

    @GetMapping("/logRecord")
    public List<LogRecordByDate> logRecords() {
        return logRecordRepository.getLogRecordsByDate();
    }

}
