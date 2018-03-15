package com.my;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;

/**
 * Created by dell on 2017/10/19.
 */
public class JodaUtil {
    public static void main(String[] args) {

        int hour = DateTimeUtil.getCurrentHour();
        LocalTime localTime = new LocalTime(13, 30, 26, 0);
        LocalTime beginTime = new LocalTime(07, 30, 00, 0);
        LocalTime endTime = new LocalTime(23, 30, 00, 0);
        Interval interval = new Interval(7,23);
        boolean contained = interval.contains(new DateTime("14"));
        System.out.println(contained);
        DateTime df;
    }
}
