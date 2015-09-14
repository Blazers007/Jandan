package com.blazers.jandan.models.jandan;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blazers on 2015/9/1.
 */
public class NewsPost extends RealmObject {
    @PrimaryKey
    private long id;
    private String thumbUrl;
    private String title;
    private String date;
    private long views;
    private String tagTitle; // tags 可为多个 因为是JSONArray形式存在
    private String author;
    private String html;

    private String url;
    private long page;               // 所属的页码

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        return post == null? null : post.getHtml();
    }

    public static NewsPost getPostById(Realm realm, long id) {
        return realm.where(NewsPost.class).equalTo("id", id).findFirst();
    }

    public static NewsPost copySimplePost(NewsPost copy) {
        NewsPost post = new NewsPost();
        post.setTitle(copy.getTitle());
        post.setUrl(copy.getUrl());
        post.setHtml(copy.getHtml());
        return post;
    }
}
