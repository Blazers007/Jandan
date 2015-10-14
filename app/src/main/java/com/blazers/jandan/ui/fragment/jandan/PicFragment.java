package com.blazers.jandan.ui.fragment.jandan;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.models.jandan.Image;
import com.blazers.jandan.models.jandan.Post;
import com.blazers.jandan.network.Parser;
import com.blazers.jandan.ui.fragment.BaseFragment;
import com.blazers.jandan.util.RecyclerViewHelper;
import com.blazers.jandan.util.TimeHelper;
import com.blazers.jandan.views.GreySpaceItemDerocation;
import com.blazers.jandan.ui.adapters.JandanImageAdapter;
import com.blazers.jandan.views.widget.LoadMoreRecyclerView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import io.realm.Realm;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blazers on 15/9/8.
 */
public class PicFragment extends BaseFragment {

    private Realm realm;

    @Bind(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_list) LoadMoreRecyclerView recyclerView;
    @Bind(R.id.load_more_progress) SmoothProgressBar smoothProgressBar;

    private JandanImageAdapter adapter;
    private ArrayList<Image> imageArrayList = new ArrayList<>();
    private int page = 1;
    private String type;

    /* Constructor */
    public static PicFragment newInstance(String type) {
        Bundle data = new Bundle();
        data.putString("type", type);
        PicFragment picFragment = new PicFragment();
        picFragment.setTAG(type);
        picFragment.setArguments(data);
        return picFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        realm = Realm.getInstance(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_refresh_load, container, false);
        if (getArguments() != null && getArguments().getString("type") != null) {
            ButterKnife.bind(this, root);
            type = getArguments().getString("type");
            initRecyclerView();
        }
        return root;
    }


    void initRecyclerView() {
        /* 从数据库中读取 有两个标志位标志当前的第一个跟最后一个 然后从数据库中读取  顺便发起请求Service更新数据库 */
        recyclerView.setLayoutManager(RecyclerViewHelper.getVerticalLinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new GreySpaceItemDerocation());
        recyclerView.setItemAnimator(new FadeInUpAnimator());
        adapter = new JandanImageAdapter(getActivity(), imageArrayList);
        recyclerView.setAdapter(adapter);
        /* 初始化加载更多 */
        recyclerView.setLoadMoreListener(this::loadMore);
        /* 初始化下拉刷新 */
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FF9900"), Color.parseColor("#009900"), Color.parseColor("#000099"));
        swipeRefreshLayout.setOnRefreshListener(this::refresh);
        // 首先从数据库读取 在判断是否需要加载
        List<Image> localImageList = Post.getAllImages(realm, 1, type);
        imageArrayList.addAll(localImageList);
        adapter.notifyItemRangeInserted(0, localImageList.size());
        // 如果数据为空 或 时间大于30分钟 则更新
        if (localImageList.size() == 0
            || TimeHelper.getThatTimeOffsetByNow(localImageList.get(0).getPost().getComment_date()) > 30 * TimeHelper.ONE_MIN) {
            refresh();
        }
    }

    void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        Parser.getInstance().getPictureData(page = 1, type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                list -> {
                    swipeRefreshLayout.setRefreshing(false);
                    imageArrayList.clear();
                    adapter.notifyDataSetChanged();
                    // 写入数据库
                    realm.beginTransaction();
                    for (Post post : list) {
                        Post saved = realm.copyToRealmOrUpdate(post);
                        for (Image image : post.getTempImages()) {
                            Image savedImage = realm.copyToRealmOrUpdate(image);
                            saved.getImages().add(savedImage);
                        }
                    }
                    realm.commitTransaction();
                    // 取出图片
                    List<Image> imageList = Post.getAllImages(list);
                    int size = imageList.size();
                    imageArrayList.addAll(imageList);
                    adapter.notifyItemRangeInserted(0, size);

                },
                throwable -> Log.e("Refresh", throwable.toString())
            );
    }

    void loadMore() {
        smoothProgressBar.setVisibility(View.VISIBLE);
        Parser.getInstance().getPictureData(++page, type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                list -> {
                    smoothProgressBar.setVisibility(View.GONE);
                    // 写入数据库
                    realm.beginTransaction();
                    for (Post post : list) {
                        Post saved = realm.copyToRealmOrUpdate(post);
                        for (Image image : post.getTempImages()) {
                            Image savedImage = realm.copyToRealmOrUpdate(image);
                            saved.getImages().add(savedImage);
                        }
                    }
                    realm.commitTransaction();
                    // 取出图片
                    List<Image> imageList = Post.getAllImages(list);
                    int start = imageArrayList.size();
                    int size = imageList.size();
                    imageArrayList.addAll(imageList);
                    adapter.notifyItemRangeInserted(start, size);
                },
                throwable -> Log.e("LoadMore", throwable.toString())
            );
    }

    @Override
    public void onDetach() {
        super.onDetach();
        realm.close();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
