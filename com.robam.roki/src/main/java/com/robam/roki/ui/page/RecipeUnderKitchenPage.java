//package com.robam.roki.ui.page;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.WebChromeClient;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//
//import com.legent.ui.ext.BasePage;
//import com.legent.ui.ext.dialogs.ProgressDialogHelper;
//import com.legent.ui.ext.views.ExtWebView;
//import com.legent.utils.LogUtils;
//import com.robam.roki.R;
//import com.robam.roki.ui.PageArgumentKey;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//
//
///**
// * Created by 14807 on 2018/2/1.
// */
//
//public class RecipeUnderKitchenPage extends BasePage {
//
//    @InjectView(R.id.webview)
//    ExtWebView mWebview;
//    private String mUrl;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.page_underkitch, container, false);
//        ButterKnife.inject(this, view);
//        Bundle bundle = getArguments();
//        mUrl = bundle.getString(PageArgumentKey.Url);
//        setiingWebToJS();
//        initUrl();
//        return view;
//    }
//
//    private void initUrl() {
//        LogUtils.i("20180201","url:"+mUrl);
//        mWebview.loadUrl(mUrl);
//    }
//
//    private void setiingWebToJS() {
//
//        WebSettings settings = mWebview.getSettings();
//        settings.setJavaScriptEnabled(true);
////        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS); //图片自适应大小 NARROW_COLUMNS);// 排版适应屏幕
//        mWebview.addJavascriptInterface(new CallMethodFromAndroidLister(cx), "RecipeDetail");
//
//        mWebview.setWebChromeClient(new WebChromeClient());
//        mWebview.setWebViewClient(new WebViewClient(){
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
//
//        });
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.reset(this);
//    }
//
//    class CallMethodFromAndroidLister{
//        Context context;
//        public CallMethodFromAndroidLister(Context context) {
//            this.context = context;
//        }
//    }
//}
