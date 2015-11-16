package com.blazers.jandan.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Blazers on 2015/9/14.
 */
public class AutoLayoutLinearLayout extends LinearLayout {

    private static final int PADDING_HOR = 5;//水平方向padding
    private static final int PADDING_VERTICAL = 2;//垂直方向padding
    private static final int SIDE_MARGIN = 10;//左右间距
    private static final int TEXT_MARGIN = 10;

    public AutoLayoutLinearLayout(Context context) {
        super(context);
    }

    public AutoLayoutLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLayoutLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int x = 0;//横坐标
        int y = 0;//纵坐标
        int rows = 1;//总行数
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);  //  获取ViewGroup宽度
        int actualWidth = specWidth - SIDE_MARGIN * 2;//实际宽度
        //  获得子View的个数，下面遍历这些子View设置宽高
        int childCount = getChildCount();
        for(int index = 0 ; index < childCount ; index++){
            View child = getChildAt(index);
//            child.setPadding(PADDING_HOR, PADDING_VERTICAL, PADDING_HOR, PADDING_VERTICAL);
            //  在方法onMeasure中调用孩子的measure方法 ---> 设置子View宽高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
//            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
//            Log.i("Child Measured", "w ->" + width + "  h->" + height + "  ID->" + index);
            x += width + TEXT_MARGIN;
            if(x > actualWidth){//换行
                x = width;
                rows++;
            }
            y = rows * (height + TEXT_MARGIN);
//            Log.i("Measured Row", "" + rows);
        }
        setMeasuredDimension(actualWidth, y);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int autualWidth = r - l;
        int x = SIDE_MARGIN;// 横坐标开始
        int y = 0;//纵坐标开始
        int rows = 1;
        for(int i = 0 ; i < childCount ; i++){
            View view = getChildAt(i);
            int width = view.getMeasuredWidth();
            int height = view.getMeasuredHeight();
//            Log.e("Child Measured", "w ->" + width + "  h->" + height + "  ID->" + i);
            x += width + TEXT_MARGIN;
            if(x > autualWidth){
                x = width + SIDE_MARGIN;
                rows++;
            }
            y = rows * (height + TEXT_MARGIN);
            if(i == 0){
                view.layout(x - width -TEXT_MARGIN, y - height, x - TEXT_MARGIN, y);
            }else{
                view.layout(x - width, y - height, x, y);
            }
        }
    }

    @Override
    public void addView(View child) {
        super.addView(child);
    }
}
