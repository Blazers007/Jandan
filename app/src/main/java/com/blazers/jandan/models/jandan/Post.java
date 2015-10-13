package com.blazers.jandan.models.jandan;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
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
    private long comment_ID;         // 每一贴的唯一ID
    private long comment_post_ID;    // 每次发帖的ID 每次发帖可发多张帖子
    private String comment_author;   // 作者
    private String comment_date;     // 发布日期
    private String vote_positive;    // OO数量
    private String vote_negative;    // XX数量
    private String text_content;     // 文本内容
    /* By Setter */
    private int image_size;          // 含有的图片数量
    private String type;                // 所属的类型 或joke 图片 或 妹子图
    private RealmList<Image> images; // 包含的图片
    private long page;
    private int commentNumber;
    /* Getter & Setter */

    public long getComment_ID() {
        return comment_ID;
    }

    public void setComment_ID(long comment_ID) {
        this.comment_ID = comment_ID;
    }

    public long getComment_post_ID() {
        return comment_post_ID;
    }

    public void setComment_post_ID(long comment_post_ID) {
        this.comment_post_ID = comment_post_ID;
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

    public String getVote_positive() {
        return vote_positive;
    }

    public void setVote_positive(String vote_positive) {
        this.vote_positive = vote_positive;
    }

    public String getVote_negative() {
        return vote_negative;
    }

    public void setVote_negative(String vote_negative) {
        this.vote_negative = vote_negative;
    }

    public String getText_content() {
        return text_content;
    }

    public void setText_content(String text_content) {
        this.text_content = text_content;
    }

    public int getImage_size() {
        return image_size;
    }

    public void setImage_size(int image_size) {
        this.image_size = image_size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RealmList<Image> getImages() {
        return images;
    }

    public void setImages(RealmList<Image> images) {
        this.images = images;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public int getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }

    /* APIs */
    public static List<Post> getImagePosts(Realm realm, long page, String type) {
        RealmResults<Post> onePage =  realm.where(Post.class).equalTo("type", type).equalTo("page", page).findAllSorted("comment_ID", false);
        if (onePage != null && onePage.size() > 0) {
            return onePage.subList(0, onePage.size() > 20 ? 20 : onePage.size());
        }
        return null;
    }

    public static List<Image> getAllImages(Realm realm, long page, String type) {
        List<Image> imageList = new ArrayList<>();
        List<Post> results = getImagePosts(realm, page, type);
        if (results != null){
            for (Post post : results)
                imageList.addAll(post.getImages());
        }
        return imageList;
    }
}
