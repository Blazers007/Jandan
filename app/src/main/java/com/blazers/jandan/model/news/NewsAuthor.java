package com.blazers.jandan.model.news;

import io.realm.RealmObject;

/**
 * Created by blazers on 2016/12/7.
 */

public class NewsAuthor extends RealmObject {
    /**
     * id : 587
     * slug : cedric
     * name : 许叔
     * first_name : Cedric
     * last_name : Hsu
     * nickname : 许叔
     * url : http://weibo.com/cedrichsu
     * description :
     */

    public int id;
    public String slug;
    public String name;
    public String first_name;
    public String last_name;
    public String nickname;
    public String url;
    public String description;
}
