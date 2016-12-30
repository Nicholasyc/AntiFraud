package com.wangyichao.service;

import com.wangyichao.dao.Config;
import com.wangyichao.dao.GovernmentAdminDao;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static java.lang.Math.PI;

/**
 * Created by Nicholas_Wang on 2016/12/26.
 */
@Service("governmentAdminService")
public class GovernmentAdminService {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource(name = "governmentAdminDao")
    private GovernmentAdminDao governmentAdminDao;

    /**
     * 获取车辆基本信息
     *
     * @return
     */
    public JSONArray getBasicCarInfo() {
        return governmentAdminDao.getBasicCarInfo();
    }

    /**
     * 根据车牌号获取高速收费流水详情
     *
     * @param carNo
     * @return
     */
    public JSONArray getChargeDataDetail(String carNo) {
        return governmentAdminDao.getChargeDataDetail(carNo);
    }

    /**
     * 获取车辆轨迹信息
     *
     * @param carNo
     * @return
     */
    public JSONArray getAllDrivingRoute(String carNo) {
        File file = new File(Config.getProperty("ADMIN_TRACK_URL"));
        String carPath = carNo + "_1_";
//        System.out.println("car file: " + carPath);
        JSONArray pathArray = new JSONArray();
        JSONObject path = new JSONObject();
        for (File f : file.listFiles()) {
            JSONArray locArray = new JSONArray();
            if (f.getName().startsWith(carPath)) {
                locArray = governmentAdminDao.getDrivingRouteDetail(f);
                pathArray.addAll(locArray);
            }
        }
        return pathArray;
    }

    /**
     * 获取车辆路过的收费站信息（按时间戳）文件获取
     * 保留
     *
     * @param carNo
     * @return
     */
    public JSONArray getTollGateWithTimeStampFromFile(String carNo) {
        File file = new File(Config.getProperty("ADMIN_TRACK_URL"));
        String carToll = carNo + "_0_";
        JSONArray tollArray = new JSONArray();
        JSONObject toll = new JSONObject();
        for (File f : file.listFiles()) {

            if (f.getName().startsWith(carToll)) {
                JSONObject tollInfo = new JSONObject();
                tollInfo = governmentAdminDao.getTollGateData(f);
                String timeStamp = f.getName().split("_")[2].split("\\.")[0];
//                System.out.println("timeStamp: " + timeStamp);
                toll.put("timeStamp", timeStamp);
                toll.put("tollInfo", tollInfo);
                tollArray.add(toll);
            }
        }
        return tollArray;
    }

    /**
     * 获取车辆路过的收费站信息（按时间戳）数据库获取
     *
     * @param carNo
     * @return
     */
    public JSONArray getTollGateWithTimeStamp(String carNo) {
        return governmentAdminDao.getTollGateData(carNo);
    }

    /**
     * 获取车辆轨迹信息（按时间戳）
     *
     * @param carNo
     * @return
     */
    public JSONArray getDrivingRouteWithTimeStamp(String carNo) {
//        GovernmentAdminDao governmentAdminDao = new GovernmentAdminDao();
        File file = new File(Config.getProperty("ADMIN_TRACK_URL"));
        String carPath = carNo + "_1_";
//        System.out.println("car file: " + carPath);
        JSONArray pathArrays = new JSONArray();
        for (File f : file.listFiles()) {
            JSONArray locArray = new JSONArray();
            if (f.getName().startsWith(carPath)) {
                locArray = governmentAdminDao.getDrivingRouteDetail(f);
                System.out.println(locArray.size());
                JSONArray paths = new JSONArray();
                for (int i = 0; i < locArray.size()-1; i++) {
                    if (separate(locArray.getJSONObject(i), locArray.getJSONObject(i+1))) {
//                        path.put("timeStamp", timeStamp);
//                        path.put("locArray", locArray.subList());
                        paths.add(locArray.getJSONObject(i));
                        pathArrays.add(paths);
                        paths.clear();
                    } else {
                        paths.add(locArray.getJSONObject(i));
                    }
                }
                pathArrays.add(paths);
            }
        }
        return pathArrays;
    }


    private boolean separate(JSONObject obj1, JSONObject obj2) {
        try {
            long time1 = sdf.parse(obj1.get("time").toString()).getTime();
            long time2 = sdf.parse(obj2.get("time").toString()).getTime();
            Double between = Double.valueOf(Math.abs(time1 - time2) / Double.valueOf(1000 * 60));
            if (time1 == time2) {
                return false;
            }
            if (between > 30) {
                return true;
            }
            double lng1 = Double.parseDouble(obj1.get("lng").toString());
            double lat1 = Double.parseDouble(obj1.get("lat").toString());
            double lng2 = Double.parseDouble(obj2.get("lng").toString());
            double lat2 = Double.parseDouble(obj2.get("lat").toString());
            double distance = calcDistance(lng1, lat1, lng2, lat2) / 1000; // 公里
            Double time = Double.valueOf(between / 60); // 小时
            double speed = distance / time;
            if (speed > 500) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    private double calcDistance(double Longitude1, double Latitude1, double Longitude2, double Latitude2) {
        double dRadLat1 = Rad(Latitude1);
        double dRadLat2 = Rad(Latitude2);
        double a = dRadLat1 - dRadLat2;
        double b = Rad(Longitude1) - Rad(Longitude2);

        double dRad = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(dRadLat1) * Math.cos(dRadLat2) * Math.pow(Math.sin(b / 2), 2)));
//        double dDistance = dRad * EARTH_RADIUS;
        double dDistance = dRad * 6378.137;
        dDistance = round(dDistance * 10000) / 10000;
        return Math.abs(dDistance * 1000);
    }

    private double Rad(double dDegree) {
        return dDegree * PI / 180.0;
    }

    private double round(double d) {
        return Math.floor(d + 0.5);
    }

//    public static void main(String[] args) {
//        Double Longitude1 = 115.0422;
//        Double Latitude1 = 22.8058;
////        String time1 = "2016-12-13 20:41:18";
//        Double Longitude2 = 115.0476;
//        Double Latitude2 = 22.8048;
////        String time2 = "2016-12-13 20:41:27";
////        JSONObject obj1 = new JSONObject();
////        JSONObject obj2 = new JSONObject();
////        obj1.put("time", time1);
////        obj1.put("lng", Longitude1);
////        obj1.put("lat", Latitude1);
////        obj2.put("time", time2);
////        obj2.put("lng", Longitude2);
////        obj2.put("lat", Latitude2);
//
//        double num = calcDistance(Longitude1, Latitude1, Longitude2, Latitude2);
//
//        System.out.println(num);
////        JSONArray result = getDrivingRouteWithTimeStamp("粤D08911");
////        System.out.println(result.size());
//    }

}
