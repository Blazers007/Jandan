package com.blazers.jandan.model.count;

import java.io.Serializable;

/**
 * Created by Blazers on 2015/11/23.
 */
public class Count implements Serializable {

    public static final int NEWS = 0, WULIAO = 1, JOKE = 2, MEIZI = 3;

    public int type;
    public long count;

    public Count(int type, long count) {
        this.type = type;
        this.count = count;
    }
}
