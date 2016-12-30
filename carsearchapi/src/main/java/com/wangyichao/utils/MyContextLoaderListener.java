package com.wangyichao.utils;

import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * Created by Nicholas_Wang on 2016/12/8.
 */
public class MyContextLoaderListener extends ContextLoaderListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("容器初始化");
        super.contextInitialized(event);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

//        DataBaseTool dataBaseTool = DataBaseTool.getInstance();
//        dataBaseTool.closePool();
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
            }

        }
        System.out.println("已销毁所有的dirvers");
        super.contextDestroyed(event);
    }
}
