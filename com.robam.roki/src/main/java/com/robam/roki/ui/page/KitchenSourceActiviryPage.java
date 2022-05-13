//package com.robam.roki.ui.page;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.graphics.Bitmap;
//import android.location.Location;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
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
//
//import com.legent.VoidCallback;
//import com.legent.plat.Plat;
//import com.legent.ui.UIService;
//import com.legent.ui.ext.BasePage;
//import com.legent.ui.ext.dialogs.DialogHelper;
//import com.legent.ui.ext.dialogs.ProgressDialogHelper;
//import com.legent.ui.ext.views.ExtWebView;
//import com.legent.utils.LogUtils;
//import com.robam.common.services.CookbookManager;
//import com.robam.roki.R;
//import com.robam.roki.factory.RokiDialogFactory;
//import com.robam.roki.listener.IRokiDialog;
//import com.robam.roki.model.bean.City;
//import com.robam.roki.ui.dialog.KitchenSourceShareDialog;
//import com.robam.roki.utils.CityCodeUtil;
//import com.robam.roki.utils.DialogUtil;
//import com.robam.roki.utils.LocationUtils;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//
///**
// * Created by Administrator on 2017/7/22.
// * 厨源活动页面
// */
//
//public class KitchenSourceActiviryPage extends BasePage {
//
//    //        final String KIRCHEN_SOURCE_ACT_URL = "https://develop.h5.myroki.com/#/chuYuanActivity";
//    final String KIRCHEN_SOURCE_ACT_URL = "https://h5.myroki.com/#/chuYuanActivity";
//
//    @InjectView(R.id.ev_kitchen_source_act)
//    ExtWebView evKitchenSourceAct;
//    private String cityNa;
//    private Integer cityCo;
//    private boolean logon = Plat.accountService.isLogon();
//    private Long userId = Plat.accountService.getCurrentUserId();
//    private boolean gpsEnabled;
//    static boolean gpsFalg = true;//定位标识符
//    static City city;
//    private String copyUrl;
//
//    private boolean needClearHistory = true;
//    Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//
//            switch (msg.what) {
//                case 1:
//                    initPage();
//                    break;
//            }
//        }
//    };
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.page_kitchen_source_act, container, false);
//        ButterKnife.inject(this, view);
//        gpsEnabled = LocationUtils.isGpsEnabled(cx);
//        initGPSAndNowork();
//        setiingWebToJS();
//        return view;
//    }
//
//    //定位
//    private void initGPSAndNowork() {
//        if (gpsEnabled) {
//            boolean locationEnabled = LocationUtils.isLocationEnabled(cx);
//            LogUtils.i("20180416", "gpsFalg:" + gpsFalg + " locationEnabled:" + locationEnabled);
//            if (locationEnabled && gpsFalg) {
//                LocationUtils.OnLocationChangeListener listener =
//                        new LocationUtils.OnLocationChangeListener() {
//                            @Override
//                            public void getLastKnownLocation(Location location) {
//                                double latitude;
//                                double longitude;
//                                LogUtils.i("20180416", "location:" + location);
//                                if (location == null) {
//                                    latitude = 30.44685;
//                                    longitude = 120.28829;
//                                } else {
//                                    latitude = location.getLatitude();
//                                    longitude = location.getLongitude();
//                                }
//                                String cityName = LocationUtils.getLocality(cx, latitude, longitude);
//                                Integer cityCode = CityCodeUtil.getCityCode(cityName);
//                                LogUtils.i("20180416", "cityName:" + cityName + " cityCode:" + cityCode);
//                                cityNa = cityName;
//                                cityCo = cityCode;
//                                if (cityCo != null && cityNa != null) {
//                                    if (city == null) {
//                                        city = new City();
//                                        City.setCityName(cityName);
//                                        City.setCityCode(cityCode);
//                                    } else {
//                                        City.setCityName(cityName);
//                                        City.setCityCode(cityCode);
//                                    }
//                                    gpsFalg = false;
//                                    mHandler.sendEmptyMessage(1);
//                                }
//
//                            }
//
//                            @Override
//                            public void onLocationChanged(Location location) {
//                            }
//
//                            @Override
//                            public void onStatusChanged(String provider, int status, Bundle extras) {
//                            }
//                        };
//                LocationUtils.register(cx, 2000, 30, listener);
//
//            } else {
//                initUrl(logon, userId);
//            }
//        } else {
//            showGpsDialog();
//        }
//    }
//
//    /**
//     * 设置webView和js交互
//     */
//    private void setiingWebToJS() {
//
//        WebSettings settings = evKitchenSourceAct.getSettings();
//        settings.setJavaScriptEnabled(true);
//        evKitchenSourceAct.addJavascriptInterface(new CallMethodFromAndroidLister(cx), "RecipeDetail");
//        evKitchenSourceAct.setWebChromeClient(new WebChromeClient());
//        evKitchenSourceAct.setWebViewClient(new WebViewClient() {
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
//                copyUrl = url;
//                ProgressDialogHelper.setRunning(cx, false);
//            }
//
//            @Override
//            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
//                super.doUpdateVisitedHistory(view, url, isReload);
//                LogUtils.i("20180416", "needClearHistory:" + needClearHistory);
//                if (needClearHistory) {
//                    needClearHistory = false;
//                    evKitchenSourceAct.clearHistory();//清除历史记录
//                    evKitchenSourceAct.reload();
//                }
//            }
//        });
//
//    }
//
//    /**
//     * 询问是否打开Gps
//     */
//    private void showGpsDialog() {
//        DialogHelper.newDialog_OkCancel(cx, "确定打开GPS吗?", null, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                if (which == DialogInterface.BUTTON_POSITIVE) {
//                    LocationUtils.openGpsSettings(cx);
//                    UIService.getInstance().popBack();
//                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
//                    initNoGpsUrl(logon, userId);
//                }
//            }
//        }).show();
//    }
//
//    //用户选择不定位时加载的页面
//    private void initNoGpsUrl(boolean logon, Long userId) {
//        if (logon) {
//            evKitchenSourceAct.loadUrl(KIRCHEN_SOURCE_ACT_URL + "?userId=" + userId);
//        } else {
//            evKitchenSourceAct.loadUrl(KIRCHEN_SOURCE_ACT_URL);
//        }
//    }
//
//    private void initPage() {
//        if (cityCo != null && cityNa != null) {
//            initUrl(logon, userId);
//        }
//    }
//
//    private void initUrl(boolean logon, Long userId) {
//
//        if (cityCo == null && cityNa == null) {
//            cityCo = City.getCityCode();
//            cityNa = city.getCityName();
//        }
//
//        if (logon) {
//            evKitchenSourceAct.loadUrl(KIRCHEN_SOURCE_ACT_URL + "?cityId=" + cityCo + "&cityName=" + cityNa + "&userId=" + userId);
//            LogUtils.i("20180416", "log:" + KIRCHEN_SOURCE_ACT_URL + "?cityId=" + cityCo + "&cityName=" + cityNa + "&userId=" + userId);
//        } else {
//            evKitchenSourceAct.loadUrl(KIRCHEN_SOURCE_ACT_URL + "?cityId=" + cityCo + "&cityName=" + cityNa);
//            LogUtils.i("20180416", "no_log:" + KIRCHEN_SOURCE_ACT_URL + "?cityId=" + cityCo + "&cityName=" + cityNa);
//        }
//    }
//
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.reset(this);
//        LocationUtils.unregister();
//    }
//
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && evKitchenSourceAct.canGoBack()) {
//            evKitchenSourceAct.reload();
//            evKitchenSourceAct.goBack();// 返回前一个页面
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    class CallMethodFromAndroidLister {
//
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
//            if (evKitchenSourceAct.canGoBack()) {
//                evKitchenSourceAct.goBack();
//            } else {
//                UIService.getInstance().popBack();
//                cityNa = null;
//                cityCo = null;
//            }
//        }
//
//        @JavascriptInterface
//        public void gps() {
//            gpsFalg = true;
//            needClearHistory = true;
//            if (gpsEnabled) {
//                initGPSAndNowork();
//            } else {
//                showGpsDialog();
//            }
//
//        }
//
//        /**
//         * 分享文章方法
//         */
//        @JavascriptInterface
//        public void shareActive(String id, String imgUrl, String title, String text) {
//            String activeUrl = KIRCHEN_SOURCE_ACT_URL + "/articleShare?id=" + id;
//            KitchenSourceShareDialog.show(cx, activeUrl, imgUrl, title, text);
//        }
//
//        /**
//         * 分享视频方法
//         */
//        @JavascriptInterface
//        public void shareVideo(String id, String url, String imgUrl, String title, String text) {
//            String videoUrl = KIRCHEN_SOURCE_ACT_URL + "/videoShare?id=" + id + "&videoUrl=" + url;
//
//            KitchenSourceShareDialog.show(cx, id, videoUrl, imgUrl, title, text);
//        }
//
//
//        /**
//         * 在H5页面选择城市时清空之前浏览记录
//         */
//        @JavascriptInterface
//        public void getCity(String cityName, String cityCode) {
//            int cityC = Integer.parseInt(cityCode);
//            City.setCityName(cityName);
//            City.setCityCode(cityC);
//            needClearHistory = true;
//
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
//                            evKitchenSourceAct.reload();
//                            LogUtils.i("20171228", "id:" + id);
//                        }
//
//                        @Override
//                        public void onFailure(Throwable t) {
//                            LogUtils.i("20171228", "t:" + t);
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
//
//    }
//
//}
