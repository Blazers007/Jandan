package com.blazers.jandan.model.event;

import com.blazers.jandan.util.log.Log;

/**
 * Created by blazers on 2016/12/8.
 */

public class FastScrollUpEvent {

    public int index;

    public FastScrollUpEvent(int index) {
        this.index = index;
        Log.i("ScrollUpEvent" + index);
    }
}
