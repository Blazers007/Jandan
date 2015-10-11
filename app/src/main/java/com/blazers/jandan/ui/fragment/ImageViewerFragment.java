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
import com.blazers.jandan.views.widget.ZoomableDraweeView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;

/**
 * Created by Blazers on 15/8/27.
 */
public class ImageViewerFragment extends DialogFragment {

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
                .build();
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .setProgressBarImage(new ProgressBarDrawable())
                .build();

        view.setController(ctrl);
        view.setHierarchy(hierarchy);
        return root;
    }

    class FrescoControlListener extends BaseControllerListener<ImageInfo> {

        private ZoomableDraweeView draweeView;

        public FrescoControlListener(ZoomableDraweeView draweeView) {
            this.draweeView = draweeView;
        }

        @Override
        public void onFinalImageSet(String s, ImageInfo imageInfo, Animatable animatable) {
            if (imageInfo == null) {
                return;
            }
            if (imageInfo.getWidth() > 2048 || imageInfo.getHeight() > 2048)
                view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            else
                view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            float asp = (float)imageInfo.getWidth() / (float)(imageInfo.getHeight());
            if (asp <= 0.4) {
                draweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP);
                draweeView.getHierarchy().setActualImageFocusPoint(new PointF(0.5f, 0f));
            }
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
