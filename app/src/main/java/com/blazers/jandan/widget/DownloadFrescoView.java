package com.blazers.jandan.widget;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import com.blazers.jandan.orm.Picture;
import com.blazers.jandan.util.network.ImageDownloader;
import com.blazers.jandan.util.sdcard.SdcardHelper;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.internal.Closeables;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imageformat.ImageFormatChecker;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.memory.PooledByteBuffer;
import com.facebook.imagepipeline.memory.PooledByteBufferInputStream;
import com.facebook.imagepipeline.request.ImageRequest;
import io.realm.Realm;
import java.io.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Blazers on 2015/8/28.
 */
public class DownloadFrescoView extends SimpleDraweeView {

    private DownloadFrescoView self = this;
    private Context context = getContext();
    /* Image Request */
    private ImageRequest imageRequest;

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

    public void showImageWeb(String url) {
        imageRequest = ImageRequest.fromUri(Uri.parse(url));
        showImage();
    }

    public void showImageLocal(String path) {
        imageRequest = ImageRequest.fromUri(Uri.parse("file://" + path));
        showImage();
    }

    private void showImage() {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setAutoPlayAnimations(true)
                .setControllerListener(new FrescoControllListener(self))
                .build();
        self.setController(controller);
    }

    public void saveFileToSdcard() {
        /* 订阅事件 */
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<PooledByteBuffer>> dataSource = imagePipeline.fetchEncodedImage(imageRequest, context.getApplicationContext());
        DataSubscriber<CloseableReference<PooledByteBuffer>> dataSubscriber = new BaseDataSubscriber<CloseableReference<PooledByteBuffer>>() {
            @Override
            protected void onNewResultImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                if (!dataSource.isFinished())
                    return;
                CloseableReference<PooledByteBuffer> buffRef = dataSource.getResult();
                if (buffRef != null) {
                    PooledByteBufferInputStream is = new PooledByteBufferInputStream(buffRef.get());
                    try {
                        ImageFormat imageFormat = ImageFormatChecker.getImageFormat(is);
                        new AsyncTask<Void, Void, Boolean>(){
                            @Override
                            protected Boolean doInBackground(Void... params) {
                                try {
                                    File file = SdcardHelper.createImageFile(imageFormat.name().toLowerCase());
                                    FileOutputStream fos;
                                    fos = new FileOutputStream(file);
                                    byte[] buffer = new byte[1024*10];
                                    while(true){
                                        int length = is.read(buffer);
                                        if (length == -1)
                                            break;
                                        fos.write(buffer, 0, length);
                                    }
                                    fos.close();
                                    return true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return false;
                            }

                            @Override
                            protected void onPostExecute(Boolean result) {

                            }
                        }.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        dataSource.close();
                        Closeables.closeQuietly(is);
                        CloseableReference.closeSafely(buffRef);
                    }
                }
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                dataSource.close();
            }
        };
        dataSource.subscribe(dataSubscriber, CallerThreadExecutor.getInstance());
    }

    class FrescoControllListener implements ControllerListener<ImageInfo> {

        private SimpleDraweeView draweeView;

        public FrescoControllListener(SimpleDraweeView draweeView) {
            this.draweeView = draweeView;
        }

        @Override
        public void onSubmit(String s, Object o) {

        }

        @Override
        public void onFinalImageSet(String s, ImageInfo imageInfo, Animatable animatable) {
            if (imageInfo == null) {
                return;
            }
             /* Resizing the item */
            if(imageInfo.getWidth() > imageInfo.getHeight()) {
                float asp = (float)imageInfo.getWidth() / (float)(imageInfo.getHeight());
                draweeView.setAspectRatio(asp);
            }
        }

        @Override
        public void onIntermediateImageSet(String s, ImageInfo imageInfo) {

        }

        @Override
        public void onIntermediateImageFailed(String s, Throwable throwable) {

        }

        @Override
        public void onFailure(String s, Throwable throwable) {
            /*  TODO:重新下载  */
        }

        @Override
        public void onRelease(String s) {

        }
    }
}
