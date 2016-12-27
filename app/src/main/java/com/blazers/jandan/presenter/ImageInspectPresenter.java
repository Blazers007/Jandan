package com.blazers.jandan.presenter;

import com.blazers.jandan.presenter.base.BasePresenter;
import com.blazers.jandan.ui.activity.ImageInspectView;

/**
 * Created by blazers on 2016/11/11.
 */

public class ImageInspectPresenter extends BasePresenter<ImageInspectView> {

    public ImageInspectPresenter(ImageInspectView view) {
        super(view);
    }

    public void downloadImage(String url) {
//        if (mDownloading)
//            return;
//        mDownloading = true;
//        Observable.just(mViewImageEvent.mOriginUrl)
//                .map(ImageDownloader.getInstance()::doSavingImage)
//                .compose(RxHelper.applySchedulers())
//                .subscribe(localImage -> {
//
//                    DBHelper.saveToRealm(getRealm(), localImage);
//                    mView.showToast("图片保存成功");
//                }, throwable -> {
//                    mDownloading = false;
//                    Log.e("Error", throwable.toString());
//                });

    }

    public void shareImage() {

    }
}
