package com.blazers.jandan.presenter;

import android.util.Log;

import com.blazers.jandan.model.DataManager;
import com.blazers.jandan.model.image.SingleImage;
import com.blazers.jandan.presenter.base.BaseLoadMoreRefreshPresenter;
import com.blazers.jandan.ui.fragment.readingsub.ImageView;
import com.blazers.jandan.util.ListHelper;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by blazers on 2016/12/2.
 */

public class ImagePresenter extends BaseLoadMoreRefreshPresenter<ImageView> {

    public static final String TAG_WULIAO = "wuliao";
    public static final String TAG_MEIZHI = "meizhi";

    private int mPage = 1;
    private String mCurrentTag;

    public ImagePresenter(String currentTag, ImageView view) {
        super(view);
        mCurrentTag = currentTag;
        view.setTag(mCurrentTag);
    }

    @Override
    public void onInitPageData() {
        Log.i("Presenter", "==OnInitImagePageData==");
        Observable<List<SingleImage>> observable;
        if (mCurrentTag.equals(TAG_WULIAO)) {
            observable = DataManager.getInstance().getWuliaoDataFromDB(mPage);
        } else {
            observable = DataManager.getInstance().getMeizhiDataFromDB(mPage);
        }
        addFUISubscription(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(posts -> {
                    Log.e("onInitImagePageData", "==OnNext==");
                    // 有数据则首先显示
                    if (ListHelper.isNotEmptySafe(posts)) {
                        mView.refreshDataList(posts);
                        // 在根据刷新时间判断是否需要刷新
//                        if (TimeHelper.isTimeEnoughForRefreshing(SPHelper.getLastRefreshTime(mCurrentTag))) {
//                            onRefresh();
//                        }
                    } else {
                        // 无数据则直接刷新
                        onRefresh();
                    }
                }, error -> {
                    Log.e("onInitImagePageData", error.toString());
                    onRefresh();
                }));
    }

    @Override
    public void onRefresh() {
        Log.i("Presenter", "==OnRefresh==");
        mPage = 1;
        Observable<List<SingleImage>> observable;
        if (mCurrentTag.equals(TAG_WULIAO)) {
            observable = DataManager.getInstance().getWuliaoData(mPage);
        } else {
            observable = DataManager.getInstance().getMeizhiData(mPage);
        }
        addFUISubscription(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(posts -> {
                    // 刷新成功 更新UI
                    if (ListHelper.isNotEmptySafe(posts)) {
                        mView.refreshDataList(posts);
                        mView.hideRefreshingView(true);
                    } else {
                        mView.hideRefreshingView(false);
                    }
                    // 刷新失败 不更新UI弹出提示
                }, error -> {
                    Log.e("onRefresh", error.toString());
                    mView.hideRefreshingView(false);
                }));
    }

    @Override
    public void onLoadMore() {
        Log.i("Presenter", "==OnRefresh==");
        mPage++;
        Observable<List<SingleImage>> observable;
        if (mCurrentTag.equals(TAG_WULIAO)) {
            observable = DataManager.getInstance().getWuliaoData(mPage);
        } else {
            observable = DataManager.getInstance().getMeizhiData(mPage);
        }
        addFUISubscription(observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(posts -> {
                    // 刷新成功 更新UI
                    if (ListHelper.isNotEmptySafe(posts)) {
                        mView.addDataList(posts);
                        mView.hideLoadMoreView(true);
                    } else {
                        mView.hideLoadMoreView(false);
                    }
                    // 刷新失败 不更新UI弹出提示
                }, error -> {
                    Log.e("onRefresh", error.toString());
                    mView.hideLoadMoreView(false);
                }));
    }


    /**
     * 点击查看图片
     *
     * @param singleImage
     */
    public void onClickImage(SingleImage singleImage) {
        mView.onInspectImage(singleImage);
    }

    public void setFavorite(SingleImage singleImage) {

    }

    public void setUnfavorite(SingleImage singleImage) {

    }

}
