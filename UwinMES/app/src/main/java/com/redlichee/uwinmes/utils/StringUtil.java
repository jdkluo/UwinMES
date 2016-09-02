package com.redlichee.uwinmes.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 密码内容操作
 * @author Crazy24k@gmail.com
 * 
 */
public class StringUtil {
	
	private final String[] ChineseInterpunction = { "。", "，", "；", "：", "？", "！",
			"……", "—", "～", "〔", "〕", "《", "》", "‘", "’", "“", "”" };
	
	private final String[]   EnglishInterpunction = {".", ",", ";", ":", "?", "!", "…", "-", "~", "(", ")", "<", ">", "'", "'","\"","\""}; 
	/**
	 * 去除特殊字符或将所有中文标号替换为英文标号
	 * 
	 * @param str
	 * @return
	 */
	public static String stringFilter(String str) {
		str = str.replaceAll("【", "[").replaceAll("】", "]")
				.replaceAll("！", "!").replaceAll("：", ":")
				.replaceAll("（", "( ").replaceAll("）", " )");// 替换中文标号
		String regEx = "[『』]"; // 清除掉特殊字符
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}
	/**
	 * 去除特殊字符
	 * 
	 * @param str
	 * @return
	 */
	public static String stringRegEx(String str) {
		String regEx = "[//[//]]"; // 清除掉特殊字符
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}
	/**
	 * 是否不为空
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNotEmpty(String s) {
		return s != null && !"".equals(s.trim());
	}
	
	public static String stringSpecial(String str) {
		str = str.replaceAll("<br>", "\n").replace("&lt;br&gt;", "\n");// 替换中文标号
		String regEx = "『』"; // 清除掉特殊字符
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	/**
	 * 是否为空
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		return s == null || "".equals(s.trim());
	}

	/**
	 * 通过{n},格式化.
	 * 
	 * @param src
	 * @param objects
	 * @return
	 */
	public static String format(String src, Object... objects) {
		int k = 0;
		for (Object obj : objects) {
			src = src.replace("{" + k + "}", obj.toString());
			k++;
		}
		return src;
	}
	
	/**
	 * 过滤空字符
	 * 
	 * @param str
	 * @return
	 */
	public static String filterEmptyStr(String str) {
		if ("null".trim().equals(str) || TextUtils.isEmpty(str)) {
			return "";
		}
		return str;
	}
	
	/**
	 * 对比两字符串是否相同
	 * @param str
	 * @param str2
	 * @param defalut 
	 * @return
	 */
	public static boolean getStrToBoolean(String str, String str2, boolean defalut){			
		try {
			if(str.equals(str2)){
				return false;
			}else{
				return true;
			}
		} catch (Exception e) {		
			LogUtils.e("String", e.toString());
		}
		return defalut;
	}
	
	/**
	 * 分割字符串
	 * @param str 字符串
	 * @param regularExpression 分割符
	 * @param limit  最多分割出limit个字符串
	 * @return String[]
	 */
	public static String[] splitsStr(String str, String regularExpression, int limit) {
//		if (TextUtils.isEmpty(str) || str.contains(regularExpression)) {
//			return null;
//		}
		if (TextUtils.isEmpty(str)) {
			return null;
		}

	    String[] strArray = str.split(regularExpression, limit);
	    
		return strArray;
	}
	
	/**
	 * 字符串转char[]
	 * @param str	字符串
	 * @return
	 */
	public static char[] strToChars(String str) {
		
		char[] StrChar=new char[str.length()];
		StrChar=str.toCharArray();
		
		return StrChar;
	}
	
	
	/**
	 * 根据"0"false/"1"true返回布尔值
	 * @param str
	 * @return
	 */
	public static boolean returnBoolean(String str){			
		try {
			if("1".equals(str)){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {		
			LogUtils.e("String", e.toString());
		}
		return false;
	}
	
	/**
	 * 小数点后保留X小数
	 * @param str 
	 * @param pattern  "0.00"
	 * @return
	 */
	public static String decimalFormat(String str, String pattern) {
		try {
			DecimalFormat df = new DecimalFormat(pattern);
			String strDf = df.format(Double.parseDouble(str));
			return strDf;
		} catch (Exception e) {
			LogUtils.e("String", e.toString());
			return "0.00";
		}
	}



}
