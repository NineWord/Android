package com.fyl.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {

	private static Toast mToast=null;

	public static void showToast(Context mContext, String msg, int duration){
		cancelToast();
		mToast=Toast.makeText(mContext.getApplicationContext(), msg, duration);
		mToast.show();
	}
	
	/**
	 * 显示Toast提示，时长默认为Toast.LENGTH_SHORT
	 */
	public static void showShortToast(Context mContext, String msg){
		cancelToast();
		mToast=Toast.makeText(mContext.getApplicationContext(), msg, Toast.LENGTH_SHORT);
		mToast.show();
	}
	
	/**
	 * 显示Toast提示，时长默认为Toast.LENGTH_LONG
	 */
	public static void showLongToast(Context mContext, String msg){
		cancelToast();
		mToast=Toast.makeText(mContext.getApplicationContext(), msg, Toast.LENGTH_LONG);
		mToast.show();
	}
	
	public static void cancelToast(){
		if(mToast!=null){
			mToast.cancel();
			mToast=null;
		}
	}
}
