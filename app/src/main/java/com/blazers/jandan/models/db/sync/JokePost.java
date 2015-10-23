package com.blazers.jandan.models.db.sync;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Blazers on 2015/9/7.
 */
public class JokePost extends RealmObject implements Serializable{
    @PrimaryKey
    private long comment_ID;
    private String comment_content;
    private String comment_author;
    private String comment_date;
    // 页码
    private long page;

    public long getComment_ID() {
        return comment_ID;
    }

    public void setComment_ID(long comment_ID) {
        this.comment_ID = comment_ID;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getComment_author() {
        return comment_author;
    }

    public void setComment_author(String comment_author) {
        this.comment_author = comment_author;
    }

    public String getComment_date() {
        return comment_date;
    }

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    /* APIs */
    public static List<JokePost> getAllPost(Realm realm, int page) {
        return realm.where(JokePost.class).equalTo("page", page).findAllSorted("comment_ID", false);
    }
}
