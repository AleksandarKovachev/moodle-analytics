var chart;

$(document).ready(function() {
    chart = Highcharts.chart('container', {
        chart: {
            scrollablePlotArea: {
                minWidth: 700
            },
            events: {
                load: requestData
            }
        },
        title: {
            text: 'Daily sessions at www.highcharts.com'
        },
        subtitle: {
            text: 'Source: Google Analytics'
        },
        xAxis: {
            tickInterval: 7 * 24 * 3600 * 1000, // one week
            tickWidth: 0,
            type: 'datetime',
            gridLineWidth: 1,
            labels: {
                align: 'left',
                x: 3,
                y: -3
            }
        },
        yAxis: [{ // left y axis
            title: {
                text: "Log records"
            },
            labels: {
                align: 'left',
                x: 3,
                y: 16,
                format: '{value:.,0f}'
            },
            showFirstLabel: false
        }, { // right y axis
            linkedTo: 0,
            gridLineWidth: 0,
            opposite: true,
            title: {
                text: null
            },
            labels: {
                align: 'right',
                x: -3,
                y: 16,
                format: '{value:.,0f}'
            },
            showFirstLabel: false
        }],
        tooltip: {
            shared: true,
            crosshairs: true
        },
        plotOptions: {
            series: {
                cursor: 'pointer',
                point: {
                    events: {
                        click: function (e) {
                            hs.htmlExpand(null, {
                                pageOrigin: {
                                    x: e.pageX || e.clientX,
                                    y: e.pageY || e.clientY
                                },
                                headingText: this.series.name,
                                maincontentText: Highcharts.dateFormat('%A, %b %e, %Y', this.x) + ':<br/> ' +
                                    this.y + ' sessions',
                                width: 200
                            });
                        }
                    }
                },
                marker: {
                    lineWidth: 1
                }
            }
        }
    });
});

function requestData() {
    $.ajax({
        url: '/logRecord',
        type: "GET",
        success: function(response) {
            var data = [];

            $.each(response, function(index, logRecord) {
                data.push({x:new Date(logRecord.date), y: logRecord.logRecordsCount});
            });

            chart.addSeries({
                name: "logRecords",
                lineWidth: 4,
                marker: {
                  radius: 4
                },
                data: data
            });
        },
        cache: true
    });
}