package com.redlichee.uwinmes.utils;

import android.annotation.SuppressLint;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat")
public class DateUtils {

	// "created_at": "Wed Jun 17 14:26:24 +0800 2015"

	public static final long ONE_MINUTE_MILLIONS = 60 * 1000;
	public static final long ONE_HOUR_MILLIONS = 60 * ONE_MINUTE_MILLIONS;
	public static final long ONE_DAY_MILLIONS = 24 * ONE_HOUR_MILLIONS;

	public static String getShortTime(String dateStr) {
		String str = "";

		SimpleDateFormat sdf = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
		try {
			Date date = sdf.parse(dateStr);
			Date curDate = new Date();

			long durTime = curDate.getTime() - date.getTime();
			int dayStatus = calculateDayStatus(date, curDate);

			if (durTime <= 10 * ONE_MINUTE_MILLIONS) {
				str = "刚刚";
			} else if (durTime < ONE_HOUR_MILLIONS) {
				str = durTime / ONE_MINUTE_MILLIONS + "分钟前";
			} else if (dayStatus == 0) {
				str = durTime / ONE_HOUR_MILLIONS + "小时前";
			} else if (dayStatus == -1) {
				str = "昨天" + DateFormat.format("HH:mm", date);
			} else if (isSameYear(date, curDate) && dayStatus < -1) {
				str = DateFormat.format("MM-dd", date).toString();
			} else {
				str = DateFormat.format("yyyy-MM", date).toString();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return str;
	}

	public static boolean isSameYear(Date targetTime, Date compareTime) {
		Calendar tarCalendar = Calendar.getInstance();
		tarCalendar.setTime(targetTime);
		int tarYear = tarCalendar.get(Calendar.YEAR);

		Calendar compareCalendar = Calendar.getInstance();
		compareCalendar.setTime(compareTime);
		int comYear = compareCalendar.get(Calendar.YEAR);

		return tarYear == comYear;
	}

	public static int calculateDayStatus(Date targetTime, Date compareTime) {
		Calendar tarCalendar = Calendar.getInstance();
		tarCalendar.setTime(targetTime);
		int tarDayOfYear = tarCalendar.get(Calendar.DAY_OF_YEAR);

		Calendar compareCalendar = Calendar.getInstance();
		compareCalendar.setTime(compareTime);
		int comDayOfYear = compareCalendar.get(Calendar.DAY_OF_YEAR);

		return tarDayOfYear - comDayOfYear;
	}

	/**
	 * 获取系统时间
	 * @pattern 输出时间格式
	 * @return	输出字符串时间
	 */
	public static String getSystime(String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	/**
	 * 根据时间获取几分钟后的时间
	 * 
	 * @param date
	 * @param minus
	 * @return
	 */
	public static String getPeriodDate(Date date, int minus, String Datetype) {
		int endDay;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		endDay = calendar.get(Calendar.MINUTE) + minus;
		calendar.set(Calendar.MINUTE, endDay);
		Date d = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat(Datetype);
		// Log.e("当前时间", string);
		return formatter.format(d);

	}

	/**
	 * 根据时间获取几分钟后的时间
	 *
	 * @param minus
	 * @return
	 */
	public static String getPeriodString(String strDate, int minus, String Datetype) {
		int endDay;
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm");//"yyyy-MM-dd"
		Calendar calendar = Calendar.getInstance();

		Date date = null;
		try {

			date = sdf.parse(strDate);
			calendar.setTime(date);

		} catch (ParseException e) {
			e.printStackTrace();
			calendar = Calendar.getInstance();//报错时，返回当前时间
		}

		endDay = calendar.get(Calendar.MINUTE) + minus;
		calendar.set(Calendar.MINUTE, endDay);
		Date d = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat(Datetype);
		// Log.e("当前时间", string);
		return formatter.format(d);

	}

	/*
	 * 获取当前时间
	 */
	public static String getDate() {
		SimpleDateFormat myFormatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		return myFormatter.format(calendar.getTime());
	}

	/**
	 * 根据格式获取当前时间
	 * 
	 * @param fm
	 * @return
	 */
	public static String getCurDate(String fm) {
		SimpleDateFormat myFormatter = new SimpleDateFormat(fm);
		Calendar calendar = Calendar.getInstance();
		return myFormatter.format(calendar.getTime());
	}

	/*
	 * 获取当前时间 yyyy-MM-dd HH:mm:ss
	 */
	public static String getCrDate() {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		return myFormatter.format(calendar.getTime());
	}

	/*
	 * 获取当前时间 yyy-MM-dd
	 */
	public static String getCrDateYMD() {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		return myFormatter.format(calendar.getTime());
	}

	/*
	 * 获取当前时间 Long格式
	 */
	public static long getCrDateLong() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getTimeInMillis();
	}

	/**
	 * 转换字符串
	 * 
	 * @return"yyyy-MM-dd HH:mm:ss"
	 */
	public static String dataToString(Date date, String resStyle) {
		SimpleDateFormat myFormatter = new SimpleDateFormat(resStyle);
		// Calendar calendar = Calendar.getInstance();
		return myFormatter.format(date);
	}

	/**
	 * 获取当前一周的第一天
	 * 
	 * @return
	 */
	public static String getWeekdata() {

		Calendar currentDate = Calendar.getInstance(Locale.CHINA);
		currentDate.setFirstDayOfWeek(Calendar.MONDAY);
		currentDate.setTimeInMillis(System.currentTimeMillis());
		// currentDate.set(Calendar.HOUR_OF_DAY, 0);
		// currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return dataToString(currentDate.getTime(), "yyyy-MM-dd");
	}

	/**
	 * 计算两个日期型的时间相差多少时间
	 * 
	 * @param startDate  开始日期
	 * @param endDate  结束日期
	 * @return 时间的长度
	 */
	public static String dateDistance(Date startDate, Date endDate) {

		if (startDate == null || endDate == null) {
			return null;
		}
		long timeLong = endDate.getTime() - startDate.getTime();
		if (timeLong < 60 * 1000)
			return timeLong / 1000 + "秒前";
		else if (timeLong < 60 * 60 * 1000) {
			timeLong = timeLong / 1000 / 60;
			return timeLong + "分钟前";
		} else if (timeLong < 60 * 60 * 24 * 1000) {
			timeLong = timeLong / 60 / 60 / 1000;
			return timeLong + "小时前";
		} else if (timeLong < 60 * 60 * 24 * 1000 * 7) {
			timeLong = timeLong / 1000 / 60 / 60 / 24;
			return timeLong + "天前";
		} else if (timeLong < 60 * 60 * 24 * 1000 * 7 * 4) {
			timeLong = timeLong / 1000 / 60 / 60 / 24 / 7;
			return timeLong + "周前";
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
			return sdf.format(startDate);
		}
	}
	
	/**
	 * 计算两个日期型的时间相差多少时间
	 * 
	 * @param startDate  开始日期
	 * @param endDate  结束日期
	 * @return 时间的长度
	 */
	public static String twoDateDistance(long startDate, long endDate) {
		
		long between=(endDate - startDate)/1000;//除以1000是为了转换成秒
		
		long day1=between/(24*3600);//天
		long hour1=between%(24*3600)/3600;//时
		long minute1=between%3600/60;//分
		long second1=between%60/60;//秒
		
		if (between > 24*3600){
			return ""+day1+"天"+hour1+"时"+minute1+"分";
		}else if(between > 3600){
			return ""+hour1+"时"+minute1+"分";
		}else if(between > 60){
			return ""+minute1+"分";
		}
		
		return "0分";
		
	}
	
	/**
	 * 计算两个日期型的时间相差多少时间
	 * 
	 * @param startDate  开始日期
	 * @param endDate  结束日期
	 * @return 时间的长度
	 */
	public static String twoDateDistance(String startDate, String endDate, String fm) {

		if (startDate == null || endDate == null) {
			return "0分";
		}
		
		if ("".equals(startDate) || "".equals(endDate)) {
			return "0分";
		}
		
		Date start = StrToDate(startDate, fm);
		Date end = StrToDate(endDate, fm);
		long between=(end.getTime() - start.getTime())/1000;//除以1000是为了转换成秒
		
		long day1=between/(24*3600);//天
		long hour1=between%(24*3600)/3600;//时
		long minute1=between%3600/60;//分
		long second1=between%60/60;//秒
		
		if (between > 24*3600){
			return ""+day1+"天"+hour1+"时"+minute1+"分";
		}else if(between > 3600){
			return ""+hour1+"时"+minute1+"分";
		}else if(between > 60){
			return ""+minute1+"分";
		}
		
		return "0分";
		
	}

	/**
	 * 将长时间格式字符串转换为时间yyyy-MM-dd HH:mm E
	 * 
	 */
	public static String dateLongToStr(Long date, String dateformat) {
		SimpleDateFormat formatter = new SimpleDateFormat(dateformat);//"yyyy-MM-dd HH:mm E"
		String dateString = formatter.format(date);
		return dateString;
	}
	
	/**
	 * String 转化Long
	 * 
	 */
	public static Long dateStrToLong(String strDate, String dateformat) {
		
		SimpleDateFormat sdf= new SimpleDateFormat(dateformat);//"yyyy-MM-dd"
		Calendar calendar = Calendar.getInstance();
		Date date = null;
		
		try {
			
			date = sdf.parse(strDate);
			calendar.setTime(date);
			return calendar.getTimeInMillis();
			
		} catch (ParseException e) {
			e.printStackTrace();
			return calendar.getTimeInMillis();//报错时，返回当前时间
		}
		
	}
	
	/**
	 * 计算两个日期型的时间相差多少时间
	 * 
	 * @param startDate  开始日期
	 * @param endDate  结束日期
	 * @return bool 值
	 */
	public static Boolean compareBetweenDate(Date startDate, Date endDate) {

		if (startDate == null || endDate == null) {
			return false;
		}
		long timeLong = endDate.getTime() - startDate.getTime();
		if (timeLong < 0) {
			return false;
		}
		return true;
	}

	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 * @return date
	 */
	public static Date StrToDate(String str) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 * @return date
	 */
	public static Date StrToDate(String str,String fm) {

		SimpleDateFormat format = new SimpleDateFormat(fm);
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 字符串格式转换
	 * 
	 * @param strData
	 *            字符串日期
	 * @return dateformat 输出格式
	 */
	public static String DateStrToStr(String strData, String dateformat) {
		SimpleDateFormat format = new SimpleDateFormat(dateformat);
		String str = format.format(StrToDate(strData));
		return str;
	}

	/**
	 * 日期转换成Java字符串
	 * 
	 * @param date
	 * @return str
	 */
	public static String DateToStr(Date date) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String str = format.format(date);
		return str;
	}
	
	/**
	 * String 转化Calendar
	 * 
	 * @param strDate
	 * @return calendar
	 * @throws ParseException 
	 */
	public static Calendar StrToCal(String strDate, String pattern) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);//"yyyy-MM-dd HH:mm"

		Date date = sdf.parse(strDate);

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		return calendar;

	}
	
