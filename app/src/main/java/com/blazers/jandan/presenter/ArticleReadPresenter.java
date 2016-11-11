package com.blazers.jandan.presenter;

import android.content.Context;
import android.util.Log;

import com.blazers.jandan.models.db.local.LocalArticleHtml;
import com.blazers.jandan.models.db.local.LocalFavNews;
import com.blazers.jandan.models.db.sync.NewsPost;
import com.blazers.jandan.network.Parser;
import com.blazers.jandan.presenter.base.BasePresenter;
import com.blazers.jandan.ui.activity.ArticleReadView;
import com.blazers.jandan.util.DBHelper;
import com.blazers.jandan.util.rxbus.event.ViewArticleEvent;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by blazers on 2016/11/11.
 */

public class ArticleReadPresenter extends BasePresenter<ArticleReadView> {

    private NewsPost mNewsPost;

    public ArticleReadPresenter(ArticleReadView view, Context context) {
        super(view, context);
        // 获取Model层
        long id = getIntent().getLongExtra(ViewArticleEvent.KEY, -1);
        mNewsPost = NewsPost.getPostById(mRealm, id);
        if (mNewsPost == null || id == -1)
            getActivity().finish();
    }

    public void loadHtmlString() {
        LocalArticleHtml articleHtml = mRealm.where(LocalArticleHtml.class).equalTo("id", mNewsPost.getId()).findFirst();
        if (null != articleHtml && !articleHtml.getHtml().isEmpty()) {

            mView.showHtmlPageByString(articleHtml.getHtml());
        } else {
            mView.showLoadingProgress();
            Parser parser = Parser.getInstance();
            parser.getNewsContentData(mNewsPost.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            localArticleHtml -> {
                                DBHelper.saveToRealm(mRealm, localArticleHtml);
                                mView.showHtmlPageByString(localArticleHtml.getHtml());
                                mView.hideLoadingProgress();
                            },
                            throwable -> Log.e("err", throwable.toString())
                    );
        }
    }

    public boolean isThisArticleMyFav() {
        return LocalFavNews.isThisFaved(mRealm, mNewsPost.getId());
    }

    public void toggleThisArticleFavState() {
        LocalFavNews.setThisFavedOrNot(!isThisArticleMyFav(), mRealm, mNewsPost.getId());
    }


}
