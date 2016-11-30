package com.blazers.jandan.presenter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.blazers.jandan.model.database.local.LocalImage;
import com.blazers.jandan.util.ImageDownloader;
import com.blazers.jandan.presenter.base.BasePresenter;
import com.blazers.jandan.ui.activity.ImageInspectView;
import com.blazers.jandan.util.DBHelper;
import com.blazers.jandan.util.RxHelper;
import com.blazers.jandan.util.SdcardHelper;
import com.blazers.jandan.model.event.ViewImageEvent;

import rx.Observable;

/**
 * Created by blazers on 2016/11/11.
 */

public class ImageInspectPresenter extends BasePresenter<ImageInspectView> {

    private ViewImageEvent mViewImageEvent;
    private boolean mDownloaded;
    private boolean mDownloading;


    public ImageInspectPresenter(ImageInspectView view, Context context) {
        super(view, context);
        mViewImageEvent = (ViewImageEvent) getIntent().getSerializableExtra(ViewImageEvent.KEY);
        if (mViewImageEvent == null) {
            getActivity().finish();
        }
    }

    /**
     * 获取
     * @return 图片Uri
     */
    public Uri getImageUrl() {
        LocalImage localImage = getRealm().where(LocalImage.class).equalTo("url", mViewImageEvent.originUrl).findFirst();
        String uri;
        if (localImage != null && SdcardHelper.isThisFileExist(localImage.getLocalUrl())) {
            return Uri.parse("file://" + localImage.getLocalUrl());
//            downloaded = true;
        } else {
            return Uri.parse(mViewImageEvent.originUrl);
        }
    }

    public String getImageContent() {
        return mViewImageEvent.contentStr;
    }

    public boolean isDownloaded() {
        return mDownloaded;
    }

    public void downloadImage() {
        if (mDownloading)
            return;
        mDownloading = true;
        Observable.just(mViewImageEvent.originUrl)
                .map(ImageDownloader.getInstance()::doSavingImage)
                .compose(RxHelper.applySchedulers())
                .subscribe(localImage -> {

                    DBHelper.saveToRealm(getRealm(), localImage);
                    mView.showToast("图片保存成功");
                }, throwable -> {
                    mDownloading = false;
                    Log.e("Error", throwable.toString());
                });

    }


}
