package com.blazers.jandan.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.blazers.jandan.R;
import com.blazers.jandan.models.db.local.LocalImage;
import com.blazers.jandan.models.db.sync.ImagePost;
import com.blazers.jandan.models.pojo.image.ImageRelateToPost;
import com.blazers.jandan.ui.activity.MainActivity;
import com.blazers.jandan.util.FileHelper;
import com.blazers.jandan.views.DownloadFrescoView;
import com.blazers.jandan.views.ThumbTextButton;
import io.realm.Realm;

import java.util.ArrayList;

/**
 * Created by Blazers on 2015/9/10.
 *
 * 抽象煎蛋各适配器而来 分离 Controller 与 View
 *
 */
public class JandanImageAdapter extends RecyclerView.Adapter<JandanImageAdapter.JandanHolder> {

    private LayoutInflater inflater;
    private ArrayList<ImageRelateToPost> imageArrayList;
    private Context context;
    private Realm realm; // 持有该Recycler的线程持有

    public JandanImageAdapter(Context context, Realm realm, ArrayList<ImageRelateToPost> imagePostsArrayList) {
        this.inflater = LayoutInflater.from(context);
        this.imageArrayList = imagePostsArrayList;
        this.context = context;
        this.realm = realm;
    }

    @Override
    public JandanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_jandan_image_2, parent, false);
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
        if (localImage != null && FileHelper.isThisFileExist(localImage.getLocalUrl())) {
            holder.draweeView.showImage("file://" + localImage.getLocalUrl(), holder.save); //TODO: 这种参数传递可能导致无法正确调用Trigger
            holder.save.setVisibility(View.VISIBLE);
            holder.save.setImageResource(R.mipmap.ic_publish_16dp);
        } else {
            holder.draweeView.showImage(image.url, holder.save);
            holder.save.setVisibility(View.INVISIBLE);
            holder.save.setImageResource(R.drawable.selector_download);
        }
        // 根据尺寸显示图片信息
        holder.draweeView.setImageInfoListener((width, height) -> {
            if (width > 2048 || height > 2048) {
                holder.hint.setText("长图片 喵喵 ~");
                holder.hint.setVisibility(View.VISIBLE);
            } else
                holder.hint.setVisibility(View.GONE);
        });
    }

    @Override
    public int getItemCount() {
        return imageArrayList.size();
    }

    class JandanHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.content) DownloadFrescoView draweeView;
        @Bind(R.id.author) TextView author;
        @Bind(R.id.text) TextView text;
        @Bind(R.id.date) TextView date;
        @Bind(R.id.btn_save) ImageButton save;
        @Bind(R.id.btn_share) ImageButton share;
        @Bind(R.id.btn_oo) ThumbTextButton oo;
        @Bind(R.id.btn_xx) ThumbTextButton xx;
        @Bind(R.id.hint) TextView hint;
        @Bind(R.id.btn_comment) ThumbTextButton comment;

        public JandanHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.btn_comment)
        public void showComment() {
            long id = imageArrayList.get(getAdapterPosition()).holder.getComment_ID();
            ((MainActivity)context).pushInCommentFragment(id);
        }

        @OnClick(R.id.btn_save)
        public void doDownload() {

        }

    }

    public interface ImageInfo {
        void onLoaded(int width, int height);
    }
}
