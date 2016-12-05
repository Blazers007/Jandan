package com.blazers.jandan.presenter;

import android.util.Log;

import com.blazers.jandan.model.DataManager;
import com.blazers.jandan.model.news.NewsPage;
import com.blazers.jandan.presenter.base.BaseLoadMoreRefreshPresenter;
import com.blazers.jandan.ui.fragment.readingsub.NewsView;
import com.blazers.jandan.util.ListHelper;
import com.blazers.jandan.util.SPHelper;
import com.blazers.jandan.util.TimeHelper;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by blazers on 2016/11/30.
 */

public class NewsPresenter extends BaseLoadMoreRefreshPresenter<NewsView> {

    public static final String TAG_NEWS = "news";

    private int mPage = 1;

    public NewsPresenter(NewsView view) {
        super(view);
        view.setTag(TAG_NEWS);
    }


    /**
     * 尝试读取数据库中的旧数据便与展示
     */
    @Override
    public void onInitPageData() {
        Log.i("Presenter", "==OnInitNewsPageData==");
        addFUISubscription(DataManager.getInstance()
                .getNewsDataFromDB(mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(posts -> {
                    // 有数据则首先显示
                    if (ListHelper.isNotEmptySafe(posts)) {
                        mView.refreshDataList(posts);
                        // 在根据刷新时间判断是否需要刷新
//                        if (TimeHelper.isTimeEnoughForRefreshing(SPHelper.getLastRefreshTime(TAG_NEWS))) {
//                            onRefresh();
//                        }
                    } else {
                        onRefresh();
                    }
                }, error -> {
                    System.out.println("onInitNewsPageData" + error.toString());
                    onRefresh();
                })); // 出错直接刷新
    }


    /**
     * 刷新内容
     */
    public void onRefresh() {
        Log.i("Presenter", "==OnRefresh==");
        mPage = 1;
        addFUISubscription(DataManager.getInstance().getNewsData(mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(posts -> {
                    // 刷新成功 更新UI
                    if (ListHelper.isNotEmptySafe(posts)) {
                        mView.refreshDataList(posts);
                        mView.hideRefreshingView(true);
                    } else {
                        mView.hideRefreshingView(false);
                    }
                }, error -> {
                    System.out.println("onInitNewsPageData" +  error.toString());
                    mView.hideRefreshingView(false);
                }));
    }

    /**
     * 加载下一页
     */
    public void onLoadMore() {
        mPage++;
        addFUISubscription(DataManager.getInstance().getNewsData(mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(posts -> {
                    if (ListHelper.isNotEmptySafe(posts)) {
                        // 刷新成功 更新UI
                        mView.addDataList(posts);
                        mView.hideLoadMoreView(true);
                    } else {
                        // 刷新失败 不更新UI弹出提示
                        mView.hideLoadMoreView(false);
                    }
                }, error -> {
                    System.out.println("onInitNewsPageData" +  error.toString());
                    mView.hideLoadMoreView(false);
                }));

    }

    /**
     * 点击PostItem
     *
     * @param
     */
    public void onClickPost(NewsPage.Posts postsBean) {
        mView.onGoToNewsRead(postsBean);
    }
}
