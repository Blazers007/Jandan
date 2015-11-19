package com.blazers.jandan.models.db.local;

import com.blazers.jandan.util.DBHelper;
import com.blazers.jandan.util.TimeHelper;
import com.google.gson.annotations.Expose;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

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
}
