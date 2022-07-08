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

import androidx.annotation.Nullable;
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
import android.view.ViewGroup;
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
import com.google.firebase.analytics.FirebaseAnalytics;
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
import com.legent.plat.events.DeviceNameChangeEvent;
import com.legent.plat.events.DeviceSelectedEvent;
import com.legent.plat.events.MessageEvent;
import com.legent.plat.events.MessageEventNumber;
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
import com.robam.roki.net.OnRequestListener;
import com.robam.roki.net.request.api.MessageApi;
import com.robam.roki.net.request.bean.MessageUserUnreadBean;
import com.robam.roki.observer.DuiCommandApiObserver;
import com.robam.roki.observer.DuiNativeApiObserver;
import com.robam.roki.service.AppService;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.UIListeners;
import com.robam.roki.ui.activity3.MessageActivity;
import com.robam.roki.ui.activity3.RWebActivity;
import com.robam.roki.ui.adapter.IntellkitchenAdapter;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.helper3.DialogHelper;
import com.robam.roki.ui.helper3.MyIndicator;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;
import com.robam.roki.ui.view.networkoptimization.BleConnectActivity;
import com.robam.roki.ui.view.networkoptimization.NetWorkStateUtils;
import com.robam.roki.utils.LoginUtil;
import com.robam.roki.utils.PermissionsUtils;
import com.robam.roki.utils.ToolUtils;
import com.robam.roki.utils.suspendedball.SystemUtils;
import com.yatoooon.screenadaptation.ScreenAdapterTools;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.legent.ContextIniter.cx;

/**
 * 智能页面
 */
public class HomeDeviceView extends FrameLayout implements UIListeners.IRefresh, OnRequestListener {

    //标题栏
    @InjectView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.ll_title)
    LinearLayout mLlTitle;
//    @InjectView(R.id.gif)
//    ImageView mGif;
//    @InjectView(R.id.titleView)
//    TitleBar mTitleView;

    @InjectView(R.id.br_kichen_home)
    Banner bannerKichen;
    @InjectView(R.id.tv_device_intellectual_products)
    TextView mTvDeviceIntellectualProducts;
    @InjectView(R.id.rl_kitchenknowledge)
    RelativeLayout rlKitchenknowledge;
    @InjectView(R.id.iv_kitchen_knowledge)
    ImageView ivKitchenKnowledge;

    @InjectView(R.id.rel_homedevice)
    RelativeLayout rel_homedevice;
    @InjectView(R.id.deviceContainer)
    GridLayout deviceContainer;
    @InjectView(R.id.ll_empty)
    LinearLayout llEmpty;
    @InjectView(R.id.rl_add_device)
    RelativeLayout rlAddDevice;

    FragmentActivity activity ;

    List<IDevice> IdeviceList = new ArrayList<IDevice>();
    List<IDevice> IdeviceList1 = new ArrayList<IDevice>();
    List<IDevice> finaldeviceList = new ArrayList<IDevice>();
//    IntellkitchenAdapter mIntellkitchenAdapter;
    BannerImageAdapter bannerImageAdapter;

    List<CookingKnowledge> mCookingKnowledges = new ArrayList<>();
    int fanNumber = 0;
    MessageApi mMessageApi;
    int otherDeviceNumber = 0;
    private boolean mLogon;
    //    private FirebaseAnalytics firebaseAnalytics;
    private UIRefreshBroadcastReceiver receiver;
    //定义界面刷新常量
    protected static final String ACTION = "com.robam.roki.senduirefreshcommand";


//    private DeviceNewItemView deviceItemView;
    private AbsFan absFan;
    private AbsSteamoven absSteamoven, absSteamoven1;
    private AbsOven absOven1, absOven2;
    private AbsMicroWave absMicroWave1;
    private int totalTime;
    private AbsMicroWave absMicroWave;
    private AbsSteameOvenOne absSteameOvenOne;
    private AbsRika absRika;


    List<IDevice> fanList = new LinkedList<>();

