package com.blazers.jandan.ui.fragment.favoritesub;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.models.db.local.LocalFavImages;
import com.blazers.jandan.models.db.local.LocalFavNews;
import com.blazers.jandan.rxbus.event.ViewImageEvent;
import com.blazers.jandan.ui.activity.ImageViewerActivity;
import com.blazers.jandan.ui.fragment.base.BaseSwipeRefreshFragment;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blazers on 2015/11/12.
 */
public class FavoriteImageFragment extends BaseSwipeRefreshFragment {

    private ArrayList<LocalFavImages> list;
    private FavImageAdapter adapter;

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
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void refresh() {
        List<LocalFavImages> addons = realm.where(LocalFavImages.class).findAllSorted("favTime", false);
        if (null != addons)
            list.addAll(addons);
        refreshComplete();
    }

    /**
     * Adapter
     * */
    class FavImageAdapter extends RecyclerView.Adapter<FavImageAdapter.MeizhiHolder> {

        @Override
        public MeizhiHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MeizhiHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_favorite_image, parent, false));
        }

        @Override
        public void onBindViewHolder(MeizhiHolder holder, int position) {
            holder.simpleDraweeView.setImageURI(Uri.parse(list.get(position).getUrl()));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MeizhiHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.content) SimpleDraweeView simpleDraweeView;

            public MeizhiHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(v->{
                    LocalFavImages images = list.get(getAdapterPosition());
                    startActivity(
                        new Intent(getActivity(), ImageViewerActivity.class)
                            .putExtra(ViewImageEvent.KEY, new ViewImageEvent(images.getUrl(), ""))
                    );
                });
                //
                itemView.setOnLongClickListener(v->{
                    int position = getAdapterPosition();
                    LocalFavImages image = list.remove(position);
                    String url = image.getUrl();
                    long time = image.getFavTime();
                    adapter.notifyItemRemoved(position);
                    LocalFavImages.setThisFavedOrNot(false, realm, url);
                    // 不需要考虑作用域？
                    Snackbar.make(recyclerView, "已经删除该收藏", Snackbar.LENGTH_SHORT).setActionTextColor(Color.rgb(201, 201, 201)).setAction("撤销", vi->{
                        LocalFavImages delete = LocalFavImages.setThisFavedOrNot(true, realm, url, time);
                        list.add(position, delete);
                        adapter.notifyItemInserted(position);
                    }).show();
                    return true;
                });
            }
        }
    }
}
