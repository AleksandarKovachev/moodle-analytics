package com.moodle.analytics.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogErrorFile {

    private String fileName;

    private List<LogError> logErrors;

    private String directory;

    private Date fileDate;

}
