package com.redlichee.uwinmes.utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtils {
	public static String getJSONString(JSONObject o, String key) {
		try {
			if ("null".equals(o.getString(key)) || o.getString(key) == null){
				return "";
			}
			return o.getString(key);
		} catch (Exception e) {
			LogUtils.e("JsonUtils", e.toString());
		}
		return "";
	}

	public static JSONObject getJSONObject(JSONObject jo, String key) {
		try {
			return jo.getJSONObject(key);
		} catch (Exception e) {
			LogUtils.e("JsonUtils", e.toString());
		}
		return null;
	}

	public static JSONArray getJsonArray(JSONObject obj, String key) {
		try {
			return obj.getJSONArray(key);
		} catch (Exception e) {
			LogUtils.e("JsonUtils", e.toString());
		}
		return null;
	}
	/**
	 * 安全处理
	 * @param obj
	 * @param key
	 * @return
	 */
	public static JSONArray getSafeJsonArray(JSONObject obj, String key) {
		
		try {
			return obj.getJSONArray(key);
		} catch (Exception e) {
			LogUtils.e("JsonUtils", e.toString());
		}
		return new JSONArray();
	}

	public static boolean getJSONBoolean(JSONObject jo, String key,
			boolean defalut) {
		try {
			return jo.getBoolean(key);
		} catch (Exception e) {
			LogUtils.e("JsonUtils", e.toString());
		}
		return defalut;
	}

	public static int getJSONInt(JSONObject o, String key) {
		try {
			return o.getInt(key);
		} catch (Exception e) {
			LogUtils.e("JsonUtils", e.toString());
		}
		return 0;
	}
	
	public static Double getJSONDouble(JSONObject o, String key) {
		try {
			return o.optDouble(key);
		} catch (Exception e) {
			LogUtils.e("JsonUtils", e.toString());
		}
		return null;
	}

	public static boolean isJson(String text) {
		try {
			new JSONObject(text);
			return true;
		} catch (Exception e) {
			LogUtils.e("JsonUtils", e.toString());
		}
		return false;
	}

	public static JSONObject getJSONObject(String text) {
		try {
			return new JSONObject(text);
		} catch (Exception e) {
			LogUtils.e("JsonUtils", e.toString());
		}
		return null;
	}

}
