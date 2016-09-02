package com.redlichee.uwinmes.utils.net;

/**
 * Created by Administrator on 2016/8/8.
 */

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

/**
 * Created by czz on 2016/5/26.
 */
public class TAsyBastClient {

    public interface DealS {
        public void success(String result);
    }

    public interface DealE {
        public void error(String result);
    }

    public interface DealFinal {
        public void d_final();
    }

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResultDeal asyncHttpResultDeal) {
        client.get(url, params, asyncHttpResultDeal);
    }

    public static void post(String url, RequestParams params, AsyncHttpResultDeal asyncHttpResultDeal) {
        client.post(url, params, asyncHttpResultDeal);
    }

    public static class AsyncHttpResultDeal extends AsyncHttpResponseHandler {
        DealS ds;
        DealE de;
        DealFinal df;

        /**
         * @param ds 请求-成功处理
         */
        public AsyncHttpResultDeal(DealS ds) {
            this(ds, null);
        }

        /**
         * @param ds 请求-成功处理
         * @param de 请求-失败处理
         */
        public AsyncHttpResultDeal(DealS ds, DealE de) {
            this(ds, de, null);
        }

        /**
         * @param ds 请求-成功处理
         * @param de 请求-失败处理
         * @param df 请求-最终处理，比如不管请求成功还是失败都要关闭Dialog
         */
        public AsyncHttpResultDeal(DealS ds, DealE de, DealFinal df) {
            this.ds = ds;
            this.de = de;
            this.df = df;
        }

        //请求开始
        @Override
        public void onStart() {
            super.onStart();
        }

        //请求成功 HTTP statusCode:200 ok
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            if (ds != null) {
                if (responseBody != null && responseBody.length > 0) {
                    String result = new String(responseBody);
                    ds.success(result);
                }
            }
            if (df != null) {
                df.d_final();
            }
        }

        //请求失败 HTTP statusCode:4XX (eg. 401, 403, 404)
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            if (de != null) {
                if (responseBody != null && responseBody.length > 0) {
                    String result = new String(responseBody);
                    de.error(result);
                }
            }
            if (df != null) {
                df.d_final();
            }
        }

        //重试
        @Override
        public void onRetry(int retryNo) {
            super.onRetry(retryNo);
        }
    }
}
