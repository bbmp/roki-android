package com.robam.roki.ui.page.recipedetail;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.events.AppVisibleEvent;
import com.legent.plat.Plat;
import com.legent.plat.events.FloatHelperEvent;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.CookBookVideo;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.CookbookPlatforms;
import com.robam.common.pojos.Dc;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.PreSubStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.services.CookbookManager;
import com.robam.common.services.StoreService;
import com.robam.common.util.RecipeUtils;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.R;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.RomUtils;
import com.robam.roki.ui.adapter3.RvDeviceAdapter;
import com.robam.roki.ui.adapter3.RvRecipeDetailAdapter;
import com.robam.roki.ui.bean3.RecipeDetailItem;
import com.robam.roki.ui.dialog.CookbookShareDialog;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.form.RecipeActivity;
import com.robam.roki.ui.form.RecipeNoDeviceActivity;
import com.robam.roki.ui.form.RecipePotActivity;
import com.robam.roki.ui.form.RecipeRRQZActivity;
import com.robam.roki.ui.page.ClassifyTagRecipePage;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;
import com.robam.roki.ui.widget.base.BaseDialog;
import com.robam.roki.ui.widget.view.PlayerView;
import com.robam.roki.utils.ButtonUtils;
import com.robam.roki.utils.DeviceSelectUtils;
import com.robam.roki.utils.PermissionsUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @author r210190
 * des：3.2菜谱详情页
 */

public class RecipeDetailPage extends MyBasePage<MainActivity> implements KeyboardWatcher.SoftKeyboardStateListener{

    private MultiTransformation options = new MultiTransformation<>(new CenterCrop(),
            new RoundedCornersTransformation(999, 0));
    private static int pageKey = 0;
    private static Long ID;
    private String entranceCode;
    private String platformCode;
    private String mGuid;
    private CookStep cookStepTemp;
    private boolean isRQZ;//是否燃氣灶
    private AbsFan fan;

    private final String STATIC_COOK = "10";
    /**
     * 菜谱详情
     */
    private Recipe cookbook;
    /**
     * 烹饪步骤
     */
    private CookBookVideo cookBookVideo;
    private PlayerView mPlayerView;
    /**
     * 详情数据adapter
     */
    private RvRecipeDetailAdapter rvRecipeDetailAdapter;
    /**
     * 自动烹饪
     */
    private AppCompatButton btnAutomatic;
    /**
     * 顶部title
     */
    private RelativeLayout titleItem;
    /**
     * 返回
     */
    private ImageView imgreturn;
    /**
     * 收藏
     */
    private ImageView imgFavority;
    /**
     * 分享
     */
    private ImageView imgShare;
    /**
     * 添加设备
     */
    private View btnAddDevice;
    /**
     * 不使用设备
     */
    private View btnNotDevice;
    private BaseDialog baseDialog;
    private TextView tvCookName;

    /**
     * 烹饪步骤 不包含备菜步骤
     */
    private ArrayList<CookStep> cookSteps;

