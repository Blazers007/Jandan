package com.blazers.jandan.ui.fragment;

import android.graphics.Color;
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
import com.blazers.jandan.orm.meizi.Picture;
import com.blazers.jandan.util.network.JandanParser;
import com.blazers.jandan.widget.DownloadFrescoView;
import com.blazers.jandan.widget.LoadMoreRecyclerView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Blazers on 2015/8/25.
 */
public class MeiziFragment extends Fragment {

    private static final String TAG = MeiziFragment.class.getSimpleName();

    @Bind(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_list) LoadMoreRecyclerView meiziList;
    @Bind(R.id.load_more_progress) SmoothProgressBar smoothProgressBar;

    private Realm realm;
    private MeiziAdapter adapter;
    private RealmResults<Picture> meiziPics;
    private int listSize;
    private int nowViewPosition;

    /* Beta */


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
        /* Loadmore */
        meiziList.setLoadMoreListener(() -> {
            smoothProgressBar.setVisibility(View.VISIBLE);
            new AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... params) {
                    JandanParser.getInstance().parseMeiziAPI(false);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    long last = Long.parseLong(meiziPics.last().getComment_ID_index().split("_")[0]);
                    meiziPics.addAll(meiziPics.size(), realm.where(Picture.class).lessThan("comment_ID_index", last).findAllSorted("comment_ID_index", false));
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
                    JandanParser.getInstance().parseMeiziAPI(true);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    meiziPics = realm.where(Picture.class).findAllSorted("comment_ID_index", false);
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
        meiziPics = realm.where(Picture.class).findAllSorted("comment_ID_index", false);
        listSize = meiziPics.size();
        Log.e("SIZE", "= " + meiziPics.size());
        /* Update 需要整合 以及更智能的自动更新判断 */
        if (listSize == 0) {
            swipeRefreshLayout.setRefreshing(true);
            new AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... params) {
                    JandanParser.getInstance().parseMeiziAPI(true);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    meiziPics = realm.where(Picture.class).findAllSorted("comment_ID_index", false);
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

    //     gengxin duibi xianyou

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
            meiziHolder.draweeView.setAspectRatio(0.618f);
            meiziHolder.draweeView.showImage(picture);
            meiziHolder.author.setText(picture.getMeizi().getComment_author());
        }

        @Override
        public int getItemCount() {
            return listSize;
        }

        class MeiziHolder extends RecyclerView.ViewHolder {

            public DownloadFrescoView draweeView;
            public TextView author;

            public MeiziHolder(View itemView) {
                super(itemView);
                draweeView = (DownloadFrescoView) itemView.findViewById(R.id.drweeView);
                author = (TextView) itemView.findViewById(R.id.textView);
            }
        }
    }
}