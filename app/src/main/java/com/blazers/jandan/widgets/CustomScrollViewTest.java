package com.blazers.jandan.widgets;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.OverScroller;

/**
 * Created by blazers on 2016/11/16.
 */

public class CustomScrollViewTest extends ViewGroup {

    private OverScroller mOverScroller;

    private RectF mCurrentViewport = new RectF();

    public CustomScrollViewTest(Context context) {
        this(context, null);
    }

    public CustomScrollViewTest(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomScrollViewTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(true);
    }

    @Override
    protected void onLayout(boolean b, int left, int top, int right, int bottom) {

    }
}
