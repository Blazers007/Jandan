package com.blazers.jandan.ui.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.blazers.jandan.util.SPHelper;

/**
 * Created by Blazers on 15/9/19.
 *
 * Update @ 11-16
 */
public class VerticalDividerItemDecoration extends RecyclerView.ItemDecoration {

    private Context context;
    private Paint paint;
    private int height;
    private int color;

    public VerticalDividerItemDecoration(Context context, int height, int color) {
        super();
        this.context = context;
        this.height = height;
        this.color = color;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, height);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (SPHelper.getBooleanSP(context, SPHelper.NIGHT_MODE_ON, false)) {
            paint.setColor(Color.rgb(55, 56, 54));
        } else {
            paint.setColor(color);
        }
        int width = parent.getRight();
        for (int i = 0 ; i < parent.getChildCount() ; i ++) {
            View child = parent.getChildAt(i);
            int top = child.getBottom();
            c.drawRect(0, top, width, top + height, paint);
        }
    }
}
