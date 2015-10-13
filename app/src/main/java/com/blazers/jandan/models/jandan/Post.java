package com.blazers.jandan.models.jandan;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blazers on 15/8/25.
 *
 * JSON 转数据库持久化
 *
 */
public class Post extends RealmObject {

    /* From JSON */
    @PrimaryKey
    public long comment_ID;         // 每一贴的唯一ID
    public long comment_post_ID;    // 每次发帖的ID 每次发帖可发多张帖子
    public String comment_author;   // 作者
    public String comment_date;     // 发布日期
    public String vote_positive;    // OO数量
    public String vote_negative;    // XX数量
    public String text_content;     // 文本内容
    /* By Setter */
    public int image_size;          // 含有的图片数量
    public long page;               // 所属的页码
    public String type;                // 所属的类型 或joke 图片 或 妹子图
    public RealmList<Image> images; // 包含的图片


    /* APIs */
    public static List<Post> getImagePosts(Realm realm, long page, String type) {
        return realm.where(Post.class).equalTo("page", page).equalTo("type", type).findAllSorted("comment_ID", false);
    }

    public static List<Image> getAllImages(Realm realm, long page, String type) {
        List<Image> imageList = new ArrayList<>();
        for (Post post : getImagePosts(realm, page, type)) {
            imageList.addAll(post.images);
        }
        return imageList;
    }
}
