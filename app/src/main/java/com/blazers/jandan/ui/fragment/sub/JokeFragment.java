package com.blazers.jandan.ui.fragment.sub;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.blazers.jandan.R;
import com.blazers.jandan.models.db.local.LocalVote;
import com.blazers.jandan.models.db.sync.JokePost;
import com.blazers.jandan.network.Parser;
import com.blazers.jandan.rxbus.Rxbus;
import com.blazers.jandan.rxbus.event.CommentEvent;
import com.blazers.jandan.ui.fragment.base.BaseSwipeLoadMoreFragment;
import com.blazers.jandan.util.DBHelper;
import com.blazers.jandan.util.NetworkHelper;
import com.blazers.jandan.util.RxHelper;
import com.blazers.jandan.util.TimeHelper;
import com.blazers.jandan.views.ThumbTextButton;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blazers on 15/9/1.
 *
 * 段子页面
 */
@SuppressWarnings("unused")
public class JokeFragment extends BaseSwipeLoadMoreFragment{

    public static final String TAG = JokeFragment.class.getSimpleName();
    // private
    private JokeAdapter adapter;
    private ArrayList<JokePost> mJokePostArrayList = new ArrayList<>();
    private int mPage = 1;

    public JokeFragment() {
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
        trySetupRecyclerViewWithAdapter(adapter = new JokeAdapter());
        // 加载数据
        List<JokePost> localImageList = JokePost.getAllPost(realm, 1);
        mJokePostArrayList.addAll(localImageList);
        adapter.notifyItemRangeInserted(0, localImageList.size());
        // 如果数据为空 或 时间大于30分钟 则更新
        if (localImageList.size() == 0
            || TimeHelper.getThatTimeOffsetByNow(localImageList.get(0).getComment_date()) > 30 * TimeHelper.ONE_MIN) {
            swipeRefreshLayout.post(()->swipeRefreshLayout.setRefreshing(true));
            refresh();
        }
    }

    @Override
    public void refresh() {
        mPage = 1;
        if (NetworkHelper.netWorkAvailable(getActivity())) {
            Parser parser = Parser.getInstance();
            parser.getJokeData(mPage)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(list -> DBHelper.saveToRealm(realm, list))
                .subscribe(list -> {
                    refreshComplete();
                    //
                    mJokePostArrayList.clear();
                    adapter.notifyDataSetChanged();
                    // 插入数据
                    mJokePostArrayList.addAll(list);
                    adapter.notifyItemRangeInserted(0, list.size());
                }, throwable -> {
                    refreshError();
                    Log.e("Joke", throwable.toString());
                });
        } else {
            List<JokePost> list = JokePost.getAllPost(realm, mPage);
            if (null != list && list.size() > 0) {
                // 清空
                mJokePostArrayList.clear();
                adapter.notifyDataSetChanged();
                // 添加
                mJokePostArrayList.addAll(list);
                adapter.notifyItemRangeInserted(0, list.size());
            } else {
                Toast.makeText(getActivity(), R.string.there_is_no_more, Toast.LENGTH_SHORT).show();
            }
            refreshComplete();
        }
    }

