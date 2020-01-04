var piechart;

$(document).ready(function() {
    $('#pieChartReportTypeSelect').on('change', function() {
        initializePieChart(this.value);
    });

    initializePieChart("EVENT_CONTEXT");
});

function initializePieChart(reportType) {
    piechart = new Highcharts.Chart({
        chart: {
            renderTo: 'piechart',
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            type: 'pie',
            events: {
                load: requestPieChartData(reportType)
            }
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b>: {point.percentage:.1f} %'
                }
            }
        }
    });
}

function requestPieChartData(reportType) {
    $.ajax({
        url: '/logRecord/pieChart?reportType=' + reportType,
        type: "GET",
        success: function(response) {
            var data = [];

            $.each(response, function(index, logRecord) {
                data.push({name: logRecord.value, y: logRecord.count});
            });

            piechart.addSeries({
                name: reportType,
                data: data
            });
        },
        cache: true
    });
}