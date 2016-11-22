package com.blazers.jandan.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.view.animation.BounceInterpolator;
import android.widget.OverScroller;
import com.blazers.jandan.R;
import com.blazers.jandan.util.Dppx;

/**
 * Created by Blazers on 2015/10/27.
 *
 * 自定义的无限横向滚动SeekBar 实现数值的选取 而不需要输入
 *
 * 如果需要支持小数 则添加一个转化器即可
 *
 */
public class InfiniteSeekBar extends View implements GestureDetector.OnGestureListener {

    /* 参数 */
    private boolean mSupportNegative = false;       // 默认不支持负数

    /* 数值相关参数 */
    private int mDefaultRangeStart = 1;
    private int mDefaultRangeEnd = 30;
    private int mDefaultSelectedValue = 1;          // 显示的值

    private int mDefaultSelectedIndex;              // 算中的顺序

    /* UI相关参数 */
    private float mDefaultTextSize = Dppx.Dp2Px(getContext(), 14);
    private Paint.FontMetricsInt mFontMetrics;

    private int mDefaultSelectedTextColor = Color.rgb(20, 136, 255);
    private int mDefaultNormalTextColor = Color.rgb(23, 23, 23);
    private int mDefaultSegmentWidth = Dppx.Dp2Px(getContext(), 36);                // 默认每格大小
    private int mDefaultSelectorWidth = mDefaultSegmentWidth + Dppx.Dp2Px(getContext(), 8);
    private int mDefaultSelectorBitmapResource = R.drawable.selector_default_btn;   // 默认指示器的图像

    /* 动画计算参数 */
    private float mXOffset;                         // 需要计算的出
    private VelocityTracker mVelocityTracker;
    private int mMaxVelocity;

    private float mDefaultSegmentAttractSpeed = 32;           // 每一段的阻力
    private float mDefaultOverScrollDistance = Dppx.Dp2Px(getContext(), 32);   // 若滑动之头或尾能够额外滑动的最大距离 越大X衰减越接近0

    /* Value Formatter */
    private ValueFormatter mValueFormatter;

    /* 画笔 */
    private Paint mTextPaint;

    /* 2015 11-16 Update 采用OverScroller */
    private OverScroller mScroller;
    private GestureDetector mGestureDetector;


    public InfiniteSeekBar(Context context) {
        super(context);
        init(context, null);
    }

    public InfiniteSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public InfiniteSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    /**
     * 初始化方法的入口
     * */
    void init(Context context, AttributeSet attrs) {
        // Paint
        initPaint();
        // Scroller
        mScroller = new OverScroller(context, new BounceInterpolator());
        mGestureDetector = new GestureDetector(context, this);
        /* 设置速度计算 */
        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        /* 根据参数赋值计算一些初始化的数值 */
        mXOffset = (mDefaultSelectedValue - mDefaultRangeStart) * mDefaultSegmentWidth;
    }


    /**
     * 初始化各部分画笔
     * */
    void initPaint() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mDefaultTextSize);

        mFontMetrics = mTextPaint.getFontMetricsInt();
    }


    private float lastTouchX;
    private float velocityX;
    /**
     * 记录滑动  计算滑动速度 更改参数 更新UI  TODO:采用GestureDetector来实现点击操作
     * */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }


    /**
     * 记录当前状态
     * */
    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    /**
     * 根据参数绘制
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float middleLine = getMeasuredWidth() / 2;
        float middleY = getMeasuredHeight() / 2;
        // 位置都是文字所处Rect的中轴线?
        int index = 0;
        for (int value = mDefaultRangeStart ; value <= mDefaultRangeEnd; value ++, index++) {
            // 计算每个Seg的中轴线的偏移量 - 0的偏移量 得出实际偏移量
            float offset = index * mDefaultSegmentWidth + mXOffset;
            // 判断是否需要绘制
            if (offset > -middleLine && offset < middleLine) {
                // 绘制文字
                String text;
                if (null != mValueFormatter) {
                    text = mValueFormatter.setXValue(value);
                } else {
                    text = "" + value;
                }
                mTextPaint.setAlpha(getTextAlphaByOffset(offset, middleLine));
                float textWidth = mTextPaint.measureText(text);
                float x = middleLine + offset - textWidth/2;
                float baseline = (getMeasuredHeight() - mFontMetrics.bottom + mFontMetrics.top) / 2 - mFontMetrics.top;
                if (Math.abs(offset) < mDefaultSegmentWidth/2) {
                    mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
                    mTextPaint.setColor(mDefaultSelectedTextColor);
                    canvas.drawText(text, x, baseline, mTextPaint);
                    mDefaultSelectedValue = value;
                    mDefaultSelectedIndex = index;
                    Log.e(">>>Selected<<<", "" + mDefaultSelectedValue);
                } else {
                    mTextPaint.setTypeface(Typeface.DEFAULT);
                    mTextPaint.setColor(mDefaultNormalTextColor);
                    canvas.drawText(text, x, baseline, mTextPaint);
                }
                //
            }
        }
    }


    /**
     * 计算文字的透明度  10~255
     * */
    int getTextAlphaByOffset(float offset, float halfWidth) {
        float x = halfWidth - Math.abs(offset);
        return (int)(20 + Math.pow((15*(x/halfWidth)), 2));
    }


    private boolean mScrolling;
    private Runnable mScrollingRunnable;
    /**
     * 继续滑动
     * */
    void computeSeekBarScroll() {
        mScrolling = true;
        if (null == mScrollingRunnable) {
            mScrollingRunnable = ()->{
                if (Math.abs(velocityX) < 5 && mScrolling){ // 速度大于0或者处于某个值之内
                    mXOffset += velocityX;
                    velocityX *= 0.9f;
                    Log.e("Speed", "" + velocityX);
                    invalidate();
                    postDelayed(mScrollingRunnable, 16);
                } else {
                    velocityX = 0;
                    mScrolling = false;
                }
            };
        }
        post(mScrollingRunnable);
    }

    /**
     * 接口
     * */
    public interface ValueFormatter {
        String setXValue(int index);
    }

    public void setValueFormatter(ValueFormatter formatter) {
        this.mValueFormatter = formatter;
    }

    public int getSelectedValue() {
        return mDefaultSelectedValue;
    }


    /* OnGesture Detector */
    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        mXOffset -= distanceX;
        invalidate();
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (velocityY > 0) {
            // 消耗掉
//            mScroller.fling((int)mXOffset, 0, 0, (int)velocityY, 0, 300, 0, 0);
            return true;
        }
        return false;
    }

    /* Compute Scroll */

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            mXOffset = mScroller.getCurrX();
            invalidate();
        }
    }
}
