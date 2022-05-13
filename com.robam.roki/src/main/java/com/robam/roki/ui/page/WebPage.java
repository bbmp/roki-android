//package com.robam.roki.ui.page;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import androidx.annotation.Nullable;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.JavascriptInterface;
//import android.webkit.WebChromeClient;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.legent.ui.UIService;
//import com.legent.ui.ext.BasePage;
//import com.legent.ui.ext.dialogs.ProgressDialogHelper;
//import com.legent.ui.ext.views.ExtWebView;
//import com.robam.common.ui.UiHelper;
//import com.robam.roki.R;
//import com.robam.roki.ui.PageKey;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import butterknife.OnClick;
//
///**
// * Created by Administrator on 2017/7/31.
// * 有赞活动页面
// */
//
//public class WebPage extends BasePage {
//
//    final String KITCHEN_SOURCE_H5_URL = "http://develop.h5.myroki.com/#/yanzanActivity";
//
//    @InjectView(R.id.web)
//    ExtWebView web;
//    @InjectView(R.id.iv_back)
//    ImageView ivBack;
//    @InjectView(R.id.tv_title)
//    TextView tvTitle;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.page_web, container, false);
//        ButterKnife.inject(this, view);
//        setiingWebToJS();
//        initPage();
//        return view;
//    }
//
//    private void initPage() {
//        web.loadUrl(KITCHEN_SOURCE_H5_URL);
//    }
//
//
//    private void setiingWebToJS() {
//
//        WebSettings settings = web.getSettings();
//        settings.setJavaScriptEnabled(true);
//        web.addJavascriptInterface(new CallMethodFromAndroidLister(cx), "RecipeDetail");
//        web.setWebChromeClient(new WebChromeClient());
//        web.setWebViewClient(new WebViewClient() {
//
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                ProgressDialogHelper.setRunning(cx, true);
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                ProgressDialogHelper.setRunning(cx, false);
//            }
//        });
//    }
//
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            UIService.getInstance().returnHome();
//        }
//        return true;
//    }
//
//
//    @OnClick(R.id.iv_back)
//    public void onViewClicked() {
//        UIService.getInstance().returnHome();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.reset(this);
//    }
//
//    private class CallMethodFromAndroidLister {
//        Context context;
//
//        public CallMethodFromAndroidLister(Context context) {
//            this.context = context;
//        }
//
//        /**
//         * 商品详情页
//         */
//        @JavascriptInterface
//        public void toYeGo(String url) {
//
//            if (UiHelper.checkAuthWithDialog(cx, PageKey.UserLogin)) {
//
//                Bundle bd = new Bundle();
//                bd.putString("url", url);
//                UIService.getInstance().postPage(PageKey.YouzanOrderDetails, bd);
//            }
//
//        }
//
//    }
//}
