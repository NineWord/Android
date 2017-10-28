package com.fyl.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 日志存储类，注意init最先调用
 * @author fyl create by 2016-9-7
 */
public class LogStorage {
    //private static volatile LogStorage sInstance = null;
    private static BlockingQueue<String> sBlockingQueue;
    private static Date sToday = new Date();
    //
    private BufferedWriter mWriter;
    private boolean mRunning = true;
    private WriteLogThread mThread;

    private File mLogFileDir;
    private static final String LOG_EXT = ".txt";
    private static final String DATE = "yyyy-MM-dd";
    private static final String TIME = "HH:mm:ss";

    private final Object writerLock = new Object();

    private String mLogDir;         // FilePathUtils.LOG_RECORD_PATH
    private String mBuildDate;      // 编译时间 CommonUtils.getBuildDateAsString(App.context(), dateFormat)
    private String mVersion;        // 客户端 CommonUtils.getCurrentVersionName(App.context()) + " (" +CommonUtils.getCurrentVersion(App.context()) + ")\n"
    private String mDeviceName;     // 设备型号 CommonUtils.getDeviceModelName() CommonUtils.getSystemVersion()
    private String mUuid;           // 设备ID CommonUtils.getInstallationId()


    private LogStorage() {
        sBlockingQueue = new LinkedBlockingQueue<>(5000);
        mThread = new WriteLogThread();
        mThread.start();
    }

    private static class SingletonHolder {
        public static LogStorage INSTANCE = new LogStorage();
    }
    
    /**
     * 获取日志存储对象
     */
    public static LogStorage getInstance() {
        /*if (sInstance == null) {
            synchronized (LogStorage.class) {
                if (sInstance == null) {
                    sInstance = new LogStorage();
                }
            }
        }*/
        return SingletonHolder.INSTANCE;
    }




    /**
     * 参数初始化(此函数需最新调用)
     *
     * @param logDir     日志存放路径文件夹，默认私有路径，公有路径权限申请问题需要在调用地方申请。
     * @param buildDate  apk编译日期
     * @param version    apk版本
     * @param deviceName 设备名称如：xiaomi5
     * @param uuid       设备号
     */
    public void init(String logDir, String buildDate, String version, String deviceName, String uuid) {
        mLogDir = logDir;
        mBuildDate = buildDate;
        mVersion = version;
        mDeviceName = deviceName;
        mUuid = uuid;
    }


    /**
     * 获取日志文件所在的目录
     */
    public String getLogDir() {
        if (mLogFileDir != null) {
            return mLogFileDir.getAbsolutePath();
        }
        return "";
    }

    /**
     * 打开日志文件
     */
    public boolean openFile() {
        if (mLogDir == null) return false;

        String fileName = new SimpleDateFormat(DATE, Locale.getDefault()).format(new Date());
        String fullPathFile = mLogDir + fileName + LOG_EXT;
        mLogFileDir = new File(mLogDir);
        if (!mLogFileDir.exists()) {
            boolean success = mLogFileDir.mkdirs();
            if (!success) {
                System.out.println("error openFile() mkdirs fail.");
            }
        }
        try {
            if (mWriter != null) {
                close();
                mWriter = new BufferedWriter(new FileWriter(fullPathFile, true));
            } else {
                mWriter = new BufferedWriter(new FileWriter(fullPathFile, true));
            }

            writeHead();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void close() {
        if (mWriter != null) {
            try {
                synchronized (writerLock) {
                    mWriter.close();
                }
                mWriter = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 写入头信息
     */
    private void writeHead() {
        if (mWriter == null) return;
        synchronized (writerLock) {
            try {
                mWriter.write("===================== start log ====================\n");
                mWriter.write("编译时间: " + mBuildDate + "\n");
                mWriter.write("当前时间: " + new SimpleDateFormat(DATE + " " + TIME, Locale.getDefault()).format(new Date()) + "\n");
                mWriter.write("客户端:   " + mVersion + "\n");
                mWriter.write("设备型号: " + mDeviceName + "\n");
                mWriter.write("设备ID:  " + mUuid + "\n");
                mWriter.write("----------------------------------------------------\n");
                mWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 增加日志信息
     */
    public void add(String log) {
        if (mRunning) {
            sToday.setTime(System.currentTimeMillis());
            String l = new SimpleDateFormat(TIME, Locale.getDefault()).format(sToday) + " " + log;

            try {
                sBlockingQueue.offer(l);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 日志写入文件中
     */
    class WriteLogThread extends Thread {
        public WriteLogThread() {
            setName("WriteLogThread");
        }

        @Override
        public void run() {
            while (mLogDir == null) {
                try {
                    sleep(1000 * 5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                mRunning = openFile();
                while (mRunning) {
                    if (mWriter != null) {
                        String log = sBlockingQueue.take();
                        synchronized (writerLock) {
                            mWriter.write(log);
                            mWriter.newLine();
                            mWriter.flush();
                        }
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                mRunning = false;
                close();
            }
        }
    }
}
