package com.blazers.jandan.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
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
public class InfiniteSeekBar extends View {

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
        initPaint();
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
        if (null == mVelocityTracker)
            mVelocityTracker = VelocityTracker.obtain();
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 取消滑动
                mScrolling = false;
                removeCallbacks(mScrollingRunnable);
                velocityX = 0;
                lastTouchX = event.getX();
//                Log.i(">>> [ Start ] <<<", "" + lastTouchX);
                break;
            case MotionEvent.ACTION_MOVE:
                // 计算偏移量 并对越界的进行衰减判断
                float nowX = event.getX();
                float offsetX = nowX - lastTouchX;
                // 是否需要衰减
                if (mXOffset > 0 && offsetX >0) {
                    offsetX *= (1 - mXOffset / 140.0f);
                } else if (mXOffset < (mDefaultRangeEnd+2) * -100 && offsetX < 0) {
                    offsetX *= (1 - Math.abs(mXOffset) / ((mDefaultRangeEnd+6)*100.0f));
                }
                lastTouchX = nowX;
                mXOffset += offsetX;
                Log.i(">>> [ XOffset ] <<<", "" + mXOffset);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 计算速度
                mVelocityTracker.computeCurrentVelocity(100, mMaxVelocity);
                velocityX = mVelocityTracker.getXVelocity();
//                Log.i(">>> [Vel X] <<<", "" + velocityX);
                // 计算加速度 延时动画效果
                float upX = event.getX();
//                Log.i(">>> [ UP ] <<<", "" + upX);
//                Log.i(">>> [ Final Speed ] <<<", "" + velocityX);
                // 根据速度计算是否需要滑动 如果不需要则画像最近的一个
                if (velocityX > 100) {
                    computeSeekBarScroll();
                } else {
                    computeToSegment();
                }
                // 释放
                if(null != mVelocityTracker) {
                    mVelocityTracker.clear();
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
        }
        invalidate();
        return true;
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
                    computeToSegment();
                }
            };
        }
        post(mScrollingRunnable);
    }


    private Runnable mSegmentScrollRunnable;
    /**
     * 滑动到最近的一个地方
     * */
    void computeToSegment() {
//        mScrolling = true;
//        if (mSegmentScrollRunnable == null) {
//            mSegmentScrollRunnable = ()->{
//                if (mScrolling) {
//                    float x = mXOffset + (mDefaultSelectedValue - mDefaultRangeStart) * mDefaultSegmentWidth;
//                    if (Math.abs(x) < 20) {
//                        mXOffset+=2;
//                        invalidate();
//                        postDelayed(mSegmentScrollRunnable, 16);
//                    } else {
//                        mXOffset = (mDefaultSelectedValue - mDefaultRangeStart) * mDefaultSegmentWidth;
//                        invalidate();
//                        mScrolling = false;
//                    }
//                }
//            };
//        }
//        post(mSegmentScrollRunnable);
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

}
