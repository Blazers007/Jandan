package com.blazers.jandan.network;

import android.content.Context;
import com.squareup.okhttp.OkHttpClient;
import io.realm.Realm;

/**
 * Created by Blazers on 2015/9/9.
 */
public abstract class HttpParser {
    /* INSTANCE */
    protected static Context mContext;
    protected static OkHttpClient mClient;
    protected Realm mRealm; //

    public static void init(Context context) {
        mContext = context;
        mClient = new OkHttpClient();
    }

}
