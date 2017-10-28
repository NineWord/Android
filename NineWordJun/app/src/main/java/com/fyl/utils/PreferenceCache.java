package com.fyl.utils;

import android.content.Context;

/**
 * 统一存储工程中SharedPreferences变量值
 * Created by fyl on 2016/5/17.
 */
public class PreferenceCache {

    /*************************************************************
     * 日志收集开关状态
     ************************************************************/
    private static String LOG_STATE = "log4j_open";

    //@return: true->open, false->close
    public static boolean getLogOpen(Context context) {
        return PreferenceUtil.getValue(context, LOG_STATE, false);
    }

    public static void openLog(Context context) {
        PreferenceUtil.setValue(context, LOG_STATE, true);//
    }

    public static void closeLog(Context context) {
        PreferenceUtil.setValue(context, LOG_STATE, false);//
    }

    /*************************************************************
     * 藏语开关状态
     ************************************************************/
    private static String TIBETAN_STATE = "tibetan_state";

    //@return: true->open, false->close
    public static boolean getTibetan(Context context) {
        return PreferenceUtil.getValue(context, TIBETAN_STATE, false);
    }

    public static void openTibetan(Context context) {
        PreferenceUtil.setValue(context, TIBETAN_STATE, true);//
    }

    public static void closeTibetan(Context context) {
        PreferenceUtil.setValue(context, TIBETAN_STATE, false);//
    }

    /*************************************************************
     * 多语言状态
     ************************************************************/
    private static String LANGUAGE_TYPE = "zh";

    public static String getLanguageType(Context context) {
        return PreferenceUtil.getStringValue(context, LANGUAGE_TYPE, "zh");
    }

    public static void setLanguageType(Context context, String language) {
        PreferenceUtil.setStringValue(context, LANGUAGE_TYPE, language);
    }


}
