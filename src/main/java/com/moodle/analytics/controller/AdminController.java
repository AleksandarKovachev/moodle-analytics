package com.moodle.analytics.controller;

import com.moodle.analytics.entity.Configuration;
import com.moodle.analytics.entity.LogRecord;
import com.moodle.analytics.entity.SyncJob;
import com.moodle.analytics.repository.LogRecordRepository;
import com.moodle.analytics.resource.LogError;
import com.moodle.analytics.resource.LogErrorFile;
import com.moodle.analytics.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminController {

    @Autowired
    private LogRecordRepository logRecordRepository;

    @Autowired
    private ConfigurationService configurationService;

    @GetMapping("/configure")
    public ModelAndView configure() {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("configurations", configurationService.getAllConfigurations());
        modelMap.addAttribute("syncJobValues", SyncJob.values());
        return new ModelAndView("configure", modelMap);
    }

    @PostMapping("/configure/{id}")
    public ResponseEntity<Configuration> configure(@PathVariable Long id, @RequestParam String value, Principal principal) {
        Configuration configuration = configurationService.updateConfiguration(id, value, principal);
        return new ResponseEntity<>(configuration, HttpStatus.OK);
    }

    @GetMapping("/logRecord/error")
    public ModelAndView errorLogRecord() {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("logErrorFiles", getLogErrorFiles());
        return new ModelAndView("fragment/errorLogRecords", modelMap);
    }

    private List<LogErrorFile> getLogErrorFiles() {
        List<LogRecord> logRecords = logRecordRepository.findByIsErrorOrderByRecordSequenceAsc(1);
        List<LogErrorFile> logErrorFiles = new ArrayList<>();
        for (LogRecord logRecord : logRecords) {
            List<LogErrorFile> logErrorFileList = logErrorFiles
                    .stream()
                    .filter(l -> logRecord.getFileName().equals(l.getFileName()))
                    .collect(Collectors.toList());
            if (logErrorFileList.isEmpty()) {
                LogErrorFile logErrorFile = getLogErrorFile(logRecord);
                List<LogError> logErrors = new ArrayList<>();
                logErrors.add(getLogError(logRecord));
                logErrorFile.setLogErrors(logErrors);
                logErrorFiles.add(logErrorFile);
            } else {
                logErrorFileList.get(0).getLogErrors().add(getLogError(logRecord));
            }
        }
        return logErrorFiles;
    }

    private LogErrorFile getLogErrorFile(LogRecord logRecord) {
        LogErrorFile logErrorFile = new LogErrorFile();
        logErrorFile.setFileName(logRecord.getFileName());
        logErrorFile.setDirectory(logRecord.getDirectory());
        logErrorFile.setFileDate(logRecord.getFileDate());
        return logErrorFile;
    }

    private LogError getLogError(LogRecord logRecord) {
        LogError logError = new LogError();
        logError.setLine(logRecord.getRawLine());
        logError.setLineNumber(logRecord.getRecordSequence());
        return logError;
    }

}
