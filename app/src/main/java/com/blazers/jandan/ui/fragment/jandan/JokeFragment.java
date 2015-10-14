package com.blazers.jandan.ui.fragment.jandan;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.models.jandan.Image;
import com.blazers.jandan.models.jandan.JokePost;
import com.blazers.jandan.models.jandan.Post;
import com.blazers.jandan.network.Parser;
import com.blazers.jandan.util.RecyclerViewHelper;
import com.blazers.jandan.util.TimeHelper;
import com.blazers.jandan.views.GreySpaceItemDerocation;
import com.blazers.jandan.views.widget.LoadMoreRecyclerView;
import com.blazers.jandan.views.widget.ThumbTextButton;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import io.realm.Realm;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blazers on 15/9/1.
 */
public class JokeFragment extends Fragment{

    public static final String TAG = JokeFragment.class.getSimpleName();
    private Realm realm;

    @Bind(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_list) LoadMoreRecyclerView jokeList;
    @Bind(R.id.load_more_progress) SmoothProgressBar smoothProgressBar;

    private JokeAdapter adapter;
    private ArrayList<JokePost> mJokePostArrayList = new ArrayList<>();
    private int mPage = 1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        realm = Realm.getInstance(context);
    }

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
        jokeList.setLayoutManager(RecyclerViewHelper.getVerticalLinearLayoutManager(getActivity()));
        jokeList.addItemDecoration(new GreySpaceItemDerocation());
        jokeList.setItemAnimator(new SlideInUpAnimator());
        adapter = new JokeAdapter();
        jokeList.setAdapter(adapter);
        /* Loadmore */
        jokeList.setLoadMoreListener(this::loadMore);
        /* Refresh */
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FF9900"), Color.parseColor("#009900"), Color.parseColor("#000099"));
        swipeRefreshLayout.setOnRefreshListener(this::refresh);
        //
        List<JokePost> localImageList = JokePost.getAllPost(realm, 1);
        mJokePostArrayList.addAll(localImageList);
        adapter.notifyItemRangeInserted(0, localImageList.size());
        // 如果数据为空 或 时间大于30分钟 则更新
        if (localImageList.size() == 0
            || TimeHelper.getThatTimeOffsetByNow(localImageList.get(0).getComment_date()) > 30 * TimeHelper.ONE_MIN) {
            refresh();
        }

    }

    void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        mPage = 1;
        Parser parser = Parser.getInstance();
        parser.getJokeData(mPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(data -> {
                swipeRefreshLayout.setRefreshing(false);
                mJokePostArrayList.clear();
                adapter.notifyDataSetChanged();
                // 写入数据库
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(data);
                realm.commitTransaction();
                // 插入数据
                int size = data.size();
                mJokePostArrayList.addAll(data);
                adapter.notifyItemRangeInserted(0 ,size);
            }, throwable -> Log.e("Joke", throwable.toString()));
    }

    void loadMore() {
        smoothProgressBar.setVisibility(View.VISIBLE);
        mPage ++;
        Parser parser = Parser.getInstance();
        parser.getJokeData(mPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(data -> {
                smoothProgressBar.setVisibility(View.GONE);
                // 写入数据库
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(data);
                realm.commitTransaction();
                // 插入数据
                int start = mJokePostArrayList.size();
                int size = data.size();
                mJokePostArrayList.addAll(data);
                adapter.notifyItemRangeInserted(start ,size);
            }, throwable -> Log.e("Joke", throwable.toString()));
    }


    class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.JokeHolder> {

        private LayoutInflater inflater;

        public JokeAdapter() {
            inflater = LayoutInflater.from(getActivity());
        }

        @Override
        public JokeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.item_jandan_joke, parent, false);
            return new JokeHolder(v);
        }

        @Override
        public void onBindViewHolder(JokeHolder holder, int position) {
            JokePost joke = mJokePostArrayList.get(position);
            holder.content.setText(joke.getComment_content());
            holder.author.setText("@"+joke.getComment_author());
            holder.date.setText(TimeHelper.getSocialTime(joke.getComment_date()));
            holder.thumbUp.setThumbText("15");
            holder.thumbDown.setThumbText("15");
            holder.comment.setThumbText("15");
        }

        @Override
        public int getItemCount() {
            return mJokePostArrayList.size();
        }

        class JokeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView content, author, date;
            public ThumbTextButton thumbUp, thumbDown, comment;
            public JokeHolder(View itemView) {
                super(itemView);
                content = (TextView) itemView.findViewById(R.id.content);
                author = (TextView) itemView.findViewById(R.id.author);
                date = (TextView) itemView.findViewById(R.id.date);

                thumbUp = (ThumbTextButton) itemView.findViewById(R.id.btn_oo);
                thumbDown = (ThumbTextButton) itemView.findViewById(R.id.btn_xx);
                comment = (ThumbTextButton) itemView.findViewById(R.id.btn_comment);

                thumbUp.setOnClickListener(this);
                thumbDown.setOnClickListener(this);
                comment.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_oo:
                        thumbUp.addThumbText(1);
                        break;
                    case R.id.btn_xx:
                        thumbDown.addThumbText(1);
                        break;
                }
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        realm.close();
    }
}
