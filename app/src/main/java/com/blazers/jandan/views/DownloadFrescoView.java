package com.blazers.jandan.views;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.blazers.jandan.ui.fragment.ImageViewerFragment;
import com.blazers.jandan.ui.adapters.JandanImageAdapter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;


/**
 * Created by Blazers on 2015/8/28.
 */
public class DownloadFrescoView extends SimpleDraweeView implements View.OnClickListener{

    /* Vars */
    private ControllerListener<ImageInfo> listener;

    private JandanImageAdapter.ImageInfo imageInfoListener;

    public DownloadFrescoView(Context context) {
        super(context);
    }

    public DownloadFrescoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DownloadFrescoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setListener(ControllerListener listener) {
        this.listener = listener;
    }

    public String url;

    public void showImage(String url, View trigger) {
        this.url = url;
        /* Init vars prefer to load from local storage */
        ImageRequest imageRequest = ImageRequest.fromUri(Uri.parse(url));
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setControllerListener(new FrescoControlListener(this, trigger))
                .setAutoPlayAnimations(false);
        if (listener != null)
            builder.setControllerListener(listener);
        setController(builder.build());
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                .setProgressBarImage(new ProgressBarDrawable())
                .build();
        setHierarchy(hierarchy);
        setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Bundle args = new Bundle();
        args.putString("url", url);
        DialogFragment fragment = new ImageViewerFragment();
        fragment.setArguments(args);
        fragment.show(((AppCompatActivity)getContext()).getSupportFragmentManager(), "tag");
//        Intent intent = new Intent(getContext(), ImageViewerActivity.class);
//        intent.putExtra("url", url);
//        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                (MainActivity)getContext(), this, ImageViewerActivity.TRANSIT_PIC);
//        ActivityCompat.startActivity((MainActivity)getContext(), intent, optionsCompat.toBundle());
    }

    public void setImageInfoListener (JandanImageAdapter.ImageInfo listener) {
        this.imageInfoListener = listener;
    }

    class FrescoControlListener extends BaseControllerListener<ImageInfo> {

        private DownloadFrescoView draweeView;
        private View trigger;

        public FrescoControlListener(DownloadFrescoView draweeView, View trigger) {
            this.draweeView = draweeView;
            this.trigger = trigger;
        }

        @Override
        public void onFinalImageSet(String s, ImageInfo imageInfo, Animatable animatable) {
            if (imageInfo == null) {
                return;
            }
            if (imageInfoListener != null)
                imageInfoListener.onLoaded(imageInfo.getWidth(), imageInfo.getHeight());
            if (imageInfo.getWidth() > 2048 || imageInfo.getHeight() > 2048)
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            else
                setLayerType(View.LAYER_TYPE_HARDWARE, null);
            // 加载完毕 可以下载
            trigger.setVisibility(View.VISIBLE);
            float asp = (float)imageInfo.getWidth() / (float)(imageInfo.getHeight());
            draweeView.setAspectRatio(asp);
            if (asp <= 0.4) {
                draweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP);
                draweeView.getHierarchy().setActualImageFocusPoint(new PointF(0.5f, 0f));
                draweeView.setAspectRatio(1.118f);
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
