package com.robam.roki.ui.page;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.dao.DaoHelper;
import com.legent.plat.Plat;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.events.UserLoginEvent;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.dialogs.DialogHelper;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.ui.ext.views.ExtWebView;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.FavorityBookRefreshEvent;
import com.robam.common.events.HomeRecipeViewEvent;
import com.robam.common.events.RecipeCookFinishEven;
import com.robam.common.events.ShareRecipePictureEvent;
import com.robam.common.events.UMPushRecipeEvent;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.CookbookPlatforms;
import com.robam.common.pojos.Dc;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamMode;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.microwave.MicroWaveStatus;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.services.CookbookManager;
import com.robam.common.services.StoreService;
import com.robam.common.ui.UiHelper;
import com.robam.common.util.DeviceSelUtils;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.CookbookShareDialog;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.form.RecipeActivity;
import com.robam.roki.ui.form.RecipePotActivity;
import com.robam.roki.ui.form.RecipeRRQZActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;
import com.robam.roki.ui.view.RecipeDetailView;
import com.robam.roki.ui.view.umpush.UMPushMsg;
import com.robam.roki.utils.DeviceSelectUtils;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.LoginUtil;
import com.robam.roki.utils.PermissionsUtils;
import com.robam.roki.utils.ToolUtils;

import org.eclipse.jetty.util.ajax.JSON;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 菜谱详情页
 */
public class RecipeDetailPage extends MyBasePage<MainActivity> {
    static int pageKey;
    public static final int HomeRecipeView = 1;//主页面
    public static final int ThemeDetailPage = 2;//主题页面
    public static final int DynamicRecipeShow = 3;//厨艺动态页面
    public static final int LiveRecipeShow = 4;//视频页面
    public static final int DeviceRecipePage = 5;//设备菜谱页面
    public static final int HistoryRecipePage = 6;//更多菜谱
    public static final int RecipeSearchPage = 7;//搜索菜谱页面
    public static final int MyCollectPage = 8;//我的收藏页面
    public static final int RecipeBanner2Page = 9;//菜谱预览页面1
    public static final int RecipeBannerPage = 10;//菜谱预览页面2
    public static final int SharePage = 11;//菜谱分享页面
    public static final int unKnown = 12;//未知
    StoreService ss = StoreService.getInstance();
    private long mId;

    public static void show(Recipe recipe, long recipeId, int source, String entranceCode, String platformCode, String mGuid) {
        pageKey = source;
        Bundle bd = new Bundle();
        bd.putLong(PageArgumentKey.BookId, recipeId);
        bd.putString(PageArgumentKey.entranceCode, entranceCode);
        bd.putString(PageArgumentKey.platformCode, platformCode);
        bd.putSerializable(PageArgumentKey.Bean, recipe);
        bd.putSerializable(PageArgumentKey.Guid, mGuid);
        UIService.getInstance().postPage(PageKey.RecipeDetail, bd);
    }

    public static void show(Recipe recipe, long recipeId, int source, String entranceCode) {
        pageKey = source;
        Bundle bd = new Bundle();
        bd.putLong(PageArgumentKey.BookId, recipeId);
        bd.putString(PageArgumentKey.entranceCode, entranceCode);
        bd.putSerializable(PageArgumentKey.Bean, recipe);
        UIService.getInstance().postPage(PageKey.RecipeDetail, bd);
    }


    public static void show(long recipeId, int source, String entranceCode) {
        pageKey = source;
        Bundle bd = new Bundle();
        bd.putLong(PageArgumentKey.BookId, recipeId);
        bd.putString(PageArgumentKey.entranceCode, entranceCode);
        UIService.getInstance().postPage(PageKey.RecipeDetail, bd);
    }

    public static void show(long recipeId, int source, String entranceCode, String mGuid) {
        pageKey = source;
        Bundle bd = new Bundle();
        bd.putLong(PageArgumentKey.BookId, recipeId);
        bd.putString(PageArgumentKey.entranceCode, entranceCode);
        bd.putString(PageArgumentKey.Guid, mGuid);
        UIService.getInstance().postPage(PageKey.RecipeDetail, bd);
    }

    public static void show(long recipeId, int source) {
        pageKey = source;
        Bundle bd = new Bundle();
        bd.putLong(PageArgumentKey.BookId, recipeId);
        UIService.getInstance().postPage(PageKey.RecipeDetail, bd);
    }

    @InjectView(R.id.webview)
    ExtWebView webview;
    @InjectView(R.id.imgFavority)
    ImageView imgFavority;
    @InjectView(R.id.title_item)
    LinearLayout title_item;
    @InjectView(R.id.imgreturn)
    ImageView imgReturn;

    private Long ID;
    private String entranceCode;
    private String platformCode;
    private String mGuid;
    private CookStep cookStepTemp;
    private boolean isRQZ;//是否燃氣灶
    private Recipe recipe;
    private Recipe book;
    private AbsFan fan;

