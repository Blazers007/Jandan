package com.blazers.jandan.models.db.local;

import android.os.Parcelable;
import com.blazers.jandan.util.DBHelper;
import com.blazers.jandan.util.TimeHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blazers on 2015/11/12.
 */
public class LocalFavImages extends RealmObject {

    @PrimaryKey @Expose
    private String url;
    @Expose
    private long favTime;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getFavTime() {
        return favTime;
    }

    public void setFavTime(long favTime) {
        this.favTime = favTime;
    }

    /* Public APIs */
    public static boolean isThisFaved(Realm realm, String url) {
        return realm.where(LocalFavImages.class).equalTo("url", url).findFirst() != null;
    }

    public static LocalFavImages setThisFavedOrNot(boolean fav, Realm realm, String url) {
        return setThisFavedOrNot(fav, realm, url, TimeHelper.currentTime());
    }

    public static LocalFavImages setThisFavedOrNot(boolean fav, Realm realm, String url, long time) {
        if (fav) {
            LocalFavImages local = new LocalFavImages();
            local.setUrl(url);
            local.setFavTime(time);
            DBHelper.saveToRealm(realm, local);
            return local;
        } else {
            LocalFavImages local = realm.where(LocalFavImages.class).equalTo("url", url).findFirst();
            DBHelper.removeFromRealm(realm, local);
            return null;
        }
    }

    public static void setTheseFavedOrNot(boolean fav, Realm realm, List<String> urls, List<Long> times) {
        if (fav) {
            List<LocalFavImages> localFavImages = new ArrayList<>();
            for (int i = 0 ; i < urls.size() ; i++ ) {
                LocalFavImages local = new LocalFavImages();
                local.setUrl(urls.get(i));
                local.setFavTime(times.get(i));
                localFavImages.add(local);
            }
            DBHelper.saveToRealm(realm, localFavImages);
        } else {
            for (int i = 0 ; i < urls.size() ; i++ ) {
                DBHelper.removeFromRealm(realm, realm.where(LocalFavImages.class).equalTo("url", urls.get(i)).findFirst());
            }
        }
    }

    /**
     * Translate to json data 因为查询出来的是代理类 无法直接转化为GSON对象
     * */
    public static String translateToJson(List<LocalFavImages> list) {
        JsonArray array = new JsonArray();
        for (LocalFavImages item : list) {
            JsonObject object = new JsonObject();
            object.addProperty("url", item.getUrl());
            array.add(object);
        }
        return array.toString();
    }

}
