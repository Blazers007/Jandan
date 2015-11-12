package com.blazers.jandan.models.db.local;

import com.blazers.jandan.util.DBHelper;
import com.blazers.jandan.util.TimeHelper;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Blazers on 2015/11/12.
 */
public class LocalFavNews extends RealmObject {
    @PrimaryKey
    private long id;
    private long favTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFavTime() {
        return favTime;
    }

    public void setFavTime(long favTime) {
        this.favTime = favTime;
    }

    /* Public APIs */
    public static boolean isThisFaved(Realm realm, long id) {
        return realm.where(LocalFavNews.class).equalTo("id", id).findFirst() != null;
    }

    public static void setThisFavedOrNot(boolean fav, Realm realm, long id) {
        if (fav)  {
            LocalFavNews local = new LocalFavNews();
            local.setId(id);
            local.setFavTime(TimeHelper.currentTime());
            DBHelper.saveToRealm(realm, local);
        } else {
            LocalFavNews local = realm.where(LocalFavNews.class).equalTo("id", id).findFirst();
            DBHelper.removeFromRealm(realm, local);
        }
    }
}