    StringBuffer buf = new StringBuffer();
    /**
     * 步骤
     */
    private int step;
    int firstItemPosition;
    static List<Long> idTemp = new ArrayList();

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
        if (idTemp == null || idTemp.size() == 0) {
            idTemp = new ArrayList<>();
            pageKey = source;
            Bundle bd = new Bundle();
            bd.putInt("pageKey", source);
            bd.putLong(PageArgumentKey.BookId, recipeId);
            UIService.getInstance().postPage(PageKey.RecipeDetail, bd);
        } else {
            ID = recipeId;
            idTemp.add(ID);
            EventUtils.postEvent(new PageBackEvent("FloatWindow"));
        }
    }

    private RecyclerView rvRecipeDetail;

    @Override
    protected int getLayoutId() {
        return R.layout.page_recipe_detail_3;
    }


    private void dealScrollEvent(int firstItemPosition, int lastItemPosition, LinearLayoutManager linearManager) {

    }

    /**
     * 修改状态栏颜色，支持4.4以上版本
     * @param activity
     * @param colorId
     */
    public static void setStatusBarColor(Activity activity, int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(colorId));
        }
    }


    /**
     *  修改状态栏文字颜色，这里小米，魅族区别对待。
     */
    public static void setLightStatusBar(final Activity activity, final boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            switch (RomUtils.getLightStatusBarAvailableRomType()) {
                case RomUtils.AvailableRomType.MIUI:
                    MIUISetStatusBarLightMode(activity, dark);
                    break;

                case RomUtils.AvailableRomType.FLYME:
                    setFlymeLightStatusBar(activity, dark);

                    break;

                case RomUtils.AvailableRomType.ANDROID_NATIVE:
                    setAndroidNativeLightStatusBar(activity, dark);
                    break;

            }
        }
    }


    public static boolean MIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && RomUtils.isMiUIV7OrAbove()) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    }
                }
            } catch (Exception e) {

            }
        }
        return result;
    }

    private static boolean setFlymeLightStatusBar(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }

    private static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
    @Override
    protected void initView() {
//        setStateBarTransparent();
//        StatusBarUtils.setColor(cx, Color.WHITE);
        StatusBarUtils.setTextDark(cx, true);
        titleItem = (RelativeLayout) findViewById(R.id.title_item);
        imgreturn = (ImageView) findViewById(R.id.imgreturn);
        imgFavority = (ImageView) findViewById(R.id.imgFavority);
        imgShare = (ImageView) findViewById(R.id.imgShare);
        rvRecipeDetail = (RecyclerView) findViewById(R.id.rv_recipe_detail);
        btnAutomatic = (AppCompatButton) findViewById(R.id.btn_automatic);
        rvRecipeDetail.setLayoutManager(new LinearLayoutManager(cx, RecyclerView.VERTICAL, false));
        rvRecipeDetailAdapter = new RvRecipeDetailAdapter(this);
        rvRecipeDetail.setAdapter(rvRecipeDetailAdapter);
        tvCookName = (TextView) findViewById(R.id.tv_cook_name);
        //默认黑色
        setStatusBarColor(getActivity(),R.color.black);
        setLightStatusBar(getActivity(),false);
        imgFavority.setImageResource(R.drawable.ic_recipe_favority_black_shape);

        rvRecipeDetail.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();


//                layoutManager.getChildAt()

                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取第一个可见view的位置

                    firstItemPosition = linearManager.findFirstVisibleItemPosition();

                    //获取最后一个可见view的位置
                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        dealScrollEvent(firstItemPosition, lastItemPosition, linearManager);
                    }


                    if (firstItemPosition == 0) {
//                        btnAutomatic.setVisibility(View.GONE);
                        tvCookName.setVisibility(View.INVISIBLE);
                        titleItem.setBackgroundColor(Color.TRANSPARENT);
                        imgreturn.setImageResource(R.mipmap.icon_back_has_black);
                        imgFavority.setImageResource(R.drawable.ic_recipe_favority_black_shape);
                        imgFavority.setSelected(cookbook.collected);
                        imgShare.setImageResource(R.mipmap.icon_share_black);
                        setStatusBarColor(getActivity(),R.color.black);
                        setLightStatusBar(getActivity(),false);

                    } else {
                        if (STATIC_COOK.equals(cookbook.getCookbookType())) {
                            btnAutomatic.setText("菜谱教学");
                        }
                        btnAutomatic.setVisibility(View.VISIBLE);
                        setStatusBarColor(getActivity(),R.color.white);
                        setLightStatusBar(getActivity(),true);
                        titleItem.setBackgroundColor(Color.WHITE);
                        tvCookName.setVisibility(View.VISIBLE);
                        imgreturn.setImageResource(R.drawable.icon_left_recipe);
                        imgFavority.setImageResource(R.drawable.ic_recipe_favority);
                        imgFavority.setSelected(cookbook.collected);
                        imgShare.setImageResource(R.mipmap.ic_recipe_detail_share);
                        tvCookName.setText(cookbook.name);

                    }
                }
            }
        });

