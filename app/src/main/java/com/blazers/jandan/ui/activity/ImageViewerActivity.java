package com.blazers.jandan.ui.activity;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Window;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.ui.activity.base.BaseActivity;
import com.blazers.jandan.views.widget.ZoomableDraweeView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;

/**
 * Created by Blazers on 2015/9/11.
 */
public class ImageViewerActivity extends BaseActivity {

    public static final String TRANSIT_PIC = "picture";

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.viewer) ZoomableDraweeView zoomableDraweeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        ButterKnife.bind(this);
        /* Init Toolbar */
//        initToolbarByType(layout_toolbar, ToolbarType.FINISH);
//        setToolbarTitle("Picture");
//        setContentFloatingModeEnabled(true);
//        hideNavigationBar(layout_toolbar);
        /* */
        ViewCompat.setTransitionName(zoomableDraweeView, TRANSIT_PIC);

        DraweeController ctrl = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(getIntent().getStringExtra("url")))
                .setTapToRetryEnabled(true)
                .setAutoPlayAnimations(true)
//                .setControllerListener(new FrescoControlListener(view)
                .build();
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .setProgressBarImage(new ProgressBarDrawable())
                .build();

        zoomableDraweeView.setController(ctrl);
        zoomableDraweeView.setHierarchy(hierarchy);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && Build.VERSION.SDK_INT>=21) {
            finishAfterTransition();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
