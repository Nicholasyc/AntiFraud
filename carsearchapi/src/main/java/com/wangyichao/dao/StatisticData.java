package com.wangyichao.dao;

import com.wangyichao.utils.DataBaseTool;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.support.RestGatewaySupport;
import sun.dc.pr.PRError;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Nicholas_Wang on 2016/11/18.
 */
@Repository("statisticData")
public class StatisticData {

    public static DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    //    DataBaseTool dataBaseTool = new DataBaseTool();
    DataBaseTool dataBaseTool = DataBaseTool.getInstance();

    // 获取当前状况展示区域数据
    public Double getOneMonthSaveMoney() {
        Double saveMoney = 0.0;
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT SUM(escapemoney) as saveMoney from carsearch WHERE action=1 and exTime > DATE_SUB(NOW(),INTERVAL 2 MONTH)";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                saveMoney = rs.getDouble("saveMoney");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
//                if (conn != null) {
////                    dataBaseTool.releaseConn(conn);
//                    conn.close();
//                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return saveMoney;
    }

    public Double getLastMonthSaveMoney() {
        Double saveMoney = 0.0;
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT SUM(escapemoney) as saveMoney from carsearch WHERE action=1 and exTime BETWEEN DATE_SUB(NOW(),INTERVAL 3 MONTH) and DATE_SUB(NOW(),INTERVAL 2 MONTH)";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                saveMoney = rs.getDouble("saveMoney");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
//                if (conn != null) {
////                    dataBaseTool.releaseConn(conn);
//                    conn.close();
//                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return saveMoney;
    }

    public Double getLastYearMonthSaveMoney() {
        Double saveMoney = 0.0;
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT SUM(escapemoney) as saveMoney from carsearch WHERE action=1 and exTime BETWEEN DATE_SUB(NOW(),INTERVAL 15 MONTH) and DATE_SUB(NOW(),INTERVAL 14 MONTH)";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                saveMoney = rs.getDouble("saveMoney");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
//                if (conn != null) {
////                    dataBaseTool.releaseConn(conn);
//                    conn.close();
//                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return saveMoney;
    }

    /**
     * 近一个月打击逃费次数
     *
     * @return
     */
    public int getOneMonthFindDodgeTimes() {
        int times = 0;
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(action) as times from carsearch WHERE action=1 and exTime > DATE_SUB(NOW(),INTERVAL 2 MONTH)";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                times = rs.getInt("times");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
//                if (conn != null) {
////                    dataBaseTool.releaseConn(conn);
//                    conn.close();
//                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return times;
    }

    /**
     * 上个月打击逃费次数
     *
     * @return
     */
    public int getLastMonthFindDodgeTimes() {
        int times = 0;
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(action) as times from carsearch WHERE action=1 and exTime BETWEEN DATE_SUB(NOW(),INTERVAL 3 MONTH) and DATE_SUB(NOW(),INTERVAL 2 MONTH)";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                times = rs.getInt("times");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
//                if (conn != null) {
////                    dataBaseTool.releaseConn(conn);
//                    conn.close();
//                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return times;
    }

    /**
     * 去年本月打击逃费次数
     *
     * @return
     */
    public int getLastYearMonthFindDodgeTimes() {
        int times = 0;
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(action) as times from carsearch WHERE action=1 and exTime BETWEEN DATE_SUB(NOW(),INTERVAL 15 MONTH) and DATE_SUB(NOW(),INTERVAL 14 MONTH)";
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                times = rs.getInt("times");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
//                if (conn != null) {
////                    dataBaseTool.releaseConn(conn);
//                    conn.close();
//                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return times;
    }


