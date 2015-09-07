package com.blazers.jandan.orm.joke;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Blazers on 2015/9/7.
 */
public class Joke extends RealmObject {
    @PrimaryKey
    private long comment_ID;
    private String comment_contnet;
    private String comment_author;
    private String comment_date;

    public long getComment_ID() {
        return comment_ID;
    }

    public void setComment_ID(long comment_ID) {
        this.comment_ID = comment_ID;
    }

    public String getComment_contnet() {
        return comment_contnet;
    }

    public void setComment_contnet(String comment_contnet) {
        this.comment_contnet = comment_contnet;
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
}
