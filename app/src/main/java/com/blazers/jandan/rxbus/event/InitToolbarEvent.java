package com.blazers.jandan.rxbus.event;

import android.support.v7.widget.Toolbar;

/**
 * Created by Blazers on 2015/10/28.
 */
public class InitToolbarEvent {
    public Toolbar toolbar;

    public InitToolbarEvent(Toolbar toolbar) {
        this.toolbar = toolbar;
    }
}
