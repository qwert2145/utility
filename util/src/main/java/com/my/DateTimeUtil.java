package com.my;

import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * 日期工具类
 * 
 */
public class DateTimeUtil {

	final static Logger logger = Logger.getLogger(DateTimeUtil.class);

	public static final String	TIME_FORMAT_SHORT				= "yyyyMMddHHmmss";
	public static final String	TIME_FORMAT_YMD					= "yyyy/MM/dd HH:mm:ss";
	public static final String	TIME_FORMAT_NORMAL				= "yyyy-MM-dd HH:mm:ss";
	public static final String	TIME_FORMAT_ENGLISH				= "MM/dd/yyyy HH:mm:ss";
	public static final String	TIME_FORMAT_CHINA				= "yyyy年MM月dd日 HH时mm分ss秒";
	public static final String	TIME_FORMAT_CHINA_S				= "yyyy年M月d日 H时m分s秒";
	public static final String	TIME_FORMAT_SHORT_S				= "HH:mm:ss";

	public static final String	DATE_FORMAT_TINY				= "yyMMdd";
	public static final String	DATE_FORMAT_SHORT				= "yyyyMMdd";
	public static final String	DATE_FORMAT_NORMAL				= "yyyy-MM-dd";
	public static final String	DATE_FORMAT_ENGLISH				= "MM/dd/yyyy";
	public static final String	DATE_FORMAT_CHINA				= "yyyy年MM月dd日";
	public static final String	DATE_FORMAT_CHINA_YEAR_MONTH	= "yyyy年MM月";
	public static final String	MONTH_FORMAT					= "yyyyMM";
	public static final String	YEAR_MONTH_FORMAT				= "yyyy-MM";
	public static final String	DATE_FORMAT_MINUTE				= "yyyyMMddHHmm";
	public static final String	MONTH_DAY_FORMAT				= "MM-dd";

	/**
	 * 得到时间字符串,格式为 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 *            待格式化日期
	 * @return 返回格式化后的时间字符串
	 * @since 0.1
	 */
	public static String getTimeNormalString(Date date) {
		DateFormat fmt = new SimpleDateFormat(TIME_FORMAT_NORMAL);
		return fmt.format(date);
	}
	
	/**
	 * 得到时间字符串,格式为 yyyy-MM-dd
	 * 
	 * @param date
	 *            待格式化日期
	 * @return 返回格式化后的时间字符串
	 * @since 0.1
	 */
	public static String getDateNormalString(Date date) {
		DateFormat fmt = new SimpleDateFormat(DATE_FORMAT_NORMAL);
		return fmt.format(date);
	}

	/**
	 * 得到时间字符串,根据使用的格式模板
	 * 
	 * @param date
	 *            待格式化日期
	 * @param pattern
	 *            格式化模板
	 * @return 返回格式化后的时间字符串
	 */
	public static String getTimeString(Date date, String pattern) {
		DateFormat fmt = new SimpleDateFormat(pattern);
		return fmt.format(date);
	}

	/**
	 * Description:TODO 获取当前时间字符串,格式为 yyyy-MM-dd HH:mm:ss
	 * 
	 * @return String 返回格式化后的时间字符串 添加人: huwenhu
	 */
	public static String getNowTimeNormalString() {
		Date date = new Date();
		return getTimeNormalString(date);
	}

	/**
	 * 获取本月的年月日
	 * 
	 * @param toFormat
	 *            日期格式
	 * @return
	 */
	public static String getDate(String toFormat) {
		SimpleDateFormat format = new SimpleDateFormat(toFormat);
		String time = format.format(new Date());
		return time;
	}

	public static Date parseDate(String strDate, String pattern) {
		DateFormat fmt = new SimpleDateFormat(pattern);
		try {
			return fmt.parse(strDate);
		} catch (ParseException e) {
			logger.warn(e.getMessage(), e);
			throw new IllegalArgumentException("Date or Time String is invalid.");
		}
	}

