package com.blazers.jandan.presenter;

import com.blazers.jandan.model.DataManager;
import com.blazers.jandan.model.event.ViewImageEvent;
import com.blazers.jandan.model.news.NewsPage;
import com.blazers.jandan.model.news.NewsPost;
import com.blazers.jandan.presenter.base.BasePresenter;
import com.blazers.jandan.ui.activity.NewsReadView;

/**
 * Created by blazers on 2016/11/11.
 */

public class NewsReadPresenter extends BasePresenter<NewsReadView> {

    private NewsPost mPost;

    private boolean mIsFavorite;

    public NewsReadPresenter(NewsReadView view, int postId) {
        super(view);
        // Prepare data
        mPost = DataManager.getInstance().getNewsPost(postId);
//        mIsFavorite = DataManager.getInstance().isNewsFavorite(mPost);
        String content = mPost.content;
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("<html><body>");
        sb.append("<head>");
        sb.append("<link id=\"style\" rel=\"stylesheet\" type=\"text/css\" href=\"\" />");
        sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\" />");
        sb.append("<script src=\"js/main.js\" type=\"text/javascript\"></script>");
        sb.append("</head>");
        sb.append(content);
        sb.append("</body></html>");
        // 是否需要替换本地文件? 替换了本地文件之后如何在页面内判断是否已经下载？
        // 添加回复部分，采用js进行填写与布局
        mView.onLoadHtmlString(sb.toString());
        mView.onSetFavoriteIconState(mIsFavorite);
        mView.onHideLoadingProgress();
    }

    /**
     * 点击收藏
     */
    public void clickFavoriteButton() {
        mIsFavorite = !mIsFavorite;
//        DataManager.getInstance().manageNewsFavorite(mPost, mIsFavorite);
        mView.onAnimateToFavoriteOrNot(mIsFavorite);
    }

    public void clickWebImage(String src, String ret) {
        if (src.startsWith("file:")) {
            // 如果下载则一定是下载的medium格式的文件
            String path = src.substring(5);
            mView.onNavigateToInspectImage(new ViewImageEvent(src, ret, path));
        } else {
            // 下载的时候如何查看 ?
            src = src.replace("small", "medium"); // 目前仅发现该
            mView.onNavigateToInspectImage(new ViewImageEvent(src, ret, null));
        }
    }
}
