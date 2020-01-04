package com.moodle.analytics.service;

import com.moodle.analytics.entity.LogRecord;
import com.moodle.analytics.repository.ConfigurationRepository;
import com.moodle.analytics.repository.LogRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

@Service
@Slf4j
public class ParseLogService {

    private static final SimpleDateFormat LOG_DATE_FORMAT = new SimpleDateFormat("dd/MM/yy, HH:mm");

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @Async
    public void parseLogs() {
        String logDirectoryPath = configurationRepository.findById(1L).orElseThrow(IllegalArgumentException::new).getValue();
        try (Stream<Path> filePathStream = Files.walk(Paths.get(logDirectoryPath))) {
            filePathStream.forEach(filePath -> {
                if (Files.isRegularFile(filePath) && logRecordRepository.findByFileName(filePath.getFileName().toString()).isEmpty()) {
                    try (BufferedReader reader = new BufferedReader(new BufferedReader(new InputStreamReader(
                            new FileInputStream(filePath.toFile()), StandardCharsets.UTF_8)))) {
                        String line;
                        long recordSequence = 1;
                        while ((line = reader.readLine()) != null) {
                            processLine(recordSequence, filePath, line, logDirectoryPath);
                            recordSequence++;
                        }
                    } catch (IOException e) {
                        log.error("An error has occurred while reading file: " + filePath, e);
                    }
                }
            });
        } catch (IOException e) {
            log.error("An error has occurred while exploring files from directory: " + logDirectoryPath, e);
        }
    }

    private void processLine(long recordSequence, Path filePath, String line, String logDirectoryPath) {
        String date = line.substring(1, line.lastIndexOf("\""));
        String currentLine = line.substring(line.lastIndexOf("\"") + 2);
        String[] columns = currentLine.split(",");

        if (columns.length != 6) {
            log.error("The line '" + line + "' is with not proper format.");
            return;
        }

        LogRecord logRecord = new LogRecord();
        logRecord.setRecordDate(getLogDate(date, line));
        logRecord.setEventContext(columns[0]);
        logRecord.setComponent(columns[1]);
        logRecord.setEventName(columns[2]);
        logRecord.setDescription(columns[3]);
        logRecord.setOrigin(columns[4]);
        logRecord.setIpAddress(columns[5]);

        logRecord.setRecordSequence(recordSequence);
        logRecord.setCreatedTimestamp(new Date());
        logRecord.setFileDate(new Date(filePath.toFile().lastModified()));
        logRecord.setFileName(filePath.getFileName().toString());
        logRecord.setDirectory(logDirectoryPath);

        logRecordRepository.save(logRecord);
    }

    private Date getLogDate(String value, String line) {
        try {
            return LOG_DATE_FORMAT.parse(value);
        } catch (ParseException e) {
            log.error("An error has occurred while parsing the date from log with line data: " + line, e);
            return null;
        }
    }

}
