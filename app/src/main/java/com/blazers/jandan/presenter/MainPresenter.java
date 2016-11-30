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
    }

    @Override
    public void onResume() {
        super.onResume();
        // 添加订阅事件
        addFUISubscription(subScribeRxBusEventOnUiThread(ViewCommentEvent.class).subscribe(mView::gotoCommentActivity));
        addFUISubscription(subScribeRxBusEventOnUiThread(ViewArticleEvent.class).subscribe(mView::gotoViewArticleActivity));
        addFUISubscription(subScribeRxBusEventOnUiThread(ViewImageEvent.class).subscribe(mView::gotoViewImageActivity));
    }

    @Override
    public void onDestory() {
        super.onDestory();
        DBHelper.releaseAllTempRealm();
    }
}
