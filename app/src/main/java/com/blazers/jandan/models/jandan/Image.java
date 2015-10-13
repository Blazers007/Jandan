package com.blazers.jandan.models.jandan;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

import java.util.List;

/**
 * Created by Blazers on 2015/8/27.
 */
public class Image extends RealmObject {

    @PrimaryKey
    public long id;
    public String url;
    public String localUrl;
    public String size;
    // Relationship
    public Post post;


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
