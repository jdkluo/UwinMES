package com.redlichee.uwinmes.utils.net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.redlichee.uwinmes.R;
import com.redlichee.uwinmes.application.Config;
import com.redlichee.uwinmes.utils.JsonUtils;
import com.redlichee.uwinmes.utils.LogUtils;
import com.redlichee.uwinmes.utils.NetWorkUtils;
import com.redlichee.uwinmes.utils.SharedPreUtil;
import com.redlichee.uwinmes.utils.view.ShowAlertView;
import com.redlichee.uwinmes.view.activity.LoginActivity;
import com.redlichee.uwinmes.widget.ProgressBarView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.HttpEntity;

public class HttpGetData {

	public static final int  TIMEOUT_VALUE = 15000;//超时时长
	public static final int  TIMEOUT_VALUE_UPLOAD = 20000;//上传文件超时时长

	private static AsyncHttpClient client = new AsyncHttpClient();

	/**
	 * 获取AsyncHttpClient对象
	 * @return
     */
	public AsyncHttpClient getAsyncHttpClient() {
		return client;
	}

	/**
	 * 取消网络请求
	 * @param context
	 * @param mayInterruptIfRunning
     */
	public void cancelRequests(Context context, boolean mayInterruptIfRunning) {
		client.cancelRequests(context, mayInterruptIfRunning);
	}

	/**
	 * 取消所有请求
	 * @param mayInterruptIfRunning
     */
	public void cancelAllRequests(boolean mayInterruptIfRunning) {
		client.cancelAllRequests(mayInterruptIfRunning);
	}

	public static void get(final Context mContext, String url,
			RequestParams params, String msg, final getDataCallBack callBack) {

		if (!NetWorkUtils.isNetWorkConnected(mContext)) {
			showToast(mContext, mContext.getString(R.string.networkalter));
			return;
		}
		//圆形进度条
		final ProgressBarView progressBar = new ProgressBarView(mContext);

		if (!msg.equals("")) {
			progressBar.showProgressBar(msg);
		}

		client.setTimeout(TIMEOUT_VALUE);
		client.get(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
				callBack.succcess(new String(responseBody));
			}

			@Override
			public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
				callBack.fail(ConnetFailturMessage.connetFailturMessage(error.toString()));
			}

