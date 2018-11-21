package com.wis.basis.common.utils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.DateUtil;

/**
 * Date Utility Class. And please use Apache common DateUtils to add, judge
 * equality, round, truncate date.
 * 
 * @author sherlockq
 * @see org.apache.commons.lang.time.DateUtils
 */
public class DateUtils {

	private static final Log log = LogFactory.getLog(LogConstant.SYS_LOG);

	/**
	 * FastDateFormat cache map
	 */
	protected static Map<String, FastDateFormat> dateFormatMap = new HashMap<String, FastDateFormat>();

	/**
	 * Date format pattern
	 */
	public final static String FORMAT_DATE_DEFAULT = "yyyy-MM-dd";
	public final static String FORMAT_DATE_YYYYMMDD = "yyyyMMdd";
	public final static String FORMAT_DATE_YYYY_MM_DD = "yyyy-MM-dd";
	public final static String FORMAT_DATE_YYYY_M_D = "yyyy-M-d";
	public final static String FORMAT_DATE_SLASH_YYYY_MM_DD = "yyyy/MM/dd";
	public final static String FORMAT_DATE_SLASH_YYYY_M_DD = "yyyy/M/dd";
	public final static String FORMAT_DATE_SLASH_YYYY_MM_D = "yyyy/MM/d";
	public final static String FORMAT_DATE_SLASH_YYYY_M_D = "yyyy/M/d";
	public final static String FORMAT_DATE_YYMMDD = "yyMMdd";
	public final static String FORMAT_DATE_YYMM = "yyMM";
	public final static String FORMAT_DATE_MMDD = "MMdd";
	public final static String FORMAT_DATE_YYYY = "yyyy";
	public final static String FORMAT_DATE_YYYY_MM = "yyyy-MM";
	public final static String FORMAT_DATE_YYYYMM = "yyyyMM";
	/**
	 * DateTime format pattern
	 */
	public final static String FORMAT_DATETIME_DEFAULT = "yyyy-MM-dd HH:mm:ss";
	public final static String FORMAT_DATETIME_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public final static String FORMAT_DATETIME_YYYY_M_D_HH_MM_SS = "yyyy-M-d HH:mm:ss";

	public final static String FORMAT_DATETIME_YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
	public final static String FORMAT_DATETIME_YYYY_MM_DD_HHMM = "yyyy-MM-dd HHmm";
	public final static String FORMAT_DATETIME_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public final static String FORMAT_DATETIME_YYYY_MM_DD_HH_MM_REPORT = "yyyy/MM/dd HH:mm";
	public final static String FORMAT_DATETIME_YYYY_MM_DD_HHMMSS = "yyyy-MM-dd HHmmss";
	public final static String FORMAT_DATETIME_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public final static String FORMAT_DATETIME_EXIF = "yyyy:MM:dd HH:mm:ss";

	/**
	 * Time format pattern
	 */
	public final static String FORMAT_TIME_DEFAULT = "HH:mm";
	public final static String FORMAT_TIME_HH_MM = "HH:mm";
	public final static String FORMAT_TIME_HHMM = "HHmm";
	public final static String FORMAT_TIME_HH_MM_SS = "HH:mm:ss";
	public final static String FORMAT_TIME_HHMMSS = "HHmmss";

	/**
	 * Returns FastDateFormat according to given pattern which is cached.<br/>
	 * This method is not public due to that the FastDateFormat instance may be
	 * altered by outside.
	 * 
	 * @param formatPattern date format pattern string
	 * @return Cached FastDateFormat instance which should not be altered in any
	 *         way.
	 */
	protected static Format getCachedDateFormat(String formatPattern) {
		// Due to simpledateformat's non thread-local, return a new pattern
		// every time.
		FastDateFormat dateFormat = dateFormatMap.get(formatPattern);

		if (dateFormat == null) {

			dateFormat = FastDateFormat.getInstance(formatPattern);
			dateFormatMap.put(formatPattern, dateFormat);

		}

		return dateFormat;

	}

