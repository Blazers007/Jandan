package com.blazers.jandan.ui.activity;

import android.net.Uri;

/**
 * Created by blazers on 2016/11/11.
 */

public interface ImageInspectView {

    void setDownloadButtonDone();

    void showImageByUri(Uri uri);

    void showToast(String msg);

}
