package com.blazers.jandan.util.rxbus.event;

import java.io.Serializable;

/**
 * Created by Blazers on 2015/11/19.
 */
public class ViewArticleEvent implements Serializable {

    public static final String KEY = "ViewArticleEvent";

    public long id;
    public String title;

    public ViewArticleEvent(long id, String title) {
        this.id = id;
        this.title = title;
    }
}
