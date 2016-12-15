package com.blazers.jandan.util.log;

import timber.log.Timber;

/**
 * Created by blazers on 2016/12/7.
 */

public class LoggerImpl implements ILog {


    @Override
    public void v(String msg) {
        Timber.v(msg);
    }

    @Override
    public void v(String tag, String msg) {
        Timber.v(tag, msg);
    }

    @Override
    public void e(String msg) {
        Timber.e(msg);
    }

    @Override
    public void e(String tag, String msg) {
        Timber.e(msg, tag);
    }

    @Override
    public void e(Throwable throwable) {
        Timber.e(throwable);
    }

    @Override
    public void e(String tag, Throwable throwable) {
        Timber.e(throwable, tag);
    }

    @Override
    public void e(String tag, String msg, Throwable throwable) {
        Timber.e(throwable, msg, tag);
    }

    @Override
    public void d(String msg) {
        Timber.d(msg);
    }

    @Override
    public void d(String tag, String msg) {
        Timber.d(msg, tag);
    }

    @Override
    public void i(String msg) {
        Timber.i(msg);
    }

    @Override
    public void i(String tag, String msg) {
        Timber.i(msg, tag);
    }

    @Override
    public void w(String msg) {
        Timber.w(msg);
    }

    @Override
    public void w(String tag, String msg) {
        Timber.w(tag, msg);
    }
}
