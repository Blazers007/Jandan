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
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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

//    @Bind(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;
//    @Bind(R.id.recycler_list) RecyclerView meiziList;

    @Bind(R.id.tab_layout) TabLayout tabLayout;
    @Bind(R.id.view_pager) ViewPager viewPager;

    private ArrayList<View> pages;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_meizi, container, false);
        ButterKnife.bind(this, root);
        initViewPager();
        return root;
    }

    /**
     * 从现有的数据库中读取 若没有数据库(首次进入)则建立数据库
     * 保存 首/尾 标志位ID
     * 并随后调用一次刷新 刷新后对比 若最新ID比当前ID大则更新
     * */


//    void initRecyclerView() {
//        /* 从数据库中读取 有两个标志位标志当前的第一个跟最后一个 然后从数据库中读取  顺便发起请求Service更新数据库 */
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        meiziList.setLayoutManager(linearLayoutManager);
//
//        adapter = new MeiziAdapter();
//        meiziList.setAdapter(adapter);
//
//        /* */
//        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FF9900"), Color.parseColor("#009900"), Color.parseColor("#000099"));
//        swipeRefreshLayout.setOnRefreshListener(() -> {
//            /* 发起加载 加载后从数据库加载 然后显示 然后隐藏 */
//
//        });
//    }

    void initViewPager() {
        viewPager.setAdapter(new MeiziPager());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == viewPager.getAdapter().getCount() - 1) {
//                    pages.add(initOnePage(position+1));
//                    viewPager.getAdapter().notifyDataSetChanged();
//                    Snackbar.make(getView(), "ADD", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    class MeiziPager extends PagerAdapter {

        public MeiziPager() {
            pages = new ArrayList<>();
            pages.add(initOnePage(0));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public int getCount() {
            return pages.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = pages.get(position);
            container.addView(v);
            return v;
        }
    }

    private View initOnePage(int page) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.pager_image_recycler, null);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new MeiziAdapter(page));
        return v;
    }

    /* Meizi Adapter */
    class MeiziAdapter extends RecyclerView.Adapter<MeiziAdapter.MeiziHolder>{

        private LayoutInflater inflater;
        private ArrayList<MeiziModel> meiziModels = new ArrayList<>();
        private MeiziAdapter adapter;

        public MeiziAdapter(int page) {
            inflater = LayoutInflater.from(getActivity());
            adapter = this;
            new AsyncTask<Void,Void,ArrayList<MeiziModel>>(){
                @Override
                protected ArrayList<MeiziModel> doInBackground(Void... params) {
                    return page == 0 ? MeiziParser.parse() : MeiziParser.parse(page);
                }
                @Override
                protected void onPostExecute(ArrayList<MeiziModel> meizi) {
                    meiziModels = meizi;
                    adapter.notifyDataSetChanged();
                    super.onPostExecute(meizi);
                }
            }.execute();
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
                            Log.i(TAG, ""+imageInfo.getWidth() + "  " +
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
