/**
 * Created by Nicholas_Wang on 2016/12/26.
 */


var app = angular.module('governmentAdminApp', []);
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

app.controller('governmentAdminCtrl', function ($scope, $http, $interval, $timeout) {

    // 系统计时器
    // var timer = $interval(function () {
    //     $scope.datetime = new Date();
    // }, 1000);


    var basicCarInfoUrl = rootUrl + "admin/getBasicCarInfo";
    $http.get(basicCarInfoUrl).success(function (response) {
        $scope.basicCarInfos = response;
    }).error(function (data) {
        console.log(data);
    });

    $scope.showDetails = function (carNo) {

        map.clearOverlays();
        $scope.carNo = carNo;

        var getChargeDataUrl = rootUrl + "admin/getChargeDataDetail?carNo=" + carNo;
        $http.get(getChargeDataUrl).success(function (response) {
            $scope.chargeDatas = response;
        }).error(function (data) {
            console.log(data);
        });

        var getAllDrivingRouteUrl = rootUrl + "admin/getDrivingRouteWithTimeStamp?carNo=" + carNo;
        $http.get(getAllDrivingRouteUrl).success(function (response) {

            $scope.drivingRouteDatas = [];
            var segment = 1;
            for (var i in response) {
                var paths = response[i];
                for (var index in paths) {

                    (function () {
                        var infoMap = {};
                        infoMap.segment = "轨迹段" + segment;
                        infoMap.time = paths[index].time;
                        infoMap.lng = paths[index].lng;
                        infoMap.lat = paths[index].lat;
                        var point = new BMap.Point(paths[index].lng, paths[index].lat);
                        geoc.getLocation(point, function (rs) {
                            var addComp = rs.addressComponents;
                            var location = addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber;
                            infoMap.location = location;
                            $scope.$apply(function () {
                                $scope.drivingRouteDatas.push(infoMap);
                            });
                        });

                    })(index)
                }
                // segment = Number(segment) + Number(1);
                segment++;
            }


        }).error(function (data) {
            console.log(data);
        });

        var getTollGateWithTimeStampUrl = rootUrl + "admin/getTollGateWithTimeStamp?carNo=" + carNo;
        $http.get(getTollGateWithTimeStampUrl).success(function (response) {
            drawNavigation(response, carNo);
        }).error(function (data) {
            console.log(data);
        });

        var getDrivingRouteWithTimeStampUrl = rootUrl + "admin/getDrivingRouteWithTimeStamp?carNo=" + carNo;
        $http.get(getDrivingRouteWithTimeStampUrl).success(function (response) {
            drawTrack(response, carNo);
        }).error(function (data) {
            console.log(data);
        });

    };

    /**
     * 绘制导航路线
     */
    var drawNavigation = function (tollArray, carNo) {
        var points = [];
        var segment = 1;
        for (var index in tollArray) {
            (function () {
                var toll = tollArray[index];
                // 收费站起点和终点
                console.log('toll gps: ' + toll.enGPS);
                var tspoint = new BMap.Point(toll.enGPS.split(",")[0], toll.enGPS.split(",")[1]);
                var tepoint = new BMap.Point(toll.exGPS.split(",")[0], toll.exGPS.split(",")[1]);
                var tsTime = toll.enTime.split(" ")[1].substring(0,5);
                var teTime = toll.exTime.split(" ")[1].substring(0,5);
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

                var tolllabel1 = new BMap.Label(carNo + "收费段" + segment + "起" + "<br>" + toll.enStation + " " + tsTime, tollopts1);  // 创建文本标注对象
                var tolllabel2 = new BMap.Label(carNo + "收费段" + segment + "终" + "<br>" + toll.exStation + " " + teTime, tollopts2);  // 创建文本标注对象
                tolllabel1.setStyle({
                    color: "red",
                    fontSize: "10px",
                    height: "40px",
                    lineHeight: "15px",
                    fontFamily: "微软雅黑"
                });
                tolllabel2.setStyle({
                    color: "red",
                    fontSize: "10px",
                    height: "40px",
                    lineHeight: "15px",
                    fontFamily: "微软雅黑"
                });
                map.addOverlay(tolllabel1);
                map.addOverlay(tolllabel2);
                points.push(tspoint);
                points.push(tepoint);
                segment++;
            })(index)
        }
        var view = map.getViewport(eval(points));
        var mapZoom = view.zoom;
        var centerPoint = view.center;
        map.centerAndZoom(centerPoint, mapZoom);

    };

    /**
     * 绘制行使轨迹
     */
    var drawTrack = function (pathArray, carNo) {
        for (var index in pathArray) {
            (function () {
                var segment = Number(index) + Number(1);
                var pathInfo = pathArray[index];
                // var pathInfo = path.locArray;
                var lastIndex = pathInfo.length - 1;

                var startLng = pathInfo[0].lng;
                var endLng = pathInfo[lastIndex].lng;
                var startLat = pathInfo[0].lat;
                var endLat = pathInfo[lastIndex].lat;
                var startTime = pathInfo[0].time;
                var endTime = pathInfo[lastIndex].time;

                // 在起点和终点设置名称
                // 行驶轨迹起点和终点
                var point1 = new BMap.Point(startLng, startLat);
                var point2 = new BMap.Point(endLng, endLat);

                geoc.getLocation(point1, function (rs) {
                    var addComp = rs.addressComponents;
                    var location = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
                    startTime = startTime.split(" ")[1].substring(0,5);
                    var opts1 = {
                        position: point1,    // 指定文本标注所在的地理位置
                        offset: new BMap.Size(10, -10)    //设置文本偏移量
                    };
                    var label1 = new BMap.Label(carNo + "轨迹段" + segment + "起" + "<br>" + location + " " + startTime, opts1);  // 创建文本标注对象
                    label1.setStyle({
                        color: "blue",
                        fontSize: "10px",
                        height: "40px",
                        lineHeight: "15px",
                        fontFamily: "微软雅黑"
                    });
                    map.addOverlay(label1);
                });
                geoc.getLocation(point2, function (rs) {
                    var addComp = rs.addressComponents;
                    var location = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
                    endTime = endTime.split(" ")[1].substring(0,5);
                    var opts2 = {
                        position: point2,    // 指定文本标注所在的地理位置
                        offset: new BMap.Size(10, -10)    //设置文本偏移量
                    };
                    var label2 = new BMap.Label(carNo + "轨迹段" + segment + "终" + "<br>" + location + " " + endTime, opts2);  // 创建文本标注对象
                    label2.setStyle({
                        color: "blue",
                        fontSize: "10px",
                        height: "40px",
                        lineHeight: "15px",
                        fontFamily: "微软雅黑"
                    });
                    map.addOverlay(label2);
                });

                // 遍历每辆车的路线
                var arrPoint = []; //线路点
                for (var i = 0; i < pathInfo.length; i++) {
                    arrPoint.push(new BMap.Point(pathInfo[i].lng, pathInfo[i].lat));
                }
                var polyline1 = new BMap.Polyline(arrPoint, {
                    strokeColor: "blue",
                    strokeWeight: 5,
                    strokeOpacity: 0.5
                });   //创建折线

                var InfoOpts = {
                    width: 200,     // 信息窗口宽度
                    height: 100,     // 信息窗口高度
                    title: carNo, // 信息窗口标题
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
                        }
                        var infoWindow = new BMap.InfoWindow(text, InfoOpts);
                        map.openInfoWindow(infoWindow, e.point); //开启信息窗口
                    }

                });

                polyline1.addEventListener('mouseout', function (e) {
                    map.closeInfoWindow();
                });
                map.addOverlay(polyline1);   //增加折线
            })(index)

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