package com.wangyichao.controller;

import com.wangyichao.service.StatisticService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Nicholas_Wang on 2016/11/18.
 */
@Controller
@RequestMapping(value = "/statistic")
public class StatisticController {

    @Resource(name = "statisticService")
    private StatisticService statisticService;

    /**
     * 获取近一个月挽回逃费损失
     * @param response
     */
    @RequestMapping(value = "/getThisMonthSaveMoney", method = RequestMethod.GET)
    @ResponseBody
    public void getThisMonthSaveMoney(HttpServletResponse response){
        JSONObject result = statisticService.getThisMonthSaveMoney();
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取近一个月打击逃费次数
     * @param response
     */
    @RequestMapping(value = "/getThisMonthFindDodgeTimes", method = RequestMethod.GET)
    @ResponseBody
    public void getThisMonthFindDodgeTimes(HttpServletResponse response){
        JSONObject result = statisticService.getThisMonthFindDodgeTimes();
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取近一个月打逃平均每笔换回损失
     * @param response
     */
    @RequestMapping(value = "/getThisMonthAverageSaveMoney", method = RequestMethod.GET)
    @ResponseBody
    public void getThisMonthAverageSaveMoney(HttpServletResponse response){
        JSONObject result = statisticService.getThisMonthAverageSaveMoney();
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取挽回逃费损失金额/报警损失金额按月变化数据
     * @param request
     * @param response
     * @param year
     */
    @RequestMapping(value = "/getLossData", method = RequestMethod.GET)
    @ResponseBody
    public void getLossData(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "year", required = true) int year) {
        JSONObject result = statisticService.getLossData(year);
        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取公司确认逃费次数/报警逃费次数按月变化数据
     * @param request
     * @param response
     * @param year
     */
    @RequestMapping(value = "/getDodgeTollTimesData", method = RequestMethod.GET)
    @ResponseBody
    public void getDodgeTollTimesData(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam(value = "year", required = true) int year) {
        JSONObject result = statisticService.getDodgeTollTimesData(year);
        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取公司确认冲卡等暴力逃费数量
     * @param request
     * @param response
     * @param year
     */
    @RequestMapping(value = "/getViolenceAmountData", method = RequestMethod.GET)
    @ResponseBody
    public void getViolenceAmountData(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam(value = "year", required = true) int year) {
        JSONObject result = statisticService.getViolenceAmountData(year);
        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取公司倒卡换卡遮挡卡逃费行为数量
     * @param request
     * @param response
     * @param year
     */
    @RequestMapping(value = "/getChangeCardData", method = RequestMethod.GET)
    @ResponseBody
    public void getChangeCardData(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam(value = "year", required = true) int year) {
        JSONObject result = statisticService.getChangeCardData(year);
        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取公司货车计重造假逃费行为数量
     * @param request
     * @param response
     * @param year
     */
    @RequestMapping(value = "/getWeightFakeData", method = RequestMethod.GET)
    @ResponseBody
    public void getWeightFakeData(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam(value = "year", required = true) int year) {
        JSONObject result = statisticService.getWeightFakeData(year);
        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取公司假冒优惠车辆逃费行为数量
     * @param request
     * @param response
     * @param year
     */
    @RequestMapping(value = "/getFakeDiscountData", method = RequestMethod.GET)
    @ResponseBody
    public void getFakeDiscountData(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam(value = "year", required = true) int year) {
        JSONObject result = statisticService.getFakeDiscountData(year);
        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取分收费站考核数据
     * @param request
     * @param response
     * @param scope
     */
    @RequestMapping(value = "/getTableData", method = RequestMethod.GET)
    @ResponseBody
    public void getTableData(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam(value = "scope", required = true) int scope) {
        JSONArray result = statisticService.getTableData(scope);
        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取分收费站考核数据
     * @param request
     * @param response
     * @param scope
     */
    @RequestMapping(value = "/getOperatorsTableDataArray", method = RequestMethod.GET)
    @ResponseBody
    public void getOperatorsTableDataArray(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "scope", required = true) int scope) {
        JSONArray result = statisticService.getOperatorsTableDataArray(scope);
        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
