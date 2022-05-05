package com.robam.roki.ui.page;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.ui.ext.views.ExtWebView;
import com.legent.utils.LogUtils;
import com.robam.common.util.RecipeRequestIdentification;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sylar on 15/7/6.
 */
public class RecipeDetail3rdPage extends BasePage {


    @InjectView(R.id.img_back)
    ImageView mImgBack;
    @InjectView(R.id.web_view)
    ExtWebView mWebView;
    String URL = "http://h5.myroki.com/dist/index.html#/douGuoDetail?cookbookId=";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_3rd_web_page, container, false);
        ButterKnife.inject(this, view);
        Bundle bundle = getArguments();
        initView();
        if (bundle != null) {
            long bookId = bundle.getLong(PageArgumentKey.BookId);
            String url = URL + bookId;
            String bdUrl = bundle.getString(PageArgumentKey.Url);
            if (TextUtils.isEmpty(bdUrl)){
                mWebView.loadUrl(url);
            }else {
                mWebView.loadUrl(bdUrl);
            }
        }

        return view;
    }

    private void initView() {
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        UIService.getInstance().popBack();
    }

}
