package com.blazers.jandan.models.db.local;

import com.blazers.jandan.models.db.sync.JokePost;
import com.blazers.jandan.util.DBHelper;
import com.blazers.jandan.util.TimeHelper;
import com.google.gson.annotations.Expose;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Blazers on 2015/11/12.
 */
public class LocalFavJokes extends RealmObject {

    @PrimaryKey
    @Expose
    private long comment_ID;
    @Expose
    private long favTime;
    // 减少查询代码
    private JokePost jokePost;

    /* Public APIs */
    public static boolean isThisFaved(Realm realm, long comment_ID) {
        return realm.where(LocalFavJokes.class).equalTo("comment_ID", comment_ID).findFirst() != null;
    }

    public static LocalFavJokes setThisFavedOrNot(boolean fav, Realm realm, long commentId) {
        return setThisFavedOrNot(fav, realm, commentId, TimeHelper.currentTime());
    }

    public static LocalFavJokes setThisFavedOrNot(boolean fav, Realm realm, long commentId, long time) {
        if (fav) {
            LocalFavJokes local = new LocalFavJokes();
            local.setComment_ID(commentId);
            local.setFavTime(time);
            JokePost jokePost = realm.where(JokePost.class).equalTo("comment_ID", commentId).findFirst();
            local.setJokePost(jokePost);
            DBHelper.saveToRealm(realm, local);
            return local;
        } else {
            LocalFavJokes local = realm.where(LocalFavJokes.class).equalTo("comment_ID", commentId).findFirst();
            DBHelper.removeFromRealm(realm, local);
            return null;
        }
    }

    public long getComment_ID() {
        return comment_ID;
    }

    public void setComment_ID(long comment_ID) {
        this.comment_ID = comment_ID;
    }

    public long getFavTime() {
        return favTime;
    }

    public void setFavTime(long favTime) {
        this.favTime = favTime;
    }

    public JokePost getJokePost() {
        return jokePost;
    }

    public void setJokePost(JokePost jokePost) {
        this.jokePost = jokePost;
    }
}
