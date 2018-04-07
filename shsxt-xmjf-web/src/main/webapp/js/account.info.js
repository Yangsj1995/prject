/**
 * Created by lp on 2018/3/8.
 */
var chart = null;
$(function () {
    loadAccountDataInfo();
    loadLastFiveMonthsInvestDataInfo();
});

function loadAccountDataInfo() {
    $.ajax({
        type:"post",
        url:ctx+"/account/queryAccountInfoByUserId",
        dataType:"json",
        success:function (data) {
            console.log(data);
            if(data.length>0){
                $('#pie_chart').highcharts({
                    chart: {
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false,
                        spacing : [100, 0 , 40, 0]
                    },
                    title: {
                        floating:true,
                        text: '总资产:'+data[0].y+"￥"
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
                                format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                                style: {
                                    color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                                }
                            },
                            point: {
                                events: {
                                    /*mouseOver: function(e) {  // 鼠标滑过时动态更新标题
                                        // 标题更新函数，API 地址：https://api.hcharts.cn/highcharts#Chart.setTitle
                                        chart.setTitle({
                                            text: e.target.name+ '\t'+ e.target.y + ' %'
                                        });
                                    }*/
                                    //,
                                    // click: function(e) { // 同样的可以在点击事件里处理
                                    //     chart.setTitle({
                                    //         text: e.point.name+ '\t'+ e.point.y + ' %'
                                    //     });
                                    // }
                                }
                            },
                        }
                    },
                    series: [{
                        type: 'pie',
                        innerSize: '80%',
                        name: '市场份额',
                        data:data
                    }]
                }, function(c) {
                    // 环形图圆心
                    var centerY = c.series[0].center[1],
                        titleHeight = parseInt(c.title.styles.fontSize);
                    c.setTitle({
                        y:centerY + titleHeight/2
                    });
                    chart = c;
                });
            }
        }
    })
}


function loadLastFiveMonthsInvestDataInfo() {
    $.ajax({
        type:"post",
        url:ctx+"/busItemInvest/countLastFiveMonthsInvestInfoByUserId",
        dataType:"json",
        success:function (data) {
            var months=data.months;
            var totals=data.totals;
            Highcharts.chart('line_chart', {
                chart: {
                    type: 'spline'
                },
                title: {
                    text: '投资用户投资收益折线图'
                },
                subtitle: {
                    text: '数据来源: 小马金服'
                },
                xAxis: {
                    categories: months
                },
                yAxis: {
                    title: {
                        text: '总金额'
                    },
                    labels: {
                        formatter: function () {
                            return this.value + '￥';
                        }
                    }
                },
                tooltip: {
                    crosshairs: true,
                    shared: true
                },
                plotOptions: {
                    spline: {
                        marker: {
                            radius: 4,
                            lineColor: '#666666',
                            lineWidth: 1
                        }
                    }
                },
                series: [
                    {
                        name: '投资',
                        marker: {
                            symbol: 'square'
                        },
                        data: totals
                    }]
            });
        }
    });


}

