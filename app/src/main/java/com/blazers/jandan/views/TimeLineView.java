package com.blazers.jandan.views;

import android.content.Context;
import android.graphics.Canvas;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.blazers.jandan.R;
import com.blazers.jandan.models.db.local.LocalFavImages;
import com.blazers.jandan.models.pojo.timeline.Timeline;
import com.blazers.jandan.util.Dppx;
import com.blazers.jandan.util.TimeHelper;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Blazers on 2015/11/12.
 */
public class TimeLineView extends RelativeLayout {

    private LayoutInflater inflater;

    public TimeLineView(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_timeline, this, true);
    }

    public void setupViewsByTimeline(Timeline timeline) {
        ((TextView) findViewById(R.id.date)).setText(timeline.date);
        //auto stack
        AutoLayoutLinearLayout autoLayoutLinearLayout = (AutoLayoutLinearLayout) findViewById(R.id.auto);

        /* 宽度应该是固定的 */
        int w = (int)((Dppx.getScreenWidth(getContext()) - Dppx.Dp2Px(getContext(), 88)) / 3) - 30;

        if (null != timeline.favImagesList) {
            for (LocalFavImages image : timeline.favImagesList) {
                SimpleDraweeView simpleDraweeView = (SimpleDraweeView)inflater.inflate(R.layout.item_favorite_image, null);
                simpleDraweeView.setImageURI(Uri.parse(image.getUrl()));
                autoLayoutLinearLayout.addView(simpleDraweeView,w, w);
            }
        }
    }
}
