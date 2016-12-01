package com.blazers.jandan.presenter.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blazers.jandan.util.Rxbus;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

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
     *
     * 一个对应生命周期 onCreate -- onDestory
     * 另外一个对应    onResume -- onPause 只在前台生效
     */
    private CompositeSubscription mFullLifeTimeSubscriptions, mFrontUISubscriptions;

    /**
     * Realm引用
     */
    private Realm mRealm;

    /**
     * SnappyDB
     */
    private DB mDB;

    /**
     * 是否处于前台运行状态 onResume <---- [Here] ----> onPause
     */
    private boolean mIsFullyVisible = false;

    public BasePresenter(T view, Context context) {
        mView = view;
        try {
            mDB = DBFactory.open(context);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        mContextWeakReference = new WeakReference<>(context);
    }

    /**
     * 获取Context引用
     *
     * @return Context引用
     */
    public Context getContext() {
        return mContextWeakReference.get();
    }

    /**
     * 获取Activity对象
     * @return Activity对象
     */
    public Activity getActivity() {
        return (Activity) getContext();
    }

    /**
     * 获取Intent对象
     * @return Intent对象
     */
    public Intent getIntent() {
        return getActivity().getIntent();
    }

    /**
     * 获取Realm对象
     * @return Realm对象
     */
    public Realm getRealm() {
        if (mRealm == null || mRealm.isClosed()) {
            mRealm = Realm.getDefaultInstance();
        }
        return mRealm;
    }

    public DB getDB() {
        return mDB;
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
    protected void addFUISubscription(Subscription subscription) {
        if (!mIsFullyVisible) {
            Log.e("[error]", "you can't add Front UI subscription unless the ui is between onResume() and onPause()");
            return;
        }
        if (mFrontUISubscriptions == null) {
            mFrontUISubscriptions = new CompositeSubscription();
        }
        mFrontUISubscriptions.add(subscription);
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
        // 关闭数据库
        if (mRealm != null && !mRealm.isClosed()) {
            mRealm.close();
            mRealm = null;
        }
        try {
            if (mDB != null && mDB.isOpen()) {
                mDB.close();
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }
}
