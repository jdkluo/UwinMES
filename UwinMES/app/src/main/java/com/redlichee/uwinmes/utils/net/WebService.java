package com.redlichee.uwinmes.utils.net;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.SimpleArrayMap;
import android.text.TextUtils;

import com.redlichee.uwinmes.application.Config;
import com.redlichee.uwinmes.utils.LogUtils;
import com.redlichee.uwinmes.utils.SharedPreUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * WebService访问方式的工具类
 */
public class WebService {
    // 访问的服务器是否由dotNet开发
    public static boolean isDotNet = true;
    // 线程池的大小
    private static int threadSize = 5;
    // 创建一个可重用固定线程数的线程池，以共享的无界队列方式来运行这些线程
    private static ExecutorService threadPool = Executors.newFixedThreadPool(threadSize);
    // 连接响应标示
    public static final int SUCCESS_FLAG = 0;
    public static final int ERROR_FLAG = 1;

    /**
     * 调用WebService接口，此方法只访问过用Java写的服务器
     *
     * @param methodName      WebService的调用方法名
     * @param mapParams       WebService的参数集合，可以为null
     * @param reponseCallBack 服务器响应接口
     */
    public static void call(Activity mActivity, final String methodName, SimpleArrayMap<String, String> mapParams, final Response reponseCallBack) {
        String URL_WEBSERVICE;//服务器地址
        if(Config.IS_RELEASE){
            URL_WEBSERVICE = Config.URL_WEBSERVICE;
        } else {
            String server_address = new SharedPreUtil(mActivity).get(Config.SERVER_ADDRESS, Config.URL_SERVICE_IP);
            Config.URL_SERVICE_IP_TEST = TextUtils.isEmpty(server_address) ? Config.URL_SERVICE_IP : server_address;
            URL_WEBSERVICE = Config.URL_SERVICE_IP_TEST + Config.URL_ASMX;
        }
        // 1.创建HttpTransportSE对象，传递WebService服务器地址
        final HttpTransportSE transport = new HttpTransportSE(URL_WEBSERVICE);
        transport.debug = true;
        // 2.创建SoapObject对象用于传递请求参数
        final SoapObject request = new SoapObject(Config.URL_NAME_SPACE, methodName);
        // 2.1.添加参数也可以不传
        if (mapParams != null) {
            for (int index = 0; index < mapParams.size(); index++) {
                String key = mapParams.keyAt(index);
                String value = mapParams.get(key);
                request.addProperty(key, value);
            }
        }

        // 3.实例化SoapSerializationEnvelope，传入WebService的SOAP协议的版本号
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = isDotNet; // 设置是否调用的是.Net开发的WebService
        envelope.setOutputSoapObject(request);

        LogUtils.d("WebService", URL_WEBSERVICE + "/" + methodName + " parsms:" + mapParams.toString());

        // 4.用于子线程与主线程通信的Handler，网络请求成功时会在子线程发送一个消息，然后在主线程上接收
        final Handler responseHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // 根据消息的arg1值判断调用哪个接口
                if (msg.arg1 == SUCCESS_FLAG){
                    LogUtils.d("WebService:" , (SoapObject) msg.obj != null ? ((SoapObject) msg.obj).toString() : "null");
                    reponseCallBack.onSuccess((SoapObject) msg.obj);
                } else{
                    reponseCallBack.onError((Exception) msg.obj);
                }
            }

        };

        // 5.提交一个子线程到线程池并在此线种内调用WebService
        if (threadPool == null || threadPool.isShutdown())
            threadPool = Executors.newFixedThreadPool(threadSize);
        threadPool.submit(new Runnable() {

            @Override
            public void run() {
                SoapObject result = null;
                try {
                    // 解决EOFException
                    System.setProperty("http.keepAlive", "false");
                    // 连接服务器
                    transport.call(Config.URL_NAME_SPACE + methodName, envelope);
                    if (envelope.getResponse() != null) {
                        // 获取服务器响应返回的SoapObject
                        result = (SoapObject) envelope.bodyIn;
                        // 将获取的消息利用Handler发送到主线程
                        responseHandler.sendMessage(responseHandler.obtainMessage(0, SUCCESS_FLAG, 0, result));
                    }
                } catch (IOException e) {
                    // 当call方法的第一个参数为null时会有一定的概念抛IO异常
                    // 因此需要需要捕捉此异常后用命名空间加方法名作为参数重新连接
                    e.printStackTrace();
                    // e1.printStackTrace();
                    responseHandler.sendMessage(responseHandler.obtainMessage(0, ERROR_FLAG, 0, "网络错误，请稍后再试！"));
                } catch (XmlPullParserException e) {
                    // e.printStackTrace();
                    responseHandler.sendMessage(responseHandler.obtainMessage(0, ERROR_FLAG, 0, "网络错误，请稍后再试！"));
                }
            }
        });
    }

    /**
     * 设置线程池的大小
     *
     * @param threadSize
     */
    public static void setThreadSize(int threadSize) {
        WebService.threadSize = threadSize;
        threadPool.shutdownNow();
        threadPool = Executors.newFixedThreadPool(WebService.threadSize);
    }

    /**
     * 服务器响应接口，在响应后需要回调此接口
     */
    public interface Response {
        public void onSuccess(SoapObject result);

        public void onError(Exception e);
    }

}