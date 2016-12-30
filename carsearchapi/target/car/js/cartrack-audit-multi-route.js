/**
 * Created by Nicholas_Wang on 2016/12/7.
 */

var app = angular.module('carTrackAuditApp', []);
var map = new BMap.Map("allmap");
map.enableScrollWheelZoom();
var point = new BMap.Point(113.264469, 23.134245);
map.centerAndZoom(point, 10);
// 添加带有定位的导航控件
var navigationControl = new BMap.NavigationControl({
    // 靠左上角位置
    anchor: BMAP_ANCHOR_TOP_LEFT,
    // LARGE类型
    type: BMAP_NAVIGATION_CONTROL_LARGE,
    // 启用显示定位
    enableGeolocation: true
});
map.addControl(navigationControl);

var truckDic = {};
var trffic_color = ['#00FF00', '#FFFF00', '#FF8000', '#FF0000'];
var geoc = new BMap.Geocoder(); // 获取地理位置

app.controller('carTrackAuditCtrl', function ($scope, $http, $interval) {

    // 系统计时器
    var timer = $interval(function () {
        $scope.datetime = new Date();
    }, 1000);


    $scope.count = 0;
    $scope.searchCarNo = function () {
        // map.clearOverlays();
        $scope.count = 0;
        console.log('this: ' + $scope.count);
        var carNo = $scope.carNo;
        carNo = carNo.replace(/\s/g, "");
        console.log('carNo search:', carNo);
        $(function () {

            $scope.total = 0;
            var scroll = true;
            console.log('total first: ' + $scope.total);
            $('#dataRows tr').each(function () {
                console.log('text: ', $(this).find('font').text());
                if ($(this).find('font').text() == carNo) {
                    $scope.total = Number($scope.total) + Number(1);
                    if (scroll) {
                        $('#scrollBar').animate({
                            scrollTop: $(this).position().top + $("#scrollBar").scrollTop()
                        }, 1500);
                        scroll = false;
                    }
                    // return false;
                }
            });
            console.log('total: ' + $scope.total);
        });
        var carArray = $scope.carInfoArr;
        for (var i in carArray) {
            if (carArray[i].carNo == carNo) {
                searchCarInfo(carArray[i].exRecordNo);

                break;
            }
        }
        // showCarTrack(carNo);
    };

    $scope.searchNext = function () {
        $scope.total = Number($scope.total) - Number(1);
        $scope.count = Number($scope.count) + Number(1);
        console.log('next: ' + $scope.count);
        var carNo = $scope.carNo;
        carNo = carNo.replace(/\s/g, "");
        console.log('carNo search:', carNo);
        $(function () {
            var sc = 0;
            $('#dataRows tr').each(function () {
                console.log('text: ', $(this).find('font').text());
                if ($(this).find('font').text() == carNo) {
                    console.log('sc: ' + sc);
                    console.log($scope.count);
                    if (sc == $scope.count) {
                        $('#scrollBar').animate({
                            scrollTop: $(this).position().top + $("#scrollBar").scrollTop()
                        }, 1500);
                        return false;
                    }
                    sc++;
                }
            });
        });
        // var carArray = $scope.carInfoArr;
        // for (var i in carArray) {
        //     if (carArray[i].carNo == carNo) {
        //         searchCarInfo(carArray[i].exRecordNo);
        //         break;
        //     }
        // }
        // showCarTrack(carNo);
    };


    var url = rootUrl + "getDodgeTollAuditInfo";
    $http.get(url).success(function (response) {
        console.log('carInfoArr: ', response);
        $scope.carInfoArr = response;

    });

    var carSelected = [];
    $scope.isChecked = function ($event, carNo) {

        var checkbox = $event.target;
        if (checkbox.checked) {
            carSelected.push(carNo);
            // alert(carNo + " is selected")
        } else {
            var carIndex = carSelected.indexOf(carNo);
            carSelected.splice(carIndex, 1);
            // alert(carNo + " is not selected")
        }
        // console.log(carSelected);

        var carNos = carSelected.join(",");
        showCarsTrack(carNos);
    };


    var showCarsTrack = function (carNos) {
        map.clearOverlays();
        carSearchUrl = rootUrl + 'searchcars?carNos=';
        $http.get(carSearchUrl + carNos).success(function (response) {

            var points = [];
            console.log(response);
            // console.log(response.length);
            var cars = carNos.split(",");

            for (var i in response) {
                console.log(response[i]);
                var obj = response[i];
                if (obj['pathInfo'].length == 0) {
                    // alert("对不起,没有对应的车辆信息");
                } else {
                    // 渲染数据
                    // console.log(response);
                    var tollInfo = obj['tollInfo'];
                    var pathInfo = obj['pathInfo'];
                    var lastIndex = pathInfo.length - 1;

                    var startTime = pathInfo[0].time;
                    var endTime = pathInfo[lastIndex].time;
                    var startLng = pathInfo[0].lng;
                    var endLng = pathInfo[lastIndex].lng;
                    var startLat = pathInfo[0].lat;
                    var endLat = pathInfo[lastIndex].lat;


                    // 遍历每辆车的路线
                    var arrPoint = []; //线路点
                    for (var index = 0; index < pathInfo.length; index++) {
                        arrPoint.push(new BMap.Point(pathInfo[index].lng, pathInfo[index].lat));
                    }
                    var polyline1 = new BMap.Polyline(arrPoint, {strokeColor: "blue", strokeWeight: 5, strokeOpacity: 0.5});   //创建折线
                    polyline1.addEventListener('mouseover',function (e) {

                        for (var index in response) {
                            var car = response[index];
                            for (var i = 0; i < car.pathInfo.length; i++) {
                                var text = " ";
                                var lng1 = Number(car.pathInfo[i].lng);
                                var lng2 = Number(e.point.lng);
                                var lat1 = Number(car.pathInfo[i].lat);
                                var lat2 = Number(e.point.lat);
                                var compare1 = Math.abs(lng1-lng2);
                                var compare2 = Math.abs(lat1-lat2);
                                if ( compare1 <= 0.01 && compare2 <= 0.01) {
                                    text = car.pathInfo[i].time;
                                } else {
                                    continue;
                                    // text = e.point.lng + ' ' + e.point.lat + ' ' + compare1 + ' ' + compare2;
                                }
                                var InfoOpts = {
                                    width : 150,     // 信息窗口宽度
                                    height: 50,     // 信息窗口高度
                                    title : car.carNo , // 信息窗口标题
                                    enableMessage:true //设置允许信息窗发送短息
                                };
                                var infoWindow = new BMap.InfoWindow(text, InfoOpts);
                                // console.log('mouse over ' + e.point.lat);
                                map.openInfoWindow(infoWindow, e.point); //开启信息窗口
                            }
                        }



                    });

                    polyline1.addEventListener('mouseout', function (e) {
                        map.closeInfoWindow();
                    });
                    map.addOverlay(polyline1);   //增加折线
                    // addArrow(polyline1,20,Math.PI/7);

                    // 在 lastindex/2 位置车牌
                    var halfindex = parseInt(lastIndex / 2);
                    var carNoPoint = new BMap.Point(pathInfo[halfindex].lng, pathInfo[halfindex].lat);
                    var carNoOpts = {
                        position: carNoPoint,    // 指定文本标注所在的地理位置
                        offset: new BMap.Size(10, -10)    //设置文本偏移量
                    };
                    var carNoLabel = new BMap.Label(obj.carNo, carNoOpts);  // 创建文本标注对象
                    carNoLabel.setStyle({
                        color: "red",
                        fontSize: "10px",
                        height: "15px",
                        lineHeight: "15px",
                        fontFamily: "微软雅黑"
                    });
                    map.addOverlay(carNoLabel);

                    // 在起点和终点设置名称
                    // 行驶轨迹起点和终点
                    var point1 = new BMap.Point(startLng, startLat);
                    var point2 = new BMap.Point(endLng, endLat);
                    var opts1 = {
                        position: point1,    // 指定文本标注所在的地理位置
                        offset: new BMap.Size(10, -10)    //设置文本偏移量
                    };
                    var opts2 = {
                        position: point2,    // 指定文本标注所在的地理位置
                        offset: new BMap.Size(10, -10)    //设置文本偏移量
                    };

                    var label1 = new BMap.Label(obj.carNo + " OBU轨迹·起 " + startTime, opts1);  // 创建文本标注对象
                    var label2 = new BMap.Label(obj.carNo + " OBU轨迹·终 " + endTime, opts2);  // 创建文本标注对象
                    label1.setStyle({
                        color: "red",
                        fontSize: "10px",
                        height: "15px",
                        lineHeight: "15px",
                        fontFamily: "微软雅黑"
                    });
                    label2.setStyle({
                        color: "red",
                        fontSize: "10px",
                        height: "15px",
                        lineHeight: "15px",
                        fontFamily: "微软雅黑"
                    });
                    map.addOverlay(label1);
                    map.addOverlay(label2);

                    // // 获取起点终点地理位置
                    // geoc.getLocation(point1, function (rs) {
                    //     var addComp = rs.addressComponents;
                    //
                    //     $scope.$apply(function () {
                    //         $scope.startLocation = addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber;
                    //     });
                    //
                    //
                    //     // console.log('start location: ' + startLocation);
                    //     // alert(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber);
                    // });
                    // geoc.getLocation(point2, function (rs) {
                    //     var addComp = rs.addressComponents;
                    //     $scope.$apply(function () {
                    //         $scope.endLocation = addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber;
                    //     });
                    //
                    //     // console.log('end location: ' + endLocation);
                    //     // alert(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber);
                    // });

                    // 收费站起点和终点
                    var tsTime = tollInfo.tollArray[0].time;
                    var teTime = tollInfo.tollArray[1].time;
                    var tspoint = new BMap.Point(tollInfo.tollArray[0].lng, tollInfo.tollArray[0].lat);
                    var tepoint = new BMap.Point(tollInfo.tollArray[1].lng, tollInfo.tollArray[1].lat);
                    // 绘制导航路线
                    addTruckLushu(0, tspoint, tepoint, 3);

                    // 在收费站起点和终点设置名称
                    var tollopts1 = {
                        position: tspoint,    // 指定文本标注所在的地理位置
                        offset: new BMap.Size(10, -10)    //设置文本偏移量
                    };
                    var tollopts2 = {
                        position: tepoint,    // 指定文本标注所在的地理位置
                        offset: new BMap.Size(10, -10)    //设置文本偏移量
                    };

                    var tolllabel1 = new BMap.Label(obj.carNo + " 收费公路·起 " + tsTime, tollopts1);  // 创建文本标注对象
                    var tolllabel2 = new BMap.Label(obj.carNo + " 收费公路·终 " + teTime, tollopts2);  // 创建文本标注对象
                    tolllabel1.setStyle({
                        color: "red",
                        fontSize: "10px",
                        height: "15px",
                        lineHeight: "15px",
                        fontFamily: "微软雅黑"
                    });
                    tolllabel2.setStyle({
                        color: "red",
                        fontSize: "10px",
                        height: "15px",
                        lineHeight: "15px",
                        fontFamily: "微软雅黑"
                    });
                    map.addOverlay(tolllabel1);
                    map.addOverlay(tolllabel2);

                    points.push(point1);
                    points.push(point2);
                    points.push(tspoint);
                    points.push(tepoint);
                    // var points = [point1, point2,tspoint,tepoint];
                    // var view = map.getViewport(eval(points));
                    // var mapZoom = view.zoom;
                    // var centerPoint = view.center;
                    // map.centerAndZoom(centerPoint,mapZoom);
                }
            }

            var view = map.getViewport(eval(points));
            var mapZoom = view.zoom;
            var centerPoint = view.center;
            map.centerAndZoom(centerPoint,mapZoom);
            // console.log(carNos);



        }).error(function (data) {
            console.log(data);
        });
    };


    var showCarTrack = function (carNo) {
        map.clearOverlays();

        var carSearchUrl = rootUrl + 'search?carNo=';


        $http.get(carSearchUrl + carNo).success(function (response) {

            if (response['pathInfo'].length == 0) {
                alert("对不起,没有对应的车辆信息");
            } else {
                // 渲染数据
                console.log(response);
                var tollInfo = response['tollInfo'];
                var pathInfo = response['pathInfo'];
                var lastIndex = pathInfo.length - 1;

                var startLng = pathInfo[0].lng;
                var endLng = pathInfo[lastIndex].lng;
                var startLat = pathInfo[0].lat;
                var endLat = pathInfo[lastIndex].lat;


                // 遍历每辆车的路线
                var arrPoint = []; //线路点
                for (var index = 0; index < pathInfo.length; index++) {
                    arrPoint.push(new BMap.Point(pathInfo[index].lng, pathInfo[index].lat));
                }
                var polyline1 = new BMap.Polyline(arrPoint, {
                    strokeColor: "blue",
                    strokeWeight: 5,
                    strokeOpacity: 0.5
                });   //创建折线

                var InfoOpts = {
                    width: 200,     // 信息窗口宽度
                    height: 100,     // 信息窗口高度
                    title: "到达此点时间", // 信息窗口标题
                    enableMessage: true //设置允许信息窗发送短息
                    // message:"亲耐滴，晚上一起吃个饭吧？戳下面的链接看下地址喔~"
                };

                polyline1.addEventListener('mouseover', function (e) {

                    for (var i = 0; i < pathInfo.length; i++) {
                        var text = " ";
                        var lng1 = Number(pathInfo[i].lng);
                        var lng2 = Number(e.point.lng);
                        var lat1 = Number(pathInfo[i].lat);
                        var lat2 = Number(e.point.lat);
                        var compare1 = Math.abs(lng1 - lng2);
                        var compare2 = Math.abs(lat1 - lat2);
                        if (compare1 <= 0.01 && compare2 <= 0.01) {
                            text = pathInfo[i].time;
                        } else {
                            continue;
                            // text = e.point.lng + ' ' + e.point.lat + ' ' + compare1 + ' ' + compare2;
                        }
                        var infoWindow = new BMap.InfoWindow(text, InfoOpts);
                        // console.log('mouse over ' + e.point.lat);
                        map.openInfoWindow(infoWindow, e.point); //开启信息窗口
                    }

                });

                polyline1.addEventListener('mouseout', function (e) {
                    map.closeInfoWindow();
                });
                map.addOverlay(polyline1);   //增加折线
                // addArrow(polyline1,10,Math.PI/7);

                // 在 lastindex/2 位置车牌
                var halfindex = parseInt(lastIndex / 2);
                var carNoPoint = new BMap.Point(pathInfo[halfindex].lng, pathInfo[halfindex].lat);
                var carNoOpts = {
                    position: carNoPoint,    // 指定文本标注所在的地理位置
                    offset: new BMap.Size(10, -10)    //设置文本偏移量
                };
                var carNoLabel = new BMap.Label(carNo, carNoOpts);  // 创建文本标注对象
                carNoLabel.setStyle({
                    color: "red",
                    fontSize: "10px",
                    height: "15px",
                    lineHeight: "15px",
                    fontFamily: "微软雅黑"
                });
                map.addOverlay(carNoLabel);
                // map.panTo(carNoPoint);
                // 在起点和终点设置名称
                // 行驶轨迹起点和终点
                var point1 = new BMap.Point(startLng, startLat);
                var point2 = new BMap.Point(endLng, endLat);
                var opts1 = {
                    position: point1,    // 指定文本标注所在的地理位置
                    offset: new BMap.Size(10, -10)    //设置文本偏移量
                };
                var opts2 = {
                    position: point2,    // 指定文本标注所在的地理位置
                    offset: new BMap.Size(10, -10)    //设置文本偏移量
                };

                var label1 = new BMap.Label(carNo + "OBU轨迹·起", opts1);  // 创建文本标注对象
                var label2 = new BMap.Label(carNo + "OBU轨迹·终", opts2);  // 创建文本标注对象
                label1.setStyle({
                    color: "red",
                    fontSize: "10px",
                    height: "15px",
                    lineHeight: "15px",
                    fontFamily: "微软雅黑"
                });
                label2.setStyle({
                    color: "red",
                    fontSize: "10px",
                    height: "15px",
                    lineHeight: "15px",
                    fontFamily: "微软雅黑"
                });
                map.addOverlay(label1);
                map.addOverlay(label2);

                // 获取起点终点地理位置
                geoc.getLocation(point1, function (rs) {
                    var addComp = rs.addressComponents;

                    $scope.$apply(function () {
                        $scope.startLocation = addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber;
                    });

                });
                geoc.getLocation(point2, function (rs) {
                    var addComp = rs.addressComponents;
                    $scope.$apply(function () {
                        $scope.endLocation = addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber;
                    });
                });

                // 收费站起点和终点
                var tspoint = new BMap.Point(tollInfo.tollArray[0].lng, tollInfo.tollArray[0].lat);
                var tepoint = new BMap.Point(tollInfo.tollArray[1].lng, tollInfo.tollArray[1].lat);
                // 绘制导航路线
                addTruckLushu(0, tspoint, tepoint, 3);

                // 在收费站起点和终点设置名称
                var tollopts1 = {
                    position: tspoint,    // 指定文本标注所在的地理位置
                    offset: new BMap.Size(10, -10)    //设置文本偏移量
                };
                var tollopts2 = {
                    position: tepoint,    // 指定文本标注所在的地理位置
                    offset: new BMap.Size(10, -10)    //设置文本偏移量
                };

                var tolllabel1 = new BMap.Label(carNo + "收费公路·起", tollopts1);  // 创建文本标注对象
                var tolllabel2 = new BMap.Label(carNo + "收费公路·终", tollopts2);  // 创建文本标注对象
                tolllabel1.setStyle({
                    color: "red",
                    fontSize: "10px",
                    height: "15px",
                    lineHeight: "15px",
                    fontFamily: "微软雅黑"
                });
                tolllabel2.setStyle({
                    color: "red",
                    fontSize: "10px",
                    height: "15px",
                    lineHeight: "15px",
                    fontFamily: "微软雅黑"
                });
                map.addOverlay(tolllabel1);
                map.addOverlay(tolllabel2);

                var points = [point1, point2, tspoint, tepoint];
                var view = map.getViewport(eval(points));
                var mapZoom = view.zoom;
                var centerPoint = view.center;
                map.centerAndZoom(centerPoint, mapZoom);
            }

        }).error(function (data) {
            console.log(data);
        });
    };

    // function addArrow(polyline,length,angleValue){ //绘制箭头的函数
    //     var linePoint=polyline.getPath();//线的坐标串
    //     var arrowCount=linePoint.length;
    //     for(var i =1;i<arrowCount;i=i+10){ //在拐点处绘制箭头
    //         var pixelStart=map.pointToPixel(linePoint[i-1]);
    //         var pixelEnd=map.pointToPixel(linePoint[i]);
    //         var angle=angleValue;//箭头和主线的夹角
    //         var r=length; // r/Math.sin(angle)代表箭头长度
    //         var delta=0; //主线斜率，垂直时无斜率
    //         var param=0; //代码简洁考虑
    //         var pixelTemX,pixelTemY;//临时点坐标
    //         var pixelX,pixelY,pixelX1,pixelY1;//箭头两个点
    //         if(pixelEnd.x-pixelStart.x==0){ //斜率不存在是时
    //             // pixelTemX=pixelEnd.x;
    //             // if(pixelEnd.y>pixelStart.y)
    //             // {
    //             //     pixelTemY=pixelEnd.y-r;
    //             // }
    //             // else
    //             // {
    //             //     pixelTemY=pixelEnd.y+r;
    //             // }
    //             // //已知直角三角形两个点坐标及其中一个角，求另外一个点坐标算法
    //             // pixelX=pixelTemX-r*Math.tan(angle);
    //             // pixelX1=pixelTemX+r*Math.tan(angle);
    //             // pixelY=pixelY1=pixelTemY;
    //             continue;
    //         }
    //         else  //斜率存在时
    //         {
    //             delta=(pixelEnd.y-pixelStart.y)/(pixelEnd.x-pixelStart.x);
    //             param=Math.sqrt(delta*delta+1);
    //
    //             if((pixelEnd.x-pixelStart.x)<0) //第二、三象限
    //             {
    //                 pixelTemX=pixelEnd.x+ r/param;
    //                 pixelTemY=pixelEnd.y+delta*r/param;
    //             }
    //             else//第一、四象限
    //             {
    //                 pixelTemX=pixelEnd.x- r/param;
    //                 pixelTemY=pixelEnd.y-delta*r/param;
    //             }
    //             //已知直角三角形两个点坐标及其中一个角，求另外一个点坐标算法
    //             pixelX=pixelTemX+ Math.tan(angle)*r*delta/param;
    //             pixelY=pixelTemY-Math.tan(angle)*r/param;
    //
    //             pixelX1=pixelTemX- Math.tan(angle)*r*delta/param;
    //             pixelY1=pixelTemY+Math.tan(angle)*r/param;
    //         }
    //
    //         var pointArrow=map.pixelToPoint(new BMap.Pixel(pixelX,pixelY));
    //         var pointArrow1=map.pixelToPoint(new BMap.Pixel(pixelX1,pixelY1));
    //         var Arrow = new BMap.Polyline([
    //             pointArrow,
    //             linePoint[i],
    //             pointArrow1
    //         ], {strokeColor:"blue", strokeWeight:3, strokeOpacity:0.5});
    //         var arrow = map.addOverlay(Arrow);
    //
    //     }
    // }

    var searchCarInfo = function (exRecordNo) {
        var url = rootUrl + 'getDodgeTollCarInfo?carNo=';
        $http.get(url + exRecordNo).success(function (response) {
            if (response.length == 0) {
                alert('没有此车辆信息');
            }
            console.log('车辆信息: ' + response);
            $scope.carInfo = response;
            getCarLocation($scope.carInfo.carNo);

        }).error(function (data) {
            console.log(data);
        });
    };

    // 对比车辆路线
    $scope.carTrack = function (carNo) {

        showCarTrack(carNo);

    };


    var getCarLocation = function (carNo) {
        var carSearchUrl = rootUrl + 'search?carNo=';

        $http.get(carSearchUrl + carNo).success(function (response) {

            if (response['pathInfo'].length == 0) {
                alert("对不起,没有对应的车辆信息");
            } else {
                // 渲染数据
                var pathInfo = response['pathInfo'];
                var lastIndex = pathInfo.length - 1;

                var startLng = pathInfo[0].lng;
                var endLng = pathInfo[lastIndex].lng;
                var startLat = pathInfo[0].lat;
                var endLat = pathInfo[lastIndex].lat;

                // 行驶轨迹起点和终点
                var point1 = new BMap.Point(startLng, startLat);
                var point2 = new BMap.Point(endLng, endLat);

                // 获取起点终点地理位置
                geoc.getLocation(point1, function (rs) {
                    var addComp = rs.addressComponents;

                    $scope.$apply(function () {
                        $scope.startLocation = addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber;
                    });

                });
                geoc.getLocation(point2, function (rs) {
                    var addComp = rs.addressComponents;
                    $scope.$apply(function () {
                        $scope.endLocation = addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber;
                    });
                });

            }

        }).error(function (data) {
            console.log(data);
        });
    };

    // 展示车辆详细信息
    $scope.getCarInfo = function (exRecordNo) {

        searchCarInfo(exRecordNo);


    };

    // 详情表格中确认按钮功能实现
    $scope.confirmSuspicion = function (carNo, operate) {
        console.log("carNo: " + carNo);
        console.log("operation: " + operate);
        var url = rootUrl + 'dodgeTollCarConfirm' + '?carNo=' + carNo + '&operation=' + operate;
        $http.get(url).success(function (response) {

            var carArray = $scope.carInfoArr;
            for (var i in carArray) {
                // console.log('i: ' + i);
                if (carArray[i].exRecordNo == carNo) {
                    console.log('find carNO: ' + carArray[i].carNo);
                    carArray[i].action = operate;
                    // carArray[i].flag = 'S';
                    break;
                }
            }

            $scope.carInfo.action = operate;
        }).error(function (data) {
            console.log(data)
        });
    };

    // 修改车牌号码
    var statu = 0;
    $scope.changeCarNo = function ($event, carN, exRecordNo) {
        console.log(carN);
        var carNo = document.getElementById(exRecordNo);
        console.log('carNo:' + carNo.toString());
        var edit = $event.target;
        console.log('eidt:' + edit.toString());
        console.log('status:' + statu);
        if (statu == 0) {
            edit.innerHTML = '保存';
            // carNo.innerHTML = "";
            carNo.innerHTML = '<input type="text" class="item_input" style="width: 63px" value="' + carN + '">';
            statu = 1;
        } else {
            edit.innerHTML = '纠正车牌';
            var newCarNo = document.getElementsByClassName("item_input")[0].value;
            // 当需要实时更新的时候用
            if ($scope.carInfo != null && $scope.carInfo.carNo == carN) {
                $scope.carInfo.carNo = newCarNo;
            }
            var url = rootUrl + 'changeCarNo' + '?exRecordNo=' + exRecordNo + '&newCarNo=' + newCarNo;
            carNo.innerHTML = newCarNo;
            statu = 0;
            $http.get(url).success(function (response) {
                // var updateurl = rootUrl + "getDodgeTollAuditInfo";
                //
                // $http.get(updateurl).success(function (response) {
                //     // console.log(response);
                //     $scope.carInfoArr = response;
                //
                // });
                for (var index in $scope.carInfoArr) {
                    if ($scope.carInfoArr[index].carNo == carN) {
                        console.log('found this car: ' + carN);
                        $scope.carInfoArr[index].carNo = newCarNo;
                        console.log('change this car: ' + $scope.carInfoArr[index].carNo);
                        break;
                    }
                }
                console.log('修改成功');
            }).error(function (data) {
                console.log(data);
            });

        }

    };


});

