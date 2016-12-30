package com.wangyichao.service;

import com.wangyichao.dao.StatisticData;
import net.sf.ezmorph.array.DoubleArrayMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Nicholas_Wang on 2016/11/18.
 */
@Service("statisticService")
public class StatisticService {

    @Resource(name = "statisticData")
    private StatisticData statisticData;

    /**
     * 获取挽回逃费损失金额/报警损失金额按月变化数据
     * @param year
     * @return
     */
    public JSONObject getLossData(int year) {
        return statisticData.getLossData(year);
    }

    /**
     * 获取公司确认逃费次数/报警逃费次数按月变化数据
     * @param year
     * @return
     */
    public JSONObject getDodgeTollTimesData(int year) {
        return statisticData.getDodgeTollTimes(year);
    }

    public JSONObject getViolenceAmountData(int year) {
        return statisticData.getViolenceAmount(year);
    }

    public JSONObject getChangeCardData(int year) {
        return statisticData.getChangeCardData(year);
    }

    public JSONObject getWeightFakeData(int year) {
        return statisticData.getWeightFakeData(year);
    }

    public JSONObject getFakeDiscountData(int year) {
        return statisticData.getFakeDiscountData(year);
    }

    public JSONArray getTableData(int scope) {
        return statisticData.getTableDataArray(scope);
    }

    public JSONObject getThisMonthSaveMoney() {
        JSONObject saveMoney = new JSONObject();
        Double thisMonthMoney = statisticData.getOneMonthSaveMoney()/100;
        Double lastMonthMoney = statisticData.getLastMonthSaveMoney()/100;
        Double lastYearMonthMoney = statisticData.getLastYearMonthSaveMoney()/100;
        Double huanbi = (thisMonthMoney-lastMonthMoney)/lastMonthMoney*100;
        Double tongbi = (thisMonthMoney-lastYearMonthMoney)/lastYearMonthMoney*100;
        if (thisMonthMoney.isInfinite()) {
            thisMonthMoney = 0.0;
        }
        if (huanbi.isInfinite()) {
            huanbi = 0.0;
        }
        if (tongbi.isInfinite()) {
            tongbi = 0.0;
        }
        saveMoney.put("money", thisMonthMoney);
        saveMoney.put("huanbi", huanbi);
        saveMoney.put("tongbi", tongbi);
        return saveMoney;
    }

    public JSONObject getThisMonthFindDodgeTimes() {
        JSONObject times = new JSONObject();
        int thisMonthTimes = statisticData.getOneMonthFindDodgeTimes();
        int lastMonthTimes = statisticData.getLastMonthFindDodgeTimes();
        int lastYearMonthTimes = statisticData.getLastYearMonthFindDodgeTimes();
        Double huanbi = new Double(thisMonthTimes - lastMonthTimes) / lastMonthTimes * 100;
        Double tongbi = new Double(thisMonthTimes - lastYearMonthTimes) / lastYearMonthTimes * 100;

        if (huanbi.isInfinite()) {
            huanbi = 0.0;
        }
        if (tongbi.isInfinite()) {
            tongbi = 0.0;
        }
        times.put("times", thisMonthTimes);
        times.put("huanbi", huanbi);
        times.put("tongbi", tongbi);
        return times;
    }

    public JSONObject getThisMonthAverageSaveMoney() {
        JSONObject saveMoney = new JSONObject();
        Double thisMonthMoney = statisticData.getOneMonthSaveMoney()/100;
        Double lastMonthMoney = statisticData.getLastMonthSaveMoney()/100;
        Double lastYearMonthMoney = statisticData.getLastYearMonthSaveMoney()/100;
        int thisMonthTimes = statisticData.getOneMonthFindDodgeTimes();
        int lastMonthTimes = statisticData.getLastMonthFindDodgeTimes();
        int lastYearMonthTimes = statisticData.getLastYearMonthFindDodgeTimes();
        Double averageMoney = thisMonthMoney/thisMonthTimes;
        Double lastMonthAveMoney = lastMonthMoney/lastMonthTimes;
        Double lastYearMonthAveMoney = lastYearMonthMoney/lastYearMonthTimes;
        Double huanbi = (averageMoney - lastMonthAveMoney) / lastMonthAveMoney * 100;
        Double tongbi = (averageMoney - lastYearMonthAveMoney) / lastYearMonthAveMoney * 100;
        if (averageMoney.isInfinite()) {
            averageMoney = 0.0;
        }
        if (huanbi.isInfinite()) {
            huanbi = 0.0;
        }
        if (tongbi.isInfinite()) {
            tongbi = 0.0;
        }
        saveMoney.put("averageMoney", averageMoney);
        saveMoney.put("huanbi", huanbi);
        saveMoney.put("tongbi", tongbi);
        return saveMoney;
    }

    public JSONArray getOperatorsTableDataArray(int scope) {
        return statisticData.getOperatorsTableDataArray(scope);
    }

}
