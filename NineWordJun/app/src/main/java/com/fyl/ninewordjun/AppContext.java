package com.fyl.ninewordjun;

import android.app.Application;
import com.fyl.ninewordjun.greendao.db.DBHelper;
import com.fyl.ninewordjun.media.VoicePlayer;

import io.reactivex.schedulers.Schedulers;


/**
 * Created by laofuzi on 2017/10/28.
 *
 */

public class AppContext extends Application {
    public static AppContext instances;

    public static AppContext getInstances(){
        return instances;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;

        VoicePlayer.getInstance();
        Schedulers.io().createWorker().schedule(new Runnable() {
            @Override
            public void run() {
                DBHelper.init(AppContext.getInstances()); // 初始化数据库
            }
        });
    }

}
