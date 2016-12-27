package com.blazers.jandan.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.blazers.jandan.R;
import com.blazers.jandan.ui.activity.base.BaseActivity;

import butterknife.BindView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by Blazers on 2015/11/24.
 * 评论页面
 */
public class CommentActivity extends BaseActivity {

    public static final String TAG = CommentActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nothing_hint)
    LinearLayout hint;
    @BindView(R.id.comment_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.comment_recycler_view)
    RecyclerView commentRecyclerView;
    @BindView(R.id.progress_wheel)
    SmoothProgressBar progressWheel;

    // 此片评论的ID
    private long commentId;


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_comment);
//        initToolbarByTypeWithShadow(null, toolbar, ToolbarType.FINISH);
//        // 设置下拉刷新
//        swipeRefreshLayout.setOnRefreshListener(() -> loadCommentById(commentId));
//        // 获取数据
//        loadCommentById(commentId = ((ViewCommentEvent) getIntent().getSerializableExtra(ViewCommentEvent.KEY)).id);
//    }

//    void loadCommentById(long id) {
//        DataManager.getInstance().getCommentById(id)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        this::parseAndShowComments,
//                        throwable -> {
//                            progressWheel.animate().alpha(0).translationY(-96).setStartDelay(200).setDuration(300).start();
//                            hint.setVisibility(View.VISIBLE);
//                            Log.e("[Comments]", throwable.toString());
//                        }, () -> {
//                            if (swipeRefreshLayout.isRefreshing()) {
//                                swipeRefreshLayout.postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 300);
//                            }
//                        }
//                );
//    }
//
//    void parseAndShowComments(Comments comments) {
//        progressWheel.animate().alpha(0).translationY(-96).setStartDelay(200).setDuration(300).start();
//        if (comments.response.size() > 0) {
//            commentRecyclerView.setLayoutManager(RecyclerViewHelper.getVerticalLinearLayoutManager(this));
//            JandanCommentAdapter adapter = new JandanCommentAdapter(LayoutInflater.from(this), comments);
//            commentRecyclerView.setAdapter(adapter);
//        } else {
//            hint.setVisibility(View.VISIBLE);
//        }
//    }
//
//
//    /**
//     * Adapter
//     */
//    class JandanCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//        private LayoutInflater inflater;
//        private Comments comments;
//        // hold here
//        private ArrayList<CommentPost> commentPosts;
//        private Gson gson;
//
//        public JandanCommentAdapter(LayoutInflater inflater, Comments comments) {
//            this.inflater = inflater;
//            this.comments = comments;
//            gson = new Gson();
//            commentPosts = new ArrayList<>();
//            // 热门
//            for (String hotKey : comments.hotPosts) {
//                CommentPost post = gson.fromJson(comments.parentPosts.get(hotKey), CommentPost.class);
//                post._type = CommentPost.POST;
//                commentPosts.add(post);
//            }
//            if (commentPosts.size() > 0) {
//                CommentPost hotDivider = new CommentPost();
//                hotDivider._type = CommentPost.HOT_DIVIDER;
//                hotDivider._dividerName = "热门评论";
//                commentPosts.add(0, hotDivider);
//            }
//            CommentPost divider = new CommentPost();
//            divider._type = CommentPost.NORMAL_DIVIDER;
//            divider._dividerName = "评论";
//            commentPosts.add(divider);
//            // 普通
//            for (String normalKey : comments.response) {
//                CommentPost post = gson.fromJson(comments.parentPosts.get(normalKey), CommentPost.class);
//                post._type = CommentPost.POST;
//                commentPosts.add(post);
//            }
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            return commentPosts.get(position)._type;
//        }
//
//        @Override
//        public int getItemCount() {
//            return commentPosts.size();
//        }
//
//
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            switch (viewType) {
//                case CommentPost.HOT_DIVIDER:
//                case CommentPost.NORMAL_DIVIDER:
//                    return new DividerHolder(inflater.inflate(R.layout.item_comment_divider, parent, false));
//                case CommentPost.POST:
//                    return new CommentHolder(inflater.inflate(R.layout.item_comment, parent, false));
//            }
//            return null;
//        }
//
//        @Override
//        public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
//            CommentPost post = commentPosts.get(position);
//            switch (getItemViewType(position)) {
//                case CommentPost.HOT_DIVIDER:
//                    DividerHolder hot = (DividerHolder) h;
//                    hot.icon.setImageResource(R.drawable.ic_hot_24dp);
//                    hot.text.setText(R.string.hot);
//                    hot.text.setTextColor(Color.parseColor("#ef4a4f"));
//                    break;
//                case CommentPost.NORMAL_DIVIDER:
//                    DividerHolder not = (DividerHolder) h;
//                    not.icon.setImageResource(R.drawable.ic_not_hot_24dp);
//                    not.text.setText(R.string.not_hot);
//                    not.text.setTextColor(Color.parseColor("#1d6FC5"));
//                    break;
//                case CommentPost.POST:
//                    CommentHolder holder = (CommentHolder) h;
//                    holder.userName.setText(String.format("@%s", post.author.name));
//                    holder.commentDate.setText(post.created_at);
//                    holder.message.setText(post.message);
//                    if (TextUtils.isEmpty(post.author.avatar_url))
//                        holder.userHead.setImageURI(
//                                // http://stackoverflow.com/questions/30887615/loading-drawable-image-resource-in-frescos-simpledraweeview
//                                new Uri.Builder()
//                                        .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
//                                        .path(String.valueOf(R.drawable.fakeimg))
//                                        .build()
//                        ); //测试
//                    else
//                        holder.userHead.setImageURI(Uri.parse(post.author.avatar_url));
//                    if (post.parents.size() > 0) {
//                        holder.quoteView.setVisibility(View.VISIBLE);
//                        holder.quoteView.setUpQuoteLink(gson, comments, post.parents, post.parents.size() - 1);
//                    } else {
//                        holder.quoteView.setVisibility(View.GONE);
//                    }
//                    break;
//            }
//        }
//
//        /**
//         * 评论Holder
//         */
//        class CommentHolder extends RecyclerView.ViewHolder {
//            @BindView(R.id.user_name)
//            TextView userName;
//            @BindView(R.id.user_head)
//            SimpleDraweeView userHead;
//            @BindView(R.id.comment_date)
//            TextView commentDate;
//            @BindView(R.id.message)
//            TextView message;
//            @BindView(R.id.quote)
//            QuoteView quoteView;
//
//            public CommentHolder(View itemView) {
//                super(itemView);
//                ButterKnife.bind(this, itemView);
//                // Init PlaceHolder
//                userHead.getHierarchy()
//                        .setPlaceholderImage(R.drawable.fakeimg);
//            }
//        }
//
//        /**
//         * 分隔线Holder
//         */
//        class DividerHolder extends RecyclerView.ViewHolder {
//            @BindView(R.id.divider_icon)
//            ImageView icon;
//            @BindView(R.id.divider_text)
//            TextView text;
//
//            public DividerHolder(View itemView) {
//                super(itemView);
//                ButterKnife.bind(this, itemView);
//            }
//        }
//    }
}
