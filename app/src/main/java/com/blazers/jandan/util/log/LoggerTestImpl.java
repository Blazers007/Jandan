package com.blazers.jandan.util.log;

import java.util.Locale;

/**
 * Created by blazers on 2016/12/7.
 */

public class LoggerTestImpl implements ILog {

    private static final String ANSI_RESET = "\u001B[0m";

    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";


    private static String generateTag() {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[4];
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(Locale.CHINA, tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        return tag;
    }

    @Override
    public void v(String msg) {
        System.out.println(ANSI_WHITE + generateTag() + ":" + msg + ANSI_RESET);
    }

    @Override
    public void v(String tag, String msg) {
        System.out.println(ANSI_WHITE + tag + ":" + msg + ANSI_RESET);
    }

    @Override
    public void e(String msg) {
        System.out.println(ANSI_RED + generateTag() + ":" + msg + ANSI_RESET);
    }

    @Override
    public void e(String tag, String msg) {
        System.out.println(ANSI_RED + tag + ":" + msg + ANSI_RESET);
    }

    @Override
    public void e(Throwable throwable) {
        System.out.println(ANSI_RED + ":" + throwable.toString() + ANSI_RESET);
    }

    @Override
    public void e(String tag, Throwable throwable) {
        System.out.println(ANSI_RED + tag + ":" + throwable.toString() + ANSI_RESET);
    }

    @Override
    public void e(String tag, String msg, Throwable throwable) {
        System.out.println(ANSI_RED + tag + ":" + msg + "\n" + throwable.toString() + ANSI_RESET);
    }

    @Override
    public void d(String msg) {
        System.out.println(ANSI_BLUE + generateTag() + ":" + msg + ANSI_RESET);
    }

    @Override
    public void d(String tag, String msg) {
        System.out.println(ANSI_BLUE + tag + ":" + msg + ANSI_RESET);
    }

    @Override
    public void i(String msg) {
        System.out.println(ANSI_GREEN + generateTag() + ":" + msg + ANSI_RESET);
    }

    @Override
    public void i(String tag, String msg) {
        System.out.println(ANSI_GREEN + tag + ":" + msg + ANSI_RESET);
    }

    @Override
    public void w(String msg) {
        System.out.println(ANSI_YELLOW + generateTag() + ":" + msg + ANSI_RESET);
    }

    @Override
    public void w(String tag, String msg) {
        System.out.println(ANSI_YELLOW + tag + ":" + msg + ANSI_RESET);
    }
}
