package com.fyl.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class CommonUtils {


    private static String sUuid = null;
       // private static String sUuidFile = FilePathUtils.APP_PRIVATE_FIFLE_PATH + "/aaa.txt";
    private static String sUuidFile = FilePathUtils.APP_PRIVATE_FIFLE_PATH +"/aaa.txt";

    /**
     * fyl modified at 2016-06-16
     * 获取安装id     *
     */
    public synchronized static String getInstallationId() {
        if (sUuid == null || sUuid.length() == 0) {
            sUuid = createUuid(sUuidFile);
        }
        return sUuid;
    }

    public static String getUuidFile() {
        return sUuidFile;
    }

    /**
     * fyl modified at 2016-06-16
     * 生成uuid
     *
     * @param filePath 保存uuid文件路径
     */
    public static String createUuid(String filePath) {
        String uuid;
        if (!FileUtils.isFileExist(filePath)) {// 文件不存在
            uuid = UUID.randomUUID().toString();
            try {
                FileUtils.writeFile(filePath, uuid, false);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        } else {// 文件存在
            try {
                StringBuilder sb = FileUtils.readFile(filePath, "utf-8");
                uuid = sb==null ? null : sb.toString();
                if(uuid==null||uuid.length()==0){
                    uuid = UUID.randomUUID().toString();
                    try {
                        FileUtils.writeFile(filePath, uuid, false);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                }
            } catch (RuntimeException e) {
                uuid = UUID.randomUUID().toString();
                e.printStackTrace();
            }
        }
        return uuid;
    }


    /**
     * fyl
     * 获取当前客户端版本信息
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }


    /**
     * 获取当前客户端版本信息
     */
    public static int getCurrentVersion(Context context) {
        try {
            if (context == null) return 0;
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName
                    (), 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        return 0;
    }

    /**
     * 获得编译时间
     *
     * @param context    上下文
     * @param dateFormat 时间格式
     */
    public static String getBuildDateAsString(Context context, DateFormat dateFormat) {
        String buildDate;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context
                    .getPackageName(), 0);
            ZipFile zf = new ZipFile(ai.sourceDir);
            ZipEntry ze = zf.getEntry("classes.dex");
            long time = ze.getTime();
            buildDate = dateFormat.format(new Date(time));
            zf.close();
        } catch (Exception e) {
            buildDate = "Unknown";
        }
        return buildDate;
    }

    /**
     * 获取设备型号
     */
    public static String getDeviceModelName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    /**
     * 获取当前客户端版本信息
     */
    public static String getCurrentVersionName(Context context) {
        try {
            if (context == null) return null;
            if (context.getPackageManager() == null) return null;
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName
                    (), 0);
            if (info == null) return null;
            return info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * 获取设备名
     */
    public static String getDeviceName() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 获取设备ID的CRC
     *
     * modified by liy 2016.6.7
     */
    private static long sDeviceId;

    public static int getDeviceId() {
        if (sDeviceId != 0) return (int) sDeviceId;
        String id = getInstallationId();
        CRC32 crc = new CRC32();
        crc.update(id.getBytes());
        sDeviceId = crc.getValue();
        Log.i(CommonUtils.class.getSimpleName(), "value=" + sDeviceId);
        return (int) sDeviceId;
    }


    /**
     * 安装apk
     */
    public static void installApk(Context mContext, String apkFilePath) {
        File apkfile = new File(apkFilePath);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android" +
                ".package-archive");
        mContext.startActivity(i);
    }


    /**
     * 获取当前日期
     */
    public static String getCurrentDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH-mm-ss", Locale.getDefault());
        return sdf.format(new Date());
    }


    public static String getFileNameNoEx(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        int idx = filePath.lastIndexOf(File.separator);
        if (idx < 0) return null;
        String name = filePath.substring(idx);
        return name.substring(0, name.lastIndexOf("."));
    }

    public static boolean isEmail(String value) {
        if (TextUtils.isEmpty(value)) {
            return false;
        }
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        return pattern.matcher(value).matches();
    }

    public static boolean isMobileNumber(String value) {
        if (TextUtils.isEmpty(value)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8]))\\d{8}$");
        return pattern.matcher(value).matches();
    }

    public static boolean isPassword(String value) {
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[0-9a-zA-Z]{6,20}$");
        return pattern.matcher(value).matches();
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    /**
     * 非法字符验证
     */
    public static boolean isSpecialCharacter(String value) {
        if (TextUtils.isEmpty(value)) {
            return false;
        }

        Pattern pattern = Pattern.compile("^[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？]");
        return pattern.matcher(value).matches();
    }
    //string 为要判断的字符串
    public static boolean isConSpeCharacters(String string){
        String reExp = "[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*";
        return !string.replaceAll(reExp, "").isEmpty();
    }

    /**
     * 判断某个隐式的Intent是否可用
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean isIntentAvailabale(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(intent,
                PackageManager.MATCH_ALL);
//		System.out.println("size = "+resolveInfoList.size());
        return resolveInfoList.size() != 0;
    }

    /**
     * 从字符串中截取连续6位数字组合 ([0-9]{" + 6 + "})截取六位数字 进行前后断言不能出现数字 用于从短信中获取动态密码
     *
     * @param str 短信内容
     * @return 截取得到的6位动态密码
     */
    public static String getDynamicPassword(String str) {
        // 6是验证码的位数一般为六位
        Pattern continuousNumberPattern = Pattern.compile("(?<![0-9])([0-9]{"
                + 6 + "})(?![0-9])");
        Matcher m = continuousNumberPattern.matcher(str);
        String dynamicPassword = "";
        while (m.find()) {
            System.out.print(m.group());
            dynamicPassword = m.group();
        }

        return dynamicPassword;
    }

    //获取设备型号
    public static String getDeceiveModel() {
        String model = android.os.Build.MODEL;
        return model;
    }

    //获取设备IMEI
    public static String getDeviceImei(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context
                .TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    //获取设备品牌
    public static String getPhoneBrand() {
        return Build.MANUFACTURER;
    }

    //获取安装渠道
    public static String getInstallChannel(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context
                    .getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString("ND_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            //e.printStackTrace();
        }
        return "";
    }

    /**
     * 根据比例值，同步大小,
     *
     * @return point.x宽   point.y高
     */
    public static Point syncSizeRatio(int width1, int height1, int width2, int height2) {
        Point point = new Point(width2, height2);
        float ratio1 = (float) width1 / (float) height1;
        float ratio2 = (float) width2 / (float) height2;
        if (ratio1 > ratio2) {
            point.y = (int) (width2 / ratio1);
        } else {
            point.x = (int) (height2 * ratio1);
        }
        return point;
    }

}


