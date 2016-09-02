package com.redlichee.uwinmes.application;

import android.os.Environment;

/**
 * 全局变量类
 * @author 
 *
 */
public class Globals {
	
	public final static String IMAGE_SAVEPATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/Android/data/com.redlichee.uwinmes/files/Images/";

	public final static String VOICE_SAVEPATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/Android/data/com.redlichee.uwinmes/files/Voice/";

	public final static String FILE_SAVEPATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/Android/data/com.redlichee.uwinmes/files/File/";

}
