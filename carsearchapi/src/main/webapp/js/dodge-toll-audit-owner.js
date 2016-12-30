/**
 * Created by Nicholas_Wang on 2016/11/18.
 */

var app = angular.module('statisticApp', []);
app.controller('statisticCtrl', function ($scope, $http, $interval) {

    // 系统时间控制
    var timer = $interval(function () {
        $scope.datetime = new Date();
    }, 1000);


    // 历史走势展示区域
    var year = 2016;

    // 获取挽回逃费损失金额/报警损失金额按月变化数据
    var lossUrl = rootUrl + 'statistic/getLossData?year=' + year;
    $http.get(lossUrl).success(function (response) {
        // console.log(response);
        // 路径配置
        require.config({
            paths: {
                echarts: 'http://echarts.baidu.com/build/dist'
            }
        });
        var saveMoney = response.saveMoney;
        var lostMoney = response.lostMoney;
        require(
            [
                'echarts',
                'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
            ],
            function (ec) {
                // 基于准备好的dom，初始化echarts图表
                var myChart = ec.init(document.getElementById('echarts1'));
                var option = {
                    tooltip: {
                        show: true
                    },
                    legend: {
                        data: ['挽回逃费损失金额', '报警损失金额']
                    },
                    xAxis: [
                        {
                            type: 'category',
                            data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value'
                        }
                    ],
                    series: [
                        {
                            "name": "挽回逃费损失金额",
                            "type": "bar",
                            "data": [saveMoney[0], saveMoney[1], saveMoney[2], saveMoney[3], saveMoney[4], saveMoney[5], saveMoney[6], saveMoney[7], saveMoney[8], saveMoney[9], saveMoney[10], saveMoney[11]],
                            itemStyle: {normal: {color: 'rgba(0,137,255,1)'}}
                        },
                        {
                            "name": "报警损失金额",
                            "type": "bar",
                            "data": [lostMoney[0], lostMoney[1], lostMoney[2], lostMoney[3], lostMoney[4], lostMoney[5], lostMoney[6], lostMoney[7], lostMoney[8], lostMoney[9], lostMoney[10], lostMoney[11]],
                            itemStyle: {normal: {color: 'rgba(229,0,255,1)'}}
                        }
                    ]
                };

                // 为echarts对象加载数据
                myChart.setOption(option);
            }
        );

    }).error(function (data) {
        console.log(data);
    });


    // 获取公司确认逃费次数/报警逃费次数按月变化数据
    var dodgeTollTimesUrl = rootUrl + 'statistic/getDodgeTollTimesData?year=' + year;
    $http.get(dodgeTollTimesUrl).success(function (response) {
        console.log(response);
        // 路径配置
        require.config({
            paths: {
                echarts: 'http://echarts.baidu.com/build/dist'
            }
        });
        var confirmTimes = response.confirmTimes;
        var alarmTimes = response.alarmTimes;
        require(
            [
                'echarts',
                'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
            ],
            function (ec) {
                // 基于准备好的dom，初始化echarts图表
                var myChart = ec.init(document.getElementById('echarts2'));
                var option = {
                    tooltip: {
                        show: true
                    },
                    legend: {
                        data: ['确认逃费次数', '报警逃费次数']
                    },
                    xAxis: [
                        {
                            type: 'category',
                            data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value'
                        }
                    ],
                    series: [
                        {
                            "name": "确认逃费次数",
                            "type": "bar",
                            "data": [confirmTimes[0], confirmTimes[1], confirmTimes[2], confirmTimes[3], confirmTimes[4], confirmTimes[5], confirmTimes[6], confirmTimes[7], confirmTimes[8], confirmTimes[9], confirmTimes[10], confirmTimes[11]],
                            itemStyle: {normal: {color: 'rgba(0,137,255,1)'}}
                        },
                        {
                            "name": "报警逃费次数",
                            "type": "bar",
                            "data": [alarmTimes[0], alarmTimes[1], alarmTimes[2], alarmTimes[3], alarmTimes[4], alarmTimes[5], alarmTimes[6], alarmTimes[7], alarmTimes[8], alarmTimes[9], alarmTimes[10], alarmTimes[11]],
                            itemStyle: {normal: {color: 'rgba(229,0,255,1)'}}
                        }
                    ]
                };

                // 为echarts对象加载数据
                myChart.setOption(option);
            }
        );
    }).error(function (data) {
        console.log(data);
    });

    // 获取公司确认冲卡等暴力逃费数量
    var violenceAmountUrl = rootUrl + 'statistic/getViolenceAmountData?year=' + year;
    $http.get(violenceAmountUrl).success(function (response) {
        console.log(response);
        // 路径配置
        require.config({
            paths: {
                echarts: 'http://echarts.baidu.com/build/dist'
            }
        });
        var violenceAmount = response.violenceAmount;
        require(
            [
                'echarts',
                'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
            ],
            function (ec) {
                // 基于准备好的dom，初始化echarts图表
                var myChart = ec.init(document.getElementById('echarts3'));
                var option = {
                    tooltip: {
                        show: true
                    },
                    legend: {
                        data: ['公司确认冲卡等暴力逃费数量']
                    },
                    xAxis: [
                        {
                            type: 'category',
                            data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value'
                        }
                    ],
                    series: [
                        {
                            "name": "公司确认冲卡等暴力逃费数量",
                            "type": "bar",
                            "data": [violenceAmount[0], violenceAmount[1], violenceAmount[2], violenceAmount[3], violenceAmount[4], violenceAmount[5], violenceAmount[6], violenceAmount[7], violenceAmount[8], violenceAmount[9], violenceAmount[10], violenceAmount[11]],
                            itemStyle: {normal: {color: 'rgba(0,137,255,1)'}}
                        }
                    ]
                };

                // 为echarts对象加载数据
                myChart.setOption(option);
            }
        );
    }).error(function (data) {
        console.log(data);
    });

    // 获取公司倒卡换卡遮挡卡逃费行为数量
    var changCardUrl = rootUrl + 'statistic/getChangeCardData?year=' + year;
    $http.get(changCardUrl).success(function (response) {
        console.log(response);
        // 路径配置
        require.config({
            paths: {
                echarts: 'http://echarts.baidu.com/build/dist'
            }
        });
        var changeCard = response.changeCard;
        require(
            [
                'echarts',
                'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
            ],
            function (ec) {
                // 基于准备好的dom，初始化echarts图表
                var myChart = ec.init(document.getElementById('echarts4'));
                var option = {
                    tooltip: {
                        show: true
                    },
                    legend: {
                        data: ['公司倒卡换卡遮挡卡逃费行为数量']
                    },
                    xAxis: [
                        {
                            type: 'category',
                            data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value'
                        }
                    ],
                    series: [
                        {
                            "name": "公司倒卡换卡遮挡卡逃费行为数量",
                            "type": "bar",
                            "data": [changeCard[0], changeCard[1], changeCard[2], changeCard[3], changeCard[4], changeCard[5], changeCard[6], changeCard[7], changeCard[8], changeCard[9], changeCard[10], changeCard[11]],
                            itemStyle: {normal: {color: 'rgba(0,137,255,1)'}}
                        }
                    ]
                };

                // 为echarts对象加载数据
                myChart.setOption(option);
            }
        );
    }).error(function (data) {
        console.log(data);
    });

    // 获取公司货车计重造假逃费行为数量
    var weightFakeUrl = rootUrl + 'statistic/getWeightFakeData?year=' + year;
    $http.get(weightFakeUrl).success(function (response) {
        console.log(response);
        // 路径配置
        require.config({
            paths: {
                echarts: 'http://echarts.baidu.com/build/dist'
            }
        });
        var weightFake = response.weightFake;
        require(
            [
                'echarts',
                'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
            ],
            function (ec) {
                // 基于准备好的dom，初始化echarts图表
                var myChart = ec.init(document.getElementById('echarts5'));
                var option = {
                    tooltip: {
                        show: true
                    },
                    legend: {
                        data: ['货车计重造假逃费行为数量']
                    },
                    xAxis: [
                        {
                            type: 'category',
                            data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value'
                        }
                    ],
                    series: [
                        {
                            "name": "货车计重造假逃费行为数量",
                            "type": "bar",
                            "data": [weightFake[0], weightFake[1], weightFake[2], weightFake[3], weightFake[4], weightFake[5], weightFake[6], weightFake[7], weightFake[8], weightFake[9], weightFake[10], weightFake[11]],
                            itemStyle: {normal: {color: 'rgba(0,137,255,1)'}}
                        }
                    ]
                };

                // 为echarts对象加载数据
                myChart.setOption(option);
            }
        );
    }).error(function (data) {
        console.log(data);
    });

    // 获取公司假冒优惠车辆逃费行为数量
    var fakeDiscountUrl = rootUrl + 'statistic/getFakeDiscountData?year=' + year;
    $http.get(fakeDiscountUrl).success(function (response) {
        console.log(response);
        // 路径配置
        require.config({
            paths: {
                echarts: 'http://echarts.baidu.com/build/dist'
            }
        });
        var fakeDiscount = response.fakeDiscount;
        require(
            [
                'echarts',
                'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
            ],
            function (ec) {
                // 基于准备好的dom，初始化echarts图表
                var myChart = ec.init(document.getElementById('echarts6'));
                var option = {
                    tooltip: {
                        show: true
                    },
                    legend: {
                        data: ['假冒优惠车辆逃费行为数量']
                    },
                    xAxis: [
                        {
                            type: 'category',
                            data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value'
                        }
                    ],
                    series: [
                        {
                            "name": "假冒优惠车辆逃费行为数量",
                            "type": "bar",
                            "data": [fakeDiscount[0], fakeDiscount[1], fakeDiscount[2], fakeDiscount[3], fakeDiscount[4], fakeDiscount[5], fakeDiscount[6], fakeDiscount[7], fakeDiscount[8], fakeDiscount[9], fakeDiscount[10], fakeDiscount[11]],
                            itemStyle: {normal: {color: 'rgba(0,137,255,1)'}}
                        }
                    ]
                };

                // 为echarts对象加载数据
                myChart.setOption(option);
            }
        );
    }).error(function (data) {
        console.log(data);
    });

    // 当前状况展示区域
    // 获取近一个月挽回逃费损失
    var saveUrl = rootUrl + 'statistic/getThisMonthSaveMoney';
    $http.get(saveUrl).success(function (response) {
        $scope.saveMoney = response;
    }).error(function (data) {
        console.log(data);
    });
    // 获取近一个月打击逃费次数
    var findTollUrl = rootUrl + 'statistic/getThisMonthFindDodgeTimes';
    $http.get(findTollUrl).success(function (response) {
        $scope.findTollTimes = response;
    }).error(function (data) {
        console.log(data);
    });
    // 获取近一个月打逃平均每笔换回损失
    var averageUrl = rootUrl + 'statistic/getThisMonthAverageSaveMoney';
    $http.get(averageUrl).success(function (response) {
        $scope.averageMoney = response;
    }).error(function (data) {
        console.log(data);
    });


    // 获取分收费站考核数据
    var getGateTableData = function (scope) {
        var tableUrl = rootUrl + "statistic/getTableData?scope=" + scope;
        $http.get(tableUrl).success(function (response) {
            // $scope.rows = response.table;
            // console.log('gg rows: ' + $scope.rows[0].station);
            // $scope.rows = response;
            // console.log('response: ' + response);
            dataTableShow(response);
        }).error(function (data) {
            console.log(data);
        });

    };

    var dataTableShow = function (dataSet) {
        //百分率排序
        jQuery.fn.dataTableExt.oSort['number-fate-asc'] = function (s1, s2) {
            // var m = s1.match(/>(\S*)</)[1];
            // var n = s2.match(/>(\S*)</)[1];
            // console.log('m and n: ' + m,n);
            // console.log('match: ' + m);
            if (s1.length>s2.length) {
                return 1;
            } else if (s1.length<s2.length) {
                return -1;
            } else {
                var x = s1.replace('%','');
                var y = s2.replace('%','');
                // console.log(s1);
                return ((parseFloat(x) > parseFloat(y)) ? 1 : ((parseFloat(x) < parseFloat(y)) ?  -1 : 0));
            }
        };

        jQuery.fn.dataTableExt.oSort['number-fate-desc'] = function (s1, s2) {
            // var m = s1.match(/>(\S*)</)[1];
            // var n = s2.match(/>(\S*)</)[1];
            if (s1.length>s2.length) {
                return -1;
            } else if (s1.length<s2.length) {
                return 1;
            } else {
                var x = s1.replace('%','');
                var y = s2.replace('%','');
                return ((parseFloat(x) > parseFloat(y)) ? -1 : ((parseFloat(x) < parseFloat(y)) ?  1 : 0));
            }

        };
        //中文数字排序
        jQuery.fn.dataTableExt.oSort['chinesenum-string-asc'] = function (s1, s2) {
            // console.log('x,y: ' +x+' '+y);
            // var reg = /[\u4E00-\u9FA5]/g;
            // var m = s1.replace(reg,'');
            // var n = s2.replace(reg,'');
            // console.log(m,n);
            return ((parseFloat(s1) > parseFloat(s2)) ? 1 : ((parseFloat(s1) < parseFloat(s2)) ?  -1 : 0));
        };
        jQuery.fn.dataTableExt.oSort['chinesenum-string-desc'] = function (s1, s2) {
            // var reg = /[\u4E00-\u9FA5]/g;
            // var m = s1.replace(reg,'');
            // var n = s2.replace(reg,'');
            // console.log(m,n);
            return ((parseFloat(s1) > parseFloat(s2)) ? -1 : ((parseFloat(s1) < parseFloat(s2)) ?  1 : 0));
        };
        // //中文排序
        // jQuery.fn.dataTableExt.oSort['chinese-string-asc'] = function (x, y) {
        //     return ((x>y)?1:(x<y)?-1:0);
        // };
        // jQuery.fn.dataTableExt.oSort['chinese-string-desc'] = function (x, y) {
        //     return ((x>y)?-1:(x<y)?1:0);
        // };

        $(document).ready(function () {
            // console.log('haha: ' + $scope.rows[0].station);
            // var dataSet = $scope.rows;

            $('#myTable').DataTable({
                destroy: true,
                "data": dataSet,
                "columns": [
                    {"data": 'station'},
                    {"data": 'action1'},
                    {"data": 'saveMoney'},
                    {"data": 'rate'}
                ],
                "aoColumns": [
                    null, //中文排序列
                    {"sType": "chinesenum-string"},
                    {"sType": "chinesenum-string"},
                    {"sType": "number-fate"} //百分率排序
                ],
                "aoColumnDefs": [ { "bSortable": false, "aTargets": [ 0 ] }], // 设置第一列不可排序
                language: {
                    "sProcessing": "处理中...",
                    "sLengthMenu": "显示 _MENU_ 项结果",
                    "sZeroRecords": "没有匹配结果",
                    "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
                    "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
                    "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
                    "sInfoPostFix": "",
                    "sSearch": "搜索:",
                    "sUrl": "",
                    "sEmptyTable": "表中数据为空",
                    "sLoadingRecords": "载入中...",
                    "sInfoThousands": ",",
                    "oPaginate": {
                        "sFirst": "首页",
                        "sPrevious": "上页",
                        "sNext": "下页",
                        "sLast": "末页"
                    },
                    "oAria": {
                        "sSortAscending": ": 以升序排列此列",
                        "sSortDescending": ": 以降序排列此列"
                    }
                }
            });
        });
    };

    // 获取分收费站考核数据
    var getOperatorsTableData = function (scope) {
        var tableUrl = rootUrl + "statistic/getOperatorsTableDataArray?scope=" + scope;
        $http.get(tableUrl).success(function (response) {
            // $scope.rows = response.table;
            // console.log('gg rows: ' + $scope.rows[0].station);
            // $scope.rows = response;
            // console.log('response: ' + response);
            operatorsTableShow(response);
        }).error(function (data) {
            console.log(data);
        });

    };

    var operatorsTableShow = function (dataSet) {
        //百分率排序
        jQuery.fn.dataTableExt.oSort['number-fate-asc'] = function (s1, s2) {
            // var m = s1.match(/>(\S*)</)[1];
            // var n = s2.match(/>(\S*)</)[1];
            // console.log('m and n: ' + m,n);
            // console.log('match: ' + m);
            if (s1.length>s2.length) {
                return 1;
            } else if (s1.length<s2.length) {
                return -1;
            } else {
                var x = s1.replace('%','');
                var y = s2.replace('%','');
                // console.log(s1);
                return ((parseFloat(x) > parseFloat(y)) ? 1 : ((parseFloat(x) < parseFloat(y)) ?  -1 : 0));
            }
        };

        jQuery.fn.dataTableExt.oSort['number-fate-desc'] = function (s1, s2) {
            // var m = s1.match(/>(\S*)</)[1];
            // var n = s2.match(/>(\S*)</)[1];
            if (s1.length>s2.length) {
                return -1;
            } else if (s1.length<s2.length) {
                return 1;
            } else {
                var x = s1.replace('%','');
                var y = s2.replace('%','');
                return ((parseFloat(x) > parseFloat(y)) ? -1 : ((parseFloat(x) < parseFloat(y)) ?  1 : 0));
            }

        };
        //中文数字排序
        jQuery.fn.dataTableExt.oSort['chinesenum-string-asc'] = function (s1, s2) {
            // console.log('x,y: ' +x+' '+y);
            // var reg = /[\u4E00-\u9FA5]/g;
            // var m = s1.replace(reg,'');
            // var n = s2.replace(reg,'');
            // console.log(m,n);
            return ((parseFloat(s1) > parseFloat(s2)) ? 1 : ((parseFloat(s1) < parseFloat(s2)) ?  -1 : 0));
        };
        jQuery.fn.dataTableExt.oSort['chinesenum-string-desc'] = function (s1, s2) {
            // var reg = /[\u4E00-\u9FA5]/g;
            // var m = s1.replace(reg,'');
            // var n = s2.replace(reg,'');
            // console.log(m,n);
            return ((parseFloat(s1) > parseFloat(s2)) ? -1 : ((parseFloat(s1) < parseFloat(s2)) ?  1 : 0));
        };
        // //中文排序
        // jQuery.fn.dataTableExt.oSort['chinese-string-asc'] = function (x, y) {
        //     return ((x>y)?1:(x<y)?-1:0);
        // };
        // jQuery.fn.dataTableExt.oSort['chinese-string-desc'] = function (x, y) {
        //     return ((x>y)?-1:(x<y)?1:0);
        // };

        $(document).ready(function () {
            // console.log('haha: ' + $scope.rows[0].station);
            // var dataSet = $scope.rows;

            $('#operatorsTable').DataTable({
                destroy: true,
                "data": dataSet,
                "columns": [
                    {"data": 'operatorid'},
                    {"data": 'station'},
                    {"data": 'action1'},
                    {"data": 'saveMoney'},
                    {"data": 'rate'}
                ],
                "aoColumns": [
                    {"sType": "chinesenum-string"}, //中文排序列
                    null,
                    {"sType": "chinesenum-string"},
                    {"sType": "chinesenum-string"},
                    {"sType": "number-fate"} //百分率排序
                ],
                "aoColumnDefs": [ { "bSortable": false, "aTargets": [ 1 ] }], // 设置第一列不可排序
                language: {
                    "sProcessing": "处理中...",
                    "sLengthMenu": "显示 _MENU_ 项结果",
                    "sZeroRecords": "没有匹配结果",
                    "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
                    "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
                    "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
                    "sInfoPostFix": "",
                    "sSearch": "搜索:",
                    "sUrl": "",
                    "sEmptyTable": "表中数据为空",
                    "sLoadingRecords": "载入中...",
                    "sInfoThousands": ",",
                    "oPaginate": {
                        "sFirst": "首页",
                        "sPrevious": "上页",
                        "sNext": "下页",
                        "sLast": "末页"
                    },
                    "oAria": {
                        "sSortAscending": ": 以升序排列此列",
                        "sSortDescending": ": 以降序排列此列"
                    }
                }
            });
        });
    };

    // 初始化表格数据
    getGateTableData(12);
    getOperatorsTableData(12);

    // 设置及初始化下拉列表
    $scope.options = {
        '最近3个月': 3,
        '最近半年': 6,
        '最近一年': 12
    };
    $scope.selectedOption = 12;
    $scope.changeScope = function (selectedOption) {
        // 根据传入的时间范围更新表格数据
        // $scope.rows = "";
        // var table = $('#example').DataTable();
        // table.fnClearTable(table);
        getGateTableData(selectedOption);
    };

    // 设置及初始化下拉列表
    $scope.operatorsOptions = {
        '最近半年': 6,
        '最近一年': 12
    };
    $scope.operatorsSelectedOption = 12;
    $scope.operatorsChangeScope = function (selectedOption) {
        // 根据传入的时间范围更新表格数据
        // $scope.rows = "";
        // var table = $('#example').DataTable();
        // table.fnClearTable(table);
        getOperatorsTableData(selectedOption);
    };



});



