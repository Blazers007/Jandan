package com.blazers.jandan.widget;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import com.blazers.jandan.R;
import com.blazers.jandan.orm.PictureInterface;
import com.blazers.jandan.orm.meizi.Picture;
import com.blazers.jandan.network.ImageDownloader;
import com.blazers.jandan.ui.fragment.ImageViewerFragment;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import io.realm.Realm;

import java.io.*;


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

    private PictureInterface picture;

    public void showImage(PictureInterface picture) {
        this.picture = picture;
        /* Init vars prefer to load from local storage */
        ImageRequest imageRequest;
        if (picture.getLocalUrl() != null && !picture.getLocalUrl().equals(""))
            imageRequest = ImageRequest.fromUri(Uri.parse("file://" + (picture.getLocalUrl())));
        else
            imageRequest = ImageRequest.fromUri(Uri.parse(picture.getUrl()));
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
        args.putString("url", picture.getUrl());
        DialogFragment fragment = new ImageViewerFragment();
        fragment.setArguments(args);
        fragment.show(((AppCompatActivity)getContext()).getSupportFragmentManager(), "tag");
    }

    public PictureInterface getPicture() {
        return picture;
    }
}
