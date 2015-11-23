package com.blazers.jandan.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.*;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.models.db.local.LocalArticleHtml;
import com.blazers.jandan.models.db.local.LocalFavNews;
import com.blazers.jandan.models.db.sync.NewsPost;
import com.blazers.jandan.network.Parser;
import com.blazers.jandan.rxbus.event.ViewImageEvent;
import com.blazers.jandan.ui.activity.base.BaseActivity;
import com.blazers.jandan.util.DBHelper;
import com.blazers.jandan.util.SPHelper;
import com.blazers.jandan.util.ShareHelper;
import com.blazers.jandan.views.ObservableWebView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewsReadActivity extends BaseActivity {

    @Bind(R.id.toolbar_with_shadow) LinearLayout toolbarWrapper;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.webView) ObservableWebView webView;
    @Bind(R.id.progress_wheel) CircularProgressBar progressWheel;
    @Bind(R.id.fab_fav) FloatingActionButton fabFav;

    /* Vars for testing the scroll visible effect */
    private static final int HIDE_THRESHOLD = 256;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;
    private float webViewContentHeight, scale = 3.0f;
    private boolean isThisFaved;

    /* Vars */
    private NewsPost post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long id = getIntent().getLongExtra("id", -1);
        post = NewsPost.getPostById(realm, id);
        if (post == null || id == -1)
            finish();
        setContentView(R.layout.activity_news_read);
        ButterKnife.bind(this);
        /* Init Toolbar */
        initToolbarByTypeWithShadow(toolbarWrapper, toolbar, ToolbarType.FINISH);
        setToolbarTitle(getIntent().getStringExtra("title"));
        setContentFloatingModeEnabled(true);
        /* Fav */
        initFloatingActionButton();
        /* Init Appbar listener */
        initWebview();
        // 加载
        loadWebviewArticle();
        // Transition
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Explode explode = new Explode();
//            explode.setDuration(1000);
//            getWindow().setEnterTransition(explode);
//
//            Slide slide = new Slide();
//            slide.setDuration(1000);
//            getWindow().setReturnTransition(slide);
//        }
    }

    /**
     * 初始化FAV图标以及状态
     * */
    void initFloatingActionButton() {
        isThisFaved = LocalFavNews.isThisFaved(realm, post.getId());
        fabFav.setImageResource(isThisFaved ? R.drawable.ic_favorite_white_24dp : R.drawable.ic_favorite_border_white_24dp);
        fabFav.setOnClickListener(v -> {
            LocalFavNews.setThisFavedOrNot(isThisFaved = !isThisFaved, realm, post.getId());
            fabFav.animate().rotationX(fabFav.getRotationX() == 0? 360 : 0).setDuration(300).start();
            fabFav.postDelayed(() -> fabFav.setImageResource(isThisFaved ? R.drawable.ic_favorite_white_24dp : R.drawable.ic_favorite_border_white_24dp), 150);
        });
    }

    void initWebview() {
        webView.setListener(((left, top, oldLeft, oldTop) -> {
            // 滚动到底部 显示
            webViewContentHeight = webView.getContentHeight() * scale;
            float webViewCurrentHeight = (webView.getHeight() + webView.getScrollY());
            Log.i("WB", "WB Content Height ->" + webViewContentHeight + "   Current ->" + webViewCurrentHeight);
            if ((webViewContentHeight - webViewCurrentHeight) == 0) {
                showSystemUI(toolbar);
                fabFav.animate().translationY(-getNavigationBarHeight()).setDuration(300).start();
                controlsVisible = true;
                scrolledDistance = 0;
            }else {
                int distance = top - oldTop;
                // 向上滚动距离大于隐藏Trigger
                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    //hide
                    hideSystemUI(toolbar);
                    fabFav.animate().translationY(300).setDuration(300).start();
                    controlsVisible = false;
                    scrolledDistance = 0;
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    showSystemUI(toolbar);
                    fabFav.animate().translationY(-getNavigationBarHeight()).setDuration(300).start();
                    controlsVisible = true;
                    scrolledDistance = 0;
                }
                if((controlsVisible && distance>0) || (!controlsVisible && distance<0)) {
                    scrolledDistance += distance;
                }
            }
        }));
        // 进度条
        // 加载进度条
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            // 记录缩放值
            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                super.onScaleChanged(view, oldScale, newScale);
                scale = newScale;
            }

            // 判断模式
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (SPHelper.getBooleanSP(NewsReadActivity.this, SPHelper.NIGHT_MODE_ON, false)) {
                    webView.loadUrl("javascript:initWithCssType('night')");
                } else {
                    webView.loadUrl("javascript:initWithCssType('day')");
                }
            }
        });
        // 设置
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setLoadsImagesAutomatically(true);
        // JS交互
        webView.addJavascriptInterface(new JavaScript(), "blazers");
    }

    void loadWebviewArticle() {
        LocalArticleHtml articleHtml = realm.where(LocalArticleHtml.class).equalTo("id", post.getId()).findFirst();
        if (null != articleHtml && !articleHtml.getHtml().isEmpty()) {
            progressWheel.animate().alpha(0).translationY(-96).setStartDelay(200).setDuration(300).start();
            webView.loadDataWithBaseURL("file:///android_asset", articleHtml.getHtml(), "text/html; charset=UTF-8", null, null);
        } else {
            Parser parser = Parser.getInstance();
            parser.getNewsContentData(post.getId())
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
    }

    class JavaScript {
        @JavascriptInterface
        public void viewImageBySrc(String src, String alt) {
            Log.e("Src", src);
            src = src.replace("small", "medium"); // 目前仅发现该
            Intent intent = new Intent(NewsReadActivity.this, ImageViewerActivity.class);
            ViewImageEvent event = new ViewImageEvent(src, alt);
            intent.putExtra(ViewImageEvent.KEY, event);
            startActivity(intent);
        }

        /**
         * 更换所有Img的Src为本地file
         * */
        @JavascriptInterface
        public String replaceSrcToLocalFile(String src) {
            return "file://aa";
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_read, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_share:
                ShareHelper.shareWebPage(this, post.getTitle(), post.getUrl());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
