package com.blazers.jandan.util.log;

/**
 * Created by blazers on 2016/12/8.
 */

public class Log {
    private static ILog mLogger = new LoggerImpl();


    public static void v(String msg) {
        mLogger.v(msg);
    }

    public static void v(String tag, String msg) {
        mLogger.v(tag, msg);
    }


    public static void e(String msg) {
        mLogger.e(msg);
    }


    public static void e(String tag, String msg) {
        mLogger.e(tag, msg);
    }


    public static void e(Throwable throwable) {
        mLogger.e(throwable);
    }


    public static void e(String tag, Throwable throwable) {
        mLogger.e(tag, throwable);
    }


    public static void e(String tag, String msg, Throwable throwable) {
        mLogger.e(tag, msg, throwable);
    }


    public static void d(String msg) {
        mLogger.d(msg);
    }


    public static void d(String tag, String msg) {
        mLogger.d(tag, msg);
    }


    public static void i(String msg) {
        mLogger.i(msg);
    }


    public static void i(String tag, String msg) {
        mLogger.i(tag, msg);
    }


    public static void w(String msg) {
        mLogger.w(msg);
    }


    public static void w(String tag, String msg) {
        mLogger.w(tag, msg);
    }
}
