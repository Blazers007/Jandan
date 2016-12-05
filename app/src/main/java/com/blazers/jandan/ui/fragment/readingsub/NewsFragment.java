package com.blazers.jandan.ui.fragment.readingsub;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.blazers.jandan.BR;
import com.blazers.jandan.R;
import com.blazers.jandan.model.news.NewsPage;
import com.blazers.jandan.presenter.NewsPresenter;
import com.blazers.jandan.presenter.NewsReadPresenter;
import com.blazers.jandan.ui.activity.NewsReadActivity;
import com.blazers.jandan.ui.adapter.BaseSingleMVVMAdapter;
import com.blazers.jandan.ui.fragment.base.BaseSwipeLoadMoreFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blazers on 2015/8/27.
 * <p>
 * 新鲜事列表
 */
@SuppressWarnings("unused")
public class NewsFragment extends BaseSwipeLoadMoreFragment<NewsPresenter> implements NewsView {

    private BaseSingleMVVMAdapter<NewsPage.Posts, NewsPresenter> mAdapter;
    private List<NewsPage.Posts> mList = new ArrayList<>();

    @Override
    protected void initPresenter() {
        mPresenter = new NewsPresenter(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_common_refresh_load;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    void initRecyclerView() {
        trySetupSwipeRefreshLayout();
        trySetupRecyclerViewWithAdapter(mAdapter = new BaseSingleMVVMAdapter<>(
                LayoutInflater.from(getActivity()),
                R.layout.item_jandan_news,
                mList,
                mPresenter,
                BR.nBean,
                BR.nPresenter
        ));
        // Try to load from db
        mPresenter.onInitPageData();
    }

    @Override
    public void refreshDataList(List<NewsPage.Posts> postsBeanList) {
        mList.clear();
        mList.addAll(postsBeanList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void addDataList(List<NewsPage.Posts> postsBeanList) {
        // 是否区分重复元素？ 能否在这区分重复元素  --> 索性不考虑
        int start = mList.size();
        int size = postsBeanList.size();
        mList.addAll(postsBeanList);
        mAdapter.notifyItemRangeInserted(start, size);
        mRecyclerView.smoothScrollBy(0, 96);
    }

    @Override
    public void onGoToNewsRead(NewsPage.Posts post) {
        getActivity().startActivity(
                new Intent(getContext(), NewsReadActivity.class)
                        .putExtra(NewsReadPresenter.KEY_NEWS_POST, post)
        );
        getActivity().overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_right_out);
    }
}