    private ArrayList<CookStep> cookSteps;
    //private final String H5_BASE_URL = "http://h5.myroki.com:80/#/cookBookDetail";//废弃
    //private final String H5_BASE_URL = "http://h5.myroki.com/dist/index.html#/cookBookDetail";
    //private final String H5_BASE_URL = "http://develop.h5.myroki.com/dist/index.html#/cookBookDetail";

//        private final String H5_BASE_URL = "http://develop.h5.myroki.com/dist/index.html#/automaticCooking";
    private final String H5_BASE_URL = "https://h5.myroki.com/dist/index.html#/automaticCooking";
    private final String H5_BASE_MORE_URL = "https://h5.myroki.com:80/#/moreCook?cookbookId=";
    //private final String H5_BASE_MORE_URL = "http://develop.h5.myroki.com:80/#/moreCook?cookbookId=";
    //private final String H5_BASE_MORE_URL = "http://develop.h5.myroki.com/dist/index.html#/moreCook?cookbookId=";

    IRokiDialog dialogTips = null;
    @SuppressLint("HandlerLeak")
    Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (buf.length() != 0) {
                        ToastUtils.show("无" + buf + ",连接后才可自动烹饪");
                    }
                    buf.delete(0, buf.length());
                    break;
                case 2:

                    break;
                default:
                    break;
            }
        }
    };
    StringBuffer buf = new StringBuffer();


    @Override
    protected int getLayoutId() {
        return R.layout.page_recipe_detail;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setColor(getContext() , getResources().getColor(R.color.white));
        StatusBarUtils.setTextDark(getContext(), true);
        setiingWebToJS();
    }

    @Override
    protected void initData() {
        ID = getArguments().getLong(PageArgumentKey.BookId);
        entranceCode = getArguments().getString(PageArgumentKey.entranceCode);
        platformCode = getArguments().getString(PageArgumentKey.platformCode);
        mGuid = getArguments().getString(PageArgumentKey.Guid);
        LogUtils.i("202010241031", "mGuid:::" + mGuid);
        recipe = (Recipe) getArguments().getSerializable(PageArgumentKey.Bean);

        if (ID == null) {
            ToastUtils.show("菜谱数据解析异常");
            UIService.getInstance().popBack();
        }

        init(ID, entranceCode);
        dialogTips = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_02);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.imgreturn)
    public void onClickReturn() {
        if (entranceCode != null && entranceCode.equals("code13")) {
            MainActivity.start(activity);
        } else {
            UIService.getInstance().popBack();
        }
    }

    //收藏点击事件
    @OnClick(R.id.imgFavority)
    public void onClickCollect() {
        if (!Plat.accountService.isLogon()) {
            login();
        } else {
            if (recipe == null) {
                recipe = book;
            }
            if (recipe != null) {
//                UiHelper.onFavority(recipe, imgFavority, null);
                collect();
            }
        }
    }

    /**
     * 收藏
     */
    private void collect() {
        boolean isLogin = Plat.accountService.isLogon();
        if (isLogin) {
            if (recipe.collected) {
                ProgressDialogHelper.setRunning(cx, true);
                CookbookManager.getInstance().deleteFavorityCookbooks(recipe.id, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        ProgressDialogHelper.setRunning(cx, false);
                        ToastUtils.show("已取消收藏");
                        recipe.setIsCollected(false);
                        imgFavority.setImageResource(com.robam.common.R.drawable.ic_baseline_favorite_border_24);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ProgressDialogHelper.setRunning(cx, false);
                        ToastUtils.show(t.getMessage());
                    }
                });
            } else {
                ProgressDialogHelper.setRunning(cx, true);
                CookbookManager.getInstance().addFavorityCookbooks(recipe.id, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        ProgressDialogHelper.setRunning(cx, false);
                        ToastUtils.show("收藏成功");
                        recipe.setIsCollected(true);
                        imgFavority.setImageResource(com.robam.common.R.drawable.ic_baseline_favorite_24);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ProgressDialogHelper.setRunning(cx, false);
                        ToastUtils.show(t.getMessage());
                    }
                });
            }

        } else {
            if (CmccLoginHelper.getInstance().isGetPhone) {
                CmccLoginHelper.getInstance().loginAuth();
            } else {
                CmccLoginHelper.getInstance().login();
            }
        }
    }

    private void login() {
        if (CmccLoginHelper.getInstance().isGetPhone) {
            CmccLoginHelper.getInstance().loginAuth();
        } else {
            CmccLoginHelper.getInstance().login();
        }
    }

    //收藏菜谱刷新
    @Subscribe
    public void onEvent(FavorityBookRefreshEvent event) {
        if (event.bookId != recipe.id)
            return;
        DaoHelper.refresh(recipe);
        imgFavority.setSelected(recipe.isFavority);
    }

    @OnClick(R.id.imgShare)
    public void onClickShare() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int selfPermission = ContextCompat.checkSelfPermission(cx, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (selfPermission == 0) {
                CookbookShareDialog.show(cx, book);
            } else {
                PermissionsUtils.checkPermission(cx, Manifest.permission.READ_EXTERNAL_STORAGE, PermissionsUtils.CODE_RECIPE_DETAIL_SHARE);
            }
        } else {
            CookbookShareDialog.show(cx, book);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                if (PermissionsUtils.CODE_RECIPE_DETAIL_SHARE == requestCode) {
                    CookbookShareDialog.show(cx, book);
                } else if (PermissionsUtils.CODE_RECIPE_DETAIL_CAMERA == requestCode) {
                    Activity atv = UIService.getInstance().getMain().getActivity();
                    RecipeShowPage.showCooking(atv, mId);
                }
            }
        }
    }

    private void setiingWebToJS() {

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webview.addJavascriptInterface(new CallMethodFromAndroidLister(cx), "RecipeDetail");

        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient() {
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

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                //super.onReceivedError(view, request, error);

            }
        });
        webview.setScrollHeightLister(new ExtWebView.ScrollHeightLister() {
            @Override
            public void ScrollChangedHeightW(int l, int t, int oldl, int oldt) {
                if (oldt > 1600) {
                    title_item.setVisibility(View.VISIBLE);
//                    imgReturn.setVisibility(View.GONE);
                }
                if (oldt < 1600) {
                    title_item.setVisibility(View.VISIBLE);
//                    imgReturn.setVisibility(View.VISIBLE);
                }
            }
        });

    }


    public boolean isCollect;

    List<String> platformCodeList = new ArrayList<>();

    void init(final long recipeid, String entranceCode) {
        final long userId = Plat.accountService.getCurrentUserId();

        ss.getCookbookById(recipeid, entranceCode, "0", new Callback<Recipe>() {
            @Override
            public void onSuccess(final Recipe recipe) {
                if (recipe == null) {
                    ToastUtils.show("没有这道菜");
                    UIService.getInstance().popBack();
                    return;
                }
                ss.getIsCollectBookId(userId, recipeid, new Callback<Reponses.IsCollectBookResponse>() {
                    @Override
                    public void onSuccess(Reponses.IsCollectBookResponse isCollectBookResponse) {
                        try {
                            if (isCollectBookResponse != null) {
                                isCollect = isCollectBookResponse.isCollect;
                                if (isCollect) {
                                    imgFavority.setSelected(true);
                                    if (recipe.id == recipeid) {
                                        recipe.setIsCollected(isCollect);
                                    }
                                } else {
                                    imgFavority.setSelected(false);
                                    if (recipe.id == recipeid) {
                                        recipe.setIsCollected(isCollect);
                                    }
                                }
                                book = recipe;
                                if (book.getJs_dcs().size() == 0) {
                                    isRQZ = true;
                                } else {
                                    for (Dc dc : book.getJs_dcs()) {
                                        if (DeviceType.RRQZ.equals(dc.getDc())) {
                                            isRQZ = true;
                                            break;
                                        } else if (DeviceType.RZQL.equals(dc.getDc())) {

                                        }
                                    }
                                }
                            }
                            List<CookbookPlatforms> cookbookPlatformsList = book.cookbookPlatformsList;

                            if (platformCodeList != null) {
                                platformCodeList.clear();
                            }
                            if (cookbookPlatformsList != null) {
                                for (int i = 0; i < cookbookPlatformsList.size(); i++) {
                                    String platformCode = cookbookPlatformsList.get(i).platformCode;
                                    platformCodeList.add(platformCode);
                                    LogUtils.i("2020060504", "platformCode:::" + platformCode);
                                }
                            }
                        } catch (Exception e) {

                        }




                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
//                ToastUtils.showShort(t.getMessage());
            }

        });
        LogUtils.i("2020070107", "recipeid::" + recipeid);
        ss.getCookBookSteps(recipeid, "", "", new Callback<List<CookStep>>() {
            @Override
            public void onSuccess(List<CookStep> cookStepsTemp) {
                cookSteps = (ArrayList<CookStep>) cookStepsTemp;
                if (book != null) {
                    book.setIsFavority(isCollect);
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });

        initUrl(ID);

    }

    private void initUrl(Long id) {
        long userId = Plat.accountService.getCurrentUserId();
        if (userId == 0) {
            webview.loadUrl(H5_BASE_URL + "?cookbookId=" + id);
        } else {
            webview.loadUrl(H5_BASE_URL + "?userId=" + userId + "&cookbookId=" + id);
//            webview.reload();
        }
    }

    @Subscribe
    public void onEvent(UMPushRecipeEvent event) {
        init(UMPushMsg.getMsgId(), "");
    }

    @Subscribe
    public void onEvent(ShareRecipePictureEvent event) {
        initUrl(ID);
    }

//    @Subscribe
//    public void onEvent(UserLoginEvent event) {
//        //initUrl(ID);
//        UIService.getInstance().popBack();
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (null != webview) {
            if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
                webview.goBack();// 返回前一个页面
                return true;
            }
        }
        if (entranceCode != null && entranceCode.equals("code13")) {
            MainActivity.start(activity);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventUtils.postEvent(new PageBackEvent("RecipeDetailPage"));
        ButterKnife.reset(this);
    }

    public class CallMethodFromAndroidLister {
        Context context;
        String cookBookId = String.valueOf(ID);

        public CallMethodFromAndroidLister(Context context) {
            this.context = context;
        }

        /**
         * 添加到购物车
         */
        @JavascriptInterface
        public void addBuy(String cookBookId) {
            ToastUtils.show(R.string.add_buy);
        }

        /**
         * 取消购物车
         */
        @JavascriptInterface
        public void cancelBuy(String cookBookId) {
            ToastUtils.show(R.string.cancel_buy);
        }


        @JavascriptInterface
        public void userIsNotLog(boolean notLog) {

            if (notLog) {
                login();

            } else {

            }

        }

        /**
         * 点赞提示
         *
         * @param text
         */
        @JavascriptInterface
        public void bombbox(String text) {
            ToastUtils.show(text);
        }

        /**
         * 开始烹饪
         */
        @JavascriptInterface
        public void startCooking(String cookBookId) {

            if (cookSteps.size() == 0) {
                ToastUtils.show("此菜谱还未上传完，请过阵子再来看哈");
                return;
            }

            if (cookSteps != null && "".equals(cookSteps.get(0).getDc())) {
                ToastUtils.show("此菜谱不支持自动烹饪");
                return;
            }
            if (recipe != null) {
                ToolUtils.logEvent("菜谱", "开始烹饪:" + recipe.getName(), "roki_美食");
            }
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    this.cancel();
                }
            }, 1000);
            //燃气灶
            if (isRQZ) {
                List<AbsFan> fanList = Utils.getFan();
                List<Stove> stoves = new ArrayList<>();
                for (int i = 0; i < fanList.size(); i++) {
                    if ((Stove) fanList.get(i).getChildStove() != null) {
                        stoves.add((Stove) fanList.get(i).getChildStove());
                    }
                }
                List<Dc> listDc = book.getJs_dcs();
                if (isContainAllDevice(listDc)) {
                    if (stoves.size() > 1) {
                        //从菜谱模块进来
                        if (mGuid == null || "".equals(mGuid)) {
                            //烟机选择界面
                            Helper.newDeviceSelectStoveDialog(cx, stoves, new Callback<IDevice>() {
                                @Override
                                public void onSuccess(IDevice iDevice) {
                                    Stove stove = (Stove) iDevice;
                                    stoveWork(stove);
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    t.printStackTrace();
                                }
                            });

                        } else {
                            //从设备进来
                            for (int i = 0; i < stoves.size(); i++) {
                                if ((stoves.get(i).getGuid().getGuid()).equals(mGuid)) {
                                    Stove stove = stoves.get(i);
                                    stoveWork(stove);
                                }
                            }
                        }
                    } else {
                        Stove stove = stoves.get(0);
                        stoveWork(stove);
                    }

                } else {
                    Message msg = Message.obtain();
                    msg.what = 1;
                    hander.sendMessage(msg);
                }
            } else {

                List<Dc> listDc = book.getJs_dcs();
                if (isContainAllDevice(listDc)) {
                    String dcTemp = cookSteps.get(0).getDc();
                    getDeviceForFirstStep(dcTemp);
                } else {
                    Message msg = Message.obtain();
                    msg.what = 1;
                    hander.sendMessage(msg);
                }
            }
        }

        private void selectStove(final AbsFan fantemp) {
            if (fantemp != null) {
                if (fantemp.getChildStove() != null) {
                    final Stove stove = fantemp.getChildStove();
                    Helper.newDeviceSelectStoveHeadDialog(cx, new Callback<Integer>() {
                        @Override
                        public void onSuccess(Integer integer) {
                            if (book==null||book.getCookbookType()==null){
                                return;
                            }
                            LogUtils.i("20180404", "type:" + book.getCookbookType());
                            if (integer == 0) {
                                if ("4".equals(book.getCookbookType())) {
                                    if (stove.leftHead.status == 1) {
                                        Bundle bd = new Bundle();
                                        bd.putSerializable("list", cookSteps);
                                        bd.putInt("stoveHeadId", integer);
                                        new Timer().schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                                                this.cancel();
                                            }
                                        }, 1000);
                                        RecipePotActivity.start(getActivity(), book, bd, fantemp.getGuid().getGuid());
                                    } else {
                                        stoveOnTips(stove, integer, fantemp);
                                    }
                                } else {
                                    if (stove.leftHead.status == 1) {
                                        Bundle bd = new Bundle();
                                        bd.putSerializable("list", cookSteps);
                                        bd.putInt("stoveHeadId", integer);
                                        new Timer().schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                                                this.cancel();
                                            }
                                        }, 1000);
                                        RecipeRRQZActivity.start(getActivity(), book, bd, fantemp.getGuid().getGuid());
                                    } else if (stove.leftHead.status == 2) {
                                        ToastUtils.show(R.string.recipe_stove_head_is_working);
                                    } else {
                                        stoveOnTips(stove, integer, fantemp);
                                    }

                                }


                            } else {
                                if ("4".equals(book.getCookbookType())) {
                                    if (stove.rightHead.status == 1) {
                                        Bundle bd = new Bundle();
                                        bd.putSerializable("list", cookSteps);
                                        bd.putInt("stoveHeadId", integer);
                                        new Timer().schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                                                this.cancel();
                                            }
                                        }, 1000);
                                        RecipePotActivity.start(getActivity(), book, bd, fantemp.getGuid().getGuid());
                                    } else {
                                        stoveOnTips(stove, integer, fantemp);
                                    }
                                } else {
                                    if (stove.rightHead.status == 1) {
                                        Bundle bd = new Bundle();
                                        bd.putSerializable("list", cookSteps);
                                        bd.putInt("stoveHeadId", integer);
                                        new Timer().schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                                                this.cancel();
                                            }
                                        }, 1000);
                                        RecipeRRQZActivity.start(getActivity(), book, bd, fantemp.getGuid().getGuid());
                                    } else if (stove.rightHead.status == 2) {
                                        ToastUtils.show(R.string.recipe_stove_head_is_working);
                                    } else {
                                        stoveOnTips(stove, integer, fantemp);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
                }
            }
        }


        private void stoveWork(Stove stove) {
            fan = (AbsFan) stove.getParent();
            if (!fan.isConnected()) {
                ToastUtils.show("烟机已离线，连接后才可进行自动烹饪");
                return;
            }
            if (!stove.isConnected()) {
                ToastUtils.show("灶具已离线，连接后才可进行自动烹饪");
                return;
            }
            if ("R9B37".equals(stove.getDt())) {
                ToastUtils.show("不支持9B37");
                return;
            }
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    this.cancel();
                }
            }, 1000);

            if (platformCodeList != null && !platformCodeList.contains(stove.getDp())) {
                ToastUtils.show("该设备未适配，请选择其他设备");
                return;
            }

            if (IRokiFamily.IRokiDevicePlat.RQZ02.equals(stove.getDp())) {
                if (stove.rightHead.status == 0 && stove.leftHead.status == 0) {
                    stoveOffAllTips(stove, fan);
                } else {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            this.cancel();
                        }
                    }, 1000);
                    selectStove(fan);//左右炉头选择
                }
            } else {
                if (stove.rightHead.status == 0 && stove.leftHead.status == 0) {
                    stoveOffAllTips(stove, fan);
                } else {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            this.cancel();
                        }
                    }, 1000);
                    selectStove(fan);//左右炉头选择
                }
            }

        }


        private void stoveOffAllTips(final Stove stove, final AbsFan fantemp) {

            Helper.newStoveSelectAllOffTips(cx, stove, new Callback<Integer>() {

                @Override
                public void onSuccess(Integer integer) {
                    LogUtils.i("20180404", "stoveOffAllTips");
                    Bundle bd = new Bundle();
                    bd.putSerializable("list", cookSteps);
                    bd.putInt("stoveHeadId", integer);
                    if ("4".equals(book.getCookbookType())) {
                        RecipePotActivity.start(getActivity(), book, bd, fantemp.getGuid().getGuid());
                    } else {
                        RecipeRRQZActivity.start(getActivity(), book, bd, fantemp.getGuid().getGuid());
                    }

                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }

        private void stoveOnTips(final Stove stove, final Integer integer, final AbsFan fantemp) {
            Helper.newStoveSelectTipsDialog(cx, stove, integer, new Callback<Integer>() {

                @Override
                public void onSuccess(Integer flag) {
                    LogUtils.i("20180404", "stoveOnTips");
                    LogUtils.i("20180111", "falg:" + stove.rightHead.getStatus());
                    if (flag == 0) {
                        Bundle bd = new Bundle();
                        bd.putSerializable("list", cookSteps);
                        bd.putInt("stoveHeadId", integer);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                this.cancel();
                            }
                        }, 2000);
                        if ("4".equals(book.getCookbookType())) {
                            RecipePotActivity.start(getActivity(), book, bd, fantemp.getGuid().getGuid());
                        } else {
                            RecipeRRQZActivity.start(getActivity(), book, bd, fantemp.getGuid().getGuid());
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }

        private void getDeviceForFirstStep(String dcTemp) {
            List<IDevice> list = Plat.deviceService.queryAll();
            DeviceSelectUtils.getInstance().setList(list);//因为有的时候查不到设备的状况故动态查一次
            List<String> dcList = DeviceSelectUtils.getInstance().dcSubString(dcTemp);
            List<IDevice> listTemp = DeviceSelectUtils.getInstance().deviceIDev(dcList);
            if (listTemp.size() > 1) {//设备数大于1

                if (mGuid != null && !"".equals(mGuid)) {
                    for (int i = 0; i < listTemp.size(); i++) {
                        if (listTemp.get(i).getGuid().getGuid().equals(mGuid)) {
                            IDevice iDevice = listTemp.get(i);

                            if (!iDevice.isConnected()) {
                                ToastUtils.show("设备已离线，连接后才可进行自动烹饪");
                            } else {
                                if (isAlram(iDevice)) {
                                    ToastUtils.show(iDevice.getDeviceType() + "报警，恢复后才可烹饪");
                                    return;
                                }
                                if (isOpenDoor(iDevice)) {
                                    ToastUtils.show(iDevice.getDeviceType() + "门未关，无法开始工作");
                                    return;
                                }
                                if (isSteamCleanState(listTemp.get(0))) {
                                    ToastUtils.show("设备需要除垢后才能继续工作，请先除垢");
                                    return;
                                }
                                if (isWaterBoxState(iDevice)) {
                                    com.legent.utils.api.ToastUtils.show(R.string.device_alarm_water_out, Toast.LENGTH_SHORT);
                                    return;
                                }

                                if (isWhichDevice(iDevice)) {
                                    ToastUtils.show(iDevice.getDeviceType() + "被占用，停止后才可自动烹饪");
                                    return;
                                }

                                if (platformCodeList != null && !platformCodeList.contains(iDevice.getDp())) {
                                    ToastUtils.show("该设备未适配，请选择其他设备");
                                    return;
                                }
                                Bundle bd = new Bundle();
                                bd.putSerializable("list", cookSteps);
                                RecipeActivity.start(getActivity(), book, bd, iDevice.getGuid().getGuid());
                            }


                        }
                    }


                } else {

                    Helper.newDeviceSelectNewDialog(cx, listTemp, new Callback<IDevice>() {
                        @Override
                        public void onSuccess(IDevice iDevice) {
                            //判断设备是否被占用
                            //判断设备是否在线
                            if (!iDevice.isConnected()) {
                                ToastUtils.show("设备已离线，连接后才可进行自动烹饪");
                            } else {
                                if (isAlram(iDevice)) {
                                    ToastUtils.show(iDevice.getDeviceType() + "报警，恢复后才可烹饪");
                                    return;
                                }
                                if (isOpenDoor(iDevice)) {
                                    ToastUtils.show(iDevice.getDeviceType() + "门未关，无法开始工作");
                                    return;
                                }
                                if (isSteamCleanState(listTemp.get(0))) {
                                    ToastUtils.show("设备需要除垢后才能继续工作，请先除垢");
                                    return;
                                }
                                if (isWaterBoxState(iDevice)) {
                                    ToastUtils.show("水箱已弹出，请检查水箱状态");
                                    return;
                                }
                                if (isWhichDevice(iDevice)) {
                                    ToastUtils.show(iDevice.getDeviceType() + "被占用，停止后才可自动烹饪");
                                    return;
                                }

                                if (platformCodeList != null && platformCodeList.size() != 0 && !platformCodeList.contains(iDevice.getDp())) {
                                    ToastUtils.show("该设备未适配，请选择其他设备");
                                    return;
                                }
                                Bundle bd = new Bundle();
                                bd.putSerializable("list", cookSteps);
                                RecipeActivity.start(getActivity(), book, bd, iDevice.getGuid().getGuid());
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });


                }


            } else {
                if (listTemp.size() != 0) {
                    if (!listTemp.get(0).isConnected()) {
                        ToastUtils.show("设备已离线，连接后才可进行自动烹饪");
                        return;
                    } else {
                        if (isAlram(listTemp.get(0))) {
                            ToastUtils.show(listTemp.get(0).getDeviceType() + "报警，恢复后才可烹饪");
                            return;
                        }
                        if (isOpenDoor(listTemp.get(0))) {
                            ToastUtils.show(listTemp.get(0).getDeviceType() + "门未关，无法开始工作");
                            return;
                        }

                        if (isSteamCleanState(listTemp.get(0))) {
                            ToastUtils.show("设备需要除垢后才能继续工作，请先除垢");
                            return;
                        }
                        if (isWaterBoxState(listTemp.get(0))) {
                            ToastUtils.show("水箱已弹出，请检查水箱状态");
                            return;
                        }
                        if (platformCodeList != null && platformCodeList.size() != 0 && !platformCodeList.contains(listTemp.get(0).getDp())) {
                            ToastUtils.show("该设备未适配，请选择其他设备");
                            return;
                        }

                        if (isWhichDevice(listTemp.get(0))) {
                            ToastUtils.show(listTemp.get(0).getDeviceType() + "被占用，停止后才可自动烹饪");
                            return;
                        } else {
                            Bundle bd = new Bundle();
                            bd.putSerializable("list", cookSteps);
                            RecipeActivity.start(getActivity(), book, bd, listTemp.get(0).getGuid().getGuid());
                        }

                    }
                } else {
                    ToastUtils.show("无相应的设备,连接后才可自动烹饪");
                }
            }
        }

        private Boolean isOpenDoor(IDevice idevice) {
            switch (idevice.getDc()) {
                case "RZQL":
                    AbsSteamoven deviceSteam = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                    if (deviceSteam.doorState == 0) {
                        return true;
                    }
                    break;
                case "RZKY":
                    AbsSteameOvenOne deviceSteam2 = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                    if (deviceSteam2.doorStatusValue == 1) {
                        return true;
                    }
                    break;
                default:
                    break;
            }
            return false;
        }

        private Boolean isWaterBoxState(IDevice idevice) {
            switch (idevice.getDc()) {
                case "RZQL":
                    AbsSteamoven deviceSteam = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                    if (IRokiFamily.RS209.equals(deviceSteam.getDt())) {
                        return false;
                    }
                    if (deviceSteam.waterboxstate == 0) {
                        return true;
                    }
                    break;
                case "RZKY":
                    AbsSteameOvenOne steamOvenOen = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                    if (steamOvenOen.WaterStatus == 1 && (steamOvenOen.workModel > 12 && steamOvenOen.workModel < 23)) {
                        ToastUtils.show(R.string.device_alarm_water_out);
                        return true;
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
        private Boolean isSteamCleanState(IDevice idevice) {
            switch (idevice.getDc()) {
                case "RZQL":
                    AbsSteamoven deviceSteam = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                    if (deviceSteam.descaleModeStageValue != 0||deviceSteam.WeatherDescalingValue == 1) {
                        return true;
                    }
                    break;
                default:
                    break;
            }
            return false;
        }


        private Boolean isAlram(IDevice idevice) {
            switch (idevice.getDc()) {
                case "RDKX":
                    AbsOven deviceOven = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                    if (deviceOven.status == OvenStatus.AlarmStatus) {
                        return true;
                    }
                    break;
                case "RWBL":
                    AbsMicroWave deviceMicro = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                    if (deviceMicro.state == MicroWaveStatus.Alarm) {
                        return true;
                    }
                    break;
                case "RZQL":
                    AbsSteamoven deviceSteam = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                    if (deviceSteam.status == SteamStatus.AlarmStatus) {
                        return true;
                    }
                    break;
                case "RZKY":
                    AbsSteameOvenOne steamOvenOen = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                    if (steamOvenOen.powerOnStatus == SteamOvenOnePowerOnStatus.AlarmStatus) {
                        return true;
                    }
                    break;
                default:
                    break;
            }
            return false;
        }

        private Boolean isWhichDevice(IDevice idevice) {
            switch (idevice.getDc()) {
                case "RDKX":
                    AbsOven deviceOven = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                    if (deviceOven.status == OvenStatus.Working || deviceOven.status == OvenStatus.Pause ||
                            deviceOven.status == OvenStatus.PreHeat) {
                        return true;
                    }
                    break;
                case "RWBL":
                    AbsMicroWave deviceMicro = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                    if (deviceMicro.state == MicroWaveStatus.Pause || deviceMicro.state == MicroWaveStatus.Run) {
                        return true;
                    }
                    break;
                case "RZQL":
                    AbsSteamoven deviceSteam = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                    if (deviceSteam.status == SteamStatus.Pause || deviceSteam.status == SteamStatus.PreHeat ||
                            deviceSteam.status == SteamStatus.Working) {
                        return true;
                    }
                    break;
                case "RZKY":
                    AbsSteameOvenOne steamOvenOen = Plat.deviceService.lookupChild(idevice.getGuid().getGuid());
                    if (steamOvenOen.powerOnStatus == SteamOvenOnePowerOnStatus.Pause || steamOvenOen.powerOnStatus
                            == SteamOvenOnePowerOnStatus.WorkingStatus) {
                        return true;
                    }
                    break;
                default:
                    break;
            }
            return false;
        }

        private boolean isContainAllDevice(List<Dc> listDc) {
            List<IDevice> list = Plat.deviceService.queryAll();
            HashSet<String> listTemp = new HashSet<>();
            HashSet<String> listTemp2 = new HashSet<>();
            List<String> listDevice = new ArrayList<>();
            for (int i = 0; i < listDc.size(); i++) {
                if (listDc.get(i).getDc().contains("||")) {
                    String[] str = listDc.get(i).getDc().split("\\|\\|");
                    for (int j = 0; j < str.length; j++) {
                        listTemp.add(str[j]);
                    }
                } else {
                    listTemp.add(listDc.get(i).getDc());
                }
            }
            listTemp2.addAll(listTemp);
            for (IDevice idevice : list) {
                if (idevice instanceof AbsFan) {
                    if (((AbsFan) idevice).getChildStove() != null) {
                        IDevice childStove = ((AbsFan) idevice).getChildStove();
                        listDevice.add(childStove.getDc());
                    }
                } else {
                    listDevice.add(idevice.getDc());

                }


            }
            //取两个List的交集（账号下的设备与该菜谱支持的设备）
            listTemp.retainAll(listDevice);
            //该菜谱支持的设备中不包含该账号的设备
            listTemp2.removeAll(listTemp);

            if (listTemp2.size() == 0) {
                return true;
            } else {
                for (String str : listTemp2) {

                    switch (str) {
                        case "RRQZ":
                            for (int i = 0; i < listDc.size(); i++) {
                                if (listDc.get(i).getDc().contains("RRQZ") && listDevice.contains("RRQZ")) {
                                    return true;
                                } else {
                                    withOutDevice(str);
                                    break;
                                }
                            }
                            break;
                        case "KZNZ":
                            for (int i = 0; i < listDc.size(); i++) {
                                if (listDc.get(i).getDc().contains("KZNZ") && listDevice.contains("KZNZ")) {
                                    return true;
                                } else {
                                    withOutDevice(str);
                                    break;
                                }
                            }
                            break;
                        case "RZQL":
                        case "RDKX":
                            for (int i = 0; i < listDc.size(); i++) {
                                if (listDc.get(i).getDc().contains("RZKY") && listDevice.contains("RZKY")) {
                                    return true;
                                } else {
                                    withOutDevice(str);
                                    break;
                                }
                            }
                            break;
                        case "RWBL":
                            withOutDevice(str);
                            break;
                        case "RZKY":
                            if (listDc.size() > 1) {
                                if (listDevice.contains("RDKX") && listDevice.contains("RZQL")) {
                                    return true;
                                } else {
                                    withOutDevice(str);
                                }
                            } else {
                                for (int i = 0; i < listDc.size(); i++) {
                                    if (listDc.get(i).getDc().contains("RDKX") && listDevice.contains("RDKX")) {
                                        return true;
                                    } else if (listDc.get(i).getDc().contains("RZQL") && listDevice.contains("RZQL")) {
                                        return true;
                                    } else {
                                        withOutDevice(str);
                                        break;
                                    }
                                }

                            }

                            break;
                        case "RIKA":
                            for (int i = 0; i < listDc.size(); i++) {
                                if (listDc.get(i).getDc().contains("RIKA") && listDevice.contains("RIKA")) {
                                    return true;
                                } else {
                                    withOutDevice(str);
                                    break;
                                }
                            }
                            break;

                        default:
                            break;
                    }
                }
                return false;
            }

        }

        private void withOutDevice(String str) {
            HashSet<String> temp = new HashSet<>();
            temp.clear();
            if (DeviceType.RRQZ.equals(str)) {
                String s = null;
                if (buf.length() > 1) {
                    s = "或灶具";
                } else {
                    s = "灶具";
                }
                temp.add(s);

            } else if (DeviceType.KZNZ.equals(str)) {
                String s = null;
                if (buf.length() > 1) {
                    s = "或智能灶";
                } else {
                    s = "智能灶";
                }
                temp.add(s);

            } else if (DeviceType.RWBL.equals(str)) {
                String s = null;
                if (buf.length() > 1) {
                    s = "或微波炉";
                } else {
                    s = "微波炉";
                }
                temp.add(s);
            } else if (DeviceType.RZQL.equals(str)) {
                String s = null;
                if (buf.length() > 1) {
                    s = "或电蒸箱";
                } else {
                    s = "电蒸箱";
                }
                temp.add(s);
            } else if (DeviceType.RZNG.equals(str)) {
                String s = null;
                if (buf.length() > 1) {
                    s = "或智能锅";
                } else {
                    s = "智能锅";
                }
                temp.add(s);

            } else if (DeviceType.RIKA.equals(str)) {
                String s = null;
                if (buf.length() > 1) {
                    s = "或集成烟机";
                } else {
                    s = "集成烟机";
                }
                temp.add(s);

            } else if (DeviceType.RDKX.equals(str)) {
                String s = null;
                if (buf.length() > 1) {
                    s = "或电烤箱";
                } else {
                    s = "电烤箱";
                }
                temp.add(s);
            } else if (DeviceType.RZKY.equals(str)) {
                String s = null;
                if (buf.length() > 1) {
                    s = "或一体机";
                } else {
                    s = "一体机";
                }
                temp.add(s);
            }
            for (String s : temp) {
                buf.append(s);
            }
        }


        /**
         * 分享方法
         *
         * @param json
         */
        @JavascriptInterface
        public void shareCookBook(JSON json) {
            if (recipe != null) {
                ToolUtils.logEvent("菜谱", "分享菜谱:" + recipe.getName(), "roki_美食");
            }
            CookbookShareDialog.show(cx, book);
        }

        /**
         * 添加收藏
         */
        @JavascriptInterface
        public void Collection(String cookBookId) {
            if (recipe != null) {
                ToolUtils.logEvent("菜谱", "收藏菜谱:" + recipe.getName(), "roki_美食");
            }
            EventUtils.postEvent(new HomeRecipeViewEvent(HomeRecipeViewEvent.RecipeFavoriteChange));
        }

        /**
         * 取消收藏
         */
        @JavascriptInterface
        public void cancelCollect(String cookBookId) {
            if (recipe != null) {
                ToolUtils.logEvent("菜谱", "取消收藏菜谱:" + recipe.getName(), "roki_美食");
            }
            EventUtils.postEvent(new HomeRecipeViewEvent(HomeRecipeViewEvent.RecipeFavoriteChange));
        }

        /**
         * more页面
         */
        @JavascriptInterface
        public void toMoreCook(String cookBookId) {
            LogUtils.i("20181117", "ci:" + cookBookId);
            Bundle bd = new Bundle();
            bd.putString("cookBookId", cookBookId);
            UIService.getInstance().postPage(PageKey.MoreCook, bd);
        }

        /**
         * 上传菜谱相册
         */
        @JavascriptInterface
        public void uploadCook(String cookBookId) {
            mId = Long.parseLong(cookBookId);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int selfPermission = ContextCompat.checkSelfPermission(cx, Manifest.permission.CAMERA);
                if (selfPermission == 0) {
                    Activity atv = UIService.getInstance().getMain().getActivity();
                    RecipeShowPage.showCooking(atv, mId);
                } else {
                    PermissionsUtils.checkPermission(cx, Manifest.permission.CAMERA, PermissionsUtils.CODE_RECIPE_DETAIL_CAMERA);
                }
            } else {
                Activity atv = UIService.getInstance().getMain().getActivity();
                RecipeShowPage.showCooking(atv, mId);
            }

        }

        /**
         * 商品详情页
         */
        @JavascriptInterface
        public void toYeGo(String url) {
            if (LoginUtil.checkWhetherLogin(cx, PageKey.UserLogin)) {
                Bundle bd = getArguments();
                bd.putString(PageArgumentKey.Url, url);
                UIService.getInstance().postPage(PageKey.YouzanOrderDetails, bd);
            }
        }

        /**
         * 返回方法
         */
        @JavascriptInterface
        public void goback(boolean b) {

        }

        @JavascriptInterface
        public void alertText(String text) {
            DialogHelper.newDialog_OkCancel(cx, text, null,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();

        }

    }

    @Subscribe
    public void onEvent(UserLoginEvent event) {
        if (!Plat.accountService.isLogon()) {
            return;
        }
        ss.getIsCollectBookId(Plat.accountService.getCurrentUserId(), ID, new Callback<Reponses.IsCollectBookResponse>() {
            @Override
            public void onSuccess(Reponses.IsCollectBookResponse isCollectBookResponse) {
                if (isCollectBookResponse != null) {
                    isCollect = isCollectBookResponse.isCollect;
                    if (isCollect && imgFavority != null) {
                        imgFavority.setSelected(true);
                    } else {
                        imgFavority.setSelected(false);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show(t.getMessage());
            }
        });
    }
    @Subscribe
    public void onEvent(RecipeCookFinishEven event) {
        LogUtils.i("20210929", "返回" );
//        changeThread();
//        getActivity().finish();
//
//        Message msg = Message.obtain();
//        msg.what = 1;
//        hander.sendMessage(msg);


        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                ToastUtils.show("返回");
                UIService.getInstance().popBack();

            }
        });
    }

}

