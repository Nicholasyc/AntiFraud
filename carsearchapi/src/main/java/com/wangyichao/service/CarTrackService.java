package com.wangyichao.service;

import com.wangyichao.dao.CarPath;
import com.wangyichao.dao.CarTrackTollGate;
import com.wangyichao.dao.Config;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

/**
 * Created by Nicholas_Wang on 2016/11/4.
 */
@Service("carTrackService")
public class CarTrackService {

    @Resource(name = "carTrackTollGate")
    private CarTrackTollGate carTrackTollGate;
    @Resource(name = "carPath")
    private CarPath carPathDao;

    public JSONArray multiCarTrackInfoSearch(String carNos) {
        JSONArray cars = new JSONArray();
        String[] carNoArr = carNos.split(",");
        for (String carNo : carNoArr) {
            JSONObject car = carTrackInfoSearch(carNo);
            cars.add(car);
        }
        return cars;
    }


    public JSONObject carTrackInfoSearch(String carNo) {

        File file = new File(Config.getProperty("CAR_TRACK_URL"));
        String carToll = carNo + "_0.txt";
        String carPath = carNo + "_1.txt";
        JSONArray pathArray = new JSONArray();
        JSONObject toll = new JSONObject();
        JSONObject track = new JSONObject();
        for (File f : file.listFiles()) {
            if (carToll.equals(f.getName())) {
                toll = carTrackTollGate.getCarTrackTollGateDate(f);
            } else if (carPath.equals(f.getName())) {
                pathArray = carPathDao.getPathData(f);
            } else {
                continue;
            }
        }
        track.put("carNo", carNo);
        track.put("tollInfo", toll);
        track.put("pathInfo", pathArray);
//        return track.toString();
        return track;
    }
}
