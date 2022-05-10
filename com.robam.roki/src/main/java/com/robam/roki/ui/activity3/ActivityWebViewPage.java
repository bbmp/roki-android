package com.robam.roki.ui.activity3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.common.eventbus.Subscribe;
import com.google.common.io.BaseEncoding;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.UserLoginNewEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.ui.ext.views.ExtWebView;
import com.legent.utils.LogUtils;
import com.robam.common.events.WxCodeShareEvent;
import com.robam.common.services.CookbookManager;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.H5ActivityShareDialog;
import com.robam.roki.ui.dialog.KitchenSourceShareDialog;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;
import com.robam.roki.utils.ApiSecurityExample;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.PermissionsUtils;
import com.robam.roki.utils.ToolUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * H5活动页面
 */

public class ActivityWebViewPage extends MyBasePage<MainActivity> {

    @InjectView(R.id.ev_act_webview)
    ExtWebView evActWebview;
    @InjectView(R.id.title_item)
    RelativeLayout title_item;
    @InjectView(R.id.imgShare)
    ImageView imgShare;
    @InjectView(R.id.tv_cook_name)
    TextView tvTitle;

    public static String kitchenKnowledge_url = "https://h5.myroki.com/#/kitchenKnowledge";

    final String KIRCHEN_SOURCE_ACT_URL = "https://h5.myroki.com/#/chuYuanActivity";//分享链接

