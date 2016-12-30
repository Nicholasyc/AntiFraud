package com.wangyichao.dao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.io.*;

/**
 * Created by Nicholas_Wang on 2016/11/12.
 * 该类目前保留
 */
@Repository("dodgeToll")
public class DodgeToll {
    /**
     * 获取四种逃费类型车的信息
     * @return
     */
    public JSONObject getDodgeTollCar(){
        // 中途取车
        JSONArray middleGet = new JSONArray();
        // 遮挡车牌
        JSONArray shadeCard = new JSONArray();
        // 中途交换卡
        JSONArray changeCard = new JSONArray();
        // 套牌
        JSONArray falseLicence = new JSONArray();
        // 返回
        JSONObject dodgeTollCarInfo = new JSONObject();

        BufferedReader reader = null;
        try {
            String fileName = Config.getProperty("DODGE_TOLL_CAR")+"dodgetoll.txt";
//            System.out.print("fileName: " + fileName);
            File f = new File(fileName);

            reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
            String line;
            while ((line=reader.readLine())!=null){
                String[] carInfo = line.split(",");
                if (carInfo[1].equals("中途取得卡")) {
                    System.out.println(carInfo[0]);
                    middleGet.add(carInfo[0]);
                } else if (carInfo[1].equals("车机和车分离及套牌")) {
                    falseLicence.add(carInfo[0]);
                } else if (carInfo[1].equals("交换卡")) {
                    changeCard.add(carInfo[0]);
                } else {
                    shadeCard.add(carInfo[0]);
                }
            }
//            System.out.print("middle: "+middleGet);
            dodgeTollCarInfo.put("middleGet", middleGet);
            dodgeTollCarInfo.put("shadeCard", shadeCard);
            dodgeTollCarInfo.put("changeCard", changeCard);
            dodgeTollCarInfo.put("falseLicence", falseLicence);
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

        return dodgeTollCarInfo;

    }

}
