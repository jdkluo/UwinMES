package com.redlichee.uwinmes.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import com.redlichee.uwinmes.R;
import com.redlichee.uwinmes.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 日期时间选择控件 使用方法： private EditText inputDate;
 * //需要设置的日期时间文本编辑框 private String
 * initDateTime="2012年9月3日 14:44",
 * //初始日期时间值 在点击事件中使用：
 * inputDate.setOnClickListener(new OnClickListener() {
 * 
 * @Override public void onClick(View v) { DateTimePickDialogUtil
 *           dateTimePicKDialog=new
 *           DateTimePickDialogUtil(SinvestigateActivity.this,initDateTime);
 *           dateTimePicKDialog.dateTimePicKDialog(inputDate);
 * 
 *           } });
 * 
 * @author
 */
public class DatePickSeleteDialog implements OnDateChangedListener,
		OnTimeChangedListener {
	private DatePicker datePicker;
	private TimePicker timePicker;
	private AlertDialog ad;
	private String dateTime;
	private String initDateTime;
	private Activity activity;
	private String datePattern = "yyyy-MM-dd";
	
	
	private DatepickClickListener clickListener;

	/**
	 * 日期时间弹出选择框构造函数
	 * 
	 * @param activity   ：调用的父activity
	 * @param initDateTime 初始日期时间值，作为弹出窗口的标题和日期时间初始值
	 */
	public DatePickSeleteDialog(Activity activity, String initDateTime) {
		this.activity = activity;
		this.initDateTime = initDateTime;

	}
	/**
	 * 日期时间弹出选择框构造函数
	 * 
	 * @param activity ：调用的父activity
	 * @param initDateTime  初始日期时间值，作为弹出窗口的标题和日期时间初始值
	 */
	public DatePickSeleteDialog(Activity activity, String initDateTime, DatepickClickListener mClickListener) {
		this.activity = activity;
		this.initDateTime = initDateTime;
		this.clickListener = mClickListener;

	}

	public void init(DatePicker datePicker) {
		Calendar calendar = Calendar.getInstance();
		if (!(null == initDateTime || "".equals(initDateTime))) {
			calendar = this.getCalendarByInintData(initDateTime);
		} else {
			initDateTime = calendar.get(Calendar.YEAR) + "年"
					+ calendar.get(Calendar.MONTH) + "月"
					+ calendar.get(Calendar.DAY_OF_MONTH) + "日 "
					+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
					+ calendar.get(Calendar.MINUTE);
		}

		datePicker.init(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), this);
	}

	/**
	 * EditText弹出日期时间选择框方法
	 * 
	 * @param inputDate :为需要设置的日期时间文本编辑框
	 * @return
	 */
	public AlertDialog dateTimePicKDialog(final EditText inputDate) {
		LinearLayout dateTimeLayout = (LinearLayout) activity
				.getLayoutInflater().inflate(R.layout.dailog_common_datetime, null);
		datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
		timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.timepicker);
		timePicker.setVisibility(View.GONE);
		init(datePicker);
//		timePicker.setIs24HourView(true);
//		timePicker.setOnTimeChangedListener(this);

		ad = new AlertDialog.Builder(activity)
				.setTitle(initDateTime)
				.setView(dateTimeLayout)
				.setPositiveButton("设置", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						int n = DateUtils.compare_date(dateTime, DateUtils.getSystime("yyyy-MM-dd"), "yyyy-MM-dd");
						if(n==1){
							Toast.makeText(activity, "时间不能大于当前时间", Toast.LENGTH_LONG).show();
						}else{
							inputDate.setText(dateTime);
						}
					}
				}).show();
//				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//						inputDate.setText("");
//					}
//				}).show();

		onDateChanged(null, 0, 0, 0);
		return ad;
	}
	/**
	 * TextView弹出日期时间选择框方法
	 * 
	 * @param inputDate:为需要设置的日期时间文本编辑框
	 * @return
	 */
	public AlertDialog dateTimePicKTextViewDialog(final TextView inputDate, final String datePattern) {
		this.datePattern = datePattern;
		LinearLayout dateTimeLayout = (LinearLayout) activity
				.getLayoutInflater().inflate(R.layout.dailog_common_datetime, null);
		datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
		timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.timepicker);
		timePicker.setVisibility(View.GONE);
		init(datePicker);
		
		ad = new AlertDialog.Builder(activity)
		.setTitle(initDateTime)
		.setView(dateTimeLayout)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				int n = DateUtils.compare_date(dateTime, DateUtils.getSystime(datePattern), "yyyy-MM-dd");
				if(n==1){
					Toast.makeText(activity, "时间不能大于当前时间", Toast.LENGTH_LONG).show();
				}else{
					clickListener.yesClick(dateTime);
					inputDate.setText(dateTime);
				}
			}
		}).show();
		
		onDateChanged(null, 0, 0, 0);
		return ad;
	}

	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		onDateChanged(null, 0, 0, 0);
	}

	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// 获得日历实例
		Calendar calendar = Calendar.getInstance();

		calendar.set(datePicker.getYear(), datePicker.getMonth(),
				datePicker.getDayOfMonth());
		SimpleDateFormat sdf = new SimpleDateFormat(this.datePattern);

		dateTime = sdf.format(calendar.getTime());
		ad.setTitle(dateTime);
	}

	/**
	 * 实现将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒,并赋值给calendar
	 * 
	 * @param initDateTime
	 *            初始日期时间值 字符串型
	 * @return Calendar
	 */
	private Calendar getCalendarByInintData(String initDateTime) {
		Calendar calendar = Calendar.getInstance();

		// 将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒
		String date = spliteString(initDateTime, "日", "index", "front"); // 日期
//		String time = spliteString(initDateTime, "日", "index", "back"); // 时间

		String yearStr = spliteString(date, "年", "index", "front"); // 年份
		String monthAndDay = spliteString(date, "年", "index", "back"); // 月日

		String monthStr = spliteString(monthAndDay, "月", "index", "front"); // 月
		String dayStr = spliteString(monthAndDay, "月", "index", "back"); // 日

//		String hourStr = spliteString(time, ":", "index", "front"); // 时
//		String minuteStr = spliteString(time, ":", "index", "back"); // 分

		int currentYear = Integer.valueOf(yearStr.trim()).intValue();
		int currentMonth = Integer.valueOf(monthStr.trim()).intValue() - 1;
		int currentDay = Integer.valueOf(dayStr.trim()).intValue();
//		int currentHour = Integer.valueOf(hourStr.trim()).intValue();
//		int currentMinute = Integer.valueOf(minuteStr.trim()).intValue();

		calendar.set(currentYear, currentMonth, currentDay);
		return calendar;
	}

	/**
	 * 截取子串
	 * 
	 * @param srcStr
	 *            源串
	 * @param pattern
	 *            匹配模式
	 * @param indexOrLast
	 * @param frontOrBack
	 * @return
	 */
	public static String spliteString(String srcStr, String pattern,
			String indexOrLast, String frontOrBack) {
		String result = "";
		int loc = -1;
		if (indexOrLast.equalsIgnoreCase("index")) {
			loc = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置
		} else {
			loc = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置
		}
		if (frontOrBack.equalsIgnoreCase("front")) {
			if (loc != -1)
				result = srcStr.substring(0, loc); // 截取子串
		} else {
			if (loc != -1)
				result = srcStr.substring(loc + 1, srcStr.length()); // 截取子串
		}
		return result;
	}
	

	public interface DatepickClickListener {
		/**
		 * @param comStr 完整字符
		 */
		void yesClick(String comStr);

		void noClick();

	}

}