			@Override
			public void onFinish() {
				super.onFinish();
				progressBar.dismissProgressBar();
			}
		});

	}

	/**
	 * 网络请求封装方法
	 *
	 * @param mContext
	 * @param url  接口地址
	 * @param params  参数
	 * @param callBack  回调
	 */
	// post方法进行网络请求
	public static void post(final Activity mContext, String url, RequestParams params, String msg, final getDataCallBack callBack) {
		if (mContext == null){ return; }

		//检查网络
		if (!NetWorkUtils.isNetWorkConnected(mContext)) {
			showToast(mContext, mContext.getString(R.string.networkalter));
//			callBack.fail(mContext.getString(R.string.networkalter));
			return;
		}
		//圆形进度条
		final ProgressBarView progressBar = new ProgressBarView(mContext);

		//msg不为空时显示圆形进度条
		if (!TextUtils.isEmpty(msg)) {
			progressBar.showProgressBar(msg);
		}

		params.put("tokenId",new SharedPreUtil(mContext).get(Config.TOKEN_ID));
		LogUtils.d("HttpGetData", getAbsoluteUrl(mContext, url) + " parsms:" + params);
		client.setTimeout(TIMEOUT_VALUE);

		client.post(getAbsoluteUrl(mContext, url), params,new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
				LogUtils.e("HttpGetData", new String(responseBody));

				JSONObject jObject;
				try {
					jObject = new JSONObject(new String(responseBody));
					int code = JsonUtils.getJSONInt(jObject, "code");

					if (code != 3) {
						callBack.succcess(new String(responseBody));
					} else {
						if (!mContext.isFinishing()) {
							showToast(mContext, "登录超时");
							SharedPreferences share = mContext.getSharedPreferences(Config.PLIS_NAME,Context.MODE_PRIVATE);
							share.edit().putString(Config.TOKEN_ID,"").commit();

							Intent login = new Intent(mContext,LoginActivity.class);
							login.putExtra("loginType", true);
							mContext.startActivity(login);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
				Log.e("error4", String.valueOf(statusCode) + "**Throwable--" + error.toString());
				//请求错误弹出对话框提示
				if (!mContext.isFinishing()) {
					callBack.fail(ConnetFailturMessage.connetFailturMessage(error.toString()));
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				//请求完成时dismiss进度条
				if (!mContext.isFinishing()) {
					progressBar.dismissProgressBar();
				}
			}
		});
	}


	/**
	 * 网络请求封装方法
	 *
	 * @param mContext
	 * @param url  接口地址
	 * @param params  参数
	 * @param callBack  回调
	 */
	// post方法进行网络请求
	public static void post(final Context mContext, String url, RequestParams params, String msg, final getDataCallBack callBack) {
		if (mContext == null){ return; }

		//检查网络
		if (!NetWorkUtils.isNetWorkConnected(mContext)) {
			showToast(mContext, mContext.getString(R.string.networkalter));
//			callBack.fail(mContext.getString(R.string.networkalter));
			return;
		}
		//圆形进度条
		final ProgressBarView progressBar = new ProgressBarView(mContext);

		//msg不为空时显示圆形进度条
		if (!TextUtils.isEmpty(msg)) {
			progressBar.showProgressBar(msg);
		}

		params.put("tokenId",new SharedPreUtil(mContext).get(Config.TOKEN_ID));
		LogUtils.d("HttpGetData", getAbsoluteUrl(mContext, url) + " parsms:" + params);
		client.setTimeout(TIMEOUT_VALUE);

		client.post(getAbsoluteUrl(mContext, url), params,new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
				LogUtils.e("HttpGetData", new String(responseBody));

				JSONObject jObject;
				try {
					jObject = new JSONObject(new String(responseBody));
					int code = JsonUtils.getJSONInt(jObject, "code");

					if (code != 3) {
						callBack.succcess(new String(responseBody));
					} else {
						showToast(mContext, "登录超时");
						SharedPreferences share = mContext.getSharedPreferences(Config.PLIS_NAME,Context.MODE_PRIVATE);
						share.edit().putString(Config.TOKEN_ID,"").commit();

						Intent login = new Intent(mContext,LoginActivity.class);
						login.putExtra("loginType", true);
						mContext.startActivity(login);

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
				Log.e("error4", String.valueOf(statusCode) + "**Throwable--" + error.toString());
				//请求错误弹出对话框提示
				callBack.fail(ConnetFailturMessage.connetFailturMessage(error.toString()));
			}

			@Override
			public void onFinish() {
				super.onFinish();
				//请求完成时dismiss进度条
				progressBar.dismissProgressBar();
			}

		});
	}

	/**
	 * 没有回调的网络请求封装方法
	 *
	 * @param mContext
	 * @param url  接口地址
	 * @param params  参数
	 */
	public static void postNoCallBack(Context mContext, String url,RequestParams params) {
		if (mContext == null){ return; }
		//检查网络
		if (!NetWorkUtils.isNetWorkConnected(mContext)) {
			showToast(mContext, mContext.getString(R.string.networkalter));
			return;
		}
		params.put("tokenId",new SharedPreUtil(mContext).get(Config.TOKEN_ID));
		LogUtils.d("HttpGetData", getAbsoluteUrl(mContext, url) + " parsms:" + params);

		client.setTimeout(TIMEOUT_VALUE);
		client.post(getAbsoluteUrl(mContext, url), params,new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
				LogUtils.e("HttpGetData", new String(responseBody));
			}

			@Override
			public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
				Log.e("error4", String.valueOf(statusCode) + "**Throwable--" + error.toString());
			}

			@Override
			public void onFinish() {
				super.onFinish();
			}
		});
	}

	/**
	 * 网路请求封装方法
	 * 
	 * @param mContext
	 * @param url 接口地址
	 * @param params  参数
	 * @param callBack   回调
	 */
	// post方法进行网络请求
	public static void wxpost(final Context mContext, String url,
			String params, String msg, final getDataCallBack callBack) {
		if (mContext == null){ return; }
		if (!NetWorkUtils.isNetWorkConnected(mContext)) {
			showToast(mContext, mContext.getString(R.string.networkalter));
			return;
		}

		//圆形进度条
		final ProgressBarView progressBar = new ProgressBarView(mContext);

		//msg不为空时显示圆形进度条
		if (!TextUtils.isEmpty(msg)) {
			progressBar.showProgressBar(msg);
		}

		HttpEntity entity = null;
		try {
			entity = new cz.msebera.android.httpclient.entity.StringEntity(params);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		client.setTimeout(TIMEOUT_VALUE);
		client.post(mContext, url, entity, "UTF-8", new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {

						LogUtils.d("HttpGetData", new String(responseBody));
						callBack.succcess(new String(responseBody));
					}

					@Override
					public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

						LogUtils.d("HttpGetData", error.toString());
						ShowAlertView.showDialog(mContext, error.toString());
						callBack.fail(ConnetFailturMessage.connetFailturMessage(error.toString()));
					}

					@Override
					public void onFinish() {
						super.onFinish();
						//请求完成时dismiss进度条
						progressBar.dismissProgressBar();

					}
				});
	}

	/**
	 * @param path  要上传的文件路径
	 * @param url   服务端接收URL
	 * @throws Exception
	 */
	public static void uploadFile(Context mContext, String path, String url, String msg,
			final upLoadfileCallBack callBack)throws Exception {
		if (mContext == null){ return; }
		File file = new File(path);
		if (file.exists() && file.length() > 0) {

			RequestParams params = new RequestParams();
			params.put("file", file);
			params.put("tokenId", new SharedPreUtil(mContext).get(Config.TOKEN_ID));


			//圆形进度条
			final ProgressBarView progressBar = new ProgressBarView(mContext);

			//msg不为空时显示圆形进度条
			if (!TextUtils.isEmpty(msg)) {
				progressBar.showProgressBar(msg);
			}
			
			client.setTimeout(TIMEOUT_VALUE_UPLOAD);
			// 上传文件
			client.post(getAbsoluteUrl(mContext, url), params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
					// 上传成功后要做的工作
					callBack.success(new String(responseBody));
				}

				@Override
				public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
					// 上传失败后要做到工作
					callBack.fail("上传失败");
				}

				@Override
				public void onFinish() {
					super.onFinish();
					//请求完成时dismiss进度条
					progressBar.dismissProgressBar();
				}

				@Override
				public void onProgress(long bytesWritten, long totalSize) {
					super.onProgress(bytesWritten, totalSize);

				}

				@Override
				public void onRetry(int retryNo) {
					super.onRetry(retryNo);
					// 返回重试次数
				}

					});
		} else {
			showToast(mContext, "文件不存在");
		}
	}

	public static String getAbsoluteUrl(Context mContext, String relativeUrl) {
		if (!Config.IS_RELEASE) {
			//内侧版设置服务器地址
			SharedPreferences share = mContext.getSharedPreferences(Config.PLIS_NAME,Context.MODE_PRIVATE);
			String server_address = share.getString(Config.SERVER_ADDRESS, Config.URL_SERVICE_IP_TEST);
			Config.URL_SERVICE_IP_TEST = server_address;
			return Config.URL_SERVICE_IP_TEST + relativeUrl;
		}
		return Config.URL_WEBSERVICE + relativeUrl;

	}

	public interface getDataCallBack {
		public void succcess(String res);

		public void fail(String error);
	}

	public interface upLoadfileCallBack {
		public void success(String res);

		public void fail(String error);

	}

	/**
	 * Toast
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}


}
