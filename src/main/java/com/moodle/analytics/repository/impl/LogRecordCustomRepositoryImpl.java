package com.moodle.analytics.repository.impl;

import com.moodle.analytics.constant.ReportType;
import com.moodle.analytics.repository.LogRecordCustomRepository;
import com.moodle.analytics.resource.LogRecordByDate;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.range.DateRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.util.*;
import java.util.stream.Collectors;

public class LogRecordCustomRepositoryImpl implements LogRecordCustomRepository {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public List<LogRecordByDate> getLogRecordsByDate(ReportType reportType, Long fromDate, Long toDate) {
        DateRangeAggregationBuilder dateRangeAggregationBuilder = prepareDateRangeAggregation(reportType, fromDate, toDate);

        DateHistogramAggregationBuilder dateHistogramAggregation = AggregationBuilders.dateHistogram("date")
                .minDocCount(0)
                .dateHistogramInterval(DateHistogramInterval.DAY)
                .field("recordDate");

        SearchResponse response = elasticsearchTemplate.getClient()
                .prepareSearch("log_record")
                .setQuery(QueryBuilders.matchAllQuery())
                .addAggregation(dateRangeAggregationBuilder != null
                        ? dateRangeAggregationBuilder.subAggregation(dateHistogramAggregation)
                        : dateHistogramAggregation)
                .execute().actionGet();

        List<InternalDateHistogram.Bucket> dateHistogramBuckets = null;

        if (dateRangeAggregationBuilder != null) {
            Map<String, Aggregation> results = response.getAggregations().asMap();
            Range rangeAggregation = (Range) results.get("date_range");
            InternalDateHistogram dateHistogram = rangeAggregation.getBuckets().get(0).getAggregations().get("date");
            if (dateHistogram != null) {
                dateHistogramBuckets = dateHistogram.getBuckets();
            } else {
                dateHistogramBuckets = Collections.emptyList();
            }
        } else {
            dateHistogramBuckets = ((InternalDateHistogram) response.getAggregations().getAsMap().get("date")).getBuckets();
        }
        return dateHistogramBuckets
                .stream()
                .map(bucket -> new LogRecordByDate(((DateTime) bucket.getKey()).toDate(), bucket.getDocCount()))
                .collect(Collectors.toList());
    }

    private DateRangeAggregationBuilder prepareDateRangeAggregation(ReportType reportType, Long fromDate, Long toDate) {
        DateRangeAggregationBuilder dateAggregation = AggregationBuilders.dateRange("date_range").field("recordDate");
        if (reportType == ReportType.CUSTOM) {
            dateAggregation.addRange(fromDate, toDate);
            return dateAggregation;
        } else if (reportType == ReportType.ALL) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        switch (reportType) {
            case DAILY:
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                break;
            case WEEKLY:
                calendar.add(Calendar.WEEK_OF_MONTH, -1);
                break;
            case TWO_WEEKS:
                calendar.add(Calendar.WEEK_OF_MONTH, -2);
                break;
            case MONTHLY:
                calendar.add(Calendar.MONTH, -1);
                break;
            case SIX_MONTHS:
                calendar.add(Calendar.MONTH, -6);
                break;
            case YEARLY:
                calendar.add(Calendar.YEAR, -1);
                break;
            case TWO_YEARS:
                calendar.add(Calendar.YEAR, -2);
                break;
            default:
                break;
        }
        dateAggregation.addUnboundedFrom(calendar.getTimeInMillis());
        return dateAggregation;
    }

}
