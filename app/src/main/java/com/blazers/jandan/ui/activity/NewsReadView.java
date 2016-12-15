package com.blazers.jandan.ui.activity;

import com.blazers.jandan.model.event.ViewImageEvent;

/**
 * Created by blazers on 2016/11/11.
 */

public interface NewsReadView {

    void onSetFavoriteIconState(boolean favOrNot);

    void onAnimateToFavoriteOrNot(boolean favOrNot);

    void onLoadHtmlString(String htmlString);

    void onNavigateToInspectImage(ViewImageEvent viewImageEvent);

    void onShowLoadingProgress();

    void onHideLoadingProgress();

}
