package com.redlichee.uwinmes.application;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Activity管理类
 * @author 
 *
 */
public class MyApplication extends Application {

	private List<Activity> activityList = new LinkedList<Activity>();
	private static MyApplication instance;


	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;

	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
	/**
	 * 获到app实例
	 * @return
	 */
	public static MyApplication getInstance() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;

	}


	public void addActivity(Activity activity) {
		if(!activityList.contains(activity)){
			activityList.add(activity);
		}
	}
	
	public void removeActivity(Activity activity) {		
		if(activityList.contains(activity)){
			activityList.remove(activity);
		}
	}
	
	
	public void finishAllActivity() {
		for (Activity activity : activityList) {
			if(activity != null){
				activity.finish();
			}
		}
	}
	
	/**
	 * 退出app
	 */
	public void exit() {
		finishAllActivity();	
		System.exit(0);		
	}
    
}
