package com.robam.roki.ui.page;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.ui.ext.views.ExtWebView;
import com.legent.utils.LogUtils;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/1/23.
 */

public class MoreCookPage extends BasePage {
    @InjectView(R.id.more_imgreturn)
    ImageView more_imgreturn;
    @InjectView(R.id.title)
    TextView title;
    ExtWebView webView;
    //测试服务器
//    private final String H5_BASE_URL = "http://develop.h5.myroki.com/dist/index.html#/moreCook";
    //正式服务器
    private final String H5_BASE_URL = "http://h5.myroki.com/dist/index.html#/moreCook";
    String cookBookId;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle save) {

        cookBookId = getArguments().getString("cookBookId");
        View view = inflater.inflate(R.layout.more_cook_page, null, false);
        webView = view.findViewById(R.id.more_webview);
        ButterKnife.inject(this, view);
        setiingWebToJS();
        initUrl();
        return view;
    }

    @OnClick(R.id.more_imgreturn)
    public void onClick(){
        UIService.getInstance().popBack();
    }

    private void initUrl() {
        long userId = Plat.accountService.getCurrentUserId();
        if (userId == 0) {
            webView.loadUrl(H5_BASE_URL + "?cookbookId=" +cookBookId);
        } else {
            webView.loadUrl(H5_BASE_URL + "?userId=" + userId + "&cookbookId=" + cookBookId);
            LogUtils.i("20181107","url:"+H5_BASE_URL + "?userId=" + userId + "&cookbookId=" + cookBookId);
            webView.reload();
        }
    }

    private void setiingWebToJS() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                ProgressDialogHelper.setRunning(cx, true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                ProgressDialogHelper.setRunning(cx, false);
            }
        });
    }
}
