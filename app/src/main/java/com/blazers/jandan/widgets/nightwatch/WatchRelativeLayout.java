package com.blazers.jandan.widgets.nightwatch;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import com.blazers.jandan.R;
import com.blazers.jandan.util.SPHelper;

/**
 * Created by Blazers on 2015/11/4.
 */
public class WatchRelativeLayout extends RelativeLayout implements INightWatch {

    /* 保存变量 */
    private Drawable bgDay, bgNight;

    public WatchRelativeLayout(Context context) {
        super(context);
        init(context, null);
    }

    public WatchRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WatchRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 读取两套皮肤 优先读取Style随后的其他属性则可以覆盖掉Style便于单独控制
     * */
    void init(Context context, AttributeSet attrs) {
        if (null == attrs)
            return;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WatchView, 0, 0);
        try {
            /* 一般 */
            bgDay = a.getDrawable(R.styleable.WatchView_wtg_day_bg);
            bgNight = a.getDrawable(R.styleable.WatchView_wtg_night_bg);
        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            a.recycle();
        }
        if (SPHelper.getBooleanSP(context, SPHelper.NIGHT_MODE_ON, false)) {
            setNightMode();
        } else {
            setDayMode();
        }
    }

    @Override
    public void setDayMode() {
        if (null != bgDay)
            setBackground(bgDay);
    }

    @Override
    public void setNightMode() {
        if (null != bgNight)
            setBackground(bgNight);
    }
}
