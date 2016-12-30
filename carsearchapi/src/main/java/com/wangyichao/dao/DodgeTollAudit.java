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
 * Created by Nicholas_Wang on 2016/11/16.
 * DodgeToll 类的改动版本
 */
@Repository("dodgeTollAudit")
public class DodgeTollAudit {

    DataBaseTool dataBaseTool = DataBaseTool.getInstance();
//    DataBaseTool dataBaseTool = new DataBaseTool();

    /**
     * 获取车辆基本数据
     * 保留
     *
     * @return
     */
    public JSONArray getDodgeTollAuditInfo() {

        JSONArray carInfoArr = new JSONArray();

        BufferedReader reader = null;
        String fileName = Config.getProperty("DODGE_TOLL_CAR") + "逃费稽核展现结果-20160828.csv";
        File f = new File(fileName);
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
            String line = "";
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                if (lineNum == 1) {
                    // 第一行为表标题略过
                    lineNum++;
                    continue;
                }
                JSONObject carInfo = new JSONObject();
                String[] car = line.split(",");
                carInfo.put("carNo", car[3]);
                carInfo.put("exTime", car[2]);
                // 由于数据里面有经纬度，所以多了 逗号分隔符 否则为23
                carInfo.put("history", car[27]);
                if (car[24].equals("无")) {
                    // 如果逃费历史大于0则预警
                    if (Integer.valueOf(car[27]) > 0) {
                        carInfo.put("audit", "预警");
                    } else {
                        carInfo.put("audit", "正常");
                    }

                } else if (car[24].equals("0")) {
                    carInfo.put("audit", "倒卡和遮挡卡");
                } else if (car[24].equals("1")) {
                    carInfo.put("audit", "两车交换卡");
                } else if (car[24].equals("10")) {
                    carInfo.put("audit", "中途取得卡");
                } else {
                    carInfo.put("audit", "车机和车分离及套牌");
                }
                carInfoArr.add(carInfo);
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
        return carInfoArr;

    }

//    public static void main(String[] args) {
//        System.out.println(getDodgeTollAuditInfo().toString());
//    }

    /**
     * 获取某个车辆信息
     * 保留
     *
     * @param carNo
     * @return
     */
    public JSONObject getDodgeTollCarInfo(String carNo) {
        JSONObject carInfo = new JSONObject();
        BufferedReader reader = null;
        String fileName = Config.getProperty("DODGE_TOLL_CAR") + "逃费稽核展现结果-20160828.csv";
        File f = new File(fileName);
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
            String line = "";
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                if (lineNum == 1) {
                    // 第一行为表标题略过
                    lineNum++;
                    continue;
                }
                String[] car = line.split(",");
                if (car[3].equals(carNo)) {
                    carInfo.put("carNo", carNo);
                    carInfo.put("escapeDist", car[23]);
                    carInfo.put("escapeMoney", car[26]);
                    carInfo.put("enStation", car[5]);
                    carInfo.put("exStation", car[7]);
                    carInfo.put("enTime", car[1]);
                    carInfo.put("exTime", car[2]);
                    carInfo.put("history", car[27]);
                    if (car[24].equals("无")) {
                        // 如果逃费历史大于0则预警
                        if (Integer.valueOf(car[27]) > 0) {
                            carInfo.put("behavior", "预警");
                        } else {
                            carInfo.put("behavior", "正常");
                        }

                    } else if (car[24].equals("0")) {
                        carInfo.put("behavior", "倒卡和遮挡卡");
                    } else if (car[24].equals("1")) {
                        carInfo.put("behavior", "两车交换卡");
                    } else if (car[24].equals("10")) {
                        carInfo.put("behavior", "中途取得卡");
                    } else {
                        carInfo.put("behavior", "车机和车分离及套牌");
                    }

                }
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
        return carInfo;
    }

