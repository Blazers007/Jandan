package com.blazers.jandan.util;

import android.content.Context;
import io.realm.Realm;
import io.realm.RealmObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Blazers on 2015/10/26.
 */
public class DBHelper {

    private static final List<Realm> realms = new ArrayList<>();

    /**
     * 存储数组到数据库中
     * */
    public static <E extends RealmObject> void saveToRealm(Realm realm, Iterable<E> objects) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(objects);
        realm.commitTransaction();
    }

    /**
     * 存储数组到数据库中 2
     * */
    public static <E extends RealmObject> void saveToRealm(Context context, Iterable<E> objects) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(objects);
        realm.commitTransaction();
        realm.close();
    }

    /**
     * 存储单个到数据库中
     * */
    public static <E extends RealmObject> void saveToRealm(Realm realm, E object) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(object);
        realm.commitTransaction();
    }

    /**
     * 存储单个到数据库中 2
     * */
    public static <E extends RealmObject> void saveToRealm(Context context, E object) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(object);
        realm.commitTransaction();
        realm.close();
    }


    /**
     * 移除某一行
     * */
    public static <E extends RealmObject> void removeFromRealm(Realm realm, E object) {
        if (null != object) {
            realm.beginTransaction();
            object.deleteFromRealm();
            realm.commitTransaction();
        }
    }

    /**
     * 缓存Realm在退出的时候释放掉
     * */
    public static Realm generateTempRealm(Context context) {
        Realm realm = Realm.getDefaultInstance();
        realms.add(realm);
        return realm;
    }

    /**
     *
     * */
    public static void releaseAllTempRealm() {
        for (Realm realm : realms)
            realm.close();
    }
}