    @Override
    public void loadMore() {
        if (swipeRefreshLayout.isRefreshing()) {
            Log.i(TAG, "正在刷新中,所以无法加载更多");
            return;
        }
        mPage ++;
        /* 判断网络状态 */
        if (NetworkHelper.netWorkAvailable(getActivity())) {
            smoothProgressBar.setVisibility(View.VISIBLE);
            Parser parser = Parser.getInstance();
            parser.getJokeData(mPage)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(list -> DBHelper.saveToRealm(realm, list))
                .subscribe(list -> {
                    loadMoreComplete();
                    // 插入数据
                    int start = mJokePostArrayList.size();
                    mJokePostArrayList.addAll(list);
//                    adapter.notifyItemRangeInserted(start, list.size());
                    adapter.notifyDataSetChanged();
                }, throwable -> {
                    loadMoreError();
                    Log.e("Joke", throwable.toString());
                });
        } else {
            // 尝试从本地数据库读取
            List<JokePost> list = JokePost.getAllPost(realm, mPage);
            if (null != list && list.size() > 0) {
                int start = mJokePostArrayList.size();
                mJokePostArrayList.addAll(list);
//                adapter.notifyItemRangeInserted(start, list.size());
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), R.string.there_is_no_more, Toast.LENGTH_SHORT).show();
            }
            loadMoreComplete();
        }
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
            holder.author.setText(String.format("@%s", joke.getComment_author()));
            holder.date.setText(TimeHelper.getSocialTime(joke.getComment_date()));
            holder.oo.setThumbText(joke.getVote_positive());
            holder.xx.setThumbText(joke.getVote_negative());
            holder.comment.setThumbText(String.format("%d", joke.getCommentNumber()));
            //TODO 优化数据库查询 或者缓存
            LocalVote vote = realm.where(LocalVote.class).equalTo("id", joke.getComment_ID()).findFirst();
            if (vote != null){
                if (vote.getId() > 0) {
                    holder.oo.setPressed(true);
                    holder.xx.setPressed(false);
                }else if (vote.getId() < 0) {
                    holder.oo.setPressed(false);
                    holder.xx.setPressed(true);
                }
            } else {
                holder.oo.setPressed(false);
                holder.xx.setPressed(false);
            }
        }

        @Override
        public int getItemCount() {
            return mJokePostArrayList.size();
        }

        class JokeHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.content) TextView content;
            @Bind(R.id.author) TextView author;
            @Bind(R.id.date) TextView date;
            @Bind(R.id.btn_oo) ThumbTextButton oo;
            @Bind(R.id.btn_xx) ThumbTextButton xx;
            @Bind(R.id.btn_comment) ThumbTextButton comment;

            public JokeHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            @OnClick({R.id.btn_oo, R.id.btn_xx})
            public void vote(View view) {
                JokePost post = mJokePostArrayList.get(getAdapterPosition());
                /* 查看是否已经投票 */
                LocalVote vote = realm.where(LocalVote.class).equalTo("id", post.getComment_ID()).findFirst();
                if (vote != null && vote.getId() != 0){
                    Toast.makeText(getActivity(), R.string.warn_already_vote, Toast.LENGTH_SHORT).show();
                    return;
                }
                switch (view.getId()) {
                    case R.id.btn_oo:
                        Parser.getInstance().voteByCommentIdAndVote(post.getComment_ID(), true)
                            .compose(RxHelper.applySchedulers())
                            .subscribe(s->{
                                oo.addThumbText(1);
                                LocalVote v = new LocalVote();
                                v.setId(post.getComment_ID());
                                v.setVote(1);
                                DBHelper.saveToRealm(realm, v);
                            },throwable -> Log.e("Vote", throwable.toString()));
                        break;
                    case R.id.btn_xx:
                        Parser.getInstance().voteByCommentIdAndVote(post.getComment_ID(), true)
                            .compose(RxHelper.applySchedulers())
                            .subscribe(s -> {
                                xx.addThumbText(1);
                                LocalVote v = new LocalVote();
                                v.setId(post.getComment_ID());
                                v.setVote(-1);
                                DBHelper.saveToRealm(realm, v);
                            }, throwable -> Log.e("Vote", throwable.toString()));
                        break;
                }
            }

            @OnClick(R.id.btn_comment)
            public void showComment(){
                Rxbus.getInstance().send(new CommentEvent(mJokePostArrayList.get(getAdapterPosition()).getComment_ID()));
            }
        }
    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        /*
//        * http://stackoverflow.com/questions/15207305/getting-the-error-java-lang-illegalstateexception-activity-has-been-destroyed
//        * */
//        try {
//            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
//            childFragmentManager.setAccessible(true);
//            childFragmentManager.set(this, null);
//        } catch (NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
