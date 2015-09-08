package com.blazers.jandan.ui.fragment;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.network.ImageDownloader;
import com.blazers.jandan.network.JandanParser;
import com.blazers.jandan.orm.meizi.Picture;
import com.blazers.jandan.util.RecyclerViewHelper;
import com.blazers.jandan.widget.DownloadFrescoView;
import com.blazers.jandan.widget.LoadMoreRecyclerView;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.imagepipeline.image.ImageInfo;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import io.realm.Realm;
import io.realm.RealmResults;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

import java.io.File;

/**
 * Created by Blazers on 15/9/8.
 */
public class PicFragment extends Fragment {

    @Bind(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_list) LoadMoreRecyclerView meiziList;
    @Bind(R.id.load_more_progress) SmoothProgressBar smoothProgressBar;

    private Realm realm;
    private MeiziAdapter adapter;
    private RealmResults<Picture> meiziPics;
    private int listSize;

    /* Beta */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_refresh_load, container, false);
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
        meiziList.setLayoutManager(RecyclerViewHelper.getVerticalLinearLayoutManager(getActivity()));
        meiziList.setItemAnimator(new FadeInUpAnimator());
        /* Loadmore */
        meiziList.setLoadMoreListener(() -> {
            smoothProgressBar.setVisibility(View.VISIBLE);
            new AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... params) {
                    JandanParser.getInstance().parsePicAPI(false);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    long last = Long.parseLong(meiziPics.last().getComment_ID_index().split("_")[0]);
                    meiziPics.addAll(meiziPics.size(), realm.where(Picture.class).equalTo("type", "pic").lessThan("comment_ID_index", last).findAllSorted("comment_ID_index", false));
                    listSize = meiziPics.size();
                    adapter.notifyDataSetChanged();
                    meiziList.endLoading();
                    smoothProgressBar.setVisibility(View.GONE);
                    super.onPostExecute(aVoid);
                }
            }.execute();
        });

        /* Set Adapter */
        adapter = new MeiziAdapter();
        meiziList.setAdapter(adapter);
        /* */
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FF9900"), Color.parseColor("#009900"), Color.parseColor("#000099"));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            /* 发起加载 加载后从数据库加载 然后显示 然后隐藏 */
            new AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... params) {
                    JandanParser.getInstance().parsePicAPI(true);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    meiziPics = realm.where(Picture.class).equalTo("type", "pic").findAllSorted("comment_ID_index", false);
                    listSize = meiziPics.size();
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    super.onPostExecute(aVoid);
                }
            }.execute();
        });

        /* Pull to load */
    }

    void initMeiziPics() {
        realm = Realm.getInstance(getActivity());
        meiziPics = realm.where(Picture.class).equalTo("type", "pic").findAllSorted("comment_ID_index", false);
        listSize = meiziPics.size();
        Log.e("SIZE", "= " + meiziPics.size());
        /* Update 需要整合 以及更智能的自动更新判断 */
        if (listSize == 0) {
            swipeRefreshLayout.setRefreshing(true);
            new AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... params) {
                    JandanParser.getInstance().parsePicAPI(true);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    meiziPics = realm.where(Picture.class).equalTo("type", "pic").findAllSorted("comment_ID_index", false);
                    listSize = meiziPics.size();
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

    /* Meizi Adapter */
    class MeiziAdapter extends RecyclerView.Adapter<MeiziAdapter.MeiziHolder>{

        private LayoutInflater inflater;

        public MeiziAdapter() {
            inflater = LayoutInflater.from(getActivity());
        }

        @Override
        public MeiziHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.item_meizi, parent, false);
            return new MeiziHolder(v);
        }


        @Override
        public void onBindViewHolder(MeiziHolder meiziHolder, int i) {
            Picture picture = meiziPics.get(i);
            String comment = picture.getMeizi().getText_content();
            meiziHolder.draweeView.setAspectRatio(1.318f);
            meiziHolder.draweeView.showImage(picture);
            meiziHolder.comment.setText(comment);
            meiziHolder.author.setText("@" + picture.getMeizi().getComment_author());
            meiziHolder.date.setText(picture.getMeizi().getComment_date());
            /* Update UI */
            if (picture.getLocalUrl() != null && !picture.getLocalUrl().equals(""))
                meiziHolder.save.setImageResource(R.mipmap.ic_publish_16dp);
            else
                meiziHolder.save.setImageResource(R.drawable.selector_download);
            if (comment.trim().equals(""))
                meiziHolder.comment.setVisibility(View.GONE);
            else
                meiziHolder.comment.setVisibility(View.VISIBLE);

        }

        @Override
        public int getItemCount() {
            return listSize;
        }

        class MeiziHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            public DownloadFrescoView draweeView;
            public TextView author, date, comment;
            public ImageButton like, dislike, save;

            public MeiziHolder(View itemView) {
                super(itemView);
                draweeView = (DownloadFrescoView) itemView.findViewById(R.id.content);
                author = (TextView) itemView.findViewById(R.id.author);
                comment = (TextView) itemView.findViewById(R.id.comment);
                date = (TextView) itemView.findViewById(R.id.date);
                save = (ImageButton) itemView.findViewById(R.id.btn_save);
                /* 绑定点击事件 */
                draweeView.setListener(new FrescoControlListener(draweeView, save));
                save.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                ImageButton trigger = (ImageButton) view;
                int position = getAdapterPosition();
                Picture picture = meiziPics.get(position);
                final String url = picture.getUrl();
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
            if (asp <= 0.4) {
                draweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP);
                draweeView.getHierarchy().setActualImageFocusPoint(new PointF(0.5f, 0f));
                draweeView.setAspectRatio(1.118f);
            }

        }

        @Override
        public void onFailure(String s, Throwable throwable) {
            Picture picture = draweeView.getPicture();
            if (!picture.getLocalUrl().equals("")) { // 从本地加载图片失败 删除数据库缓存地址
                Realm realm = Realm.getInstance(getActivity());
                realm.beginTransaction();
                picture.setLocalUrl("");
                realm.commitTransaction();
                realm.close();
                /* 重新从网络加载图片 此时 localUrl已经被赋值为空 */
                draweeView.showImage(picture);
            } else { // 从网络加载图片失败 检查网络连接

            }
        }

        @Override
        public void onRelease(String s) {
            Log.i("Release Image", s);
        }
    }
}
