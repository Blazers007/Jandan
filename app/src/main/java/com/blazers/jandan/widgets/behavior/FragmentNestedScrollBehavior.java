package com.blazers.jandan.widgets.behavior;

import android.animation.Animator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.blazers.jandan.R;
import com.blazers.jandan.util.log.Log;

/**
 * Created by blazers on 2016/11/15.
 * <p>
 * 处理内部的滑动操作与外部其他View的互动
 */

public class FragmentNestedScrollBehavior extends CoordinatorLayout.Behavior<FrameLayout> {

    private boolean mIsAnimating = false;
    private View bottomNavigationView;

    private Animator.AnimatorListener mListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            mIsAnimating = true;
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            mIsAnimating = false;
        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    };

    public FragmentNestedScrollBehavior() {
    }

    public FragmentNestedScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * 是否开始滑动？
     *
     * @param coordinatorLayout
     * @param child
     * @param directTargetChild
     * @param target
     * @param nestedScrollAxes
     * @return
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FrameLayout child, View directTargetChild, View target, int nestedScrollAxes) {
//        Log.i("====", "StartNestedScroll  ---> " + directTargetChild.getClass().getSimpleName());
//        Log.i("====", "StartNestedScroll  ---> " + target.getClass().getSimpleName());
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    /**
     * 滑动
     *
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param dxConsumed
     * @param dyConsumed
     * @param dxUnconsumed
     * @param dyUnconsumed
     */
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FrameLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.i("====", "NestedScroll  ---> " + target.getClass().getSimpleName() + " dyConsumed -> " + dyConsumed + "   dyUnconsumed -> " + dyUnconsumed);
        if (mIsAnimating) {
            return;
        }
        if (bottomNavigationView == null) {
            // TODO : 2018年01月30日15:58:46 注解
//            bottomNavigationView = coordinatorLayout.findViewById(R.id.bottom_navigation_view_wrapper);
        }
        if (bottomNavigationView != null) {
            if (dyConsumed > 0) {
                bottomNavigationView.animate().translationY(bottomNavigationView.getHeight()).setDuration(500).setListener(mListener).start();
            } else {
                bottomNavigationView.animate().translationY(0).setDuration(500).setListener(mListener).start();
            }
        }
    }


    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, FrameLayout child, View directTargetChild, View target, int nestedScrollAxes) {
//        Log.i("====", "NestedScrollAccepted  ---> " + directTargetChild.getClass().getSimpleName());
//        Log.i("====", "NestedScrollAccepted  ---> " + target.getClass().getSimpleName());
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, FrameLayout child, View target, int dx, int dy, int[] consumed) {
//        Log.i("====", "NestedPreScroll  ---> " + target.getClass().getSimpleName() + " dy -> " + dy + "   consumed -> " + consumed[1]);
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    }


    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, FrameLayout child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, FrameLayout child, View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, FrameLayout child, View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }
}
