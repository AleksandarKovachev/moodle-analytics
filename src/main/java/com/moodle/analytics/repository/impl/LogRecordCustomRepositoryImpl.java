package com.moodle.analytics.repository.impl;

import com.moodle.analytics.repository.LogRecordCustomRepository;
import com.moodle.analytics.resource.LogRecordByDate;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class LogRecordCustomRepositoryImpl implements LogRecordCustomRepository {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public List<LogRecordByDate> getLogRecordsByDate() {
        SearchResponse response = elasticsearchTemplate.getClient()
                .prepareSearch("log_record")
                .setQuery(QueryBuilders.matchAllQuery())
                .addAggregation(
                        AggregationBuilders.dateHistogram("date")
                                .minDocCount(0)
                                .dateHistogramInterval(DateHistogramInterval.DAY)
                                .field("recordDate"))
                .execute().actionGet();

        List<InternalDateHistogram.Bucket> dateHistogram = ((InternalDateHistogram) response.getAggregations().getAsMap().get("date")).getBuckets();
        return dateHistogram
                .stream()
                .map(bucket -> new LogRecordByDate(((DateTime) bucket.getKey()).toDate(), bucket.getDocCount()))
                .collect(Collectors.toList());
    }

}
