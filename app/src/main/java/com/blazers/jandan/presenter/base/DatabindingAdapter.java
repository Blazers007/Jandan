package com.blazers.jandan.presenter.base;

import android.databinding.BindingAdapter;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by blazers on 2016/11/30.
 */

public class DatabindingAdapter {

    @BindingAdapter({"image_url"})
    public static void loadImage(SimpleDraweeView simpleDraweeView, String url) {
        simpleDraweeView.setImageURI(Uri.parse(url));
        //            LocalImage localImage = realm.where(LocalImage.class).equalTo("url", newsPost.getThumbUrl()).findFirst();
//            if (localImage != null && SdcardHelper.isThisFileExist(localImage.getLocalUrl())) {
//                newsHolder.draweeView.setImageURI(Uri.parse("file://" + localImage.getLocalUrl()));
//            } else {
//                newsHolder.draweeView.setImageURI(Uri.parse(newsPost.getThumbUrl()));
//            }
    }
}
