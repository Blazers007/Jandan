package com.blazers.jandan.rxbus.event;

/**
 * Created by Blazers on 2015/11/19.
 */
public class ViewArticleEvent {

    public long id;
    public String title;

    public ViewArticleEvent(long id, String title) {
        this.id = id;
        this.title = title;
    }
}
