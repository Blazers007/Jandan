package com.blazers.jandan.model.joke;

import com.blazers.jandan.model.RealmString;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by blazers on 2016/12/8.
 */
public class JokeComment extends RealmObject {
    /**
     * comment_ID : 3342612
     * comment_post_ID : 55592
     * comment_author : 六小0童
     * comment_author_email : raymanwen@gmail.com
     * comment_author_url :
     * comment_author_IP : 116.226.121.105
     * comment_date : 2016-12-08 14:08:04
     * comment_date_gmt : 2016-12-08 06:08:04
     * comment_content : 一男子走进保健品店“帮我拿一盒TT，要黑色的”店员看了看他说：“先生，彩色的不是更有情趣吗”男子叹息了一声“哎，领导去世了，我去安慰嫂子，黑色的显得庄重”
     * comment_karma : 0
     * comment_approved : 1
     * comment_agent : Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36
     * comment_type :
     * comment_parent : 0
     * user_id : 0
     * comment_subscribe : N
     * comment_reply_ID : 0
     * vote_positive : 6
     * vote_negative : 7
     * vote_ip_pool :
     * text_content : 一男子走进保健品店“帮我拿一盒TT，要黑色的”店员看了看他说：“先生，彩色的不是更有情趣吗”男子叹息了一声“哎，领导去世了，我去安慰嫂子，黑色的显得庄重”
     * videos : []
     * pics : ["http://ww2.sinaimg.cn/mw600/86792da5gw1fainxs5sigj20r01pwwob.jpg"]
     */
    public static final String ID = "comment_ID";
    @PrimaryKey
    public String comment_ID;
    public int page;
    public boolean favorite;
    public Date favorite_time;
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
    //    public List<?> videos;
    public RealmList<RealmString> pics;
}
