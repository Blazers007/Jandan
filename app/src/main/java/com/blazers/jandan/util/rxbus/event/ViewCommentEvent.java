package com.blazers.jandan.util.rxbus.event;

import java.io.Serializable;

/**
 * Created by Blazers on 2015/10/28.
 */
public class ViewCommentEvent implements Serializable {

    public static final String KEY = "CommentEvent";

    // Id >= 0 正常 否则弹出
    public long id;

    public ViewCommentEvent(long id) {
        this.id = id;
    }
}