    /**
     * 从数据库获取车辆逃费监控信息
     *
     * @return
     */
    public JSONArray getDodgeTollAuditInfoFromSql() {
        JSONArray carInfoArr = new JSONArray();
        Connection conn = dataBaseTool.getConn();
        String sql = "select exRecordNo,exPlateNumber,exTime,history,escapetype,action,flag from carsearch where fordemo = 'TRUE' order by (exTime) desc";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                JSONObject carInfo = new JSONObject();
                carInfo.put("exRecordNo", rs.getString("exRecordNo"));
                carInfo.put("carNo", rs.getString("exPlateNumber"));
                carInfo.put("exTime", rs.getString("exTime"));
                carInfo.put("history", rs.getString("history"));
                carInfo.put("audit", rs.getString("escapetype"));
                carInfo.put("action", rs.getString("action"));
                carInfo.put("flag", rs.getString("flag"));
                carInfoArr.add(carInfo);
//                System.out.println("carInfo: " + carInfo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBaseTool.close(rs, ps, conn);
        }

        return carInfoArr;
    }

    /**
     * 从数据库获取车辆逃费监控信息 限制查询条数
     *
     * @return
     */
    public JSONArray getDodgeTollAuditInfoFromSqlLimit(int count) {
        JSONArray carInfoArr = new JSONArray();
        Connection conn = dataBaseTool.getConn();
        String sql = "select exRecordNo,exPlateNumber,exTime,history,escapetype,action,flag from carsearch where fordemo = 'TRUE' order by (exTime) asc LIMIT ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, count);
            rs = ps.executeQuery();
            while (rs.next()) {
                JSONObject carInfo = new JSONObject();
                carInfo.put("exRecordNo", rs.getString("exRecordNo"));
                carInfo.put("carNo", rs.getString("exPlateNumber"));
                carInfo.put("exTime", rs.getString("exTime"));
                carInfo.put("history", rs.getString("history"));
                carInfo.put("audit", rs.getString("escapetype"));
                carInfo.put("action", rs.getString("action"));
                carInfo.put("flag", rs.getString("flag"));
                carInfoArr.add(carInfo);
//                System.out.println("carInfo: " + carInfo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBaseTool.close(rs, ps, conn);

        }

        return carInfoArr;
    }

    /**
     * 根据流水号获取某辆车的详细信息
     *
     * @param exRecordNo
     * @return
     */
    public JSONObject getDodgeTollCarInfoFromSql(String exRecordNo) {
        JSONObject carInfo = new JSONObject();
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from carsearch where exRecordNo = ? and fordemo = 'TRUE'";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, exRecordNo);
            rs = ps.executeQuery();
            while (rs.next()) {
                carInfo.put("exRecordNo", rs.getString("exRecordNo"));
                carInfo.put("carNo", rs.getString("exPlateNumber"));
                carInfo.put("escapeDist", rs.getString("escapeDistance"));
                carInfo.put("escapeMoney", rs.getString("escapemoney"));
                carInfo.put("enStation", rs.getString("enStation"));
                carInfo.put("exStation", rs.getString("exStation"));
                carInfo.put("enTime", rs.getString("enTime"));
                carInfo.put("exTime", rs.getString("exTime"));
                carInfo.put("history", rs.getString("history"));
                carInfo.put("behavior", rs.getString("escapetype"));
                carInfo.put("action", rs.getString("action"));
                carInfo.put("cashMoney", rs.getString("cashmoney"));
                carInfo.put("flag", rs.getString("flag"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBaseTool.close(rs, ps, conn);
        }

        return carInfo;
    }

    /**
     * 确认车辆是否逃费操作
     *
     * @param exRecordNo
     * @param operation
     * @return
     */
    public boolean dodgeTollCarConfirm(String exRecordNo, String operation) {
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        String sql = "update carsearch set action = ? where exRecordNo = ? and fordemo = 'TRUE'";
        int result = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, operation);
            ps.setString(2, exRecordNo);
            result = ps.executeUpdate();
            System.out.println("update result: " + result);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBaseTool.close(ps, conn);
        }
        return true;
    }

    /**
     * 修噶车牌号信息
     *
     * @param exRecordNo
     * @param newCarNo
     * @return
     */
    public boolean changeCarNo(String exRecordNo, String newCarNo) {
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        String sql = "update carsearch set exPlateNumber = ? where exRecordNo = ? and fordemo = 'TRUE'";
        int result = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, newCarNo);
            ps.setString(2, exRecordNo);
            result = ps.executeUpdate();
            System.out.println("update result: " + result);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBaseTool.close(ps, conn);
        }

        return true;
    }

}
