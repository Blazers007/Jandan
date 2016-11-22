package com.blazers.jandan.presenter;

import android.content.Context;

import com.blazers.jandan.presenter.base.BasePresenter;
import com.blazers.jandan.ui.activity.MainView;
import com.blazers.jandan.util.DBHelper;
import com.blazers.jandan.model.event.ViewArticleEvent;
import com.blazers.jandan.model.event.ViewCommentEvent;
import com.blazers.jandan.model.event.ViewImageEvent;

/**
 * Created by blazers on 2016/11/11.
 *
 * MainActivity的Presenter层
 *
 */
public class MainPresenter extends BasePresenter<MainView> {

    public MainPresenter(MainView view, Context context) {
        super(view, context);
        // 添加订阅事件
        addUiSubscription(subScribeRxBusEventOnUiThread(ViewCommentEvent.class).subscribe(mView::gotoCommentActivity));
        addUiSubscription(subScribeRxBusEventOnUiThread(ViewArticleEvent.class).subscribe(mView::gotoViewArticleActivity));
        addUiSubscription(subScribeRxBusEventOnUiThread(ViewImageEvent.class).subscribe(mView::gotoViewImageActivity));
    }

    @Override
    public void release() {
        super.release();
        DBHelper.releaseAllTempRealm();
    }
}
