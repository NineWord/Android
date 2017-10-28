package com.fyl.utils;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 *
 * Created by Administrator on 2016/2/24.
 */
public class PreferenceUtil {
    public static String getStringValue(Context context, String key, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defaultValue);
    }

    public static void setStringValue(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).commit();
    }

    public static boolean getValue(Context context, String key, boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public static void setValue(Context context, String key, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(key, value).commit();
    }

    public static boolean isFirstUse(Context context, String key) {
        boolean v = getValue(context, key, false);
        if (!v) setValue(context, key, true);
        return v;
    }
}
