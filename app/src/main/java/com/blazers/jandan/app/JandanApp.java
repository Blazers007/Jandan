package com.blazers.jandan.app;

import android.app.Application;

import com.blazers.jandan.network.Parser;
import com.blazers.jandan.util.LoggintInterceptor;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import jonathanfinerty.once.Once;
import okhttp3.OkHttpClient;

/**
 * Created by Blazers on 2015/8/25.
 */
public class JandanApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Realm
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .build();
        Realm.setDefaultConfiguration(config);
        // 初始化CrashHandler
//        CrashHandler.getInstance().init(this);
        // 初始化Fresco
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggintInterceptor())
                .build();
        ImagePipelineConfig imagePipelineConfig = OkHttpImagePipelineConfigFactory
                .newBuilder(this, okHttpClient)
                .build();
        Fresco.initialize(this, imagePipelineConfig);
        // 初始化OKHTTP解析
        Parser.init(this);
        // Once
        Once.initialise(this);
    }
}
