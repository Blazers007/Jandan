package com.blazers.jandan.ui.fragment.huaban;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.common.URL;
import com.blazers.jandan.network.ImageDownloader;
import com.blazers.jandan.network.HuabanParser;
import com.blazers.jandan.models.HuabanPin;
import com.blazers.jandan.util.RecyclerViewHelper;
import com.blazers.jandan.views.widget.DownloadFrescoView;
import com.blazers.jandan.views.widget.LoadMoreRecyclerView;
import com.blazers.jandan.views.widget.ThumbTextButton;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import io.realm.Realm;
import io.realm.RealmResults;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

import java.io.File;

/**
 * Created by Blazers on 2015/9/9.
 */
public class PinFragment extends Fragment {


    @Bind(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_list) LoadMoreRecyclerView recyclerList;
    @Bind(R.id.load_more_progress) SmoothProgressBar smoothProgressBar;

    private Realm realm;
    private RealmResults<HuabanPin> huabanPins;
    private int listSize;
    private PinAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_refresh_load, container, false);
        ButterKnife.bind(this, root);
        initRecyclerView();
        initHuabanPins();
        return root;
    }


    void initRecyclerView() {
        /* 从数据库中读取 有两个标志位标志当前的第一个跟最后一个 然后从数据库中读取  顺便发起请求Service更新数据库 */
        recyclerList.setLayoutManager(RecyclerViewHelper.getVerticalLinearLayoutManager(getActivity()));
        recyclerList.setItemAnimator(new FadeInUpAnimator());
        /* Loadmore */
        recyclerList.setLoadMoreListener(() -> {
            smoothProgressBar.setVisibility(View.VISIBLE);
            new AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... params) {
                    HuabanParser.getInstance().parseQueryPinAPI("妹子", 1);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    long last = huabanPins.last().getPin_id();
                    huabanPins = HuabanPin.loadMoreLessThan(realm, last);
                    listSize = huabanPins.size();
                    adapter.notifyDataSetChanged();
                    recyclerList.endLoading();
                    smoothProgressBar.setVisibility(View.GONE);
                    super.onPostExecute(aVoid);
                }
            }.execute();
        });

        /* Set Adapter */
        adapter = new PinAdapter();
        recyclerList.setAdapter(adapter);
        /* */
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FF9900"), Color.parseColor("#009900"), Color.parseColor("#000099"));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            /* 发起加载 加载后从数据库加载 然后显示 然后隐藏 */
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    HuabanParser.getInstance().parseQueryPinAPI("妹子", 1);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    huabanPins = HuabanPin.findAllSortDesc(realm);
                    listSize = huabanPins.size();
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    super.onPostExecute(aVoid);
                }
            }.execute();
        });
    }

    void initHuabanPins() {
        realm = Realm.getInstance(getActivity());
        huabanPins = HuabanPin.findAllSortDesc(realm);
        listSize = huabanPins.size();
        Log.e("SIZE", "= " + huabanPins.size());
        /* Update 需要整合 以及更智能的自动更新判断 */
        if (listSize == 0) {
            swipeRefreshLayout.setRefreshing(true);
            new AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... params) {
                    HuabanParser.getInstance().parseQueryPinAPI("妹子", 1);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    huabanPins = HuabanPin.findAllSortDesc(realm);
                    listSize = huabanPins.size();
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    super.onPostExecute(aVoid);
                }
            }.execute();
        }
    }

    @Override
    public void onDestroyView() {
        if (realm != null)
            realm.close();
        super.onDestroyView();
    }

    /* ImagePost Adapter */
    class PinAdapter extends RecyclerView.Adapter<PinAdapter.HuabanHolder>{

        private LayoutInflater inflater;

        public PinAdapter() {
            inflater = LayoutInflater.from(getActivity());
        }

        @Override
        public HuabanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.item_huaban, parent, false);
            return new HuabanHolder(v);
        }


        @Override
        public void onBindViewHolder(HuabanHolder meiziHolder, int i) {
            HuabanPin picture = huabanPins.get(i);
            meiziHolder.content.setAspectRatio(1.318f);
//            meiziHolder.content.showImage(URL.getHuabanPiCByQuality(picture.getFile_key(), URL.HUABAN_QULITY.FULL));
            meiziHolder.userHead.setImageURI(Uri.parse(URL.getHuabanPiCByQuality(picture.getUser_key(), URL.HUABAN_QULITY.SQ75W)));
            /* Update UI */
            if (picture.getLocalUrl() != null && !picture.getLocalUrl().equals(""))
                meiziHolder.save.setImageResource(R.mipmap.ic_publish_16dp);
            else
                meiziHolder.save.setImageResource(R.drawable.selector_download);

        }

        @Override
        public int getItemCount() {
            return listSize;
        }

        class HuabanHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            public SimpleDraweeView userHead;
            public DownloadFrescoView content;
            public TextView author, date, comment;
            public ThumbTextButton fav;
            public ImageButton save;

            public HuabanHolder(View itemView) {
                super(itemView);
                userHead = (SimpleDraweeView) itemView.findViewById(R.id.user_head);
                content = (DownloadFrescoView) itemView.findViewById(R.id.content);
                author = (TextView) itemView.findViewById(R.id.author);
                comment = (TextView) itemView.findViewById(R.id.comment);
                date = (TextView) itemView.findViewById(R.id.date);
                fav = (ThumbTextButton) itemView.findViewById(R.id.btn_fav);
                save = (ImageButton) itemView.findViewById(R.id.btn_save);
                /* 绑定点击事件 */
                content.setListener(new FrescoControlListener(content, save));
                save.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                ImageButton trigger = (ImageButton) view;
                int position = getAdapterPosition();
                HuabanPin picture = huabanPins.get(position);
                final String url = URL.getHuabanPiCByQuality(picture.getFile_key(), URL.HUABAN_QULITY.FULL);
                new AsyncTask<Void, Void, String>(){
                    @Override
                    protected String doInBackground(Void... params) {
                        return ImageDownloader.getInstance().doDownloading(url);
                    }

                    @Override
                    protected void onPostExecute(String path) {
                        trigger.setImageResource(R.mipmap.ic_publish_16dp);//TODO: 应当直接隐藏或者更换为以保存按钮
                        Realm realm = Realm.getInstance(getActivity());
                        realm.beginTransaction();   //No outside changes to a Realm is allowed while iterating a RealmResults. Use iterators methods instead.
                        picture.setLocalUrl(path);
                        realm.commitTransaction();
                        realm.close();
                        Snackbar.make(getView(), "保存完毕", Snackbar.LENGTH_SHORT).setAction("删除", v->{
                            new File(path).delete();
                            Realm realm2 = Realm.getInstance(getActivity());
                            realm2.beginTransaction();
                            picture.setLocalUrl("");
                            realm2.commitTransaction();
                            realm2.close();
                            Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                        }).show();
                    }
                }.execute();
            }
        }
    }

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
            if (asp <= 0.4) {draweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP);
                draweeView.getHierarchy().setActualImageFocusPoint(new PointF(0.5f, 0f));
                draweeView.setAspectRatio(1.118f);
            }
        }

        @Override
        public void onFailure(String s, Throwable throwable) {

        }

        @Override
        public void onRelease(String s) {
            Log.i("Release Image", s);
        }
    }
}
