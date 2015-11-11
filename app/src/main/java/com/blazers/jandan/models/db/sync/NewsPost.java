package com.blazers.jandan.models.db.sync;

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
    // Page
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

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    /* APIs */
    public static List<NewsPost> getAllPost(Realm realm, long page) {
        return realm.where(NewsPost.class).equalTo("page", page).findAllSorted("date", false);
    }


    public static NewsPost getPostById(Realm realm, long id) {
        return realm.where(NewsPost.class).equalTo("id", id).findFirst();
    }

}
