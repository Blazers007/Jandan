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
        CrashHandler.getInstance().init(this);
        Fresco.initialize(this, ImagePipelineConfigFactory.getImagePipelineConfig(this));
        Parser.init(this);

//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
//        .build();
//        ImageLoader.getInstance().init(config);
    }

}
