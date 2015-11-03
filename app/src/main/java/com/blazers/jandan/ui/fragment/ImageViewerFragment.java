package com.blazers.jandan.ui.fragment;

import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.views.fresco.ZoomableDraweeView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Blazers on 15/8/27.
 */
public class ImageViewerFragment extends DialogFragment {

    public static final String TAG = ImageViewerFragment.class.getSimpleName();

    @Bind(R.id.viewer) ZoomableDraweeView view;
    private Uri uri;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        Log.i("URL", args.getString("url"));
        uri = Uri.parse(args.getString("url"));
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_image_viewer, container, false);
        ButterKnife.bind(this, root);
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
        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

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