// 未发送的事件
//    @Subscribe
//    public void onEvent(CallResultApiEvent apiEvent) {
//        actionListener(apiEvent.getCommandApi(), apiEvent.getData());
//    }
//
//
//    @Subscribe
//    public void onEvent(QueryResultApiEvent queryResultApiEvent) {
//        actionListener(queryResultApiEvent.getNativeApi(), queryResultApiEvent.getData());
//    }

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
        mMessageApi.getUserUnreadMessage(R.layout.view_home_device);
    }

    @Subscribe
    public void onEvent(UserLogoutEvent event) {
//        refreshWhenPull();
        initData();

        findViewById(R.id.frag_txt_notification_number).setVisibility(View.GONE);
        EventUtils.postEvent(new MessageEventNumber(0));
    }

    @Subscribe
    public void onEvent(MessageEvent event) {
        mMessageApi.getUserUnreadMessage(R.layout.view_home_device);
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
//        onRefresh();
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
    }

    @Subscribe
    public void onEvent(NewBieGuideEvent event) {
        Log.e(TAG,"onEventNewBieGuideEvent");

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
    @Subscribe
    public void onEvent(DeviceNameChangeEvent event){
        onRefresh();
    }
    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_home_device, this, true);
        setStateBarFixer(view, getContext());
        if (!view.isInEditMode()) {
            ScreenAdapterTools.getInstance().loadView(view);
            ButterKnife.inject(this, view);
            initData();

            setInitView();
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
//                    refreshWhenPull();
                    initData();
                }
            });
        }
        mLogon = Plat.accountService.isLogon();


        findViewById(R.id.frag_image_message).setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {

                if (Plat.accountService.isLogon()){
                    getContext().startActivity(new Intent(getContext(), MessageActivity.class));
                }else{
                    CmccLoginHelper.getInstance().toLogin();
                }

            }
        });
