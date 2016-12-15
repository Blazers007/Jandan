package com.blazers.jandan.ui.fragment.favoritesub;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.blazers.jandan.R;
import com.blazers.jandan.ui.fragment.base.BaseSwipeRefreshFragment;
import com.blazers.jandan.widgets.VerticalDividerItemDecoration;

/**
 * Created by Blazers on 2015/11/13.
 */
public class FavoriteJokesFragment extends BaseSwipeRefreshFragment {

//    private List<LocalFavJokes> list;
//    private FavJokeAdapter adapter;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_common_fav_refresh_recyclerview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trySetupSwipeRefreshLayout();
        // init
//        list = new ArrayList<>();
//        List<LocalFavJokes> addons = realm.where(LocalFavJokes.class).findAllSorted("favTime", Sort.DESCENDING);
//        if (null != addons)
//            list.addAll(addons);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new VerticalDividerItemDecoration(getActivity(), 2, Color.rgb(201, 201, 201)));
//        mRecyclerView.setAdapter(adapter = new FavJokeAdapter());
        refresh();
    }

    @Override
    public void refresh() {
//        list.clear();
//        List<LocalFavJokes> addons = realm.where(LocalFavJokes.class).findAllSorted("favTime", Sort.DESCENDING);
//        if (null != addons)
//            list.addAll(addons);
//        refreshComplete();
    }

    /**
     * Adapter
     */
//    class FavJokeAdapter extends RecyclerView.Adapter<FavJokeAdapter.JokeHolder> {
//
//        @Override
//        public JokeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            return new JokeHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_favorite_joke, parent, false));
//        }
//
//        @Override
//        public void onBindViewHolder(JokeHolder holder, int position) {
//            holder.content.setText(list.get(position).getJokePost().getComment_content());
//        }
//
//        @Override
//        public int getItemCount() {
//            return list.size();
//        }
//
//        class JokeHolder extends RecyclerView.ViewHolder {
//            @BindView(R.id.fav_joke_text)
//            ExpandableTextView content;
//
//            public JokeHolder(View itemView) {
//                super(itemView);
//                ButterKnife.bind(this, itemView);
//                //
//                itemView.setOnLongClickListener(v -> {
//                    int position = getAdapterPosition();
//                    LocalFavJokes joke = list.remove(position);
//                    long id = joke.getComment_ID();
//                    long time = joke.getFavTime();
//                    adapter.notifyItemRemoved(position);
////                    LocalFavJokes.setThisFavedOrNot(false, realm, id);
//                    // 不需要考虑作用域？会不会导致临时变量无法释放?
//                    Snackbar.make(mRecyclerView, "已经删除该收藏", Snackbar.LENGTH_SHORT).setActionTextColor(Color.rgb(201, 201, 201)).setAction("撤销", vi -> {
////                        LocalFavJokes delete = LocalFavJokes.setThisFavedOrNot(true, realm, id, time);
////                        list.add(position, delete);
//                        adapter.notifyItemInserted(position); // getAdapterPosition() 因为已经被移除 故返回 -1
//                    }).show();
//                    return true;
//                });
//            }
//        }
//    }
}
