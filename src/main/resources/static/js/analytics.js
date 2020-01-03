var chart;

$(document).ready(function() {
    $('#reportTypeSelect').on('change', function() {
        if (this.value != 'CUSTOM') {
            initializeChart(this.value);
            $("#customReportType").hide();
        } else {
            $("#customReportType").show();
        }
    });

    $('#fromDate').change(function() {
        var fromDate = $('#fromDate').data('datepicker').getDate();
        var toDate = $('#toDate').data('datepicker').getDate();

        if (toDate) {
            if (fromDate > toDate) {
                alert(errorDateInput);
                $('#fromDate').val("").datepicker("update");
            } else {
                initializeChart('CUSTOM', fromDate.getTime(), toDate.getTime());
            }
        }
    });

    $('#toDate').change(function() {
        var fromDate = $('#fromDate').data('datepicker').getDate();
        var toDate = $('#toDate').data('datepicker').getDate();

        if (fromDate) {
            if (fromDate > toDate) {
                alert(errorDateInput);
                $('#toDate').val("").datepicker("update");
            } else {
                initializeChart('CUSTOM', fromDate.getTime(), toDate.getTime());
            }
        }
    });

    initializeChart('ALL');
});

function initializeChart(reportType, fromDate, toDate) {
    chart = new Highcharts.Chart({
        chart: {
            renderTo: 'container',
            type: 'line',
            reflow: true,
            scrollablePlotArea: {
                minWidth: 700
            },
            events: {
                load: requestData(reportType, fromDate, toDate)
            }
        },
        title: {
            text: analyticsEventTitle
        },
        subtitle: {
            text: analyticsEventSubtitle
        },
        xAxis: {
            tickInterval: getTickTime(reportType, fromDate, toDate),
            tickWidth: 0,
            type: 'datetime',
            gridLineWidth: 1,
            title: {
                text: analyticsEventDates
            },
            labels: {
                align: 'left',
                x: 3,
                y: -3
            }
        },
        yAxis: [{ // left y axis
            title: {
                text: analyticsLogRecords
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
}

function getTickTime(reportType, fromDate, toDate) {
    switch(reportType) {
      case "ALL":
      case "TWO_YEARS":
      case "YEARLY":
      case "SIX_MONTHS":
        return 2592000000;
      case "MONTHLY":
        return 604800000;
      case "TWO_WEEKS":
      case "WEEKLY":
          return 86400000;
      case "DAILY":
        return 3600000;
      default:
        return 604800000;
    }
}

function requestData(reportType, fromDate, toDate) {
    var requestParams = "?reportType=" + reportType;
    if (fromDate && toDate) {
        requestParams += "&fromDate=" + fromDate + "&toDate=" + toDate;
    }
    $.ajax({
        url: '/logRecord' + requestParams,
        type: "GET",
        success: function(response) {
            var data = [];

            $.each(response, function(index, logRecord) {
                data.push({x:new Date(logRecord.date), y: logRecord.logRecordsCount});
            });

            chart.addSeries({
                name: analyticsLogRecords,
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