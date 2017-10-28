package com.fyl.ninewordjun.utils;

import java.util.List;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

//	public static boolean IS_NETWORK_AVAILABLE;// 当前网络是否可用
	/**
	 * 检测网络是否连接
	 */
	public static boolean isNetConnected(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo[] infos = cm.getAllNetworkInfo();
			if (infos != null) {
				for (NetworkInfo ni : infos) {
					if (ni.isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 检测wifi是否连接
	 */
	public static boolean isWifiConnected(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			if (networkInfo != null
					&& networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检测3G是否连接
	 */
	public static boolean is3gConnected(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			if (networkInfo != null
					&& networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检测GPS是否打开
	 */
	public static boolean isGpsEnabled(Context ctx) {
		LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
		List<String> accessibleProviders = lm.getProviders(true);
		for (String name : accessibleProviders) {
			if ("gps".equals(name)) {
				return true;
			}
		}
		return false;
	}

}
