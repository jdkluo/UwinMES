package com.redlichee.uwinmes.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: DeviceUtil
 * @Description: TODO 设备的一些信息
 * @author luomw
 * @date 2015年8月27日 下午12:53:48
 */
public class DeviceUtil {
	
		WindowManager wm ;
		DisplayMetrics displayMetrics;
	
	public DeviceUtil(Context context) {
		wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		displayMetrics = new DisplayMetrics();
	}
	
	public int getScreenWidth() {
		return wm.getDefaultDisplay().getWidth();
	}
	
	public  int getScreenHeight() {
		return wm.getDefaultDisplay().getHeight();
	}

	/**
	 * 获取设备ID
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceInfo(Context context) {
		try {
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			String device_id = tm.getDeviceId();

			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

			String mac = wifi.getConnectionInfo().getMacAddress();

			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}

			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}

			return device_id;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @Title: resetProgram
	 * @Description: TODO 重启软件
	 * @param mContext
	 */
	public static void resetProgram(Context mContext) {
		Intent i = ((ContextWrapper) mContext)
				.getBaseContext()
				.getPackageManager()
				.getLaunchIntentForPackage(((ContextWrapper) mContext).getBaseContext().getPackageName());
		i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mContext.startActivity(i);
		((Activity) mContext).finish();
		System.exit(0);
	}

	
	/**
	 * @Title: getCurrentTime
	 * @Description: TODO 获取当前时间 yyyy_MM_dd_HH_mm_ss
	 *  @return 当前时间 
	 */
	public static String getCurrentTime() {
		return	new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
		.format(new Date(System.currentTimeMillis()));
	}
}
