package com.wangyichao.controller;

import com.wangyichao.service.GovernmentAdminService;
import com.wangyichao.utils.PrintResult;
import net.sf.json.JSONArray;
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
import java.io.UnsupportedEncodingException;

/**
 * Created by Nicholas_Wang on 2016/12/26.
 */
@Controller
@RequestMapping(value = "/admin")
public class GovernmentAdminController {

    @Resource(name = "governmentAdminService")
    private GovernmentAdminService governmentAdminService;

    /**
     * 获取车辆基本信息
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getBasicCarInfo", method = {RequestMethod.GET})
    @ResponseBody
    public void getBasicCarInfo(HttpServletRequest request, HttpServletResponse response) {
        JSONArray carInfoArray = governmentAdminService.getBasicCarInfo();
        response.setContentType("text/html;charset=utf-8");
        PrintResult.printResut(response, carInfoArray);
    }

    /**
     * 根据车牌号获取高速收费流水详情
     * @param request
     * @param response
     * @param car
     */
    @RequestMapping(value = "/getChargeDataDetail", method = {RequestMethod.GET})
    @ResponseBody
    public void getChargeDataDetail(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam(value = "carNo", required = true) String car) {
        String carNo = "";
        try {
            carNo = new String(car.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JSONArray chargeDataArray = new JSONArray();
        chargeDataArray = governmentAdminService.getChargeDataDetail(carNo);
        response.setContentType("text/html;charset=utf-8");
        PrintResult.printResut(response, chargeDataArray);
    }

    /**
     * 根据车牌号获取行使轨迹详情
     * @param request
     * @param response
     * @param car
     */
    @RequestMapping(value = "/getAllDrivingRoute", method = {RequestMethod.GET})
    @ResponseBody
    public void getAllDrivingRoute(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam(value = "carNo", required = true) String car) {
        String carNo = "";
        try {
            carNo = new String(car.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JSONArray driveRouteArray = governmentAdminService.getAllDrivingRoute(carNo);
        response.setContentType("text/html;charset=utf-8");
//        System.out.println(driveRouteArray.toString());
        PrintResult.printResut(response, driveRouteArray);

    }

    /**
     * 根据车辆获取收费站信息，以后可能加入时间范围参数 scope
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getTollGateWithTimeStamp", method = {RequestMethod.GET})
    @ResponseBody
    public void getTollGateWithTimeStamp(HttpServletRequest request, HttpServletResponse response,
                                         @RequestParam(value = "carNo", required = true) String car) {
        String carNo = "";
        try {
            carNo = new String(car.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JSONArray tollGateWithTimeStamp = governmentAdminService.getTollGateWithTimeStamp(carNo);
        response.setContentType("text/html;charset=utf-8");
        PrintResult.printResut(response, tollGateWithTimeStamp);

    }

    /**
     * 根据车辆获取收费站信息，以后可能加入时间范围参数 scope
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getDrivingRouteWithTimeStamp", method = {RequestMethod.GET})
    @ResponseBody
    public void getDrivingRouteWithTimeStamp(HttpServletRequest request, HttpServletResponse response,
                                         @RequestParam(value = "carNo", required = true) String car) {
        String carNo = "";
        try {
            carNo = new String(car.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JSONArray drivingRouteWithTimeStamp = governmentAdminService.getDrivingRouteWithTimeStamp(carNo);
        response.setContentType("text/html;charset=utf-8");
        PrintResult.printResut(response, drivingRouteWithTimeStamp);

    }
}
