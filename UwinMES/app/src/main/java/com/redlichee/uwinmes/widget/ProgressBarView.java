package com.redlichee.uwinmes.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.redlichee.uwinmes.R;


/**
 * 自定义ProgressBarView
 * Created by LMW on 2016/5/30.
 */
public class ProgressBarView {

    private Dialog dialog;
    private Context mContext;
    private View mView;
    private TextView tvMsg;

    public ProgressBarView(Context mContext){
        this.mContext = mContext;
        dialog = new Dialog(mContext, R.style.dialogProgressBar);// builder = new Dialog .Builder(mContext).create();
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_progress_wheel, null);
        tvMsg = (TextView) mView.findViewById(R.id.text_msg);
        dialog.setCancelable(true);//返回键是否取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.getWindow().setContentView(mView);
    }


    /**
     * 显示ProgressBar
     * @param msg       显示的文字
     */
    public void showProgressBar(String msg){
        tvMsg.setText(msg);

        dialog.show();
    }

    /**
     * 关闭ProgressBar
     */
    public void dismissProgressBar(){
        dialog.dismiss();
    }
}
