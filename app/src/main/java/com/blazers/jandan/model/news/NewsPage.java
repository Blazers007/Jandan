package com.blazers.jandan.model.news;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by blazers on 2016/11/30.
 */

public class NewsPage extends RealmObject {

    @PrimaryKey
    public int current_page;
    public String status;
    public int count;
    public int count_total;
    public int pages;
    public RealmList<PostsBean> posts;
}
