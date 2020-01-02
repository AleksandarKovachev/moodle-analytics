package com.moodle.analytics.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogRecordByDate {

    private Date date;

    private long logRecordsCount;

}
