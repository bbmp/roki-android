//package com.robam.roki.ui.page;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import androidx.annotation.Nullable;
//
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.JavascriptInterface;
//import android.webkit.WebChromeClient;
//import android.webkit.WebResourceError;
//import android.webkit.WebResourceRequest;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.legent.ui.UIService;
//import com.legent.ui.ext.BasePage;
//import com.legent.ui.ext.dialogs.DialogHelper;
//import com.legent.ui.ext.dialogs.ProgressNewDialogHelper;
//import com.legent.ui.ext.views.ExtWebView;
//import com.legent.utils.LogUtils;
//import com.legent.utils.api.PreferenceUtils;
//import com.legent.utils.api.ToastUtils;
//import com.robam.common.Utils;
//import com.robam.common.pojos.device.fan.AbsFan;
//import com.robam.roki.MobApp;
//import com.robam.roki.R;
//import com.robam.roki.ui.PageArgumentKey;
//import com.robam.roki.ui.PageKey;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import butterknife.OnClick;
//
///**
// * 添加厨电页面
// * Created by Administrator on 2017/8/21.
// */
//public class DeviceAddNewPage extends BasePage {
//
//    @InjectView(R.id.webview)
//    public ExtWebView webview;
//    private final static int SCANNIN_GREQUEST_CODE = 100;
//    private final String H5_DEVICE_ADD_URL = "http://h5.myroki.com/#/productList";
//    private final String H5_DEVICE_DETAIL_URL = "http://h5.myroki.com/#/addProduct?";
//    //    private final String H5_DEVICE_ADD_URL = "http://develop.h5.myroki.com/#/productList";
//    private final String KEY_AFTER_SALES_TEXT = "95105855";
//    private String mProduct;
//    @InjectView(R.id.iv_mask)
//    ImageView mIvMask;
//
//    private String mAddDevice;
//    Handler mHandler = new MyHandler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    webview.loadUrl((String) msg.obj);
//                    break;
//            }
//
//        }
//    };
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            String url = bundle.getString(PageArgumentKey.Url);
//            if (!TextUtils.isEmpty(url) && url.contains("&")) {
//                mProduct = url.split("&",2)[1];
//            }
//            if (mProduct.contains("&") && (mProduct.split("&").length == 2)) {
//                mProduct = mProduct + "&productType=1";
//            }
//        }
//
//        View view = inflater.inflate(R.layout.page_device_add_new, container, false);
//        ButterKnife.inject(this, view);
//        mAddDevice = PreferenceUtils.getString("addDevice", null);
//        if (mAddDevice == null) {
//            mIvMask.setVisibility(View.VISIBLE);
//        }
//        setiingWebToJS();
//        initPage();
//
//        return view;
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    private void setiingWebToJS() {
//        WebSettings settings = webview.getSettings();
//        settings.setJavaScriptEnabled(true);
//        webview.addJavascriptInterface(new CallMethodFromAndroidLister(cx), "RecipeDetail");
//        webview.setWebChromeClient(new WebChromeClient());
//        webview.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return false;
//            }
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                if (mAddDevice != null) {
//                    ProgressNewDialogHelper.setRunning(cx, true);
//                }
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                ProgressNewDialogHelper.setRunning(cx, false);
//            }
//
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                super.onReceivedError(view, request, error);
//            }
//        });
//    }
//
//    private void initPage() {
//        if (TextUtils.isEmpty(mProduct)) {
//            webview.loadUrl(H5_DEVICE_ADD_URL);
//        } else {
//            webview.loadUrl(H5_DEVICE_DETAIL_URL + mProduct);
//        }
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.reset(this);
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
//            webview.goBack();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @OnClick(R.id.iv_mask)
//    public void onViewClicked() {
//        mIvMask.setVisibility(View.GONE);
//        PreferenceUtils.setString("addDevice", "newbie");
//    }
//
//    class CallMethodFromAndroidLister {
//        Context context;
//
//        public CallMethodFromAndroidLister(Context context) {
//            this.context = context;
//        }
//
//        @JavascriptInterface
//        public void userIsNotLog(boolean notLog) {
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
//        /**
//         * 返回方法
//         */
//        @JavascriptInterface
//        public void goback(boolean b) {
//            LogUtils.i("20180928", "b :" + b);
//            webview.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (webview.canGoBack()) {
//                        LogUtils.i("20180928", "li ");
//                        webview.goBack();
//                    } else {
//                        LogUtils.i("20180928", "mian ");
//                        UIService.getInstance().popBack();
//                    }
//                }
//            });
//        }
//
//        /**
//         * 红圈闪烁中操作
//         */
//        @JavascriptInterface
//        public void linkWifi(String guid) {
//            Log.e("cjh", "linkWifi: "+guid );
//            Bundle bd = new Bundle();
//            bd.putString(PageArgumentKey.Guid, guid);
//            LogUtils.i("2020031307", "guid:" + guid);
//            UIService.getInstance().postPage(PageKey.WifiConnect, bd);
//        }
//
//        @JavascriptInterface
//        public void linkWifi() {
//            Log.e("cjh", "linkWifisssss: " );
//            UIService.getInstance().postPage(PageKey.WifiConnect);
//        }
//
//
//        //暂不做手动联网处理
//        @JavascriptInterface
//        public void bluetoothwifi() {
//            if (webview.canGoBack()) {
//                webview.goBack();
//            }
//        }
//
//        /**
//         * 确定按钮操作
//         */
//        @JavascriptInterface
//        public void sureBtn() {
//            if (webview.canGoBack()) {
//                webview.goBack();
//            }
//        }
//
//        /**
//         * 有屏烟机确定操作
//         */
//        @JavascriptInterface
//        public void scanAndCode() {
//            String name = Thread.currentThread().getName();
//            if (webview.canGoBack()) {
//                webview.goBack();
//            }
//        }
//
//        @JavascriptInterface
//        public void telPhone() {
//            Uri uri = Uri.parse(String.format("tel:%s", KEY_AFTER_SALES_TEXT));
//            Intent it = new Intent(Intent.ACTION_DIAL, uri);
//            startActivity(it);
//        }
//
//
//        @JavascriptInterface
//        public void selectDevice(String url) {
//            Log.e("cjh", "selectDevice: "+url );
//            AbsFan fan = Utils.getDefaultFan();
//            if (null == fan) {
//                ToastUtils.show(R.string.add_fan, Toast.LENGTH_SHORT);
//            } else {
//                Message msg = mHandler.obtainMessage();
//                msg.what = 0;
//                msg.obj = url;
//                mHandler.sendMessage(msg);
//            }
//
//        }
//
//    }
//
//    @OnClick(R.id.get_url)
//    public void clickUrl() {
//        ToastUtils.show(webview.getUrl(), 2000);
//    }
//}
