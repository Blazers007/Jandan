package com.blazers.jandan.models.jandan.local;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by Blazers on 2015/9/11.
 */
public class LocalStorageFlag extends RealmObject {

    private String type;
    private long page;
    private boolean complete;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    /* API */
    public static LocalStorageFlag getFlag(Realm realm, long page, String type) {
        return realm.where(LocalStorageFlag.class).equalTo("page", page).equalTo("type", type).findFirst();
    }

    public static boolean isLocalStorageComplete(Realm realm, long page, String type) {
        LocalStorageFlag flag = getFlag(realm, page, type);
        return flag != null && flag.isComplete();
    }
}
