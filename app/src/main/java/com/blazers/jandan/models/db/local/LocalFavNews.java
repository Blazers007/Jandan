package com.blazers.jandan.models.db.local;

import com.blazers.jandan.models.db.sync.NewsPost;
import com.blazers.jandan.util.DBHelper;
import com.blazers.jandan.util.TimeHelper;
import com.google.gson.annotations.Expose;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Blazers on 2015/11/12.
 */
public class LocalFavNews extends RealmObject {

    @PrimaryKey
    @Expose
    private long id;
    @Expose
    private long favTime;
    private NewsPost newsPost;

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

    public NewsPost getNewsPost() {
        return newsPost;
    }

    public void setNewsPost(NewsPost newsPost) {
        this.newsPost = newsPost;
    }

    /* Public APIs */
    public static boolean isThisFaved(Realm realm, long id) {
        return realm.where(LocalFavNews.class).equalTo("id", id).findFirst() != null;
    }

    public static LocalFavNews setThisFavedOrNot(boolean fav, Realm realm, long id) {
        return setThisFavedOrNot(fav, realm, id, TimeHelper.currentTime());
    }

    public static LocalFavNews setThisFavedOrNot(boolean fav, Realm realm, long id, long time) {
        if (fav)  {
            LocalFavNews local = new LocalFavNews();
            local.setId(id);
            local.setFavTime(time);
            NewsPost post = realm.where(NewsPost.class).equalTo("id", id).findFirst();
            local.setNewsPost(post);
            DBHelper.saveToRealm(realm, local);
            return local;
        } else {
            LocalFavNews local = realm.where(LocalFavNews.class).equalTo("id", id).findFirst();
            DBHelper.removeFromRealm(realm, local);
            return null;
        }
    }
}
