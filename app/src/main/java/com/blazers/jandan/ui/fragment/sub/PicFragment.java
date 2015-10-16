package com.blazers.jandan.ui.fragment.sub;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.blazers.jandan.R;
import com.blazers.jandan.models.jandan.Image;
import com.blazers.jandan.models.jandan.Post;
import com.blazers.jandan.network.Parser;
import com.blazers.jandan.ui.fragment.base.BaseSwipeLoadMoreFragment;
import com.blazers.jandan.util.TimeHelper;
import com.blazers.jandan.ui.adapters.JandanImageAdapter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blazers on 15/9/8.
 */
public class PicFragment extends BaseSwipeLoadMoreFragment {

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
        trySetupRecyclerViewWithAdapter(adapter = new JandanImageAdapter(getActivity(), imageArrayList));
        // 首先从数据库读取 在判断是否需要加载
        List<Image> localImageList = Post.getAllImages(realm, 1, type);
        imageArrayList.addAll(localImageList);
        adapter.notifyItemRangeInserted(0, localImageList.size());
        // 如果数据为空 或 时间大于30分钟 则更新
        if (localImageList.size() == 0) {
            refresh();
        }
    }

    @Override
    public void refresh() {
        Parser.getInstance().getPictureData(page = 1, type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                list -> {
                    refreshComplete();
                    // 处理数据
                    imageArrayList.clear();
                    adapter.notifyDataSetChanged();
                    // 写入数据库
                    realm.beginTransaction();
                    for (Post post : list) {
                        Post saved = realm.copyToRealmOrUpdate(post);
                        for (Image image : post.getTempImages()) {
                            Image savedImage = realm.copyToRealmOrUpdate(image);
                            // 建立关系
                            savedImage.setPost(saved);
                            saved.getImages().add(savedImage);
                        }
                    }
                    realm.commitTransaction();
                    // 取出图片
                    List<Image> imageList = Post.getAllImages(realm, 1, type);
                    int size = imageList.size();
                    imageArrayList.addAll(imageList);
                    adapter.notifyItemRangeInserted(0, size);

                },
                throwable -> {
                    refreshError();
                    Log.e("Refresh", throwable.toString());
                }
            );
    }

    @Override
    public void loadMore() {
        Parser.getInstance().getPictureData(++page, type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                list -> {
                    loadMoreComplete();
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
                throwable -> {
                    loadMoreError();
                    Log.e("LoadMore", throwable.toString());
                }
            );
    }
}
