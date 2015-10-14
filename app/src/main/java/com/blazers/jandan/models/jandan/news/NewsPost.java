package com.blazers.jandan.models.jandan.news;

import com.google.gson.annotations.SerializedName;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Blazers on 2015/9/1.
 */
public class NewsPost extends RealmObject implements Serializable {
    @PrimaryKey
    private long id;
    private String url;
    private String title;
    private String date;
    /* Parse */
    private String thumbUrl;
    private long views;
    private String tagTitle; // tags 可为多个 因为是JSONArray形式存在
    private String authorName;
    private String html;        // 应当做成本地缓存 便于清理
    private long page;               // 所属的页码


    /* Getter & Setter */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public String getTagTitle() {
        return tagTitle;
    }

    public void setTagTitle(String tagTitle) {
        this.tagTitle = tagTitle;
    }



    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }


    
    /* APIs */
    public static List<NewsPost> getAllPost(Realm realm, long page) {
        return realm.where(NewsPost.class).equalTo("page", page).findAllSorted("id", false);
    }

    public static String getPostHtmlById(Realm realm, long id) {
        NewsPost post = getPostById(realm, id);
        return post == null? null : post.html;
    }

    public static NewsPost getPostById(Realm realm, long id) {
        return realm.where(NewsPost.class).equalTo("id", id).findFirst();
    }

    public static NewsPost copySimplePost(NewsPost copy) {
        NewsPost post = new NewsPost();
        post.title = copy.title;
        post.url = copy.url;
        post.html = copy.html;
        return post;
    }
}
