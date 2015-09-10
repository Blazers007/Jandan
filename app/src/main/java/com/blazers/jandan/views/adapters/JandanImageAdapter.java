package com.blazers.jandan.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.blazers.jandan.R;
import com.blazers.jandan.models.local.OSBSImage;
import com.blazers.jandan.models.jandan.Image;
import com.blazers.jandan.views.widget.DownloadFrescoView;
import com.blazers.jandan.views.widget.ThumbTextButton;
import io.realm.RealmResults;

/**
 * Created by Blazers on 2015/9/10.
 *
 * 抽象煎蛋各适配器而来 分离 Controller 与 View
 *
 */
public class JandanImageAdapter extends RecyclerView.Adapter<JandanImageAdapter.JandanHolder> {

    private LayoutInflater inflater;
    private RealmResults<Image> imageRealmResults;

    public JandanImageAdapter(Context context, RealmResults<Image> imageRealmResults) {
        this.inflater = LayoutInflater.from(context);
        this.imageRealmResults = imageRealmResults;
    }

    @Override
    public JandanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_meizi, parent, false);
        return new JandanHolder(itemView);
    }

    @Override
    public void onBindViewHolder(JandanHolder holder, int position) {
        /* Get data */
        Image image = imageRealmResults.get(position);
        String comment = image.getPost().getText_content();
        OSBSImage osbsImage = image.getImage();
        /* Set data */
        holder.draweeView.setAspectRatio(1.318f);
        holder.author.setText("@" + image.getPost().getComment_author());
        holder.date.setText(image.getPost().getComment_date());
        holder.oo.setThumbText(image.getPost().getVote_positive());
        holder.xx.setThumbText(image.getPost().getVote_negative());
        holder.comment.setThumbText(image.getPost().getImage_size()+"");

        if (osbsImage.getLocal_url() != null && !osbsImage.getLocal_url().equals("")) {
            holder.draweeView.showImage(osbsImage.getLocal_url(), holder.save); //TODO: 这种参数传递可能导致无法正确调用Trigger
            holder.save.setImageResource(R.mipmap.ic_publish_16dp);
        } else {
            holder.draweeView.showImage(osbsImage.getWeb_url(), holder.save);
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
        return imageRealmResults.size();
    }

    class JandanHolder extends RecyclerView.ViewHolder {

        public DownloadFrescoView draweeView;
        public TextView author, date, text;
        public ImageButton save, share;
        public ThumbTextButton oo, xx, comment;

        public JandanHolder(View itemView) {
            super(itemView);
            draweeView = (DownloadFrescoView) itemView.findViewById(R.id.content);
            author = (TextView) itemView.findViewById(R.id.author);
            text = (TextView) itemView.findViewById(R.id.text);
            date = (TextView) itemView.findViewById(R.id.date);
            save = (ImageButton) itemView.findViewById(R.id.btn_save);
            share = (ImageButton) itemView.findViewById(R.id.btn_share);

            oo = (ThumbTextButton) itemView.findViewById(R.id.btn_oo);
            xx = (ThumbTextButton) itemView.findViewById(R.id.btn_xx);
            comment = (ThumbTextButton) itemView.findViewById(R.id.btn_comment);
        }
    }
}
