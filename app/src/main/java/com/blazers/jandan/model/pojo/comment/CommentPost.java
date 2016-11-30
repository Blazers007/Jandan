package com.blazers.jandan.model.pojo.comment;

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
    //
    public String message;
    public String created_at; //2015-10-12T21:51:56+08:00
    public List<String> parents;

    // Nested
    public Author author;
}
