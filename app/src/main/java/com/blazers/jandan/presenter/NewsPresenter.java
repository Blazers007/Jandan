package com.blazers.jandan.presenter;

import android.content.Context;
import android.util.Log;

import com.blazers.jandan.api.DataManager;
import com.blazers.jandan.model.event.ViewArticleEvent;
import com.blazers.jandan.model.news.PostsBean;
import com.blazers.jandan.presenter.base.BaseLoadMoreRefreshPresenter;
import com.blazers.jandan.ui.fragment.readingsub.NewsView;
import com.blazers.jandan.util.Rxbus;
import com.blazers.jandan.util.SPHelper;
import com.blazers.jandan.util.TimeHelper;

import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by blazers on 2016/11/30.
 */

public class NewsPresenter extends BaseLoadMoreRefreshPresenter<NewsView> {

    private int mPage = 1;

    public NewsPresenter(NewsView view, Context context) {
        super(view, context);
    }


    /**
     * 尝试读取数据库中的旧数据便与展示
     */
    public void onInitPageData() {
        Log.i("Presenter", "==OnInitPageData==");
        DataManager.getInstance()
                .getNewsDataFromDB(mPage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsPage -> {
                    Log.e("onInitPageData", "==OnNext==");
                    // 有数据则首先显示
                    if (newsPage != null && newsPage.posts != null) {
                        mView.refreshDataList(newsPage.posts);
                        // 在根据刷新时间判断是否需要刷新
                        if (TimeHelper.isTimeEnoughForRefreshing(SPHelper.getLastRefreshTime(getActivity(), "news"))) {
//                            onRefresh();
                        }
                    } else {
                        // 无数据则直接刷新
//                        onRefresh();
                    }
                }, error -> {
                    Log.e("onInitPageData", error.toString());
                    onRefresh();
                }); // 出错直接刷新
    }


    /**
     * 刷新内容
     */
    public void onRefresh() {
        Log.i("Presenter", "==OnRefresh==");
        mPage = 1;
        DataManager.getInstance().getNewsData(getActivity(), mPage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsPage -> {
                    // 刷新成功 更新UI
                    mView.refreshDataList(newsPage.posts);
                    mView.hideRefreshingView(true);
                    // 刷新失败 不更新UI弹出提示
                }, error -> {
                    Log.e("onRefresh", error.toString());
                    mView.hideRefreshingView(false);
                });
    }

    /**
     * 加载下一页
     */
    public void onLoadMore() {
//        mPage++;
//        DataManager.getInstance().getNewsData(getActivity(), mPage)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(newsPage -> {
//                    // 刷新成功 更新UI
//                    mView.addDataList(newsPage.posts);
//                    mView.hideLoadMoreView(true);
//                    // 刷新失败 不更新UI弹出提示
//                }, error -> {
//                    Log.e("onLoadMore", error.toString());
//                    mView.hideLoadMoreView(false);
//                });

    }

    /**
     * 点击PostItem
     *
     * @param postsBean post
     */
    public void onClickPost(PostsBean postsBean) {
        Rxbus.getInstance().send(new ViewArticleEvent(postsBean.id, postsBean.title));
    }
}
