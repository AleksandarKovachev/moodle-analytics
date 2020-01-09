package com.moodle.analytics.service;

import com.moodle.analytics.entity.LogRecord;
import com.moodle.analytics.repository.LogRecordRepository;
import com.moodle.analytics.resource.LogError;
import com.moodle.analytics.resource.LogErrorFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogRecordService {

    @Autowired
    private LogRecordRepository logRecordRepository;

    public List<LogErrorFile> getLogErrorFiles() {
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
