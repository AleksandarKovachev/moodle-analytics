var columnChart;

$(document).ready(function() {
    $('#columnChartReportTypeSelect').on('change', function() {
        initializeColumnChart(this.value);
    });

    initializeColumnChart("DAY_OF_WEEK");
});

function initializeColumnChart(reportType) {
    columnChart = new Highcharts.Chart({
        chart: {
            renderTo: 'columnChart',
            type: 'column',
            marginTop: 40,
            marginBottom: 80,
            plotBorderWidth: 1,
            events: {
                load: requestColumnChartData(reportType)
            }
        },

        title: {
            text: null
        },

        xAxis: {
            type: 'category'
        },

        accessibility: {
            announceNewData: {
                enabled: true
            }
        },

        plotOptions: {
            series: {
                borderWidth: 0,
                dataLabels: {
                    enabled: true,
                    format: '{point.y:.0f}'
                }
            }
        },

        legend: {
            enabled: false
        },

        tooltip: {
            headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
            pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.0f}</b><br/>'
        },
    });
}

function requestColumnChartData(reportType) {
    $.ajax({
        url: '/logRecord/columnChart?reportType=' + reportType,
        type: "GET",
        success: function(response) {
            var data = [];

            $.each(response, function(index, logRecord) {
                data.push({name: logRecord.value, y: logRecord.count});
            });

            columnChart.addSeries({
                name: reportType,
                data: data
            });
        },
        cache: true
    });
}