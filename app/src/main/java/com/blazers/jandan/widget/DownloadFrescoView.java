package com.blazers.jandan.widget;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import com.blazers.jandan.ui.fragment.ImageViewerFragment;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
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

    public void showImage(String url) {
        this.url = url;
        /* Init vars prefer to load from local storage */
        ImageRequest imageRequest = ImageRequest.fromUri(Uri.parse(url));
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
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
    }
}
