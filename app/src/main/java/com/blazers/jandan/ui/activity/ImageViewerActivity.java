package com.blazers.jandan.ui.activity;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.ui.activity.base.BaseActivity;
import com.blazers.jandan.views.fresco.ZoomableDraweeView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;

public class ImageViewerActivity extends BaseActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.viewer) ZoomableDraweeView view;
    @Bind(R.id.btn_save) ImageButton btnSave;
    @Bind(R.id.content) TextView content;

    /* Vars */
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        ButterKnife.bind(this);
        initToolbarByTypeWithShadow(null, toolbar, ToolbarType.FINISH);
        initScalableImage();
    }

    void initScalableImage() {
        uri = Uri.parse(getIntent().getStringExtra("url"));
        DraweeController ctrl = Fresco.newDraweeControllerBuilder()
            .setUri(uri)
            .setTapToRetryEnabled(true)
            .setAutoPlayAnimations(true)
            .setControllerListener(new FrescoControlListener())
            .build();
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
            .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
            .setProgressBarImage(new ProgressBarDrawable())
            .build();

        view.setController(ctrl);
        view.setHierarchy(hierarchy);
    }


    /**
     * Listener
     * */
    class FrescoControlListener extends BaseControllerListener<ImageInfo> {

        @Override
        public void onFinalImageSet(String s, ImageInfo imageInfo, Animatable animatable) {
            if (imageInfo == null) {
                return;
            }
            if (imageInfo.getWidth() > 2048 || imageInfo.getHeight() > 2048)
                view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            else
                view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        @Override
        public void onFailure(String s, Throwable throwable) {

        }

        @Override
        public void onRelease(String s) {
            Log.i("Release Image", s);
        }
    }
}
