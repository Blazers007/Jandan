package com.blazers.jandan;

import android.app.Application;
import com.blazers.jandan.network.Parser;
import com.blazers.jandan.util.ImagePipelineConfigFactory;
import com.facebook.drawee.backends.pipeline.Fresco;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Blazers on 2015/8/25.
 */
public class JandanApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化CrashHandler
//        CrashHandler.getInstance().init(this);
        // 初始化Fresco
        Fresco.initialize(this, ImagePipelineConfigFactory.getOkHttpImagePipelineConfig(this));
        // 初始化OKHTTP解析
        Parser.init(this);
    }

}
