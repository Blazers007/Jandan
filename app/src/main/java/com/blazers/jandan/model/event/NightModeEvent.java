package com.blazers.jandan.model.event;

import java.io.Serializable;

/**
 * Created by Blazers on 2015/10/28.
 */
public class NightModeEvent implements Serializable {
    public boolean nightModeOn;
    public NightModeEvent(boolean nightModeOn) {
        this.nightModeOn = nightModeOn;
    }
}
