package com.blazers.jandan.widgets;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by blazers on 2016/11/16.
 */

public class MiddleManScrollView extends ViewGroup implements NestedScrollingParent, NestedScrollingChild {

    private final NestedScrollingParentHelper mParentHelper;
    private final NestedScrollingChildHelper mChildHelper;
    private ScrollerCompat mScroller;

    public MiddleManScrollView(Context context) {
        this(context, null);
    }

    public MiddleManScrollView(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public MiddleManScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Init Helper
        mParentHelper = new NestedScrollingParentHelper(this);
        mChildHelper = new NestedScrollingChildHelper(this);
    }

    /* =============================================================================================
     *
     *                              NestedScroll - Child
     *
     * =============================================================================================
     */

    /**
     *
     * @param enabled
     */
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        super.setNestedScrollingEnabled(enabled);
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isNestedScrollingEnabled() {
        return mChildHelper.isNestedScrollingEnabled();
    }

    /**
     *
     * @param axes
     * @return
     */
    @Override
    public boolean startNestedScroll(int axes) {
        return mChildHelper.startNestedScroll(axes);
    }

    /**
     *
     */
    @Override
    public void stopNestedScroll() {
        mChildHelper.stopNestedScroll();
    }

    /**
     *
     * @return
     */
    @Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    /**
     *
     * @param dxConsumed
     * @param dyConsumed
     * @param dxUnconsumed
     * @param dyUnconsumed
     * @param offsetInWindow
     * @return
     */
    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    /**
     *
     * @param dx
     * @param dy
     * @param consumed
     * @param offsetInWindow
     * @return
     */
    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    /**
     *
     * @param velocityX
     * @param velocityY
     * @param consumed
     * @return
     */
    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    /**
     *
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }


    /* =============================================================================================
     *
     *                                         Layout
     *
     * =============================================================================================
     */


    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }
}
