package com.blazers.jandan.ui.fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.network.JandanParser;
import com.blazers.jandan.orm.joke.Joke;
import com.blazers.jandan.orm.meizi.Picture;
import com.blazers.jandan.util.RecyclerViewHelper;
import com.blazers.jandan.widget.LoadMoreRecyclerView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Blazers on 15/9/1.
 */
public class JokeFragment extends Fragment{

    public static final String TAG = JokeFragment.class.getSimpleName();

    @Bind(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_list) LoadMoreRecyclerView jokeList;
    @Bind(R.id.load_more_progress) SmoothProgressBar smoothProgressBar;

    private Realm mRealm;
    private JokeAdapter adapter;
    private RealmResults<Joke> jokeRealmResults;
    private int listSize;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_refresh_load, container, false);
        ButterKnife.bind(this, root);
        initRecyclerView();
        initJokes();
        return root;
    }

    void initRecyclerView() {
        /* 从数据库中读取 有两个标志位标志当前的第一个跟最后一个 然后从数据库中读取  顺便发起请求Service更新数据库 */
        jokeList.setLayoutManager(RecyclerViewHelper.getVerticalLinearLayoutManager(getActivity()));
        jokeList.addItemDecoration(RecyclerViewHelper.getDefaultVeriticalDivider(getActivity()));
        /* Loadmore */
        jokeList.setLoadMoreListener(() -> {
            smoothProgressBar.setVisibility(View.VISIBLE);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    JandanParser.getInstance().parseJokeAPI(false);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    adapter.notifyDataSetChanged();
                    jokeList.endLoading();
                    smoothProgressBar.setVisibility(View.GONE);
                    super.onPostExecute(aVoid);
                }
            }.execute();
        });

        /* Set Adapter */
        adapter = new JokeAdapter();
        jokeList.setAdapter(adapter);
        /* */
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FF9900"), Color.parseColor("#009900"), Color.parseColor("#000099"));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            /* 发起加载 加载后从数据库加载 然后显示 然后隐藏 */
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    JandanParser.getInstance().parseNewsAPI(true);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    /* 应该首先缓存到数据库 然后仅仅加载部分 如果数据库没有则更新数据库并显示 */
                    swipeRefreshLayout.setRefreshing(false);
                    super.onPostExecute(aVoid);
                }
            }.execute();
        });
    }


    void initJokes() {
        mRealm = Realm.getInstance(getActivity());
        jokeRealmResults = mRealm.where(Joke.class).findAllSorted("comment_ID", false);
        listSize = jokeRealmResults.size();
        if (listSize == 0) {
            swipeRefreshLayout.setRefreshing(true);
            new AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... params) {
                    JandanParser.getInstance().parseJokeAPI(true);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    jokeRealmResults = mRealm.where(Joke.class).findAllSorted("comment_ID", false);
                    listSize = jokeRealmResults.size();
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    super.onPostExecute(aVoid);
                }
            }.execute();
        }
    }

    class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.JokeHolder> {

        private LayoutInflater inflater;

        public JokeAdapter() {
            inflater = LayoutInflater.from(getActivity());
        }

        @Override
        public JokeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.item_joke, parent, false);
            return new JokeHolder(v);
        }

        @Override
        public void onBindViewHolder(JokeHolder holder, int position) {
            Joke joke = jokeRealmResults.get(position);
            holder.content.setText(joke.getComment_contnet());
        }

        @Override
        public int getItemCount() {
            return listSize;
        }

        class JokeHolder extends RecyclerView.ViewHolder {
            public TextView content;
            public JokeHolder(View itemView) {
                super(itemView);
                content = (TextView) itemView.findViewById(R.id.content);
            }
        }
    }

    @Override
    public void onDestroyView() {
        if (mRealm != null)
            mRealm.close();
        super.onDestroyView();
    }
}
