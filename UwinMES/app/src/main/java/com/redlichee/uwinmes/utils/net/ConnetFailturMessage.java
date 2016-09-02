package com.redlichee.uwinmes.utils.net;

public class ConnetFailturMessage {
	public static String connetFailturMessage(String msg) {
		/**
		 * @功能 申请数据异常向用户提示信息
		 * @author wujh
		 * @date 2014.6.15
		 */
		if (msg.contains("ConnectTimeoutException") || msg.contains("SocketTimeoutException")) {
			return "网络不给力，请求超时！";
		} else if (msg.contains("500") || msg.contains("HttpHostConnectException") || msg.contains("Server Error")){
			return "服务器内部错误，我们将会尽快处理！";
		}else{
			return "网络错误，请稍后再试！";
		}

	}
}
