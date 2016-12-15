package com.blazers.jandan.util.log;


/**
 * Created by blazers on 2016/12/7.
 */

public interface ILog {

    void v(String msg);

    void v(String tag, String msg);

    void e(String msg);

    void e(String tag, String msg);

    void e(Throwable throwable);

    void e(String tag, Throwable throwable);

    void e(String tag, String msg, Throwable throwable);

    void d(String msg);

    void d(String tag, String msg);

    void i(String msg);

    void i(String tag, String msg);

    void w(String msg);

    void w(String tag, String msg);
}
