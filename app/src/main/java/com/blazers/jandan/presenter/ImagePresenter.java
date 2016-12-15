package com.blazers.jandan.presenter;

import com.blazers.jandan.model.DataManager;
import com.blazers.jandan.model.image.SingleImage;
import com.blazers.jandan.presenter.base.BaseLoadMoreRefreshPresenter;
import com.blazers.jandan.ui.fragment.readingsub.ImageView;
import com.blazers.jandan.util.ListHelper;
import com.blazers.jandan.util.log.Log;

import java.util.List;

import rx.Observable;

/**
 * Created by blazers on 2016/12/2.
 */

public class ImagePresenter extends BaseLoadMoreRefreshPresenter<ImageView> {

    public static final String TAG_WULIAO = "wuliao";
    public static final String TAG_MEIZHI = "meizhi";

    private String mCurrentTag;

    public ImagePresenter(String currentTag, ImageView view) {
        super(view);
        mCurrentTag = currentTag;
        view.setTag(mCurrentTag);
    }

    @Override
    public void initPageData() {
        Log.i("Presenter", "==OnInitImagePageData==");
        List<SingleImage> ret;
        if (mCurrentTag.equals(TAG_WULIAO)) {
            ret = DataManager.getInstance().getWuliaoDataFromDB(mPage);
        } else {
            ret = DataManager.getInstance().getMeizhiDataFromDB(mPage);
        }
        if (ListHelper.isNotEmptySafe(ret)) {
            mView.onRefreshDataList(ret);
            // 在根据刷新时间判断是否需要刷新
//            if (TimeHelper.isTimeEnoughForRefreshing(SPHelper.getLastRefreshTime(mCurrentTag))) {
//                refresh();
//            }
        } else {
            // 无数据则直接刷新
            mView.onShowRefreshing();
            refresh();
        }
    }

    @Override
    public void refresh() {
        if (mIsRefreshing) {
            return;
        }
        mIsRefreshing = true;
        Log.i("Presenter", "==OnRefresh==");
        mPage = 1;
        Observable<List<SingleImage>> observable;
        if (mCurrentTag.equals(TAG_WULIAO)) {
            observable = DataManager.getInstance().getWuliaoData(mPage);
        } else {
            observable = DataManager.getInstance().getMeizhiData(mPage);
        }
        addFUISubscription(observable.doOnUnsubscribe((()->{

        })).subscribe(posts -> {
            // 刷新成功 更新UI
            mIsRefreshing = false;
            mView.onRefreshDataList(posts);
            mView.onHideRefreshing(true);
        }, error -> {
            Log.e("refresh", error.toString());
            mIsRefreshing = false;
            mView.onHideRefreshing(false);
        }));
    }

    @Override
    public void loadMore() {
        if (mToTheEnd || mIsLoading) {
            return;
        }
        mIsLoading = true;
        Log.i("Presenter", "==onLoadMore==");
        mPage++;
        Observable<List<SingleImage>> observable;
        if (mCurrentTag.equals(TAG_WULIAO)) {
            observable = DataManager.getInstance().getWuliaoData(mPage);
        } else {
            observable = DataManager.getInstance().getMeizhiData(mPage);
        }
        addFUISubscription(observable.subscribe(posts -> {
            // 刷新成功 更新UI
            mIsLoading = false;
            if (posts.size() == 0) {
                mToTheEnd = true;
                return;
            }
            mView.onAddDataList(posts);
            mView.hideLoadMoreView(true);
        }, error -> {
            Log.e("refresh", error.toString());
            mIsLoading = false;
            mView.hideLoadMoreView(false);
        }));
    }

    /**
     * 点击查看图片
     *
     * @param singleImage
     */
    public void clickImage(SingleImage singleImage) {
        mView.onInspectImage(singleImage);
    }

    /**
     * 设置收藏与否
     * @param singleImage
     * @param favOrNot
     */
    public void setFavoriteOrNot(SingleImage singleImage, boolean favOrNot) {
        DataManager.getInstance().manageImageFavorite(singleImage, favOrNot);
    }
}