	/**
	 * 把日期字符串转换为日期类型
	 * 
	 * @param dateStr
	 *            日期字符串
	 * @return 日期
	 * @since 0.1
	 */
	public static Date convertAsDate(String dateStr) {
		if (dateStr == null || "".equals(dateStr)) {
			return null;
		}
		DateFormat fmt = null;
		if (dateStr.matches("\\d{14}")) {
			fmt = new SimpleDateFormat(TIME_FORMAT_SHORT);
		} else if (dateStr.matches("\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}")) {
			fmt = new SimpleDateFormat(TIME_FORMAT_NORMAL);
		} else if (dateStr.matches("\\d{1,2}/\\d{1,2}/\\d{4} \\d{1,2}:\\d{1,2}:\\d{1,2}")) {
			fmt = new SimpleDateFormat(TIME_FORMAT_ENGLISH);
		} else if (dateStr.matches("\\d{4}年\\d{1,2}月\\d{1,2}日 \\d{1,2}时\\d{1,2}分\\d{1,2}秒")) {
			fmt = new SimpleDateFormat(TIME_FORMAT_CHINA);
		} else if (dateStr.matches("\\d{8}")) {
			fmt = new SimpleDateFormat(DATE_FORMAT_SHORT);
		} else if (dateStr.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
			fmt = new SimpleDateFormat(DATE_FORMAT_NORMAL);
		} else if (dateStr.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
			fmt = new SimpleDateFormat(DATE_FORMAT_ENGLISH);
		} else if (dateStr.matches("\\d{4}年\\d{1,2}月\\d{1,2}日")) {
			fmt = new SimpleDateFormat(DATE_FORMAT_CHINA);
		} else if (dateStr.matches("\\d{4}\\d{1,2}\\d{1,2}\\d{1,2}\\d{1,2}")) {
			fmt = new SimpleDateFormat(DATE_FORMAT_MINUTE);
		} else if (dateStr.matches("\\d{1,2}:\\d{1,2}:\\d{1,2}")) {
			fmt = new SimpleDateFormat(TIME_FORMAT_SHORT_S);
		}
		try {
			return fmt.parse(dateStr);
		} catch (ParseException e) {
			logger.warn(e.getMessage(), e);
			throw new IllegalArgumentException("Date or Time String is invalid.");
		}
	}

	public static Date addDay(Date date, int day) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);
		return calendar.getTime();
	}

	public static Date addMinute(Date date, int minute) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minute);
		return calendar.getTime();
	}

	public static Date addSecond(Date date, int second) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, second);
		return calendar.getTime();
	}

	public static Date addHour(Date date, int hour) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, hour);
		return calendar.getTime();
	}
	
	public static String addMonth(String strDate, int month) {
		DateFormat fmt = new SimpleDateFormat(DATE_FORMAT_NORMAL);
		Date date=null;
		try {
			date = fmt.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, month);
		return fmt.format(calendar.getTime()) ;
	}
	
	public static int getCurrentYear(){
		return Calendar.getInstance().get(Calendar.YEAR);
	}
	
	public static int getCurrentMonth(){
		return Calendar.getInstance().get(Calendar.MONTH)+1;
	}
	
	public static int getCurrentDay(){
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	public static int getCurrentHour(){
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}
	public static int getYearOfDate(String strDate){
		Date date=null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}
	
	public static int getMonthOfDate(String strDate){
		Date date=null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}
	
	public static int getDayOfDate(String strDate){
		Date date=null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	
	
	/**
	 * 取两日期相差天数
	 * 
	 * @param beginningDate
	 * @param endingDate
	 * @return
	 */
	public static final int getDaydifference(Date beginningDate,Date endingDate)
	{
		long diff=endingDate.getTime()-beginningDate.getTime();
		return (int)(TimeUnit.DAYS.convert(diff,TimeUnit.MILLISECONDS));
	}

	public static String delayScheduleDateBySecond(Date dtNow, int second) {
		return getTimeNormalString(addSecond(dtNow, second));
	}

	public static String delayScheduleDateByMinute(Date dtNow, int minute) {
		return getTimeNormalString(addMinute(dtNow, minute));
	}

	public static String delayScheduleDateByHour(Date dtNow, int hour) {
		return getTimeNormalString(addHour(dtNow, hour));
	}

	public static void main(String[] args) {
		System.out.println(getCurrentHour());
	}
}
