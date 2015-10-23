package com.blazers.jandan.views.loadmore;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Blazers on 2015/8/27.
 */
public class LoadMoreRecyclerView extends RecyclerView {

    protected ScrollDirection mCurScrollingDirection;
    private int mPrevFirstVisibleItem;
    private boolean mIsLoadMoreEnabled = true;
    private PullCallback mPullCallback;
    private int mLoadMoreOffset = 5;
    private RecyclerViewPositionHelper mRecyclerViewHelper;

    public LoadMoreRecyclerView(Context context) {
        super(context);
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        /**
         * 在Fragment中使用的时候会导致 LayoutManage null 错误！
         * */
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(this);
        init();
    }

    void init() {
        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mCurScrollingDirection = null;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mCurScrollingDirection == null) { //User has just started a scrolling motion
                    mCurScrollingDirection = ScrollDirection.SAME;
                    mPrevFirstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
                } else {
                    final int firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
                    if (firstVisibleItem > mPrevFirstVisibleItem) {
                        //User is scrolling up
                        mCurScrollingDirection = ScrollDirection.UP;
                    } else if (firstVisibleItem < mPrevFirstVisibleItem) {
                        //User is scrolling down
                        mCurScrollingDirection = ScrollDirection.DOWN;
                    } else {
                        mCurScrollingDirection = ScrollDirection.SAME;
                    }
                    mPrevFirstVisibleItem = firstVisibleItem;
                }

                if (mIsLoadMoreEnabled && (mCurScrollingDirection == ScrollDirection.UP)) {
                    //We only need to paginate if user scrolling near the end of the list
                    if (!mPullCallback.isLoading() && !mPullCallback.hasLoadedAllItems()) {
                        //Only trigger a load more if a load operation is NOT happening AND all the items have not been loaded
                        final int totalItemCount = mRecyclerViewHelper.getItemCount();
                        final int firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
                        final int visibleItemCount = Math.abs(mRecyclerViewHelper.findLastVisibleItemPosition() - firstVisibleItem);
                        final int lastAdapterPosition = totalItemCount - 1;
                        final int lastVisiblePosition = (firstVisibleItem + visibleItemCount) - 1;
                        if (lastVisiblePosition >= (lastAdapterPosition - mLoadMoreOffset)) {
                            if (null != mPullCallback) {
                                mPullCallback.onLoadMore();
                            }
                        }
                    }
                }
            }
        });
    }

    public void setPullCallback(PullCallback mPullCallback) {
        this.mPullCallback = mPullCallback;
    }

    public void setLoadMoreOffset(int mLoadMoreOffset) {
        this.mLoadMoreOffset = mLoadMoreOffset;
    }

    public void isLoadMoreEnabled(boolean mIsLoadMoreEnabled) {
        this.mIsLoadMoreEnabled = mIsLoadMoreEnabled;
    }

}