	/**
	 * Returns DateFormat according to given pattern.
	 * 
	 * @param formatPattern date format pattern string
	 * @return DateFormat instance.
	 */
	public static DateFormat getDateFormat(String formatPattern) {

		return new SimpleDateFormat(formatPattern);

	}

	/**
	 * Format the date according to the pattern.
	 * 
	 * @param date          Date to format. If it's null, the result will be null.
	 * @param formatPattern Date format pattern string. You can find common ones in
	 *                      DateUtils class.
	 * @return Formatted date in String
	 * @see DateUtil
	 */
	public static String formatDate(Date date, String formatPattern) {
		if (date == null) {
			return null;
		}
		return getCachedDateFormat(formatPattern).format(date);
	}

	/**
	 * Format the date in default pattern: yyyy-MM-dd.
	 * 
	 * @param date Date to format. If it's null, the result will be null.
	 * @return Formatted date in String
	 * @see DateUtil#FORMAT_DATE_DEFAULT
	 * @see DateUtil#formatDate(Date, String)
	 */
	public static String formatDate(Date date) {
		return formatDate(date, FORMAT_DATE_DEFAULT);
	}

	/**
	 * Format the date in default date-time pattern: yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date Date to format. If it's null, the result will be null.
	 * @return Formatted date-time in String
	 * @see DateUtil#FORMAT_DATETIME_DEFAULT
	 * @see DateUtil#formatDate(Date, String)
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, FORMAT_DATETIME_DEFAULT);
	}

	public static String formatDateTime() {
		return formatDate(getCurrentDate(), FORMAT_DATETIME_DEFAULT);
	}

	/**
	 * Format the date in default time pattern: HH:mm:ss
	 * 
	 * @param date Date to format. If it's null, the result will be null.
	 * @return Formatted time in String
	 * @see DateUtil#FORMAT_TIME_DEFAULT
	 * @see DateUtil#formatDate(Date, String)
	 */
	public static String formatTime(Date date) {
		return formatDate(date, FORMAT_TIME_DEFAULT);
	}

	/**
	 * 转换位时分秒
	 **/
	public static String formatHMS(Date date) {
		return formatDate(date, FORMAT_TIME_HH_MM_SS);
	}

	/**
	 * Returns current system date.
	 * 
	 * @return current system date.
	 * @see Date#Date()
	 */
	public static Date getCurrentDate() {
		return new Date();
	}

	public static Date parseDate(String stringValue) {
		return parse(stringValue, FORMAT_DATE_DEFAULT);
	}

	public static Date parseDateTime(String stringValue) {
		return parse(stringValue, FORMAT_DATETIME_DEFAULT);
	}

	/**
	 * Parse string value to Date by given format pattern. If parse failed, null
	 * would be returned.
	 * 
	 * @param stringValue   date value as string.
	 * @param formatPattern format pattern.
	 * @return Date represents stringValue, null while parse exception occurred.
	 */
	public static Date parse(String stringValue, String formatPattern) {

		try {
			return new SimpleDateFormat(formatPattern).parse(stringValue);
		} catch (ParseException e) {
			log.warn("parse error:" + e.toString());
			return null;
		}

	}

	/**
	 * Parse string value to Date by given format pattern. If string value is null
	 * or parse failed, null would be returned.
	 */
	public static Date parseAvoidNull(String stringValue, String formatPattern) throws ParseException {
		if (stringValue == null || stringValue.trim().equals("")) {
			return null;
		}

		int index = stringValue.indexOf("-");
		if (index > 0) {
			String str = stringValue.substring(0, index);
			if (str.length() == 2) {
				stringValue = "20" + stringValue;
			}
		}

		return new SimpleDateFormat(formatPattern).parse(stringValue);
	}

	/**
	 * Parses given Date to a Timestamp instance with same date in milliseconds
	 * precision.
	 * 
	 * @param date Date to parse.
	 * @return Timestamp in milliseconds precision
	 */
	public static Timestamp parseTimestamp(Date date) {
		return new Timestamp(date.getTime());
	}

