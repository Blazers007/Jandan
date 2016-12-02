package com.blazers.jandan.model.image;

import android.databinding.BindingAdapter;
import android.net.Uri;

import com.blazers.jandan.model.Time;
import com.blazers.jandan.model.news.NewsPage;
import com.blazers.jandan.widgets.AutoScaleFrescoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by blazers on 2016/12/2.
 *
 * 用于目前单张显示模式
 *
 */

public class SingleImage extends Time {

    public String id;
    public ImagePage.Comments comment;
    public String url;

    public SingleImage(int index, ImagePage.Comments comment, String url) {
        this.id = comment.comment_ID + ":" + index;
        this.comment = comment;
        this.url = url;
    }

    /**
     * 将一片Post中的图片提取出来
     * @param commentsList
     * @return
     */
    public static List<SingleImage> splitToSingle(List<ImagePage.Comments> commentsList) {
        if (commentsList == null)
            return null;
        List<SingleImage> singleImageList = new ArrayList<>();
        for (ImagePage.Comments comments : commentsList) {
            if (comments != null && comments.pics != null) {
                for (int i = 0 ; i < comments.pics.size() ; i ++) {
                    singleImageList.add(new SingleImage(i, comments, comments.pics.get(i)));
                }
            }
        }
        return singleImageList;
    }


    @BindingAdapter("image_url")
    public static void showImage(AutoScaleFrescoView autoScaleFrescoView, String url) {
        // 查找本地是否有
        autoScaleFrescoView.setAspectRatio(1.418f);
        autoScaleFrescoView.showImage(null, url);
    }

}
