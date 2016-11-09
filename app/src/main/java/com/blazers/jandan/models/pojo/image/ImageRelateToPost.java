package com.blazers.jandan.models.pojo.image;
import com.blazers.jandan.models.db.sync.ImagePost;

/**
 * Created by Blazers on 2015/10/21.
 *
 * Image对象的ViewModel
 */
public class ImageRelateToPost {
    public ImagePost holder;
    public String url;

    public ImageRelateToPost(ImagePost holder, String url) {
        this.holder = holder;
        this.url = url;
    }
}
