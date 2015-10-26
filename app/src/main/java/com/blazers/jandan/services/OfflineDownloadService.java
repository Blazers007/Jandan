package com.blazers.jandan.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import com.blazers.jandan.IOfflineDownloadInterface;
import com.blazers.jandan.R;
import com.blazers.jandan.models.db.sync.ImagePost;
import com.blazers.jandan.network.ImageDownloader;
import com.blazers.jandan.network.Parser;
import com.blazers.jandan.ui.activity.MainActivity;
import com.blazers.jandan.util.DBHelper;
import com.blazers.jandan.util.NetworkHelper;
import com.blazers.jandan.util.NotificationHelper;
import com.blazers.jandan.util.TimeHelper;
import io.realm.Realm;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Blazers on 2015/10/22.
 */
public class OfflineDownloadService extends Service {

    /* 数据库 */
    private Realm realm;
    /* NOTIFY TAG */
    public static final int NOTI_NEWS = 11;
    public static final int NOTI_WULIAO = 12;
    public static final int NOTI_JOKES = 13;
    public static final int NOTI_MEIZI = 14;

    /* 离线公共参数 */
    private int offlinePage;
    /* 离线图片用参数 */
    private int imageSize;
    private int downloadedSize;

    /**
     * AIDL Binder实例化
     * */
    private IOfflineDownloadInterface.Stub binder = new IOfflineDownloadInterface.Stub() {

        /**
         * 离线下载新闻
         * */
        @Override
        public void startDownloadNews(int fromPage, int pageSize) throws RemoteException {

        }

        /**
         * 离线下载图片
         * */
        @Override
        public void startDownloadPicture(String type, int fromPage, int pageSize) throws RemoteException {
            if (!NetworkHelper.netWorkAvailable(OfflineDownloadService.this)) {
                Toast.makeText(OfflineDownloadService.this, R.string.no_connect_toast, Toast.LENGTH_SHORT).show();
                return;
            }
            //  初始化或还原参数
            offlinePage = pageSize;
            imageSize = 0;
            downloadedSize = 0;
            PendingIntent pendingIntent = PendingIntent.getActivity(
                OfflineDownloadService.this,
                0,
                new Intent(OfflineDownloadService.this, MainActivity.class),
                PendingIntent.FLAG_ONE_SHOT);
            //  开启Notification提示
            NotificationHelper.showOfflineNotification(
                OfflineDownloadService.this,
                NOTI_MEIZI,
                "下载无聊图",
                "正在读取页面信息",
                "",
                pendingIntent
            );
            //
            Observable.range(fromPage, pageSize)
                .flatMap(page -> Parser.getInstance().getPictureData(page, type))           // 1 - 获取该页码的数据
                .doOnNext(list->DBHelper.saveToRealm(OfflineDownloadService.this, list))    // 2 - IO线程中写入数据库
                .map(ImagePost::getAllImageFromList)                                        // 3 - 解析出图片信息
                .doOnNext(list -> imageSize = list.size())                                   // 4 - 记录图片数量
                .flatMap(Observable::from)                                                  // 5 - 利用from操作符逐一处理
                .map(ImageDownloader.getInstance()::doDownloadingImage)                     // 6 - 利用map操作符完成下载与转换
                .filter(localImage -> localImage != null)                                   // 7 - 过滤掉没有下载成功的
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    localImage -> {
                        Log.i("下载完成", localImage.getLocalUrl());
                        NotificationHelper.showOfflineNotification(
                            OfflineDownloadService.this,
                            NOTI_MEIZI,
                            "下载无聊图",
                            "下载完成 " + localImage.getUrl(),
                            "已下载" + ++downloadedSize,
                            pendingIntent
                        );
                        DBHelper.saveToRealm(realm, localImage);
                    }, throwable -> {
                        Log.e("Error", throwable.toString());
                    }, ()->{
                        Log.i(">>>>>下载全部完毕<<<<<", TimeHelper.getTime());
                        NotificationHelper.showOfflineNotification(
                            OfflineDownloadService.this,
                            NOTI_MEIZI,
                            "下载完毕",
                            "没网络也可以阅读咯",
                            "",
                            null
                        );
                    }
                );
        }

        /**
         * 离线下载段子
         * */
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
     * 释放掉Binder但不一定会停止Service
     * */
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    /**
     * 初始化对象
     * */
    @Override
    public void onCreate() {
        super.onCreate();
        realm = Realm.getInstance(this);
    }

    /**
     * 释放资源!
     * */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != realm)
            realm.close();
    }
}
