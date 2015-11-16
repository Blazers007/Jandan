package com.blazers.jandan.ui.fragment.favoritesub;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.blazers.jandan.ui.fragment.base.BaseSwipeRefreshFragment;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blazers on 2015/11/13.
 */
public class FavoriteJokesFragment extends BaseSwipeRefreshFragment {


    private List<LocalFavJokes> list;

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
        recyclerView.setAdapter(new FavJokeAdapter());
    }

    @Override
    public void refresh() {
        list.clear();
        List<LocalFavJokes> addons = realm.where(LocalFavJokes.class).findAllSorted("favTime", false);
        if (null != addons)
            list.addAll(addons);
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
            }
        }
    }
}
