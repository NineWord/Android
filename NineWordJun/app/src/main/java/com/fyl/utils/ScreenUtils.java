package com.fyl.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

//import org.greenrobot.eventbus.EventBus;

public class ScreenUtils {
    /**
     * 获取屏幕方向
     *
     * @return true竖屏，false横屏
     */
    public static boolean getScreenOrientation(Context context) {
        //Configuration.ORIENTATION_PORTRAIT Configuration.ORIENTATION_LANDSCAPE
        return context.getResources().getConfiguration().orientation == Configuration
                .ORIENTATION_PORTRAIT;
    }

    /**
     * 屏幕翻转
     */
    public static void changeScreenOrientation(Activity activity, int orientation) {
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            activity.setRequestedOrientation(orientation);
        }
    }
}
