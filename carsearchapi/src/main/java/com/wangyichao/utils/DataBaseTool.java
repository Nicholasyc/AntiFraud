package com.wangyichao.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.wangyichao.dao.Config;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.*;
import java.util.LinkedList;

/**
 * Created by Nicholas_Wang on 2016/11/17.
 */
public class DataBaseTool {


    private static DataBaseTool instance = null;
    private static ComboPooledDataSource ds = new ComboPooledDataSource();
    private static Connection conn;


    private DataBaseTool() {

        try {
            ds.setDriverClass(Config.getProperty("driverClassName"));
            ds.setJdbcUrl(Config.getProperty("url"));
            ds.setUser(Config.getProperty("username"));
            ds.setPassword(Config.getProperty("password"));
            ds.setInitialPoolSize(Integer.parseInt(Config.getProperty("initialPoolSize")));
            ds.setMaxPoolSize(Integer.parseInt(Config.getProperty("maxPoolSize")));
            ds.setMinPoolSize(Integer.parseInt(Config.getProperty("minPoolSize")));
            ds.setAcquireIncrement(Integer.parseInt(Config.getProperty("acquireIncrement")));
            ds.setMaxStatements(Integer.parseInt(Config.getProperty("maxStatements")));
            ds.setIdleConnectionTestPeriod(Integer.parseInt(Config.getProperty("idleConnectionTestPeriod")));
            ds.setMaxIdleTime(Integer.parseInt(Config.getProperty("maxIdleTime")));
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    public static DataBaseTool getInstance() {
        if (instance == null) {
            instance = new DataBaseTool();
        }
        return instance;
    }



    public Connection getConn() {
//
        try {
            conn = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    public void close(ResultSet rs, PreparedStatement ps, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(PreparedStatement ps, Connection conn) {
        try {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}
