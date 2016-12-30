/**
 * Created by Nicholas_Wang on 2016/11/11.
 */

// var utils = angular.module('Utils', []);//deal with CORS
// utils.config(['$httpProvider', config]);
// function config($httpProvider) {
//     $httpProvider.defaults.withCredentials = true;
// }

var app = angular.module('auditApp', []);
var map = new BMap.Map("allmap");
map.enableScrollWheelZoom();
var point = new BMap.Point(113.264469, 23.134245);
map.centerAndZoom(point, 10);

var truckDic = {};
var trffic_color = ['#00FF00', '#FFFF00', '#FF8000', '#FF0000'];
var geoc = new BMap.Geocoder(); // 获取地理位置
app.controller('auditCtrl', function ($scope, $http) {
    // url = "http://localhost:8080/car/getDodgeTollCarList";
    var url = rootUrl + "getDodgeTollCarList";
    // url = 'http://localhost:8080/ServiceManagement/appcar/getDodgeTollCarList';
    $http.get(url).success(function (response) {
        // console.log(response);
        $scope.middleGet = response['middleGet'];
        $scope.shadeCard = response['shadeCard'];
        $scope.changeCard = response['changeCard'];
        $scope.falseLicence = response['falseLicence'];

    });
    var carSelected = [];
    $scope.isChecked = function ($event, carNo) {

        // console.log("target: " + $event.target);
        map.clearOverlays();
        var checkbox = $event.target;
        if (checkbox.checked){
            carSelected.push(carNo);
            // alert(carNo + " is selected")
        } else {
            var carIndex = carSelected.indexOf(carNo);
            carSelected.splice(carIndex,1);
            // alert(carNo + " is not selected")
        }
        // console.log(carSelected);

        var carNos = carSelected.join(",");

        // carSearchUrl = 'http://localhost:8080/searchcars?carNos=';
        // url = 'http://www.union01.com:9080/car/search?carNo='
        carSearchUrl = rootUrl + 'searchcars?carNos=';
        $http.get(carSearchUrl + carNos).success(function (response) {

            console.log(response);
            console.log(response.length);
            for (var i in response) {
                console.log(response[i]);
                var obj = response[i];
                if (obj['pathInfo'].length == 0) {
                    alert("对不起,没有对应的车辆信息");
                } else {
                    // 渲染数据
                    // console.log(response);
                    var tollInfo = obj['tollInfo'];
                    var pathInfo = obj['pathInfo'];
                    var lastIndex = pathInfo.length - 1;

                    // var startTime = pathInfo[0].time;
                    // var endTime = pathInfo[lastIndex].time;
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
                    map.addOverlay(polyline1);   //增加折线

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

                    var label1 = new BMap.Label(obj.carNo + "OBU轨迹·起", opts1);  // 创建文本标注对象
                    var label2 = new BMap.Label(obj.carNo + "OBU轨迹·终", opts2);  // 创建文本标注对象
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


                        // console.log('start location: ' + startLocation);
                        // alert(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber);
                    });
                    geoc.getLocation(point2, function (rs) {
                        var addComp = rs.addressComponents;
                        $scope.$apply(function () {
                            $scope.endLocation = addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber;
                        });

                        // console.log('end location: ' + endLocation);
                        // alert(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber);
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

                    var tolllabel1 = new BMap.Label(obj.carNo + "收费公路·起", tollopts1);  // 创建文本标注对象
                    var tolllabel2 = new BMap.Label(obj.carNo + "收费公路·终", tollopts2);  // 创建文本标注对象
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

                    // // 入口收费站信息
                    // $scope.startTollTime = tollInfo.tollArray[0].time;
                    // $scope.startTollName = tollInfo.tollName.startToll;
                    // $scope.startTollLng = tollInfo.tollArray[0].lng;
                    // $scope.startTollLat = tollInfo.tollArray[0].lat;
                    // // 出口收费站信息
                    // $scope.endTollTime = tollInfo.tollArray[1].time;
                    // $scope.endTollName = tollInfo.tollName.endToll;
                    // $scope.endTollLng = tollInfo.tollArray[1].lng;
                    // $scope.endTollLat = tollInfo.tollArray[1].lat;
                    // // 车辆出发地信息
                    // $scope.startTime = startTime;
                    // // $scope.startLocation =
                    // $scope.startLng = startLng;
                    // $scope.startLat = startLat;
                    // // 车辆终点地信息
                    //
                    // $scope.endTime = endTime;
                    // // $scope.endLocation =
                    // $scope.endLng = endLng
                    // $scope.endLat = endLat;
                }
            }

            console.log(carNos);



        }).error(function (data) {
            console.log(data);
        });

        // alert(carNo + "is element is checked");
        // console.log(map.getOverlays());
    }


});
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