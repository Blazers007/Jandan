package com.blazers.jandan.ui.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Blazers on 2015/10/27.
 */
public class SeekBar  extends View {

    private final static int IDLE = 0, MOVING = 1;
    private final static int BIG_RADIUS = 44, SMALL_RADIUS = 28;

    //  可以指定或是获取的
    private int mMax = 30;
    private int mTickNumber = 6;    //  几个刻度
    private int mNorProgress = 0;

    //  标志位
    private int mNowState = IDLE;      //  当前处于哪种状态
    private int mIdleRadius = 28;    //  静止时的半径
    private int mMovingRadius = 28;  //  移动时候的半径
    private int mHintRadius = 52;    //  依次递增
    private int mHintTextSize = 48;  //  提示文字的大小
    private float mHintX;

    private int mIdleColor = Color.rgb(255, 67, 50);     //  静止时的颜色
    private int mMovingColor = Color.rgb(255,67,50);   //  滑动时的颜色
    private int mHintColor = Color.rgb(63,81,181);     //  提示背景色
    private int mHintTextColor = Color.WHITE; //  提示文字颜色

    private int mLineIdleHeight = 4;//  线粗细
    private int mLineCoverHeight = 10;
    private int mLineLeftX; //  开始位置
    private int mLineRightX;//  结束位置

    private int mTickHeight = 24 ;    //  刻度的高度
    private int mTickWidth = 8 ;     //  刻度的宽度
    private int mTickColor = Color.GRAY;     //  刻度的颜色

    private int mLineIdleColor = Color.GRAY;   //  线的基本色
    private int mLineCoverColor = Color.rgb(255,67,50);    //  线的覆盖色

    private int CH; //  线的Y坐标

    private int mHintYOffset = 0;       //  用于控制提示框的上下移动与透明度
    private int mHintAlphaOffset = 0;

    public SeekBar(Context context) {
        super(context);
    }

