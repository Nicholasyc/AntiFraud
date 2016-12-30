package com.wangyichao.controller;

import com.wangyichao.dao.DodgeToll;
import com.wangyichao.dao.DodgeTollAudit;
import com.wangyichao.service.CarTrackService;
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
import java.io.UnsupportedEncodingException;

/**
 * Created by Nicholas_Wang on 2016/11/4.
 */
@Controller
@RequestMapping("/")
public class CarTrackController {

    @Resource(name = "carTrackService")
    private CarTrackService carTrackService;
    @Resource(name = "dodgeTollAudit")
    private DodgeTollAudit dodgeTollAudit;
    @Resource(name = "dodgeToll")
    private DodgeToll dodgeToll;

    /**
     * 查询一辆车的路线信息
     * @param request
     * @param response
     * @param car
     */
    @RequestMapping(value = "search", method = {RequestMethod.GET})
    @ResponseBody
    public void carNoSearch(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam(value = "carNo", required = true) String car){
        String carNo = "";
        try {
            carNo = new String(car.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("car: " + car);
        System.out.println(request.getQueryString());
        System.out.println("carNo: " + carNo);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("text/html;charset=UTF-8");
        String result = carTrackService.carTrackInfoSearch(carNo).toString();
        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.print(result);
//        return result;
    }

    /**
     * 获取逃费稽核展现结果数据
     * @param response
     */
    @RequestMapping(value = "getDodgeTollAuditInfo", method = {RequestMethod.GET})
    @ResponseBody
    public void getDodgeTollAuditInfo(HttpServletResponse response){

//        ,
//        @RequestParam(value = "count", required = true) int count
//        JSONArray carInfoArr = dodgeTollAudit.getDodgeTollAuditInfoFromSqlLimit(count);
        JSONArray carInfoArr = dodgeTollAudit.getDodgeTollAuditInfoFromSql();
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(carInfoArr);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据多个 carNo 查询多辆车路线信息 保留
     * @param request
     * @param response
     * @param car
     */
    @RequestMapping(value = "searchcars", method = {RequestMethod.GET})
    @ResponseBody
    public void multiCarNoSearch(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "carNos", required = true) String car){
        String carNos = "";
        try {
            carNos = new String(car.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Origin", "*");
        JSONArray result = carTrackService.multiCarTrackInfoSearch(carNos);
//        String result = carTrackService.multiCarTrackInfoSearch(carNos).toString();
        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.print(result);
//        return result;
    }

    /**
     * 获取四种逃费车名单 保留
     * @param response
     */
    @RequestMapping(value = "getDodgeTollCarList", method = {RequestMethod.GET})
    @ResponseBody
    public void dodgeTollCarListGet(HttpServletResponse response){

        JSONObject dodgeTollCarInfo = dodgeToll.getDodgeTollCar();
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(dodgeTollCarInfo.toString());
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看某辆逃费车的详细信息
     * @param request
     * @param response
     * @param car
     */
    @RequestMapping(value = "getDodgeTollCarInfo", method = {RequestMethod.GET})
    @ResponseBody
    public void getDodgeTollCarInfo(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "carNo", required = true) String car){

        String carNo = "";
        try {
            carNo = new String(car.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String result = dodgeTollAudit.getDodgeTollCarInfoFromSql(carNo).toString();
        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.print(result);
//        return result;
    }

    @RequestMapping(value = "dodgeTollCarConfirm", method = {RequestMethod.GET})
    @ResponseBody
    public void dodgeTollCarConfirm(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam(value = "carNo", required = true) String car,
                                    @RequestParam(value = "operation", required = true) String operation){
        System.out.println("confirm api query: " + request.getQueryString());
        String carNo = "";
        try {
            carNo = new String(car.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Boolean result = dodgeTollAudit.dodgeTollCarConfirm(carNo, operation);
        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.print(result);
//        return result;
    }

    @RequestMapping(value = "changeCarNo", method = {RequestMethod.GET})
    @ResponseBody
    public void changeCarNo(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam(value = "exRecordNo", required = true) String exRecordNo,
                                    @RequestParam(value = "newCarNo", required = true) String car){
        String newCarNo = "";
        try {
            newCarNo = new String(car.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Boolean result = dodgeTollAudit.changeCarNo(exRecordNo, newCarNo);
        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(result);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.print(result);
//        return result;
    }

}
