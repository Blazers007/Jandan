package com.blazers.jandan.views;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.models.jandan.comment.CommentPost;
import com.blazers.jandan.models.jandan.comment.Comments;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Blazers on 2015/10/13.
 */
public class QuoteView extends RelativeLayout {

    @Bind(R.id.user_head) SimpleDraweeView userHead;
    @Bind(R.id.user_name) TextView userName;
    @Bind(R.id.message) TextView message;
    @Bind(R.id.parent_quote) QuoteView quoteView;

    public QuoteView(Context context) {
        super(context);
    }

    public QuoteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QuoteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setUpQuoteLink(Gson gson, Comments comments, List<String> quotes, int index) {
        if (index > quotes.size() - 1) {
            ((ViewGroup)getParent()).removeView(this);
            return;
        }
        View v = LayoutInflater.from(getContext()).inflate(R.layout.view_quote, this, true);
        ButterKnife.bind(this, v);

        if (index == 0)
            setBackgroundResource(R.drawable.quote_background_with_solid);

        CommentPost quote = gson.fromJson(comments.parentPosts.get(quotes.get(index)), CommentPost.class);
        if (quote.author.avatar_url == null)
            userHead.setImageURI(Uri.parse("http://tp4.sinaimg.cn/3195783623/50/0/1")); //测试
        else
            userHead.setImageURI(Uri.parse(quote.author.avatar_url));
        userName.setText(quote.author.name);
        message.setText(quote.message);
        // 传给下一层判断
        quoteView.setUpQuoteLink(gson, comments, quotes, ++index);
    }
}
