package com.fyl.ninewordjun.greendao.db;

import android.content.Context;

import com.fyl.ninewordjun.AppContext;
import com.fyl.ninewordjun.greendao.gen.DaoMaster;
import com.fyl.ninewordjun.greendao.gen.DaoSession;

/**
 * Created by anye0 on 2016/7/24.
 */
public class DBHelper {
    private static DBHelper instance;
    //private static Context mContext;

    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    private DBHelper(Context context) {
        //mContext = context;
        instance = this;

        DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, "nineword.db", null);
        daoMaster = new DaoMaster(helper.getWritableDatabase());
        daoSession = daoMaster.newSession();
    }

    /**
     * 取得DaoMaster
     *
     * @return DaoMaster
     */
    public DaoMaster getDaoMaster(Context context) {
        return daoMaster;
    }

    /**
     * 取得DaoSession
     *
     * @return DaoSession
     */
    public DaoSession getDaoSession() {
        return daoSession;
    }


    public static DBHelper getInstance() {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper(AppContext.getInstances());
                }
            }
        }
        return instance;
    }

    public static void init(Context context) {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper(context);
                }
            }
        }
    }

}
