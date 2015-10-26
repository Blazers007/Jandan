package com.blazers.jandan.ui.fragment.sub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.blazers.jandan.R;
import com.blazers.jandan.models.db.sync.NewsPost;
import com.blazers.jandan.network.Parser;
import com.blazers.jandan.ui.activity.NewsReadActivity;
import com.blazers.jandan.ui.fragment.base.BaseSwipeLoadMoreFragment;
import com.blazers.jandan.util.DBHelper;
import com.blazers.jandan.util.NetworkHelper;
import com.blazers.jandan.util.TimeHelper;
import com.facebook.drawee.view.SimpleDraweeView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blazers on 2015/8/27.
 */
public class NewsFragment extends BaseSwipeLoadMoreFragment {

    public static final String TAG = NewsFragment.class.getSimpleName();

    private NewsAdapter adapter;
    private ArrayList<NewsPost> mNewsPostArrayList = new ArrayList<>();
    private int mPage = 1;

    public NewsFragment() {
        super();
        setTAG(TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_load, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    void initRecyclerView() {
        trySetupSwipeRefreshLayout();
        trySetupRecyclerViewWithAdapter(adapter = new NewsAdapter());
        //
        List<NewsPost> localNewsList = NewsPost.getAllPost(realm, 1);
        mNewsPostArrayList.addAll(localNewsList);
        adapter.notifyItemRangeInserted(0, localNewsList.size());
        // 如果数据为空 或 时间大于30分钟 则更新
        if (localNewsList.size() == 0
            || TimeHelper.getThatTimeOffsetByNow(localNewsList.get(0).getDate()) > 30 * TimeHelper.ONE_MIN) {
            swipeRefreshLayout.post(()->swipeRefreshLayout.setRefreshing(true));
            refresh();
        }
    }

    @Override
    public void refresh() {
        mPage = 1;
        if (NetworkHelper.netWorkAvailable(getActivity())) {
            Parser parser = Parser.getInstance();
            parser.getNewsData(mPage)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(list -> DBHelper.saveToRealm(realm, list))
                .subscribe(list -> {
                    refreshComplete();
                    // 更新UI
                    mNewsPostArrayList.clear();
                    adapter.notifyDataSetChanged();
                    //
                    mNewsPostArrayList.addAll(list);
                    adapter.notifyItemRangeInserted(0, list.size());
                }, throwable -> {
                    refreshComplete();
                    Log.e("News", throwable.toString());
                });
        } else {
            List<NewsPost> list = NewsPost.getAllPost(realm, mPage);
            if (null != list && list.size() > 0) {
                mNewsPostArrayList.clear();
                //
                mNewsPostArrayList.addAll(list);
                adapter.notifyItemRangeInserted(0, list.size());
            } else {
                Toast.makeText(getActivity(), R.string.there_is_no_more, Toast.LENGTH_SHORT).show();
            }
            refreshComplete();
        }
    }

    @Override
    public void loadMore() {
        mPage++;
        if (NetworkHelper.netWorkAvailable(getActivity())) {
            Parser parser = Parser.getInstance();
            parser.getNewsData(mPage)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(list -> DBHelper.saveToRealm(realm, list))
                .subscribe(data -> {
                    loadMoreComplete();
                    // 更新UI
                    int start = mNewsPostArrayList.size();
                    mNewsPostArrayList.addAll(data);
//                    adapter.notifyItemRangeInserted(start, data.size());
                    adapter.notifyDataSetChanged();
                }, throwable -> {
                    loadMoreError();
                    Log.e("News LoadMore", throwable.toString());
                });
        } else {
            List<NewsPost> list = NewsPost.getAllPost(realm, mPage);
            if (null != list && list.size() > 0) {
                int start = mNewsPostArrayList.size();
                mNewsPostArrayList.addAll(list);
//                adapter.notifyItemRangeInserted(start, list.size());
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), R.string.there_is_no_more, Toast.LENGTH_SHORT).show();
            }
            loadMoreComplete();
        }
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
