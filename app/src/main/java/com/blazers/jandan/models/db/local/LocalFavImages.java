package com.blazers.jandan.models.db.local;

import com.blazers.jandan.util.DBHelper;
import com.blazers.jandan.util.TimeHelper;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Blazers on 2015/11/12.
 */
public class LocalFavImages extends RealmObject {
    @PrimaryKey
    private String url;
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

    public static void setThisFavedOrNot(boolean fav, Realm realm, String url) {
        if (fav) {
            LocalFavImages local = new LocalFavImages();
            local.setUrl(url);
            local.setFavTime(TimeHelper.currentTime());
            DBHelper.saveToRealm(realm, local);
        } else {
            LocalFavImages local = realm.where(LocalFavImages.class).equalTo("url", url).findFirst();
            DBHelper.removeFromRealm(realm, local);
        }
    }
}
