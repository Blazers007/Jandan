package com.blazers.jandan.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blazers.jandan.model.DataManager;
import com.blazers.jandan.model.event.ViewImageEvent;
import com.blazers.jandan.model.image.SingleImage;
import com.blazers.jandan.presenter.base.BaseLoadMoreRefreshPresenter;
import com.blazers.jandan.ui.activity.ImageInspectActivity;
import com.blazers.jandan.ui.fragment.readingsub.ImageView;
import com.blazers.jandan.util.SPHelper;
import com.blazers.jandan.util.TimeHelper;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by blazers on 2016/12/2.
 */

public class ImagePresenter extends BaseLoadMoreRefreshPresenter<ImageView> {

    public static final String WULIAO = "wuliao";
    public static final String MEIZHI = "meizhi";

    private int mPage = 1;
    private String mType;

    public ImagePresenter(String type, ImageView view, Context context) {
        super(view, context);
        mType = type;
    }

    @Override
    public void onInitPageData() {
        Log.i("Presenter", "==OnInitImagePageData==");
        Observable<List<SingleImage>> observable;
        if (mType.equals(WULIAO)) {
            observable = DataManager.getInstance().getWuliaoDataFromDB(mPage);
        } else {
            observable = DataManager.getInstance().getMeizhiDataFromDB(mPage);
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(posts -> {
                    Log.e("onInitImagePageData", "==OnNext==");
                    // 有数据则首先显示
                    if (posts != null && !posts.isEmpty()) {
                        mView.refreshDataList(posts);
                        // 在根据刷新时间判断是否需要刷新
                        if (TimeHelper.isTimeEnoughForRefreshing(SPHelper.getLastRefreshTime(getActivity(), "news"))) {
//                            onRefresh();
                        }
                    } else {
                        // 无数据则直接刷新
//                        onRefresh();
                    }
                }, error -> {
                    Log.e("onInitImagePageData", error.toString());
                    onRefresh();
                }); // 出错直接刷新
    }

    @Override
    public void onRefresh() {
        Log.i("Presenter", "==OnRefresh==");
        mPage = 1;
        Observable<List<SingleImage>> observable;
        if (mType.equals(WULIAO)) {
            observable = DataManager.getInstance().getWuliaoData(getContext(), mPage);
        } else {
            observable = DataManager.getInstance().getMeizhiData(getContext(), mPage);
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(posts -> {
                    // 刷新成功 更新UI
                    if (posts != null && !posts.isEmpty()) {
                        mView.refreshDataList(posts);
                        mView.hideRefreshingView(true);
                    }
                    // 刷新失败 不更新UI弹出提示
                }, error -> {
                    Log.e("onRefresh", error.toString());
                    mView.hideRefreshingView(false);
                });
    }

    @Override
    public void onLoadMore() {
        Log.i("Presenter", "==OnRefresh==");
        mPage++;
        Observable<List<SingleImage>> observable;
        if (mType.equals(WULIAO)) {
            observable = DataManager.getInstance().getWuliaoData(getContext(), mPage);
        } else {
            observable = DataManager.getInstance().getMeizhiData(getContext(), mPage);
        }
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(posts -> {
                    // 刷新成功 更新UI
                    if (posts != null && !posts.isEmpty()) {
                        mView.addDataList(posts);
                        mView.hideLoadMoreView(true);
                    }
                    // 刷新失败 不更新UI弹出提示
                }, error -> {
                    Log.e("onRefresh", error.toString());
                    mView.hideLoadMoreView(false);
                });
    }

    public void onClickImage(SingleImage singleImage) {
        // StartActivity
        getActivity().startActivity(
                new Intent(getActivity(), ImageInspectActivity.class)
                        .putExtra(ViewImageEvent.KEY, new ViewImageEvent(singleImage.url, singleImage.comment.text_content))
        );
    }

}
