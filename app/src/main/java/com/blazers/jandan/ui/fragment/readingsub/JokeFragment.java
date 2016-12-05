package com.blazers.jandan.ui.fragment.readingsub;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blazers.jandan.BR;
import com.blazers.jandan.R;
import com.blazers.jandan.model.DataManager;
import com.blazers.jandan.model.joke.JokePage;
import com.blazers.jandan.model.news.NewsPage;
import com.blazers.jandan.presenter.JokePresenter;
import com.blazers.jandan.ui.adapter.BaseSingleMVVMAdapter;
import com.blazers.jandan.util.Rxbus;
import com.blazers.jandan.model.event.ViewCommentEvent;
import com.blazers.jandan.ui.fragment.base.BaseSwipeLoadMoreFragment;
import com.blazers.jandan.util.NetworkHelper;
import com.blazers.jandan.util.RxHelper;
import com.blazers.jandan.util.ShareHelper;
import com.blazers.jandan.widgets.ThumbTextButton;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.util.ArrayList;
import java.util.List;

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
public class JokeFragment extends BaseSwipeLoadMoreFragment<JokePresenter> implements JokeView {

    // private
    private BaseSingleMVVMAdapter mAdapter;
    private List<JokePage.Comments> mList = new ArrayList<>();

    @Override
    protected void initPresenter() {
        mPresenter = new JokePresenter(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_common_refresh_load;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    void initRecyclerView() {
        trySetupSwipeRefreshLayout();
        trySetupRecyclerViewWithAdapter(mAdapter = new BaseSingleMVVMAdapter<>(
                LayoutInflater.from(getActivity()),
                R.layout.item_jandan_joke,
                mList,
                mPresenter,
                BR.jBean,
                BR.jPresenter
        ));
        // Try to load from db
        mPresenter.onInitPageData();
    }

    @Override
    public void refreshDataList(List<JokePage.Comments> postsBeanList) {
        mList.clear();
        mList.addAll(postsBeanList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void addDataList(List<JokePage.Comments> postsBeanList) {
        // 是否区分重复元素？ 能否在这区分重复元素  --> 索性不考虑
        int start = mList.size();
        int size = postsBeanList.size();
        mList.addAll(postsBeanList);
        mAdapter.notifyItemRangeInserted(start, size);
        mRecyclerView.smoothScrollBy(0, 96);
    }


//    class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.JokeHolder> {
//        private LayoutInflater inflater;
//
//        public JokeAdapter() {
//            inflater = LayoutInflater.from(getActivity());
//        }
//
//        @Override
//        public JokeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View v = inflater.inflate(R.layout.item_jandan_joke, parent, false);
//            return new JokeHolder(v);
//        }
//
//        @Override
//        public void onBindViewHolder(JokeHolder holder, int position) {
////            JokePost joke = mList.get(position);
////            holder.content.setText(joke.getComment_content());
////            holder.author.setText(String.format("@%s", joke.getComment_author()));
////            holder.date.setText(TimeHelper.getSocialTime(joke.getComment_date()));
////            holder.oo.setThumbText(joke.getVote_positive());
////            holder.xx.setThumbText(joke.getVote_negative());
////            holder.comment.setThumbText(String.format("%d", joke.getCommentNumber()));
////            holder.fav.setFavorite(LocalFavJokes.isThisFaved(realm, joke.getComment_ID()), false);
////            //TODO 优化数据库查询 或者缓存
////            LocalVote vote = LocalVote.getLocalVoteById(realm, joke.getComment_ID());
////            if (vote != null) {
////                if (vote.getId() > 0) {
////                    holder.oo.setPressed(true);
////                    holder.xx.setPressed(false);
////                } else if (vote.getId() < 0) {
////                    holder.oo.setPressed(false);
////                    holder.xx.setPressed(true);
////                }
////            } else {
////                holder.oo.setPressed(false);
////                holder.xx.setPressed(false);
////            }
//        }
//
//        @Override
//        public int getItemCount() {
//            return mList.size();
//        }
//
//        class JokeHolder extends RecyclerView.ViewHolder {
//
//            @BindView(R.id.content)
//            TextView content;
//            @BindView(R.id.author)
//            TextView author;
//            @BindView(R.id.date)
//            TextView date;
//            @BindView(R.id.btn_oo)
//            ThumbTextButton oo;
//            @BindView(R.id.btn_xx)
//            ThumbTextButton xx;
//            @BindView(R.id.btn_comment)
//            ThumbTextButton comment;
//            @BindView(R.id.btn_fav)
//            MaterialFavoriteButton fav;
//
//            public JokeHolder(View itemView) {
//                super(itemView);
//                ButterKnife.bind(this, itemView);
////                fav.setOnFavoriteChangeListener(
////                        (view, favorite) -> LocalFavJokes.setThisFavedOrNot(favorite, realm, mList.get(getAdapterPosition()).getComment_ID())
////                );
//            }
//
//            /**
//             * 投票
//             */
//            @OnClick({R.id.btn_oo, R.id.btn_xx})
//            public void vote(View view) {
//                OldJokePost post = mList.get(getAdapterPosition());
//                /* 查看是否已经投票 */
////                LocalVote vote = realm.where(LocalVote.class).equalTo("id", post.getComment_ID()).findFirst();
////                if (vote != null && vote.getId() != 0) {
////                    Toast.makeText(getActivity(), R.string.warn_already_vote, Toast.LENGTH_SHORT).show();
////                    return;
////                }
//                switch (view.getId()) {
//                    case R.id.btn_oo:
//                        DataManager.getInstance().voteByCommentIdAndVote(post.getComment_ID(), true)
//                                .compose(RxHelper.applySchedulers())
//                                .subscribe(s -> {
//                                    oo.addThumbText(1);
//                                    LocalVote v = new LocalVote();
//                                    v.setId(post.getComment_ID());
//                                    v.setVote(1);
////                                    DBHelper.saveToRealm(realm, v);
//                                }, throwable -> Log.e("Vote", throwable.toString()));
//                        break;
//                    case R.id.btn_xx:
//                        DataManager.getInstance().voteByCommentIdAndVote(post.getComment_ID(), true)
//                                .compose(RxHelper.applySchedulers())
//                                .subscribe(s -> {
//                                    xx.addThumbText(1);
//                                    LocalVote v = new LocalVote();
//                                    v.setId(post.getComment_ID());
//                                    v.setVote(-1);
////                                    DBHelper.saveToRealm(realm, v);
//                                }, throwable -> Log.e("Vote", throwable.toString()));
//                        break;
//                }
//            }
//
//            /**
//             * 评论
//             */
//            @OnClick(R.id.btn_comment)
//            public void showComment() {
//                Rxbus.getInstance().send(new ViewCommentEvent(mList.get(getAdapterPosition()).getComment_ID()));
//            }
//
//            /**
//             * 分享
//             */
//            @OnClick(R.id.btn_share)
//            public void share() {
//                ShareHelper.shareText(getActivity(), "新鲜事", mList.get(getAdapterPosition()).getComment_content());
//            }
//        }
//    }
//
////    @Override
////    public void onDetach() {
////        super.onDetach();
////        /*
////        * http://stackoverflow.com/questions/15207305/getting-the-error-java-lang-illegalstateexception-activity-has-been-destroyed
////        * */
////        try {
////            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
////            childFragmentManager.setAccessible(true);
////            childFragmentManager.set(this, null);
////        } catch (NoSuchFieldException e) {
////            throw new RuntimeException(e);
////        } catch (IllegalAccessException e) {
////            throw new RuntimeException(e);
////        }
////    }
}
