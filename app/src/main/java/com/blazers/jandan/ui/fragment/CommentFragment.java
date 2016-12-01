package com.blazers.jandan.ui.fragment;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blazers.jandan.R;
import com.blazers.jandan.model.DataManager;
import com.blazers.jandan.model.event.ViewCommentEvent;
import com.blazers.jandan.model.pojo.comment.CommentPost;
import com.blazers.jandan.model.pojo.comment.Comments;
import com.blazers.jandan.ui.fragment.base.BaseFragment;
import com.blazers.jandan.util.RecyclerViewHelper;
import com.blazers.jandan.util.Rxbus;
import com.blazers.jandan.widgets.QuoteView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Blazers on 2015/10/13.
 * <p>
 * 查看评论列表的Fragment
 */
public class CommentFragment extends BaseFragment {

    public static final String TAG = CommentFragment.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nothing_hint)
    LinearLayout hint;
    @BindView(R.id.comment_recycler_view)
    RecyclerView commentRecyclerView;
    @BindView(R.id.progress_wheel)
    MaterialProgressBar progressWheel;

    public static CommentFragment NewInstance(long commentId) {
        CommentFragment fragment = new CommentFragment();
        Bundle data = new Bundle();
        data.putLong("commentId", commentId);
        fragment.setArguments(data);
        fragment.setTAG(TAG);
        return fragment;
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
        if (getArguments() == null || getArguments().getLong("commentId", -1) == -1) {
            // 关闭
            return null;
        }
        View root = inflater.inflate(R.layout.fragment_comments, container, false);
        ButterKnife.bind(this, root);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("评论");
        toolbar.setNavigationOnClickListener(v -> Rxbus.getInstance().send(new ViewCommentEvent(-1)));

        hint.setVisibility(View.GONE);

        loadCommentById(getArguments().getLong("commentId", -1));
        return root;
    }

    void loadCommentById(long id) {
        DataManager.getInstance().getCommentById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::parseAndShowComments,
                        throwable -> {
                            progressWheel.animate().alpha(0).translationY(-96).setStartDelay(200).setDuration(300).start();
                            hint.setVisibility(View.VISIBLE);
                            Log.e("[Comments]", throwable.toString());
                        }
                );
    }

    void parseAndShowComments(Comments comments) {
        progressWheel.animate().alpha(0).translationY(-96).setStartDelay(200).setDuration(300).start();
        if (comments.response.size() > 0) {
            commentRecyclerView.setLayoutManager(RecyclerViewHelper.getVerticalLinearLayoutManager(getActivity()));
            JandanCommentAdapter adapter = new JandanCommentAdapter(LayoutInflater.from(getActivity()), comments);
            commentRecyclerView.setAdapter(adapter);
        } else {
            hint.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Adapter
     */
    class JandanCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private LayoutInflater inflater;
        private Comments comments;
        // hold here
        private ArrayList<CommentPost> commentPosts;
        private Gson gson;

        public JandanCommentAdapter(LayoutInflater inflater, Comments comments) {
            this.inflater = inflater;
            this.comments = comments;
            gson = new Gson();
            commentPosts = new ArrayList<>();
            // 热门
            for (String hotKey : comments.hotPosts) {
                CommentPost post = gson.fromJson(comments.parentPosts.get(hotKey).toString(), CommentPost.class);
                post._type = CommentPost.POST;
                commentPosts.add(post);
            }
            if (commentPosts.size() > 0) {
                CommentPost hotDivider = new CommentPost();
                hotDivider._type = CommentPost.HOT_DIVIDER;
                hotDivider._dividerName = "热门评论";
                commentPosts.add(0, hotDivider);
            }
            CommentPost divider = new CommentPost();
            divider._type = CommentPost.NORMAL_DIVIDER;
            divider._dividerName = "评论";
            commentPosts.add(divider);
            // 普通
            for (String normalKey : comments.response) {
                CommentPost post = gson.fromJson(comments.parentPosts.get(normalKey).toString(), CommentPost.class);
                post._type = CommentPost.POST;
                commentPosts.add(post);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return commentPosts.get(position)._type;
        }

        @Override
        public int getItemCount() {
            return commentPosts.size();
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case CommentPost.HOT_DIVIDER:
                case CommentPost.NORMAL_DIVIDER:
                    return new DividerHolder(inflater.inflate(R.layout.item_comment_divider, parent, false));
                case CommentPost.POST:
                    return new CommentHolder(inflater.inflate(R.layout.item_comment, parent, false));
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
            CommentPost post = commentPosts.get(position);
            switch (getItemViewType(position)) {
                case CommentPost.HOT_DIVIDER:
                    DividerHolder hot = (DividerHolder) h;
                    hot.icon.setImageResource(R.drawable.ic_hot_24dp);
                    hot.text.setText(R.string.hot);
                    hot.text.setTextColor(Color.parseColor("#ef4a4f"));
                    break;
                case CommentPost.NORMAL_DIVIDER:
                    DividerHolder not = (DividerHolder) h;
                    not.icon.setImageResource(R.drawable.ic_not_hot_24dp);
                    not.text.setText(R.string.not_hot);
                    not.text.setTextColor(Color.parseColor("#1d6FC5"));
                    break;
                case CommentPost.POST:
                    CommentHolder holder = (CommentHolder) h;
                    holder.userName.setText(String.format("@%s", post.author.name));
                    holder.commentDate.setText(post.created_at);
                    holder.message.setText(post.message);
                    if (post.author.avatar_url == null)
                        holder.userHead.setImageURI(Uri.parse("http://tp4.sinaimg.cn/3195783623/50/0/1")); //测试
                    else
                        holder.userHead.setImageURI(Uri.parse(post.author.avatar_url));
                    if (post.parents.size() > 0) {
                        holder.quoteView.setVisibility(View.VISIBLE);
                        holder.quoteView.setUpQuoteLink(gson, comments, post.parents, post.parents.size() - 1);
                    } else {
                        holder.quoteView.setVisibility(View.GONE);
                    }
                    break;
            }
        }

        /**
         * 评论Holder
         */
        class CommentHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.user_name)
            TextView userName;
            @BindView(R.id.user_head)
            SimpleDraweeView userHead;
            @BindView(R.id.comment_date)
            TextView commentDate;
            @BindView(R.id.message)
            TextView message;
            @BindView(R.id.quote)
            QuoteView quoteView;

            public CommentHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        /**
         * 分隔线Holder
         */
        class DividerHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.divider_icon)
            ImageView icon;
            @BindView(R.id.divider_text)
            TextView text;

            public DividerHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
