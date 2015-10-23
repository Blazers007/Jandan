package com.blazers.jandan.models.pojo.comment;

import java.util.List;

/**
 * Created by Blazers on 2015/10/13.
 *
 * 评论的主体对象
 *
 */
public class CommentPost {

    public static final int HOT_DIVIDER = 1, NORMAL_DIVIDER = 2, POST = 3;
    // For Divider
    public int _type;
    public String _dividerName;

    public String post_id; //"6204830194721915649"
    public String thread_id; //"6204757592596103937"
    public String status;

    public String message;
    public String created_at; //2015-10-12T21:51:56+08:00

//    public String parent_id;
//    public String root_id;
    public long reposts;
    public long comments;

    public long likes;
    public long dislikes;

    public String author_id;
    public String author_key;

    public List<String> parents;

    // Nested
    public Author author;
}
