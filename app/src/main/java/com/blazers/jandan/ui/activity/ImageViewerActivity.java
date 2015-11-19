package com.blazers.jandan.ui.activity;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.models.db.local.LocalImage;
import com.blazers.jandan.network.ImageDownloader;
import com.blazers.jandan.rxbus.event.ViewImageEvent;
import com.blazers.jandan.ui.activity.base.BaseActivity;
import com.blazers.jandan.util.DBHelper;
import com.blazers.jandan.util.RxHelper;
import com.blazers.jandan.util.SdcardHelper;
import com.blazers.jandan.views.fresco.ZoomableDraweeView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;
import rx.Observable;

public class ImageViewerActivity extends BaseActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.viewer) ZoomableDraweeView view;
    @Bind(R.id.btn_save) ImageButton btnSave;
    @Bind(R.id.content) TextView content;
    @Bind(R.id.image_view_extra) RelativeLayout extras;

    /* Vars */
    private boolean uiIsShowing = true;
    private ViewImageEvent event;
    private boolean downloaded = false, downloading = false;

    private long touchStartTime;
    private float touchStartX, touchStartY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        ButterKnife.bind(this);
        initToolbarByTypeWithShadow(null, toolbar, ToolbarType.FINISH);
        setContentFloatingModeEnabled(true);
        setToolbarTitle("");
        extras.setPadding(0, 0, 0, getNavigationBarHeight());
        toggleUI();
        // 判断数值是否正确送入
        event = (ViewImageEvent)getIntent().getSerializableExtra(ViewImageEvent.KEY);
        if (null == event){
            return;
        }
        initScalableImage();
        initExtra();
    }

    void initScalableImage() {
        // 加载图片 首先判断本地是否有
        LocalImage localImage = realm.where(LocalImage.class).equalTo("url", event.originUrl).findFirst();
        String uri;
        if (localImage != null && SdcardHelper.isThisFileExist(localImage.getLocalUrl())) {
            uri = "file://" + localImage.getLocalUrl();
            downloaded = true;
        } else {
            uri = event.originUrl;
        }
        // 显示图片
        DraweeController ctrl = Fresco.newDraweeControllerBuilder()
            .setUri(Uri.parse(uri))
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
        // 触摸
        view.setOnTouchListener((v, event)->{
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    if (event.getPointerCount() == 1) {
                        touchStartTime = System.currentTimeMillis();
                        touchStartX = event.getX();
                        touchStartY = event.getY();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (System.currentTimeMillis() - touchStartTime < 400
                        && event.getPointerCount() == 1
                        && Math.abs(Math.pow(touchStartX-event.getX(), 2) + Math.pow(touchStartY-event.getY(), 2)) < 200) {
                        toggleUI();
                    }
                    break;
            }
            return false;
        });
    }

    /**
     * 初始化下方额外内容区域
     * */
    void initExtra() {
        // 内容
        content.setText(event.contentStr);
        /* 判断是否已经下载 */
        if (downloaded) {
            btnSave.setClickable(false);
            btnSave.setImageResource(R.mipmap.ic_publish_16dp);
        } else {
            btnSave.setOnClickListener(v->{
                if (downloading)
                    return;
                downloading = true;
                Observable.just(event.originUrl)
                    .map(ImageDownloader.getInstance()::doSavingImage)
                    .compose(RxHelper.applySchedulers())
                    .subscribe(localImage -> {
                        Toast.makeText(this, "图片保存成功", Toast.LENGTH_SHORT).show();
                        btnSave.setImageResource(R.mipmap.ic_publish_16dp);
                        btnSave.setClickable(false);
                        DBHelper.saveToRealm(realm, localImage);
                    }, throwable -> {
                        downloading = false;
                        Log.e("Error", throwable.toString());
                    });
            });
        }
    }

    /**
     * 隐藏 / 全屏
     * */
    public void toggleUI(){
        Log.e("C","C");
        if (uiIsShowing) {
            // hide
            hideSystemUI(toolbar);
            extras.animate().translationY(extras.getMeasuredHeight()).setDuration(400).setStartDelay(200).start();
            uiIsShowing = false;
        } else {
            // show
            showSystemUI(toolbar);
            extras.animate().translationY(0).setDuration(400).setStartDelay(200).start();
            uiIsShowing = true;
        }
    }



    /**
     * 控制硬件加速开启与否
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


    /**
     * Toolbar Menu
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
