package com.blazers.jandan.model.news;

import io.realm.RealmObject;

/**
 * Created by blazers on 2016/11/30.
 */
public class CommentsBean extends RealmObject {
    /**
     * id : 3336568
     * name : 海狸
     * url :
     * date : 2016-11-30 15:36:21
     * content : <p>今天天气不错</p>

     * parent : 0
     * vote_positive : 0
     * vote_negative : 0
     * index : 1
     */

    public int id;
    public String name;
    public String url;
    public String date;
    public String content;
    public int parent;
    public int vote_positive;
    public int vote_negative;
    public int index;
}
