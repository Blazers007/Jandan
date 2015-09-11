package com.blazers.jandan.ui.fragment.jandan;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.network.JandanParser;
import com.blazers.jandan.models.jandan.Image;
import com.blazers.jandan.ui.fragment.app.BaseFragment;
import com.blazers.jandan.util.RecyclerViewHelper;
import com.blazers.jandan.views.adapters.JandanImageAdapter;
import com.blazers.jandan.views.widget.DownloadFrescoView;
import com.blazers.jandan.views.widget.LoadMoreRecyclerView;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.imagepipeline.image.ImageInfo;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import io.realm.Realm;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

import java.util.ArrayList;

/**
 * Created by Blazers on 15/9/8.
 */
public class PicFragment extends BaseFragment {

    @Bind(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_list) LoadMoreRecyclerView meiziList;
    @Bind(R.id.load_more_progress) SmoothProgressBar smoothProgressBar;

    private JandanImageAdapter mAdapter;
    private ArrayList<Image> mImageArrayList = new ArrayList<>();
    private int mPage = 1;

    public PicFragment() {
        super();
        setTAG("Pic Fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("Pic Fragment", "OnCreateView");
        View root = inflater.inflate(R.layout.fragment_refresh_load, container, false);
        ButterKnife.bind(this, root);
        initRecyclerView();
        return root;
    }



    /**
     * 从现有的数据库中读取 若没有数据库(首次进入)则建立数据库
     * 保存 首/尾 标志位ID
     * 并随后调用一次刷新 刷新后对比 若最新ID比当前ID大则更新
     * */
    void initRecyclerView() {
        /* 从数据库中读取 有两个标志位标志当前的第一个跟最后一个 然后从数据库中读取  顺便发起请求Service更新数据库 */
        meiziList.setLayoutManager(RecyclerViewHelper.getVerticalLinearLayoutManager(getActivity()));
        meiziList.setItemAnimator(new FadeInUpAnimator());
        mAdapter = new JandanImageAdapter(getActivity(), mImageArrayList);
        meiziList.setAdapter(mAdapter);

        /* Loadmore */
        meiziList.setLoadMoreListener(() -> {
            smoothProgressBar.setVisibility(View.VISIBLE);
            getData(false, ++mPage, "wuliao", mImageArrayList, mAdapter);
        });
        /* Set Adapter */
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FF9900"), Color.parseColor("#009900"), Color.parseColor("#000099"));
        swipeRefreshLayout.setOnRefreshListener(() -> {

        });

        getData(true, mPage, "wuliao", mImageArrayList, mAdapter);
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /* ImagePosts Adapter */

    class FrescoControlListener extends BaseControllerListener<ImageInfo> {

        private DownloadFrescoView draweeView;
        private View trigger;

        public FrescoControlListener(DownloadFrescoView draweeView, View trigger) {
            this.draweeView = draweeView;
            this.trigger = trigger;
        }

        @Override
        public void onFinalImageSet(String s, ImageInfo imageInfo, Animatable animatable) {
            if (imageInfo == null) {
                return;
            }
            // 加载完毕 可以下载
            trigger.setVisibility(View.VISIBLE);
            float asp = (float)imageInfo.getWidth() / (float)(imageInfo.getHeight());
            draweeView.setAspectRatio(asp);
            if (asp <= 0.4) {
                draweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP);
                draweeView.getHierarchy().setActualImageFocusPoint(new PointF(0.5f, 0f));
                draweeView.setAspectRatio(1.118f);
            }

        }

        @Override
        public void onFailure(String s, Throwable throwable) {
//            Image picture = draweeView.getPicture();
//            if (!picture.getLocalUrl().equals("")) { // 从本地加载图片失败 删除数据库缓存地址
//                Realm realm = Realm.getInstance(getActivity());
//                realm.beginTransaction();
//                picture.setLocalUrl("");
//                realm.commitTransaction();
//                realm.close();
//                /* 重新从网络加载图片 此时 localUrl已经被赋值为空 */
//                draweeView.showImage(picture);
//            } else { // 从网络加载图片失败 检查网络连接
//
//            }
        }

        @Override
        public void onRelease(String s) {
            Log.i("Release Image", s);
        }
    }


}
