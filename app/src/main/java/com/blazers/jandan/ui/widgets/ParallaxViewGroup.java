package com.blazers.jandan.ui.widgets;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by blazers on 2016/11/15.
 *
 *
 *
 */
public class ParallaxViewGroup extends RelativeLayout implements NestedScrollingParent, NestedScrollingChild {

    public ParallaxViewGroup(Context context) {
        super(context);
    }

    public ParallaxViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
