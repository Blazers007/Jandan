package com.blazers.jandan.util.rxbus.event;

import java.io.Serializable;

/**
 * Created by Blazers on 2015/10/28.
 */
public class ViewImageEvent implements Serializable {

    public static final String KEY = "ViewImageEvent";

    public String originUrl;
    public String contentStr;

    public ViewImageEvent(String originUrl, String contentStr) {
        this.originUrl = originUrl;
        this.contentStr = contentStr;
    }
}
