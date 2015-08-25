package com.blazers.jandan;

import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Blazers on 2015/8/25.
 */
public class JandanApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
