package com.redlichee.uwinmes.utils;

import android.text.TextUtils;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

	public RegexUtil() {

	}

	/**
	 * 字符串截取
	 */
	public static String getUrlParese(String key, String Url) {

		Pattern pat = Pattern.compile("(^|&|\\:)+" +key + "=+([^&]*)(&|$)");
		Matcher mat = pat.matcher(Url);
		String res = "";
		if (mat.find()) {
			for (int i = 0; i < mat.groupCount(); i++) {
				res = mat.group(i);
				Log.i("regex", res);
			}
			
		}

		return res;
	}

	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
		String telRegex = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles)) return false;
		else return mobiles.matches(telRegex);
	}
}