//        fabRecipe = (DragFloatActionButton2) findViewById(R.id.fab_recipe);
//        fabRecipe.setOnClickListener(new DragFloatActionButton2.OnClickListener() {
//
//            @Override
//            public void onClick() {
////                SpeechCookBaiduActivity.start(cx, cookbook);
//
//                Bundle bd = new Bundle();
//                bd.putSerializable("list", cookSteps);
//                RecipeNoDeviceActivity.start(getActivity(), cookbook, bd, "", step);
//            }
//        });
        setOnClickListener(btnAutomatic, imgShare, imgFavority, imgreturn);
    }

    private void noDevice(){
        if (baseDialog != null) {
            baseDialog.dismiss();
        }
//            SpeechCookBaiduActivity.start(cx, cookbook);
        Bundle bd = new Bundle();
        bd.putSerializable("list", cookSteps);
        bd.putSerializable("recipe", cookbook);
        RecipeNoDeviceActivity.start(getActivity(), cookbook, bd, "");
    }

    @Override
    public void onClick(View view) {
        if (view == btnAutomatic) {
            if (STATIC_COOK.equals(cookbook.getCookbookType())) {
                noDevice();
            }else {
                if (ButtonUtils.isFastDoubleClick(view.getId())) {
                    ToastUtils.show("请不要连续点击");
                    return;
                }
//            ToastUtils.show("自动烹饪");
                if (Plat.accountService.isLogon()) {
                    //燃气灶和设备菜谱逻辑区别
                    if (isRQZ) {
                        rqzWork();
                    } else {
                        if (mGuid != null && !"".equals(mGuid)) {
                            work(mGuid);
                        } else {
                            deviceDialog();
                        }
                    }
                } else {
                    CmccLoginHelper.getInstance().toLogin();
                }
            }
        } else if (view == btnNotDevice) {
            noDevice();
//            ToastUtils.show("不用设备");
        } else if (view == btnAddDevice) {
//            ToastUtils.show("添加设备");
            if (baseDialog != null) {
                baseDialog.dismiss();
            }
            Bundle bundle = new Bundle();
            bundle.putInt("form", 1);
            UIService.getInstance().postPage(PageKey.DeviceAdd, bundle);
        } else if (view == imgShare) {
            share();
        } else if (view == imgFavority) {
            collect();
        } else if (view == imgreturn) {
            backPageRec();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backPageRec();
            return true;
        }
        return false;
    }

    //逐级返回页面
    private void backPageRec() {
        if (idTemp.size() <= 1) {
            if (rvRecipeDetailAdapter != null) {
                rvRecipeDetailAdapter.setStopVideo();
            }
            UIService.getInstance().popBack();
        } else {
            ID = idTemp.get(idTemp.size() - 2);
            idTemp.remove(idTemp.size() - 1);
            getCookDetail(ID);
        }
    }

    @Override
    protected void initData() {
        ID = getArguments().getLong(PageArgumentKey.BookId);
        entranceCode = getArguments().getString(PageArgumentKey.entranceCode);
        platformCode = getArguments().getString(PageArgumentKey.platformCode);
        mGuid = getArguments().getString(PageArgumentKey.Guid);
        pageKey = getArguments().getInt("pageKey", 0);
        LogUtils.i("202010241031", "mGuid:::" + mGuid);
//        recipe = (Recipe) getArguments().getSerializable(PageArgumentKey.Bean);
        if (ID == null) {
            ToastUtils.show("菜谱数据解析异常");
            UIService.getInstance().popBack();
            return;
        }
        KeyboardWatcher.with(activity)
                .setListener(this);
        getCookDetail(ID);
        idTemp.add(ID);


    }

    /**
     * 获取菜谱详情
     */
    private void getCookDetail(long recipeid) {

        ProgressDialogHelper.setRunning(cx, true);
        RokiRestHelper.getCookbookById(recipeid, "1", "1", Reponses.CookbookResponse.class, new RetrofitCallback<Reponses.CookbookResponse>() {

            @Override
            public void onSuccess(Reponses.CookbookResponse cookbookResponse) {
                if (null != cookbookResponse) {
                    cookbook = cookbookResponse.cookbook;
                    imgFavority.setSelected(cookbook.collected);
                    boolean isLogin = Plat.accountService.isLogon();
                    if (!isLogin) {
                        setData();
                    } else {
                        setData();
                        RokiRestHelper.getIsCollectBook(Plat.accountService.getCurrentUserId(), recipeid, Reponses.IsCollectBookResponse.class,
                                new RetrofitCallback<Reponses.IsCollectBookResponse>() {
                            @Override
                            public void onSuccess(Reponses.IsCollectBookResponse isCollectBookResponse) {
                                try {
                                    if (isCollectBookResponse != null) {
                                        boolean isCollect = isCollectBookResponse.isCollect;
                                        if (isCollect) {
                                            imgFavority.setSelected(true);
                                            if (cookbook.id == recipeid) {
                                                cookbook.collected = true;
                                                imgFavority.setImageResource(R.drawable.ic_recipe_favority_black_shape);
                                            }
                                        } else {
                                            imgFavority.setSelected(false);
                                            if (cookbook.id == recipeid) {
                                                cookbook.collected = false;
                                                imgFavority.setImageResource(R.drawable.ic_recipe_favority_black_shape);
                                            }
                                        }
                                        imgFavority.setSelected(cookbook.collected);
                                    }
                                } catch (Exception e) {

                                }
                            }

                            @Override
                            public void onFaild(String err) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFaild(String err) {

            }
        });

    }

    /**
     * 设置菜谱数据
     */
    private void setData() {

        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(new Runnable() {
            @Override
            public void run() {
                // ...
                if (cookbook != null) {
                    //判断此菜谱是否是燃气菜谱
                    if (cookbook.getJs_dcs().size() == 0) {
                        isRQZ = true;
                    } else {
                        for (Dc dc : cookbook.getJs_dcs()) {
                            if (DeviceType.RRQZ.equals(dc.getDc())) {
                                isRQZ = true;
                                break;
                            }
                        }
                    }

                    ArrayList<RecipeDetailItem> recipeDetailItems = new ArrayList<>();
                    //showType失效，方法中判断showType
                    String recipeImgUrl = RecipeUtils.getRecipeImgUrl(cookbook);
                    recipeDetailItems.add(new RecipeDetailItem(cookbook.showType, cookbook.video, recipeImgUrl));
                    recipeDetailItems.add(new RecipeDetailItem(cookbook.materials));
                    int step1 = 0;
                    int step2 = 0;
                    cookSteps = new ArrayList<>();
                    if (null != cookbook.js_cookSteps && cookbook.js_cookSteps.size() != 0) {
                        for (PreSubStep cookprepareStep : cookbook.preStep.getPreSubSteps()) {
                            cookprepareStep.isPrepareStep = true;
                            recipeDetailItems.add(new RecipeDetailItem(cookprepareStep));
                            step1++;
                        }
                    }
                    if (null != cookbook.js_cookSteps && cookbook.js_cookSteps.size() != 0) {
                        for (CookStep cookStep : cookbook.js_cookSteps) {
                            if (!cookStep.isPrepareStep) {
                                cookStep.isPrepareStep = false;
                                cookStep.order = cookSteps.size() + 1;
                                recipeDetailItems.add(new RecipeDetailItem(cookStep));
                                cookSteps.add(cookStep);
                                step2++;
                            }

                        }
                    }

                    int finalStep = step1;
                    int finalStep1 = step2;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvCookName.setText(cookbook.name);
                            rvRecipeDetailAdapter.setCookbook(cookbook, finalStep, finalStep1);
                            if (recipeDetailItems != null && recipeDetailItems.size() > 0) {
                                recipeDetailItems.get(0).isPlaying = true;
                            }
                            rvRecipeDetailAdapter.setNewInstance(recipeDetailItems);
//                            rvRecipeDetail.scrollToPosition(0);
                            ProgressDialogHelper.setRunning(cx, false);
                            loadRecipeData();

                        }
                    });

                }
            }
        });

    }


    /**
     * 获取大家都在做菜谱数据
     */
    private void loadRecipeData() {
        RokiRestHelper.getbyTagOtherCooks(null, true, 0, 5, -1, null,
                Reponses.PersonalizedRecipeResponse.class, new RetrofitCallback<Reponses.PersonalizedRecipeResponse>() {
                    @Override
                    public void onSuccess(Reponses.PersonalizedRecipeResponse personalizedRecipeResponse) {
                        if (null != personalizedRecipeResponse) {
                            List<Recipe> recipes = personalizedRecipeResponse.cookbooks;
                            if (recipes != null && recipes.size() != 0) {
                                rvRecipeDetailAdapter.addData(new RecipeDetailItem(recipes));
                                rvRecipeDetailAdapter.addOnFootItemClickListener(new RvRecipeDetailAdapter.OnFootItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        ID = recipes.get(position).id;
                                        getCookDetail(ID);
                                        idTemp.add(ID);

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onFaild(String err) {
                        ToastUtils.show(err);
                    }
        });
    }

    /**
     * 选择设备dialog
     */

    private void deviceDialog() {
//        if (cookbook != null && "".equals(cookbook.js_cookSteps.get(0).getDc())) {
//            ToastUtils.show("此菜谱不支持自动烹饪");
//            return;
//        }
//        if (cookbook.js_cookSteps.size() == 0) {
//            ToastUtils.show("此菜谱还未上传完，请过阵子再来看哈");
//            return;
//        }
        List<Dc> listDc = cookbook.getJs_dcs();
//        if (!isContainAllDevice(listDc)) {
//            return;
//        }
        List<IDevice> list = Plat.deviceService.queryAll();
        DeviceSelectUtils.getInstance().setList(list);//因为有的时候查不到设备的状况故动态查一次
        List<String> dcList = DeviceSelectUtils.getInstance().dcSubString(cookbook.getJs_dcs().get(0).dc);
        List<IDevice> listTemp = DeviceSelectUtils.getInstance().deviceIDev(dcList);

        //根据菜谱参数筛选支持的设备
        List<CookbookPlatforms> cookbookPlatformsList = cookbook.cookbookPlatformsList;
        List<String> platformCodeList = new ArrayList<>();
        if (cookbookPlatformsList != null) {
            for (CookbookPlatforms cookbookPlatforms : cookbookPlatformsList
            ) {
                String platformCode = cookbookPlatforms.platformCode;
                platformCodeList.add(platformCode);
            }
        }
        List<IDevice> iDevices1 = DeviceSelectUtils.getInstance().deviceIDevPlatformCode(listTemp, platformCodeList);

        baseDialog = new BaseDialog(cx);
        baseDialog.setContentView(R.layout.dialog_device);
        baseDialog.setCanceledOnTouchOutside(true);
        baseDialog.setGravity(Gravity.BOTTOM);
        baseDialog.setWidth(getActivity().getWindowManager().getDefaultDisplay().getWidth());
        RecyclerView rvDevice = (RecyclerView) baseDialog.findViewById(R.id.rv_device);
        rvDevice.setLayoutManager(new LinearLayoutManager(cx, LinearLayoutManager.VERTICAL, false));
        RvDeviceAdapter rvDeviceAdapter = new RvDeviceAdapter();
        rvDevice.setAdapter(rvDeviceAdapter);
//        List<IDevice> iDevices = DeviceService.getInstance().queryAll();
        if (iDevices1 == null || iDevices1.size() == 0) {
            View view = getLayoutInflater().inflate(R.layout.item_detail3_device_empty, new FrameLayout(cx), false);
            btnAddDevice = view.findViewById(R.id.btn_add_device);
            btnNotDevice = view.findViewById(R.id.btn_not_device);
            setOnClickListener(btnAddDevice, btnNotDevice);
            rvDeviceAdapter.setEmptyView(view);
        } else {
            rvDeviceAdapter.addData(iDevices1);
            View head = getLayoutInflater().inflate(R.layout.item_detail3_device_header, new FrameLayout(cx), false);
            rvDeviceAdapter.addHeaderView(head);
            btnNotDevice = head.findViewById(R.id.btn_not_device);
            View foot = getLayoutInflater().inflate(R.layout.item_detail3_device_foot, new FrameLayout(cx), false);
            rvDeviceAdapter.addFooterView(foot);
            btnAddDevice = foot.findViewById(R.id.ll_add_device);
            setOnClickListener(btnAddDevice, btnNotDevice);
        }
        baseDialog.show();
        rvDeviceAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
//                mGuid = rvDeviceAdapter.getItem(position).getGuid().getGuid();
                if (baseDialog != null) {
                    baseDialog.dismiss();
                }
                work(rvDeviceAdapter.getItem(position).getGuid().getGuid());
//                Bundle bd = new Bundle();
//                bd.putSerializable("list", cookSteps);
//                RecipeActivity.start(getActivity(), cookbook, bd, rvDeviceAdapter.getItem(position).getGuid().getGuid());
            }
        });
    }

    /**
     * 分享
     */
    public void share() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int selfPermission = ContextCompat.checkSelfPermission(cx, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (selfPermission == 0) {

                CookbookShareDialog.show(cx, cookbook);
            } else {
                PermissionsUtils.checkPermission(cx, Manifest.permission.READ_EXTERNAL_STORAGE, PermissionsUtils.CODE_RECIPE_DETAIL_SHARE);
            }
        } else {
            CookbookShareDialog.show(cx, cookbook);
        }
        if (CookbookShareDialog.cookbookShareDialog!=null) {
            CookbookShareDialog.cookbookShareDialog.addOnFcListener(new CookbookShareDialog.OnFcListener() {
                @Override
                public void onFcClick() {
                    startFloatingService();
                }
            });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionsUtils.CODE_RECIPE_DETAIL_SHARE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] ==PackageManager.PERMISSION_GRANTED) {

                      CookbookShareDialog.show(cx, cookbook);
                } else {
                       CookbookShareDialog.init(cx, cookbook);
                      com.legent.utils.api.ToastUtils.showLong("您拒绝了一些应用需要的权限，可能导致部分功能不能正常使用哦!");

                }
               CookbookShareDialog.cookbookShareDialog.addOnFcListener(() ->  startFloatingService());

                break;
            default:
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }

    /**
     * 收藏
     */
    private void collect() {
        boolean isLogin = Plat.accountService.isLogon();
        if (isLogin) {
            if (cookbook.collected) {
                ProgressDialogHelper.setRunning(cx, true);
                CookbookManager.getInstance().deleteFavorityCookbooks(cookbook.id, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        ProgressDialogHelper.setRunning(cx, false);
                        ToastUtils.show("取消收藏");
                        cookbook.setIsCollected(false);
//                        imgFavority.set(false);
                        if (firstItemPosition == 0) {
                            imgFavority.setImageResource(R.drawable.icon_collect);
                        } else {
                            imgFavority.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ProgressDialogHelper.setRunning(cx, false);
                        ToastUtils.show(t.getMessage());
                    }
                });
            } else {
                ProgressDialogHelper.setRunning(cx, true);
                CookbookManager.getInstance().addFavorityCookbooks(cookbook.id, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        ProgressDialogHelper.setRunning(cx, false);
                        ToastUtils.show("收藏成功");
                        cookbook.setIsCollected(true);
//                        imgFavority.setChecked(true);
                        if (firstItemPosition == 0) {
                            imgFavority.setImageResource(R.drawable.icon_collected);
                        } else {
                            imgFavority.setImageResource(R.drawable.ic_baseline_favorite_24);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ProgressDialogHelper.setRunning(cx, false);
                        ToastUtils.show(t.getMessage());
                    }
                });
            }

        } else {

            CmccLoginHelper.getInstance().toLogin();
        }
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

