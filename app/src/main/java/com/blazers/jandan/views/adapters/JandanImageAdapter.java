package com.blazers.jandan.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.models.jandan.Image;
import com.blazers.jandan.views.widget.DownloadFrescoView;
import com.blazers.jandan.views.widget.ThumbTextButton;

import java.util.ArrayList;

/**
 * Created by Blazers on 2015/9/10.
 *
 * 抽象煎蛋各适配器而来 分离 Controller 与 View
 *
 */
public class JandanImageAdapter extends RecyclerView.Adapter<JandanImageAdapter.JandanHolder> {

    private LayoutInflater inflater;
    private ArrayList<Image> imageArrayList;
    private int page;

    public JandanImageAdapter(Context context, ArrayList<Image> imagePostsArrayList) {
        this.inflater = LayoutInflater.from(context);
        this.imageArrayList = imagePostsArrayList;
    }

    @Override
    public JandanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_jandan_image_2, parent, false);
        return new JandanHolder(itemView);
    }

    @Override
    public void onBindViewHolder(JandanHolder holder, int position) {
        /* Get data */
        Image image = imageArrayList.get(position);
        String comment = image.getPost().getText_content();
        /* Set data */
        holder.draweeView.setAspectRatio(1.318f);
        holder.author.setText("@" + image.getPost().getComment_author());
        holder.date.setText(image.getPost().getComment_date());
        holder.oo.setThumbText(image.getPost().getVote_positive());
        holder.xx.setThumbText(image.getPost().getVote_negative());
        holder.comment.setThumbText(image.getPost().getImage_size()+"");

        if (image.getLocalUrl() != null && !image.getLocalUrl().equals("")) {
            holder.draweeView.showImage(image.getLocalUrl(), holder.save); //TODO: 这种参数传递可能导致无法正确调用Trigger
            holder.save.setImageResource(R.mipmap.ic_publish_16dp);
        } else {
            holder.draweeView.showImage(image.getUrl(), holder.save);
            holder.save.setImageResource(R.drawable.selector_download);
        }

        if (comment.trim().equals(""))
            holder.comment.setVisibility(View.GONE);
        else {
            holder.text.setVisibility(View.VISIBLE);
            holder.text.setText(comment);
        }
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
        @Bind(R.id.btn_comment) ThumbTextButton comment;

        public JandanHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