// 绘制路线
function addTruckLushu(truck_id, start_p, end_p, traffic) {

    //收费站之间的路书
    var lushu;
    // 实例化一个驾车导航用来生成路线
    var routePolicy = [BMAP_DRIVING_POLICY_LEAST_TIME, BMAP_DRIVING_POLICY_LEAST_DISTANCE, BMAP_DRIVING_POLICY_AVOID_HIGHWAYS];
    var drv = new BMap.DrivingRoute('广州', {
        onSearchComplete: function (res) {
            if (drv.getStatus() == BMAP_STATUS_SUCCESS) {
                var arrPois = []; //线路点
                var plan = res.getPlan(0);
                for (var j = 0; j < plan.getNumRoutes(); j++) {
                    var route = plan.getRoute(j);
                    arrPois = arrPois.concat(route.getPath());
                }
                map.addOverlay(new BMap.Polyline(arrPois, {strokeColor: trffic_color[traffic]}));

                lushu = new BMapLib.LuShu(map, arrPois, {
                    defaultContent: "收费站线路",
                    icon: new BMap.Icon('truck_mini.png', new BMap.Size(52, 26), {anchor: new BMap.Size(27, 13)}),
                    speed: 4500
                });
                truckDic[truck_id] = {"lushu": lushu}
            }
        }
    });
    drv.setPolicy(routePolicy[1]);
    drv.search(start_p, end_p);
}

