package com.blazers.jandan.model.news;

import io.realm.RealmObject;

/**
 * Created by blazers on 2016/12/7.
 */

public class NewsCommentsRank extends RealmObject {
    /**
     * id : 3341879
     * name : 茶苯海明
     * url :
     * date : 2016-12-07 16:09:20
     * content : <p>第一版〈理论力学〉，比圣经更伟大的著作</p>

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
