package com.blazers.jandan.ui.fragment.jandan;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import com.blazers.jandan.models.jandan.news.NewsPost;
import com.blazers.jandan.network.Parser;
import com.blazers.jandan.ui.activity.NewsReadActivity;
import com.blazers.jandan.util.RecyclerViewHelper;
import com.blazers.jandan.views.widget.LoadMoreRecyclerView;
import com.facebook.drawee.view.SimpleDraweeView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.ArrayList;

/**
 * Created by Blazers on 2015/8/27.
 */
public class NewsFragment extends Fragment {

    public static final String TAG = NewsFragment.class.getSimpleName();

    @Bind(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_list) LoadMoreRecyclerView newsList;
    @Bind(R.id.load_more_progress) SmoothProgressBar smoothProgressBar;

    private NewsAdapter adapter;
    private ArrayList<NewsPost> mNewsPostArrayList = new ArrayList<>();
    private int mPage = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_refresh_load, container, false);
        ButterKnife.bind(this, root);
        initRecyclerView();
        return root;
    }

    void initRecyclerView() {
        /* 从数据库中读取 有两个标志位标志当前的第一个跟最后一个 然后从数据库中读取  顺便发起请求Service更新数据库 */
        newsList.setLayoutManager(RecyclerViewHelper.getVerticalLinearLayoutManager(getActivity()));
        newsList.addItemDecoration(RecyclerViewHelper.getDefaultVeriticalDivider(getActivity()));
        newsList.setItemAnimator(new SlideInLeftAnimator());
        /* Loadmore */
        newsList.setLoadMoreListener(() -> {
            smoothProgressBar.setVisibility(View.VISIBLE);
            mPage++;
            getData();
        });

        /* Set Adapter */
        adapter = new NewsAdapter();
        newsList.setAdapter(adapter);
        /* */
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FF9900"), Color.parseColor("#009900"), Color.parseColor("#000099"));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            /* 发起加载 加载后从数据库加载 然后显示 然后隐藏 */
            mPage = 1;
            getData();
        });
        getData();
    }

    private void getData() {
        /* TODO: 首先加载上次最后看到的？还是上次缓存的最新的一页数据 */
        Parser parser = Parser.getInstance();
        parser.getNewsData(mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    mNewsPostArrayList.addAll(data);
                    adapter.notifyDataSetChanged();
                }, throwable -> throwable.printStackTrace());
    }

    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder>{

        private LayoutInflater inflater;

        public NewsAdapter() {
            inflater = LayoutInflater.from(getActivity());
        }

        @Override
        public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.item_jandan_news_list, parent, false);
            return new NewsHolder(v);
        }


        @Override
        public void onBindViewHolder(NewsHolder newsHolder, int i) {
            NewsPost newsList = mNewsPostArrayList.get(i);
            newsHolder.draweeView.setImageURI(Uri.parse(newsList.getThumbUrl()));
            newsHolder.title.setText(newsList.getTitle());
            newsHolder.content.setText(newsList.getAuthorName() + "  @ " + newsList.getDate());
        }

        @Override
        public int getItemCount() {
            return mNewsPostArrayList.size();
        }

        class NewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            public SimpleDraweeView draweeView;
            public TextView title, content;

            public NewsHolder(View itemView) {
                super(itemView);
                draweeView = (SimpleDraweeView) itemView.findViewById(R.id.news_image);
                title = (TextView) itemView.findViewById(R.id.news_title);
                content = (TextView) itemView.findViewById(R.id.news_content);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                NewsPost newsList = mNewsPostArrayList.get(getAdapterPosition());
                startActivity(
                        new Intent(getActivity(), NewsReadActivity.class)
                                .putExtra("id", newsList.getId())
                                .putExtra("title", newsList.getTitle())
                );
            }
        }
    }
}