	/**
	 * Calendar 转化 String
	 * 
	 * @return calendar
	 * @throws ParseException 
	 */
	public static String CalToStr(Calendar cal, String pattern){

		 try {
			
			 SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			 
			 String dateStr = sdf.format(cal.getTime());
			 
			 return dateStr;
		} catch (Exception e) {
			return getCurDate(pattern);
		}
		
	}

	/**
	 * 字符串转换成日期
	 * 
	 * @param str
	 * @return date
	 */
	public static String StrDateFormat(String str) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		String strDate = null;
		java.text.DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = fmt.parse(str);
			strDate = format.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return strDate;
	}
	
	/**
	 * 字符串格式转换
	 * @param str 字符串
	 * @param pattern1 输出格式 例如："yyyy年MM月dd日"
	 * @param pattern2 输入格式 例如："yyyy-MM-dd"
	 * @return
	 */
	public static String StrFormat(String str, String pattern1, String pattern2) {
		
		SimpleDateFormat format = new SimpleDateFormat(pattern1);//输出格式"yyyy年MM月dd日"
		String strDate = null;
		java.text.DateFormat fmt = new SimpleDateFormat(pattern2);//输入格式"yyyy-MM-dd"
		Date date;
		try {
			date = fmt.parse(str);
			strDate = format.format(date);
			return strDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return getCurDate(pattern1);//若转化失败返回当天日期
	}
	
	/**
	 * 比较两时间大小
	 * @param DATE1	此时间大返回1，相等返回0
	 * @param DATE2	此时间大返回-1，相等返回0
	 * @return
	 */
	public static boolean compareDate(long DATE1, long DATE2) {
		
		try {
			if (DATE1 > DATE2) {
				return true;
			} else if (DATE1 < DATE2) {
				return false;
			} else {
				return false;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}

	/**
	 * 比较两时间大小
	 * @param DATE1	此时间大返回1，相等返回0
	 * @param DATE2	此时间大返回-1，相等返回0
	 */
	public static int compare_date(String DATE1, String DATE2, String pattern) {

		java.text.DateFormat df = new SimpleDateFormat(pattern);
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 比较时间大小
	 * date1	时间1
	 * date2	时间2
	 * pattern	时间格式
	 * return 1：date1>date2
	 * return -1:date1<date2
	 * return 0:date1=date2
	 * return -2:格式不正确
	 */
	public static int compareDate(String date1, String date2, String pattern) {
		
		java.text.DateFormat df = new SimpleDateFormat(pattern);//"yyyy-MM-dd"
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else if (dt1.getTime() == dt2.getTime()) {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return -2;

	}
	
    //早于截止时间
    public static boolean isDateBefore(String endTime, String datePattern){    
          try{
                Date date1 = new Date();
                java.text.DateFormat df = new SimpleDateFormat(datePattern);//"yyyy-MM-dd"
                return date1.before(df.parse(endTime)); 
          }catch(ParseException e){
              System.out.print(e.getMessage());
              return false;
        }
    }
    
    //晚于起始时间
    public static boolean isDateAfter(String startTime, String datePattern){
        try{
            Date date1 = new Date();
            java.text.DateFormat df = new SimpleDateFormat(datePattern);//"yyyy-MM-dd"
            return date1.after(df.parse(startTime)); 
        }catch(ParseException e){
            System.out.print(e.getMessage());
            return false;
        }
    }

	/**
	 * 得到两日期相差几个月
	 * 
	 * @return
	 */
	public static int getMonth(String startDate, String endDate,String datePattern) {
		SimpleDateFormat f = new SimpleDateFormat(datePattern);// "yyyy-MM-dd"
		int monthday;
		try {
			Date startDate1 = f.parse(startDate);
			// 开始时间与今天相比较
			Date endDate1 = f.parse(endDate);

			Calendar starCal = Calendar.getInstance();
			starCal.setTime(startDate1);

			int sYear = starCal.get(Calendar.YEAR);
			int sMonth = starCal.get(Calendar.MONTH);
			// int sDay = starCal.get(Calendar.DATE);

			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate1);
			int eYear = endCal.get(Calendar.YEAR);
			int eMonth = endCal.get(Calendar.MONTH);
			// int eDay = endCal.get(Calendar.DATE);

			monthday = ((eYear - sYear) * 12 + (eMonth - sMonth));

			// if (sDay < eDay) {
			// monthday = monthday + 1;
			// }
			return monthday;
		} catch (ParseException e) {
			LogUtils.d("getMonth", "获取相差月数失败");
			monthday = 0;
		}
		return monthday;
	}
	
	/** 
	*字符串的日期格式的计算 
	*/ 
	public static int daysBetween(String smdate, String bdate, String datePattern){
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
		try {
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(smdate));
			long time1 = cal.getTimeInMillis();
			cal.setTime(sdf.parse(bdate));
			long time2 = cal.getTimeInMillis();
			long between_days = (time2 - time1) / (1000 * 3600 * 24);
			
			return Integer.parseInt(String.valueOf(between_days));
		} catch (Exception e) {
			LogUtils.d("getMonth", "获取相差天数失败");
			return 0;
		}
	}

	/**
	 * 获取前一天的日期 
	 * @param datePattern
	 * @return
	 */
	public static String getDelayingDay(String date,String datePattern) {
		SimpleDateFormat f = new SimpleDateFormat(datePattern);// "yyyy-MM-dd"
		try {
			Date startDate1 = f.parse(date);
			
			Calendar starCal = Calendar.getInstance();
			starCal.setTime(startDate1);
			int sDay = starCal.get(Calendar.DATE);
			
			starCal.set(Calendar.DATE, sDay--);
			
			return f.format(starCal.getTime());
		} catch (ParseException e) {
			LogUtils.d("getDelayingDay", "获取前一天的日期失败");
		}
		return date;
	}
	/*
	 * 1.Calendar 转化 String
	
	Calendar calendat = Calendar.getInstance();
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	String dateStr = sdf.format(calendar.getTime());
	
	 
	
	2.String 转化Calendar
	
	String str="2012-5-27";
	
	SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
	
	Date date =sdf.parse(str);
	
	Calendar calendar = Calendar.getInstance();
	
	calendar.setTime(date);
	
	 
	
	3.Date 转化String
	
	SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
	
	String dateStr=sdf.format(new Date());
	
	 
	
	4.String 转化Date
	
	String str="2012-5-27";
	
	SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
	
	Date date= sdf.parse(str);
	
	 
	
	5.Date 转化Calendar
	
	Calendar calendar = Calendar.getInstance();
	
	calendar.setTime(new java.util.Date());
	
	 
	
	6.Calendar转化Date
	
	Calendar calendar = Calendar.getInstance();
	
	java.util.Date date =calendar.getTime();
	
	 
	
	7.String 转成 Timestamp
	
	Timestamp ts = Timestamp.valueOf("2012-1-14 08:11:00");
	
	 
	
	8.Date 转 TimeStamp
	
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	String time = df.format(new Date());
	
	Timestamp ts = Timestamp.valueOf(time);
	 */
	
/*	java计算时间差及比较时间大小
	比如：现在是2004-03-26 13：31：40
	过去是：2004-01-02 11：30：24
	我现在要获得两个日期差，差的形式为：XX天XX小时XX分XX秒

	方法一：
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	try
	{
	Date d1 = df.parse("2004-03-26 13:31:40");
	Date d2 = df.parse("2004-01-02 11:30:24");
	long diff = d1.getTime() - d2.getTime();
	long days = diff / (1000 * 60 * 60 * 24);
	}
	catch (Exception e)
	{
	}

	方法二： SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	java.util.Date now = df.parse("2004-03-26 13:31:40");
	java.util.Date date=df.parse("2004-01-02 11:30:24");
	long l=now.getTime()-date.getTime();
	long day=l/(24*60*60*1000);
	long hour=(l/(60*60*1000)-day*24);
	long min=((l/(60*1000))-day*24*60-hour*60);
	long s=(l/1000-day*24*60*60-hour*60*60-min*60);
	System.out.println(""+day+"天"+hour+"小时"+min+"分"+s+"秒");

	方法三：
	SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	java.util.Date begin=dfs.parse("2004-01-02 11:30:24");
	java.util.Date end = dfs.parse("2004-03-26 13:31:40");
	long between=(end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒

	long day1=between/(24*3600);
	long hour1=between%(24*3600)/3600;
	long minute1=between%3600/60;
	long second1=between%60/60;
	System.out.println(""+day1+"天"+hour1+"小时"+minute1+"分"+second1+"秒");


	====================================================

	java 比较时间大小 

	String s1="2008-01-25 09:12:09";
	String s2="2008-01-29 09:12:11";
	java.text.DateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	java.util.Calendar c1=java.util.Calendar.getInstance();
	java.util.Calendar c2=java.util.Calendar.getInstance();
	try
	{
	c1.setTime(df.parse(s1));
	c2.setTime(df.parse(s2));
	}catch(java.text.ParseException e){
	System.err.println("格式不正确");
	}
	int result=c1.compareTo(c2);
	if(result==0)
	System.out.println("c1相等c2");
	else if(result<0)
	System.out.println("c1小于c2");
	else
	System.out.println("c1大于c2");*/
	
	
}
