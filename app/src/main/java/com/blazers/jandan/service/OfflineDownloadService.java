package com.blazers.jandan.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.blazers.jandan.R;
import com.blazers.jandan.util.ImageDownloader;
import com.blazers.jandan.model.DataManager;
import com.blazers.jandan.ui.activity.MainActivity;
import com.blazers.jandan.util.NetworkHelper;
import com.blazers.jandan.util.NotificationHelper;
import com.blazers.jandan.util.RxHelper;
import com.blazers.jandan.util.TimeHelper;

import rx.Observable;

/**
 * Created by Blazers on 2015/10/22.
 *
 * TODO: 重构下载服务  下载结束后 自动结束Service 并提供 失败下载重试的操作！
 *
 */
public class OfflineDownloadService extends IntentService {

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

    public OfflineDownloadService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO: 采用IntentService避免长时间开启后台线程
    }

    public void startDownloadNews(int fromPage, int pageSize) throws RemoteException {
//        Observable.range(fromPage, pageSize)
//                .flatMap(DataManager.getInstance()::getNewsData)
//                .doOnNext(list -> DBHelper.saveToRealm(OfflineDownloadService.this, list))
//                .flatMap(Observable::from)
//                .map(newsPost -> {
//                    DBHelper.saveToRealm(OfflineDownloadService.this, ImageDownloader.getInstance().doSimpleOfflineCaching(newsPost.getThumbUrl()));
//                    return newsPost.getId();
//                })
//                .flatMap(DataManager.getInstance()::getNewsContentData)
//                .compose(RxHelper.applySchedulers())
//                .subscribe(
//                        localArticleHtml -> {
//                            DBHelper.saveToRealm(realm, localArticleHtml);
//                            Log.i("离线文章", "下载完毕1");
//                        },
//                        throwable -> {
//                            Log.e("Error", throwable.toString());
//                        },
//                        () -> {
//                            Log.e("离线文章", "全部下载完毕");
//                        }
//                );
    }

    /**
     * 离线下载图片
     * */
//    public void startDownloadPicture(String type, int fromPage, int pageSize) throws RemoteException {
//        if (!NetworkHelper.netWorkAvailable(OfflineDownloadService.this)) {
//            Toast.makeText(OfflineDownloadService.this, R.string.no_connect_toast, Toast.LENGTH_SHORT).show();
//            return;
//        }
//        //  初始化或还原参数
//        offlinePage = pageSize;
//        imageSize = 0;
//        downloadedSize = 0;
//
//        /**
//         * 精简优化Notification方法
//         * */
//        PendingIntent pendingIntent = PendingIntent.getActivity(
//                OfflineDownloadService.this,
//                0,
//                new Intent(OfflineDownloadService.this, MainActivity.class),
//                PendingIntent.FLAG_ONE_SHOT);
//        //  开启Notification提示
//        NotificationHelper.showOfflineNotification(
//                OfflineDownloadService.this,
//                NOTI_MEIZI,
//                "下载无聊图",
//                "正在读取页面信息",
//                "",
//                pendingIntent
//        );
//        //
//        Observable.range(fromPage, pageSize)
//                .flatMap(page -> DataManager.getInstance().getPictureData(page, type))           // 1 - 获取该页码的数据
//                .doOnNext(list -> DBHelper.saveToRealm(OfflineDownloadService.this, list))    // 2 - IO线程中写入数据库
//                .map(OldImagePost::getAllImageFromList)                                        // 3 - 解析出图片信息
//                .doOnNext(list -> imageSize = list.size())                                   // 4 - 记录图片数量
//                .flatMap(Observable::from)                                                  // 5 - 利用from操作符逐一处理
//                .map(ImageDownloader.getInstance()::doOfflineCachingImage)                     // 6 - 利用map操作符完成下载与转换
//                .filter(localImage -> localImage != null)                                   // 7 - 过滤掉没有下载成功的
//                .compose(RxHelper.applySchedulers())
//                .subscribe(
//                        localImage -> {
//                            Log.i("下载完成", localImage.getLocalUrl());
//                            NotificationHelper.showOfflineNotification(
//                                    OfflineDownloadService.this,
//                                    NOTI_MEIZI,
//                                    "下载无聊图",
//                                    "下载完成 " + localImage.getUrl(),
//                                    "已下载" + ++downloadedSize,
//                                    pendingIntent
//                            );
//                            DBHelper.saveToRealm(realm, localImage);
//                        }, throwable -> Log.e("Error", throwable.toString()),
//                        () -> {
//                            Log.i(">>>>>下载全部完毕<<<<<", TimeHelper.getTime());
//                            NotificationHelper.showOfflineNotification(
//                                    OfflineDownloadService.this,
//                                    NOTI_MEIZI,
//                                    "下载完毕",
//                                    "没网络也可以阅读咯",
//                                    "",
//                                    null
//                            );
//                        }
//                );
//    }
//
//    /**
//     * 离线下载段子
//     * */
//    public void startDownloadJokes(int fromPage, int pageSize) throws RemoteException {
//        Observable.range(fromPage, pageSize)
//                .flatMap(DataManager.getInstance()::getJokeData)
//                .compose(RxHelper.applySchedulers())
//                .subscribe(
//                        list -> DBHelper.saveToRealm(OfflineDownloadService.this, list),
//                        throwable -> Log.e("Error Joke", throwable.toString()),
//                        () -> Log.e("离线端子", "全部下载完毕")
//                );
//    }
//
//    /**
//     * 初始化对象
//     */
//    @Override
//    public void onCreate() {
//        Log.e(">>Service<<", "Create");
//        super.onCreate();//    }
//
//    /**
//     * 释放资源!
//     */
//    @Override
//    public void onDestroy() {
//        Log.e(">>Service<<", "Destroy");
//        super.onDestroy();
//    }
}
