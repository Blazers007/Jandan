package com.blazers.jandan.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.models.db.local.LocalArticleHtml;
import com.blazers.jandan.models.db.sync.NewsPost;
import com.blazers.jandan.network.Parser;
import com.blazers.jandan.ui.activity.base.BaseActivity;
import com.blazers.jandan.util.DBHelper;
import com.blazers.jandan.util.ShareHelper;
import com.blazers.jandan.views.ObservableWebView;
import com.pnikosis.materialishprogress.ProgressWheel;
import io.realm.Realm;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewsReadActivity extends BaseActivity {

    /* Realm数据库 */
    private Realm realm;

    @Bind(R.id.toolbar_with_shadow) LinearLayout toolbarWrapper;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.webView) ObservableWebView webView;
    @Bind(R.id.progress_wheel) ProgressWheel progressWheel;

    /* Vars for testing the scroll visible effect */
    private static final int HIDE_THRESHOLD = 256;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;

    /* Vars */
    private NewsPost post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getInstance(this);
        long id = getIntent().getLongExtra("id", -1);
        post = NewsPost.getPostById(realm, id);
        if (post == null)
            finish();
        setContentView(R.layout.activity_news_read);
        ButterKnife.bind(this);

        /* Init Toolbar */
        initToolbarByTypeWithShadow(toolbarWrapper, toolbar, ToolbarType.FINISH);
        setToolbarTitle(getIntent().getStringExtra("title"));
        setContentFloatingModeEnabled(true);
        /* Init Appbar listener */
        webView.setListener(((left, top, oldLeft, oldTop) -> {
            int distance = top - oldTop;
            if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                //hide
                hideNavigationBar(toolbar);
                controlsVisible = false;
                scrolledDistance = 0;
            } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                showSystemUI(toolbar);
                controlsVisible = true;
                scrolledDistance = 0;
            }
            if((controlsVisible && distance>0) || (!controlsVisible && distance<0)) {
                scrolledDistance += distance;
            }
        }));
        /* 更合理的提示与判断 */
        if (id == -1)
            finish();
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setLoadsImagesAutomatically(true);
        // 查看有无本地缓存
        LocalArticleHtml articleHtml = realm.where(LocalArticleHtml.class).equalTo("id", post.getId()).findFirst();
        if (null != articleHtml && !articleHtml.getHtml().isEmpty()) {
            progressWheel.animate().alpha(0).translationY(-96).setStartDelay(200).setDuration(300).start();
            webView.loadDataWithBaseURL("file:///android_asset", articleHtml.getHtml(), "text/html; charset=UTF-8", null, null);
        } else {
            Parser parser = Parser.getInstance();
            parser.getNewsContentData(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    localArticleHtml -> {
                        DBHelper.saveToRealm(realm, localArticleHtml);
                        progressWheel.animate().alpha(0).translationY(-96).setStartDelay(200).setDuration(300).start();
                        webView.loadDataWithBaseURL("file:///android_asset", localArticleHtml.getHtml(), "text/html; charset=UTF-8", null, null);
                    },
                    throwable -> Log.e("err", throwable.toString())
                );
        }

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.loadUrl("javascript:loadCssFile('day')");

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_read, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                webView.loadUrl("javascript:loadCssFile('night')");
                return true;
            case R.id.action_share:
                ShareHelper.shareWebPage(this, post.getTitle(), post.getUrl());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