    public SeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //  获取控件的高度以及宽度来计算
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mLineLeftX = getPaddingLeft() + mHintRadius;
        mLineRightX = getWidth() - getPaddingRight() - mHintRadius;
//        CH = getHeight() / 2 + 18;  //向下偏移 25
        CH = getHeight()/2 + 36;  //向下偏移 25
        //  初始值为0
        mHintX = mLineLeftX;
        //
    }

    /**
     *  画出最终样式
     * @param canvas    画板
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //  01 画最底层的背景色 刻度
        //  02 根据进度画出已选择的进度（颜色 + 宽度）
        //  03 根据手势状态画出指示器位置 以及改变滑动 按钮的半径
        drawBackground(canvas);
        drawTick(canvas);
        drawCover(canvas);
        drawHint(canvas);
        drawCircle(canvas);

    }

    @SuppressWarnings("all")
    void drawBackground(Canvas canvas){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mLineIdleColor);
        paint.setStrokeWidth(mLineIdleHeight);
        canvas.drawLine(mLineLeftX, CH , mLineRightX, CH, paint);
    }

    void drawTick(Canvas canvas){
        int tickHeadY = CH - (mTickHeight / 2);
        int tickFooterY = CH + (mTickHeight / 2);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);

        textPaint.setTextAlign(Paint.Align.CENTER);

        int lineWidth = mLineRightX - mLineLeftX;

        for(int i = 0 ; i < mTickNumber ; i ++){
            int x = lineWidth * (i + 1) / mTickNumber;
            int tick = mMax * (i + 1) / mTickNumber;
            //  判断是否已经被选中
            if(mNorProgress < tick){
                paint.setColor(mTickColor);
                paint.setStrokeWidth(mTickWidth);

                textPaint.setColor(mTickColor);
                textPaint.setStrokeWidth(mTickWidth);
                textPaint.setTextSize(32);
            }else{
                paint.setColor(mLineCoverColor);
                paint.setStrokeWidth(mTickWidth + 1);

                textPaint.setColor(mLineCoverColor);
                textPaint.setStrokeWidth(mTickWidth);
                textPaint.setTextSize(36);
            }

            canvas.drawLine(mLineLeftX + x, tickHeadY, mLineLeftX +  x, tickFooterY, paint);
            canvas.drawText(Integer.toString(tick) +":00", mLineLeftX + x, tickFooterY + 36, textPaint);
        }
    }

    void drawCover(Canvas canvas){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mLineCoverColor);
        paint.setStrokeWidth(mLineCoverHeight);
        //  计算位置
        int lineWidth = mLineRightX - mLineLeftX;
        int x = lineWidth * mNorProgress / mMax;

        canvas.drawLine(mLineLeftX, CH , mLineLeftX + x , CH, paint);
    }

    void drawHint(Canvas canvas){
        if(mNowState != MOVING)
            return;
        //  判断状态
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mHintColor);
        paint.setAlpha(255- mHintAlphaOffset);

        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setColor(mHintTextColor);
        textPaint.setStrokeWidth(mTickWidth);
        textPaint.setTextSize(mHintTextSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAlpha(255 - mHintAlphaOffset);

        //  计算位置
//        int lineWidth = mLineRightX - mLineLeftX;
//        int x = lineWidth * mNorProgress / mMax;
        canvas.drawCircle(mHintX, CH - mTickHeight - mHintRadius - mMovingRadius + mHintYOffset, mHintRadius, paint);
        canvas.drawText(Integer.toString(mNorProgress), mHintX, CH - mTickHeight - mHintRadius - mMovingRadius + 16 + mHintYOffset, textPaint);
    }

    void drawCircle(Canvas canvas){
        //  计算位置
        int lineWidth = mLineRightX - mLineLeftX;
        int x = lineWidth * mNorProgress / mMax;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mIdleColor);
        canvas.drawCircle(mHintX, CH, mIdleRadius, paint);
        //  判断状态
        if(mNowState == MOVING) {
            Paint _paint = new Paint();
            _paint.setAntiAlias(true);
            _paint.setColor(mMovingColor);
            _paint.setAlpha(200);
            canvas.drawCircle(mHintX, CH, mMovingRadius, _paint);
        }else{
            Paint _paint = new Paint();
            _paint.setAntiAlias(true);
            _paint.setColor(Color.WHITE);
            canvas.drawCircle(mHintX, CH, mMovingRadius - 4, _paint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //  符合范围才计算
        float x = event.getX();
        int offset = (int)(x - mLineLeftX);
        int target = mMax * offset / (mLineRightX - mLineLeftX);
        if(target >= 0 && target <= mMax)
            mNorProgress = mMax * offset / (mLineRightX - mLineLeftX);
        if( x >= mLineLeftX && x <= mLineRightX)
            mHintX = x;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mNowState = MOVING;
                mMovingRadius = 14;
                mHintYOffset = 26;
                mHintAlphaOffset = 234;
                //  清空消息队列
                clearAllMessage();
                handler.post(show);
                handler.post(bigger);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mMovingRadius = 22;
                mHintYOffset = 0;
                mHintAlphaOffset = 0;
                //  清空消息队列
                clearAllMessage();
                handler.post(hide);
                handler.post(smaller);
                break;
        }
        //  return True if the event was handled, false otherwise.
        invalidate();
        return true;
    }

    /*
        动画部分
     */

    private final int TYPE_BIGGER = 0, TYPE_SMALLER = 1, TYPE_SHOW = 2, TYPE_HIDE = 3;

    Runnable bigger = new Runnable() {
        @Override
        public void run() {
            mMovingRadius ++;
            handler.sendEmptyMessage(TYPE_BIGGER);
        }
    };

    Runnable smaller = new Runnable() {
        @Override
        public void run() {
            mMovingRadius --;
            handler.sendEmptyMessage(TYPE_SMALLER);
        }
    };

    //  Set yOffset = 26   8times
    //  Set alphaOffset = 234
    Runnable show = new Runnable() {
        @Override
        public void run() {
            mHintYOffset --;
            mHintAlphaOffset -= 9;
            handler.sendEmptyMessage(TYPE_SHOW);
        }
    };

    //  Set yOffset = 0  8times
    //  Set alphaOffset = 0
    Runnable hide = new Runnable() {
        @Override
        public void run() {
            mHintYOffset ++;
            mHintAlphaOffset += 9;
            handler.sendEmptyMessage(TYPE_HIDE);
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //  判断种类 并判断是否继续
            switch (msg.what){
                case TYPE_BIGGER:
                    if(mMovingRadius < BIG_RADIUS)
                        handler.postDelayed(bigger,16);
                    break;
                case TYPE_SMALLER:
                    if(mMovingRadius > SMALL_RADIUS)
                        handler.postDelayed(smaller,16);
                    break;
                case TYPE_SHOW:
                    if(mHintYOffset > 0)
                        handler.postDelayed(show,5);
                    break;
                case TYPE_HIDE:
                    if(mHintYOffset < 26)
                        handler.postDelayed(hide,5);
                    else
                        mNowState = IDLE;
                    break;
            }
            invalidate();
        }
    };

    void clearAllMessage(){
        handler.removeCallbacks(smaller);
        handler.removeCallbacks(bigger);
        handler.removeCallbacks(show);
        handler.removeCallbacks(hide);
    }

    //  外部接口
    public int getProgress(){
        return mNorProgress;
    }
}
