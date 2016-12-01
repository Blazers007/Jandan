package com.blazers.jandan.ui.fragment.readingsub;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blazers.jandan.R;
import com.blazers.jandan.model.database.local.LocalVote;
import com.blazers.jandan.model.database.sync.OldImagePost;
import com.blazers.jandan.model.pojo.image.ImageRelateToPost;
import com.blazers.jandan.util.ImageDownloader;
import com.blazers.jandan.model.DataManager;
import com.blazers.jandan.util.Rxbus;
import com.blazers.jandan.model.event.ViewCommentEvent;
import com.blazers.jandan.model.event.ViewImageEvent;
import com.blazers.jandan.ui.fragment.base.BaseSwipeLoadMoreFragment;
import com.blazers.jandan.util.DBHelper;
import com.blazers.jandan.util.NetworkHelper;
import com.blazers.jandan.util.RxHelper;
import com.blazers.jandan.util.SPHelper;
import com.blazers.jandan.util.ShareHelper;
import com.blazers.jandan.widgets.AutoScaleFrescoView;
import com.blazers.jandan.widgets.ThumbTextButton;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Blazers on 15/9/8.
 * <p>
 * 图片显示
 */
@RuntimePermissions
@SuppressWarnings("unused")
public class PicFragment extends BaseSwipeLoadMoreFragment {

    private JandanImageAdapter adapter;
    private ArrayList<ImageRelateToPost> imageArrayList = new ArrayList<>();
    private ImageRelateToPost mTempTask;
    private int mPage = 1;


