package com.blazers.jandan.views;

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
 */
public class GreySpaceItemDecoration extends RecyclerView.ItemDecoration {

    private Context context;
    private Paint paint;

    public GreySpaceItemDecoration(Context context) {
        super();
        this.context = context;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, 24);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (SPHelper.getBooleanSP(context, SPHelper.NIGHT_MODE_ON, false)) {
            paint.setColor(Color.rgb(55, 56, 54));
        } else {
            paint.setColor(Color.rgb(241, 242, 241));
        }
        int width = parent.getRight();
        for (int i = 0 ; i < parent.getChildCount() ; i ++) {
            View child = parent.getChildAt(i);
            int top = child.getBottom();
            c.drawRect(0, top, width, top + 24, paint);
        }
    }
}
