package com.robam.roki.ui.form;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.legent.utils.LogUtils;
import com.robam.roki.R;

/**
 * Created by yinwei on 2017/8/14.
 */

public class VideoActivity extends Activity{
    private WebView webView;
    private FrameLayout video_fullView;// 全屏时视频加载view
    private View xCustomView;
    private ProgressDialog waitdialog = null;
    private WebChromeClient.CustomViewCallback xCustomViewCallback;
 //   private myWebChromeClient xwebchromeclient;
    String vid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉应用标题
        setContentView(R.layout.recipe_live_new);
        Intent intent =getIntent();
        vid = intent.getStringExtra("vid");
        LogUtils.i("20170814","vid"+vid);
        waitdialog = new ProgressDialog(this);
       // waitdialog.setTitle("提示");
        waitdialog.setMessage("视频页面加载中...");
        waitdialog.setIndeterminate(true);
        waitdialog.setCancelable(true);
        waitdialog.show();

        webView = findViewById(R.id.weblive);
        video_fullView = findViewById(R.id.video_fullView);

        setiingWebToJS();
        webView.setWebViewClient(new myWebViewClient());
        webView.loadUrl("http://h5.myroki.com/#/cookbookVideo?videoUrl="+vid);
    }



    private void setiingWebToJS() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
    }

    public class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            waitdialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            VideoActivity.this.finish();
        }
        return false;
    }
}

