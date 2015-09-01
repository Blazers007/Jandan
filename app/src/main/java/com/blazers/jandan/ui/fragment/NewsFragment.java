package com.blazers.jandan.ui.fragment;

import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.orm.meizi.Picture;
import com.blazers.jandan.orm.news.NewsList;
import com.blazers.jandan.util.divider.DividerItemDecoration;
import com.blazers.jandan.util.network.JandanParser;
import com.blazers.jandan.widget.DownloadFrescoView;
import com.blazers.jandan.widget.LoadMoreRecyclerView;
import com.facebook.drawee.view.SimpleDraweeView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Blazers on 2015/8/27.
 */
public class NewsFragment extends Fragment {

    private static final String TAG = NewsFragment.class.getSimpleName();

    @Bind(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_list) LoadMoreRecyclerView newsList;
    @Bind(R.id.load_more_progress) SmoothProgressBar smoothProgressBar;

    private Realm realm;
    private NewsAdapter adapter;
    private RealmResults<NewsList> newsListRealmResults;
    private int listSize;
    private int nowViewPosition;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_meizi, container, false);
        ButterKnife.bind(this, root);
        initRecyclerView();
        initNews();
        return root;
    }

    void initRecyclerView() {
        /* 从数据库中读取 有两个标志位标志当前的第一个跟最后一个 然后从数据库中读取  顺便发起请求Service更新数据库 */
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        newsList.setLayoutManager(linearLayoutManager);
        newsList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        /* Loadmore */
        newsList.setLoadMoreListener(() -> {
            smoothProgressBar.setVisibility(View.VISIBLE);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    JandanParser.getInstance().parseMeiziAPI(false);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    adapter.notifyDataSetChanged();
                    newsList.endLoading();
                    smoothProgressBar.setVisibility(View.GONE);
                    super.onPostExecute(aVoid);
                }
            }.execute();
        });

        /* Set Adapter */
        adapter = new NewsAdapter();
        newsList.setAdapter(adapter);
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

                    swipeRefreshLayout.setRefreshing(false);
                    super.onPostExecute(aVoid);
                }
            }.execute();
        });
    }



    void initNews() {
        realm = Realm.getInstance(getActivity());
        newsListRealmResults = realm.where(NewsList.class).findAllSorted("id", false);
        listSize = newsListRealmResults.size();
        Log.e("SIZE", "= " + newsListRealmResults.size());
        /* Update 需要整合 以及更智能的自动更新判断 */
        if (listSize == 0) {
            swipeRefreshLayout.setRefreshing(true);
            new AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... params) {
                    JandanParser.getInstance().parseNewsAPI(true);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    newsListRealmResults = realm.where(NewsList.class).findAllSorted("id", false);
                    listSize = newsListRealmResults.size();
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    super.onPostExecute(aVoid);
                }
            }.execute();
        }
    }

    @Override
    public void onDestroyView() {
        if (realm != null)
            realm.close();
        super.onDestroyView();
    }

    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder>{

        private LayoutInflater inflater;

        public NewsAdapter() {
            inflater = LayoutInflater.from(getActivity());
        }

        @Override
        public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.item_news, parent, false);
            return new NewsHolder(v);
        }


        @Override
        public void onBindViewHolder(NewsHolder newsHolder, int i) {
            NewsList newsList = newsListRealmResults.get(i);
            newsHolder.draweeView.setImageURI(Uri.parse(newsList.getThumbUrl()));
            newsHolder.title.setText(newsList.getTitle());
            newsHolder.content.setText(newsList.getAuthor() + "  @ " + newsList.getDate());
        }

        @Override
        public int getItemCount() {
            return listSize;
        }

        class NewsHolder extends RecyclerView.ViewHolder {

            public SimpleDraweeView draweeView;
            public TextView title, content;

            public NewsHolder(View itemView) {
                super(itemView);
                draweeView = (SimpleDraweeView) itemView.findViewById(R.id.news_image);
                title = (TextView) itemView.findViewById(R.id.news_title);
                content = (TextView) itemView.findViewById(R.id.news_content);
            }
        }
    }
}
