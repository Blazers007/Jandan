package com.blazers.jandan.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.blazers.jandan.R;
import com.blazers.jandan.model.event.ViewImageEvent;
import com.blazers.jandan.model.news.NewsPage;
import com.blazers.jandan.presenter.NewsReadPresenter;
import com.blazers.jandan.ui.activity.base.BaseActivity;
import com.blazers.jandan.util.SPHelper;
import com.blazers.jandan.util.ShareHelper;
import com.blazers.jandan.widgets.ObservableWebView;

import butterknife.BindView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static com.blazers.jandan.presenter.NewsReadPresenter.KEY_NEWS_POST;

public class NewsReadActivity extends BaseActivity<NewsReadPresenter> implements NewsReadView {

    /* Vars for testing the scroll visible effect */
    private static final int HIDE_THRESHOLD = 256;
    @BindView(R.id.toolbar_with_shadow)
    LinearLayout toolbarWrapper;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.webView)
    ObservableWebView webView;
    @BindView(R.id.progress_wheel)
    MaterialProgressBar progressWheel;
    @BindView(R.id.fab_fav)
    FloatingActionButton fabFav;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;
    private float webViewContentHeight, scale = 3.0f;


    @Override
    public void initPresenter() {
        // 获取Model层
        Object obj = getIntent().getSerializableExtra(KEY_NEWS_POST);
        if (obj == null) {
            finish();
        }
        mPresenter = new NewsReadPresenter(this, (NewsPage.Posts) obj);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_read);
        /* Init Toolbar */
        initToolbarByTypeWithShadow(toolbarWrapper, toolbar, ToolbarType.FINISH);
        setToolbarTitle(getIntent().getStringExtra("title"));
        setContentFloatingModeEnabled(true);
        /* Fav */
        initFloatingActionButton();
        /* Init Appbar listener */
        initWebview();
        // 加载
        mPresenter.onInitWebPage();
    }

    /**
     * 初始化FAV图标以及状态
     */
    void initFloatingActionButton() {
        fabFav.setOnClickListener(v -> {
            // 点击收藏
            mPresenter.onClickFavoriteButton();
        });
    }

    /**
     * 初始化WebView
     */
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
            } else {
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
                // 累加滚动距离
                if ((controlsVisible && distance > 0) || (!controlsVisible && distance < 0)) {
                    scrolledDistance += distance;
                }
            }
        }));
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_read, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_comment:
                // 跳转到评论页面
                break;
            case R.id.action_share:
//                ShareHelper.shareWebPage(getContext(), mPost.title, mPost.url);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setFavIconFavOrNot(boolean favOrNot) {
        fabFav.setImageResource(favOrNot ? R.drawable.ic_favorite_white_24dp : R.drawable.ic_favorite_border_white_24dp);
    }

    @Override
    public void animateToFavOrNot(boolean favOrNot) {
        fabFav.animate().rotationX(fabFav.getRotationX() == 0 ? 360 : 0).setDuration(300).start();
        fabFav.postDelayed(() -> setFavIconFavOrNot(favOrNot), 300);
    }

    @Override
    public void showHtmlPageByString(String htmlString) {
        webView.loadDataWithBaseURL("file:///android_asset", htmlString, "text/html; charset=UTF-8", null, null);
    }

    @Override
    public void showLoadingProgress() {
        progressWheel.animate().alpha(0).translationY(-96).setStartDelay(200).setDuration(300).start();

    }

    @Override
    public void hideLoadingProgress() {
        progressWheel.setTranslationY(0);
        progressWheel.setAlpha(1);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_out_backin, R.anim.activity_slide_right_in_backout);
    }

    class JavaScript {
        @JavascriptInterface
        public void viewImageBySrc(String src, String alt) {
            Log.e("Src", src);
            src = src.replace("small", "medium"); // 目前仅发现该
            Intent intent = new Intent(NewsReadActivity.this, ImageInspectActivity.class);
            ViewImageEvent event = new ViewImageEvent(src, alt);
            intent.putExtra(ViewImageEvent.KEY, event);
            startActivity(intent);
        }

        /**
         * 更换所有Img的Src为本地file
         */
        @JavascriptInterface
        public String replaceSrcToLocalFile(String src) {
            return "file://aa";
        }
    }
}
