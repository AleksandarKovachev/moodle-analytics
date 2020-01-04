package com.moodle.analytics.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Document(indexName = "log_record")
public class LogRecord {

    @Id
    private String id;

    private Long recordSequence;

    @Field(type = FieldType.Date)
    private Date recordDate;

    private String eventContext;

    private String component;

    private String eventName;

    private String description;

    private String origin;

    private String ipAddress;

    private String fileName;

    private String directory;

    @Field(type = FieldType.Date)
    private Date fileDate;

    @Field(type = FieldType.Date)
    private Date createdTimestamp;

}
