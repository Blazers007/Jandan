package com.blazers.jandan.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.widget.TextView;

/**
 * Created by Blazers on 2015/9/9.
 */
public class FontFace {

    public static Typeface getTypeface(Context ctx,String assetFile){
        AssetManager mgr = ctx.getAssets();//得到AssetManager
        return Typeface.createFromAsset(mgr,assetFile);//根据路径得到Typeface
    }

    public static void setTypeface(@NonNull Context ctx,String assetFile,TextView ... textViews){
        AssetManager mgr = ctx.getAssets();//得到AssetManager
        Typeface tf = Typeface.createFromAsset(mgr, assetFile);
        for(TextView t : textViews){
            t.setTypeface(tf);
        }
    }
}
