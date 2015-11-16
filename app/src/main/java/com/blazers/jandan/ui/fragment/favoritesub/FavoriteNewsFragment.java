package com.blazers.jandan.ui.fragment.favoritesub;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.models.db.local.LocalFavNews;
import com.blazers.jandan.models.db.sync.NewsPost;
import com.blazers.jandan.ui.fragment.base.BaseSwipeRefreshFragment;
import com.blazers.jandan.views.nightwatch.WatchTextView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blazers on 2015/11/13.
 *
 * 不存在LoadMore模块
 */
public class FavoriteNewsFragment extends BaseSwipeRefreshFragment {

    private List<LocalFavNews> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_common_fav_refresh_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        trySetupSwipeRefreshLayout();
        // Init
        list = new ArrayList<>();
        List<LocalFavNews> addons = realm.where(LocalFavNews.class).findAllSorted("favTime", false);
        if (null != addons)
            list.addAll(addons);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new FavNewsAdapter());

    }

    @Override
    public void refresh() {

    }

    /**
     * Adapter
     * */
    class FavNewsAdapter extends RecyclerView.Adapter<FavNewsAdapter.NewsHolder> {
        @Override
        public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new NewsHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_favorite_news, parent, false));
        }

        @Override
        public void onBindViewHolder(NewsHolder holder, int position) {
            NewsPost post = list.get(position).getNewsPost();
            holder.simpleDraweeView.setImageURI(Uri.parse(post.getThumbUrl()));
            holder.watchTextView.setText(post.getTitle());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class NewsHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.news_image) SimpleDraweeView simpleDraweeView;
            @Bind(R.id.news_title) WatchTextView watchTextView;

            public NewsHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
