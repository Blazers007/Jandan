package com.blazers.jandan.model.image;

import android.databinding.BindingAdapter;

import com.blazers.jandan.model.DataManager;
import com.blazers.jandan.model.extension.Time;
import com.blazers.jandan.presenter.ImagePresenter;
import com.blazers.jandan.widgets.AutoScaleFrescoView;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by blazers on 2016/12/2.
 *
 * 用于目前单张显示模式
 *
 */

public class SingleImage extends RealmObject {

    public static final String ID = "id";
    public static final String TYPE = "type";

    @PrimaryKey
    public String id;
    public int page;
    public int type;
    public String url;
    public boolean favorite;
    public Date favorite_time;
    public ImageComment comment;


    public SingleImage() {

    }


    public static List<SingleImage> splitToSingle(Realm realm, ImageComment comment) {
        if (comment == null)
            return null;
        List<SingleImage> singleImageList = new ArrayList<>();
        if (comment.pics != null) {
            int index = comment.pics.size();
            for (int i = 0 ; i < comment.pics.size() ; i ++) {
                String id = comment.comment_ID + ":" + index--;
                SingleImage singleImage = realm.where(SingleImage.class).equalTo(ID, id).findFirst();
                if (singleImage == null) {
                    singleImage = realm.createObject(SingleImage.class, id);
                    singleImage.comment = comment;
                }
                singleImageList.add(singleImage);
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

    @BindingAdapter({"init_fav_presenter", "init_fav_image"})
    public static void initFavorite(MaterialFavoriteButton materialFavoriteButton, ImagePresenter presenter, SingleImage singleImage) {
        materialFavoriteButton.setFavorite(singleImage.favorite, false);
        materialFavoriteButton.setOnFavoriteChangeListener((buttonView, favorite) -> presenter.setFavoriteOrNot(singleImage, favorite));
    }
}
