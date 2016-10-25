package com.blazers.jandan.util;

import rx.Observable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Blazers on 2015/10/28.
 */
public class RxHelper {
    /**
     * 转化普通的RX操作
     * */
    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * RetryWithDelay
     */
}