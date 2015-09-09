package com.blazers.jandan.orm.meizi;

import com.blazers.jandan.common.URL;
import com.blazers.jandan.orm.PictureInterface;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Blazers on 2015/8/27.
 */
public class Picture extends RealmObject implements PictureInterface{

    @PrimaryKey
    private String comment_ID_index;// Belong to group and index split by _
    private String url;     // Url
    private String localUrl;// Local Storage
    private Meizi meizi;    // Relationship

    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public RealmObject getOrmObject() {
        return this;
    }

    @Override
    public String getUrlByQulity(URL.HUABAN_QULITY qulity) {
        return url;
    }
    
    /* APIs */
    /* APIS */
    public static RealmResults<Picture> findAllSortDesc(Realm realm, String type) {
        return realm.where(Picture.class).equalTo("type", type).findAllSorted("comment_ID_index", false);
    }

    public static RealmResults<Picture> loadMoreLessThan(Realm realm, String type, long last) {
        return realm.where(Picture.class).lessThan("pin_id", last).findAll();
    }
}
