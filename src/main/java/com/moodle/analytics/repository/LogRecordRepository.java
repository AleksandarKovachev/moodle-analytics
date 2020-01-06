package com.moodle.analytics.repository;

import com.moodle.analytics.entity.LogRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LogRecordRepository extends ElasticsearchRepository<LogRecord, String>, LogRecordCustomRepository {

    List<LogRecord> findByFileName(String fileName, Pageable pageable);

    List<LogRecord> findByIsErrorOrderByRecordSequenceAsc(int isError);

}
