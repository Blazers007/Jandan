package com.blazers.jandan.presenter;

import com.blazers.jandan.model.DataManager;
import com.blazers.jandan.model.joke.JokeComment;
import com.blazers.jandan.presenter.base.BaseLoadMoreRefreshPresenter;
import com.blazers.jandan.ui.fragment.readingsub.JokeView;
import com.blazers.jandan.util.ListHelper;
import com.blazers.jandan.util.log.Log;

import java.util.List;

/**
 * Created by blazers on 2016/12/2.
 */

public class JokePresenter extends BaseLoadMoreRefreshPresenter<JokeView> {

    public static final String TAG_JOKE = "joke";

    private int mPage = 1;

    public JokePresenter(JokeView view) {
        super(view);
        view.setTag(TAG_JOKE);
    }

    @Override
    public void initPageData() {
        Log.i("Presenter", "==OnInitJokePageData==");
        List<JokeComment> ret = DataManager.getInstance().getJokeDataFromDB(mPage);
        if (ListHelper.isNotEmptySafe(ret)) {
            mView.onRefreshDataList(ret);
            // 在根据刷新时间判断是否需要刷新
//            if (TimeHelper.isTimeEnoughForRefreshing(SPHelper.getLastRefreshTime(TAG_JOKE))) {
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
        mIsRefreshing = addFUISubscription(DataManager.getInstance().getJokeData(mPage).subscribe(posts -> {
            // 刷新成功 更新UI
            mIsRefreshing = false;
            mView.onRefreshDataList(posts);
            mView.onHideRefreshing(true);
        }, error -> {
            Log.e("onRefreshJoke: ", error.toString());
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
        mPage++;
        addFUISubscription(DataManager.getInstance().getJokeData(mPage).subscribe(posts -> {
            // 刷新成功 更新UI
            if (posts.size() == 0) {
                mToTheEnd = true;
                return;
            }
            mView.onAddDataList(posts);
            mView.hideLoadMoreView(true);
        }, error -> {
            Log.e("onLoadMoreJoke: ", error.toString());
            mView.hideLoadMoreView(false);
        }));
    }


    public void clickFavorite() {

    }
}
