package com.blazers.jandan.ui.activity;

import com.blazers.jandan.model.event.ViewCommentEvent;
import com.blazers.jandan.model.event.ViewArticleEvent;
import com.blazers.jandan.model.event.ViewImageEvent;

/**
 * Created by blazers on 2016/11/11.
 *
 * MainActivity的View层
 *
 */

public interface MainView {

    void showBottomNavigationView();

    void hideBottomNavigationView();

//    void gotoCommentActivity(ViewCommentEvent commentEvent);
//
//    void gotoViewArticleActivity(ViewArticleEvent viewArticleEvent);
//
//    void gotoViewImageActivity(ViewImageEvent viewImageEvent);

}
