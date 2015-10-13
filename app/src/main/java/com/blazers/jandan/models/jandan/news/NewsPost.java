package com.blazers.jandan.models.jandan.news;

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
    public long id;
    public String url;
    public String title;
    public String date;


    public String thumbUrl;
    public long views;
    public String tagTitle; // tags 可为多个 因为是JSONArray形式存在
    public String author;
    public String html;


    public long page;               // 所属的页码

    
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