    public JSONObject getLossData(int year) {
        JSONObject lossData = new JSONObject();
        // 挽回逃费损失金额
        JSONArray saveLossMoney = new JSONArray();
        // 报警损失金额
        JSONArray alarmLossMoney = new JSONArray();

        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        // DATE_FORMAT(exTime,'%Y') = ? and
        String saveMoneySql = "SELECT SUM(escapeMoney) as saveMoney, DATE_FORMAT(exTime,'%m') as permonth from carsearch WHERE action=1 and fordemo='FALSE' GROUP BY permonth";
        try {
            ps = conn.prepareStatement(saveMoneySql);
//            ps.setInt(1, year);
            rs = ps.executeQuery();
            int index = 1;
            while (rs.next()) {
                // 将查出来的数据和月份对应，防止有的月份没有数据
                while (index < 13) {
                    if (index == rs.getInt("permonth")) {
                        saveLossMoney.add(decimalFormat.format(rs.getDouble("saveMoney") / 100));
                        break;
                    } else {
                        saveLossMoney.add("0");
                    }
                    index++;
                }
                index++;
            }
            if (saveLossMoney.size() != 12) {
                int i = saveLossMoney.size();
                while (i < 12) {
                    saveLossMoney.add("0");
                    i++;
                }
            }
//            System.out.println(saveLossMoney);

            // 报警损失金额
            String lossMoneySql = "SELECT SUM(escapeMoney) as lostMoney, DATE_FORMAT(exTime,'%m') as permonth from carsearch WHERE flag != 'N' and fordemo='FALSE' GROUP BY permonth";
            ps = conn.prepareStatement(lossMoneySql);
//            ps.setInt(1, year);
            rs = ps.executeQuery();
            int index1 = 1;
            while (rs.next()) {
                while (index1 < 13) {
                    if (index1 == rs.getInt("permonth")) {
                        alarmLossMoney.add(decimalFormat.format(rs.getDouble("lostMoney") / 100));
                        break;
                    } else {
                        alarmLossMoney.add("0");
                    }
                    index1++;
                }
                index1++;
            }
            if (alarmLossMoney.size() != 12) {
                int i = alarmLossMoney.size();
                while (i < 12) {
                    alarmLossMoney.add("0");
                    i++;
                }
            }
//            System.out.println(alarmLossMoney);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
//                if (conn != null) {
////                    dataBaseTool.releaseConn(conn);
//                    conn.close();
//                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        lossData.put("saveMoney", saveLossMoney);
        lossData.put("lostMoney", alarmLossMoney);
        return lossData;
    }

    public JSONObject getDodgeTollTimes(int year) {
        JSONObject dodgeTollTimes = new JSONObject();
        // 公司确认逃费次数
        JSONArray confirmTimes = new JSONArray();
        // 报警逃费次数
        JSONArray alarmTimes = new JSONArray();

        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String confirmSql = "SELECT COUNT(action) as confirmTimes,DATE_FORMAT(exTime,'%m') as permonth FROM carsearch WHERE action=1 and fordemo='FALSE' GROUP BY permonth";
        try {
            ps = conn.prepareStatement(confirmSql);
//            ps.setInt(1, year);
            rs = ps.executeQuery();
            int index = 1;
            while (rs.next()) {
                // 将查出来的数据和月份对应，防止有的月份没有数据
                while (index < 13) {
                    if (index == rs.getInt("permonth")) {
                        confirmTimes.add(rs.getInt("confirmTimes"));
                        break;
                    } else {
                        confirmTimes.add("0");
                    }
                    index++;
                }
                index++;
            }
            if (confirmTimes.size() != 12) {
                int i = confirmTimes.size();
                while (i < 12) {
                    confirmTimes.add("0");
                    i++;
                }
            }

            String alarmSql = "SELECT COUNT(flag)as alarmTimes, DATE_FORMAT(exTime,'%m')as permonth FROM carsearch WHERE flag!='N' and fordemo='FALSE' GROUP BY permonth";
            ps = conn.prepareStatement(alarmSql);
//            ps.setInt(1, year);
            rs = ps.executeQuery();
            int index1 = 1;
            while (rs.next()) {
                while (index1 < 13) {
                    if (index1 == rs.getInt("permonth")) {
                        alarmTimes.add(rs.getInt("alarmTimes"));
                        break;
                    } else {
                        alarmTimes.add("0");
                    }
                    index1++;
                }
                index1++;
            }
            if (alarmTimes.size() != 12) {
                int i = alarmTimes.size();
                while (i < 12) {
                    alarmTimes.add("0");
                    i++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
//                if (conn != null) {
////                    dataBaseTool.releaseConn(conn);
//                    conn.close();
//                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        dodgeTollTimes.put("confirmTimes", confirmTimes);
        dodgeTollTimes.put("alarmTimes", alarmTimes);
        return dodgeTollTimes;
    }

    public JSONObject getViolenceAmount(int year) {
        JSONObject violenceAmount = new JSONObject();
        JSONArray rushAmount = new JSONArray();
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(flag) as times, DATE_FORMAT(exTime,'%m') as permonth from carsearch WHERE flag='0C' and fordemo='FALSE' and action=1 GROUP BY permonth";
        try {
            ps = conn.prepareStatement(sql);
//            ps.setInt(1, year);
            rs = ps.executeQuery();
            int index = 1;
            while (rs.next()) {
                while (index < 13) {
                    if (index == rs.getInt("permonth")) {
                        rushAmount.add(rs.getInt("times"));
                        break;
                    } else {
                        rushAmount.add("0");
                    }
                    index++;
                }
                index++;
            }
            if (rushAmount.size() != 12) {
                int i = rushAmount.size();
                while (i < 12) {
                    rushAmount.add("0");
                    i++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
//                if (conn != null) {
////                    dataBaseTool.releaseConn(conn);
//                    conn.close();
//                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        violenceAmount.put("violenceAmount", rushAmount);
        return violenceAmount;
    }


    public JSONObject getChangeCardData(int year) {
        JSONObject changeCardData = new JSONObject();
        JSONArray changeCard = new JSONArray();
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(flag) as times, DATE_FORMAT(exTime,'%m') as permonth from carsearch WHERE (flag='00' or flag='01' or flag='11' or flag='10') and fordemo='FALSE' and action=1 GROUP BY permonth";
        try {
            ps = conn.prepareStatement(sql);
//            ps.setInt(1, year);
            rs = ps.executeQuery();
            int index = 1;
            while (rs.next()) {
                while (index < 13) {
                    if (index == rs.getInt("permonth")) {
                        changeCard.add(rs.getInt("times"));
                        break;
                    } else {
                        changeCard.add("0");
                    }
                    index++;
                }
                index++;
            }
            if (changeCard.size() != 12) {
                int i = changeCard.size();
                while (i < 12) {
                    changeCard.add("0");
                    i++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
//                if (conn != null) {
////                    dataBaseTool.releaseConn(conn);
//                    conn.close();
//                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        changeCardData.put("changeCard", changeCard);
        return changeCardData;
    }

    public JSONObject getWeightFakeData(int year) {
        JSONObject weightFakeData = new JSONObject();
        JSONArray weightFake = new JSONArray();
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(flag) as times, DATE_FORMAT(exTime,'%m') as permonth from carsearch WHERE flag='0A' and fordemo='FALSE' and action=1 GROUP BY permonth";
        try {
            ps = conn.prepareStatement(sql);
//            ps.setInt(1, year);
            rs = ps.executeQuery();
            int index = 1;
            while (rs.next()) {
                while (index < 13) {
                    if (index == rs.getInt("permonth")) {
                        weightFake.add(rs.getInt("times"));
                        break;
                    } else {
                        weightFake.add("0");
                    }
                    index++;
                }
                index++;
            }
            if (weightFake.size() != 12) {
                int i = weightFake.size();
                while (i < 12) {
                    weightFake.add("0");
                    i++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
//                if (conn != null) {
////                    dataBaseTool.releaseConn(conn);
//                    conn.close();
//                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        weightFakeData.put("weightFake", weightFake);
        return weightFakeData;
    }

    public JSONObject getFakeDiscountData(int year) {
        JSONObject fakeDiscountData = new JSONObject();
        JSONArray fakeDiscount = new JSONArray();
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(flag) as times, DATE_FORMAT(exTime,'%m') as permonth from carsearch WHERE flag='0B' and fordemo='FALSE' and action=1 GROUP BY permonth";
        try {
            ps = conn.prepareStatement(sql);
//            ps.setInt(1, year);
            rs = ps.executeQuery();
            int index = 1;
            while (rs.next()) {
                while (index < 13) {
                    if (index == rs.getInt("permonth")) {
                        fakeDiscount.add(rs.getInt("times"));
                        break;
                    } else {
                        fakeDiscount.add("0");
                    }
                    index++;
                }
                index++;
            }
            if (fakeDiscount.size() != 12) {
                int i = fakeDiscount.size();
                while (i < 12) {
                    fakeDiscount.add("0");
                    i++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
//                if (conn != null) {
////                    dataBaseTool.releaseConn(conn);
//                    conn.close();
//                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        fakeDiscountData.put("fakeDiscount", fakeDiscount);
        return fakeDiscountData;
    }
    //SELECT CONCAT(exRoad,exStation) as station,(SELECT SUM(escapemoney) as action1 from carsearch WHERE action=1 and CONCAT(exRoad,exStation)=station and fordemo='FALSE')as saveMoney,(SELECT count(action) as action1 from carsearch WHERE action=1 and CONCAT(exRoad,exStation)=station and fordemo='FALSE') as action1,(SELECT count(flag) from carsearch WHERE flag!='N' and fordemo='FALSE' and CONCAT(exRoad,exStation)=station)as alarm from carsearch WHERE fordemo='FALSE' group by station

    // 由于适应 dataTables 表格控件，暂时将此方法保留，使用下面的 getTableDataArray 方法
    public JSONObject getTableData(int scope) {
        JSONObject tableData = new JSONObject();
        JSONArray rows = new JSONArray();
        JSONObject row = new JSONObject();
        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stationSql = "select CONCAT(exRoad,exStation) as station from carsearch WHERE fordemo='FALSE' group by station";

//        String sql = "SELECT CONCAT(exRoad,exStation) as station,(SELECT SUM(escapemoney) from carsearch WHERE action=1 and CONCAT(exRoad,exStation)=station and fordemo='FALSE')as saveMoney,(SELECT count(action) from carsearch WHERE action=1 and CONCAT(exRoad,exStation)=station and fordemo='FALSE') as action1,(SELECT count(flag) from carsearch WHERE flag!='N' and fordemo='FALSE' and CONCAT(exRoad,exStation)=station)as alarm from carsearch WHERE fordemo='FALSE' group by station";
        try {
            ps = conn.prepareStatement(stationSql);
            rs = ps.executeQuery();
            ArrayList<String> stations = new ArrayList<String>();
            while (rs.next()) {
                stations.add(rs.getString("station"));
            }
            String action1Sql = "SELECT count(action) as action1 from carsearch WHERE CONCAT(exRoad,exStation)=? and exTime > DATE_SUB(NOW(),INTERVAL ? MONTH) and action=1 and fordemo='FALSE'";
            String escapeMoneySql = "SELECT SUM(escapemoney) as saveMoney from carsearch WHERE CONCAT(exRoad,exStation)=? and exTime > DATE_SUB(NOW(),INTERVAL ? MONTH) and action=1 and fordemo='FALSE'";
            String alarmSql = "SELECT count(flag) as alarm from carsearch WHERE CONCAT(exRoad,exStation)=? and exTime > DATE_SUB(NOW(),INTERVAL ? MONTH) and flag!='N' and fordemo='FALSE'";
            for (int i = 0; i < stations.size(); i++) {
                // 获取该收费站 确认逃费次数
                ps = conn.prepareStatement(action1Sql);
                ps.setString(1, stations.get(i));
                ps.setInt(2, scope);
                rs = ps.executeQuery();
                while (rs.next()) {
                    row.put("station", stations.get(i));
                    row.put("action1", rs.getInt("action1"));
                }
                // 获取该收费站 挽回逃费损失
                ps = conn.prepareStatement(escapeMoneySql);
                ps.setString(1, stations.get(i));
                ps.setInt(2, scope);
                rs = ps.executeQuery();
                while (rs.next()) {
                    row.put("saveMoney", decimalFormat.format(rs.getDouble("saveMoney") / 100));
                }
                // 获取该收费站 报警处理率
                ps = conn.prepareStatement(alarmSql);
                ps.setString(1, stations.get(i));
                ps.setInt(2, scope);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getInt("alarm") != 0) {
                        row.put("rate", decimalFormat.format(Double.parseDouble(row.get("action1").toString()) / rs.getDouble("alarm") * 100));
                    } else {
                        row.put("rate", "0.00");
                    }

                }
                rows.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
//                if (conn != null) {
////                    dataBaseTool.releaseConn(conn);
//                    conn.close();
//                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        tableData.put("table", rows);
        System.out.println(tableData.toString());
        return tableData;
    }

    public JSONArray getTableDataArray(int scope) {
        JSONArray rows = new JSONArray();

        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stationSql = "select CONCAT(exRoad,exStation) as station from carsearch WHERE fordemo='FALSE' group by station";

        try {
            ps = conn.prepareStatement(stationSql);
            rs = ps.executeQuery();
            ArrayList<String> stations = new ArrayList<String>();

            while (rs.next()) {
                stations.add(rs.getString("station"));
            }
            System.out.println("stations size: " + stations.size());
            String action1Sql = "SELECT count(action) as action1 from carsearch WHERE CONCAT(exRoad,exStation)=? and exTime > DATE_SUB(NOW(),INTERVAL ? MONTH) and action=1 and fordemo='FALSE'";
            String escapeMoneySql = "SELECT SUM(escapemoney) as saveMoney from carsearch WHERE CONCAT(exRoad,exStation)=? and exTime > DATE_SUB(NOW(),INTERVAL ? MONTH) and action=1 and fordemo='FALSE'";
            String alarmSql = "SELECT count(flag) as alarm from carsearch WHERE CONCAT(exRoad,exStation)=? and exTime > DATE_SUB(NOW(),INTERVAL ? MONTH) and flag!='N' and fordemo='FALSE'";
            for (int i = 0; i < stations.size(); i++) {
                JSONArray row = new JSONArray();
//                System.out.println(stations.get(i));
                // 获取该收费站 确认逃费次数
                ps = conn.prepareStatement(action1Sql);
                ps.setString(1, stations.get(i));
                ps.setInt(2, scope);
                rs = ps.executeQuery();
                int actionNum = 0;
                while (rs.next()) {
                    row.add(stations.get(i));
                    actionNum = rs.getInt("action1");
                    row.add(actionNum);
                }
                // 获取该收费站 挽回逃费损失
                ps = conn.prepareStatement(escapeMoneySql);
                ps.setString(1, stations.get(i));
                ps.setInt(2, scope);
                rs = ps.executeQuery();
                while (rs.next()) {
                    row.add(decimalFormat.format(rs.getDouble("saveMoney") / 100));
                }
                // 获取该收费站 报警处理率
                ps = conn.prepareStatement(alarmSql);
                ps.setString(1, stations.get(i));
                ps.setInt(2, scope);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getInt("alarm") != 0) {
                        row.add(decimalFormat.format(actionNum / rs.getDouble("alarm") * 100));
                    } else {
                        row.add("0.00");
                    }

                }
                rows.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
//                if (conn != null) {
////                    dataBaseTool.releaseConn(conn);
//                    conn.close();
//                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println(rows.toString());
        return rows;
    }

    public JSONArray getOperatorsTableDataArray(int scope) {
        JSONArray rows = new JSONArray();

        Connection conn = dataBaseTool.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String operatorsSql = "SELECT DISTINCT exoperatorid,CONCAT(exRoad,exStation) as station from carsearch WHERE fordemo='FALSE' and action=1";

        try {
            ps = conn.prepareStatement(operatorsSql);
            rs = ps.executeQuery();
            ArrayList<String> operators = new ArrayList<String>();

            while (rs.next()) {
                operators.add(rs.getString("exoperatorid") + "," + rs.getString("station"));
            }
            System.out.println("stations size: " + operators.size());
            String action1Sql = "SELECT count(action) as action1 from carsearch WHERE exoperatorid=? and CONCAT(exRoad,exStation)=? and exTime > DATE_SUB(NOW(),INTERVAL ? MONTH) and action=1 and fordemo='FALSE'";
            String escapeMoneySql = "SELECT SUM(escapemoney) as saveMoney from carsearch WHERE exoperatorid=? and CONCAT(exRoad,exStation)=? and exTime > DATE_SUB(NOW(),INTERVAL ? MONTH) and action=1 and fordemo='FALSE'";
            String alarmSql = "SELECT count(flag) as alarm from carsearch WHERE exoperatorid=? and CONCAT(exRoad,exStation)=? and exTime > DATE_SUB(NOW(),INTERVAL ? MONTH) and flag!='N' and fordemo='FALSE'";
            for (int i = 0; i < operators.size(); i++) {
                JSONArray row = new JSONArray();
//                System.out.println(stations.get(i));
                // 获取该收费站 确认逃费次数
                ps = conn.prepareStatement(action1Sql);
                String operatorId = operators.get(i).split(",")[0];
                String station = operators.get(i).split(",")[1];

                ps.setString(1, operatorId);
                ps.setString(2, station);
                ps.setInt(3, scope);
                rs = ps.executeQuery();
                int actionNum = 0;
                while (rs.next()) {
                    actionNum = rs.getInt("action1");

                }
                // 去除 确认0次的
                if (actionNum == 0) {
                    continue;
                }
                row.add(operatorId);
                row.add(station);
                row.add(actionNum);
                // 获取该收费站 挽回逃费损失
                ps = conn.prepareStatement(escapeMoneySql);
                ps.setString(1, operatorId);
                ps.setString(2, station);
                ps.setInt(3, scope);
                rs = ps.executeQuery();
                while (rs.next()) {
                    row.add(decimalFormat.format(rs.getDouble("saveMoney") / 100));
                }
                // 获取该收费站 报警处理率
                ps = conn.prepareStatement(alarmSql);
                ps.setString(1, operatorId);
                ps.setString(2, station);
                ps.setInt(3, scope);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getInt("alarm") != 0) {
                        row.add(decimalFormat.format(actionNum / rs.getDouble("alarm") * 100));
                    } else {
                        row.add("0.00");
                    }

                }
                rows.add(row);
                if (rows.size() == 300) {
                    break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
//                if (conn != null) {
////                    dataBaseTool.releaseConn(conn);
//                    conn.close();
//                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println(rows.toString());
        return rows;
    }


//    public static void main(String[] args) {
////        getTableData(12);
//        getOperatorsTableDataArray(12);
//    }


}
