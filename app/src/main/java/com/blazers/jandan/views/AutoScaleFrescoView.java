package com.blazers.jandan.views;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.blazers.jandan.R;
import com.blazers.jandan.util.SPHelper;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
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
public class AutoScaleFrescoView extends SimpleDraweeView {

    public AutoScaleFrescoView(Context context) {
        super(context);
    }
    public AutoScaleFrescoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public AutoScaleFrescoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /* Vars */
    private boolean imageLoaded = false;
    private ImageView tag;

    public void showImage(ImageView tag, String url) {
        // 显示Tag
        this.tag = tag;
        if (url.substring(url.lastIndexOf(".") + 1).equals("gif"))
            tag.setImageResource(R.mipmap.ic_gif_corner_24dp);
        // 显示图片
        ImageRequest imageRequest = ImageRequest.fromUri(Uri.parse(url));
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setControllerListener(new FrescoControlListener())
                .setAutoPlayAnimations(false);
        setController(builder.build());
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                .setProgressBarImage(new ProgressBarDrawable())
                .build();
        setHierarchy(hierarchy);
    }

    class FrescoControlListener extends BaseControllerListener<ImageInfo> {

        @Override
        public void onFinalImageSet(String s, ImageInfo imageInfo, Animatable animatable) {
            if (imageInfo == null) {
                return;
            }
            int width = imageInfo.getWidth();
            int height = imageInfo.getHeight();
            // 判断大小 显示提示图片
            if (width > 2048 || height > 2048) {
                tag.setImageResource(R.mipmap.ic_more_corner_24dp);
            }
            // 控制硬件加速 以及宽高比
            if (width > 2048 || height > 2048)
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            else
                setLayerType(View.LAYER_TYPE_HARDWARE, null);
            float asp = (float)imageInfo.getWidth() / (float)(imageInfo.getHeight());
            setAspectRatio(asp);
            if (asp <= 0.4) {
                getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP);
                getHierarchy().setActualImageFocusPoint(new PointF(0.5f, 0f));
                setAspectRatio(1.118f);
            }
            // 加载完毕
            imageLoaded = true;
            // 是否自动播放
            if (SPHelper.getBooleanSP(getContext(), SPHelper.AUTO_GIF_MODE_ON, false) && animatable != null)
                animatable.start();
        }

        @Override
        public void onFailure(String s, Throwable throwable) {

        }

        @Override
        public void onRelease(String s) {
            Log.i("Release Image", s);
        }
    }

    public boolean isImageLoaded() {
        return imageLoaded;
    }
}
