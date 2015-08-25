package com.blazers.jandan.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.orm.MeiziModel;
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

import java.net.URI;
import java.util.List;

/**
 * Created by Blazers on 2015/8/25.
 */
public class MeiziFragment extends Fragment {

    @Bind(R.id.swipte_container) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_list) RecyclerView meiziList;

    private Realm realm;
    private MeiziAdapter adapter;
    private RealmResults<MeiziModel> meiziModels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_meizi, container, false);
        ButterKnife.bind(this, root);
        initRecyclerView();
        return root;
    }

    void initRecyclerView() {
        realm = Realm.getInstance(getActivity());
        final RealmQuery<MeiziModel> query = realm.where(MeiziModel.class);
        meiziModels = query.findAll();
        realm.addChangeListener(()->{
            meiziModels = query.findAll();
            adapter.notifyDataSetChanged();
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        meiziList.setLayoutManager(linearLayoutManager);
        adapter = new MeiziAdapter();
        meiziList.setAdapter(adapter);
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
            meiziHolder.draweeView.setAspectRatio(1.33f);
            meiziHolder.draweeView.setImageURI(Uri.parse(meiziModels.get(i).getUrl()));
            DraweeController controller = Fresco.newDraweeControllerBuilder()
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
                            FLog.d("Final image received! " +
                                            "Size %d x %d",
                                    "Quality level %d, good enough: %s, full quality: %s",
                                    imageInfo.getWidth(),
                                    imageInfo.getHeight(),
                                    qualityInfo.getQuality(),
                                    qualityInfo.isOfGoodEnoughQuality(),
                                    qualityInfo.isOfFullQuality());
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
            return meiziModels.size();
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
