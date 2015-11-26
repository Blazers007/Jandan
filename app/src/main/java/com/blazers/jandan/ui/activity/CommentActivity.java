package com.blazers.jandan.ui.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.models.pojo.comment.CommentPost;
import com.blazers.jandan.models.pojo.comment.Comments;
import com.blazers.jandan.network.Parser;
import com.blazers.jandan.rxbus.Rxbus;
import com.blazers.jandan.rxbus.event.CommentEvent;
import com.blazers.jandan.ui.activity.base.BaseActivity;
import com.blazers.jandan.util.RecyclerViewHelper;
import com.blazers.jandan.views.QuoteView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.ArrayList;

/**
 * Created by Blazers on 2015/11/24.
 */
public class CommentActivity extends BaseActivity {

    public static final String TAG = CommentActivity.class.getSimpleName();

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.nothing_hint) LinearLayout hint;
    @Bind(R.id.comment_swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.comment_recycler_view) RecyclerView commentRecyclerView;
    @Bind(R.id.progress_wheel) CircularProgressBar progressWheel;

    //
    private long commentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        initToolbarByTypeWithShadow(null, toolbar, ToolbarType.FINISH);
        toolbar.setNavigationOnClickListener(v -> finish());
        hint.setVisibility(View.GONE);
        commentId = getIntent().getLongExtra("commentId", -1);
        swipeRefreshLayout.setOnRefreshListener(()->loadCommentById(commentId));
        loadCommentById(commentId);
    }

    void loadCommentById(long id) {
        Parser.getInstance().getCommentById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::parseAndShowComments,
                throwable -> {
                    progressWheel.animate().alpha(0).translationY(-96).setStartDelay(200).setDuration(300).start();
                    hint.setVisibility(View.VISIBLE);
                    Log.e("[Comments]", throwable.toString());
                }, ()->{
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.postDelayed(()->swipeRefreshLayout.setRefreshing(false), 300);
                    }
                }
            );
    }

    void parseAndShowComments(Comments comments) {
        progressWheel.animate().alpha(0).translationY(-96).setStartDelay(200).setDuration(300).start();
        if (comments.response.size() > 0) {
            commentRecyclerView.setLayoutManager(RecyclerViewHelper.getVerticalLinearLayoutManager(this));
            JandanCommentAdapter adapter = new JandanCommentAdapter(LayoutInflater.from(this), comments);
            commentRecyclerView.setAdapter(adapter);
        } else {
            hint.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Adapter
     * */
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
                CommentPost post = gson.fromJson(comments.parentPosts.get(hotKey), CommentPost.class);
                post._type = CommentPost.POST;
                commentPosts.add(post);
            }
            if (commentPosts.size() > 0){
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
                CommentPost post = gson.fromJson(comments.parentPosts.get(normalKey), CommentPost.class);
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
                    DividerHolder hot = (DividerHolder)h;
                    hot.icon.setImageResource(R.drawable.ic_hot_24dp);
                    hot.text.setText(R.string.hot);
                    hot.text.setTextColor(Color.parseColor("#ef4a4f"));
                    break;
                case CommentPost.NORMAL_DIVIDER:
                    DividerHolder not = (DividerHolder)h;
                    not.icon.setImageResource(R.drawable.ic_not_hot_24dp);
                    not.text.setText(R.string.not_hot);
                    not.text.setTextColor(Color.parseColor("#1d6FC5"));
                    break;
                case CommentPost.POST:
                    CommentHolder holder = (CommentHolder)h;
                    holder.userName.setText(String.format("@%s", post.author.name));
                    holder.commentDate.setText(post.created_at);
                    holder.message.setText(post.message);
                    if (post.author.avatar_url == null)
                        holder.userHead.setImageURI(Uri.parse("http://tp4.sinaimg.cn/3195783623/50/0/1")); //测试
                    else
                        holder.userHead.setImageURI(Uri.parse(post.author.avatar_url));
                    if (post.parents.size() > 0) {
                        holder.quoteView.setVisibility(View.VISIBLE);
                        holder.quoteView.setUpQuoteLink(gson, comments, post.parents, post.parents.size()-1);
                    } else {
                        holder.quoteView.setVisibility(View.GONE);
                    }
                    break;
            }
        }

        /**
         * 评论Holder
         * */
        class CommentHolder extends RecyclerView.ViewHolder{
            @Bind(R.id.user_name)
            TextView userName;
            @Bind(R.id.user_head)
            SimpleDraweeView userHead;
            @Bind(R.id.comment_date) TextView commentDate;
            @Bind(R.id.message) TextView message;
            @Bind(R.id.quote)
            QuoteView quoteView;

            public CommentHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        /**
         * 分隔线Holder
         * */
        class DividerHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.divider_icon)
            ImageView icon;
            @Bind(R.id.divider_text) TextView text;
            public DividerHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
