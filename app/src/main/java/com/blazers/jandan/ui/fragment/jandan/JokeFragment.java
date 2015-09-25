package com.blazers.jandan.ui.fragment.jandan;

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
import com.blazers.jandan.models.jandan.JokePost;
import com.blazers.jandan.network.Parser;
import com.blazers.jandan.util.RecyclerViewHelper;
import com.blazers.jandan.util.TimeHelper;
import com.blazers.jandan.views.GreySpaceItemDerocation;
import com.blazers.jandan.views.widget.LoadMoreRecyclerView;
import com.blazers.jandan.views.widget.ThumbTextButton;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.ArrayList;

/**
 * Created by Blazers on 15/9/1.
 */
public class JokeFragment extends Fragment{

    public static final String TAG = JokeFragment.class.getSimpleName();

    @Bind(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_list) LoadMoreRecyclerView jokeList;
    @Bind(R.id.load_more_progress) SmoothProgressBar smoothProgressBar;

    private JokeAdapter adapter;
    private ArrayList<JokePost> mJokePostArrayList = new ArrayList<>();
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
        jokeList.setLayoutManager(RecyclerViewHelper.getVerticalLinearLayoutManager(getActivity()));
        jokeList.addItemDecoration(new GreySpaceItemDerocation());
        jokeList.setItemAnimator(new SlideInUpAnimator());
        /* Loadmore */
        jokeList.setLoadMoreListener(() -> {
            smoothProgressBar.setVisibility(View.VISIBLE);
            mPage ++;
            getData();
        });

        /* Set Adapter */
        adapter = new JokeAdapter();
        jokeList.setAdapter(adapter);
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
        parser.getJokeData(mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    mJokePostArrayList.addAll(data);
                    adapter.notifyDataSetChanged();
                }, throwable -> throwable.printStackTrace());
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
}
