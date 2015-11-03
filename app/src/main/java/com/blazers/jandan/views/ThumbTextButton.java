package com.blazers.jandan.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageButton;
import com.blazers.jandan.R;
import com.blazers.jandan.util.Dppx;

import java.util.ArrayList;

/**
 * Created by Blazers on 2015/9/8.
 */
public class ThumbTextButton extends ImageButton {

    /* Vars by default */
    private int TEXT_COLOR = 0xff666666;
    private int FLOAT_TEXT_COLOR = 0xff666666;
    private float TEXT_SIZE_SP = 12 * Dppx.Dp2Px(getContext(), 1);
    private float FLOAT_TEXT_SIZE_SP = 12 * Dppx.Dp2Px(getContext(), 1);
    private float TEXT_RIGHT_PADDING = 6 * Dppx.Dp2Px(getContext(), 1);

    /* Vars for static text */
    private Paint textPaint;
    private String textString;
    private Paint.FontMetricsInt fontMetrics;

    /* Animators */
    private Paint defaultAnimatorPaint;
    private ArrayList<AnimationParameters> animationParameters;

    public ThumbTextButton(Context context) {
        super(context);
        init();
    }

    public ThumbTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init();
    }

    public ThumbTextButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }

    /* 如果有需要则覆盖掉相应的参数 */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ThumbTextButton);
        TEXT_COLOR = typedArray.getColor(R.styleable.ThumbTextButton_ttb_textColor, TEXT_COLOR);
        FLOAT_TEXT_COLOR = typedArray.getColor(R.styleable.ThumbTextButton_ttb_floatTextColor, FLOAT_TEXT_COLOR);
        typedArray.recycle();
    }

    /* 初始化参数 */
    private void init() {

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(TEXT_COLOR);
        textPaint.setTextSize(TEXT_SIZE_SP);
        fontMetrics = textPaint.getFontMetricsInt();

        defaultAnimatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        defaultAnimatorPaint.setColor(FLOAT_TEXT_COLOR);
        defaultAnimatorPaint.setTextSize(FLOAT_TEXT_SIZE_SP);

        animationParameters = new ArrayList<>();
    }

    /* APIs */
    public void setThumbText(String text) {
        textString = text;
        invalidate();
    }

    public void addThumbText(int addition) {
        textString = String.valueOf(Integer.parseInt(textString) + addition);
        invalidate();
        playAnimation(addition);
        setPressed(true);
    }


    private void playAnimation(int addition) {
        AnimationParameters parameters = new AnimationParameters(addition > 0 ? "+"+addition : ""+addition, 500, defaultAnimatorPaint);
        animationParameters.add(parameters);
        parameters.start();
    }

    /* 复写实现动画效果 */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int middleLine = getMeasuredHeight() / 2;
        /* Text 布局不受限于基本布局 如 padding margin 采用自己的独特参数进行布局  */
        if (textString != null) {
            float length = textPaint.measureText(textString);
            int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            canvas.drawText(textString, width - TEXT_RIGHT_PADDING - length, baseline, textPaint);
        }
        /* 动画 */
        for (AnimationParameters para : animationParameters) {
            int alpha = para.getAlpha();
            if ( alpha < 0 ) {
                animationParameters.remove(para);
            } else {
                float length = defaultAnimatorPaint.measureText(para.getText());
                int y = (int)(middleLine * (alpha / 255f));
                defaultAnimatorPaint.setAlpha(alpha);
                canvas.drawText(para.getText(), width - TEXT_RIGHT_PADDING - length, y, defaultAnimatorPaint);
            }
        }
    }

    class AnimationParameters {
        private String text;
        private int alpha;
        private long time;
        private Paint paint;

        public AnimationParameters(String text, long time, Paint paint) {
            this.text = text;
            this.time = time;
            this.paint = paint;
            alpha = 255;
        }

        public String getText() {
            return text;
        }

        public int getAlpha() {
            return alpha;
        }

        public long getTime() {
            return time;
        }

        public Paint getPaint() {
            return paint;
        }

        public void start() {
            new Thread(()->{
                while (alpha > 0) {
                    handler.sendEmptyMessage(0);
                    try {
                        Thread.sleep(16);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    alpha -= 255f/60f;
                }
            }).start();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            invalidate();
        }
    };
}
