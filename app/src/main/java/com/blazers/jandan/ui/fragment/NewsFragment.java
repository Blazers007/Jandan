package com.blazers.jandan.ui.fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.util.network.JandanParser;
import com.blazers.jandan.widget.DownloadFrescoView;
import com.blazers.jandan.widget.LoadMoreRecyclerView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by Blazers on 2015/8/27.
 */
public class NewsFragment extends Fragment {

    private static final String TAG = NewsFragment.class.getSimpleName();

    @Bind(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_list) LoadMoreRecyclerView meiziList;
    @Bind(R.id.load_more_progress) SmoothProgressBar smoothProgressBar;

    private NewsAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_meizi, container, false);
        ButterKnife.bind(this, root);
        initRecyclerView();
        return root;
    }

    void initRecyclerView() {
        /* 从数据库中读取 有两个标志位标志当前的第一个跟最后一个 然后从数据库中读取  顺便发起请求Service更新数据库 */
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        meiziList.setLayoutManager(linearLayoutManager);
        /* Loadmore */
        meiziList.setLoadMoreListener(() -> {
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
                    meiziList.endLoading();
                    smoothProgressBar.setVisibility(View.GONE);
                    super.onPostExecute(aVoid);
                }
            }.execute();
        });

        /* Set Adapter */
        adapter = new NewsAdapter();
        meiziList.setAdapter(adapter);

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

    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder>{

        private LayoutInflater inflater;

        public NewsAdapter() {
            inflater = LayoutInflater.from(getActivity());
        }

        @Override
        public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.item_meizi, parent, false);
            return new NewsHolder(v);
        }


        @Override
        public void onBindViewHolder(NewsHolder meiziHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        class NewsHolder extends RecyclerView.ViewHolder {

            public DownloadFrescoView draweeView;
            public TextView author;

            public NewsHolder(View itemView) {
                super(itemView);
                draweeView = (DownloadFrescoView) itemView.findViewById(R.id.drweeView);
                author = (TextView) itemView.findViewById(R.id.textView);
            }
        }
    }
}
