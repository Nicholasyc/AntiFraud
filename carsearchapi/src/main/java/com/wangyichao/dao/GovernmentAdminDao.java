package com.wangyichao.dao;

import com.wangyichao.utils.DataBaseTool;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Nicholas_Wang on 2016/12/26.
 */
@Repository("governmentAdminDao")
public class GovernmentAdminDao {

    private DataBaseTool dataBaseTool = DataBaseTool.getInstance();

    public JSONArray getBasicCarInfo(){
        JSONArray carArray = new JSONArray();

        Connection conn = dataBaseTool.getConn();
        String sql = "SELECT DISTINCT exPlateNumber, exVehicleFlag FROM carsearch where fordemo='YGJ'";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                JSONObject carInfo = new JSONObject();
                carInfo.put("carNo", rs.getString("exPlateNumber"));
                carInfo.put("exVehicleFlag", rs.getString("exVehicleFlag"));
                carArray.add(carInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBaseTool.close(rs, ps, conn);
        }
        return carArray;
    }

    public JSONArray getChargeDataDetail(String carNo) {
        JSONArray chargeDataArray = new JSONArray();
        Connection conn = dataBaseTool.getConn();
        String sql = "SELECT enRoadName, enStationName, exRoadName, exStationName, enTime, exTime, chargeMiles, TIMEMINUTES, AVESPEEDKMH, ROADSPASSED from carsearch where exPlateNumber=? and fordemo = 'YGJ' order by (enTime) asc";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, carNo);
            rs = ps.executeQuery();
            int index = 1;
            while (rs.next()) {
                JSONObject chargeData = new JSONObject();
                chargeData.put("segment", "收费段"+index);
                chargeData.put("enStationName", rs.getString("enRoadName") + rs.getString("enStationName"));
                chargeData.put("exStationName", rs.getString("exRoadName") + rs.getString("exStationName"));
                chargeData.put("enTime", rs.getString("enTime"));
                chargeData.put("exTime", rs.getString("exTime"));
                chargeData.put("chargeMiles", rs.getString("chargeMiles"));
                chargeData.put("timeDrive", rs.getString("TIMEMINUTES"));
                chargeData.put("averageSpeed", rs.getString("AVESPEEDKMH"));
                String roadPassed = rs.getString("ROADSPASSED");
                if (roadPassed.indexOf(",") != -1) {
                    roadPassed = roadPassed.replaceAll("\\,","->");
                }
                chargeData.put("roadPassed", roadPassed);
                chargeDataArray.add(chargeData);
                index++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBaseTool.close(rs, ps, conn);
        }

        return chargeDataArray;
    }

    public JSONArray getDrivingRouteDetail(File f){
        JSONArray drivingRouteArray = new JSONArray();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
            String line = "";
            while((line = reader.readLine()) != null) {
                JSONObject path = new JSONObject();
                String[] row = line.split(",");
                path.put("time", row[0]);
                path.put("lng", row[1]);
                path.put("lat", row[2]);
                drivingRouteArray.add(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return drivingRouteArray;
    }

    public JSONArray getTollGateData(String carNo){
        JSONArray tollArray = new JSONArray();
        JSONObject toll = new JSONObject();
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        String sql = "SELECT enTime, exTime, enRoadName,enStationName,exRoadName,exStationName,enStationGPS,exStationGPS FROM carsearch where exPlateNumber=? and fordemo = 'YGJ' ORDER BY (enTime) asc";
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, carNo);
            rs = ps.executeQuery();
            while (rs.next()) {
                toll.put("enTime", rs.getString("enTime"));
                toll.put("exTime", rs.getString("exTime"));
                toll.put("enStation", rs.getString("enRoadName") + rs.getString("enStationName"));
                toll.put("exStation", rs.getString("exRoadName") + rs.getString("exStationName"));
                toll.put("enGPS", rs.getString("enStationGPS"));
                toll.put("exGPS", rs.getString("exStationGPS"));
                tollArray.add(toll);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBaseTool.close(rs, ps, conn);
        }

        return tollArray;
    }

    public JSONObject getTollGateData(File file){
        JSONObject tollGate = new JSONObject();
        BufferedReader reader = null;
        JSONObject tollName = new JSONObject();
        JSONArray tollLocArr = new JSONArray();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line = "";
            int lineNum = 1;
            JSONObject toll = new JSONObject();
            while ((line = reader.readLine()) != null) {
                if (lineNum == 1) {
                    String[] station = line.split(",");
                    tollName.put("startToll", station[0]);
                    tollName.put("endToll", station[1]);
                    lineNum++;
                    continue;
                }
                String[] row = line.split(",");
                toll.put("time", row[0]);
                toll.put("lng", row[1]);
                toll.put("lat", row[2]);
                tollLocArr.add(toll);
            }
            tollGate.put("tollName", tollName);
            tollGate.put("tollArray", tollLocArr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tollGate;
    }

//    public static void main(String[] args) {
//        System.out.print(getChargeDataDetail("粤D10088").toString());
//
//    }

}
