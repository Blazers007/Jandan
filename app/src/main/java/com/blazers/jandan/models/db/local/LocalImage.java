package com.blazers.jandan.models.db.local;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Blazers on 2015/10/21.
 */
public class LocalImage extends RealmObject {

    @PrimaryKey
    private String url;
    private String localUrl;
    private long width;
    private long height;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    /* Public APIs */
    public static LocalImage getLocalImageByWebUrl(Realm realm, String url) {
        return realm.where(LocalImage.class).equalTo("url", url).findFirst();
    }
}
