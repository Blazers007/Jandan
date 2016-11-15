package com.blazers.jandan.ui.widgets.behavior;

import android.content.Context;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by blazers on 2016/11/15.
 * 能够响应Snackbar的BottomNavigationView
 */
public class BottomNavigationViewBehavior extends CoordinatorLayout.Behavior<LinearLayout> {

    public BottomNavigationViewBehavior() {

    }

    public BottomNavigationViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {
        // 当前状态下仅仅SnackBar会引起改变 仅做测试用 每次布局中的View发生改变(位置？可见程度？大小？)
        float translationY = getFabTranslationYForSnackbar(parent, child);
        child.setTranslationY(translationY);
        return false;
    }


    /**
     * 遍历并获取SnackBar的位置
     * @param parent
     * @param bottomNavigationView
     * @return
     */
    private float getFabTranslationYForSnackbar(CoordinatorLayout parent, LinearLayout bottomNavigationView) {
        float minOffset = 0;
        final List<View> dependencies = parent.getDependencies(bottomNavigationView);
        for (int i = 0, z = dependencies.size(); i < z; i++) {
            final View view = dependencies.get(i);
            if (view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(bottomNavigationView, view)) {
                // SnackBar.Layout的默认布局位置是显示布局的Bottom，故一开始便是设置getHeight()的translationY 随后减小至0 既初始位置 随后又继续增加至getHeight()
                minOffset = Math.min(minOffset, ViewCompat.getTranslationY(view) - view.getHeight());
            }
        }
        return minOffset;
    }
}
