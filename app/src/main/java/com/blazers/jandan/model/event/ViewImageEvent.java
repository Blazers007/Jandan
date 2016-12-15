package com.blazers.jandan.model.event;

import com.android.annotations.Nullable;


import java.io.File;
import java.io.Serializable;

/**
 * Created by Blazers on 2015/10/28.
 */
public class ViewImageEvent implements Serializable {

    public static final String KEY = "ViewImageEvent";

    public String mOriginUrl;
    public String mContentStr;
    public @Nullable String mLocalPath;

    public ViewImageEvent(String originUrl, String contentStr, String localPath) {
        mOriginUrl = originUrl;
        mContentStr = contentStr;
        mLocalPath = localPath;
    }

    public boolean isDownloaded() {
        return null != mLocalPath && new File(mLocalPath).exists();
    }
}
