package com.blazers.jandan.presenter;

import android.content.Context;
import android.util.Log;

import com.blazers.jandan.model.database.local.LocalArticleHtml;
import com.blazers.jandan.model.database.local.LocalFavNews;
import com.blazers.jandan.model.database.local.LocalImage;
import com.blazers.jandan.model.database.sync.OldNewsPost;
import com.blazers.jandan.model.DataManager;
import com.blazers.jandan.presenter.base.BasePresenter;
import com.blazers.jandan.ui.activity.ArticleReadView;
import com.blazers.jandan.util.DBHelper;
import com.blazers.jandan.model.event.ViewArticleEvent;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by blazers on 2016/11/11.
 */

public class ArticleReadPresenter extends BasePresenter<ArticleReadView> {

    private OldNewsPost mNewsPost;

    public ArticleReadPresenter(ArticleReadView view, Context context) {
        super(view, context);
        // 获取Model层
        long id = getIntent().getLongExtra(ViewArticleEvent.KEY, -1);
        mNewsPost = OldNewsPost.getPostById(getRealm(), id);
        if (mNewsPost == null || id == -1)
            getActivity().finish();
    }

    public void loadHtmlString() {
        LocalArticleHtml articleHtml = getRealm().where(LocalArticleHtml.class).equalTo("id", mNewsPost.getId()).findFirst();
        if (null != articleHtml && !articleHtml.getHtml().isEmpty()) {

            mView.showHtmlPageByString(articleHtml.getHtml());
        } else {
            mView.showLoadingProgress();
            DataManager parser = DataManager.getInstance();
            parser.getNewsContentData(mNewsPost.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            localArticleHtml -> {
                                DBHelper.saveToRealm(getRealm(), localArticleHtml);
                                mView.showHtmlPageByString(localArticleHtml.getHtml());
                                mView.hideLoadingProgress();
                            },
                            throwable -> Log.e("err", throwable.toString())
                    );
        }
    }

    public String getLocalCachedResourceByUrl(String url) {
        LocalImage localImage = LocalImage.getLocalImageByWebUrl(getRealm(), url);
        if (localImage != null) {
            return localImage.getLocalUrl();
        }
        return null;
    }

    public boolean isThisArticleMyFav() {
        return LocalFavNews.isThisFaved(getRealm(), mNewsPost.getId());
    }

    public void toggleThisArticleFavState() {
        LocalFavNews.setThisFavedOrNot(!isThisArticleMyFav(), getRealm(), mNewsPost.getId());
    }



}