//    @Subscribe
//    public void onEvent(PageBack2Event event) {
//        if ("close".equals(event.getPageName())) {
//            fabRecipe.setVisibility(View.GONE);
//        } else if ("min".equals(event.getPageName())) {
//            fabRecipe.setVisibility(View.VISIBLE);
//            GlideApp.with(getContext())
//                    .load(cookbook.imgSmall)
//                    .placeholder(R.mipmap.icon_recipe_default)
//                    .error(R.mipmap.icon_recipe_default)
//                    .priority(Priority.HIGH)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .apply(RequestOptions.bitmapTransform(options))
//                    .into(fabRecipe);
//            this.step = event.getStep();
//        }
//
//    }

    /**
     * 开始工作
     */
    private void work(String guid) {
        //重新获取烹饪步骤
        StoreService.getInstance().getCookBookSteps(ID, "", "", new Callback<List<CookStep>>() {
            @Override
            public void onSuccess(List<CookStep> cookStepsTemp) {
                cookSteps = (ArrayList<CookStep>) cookStepsTemp;
                List<IDevice> list = Plat.deviceService.queryAll();
                DeviceSelectUtils.getInstance().setList(list);//因为有的时候查不到设备的状况故动态查一次
                List<String> dcList = DeviceSelectUtils.getInstance().dcSubString(cookbook.getJs_dcs().get(0).dc);
                List<IDevice> listTemp = DeviceSelectUtils.getInstance().deviceIDev(dcList);
                for (int i = 0; i < listTemp.size(); i++) {
                    if (listTemp.get(i).getGuid().getGuid().equals(guid)) {
                        IDevice iDevice = listTemp.get(i);

                        if (!iDevice.isConnected()) {
                            ToastUtils.show("设备已离线，连接后才可进行自动烹饪");
                        } else {
                            if (cookSteps != null && "".equals(cookSteps.get(0).getDc())) {
                                ToastUtils.show("此菜谱不支持自动烹饪");
                                return;
                            }
                            if (RecipeCookUtils.getInstance().isAlram(iDevice)) {
                                ToastUtils.show(iDevice.getDeviceType() + "报警，恢复后才可烹饪");
                                return;
                            }
                            if (RecipeCookUtils.getInstance().isOpenDoor(iDevice)) {
                                ToastUtils.show(iDevice.getDeviceType() + "门未关，无法开始工作");
                                return;
                            }

                            if (RecipeCookUtils.getInstance().isWaterBoxState(iDevice)) {
                                ToastUtils.show("水箱已弹出，请确保水箱已放好");
                                return;
                            }
                            if (RecipeCookUtils.getInstance().isWhichDevice(iDevice)) {
                                ToastUtils.show(iDevice.getDeviceType() + "被占用，停止后才可自动烹饪");
                                return;
                            }

                            Bundle bd = new Bundle();
                            bd.putSerializable("list", cookSteps);
                            bd.putSerializable("recipe", cookbook);
                            RecipeActivity.start(getActivity(), cookbook, bd, iDevice.getGuid().getGuid());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }
//--------------------------------------燃气灶菜谱逻辑如下---------------------------------------------------

    /**
     * 燃气灶菜谱工作
     */
    private void rqzWork() {
        //燃气灶
        //重新获取烹饪步骤
        StoreService.getInstance().getCookBookSteps(ID, "", "", new Callback<List<CookStep>>() {
            @Override
            public void onSuccess(List<CookStep> cookStepsTemp) {
                cookSteps = (ArrayList<CookStep>) cookStepsTemp;
                if (cookSteps != null && "".equals(cookSteps.get(0).getDc())) {
                    ToastUtils.show("此菜谱不支持自动烹饪");
                    return;
                }
                List<AbsFan> fanList = Utils.getFan();
                List<Stove> stoves = new ArrayList<>();
                for (int i = 0; i < fanList.size(); i++) {
                    if ((Stove) fanList.get(i).getChildStove() != null) {
                        stoves.add((Stove) fanList.get(i).getChildStove());
                    }
                }
                List<Dc> listDc = cookbook.getJs_dcs();
                if (isContainAllDevice(listDc)) {
//                    if (stoves.size() > 1) {
                    //从菜谱模块进来
                    if (mGuid == null || "".equals(mGuid)) {
                        //烟机选择界面
//                            Helper.newDeviceSelectStoveDialog(cx, stoves, new Callback<IDevice>() {
//                                @Override
//                                public void onSuccess(IDevice iDevice) {
//                                    Stove stove = (Stove) iDevice;
//                                    stoveWork(stove);
//                                }
//
//                                @Override
//                                public void onFailure(Throwable t) {
//                                    t.printStackTrace();
//                                }
//                            });
                        deviceDialog2(stoves);
                    } else {
                        //从设备进来
                        for (int i = 0; i < stoves.size(); i++) {
                            if ((stoves.get(i).getGuid().getGuid()).equals(mGuid)) {
                                Stove stove = stoves.get(i);
                                stoveWork(stove);
                            }
                        }
                    }
//                    } else {
//                        Stove stove = stoves.get(0);
//                        stoveWork(stove);
//                    }

                } else {
//                Message msg = Message.obtain();
//                msg.what = 1;
//                hander.sendMessage(msg);
                    deviceDialog2(stoves);
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });


    }

    private void stoveWork(Stove stove) {

        List<CookbookPlatforms> cookbookPlatformsList = cookbook.cookbookPlatformsList;
        List<String> platformCodeList = new ArrayList<>();
        if (cookbookPlatformsList != null) {
            for (int i = 0; i < cookbookPlatformsList.size(); i++) {
                String platformCode = cookbookPlatformsList.get(i).platformCode;
                platformCodeList.add(platformCode);
                LogUtils.i("2020060504", "platformCode:::" + platformCode);
            }
        }

        if (!platformCodeList.contains(stove.getDp())) {
            ToastUtils.show("该设备未适配，请选择其他设备");
            return;
        }
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
                bd.putSerializable("recipe", cookbook);
                if ("4".equals(cookbook.getCookbookType())) {
                    Pot[] pot = DeviceSelectUtils.getInstance().getPot();
                    if (pot == null || pot.length == 0 || pot[0] == null || !pot[0].isConnected()) {
                        RecipeRRQZActivity.start(getActivity(), cookbook, bd, fantemp.getGuid().getGuid());
                    } else {
                        RecipePotActivity.start(getActivity(), cookbook, bd, fantemp.getGuid().getGuid());
                    }
                } else {
                    RecipeRRQZActivity.start(getActivity(), cookbook, bd, fantemp.getGuid().getGuid());
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
                    bd.putSerializable("recipe", cookbook);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            this.cancel();
                        }
                    }, 2000);
                    if ("4".equals(cookbook.getCookbookType())) {
                        Pot[] pot = DeviceSelectUtils.getInstance().getPot();
                        if (pot == null || pot.length == 0 || pot[0] == null || !pot[0].isConnected()) {
                            RecipeRRQZActivity.start(getActivity(), cookbook, bd, fantemp.getGuid().getGuid());
                        } else {
                            RecipePotActivity.start(getActivity(), cookbook, bd, fantemp.getGuid().getGuid());
                        }

                    } else {
                        RecipeRRQZActivity.start(getActivity(), cookbook, bd, fantemp.getGuid().getGuid());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     * 选择烟机左右炉头
     *
     * @param fantemp
     */
    private void selectStove(final AbsFan fantemp) {
        if (fantemp != null) {
            if (fantemp.getChildStove() != null) {
                final Stove stove = fantemp.getChildStove();
                Helper.newDeviceSelectStoveHeadDialog(cx, new Callback<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        LogUtils.i("20180404", "type:" + cookbook.getCookbookType());
                        if (integer == 0) {
                            if ("4".equals(cookbook.getCookbookType())) {
                                if (stove.leftHead.status == 1) {
                                    Bundle bd = new Bundle();
                                    bd.putSerializable("list", cookSteps);
                                    bd.putInt("stoveHeadId", integer);
                                    bd.putSerializable("recipe", cookbook);
                                    new Timer().schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            this.cancel();
                                        }
                                    }, 1000);
                                    Pot[] pot = DeviceSelectUtils.getInstance().getPot();
                                    if (pot == null || pot.length == 0 || pot[0] == null || !pot[0].isConnected()) {
                                        RecipeRRQZActivity.start(getActivity(), cookbook, bd, fantemp.getGuid().getGuid());
                                    } else {
                                        RecipePotActivity.start(getActivity(), cookbook, bd, fantemp.getGuid().getGuid());
                                    }
                                } else if (stove.leftHead.status == 2) {
                                    ToastUtils.show(R.string.recipe_stove_head_is_working);
                                } else {
                                    stoveOnTips(stove, integer, fantemp);
                                }
                            } else {
                                if (stove.leftHead.status == 1) {
                                    Bundle bd = new Bundle();
                                    bd.putSerializable("list", cookSteps);
                                    bd.putInt("stoveHeadId", integer);
                                    bd.putSerializable("recipe", cookbook);
                                    new Timer().schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            this.cancel();
                                        }
                                    }, 1000);
                                    RecipeRRQZActivity.start(getActivity(), cookbook, bd, fantemp.getGuid().getGuid());
                                } else if (stove.leftHead.status == 2) {
                                    ToastUtils.show(R.string.recipe_stove_head_is_working);
                                } else {
                                    stoveOnTips(stove, integer, fantemp);
                                }

                            }


                        } else {
                            if ("4".equals(cookbook.getCookbookType())) {
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
                                    Pot[] pot = DeviceSelectUtils.getInstance().getPot();
                                    if (pot == null || pot.length == 0 || pot[0] == null || !pot[0].isConnected()) {
                                        RecipeRRQZActivity.start(getActivity(), cookbook, bd, fantemp.getGuid().getGuid());
                                    } else {
                                        RecipePotActivity.start(getActivity(), cookbook, bd, fantemp.getGuid().getGuid());
                                    }
                                } else if (stove.rightHead.status == 2) {
                                    ToastUtils.show(R.string.recipe_stove_head_is_working);
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
                                    RecipeRRQZActivity.start(getActivity(), cookbook, bd, fantemp.getGuid().getGuid());
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

    /**
     * 选择设备dialog （灶具）
     */

    private void deviceDialog2(List<Stove> stoves) {
        baseDialog = new BaseDialog(cx);
        baseDialog.setContentView(R.layout.dialog_device);
        baseDialog.setCanceledOnTouchOutside(true);
        baseDialog.setGravity(Gravity.BOTTOM);
        baseDialog.setWidth(getActivity().getWindowManager().getDefaultDisplay().getWidth());
        RecyclerView rvDevice = (RecyclerView) baseDialog.findViewById(R.id.rv_device);
        rvDevice.setLayoutManager(new LinearLayoutManager(cx, LinearLayoutManager.VERTICAL, false));
        RvDeviceAdapter rvDeviceAdapter = new RvDeviceAdapter();
        rvDevice.setAdapter(rvDeviceAdapter);
        //根据菜谱参数筛选支持的设备
        List<CookbookPlatforms> cookbookPlatformsList = cookbook.cookbookPlatformsList;
        List<String> platformCodeList = new ArrayList<>();
        if (cookbookPlatformsList != null) {
            for (CookbookPlatforms cookbookPlatforms : cookbookPlatformsList
            ) {
                String platformCode = cookbookPlatforms.platformCode;
                platformCodeList.add(platformCode);
            }
        }
        List<Stove> stoves2 = DeviceSelectUtils.getInstance().deviceIDevPlatformCode2(stoves, platformCodeList);
        if (stoves2 == null || stoves2.size() == 0) {
            View view = getLayoutInflater().inflate(R.layout.item_detail3_device_empty, new FrameLayout(cx), false);
            btnAddDevice = view.findViewById(R.id.btn_add_device);
            btnNotDevice = view.findViewById(R.id.btn_not_device);
            setOnClickListener(btnAddDevice, btnNotDevice);
            rvDeviceAdapter.setEmptyView(view);
        } else {
            rvDeviceAdapter.addData(stoves2);
            View head = getLayoutInflater().inflate(R.layout.item_detail3_device_header, new FrameLayout(cx), false);
            rvDeviceAdapter.addHeaderView(head);
            btnNotDevice = head.findViewById(R.id.btn_not_device);
            View foot = getLayoutInflater().inflate(R.layout.item_detail3_device_foot, new FrameLayout(cx), false);
            rvDeviceAdapter.addFooterView(foot);
            btnAddDevice = foot.findViewById(R.id.ll_add_device);
            setOnClickListener(btnAddDevice, btnNotDevice);
        }
        baseDialog.show();
        rvDeviceAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (baseDialog != null) {
                    baseDialog.dismiss();
                }
                Stove stove = (Stove) rvDeviceAdapter.getItem(position);
                stoveWork(stove);

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = activity.getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
//            StatusBarUtils.setTextDark(getContext(), true);
//        }
        idTemp.clear();
        EventUtils.postEvent(new PageBackEvent("RecipeDetailPage"));
    }

    @SuppressLint("ShowToast")
    public void startFloatingService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(activity)) {
            Toast.makeText(activity, "当前无权限，请授权", Toast.LENGTH_SHORT);
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
            startActivityForResult(intent, 0);
            return;
        }
//        if (!isStart) {
//            activity.startForegroundService(new Intent(activity, FloatingService.class));
//        }
        showFloatingWindow();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(activity)) {
                Toast.makeText(activity, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "授权成功", Toast.LENGTH_SHORT).show();
//                if (!isStart)
//                startService(new Intent(activity, FloatingService.class));
                showFloatingWindow();
//            }
            }
        }
    }

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    @SuppressLint("InflateParams")
    public void showFloatingWindow() {
        EventUtils.postEvent(new FloatHelperEvent(ID, getArguments(), RecipeUtils.getRecipeImgUrl(cookbook), 4, pageKey));
        UIService.getInstance().popBack();
    }

    boolean isOpen = true ;
    boolean btnVisi = false ;
    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {
        isOpen = true ;
        if (btnAutomatic.getVisibility() == View.VISIBLE){
            btnVisi = true ;
            btnAutomatic.setVisibility(View.INVISIBLE);
        }else {
            btnVisi = false ;
        }
        rvRecipeDetailAdapter.rvMaterialAdapter.setOpen(true);
    }

    @Override
    public void onSoftKeyboardClosed() {
        LogUtils.i("closed" , "-----");
        if (rvRecipeDetailAdapter.rvMaterialAdapter != null && isOpen ){
            rvRecipeDetailAdapter.rvMaterialAdapter.setNum();
            rvRecipeDetailAdapter.rvMaterialAdapter.setOpen(false);

        }
        if (btnVisi){
            btnAutomatic.setVisibility(View.VISIBLE);
        }else {
            btnAutomatic.setVisibility(View.INVISIBLE);
        }
        isOpen = false ;
    }

    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    // 更新悬浮窗控件布局
                    windowManager.updateViewLayout(view, layoutParams);
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    @Override
    protected void setStateBarFixer() {
        if (pageKey == ClassifyTagRecipePage.PAGEKEY) {
            StatusBarUtils.setColor(cx, getResources().getColor(R.color.white));
        } else {
            super.setStateBarFixer();
        }
//        if (StatusBarUtils.getColor(cx) == Color.TRANSPARENT ) {

//        }

    }

    @Override
    protected void setStateBarFixer2() {
        if (pageKey == ClassifyTagRecipePage.PAGEKEY) {
            StatusBarUtils.setColor(cx, getResources().getColor(R.color.white));
        } else {
            super.setStateBarFixer2();
        }

    }

    /**
     * 模拟从专题详情页返回
     *
     * @param event
     */
    @Subscribe
    public void onEvent(PageBackEvent event) {
        if (
                "FloatWindow".equals(event.getPageName())
        ) {
            getCookDetail(ID);
        }
    }

    @Subscribe
    public void onEvent(AppVisibleEvent event) {
        if (!event.isVisible) {
            if (PageKey.RecipeDetail.equals(UIService.getInstance().getTop().getCurrentPage().getPageKey()) && rvRecipeDetailAdapter != null) {
                rvRecipeDetailAdapter.setStopVideo();

            }
        }
    }
}
