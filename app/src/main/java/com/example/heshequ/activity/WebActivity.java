package com.example.heshequ.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blankj.utilcode.util.SizeUtils;
import com.example.heshequ.R;
import com.example.heshequ.base.NetWorkActivity;
import com.example.heshequ.view.ProgressView;

import org.json.JSONException;
import org.json.JSONObject;


public class WebActivity extends NetWorkActivity {
    private WebView mWebView;
    private ProgressView progressView;//进度条
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        url = getIntent().getStringExtra("url");
        mWebView = (WebView) findViewById(R.id.webview);
        initWebView();
    }

    @Override
    protected void onSuccess(JSONObject result, int where, boolean fromCache) throws JSONException {

    }

    @Override
    protected void onFailure(String result, int where) {

    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void initWebView() {
        //初始化进度条
        progressView = new ProgressView(this);
        progressView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(4)));
        progressView.setProgress(0);
        //把进度条加到Webview中
        mWebView.addView(progressView);
        mWebView.setWebChromeClient(new MyWebChromeClient());

        mWebView.getSettings().setUseWideViewPort(true);// 设置将图片适应webview
        //1、LayoutAlgorithm.NARROW_COLUMNS 适应内容大小
        //2、LayoutAlgorithm.SINGLE_COLUMN 适应屏幕，内容将自动缩放
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().supportMultipleWindows();
        mWebView.getSettings().setAllowContentAccess(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setSaveFormData(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView wv, String url) {
                if (url == null) return false;

                try {
                    if (url.startsWith("weixin://") //微信
                            || url.startsWith("alipays://") //支付宝
                            || url.startsWith("mailto://") //邮件
                            || url.startsWith("tel://")//电话
                            || url.startsWith("dianping://")//大众点评
                            || url.startsWith("baiduboxlite://")
                            || url.startsWith("baidumap://")
                        //其他自定义的scheme
                    ) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                }

                //处理http和https开头的url
                wv.loadUrl(url);
                return true;
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                //加载完毕进度条消失
                progressView.setVisibility(View.GONE);
            } else {
                //更新进度
                progressView.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.setWebChromeClient(null);
        mWebView.setWebViewClient(null);
        mWebView.getSettings().setJavaScriptEnabled(false);
        mWebView.clearCache(true);
        mWebView.removeAllViews();
        mWebView.destroy();
    }


}
