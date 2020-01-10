package com.moodle.analytics.repository.impl;

import com.moodle.analytics.constant.ColumnChartReportType;
import com.moodle.analytics.constant.DayOfWeek;
import com.moodle.analytics.constant.LineChartReportType;
import com.moodle.analytics.constant.PieChartReportType;
import com.moodle.analytics.repository.LogRecordCustomRepository;
import com.moodle.analytics.resource.LogRecordByDate;
import com.moodle.analytics.resource.LogRecordTerm;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram;
import org.elasticsearch.search.aggregations.bucket.range.DateRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.util.*;
import java.util.stream.Collectors;

public class LogRecordCustomRepositoryImpl implements LogRecordCustomRepository {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private MessageSource messageSource;

    @Override
    public List<LogRecordByDate> getLogRecordsForLineChart(LineChartReportType lineChartReportType, Long fromDate, Long toDate) {
        DateRangeAggregationBuilder dateRangeAggregationBuilder = prepareDateRangeAggregation(lineChartReportType, fromDate, toDate);

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

    @Override
    public List<LogRecordTerm> getLogRecordsForPieChart(PieChartReportType reportType) {
        SearchResponse response = elasticsearchTemplate.getClient()
                .prepareSearch("log_record")
                .setQuery(QueryBuilders.matchAllQuery())
                .addAggregation(AggregationBuilders.terms("report").field(reportType.getField() + ".keyword"))
                .execute().actionGet();
        List<StringTerms.Bucket> buckets = ((StringTerms) response.getAggregations().asMap().get("report")).getBuckets();
        return buckets
                .stream()
                .map(bucket -> new LogRecordTerm(bucket.getKeyAsString(), bucket.getDocCount()))
                .collect(Collectors.toList());
    }

    @Override
    public List<LogRecordTerm> getLogRecordsForColumnChart(ColumnChartReportType reportType) {
        SearchResponse response = elasticsearchTemplate.getClient()
                .prepareSearch("log_record")
                .setQuery(QueryBuilders.matchAllQuery())
                .addAggregation(
                        AggregationBuilders.histogram("report")
                                .interval(1)
                                .script(new Script(reportType.getScript()))
                                .order(BucketOrder.count(false)))
                .execute().actionGet();
        List<InternalHistogram.Bucket> buckets = ((InternalHistogram) response.getAggregations().asMap().get("report")).getBuckets();
        return buckets
                .stream()
                .map(bucket -> new LogRecordTerm(reportType == ColumnChartReportType.DAY_OF_WEEK ?
                        DayOfWeek.fromDayOfWeek(Double.valueOf(bucket.getKeyAsString()).intValue()).getDayName(messageSource) :
                        String.valueOf(Double.valueOf(bucket.getKeyAsString()).intValue()), bucket.getDocCount()))
                .collect(Collectors.toList());
    }

    private DateRangeAggregationBuilder prepareDateRangeAggregation(LineChartReportType lineChartReportType, Long fromDate, Long toDate) {
        DateRangeAggregationBuilder dateAggregation = AggregationBuilders.dateRange("date_range").field("recordDate");
        if (lineChartReportType == LineChartReportType.CUSTOM) {
            dateAggregation.addRange(fromDate, toDate);
            return dateAggregation;
        } else if (lineChartReportType == LineChartReportType.ALL) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        switch (lineChartReportType) {
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
