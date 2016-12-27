package com.blazers.jandan.ui.fragment.readingsub;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.blazers.jandan.BR;
import com.blazers.jandan.R;
import com.blazers.jandan.model.news.NewsPage;
import com.blazers.jandan.model.news.NewsPost;
import com.blazers.jandan.presenter.NewsPresenter;
import com.blazers.jandan.ui.activity.NewsReadActivity;
import com.blazers.jandan.ui.adapter.BaseSingleMVVMAdapter;
import com.blazers.jandan.ui.fragment.base.BaseSwipeLoadMoreFragment;
import com.blazers.jandan.util.SPHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blazers on 2015/8/27.
 * <p>
 * 新鲜事列表
 */
@SuppressWarnings("unused")
public class NewsFragment extends BaseSwipeLoadMoreFragment<NewsPresenter> implements NewsView {

    private BaseSingleMVVMAdapter<NewsPost, NewsPresenter> mAdapter;
    private List<NewsPost> mList = new ArrayList<>();


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_common_refresh_load;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new NewsPresenter(this);
        initRecyclerView();
        mPresenter.init(SPHelper.getLastRefreshTime(getActivity(), NewsFragment.class.getSimpleName()));
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
        ), 12, Color.rgb(241, 242, 241));
    }

    @Override
    public void onRefreshDataList(List<NewsPost> postsBeanList) {
        mList.clear();
        mList.addAll(postsBeanList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAddDataList(List<NewsPost> postsBeanList) {
        int start = mList.size();
        int size = postsBeanList.size();
        mList.addAll(postsBeanList);
        mRecyclerView.post(()->{
            mAdapter.notifyItemRangeInserted(start, size);
            mRecyclerView.smoothScrollBy(0, 96);
        });
    }

    @Override
    public void onNavigateToNewsRead(int postId) {
        getActivity().startActivity(
                new Intent(getContext(), NewsReadActivity.class).putExtra(NewsReadActivity.KEY_POST_ID, postId));
        getActivity().overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }
}
