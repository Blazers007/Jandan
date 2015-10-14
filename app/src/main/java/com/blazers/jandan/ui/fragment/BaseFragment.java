package com.blazers.jandan.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.blazers.jandan.models.jandan.Image;
import com.blazers.jandan.network.Parser;
import com.blazers.jandan.ui.adapters.JandanImageAdapter;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blazers on 2015/9/11.
 */
public class BaseFragment extends Fragment {

    private String TAG = "BaseFragment";

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public BaseFragment() {
        super();
        Log.i(TAG, "Constructor");
    }

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "Attach");
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "OnCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "OnActivityCreated");
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        Log.i(TAG, "OnStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "OnResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "OnPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "OnStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "OnDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "OnDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "OnDetach");
    }
}
