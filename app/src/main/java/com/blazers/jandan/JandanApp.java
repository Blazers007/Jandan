package com.blazers.jandan;

import android.app.Application;
import com.blazers.jandan.network.JandanParser;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Blazers on 2015/8/25.
 */
public class JandanApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
        Fresco.initialize(this);
        JandanParser.init(this);

    }
}
