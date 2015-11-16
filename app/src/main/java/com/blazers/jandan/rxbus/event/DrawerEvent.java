package com.blazers.jandan.rxbus.event;

/**
 * Created by Blazers on 2015/10/28.
 */
public class DrawerEvent {
    public static final int TOGGLE = 0;  // TODO 添加 TOGGLE_LOCK
    public static final int CLOSE_DRAWER = 1;
    public static final int CLOSE_DRAWER_AND_LOCK = -1;
    public static final int OPEN_DRAWER = 2;
    public static final int OPEN_DRAWER_AND_LOCK = -2;

    /* V */
    public int gravity;
    public int messageType;

    public DrawerEvent(int gravity, int messageType) {
        this.gravity = gravity;
        this.messageType = messageType;
    }
}
