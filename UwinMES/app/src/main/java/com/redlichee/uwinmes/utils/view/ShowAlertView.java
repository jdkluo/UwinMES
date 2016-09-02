package com.redlichee.uwinmes.utils.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.redlichee.uwinmes.R;
import com.redlichee.uwinmes.adapter.SolutionListAdapter;
import com.redlichee.uwinmes.model.Solution;
import com.redlichee.uwinmes.widget.CustomDialog;
import com.redlichee.uwinmes.widget.NoScrollListView;

import java.util.ArrayList;


public class ShowAlertView {

	/**
	 * 提示弹出框（无回调-Context）
	 * @param context 上下文
	 * @param msg  提示内容
     */
	public static void showDialog(Context context, String msg) {
		if (context == null){ return; }
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setTitle(context.getString(R.string.tip));
		builder.setMessage(msg);
		builder.setNegativeButton(context.getString(R.string.ok),null);
		builder.show();
	}

	/**
	 * 提示弹出框（无回调-Activity）
	 * @param activity Activity
	 * @param msg  提示内容
	 */
	public static void showDialog(Activity activity, String msg) {
		if (activity == null){ return; }
		CustomDialog.Builder builder = new CustomDialog.Builder(activity);
		builder.setTitle(activity.getString(R.string.tip));
		builder.setMessage(msg);
		builder.setNegativeButton(activity.getString(R.string.ok),null);

		Activity a = (Activity) activity;
		if (!a.isFinishing()) {
			builder.show();
		}
	}

	public static void showOkAndNegative(Context context, String msg, final ClickCallback callback) {
		if (context == null){ return; }
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setTitle(context.getString(R.string.tip));
		builder.setMessage(msg);
		builder.setNegativeButton(context.getString(R.string.negative), null);
		builder.setPositiveButton(context.getString(R.string.positive),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						callback.clickOk();
					}
				});
		builder.show();
//		Activity a = (Activity) context;
//		if (!a.isFinishing()) {
//			builder.show();
//		}
	}

	public static void showOkAndNegative(Context context, String msg, String title, final ClickCallback callback) {
		if (context == null){ return; }
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setNegativeButton(context.getString(R.string.negative), null);
		builder.setPositiveButton(context.getString(R.string.positive),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						callback.clickOk();
					}
				});
		builder.show();
//		Activity a = (Activity) context;
//		if (!a.isFinishing()) {
//			builder.show();
//		}
	}
	
	public static void showOkAndNegative(Context context, String msg,
			String title,String negative,String positive, 
			final ClickCallback callback, final ClickCallback callbackNegative) {
		if (context == null){ return; }
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(callback != null){
					callback.clickOk();
				}
			}
		});
		builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				callbackNegative.clickOk();
			}
		});
		builder.show();
//		Activity a = (Activity) context;
//		if (!a.isFinishing()) {
//			builder.show();
//		}
	}
	/**
	 * 自定义三按钮弹出对话框
	 * @param context
	 * @param msg
	 * @param title
	 * @param negative
	 * @param positive
	 * @param callback
	 * @param callbackNegative
	 */
	public static void showOkNegativeNeutral(Context context, String msg,
			String title,String negative,String neutral, String positive, 
			final ClickCallback callback, 
			final ClickCallback callbackNeutral, 
			final ClickCallback callbackNegative) {
		if (context == null){ return; }
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(callback != null){
					callback.clickOk();
				}
			}
		});
		builder.setNeutralButton(neutral, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(callbackNeutral != null){
					callbackNeutral.clickOk();
				}
			}
		});
		builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(callbackNegative != null){
					callbackNegative.clickOk();
				}
			}
		});
		builder.show();
//		Activity a = (Activity) context;
//		if (!a.isFinishing()) {
//			builder.show();
//		}
	}

	public static void showRadiolDialog(Context mContext, String title, final OvalClickCallback callback) {
		if (mContext == null){ return; }

		final Dialog d = new Dialog(mContext, R.style.DialogStyle);
		d.setContentView(R.layout.dialog_radio);
		d.setCanceledOnTouchOutside(true);
		d.setCancelable(true);
		TextView tv_title = (TextView) d.findViewById(R.id.tv_dialog_title);
		Button btn_dialog1 = (Button) d.findViewById(R.id.btn_dialog1);
		Button btn_dialog2 = (Button) d.findViewById(R.id.btn_dialog2);
		Button btn_dialog3 = (Button) d.findViewById(R.id.btn_dialog3);
		tv_title.setText(title);


		btn_dialog1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.ovalClickOk("1");
				d.dismiss();
			}
		});
		btn_dialog2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.ovalClickOk("2");
				d.dismiss();
			}
		});
		btn_dialog3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.ovalClickOk("3");
				d.dismiss();
			}
		});

		d.show();

	}

	/**
	 * 列表对话框
	 * @param mContext
	 * @param title
	 * @param data
	 * @param callback
     */
	public static void showRadiolListDialog(Context mContext, String title, ArrayList<Solution> data, final OnListClickCallback callback) {
		if (mContext == null){ return; }

		final Dialog d = new Dialog(mContext, R.style.DialogStyle);
		d.setContentView(R.layout.dialog_radio_list);
		d.setCanceledOnTouchOutside(true);
		d.setCancelable(true);
		TextView tv_title = (TextView) d.findViewById(R.id.tv_dialog_title);
		ListView mListView = (ListView) d.findViewById(R.id.lv_dialog_radio);

		tv_title.setText(title);
		mListView.setAdapter(new SolutionListAdapter(mContext, data));

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				callback.clickList(position);
				d.dismiss();
			}
		});

		d.show();

	}


	public interface OnListClickCallback {
		public void clickList(int position);

	}

	public interface OvalClickCallback {
		public void ovalClickOk(String edtStr);

	}

	public interface ClickCallback {
		public void clickOk();

	}
}
