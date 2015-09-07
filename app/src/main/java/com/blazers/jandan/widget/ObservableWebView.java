package com.blazers.jandan.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

/**
 * Created by Blazers on 2015/9/6.
 */
public class ObservableWebView extends WebView {

    /* Vars */
    private OnWebViewScrollChangeListener listener;

    public ObservableWebView(Context context) {
        super(context);
    }

    public ObservableWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (listener != null)
            listener.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (listener != null)
            listener.onScrollChanged(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    public void setListener(OnWebViewScrollChangeListener listener) {
        this.listener = listener;
    }

    public interface OnWebViewScrollChangeListener {
        void onScrollChanged(int left, int top, int oldLeft, int oldTop);
    }

}
