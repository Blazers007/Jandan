package com.blazers.jandan.ui.fragment.favoritesub;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import com.blazers.jandan.R;
import com.blazers.jandan.models.db.local.LocalFavImages;
import com.blazers.jandan.ui.activity.ImageDetailActivity;
import com.blazers.jandan.ui.fragment.base.BaseSwipeRefreshFragment;
import com.blazers.jandan.views.PopupActionModeBar;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Sort;

/**
 * Created by Blazers on 2015/11/12.
 */
public class FavoriteImageFragment extends BaseSwipeRefreshFragment {

    private List<LocalFavImages> list, mCache;
    private Set<LocalFavImages> mSelectedFavItems;
    private FavImageAdapter adapter;
    private PopupActionModeBar mPopupActionModeBar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_common_fav_refresh_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trySetupSwipeRefreshLayout();
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter = new FavImageAdapter());
        refresh();
    }

    @Override
    public void refresh() {
        list.clear();
        List<LocalFavImages> addons = realm.where(LocalFavImages.class).findAllSorted("favTime", Sort.DESCENDING);
        if (null != addons)
            list.addAll(addons);
        refreshComplete();
        adapter.notifyDataSetChanged();
    }

    /**
     * Adapter
     */
    class FavImageAdapter extends RecyclerView.Adapter<FavImageAdapter.MeizhiHolder> {

        @Override
        public MeizhiHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MeizhiHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_favorite_image, parent, false));
        }

        @Override
        public void onBindViewHolder(MeizhiHolder holder, int position) {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(list.get(position).getUrl()))
                    .setResizeOptions(new ResizeOptions(320, 320))
                    .build();
            holder.simpleDraweeView.setController(Fresco.newDraweeControllerBuilder()
                    .setOldController(holder.simpleDraweeView.getController())
                    .setImageRequest(request)
                    .build());
            if (null != mSelectedFavItems && mSelectedFavItems.contains(list.get(position))) {
                // 显示并播放动画
                holder.select.setVisibility(View.VISIBLE);
                holder.select.animate().scaleX(1).scaleY(1).setDuration(250).setInterpolator(new BounceInterpolator()).withStartAction(() -> {
                    holder.select.setScaleX(0.6f);
                    holder.select.setScaleY(0.6f);
                }).start();
            } else {
                // 隐藏
                holder.select.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MeizhiHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.content)
            SimpleDraweeView simpleDraweeView;
            @BindView(R.id.select)
            ImageView select;

            public MeizhiHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(v -> {
                    if (null == mPopupActionModeBar) {
                        ArrayList<String> stringList = new ArrayList<>();
                        for (LocalFavImages item : list)
                            stringList.add(item.getUrl());
                        Intent intent = new Intent(getActivity(), ImageDetailActivity.class)
                                .putExtra("Position", getAdapterPosition())
                                .putStringArrayListExtra("List", stringList);
                        startActivity(intent);
                    } else {
                        LocalFavImages item = list.get(getAdapterPosition());
                        if (mSelectedFavItems.contains(item)) {
                            // 取消选择
                            mSelectedFavItems.remove(item);
                            notifyItemChanged(getAdapterPosition());
                            mPopupActionModeBar.minusSelection();
                        } else {
                            mSelectedFavItems.add(item);
                            notifyItemChanged(getAdapterPosition());
                            mPopupActionModeBar.addSelection();
                        }
                    }
//                    LocalFavImages images = list.get(getAdapterPosition());
                    //                    startActivity(
//                        new Intent(getActivity(), ImageViewerActivity.class)
//                            .putExtra(ViewImageEvent.KEY, new ViewImageEvent(images.getUrl(), ""))
//                    );
//                    Intent intent = new Intent(getActivity(), ImageViewerActivity.class)
//                            .putExtra(ViewImageEvent.KEY, new ViewImageEvent(images.getUrl(), ""));
//                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), simpleDraweeView, "sharedView");
//                    ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
                });
                /**
                 * 长按进入编辑模式
                 * */
                itemView.setOnLongClickListener(v -> {
                    mPopupActionModeBar = new PopupActionModeBar(getActivity(), new PopupActionModeBar.ActionbarCallback() {
                        @Override
                        public void onBackPressed() {
                            mPopupActionModeBar = null;
                            mSelectedFavItems.clear();
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onDeletePress() {
                            mPopupActionModeBar = null;
                            mCache = new ArrayList<>();
                            mCache.addAll(list);
                            // 首先删除List使显示改变
                            for (LocalFavImages item : mSelectedFavItems) {
                                list.remove(item);
                            }
                            notifyDataSetChanged();
                            Runnable runnable = () -> {
                                // 真正的移除工作
                                if (null == mSelectedFavItems)
                                    return;
                                for (LocalFavImages item : mSelectedFavItems)
                                    LocalFavImages.setThisFavedOrNot(false, realm, item.getUrl());
                                mSelectedFavItems = null;
                                mCache = null;
                            };
                            Snackbar.make(recyclerView, "已经删除以上收藏", Snackbar.LENGTH_SHORT).setActionTextColor(Color.rgb(201, 201, 201)).setAction("撤销", vi -> {
                                // 缓存变量 并返还原有值
                                if (null == mCache)
                                    return;
                                recyclerView.removeCallbacks(runnable);
                                list.clear();
                                list.addAll(mCache);
                                notifyDataSetChanged();
                                mSelectedFavItems = null;
                                mCache = null;
                            }).show();
                            // 若短时间内再次点击 会出现问题
                            recyclerView.postDelayed(runnable, 3000);
                        }
                    });
                    mSelectedFavItems = new HashSet<>();
                    mSelectedFavItems.add(list.get(getAdapterPosition()));
                    notifyItemChanged(getAdapterPosition());
//                    Rxbus.getInstance().send(new EnterSelectModeEvent());
                    return true;
                });
            }
        }
    }
}
