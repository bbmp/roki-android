//package com.robam.roki.ui.page.device;
//
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.webkit.WebChromeClient;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.ImageView;
//
//import com.legent.plat.Plat;
//import com.legent.ui.UIService;
//import com.legent.ui.ext.BasePage;
//import com.legent.ui.ext.dialogs.ProgressDialogHelper;
//import com.legent.ui.ext.views.ExtWebView;
//import com.robam.roki.MobApp;
//import com.robam.roki.R;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import butterknife.OnClick;
//
///**
// * Created by RuanWei on 2019/9/30.
// */
//
//public class InstructionsPage extends BasePage {
//
//    final String INSTRUCTIONS_URL = "http://h5.myroki.com/oil/voiceControl.html";//链接
//
//    @InjectView(R.id.ev_instructions)
//    ExtWebView evInstructions;
//    @InjectView(R.id.img_back)
//    ImageView img_back;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
//                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        View view = inflater.inflate(R.layout.page_instructions, container, false);
//        ButterKnife.inject(this, view);
//        setiingWebToJS();
//        initPage();
//        return view;
//    }
//
//    private void initPage() {
//        boolean logon = Plat.accountService.isLogon();
//        Long userId = Plat.accountService.getCurrentUserId();
//        evInstructions.loadUrl(INSTRUCTIONS_URL);
////        if (logon) {
////            evInstructions.loadUrl(INSTRUCTIONS_URL + "?userId=" + userId);
////        } else {
////            evInstructions.loadUrl(INSTRUCTIONS_URL);
////        }
//    }
//
//    private void setiingWebToJS() {
//        WebSettings settings = evInstructions.getSettings();
//        settings.setJavaScriptEnabled(true);
////        evInstructions.addJavascriptInterface();
//        evInstructions.setWebChromeClient(new WebChromeClient());
//        evInstructions.setWebViewClient(new WebViewClient() {
//
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                ProgressDialogHelper.setRunning(cx, true);
//
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
//    public void onResume() {
//        super.onResume();
//    }
//
//    @OnClick(R.id.img_back)
//    public void onClick() {
//        UIService.getInstance().popBack();
//    }
//}
