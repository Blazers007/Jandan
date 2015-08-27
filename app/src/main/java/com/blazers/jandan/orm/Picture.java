package com.blazers.jandan.orm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Blazers on 2015/8/27.
 */
public class Picture extends RealmObject {

    private long comment_ID;// Belong to group
    private int index;      // Inner group index
    private String url;     // Url
    private Meizi meizi;    // Relationship

    public long getComment_ID() {
        return comment_ID;
    }

    public void setComment_ID(long comment_ID) {
        this.comment_ID = comment_ID;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Meizi getMeizi() {
        return meizi;
    }

    public void setMeizi(Meizi meizi) {
        this.meizi = meizi;
    }
}
