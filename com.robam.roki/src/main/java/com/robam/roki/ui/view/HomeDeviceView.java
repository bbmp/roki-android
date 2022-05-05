package com.robam.roki.ui.view;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dsk.duiwidget.ContentWidget;
import com.aispeech.dui.dsk.duiwidget.DuiWidget;
import com.aispeech.dui.dsk.duiwidget.ListWidget;
import com.bumptech.glide.Glide;
import com.google.common.eventbus.Subscribe;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.events.ClickRecipeEvent;
import com.legent.plat.events.DeviceConnectedNoticEvent;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.events.DeviceLoadCompletedEvent;
import com.legent.plat.events.DeviceSelectedEvent;
import com.legent.plat.events.UserLoginEvent;
import com.legent.plat.events.UserLogoutEvent;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.services.DeviceService;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.ui.ext.views.TitleBar;
import com.legent.utils.EventUtils;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.NetworkUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.qrcode.ScanQrActivity;
import com.robam.common.events.CallResultApiEvent;
import com.robam.common.events.DeviceDeleteEvent;
import com.robam.common.events.DeviceEasylinkCompletedEvent;
import com.robam.common.events.NewBieGuideEvent;
import com.robam.common.events.QueryResultApiEvent;
import com.robam.common.events.ReturnDeviceViewEvent;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.pojos.CookingKnowledge;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.common.pojos.device.Sterilizer.AbsSterilizer;
import com.robam.common.pojos.device.Sterilizer.Steri826;
import com.robam.common.pojos.device.Sterilizer.Steri829;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.WaterPurifier.AbsWaterPurifier;
import com.robam.common.pojos.device.cook.AbsCooker;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.FanStatus;
import com.robam.common.pojos.device.gassensor.GasSensor;
import com.robam.common.pojos.device.hidkit.AbsHidKit;
import com.robam.common.pojos.device.integratedStove.AbsIntegratedStove;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.microwave.MicroWaveStatus;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaStatus;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.common.services.CookbookManager;
import com.robam.common.services.StoreService;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.model.bean.DuiChangeBean;
import com.robam.roki.model.bean.DuiLevelBean;
import com.robam.roki.model.bean.DuiLightBean;
import com.robam.roki.model.bean.DuiMVSetBean;
import com.robam.roki.model.bean.DuiMWPowerBean;
import com.robam.roki.model.bean.DuiOvenPowerBean;
import com.robam.roki.model.bean.DuiOvenSetBean;
import com.robam.roki.model.bean.DuiPowerBean;
import com.robam.roki.model.bean.DuiRecipeCount;
import com.robam.roki.model.bean.DuiRecipeSearchBean;
import com.robam.roki.model.bean.DuiSteamBakePowerBean;
import com.robam.roki.model.bean.DuiSteamBakeSetBean;
import com.robam.roki.model.bean.DuiSteamOvenBean;
import com.robam.roki.model.bean.DuiSteamTempTimeSetBean;
import com.robam.roki.model.bean.DuiSterilizerPowerBean;
import com.robam.roki.model.bean.DuiSterilizerSetBean;
import com.robam.roki.observer.DuiCommandApiObserver;
import com.robam.roki.observer.DuiNativeApiObserver;
import com.robam.roki.service.AppService;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.UIListeners;
import com.robam.roki.ui.adapter.IntellkitchenAdapter;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.helper3.DialogHelper;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;
import com.robam.roki.ui.view.networkoptimization.BleConnectActivity;
import com.robam.roki.ui.view.networkoptimization.NetWorkStateUtils;
import com.robam.roki.utils.LoginUtil;
import com.robam.roki.utils.PermissionsUtils;
import com.robam.roki.utils.ToolUtils;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 智能页面
 */
public class HomeDeviceView extends FrameLayout implements UIListeners.IRefresh {

    //标题栏
    @InjectView(R.id.pull_refresh_scrollview)
    SwipeRefreshLayout pullRefreshScrollview;
    @InjectView(R.id.ll_title)
    LinearLayout mLlTitle;
    @InjectView(R.id.gif)
    ImageView mGif;
    @InjectView(R.id.titleView)
    TitleBar mTitleView;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @InjectView(R.id.tv_kitchen_knowledge)
    TextView mTvKitchenKnowledge;
    @InjectView(R.id.tv_device_intellectual_products)
    TextView mTvDeviceIntellectualProducts;
    @InjectView(R.id.iv_add)
    ImageView mIvAdd;
    @InjectView(R.id.rel_homedevice)
    RelativeLayout rel_homedevice;
    @InjectView(R.id.deviceContainer)
    GridLayout deviceContainer;
    @InjectView(R.id.ll_empty)
    LinearLayout llEmpty;
    FragmentActivity activity ;

    List<IDevice> IdeviceList = new ArrayList<IDevice>();
    List<IDevice> IdeviceList1 = new ArrayList<IDevice>();
    List<IDevice> finaldeviceList = new ArrayList<IDevice>();
    IntellkitchenAdapter mIntellkitchenAdapter;
    List<CookingKnowledge> mCookingKnowledges;
    int fanNumber = 0;

    int otherDeviceNumber = 0;
    private boolean mLogon;
//    private FirebaseAnalytics firebaseAnalytics;
    private UIRefreshBroadcastReceiver receiver;
    //定义界面刷新常量
    protected static final String ACTION = "com.robam.roki.senduirefreshcommand";


    private DeviceNewItemView deviceItemView;
    private AbsFan absFan;
    private AbsSteamoven absSteamoven, absSteamoven1;
    private AbsOven absOven1, absOven2;
    private AbsMicroWave absMicroWave1;
    private int totalTime;
    private AbsMicroWave absMicroWave;
    private AbsSteameOvenOne absSteameOvenOne;
    private AbsRika absRika;


    List<IDevice> fanList = new LinkedList<>();


    @Subscribe
    public void onEvent(CallResultApiEvent apiEvent) {
        actionListener(apiEvent.getCommandApi(), apiEvent.getData());
    }


    @Subscribe
    public void onEvent(QueryResultApiEvent queryResultApiEvent) {
        actionListener(queryResultApiEvent.getNativeApi(), queryResultApiEvent.getData());
    }

    public HomeDeviceView(Context context) {
        super(context);
        init(context, null);

    }
    public HomeDeviceView(Context context , FragmentActivity activity) {
        super(context);
        this.activity = activity ;
        init(context, null);
        refreshWhenPull();
    }
    public HomeDeviceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public HomeDeviceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventUtils.regist(this);
        receiver = new UIRefreshBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        //动态注册UIRefreshBroadcastReceiver
        getContext().registerReceiver(receiver, filter);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventUtils.unregist(this);
        getContext().unregisterReceiver(receiver);
    }

    @Subscribe
    public void onEvent(UserLoginEvent event) {
        refreshWhenPull();
        initData();
    }

    @Subscribe
    public void onEvent(UserLogoutEvent event) {
//        refreshWhenPull();
        initData();
        mGif.setVisibility(VISIBLE);
    }



    @Subscribe
    public void onEvent(DeviceEasylinkCompletedEvent event) {
        Log.e(TAG,"onEventDeviceEasylinkCompletedEvent");
        AppService.getInstance().init(Plat.app);
        onRefresh();
    }

    @Subscribe
    public void onEvent(DeviceConnectedNoticEvent event) {
        Log.e(TAG,"onEventDeviceConnectedNoticEvent");
        AppService.getInstance().onLogin();
        onRefresh();
    }


    @Subscribe
    public void onEvent(DeviceSelectedEvent event) {
        Log.e(TAG,"onEventDeviceSelectedEvent");
        onRefresh();
    }

    private static final String TAG = "HomeDeviceView";
    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        onRefresh();
//        Log.e(TAG,"DeviceConnectionChangedEvent"+event.device.getGuid());
//        for (int i = 0; i < deviceContainer.getChildCount(); i++) {
//            DeviceNewItemView mDeviceNewItemView = (DeviceNewItemView) deviceContainer.getChildAt(i);
//
//            if (mDeviceNewItemView.device.getGuid().equals(event.device.getGuid())){
//                if (!event.isConnected) {
//                    mDeviceNewItemView.setImageDeviceOfflinePrompt(R.drawable.bg_dish_washer_show);
//                    if (mDeviceNewItemView.getSelect()){
//                        mDeviceNewItemView.setSelected(false);
//                    }
//                }else{
//                    mDeviceNewItemView.setImageDeviceOfflinePromptNull();
//                }
//                break;
//            }
//        }
    }

    @Subscribe
    public void onEvent(NewBieGuideEvent event) {
        Log.e(TAG,"onEventNewBieGuideEvent");
        mIvAdd.setVisibility(VISIBLE);
    }

    @Subscribe
    public void onEvent(DeviceDeleteEvent event) {
        Log.e(TAG,"onEventDeviceDeleteEvent");
        AppService.getInstance().onLogin();
        onRefresh();
    }

 /*   @Subscribe
    public void onEvent(DeviceStatusChangedEvent event) {
        onRefresh();
    }*/




    @Subscribe
    public void onEvent(DeviceLoadCompletedEvent event) {
        Log.e(TAG,"onEventDeviceLoadCompletedEvent");
        onRefresh();
    }

    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_home_device, this, true);
        if (!view.isInEditMode()) {
            ScreenAdapterTools.getInstance().loadView(view);
            ButterKnife.inject(this, view);
            initData();
            mGif.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(MainActivity.activity)
                    .load(R.drawable.bg_device)
//                    .crossFade()
                    .into(mGif);
            setInitView();
//            pullRefreshScrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
//            pullRefreshScrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
//                @Override
//                public void onRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
//                    refreshWhenPull();
//                    initData();
//                }
//            });
            pullRefreshScrollview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshWhenPull();
                    initData();
                }
            });
        }
        mLogon = Plat.accountService.isLogon();

        mIvAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                boolean connect = PreferenceUtils.getBool("connect", false);
//                if (!connect) {
//                    ToastUtils.showShort(R.string.device_mqtt_not_connected);
//                    return;
//                }
                boolean wifi = NetWorkStateUtils.isWifi(getContext());
                boolean isLog = Plat.accountService.isLogon();

                if(!isLog){
                    startLogin();
                    return;
                }
                if (isLog && wifi) {
                    UIService.getInstance().postPage(PageKey.DeviceAdd);
                } else if (isLog && !wifi) {
                    UIService.getInstance().postPage(PageKey.SettingWifi);
                } else if (!isLog && wifi) {
//                    LoginUtil.checkWhetherLogin(getContext(), PageKey.UserLogin);
                    LoginUtil.checkWhetherLogin2(getContext(), activity);
                } else if (!isLog && !wifi) {
//                    LoginUtil.checkWhetherLogin(getContext(), PageKey.UserLogin);
                    LoginUtil.checkWhetherLogin2(getContext(), activity);
                }
            }
        });


