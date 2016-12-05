package com.blazers.jandan.util;

import java.util.List;

/**
 * Created by blazers on 2016/12/5.
 */

public class ListHelper {

    public static boolean isNotEmptySafe(List list) {
        return list != null && !list.isEmpty();
    }

}
