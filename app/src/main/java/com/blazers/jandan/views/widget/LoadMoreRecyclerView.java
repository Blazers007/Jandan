package com.blazers.jandan.views.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Blazers on 2015/8/27.
 */
public class LoadMoreRecyclerView extends RecyclerView {

    public static final int UP = 0, DOWN = 1, SAME = 2;
    protected int mCurScrollingDirection;
    private int mPrevFirstVisibleItem;
    private boolean mIsLoadMoreEnabled = true;
    private boolean mIsLoading;

    private int mLoadMoreOffset = 1;

    private RecyclerViewPositionHelper mRecyclerViewHelper;

    private LoadMoreListener loadMoreListener;

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
        mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(this);
        init();
    }

    void init() {
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mCurScrollingDirection = -1;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mCurScrollingDirection == -1) { //User has just started a scrolling motion
                    mCurScrollingDirection = SAME;
                    mPrevFirstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
                } else {
                    final int firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
                    if (firstVisibleItem > mPrevFirstVisibleItem) {
                        //User is scrolling up
                        mCurScrollingDirection = UP;
                    } else if (firstVisibleItem < mPrevFirstVisibleItem) {
                        //User is scrolling down
                        mCurScrollingDirection = DOWN;
                    } else {
                        mCurScrollingDirection = SAME;
                    }
                    mPrevFirstVisibleItem = firstVisibleItem;
                }


                if (mIsLoadMoreEnabled && (mCurScrollingDirection == UP)) {
                    if (!mIsLoading) {
                        final int totalItemCount = getAdapter().getItemCount();
                        final int firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
                        final int visibleItemCount = Math.abs(mRecyclerViewHelper.findLastVisibleItemPosition() - firstVisibleItem);
                        final int lastAdapterPosition = totalItemCount - 1;
                        final int lastVisiblePosition = (firstVisibleItem + visibleItemCount) - 1;
                        if (lastVisiblePosition >= (lastAdapterPosition - mLoadMoreOffset) && totalItemCount > 0) {
                            if (null != loadMoreListener) {
                                mIsLoading = true;
                                loadMoreListener.startLoadMore();
                            }
                        }
                    }
                }
            }
        });
    }

    public void endLoading() {
        mIsLoading = false;
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public interface LoadMoreListener {
        void startLoadMore();
    }
}
