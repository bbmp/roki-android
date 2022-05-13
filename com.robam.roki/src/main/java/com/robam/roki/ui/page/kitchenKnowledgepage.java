//package com.robam.roki.ui.page;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.ActivityInfo;
//import android.content.pm.PackageManager;
//import android.content.res.Configuration;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.content.ContextCompat;
//
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.webkit.JavascriptInterface;
//import android.webkit.WebChromeClient;
//import android.webkit.WebResourceRequest;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.Toast;
//
//import com.google.common.eventbus.Subscribe;
//import com.legent.VoidCallback;
//import com.legent.plat.Plat;
//import com.legent.plat.events.UserLoginNewEvent;
//import com.legent.ui.UIService;
//import com.legent.ui.ext.BasePage;
//import com.legent.ui.ext.dialogs.ProgressDialogHelper;
//import com.legent.ui.ext.views.ExtWebView;
//import com.legent.utils.LogUtils;
//import com.legent.utils.api.ToastUtils;
//import com.robam.common.events.WxCode2Event;
//import com.robam.common.events.WxCodeShareEvent;
//import com.robam.common.services.CookbookManager;
//import com.robam.roki.MobApp;
//import com.robam.roki.R;
//import com.robam.roki.factory.RokiDialogFactory;
//import com.robam.roki.listener.IRokiDialog;
//import com.robam.roki.ui.PageArgumentKey;
//import com.robam.roki.ui.PageKey;
//import com.robam.roki.ui.dialog.KitchenSourceShareDialog;
//import com.robam.roki.ui.form.MainActivity;
//import com.robam.roki.ui.page.login.MyBasePage;
//import com.robam.roki.ui.page.login.helper.CmccLoginHelper;
//import com.robam.roki.ui.page.login.helper.LoginHelper;
//import com.robam.roki.utils.DialogUtil;
//import com.robam.roki.utils.PermissionsUtils;
//import com.robam.roki.utils.StringUtil;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//
//import static com.mob.tools.utils.DeviceHelper.getApplication;
//
///**
// * Created by Administrator on 2017/7/24.
// * 厨房知识页面
// */
//
//public class kitchenKnowledgepage extends MyBasePage<MainActivity> {
//
//    @InjectView(R.id.ev_kitchen_knowledg_act)
//    ExtWebView evKitchenKnowledgAct;
//
////    final String KIRCHEN_KNOWLEDGE_ACT_URL = "http://develop.h5.myroki.com/#/kitchenKnowledge";
////    final String KIRCHEN_KNOWLEDGE_ACT_URL = "https://h5.myroki.com/#/kitchenKnowledge";
//     String url = "https://h5.myroki.com/#/kitchenKnowledge";
//
////      final String KIRCHEN_SOURCE_ACT_URL = "http://develop.h5.myroki.com/#/chuYuanActivity";//分享链接
//    final String KIRCHEN_SOURCE_ACT_URL = "https://h5.myroki.com/#/chuYuanActivity";//分享链接
//
//    Handler mHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    evKitchenKnowledgAct.reload();
//                    break;
//            }
//        }
//    };
//    private String mActiveUrl;
//    private String mShareImgUrl;
//    private String mShareTitle;
//    private String mShareText;
//    private String mVideoUrl;
//    private String mShareId;
//    private CmccLoginHelper instance;
////    @Nullable
////    @Override
////    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
////                             @Nullable Bundle savedInstanceState) {
////        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
////        View view = inflater.inflate(R.layout.page_kitchen_knowledg, container, false);
////        ButterKnife.inject(this, view);
////        setiingWebToJS();
////        initPage();
////        return view;
////    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.page_kitchen_knowledg;
//    }
//
//    @Override
//    protected void initView() {
////        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
////        setiingWebToJS();
////        initPage();
//    }
//
//    @Override
//    protected void initData() {
//        instance = CmccLoginHelper.getInstance();
//        instance.initSdk(activity);
//        instance.getPhnoeInfo();
//        url  = getArguments().getString(PageArgumentKey.Url);
//        setiingWebToJS();
//        initPage();
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    private void initPage() {
//        boolean logon = Plat.accountService.isLogon();
//        Long userId = Plat.accountService.getCurrentUserId();
//        if (logon) {
////            evKitchenKnowledgAct.loadUrl(url + "?userId=" + userId);
//            evKitchenKnowledgAct.loadUrl(url + "?userId=" + userId);
//        } else {
//            evKitchenKnowledgAct.loadUrl(url);
//        }
//    }
//
//    @SuppressLint("SetJavaScriptEnabled")
//    private void setiingWebToJS() {
//
//        WebSettings settings = evKitchenKnowledgAct.getSettings();
//        settings.setJavaScriptEnabled(true);
//
//        settings.setDomStorageEnabled(true);// 打开本地缓存提供JS调用,至关重要
//        settings.setAppCacheMaxSize(1024 * 1024 * 8);// 实现8倍缓存
//        settings.setAllowFileAccess(true);
//        settings.setAppCacheEnabled(true);
////        String appCachePath = getApplication().getCacheDir().getAbsolutePath();
////        settings.setAppCachePath(appCachePath);
////        settings.setDatabaseEnabled(true);
//
//        evKitchenKnowledgAct.addJavascriptInterface(new CallMethodFromAndroidLister(cx), "RecipeDetail");
//        evKitchenKnowledgAct.setWebChromeClient(new WebChromeClient());
////        settings.setCacheMode();
//        evKitchenKnowledgAct.setWebViewClient(new WebViewClient() {
//
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
////                evKitchenKnowledgAct.loadUrl(url);
//                ProgressDialogHelper.setRunning(cx, true);
//
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
////                evKitchenKnowledgAct.loadUrl(url);
////                evKitchenKnowledgAct.reload();
//                ProgressDialogHelper.setRunning(cx, false);
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                WebView.HitTestResult hit = view.getHitTestResult();
//                //hit.getExtra()为null或者hit.getType() == 0都表示即将加载的URL会发生重定向，需要做拦截处理
//                if (TextUtils.isEmpty(hit.getExtra()) || hit.getType() == 0) {
//                    //通过判断开头协议就可解决大部分重定向问题了，有另外的需求可以在此判断下操作
//                    Log.e("重定向", "重定向: " + hit.getType() + " && EXTRA（）" + hit.getExtra() + "------");
//                    Log.e("重定向", "GetURL: " + view.getUrl() + "\n" +"getOriginalUrl()"+ view.getOriginalUrl());
//                    Log.d("重定向", "URL: " + url);
//                }
//
//                if (url.startsWith("http://") || url.startsWith("https://")) { //加载的url是http/https协议地址
//                    view.loadUrl(url);
//                    return false; //返回false表示此url默认由系统处理,url未加载完成，会继续往下走
//
//                } else { //加载的url是自定义协议地址
//                    try {
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                        activity.startActivity(intent);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return true;
//                }
//
//            }
//        });
//
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
//        LogUtils.i("20170801", "event:" + event);
//
//        if (keyCode == KeyEvent.KEYCODE_BACK && evKitchenKnowledgAct.canGoBack()) {
//            if (evKitchenKnowledgAct != null) {
//                evKitchenKnowledgAct.goBack();// 返回前一个页面
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        for (int i = 0; i < grantResults.length; i++) {
//            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                if (PermissionsUtils.CODE_KITCHEN_SHARE_ACTIVE == requestCode) {
//                    KitchenSourceShareDialog.show(cx, mActiveUrl, mShareImgUrl, mShareTitle, mShareText);
//                } else if (PermissionsUtils.CODE_KITCHEN_SHARE_VIDEO == requestCode) {
//                    KitchenSourceShareDialog.show(cx, mShareId, mVideoUrl, mShareImgUrl, mShareTitle, mShareText);
//                }
//            }
//        }
//
//
//    }
//
//    class CallMethodFromAndroidLister {
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
//            evKitchenKnowledgAct.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (evKitchenKnowledgAct.canGoBack()) {
//                        evKitchenKnowledgAct.goBack();
//                    } else {
//                        UIService.getInstance().popBack();
//                    }
//                }
//            });
//
//        }
//
//        /**
//         * 分享文章方法
//         */
//        @JavascriptInterface
//        public void shareActive(String id, String imgUrl, String title, String text) {
//            mActiveUrl = KIRCHEN_SOURCE_ACT_URL + "/articleShare?id=" + id;
//            mShareImgUrl = imgUrl;
//            mShareTitle = title;
//            mShareText = text;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                int selfPermission = ContextCompat.checkSelfPermission(cx, Manifest.permission.READ_EXTERNAL_STORAGE);
//                if (selfPermission == 0) {
//                    KitchenSourceShareDialog.show(cx, mActiveUrl, imgUrl, title, text);
//                } else {
//                    PermissionsUtils.checkPermission(cx, Manifest.permission.READ_EXTERNAL_STORAGE, PermissionsUtils.CODE_KITCHEN_SHARE_ACTIVE);
//                }
//            } else {
//                KitchenSourceShareDialog.show(cx, mActiveUrl, imgUrl, title, text);
//            }
//
//
//        }
//
//        /**
//         * 分享视频方法
//         */
//        @JavascriptInterface
//        public void shareVideo(String id, String url, String imgUrl, String title, String text) {
//            mVideoUrl = KIRCHEN_SOURCE_ACT_URL + "/videoShare?id=" + id + "&videoUrl=" + url;
//            mShareId = id;
//            mShareImgUrl = imgUrl;
//            mShareTitle = title;
//            mShareText = text;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                int selfPermission = ContextCompat.checkSelfPermission(cx, Manifest.permission.READ_EXTERNAL_STORAGE);
//                if (selfPermission == 0) {
//                    KitchenSourceShareDialog.show(cx, id, mVideoUrl, imgUrl, title, text);
//                } else {
//                    PermissionsUtils.checkPermission(cx, Manifest.permission.READ_EXTERNAL_STORAGE, PermissionsUtils.CODE_KITCHEN_SHARE_VIDEO);
//                }
//            } else {
//                KitchenSourceShareDialog.show(cx, id, mVideoUrl, imgUrl, title, text);
//            }
//
//
//        }
//
//        /**
//         * 跳转到菜谱详情页面
//         *
//         * @param id
//         */
//        @JavascriptInterface
//        public void cookDetail(String id) {
//
//            LogUtils.i("20170803", "id:" + id);
//            long recipeId = Long.parseLong(id);
//            RecipeDetailPage.show(recipeId, RecipeDetailPage.unKnown);
//        }
//
//        /**
//         * 删除评论
//         *
//         * @param id
//         */
//        @JavascriptInterface
//        public void deleteComment(final String id) {
//
//            final IRokiDialog deleteCommentDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
//            deleteCommentDialog.setTitleText(R.string.is_delete_title);
//            deleteCommentDialog.setContentText(R.string.is_delete_content);
//            deleteCommentDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    deleteCommentDialog.dismiss();
//                    CookbookManager.getInstance().deleteKitComment(Long.parseLong(id), new VoidCallback() {
//                        @Override
//                        public void onSuccess() {
//                            //删除后刷新页面
//                            mHandler.sendEmptyMessage(0);
//                        }
//
//                        @Override
//                        public void onFailure(Throwable t) {
//
//                        }
//                    });
//                }
//            });
//            deleteCommentDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    deleteCommentDialog.dismiss();
//                }
//            });
//            deleteCommentDialog.show();
//
//
//        }
//
//        /**
//         * 跳转到登录页面
//         */
//        @JavascriptInterface
//        public void toAndroidLoginPage(final String type) {
//            startLogin();
//        }
//
//        /**
//         *分享活动
//         */
//        @JavascriptInterface
//        public void androidShareLayer() {
////            mActiveUrl = KIRCHEN_SOURCE_ACT_URL + "/articleShare?id=" + id;
////            mShareImgUrl = imgUrl; //http://roki.oss-cn-hangzhou.aliyuncs.com/activity/picture/cover/1a722ccc-c03c-4177-a38a-46f5e18f504a.jpg
//            mShareImgUrl = "http://roki.oss-cn-hangzhou.aliyuncs.com/activity/picture/cover/1a722ccc-c03c-4177-a38a-46f5e18f504a.jpg";
//            mShareTitle = title;
////            mShareText = text;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                int selfPermission = ContextCompat.checkSelfPermission(cx, Manifest.permission.READ_EXTERNAL_STORAGE);
//                if (selfPermission == 0) {
//                    KitchenSourceShareDialog.show(cx,  url, mShareImgUrl, "标题", "文案");
////                    KitchenSourceShareDialog.show(cx, mActiveUrl, url, title, "分享文案");
//                } else {
//                    PermissionsUtils.checkPermission(cx, Manifest.permission.READ_EXTERNAL_STORAGE, PermissionsUtils.CODE_KITCHEN_SHARE_ACTIVE);
//                }
//            } else {
////                KitchenSourceShareDialog.show(cx, mActiveUrl, imgUrl, title, text);
//                KitchenSourceShareDialog.show(cx,  url, mShareImgUrl, "标题", "文案");
//            }
//        }
//
//
//    }
//    /**
//     * 登录界面
//     */
//    private void startLogin() {
////        UserActivity.start(getActivity(),true);
//        if (instance.isGetPhone) {
//            instance.loginAuth();
//        } else {
//            instance.login();
//        }
//
////
////        UserActivity.start(this);
////        if (CmccLoginHelper.getInstance().isGetPhone) {
////            CmccLoginHelper.getInstance().loginAuth();
////        } else {
////            CmccLoginHelper.getInstance().login();
////        }
//    }
//
//    /**
//     * 登录成功事件
//     */
//    @Subscribe
//    public void onEvent(UserLoginNewEvent userLoginNewEvent) {
//        initPage();
//
//    }
//    @Subscribe
//    public void onEvent(WxCodeShareEvent event) {
//        ToastUtils.show("分享成功",Toast.LENGTH_SHORT);
//
//    }
//    private void fullScreen() {
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        } else {
//            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
//    }
//
//
//    class CustomWebViewChromeClient extends WebChromeClient{
//
//        @Override
//        public void onShowCustomView(View view, CustomViewCallback callback) {
//            fullScreen();
////            mWebView.setVisibility(View.GONE);
////            mVideoContainer.setVisibility(View.VISIBLE);
////            mVideoContainer.addView(view);
////            mCallBack = callback;
//            super.onShowCustomView(view, callback);
//        }
//    }
//}
