//package com.robam.roki.ui.page;
//
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import androidx.annotation.Nullable;
//import android.text.TextUtils;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.WebChromeClient;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.legent.Callback;
//import com.legent.Helper;
//import com.legent.plat.Plat;
//import com.legent.plat.pojos.User;
//import com.legent.ui.UIService;
//import com.legent.ui.ext.BasePage;
//import com.legent.ui.ext.dialogs.ProgressDialogHelper;
//import com.legent.ui.ext.dialogs.ProgressDialog;
//import com.legent.ui.ext.dialogs.ProgressDialogHelper;
//import com.legent.ui.ext.dialogs.ProgressNewDialogHelper;
//import com.legent.utils.EventUtils;
//import com.legent.utils.LogUtils;
//import com.robam.common.events.OrderChatEvent;
//import com.robam.common.io.cloud.Reponses;
//import com.robam.common.io.cloud.RokiRestHelper;
//import com.robam.common.pojos.Token;
//import com.robam.common.services.CookbookManager;
//import com.robam.common.services.StoreService;
//import com.robam.roki.R;
//import com.robam.roki.ui.PageArgumentKey;
//import com.robam.roki.utils.YouzanUrlUtils;
//import com.youzan.sdk.YouzanToken;
//import com.youzan.sdk.event.AbsAuthEvent;
//import com.youzan.sdk.web.plugin.YouzanBrowser;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import butterknife.OnClick;
//
///**
// * Created by Administrator on 2017/6/26.
// */
//
//public class YouzanPage extends BasePage {
//
//    @InjectView(R.id.img_youzan_back)
//    protected ImageView mImgYouzanBack;
//    @InjectView(R.id.youzan_browser)
//    protected YouzanBrowser mYouzanBrowser;
//    @InjectView(R.id.tv_title)
//    protected TextView mTvTitle;
//    protected String pageUrl;
//    protected  Bundle bundle;
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.page_youzan, container, false);
//        ButterKnife.inject(this, view);
//        EventUtils.regist(this);
//        bundle = getArguments();
//        pageUrl = bundle == null ? null : bundle.getString("pageUrl");
//        initView();
//        return view;
//    }
//
//    //初始化界面
//    protected void initView() {
//        String type;
//        final long currentUserId = Plat.accountService.getCurrentUserId();
//        User user = Plat.accountService.getCurrentUser();
//        String phone = null;
//        if (currentUserId == 0) {
//            type = "0";
//        } else {
//            type = "1";
//            phone = user.phone;
//        }
//        final String finalPhone = phone;
//        RokiRestHelper.getYouzanDetailContent(currentUserId, type, finalPhone,
//                new Callback<Reponses.TokenResponses>() {
//                    @Override
//                    public void onSuccess(Reponses.TokenResponses tokenResponses) {
//                        mYouzanBrowser.loadUrl(pageUrl);
//                        WebSettings settings = mYouzanBrowser.getSettings();
//                        settings.setJavaScriptEnabled(true);
//                        mYouzanBrowser.setWebChromeClient(new WebChromeClient() {
//                        });
//                        mYouzanBrowser.setWebViewClient(new WebViewClient() {
//                            @Override
//                            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                                super.onPageStarted(view, url, favicon);
//                                ProgressDialogHelper.setRunning(cx, true);
//                            }
//
//                            @Override
//                            public void onPageFinished(WebView view, String url) {
//                                super.onPageFinished(view, url);
//                                String title = view.getTitle();
//                                if (!TextUtils.isEmpty(title)) {
//                                    mTvTitle.setText(title);
//                                } else {
//                                    mTvTitle.setText(R.string.roki_cooking);
//                                }
//                                ProgressDialogHelper.setRunning(cx, false);
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                    }
//                });
//
//
//
//    }
//
//    //头部返回事件
//    @OnClick(R.id.img_youzan_back)
//    public void onViewClicked() {
//        if (mYouzanBrowser.canGoBack()) {
//            mYouzanBrowser.goBack();
//        } else {
//            postEvent(new OrderChatEvent());
//            UIService.getInstance().popBack();
//        }
//    }
//
//    //系统返回键处理
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && mYouzanBrowser.canGoBack()) {
//            mYouzanBrowser.goBack();
//        } else {
//            postEvent(new OrderChatEvent());
//            UIService.getInstance().popBack();
//        }
//        return true;
//    }
//
//    //页面销毁
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.reset(this);
//    }
//
//
//}
