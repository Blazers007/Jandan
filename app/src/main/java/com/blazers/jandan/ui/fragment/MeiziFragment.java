package com.blazers.jandan.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.orm.MeiziModel;
import com.blazers.jandan.util.network.MeiziParser;
import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Blazers on 2015/8/25.
 */
public class MeiziFragment extends Fragment {

    public static final String TAG = MeiziFragment.class.getSimpleName();

    @Bind(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_list) RecyclerView meiziList;

    private Realm realm;
    private MeiziAdapter adapter;
    private ArrayList<MeiziModel> meiziModels;
//    private JSONArray meiziArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_meizi, container, false);
        ButterKnife.bind(this, root);
        initRecyclerView();
        initMeiziPics();
        return root;
    }

    /**
     * 从现有的数据库中读取 若没有数据库(首次进入)则建立数据库
     * 保存 首/尾 标志位ID
     * 并随后调用一次刷新 刷新后对比 若最新ID比当前ID大则更新
     * */


    void initRecyclerView() {
        /* 从数据库中读取 有两个标志位标志当前的第一个跟最后一个 然后从数据库中读取  顺便发起请求Service更新数据库 */
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        meiziList.setLayoutManager(linearLayoutManager);

        adapter = new MeiziAdapter();
        meiziList.setAdapter(adapter);

        /* */
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FF9900"), Color.parseColor("#009900"), Color.parseColor("#000099"));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            /* 发起加载 加载后从数据库加载 然后显示 然后隐藏 */

        });
    }

    void initMeiziPics() {
//        realm = Realm.getInstance(getActivity());
//        RealmQuery<MeiziModel> query = realm.where(MeiziModel.class);
//        meiziModels = query.findAll();
        /**/
        new AsyncTask<Void,Void,ArrayList<MeiziModel>>(){

            @Override
            protected ArrayList<MeiziModel> doInBackground(Void... params) {
                return MeiziParser.parse();
            }

            @Override
            protected void onPostExecute(ArrayList<MeiziModel> meizi) {
                meiziModels = meizi;
                adapter.notifyDataSetChanged();
                super.onPostExecute(meiziModels);
            }
        }.execute();
    }

    @Override
    public void onDestroyView() {
        if (realm != null)
            realm.close();
        super.onDestroyView();
    }

    //     gengxin duibi xianyou

    /* Meizi Adapter */
    class MeiziAdapter extends RecyclerView.Adapter<MeiziAdapter.MeiziHolder>{

        private LayoutInflater inflater;

        public MeiziAdapter() {
            inflater = LayoutInflater.from(getActivity());
        }

        @Override
        public MeiziHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.item_image, parent, false);
            return new MeiziHolder(v);
        }


        @Override
        public void onBindViewHolder(MeiziHolder meiziHolder, int i) {
            meiziHolder.draweeView.setAspectRatio(0.618f);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(meiziModels.get(i).getUrl()))
                    .setControllerListener(new ControllerListener<ImageInfo>() {
                        @Override
                        public void onSubmit(String s, Object o) {

                        }

                        @Override
                        public void onFinalImageSet(String s, ImageInfo imageInfo, Animatable animatable) {
                            if (imageInfo == null) {
                                return;
                            }
                            QualityInfo qualityInfo = imageInfo.getQualityInfo();
                            Log.i(TAG, ""+imageInfo.getWidth() +
                                    imageInfo.getHeight() + "  " +
                                    qualityInfo.getQuality());
                        }

                        @Override
                        public void onIntermediateImageSet(String s, ImageInfo imageInfo) {

                        }

                        @Override
                        public void onIntermediateImageFailed(String s, Throwable throwable) {

                        }

                        @Override
                        public void onFailure(String s, Throwable throwable) {

                        }

                        @Override
                        public void onRelease(String s) {

                        }
                    })
                    .build();
            meiziHolder.draweeView.setController(controller);
        }

        @Override
        public int getItemCount() {
            return meiziModels == null ? 0 : meiziModels.size();
        }

        class MeiziHolder extends RecyclerView.ViewHolder {

            public SimpleDraweeView draweeView;

            public MeiziHolder(View itemView) {
                super(itemView);
                draweeView = (SimpleDraweeView) itemView.findViewById(R.id.drweeView);
            }
        }
    }
}
