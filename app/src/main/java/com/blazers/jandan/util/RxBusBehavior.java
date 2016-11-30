package com.blazers.jandan.util;

import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by blazers on 2016/11/29.
 * 订阅的时候会先接收到上一次发送的消息, 用于实时更新的页面由于隐藏而暂时解除订阅之后，转为可见状态时能够迅速显示最新的状态.
 * eg: 金融类app需要显示行情的fragment，采用该类进行行情转发从而能保证订阅的同时尽可能显示最新数据。
 */

public class RxBusBehavior {

    private static RxBusBehavior INSTANCE;
    private final Subject<Object, Object> _bus = new SerializedSubject<>(BehaviorSubject.create());

    private RxBusBehavior() {
    }

    public static RxBusBehavior getInstance() {
        if (null == INSTANCE)
            INSTANCE = new RxBusBehavior();
        return INSTANCE;
    }

    public void send(Object o) {
        _bus.onNext(o);
    }

    public Observable<Object> toObservable() {
        return _bus;
    }

    /**
     * Filter 指定类型的事件
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> Observable<T> filterTypeAsObservable(Class<T> eventType) {
        return _bus.ofType(eventType);
    }

    public boolean hasObservers() {
        return _bus.hasObservers();
    }
}
