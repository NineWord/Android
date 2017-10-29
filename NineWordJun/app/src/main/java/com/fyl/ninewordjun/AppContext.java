package com.fyl.ninewordjun;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.fyl.ninewordjun.greendao.db.DBHelper;
import com.fyl.ninewordjun.media.VoicePlayer;
import com.fyl.utils.Log;

import io.reactivex.schedulers.Schedulers;


/**
 * Created by laofuzi on 2017/10/28.
 *
 */

public class AppContext extends Application {
    private static AppContext mInstances;
    private Activity mActivity;


    public static AppContext getInstances(){
        return mInstances;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstances = this;

        this.registerActivityLifecycleCallbacks(callbacks);

//        VoicePlayer.getInstance();
        Schedulers.io().createWorker().schedule(new Runnable() {
            @Override
            public void run() {
                DBHelper.init(AppContext.getInstances()); // 初始化数据库
            }
        });
    }

    public Activity getActivity(){
        return mActivity;
    }

    private ActivityLifecycleCallbacks callbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            mActivity = activity;
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };
}
