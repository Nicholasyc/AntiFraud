package com.wangyichao.dao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.io.*;

/**
 * Created by Nicholas_Wang on 2016/10/31.
 */
@Repository("carPath")
public class CarPath {
    public JSONArray getPathData(File f) {
        JSONArray pathArray = new JSONArray();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
            String line = "";
            JSONObject path;
            while ((line = reader.readLine()) != null) {
                String[] rows = line.split(",");
                path = get_path_json(rows[0], rows[1], rows[2]);
                pathArray.add(path);
            }
        }  catch (IOException e) {
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
        return pathArray;
    }

    private JSONObject get_path_json(String time, String lng, String lat) {
        JSONObject path = new JSONObject();
        path.put("time", time);
        path.put("lng", lng);
        path.put("lat", lat);
        return path;
    }
}
