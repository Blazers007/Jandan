package com.blazers.jandan.model.database.sync;

import com.blazers.jandan.model.pojo.image.ImageRelateToPost;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by Blazers on 15/8/25.
 * <p>
 * JSON 转数据库持久化
 */
@RealmClass
public class ImagePost extends RealmObject {

    /* From JSON */
    @PrimaryKey
    private long comment_ID;         // 每一贴的唯一ID
    private long comment_post_ID;    // 每次发帖的ID 每次发帖可发多张帖子
    private String comment_author;   // 作者
    private String comment_date;     // 发布日期
    private String vote_positive;    // OO数量
    private String vote_negative;    // XX数量
    private String text_content;     // 文本内容
    private String picsArray;        // 目前Realm暂不支持List<String>
    /* By Setter */
    private int image_size;          // 含有的图片数量
    private String type;                // 所属的类型 或joke 图片 或 妹子图
    private long page;
    private int commentNumber;
    /* Getter & Setter */

    /* APIs */
    public static List<ImagePost> getImagePosts(Realm realm, long page, String type) {
        return realm.where(ImagePost.class).equalTo("type", type).equalTo("page", page).findAllSorted("comment_date", Sort.DESCENDING);
    }

    /**
     * 从数据库中读取图片数据
     */
    public static List<ImageRelateToPost> getAllImagesFromDB(Realm realm, long page, String type) {
        List<ImageRelateToPost> imageRelateToPostList = new ArrayList<>();
        List<ImagePost> posts = getImagePosts(realm, page, type);
        if (null != posts) {
            for (ImagePost post : posts) {
                if (null != post.getPicsArray()) {
                    String[] pics = post.getPicsArray().split(",");
                    for (String url : pics) {
                        ImageRelateToPost image = new ImageRelateToPost(post, url);
                        imageRelateToPostList.add(image);
                    }
                }
            }
        }
        return imageRelateToPostList;
    }

    /**
     * 直接从ImagePost列表中提取图片数据  TODO:修改与上一个方法 讨论哪一个更加合适
     */
    public static List<ImageRelateToPost> getAllImageFromList(List<ImagePost> posts) {
        List<ImageRelateToPost> imageRelateToPostList = new ArrayList<>();
        if (null != posts) {
            for (ImagePost post : posts) {
                if (null != post.getPicsArray()) {
                    String[] pics = post.getPicsArray().split(",");
                    for (String url : pics) {
                        ImageRelateToPost image = new ImageRelateToPost(post, url);
                        imageRelateToPostList.add(image);
                    }
                }
            }
        }
        return imageRelateToPostList;
    }

    /**
     * 从列表获取可以供下载的对象
     */

    public static int calculateImageSize(List<ImagePost> posts) {
        int sum = 0;
        for (ImagePost post : posts) {
            sum += post.getPicsArray().split(",").length;
        }
        return sum;
    }

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

    public String getPicsArray() {
        return picsArray;
    }

    public void setPicsArray(String picsArray) {
        this.picsArray = picsArray;
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
}
