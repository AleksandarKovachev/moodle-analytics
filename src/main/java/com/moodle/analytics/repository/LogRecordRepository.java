package com.moodle.analytics.repository;

import com.moodle.analytics.entity.LogRecord;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LogRecordRepository extends ElasticsearchRepository<LogRecord, String> {

    List<LogRecord> findByFileName(String fileName);

}