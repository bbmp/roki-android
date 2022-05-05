package com.robam.roki.ui.page;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.dao.DaoHelper;
import com.legent.plat.Plat;
import com.legent.plat.events.UserLoginEvent;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.services.DeviceService;
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
import com.robam.common.events.ShareRecipePictureEvent;
import com.robam.common.events.UMPushRecipeEvent;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Dc;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.recipe.AbsRecipeCookTask;
import com.robam.common.recipe.inter.IRecipe;
import com.robam.common.recipe.step.DeviceStatusCheck;
import com.robam.common.services.StoreService;
import com.robam.common.ui.UiHelper;
import com.robam.common.util.RecipeRequestIdentification;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.CookbookShareDialog;
import com.robam.roki.ui.view.umpush.UMPushMsg;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.LoginUtil;

import org.eclipse.jetty.util.ajax.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robam.roki.ui.form.MainActivity.start;

/**
 * Created by Dell on 2018/4/20.
 */

public class AutoRecipeDetailPage extends BasePage {
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


    public static void show(long recipeId, int source, String entranceCode) {
        pageKey = source;
        Bundle bd = new Bundle();
        bd.putLong(PageArgumentKey.BookId, recipeId);
        bd.putString(PageArgumentKey.entranceCode, entranceCode);
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

    private Long ID;
    private String entranceCode;
    private CookStep cookStepTemp;
    private boolean isRQZ;//是否燃氣灶
    private Recipe book;
    private AbsFan fan;

    private Stove.StoveHead stoveHead;
    private ArrayList<CookStep> cookSteps;
    //private final String H5_BASE_URL = "http://h5.myroki.com:80/#/cookBookDetail";//废弃
    //private final String H5_BASE_URL = "http://h5.myroki.com/dist/index.html#/cookBookDetail";
    //private final String H5_BASE_URL = "http://develop.h5.myroki.com/dist/index.html#/cookBookDetail";
//    private final String H5_BASE_URL = "http://develop.h5.myroki.com/dist/index.html#/automaticCooking";
    private final String H5_BASE_URL = "http://h5.myroki.com/dist/index.html#/automaticCooking";
    private final String H5_BASE_MORE_URL = "http://h5.myroki.com:80/#/moreCook?cookbookId=";
    //private final String H5_BASE_MORE_URL = "http://develop.h5.myroki.com:80/#/moreCook?cookbookId=";
    //private final String H5_BASE_MORE_URL = "http://develop.h5.myroki.com/dist/index.html#/moreCook?cookbookId=";


    IRokiDialog dialogTips = null;
    Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (buf.length() != 0)
                        ToastUtils.show("无" + buf + ",连接后才可自动烹饪", Toast.LENGTH_SHORT);
                    buf.delete(0, buf.length());
                    break;
            }
        }
    };
    StringBuffer buf = new StringBuffer();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_recipe_detail, container, false);
        ButterKnife.inject(this, view);
        setiingWebToJS();
        ID = getArguments().getLong(PageArgumentKey.BookId);
        entranceCode = getArguments().getString(PageArgumentKey.entranceCode);

        if (ID == null) {
            ToastUtils.show("菜谱数据解析异常", Toast.LENGTH_SHORT);
            UIService.getInstance().popBack();
        }

        init(ID, entranceCode);
        dialogTips = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_02);
        return view;
    }

    private void initView() {

    }


    @OnClick(R.id.imgreturn)
    public void onClickReturn() {
        if (entranceCode != null && entranceCode.equals("code13")) {
            start(activity);
        } else {
            UIService.getInstance().popBack();
        }
    }


    //收藏点击事件
    @OnClick(R.id.imgFavority)
    public void onClickCollect() {
        if (UiHelper.checkAuthWithDialog(cx, PageKey.UserLogin)) {
            UiHelper.onFavority(book, imgFavority, null);
        }
    }

    //收藏菜谱刷新
    @Subscribe
    public void onEvent(FavorityBookRefreshEvent event) {
        if (event.bookId != book.id)
            return;
        DaoHelper.refresh(book);
        imgFavority.setSelected(book.isFavority);
    }

    @OnClick(R.id.imgShare)
    public void onClickShare() {
        CookbookShareDialog.show(cx, book);
    }

    private void setiingWebToJS() {
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
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
        });
        webview.setScrollHeightLister(new ExtWebView.ScrollHeightLister() {
            @Override
            public void ScrollChangedHeightW(int l, int t, int oldl, int oldt) {
                if (oldt > 1600) {
                    title_item.setVisibility(View.VISIBLE);
                }
                if (oldt < 1600) {
                    title_item.setVisibility(View.GONE);
                }
            }
        });

    }


    public boolean isCollect;

    void init(long recipeid, String entranceCode) {
        long userId = Plat.accountService.getCurrentUserId();
        ss.getIsCollectBookId(userId, recipeid, new Callback<Reponses.IsCollectBookResponse>() {
            @Override
            public void onSuccess(Reponses.IsCollectBookResponse isCollectBookResponse) {
                if (isCollectBookResponse != null) {
                    isCollect = isCollectBookResponse.isCollect;
                    if (isCollect) {
                        imgFavority.setSelected(true);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }

        });
        ss.getCookbookById(recipeid, entranceCode, "0", new Callback<Recipe>() {
            @Override
            public void onSuccess(Recipe recipe) {
                if (recipe == null) {
                    ToastUtils.show("没有这道菜", Toast.LENGTH_SHORT);
                    UIService.getInstance().popBack();
                    return;
                }
                book = recipe;
                book.setIsFavority(isCollect);
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

            @Override
            public void onFailure(Throwable t) {
                UIService.getInstance().popBack();
            }

        });

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
            webview.reload();
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

    @Subscribe
    public void onEvent(UserLoginEvent event) {
        UIService.getInstance().popBack();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (null != webview) {
            if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
                webview.goBack();// 返回前一个页面
                return true;
            }
        }
        if (entranceCode != null && entranceCode.equals("code13")) {
            start(activity);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public class CallMethodFromAndroidLister {
        Context context;

        public CallMethodFromAndroidLister(Context context) {
            this.context = context;
        }

        /**
         * 添加到购物车
         */
        @JavascriptInterface
        public void addBuy(String cookBookId) {
            ToastUtils.showShort(R.string.add_buy);
        }

        /**
         * 取消购物车
         */
        @JavascriptInterface
        public void cancelBuy(String cookBookId) {
            ToastUtils.showShort(R.string.cancel_buy);
        }


        @JavascriptInterface
        public void userIsNotLog(boolean notLog) {

            if (notLog) {
                final IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_00);
                dialogByType.setTitleText(R.string.user_Whether_not_login);
                dialogByType.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogByType.dismiss();
                        UIService.getInstance().postPage(PageKey.UserLogin);
                    }
                });
                dialogByType.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                dialogByType.show();
            }

        }

        /**
         * 点赞提示
         *
         * @param text
         */
        @JavascriptInterface
        public void bombbox(String text) {
            ToastUtils.showShort(text);
        }

        protected Map<String, Object> mPreRunMap = new HashMap<String, Object>();
        String[] dcstr;
        boolean occupyFlag;
        String headId;

        /**
         * 开始烹饪
         */
        @JavascriptInterface
        public void startCooking(String cookBookId) {
            if (cookSteps.size() == 0) {
                ToastUtils.show("此菜谱还未上传完，请过阵子再来看哈", Toast.LENGTH_SHORT);
                return;
            }

            if (cookSteps != null && "".equals(cookSteps.get(0).getDc())) {
                ToastUtils.show("此菜谱不支持自动烹饪", Toast.LENGTH_SHORT);
                return;
            }

            AbsRecipeCookTask absRecipeCookTask = new AbsRecipeCookTask();
            mPreRunMap = absRecipeCookTask.prerun(cookSteps.get(0));
            checkDevice(mPreRunMap, new AbsRecipeCookTask.RecipeDeviceSelect<String>() {

                @Override
                public void selectguid(String guid, String... head) {
                    IDevice device = DeviceService.getInstance().lookupChild(guid);
                    if (device != null) {
                        boolean deviceIsCon = DeviceStatusCheck.getInstance().getDeviceConnect(device.getDc());
                        if (deviceIsCon) {
                            ToastUtils.showShort(getDeviceName(device.getDc()) + getString(R.string.recipe_device_no_connect));
                            return;
                        }


                        //判断设备是否被占用
                        if ("RRQZ".equals(device.getDc())) {
                            headId = head[0];
                            if (IRokiFamily.R9B39.equals(device.getDt())) {
                                Stove stove = (Stove) device;
                                if (stove.isLock) {
                                    ToastUtils.show("请在灶具上解锁", Toast.LENGTH_SHORT);
                                    return;
                                }
                            }
                            occupyFlag = DeviceStatusCheck.getInstance().getStatus(device.getDc(), head[0]);
                        } else {
                            occupyFlag = DeviceStatusCheck.getInstance().getStatus(device.getDc(), null);
                        }

                        if (occupyFlag) {
                            ToastUtils.showShort(getDeviceName(device.getDc()) + getString(R.string.recipe_device_occupy));
                            return;
                        }

                        Bundle bd = new Bundle();
                        bd.putSerializable(PageArgumentKey.BookId, book);
                        bd.putSerializable(PageArgumentKey.CookSteps, cookSteps);
                        bd.putString(PageArgumentKey.DeviceGuid, guid);
                        bd.putString(PageArgumentKey.HeadId, headId);
                        UIService.getInstance().postPage(PageKey.AutoRecipeStart, bd);
                    }
                }

                @Override
                public void cancelselect() {

                }
            });

        }

        private String getDeviceName(String dc) {
            if ("RZQL".equals(dc)) {
                return "电蒸箱";
            } else if ("RDKX".equals(dc)) {
                return "电烤箱";
            } else if ("RWBL".equals(dc)) {
                return "微波炉";
            } else if ("RZKY".equals(dc)) {
                return "蒸烤一体机";
            } else if ("RRQZ".equals(dc)) {
                return "灶具";
            }
            return null;
        }

        Dialog dialog;

        private void checkDevice(Map<String, Object> map, final AbsRecipeCookTask.RecipeDeviceSelect callback3) {
            if (!(Boolean) map.get(IRecipe.RECIPE_STEP_DC) || !(Boolean) map.get(IRecipe.DEVICE_IFHAS)) {
                ToastUtils.showShort("当前无设备");
            } else {
                List<String> occupy = (List<String>) map.get(IRecipe.DEVICE_OCCUPY);
                final List<String> availb = (List<String>) map.get(IRecipe.DEVICE_AVAILB);
                availb.addAll(occupy);
                if (Utils.isStove(availb.get(0))) {

                } else {
                    if (availb.size() == 1) {
                        callback3.selectguid(availb.get(0));
                        return;
                    }
                    IDevice device = DeviceService.getInstance().lookupChild(availb.get(0));
                    final String name1 = device.getDeviceType().getName();
                    final String id1 = device.getDeviceType().getID();
                    device = DeviceService.getInstance().lookupChild(availb.get(1));
                    final String name2 = device.getDeviceType().getName();
                    final String id2 = device.getDeviceType().getID();

                    dialog.show();
                }

            }
        }


        //开始烹饪时加载统计
        private void startLoadingStatistical() {
            ss.getCookbookById(book.id, RecipeRequestIdentification.RECIPE_COOKING, new Callback<Recipe>() {
                @Override
                public void onSuccess(Recipe recipe) {

                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }

        /**
         * 分享方法
         *
         * @param json
         */
        @JavascriptInterface
        public void shareCookBook(JSON json) {
            CookbookShareDialog.show(cx, book);
            //  ToolUtils.sendAnalystics(mTracker, "CookBookInfo", "ShareCookBook：" + Long.valueOf(cookBookId), "CookBook");
        }

        /**
         * 添加收藏
         */
        @JavascriptInterface
        public void Collection(String cookBookId) {
            EventUtils.postEvent(new HomeRecipeViewEvent(HomeRecipeViewEvent.RecipeFavoriteChange));
        }

        /**
         * 取消收藏
         */
        @JavascriptInterface
        public void cancelCollect(String cookBookId) {
            EventUtils.postEvent(new HomeRecipeViewEvent(HomeRecipeViewEvent.RecipeFavoriteChange));
        }

        /**
         * more页面
         */
        @JavascriptInterface
        public void toMoreCook(String cookBookId) {
            Bundle bd = new Bundle();
            bd.putString("cookBookId", cookBookId);
            UIService.getInstance().postPage(PageKey.MoreCook, bd);
        }

        /**
         * 上传菜谱相册
         */
        @JavascriptInterface
        public void uploadCook(String cookBookId) {
            long id = Long.parseLong(cookBookId);
            Activity atv = UIService.getInstance().getMain().getActivity();
            RecipeShowPage.showCooking(atv, id);
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
            UIService.getInstance().popBack();
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

}
