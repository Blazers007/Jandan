package com.blazers.jandan.presenter;

import com.blazers.jandan.model.DataManager;
import com.blazers.jandan.model.news.NewsPage;
import com.blazers.jandan.presenter.base.BasePresenter;
import com.blazers.jandan.ui.activity.NewsReadView;
import com.blazers.jandan.util.ShareHelper;

/**
 * Created by blazers on 2016/11/11.
 */

public class NewsReadPresenter extends BasePresenter<NewsReadView> {

    public static String KEY_NEWS_POST = "NEWS_ID";

    private NewsPage.Posts mPost;

    private boolean mIsFavorite;

    public NewsReadPresenter(NewsReadView view, NewsPage.Posts post) {
        super(view);
        // Prepare data
        mPost = post;
        mIsFavorite = DataManager.getInstance().isNewsFavorite(mPost);
    }

    public void onInitWebPage() {
        String content = mPost.content;
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("<html><body>");
        sb.append("<head>");
        sb.append("<link id=\"style\" rel=\"stylesheet\" type=\"text/css\" href=\"\" />");
        sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/style.css\" />");
        sb.append("<script src=\"file:///android_asset/js/main.js\" type=\"text/javascript\"></script>");
        sb.append("</head>");
        sb.append(content);
        sb.append("</body></html>");
        // 添加回复部分，采用js进行填写与布局
        mView.showHtmlPageByString(sb.toString());
        mView.setFavIconFavOrNot(mIsFavorite);
    }

    /**
     * 点击收藏
     */
    public void onClickFavoriteButton() {
        mIsFavorite = !mIsFavorite;
        DataManager.getInstance().manageNewsFavorite(mPost, mIsFavorite);
        mView.animateToFavOrNot(mIsFavorite);
    }
}
