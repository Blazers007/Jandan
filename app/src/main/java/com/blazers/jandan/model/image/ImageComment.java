package com.blazers.jandan.model.image;

import com.blazers.jandan.model.RealmString;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by blazers on 2016/12/8.
 */
public class ImageComment extends RealmObject {
    /**
     * comment_ID : 3342627
     * comment_post_ID : 26402
     * comment_author : 和我咬
     * comment_author_email : kclo321@gmail.com
     * comment_author_url :
     * comment_author_IP : 61.140.108.248
     * comment_date : 2016-12-08 14:31:59
     * comment_date_gmt : 2016-12-08 06:31:59
     * comment_content : <img src="https://ws3.sinaimg.cn/mw1024/b05bd106jw1e09er7liveg.gif" />
     * comment_karma : 0
     * comment_approved : 1
     * comment_agent : Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36
     * comment_type :
     * comment_parent : 0
     * user_id : 0
     * comment_subscribe : N
     * comment_reply_ID : 0
     * vote_positive : 3
     * vote_negative : 0
     * vote_ip_pool :
     * text_content :
     * pics : ["https://ws3.sinaimg.cn/mw1024/b05bd106jw1e09er7liveg.gif"]
     * videos : []
     */
    @PrimaryKey
    public String comment_ID;
    public String comment_post_ID;
    public String comment_author;
    public String comment_author_email;
    public String comment_author_url;
    public String comment_author_IP;
    public String comment_date;
    public String comment_date_gmt;
    public String comment_content;
    public String comment_karma;
    public String comment_approved;
    public String comment_agent;
    public String comment_type;
    public String comment_parent;
    public String user_id;
    public String comment_subscribe;
    public String comment_reply_ID;
    public String vote_positive;
    public String vote_negative;
    public String vote_ip_pool;
    public String text_content;
    public RealmList<RealmString> pics;
}
