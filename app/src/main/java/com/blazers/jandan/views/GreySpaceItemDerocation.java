package com.blazers.jandan.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Blazers on 15/9/19.
 */
public class GreySpaceItemDerocation extends RecyclerView.ItemDecoration {

    private Paint greyPaint;

    public GreySpaceItemDerocation() {
        super();
        greyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        greyPaint.setColor(Color.rgb(241,242,241));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, 24);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int width = parent.getRight();
        for (int i = 0 ; i < parent.getChildCount() ; i ++) {
            View child = parent.getChildAt(i);
            int top = child.getBottom();
            c.drawRect(0, top, width, top + 24, greyPaint);
        }
    }
}
