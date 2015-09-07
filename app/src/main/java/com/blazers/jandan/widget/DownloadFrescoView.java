package com.blazers.jandan.widget;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import com.blazers.jandan.R;
import com.blazers.jandan.orm.meizi.Picture;
import com.blazers.jandan.network.ImageDownloader;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import io.realm.Realm;

import java.io.*;


/**
 * Created by Blazers on 2015/8/28.
 */
public class DownloadFrescoView extends SimpleDraweeView implements View.OnClickListener {

    /* Stupid ways trying to update below */
    private DownloadFrescoView self = this;
    private Context context = getContext();

    /* Vars */
    private ImageRequest imageRequest;
    private Picture picture;
    private boolean loaded, downloaded;
    /* Vars for trigger */
    private ImageButton trigger;

    public DownloadFrescoView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public DownloadFrescoView(Context context) {
        super(context);
    }

    public DownloadFrescoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DownloadFrescoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void showImage(Picture picture) {
        /* Init vars prefer to load from local storage */
        this.picture = picture;
        if (picture.getLocalUrl() != null && !picture.getLocalUrl().equals("")) {
            imageRequest = ImageRequest.fromUri(Uri.parse("file://" + (picture.getLocalUrl())));
            showImage();
        } else {
            imageRequest = ImageRequest.fromUri(Uri.parse(picture.getUrl()));
            showImage();
            setOnClickListener(this);
        }
    }

    private void showImage() {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setAutoPlayAnimations(true)
                .setControllerListener(new FrescoControlListener(self))
                .build();
        self.setController(controller);
    }

    public void setDownloadHandlder(ImageButton trigger) {
        setOnClickListener(null);
        this.trigger = trigger;
        trigger.setClickable(true);
        trigger.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (loaded)
            return;
        if (downloaded) {
            Toast.makeText(context, "图片已经保存成功", Toast.LENGTH_SHORT).show();
            return;
        }
        final String url = picture.getUrl();
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... params) {
                return ImageDownloader.getInstance().doDownloading(url);
            }

            @Override
            protected void onPostExecute(String path) {
                trigger.setImageResource(R.mipmap.ic_publish_16dp);//TODO: 应当直接隐藏或者更换为以保存按钮
                Realm realm = Realm.getInstance(context);
                realm.beginTransaction();   //No outside changes to a Realm is allowed while iterating a RealmResults. Use iterators methods instead.
                    picture.setLocalUrl(path);
                realm.commitTransaction();
                realm.close();
                downloaded = true;
                Snackbar.make(self, "保存完毕", Snackbar.LENGTH_SHORT).setAction("删除", v->{
                    new File(path).delete();
                    Realm realm2 = Realm.getInstance(context);
                    realm2.beginTransaction();
                        picture.setLocalUrl("");
                    realm2.commitTransaction();
                    realm2.close();
                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                    downloaded = false;
                }).show();
            }
        }.execute();
    }

    class FrescoControlListener extends BaseControllerListener<ImageInfo> {

        private SimpleDraweeView draweeView;

        public FrescoControlListener(SimpleDraweeView draweeView) {
            this.draweeView = draweeView;
        }

        @Override
        public void onFinalImageSet(String s, ImageInfo imageInfo, Animatable animatable) {
            if (imageInfo == null) {
                return;
            }
            // 加载完毕 可以下载
            loaded = true;
            trigger.setVisibility(VISIBLE);
            float asp = (float)imageInfo.getWidth() / (float)(imageInfo.getHeight());
            if (asp >= 0.818) {
                draweeView.setAspectRatio(asp);
            } else if (asp <= 0.4) {
                draweeView.setAspectRatio(0.818f);
                draweeView.setScaleType(ScaleType.FIT_START);
            } else {
                draweeView.setAspectRatio(0.818f);
            }
        }

        @Override
        public void onFailure(String s, Throwable throwable) {
            if (!picture.getLocalUrl().equals("")) { // 从本地加载图片失败 删除数据库缓存地址
                Realm realm = Realm.getInstance(context);
                realm.beginTransaction();
                    picture.setLocalUrl("");
                realm.commitTransaction();
                realm.close();
                /* 重新从网络加载图片 此时 localUrl已经被赋值为空 */
                showImage(picture);
            } else { // 从网络加载图片失败 检查网络连接

            }
        }

        @Override
        public void onRelease(String s) {
            Log.i("Release Image", s);
        }
    }
}
