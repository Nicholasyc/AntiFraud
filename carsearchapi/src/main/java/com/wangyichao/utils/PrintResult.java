package com.wangyichao.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Nicholas_Wang on 2016/12/27.
 */
public class PrintResult {

    public static void printResut(HttpServletResponse response, Object object) {
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(object);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
