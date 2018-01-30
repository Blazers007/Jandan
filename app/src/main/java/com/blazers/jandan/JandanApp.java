package com.blazers.jandan;

import android.app.Application;

import com.blazers.jandan.util.LoggintInterceptor;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import jonathanfinerty.once.Once;
import okhttp3.OkHttpClient;
import timber.log.Timber;

/**
 * Created by Blazers on 2015/8/25.
 * 程序的main application
 */
public class JandanApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化Fresco
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggintInterceptor())
                .build();
        ImagePipelineConfig imagePipelineConfig = OkHttpImagePipelineConfigFactory
                .newBuilder(this, okHttpClient)
                .build();
        Fresco.initialize(this, imagePipelineConfig);
        // 初始化Once
        Once.initialise(this);
        // Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
