package com.blazers.jandan.util;

import java.io.File;

/**
 * Created by Blazers on 2015/10/21.
 */
public class FileHelper {
    public static boolean isThisFileExist(String path) {
        if (path == null || path.isEmpty())
            return false;
        File file = new File(path);
        return file.exists();
    }
}
