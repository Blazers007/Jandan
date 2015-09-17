package com.blazers.jandan.views.widget;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.blazers.jandan.R;
import com.blazers.jandan.util.Dppx;

/**
 * Created by Blazers on 2015/9/14.
 */
public class SelectableTextView extends TextView implements View.OnClickListener{

    /* Vars for default */
    private float LINE_WIDTH = Dppx.Dp2Px(getContext(), 4);
    private float LABEL_HEIGHT_PERCENT = 0.5f;
    private int COLOR = Color.rgb(50, 139, 255);
    private int LABEL_RES_ID = R.mipmap.ic_done_white;

    /* Vars for canvas */
    private Paint paint, overlayPaint, eraser;
    private Path rectPath, labelPath;
    private Rect iconRect;
    private Bitmap icon;

    /* Vars as memory */
    private Paint.FontMetricsInt fontMetrics;
    private float progress = 50f;
    private boolean selected = false;

    public SelectableTextView(Context context) {
        super(context);
        init();
    }

    public SelectableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init();
    }

    public SelectableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init() {

        setClickable(true);
        setOnClickListener(this);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(COLOR);
        paint.setStrokeWidth(LINE_WIDTH);

        overlayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        overlayPaint.setTextSize(36f);
        overlayPaint.setColor(Color.WHITE);
        fontMetrics = overlayPaint.getFontMetricsInt();

        eraser = new Paint(Paint.ANTI_ALIAS_FLAG);
        eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        icon = BitmapFactory.decodeResource(getResources(), LABEL_RES_ID);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (selected) {
            float width = getMeasuredWidth();
            float height = getMeasuredHeight();
            float labelHeight = height * LABEL_HEIGHT_PERCENT;
            /* init path */
            if (rectPath == null) {
                rectPath = new Path();
                rectPath.moveTo(0,0);
                rectPath.lineTo(width, 0);
                rectPath.lineTo(width, height);
                rectPath.lineTo(0, height);
                rectPath.close();

                labelPath = new Path();
                labelPath.moveTo(width, height);
                labelPath.lineTo(width, height - labelHeight);
                labelPath.lineTo(width - labelHeight, height);
                labelPath.close();

                iconRect = new Rect((int)(width - labelHeight/2), (int)(height - labelHeight/2), (int)width, (int)height);
            }
            /* Border */
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(rectPath, paint);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawPath(labelPath, paint);
            /* ICON
             * drawBitmap(Bitmap bitmap, Rect src, RectF dst, Paint paint)；
                Rect src: 是对图片进行裁截，若是空null则显示整个图片
                RectF dst：是图片在Canvas画布中显示的区域，
                大于src则把src的裁截区放大，
                小于src则把src的裁截区缩小。
              *
              * */
            canvas.drawBitmap(icon, null, iconRect, null);
            /* Progress */
//            canvas.drawRect(0, 0, (width * progress/100), height, paint);
//            canvas.save();

//            float length = overlayPaint.measureText("无聊图");
//            int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
//            canvas.drawText("无聊图", width / 2.0f - length/2.0f, baseline, overlayPaint);
//            Rect rect = new Rect(0, 0, (int)(width * progress/100), (int)height);
//            canvas.drawRect(rect, eraser);
        }
    }

    @Override
    public void onClick(View v) {
        selected = !selected;
        invalidate();
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }
}