    private String url = "https://h5.myroki.com/#/kitchenKnowledge";

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    evActWebview.reload();
                    break;
            }
        }
    };
    private String mActiveUrl;
    private String mShareImgUrl;
    private String mShareTitle;
    private String mShareText;
    private String mVideoUrl;
    private String mShareId;
    private CmccLoginHelper instance;
    private String H5Key;
    private String loginEvenType = null;
    private boolean isLogin = false;
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        View view = inflater.inflate(R.layout.page_kitchen_knowledg, container, false);
//        ButterKnife.inject(this, view);
//        setiingWebToJS();
//        initPage();
//        return view;
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.page_activity_webview;
    }

    @Override
    protected void initView() {
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        setiingWebToJS();
//        initPage();

        instance = CmccLoginHelper.getInstance();
        instance.initSdk(activity);
        instance.getPhnoeInfo();
        evActWebview.clearHistory();
        url = getArguments().getString(PageArgumentKey.Url);
        H5Key = getArguments().getString(PageArgumentKey.H5Key);
        setiingWebToJS();
        initPage();
    }

    @OnClick({R.id.imgreturn, R.id.imgShare})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgreturn:
                if (evActWebview.canGoBack()) {
                    evActWebview.goBack();
                } else {
                    UIService.getInstance().popBack();
                }
                break;
            case R.id.imgShare:
                ShareLayer();
                break;

        }
    }

    @Override
    protected void initData() {
//        instance = CmccLoginHelper.getInstance();
//        instance.initSdk(activity);
//        instance.getPhnoeInfo();
//        evActWebview.clearHistory();
//        url = getArguments().getString(PageArgumentKey.Url);
//        H5Key = getArguments().getString(PageArgumentKey.H5Key);
//        setiingWebToJS();
//        initPage();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void initPage() {
        mShareImgUrl = getArguments().getString("Share_Img");
        String title = getArguments().getString(PageArgumentKey.title);

        boolean logon = Plat.accountService.isLogon();
        Long userId = Plat.accountService.getCurrentUserId();
        if (logon) {
            isLogin = true;
            if (H5Key != null && H5Key.equals("common_act")) {
                if (title != null) {
                    tvTitle.setText(title);
                }
                imgShare.setVisibility(View.VISIBLE);
//                evActWebview.loadUrl(url + "?userId=" + userId);
                evActWebview.loadUrl(url);

            } else if (H5Key != null && H5Key.equals("special_act")) {
                evActWebview.loadUrl(url + "?userId=" + userId + "&terminal=app");
            }
        } else {
            if (H5Key != null && H5Key.equals("common_act")) {
                if (title != null) {
                    tvTitle.setText(title);
                }
                imgShare.setVisibility(View.VISIBLE);
                evActWebview.loadUrl(url);
            } else if (H5Key != null && H5Key.equals("special_act")) {
                evActWebview.loadUrl(url + "?userId=" + "&terminal=app");
            }
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setiingWebToJS() {

        WebSettings settings = evActWebview.getSettings();
        settings.setJavaScriptEnabled(true);

        settings.setDomStorageEnabled(true);// 打开本地缓存提供JS调用,至关重要
        settings.setAppCacheMaxSize(1024 * 1024 * 8);// 实现8倍缓存
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);

        evActWebview.addJavascriptInterface(new CallMethodFromAndroidLister(cx), "RecipeDetail");

        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (tvTitle != null && H5Key != null && H5Key.equals("special_act")) {
                    tvTitle.setText(title);
                }
            }
        };
        evActWebview.setWebChromeClient(wvcc);

        evActWebview.setWebViewClient(new WebViewClient() {


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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        LogUtils.i("20170801", "event:" + event);

        if (keyCode == KeyEvent.KEYCODE_BACK && evActWebview.canGoBack()) {
            if (evActWebview != null) {
                evActWebview.goBack();// 返回前一个页面
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                Long shareUserId = Plat.accountService.getCurrentUserId();
                long timestamp = System.currentTimeMillis();
                String urlData = "shareUserId=" + shareUserId + "&timestamp=" + timestamp;
                String sign = ApiSecurityExample.hmacSha256ToURLEncoder("Robam@2022*03", urlData);
                String shareUrl = url + "?shareUserId=" + shareUserId + "&sign=" + sign + "&timestamp=" + timestamp;
                String contText;
                if (H5Key != null && H5Key.equals("special_act")) {
                    mShareTitle = "ROBAM x AWE | 你有一个惊喜盲盒待领！";
                    contText = "ROKI 喊你来拆惊喜盲盒，赢超多好礼。";
                } else {
                    mShareTitle = tvTitle.getText().toString();
                    contText = " ";
                }

                if (PermissionsUtils.CODE_KITCHEN_SHARE_ACTIVE == requestCode) {
                    if (H5Key != null && H5Key.equals("special_act")) {
                        H5ActivityShareDialog.show(cx, shareUrl, mShareImgUrl, mShareTitle, contText);

                    } else {
                        H5ActivityShareDialog.show(cx, url, mShareImgUrl, mShareTitle, contText);

                    }
                } else if (PermissionsUtils.CODE_KITCHEN_SHARE_VIDEO == requestCode) {
                    if (H5Key != null && H5Key.equals("special_act")) {
                        H5ActivityShareDialog.show(cx, shareUrl, mShareImgUrl, mShareTitle, contText);

                    } else {
                        H5ActivityShareDialog.show(cx, url, mShareImgUrl, mShareTitle, contText);

                    }
                }
            }
        }


    }

    class CallMethodFromAndroidLister {
        Context context;

        public CallMethodFromAndroidLister(Context context) {
            this.context = context;
        }

        /**
         * 返回方法
         */
        @JavascriptInterface
        public void goback(boolean b) {
            evActWebview.post(new Runnable() {
                @Override
                public void run() {
                    if (evActWebview.canGoBack()) {
                        evActWebview.goBack();
                    } else {
                        UIService.getInstance().popBack();
                    }
                }
            });

        }

        /**
         * 跳转到登录页面
         */
        @JavascriptInterface
        public void toAndroidLoginPage(final String type) {
            loginEvenType = type;
            startLogin();
        }

        /**
         * 跳转到留言咨询
         */
        @JavascriptInterface
        public void toAndroidLeaveMsg() {
            UIService.getInstance().postPage(PageKey.Chat);
        }

        /**
         * 分享活动
         */
        @JavascriptInterface
        public void androidShareLayer() {
            Long shareUserId = Plat.accountService.getCurrentUserId();
            long timestamp = System.currentTimeMillis();
            String urlData = "shareUserId=" + shareUserId + "&timestamp=" + timestamp;
            String sign = ApiSecurityExample.hmacSha256ToURLEncoder("Robam@2022*03", urlData);
            String shareUrl = url + "?userId=&shareUserId=" + shareUserId + "&terminal=null&sign=" + sign + "&timestamp=" + timestamp;
            String contText;
            if (H5Key != null && H5Key.equals("special_act")) {
                mShareTitle = "ROBAM x AWE | 你有一个惊喜盲盒待领！";
                contText = "ROKI 喊你来拆惊喜盲盒，赢超多好礼。";
            } else {
                mShareTitle = tvTitle.getText().toString();
                contText = " ";
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int selfPermission = ContextCompat.checkSelfPermission(cx, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (selfPermission == 0) {
                    H5ActivityShareDialog.show(cx, shareUrl, mShareImgUrl, mShareTitle, contText);
                } else {
                    PermissionsUtils.checkPermission(cx, Manifest.permission.READ_EXTERNAL_STORAGE, PermissionsUtils.CODE_KITCHEN_SHARE_ACTIVE);
                }
            } else {
                H5ActivityShareDialog.show(cx, shareUrl, mShareImgUrl, mShareTitle, contText);
            }
        }


    }

    /**
     * 登录界面
     */
    private void startLogin() {
        if (instance.isGetPhone) {
            instance.loginAuth();
        } else {
            instance.login();
        }

    }

    //分享
    public void ShareLayer() {

        mShareTitle = tvTitle.getText().toString();
        String contText = " ";
        String shareUrl = url;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int selfPermission = ContextCompat.checkSelfPermission(cx, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (selfPermission == 0) {
                H5ActivityShareDialog.show(cx, shareUrl, mShareImgUrl, mShareTitle, contText, "common_act");
            } else {
                PermissionsUtils.checkPermission(cx, Manifest.permission.READ_EXTERNAL_STORAGE, PermissionsUtils.CODE_KITCHEN_SHARE_ACTIVE);
            }
        } else {
            H5ActivityShareDialog.show(cx, shareUrl, mShareImgUrl, mShareTitle, contText, "common_act");
        }
    }

    /**
     * 登录成功事件
     */
    @Subscribe
    public void onEvent(UserLoginNewEvent userLoginNewEvent) {
//        evKitchenKnowledgAct.clearHistory();
//        initPage();
       if(isLogin){
           return;
       }
        Long userId = Plat.accountService.getCurrentUserId();
//        evKitchenKnowledgAct.loadUrl("javascript:refreshMarchActivity('" + userId+ "')");
        if (loginEvenType != null && loginEvenType.equals("1")) {
            evActWebview.evaluateJavascript("skipReward('" + userId + "')", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {

                }
            });
        } else if (loginEvenType != null && loginEvenType.equals("2")) {
            evActWebview.evaluateJavascript("refreshMarchActivity('" + userId + "')", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {

                }
            });
        }
        isLogin = true;
    }

    @Subscribe
    public void onEvent(WxCodeShareEvent event) {
//        ToastUtils.show("分享成功",Toast.LENGTH_SHORT);
        shareFinish();

    }

    public void shareFinish() {
        //原生调用JS方法带返回值
        evActWebview.evaluateJavascript("shareFinish()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
//                Toast.makeText(cx,"我是调用js返回的数据："+value,Toast.LENGTH_LONG).show();
            }
        });
        //原生调用JS方法不带返回值
//        evKitchenKnowledgAct.loadUrl("javascript:shareFinish()");
    }

    private void fullScreen() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

}
