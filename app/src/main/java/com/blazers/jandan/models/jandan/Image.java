package com.blazers.jandan.models.jandan;

import com.blazers.jandan.models.local.OSBSImage;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blazers on 2015/8/27.
 */
public class Image extends RealmObject {

    @PrimaryKey
    private long comment_ID_index;    // 图片的主键 目前采用 POSTID _ 顺序组成
    private ImagePost post;             // 关联关系
    private String type;                // 所属的类型
    private OSBSImage image;            // 与本地简单图片库对应关系

    public long getComment_ID_index() {
        return comment_ID_index;
    }

    public void setComment_ID_index(long comment_ID_index) {
        this.comment_ID_index = comment_ID_index;
    }

    public ImagePost getPost() {
        return post;
    }

    public void setPost(ImagePost post) {
        this.post = post;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OSBSImage getImage() {
        return image;
    }

    public void setImage(OSBSImage image) {
        this.image = image;
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
