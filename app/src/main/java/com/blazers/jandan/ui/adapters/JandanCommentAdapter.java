package com.blazers.jandan.ui.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.models.jandan.comment.CommentPost;
import com.blazers.jandan.models.jandan.comment.Comments;
import com.blazers.jandan.views.QuoteView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Blazers on 2015/10/13.
 */
public class JandanCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private Comments comments;
    // hold here
    private ArrayList<CommentPost> commentPosts;
    private Gson gson;

    public JandanCommentAdapter(LayoutInflater inflater, Comments comments) {
        this.inflater = inflater;
        this.comments = comments;
        //
        gson = new Gson();
        commentPosts = new ArrayList<>();
        // 热门
        for (String hotKey : comments.hotPosts) {
            CommentPost post = gson.fromJson(comments.parentPosts.get(hotKey).toString(), CommentPost.class);
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
                break;
            case CommentPost.NORMAL_DIVIDER:
                break;
            case CommentPost.POST:
                CommentHolder holder = (CommentHolder)h;
                holder.userName.setText("@"+post.author.name);
                holder.commentDate.setText(post.created_at);
                holder.message.setText(post.message);
                if (post.author.avatar_url == null)
                    holder.userHead.setImageURI(Uri.parse("http://tp4.sinaimg.cn/3195783623/50/0/1")); //测试
                else
                    holder.userHead.setImageURI(Uri.parse(post.author.avatar_url));
                if (post.parents.size() > 0) {
                    holder.quoteView.setVisibility(View.VISIBLE);
                    holder.quoteView.setUpQuoteLink(gson, comments, post.parents, 0);
                } else {
                    holder.quoteView.setVisibility(View.GONE);
                }
                break;
        }
    }


    class CommentHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.user_name) TextView userName;
        @Bind(R.id.user_head) SimpleDraweeView userHead;
        @Bind(R.id.comment_date) TextView commentDate;
        @Bind(R.id.message) TextView message;
        @Bind(R.id.quote) QuoteView quoteView;

        public CommentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class DividerHolder extends RecyclerView.ViewHolder {

        public DividerHolder(View itemView) {
            super(itemView);
        }
    }

}
