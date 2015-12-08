package com.my;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wlb on 2015/12/1.
 */
public class DateUtil {
   private static SimpleDateFormat instance = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   public static Date getDate(String dateStr){
       Date date = null;
       try {
           date = instance.parse(dateStr);
       } catch (ParseException e) {
           e.printStackTrace();
       }
       return date;
   }

}
