package com.redlichee.uwinmes.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.redlichee.uwinmes.application.Config;

import java.util.Map;
import java.util.Set;

/**
 * SharedPreferences工具类
 * 
 * @author LMW
 * 
 */
public class SharedPreUtil {

	private SharedPreferences sp;
	private Editor editor;
	private String name = Config.PLIS_NAME;
	private int mode = Context.MODE_PRIVATE;

	public SharedPreUtil(Context context) {
		this.sp = context.getSharedPreferences(name, mode);
		this.editor = sp.edit();
	}

	/**
	 * 创建一个工具类，默认打开名字为name的SharedPreferences实例
	 * 
	 * @param context
	 * @param name 唯一标识
	 * @param mode 权限标识
	 */
	public SharedPreUtil(Context context, String name, int mode) {
		this.sp = context.getSharedPreferences(name, mode);
		this.editor = sp.edit();
	}

	/**
	 * 添加信息到SharedPreferences
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void add(Map<String, String> map) {
		Set<String> set = map.keySet();
		for (String key : set) {
			editor.putString(key, map.get(key));
		}
		editor.commit();
	}

	/**
	 * 添加信息到SharedPreferences
	 *
	 * @throws Exception
	 */
	public void put(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 删除信息
	 * 
	 * @throws Exception
	 */
	public void deleteAll() throws Exception {
		editor.clear();
		editor.commit();
	}

	/**
	 * 删除一条信息
	 */
	public void delete(String key) {
		editor.remove(key);
		editor.commit();
	}

	/**
	 * 获取信息
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public String get(String key) {
		if (sp != null) {
			return sp.getString(key, "");
		}
		return "";
	}

	/**
	 * 获取信息
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public String get(String key, String def) {
		if (sp != null) {
			return sp.getString(key, "");
		}
		return def;
	}

	/**
	 * 获取此SharedPreferences的Editor实例
	 * 
	 * @return
	 */
	public Editor getEditor() {
		return editor;
	}
	
	public String[] getSharedPreference(Context context, String key) {
		String regularEx = "#";
		String[] str = null;
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		String values;
		values = sp.getString(key, "");
		str = values.split(regularEx);

		return str;
	}

	public void setSharedPreference(Context context, String key, String[] values) {
		String regularEx = "#";
		String str = "";
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		if (values != null && values.length > 0) {
			for (String value : values) {
				str += value;
				str += regularEx;
			}
			Editor et = sp.edit();
			et.putString(key, str);
			et.commit();
		}
	}
}
