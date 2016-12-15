package com.blazers.jandan.presenter.base;

import com.blazers.jandan.util.Rxbus;
import com.blazers.jandan.util.log.Log;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by blazers on 2016/11/11.
 * <p>
 * 主要包含:
 * - 数据库操作
 * - 网络请求
 */

public abstract class BasePresenter<T> {

    /**
     * 持有的View层引用
     */
    protected T mView;

    /**
     * 需要批量解除订阅的Subscription集合 注意使用clear()如果使用unsubscribe()则之后也无法添加新的订阅
     * <p>
     * 一个对应生命周期 onCreate -- onDestory
     * 另外一个对应    onResume -- onPause 只在前台生效
     */
    private CompositeSubscription mFullLifeTimeSubscriptions, mFrontUISubscriptions;


    /**
     * 是否处于前台运行状态 onResume <---- [Here] ----> onPause
     */
    private boolean mIsFullyVisible = true;

    public BasePresenter(T view) {
        mView = view;
    }


    /**
     * 添加全生命周周期的订阅 添加到此处的订阅会在onDestory自动解除订阅
     *
     * @param subscription 订阅
     */
    protected void addFLTSubscription(Subscription subscription) {
        if (mFullLifeTimeSubscriptions == null) {
            mFullLifeTimeSubscriptions = new CompositeSubscription();
        }
        mFullLifeTimeSubscriptions.add(subscription);
    }


    /**
     * 必须在onResume之后 onPause之前 添加 其中的订阅会在onPause之后自动解除订阅
     *
     * @param subscription 订阅
     */
    protected boolean addFUISubscription(Subscription subscription) {
        if (!mIsFullyVisible) {
            Log.e("[error]", "you can't add Front UI subscription unless the ui is between onResume() and onPause()");
            subscription.unsubscribe();
            return false;
        }
        if (mFrontUISubscriptions == null) {
            mFrontUISubscriptions = new CompositeSubscription();
        }
        mFrontUISubscriptions.add(subscription);
        return true;
    }

    /**
     * 订阅指定类型的事件
     *
     * @param event 事件类型class
     * @param <R>   类型
     * @return 订阅
     */
    protected <R> Observable<R> subscribeRxBusEvent(Class<R> event) {
        return Rxbus.getInstance().filterTypeAsObservable(event);
    }

    /**
     * 订阅指定类型的事件
     *
     * @param event 事件类型class
     * @param <R>   类型
     * @return 在主线程上的订阅
     */
    protected <R> Observable<R> subScribeRxBusEventOnUiThread(Class<R> event) {
        return Rxbus.getInstance().filterTypeAsObservable(event).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 进入界面
     */
    public void onResume() {
        mIsFullyVisible = true;
    }


    /**
     * 离开界面
     */
    public void onPause() {
        mIsFullyVisible = false;
        if (mFullLifeTimeSubscriptions != null) {
            mFullLifeTimeSubscriptions.clear();
        }
    }


    /**
     * 相当于析构函数 与 构造函数对应
     */
    public void onDestory() {
        // 清空订阅
        if (mFullLifeTimeSubscriptions != null) {
            // clear之后可以重用而unsubscribe之后新添加的也会立即unsubscribe
            mFullLifeTimeSubscriptions.unsubscribe();
            mFullLifeTimeSubscriptions = null;
        }
    }
}
