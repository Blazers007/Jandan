package com.blazers.jandan.ui.fragment.readingsub;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blazers.jandan.R;
import com.blazers.jandan.model.database.local.LocalVote;
import com.blazers.jandan.model.database.sync.JokePost;
import com.blazers.jandan.api.DataManager;
import com.blazers.jandan.util.Rxbus;
import com.blazers.jandan.model.event.ViewCommentEvent;
import com.blazers.jandan.ui.fragment.base.BaseSwipeLoadMoreFragment;
import com.blazers.jandan.util.NetworkHelper;
import com.blazers.jandan.util.RxHelper;
import com.blazers.jandan.util.ShareHelper;
import com.blazers.jandan.widgets.ThumbTextButton;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Blazers on 15/9/1.
 * <p>
 * 段子页面
 */
@SuppressWarnings("unused")
public class JokeFragment extends BaseSwipeLoadMoreFragment {

    public static final String TAG = JokeFragment.class.getSimpleName();
    // private
    private JokeAdapter adapter;
    private ArrayList<JokePost> mList = new ArrayList<>();
    private int mPage = 1;

    public JokeFragment() {
        super();
        setTAG(TAG);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutResId() {
        return 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_common_refresh_load, container, false);
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
//        List<JokePost> localList = JokePost.getAllPost(realm, 1);
//        mList.addAll(localList);
//        adapter.notifyItemRangeInserted(0, localList.size());
        // 如果数据为空 或 时间大于30分钟 则更新
//        if (localList.size() == 0 || TimeHelper.isTimeEnoughForRefreshing(SPHelper.getLastRefreshTime(getActivity(), type))) {
//            mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));
//            refresh();
//        }
    }

    @Override
    public void refresh() {
        mPage = 1;
//        if (NetworkHelper.netWorkAvailable(getActivity())) {
//            DataManager parser = DataManager.getInstance();
//            parser.getJokeData(mPage)
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .doOnNext(list -> DBHelper.saveToRealm(realm, list))
//                    .subscribe(list -> {
//                        refreshComplete();
//                        //
//                        mList.clear();
//                        adapter.notifyDataSetChanged();
//                        // 插入数据
//                        mList.addAll(list);
//                        adapter.notifyItemRangeInserted(0, list.size());
//                    }, throwable -> {
//                        refreshError();
//                        Log.e("Joke", throwable.toString());
//                    });
//        } else {
//            List<JokePost> list = JokePost.getAllPost(realm, mPage);
//            if (null != list && list.size() > 0) {
//                // 清空
//                mList.clear();
//                adapter.notifyDataSetChanged();
//                // 添加
//                mList.addAll(list);
//                adapter.notifyItemRangeInserted(0, list.size());
//            } else {
//                Toast.makeText(getActivity(), R.string.there_is_no_more, Toast.LENGTH_SHORT).show();
//            }
//            refreshComplete();
//        }
    }

