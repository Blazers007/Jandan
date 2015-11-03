package com.blazers.jandan.rxbus.event;

/**
 * Created by Blazers on 2015/10/28.
 */
public class DrawerEvent {
    public static final int CLOSE_DRAWER = 1;
    public static final int CLOSE_DRAWER_AND_LOCK = 2;
    public static final int OPEN_DRAWER = 2;
    public static final int OPEN_DRAWER_AND_LOCK = 2;

    public int messageType;

    public DrawerEvent(int messageType) {
        this.messageType = messageType;
    }
}