	/**
	 * Compares two dates based on the specified calendar field, and ignores those
	 * fields more trivial. Neither date could be null.<br/>
	 * For example, if calendarField is Calendar.Month, then result will be 0 if
	 * 2008-8-2 and 2008-8-10 is compared. But the result will be -1 if
	 * 
	 * @param date1
	 * @param date2
	 * @param calenderField
	 * @return date1 < date2 : <0<br/>
	 *         date1 = date2 : 0<br/>
	 *         date1 > date2 : >0
	 * @throws IllegalArgumentException If either date is null.
	 * @see Calendar
	 */
	public static int compareDateToField(final Date date1, final Date date2, int calendarField) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("Neither date could be null");
		}
		Date truncatedDate1 = org.apache.commons.lang.time.DateUtils.truncate(date1, calendarField);
		Date truncatedDate2 = org.apache.commons.lang.time.DateUtils.truncate(date2, calendarField);
		return truncatedDate1.compareTo(truncatedDate2);

	}

	/**
	 * 系统常用java日期格式转化为extjs对应的日期格式
	 * 
	 * @param java2FastDateFormat
	 * @return
	 */
	public static String extjsDateFormat(String java2FastDateFormat) {
		Map<String, String> extFastDateFormatMap = new HashMap<String, String>();
		extFastDateFormatMap.put(DateUtils.FORMAT_DATE_DEFAULT, "Y-m-d");
		extFastDateFormatMap.put(DateUtils.FORMAT_DATE_YYYYMMDD, "Ymd");
		extFastDateFormatMap.put(DateUtils.FORMAT_DATE_SLASH_YYYY_M_D, "Y/m/d");
		extFastDateFormatMap.put(DateUtils.FORMAT_DATETIME_DEFAULT, "Y-m-d H:i:s");
		extFastDateFormatMap.put(DateUtils.FORMAT_DATETIME_YYYYMMDDHHMMSS, "YmdHis");
		return extFastDateFormatMap.get(java2FastDateFormat);
	}

	/*
	 * public static void main(String[] args) { Date d =
	 * DateUtils.parse("2009-01-25", FORMAT_DATE_DEFAULT);
	 * System.out.println(Calendar.getInstance(Locale.GERMANY).
	 * getMinimalDaysInFirstWeek());
	 * System.out.println(Calendar.getInstance(Locale.GERMANY).getFirstDayOfWeek
	 * ()); Calendar cal = Calendar.getInstance();
	 * System.out.println(cal.getMinimalDaysInFirstWeek()); cal.setTime(d);
	 * System.out.println(cal.get(Calendar.WEEK_OF_YEAR));
	 * System.out.println(cal.getTime()); cal.set(Calendar.DAY_OF_WEEK,
	 * Calendar.MONDAY); System.out.println(cal.getTime());
	 * 
	 * // test speed Date start = new Date(); for (int i = 0; i < 500000; i++) {
	 * DateUtils.formatDate(start); } System.out.println(new Date().getTime() -
	 * start.getTime());
	 * 
	 * // final SimpleDateFormat df = new //
	 * SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH); final String testdata[] = {
	 * "1999-04-21", "2001-01-05", "2007-12-30" };
	 * 
	 * // test Thread-safe Runnable r[] = new Runnable[testdata.length]; for (int i
	 * = 0; i < r.length; i++) { final int i2 = i; r[i] = new Runnable() { public
	 * void run() {
	 * 
	 * for (int j = 0; j < 1000; j++) { String str = testdata[i2]; String str2 =
	 * null; synchronized(df) { Date d = DateUtils.parse(str,
	 * DateUtils.FORMAT_DATE_YYYY_MM_DD); str2 = DateUtils.formatDate(d,
	 * DateUtils.FORMAT_DATE_YYYY_MM_DD); } if (!str.equals(str2)) { throw new
	 * RuntimeException("date conversion failed after " + j +
	 * " iterations. Expected " + str + " but got " + str2); } }
	 * 
	 * } }; new Thread(r[i]).start(); } }
	 */

	/**
	 * 格式化成一天中的最小时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date formatMinDate(Date date) {
		if (date != null) {
			String beginDateStr = DateUtils.formatDate(date) + " 00:00:00.000";
			date = DateUtils.parse(beginDateStr, FORMAT_DATETIME_YYYY_MM_DD_HH_MM_SS_SSS);
		}
		return date;
	}

	/**
	 * 格式化成一天中的最大时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date formatMaxDate(Date date) {
		if (date != null) {
			String endDateStr = DateUtils.formatDate(date) + " 23:59:59.999";
			date = DateUtils.parse(endDateStr, FORMAT_DATETIME_YYYY_MM_DD_HH_MM_SS_SSS);
		}
		return date;
	}

	/**
	 * 格式化成一天中的最小时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date formatMinDate(String date) {
		if (StringUtils.isNotBlank(date)) {
			String beginDateStr = date + " 00:00:00.000";
			return DateUtils.parse(beginDateStr, FORMAT_DATETIME_YYYY_MM_DD_HH_MM_SS_SSS);
		}
		return null;
	}

	/**
	 * 格式化成一天中的最大时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date formatMaxDate(String date) {
		if (StringUtils.isNotBlank(date)) {
			String endDateStr = date + " 23:59:59.999";
			return DateUtils.parse(endDateStr, FORMAT_DATETIME_YYYY_MM_DD_HH_MM_SS_SSS);
		}
		return null;
	}

	/* Migrage old DateUtils */
	public static Date parseDateShort(String str) {
		return parse(str, FORMAT_DATE_YYYY_M_D);
	}

	public static Date parseDateLong(String str) {
		return parse(str, FORMAT_DATETIME_YYYY_M_D_HH_MM_SS);
	}

	public static Date parseDateFull(String str) {
		return parse(str, FORMAT_DATETIME_YYYY_MM_DD_HH_MM_SS);
	}

	public static String formatShort(Date date) {
		return formatDate(date, FORMAT_DATE_YYYY_M_D);
	}

	public static String formatLong(Date date) {
		return formatDate(date, FORMAT_DATETIME_YYYY_M_D_HH_MM_SS);
	}

	public static String formatFull(Date date) {
		return formatDate(date, FORMAT_DATETIME_YYYY_MM_DD_HH_MM_SS);
	}

	public static String getRandomCurrentDate(String typeCode) {
		String dateStr = formatDate(new Date(), FORMAT_DATETIME_YYYYMMDDHHMMSS);
		long randomNum = Math.round(Math.random() * 900) + 100;

		return typeCode + dateStr + randomNum;
	}

	public static String add(String sourceDate, Integer days) {
		Calendar c = Calendar.getInstance();
		Date source = parse(sourceDate, FORMAT_DATE_YYYY_MM_DD);
		c.setTime(source);
		c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + days);
		return formatDate(c.getTime(), FORMAT_DATE_YYYY_MM_DD);
	}

	/** 判断目标日期是否在时间段内 */
	public static boolean between(Date start, Date end, Date target) {
		if (target.before(end) && target.after(start)) {
			return true;
		} else if (target.equals(start)) {
			return true;
		}
		return false;
	}

	/**
	 * 组装日期
	 * 
	 * @param date
	 * @param time
	 * @return
	 */
	public static Date assembleDate(Date date, String time) {
		if (date != null) {
			String dateStr = DateUtils.formatDate(date) + " " + time;
			date = DateUtils.parse(dateStr, FORMAT_DATETIME_YYYY_MM_DD_HH_MM);
		}
		return date;
	}

	public static Date add(Date date, int calendarField, int amount) {
		if (date == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(calendarField, amount);
		return c.getTime();
	}

	public static Date addDays(Date date, int amount) {
		return add(date, Calendar.DAY_OF_MONTH, amount);
	}

	public static Date addWeeks(Date date, int amount) {
		return add(date, Calendar.WEEK_OF_YEAR, amount);
	}

	public static Date parseObject(String date) {
		return DateUtils.parse(getDateNum(date), DateUtils.FORMAT_DATE_YYYYMMDD);
	}

	/** 提取数字并格式化为YYYYMMDD **/
	public static String formatObject(String date) {
		return DateUtils.formatObject(date, DateUtils.FORMAT_DATE_YYYYMMDD, DateUtils.FORMAT_DATE_YYYYMMDD);
	}

	public static Date parseObject(String date, String format) {
		return DateUtils.parse(getDateNum(date), format);
	}

	public static String formatObject(String date, String format, String formatResult) {
		return DateUtils.formatDate(parseObject(date, format), formatResult);
	}

	/** 获取所有数字 **/
	public static String getDateNum(String date) {
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(date);
		date = m.replaceAll("").trim();
		return date;
	}

	/**
	 * 字符日期格式转换
	 * 
	 * @param str        "09/14/2014"
	 * @param strFormat  "MM/dd/yyyy"
	 * @param dateFormat "yyyy-MM-dd"
	 * @return
	 */
	public static String strFormatDate(String str, String strFormat, String dateFormat) {
		try {
			SimpleDateFormat strFormatDate = new SimpleDateFormat(strFormat);
			SimpleDateFormat dateFormatstr = new SimpleDateFormat(dateFormat);
			Date date = strFormatDate.parse(str);
			return dateFormatstr.format(date);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "";
		}
	}

	/** 获得两个日期之间周六周日的总数量 */
	public static int getSundayCount(Date start, Date end) {
		int count = 0;
		Calendar calendarTemp = Calendar.getInstance();
		calendarTemp.setTime(start);
		int startWeek = calendarTemp.get(Calendar.DAY_OF_WEEK) - 1;
		if (startWeek == 6)
			calendarTemp.add(Calendar.DAY_OF_YEAR, 1);
		if (startWeek == 0)
			calendarTemp.add(Calendar.DAY_OF_YEAR, 2);
		while (calendarTemp.getTime().getTime() < end.getTime()) {
			int i = calendarTemp.get(Calendar.DAY_OF_WEEK);
			int value = i - 1;// 0-星期日
			if (value == 6 || value == 0) {// 周六，周日
				count++;
			}
			calendarTemp.add(Calendar.DAY_OF_YEAR, 1);
		}
		return count;
	}

	/** 获得两个日期之间周六周日的总毫秒 */
	public static long getSundayMillis(Date start, Date end) {
		long nd = 1000 * 24 * 60 * 60;
		int count = getSundayCount(start, end);
		return count * nd;
	}

	/** 获得两个日期之间工作日的总毫秒 */
	public static long getWorkMillis(Date start, Date end) {
		return end.getTime() - start.getTime() - getSundayMillis(start, end);
	}

	/** 毫秒转天数带1位小数 返回格式 0.0 */
	public static Double getDayHour(long diff) {
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long day = diff / nd; // 计算差多少天
		long hour = diff % nd / nh; // 计算差多少小时
		if (day > 0 || hour > 0) {
			return fomartDay(day, hour);
		}
		return 0d;
	}

	/** 获得两个日期之间的天数带1位小数 返回格式 0.0 */
	public static Double getWorkDayHour(Date start, Date end) {
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long diff = end.getTime() - start.getTime() - getSundayMillis(start, end); // 获得两个时间的毫秒时间差异
		long day = diff / nd; // 计算差多少天
		long hour = diff % nd / nh; // 计算差多少小时
		if (day > 0 || hour > 0) {
			return fomartDay(day, hour);
		}
		return null;
	}

	/** 获得两个日期之间的天数 返回格式 0 */
	public static Integer getWorkDay(Date start, Date end) {
		long nd = 1000 * 24 * 60 * 60;
		long diff = end.getTime() - start.getTime() - getSundayMillis(start, end); // 获得两个时间的毫秒时间差异
		long day = diff / nd; // 计算差多少天
		if (day > 0) {
			return (int) day;
		}
		return 0;
	}

	/** 获得两个日期之间的差值 返回格式 0天0小时0分钟0秒 */
	public static String getDayFull(Date start, Date end) {
		StringBuffer strBuf = new StringBuffer();
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		long ns = 1000;
		long diff = end.getTime() - start.getTime(); // 获得两个时间的毫秒时间差异
		long day = diff / nd; // 计算差多少天
		long hour = diff % nd / nh; // 计算差多少小时
		long min = diff % nd % nh / nm; // 计算差多少分钟
		long sec = diff % nd % nh % nm / ns;// 计算差多少秒//输出结果 //
		if (day > 0) {
			strBuf.append(day + "天");// 天
		}
		if (hour > 0) {
			strBuf.append(hour + "时");// 时
		}
		if (min > 0) {
			strBuf.append(min + "分");// 分
		}
		if (sec > 0) {
			strBuf.append(sec + "秒");// 秒
		}
		return strBuf.toString();
	}

	/** 毫秒MAP转天数带小时MAP */
	public static void copy(Map<String, Double> target, Map<String, Long> source) {
		for (String key : source.keySet()) {
			target.put(key, getDayHour(source.get(key)));
		}
	}

	/** 天，小时合成天 返回格式0.0 */
	public static Double fomartDay(long day, long hour) {
		Double value = Double.valueOf(day);
		if (hour > 0) {
			double valueDay = new BigDecimal(Double.valueOf(hour) / 24).setScale(1, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
			if (valueDay > 0 && valueDay < 1) {
				value += valueDay;
			} else if (valueDay >= 1) {
				String days = valueDay + "";
				value += Integer.valueOf(days.substring(0, days.indexOf(".")));
				value += Integer.valueOf(days.substring(days.indexOf(".") + 1));
			}
		}
		return value;
	}

	/**
	 * 返回两个日期之间的差
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static long dateDiff(Date start, Date end) {
		long nd = 1000 * 24 * 60 * 60;
		long diff = end.getTime() - start.getTime(); // 获得两个时间的毫秒时间差异
		return diff / nd; // 计算差多少天
	}

	public static Integer monthDiff(Date date) {
		Integer month = 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Calendar cal2 = Calendar.getInstance();
		cal2.set(Calendar.YEAR, cal2.get(Calendar.YEAR) - 1);
		cal2.set(Calendar.MONTH, 0);
		cal2.set(Calendar.DAY_OF_MONTH, 1);
		month = cal.get(Calendar.YEAR) * 12 + cal.get(Calendar.MONTH) - cal2.get(Calendar.YEAR) * 12
				- cal2.get(Calendar.MONTH);
		return month;
	}

	public static Date getLaterDate(Date icaDate, Date pcaDate) {
		if (icaDate == null) {
			return pcaDate;
		}
		if (pcaDate == null) {
			return icaDate;
		}

		return icaDate.after(pcaDate) ? icaDate : pcaDate;
	}

	/**
	 * @Title:getBeginDayOfWeek @Description:获取本周的开始时间 @param @return @return
	 *                          Date @throws
	 */
	public static Date getBeginDayOfWeek() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayofweek == 1) {
			dayofweek += 7;
		}
		cal.add(Calendar.DATE, 2 - dayofweek);
		return getDayStartTime(cal.getTime());
	}

	/**
	 * 
	 * @Title:getEndDayOfWeek @Description:获取本周的结束时间 @param @return @return
	 *                        Date @throws
	 */
	public static Date getEndDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getBeginDayOfWeek());
		cal.add(Calendar.DAY_OF_WEEK, 6);
		Date weekEndSta = cal.getTime();
		return getDayEndTime(weekEndSta);
	}

	/**
	 * 
	 * @Title:getDayStartTime @Description:获取某个日期的开始时间 @param @param
	 *                        d @param @return @return Timestamp @throws
	 */
	public static Timestamp getDayStartTime(Date d) {
		Calendar calendar = Calendar.getInstance();
		if (null != d)
			calendar.setTime(d);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0,
				0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new Timestamp(calendar.getTimeInMillis());
	}

	/**
	 * 
	 * @Title:getDayEndTime @Description:获取某个日期的结束时间 @param @param
	 *                      d @param @return @return Date @throws
	 */
	public static Date getDayEndTime(Date d) {
		Calendar calendar = Calendar.getInstance();
		if (null != d)
			calendar.setTime(d);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23,
				59, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

	/**
	 * 
	 * @Title:getPlusDay @Description:获取几天后的日期 @param @param
	 *                   afterDaysDate @param @return @return Date @throws
	 */
	public static Date getPlusDay(int afterDaysDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + afterDaysDate);// 让日期加传入参数
		return calendar.getTime();
	}

	public static Date getPlusDay(Date date, int afterDaysDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + afterDaysDate);// 让日期加传入参数
		return calendar.getTime();
	}

	/**
	 * @desc 返回具体日期是周几
	 * @param dateStr
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static String getWeekDayNameByDateStr(String dateStr, String pattern) throws ParseException {
		SimpleDateFormat formater = new SimpleDateFormat(pattern);
		Date date = formater.parse(dateStr);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String weekDayName = null;
		int num = calendar.get(Calendar.DAY_OF_WEEK);
		switch (num) {
		case 1:
			weekDayName = "SUN";
			break;
		case 2:
			weekDayName = "MON";
			break;
		case 3:
			weekDayName = "TUE";
			break;
		case 4:
			weekDayName = "WED";
			break;
		case 5:
			weekDayName = "THUR";
			break;
		case 6:
			weekDayName = "FRI";
			break;
		case 7:
			weekDayName = "SAT";
			break;
		}
		return weekDayName;
	}

	public static boolean checkDate(String dateVal) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			sdf.setLenient(false);
			Date d = sdf.parse(dateVal);
			String s = sdf.format(d);
			return dateVal.equals(s);
		} catch (Exception e) {
			return false;
		}
	}

	public static long getDuration(Date date, String beaginTime, String finishTime) {
		long duration = 0;
		if (null != date) {
			String newDate = formatDate(date);
			if (StringUtils.isNotBlank(beaginTime) && StringUtils.isNotBlank(finishTime)) {
				try {
					long begin = parseDateTime(newDate + " " + beaginTime).getTime();
					long finish = parseDateTime(newDate + " " + finishTime).getTime();
					if (finish < begin) {
						finish = parseDateTime(formatDate(getPlusDay(date, 1)) + " " + finishTime).getTime();
					}
					duration = finish - begin;
				} catch (Exception e) {
				}
			}
		}
		return duration;
	}

	public static String msTos(long value) {
		if (value > 0) {
			return String.valueOf(value / 1000.0);
		}
		return "0";
	}
	
	public static Integer msTosInt(long value) {
		if (value > 0) {
			return (int) (value / 1000);
		}
		return 0;
	}

	public static int getMonthLastDay(Date date) {
		Calendar a = Calendar.getInstance();
		a.setTime(date);
		a.set(Calendar.DATE, 1); // 把日期设置为当月第一天
		a.roll(Calendar.DATE, -1); // 日期回滚一天，也就是最后一天
		int MaxDate = a.get(Calendar.DATE);
		return MaxDate;
	}

	/** 获得两个日期之间的差值 返回格式 0天0小时0分钟0秒 */
	public static String getDayFull(long mss) {
		StringBuffer strBuf = new StringBuffer();
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		long ns = 1000;
		long diff = mss; // 获得两个时间的毫秒时间差异
		long day = diff / nd; // 计算差多少天
		long hour = diff % nd / nh; // 计算差多少小时
		long min = diff % nd % nh / nm; // 计算差多少分钟
		long sec = diff % nd % nh % nm / ns;// 计算差多少秒//输出结果 //
		if (day > 0) {
			strBuf.append(day + "天");// 天
		}
		if (hour > 0) {
			strBuf.append(hour + "时");// 时
		}
		if (min > 0) {
			strBuf.append(min + "分");// 分
		}
		if (sec > 0) {
			strBuf.append(sec + "秒");// 秒
		}
		if (day == 0 && hour == 0 && min == 0 && sec == 0) {
			strBuf.append(0 + "秒");// 秒
		}
		return strBuf.toString();
	}

	/**
	 * 获得指定日期的前一天
	 * 
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static Date getSpecifiedDayBefore(Date date) {
		if (null != date) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int day = c.get(Calendar.DATE);
			c.set(Calendar.DATE, day - 1);
			return parseDate(formatDate((c.getTime())));
		}
		return null;
	}

	/**
	 * 获得指定日期的后一天
	 * 
	 * @param specifiedDay
	 * @return
	 */
	public static Date getSpecifiedDayAfter(Date date) {
		if (null != date) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int day = c.get(Calendar.DATE);
			c.set(Calendar.DATE, day + 1);
			return parseDate(formatDate((c.getTime())));
		}
		return null;
	}

	/**
	 * 获取日期的前一天
	 * 
	 * @param dateStr
	 * @return
	 */
	public static String getOnTheDateByString(String dateStr) {
		Calendar c = Calendar.getInstance();
		c.setTime(parse(dateStr, FORMAT_DATE_DEFAULT));
		c.add(Calendar.DATE, -1);
		return formatDate(c.getTime());
	}

	public static String getNowDateByString() {
		return formatDate(new Date(), FORMAT_DATE_DEFAULT);
	}

	public static String formatObject2(String date, String format, String formatResult) {
		return formatDate(parse(date, format), formatResult);
	}

	/****
	 * 传入具体日期 ，返回具体日期增加一个月。
	 * 
	 * @param date 日期(2018-08-07)
	 * @author yajun_ren
	 * @throws ParseException
	 * @return Date
	 */
	public static Date subMonth(Date date) throws ParseException {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.add(Calendar.MONTH, 1);
		return rightNow.getTime();
	}

	/****
	 * 传入具体日期 ，返回具体日期增加一个月。
	 * 
	 * @param date 日期(2018-08-07)
	 * @author yajun_ren
	 * @throws ParseException
	 *  * @return String
	 */
    private  String subMonth(String date) throws ParseException {  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        Date dt = sdf.parse(date);  
        Calendar rightNow = Calendar.getInstance();  
        rightNow.setTime(dt);  
        rightNow.add(Calendar.MONTH, 1);  
        Date dt1 = rightNow.getTime();  
        String reStr = sdf.format(dt1);  
        return reStr;  
    }

	
	/**
	 * 传入具体数字，返回几天后的日期
	 * 
	 * @param date 日期(2018-08-07)
	 * @author yajun_ren
	 */
	public static Date getDate(int time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtils.parseDate(DateUtils.formatDate(new Date())));
		calendar.add(Calendar.DATE, time);
		return calendar.getTime();
	}
	
	/**
     * 获取指定年月的最后一天
     * @author yajun_ren
     * @param year
     * @param month
     * @return dateList
     */
     public static List<String> getLastDayOfMonth1(int year, int month,String formatCnf) {     
    	 List<String> dateList = new ArrayList<String>();
         Calendar cal = Calendar.getInstance();     
         //设置年份  
         cal.set(Calendar.YEAR, year);  
         //设置月份  
         cal.set(Calendar.MONTH, month-1); 
         //获取某月最大天数
         int lastDay = cal.getActualMaximum(Calendar.DATE);
         int startDay=1;
         while (startDay<=lastDay) {
        	 //设置日历中月份当天几号 
             cal.set(Calendar.DAY_OF_MONTH, startDay);  
           //格式化日期
             SimpleDateFormat sdf = new SimpleDateFormat(StringUtils.isNotBlank(formatCnf)?formatCnf:"yyyy-MM-dd");  
             dateList.add(sdf.format(cal.getTime()));
             startDay++;
		}
        return dateList;
     }
     
     public static List<String> getAllMonth(){
    	 List<String> months = new ArrayList<String>();
    	 for(int i=1;i<=12;i++) {
    		 months.add(i+"");
    	 }
    	 return months;
     }
}