//        firebaseAnalytics = MobApp.getmFirebaseAnalytics();
//        firebaseAnalytics.setCurrentScreen((Activity) cx, "设备管理主页", null);
    }
    /**
     * 登录界面
     */
    private void startLogin() {
        if (CmccLoginHelper.getInstance().isGetPhone) {
            CmccLoginHelper.getInstance().loginAuth();
        } else {
            CmccLoginHelper.getInstance().login();
        }
    }
    private void initData() {
        if (!NetworkUtils.isConnect(getContext())){
            llEmpty.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!NetworkUtils.isConnect(getContext())){
                        // 警告对话框
                        ProgressDialogHelper.setRunning(getContext(), true);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ProgressDialogHelper.setRunning(getContext(), false);
                            }
                        } , 1500);
                    }else {
                        onRefresh();
                    }

                }
            });
            llEmpty.setVisibility(VISIBLE);
            DialogHelper.notNetDialog(getContext());
        }
        StoreService.getInstance().getCookingKnowledge("cookingSkill", 1, null, 0, 3, new Callback<List<CookingKnowledge>>() {
            @Override
            public void onSuccess(List<CookingKnowledge> cookingKnowledges) {
                mCookingKnowledges = cookingKnowledges;
                if (mIntellkitchenAdapter != null) {
                    mIntellkitchenAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void setInitView() {
        if (Plat.accountService.isLogon() && DeviceService.getInstance().queryAll().size() > 0) {
            mGif.setVisibility(GONE);
            mTvKitchenKnowledge.setVisibility(VISIBLE);
            mTvKitchenKnowledge.setVisibility(VISIBLE);
            mIvAdd.setVisibility(GONE);
            setTitleBar();
            onRefresh();

        } else {
            setTitleBar();
            mGif.setVisibility(VISIBLE);
            mTvKitchenKnowledge.setVisibility(GONE);
            mTvKitchenKnowledge.setVisibility(GONE);
            String newBie = PreferenceUtils.getString("newBie", null);
            if (newBie != null) {
                mIvAdd.setVisibility(VISIBLE);
            }
        }
    }


    void setTitleBar() {
        mTitleView.setTitle(R.string.home_tab_device_kitchen);
        ImageView icon_deviceadd = TitleBar.newTitleIconView(getContext(), R.mipmap.ic_device_add, new OnClickListener() {
            @Override
            public void onClick(View v) {
//                boolean connect = PreferenceUtils.getBool("connect", false);
//                if (!connect) {
//                    ToastUtils.showShort(R.string.device_mqtt_not_connected);
//                    return;
//                }
                boolean wifi = NetWorkStateUtils.isWifi(getContext());
                boolean isLog = Plat.accountService.isLogon();
                if (!isLog){
                    startLogin();
                    return;
                }
                if (isLog && wifi) {
//                    Intent intent = new Intent(activity, BleConnectActivity.class);
//                    activity.startActivity(intent);
                    UIService.getInstance().postPage(PageKey.DeviceAdd);
                } else if (isLog && !wifi) {
                    UIService.getInstance().postPage(PageKey.SettingWifi);
                } else if (!isLog && wifi) {
//                    LoginUtil.checkWhetherLogin(getContext(), PageKey.UserLogin);
                    LoginUtil.checkWhetherLogin2(getContext(), activity);
                } else if (!isLog && !wifi) {
//                    LoginUtil.checkWhetherLogin(getContext(), PageKey.UserLogin);
                    LoginUtil.checkWhetherLogin2(getContext(), activity);
                }
            }
        });
        mTitleView.replaceRight(icon_deviceadd);
        ImageView icon_scan = TitleBar.newTitleIconView(getContext(), R.mipmap.ic_device_scan, new OnClickListener() {
            private final static int SCANNIN_GREQUEST_CODE = 100;

            @Override
            public void onClick(View view) {
//                if (LoginUtil.checkWhetherLogin(getContext(), PageKey.UserLogin)) {

                boolean isLog = Plat.accountService.isLogon();
                if (!isLog){
                    startLogin();
                    return;
                }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        int selfPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
                        if (selfPermission == 0) {
                            Activity atv = UIService.getInstance().getTop().getActivity();
                            Intent intent = new Intent();
                            intent.setClass(atv, ScanQrActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            atv.startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                        } else {
                            PermissionsUtils.checkPermission(getContext(), Manifest.permission.CAMERA, PermissionsUtils.CODE_HOME_DEVICE_CAMERA);
                        }
                    } else {
                        Activity atv = UIService.getInstance().getTop().getActivity();
                        Intent intent = new Intent();
                        intent.setClass(atv, ScanQrActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        atv.startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                    }
            }
        });
        mTitleView.replaceLeft(icon_scan);
    }


    void refreshWhenPull() {
        AppService.getInstance().onLogin();
//        pullRefreshScrollview.onRefreshComplete();
    }


    private void refreshHomeBackground() {
        if (DeviceService.getInstance().queryAll().size() > 0) {
            mGif.setVisibility(GONE);
            mTvKitchenKnowledge.setVisibility(VISIBLE);
            mTvDeviceIntellectualProducts.setVisibility(VISIBLE);
            rel_homedevice.setBackgroundResource(R.mipmap.img_kit_bg);
            mIvAdd.setVisibility(GONE);
        } else {
            mGif.setVisibility(VISIBLE);
            mTvKitchenKnowledge.setVisibility(GONE);
            mTvDeviceIntellectualProducts.setVisibility(GONE);
            rel_homedevice.setBackground(null);
            mIvAdd.setVisibility(VISIBLE);
        }
    }

    public void refreshDeviceView() {
        pullRefreshScrollview.setVisibility(VISIBLE);
        deviceContainer.removeAllViews();
        mRecyclerView.setVisibility(VISIBLE);
        fanNumber = 0;
        otherDeviceNumber = 0;
        if (IdeviceList1.size() > IdeviceList.size() || IdeviceList1.size() < IdeviceList.size()) {
            IdeviceList = IdeviceList1;
        }
        if (IdeviceList1 != null && IdeviceList1.size() > 0) {
            IdeviceList1.clear();
        }
        for (int i = 0; i < DeviceService.getInstance().queryAll().size(); i++) {
            if (DeviceService.getInstance().queryAll().get(i) instanceof AbsFan) {
                IdeviceList1.add(fanNumber, DeviceService.getInstance().queryAll().get(i));
                fanNumber++;
            }
        }
        otherDeviceNumber = IdeviceList1.size();
        for (int i = 0; i < DeviceService.getInstance().queryAll().size(); i++) {
            if (!(DeviceService.getInstance().queryAll().get(i) instanceof AbsFan)) {
                IdeviceList1.add(otherDeviceNumber, DeviceService.getInstance().queryAll().get(i));
                otherDeviceNumber++;
            }
        }


        if (finaldeviceList != null && finaldeviceList.size() > 0) {
            finaldeviceList.clear();
        }

        //上面已经将烟机保证排在前几位其他设备排在后面，下面是将灶具包含到集合里面

        for (int i = 0; i < IdeviceList.size(); i++) {

            if (IdeviceList.get(i) instanceof AbsFan) {
                finaldeviceList.add(IdeviceList.get(i));
                int size = ((AbsFan) IdeviceList.get(i)).getChildList().size();
                if (size > 0) {
                    List<IDevice> childList = ((AbsFan) IdeviceList.get(i)).getChildList();
                    for (int j = 0; j < childList.size(); j++) {
                        if (childList.get(j).getDc().equals(IDeviceType.RRQZ) || childList.get(j).getDc().equals(IDeviceType.RDCZ)) {
                            finaldeviceList.add(childList.get(j));
                        }
                    }
                    for (int j = 0; j < childList.size(); j++) {
                        if (childList.get(j).getDc().equals(IDeviceType.RZNG)) {
                            finaldeviceList.add(childList.get(j));
                        }
                    }

                    for (int j = 0; j < childList.size(); j++) {
                        if (childList.get(j).getDc().equals(IDeviceType.RQCG)) {
                            finaldeviceList.add(childList.get(j));
                        }
                    }

                }
            } else {
                finaldeviceList.add(IdeviceList.get(i));
            }
        }

        for (int i = 0; i < finaldeviceList.size(); i++) {
            deviceItemView = new DeviceNewItemView(getContext());

            DeviceNewItemView deviceItemView = new DeviceNewItemView(getContext());
            if (finaldeviceList.get(i) instanceof AbsFan) {
                deviceItemView.setTxtDeviceDesc(finaldeviceList.get(i).getCgEngName());
                deviceItemView.setTxtDeviceName(finaldeviceList.get(i).getCategoryName());
                deviceItemView.setTxtModel(((AbsFan) finaldeviceList.get(i)).getDispalyType());
                deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());
                if (!finaldeviceList.get(i).isConnected()) {
                    deviceItemView.setImageDeviceOfflinePrompt(R.mipmap.img_offline_prompt);
                }
            } else if (finaldeviceList.get(i) instanceof Stove) {
                deviceItemView.setTxtDeviceDesc(finaldeviceList.get(i).getCgEngName());
                deviceItemView.setTxtDeviceName(finaldeviceList.get(i).getCategoryName());
                deviceItemView.setTxtModel(((Stove) finaldeviceList.get(i)).getDispalyType());
                if (!finaldeviceList.get(i).isConnected()) {
                    deviceItemView.setImageDeviceOfflinePrompt(R.mipmap.img_offline_prompt);
                }
                deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());
            } else if (finaldeviceList.get(i) instanceof Pot) {
                deviceItemView.setTxtDeviceName(finaldeviceList.get(i).getCategoryName());
                deviceItemView.setTxtDeviceDesc(finaldeviceList.get(i).getCgEngName());
                if (!finaldeviceList.get(i).isConnected()) {
                    deviceItemView.setImageDeviceOfflinePrompt(R.mipmap.img_offline_prompt);
                }
//                LogUtils.i("20170330", "Robic:" + finaldeviceList.get(i).getDt());
                deviceItemView.setTxtModel(finaldeviceList.get(i).getDispalyType());
                deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());


            } else if (finaldeviceList.get(i) instanceof GasSensor) {

                deviceItemView.setTxtDeviceName(finaldeviceList.get(i).getCategoryName());
                deviceItemView.setTxtDeviceDesc(finaldeviceList.get(i).getCgEngName());
                if (!finaldeviceList.get(i).isConnected()) {
                    deviceItemView.setImageDeviceOfflinePrompt(R.mipmap.img_offline_prompt);
                } else {
                    if (((GasSensor) finaldeviceList.get(i)).status == 1) {
                        deviceItemView.setImgDeviceAlarm(R.mipmap.ic_gas_list_alarm);
                    } else if (((GasSensor) finaldeviceList.get(i)).status == 3) {
                        deviceItemView.setImgDeviceAlarm(R.mipmap.ic_gas_list_guzhang);
                    }
                }
                LogUtils.i("20180605", "ff_status:" + ((GasSensor) finaldeviceList.get(i)).status);
//                LogUtils.i("20170330", "Robic:" + finaldeviceList.get(i).getDt());
                deviceItemView.setTxtModel(finaldeviceList.get(i).getDispalyType());
                deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());
            } else {
                deviceItemView.setTxtDeviceName(finaldeviceList.get(i).getCategoryName());
                deviceItemView.setTxtModel(finaldeviceList.get(i).getDispalyType());
                LogUtils.i("20180330", "DispalyType:" + finaldeviceList.get(i).getDispalyType());
                if (finaldeviceList.get(i) instanceof AbsOven) {//烤箱
                    deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());
                    deviceItemView.setTxtDeviceDesc(finaldeviceList.get(i).getCgEngName());
                    if (!finaldeviceList.get(i).isConnected()) {
                        deviceItemView.setImageDeviceOfflinePrompt(R.mipmap.img_offline_prompt);
                    }
                } else if (finaldeviceList.get(i) instanceof AbsSteamoven) {//蒸汽炉
                    deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());
                    deviceItemView.setTxtDeviceDesc(finaldeviceList.get(i).getCgEngName());
                    if (!finaldeviceList.get(i).isConnected()) {
                        deviceItemView.setImageDeviceOfflinePrompt(R.mipmap.img_offline_prompt);
                    }
                } else if (finaldeviceList.get(i) instanceof AbsSterilizer) {//消毒柜
                    deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());
                    deviceItemView.setTxtDeviceDesc(finaldeviceList.get(i).getCgEngName());
                    if (!finaldeviceList.get(i).isConnected()) {
                        deviceItemView.setImageDeviceOfflinePrompt(R.mipmap.img_offline_prompt);
                    }
                } else if (finaldeviceList.get(i) instanceof AbsMicroWave) {//微波炉
                    deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());
                    deviceItemView.setTxtDeviceDesc(finaldeviceList.get(i).getCgEngName());
                    if (!finaldeviceList.get(i).isConnected()) {
                        deviceItemView.setImageDeviceOfflinePrompt(R.mipmap.img_offline_prompt);
                    }
                } else if (finaldeviceList.get(i) instanceof AbsWaterPurifier) {
                    deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());
                    deviceItemView.setTxtDeviceDesc(finaldeviceList.get(i).getCgEngName());
                    if (!finaldeviceList.get(i).isConnected()) {
                        deviceItemView.setImageDeviceOfflinePrompt(R.mipmap.img_offline_prompt);
                    }
                } else if (finaldeviceList.get(i) instanceof AbsSteameOvenOne) {//烤蒸一体机
                    deviceItemView.setTxtDeviceDesc(finaldeviceList.get(i).getCgEngName());
                    deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());
                    if (!finaldeviceList.get(i).isConnected()) {
                        deviceItemView.setImageDeviceOfflinePrompt(R.mipmap.img_offline_prompt);
                    }
                } else if (finaldeviceList.get(i) instanceof AbsRika) {
                    deviceItemView.setTxtDeviceDesc(finaldeviceList.get(i).getCgEngName());
                    deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());
                    if (!finaldeviceList.get(i).isConnected()) {
                        deviceItemView.setImageDeviceOfflinePrompt(R.mipmap.img_offline_prompt);
                    }
                }
                else if (finaldeviceList.get(i) instanceof AbsIntegratedStove) {
                    deviceItemView.setTxtDeviceDesc(finaldeviceList.get(i).getCgEngName());
                    deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());
                    if (!finaldeviceList.get(i).isConnected()) {
                        deviceItemView.setImageDeviceOfflinePrompt(R.mipmap.img_offline_prompt);
                    }
                } else if (finaldeviceList.get(i) instanceof AbsCooker) {
                    deviceItemView.setTxtDeviceDesc(finaldeviceList.get(i).getCgEngName());
                    deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());
                    if (!finaldeviceList.get(i).isConnected()) {
                        deviceItemView.setImageDeviceOfflinePrompt(R.mipmap.img_offline_prompt);
                    }
                }else if (finaldeviceList.get(i) instanceof AbsDishWasher){//洗碗机
                    deviceItemView.setTxtDeviceDesc(finaldeviceList.get(i).getCgEngName());
                    deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());
                    if (!finaldeviceList.get(i).isConnected()) {
                        deviceItemView.setImageDeviceOfflinePrompt(R.mipmap.img_offline_prompt);
                    }
                }else if (finaldeviceList.get(i) instanceof AbsHidKit) {
                    deviceItemView.setTxtDeviceDesc(finaldeviceList.get(i).getCgEngName());
                    deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());
                    if (!finaldeviceList.get(i).isConnected()) {
                        deviceItemView.setImageDeviceOfflinePrompt(R.mipmap.img_offline_prompt);
                    }
                }
            }
            GridLayout.LayoutParams gl = new GridLayout.LayoutParams();
            if (i % 2 != 0) {
                gl.leftMargin = (int) getRawSize(TypedValue.COMPLEX_UNIT_DIP, 4);
            }
            if (i != 0 && i != 1) {
                gl.topMargin = (int) getRawSize(TypedValue.COMPLEX_UNIT_DIP, 4);
            }
            gl.setGravity(Gravity.CENTER);
            deviceItemView.setLayoutParams(gl);
            deviceItemView.setDevice(finaldeviceList.get(i));
            deviceContainer.addView(deviceItemView);
            if (mCookingKnowledges != null && mCookingKnowledges.size() > 0) {
                mIntellkitchenAdapter = new IntellkitchenAdapter(getContext(), mCookingKnowledges
                        , new OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view) {
                        CookingKnowledge tag = (CookingKnowledge) view.getTag();
                        startPage(tag);
                    }
                });
                mRecyclerView.setAdapter(mIntellkitchenAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }

        }
    }


    private void actionListener(final String commandApi, String data) {
        Log.d("20190912", "commandApi：" + commandApi + "---data:" + data);
        try {

            switch (commandApi) {
                //打开烟机
                case DuiCommandApiObserver.ROKI_FAN_POWER:
                    openAbsFan(data);

                    break;
                //档位设置
                case DuiCommandApiObserver.ROKI_FAN_LEVEL:
                    AbsFanSetLevel(data);

                    break;
                //档位调节
                case DuiCommandApiObserver.ROKI_FAN_LEVEL_CHANGE:
                    if (AbsFanLevelChange(data)) return;
                    break;
                //灯光调节
                case DuiCommandApiObserver.ROKI_FAN_LIGHT:
                    AbsFanLight(data);
                    break;
                //菜谱搜索
                case DuiCommandApiObserver.ROKI_RECIPE_SEARCH:
                    DuiRecipeSearchBean duiRecipeSearchBean = JsonUtils.json2Pojo(data, DuiRecipeSearchBean.class);
                    String selectNo4 = duiRecipeSearchBean.getSelectNo();
                    EventUtils.postEvent(new ClickRecipeEvent(selectNo4));
                    break;
                //查询烟机状态
                case DuiNativeApiObserver.ROKI_FAN_STATE:
                    UIService.getInstance().returnHome();
                    EventUtils.postEvent(new ReturnDeviceViewEvent(true));
                    searchAbsFanState(commandApi);
                    break;
                //菜谱
                case DuiNativeApiObserver.ROKI_RECIPE_SEARCH_COUNT:
                    UIService.getInstance().returnHome();
                    EventUtils.postEvent(new ReturnDeviceViewEvent(true));
                    getRecipeSerachCount(commandApi, data);
                    break;
                //蒸箱查询
                case DuiNativeApiObserver.ROKI_STEAM_STATE:
                    UIService.getInstance().returnHome();
                    EventUtils.postEvent(new ReturnDeviceViewEvent(true));
                    getSteamState(commandApi);
                    break;
                //开关蒸箱
                case DuiCommandApiObserver.ROKI_STEAM_POWER:
                    openAbsSteam(commandApi, data);
                    break;
                //蒸箱温度时间设置
                case DuiCommandApiObserver.ROKI_STEAM_SET:
                    AbsSteamSet(data);

                    break;
                //开关烤箱
                case DuiCommandApiObserver.ROKI_OVEN_POWER:
                    openAbsOven(commandApi, data);

                    break;
                //获取烤箱状态
                case DuiNativeApiObserver.ROKI_OVEN_STATE:
                    UIService.getInstance().returnHome();
                    EventUtils.postEvent(new ReturnDeviceViewEvent(true));

                    getOvenState(commandApi);
                    break;

                //烤箱专业模式
                case DuiCommandApiObserver.ROKI_OVEN_SET:
                    AbsOvenSet(commandApi, data);

                    break;
                //微波炉开关
                case DuiCommandApiObserver.ROKI_MW_POWER:
                    if (AbsMicroWavePower(commandApi, data)) return;

                    break;
                //微波炉设置
                case DuiCommandApiObserver.ROKI_MW_SET:
                    AbsMWSet(commandApi, data);

                    break;
                //微波炉状态
                case DuiNativeApiObserver.ROKI_MW_STATE:
                    UIService.getInstance().returnHome();
                    EventUtils.postEvent(new ReturnDeviceViewEvent(true));
                    AbsMWState(commandApi, data);

                    break;
                //消毒柜状态
                case DuiNativeApiObserver.ROKI_STERILIZER_STATE:
                    UIService.getInstance().returnHome();
                    EventUtils.postEvent(new ReturnDeviceViewEvent(true));
                    AbsSterilizerState(commandApi, data);
                    break;
                //消毒柜开关
                case DuiCommandApiObserver.ROKI_STERILIZER_POWER:
                    AbsSterilizerPower(commandApi, data);
                    break;
                //消毒柜专业模式设置
                case DuiCommandApiObserver.ROKI_STERILIZER_SET:
                    if (AbsSterilizerSet(commandApi, data)) return;
                    break;

                //蒸烤一体机
                case DuiNativeApiObserver.ROKI_STEAM_BAKE_STATE:
                    UIService.getInstance().returnHome();
                    EventUtils.postEvent(new ReturnDeviceViewEvent(true));
                    AbsSteamBakeState(commandApi);
                    break;
                //蒸烤一体机开关
                case DuiCommandApiObserver.ROKI_STEAM_BAKE_POWER:
                    AbsSteamBakePower(commandApi, data);
                    break;
                //蒸烤一体机专业模式设置
                case DuiCommandApiObserver.ROKI_STEAM_BAKE_SET:
                    if (AbsSteamBakeSet(commandApi, data)) return;
                    break;

                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.i("20190803", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i("20190803", e.getMessage());
        }
    }

    private void AbsSteamBakeState(String commandApi) {
        ListWidget searchNums = new ListWidget();
        for (int i = 0; i < finaldeviceList.size(); i++) {
            if (finaldeviceList.get(i) instanceof AbsSteameOvenOne) {
                absSteameOvenOne = (AbsSteameOvenOne) finaldeviceList.get(i);
                boolean connected = absSteameOvenOne.isConnected();
                short powerStatus = absSteameOvenOne.powerStatus;
                short powerOnStatus = absSteameOvenOne.powerOnStatus;
                String dt = absSteameOvenOne.getDt();
                ContentWidget widget = new ContentWidget();
                widget.addExtra("online", connected + "");
                widget.addExtra("state", powerStatus);
                widget.addExtra("onState", powerOnStatus);
                widget.addExtra("type", dt);
                searchNums.addContentWidget(widget);
            }

        }
        DDS.getInstance().getAgent().feedbackNativeApiResult(commandApi, searchNums);
    }

    private boolean AbsSteamBakeSet(String commandApi, String data) throws Exception {
        LogUtils.i("20190831123", "ROKI_STEAM_BAKE_SET commandApi：" + commandApi + "---data:" + data);
        DuiSteamBakeSetBean duiSteamBakeSetBean = JsonUtils.json2Pojo(data, DuiSteamBakeSetBean.class);
        final String temp = duiSteamBakeSetBean.getTemp();
        final String mode = duiSteamBakeSetBean.getMode();
        final String bottomTemp = duiSteamBakeSetBean.getBottomTemp();

        LogUtils.i("20191015111111","mode::::"+mode);
        String min = "";
        if (duiSteamBakeSetBean.getTime() == null) {
            min = "0";
        } else {
            String time = duiSteamBakeSetBean.getTime();
            if (!"".equals(time)) {
                //time : 00:10:00

                String[] split = time.split(":");
                min = split[1];
//                min = time.substring(3, 5);//分
            } else {
                min = "0";
            }
        }

//    LogUtils.i("");
        String selectNoSteamBake;
        if (duiSteamBakeSetBean.getSelectNo() == null) {
            selectNoSteamBake = "1";
        } else {
            if (duiSteamBakeSetBean.getSelectNo().equals("")) {
                selectNoSteamBake = "1";
            } else {
                selectNoSteamBake = duiSteamBakeSetBean.getSelectNo();
            }
        }

        if (fanList != null) {
            fanList.clear();
        }
        for (int i = 0; i < finaldeviceList.size(); i++) {
            if (finaldeviceList.get(i) instanceof AbsSteameOvenOne) {
                AbsSteameOvenOne absSteameOvenOne = (AbsSteameOvenOne) finaldeviceList.get(i);
                fanList.add(absSteameOvenOne);
            }
        }

        if (Integer.valueOf(selectNoSteamBake) > fanList.size()) {
            return true;
        }

        final AbsSteameOvenOne absSteameOvenOne = (AbsSteameOvenOne) fanList.get(Integer.valueOf(selectNoSteamBake) - 1);


        if (deviceItemView != null) {
            deviceItemView.setDevice(absSteameOvenOne);
            deviceItemView.layout();
            LogUtils.i("20191014", "deviceItemView is not null::   " + deviceItemView);
        }
        LogUtils.i("20191014", "deviceItemView is  null::   ");

        if (absSteameOvenOne.powerStatus == SteamOvenOnePowerStatus.Off) {
            LogUtils.i("201910121111", "off" + ":::::    " + absSteameOvenOne.powerStatus);
            final String finalMin = min;
            absSteameOvenOne.setSteameOvenStatus_on(new VoidCallback() {
                @Override
                public void onSuccess() {
                    LogUtils.i("201910121111", "onSuccess");

                    if ("10".equals(mode)) {
                        sendProSteamOvenMode(mode,Integer.valueOf(temp),Integer.valueOf(bottomTemp),Integer.valueOf(finalMin));
                    }else{
                        setSteamOvenOneMode(temp, mode, finalMin, absSteameOvenOne);
                    }

                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("201910121111", t.getMessage());
                }
            });
        } else {
            LogUtils.i("201910121111", "oN" + ":::::    " + absSteameOvenOne.powerOnStatus);
            if ("10".equals(mode)) {
                sendProSteamOvenMode(mode,Integer.valueOf(temp),Integer.valueOf(bottomTemp),Integer.valueOf(min));
            }else{
                setSteamOvenOneMode(temp, mode, min, absSteameOvenOne);
            }

        }

        return false;
    }

    private void setSteamOvenOneMode(String temp, final String mode, String min, AbsSteameOvenOne absSteameOvenOne) {
        absSteameOvenOne.setSteameOvenOneRunMode(Short.parseShort(mode), Short.parseShort(min), Short.parseShort(temp), (short) 0, new VoidCallback() {
            @Override
            public void onSuccess() {
                LogUtils.i("201910121111", "成功：mode:" + mode);
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("201910121111", t.getMessage());

            }
        });
    }

    private void AbsSteamBakePower(String commandApi, String data) throws Exception {
        LogUtils.i("20190831123", "ROKI_STEAM_BAKE_POWER commandApi：" + commandApi + "---data:" + data);
        DuiSteamBakePowerBean duiSteamBakePowerBean = JsonUtils.json2Pojo(data, DuiSteamBakePowerBean.class);
        DuiSteamBakePowerBean.NluBean nlu = duiSteamBakePowerBean.getNlu();
        DuiSteamBakePowerBean.NluBean.SemanticsBean semantics = nlu.getSemantics();
        String value = semantics.getRequest().getSlots().get(0).getValue();

        if (fanList != null) {
            fanList.clear();
        }
        for (int i = 0; i < finaldeviceList.size(); i++) {
            if (finaldeviceList.get(i) instanceof AbsSteameOvenOne) {
                AbsSteameOvenOne absSteameOvenOne = (AbsSteameOvenOne) finaldeviceList.get(i);
                fanList.add(absSteameOvenOne);
            }
        }


        switch (value) {
            //打开
            case "on":
                if (deviceItemView != null) {
                    deviceItemView.setDevice(absSteameOvenOne);
                    deviceItemView.layout();
                }
                absSteameOvenOne.setSteameOvenStatus_on(new VoidCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
                break;
            //关闭
            case "off":
                if (deviceItemView != null) {
                    deviceItemView.setDevice(absSteameOvenOne);
                    deviceItemView.layout();
                }
                absSteameOvenOne.setSteameOvenStatus_Off(new VoidCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
                break;
        }
    }


    private void AbsSterilizerPower(String commandApi, String data) {

        try {

            Log.d("201908161", "commandApi：" + commandApi + "---data:" + data);

            DuiSterilizerPowerBean duiSterilizerPowerBean = JsonUtils.json2Pojo(data, DuiSterilizerPowerBean.class);

            String power1 = duiSterilizerPowerBean.getPower();

            String selectNo1;
            if (duiSterilizerPowerBean.getSelectNo() == null) {
                selectNo1 = "1";
            } else {
                if ("".equals(duiSterilizerPowerBean.getSelectNo())) {
                    selectNo1 = "1";
                } else {
                    selectNo1 = duiSterilizerPowerBean.getSelectNo();
                }
            }


            if (fanList != null) {
                fanList.clear();
            }
            for (int i = 0; i < finaldeviceList.size(); i++) {
                if (finaldeviceList.get(i) instanceof AbsSterilizer) {
                    AbsSterilizer absSterilizer = (AbsSterilizer) finaldeviceList.get(i);
                    fanList.add(absSterilizer);
                }
                if (finaldeviceList.get(i) instanceof AbsRika) {
                    AbsRika absRika = (AbsRika) finaldeviceList.get(i);
                    if ("RIKAX".equals(absRika.getDp())) {
                        fanList.add(absRika);
                    }

                }
            }

            if (Integer.valueOf(selectNo1) > fanList.size()) {
                return;
            }

            if (fanList.get(Integer.valueOf(selectNo1) - 1) instanceof AbsSterilizer) {
                AbsSterilizer absSterilizer = (AbsSterilizer) fanList.get(Integer.valueOf(selectNo1) - 1);

                switch (power1) {
                    case "on":
                        if (deviceItemView != null) {
                            deviceItemView.setDevice(absSterilizer);
                            deviceItemView.layout();
                        }
                        //929单独处理
                        if (absSterilizer instanceof Steri829) {
                            ((Steri829) absSterilizer).setSteriPower((short) 1, new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    LogUtils.i("20190816111", "Steri829开机成功");
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    LogUtils.i("20190816111", t.getMessage());

                                }
                            });
                        } else {
                            ((Steri826) absSterilizer).setSteriPower((short) 1, new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    LogUtils.i("20190816111", "Steri826开机成功");
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    LogUtils.i("20190816111", t.getMessage());
                                }
                            });
                        }


                        break;
                    case "off":
                        if (deviceItemView != null) {
                            deviceItemView.setDevice(absSterilizer);
                            deviceItemView.layout();
                        }
                        //929单独处理
                        if (absSterilizer instanceof Steri829) {
                            ((Steri829) absSterilizer).setSteriPower((short) 0, new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    LogUtils.i("20190816111", "Steri829关机成功");
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    LogUtils.i("20190816111", t.getMessage());
                                }
                            });
                        } else {
                            ((Steri826) absSterilizer).setSteriPower((short) 0, new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    LogUtils.i("20190816111", "Steri826关机成功");
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    LogUtils.i("20190816111", t.getMessage());
                                }
                            });
                        }
                        break;
                }
            } else if (fanList.get(Integer.valueOf(selectNo1) - 1) instanceof AbsRika) {
                AbsRika absRika = (AbsRika) fanList.get(Integer.valueOf(selectNo1) - 1);


                switch (power1) {
                    case "on":
                        if (deviceItemView != null) {
                            deviceItemView.setDevice(absRika);
                            deviceItemView.layout();
                        }

                        if (absRika.sterilWorkStatus == RikaStatus.STERIL_OFF) {
                            absRika.setSterilizerWorkStatus((short) 140, (short) 1, (short) 67, (short) 1,
                                    (short) 49, (short) 4, RikaStatus.STERIL_ON, (short) 0, (short) 0, (short) 0, new VoidCallback() {
                                        @Override
                                        public void onSuccess() {
                                            LogUtils.i("201909161111", "开机成功");
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                            LogUtils.i("201909161111", t.getMessage());
                                        }
                                    });


                        }
                        break;
                    case "off":
                        if (deviceItemView != null) {
                            deviceItemView.setDevice(absRika);
                            deviceItemView.layout();
                        }
                        short sterilWorkStatus = absRika.sterilWorkStatus;
                        short sterilOn = RikaStatus.STERIL_ON;
                        LogUtils.i("2019091622222", "sterilWorkStatus:::" + sterilWorkStatus + "sterilOn:::" + sterilOn);
//                        if (absRika.sterilWorkStatus == RikaStatus.STERIL_ON) {
                        absRika.setSterilizerWorkStatus((short) 140, (short) 1, (short) 67, (short) 1,
                                (short) 49, (short) 4, RikaStatus.STERIL_OFF, (short) 0, (short) 0, (short) 0, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                        LogUtils.i("201909161111", "关机成功");
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        LogUtils.i("201909161111", t.getMessage());
                                    }
                                });

