package com.blazers.jandan.views.nightwatch;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;
import com.blazers.jandan.R;

/**
 * Created by Blazers on 2015/11/4.
 *
 *
 1 - Any attribute values in the given AttributeSet.
 2 - The style resource specified in the AttributeSet (named     "style").
 3 - The default style specified by defStyleAttr and     defStyleRes
 4 - The base values in this theme.
 *
 */
public class WatchTextView extends TextView implements INightWatch {

    /* 保存变量 */
    private int dayStyleId, nightStyleId;
    private int textColorDay, textColorNight;
    private Drawable bgDay, bgNight;
    /* TODO:添加对Drawable的支持 */

    public WatchTextView(Context context) {
        super(context);
        init(context, null);
    }

    public WatchTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WatchTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    /**
     * 读取两套皮肤 优先读取Style随后的其他属性则可以覆盖掉Style便于单独控制
     * */
    void init(Context context, AttributeSet attrs) {
//        if (null == attrs)
//            return;
//        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WatchTextView, 0, 0);
//        try {
//            /* 获取Style */
//            dayStyleId = a.getResourceId(R.styleable.WatchTextView_wtv_day_style, -1);
//            nightStyleId = a.getResourceId(R.styleable.WatchTextView_wtv_night_style, -1);
//            /* 一般 */
//            textColorDay = a.getColor(R.styleable.WatchTextView_wtv_day_textColor, -1);
//            textColorNight = a.getColor(R.styleable.WatchTextView_wtv_night_textColor, -1);
//
//            bgDay = a.getDrawable(R.styleable.WatchTextView_wtv_day_bg);
//            bgNight = a.getDrawable(R.styleable.WatchTextView_wtv_night_bg);
//        }finally {
//            a.recycle();
//        }
    }

    @Override
    public void setDayMode() {
        /* 首先设置Style */
        if (dayStyleId != -1)
            setTextAppearance(getContext(), dayStyleId);
        /* 设置文字 & 背景 */   // TODO: 若文字同样采用了Selector如何处理?
        if (textColorDay != -1)
            setTextColor(textColorDay);
        if (null != bgDay)
            setBackground(bgDay);
    }

    @Override
    public void setNightMode() {
        if (nightStyleId != -1)
            setTextAppearance(getContext(), nightStyleId);
        if (textColorNight != -1)
            setTextColor(textColorDay);
        if (null != bgNight)
            setBackground(bgNight);
    }
}
