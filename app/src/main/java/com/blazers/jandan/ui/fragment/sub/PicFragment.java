package com.blazers.jandan.ui.fragment.sub;

import android.os.Bundle;
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
import butterknife.OnClick;
import com.blazers.jandan.R;
import com.blazers.jandan.models.db.local.LocalImage;
import com.blazers.jandan.models.db.local.LocalVote;
import com.blazers.jandan.models.db.sync.ImagePost;
import com.blazers.jandan.models.pojo.image.ImageRelateToPost;
import com.blazers.jandan.network.ImageDownloader;
import com.blazers.jandan.network.Parser;
import com.blazers.jandan.rxbus.Rxbus;
import com.blazers.jandan.rxbus.event.CommentEvent;
import com.blazers.jandan.ui.fragment.base.BaseSwipeLoadMoreFragment;
import com.blazers.jandan.util.*;
import com.blazers.jandan.views.DownloadFrescoView;
import com.blazers.jandan.views.ThumbTextButton;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blazers on 15/9/8.
 *
 * 图片显示
 */
@SuppressWarnings("unused")
public class PicFragment extends BaseSwipeLoadMoreFragment {

    private JandanImageAdapter adapter;
    private ArrayList<ImageRelateToPost> imageArrayList = new ArrayList<>();
    private int mPage = 1;
    private String type;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_load, container, false);
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
        // 首先从数据库读取 在判断是否需要加载
        List<ImagePost> list = applyFilter(ImagePost.getImagePosts(realm, 1, type));
        List<ImageRelateToPost> localImageList = ImagePost.getAllImageFromList(list);
        imageArrayList.addAll(localImageList);
        adapter.notifyItemRangeInserted(0, localImageList.size());
        // 如果数据为空 或 时间大于30分钟 则更新
        if (localImageList.size() == 0 || TimeHelper.isTimeEnoughForRefreshing(localImageList.get(0).holder.getComment_date())) {
            swipeRefreshLayout.post(()->swipeRefreshLayout.setRefreshing(true));
            refresh();
        }
    }

    /**
     * 过滤
     * */
    List<ImagePost> applyFilter(List<ImagePost> posts) {
        int filter = SPHelper.getIntSP(getActivity(), SPHelper.AUTO_FILTER_NUMBER, 10000);
        if (filter == 10000)
            return posts;
        List<ImagePost> newPosts = new ArrayList<>();
        for (ImagePost post : posts) {
            long voteNegative = 0;
            try {voteNegative = Long.parseLong(post.getVote_negative());} catch (Exception e){e.printStackTrace();}// 绘画出问题}
            if (voteNegative < filter)
                newPosts.add(post);
        }
        return newPosts;
    }

    @Override
    public void refresh() {
        mPage = 1;
        if (NetworkHelper.netWorkAvailable(getActivity())) {
            Parser.getInstance().getPictureData(mPage, type) // 是IO线程还是Main县城由该方法确定
                .observeOn(AndroidSchedulers.mainThread())          // 更新在某县城由自己决定
                .doOnNext(list -> DBHelper.saveToRealm(realm, list))
                .map(this::applyFilter)
                .subscribe(list -> {
                    refreshComplete();
                    // 处理数据
                    imageArrayList.clear();
                    adapter.notifyDataSetChanged();
                    // 取出图片
                    List<ImageRelateToPost> imageRelateToPostList = ImagePost.getAllImageFromList(list);
                    int size = imageRelateToPostList.size();
                    imageArrayList.addAll(imageRelateToPostList);
                    adapter.notifyItemRangeInserted(0, size);
                }, throwable -> {
                    refreshError();
                    Log.e("Refresh", throwable.toString());
                });
        } else {
            List<ImagePost> list = ImagePost.getImagePosts(realm, mPage, type);
            if (null != list && list.size() > 0) {
                List<ImageRelateToPost> imageRelateToPostList = ImagePost.getAllImageFromList(list);
                int size = imageRelateToPostList.size();
                imageArrayList.addAll(imageRelateToPostList);
                adapter.notifyItemRangeInserted(0, size);
            } else {
                Toast.makeText(getActivity(), R.string.there_is_no_more, Toast.LENGTH_SHORT).show();
            }
            refreshComplete();
        }
    }

    @Override
    public void loadMore() {
        mPage++;
        if (NetworkHelper.netWorkAvailable(getActivity())) {
            Parser.getInstance().getPictureData(mPage, type)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(list -> DBHelper.saveToRealm(realm, list))
                .subscribe(list -> {
                    loadMoreComplete();
                    // 取出图片
                    List<ImageRelateToPost> imageRelateToPostList = ImagePost.getAllImageFromList(list);
                    int start = imageArrayList.size();
                    int size = imageRelateToPostList.size();
                    imageArrayList.addAll(imageRelateToPostList);
//                    adapter.notifyItemRangeInserted(start, size);
                    adapter.notifyDataSetChanged();
                }, throwable -> {
                    loadMoreError();
                    Log.e("LoadMore", throwable.toString());
                });
        } else {
            List<ImagePost> list = ImagePost.getImagePosts(realm, mPage, type);
            if (null != list && list.size() > 0) {
                List<ImageRelateToPost> imageRelateToPostList = ImagePost.getAllImageFromList(list);
                int start = imageArrayList.size();
                int size = imageRelateToPostList.size();
                imageArrayList.addAll(imageRelateToPostList);
//                adapter.notifyItemRangeInserted(start, size);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), R.string.there_is_no_more, Toast.LENGTH_SHORT).show();
            }
            loadMoreComplete();
        }
    }


    /**
     * ----------------------------------------  Adapter
     * */
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
            if (holder.draweeView.getController() != null)
                holder.draweeView.getController().onDetach();
            if (holder.draweeView.getTopLevelDrawable() != null)
                holder.draweeView.getTopLevelDrawable().setCallback(null);
        }

        @Override
        public void onBindViewHolder(JandanHolder holder, int position) {
            /* Get data */
            ImageRelateToPost image = imageArrayList.get(position);
            ImagePost post = image.holder;
            /* Set data */
            holder.author.setText(String.format("@+%s", post.getComment_author()));
            holder.date.setText(post.getComment_date());
            holder.oo.setThumbText(post.getVote_positive());
            holder.xx.setThumbText(post.getVote_negative());
            holder.comment.setThumbText(String.format("%s", post.getCommentNumber()));
            // 填入评论文字
            String comment = post.getText_content();
            if (comment.trim().equals(""))
                holder.text.setVisibility(View.GONE);
            else {
                holder.text.setVisibility(View.VISIBLE);
                holder.text.setText(comment.trim());
            }
            // 加载图片 首先判断本地是否有
            LocalImage localImage = realm.where(LocalImage.class).equalTo("url", image.url).findFirst();
            String url;
            if (localImage != null && SdcardHelper.isThisFileExist(localImage.getLocalUrl())) {
                holder.draweeView.showImage("file://" + localImage.getLocalUrl(), holder.save); //TODO: 这种参数传递可能导致无法正确调用Trigger
                holder.save.setVisibility(View.VISIBLE);
                holder.save.setClickable(false);
                holder.save.setImageResource(R.mipmap.ic_publish_16dp);
                url = localImage.getLocalUrl();
            } else {
                holder.draweeView.showImage(image.url, holder.save);
                holder.save.setVisibility(View.INVISIBLE);
                holder.save.setClickable(true);
                holder.save.setImageResource(R.drawable.selector_download);
                url = image.url;
            }
            // 根据尺寸显示图片信息
            holder.typeHint.setImageDrawable(null);
            // 是否是GIF
            if (url.substring(url.lastIndexOf(".") + 1).equals("gif"))
                holder.typeHint.setImageResource(R.mipmap.ic_gif_corner_24dp);
            holder.draweeView.setImageInfoListener((width, height) -> {
                if (width > 2048 || height > 2048) {
                    holder.typeHint.setImageResource(R.mipmap.ic_more_corner_24dp);
                }
            });
            //TODO 优化数据库查询 或者缓存
            LocalVote vote = realm.where(LocalVote.class).equalTo("id", post.getComment_ID()).findFirst();
            if (vote != null){
                if (vote.getId() > 0) {
                    holder.oo.setPressed(true);
                    holder.xx.setPressed(false);
                }else if (vote.getId() < 0) {
                    holder.oo.setPressed(false);
                    holder.xx.setPressed(true);
                }
            } else {
                holder.oo.setPressed(false);
                holder.xx.setPressed(false);
            }
        }

        @Override
        public int getItemCount() {
            return imageArrayList.size();
        }


        /**
         * ViewHolder
         * */
        class JandanHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.content) DownloadFrescoView draweeView;
            @Bind(R.id.author) TextView author;
            @Bind(R.id.text) TextView text;
            @Bind(R.id.date) TextView date;
            @Bind(R.id.btn_save) ImageButton save;
            @Bind(R.id.btn_share) ImageButton share;
            @Bind(R.id.btn_oo) ThumbTextButton oo;
            @Bind(R.id.btn_xx) ThumbTextButton xx;
            @Bind(R.id.btn_comment) ThumbTextButton comment;
            @Bind(R.id.type_hint) ImageView typeHint;

            public JandanHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            @OnClick(R.id.btn_save)
            public void download(){
                Observable.just(imageArrayList.get(getAdapterPosition()))
                        .map(ImageDownloader.getInstance()::doSavingImage)
                        .compose(RxHelper.applySchedulers())
                        .subscribe(localImage -> {
                            Toast.makeText(getActivity(), "图片保存成功", Toast.LENGTH_SHORT).show();
                            save.setClickable(false);
                            save.setImageResource(R.mipmap.ic_publish_16dp);
                        }, throwable -> {
                            Log.e("Error", throwable.toString());
                        });
            }

            @OnClick({R.id.btn_oo, R.id.btn_xx})
            public void vote(View view) {
                ImageRelateToPost imageRelateToPost = imageArrayList.get(getAdapterPosition());
                ImagePost post = imageRelateToPost.holder;
                /* 查看是否已经投票 */
                LocalVote vote = realm.where(LocalVote.class).equalTo("id", post.getComment_ID()).findFirst();
                if (vote != null && vote.getId() != 0){
                    Toast.makeText(getActivity(), R.string.warn_already_vote, Toast.LENGTH_SHORT).show();
                    return;
                }
                switch (view.getId()) {
                    case R.id.btn_oo:
                        Parser.getInstance().voteByCommentIdAndVote(post.getComment_ID(), true)
                            .compose(RxHelper.applySchedulers())
                            .subscribe(s->{
                                oo.addThumbText(1);
                                LocalVote v = new LocalVote();
                                v.setId(post.getComment_ID());
                                v.setVote(1);
                                DBHelper.saveToRealm(realm, v);
                            },throwable -> Log.e("Vote", throwable.toString()));
                        break;
                    case R.id.btn_xx:
                        Parser.getInstance().voteByCommentIdAndVote(post.getComment_ID(), true)
                            .compose(RxHelper.applySchedulers())
                            .subscribe(s -> {
                                xx.addThumbText(1);
                                LocalVote v = new LocalVote();
                                v.setId(post.getComment_ID());
                                v.setVote(-1);
                                DBHelper.saveToRealm(realm, v);
                            }, throwable -> Log.e("Vote", throwable.toString()));
                        break;
                }
            }

            @OnClick(R.id.btn_comment)
            public void showComment(){
                Rxbus.getInstance().send(new CommentEvent(imageArrayList.get(getAdapterPosition()).holder.getComment_ID()));
            }
        }
    }
}