//        mIvAdd.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean wifi = NetWorkStateUtils.isWifi(getContext());
//                boolean isLog = Plat.accountService.isLogon();
//
//                if(!isLog){
//                    startLogin();
//                    return;
//                }
//                if (isLog && wifi) {
//                    UIService.getInstance().postPage(PageKey.DeviceAdd);
//                } else if (isLog && !wifi) {
//                    UIService.getInstance().postPage(PageKey.SettingWifi);
//                } else if (!isLog && wifi) {
//                    LoginUtil.checkWhetherLogin2(getContext(), activity);
//                } else if (!isLog && !wifi) {
//                    LoginUtil.checkWhetherLogin2(getContext(), activity);
//                }
//            }
//        });

        mMessageApi=new MessageApi(this);

        llEmpty.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        findViewById(R.id.iv_kitchen_knowledge).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RWebActivity.start(activity , "https://h5.myroki.com/#/kitchenKnowledge");
            }
        });
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
        //网络未连接
        if (!NetworkUtils.isConnect(getContext())){
            swipeRefreshLayout.setVisibility(GONE);
            llEmpty.setVisibility(VISIBLE);
            DialogHelper.notNetDialog(getContext());
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        StoreService.getInstance().getCookingKnowledge("cookingSkill", 1, null, 0, 3, new Callback<List<CookingKnowledge>>() {
            @Override
            public void onSuccess(List<CookingKnowledge> cookingKnowledges) {
                if (cookingKnowledges != null) {
                    mCookingKnowledges.clear();
                    mCookingKnowledges.addAll(cookingKnowledges);
                    refreshKitchenKnowledge();
//                    rlKitchenknowledge.setVisibility(View.VISIBLE);
                    llEmpty.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(VISIBLE);
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setInitView() {
        if (Plat.accountService.isLogon() && DeviceService.getInstance().queryAll().size() > 0) {

            setTitleBar();
            onRefresh();

        } else {
            setTitleBar();

//            String newBie = PreferenceUtils.getString("newBie", null);
//            if (newBie != null) {
//                mIvAdd.setVisibility(VISIBLE);
//            }
            rlAddDevice.setVisibility(VISIBLE);
        }
    }




    void setTitleBar() {
//        mTitleView.setTitle(R.string.home_tab_device_kitchen);
        View.OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wifi = NetWorkStateUtils.isWifi(getContext());
                boolean isLog = Plat.accountService.isLogon();
                if (!isLog){
                    startLogin();
                    return;
                }
                if (wifi) {
//                    Intent intent = new Intent(activity, BleConnectActivity.class);
//                    activity.startActivity(intent);
                    UIService.getInstance().postPage(PageKey.DeviceAdd);
                } else {
                    UIService.getInstance().postPage(PageKey.SettingWifi);
                }
            }
        };
        findViewById(R.id.ic_device_add).setOnClickListener(onClickListener);
        rlAddDevice.setOnClickListener(onClickListener);

//        findViewById(R.id.ic_device_scan).setOnClickListener( new OnClickListener() {
//            private final static int SCANNIN_GREQUEST_CODE = 100;
//
//            @Override
//            public void onClick(View view) {
////                if (LoginUtil.checkWhetherLogin(getContext(), PageKey.UserLogin)) {
//
//                boolean isLog = Plat.accountService.isLogon();
//                if (!isLog){
//                    startLogin();
//                    return;
//                }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    int selfPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
//                    if (selfPermission == 0) {
//                        Activity atv = UIService.getInstance().getTop().getActivity();
//                        Intent intent = new Intent();
//                        intent.setClass(atv, ScanQrActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        atv.startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
//                    } else {
//                        PermissionsUtils.checkPermission(getContext(), Manifest.permission.CAMERA, PermissionsUtils.CODE_HOME_DEVICE_CAMERA);
//                    }
//                } else {
//                    Activity atv = UIService.getInstance().getTop().getActivity();
//                    Intent intent = new Intent();
//                    intent.setClass(atv, ScanQrActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    atv.startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
//                }
//            }
//        });

//        mTitleView.replaceLeft(icon_scan);
    }


    void refreshWhenPull() {
        AppService.getInstance().onLogin();
    }


    private void refreshHomeBackground() {

        mTvDeviceIntellectualProducts.setVisibility(VISIBLE);
        rlAddDevice.setVisibility(View.GONE);
    }

    public void refreshDeviceView() {

        deviceContainer.removeAllViews();

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
//            deviceItemView = new DeviceNewItemView(getContext());

            DeviceNewItemView deviceItemView = new DeviceNewItemView(getContext());
            if (finaldeviceList.get(i) instanceof AbsFan) {
                String name = finaldeviceList.get(i).getName() == null
                        || finaldeviceList.get(i).getName().equals(finaldeviceList.get(i).getCategoryName()) ?
                        finaldeviceList.get(i).getDispalyType() : finaldeviceList.get(i).getName();
                deviceItemView.setTxtDeviceName(name);

                deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());

                deviceItemView.setImageDeviceOfflinePrompt(finaldeviceList.get(i).isConnected());

            } else if (finaldeviceList.get(i) instanceof Stove) {
                String name = finaldeviceList.get(i).getName() == null
                        || finaldeviceList.get(i).getName().equals(finaldeviceList.get(i).getCategoryName()) ?
                        finaldeviceList.get(i).getDispalyType() : finaldeviceList.get(i).getName();
                deviceItemView.setTxtDeviceName(name);

                deviceItemView.setImageDeviceOfflinePrompt(finaldeviceList.get(i).isConnected());
                deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());
            } else if (finaldeviceList.get(i) instanceof Pot) {
                String name = finaldeviceList.get(i).getName() == null
                        || finaldeviceList.get(i).getName().equals(finaldeviceList.get(i).getCategoryName()) ?
                        finaldeviceList.get(i).getDispalyType() : finaldeviceList.get(i).getName();
                deviceItemView.setTxtDeviceName(name);
                deviceItemView.setImageDeviceOfflinePrompt(finaldeviceList.get(i).isConnected());

                deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());


            } else if (finaldeviceList.get(i) instanceof GasSensor) {
                String name = finaldeviceList.get(i).getName() == null
                        || finaldeviceList.get(i).getName().equals(finaldeviceList.get(i).getCategoryName()) ?
                        finaldeviceList.get(i).getDispalyType() : finaldeviceList.get(i).getName();
                deviceItemView.setTxtDeviceName(name);

                deviceItemView.setImageDeviceOfflinePrompt(finaldeviceList.get(i).isConnected());
                if (finaldeviceList.get(i).isConnected()) {
                    if (((GasSensor) finaldeviceList.get(i)).status == 1) {
                        deviceItemView.setImgDeviceAlarm(R.mipmap.ic_gas_list_alarm);
                    } else if (((GasSensor) finaldeviceList.get(i)).status == 3) {
                        deviceItemView.setImgDeviceAlarm(R.mipmap.ic_gas_list_guzhang);
                    }
                }
                LogUtils.i("20180605", "ff_status:" + ((GasSensor) finaldeviceList.get(i)).status);

                deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());
            } else {
                String name = finaldeviceList.get(i).getName() == null
                        || finaldeviceList.get(i).getName().equals(finaldeviceList.get(i).getCategoryName()) ?
                        finaldeviceList.get(i).getDispalyType() : finaldeviceList.get(i).getName();
                deviceItemView.setTxtDeviceName(name);

                LogUtils.i("20180330", "DispalyType:" + finaldeviceList.get(i).getDispalyType());
                if (finaldeviceList.get(i) instanceof AbsOven) {//烤箱
                    deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());

                    deviceItemView.setImageDeviceOfflinePrompt(finaldeviceList.get(i).isConnected());
                } else if (finaldeviceList.get(i) instanceof AbsSteamoven) {//蒸汽炉
                    deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());

                    deviceItemView.setImageDeviceOfflinePrompt(finaldeviceList.get(i).isConnected());
                } else if (finaldeviceList.get(i) instanceof AbsSterilizer) {//消毒柜
                    deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());

                    deviceItemView.setImageDeviceOfflinePrompt(finaldeviceList.get(i).isConnected());
                } else if (finaldeviceList.get(i) instanceof AbsMicroWave) {//微波炉
                    deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());

                    deviceItemView.setImageDeviceOfflinePrompt(finaldeviceList.get(i).isConnected());
                } else if (finaldeviceList.get(i) instanceof AbsWaterPurifier) {
                    deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());
                    deviceItemView.setImageDeviceOfflinePrompt(finaldeviceList.get(i).isConnected());
                } else if (finaldeviceList.get(i) instanceof AbsSteameOvenOne) {//烤蒸一体机
                    deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());
                    deviceItemView.setImageDeviceOfflinePrompt(finaldeviceList.get(i).isConnected());
                } else if (finaldeviceList.get(i) instanceof AbsRika) {

                    deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());
                    deviceItemView.setImageDeviceOfflinePrompt(finaldeviceList.get(i).isConnected());
                }
                else if (finaldeviceList.get(i) instanceof AbsIntegratedStove) {
                    deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());
                    deviceItemView.setImageDeviceOfflinePrompt(finaldeviceList.get(i).isConnected());
                } else if (finaldeviceList.get(i) instanceof AbsCooker) {
                    deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());
                    deviceItemView.setImageDeviceOfflinePrompt(finaldeviceList.get(i).isConnected());
                }else if (finaldeviceList.get(i) instanceof AbsDishWasher){//洗碗机
                    deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());
                    deviceItemView.setImageDeviceOfflinePrompt(finaldeviceList.get(i).isConnected());
                }else if (finaldeviceList.get(i) instanceof AbsHidKit) {
                    deviceItemView.setImageDevice(finaldeviceList.get(i).getCgIconUrl());
                    deviceItemView.setImageDeviceOfflinePrompt(finaldeviceList.get(i).isConnected());
                }
            }
            GridLayout.LayoutParams gl = new GridLayout.LayoutParams();
            if (i % 2 == 0) {
                gl.leftMargin = (int) getRawSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            }

            if (i != 0 && i != 1) {
                gl.topMargin = (int) getRawSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            }
            gl.setGravity(Gravity.CENTER);
            deviceItemView.setLayoutParams(gl);
            deviceItemView.setDevice(finaldeviceList.get(i));
            deviceContainer.addView(deviceItemView);


        }
    }

    private void refreshKitchenKnowledge() {
        if (null == bannerImageAdapter) {
            bannerImageAdapter = new BannerImageAdapter<CookingKnowledge>(mCookingKnowledges) {

                @Override
                public void onBindView(BannerImageHolder bannerImageHolder, CookingKnowledge cookingKnowledge, int position, int size) {
                    GlideApp.with(bannerImageHolder.imageView)
                            .load(cookingKnowledge.pictureCoverUrl)
                            .placeholder(R.mipmap.banner_default)
                            .override(350*2, 131*2)
                            .into(bannerImageHolder.imageView);
                }
            };
            bannerImageAdapter.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(Object o, int i) {
                    CookingKnowledge tag = mCookingKnowledges.get(i);
                    startPage(tag);
                }
            });
            bannerKichen.setAdapter(bannerImageAdapter)
                    .addBannerLifecycleObserver(activity)
                    .setIndicator(new MyIndicator(activity));
        } else {
            bannerImageAdapter.notifyDataSetChanged();
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
            swipeRefreshLayout.setVisibility(VISIBLE);
//            if (mCookingKnowledges.size() > 0) {
//                rlKitchenknowledge.setVisibility(View.VISIBLE);
//                refreshKitchenKnowledge();
//            } else
//                rlKitchenknowledge.setVisibility(View.GONE);

            if (DeviceService.getInstance().queryAll().size() > 0) {
                refreshHomeBackground();
                refreshDeviceView();
            } else {
                deviceContainer.removeAllViews();

                mTvDeviceIntellectualProducts.setVisibility(GONE);
                rlAddDevice.setVisibility(View.VISIBLE);
//                String newbie = PreferenceUtils.getString("newBie", null);
//                if (newbie != null) {
//                    mIvAdd.setVisibility(VISIBLE);
//                }

            }
        }else {
//            if (!NetworkUtils.isConnect(getContext())){
                        llEmpty.setVisibility(VISIBLE);
                        swipeRefreshLayout.setVisibility(GONE);
                DialogHelper.notNetDialog(getContext());
//            }

        }

    }

    @Override
    public void onFailure(int requestId, int requestCode, @Nullable String msg, @Nullable Object data) {

    }

    @Override
    public void onSaveCache(int requestId, int requestCode, @Nullable Object paramObject) {

    }

    @Override
    public void onSuccess(int requestId, int requestCode, @Nullable Object paramObject) {

        if (requestId==R.layout.view_home_device){
            if (paramObject!=null){
                if (paramObject instanceof MessageUserUnreadBean){

                    EventUtils.postEvent(new MessageEventNumber(((MessageUserUnreadBean) paramObject).getPayload().getTotalUnReadCount()));
                    if (!Plat.accountService.isLogon()||((MessageUserUnreadBean) paramObject).getPayload().getTotalUnReadCount()==0) {
                        findViewById(R.id.frag_txt_notification_number).setVisibility(View.GONE);
                    }else{
                        if (((MessageUserUnreadBean) paramObject).getPayload().getTotalUnReadCount()<=99) {
                            ((TextView) findViewById(R.id.frag_txt_notification_number)).setText(((MessageUserUnreadBean) paramObject).getPayload().getTotalUnReadCount() + "");
//                            ((TextView) findViewById(R.id.frag_txt_notification_number)).setTextSize(getResources().getDimension(R.dimen.sp_12));、
                        }else if (((MessageUserUnreadBean) paramObject).getPayload().getTotalUnReadCount()>99){
                            ((TextView) findViewById(R.id.frag_txt_notification_number)).setText("99+");
//                            ((TextView) findViewById(R.id.frag_txt_notification_number)).setTextSize(getResources().getDimension(R.dimen.sp_10));
                        }
                        findViewById(R.id.frag_txt_notification_number).setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }


    //定义一个内部类广播接收者，接受删除设备之后刷新指令
    public class UIRefreshBroadcastReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            onRefresh();
        }
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

    /**
     * 设置状态栏占位
     */
    private void setStateBarFixer(View root, Context context){
        View mStateBarFixer = root.findViewById(R.id.status_bar_fix);
        if (mStateBarFixer != null){
            ViewGroup.LayoutParams layoutParams = mStateBarFixer.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = SystemUtils.getStatusBarHeight(context);
            mStateBarFixer.setLayoutParams(layoutParams);
        }
    }
}
