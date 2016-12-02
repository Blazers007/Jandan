package com.blazers.jandan.ui.activity;

/**
 * Created by blazers on 2016/11/11.
 */

public interface NewsReadView {

    void setFavIconFavOrNot(boolean favOrNot);

    void animateToFavOrNot(boolean favOrNot);

    void showHtmlPageByString(String htmlString);

    void showLoadingProgress();

    void hideLoadingProgress();

}
