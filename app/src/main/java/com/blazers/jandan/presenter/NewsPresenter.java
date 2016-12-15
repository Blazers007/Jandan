package com.blazers.jandan.presenter;

import com.blazers.jandan.model.DataManager;
import com.blazers.jandan.model.news.NewsPost;
import com.blazers.jandan.presenter.base.BaseLoadMoreRefreshPresenter;
import com.blazers.jandan.ui.fragment.readingsub.NewsView;
import com.blazers.jandan.util.ListHelper;
import com.blazers.jandan.util.log.Log;

import java.util.List;


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
    public void initPageData() {
        Log.i("Presenter", "==OnInitNewsPageData==");
        List<NewsPost> ret = DataManager.getInstance().getNewsDataFromDB(mPage);
        if (ListHelper.isNotEmptySafe(ret)) {
            mView.onRefreshDataList(ret);
            // 在根据刷新时间判断是否需要刷新
//            if (TimeHelper.isTimeEnoughForRefreshing(SPHelper.getLastRefreshTime(TAG_NEWS))) {
//                refresh();
//            }
        } else {
            mView.onShowRefreshing();
            refresh();
        }
    }


    /**
     * 刷新内容
     */
    public void refresh() {
        if (mIsRefreshing) {
            return;
        }
        mIsRefreshing = true;
        Log.i("Presenter", "==OnRefresh==");
        mPage = 1;
        addFUISubscription(DataManager.getInstance().getNewsData(mPage).subscribe(posts -> {
            // 刷新成功 更新UI
            mIsRefreshing = true;
            mView.onRefreshDataList(posts);
            mView.onHideRefreshing(true);
        }, error -> {
            mIsRefreshing = true;
            Log.e("onInitNewsPageData", error.toString());
            mView.onHideRefreshing(false);
        }));
    }

    /**
     * 加载下一页
     */
    public void loadMore() {
        if (mToTheEnd || mIsLoading) {
            return;
        }
        mIsLoading = true;
        mPage++;
        addFUISubscription(DataManager.getInstance().getNewsData(mPage).subscribe(posts -> {
            // 读取更多成功 更新UI
            mIsLoading = false;
            if (posts.size() == 0) {
                mToTheEnd = true;
                return;
            }
            mView.onAddDataList(posts);
            mView.hideLoadMoreView(true);
        }, error -> {
            mIsLoading = false;
            Log.e("onInitNewsPageData", error.toString());
            mView.hideLoadMoreView(false);
        }));

    }

    /**
     * 点击PostItem
     *
     * @param
     */
    public void onClickPost(NewsPost postsBean) {
        mView.onNavigateToNewsRead(postsBean.id);
    }
}
