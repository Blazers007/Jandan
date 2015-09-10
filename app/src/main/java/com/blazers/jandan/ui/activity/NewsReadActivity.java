package com.blazers.jandan.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.blazers.jandan.R;
import com.blazers.jandan.network.JandanParser;
import com.blazers.jandan.ui.activity.base.BaseActivity;
import com.blazers.jandan.views.widget.ObservableWebView;

public class NewsReadActivity extends BaseActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.webView) ObservableWebView webView;

    /* Vars for testing the scroll visible effect */
    private static final int HIDE_THRESHOLD = 256;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_read);
        ButterKnife.bind(this);
        /* Init Toolbar */
        initToolbarByType(toolbar, ToolbarType.FINISH);
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
        /* Init Webview */
        long id = getIntent().getLongExtra("id", -1);
        /* 更合理的提示与判断 */
        if (id == -1)
            finish();
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setLoadsImagesAutomatically(true);

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                return JandanParser.getInstance().parseNewsContent(id);
            }

            @Override
            protected void onPostExecute(String s) {
                webView.loadDataWithBaseURL("file:///android_asset", s, "text/html; charset=UTF-8", null, null);
            }
        }.execute();

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
        if (id == R.id.action_settings) {
            webView.loadUrl("javascript:loadCssFile('night')");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
