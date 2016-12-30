package com.wangyichao.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Nicholas_Wang on 2016/10/12.
 */
public class Config {

    private static String confDir;
    private static Properties clearConf;
    private final static String clearConfName = "app.properties";

    public static String getProperty(String key) {
        clearConf = new Properties();
        confDir = Thread.currentThread().getContextClassLoader().getResource("conf").getPath();
        FileInputStream clearConfFis = null;
        try {
            clearConfFis = new FileInputStream(new File(confDir + File.separator + clearConfName));
            clearConf.load(clearConfFis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clearConf.getProperty(key);
    }
}
