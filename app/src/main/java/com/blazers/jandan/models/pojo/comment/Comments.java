package com.blazers.jandan.models.pojo.comment;

import com.google.gson.JsonObject;

import java.util.List;

/**
 * Created by Blazers on 2015/10/13.
 */
public class Comments {
    public List<String> response;      // 回复的IDs  貌似没什么大用
    public List<String> hotPosts;      // 热门评论的IDs
    public JsonObject parentPosts;         // 评论 用ID取出相应的JSON
    public Thread thread;
}
