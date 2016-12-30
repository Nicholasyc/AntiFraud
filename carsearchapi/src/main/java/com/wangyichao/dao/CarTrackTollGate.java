package com.wangyichao.dao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.io.*;

/**
 * Created by Nicholas_Wang on 2016/10/30.
 */
@Repository("carTrackTollGate")
public class CarTrackTollGate {

    public JSONObject getCarTrackTollGateDate(File f){

        BufferedReader reader = null;
        String line = "";
        JSONObject tollInfo = new JSONObject();
        JSONObject tollName = new JSONObject();
        JSONArray tollLocationArray = new JSONArray();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
            JSONObject toll;
            int num = 1;
            while ((line = reader.readLine()) != null) {
                if (num == 1) {
                    tollName.put("startToll", line.split(",")[0]);
                    tollName.put("endToll", line.split(",")[1]);
                    num++;
                    continue;
                }
                String[] rows = line.split(",");
                toll = getPathJson(rows[0], rows[1], rows[2]);
                tollLocationArray.add(toll);
            }
            tollInfo.put("tollName", tollName);
            tollInfo.put("tollArray", tollLocationArray);
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

        return tollInfo;
    }

    private JSONObject getPathJson(String time, String lng, String lat) {
        JSONObject path = new JSONObject();
        path.put("time", time);
        path.put("lng", lng);
        path.put("lat", lat);
        return path;
    }
}
