package com.fyl.utils;

import android.content.Context;
import android.os.Environment;
import java.io.File;

public class FilePathUtils {
    private final static String Tag = "FilePathUtils";
    public static String APP_PRIVATE_CACHE_PATH;//外置私有cache目录
    public static String APP_PRIVATE_FIFLE_PATH;//外置私有file目录
    public static String APP_PRIVATE_FIFLE_LOG_PATH;//外置私有file/log目录


    /**
     * 初始化私有路径配置（fyl）
     * @throws IllegalArgumentException
     */
    public static void init(Context context) throws IllegalArgumentException {
        if (context == null) {
            throw new IllegalArgumentException("参数错误");
        }
        //外置私有目录
        File dir = context.getExternalCacheDir();
        if (dir != null) {
            APP_PRIVATE_CACHE_PATH = dir.getAbsolutePath();
        } else {
            Log.e(Tag, "context.getExternalCacheDir()==null, so APP_PRIVATE_CACHE_PATH is NOT exist.");
        }

        dir = context.getExternalFilesDir("");
        if (dir != null) {
            APP_PRIVATE_FIFLE_PATH = dir.getAbsolutePath();
        } else {
            Log.e(Tag, "context.getExternalFilesDir(\"\")==null, so APP_PRIVATE_FIFLE_PATH is NOT exist.");
        }

        dir = context.getExternalFilesDir("log");
        if (dir != null) {
            APP_PRIVATE_FIFLE_LOG_PATH = dir.getAbsolutePath();
        } else {
            Log.e(Tag, "context.getExternalFilesDir(\"log\")==null, so APP_PRIVATE_FIFLE_LOG_PATH is NOT exist.");
        }
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//根目录
//            HAS_EXTERNAL_STORAGE = true;
//            SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
//        } else {
//            HAS_EXTERNAL_STORAGE = false;
//        }
    }


}
