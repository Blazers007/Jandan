package com.blazers.jandan.orm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Blazers on 2015/8/27.
 */
public class Picture extends RealmObject {

    @PrimaryKey
    private String comment_ID_index;// Belong to group and index split by _
    private String url;     // Url
    private String localUrl;// Local Storage
    private Meizi meizi;    // Relationship

    public String getComment_ID_index() {
        return comment_ID_index;
    }

    public void setComment_ID_index(String comment_ID_index) {
        this.comment_ID_index = comment_ID_index;
    }

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

    public Meizi getMeizi() {
        return meizi;
    }

    public void setMeizi(Meizi meizi) {
        this.meizi = meizi;
    }
}
