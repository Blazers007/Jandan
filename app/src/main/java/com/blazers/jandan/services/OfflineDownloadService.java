package com.blazers.jandan.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import com.blazers.jandan.IOfflineDownloadInterface;
import com.blazers.jandan.models.db.sync.ImagePost;
import com.blazers.jandan.network.ImageDownloader;
import com.blazers.jandan.network.Parser;
import com.blazers.jandan.util.NetworkHelper;
import com.blazers.jandan.util.TimeHelper;
import io.realm.Realm;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Blazers on 2015/10/22.
 */
public class OfflineDownloadService extends Service {

    private Realm realm, threadRealm;
    /**
     * AIDL Binder实例化
     * */
    private IOfflineDownloadInterface.Stub binder = new IOfflineDownloadInterface.Stub() {
        @Override
        public void startDownloadNews(int fromPage, int pageSize) throws RemoteException {

        }

        @Override
        public void startDownloadPicture(String type, int fromPage, int pageSize) throws RemoteException {
            if (!NetworkHelper.netWorkAvailable(OfflineDownloadService.this)) {
                return;
            }
            Observable.range(fromPage, pageSize)
                .flatMap(page -> {
                    threadRealm = Realm.getInstance(OfflineDownloadService.this);
                    return Parser.getInstance().getPictureData(threadRealm, page, type);
                }) // 获取该页码的数据
                    .doOnNext(list -> {
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(list);
                        realm.commitTransaction();
                    })
                .map(ImagePost::getAllImageFromList)                              // 解析出图片信息
                .flatMap(Observable::from)
                .map(ImageDownloader.getInstance()::doDownloadingImage)
                .filter(localImage -> localImage != null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    localImage -> {
                        Log.i("下载完成", localImage.getLocalUrl());
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(localImage);
                        realm.commitTransaction();
                    }, throwable -> {
                        Log.e("Error", throwable.toString());
                    }, ()->{
                        threadRealm.close();
                        Log.i(">>>>>下载全部完毕<<<<<", TimeHelper.getTime());
                    }
                );
        }

        @Override
        public void startDownloadJokes(int fromPage, int pageSize) throws RemoteException {

        }
    };

    /**
     * 获取Binder
     * */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * 初始化对象
     * */
    @Override
    public void onCreate() {
        super.onCreate();
        realm = Realm.getInstance(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
