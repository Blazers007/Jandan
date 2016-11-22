package com.blazers.jandan.presenter.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.blazers.jandan.util.Rxbus;

import java.lang.ref.WeakReference;

import io.realm.Realm;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by blazers on 2016/11/11.
 *
 * 主要包含:
 *  - 数据库操作
 *  - 网络请求
 *
 */

public abstract class BasePresenter<T> {

    /**
     * 持有的View层引用
     */
    protected T mView;

    /**
     * 持有的Context若引用 避免内存泄漏
     */
    private WeakReference<Context> mContextWeakReference;

    /**
     * 需要批量解除订阅的Subscription集合 注意使用clear()如果使用unsubscribe()则之后也无法添加新的订阅
     */
    private CompositeSubscription mUiSubscriptions;

    /**
     * Realm引用
     */
    protected Realm mRealm;

    public BasePresenter(T view, Context context) {
        mView = view;
        mContextWeakReference = new WeakReference<>(context);
        mUiSubscriptions = new CompositeSubscription();
        mRealm = Realm.getDefaultInstance();
    }

    /**
     * 获取Context引用
     *
     * @return Context引用
     */
    protected Context getContext() {
        return mContextWeakReference.get();
    }

    /**
     * 获取Activity对象
     * @return Activity对象
     */
    protected Activity getActivity() {
        return (Activity) getContext();
    }

    /**
     * 获取Intent对象
     * @return Intent对象
     */
    protected Intent getIntent() {
        return getActivity().getIntent();
    }

    /**
     * 添加UI订阅
     *
     * @param subscription 订阅
     */
    protected void addUiSubscription(Subscription subscription) {
        mUiSubscriptions.add(subscription);
    }

    /**
     * 订阅指定类型的事件
     * @param event 事件类型class
     * @param <R> 类型
     * @return 订阅
     */
    protected <R> Observable<R> subscribeRxBusEvent(Class<R> event) {
        return Rxbus.getInstance().filterTypeAsObservable(event);
    }

    /**
     * 订阅指定类型的事件
     * @param event 事件类型class
     * @param <R> 类型
     * @return 在主线程上的订阅
     */
    protected <R> Observable<R> subScribeRxBusEventOnUiThread(Class<R> event) {
        return Rxbus.getInstance().filterTypeAsObservable(event).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 清空Rx订阅实践 避免在onPause之后更改UI界面
     */
    private void clearUiRequests() {
        if (mUiSubscriptions != null) {
            mUiSubscriptions.clear();
        }
    }

    /**
     * 关闭数据库
     */
    private void closeRealm() {
        if (mRealm != null) {
            mRealm.close();
        }
    }

    /**
     * 回收资源
     */
    public void release() {
        clearUiRequests();
        closeRealm();
    }

}
