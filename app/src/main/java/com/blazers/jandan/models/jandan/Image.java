package com.blazers.jandan.models.jandan;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Blazers on 2015/8/27.
 */
public class Image extends RealmObject {

    @PrimaryKey
    private long id;
    private String url;
    private String localUrl;
    private String size;

    private ImagePost post;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public ImagePost getPost() {
        return post;
    }

    public void setPost(ImagePost posts) {
        this.post = posts;
    }

    /* APIS */
    public static List<Image> findAllSortDesc(Realm realm, String type) { // default get 20
        RealmResults<Image> images = realm.where(Image.class).equalTo("type", type).findAllSorted("comment_ID_index", false);
        return images.subList(0, images.size() > 20 ? 20 : images.size());
    }

    public static List<Image> loadMoreLessThan(Realm realm, String type, long last) {
        RealmResults<Image> images = realm.where(Image.class).equalTo("type", type).lessThan("comment_ID_index", last).findAll();
        return images.subList(0, images.size() > 20 ? 20 : images.size());
    }

}
