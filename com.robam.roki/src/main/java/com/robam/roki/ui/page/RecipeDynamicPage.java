//package com.robam.roki.ui.page;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import androidx.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.JavascriptInterface;
//import android.webkit.WebChromeClient;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//
//import com.google.common.eventbus.Subscribe;
//import com.legent.events.ConnectionModeChangedEvent;
//import com.legent.plat.Plat;
//import com.legent.plat.events.UserLoginEvent;
//import com.legent.plat.pojos.User;
//import com.legent.ui.UIService;
//import com.legent.ui.ext.BasePage;
//import com.legent.ui.ext.dialogs.DialogHelper;
//import com.legent.ui.ext.dialogs.ProgressDialogHelper;
//import com.legent.ui.ext.views.ExtWebView;
//import com.legent.utils.EventUtils;
//import com.legent.utils.LogUtils;
//import com.robam.common.util.RecipeRequestIdentification;
//import com.robam.roki.R;
//import com.robam.roki.ui.PageKey;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//
///**
// * Created by Administrator on 2017/8/1
// * 动态关注H5
// */
//
//public class RecipeDynamicPage extends BasePage {
//
//    final String DYNAMIC_H5_URL = "http://develop.h5.myroki.com/#/dynamic";
//    @InjectView(R.id.webView_dynamic)
//    ExtWebView webViewDynamic;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.page_recipe_dynamic, container, false);
//        ButterKnife.inject(this, view);
//        EventUtils.regist(this);
//        setiingWebToJS();
//        initPage();
//        return view;
//    }
//
//    private void setiingWebToJS() {
//
//        WebSettings settings = webViewDynamic.getSettings();
//        settings.setJavaScriptEnabled(true);
//        webViewDynamic.addJavascriptInterface(new CallMethodFromAndroidLister(cx), "RecipeDetail");
//        webViewDynamic.setWebChromeClient(new WebChromeClient());
//        webViewDynamic.setWebViewClient(new WebViewClient() {
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
//
//
//        });
//    }
//
//     void initPage() {
//         boolean logon = Plat.accountService.isLogon();
//        long userId = Plat.accountService.getCurrentUserId();
//        if (logon){
//            webViewDynamic.loadUrl(DYNAMIC_H5_URL+"?userId="+userId);
//        }else {
//            webViewDynamic.loadUrl(DYNAMIC_H5_URL);
//        }
//    }
//
//    @Subscribe
//    public void onEvent(UserLoginEvent event) {
//        UIService.getInstance().popBack();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.reset(this);
//    }
//
//    public class CallMethodFromAndroidLister {
//        Context context;
//
//        public CallMethodFromAndroidLister(Context context) {
//            this.context = context;
//        }
//
//        /**
//         * 返回方法
//         */
//        @JavascriptInterface
//        public void goback(boolean b) {
//            UIService.getInstance().popBack();
//        }
//
//        @JavascriptInterface
//        public void userIsNotLog(boolean notLog){
//            if (notLog) {
//                DialogHelper.newDialog_OkCancel(cx, "您还未登录，快去登录吧~", null,
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                if (which == DialogInterface.BUTTON_POSITIVE) {
//                                    UIService.getInstance().postPage(PageKey.UserLogin);
//                                }
//                            }
//                        }).show();
//            }
//        }
//
//        @JavascriptInterface
//        public void cookDetail(String cookbookId){
//            LogUtils.i("20170802","cookbookId:"+cookbookId);
//            if (cookbookId != null)
//                RecipeDetailPage.show(Long.parseLong(cookbookId),
//                        RecipeDetailPage.DynamicRecipeShow, RecipeRequestIdentification.RECIPE_SHARE_CULINARY_SKILL);
//
//        }
//
//    }
//}
