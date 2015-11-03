package com.blazers.jandan.rxbus.event;

/**
 * Created by Blazers on 2015/10/28.
 */
public class CommentEvent {

    // Id >= 0 正常 否则弹出
    public long id;

    public CommentEvent(long id) {
        this.id = id;
    }
}
