package com.blazers.jandan.model.news;

import io.realm.RealmObject;

/**
 * Created by blazers on 2016/11/30.
 */
public class CategoriesBean extends RealmObject {
    /**
     * id : 1
     * slug : news
     * title : NEWS
     * description : 新鲜事
     * parent : 0
     * post_count : 52261
     */

    public int id;
    public String slug;
    public String title;
    public String description;
    public int parent;
    public int post_count;
}
