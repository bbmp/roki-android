package com.robam.roki.ui.activity3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.legent.plat.ApiSecurityExample;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.ui.ext.views.ExtWebView;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.H5ActivityShareDialog;
import com.robam.roki.utils.PermissionsUtils;


public final class NotificationWebActivity extends AppActivity {
    private static final String TAG = "SpeechCookActivity";
    private ExtWebView webView;
    private ImageView ivBack;
    private TextView tv_title;
    private String url;
    private String mShareImgUrl;
    private String shareContText;
    private String title;
    public static void start(Context context, String url) {
        Intent intent = new Intent(context, NotificationWebActivity.class);
        intent.putExtra(PageArgumentKey.Url, url);
        context.startActivity(intent);
    }
    public static void start(Context context, String url,String shareContText,String mShareImgUrl,String title) {
        Intent intent = new Intent(context, NotificationWebActivity.class);
        intent.putExtra(PageArgumentKey.Url, url);
        intent.putExtra("shareContText", shareContText);
        intent.putExtra("Share_Img", mShareImgUrl);
        intent.putExtra(PageArgumentKey.title, title);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.page_new_webclient_notif;

    }


    @Override
    protected void initView() {
        webView = (ExtWebView) findViewById(R.id.webView);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        setOnClickListener(ivBack);
//        StatusBarUtils.setColor(this, getResources().getColor(R.color.white));
//        StatusBarUtils.setTextDark(this, true);
        setiingWebToJS();
    }

    @Override
    protected void initData() {
        url = getIntent().getStringExtra(PageArgumentKey.Url);
        mShareImgUrl = getIntent().getStringExtra("Share_Img");
        shareContText = getIntent().getStringExtra("shareContText");
        title = getIntent().getStringExtra(PageArgumentKey.title);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setCallback(new ExtWebView.Callback() {
            @Override
            public void onReceivedTitle(WebView webView, String title) {
//                tv_title.setText(title);
            }

            @Override
            public void onReceivedError(WebView webView, int errorCode,
                                        String description, String failingUrl) {
                ToastUtils.showShort(description);
            }
        });
        boolean logon = Plat.accountService.isLogon();

        if (logon) {
            Long userId = Plat.accountService.getCurrentUserId();
            webView.loadUrl(url + "?userId=" + userId + "&terminal=app");
        } else {
            webView.loadUrl(url);
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setiingWebToJS() {

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        settings.setDomStorageEnabled(true);// 打开本地缓存提供JS调用,至关重要
        settings.setAppCacheMaxSize(1024 * 1024 * 8);// 实现8倍缓存
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);

        webView.addJavascriptInterface(new CallMethodFromAndroidLister(getActivity()), "RecipeDetail");
        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (title != null&& !TextUtils.isEmpty(title)) {
                    tv_title.setText(title);
                }
            }
        };
        webView.setWebChromeClient(wvcc);

        webView.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                ProgressDialogHelper.setRunning(getContext(), true);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                ProgressDialogHelper.setRunning(getContext(), false);
            }

        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        LogUtils.i("20170801", "event:" + event);

        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            if (webView != null) {
                webView.goBack();// 返回前一个页面
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
                if (PermissionsUtils.CODE_KITCHEN_SHARE_ACTIVE == requestCode) {
                    H5ActivityShareDialog.show(getContext(), shareUrl, mShareImgUrl, title, shareContText);
                } else if (PermissionsUtils.CODE_KITCHEN_SHARE_VIDEO == requestCode) {
                    H5ActivityShareDialog.show(getContext(), shareUrl, mShareImgUrl, title, shareContText);

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
            webView.post(new Runnable() {
                @Override
                public void run() {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        UIService.getInstance().popBack();
                    }
                }
            });

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int selfPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                if (selfPermission == 0) {
                    H5ActivityShareDialog.show(getContext(), shareUrl, mShareImgUrl, title, shareContText);
                } else {
                    PermissionsUtils.checkPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE, PermissionsUtils.CODE_KITCHEN_SHARE_ACTIVE);
                }
            } else {
                H5ActivityShareDialog.show(getContext(), shareUrl, mShareImgUrl, title, shareContText);
            }
        }


    }

    @Override
    public void onClick(View view) {
        if (ivBack.equals(view)) {
            finish();
        }
    }

}