//                        }

                        break;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean AbsMicroWavePower(String commandApi, String data) throws Exception {
        LogUtils.i("2019080911111", "commandApi:" + commandApi + "data:" + data);
        DuiMWPowerBean duiMWPowerBean = JsonUtils.json2Pojo(data, DuiMWPowerBean.class);
        String power = duiMWPowerBean.getPower();
        String selectNo = duiMWPowerBean.getSelectNo();

        if (fanList != null) {
            fanList.clear();
        }
        for (int i = 0; i < finaldeviceList.size(); i++) {
            if (finaldeviceList.get(i) instanceof AbsMicroWave) {
                AbsMicroWave absMicroWave = (AbsMicroWave) finaldeviceList.get(i);
                fanList.add(absMicroWave);
            }
        }

        if (Integer.valueOf(selectNo) > fanList.size()) {
            return true;
        }
        absMicroWave = (AbsMicroWave) fanList.get(Integer.valueOf(selectNo) - 1);
        LogUtils.i("20190805636363", "fanList.size():" + fanList.size() + "      selectNo:" + selectNo + "    Integer.valueOf(selectNo9)   :" + Integer.valueOf(selectNo));
        if (deviceItemView != null) {
            deviceItemView.setDevice(absMicroWave1);
            deviceItemView.layout();
        }
        if (absMicroWave != null) {
            if (absMicroWave.state == MicroWaveStatus.Run) {
                switch (power) {
                    case "stop":
                        short endStatus = 4;
                        if (absMicroWave.state == MicroWaveStatus.Run) {
                            endStatus = 4;
                        } else if (absMicroWave.state == MicroWaveStatus.Pause) {
                            endStatus = 1;
                        }
                        absMicroWave.setMicroWaveState(endStatus, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onFailure(Throwable t) {
                            }
                        });
                        break;
                    case "pause":
                        absMicroWave.setMicroWaveState(MicroWaveStatus.Pause, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                LogUtils.i("20190809999", "微波炉停止工作");
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.show("下发指令失败", Toast.LENGTH_SHORT);
                            }
                        });
                        break;
                }

            }

        }
        return false;
    }

    private boolean AbsSterilizerSet(String commandApi, String data) throws Exception {

        DuiSterilizerSetBean duiSterilizerSetBean = JsonUtils.json2Pojo(data, DuiSterilizerSetBean.class);
        final String mode = duiSterilizerSetBean.getMode();

        LogUtils.i("20191015", "mode:::1::" + mode);
        final String min;
        if (duiSterilizerSetBean.getTime() == null) {
            min = "0";
        } else {
            String time = duiSterilizerSetBean.getTime();
            if (!"".equals(time)) {
                //time : 00:10:00
//                min = time.substring(3, 5);//分
                String[] split = time.split(":");
                min = split[1];
            } else {
                min = "0";
            }
        }

        LogUtils.i("20191015", "mode:::2::" + mode);
        String selectNo2;
        if (duiSterilizerSetBean.getSelectNo() == null) {
            selectNo2 = "1";
        } else {
            if (duiSterilizerSetBean.getSelectNo().equals("")) {
                selectNo2 = "1";
            } else {
                selectNo2 = duiSterilizerSetBean.getSelectNo();
            }
        }
        LogUtils.i("20191015", "mode:::3::" + mode);
        if (fanList != null) {
            fanList.clear();
        }
        for (int i = 0; i < finaldeviceList.size(); i++) {
            if (finaldeviceList.get(i) instanceof AbsSterilizer) {
                AbsSterilizer absSterilizer1 = (AbsSterilizer) finaldeviceList.get(i);
                fanList.add(absSterilizer1);
            }

            if (finaldeviceList.get(i) instanceof AbsRika) {
                AbsRika absRika = (AbsRika) finaldeviceList.get(i);
                if ("RIKAX".equals(absRika.getDp())) {

                    fanList.add(absRika);
                }
            }
        }
        LogUtils.i("20191015", "mode:::4::" + mode);
        if (Integer.valueOf(selectNo2) > fanList.size()) {
            return true;
        }

        LogUtils.i("20191015", "mode:::5::" + mode);
        if (fanList.get(Integer.valueOf(selectNo2) - 1) instanceof AbsSterilizer) {
            final AbsSterilizer absSterilizer1 = (AbsSterilizer) fanList.get(Integer.valueOf(selectNo2) - 1);

            if (deviceItemView != null) {
                deviceItemView.setDevice(absSterilizer1);
                deviceItemView.layout();
            }

            //829单独处理
            if (absSterilizer1 instanceof Steri829) {
/**
 * 2：消毒
 * 3：保洁
 * 4：烘干
 * 7：快速杀菌/除菌
 * 8：智能检测
 * 9：感应杀菌
 * 10：暖碟
 */
                switch (mode) {


                    //消毒
                    case "2":
                        ((Steri829) absSterilizer1).setSteriDisinfect(Short.valueOf(min), new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                LogUtils.i("20190816111222", "消毒成功");
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                LogUtils.i("20190816111222", t.getMessage());
                            }
                        });
                        break;
                    //保洁
                    case "3":

                        ((Steri829) absSterilizer1).setSteriClean((short) 60, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                LogUtils.i("20190816111222", "保洁成功");
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                LogUtils.i("20190816111222", t.getMessage());
                            }
                        });
                        break;
                    //烘干
                    case "4":

                        ((Steri829) absSterilizer1).setSteriDrying(Short.valueOf(min), new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                LogUtils.i("20190816111222", "烘干成功" + min);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                LogUtils.i("20190816111222", t.getMessage() + Short.valueOf(min));
                            }
                        });
                        break;

                    //消毒
                    case "7":
                        ((Steri829) absSterilizer1).setSteriDisinfect(Short.valueOf(min), new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                LogUtils.i("20190816111222", "消毒成功");
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                LogUtils.i("20190816111222", t.getMessage());
                            }
                        });
                        break;

                    //暖碟
                    case "10":
                        LogUtils.i("20190912111", String.valueOf(min));
                        ((Steri829) absSterilizer1).setSteriDrying(Short.valueOf(min), new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                LogUtils.i("20190816111222", "暖碟成功");
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                LogUtils.i("20190816111222", t.getMessage());
                            }
                        });
                        break;


                    default:
                        break;
                }


            } else {
                switch (mode) {
                    //消毒
                    case "2":
                        ((Steri826) absSterilizer1).setSteriPower((short) 2, (short) 130, (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.show("指令下发失败", Toast.LENGTH_SHORT);
                            }
                        });

                        break;
                    //保洁
                    case "3":

                        break;
                    //烘干
                    case "4":


                        break;

                    //消毒
                    case "7":
                        ((Steri826) absSterilizer1).setSteriPower((short) 7, Short.parseShort(min), (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.show("指令下发失败", Toast.LENGTH_SHORT);
                            }
                        });

                        break;
                    //智能检测
                    case "8":
                        LogUtils.i("20190816111222", "826 智能检测");


                        ((Steri826) absSterilizer1).setSteriPower((short) 8, (short) 0, (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                LogUtils.i("20190816111222", "826 智能检测");
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.show("指令下发失败", Toast.LENGTH_SHORT);
                            }
                        });
                        break;
                    //感应杀菌
                    case "9":

                        break;
                    //暖碟
                    case "10":
                        ((Steri826) absSterilizer1).setSteriPower((short) 10, Short.parseShort(min), (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.show("指令下发失败", Toast.LENGTH_SHORT);
                            }
                        });
                        break;

                    default:
                        break;
                }

            }
        } else if (fanList.get(Integer.valueOf(selectNo2) - 1) instanceof AbsRika) {
            LogUtils.i("20191015", "mode:::6::" + mode);
            AbsRika absRika = (AbsRika) fanList.get(Integer.valueOf(selectNo2) - 1);

            if (deviceItemView != null) {
                deviceItemView.setDevice(absRika);
                deviceItemView.layout();
            }
            LogUtils.i("20190912444", min);

            LogUtils.i("20191015", "mode:::7::" + mode);
            short mins = 0;
            if ("2".equals(mode)) {
                mins = (short) (Short.valueOf(min) * 10);
            } else if ("3".equals(mode)) {
                mins = (short) 60;
            } else if ("7".equals(mode)) {
                mins = (short) 60;
            } else {
                mins = Short.valueOf(min);
            }
            if ("0".equals(min)) {
                mins = (short) 60;
            }
            LogUtils.i("201909160000", "设置成功 mode:::" + mode + "min:::" + mins);
            //消毒 2  成功
            //保洁 3  失败
            //烘干 4  成功
            //快速杀菌 7  限制60分钟
            //智能检测 8  失败
            //感应杀菌 9 失败
            //暖碟 10 失败

            LogUtils.i("20191015", "mode:::8::" + mode);
            if ("3".equals(mode) || "8".equals(mode) || "9".equals(mode) || "10".equals(mode)) {
                return false;
            }
            LogUtils.i("20191015", "mode:::9::" + mode);
            absRika.setSterilizerWorkStatus((short) 140,
                    (short) 1,
                    (short) 67,
                    (short) 1,
                    (short) 49,
                    (short) 4,
                    Short.valueOf(mode),
                    mins,
                    (short) 4,
                    (short) 0,
                    new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            LogUtils.i("201909160000", "设置成功 mode:::" + mode);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            LogUtils.i("201909160000", t.getMessage());

                        }
                    });


        }


        return false;
    }

    private void AbsSterilizerState(String commandApi, String data) {

        ListWidget listWidget = new ListWidget();
        for (int i = 0; i < finaldeviceList.size(); i++) {
            if (finaldeviceList.get(i) instanceof AbsSterilizer || finaldeviceList.get(i) instanceof AbsRika) {
                boolean connected = false;
                short status = 0;
                String dt = null;
                if (finaldeviceList.get(i) instanceof AbsSterilizer) {
                    AbsSterilizer absSterilizer = (AbsSterilizer) finaldeviceList.get(i);
                    connected = absSterilizer.isConnected();
                    status = absSterilizer.status;
                    dt = absSterilizer.getDt();
                }
                if (finaldeviceList.get(i) instanceof AbsRika) {
                    AbsRika absRika = (AbsRika) finaldeviceList.get(i);
                    if ("RIKAX".equals(absRika.getDp())) {
                        connected = absRika.isConnected();
                        status = absRika.sterilWorkStatus;
                        dt = absRika.getDt();
                    }

                }
                ContentWidget widget = new ContentWidget();
                LogUtils.i("201910142222", "connected::   " + connected + "  status:::" + status + " :::dt::" + dt);
//                if (connected &&status!=0&&dt!=null){
                if (dt != null) {
                    LogUtils.i("201910142222", "connected::   " + connected + "  status:::" + status + " :::dt::" + dt);
                    widget.addExtra("online", connected + "");
                    widget.addExtra("state", status);
                    widget.addExtra("type", dt);
                    listWidget.addContentWidget(widget);
                }

            }
        }
        DDS.getInstance().getAgent().feedbackNativeApiResult(commandApi, listWidget);
    }

    private void AbsMWState(String commandApi, String data) {
        LogUtils.i("20190805-1", "commandApi:" + commandApi + "data:" + data);

        ListWidget listWidget = new ListWidget();
        for (int i = 0; i < finaldeviceList.size(); i++) {
            if (finaldeviceList.get(i) instanceof AbsMicroWave) {
                AbsMicroWave absMicroWave = (AbsMicroWave) finaldeviceList.get(i);
                boolean connected = absMicroWave.isConnected();
                short state = absMicroWave.state;
                String dt = absMicroWave.getDt();
                ContentWidget widget = new ContentWidget();
                widget.addExtra("online", connected + "");
                widget.addExtra("state", state);
                widget.addExtra("type", dt);
                listWidget.addContentWidget(widget);
            }
        }
        DDS.getInstance().getAgent().feedbackNativeApiResult(commandApi, listWidget);
    }

    private void AbsMWSet(String commandApi, String data) throws Exception {
        LogUtils.i("20190805-3", "commandApi:" + commandApi + "data:" + data);
        DuiMVSetBean duiMVSetBean = JsonUtils.json2Pojo(data, DuiMVSetBean.class);
        String time2 = duiMVSetBean.getTime();
        String power3 = duiMVSetBean.getPower();
        String mvmodel = duiMVSetBean.getMode();

        String selectNo9;
        if (duiMVSetBean.getSelectNo() == null) {
            selectNo9 = "1";
        } else {
            selectNo9 = duiMVSetBean.getSelectNo();
        }
        try {
            //time : 00:10:00
            String[] split = time2.split(":");
            final String min = split[1];
//        final String min = time2.substring(3, 5);//分
            String second = time2.substring(time2.length() - 2, time2.length());//秒
            int i1 = Integer.parseInt(second);
            if (min.equals("0")) {
                totalTime = i1;
            } else {
                int i = Short.parseShort(min) * 60;
                totalTime = i + i1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (fanList != null) {
            fanList.clear();
        }
        for (int i = 0; i < finaldeviceList.size(); i++) {
            if (finaldeviceList.get(i) instanceof AbsMicroWave) {
                AbsMicroWave absMicroWave = (AbsMicroWave) finaldeviceList.get(i);
                fanList.add(absMicroWave);
            }
        }

        if (Integer.valueOf(selectNo9) > fanList.size()) {
            return;
        }
        absMicroWave1 = (AbsMicroWave) fanList.get(Integer.valueOf(selectNo9) - 1);

        LogUtils.i("20190805636363", "fanList.size():" + fanList.size() + "      selectNo9:" + selectNo9 + "    Integer.valueOf(selectNo9)   :" + Integer.valueOf(selectNo9));

        if (deviceItemView != null) {
            deviceItemView.setDevice(absMicroWave1);
            deviceItemView.layout();
        }

        LogUtils.i("20190805278383", "mvmodel:" + mvmodel + "totalTime:" + totalTime + "power3:" + power3);
        setMWMode(absMicroWave1, mvmodel, String.valueOf(totalTime), power3);
    }

    private void AbsOvenSet(String commandApi, String data) throws Exception {
        Log.d("20190731", "commandApi：" + commandApi + "---data:" + data);
        DuiOvenSetBean duiOvenSetBean = JsonUtils.json2Pojo(data, DuiOvenSetBean.class);
        String selectNo8;
        if (duiOvenSetBean.getSelectNo() == null) {
            selectNo8 = "1";
        } else {
            selectNo8 = duiOvenSetBean.getSelectNo();
        }

        String temp1 = duiOvenSetBean.getTemp();
        String time1 = duiOvenSetBean.getTime();
        String[] split = time1.split(":");
        final String substring1 = split[1];
//        final String substring1 = time1.substring(3, 5);
        String mode = duiOvenSetBean.getMode();

        if (fanList != null) {
            fanList.clear();
        }
        for (int i = 0; i < finaldeviceList.size(); i++) {
            if (finaldeviceList.get(i) instanceof AbsOven) {
                AbsOven absOven = (AbsOven) finaldeviceList.get(i);
                fanList.add(absOven);
            }
        }

        if (Integer.valueOf(selectNo8) > fanList.size()) {
            return;
        }
        absOven2 = (AbsOven) fanList.get(Integer.valueOf(selectNo8) - 1);

        if (deviceItemView != null) {
            deviceItemView.setDevice(absOven2);
            deviceItemView.layout();
        }
        if ("9".equals(mode)) {
            String bottomTemp = duiOvenSetBean.getBottomTemp();
            sendEXP(absOven2, MsgKeys.SetOven_RunMode_Req, mode, Integer.valueOf(temp1), Integer.valueOf(bottomTemp), Integer.valueOf(substring1));
        } else {
            send(absOven2, MsgKeys.SetOven_RunMode_Req, mode, Integer.valueOf(substring1), Integer.valueOf(temp1));
        }
    }

    private void getOvenState(String commandApi) {
        ListWidget searchNums2 = new ListWidget();
        for (int i = 0; i < finaldeviceList.size(); i++) {
            if (finaldeviceList.get(i) instanceof AbsOven) {
                AbsOven absOven = (AbsOven) finaldeviceList.get(i);
                boolean connected = absOven.isConnected();
                short status = absOven.status;
                String dt = absOven.getDt();
                ContentWidget widget = new ContentWidget();
                widget.addExtra("online", connected + "");
                widget.addExtra("state", status);
                widget.addExtra("type", dt);
                searchNums2.addContentWidget(widget);
            }
        }

        DDS.getInstance().getAgent().feedbackNativeApiResult(commandApi, searchNums2);
    }


    private void openAbsOven(String commandApi, String data) throws Exception {
        Log.d("20190712", "commandApi：" + commandApi + "---data:" + data);
        DuiOvenPowerBean duiOvenPowerBean = JsonUtils.json2Pojo(data, DuiOvenPowerBean.class);
        String selectNo7 = duiOvenPowerBean.getSelectNo();
        String power2 = duiOvenPowerBean.getPower();

        if (fanList != null) {
            fanList.clear();
        }
        for (int i = 0; i < finaldeviceList.size(); i++) {
            if (finaldeviceList.get(i) instanceof AbsOven) {
                AbsOven absOven = (AbsOven) finaldeviceList.get(i);
                fanList.add(absOven);
            }
        }

        if (Integer.valueOf(selectNo7) > fanList.size()) {
            return;
        }

        absOven1 = (AbsOven) fanList.get(Integer.valueOf(selectNo7) - 1);
        switch (power2) {
            //打开烤箱
            case "on":
                if (deviceItemView != null) {
                    deviceItemView.setDevice(absOven1);
                    deviceItemView.layout();
                }

                absOven1.setOvenStatusControl(OvenStatus.On, new VoidCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.i("20190805", t.getMessage());
                    }
                });
                break;
            //关闭烤箱
            case "off":
                if (deviceItemView != null) {
                    deviceItemView.setDevice(absOven1);
                    deviceItemView.layout();
                }

                absOven1.setOvenStatusControl(OvenStatus.Off, new VoidCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.i("20190805", t.getMessage());
                    }
                });

                break;
        }
    }

    private void AbsSteamSet(String data) throws Exception {
        LogUtils.i("20190820666", "蒸箱卡点2");
        DuiSteamTempTimeSetBean duiSteamTempTimeSetBean = JsonUtils.json2Pojo(data, DuiSteamTempTimeSetBean.class);
        String selectNo6 = duiSteamTempTimeSetBean.getSelectNo();
        final String temp = duiSteamTempTimeSetBean.getTemp();
        final String time = duiSteamTempTimeSetBean.getTime();
        String[] split = time.split(":");
        final String substring = split[1];


//        final String substring = time.substring(3, 5);

        LogUtils.i("20190820666", "蒸箱卡点3");
        if (fanList != null) {
            fanList.clear();
        }
        for (int i = 0; i < finaldeviceList.size(); i++) {
            if (finaldeviceList.get(i) instanceof AbsSteamoven) {
                AbsSteamoven absSteamoven1 = (AbsSteamoven) finaldeviceList.get(i);
                fanList.add(absSteamoven1);
            }
            if (finaldeviceList.get(i) instanceof AbsRika) {


                AbsRika absRika = (AbsRika) finaldeviceList.get(i);
                if ("RIKAZ".equals(absRika.getDp())) {
                    fanList.add(absRika);
                    LogUtils.i("20191014888", "蒸箱温度时间设置:::进来" + absRika.getDt());
                }


            }
        }
        LogUtils.i("20190820666", "蒸箱卡点4");

        if (Integer.valueOf(selectNo6) > fanList.size()) {
            return;
        }
        LogUtils.i("20190820666", "蒸箱卡点5");

        if (fanList.get(Integer.valueOf(selectNo6) - 1) instanceof AbsSteamoven) {
            absSteamoven1 = (AbsSteamoven) fanList.get(Integer.valueOf(selectNo6) - 1);
            if (deviceItemView != null) {
                deviceItemView.setDevice(absSteamoven1);
                deviceItemView.layout();
            }
            LogUtils.i("20190820666", "蒸箱卡点6");
            absSteamoven1.setSteamStatus(SteamStatus.On, new VoidCallback() {
                @Override
                public void onSuccess() {
                    LogUtils.i("20190820666", "蒸箱卡点7");
                    sendCom(Short.valueOf(substring), Short.valueOf(temp));

                    LogUtils.i("20190820222", "time：" + Short.valueOf(substring) + "temp:" + Short.valueOf(temp));

                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("20190820222", t.getMessage());

                }
            });
        } else if (fanList.get(Integer.valueOf(selectNo6) - 1) instanceof AbsRika) {
            final AbsRika absRika = (AbsRika) fanList.get(Integer.valueOf(selectNo6) - 1);
            if (deviceItemView != null) {
                deviceItemView.setDevice(absSteamoven1);
                deviceItemView.layout();
            }


            if (absRika.steamWorkStatus == RikaStatus.STEAM_OFF) {
                absRika.setSteamWorkStatus((short) 1, RikaStatus.STEAM_CATEGORYCODE, (short) 1,
                        (short) 49, (short) 1, RikaStatus.STEAM_ON, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                LogUtils.i("20190919111", "电蒸箱打开成功");
                                AbsRikaSteamModeSet(absRika, temp, substring);

                            }

                            @Override
                            public void onFailure(Throwable t) {
                                LogUtils.i("20190919111", t.getMessage());
                            }
                        });

            } else {
                AbsRikaSteamModeSet(absRika, temp, substring);
            }


        }

    }

    private void AbsRikaSteamModeSet(final AbsRika absRika, String temp, String substring) {
        absRika.setSteamRunWorkModel((short) 1, RikaStatus.STEAM_CATEGORYCODE, (short) 1,
                (short) 50, (short) 3, (short) 00, Short.valueOf(temp), Short.valueOf(substring), new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        absRika.setSteamWorkStatus((short) 1, RikaStatus.STEAM_CATEGORYCODE, (short) 1,
                                (short) 49, (short) 1, RikaStatus.STEAM_RUN, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                        LogUtils.i("20190919111", "蒸箱设置成功");

                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        LogUtils.i("20190919111", t.getMessage());
                                    }
                                });

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
    }

    private void openAbsSteam(String commandApi, String data) throws Exception {
        Log.d("20190712", "commandApi：" + commandApi + "---data:" + data);
        DuiSteamOvenBean duiSteamOvenBean = JsonUtils.json2Pojo(data, DuiSteamOvenBean.class);
        String power1 = duiSteamOvenBean.getPower();
        String selectNo5 = duiSteamOvenBean.getSelectNo();
        if (fanList != null) {
            fanList.clear();
        }
        for (int i = 0; i < finaldeviceList.size(); i++) {
            if (finaldeviceList.get(i) instanceof AbsSteamoven) {
                AbsSteamoven absSteamoven = (AbsSteamoven) finaldeviceList.get(i);
                fanList.add(absSteamoven);
            }


            if (finaldeviceList.get(i) instanceof AbsRika) {
                AbsRika absRika = (AbsRika) finaldeviceList.get(i);
                if ("RIKAZ".equals(absRika.getDp())) {
                    LogUtils.i("20191014888", "打开蒸箱:::进来" + absRika.getDt());
                    fanList.add(absRika);
                }


            }
        }

        if (Integer.valueOf(selectNo5) > fanList.size()) {
            return;
        }
        if (fanList.get(Integer.valueOf(selectNo5) - 1) instanceof AbsSteamoven) {
            AbsSteamoven absSteamoven = (AbsSteamoven) fanList.get(Integer.valueOf(selectNo5) - 1);
            switch (power1) {
                case "on":
                    if (deviceItemView != null) {
                        deviceItemView.setDevice(absSteamoven);
                        deviceItemView.layout();
                    }
                    absSteamoven.setSteamStatus(SteamStatus.On, new VoidCallback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });

                    break;
                case "off":
                    if (deviceItemView != null) {
                        deviceItemView.setDevice(absSteamoven);
                        deviceItemView.layout();
                    }
                    absSteamoven.setSteamStatus(SteamStatus.Off, new VoidCallback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
                    break;
            }
        } else if (fanList.get(Integer.valueOf(selectNo5) - 1) instanceof AbsRika) {

            AbsRika absRika = (AbsRika) fanList.get(Integer.valueOf(selectNo5) - 1);
            switch (power1) {
                case "on":
                    if (deviceItemView != null) {
                        deviceItemView.setDevice(absRika);
                        deviceItemView.layout();
                    }
                    if (absRika.steamWorkStatus == RikaStatus.STEAM_OFF) {
                        absRika.setSteamWorkStatus((short) 1, RikaStatus.STEAM_CATEGORYCODE, (short) 1,
                                (short) 49, (short) 1, RikaStatus.STEAM_ON, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                        LogUtils.i("20190919111", "打开成功");

                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        LogUtils.i("20190919111", t.getMessage());
                                    }
                                });

                    }

                    break;
                case "off":
                    if (deviceItemView != null) {
                        deviceItemView.setDevice(absRika);
                        deviceItemView.layout();
                    }

                    if (absRika.steamWorkStatus == RikaStatus.STEAM_ON) {
                        absRika.setSteamWorkStatus((short) 1, RikaStatus.STEAM_CATEGORYCODE, (short) 1,
                                (short) 49, (short) 1, RikaStatus.STEAM_OFF, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                        LogUtils.i("20190919111", "关闭成功");

                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        LogUtils.i("20190919111", t.getMessage());
                                    }
                                });

                    }

                    break;
            }
        }

    }

    private void getSteamState(String commandApi) {
        ListWidget searchNums1 = new ListWidget();
        for (int i = 0; i < finaldeviceList.size(); i++) {


            if (finaldeviceList.get(i) instanceof AbsSteamoven || finaldeviceList.get(i) instanceof AbsRika) {
                boolean connected = false;
                short status = 0;
                String dt = null;
                if (finaldeviceList.get(i) instanceof AbsSteamoven) {
                    absSteamoven = (AbsSteamoven) finaldeviceList.get(i);
                    connected = absSteamoven.isConnected();
                    status = absSteamoven.status;
                    dt = absSteamoven.getDt();
                }


                if (finaldeviceList.get(i) instanceof AbsRika) {

                    AbsRika absRika = (AbsRika) finaldeviceList.get(i);
                    if ("RIKAZ".equals(absRika.getDp())) {
                        LogUtils.i("20191014888", "蒸箱查询:::进来" + absRika.getDt());

                        connected = absRika.isConnected();
                        status = absRika.steamWorkStatus;
                        dt = absRika.getDt();
                    }

                }

                ContentWidget widget = new ContentWidget();
                if (dt != null) {
                    widget.addExtra("online", connected + "");
                    LogUtils.i("20191014888", "connected:::" + connected + dt + status);
                    widget.addExtra("state", status);
                    widget.addExtra("type", dt);
                    searchNums1.addContentWidget(widget);
                }


            }
        }
        DDS.getInstance().getAgent().feedbackNativeApiResult(commandApi, searchNums1);
    }

    private void getRecipeSerachCount(final String commandApi, String data) throws Exception {
        DuiRecipeCount duiRecipeCount = JsonUtils.json2Pojo(data, DuiRecipeCount.class);
        final String ddd = duiRecipeCount.getDdd();
        CookbookManager.getInstance().getCookbooksByName(ddd, new Callback<Reponses.CookbooksResponse>() {
            @Override
            public void onSuccess(Reponses.CookbooksResponse result) {
                if (UIService.getInstance().isCurrentPage(PageKey.RecipeDetail)) {
                    UIService.getInstance().popBack();
                }
                int count = result.count();
                DuiWidget duiWidget = new DuiWidget("text")
                        .addExtra("count", String.valueOf(count));
                DDS.getInstance().getAgent().feedbackNativeApiResult(commandApi, duiWidget);

                Bundle bundle1 = new Bundle();
                bundle1.putString(PageArgumentKey.text, ddd);
                UIService.getInstance().postPage(PageKey.RecipeSearch, bundle1);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private void searchAbsFanState(String commandApi) {
        ListWidget searchNums = new ListWidget();
        for (int i = 0; i < finaldeviceList.size(); i++) {
            if (finaldeviceList.get(i) instanceof AbsFan || finaldeviceList.get(i) instanceof AbsRika) {
                boolean light = false;
                boolean connected = false;
                if (finaldeviceList.get(i) instanceof AbsFan) {
                    absFan = (AbsFan) finaldeviceList.get(i);
                    light = absFan.light;
                    connected = absFan.isConnected();
                }
                if (finaldeviceList.get(i) instanceof AbsRika) {
                    absRika = (AbsRika) finaldeviceList.get(i);
                    short rikaFanLight = absRika.rikaFanLight;
                    if (rikaFanLight == 0) {
                        light = false;
                    } else if (rikaFanLight == 1) {
                        light = true;
                    }
                    connected = absRika.isConnected();
                }


                LogUtils.i("20190814_connected", connected + "");
                ContentWidget widget = new ContentWidget();
                widget.addExtra("online", connected + "");
                widget.addExtra("light", light ? "on" : "false");
                searchNums.addContentWidget(widget);
            }
        }
        DDS.getInstance().getAgent().feedbackNativeApiResult(commandApi, searchNums);
    }


    private void AbsFanLight(String data) throws Exception {
        DuiLightBean duiLightBean = JsonUtils.json2Pojo(data, DuiLightBean.class);
        String LightPower = duiLightBean.getPower();// on/off
        String selectNo3 = duiLightBean.getSelectNo();
        if (fanList != null) {
            fanList.clear();
        }
        for (int i = 0; i < finaldeviceList.size(); i++) {
            if (finaldeviceList.get(i) instanceof AbsFan) {
                AbsFan absFan = (AbsFan) finaldeviceList.get(i);
                fanList.add(absFan);
            }

            if (finaldeviceList.get(i) instanceof AbsRika) {
                AbsRika absRika = (AbsRika) finaldeviceList.get(i);
                fanList.add(absRika);
            }
        }

        if (Integer.valueOf(selectNo3) > fanList.size()) {
            return;

        }
        if (fanList.get(Integer.valueOf(selectNo3) - 1) instanceof AbsFan) {
            AbsFan fan4 = (AbsFan) fanList.get(Integer.valueOf(selectNo3) - 1);
            if (deviceItemView != null) {
                deviceItemView.setDevice(fan4);
                deviceItemView.layout();
            }
            boolean tag = false;
            switch (LightPower) {
                case "on":
                    tag = true;
                    break;
                case "off":
                    tag = false;
                    break;
            }
            fan4.setFanLight(tag, new VoidCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        } else if (fanList.get(Integer.valueOf(selectNo3) - 1) instanceof AbsRika) {
            final AbsRika mRika = (AbsRika) fanList.get(Integer.valueOf(selectNo3) - 1);
            if (deviceItemView != null) {
                deviceItemView.setDevice(mRika);
                deviceItemView.layout();
            }
            short light = 0;
            switch (LightPower) {
                case "on":
                    light = 1;
                    break;
                case "off":
                    light = 0;
                    break;
            }
            mRika.setFanSwitchStatus((short) 1, RikaStatus.FAN_CATEGORYCODE, (short) 1, (short) 51, (short) 1,
                    light, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            LogUtils.i("20190916888", "灯光调节成功");
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            LogUtils.i("20190916888", t.getMessage());
                        }
                    });


        }

    }

    private boolean AbsFanLevelChange(String data) throws Exception {
        DuiChangeBean duiChangeBean = JsonUtils.json2Pojo(data, DuiChangeBean.class);
        String change = duiChangeBean.getChange();// up/down
        String selectNo2 = duiChangeBean.getSelectNo();
        if (fanList != null) {
            fanList.clear();
        }

        for (int i = 0; i < finaldeviceList.size(); i++) {
            if (finaldeviceList.get(i) instanceof AbsFan || finaldeviceList.get(i) instanceof AbsRika) {

                if (finaldeviceList.get(i) instanceof AbsFan) {
                    AbsFan absFan = (AbsFan) finaldeviceList.get(i);
                    fanList.add(absFan);
                }

                if (finaldeviceList.get(i) instanceof AbsRika) {
                    AbsRika absRika = (AbsRika) finaldeviceList.get(i);
                    fanList.add(absRika);
                }

            }
        }
        if (Integer.valueOf(selectNo2) > fanList.size()) {
            return true;
        }
        if (fanList.get(Integer.valueOf(selectNo2) - 1) instanceof AbsFan) {
            AbsFan fan3 = (AbsFan) fanList.get(Integer.valueOf(selectNo2) - 1);

            if (deviceItemView != null) {
                deviceItemView.setDevice(fan3);
                deviceItemView.layout();
            }
            short level1 = absFan.level;


            switch (change) {
                case "up":
                    level1++;
                    break;
                case "down":
                    level1--;
                    break;
            }

            if (level1 < 0) {
                return true;
            }
            if (level1 > 6) {
                return true;
            }
            fan3.setFanLevel(level1, new VoidCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(Throwable t) {

                }
            });


        } else if (fanList.get(Integer.valueOf(selectNo2) - 1) instanceof AbsRika) {
            AbsRika absRika = (AbsRika) fanList.get(Integer.valueOf(selectNo2) - 1);

            if (deviceItemView != null) {
                deviceItemView.setDevice(absRika);
                deviceItemView.layout();
            }
            short level2 = absRika.rikaFanPower;
            LogUtils.i("20190916777", "rikaFanPower:::" + level2);

            switch (change) {
                case "up":
                    level2++;
                    break;
                case "down":
                    level2--;
                    break;
            }

            if (level2 < 0) {
                return true;
            }
            if (level2 > 6) {
                return true;
            }


            absRika.setFanSwitchStatus((short) 1,
                    RikaStatus.FAN_CATEGORYCODE,
                    (short) 1,
                    (short) 50,
                    (short) 1,
                    level2, new VoidCallback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure(Throwable t) {
                            LogUtils.i("20190916222", t.getMessage());
                        }
                    });


        }
        return false;

    }

    //烟机档位设置
    private void AbsFanSetLevel(String data) {
        DuiLevelBean duiLevelBean = null;
        try {
            duiLevelBean = JsonUtils.json2Pojo(data, DuiLevelBean.class);

            final String level = duiLevelBean.getLevel();// 0-6
            String selectNo1 = duiLevelBean.getSelectNo();
            if (fanList != null) {
                fanList.clear();
            }
            for (int i = 0; i < finaldeviceList.size(); i++) {
                if (finaldeviceList.get(i) instanceof AbsFan) {
                    AbsFan absFan = (AbsFan) finaldeviceList.get(i);
                    fanList.add(absFan);
                }

                if (finaldeviceList.get(i) instanceof AbsRika) {
                    AbsRika absRika = (AbsRika) finaldeviceList.get(i);
                    fanList.add(absRika);
                }
            }

            if (Integer.valueOf(selectNo1) > fanList.size()) {
                return;
            }

            if (fanList.get(Integer.valueOf(selectNo1) - 1) instanceof AbsFan) {
                AbsFan fan2 = (AbsFan) fanList.get(Integer.valueOf(selectNo1) - 1);
                if (deviceItemView != null) {
                    deviceItemView.setDevice(fan2);
                    deviceItemView.layout();
                }


                fan2.setFanLevel(Short.parseShort(level), new VoidCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            } else if (fanList.get(Integer.valueOf(selectNo1) - 1) instanceof AbsRika) {
                AbsRika absRika = (AbsRika) fanList.get(Integer.valueOf(selectNo1) - 1);
                if (deviceItemView != null) {
                    deviceItemView.setDevice(absRika);
                    deviceItemView.layout();
                }


                absRika.setFanSwitchStatus((short) 1, RikaStatus.FAN_CATEGORYCODE, (short) 1, (short) 50, (short) 1,
                        Short.parseShort(level), new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                LogUtils.i("201909161111", "level:::" + level);

                            }

                            @Override
                            public void onFailure(Throwable t) {
                                LogUtils.i("201909161111", t.getMessage());
                            }
                        });


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //语音打开烟机
    private void openAbsFan(String data) {
        DuiPowerBean duiPowerBean = null;
        try {
            duiPowerBean = JsonUtils.json2Pojo(data, DuiPowerBean.class);
            String selectNo;
            if (duiPowerBean.getSelectNo() == null) {
                selectNo = "1";
            } else {
                selectNo = duiPowerBean.getSelectNo();
            }

            String power = duiPowerBean.getPower();// on/off
            if (fanList != null) {
                fanList.clear();
            }
            for (int i = 0; i < finaldeviceList.size(); i++) {
                if (finaldeviceList.get(i) instanceof AbsFan) {
                    AbsFan absFan = (AbsFan) finaldeviceList.get(i);
                    fanList.add(absFan);
                }
                if (finaldeviceList.get(i) instanceof AbsRika) {
                    AbsRika absRika = (AbsRika) finaldeviceList.get(i);
                    fanList.add(absRika);
                }
            }

            if (Integer.valueOf(selectNo) > fanList.size()) {
                return;
            }

            if (fanList.get(Integer.valueOf(selectNo) - 1) instanceof AbsFan) {

                AbsFan fan = (AbsFan) fanList.get(Integer.valueOf(selectNo) - 1);
                LogUtils.i("20190808", power);
                switch (power) {

                    case "on":
                        if (deviceItemView != null) {
                            deviceItemView.setDevice(fan);
                            deviceItemView.layout();
                        }
                        fan.setFanStatus(FanStatus.On, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                LogUtils.i("201908081", "烟机打开成功");
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                t.printStackTrace();
                                LogUtils.i("201908081", t.getMessage());
                            }
                        });
                        break;
                    case "off":
                        if (deviceItemView != null) {
                            deviceItemView.setDevice(fan);
                            deviceItemView.layout();
                        }
                        fan.setFanStatus(FanStatus.Off, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                LogUtils.i("201908082", "烟机关闭成功");
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                t.printStackTrace();
                                LogUtils.i("201908082", t.getMessage());
                            }
                        });
                        break;

                }
            } else if (fanList.get(Integer.valueOf(selectNo) - 1) instanceof AbsRika) {
                final AbsRika mRika = (AbsRika) fanList.get(Integer.valueOf(selectNo) - 1);

                switch (power) {
                    case "on":
                        if (deviceItemView != null) {
                            deviceItemView.setDevice(mRika);
                            deviceItemView.layout();
                        }
                        mRika.setFanSwitchStatus((short) 1, RikaStatus.FAN_CATEGORYCODE, (short) 1, (short) 50, (short) 1,
                                (short) 1, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                        mRika.setCloseFanVolume((short) 1, RikaStatus.FAN_CATEGORYCODE, (short) 1, (short) 50, (short) 1,
                                                (short) 1, new VoidCallback() {
                                                    @Override
                                                    public void onSuccess() {
                                                        LogUtils.i("20190912", "打开rika烟机成功");
                                                    }

                                                    @Override
                                                    public void onFailure(Throwable t) {
                                                        LogUtils.i("20190912", t.getMessage());
                                                    }
                                                });
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {

                                    }
                                });
                        break;
                    case "off":
                        if (deviceItemView != null) {
                            deviceItemView.setDevice(mRika);
                            deviceItemView.layout();
                        }

                        if (RikaStatus.FAN_ON == mRika.rikaFanWorkStatus && mRika.rikaFanPower != 0) {
                            mRika.setCloseFanVolume((short) 1, RikaStatus.FAN_CATEGORYCODE, (short) 1, (short) 50, (short) 1,
                                    (short) 0, new VoidCallback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                        }
                                    });
                        }

                        break;

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void startPage(CookingKnowledge tag) {
        Bundle bd = new Bundle();
        bd.putLong(PageArgumentKey.Id, tag.id);
        bd.putInt(PageArgumentKey.contentType, tag.contentType);
        bd.putString(PageArgumentKey.Url, tag.videoId);
        UIService.getInstance().postPage(PageKey.KitchenKnowledgeArticle, bd);
    }

    public float getRawSize(int unit, float value) {
        Resources res = this.getResources();
        return TypedValue.applyDimension(unit, value, res.getDisplayMetrics());
    }

    @Override
    public void onRefresh() {

        if (NetworkUtils.isConnect(getContext())){
            llEmpty.setVisibility(GONE);
            if (DeviceService.getInstance().queryAll().size() > 0) {
                refreshHomeBackground();
                refreshDeviceView();
            } else {
                deviceContainer.removeAllViews();
                mRecyclerView.setVisibility(GONE);
                mTvDeviceIntellectualProducts.setVisibility(GONE);
                mTvKitchenKnowledge.setVisibility(GONE);
                rel_homedevice.setBackground(null);
                mGif.setVisibility(VISIBLE);
                String newbie = PreferenceUtils.getString("newBie", null);
                if (newbie != null) {
                    mIvAdd.setVisibility(VISIBLE);
                }

            }
        }else {
            if (!NetworkUtils.isConnect(getContext())){
                llEmpty.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRefresh();
                    }
                });
                llEmpty.setVisibility(VISIBLE);
                DialogHelper.notNetDialog(getContext());
            }

        }

    }


    //定义一个内部类广播接收者，接受删除设备之后刷新指令
    public class UIRefreshBroadcastReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            onRefresh();
        }
    }

    //蒸箱模式
    private void sendCom(final Short setTime, final Short setTemp) {

        absSteamoven1.setSteamProMode(setTime, setTemp, 0, new VoidCallback() {
            @Override
            public void onSuccess() {
                LogUtils.i("20190820666", "蒸箱卡点8");
                LogUtils.i("20190820444", "蒸箱模式:::" + setTime + ":::" + setTemp);
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20190820666", "蒸箱卡点9");
                t.printStackTrace();
                LogUtils.i("20190820444", t.getMessage());

            }
        });
    }

    //烤箱其他模式
    public void send(final AbsOven oven, final int cmd, final String mode, final int setTime, final int setTemp) {
        LogUtils.i("20180710", "cmd:" + cmd + "mode:" + mode + "setTime::" + setTime + " setTemp::" + setTemp);
        if (oven.status == OvenStatus.Off) {
            oven.setOvenStatus(OvenStatus.On, new VoidCallback() {
                @Override
                public void onSuccess() {
                    sendCom(oven, cmd, mode, setTime, setTemp);
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        } else {
            sendCom(oven, cmd, mode, setTime, setTemp);
        }
    }

    //烤箱其他模式
    private void sendCom(final AbsOven oven, final int cmd, final String mode, final int setTime, final int setTemp) {
        new Handler().postDelayed(new Runnable() {
            public void run() {

                oven.setOvenModelRunMode((short) cmd, Short.decode(mode), (short) setTime, (short) setTemp, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.show("指令下发失败了哟,请重新下发", Toast.LENGTH_SHORT);
                    }
                });
            }
        }, 500);
    }


    //烤箱专业模式设置
    private void sendEXP(AbsOven oven, int cmd, String mode, int setTempUp, int setTempDown, int setTime) {
        LogUtils.i("20180721", "code:" + cmd + " mode:" + mode + " setTime:" + setTime + " setTempUp:" +
                setTempUp + " setTempDown::" + setTempDown);
        if (oven.status == OvenStatus.Off) {
            oven.setOvenStatus(OvenStatus.On, new VoidCallback() {
                @Override
                public void onSuccess() {
                    LogUtils.i("20190805", "打开成功");

                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("20190805", t.getMessage());

                }
            });
        }

        oven.setOvenEXPModelRunMode((short) cmd, Short.decode(mode), (short) setTime,
                (short) setTempUp, (short) setTempDown, new VoidCallback() {

                    @Override
                    public void onSuccess() {
                        LogUtils.i("20190805", "成功");

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        LogUtils.i("20190805", t.getMessage());
                        ToastUtils.show("指令下发失败", Toast.LENGTH_SHORT);
                    }
                });
    }


    //微波炉设置
    private void setMWMode(AbsMicroWave mMicroWave, String model, String time, String power) {
        if (mMicroWave.doorState == 1) {
            ToastUtils.showShort(R.string.device_alarm_rika_E1);
            return;
        }

        mMicroWave.setMicroWaveProModeHeat(Short.valueOf(model), Short.valueOf(time), Short.valueOf(power), new VoidCallback() {
            @Override
            public void onSuccess() {
                LogUtils.i("20190928", "onSuccess:");
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20190928", "t:" + t);
            }
        });


    }


    private void sendProSteamOvenMode(String mode, final int setTempUp, final int setTempDown, final int setTime) {

        final short expMode = Short.parseShort(mode);
        absSteameOvenOne.setSteameOvenOneRunMode(expMode, (short) setTime,
                (short) setTempUp, (short) 0, (short) setTempDown, (short) 0, (short) 0, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        if (absSteameOvenOne != null) {
                            ToolUtils.logEvent(absSteameOvenOne.getDt(), "开始专业烘烤:" + setTempUp + ":" + setTempDown + ":" + setTime, "roki_设备");
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
    }


}
