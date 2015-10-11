package com.blazers.jandan.ui.fragment.jandan;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.models.jandan.Image;
import com.blazers.jandan.ui.fragment.app.BaseFragment;
import com.blazers.jandan.util.RecyclerViewHelper;
import com.blazers.jandan.views.GreySpaceItemDerocation;
import com.blazers.jandan.views.adapters.JandanImageAdapter;
import com.blazers.jandan.views.widget.LoadMoreRecyclerView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

import java.util.ArrayList;

/**
 * Created by Blazers on 2015/8/25.
 */
public class MeiziFragment extends BaseFragment {

    public static final String TAG = MeiziFragment.class.getSimpleName();

    @Bind(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_list) LoadMoreRecyclerView meiziList;
    @Bind(R.id.load_more_progress) SmoothProgressBar smoothProgressBar;

    private JandanImageAdapter mAdapter;
    private ArrayList<Image> mImageArrayList = new ArrayList<>();
    private int mPage = 1;

    /* Beta */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_refresh_load, container, false);
        ButterKnife.bind(this, root);
        initRecyclerView();
        return root;
    }

    /**
     * 从现有的数据库中读取 若没有数据库(首次进入)则建立数据库
     * 保存 首/尾 标志位ID
     * 并随后调用一次刷新 刷新后对比 若最新ID比当前ID大则更新
     * */
    void initRecyclerView() {
         /* 从数据库中读取 有两个标志位标志当前的第一个跟最后一个 然后从数据库中读取  顺便发起请求Service更新数据库 */
        meiziList.setLayoutManager(RecyclerViewHelper.getVerticalLinearLayoutManager(getActivity()));
        meiziList.addItemDecoration(new GreySpaceItemDerocation());
        meiziList.setItemAnimator(new FadeInUpAnimator());
        mAdapter = new JandanImageAdapter(getActivity(), mImageArrayList);
        meiziList.setAdapter(mAdapter);

        /* Loadmore */
        meiziList.setLoadMoreListener(() -> {
            smoothProgressBar.setVisibility(View.VISIBLE);
            getData(false, ++mPage, "meizi", mImageArrayList, mAdapter);
        });
        /* Set Adapter */
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FF9900"), Color.parseColor("#009900"), Color.parseColor("#000099"));
        swipeRefreshLayout.setOnRefreshListener(() -> {

        });

        getData(true, mPage, "meizi", mImageArrayList, mAdapter);
        swipeRefreshLayout.setRefreshing(true);
    }
}