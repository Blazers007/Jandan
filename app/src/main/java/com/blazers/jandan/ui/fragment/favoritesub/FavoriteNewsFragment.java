package com.blazers.jandan.ui.fragment.favoritesub;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.blazers.jandan.ui.activity.NewsReadActivity;
import com.blazers.jandan.ui.fragment.base.BaseSwipeRefreshFragment;
import com.blazers.jandan.views.VerticalDividerItemDecoration;
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
    private FavNewsAdapter adapter;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new VerticalDividerItemDecoration(getActivity(), 2, Color.rgb(201, 201, 201)));
        recyclerView.setAdapter(adapter = new FavNewsAdapter());
        refresh();
    }

    @Override
    public void refresh() {
        list.clear();
        List<LocalFavNews> addons = realm.where(LocalFavNews.class).findAllSorted("favTime", false);
        if (null != addons)
            list.addAll(addons);
        adapter.notifyDataSetChanged();
        refreshComplete();
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
                //
                itemView.setOnClickListener(v->{
                    NewsPost post = list.get(getAdapterPosition()).getNewsPost();
                    startActivity(
                        new Intent(getActivity(), NewsReadActivity.class)
                            .putExtra("id", post.getId())
                            .putExtra("title", post.getTitle())
                    );
                });
                //
                itemView.setOnLongClickListener(v->{
                    int position = getAdapterPosition();
                    LocalFavNews news = list.remove(position);
                    long id = news.getId();
                    long time = news.getFavTime();
                    adapter.notifyItemRemoved(position);
                    LocalFavNews.setThisFavedOrNot(false, realm, id);
                    // 不需要考虑作用域？会不会导致临时变量无法释放?
                    Snackbar.make(recyclerView, "已经删除该收藏",Snackbar.LENGTH_SHORT).setActionTextColor(Color.rgb(201, 201, 201)).setAction("撤销", vi -> {
                        LocalFavNews delete = LocalFavNews.setThisFavedOrNot(true, realm, id, time);
                        list.add(position, delete);
                        adapter.notifyItemInserted(position); // getAdapterPosition() 因为已经被移除 故返回 -1
                    }).show();
                    return true;
                });
            }
        }
    }
}
