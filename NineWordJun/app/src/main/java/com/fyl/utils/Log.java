package com.fyl.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

/**
 * @author fyl create by 2016-5-9 17:33:33
 * 使用此类先要 LogStorage.init（xxx）初始化
 */
public final class Log {

    //com.nd.smartcan.frame.log.LoggerStrategyLog4J
    //com.nd.smartcan.frame.log.ConfigureLog4J
    //org.apache.log4j.Logger.getLogger(tag).trace(msg, t);

    private static Level sLevel = Level.ERROR;

    public enum Level {
        TRACE(1),//verbose
        DEBUG(2),
        INFO(3),
        WARN(4),
        ERROR(5),
        // end
        ;

        private int mLevel;

        Level(int level) {
            this.mLevel = level;
        }

        public boolean isEnabled(Level otherLevel) {
            return mLevel <= otherLevel.mLevel;
        }

    }

    public static void setLevel(Level level) {
        sLevel = level;
    }

    public static void open() {
        setLevel(Level.TRACE);//TRACE
    }

    public static void close() {
        setLevel(Level.WARN);
    }


    /**
     * 输出verbose级别日志
     * <strong>级别和debug一样</strong>
     *
     * @param tag 标签
     * @param msg 内容
     */
    public static void v(String tag, String msg) {
        android.util.Log.v(tag, msg);
        println(Level.TRACE, tag, msg);
    }

    public static void v(String tag, String msg, Throwable t) {
        android.util.Log.v(tag, msg, t);
        println(Level.TRACE, tag, msg + '\n' + getStackTraceString(t));
    }

    /**
     * 输出debug级别日志
     *
     * @param tag 标签
     * @param msg 内容
     */
    public static void d(String tag, String msg) {
        android.util.Log.d(tag, msg);
        println(Level.DEBUG, tag, msg);
    }

    public static void d(String tag, String msg, Throwable t) {
        android.util.Log.d(tag, msg, t);
        println(Level.DEBUG, tag, msg + '\n' + getStackTraceString(t));
    }

    /**
     * 输出info级别日志
     *
     * @param tag 标签
     * @param msg 内容
     */
    public static void i(String tag, String msg) {
        android.util.Log.i(tag, msg);
        println(Level.INFO, tag, msg);
    }

    public static void i(String tag, String msg, Throwable t) {
        // org.apache.log4j.Logger.getLogger(tag).info(msg, t);
        android.util.Log.i(tag, msg, t);
        println(Level.INFO, tag, msg + '\n' + getStackTraceString(t));
    }


    /**
     * 输出warn级别日志
     *
     * @param tag 标签
     * @param msg 内容
     */
    public static void w(String tag, String msg) {
        android.util.Log.w(tag, msg);
        println(Level.WARN, tag, msg);
    }

    public static void w(String tag, String msg, Throwable t) {
        android.util.Log.w(tag, msg, t);
        println(Level.WARN, tag, msg + '\n' + getStackTraceString(t));
    }

    public static void w(String tag, Throwable t) {
        android.util.Log.w(tag, t);
        println(Level.WARN, tag, getStackTraceString(t));
    }

    /**
     * 输出error级别日志
     *
     * @param tag 标签
     * @param msg 内容
     */
    public static void e(String tag, String msg) {
        android.util.Log.e(tag, msg);
        println(Level.ERROR, tag, msg);
    }

    public static void e(String tag, String msg, Throwable t) {
        android.util.Log.e(tag, msg, t);
        println(Level.ERROR, tag, msg + '\n' + getStackTraceString(t));
    }

    private static int println(Level level, String tag, String msg) {
        if (sLevel.isEnabled(level)) {
            switch (level) {
                case TRACE: {
                    LogStorage.getInstance().add("V/" + tag + ": " + msg);
                    break;
                }
                case DEBUG: {
                    LogStorage.getInstance().add("D/" + tag + ": " + msg);
                    break;
                }
                case INFO: {
                    LogStorage.getInstance().add("I/" + tag + ": " + msg);
                    break;
                }
                case WARN: {
                    LogStorage.getInstance().add("W/" + tag + ": " + msg);
                    break;
                }
                case ERROR: {
                    LogStorage.getInstance().add("E/" + tag + ": " + msg);
                    break;
                }
                default:
                    break;
            }
        }
        return 0;
    }

    /**
     * Handy function to get a loggable stack trace from a Throwable
     *
     * @param tr An exception to log
     */
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }
}
