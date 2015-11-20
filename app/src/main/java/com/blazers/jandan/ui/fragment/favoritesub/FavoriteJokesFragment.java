package com.blazers.jandan.ui.fragment.favoritesub;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.models.db.local.LocalFavJokes;
import com.blazers.jandan.models.db.local.LocalFavNews;
import com.blazers.jandan.ui.fragment.base.BaseSwipeRefreshFragment;
import com.blazers.jandan.views.VerticalDividerItemDecoration;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blazers on 2015/11/13.
 */
public class FavoriteJokesFragment extends BaseSwipeRefreshFragment {

    private List<LocalFavJokes> list;
    private FavJokeAdapter adapter;

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
        // init
        list = new ArrayList<>();
        List<LocalFavJokes> addons = realm.where(LocalFavJokes.class).findAllSorted("favTime", false);
        if (null != addons)
            list.addAll(addons);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new VerticalDividerItemDecoration(getActivity(), 2, Color.rgb(201, 201, 201)));
        recyclerView.setAdapter(adapter = new FavJokeAdapter());
        refresh();
    }

    @Override
    public void refresh() {
        list.clear();
        List<LocalFavJokes> addons = realm.where(LocalFavJokes.class).findAllSorted("favTime", false);
        if (null != addons)
            list.addAll(addons);
        refreshComplete();
    }

    /**
     * Adapter
     * */
    class FavJokeAdapter extends RecyclerView.Adapter<FavJokeAdapter.JokeHolder> {

        @Override
        public JokeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new JokeHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_favorite_joke, parent, false));
        }

        @Override
        public void onBindViewHolder(JokeHolder holder, int position) {
            holder.content.setText(list.get(position).getJokePost().getComment_content());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class JokeHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.fav_joke_text) ExpandableTextView content;
            public JokeHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                //
                itemView.setOnLongClickListener(v->{
                    int position = getAdapterPosition();
                    LocalFavJokes joke = list.remove(position);
                    long id = joke.getComment_ID();
                    long time = joke.getFavTime();
                    adapter.notifyItemRemoved(position);
                    LocalFavJokes.setThisFavedOrNot(false, realm, id);
                    // 不需要考虑作用域？会不会导致临时变量无法释放?
                    Snackbar.make(recyclerView, "已经删除该收藏", Snackbar.LENGTH_SHORT).setActionTextColor(Color.rgb(201, 201, 201)).setAction("撤销", vi -> {
                        LocalFavJokes delete = LocalFavJokes.setThisFavedOrNot(true, realm, id, time);
                        list.add(position, delete);
                        adapter.notifyItemInserted(position); // getAdapterPosition() 因为已经被移除 故返回 -1
                    }).show();
                    return true;
                });
            }
        }
    }
}
