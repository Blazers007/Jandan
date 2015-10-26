package com.blazers.jandan.ui.fragment.sub;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.blazers.jandan.R;
import com.blazers.jandan.models.db.sync.ImagePost;
import com.blazers.jandan.models.pojo.image.ImageRelateToPost;
import com.blazers.jandan.network.Parser;
import com.blazers.jandan.ui.fragment.base.BaseSwipeLoadMoreFragment;
import com.blazers.jandan.ui.adapters.JandanImageAdapter;
import com.blazers.jandan.util.DBHelper;
import com.blazers.jandan.util.NetworkHelper;
import rx.android.schedulers.AndroidSchedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blazers on 15/9/8.
 */
public class PicFragment extends BaseSwipeLoadMoreFragment {

    private JandanImageAdapter adapter;
    private ArrayList<ImageRelateToPost> imageArrayList = new ArrayList<>();
    private int mPage = 1;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_load, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null && getArguments().getString("type") != null) {
            type = getArguments().getString("type");
            initRecyclerView();
        }
    }

    void initRecyclerView() {
        trySetupSwipeRefreshLayout();
        trySetupRecyclerViewWithAdapter(adapter = new JandanImageAdapter(getActivity(), realm, imageArrayList));
        // 首先从数据库读取 在判断是否需要加载
        List<ImageRelateToPost> localImageList = ImagePost.getAllImagesFromDB(realm, 1, type);
        imageArrayList.addAll(localImageList);
        adapter.notifyItemRangeInserted(0, localImageList.size());
        // 如果数据为空 或 时间大于30分钟 则更新
        if (localImageList.size() == 0) {
            swipeRefreshLayout.post(()->swipeRefreshLayout.setRefreshing(true));
            refresh();
        }
    }

    @Override
    public void refresh() {
        mPage = 1;
        if (NetworkHelper.netWorkAvailable(getActivity())) {
            Parser.getInstance().getPictureData(mPage, type) // 是IO线程还是Main县城由该方法确定
                .observeOn(AndroidSchedulers.mainThread())          // 更新在某县城由自己决定
                .doOnNext(list -> DBHelper.saveToRealm(realm, list))
                .subscribe(list -> {
                    refreshComplete();
                    // 处理数据
                    imageArrayList.clear();
                    adapter.notifyDataSetChanged();
                    // 取出图片
                    List<ImageRelateToPost> imageRelateToPostList = ImagePost.getAllImageFromList(list);
                    int size = imageRelateToPostList.size();
                    imageArrayList.addAll(imageRelateToPostList);
                    adapter.notifyItemRangeInserted(0, size);
                }, throwable -> {
                    refreshError();
                    Log.e("Refresh", throwable.toString());
                });
        } else {
            List<ImagePost> list = ImagePost.getImagePosts(realm, mPage, type);
            if (null != list && list.size() > 0) {
                List<ImageRelateToPost> imageRelateToPostList = ImagePost.getAllImageFromList(list);
                int size = imageRelateToPostList.size();
                imageArrayList.addAll(imageRelateToPostList);
                adapter.notifyItemRangeInserted(0, size);
            } else {
                Toast.makeText(getActivity(), R.string.there_is_no_more, Toast.LENGTH_SHORT).show();
            }
            refreshComplete();
        }
    }

    @Override
    public void loadMore() {
        mPage++;
        if (NetworkHelper.netWorkAvailable(getActivity())) {
            Parser.getInstance().getPictureData(mPage, type)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(list -> DBHelper.saveToRealm(realm, list))
                .subscribe(list -> {
                    loadMoreComplete();
                    // 取出图片
                    List<ImageRelateToPost> imageRelateToPostList = ImagePost.getAllImageFromList(list);
                    int start = imageArrayList.size();
                    int size = imageRelateToPostList.size();
                    imageArrayList.addAll(imageRelateToPostList);
//                    adapter.notifyItemRangeInserted(start, size);
                    adapter.notifyDataSetChanged();
                }, throwable -> {
                    loadMoreError();
                    Log.e("LoadMore", throwable.toString());
                });
        } else {
            List<ImagePost> list = ImagePost.getImagePosts(realm, mPage, type);
            if (null != list && list.size() > 0) {
                List<ImageRelateToPost> imageRelateToPostList = ImagePost.getAllImageFromList(list);
                int start = imageArrayList.size();
                int size = imageRelateToPostList.size();
                imageArrayList.addAll(imageRelateToPostList);
//                adapter.notifyItemRangeInserted(start, size);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), R.string.there_is_no_more, Toast.LENGTH_SHORT).show();
            }
            loadMoreComplete();
        }
    }
}
