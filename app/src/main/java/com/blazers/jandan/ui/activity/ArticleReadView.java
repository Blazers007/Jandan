package com.blazers.jandan.ui.activity;

/**
 * Created by blazers on 2016/11/11.
 */

public interface ArticleReadView {

    void setFavIconFavOrNot(boolean favOrNot);

    void showHtmlPageByString(String htmlString);

    void showLoadingProgress();

    void hideLoadingProgress();

}