    /* Constructor */
    public static PicFragment newInstance(String type) {
        Bundle data = new Bundle();
        data.putString("type", type);
        PicFragment picFragment = new PicFragment();
        picFragment.setTAG(type);
        picFragment.setArguments(data);
        return picFragment;
    }


    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutResId() {
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_common_refresh_load, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null && getArguments().getString("type") != null) {
            type = getArguments().getString("type");
            initRecyclerView();
        }
    }

    void initRecyclerView() {
        trySetupSwipeRefreshLayout();
        trySetupRecyclerViewWithAdapter(adapter = new JandanImageAdapter());
//        // 首先从数据库读取 在判断是否需要加载
//        List<ImagePost> list = applyFilter(ImagePost.getImagePosts(realm, 1, type));
//        List<ImageRelateToPost> localImageList = ImagePost.getAllImageFromList(list);
//        imageArrayList.addAll(localImageList);
//        adapter.notifyItemRangeInserted(0, localImageList.size());
//        // 如果数据为空 或 时间大于30分钟 则更新
//        if (localImageList.size() == 0 || TimeHelper.isTimeEnoughForRefreshing(SPHelper.getLastRefreshTime(getActivity(), type))) {
//            mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));
//            refresh();
//        }
    }

    /**
     * 过滤
     */
    List<OldImagePost> applyFilter(List<OldImagePost> posts) {
        int filter = SPHelper.getIntSP(getActivity(), SPHelper.AUTO_FILTER_NUMBER, 10000);
        if (filter == 10000)
            return posts;
        List<OldImagePost> newPosts = new ArrayList<>();
        for (OldImagePost post : posts) {
            long voteNegative = 0;
            try {
                voteNegative = Long.parseLong(post.getVote_negative());
            } catch (Exception e) {
                e.printStackTrace();
            }// 绘画出问题}
            if (voteNegative < filter)
                newPosts.add(post);
        }
        return newPosts;
    }

    @Override
    public void refresh() {
        mPage = 1;
//        if (NetworkHelper.netWorkAvailable(getActivity())) {
//            DataManager.getInstance().getPictureData(mPage, type) // 是IO线程还是Main县城由该方法确定
//                    .observeOn(AndroidSchedulers.mainThread())          // 更新在某县城由自己决定
//                    .doOnNext(list -> DBHelper.saveToRealm(realm, list))
//                    .map(this::applyFilter)
//                    .subscribe(list -> {
//                        refreshComplete();
//                        // 处理数据
//                        imageArrayList.clear();
//                        adapter.notifyDataSetChanged();
//                        // 取出图片
//                        List<ImageRelateToPost> imageRelateToPostList = ImagePost.getAllImageFromList(list);
//                        int size = imageRelateToPostList.size();
//                        imageArrayList.addAll(imageRelateToPostList);
//                        adapter.notifyItemRangeInserted(0, size);
//                    }, throwable -> {
//                        refreshError();
//                        Log.e("Refresh", throwable.toString());
//                    });
//        } else {
//            List<ImagePost> list = ImagePost.getImagePosts(realm, mPage, type);
//            if (null != list && list.size() > 0) {
//                List<ImageRelateToPost> imageRelateToPostList = ImagePost.getAllImageFromList(list);
//                int size = imageRelateToPostList.size();
//                imageArrayList.addAll(imageRelateToPostList);
//                adapter.notifyItemRangeInserted(0, size);
//            } else {
//                Toast.makeText(getActivity(), R.string.there_is_no_more, Toast.LENGTH_SHORT).show();
//            }
//            refreshComplete();
//        }
    }

    @Override
    public void loadMore() {
        mPage++;
        if (NetworkHelper.netWorkAvailable(getActivity())) {
            DataManager.getInstance().getPictureData(mPage, type)
                    .observeOn(AndroidSchedulers.mainThread())
//                    .doOnNext(list -> DBHelper.saveToRealm(realm, list))
                    .subscribe(list -> {

                        // 取出图片
                        List<ImageRelateToPost> imageRelateToPostList = OldImagePost.getAllImageFromList(list);
                        int start = imageArrayList.size();
                        int size = imageRelateToPostList.size();
                        imageArrayList.addAll(imageRelateToPostList);
//                    adapter.notifyItemRangeInserted(start, size);
                        adapter.notifyDataSetChanged();
                    }, throwable -> {
                        Log.e("LoadMore", throwable.toString());
                    });
        } else {
//            List<ImagePost> list = ImagePost.getImagePosts(realm, mPage, type);
//            if (list.size() > 0) {
//                List<ImageRelateToPost> imageRelateToPostList = ImagePost.getAllImageFromList(list);
//                int start = imageArrayList.size();
//                int size = imageRelateToPostList.size();
//                imageArrayList.addAll(imageRelateToPostList);
////                adapter.notifyItemRangeInserted(start, size);
//                adapter.notifyDataSetChanged();
//            } else {
//                Toast.makeText(getActivity(), R.string.there_is_no_more, Toast.LENGTH_SHORT).show();
//            }
        }
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showWriteExternalStorage() {
        // 已经允许读写SD卡
        if (mTempTask != null) {
            Observable.just(mTempTask)
                    .map(ImageDownloader.getInstance()::doSavingImage)
                    .compose(RxHelper.applySchedulers())
                    .subscribe(localImage -> {
                        Toast.makeText(getActivity(), "图片保存成功", Toast.LENGTH_SHORT).show();
                        DBHelper.saveToRealm(getActivity(), localImage);
                    }, throwable -> {
                        Log.e("Error", throwable.toString());
                    });
            mTempTask = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PicFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * ----------------------------------------  Adapter
     */
    class JandanImageAdapter extends RecyclerView.Adapter<JandanImageAdapter.JandanHolder> {

        private LayoutInflater inflater;

        public JandanImageAdapter() {
            this.inflater = LayoutInflater.from(getContext());
        }

        @Override
        public JandanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.item_jandan_image, parent, false);
            return new JandanHolder(itemView);
        }

        /*TODO: Totally important !!!! https://github.com/06peng/FrescoDemo/blob/master/app/src/main/java/com/mzba/fresco/ui/ImageListFragment.java  */
        @Override
        public void onViewRecycled(JandanHolder holder) {
            if (holder.content.getController() != null)
                holder.content.getController().onDetach();
            if (holder.content.getTopLevelDrawable() != null)
                holder.content.getTopLevelDrawable().setCallback(null);
        }

        @Override
        public void onBindViewHolder(JandanHolder holder, int position) {
            /* Get data */
            ImageRelateToPost image = imageArrayList.get(position);
            OldImagePost post = image.holder;
            /* Set data */
            holder.author.setText(String.format("@+%s", post.getComment_author()));
            holder.date.setText(post.getComment_date());
            holder.oo.setThumbText(post.getVote_positive());
            holder.xx.setThumbText(post.getVote_negative());
            ;
            holder.comment.setThumbText(String.format("%s", post.getCommentNumber()));
            holder.content.setAspectRatio(1.418f);
            holder.typeHint.setImageDrawable(null);
//            holder.fav.setFavorite(LocalFavImages.isThisFaved(realm, image.url), false);
            // 填入评论文字
            String comment = post.getText_content();
            if (comment.trim().equals(""))
                holder.text.setVisibility(View.GONE);
            else {
                holder.text.setVisibility(View.VISIBLE);
                holder.text.setText(comment.trim());
            }
            // 加载图片 首先判断本地是否有
//            LocalImage localImage = LocalImage.getLocalImageByWebUrl(realm, image.url);
//            String url;
//            if (localImage != null && SdcardHelper.isThisFileExist(localImage.getLocalUrl())) {
//                holder.content.showImage(holder.typeHint, "file://" + localImage.getLocalUrl());
//            } else {
//                holder.content.showImage(holder.typeHint, image.url);
//            }
            // 显示Vote信息 TODO 优化数据库查询 或者缓存
//            LocalVote vote = LocalVote.getLocalVoteById(realm, post.getComment_ID());
//            if (vote != null) {
//                if (vote.getId() > 0) {
//                    holder.oo.setPressed(true);
//                    holder.xx.setPressed(false);
//                } else if (vote.getId() < 0) {
//                    holder.oo.setPressed(false);
//                    holder.xx.setPressed(true);
//                }
//            } else {
//                holder.oo.setPressed(false);
//                holder.xx.setPressed(false);
//            }
        }

        @Override
        public int getItemCount() {
            return imageArrayList.size();
        }

        /**
         * ViewHolder
         */
        class JandanHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.content)
            AutoScaleFrescoView content;
            @BindView(R.id.author)
            TextView author;
            @BindView(R.id.text)
            TextView text;
            @BindView(R.id.date)
            TextView date;
            @BindView(R.id.btn_fav)
            MaterialFavoriteButton fav;
            @BindView(R.id.btn_share)
            ImageButton share;
            @BindView(R.id.btn_oo)
            ThumbTextButton oo;
            @BindView(R.id.btn_xx)
            ThumbTextButton xx;
            @BindView(R.id.btn_comment)
            ThumbTextButton comment;
            @BindView(R.id.type_hint)
            ImageView typeHint;

            public JandanHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                // Fav
//                fav.setOnFavoriteChangeListener(
//                        (view, favorite) -> LocalFavImages.setThisFavedOrNot(favorite, realm, imageArrayList.get(getAdapterPosition()).url)
//                );
            }

            @OnClick(R.id.content)
            public void view() {
                if (content.isImageLoaded()) {
                    ImageRelateToPost image = imageArrayList.get(getAdapterPosition());
                    Rxbus.getInstance().send(new ViewImageEvent(image.url, image.holder.getText_content()));
                }
            }

            @OnLongClick(R.id.content)
            public boolean download() {
                // 弹窗
                if (Build.VERSION.SDK_INT >= 23
                        && getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // 缓存任务
                    mTempTask = imageArrayList.get(getAdapterPosition());
                    // 请求权限
                    PicFragmentPermissionsDispatcher.showWriteExternalStorageWithCheck(PicFragment.this);
                } else {
                    Observable.just(imageArrayList.get(getAdapterPosition()))
                            .map(ImageDownloader.getInstance()::doSavingImage)
                            .compose(RxHelper.applySchedulers())
                            .subscribe(localImage -> {
                                DBHelper.saveToRealm(getActivity(), localImage);
                                Snackbar.make(mLoadMoreRecyclerView, "图片保存成功", Snackbar.LENGTH_SHORT)
                                        .setActionTextColor(getResources().getColor(R.color.yellow500))
                                        .setAction("撤销", v -> {
                                            // TODO: 添加删除逻辑
                                        })
                                        .show();
                            }, throwable -> {
                                Log.e("Error", throwable.toString());
                            });
                }
                return true;
            }

            @OnClick({R.id.btn_oo, R.id.btn_xx})
            public void vote(View view) {
                ImageRelateToPost imageRelateToPost = imageArrayList.get(getAdapterPosition());
                OldImagePost post = imageRelateToPost.holder;
                /* 查看是否已经投票 */
//                LocalVote vote = realm.where(LocalVote.class).equalTo("id", post.getComment_ID()).findFirst();
//                if (vote != null && vote.getId() != 0) {
//                    Toast.makeText(getActivity(), R.string.warn_already_vote, Toast.LENGTH_SHORT).show();
//                    return;
//                }
                // 播放动画 回馈后停止动画效果
                switch (view.getId()) {
                    case R.id.btn_oo:
                        DataManager.getInstance().voteByCommentIdAndVote(post.getComment_ID(), true)
                                .compose(RxHelper.applySchedulers())
                                .subscribe(s -> {
                                    oo.addThumbText(1);
                                    LocalVote v = new LocalVote();
                                    v.setId(post.getComment_ID());
                                    v.setVote(1);
//                                    DBHelper.saveToRealm(realm, v);
                                }, throwable -> Log.e("Vote", throwable.toString()));
                        break;
                    case R.id.btn_xx:
                        DataManager.getInstance().voteByCommentIdAndVote(post.getComment_ID(), true)
                                .compose(RxHelper.applySchedulers())
                                .subscribe(s -> {
                                    xx.addThumbText(1);
                                    LocalVote v = new LocalVote();
                                    v.setId(post.getComment_ID());
                                    v.setVote(-1);
//                                    DBHelper.saveToRealm(realm, v);
                                }, throwable -> Log.e("Vote", throwable.toString()));
                        break;
                }
            }

            @OnClick(R.id.btn_comment)
            public void showComment() {
                Rxbus.getInstance().send(new ViewCommentEvent(imageArrayList.get(getAdapterPosition()).holder.getComment_ID()));
            }

            /**
             * 分享
             */
            @OnClick(R.id.btn_share)
            public void share() {
                // 放入缓存目录中 TODO: 整合离线与缓存?
                ImageRelateToPost image = imageArrayList.get(getAdapterPosition());
                Observable.just(imageArrayList.get(getAdapterPosition()))
                        .map(ImageDownloader.getInstance()::doCachingImage)
                        .compose(RxHelper.applySchedulers())
                        .subscribe(local -> {
                            doShare(image.holder.getText_content(), local.getLocalUrl());
                            DBHelper.saveToRealm(getActivity(), local);
                        }, throwable -> {
                            Log.e("Error", throwable.toString());
                        });
            }

            void doShare(String text, String filePath) {
                ShareHelper.shareImage(getActivity(), type.equals("wuliao") ? "无聊图" : "妹子图", text, filePath);
            }
        }
    }
}
