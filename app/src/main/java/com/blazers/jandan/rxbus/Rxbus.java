package com.blazers.jandan.rxbus;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by Blazers on 2015/10/28.
 *
 * http://nerds.weddingpartyapp.com/tech/2014/12/24/implementing-an-event-bus-with-rxjava-rxbus/
 *
 */
public class Rxbus {

    private static Rxbus INSTANCE;

    private Rxbus(){}

    public static Rxbus getInstance() {
        if (null == INSTANCE)
            INSTANCE = new Rxbus();
        return INSTANCE;
    }

    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());

    public void send(Object o) {
        _bus.onNext(o);
    }

    public Observable<Object> toObservable() {
        return _bus;
    }

    public boolean hasObservers() {
        return _bus.hasObservers();
    }
}