    @Override
    public void loadMore() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            Log.i(TAG, "正在刷新中,所以无法加载更多");
            return;
        }
        mPage++;
        /* 判断网络状态 */
        if (NetworkHelper.netWorkAvailable(getActivity())) {
            mSmoothProgressBar.setVisibility(View.VISIBLE);
            DataManager parser = DataManager.getInstance();
            parser.getJokeData(mPage)
                    .observeOn(AndroidSchedulers.mainThread())
//                    .doOnNext(list -> DBHelper.saveToRealm(realm, list))
                    .subscribe(list -> {

                        int start = mList.size();
                        mList.addAll(list);
                        adapter.notifyDataSetChanged();
                    }, throwable -> {

                        Log.e("Joke", throwable.toString());
                    });
        } else {
            // 尝试从本地数据库读取
//            List<JokePost> list = JokePost.getAllPost(realm, mPage);
//            if (null != list && list.size() > 0) {
//                int start = mList.size();
//                mList.addAll(list);
//                adapter.notifyDataSetChanged();
//            } else {
//                Toast.makeText(getActivity(), R.string.there_is_no_more, Toast.LENGTH_SHORT).show();
//            }

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
//            JokePost joke = mList.get(position);
//            holder.content.setText(joke.getComment_content());
//            holder.author.setText(String.format("@%s", joke.getComment_author()));
//            holder.date.setText(TimeHelper.getSocialTime(joke.getComment_date()));
//            holder.oo.setThumbText(joke.getVote_positive());
//            holder.xx.setThumbText(joke.getVote_negative());
//            holder.comment.setThumbText(String.format("%d", joke.getCommentNumber()));
//            holder.fav.setFavorite(LocalFavJokes.isThisFaved(realm, joke.getComment_ID()), false);
//            //TODO 优化数据库查询 或者缓存
//            LocalVote vote = LocalVote.getLocalVoteById(realm, joke.getComment_ID());
//            if (vote != null) {
//                if (vote.getId() > 0) {
//                    holder.oo.setPressed(true);
//                    holder.xx.setPressed(false);
//                } else if (vote.getId() < 0) {
//                    holder.oo.setPressed(false);
//                    holder.xx.setPressed(true);
//                }
//            } else {
//                holder.oo.setPressed(false);
//                holder.xx.setPressed(false);
//            }
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class JokeHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.content)
            TextView content;
            @BindView(R.id.author)
            TextView author;
            @BindView(R.id.date)
            TextView date;
            @BindView(R.id.btn_oo)
            ThumbTextButton oo;
            @BindView(R.id.btn_xx)
            ThumbTextButton xx;
            @BindView(R.id.btn_comment)
            ThumbTextButton comment;
            @BindView(R.id.btn_fav)
            MaterialFavoriteButton fav;

            public JokeHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
//                fav.setOnFavoriteChangeListener(
//                        (view, favorite) -> LocalFavJokes.setThisFavedOrNot(favorite, realm, mList.get(getAdapterPosition()).getComment_ID())
//                );
            }

            /**
             * 投票
             */
            @OnClick({R.id.btn_oo, R.id.btn_xx})
            public void vote(View view) {
                JokePost post = mList.get(getAdapterPosition());
                /* 查看是否已经投票 */
//                LocalVote vote = realm.where(LocalVote.class).equalTo("id", post.getComment_ID()).findFirst();
//                if (vote != null && vote.getId() != 0) {
//                    Toast.makeText(getActivity(), R.string.warn_already_vote, Toast.LENGTH_SHORT).show();
//                    return;
//                }
                switch (view.getId()) {
                    case R.id.btn_oo:
                        DataManager.getInstance().voteByCommentIdAndVote(post.getComment_ID(), true)
                                .compose(RxHelper.applySchedulers())
                                .subscribe(s -> {
                                    oo.addThumbText(1);
                                    LocalVote v = new LocalVote();
                                    v.setId(post.getComment_ID());
                                    v.setVote(1);
//                                    DBHelper.saveToRealm(realm, v);
                                }, throwable -> Log.e("Vote", throwable.toString()));
                        break;
                    case R.id.btn_xx:
                        DataManager.getInstance().voteByCommentIdAndVote(post.getComment_ID(), true)
                                .compose(RxHelper.applySchedulers())
                                .subscribe(s -> {
                                    xx.addThumbText(1);
                                    LocalVote v = new LocalVote();
                                    v.setId(post.getComment_ID());
                                    v.setVote(-1);
//                                    DBHelper.saveToRealm(realm, v);
                                }, throwable -> Log.e("Vote", throwable.toString()));
                        break;
                }
            }

            /**
             * 评论
             */
            @OnClick(R.id.btn_comment)
            public void showComment() {
                Rxbus.getInstance().send(new ViewCommentEvent(mList.get(getAdapterPosition()).getComment_ID()));
            }

            /**
             * 分享
             */
            @OnClick(R.id.btn_share)
            public void share() {
                ShareHelper.shareText(getActivity(), "新鲜事", mList.get(getAdapterPosition()).getComment_content());
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
