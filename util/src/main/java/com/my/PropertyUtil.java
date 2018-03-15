package com.my;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by wlb on 2015/12/8.
 */
public class PropertyUtil {
    public void read(){
        Properties prop = new Properties();
        try {
            prop.load(PropertyUtil.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
//            LOGGER.error(e.getMessage());
        }

        int pageSize = Integer.parseInt(prop.getProperty("pageSize"));
    }

    public static String getValue(String name){
        Properties prop = new Properties();
        try {
            prop.load(PropertyUtil.class.getClassLoader().getResourceAsStream("mail.properties"));
        } catch (IOException e) {
//            LOGGER.error(e.getMessage());
        }
        return prop.getProperty(name);
    }
}
