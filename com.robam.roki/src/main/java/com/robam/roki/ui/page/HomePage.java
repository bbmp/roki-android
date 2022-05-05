package com.robam.roki.ui.page;

import static com.robam.roki.ui.page.SelectThemeDetailPage.TYPE_THEME_BANNER;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.ChatNewMsgEvent;
import com.legent.plat.events.DeviceFindEvent;
import com.legent.plat.events.FloatHelperEvent;
import com.legent.plat.events.RecipeShowEvent;
import com.legent.plat.events.UserLoginEvent;
import com.legent.plat.events.UserLogoutEvent;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.pojos.dictionary.DeviceType;
import com.legent.plat.services.ChatService;
import com.legent.plat.services.CommonService;
import com.legent.plat.services.DeviceService;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.adapters.ExtPageAdapter;
import com.legent.ui.ext.views.ExtViewPager;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.qrcode.ScanQrActivity;
import com.robam.common.events.DeviceEasylinkCompletedEvent;
import com.robam.common.events.NewBieGuideEvent;
import com.robam.common.events.ReturnDeviceViewEvent;
import com.robam.common.events.UMPushInfoEvent;
import com.robam.common.events.UMPushRecipeEvent;
import com.robam.common.events.UMPushThemeEvent;
import com.robam.common.events.UMPushVideoEvent;
import com.robam.common.pojos.RecipeConsultation;
import com.robam.common.pojos.RecipeTheme;
import com.robam.common.pojos.device.fan.IFan;
import com.robam.common.services.CookbookManager;
import com.robam.common.ui.BleRssiDevice;
import com.robam.common.util.RecipeRequestIdentification;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.MessageBean;
import com.robam.roki.observer.DuiUpdateObserver;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.UIListeners;
import com.robam.roki.ui.adapter.DialogAdapter;
import com.robam.roki.ui.adapter3.RvDeviceBluetoothAdapter;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.form.RecipeActivity;
import com.robam.roki.ui.form.RecipeNoDeviceActivity;
import com.robam.roki.ui.form.RecipePotActivity;
import com.robam.roki.ui.form.RecipeRRQZActivity;
import com.robam.roki.ui.view.HomeDeviceView;
import com.robam.roki.ui.page.mine.HomeMineView;
import com.robam.roki.ui.view.HomeRecipeView;
import com.robam.roki.ui.view.HomeRecipeView32;
import com.robam.roki.ui.view.HomeTabView;
import com.robam.roki.ui.view.networkoptimization.BleConnectActivity;
import com.robam.roki.ui.view.umpush.PushContent;
import com.robam.roki.ui.view.umpush.UMPushMsg;
import com.robam.roki.ui.widget.base.BaseDialog;
import com.robam.roki.ui.widget.layout.NoScrollViewPager;
import com.robam.roki.utils.DateUtil;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.PermissionsUtils;
import com.robam.roki.utils.UploadLogFileUtil;
import com.robam.roki.utils.bubble.BubbleDialog;
import com.robam.roki.utils.suspendedball.FloatingMagnetView;
import com.robam.roki.utils.suspendedball.FloatingView;
import com.robam.roki.utils.suspendedball.MagnetViewListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.com.heaton.blelibrary.ble.Ble;
import cn.com.heaton.blelibrary.ble.callback.BleConnectCallback;
import cn.com.heaton.blelibrary.ble.callback.BleReadCallback;
import cn.com.heaton.blelibrary.ble.callback.BleScanCallback;
import cn.com.heaton.blelibrary.ble.callback.BleWriteCallback;
import cn.com.heaton.blelibrary.ble.model.ScanRecord;
import cn.com.heaton.blelibrary.ble.utils.Utils;
import cn.com.heaton.blelibrary.ble.utils.UuidUtils;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.IFloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;
import com.yhao.floatwindow.ViewStateListenerAdapter;

/**
 * Created by sylar on 15/6/4.
 */
public class HomePage extends AbsDUIPage {

    static public final int TAB_DEVICE = 0;
    static public final int TAB_RECIPE = 1;
    static public final int TAB_IP = 2;
    static public final int TAB_PERSONAL = 3;
    @InjectView(R.id.iv_mask)
    ImageView mIvMask;
    Adapter adapter;
    @InjectView(R.id.pager)
    NoScrollViewPager pager;
    @InjectView(R.id.tabView)
    HomeTabView tabView;
    @InjectView(R.id.rl_dot_container)
    RelativeLayout rlDotContainer;
    @InjectView(R.id.user_dot)
    TextView userDot;

    /**
     * '
     * 用户主页View
     */
    private HomeMineView homeMineView;
    private HomeRecipeView32 homeRecipeView32;


    private Ble<BleRssiDevice> ble = Ble.getInstance();
    private List<BleRssiDevice> bleRssiDevices = new ArrayList<>(); //搜索到的蓝牙列表   rssi为信号强度
    private List<BluetoothGattService> gattServices = new ArrayList<>();//蓝牙服务
    /**
     * 蓝牙设备adapter
     */
    private RvDeviceBluetoothAdapter rvDeviceAdapter;
    /**
     * dialog
     */
    private BaseDialog baseDialog;


    /**
     * 蓝牙搜索回调
     */
    private BleScanCallback<BleRssiDevice> scanCallback = new BleScanCallback<BleRssiDevice>() {
        @Override
        public void onLeScan(final BleRssiDevice device, int rssi, byte[] scanRecord) {
            synchronized (ble.getLocker()) {
//                    for (int i = 0; i < bleRssiDevices.size(); i++) {
//                        BleRssiDevice rssiDevice = bleRssiDevices.get(i);
//                        if (TextUtils.equals(rssiDevice.getBleAddress(), device.getBleAddress())){
//                            return;
//                        }
//                    }
                for (BleRssiDevice rssiDevice : bleRssiDevices
                ) {
                    if (rssiDevice.getBleAddress().equals(device.getBleAddress())) {
                        if (rssi < 30) {
                            return;
                        } else {
                            return;
                        }
                    }
                }
                device.setScanRecord(ScanRecord.parseFromBytes(scanRecord));
                device.setRssi(rssi);
                if (device.getBleName() != null && device.getBleName().contains("ROBAM_RC906")) {
                    bleRssiDevices.add(device);
                    deviceBluetoothDialog();
                    if (rvDeviceAdapter != null) {
                        rvDeviceAdapter.addData(device);
                    }
                }
            }
        }


        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.e("yidao", errorCode + "");
        }
    };


    /**
     * 接收思必驰Native方法调回设备页
     */
    @Subscribe
    public void onEvent(ReturnDeviceViewEvent returnDeviceViewEvent) {

        if (returnDeviceViewEvent.getReturn()) {
            if (pager == null) {
                return;
            }
            if (TAB_DEVICE != pager.getCurrentItem()) {
                if (tabView != null) {
                    tabView.setBackgroundDrawable(null);
                }

                if (tabView != null) {
                    tabView.setOnTabSelectedCallback(tabCallback);
                    tabView.selectTab(TAB_DEVICE);
                }
            }

        }

    }


//    @Override
//    protected void duiNotifyItemInserted() {
//        mDialogAdapter.notifyItemInserted(mMessageList.size());
//        super.duiNotifyItemInserted();
//    }

//    @Override
//    protected void duiNotifyDataSetChanged() {
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                mDialogAdapter.notifyDataSetChanged();
//            }
//        });
//        super.duiNotifyDataSetChanged();
//    }

//    @Override
//    protected void setEnabled(final int type) {
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (type == DuiUpdateObserver.START) {
////                    FloatingView.get().getView().setEnabled(false);
//                } else if (type == DuiUpdateObserver.FINISH) {
////                    FloatingView.get().getView().setEnabled(true);
//                }
//            }
//        });
//        super.setEnabled(type);
//    }


    BubbleDialog mCurrentDialog;
    private BubbleDialog.Position mPosition = BubbleDialog.Position.TOP;

    //    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.page_home, container, false);
//
//
//        ButterKnife.inject(this, view);
//        adapter = new Adapter();
//        adapter.loadViews(buildViews());
//        pager.setAdapter(adapter);
//        pager.setOffscreenPageLimit(adapter.getCount());
//        if (tabView != null) {
//            tabView.setOnTabSelectedCallback(tabCallback);
//            tabView.selectTab(TAB_RECIPE);
//        }
//
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            int selfPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
////            if (selfPermission == 0) {
////                checkBlueStatus();
////            } else {
////                PermissionsUtils.checkPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION, PermissionsUtils.CODE_BLUE_TOOTH);
////            }
////        } else {
////            checkBlueStatus();
////        }
//        return view;
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.page_home;
    }

    @Override
    protected void initData() {
        adapter = new Adapter();
        adapter.loadViews(buildViews());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(adapter.getCount());
        if (tabView != null) {
            tabView.setOnTabSelectedCallback(tabCallback);
            tabView.selectTab(TAB_RECIPE);
        }
        //日志上传
        try {
            String currentDate = DateUtil.getCurrentDate();
            boolean bool = PreferenceUtils.getBool(currentDate, false);
            if (!bool && Plat.accountService.isLogon()){
                UploadLogFileUtil.uploadLog(activity);
            }
        }catch (Exception e){
            e.getMessage();
        }

        try {
         if(!TextUtils.isEmpty(MobApp.id)){
            SelectThemeDetailPage.show(Long.parseLong(MobApp.id), TYPE_THEME_BANNER);
            MobApp.id=null;
         }
        }catch (Exception e){
            Log.e("错误",e.getMessage());
        }

    }


    /**
     * 检测蓝牙是否打开
     */
    private void checkBlueStatus() {
        if (!ble.isSupportBle(cx)) {
//            finish();
        }
        if (!ble.isBleEnable()) {
            ToastUtils.showShort("请打开蓝牙！");
        } else {
            checkGpsStatus();
        }
    }

    private void checkGpsStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !Utils.isGpsOpen(activity)) {
            new AlertDialog.Builder(activity)
                    .setTitle("提示")
                    .setMessage("为了更精确的扫描到Bluetooth LE设备,请打开GPS定位")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            activity.startActivityForResult(intent, PermissionsUtils.CODE_WIFI_SSID);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create()
                    .show();
        } else {
            ble.startScan(scanCallback);
        }
    }

    //初始化友盟推送获取数据，要发送的页面
//    private void initUmPushMsgPage() {
//        //友盟推送跳转
//        if (UMPushMsg.getMsgType() != 0) {
//            if (UMPushMsg.getMsgType() == PushContent.PUSHCONINFOMATION) {
//                EventUtils.postEvent(new UMPushInfoEvent(true));
//                getToConsultationList();//咨询
//                UMPushMsg.setMsgId(UMPushMsg.getMsgId());
//                UMPushMsg.setMsgType(UMPushMsg.getMsgType());
//            } else if (UMPushMsg.getMsgType() == PushContent.PUSHTHEME) {
//                EventUtils.postEvent(new UMPushThemeEvent());
//                getToTheTheme();//主题
//                UMPushMsg.setMsgId(UMPushMsg.getMsgId());
//                UMPushMsg.setMsgType(UMPushMsg.getMsgType());
//            } else if (UMPushMsg.getMsgType() == PushContent.PUSHRECIPE) {
//                EventUtils.postEvent(new UMPushRecipeEvent(true));//菜谱
//                UMPushMsg.setMsgId(UMPushMsg.getMsgId());
//                UMPushMsg.setMsgType(UMPushMsg.getMsgType());
//            } else if (UMPushMsg.getMsgType() == PushContent.PUSHVIDEO) {//视频
//                EventUtils.postEvent(new UMPushVideoEvent(true));
//                UMPushMsg.setMsgId(UMPushMsg.getMsgId());
//                UMPushMsg.setMsgType(UMPushMsg.getMsgType());
//            }
//        }
//    }

    /**
     * 获取资讯列表
     */
//    private void getToConsultationList() {
//        CookbookManager.getInstance().getConsultationList(new Callback<List<RecipeConsultation>>() {
//            @Override
//            public void onSuccess(List<RecipeConsultation> list) {
//                if (list == null || list.size() == 0) {
//                    return;
//                }
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(PageArgumentKey.RecipeConsultation, (Serializable) list);
//                UIService.getInstance().postPage(PageKey.ConsultationList, bundle);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//            }
//        });
//    }

    /**
     * 跳转到主题界面
     */
//    private void getToTheTheme() {
//        CookbookManager.getInstance().getThemeRecipes_new(new Callback<List<RecipeTheme>>() {
//            @Override
//            public void onSuccess(List<RecipeTheme> themes) {
//                if (themes == null || themes.size() == 0) {
//                    return;
//                }
//                Bundle bundle = new Bundle();
//
//                for (int i = 0; i < themes.size(); i++) {
//                    RecipeTheme theme = themes.get(i);
//                    bundle.putSerializable(PageArgumentKey.Theme, theme);
//                }
//                UIService.getInstance().postPage(PageKey.RecipeThemePushDetail, bundle);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//            }
//        });
//    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
//        mTimer.cancel();
//        task.cancel();
    }

//    @Subscribe
//    public void onEvent(UMPushInfoEvent event) {
//        if (event.b) {
//            getToConsultationList();
//            EventUtils.postEvent(new UMPushInfoEvent(false));
//        }
//    }

//    @Subscribe
//    public void onEvent(UMPushRecipeEvent event) {
//        if (event.b) {
//            RecipeDetailPage.show(UMPushMsg.getMsgId(), RecipeDetailPage.unKnown,
//                    RecipeRequestIdentification.RECIPE_INDIVIDUATION_RECOMMEND);//跳转到指定菜谱界面
//            EventUtils.postEvent(new UMPushRecipeEvent(false));
//        }
//    }
//
//    @Subscribe
//    public void onEvent(UMPushThemeEvent event) {
//        getToTheTheme();
//    }

//    @Subscribe
//    public void onEvent(UMPushVideoEvent event) {
//        if (event.b) {
//            UIService.getInstance().postPage(PageKey.RecipeLiveList);//跳转到视频页面
//            EventUtils.postEvent(new UMPushVideoEvent(false));
//        }
//    }

//    @Subscribe
//    public void onEvent(RecipeShowEvent event) {      //处理跳转到菜谱主页的消息
//        tabView.selectTab(TAB_RECIPE);
//    }

    @Subscribe
    public void onEvent(UserLoginEvent event) {
        adapter.refresh();
    }

    @Subscribe
    public void onEvent(UserLogoutEvent event) {
        adapter.refresh();
    }

    @Subscribe
    public void onEvent(DeviceEasylinkCompletedEvent event) {
        tabView.selectTab(TAB_DEVICE);
    }

    @Subscribe
    public void onEvent(ChatNewMsgEvent event) {
        boolean hasNew = event.hasNew;
        if (hasNew) {
            userDot.setVisibility(View.VISIBLE);
        } else {
            userDot.setVisibility(View.GONE);
        }
    }

    List<View> buildViews() {
        List<View> views = Lists.newArrayList();
        views.add(new HomeDeviceView(cx, activity));
        homeRecipeView32 = new HomeRecipeView32(cx, activity);
        views.add(homeRecipeView32);
//        views.add(new HomeIpView(cx));
        homeMineView = new HomeMineView(cx, activity);
        views.add(homeMineView);
        return views;
    }


    HomeTabView.OnTabSelectedCallback tabCallback = new HomeTabView.OnTabSelectedCallback() {
        @Override
        public void onTabSelected(final int tabIndex) {
            if (tabView != null) {
                tabView.setBackgroundDrawable(null);
            }
            if (pager != null) {
                pager.setCurrentItem(tabIndex, true);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                View decorView = activity.getWindow().getDecorView();
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                activity.getWindow().setStatusBarColor(getResources().getColor(R.color.white));
                StatusBarUtils.setTextDark(getContext(), true);
            }
            switch (tabIndex) {
                case TAB_DEVICE:
//                  setRootBgRes(R.color.main_background);
                    tabView.setBackgroundResource(R.color.white);
                    String newBie = PreferenceUtils.getString("newBie", null);
                    LogUtils.i("20180823", " newBie:" + newBie);
                    if (Plat.accountService.isLogon() && DeviceService.getInstance().queryAll().size() > 0) {
                        mIvMask.setVisibility(View.GONE);
                        PreferenceUtils.setString("newBie", "new");
                    } else if (newBie == null && DeviceService.getInstance().queryAll().size() == 0) {
                        mIvMask.setVisibility(View.VISIBLE);
                        PreferenceUtils.setString("newBie", "new");
                    }

                    break;
                case TAB_RECIPE:
//                   setRootBgRes(R.color.main_background);
                    tabView.setBackgroundResource(R.color.white);
                    mIvMask.setVisibility(View.GONE);
//                    homeRecipeView3.upData();
                    break;
                case TAB_IP:
                    //R.color.White_90
                    tabView.setBackgroundResource(R.color.white);
                    mIvMask.setVisibility(View.GONE);
                    break;
                case TAB_PERSONAL:
                    //R.color.White_90
                    tabView.setBackgroundResource(R.color.white);
                    mIvMask.setVisibility(View.GONE);

                    if (Build.VERSION.SDK_INT >= 21) {
                        View decorView = activity.getWindow().getDecorView();
                        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                        activity.getWindow().setStatusBarColor(getResources().getColor(R.color.white));
                        StatusBarUtils.setTextDark(getContext(), true);
                    }
                    homeMineView.getUser();
                    break;
                default:
                    break;
            }
        }
    };


    @OnClick(R.id.iv_mask)
    public void onViewClicked() {
        mIvMask.setVisibility(View.GONE);
        EventUtils.postEvent(new NewBieGuideEvent());
    }


    class Adapter extends ExtPageAdapter {
        void refresh() {
            for (int i = 0; i < list.size(); i++) {
                refresh(i);
            }
        }

        void refresh(int tabIndex) {
            View view = list.get(tabIndex);
            if (view instanceof UIListeners.IRefresh) {
                UIListeners.IRefresh refView = (UIListeners.IRefresh) view;
//                refView.onRefresh();
            }
        }

    }

    public class HomeTabSelectedEvent {
        public int tabIndex;

        public HomeTabSelectedEvent(int tabIndex) {
            this.tabIndex = tabIndex;
        }
    }

    private final static int SCANNIN_GREQUEST_CODE = 100;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (PermissionsUtils.CODE_HOME_DEVICE_CAMERA == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Activity atv = UIService.getInstance().getTop().getActivity();
                    Intent intent = new Intent();
                    intent.setClass(atv, ScanQrActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    atv.startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                }
            }
        }

    }

    /**
     *
     */
    public void connectDevice(BleRssiDevice device) {
        if (bleRssiDevices.size() == 0) {
            return;
        }
        /**
         * 连接蓝牙
         */
        ble.connect(device, new BleConnectCallback<BleRssiDevice>() {
            @Override
            public void onConnectionChanged(BleRssiDevice device) {
                if (device.isConnected()) {
                    ToastUtils.showShort("已连接");
                    onSend(device);
//                    UIService.getInstance().postPage(PageKey.DeviceWifiConnect, null);
//                    onSend(device);
                } else if (device.isConnecting()) {
                    ToastUtils.showShort("连接中...");
                } else if (device.isDisconnected()) {
                    ToastUtils.showShort("未连接");
                }
            }

            @Override
            public void onServicesDiscovered(BleRssiDevice device, final BluetoothGatt gatt) {
                super.onServicesDiscovered(device, gatt);
                gatt.requestMtu(200);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gattServices.clear();
                        gattServices.addAll(gatt.getServices());
                    }
                });
            }
        });
    }

    /**
     * 显示设备
     */
    private void deviceBluetoothDialog() {
        if (!PreferenceUtils.getBool("isShowBluetoothDevice", true)) {
            return;
        }
        if (baseDialog == null) {
            baseDialog = new BaseDialog(cx);
            baseDialog.setContentView(R.layout.dialog_device_bluetooth);
            baseDialog.setCanceledOnTouchOutside(true);
            baseDialog.setGravity(Gravity.BOTTOM);
            baseDialog.setWidth(activity.getWindowManager().getDefaultDisplay().getWidth());
            RecyclerView rvDevice = (RecyclerView) baseDialog.findViewById(R.id.rv_device);
            TextView tv_not_tips = (TextView) baseDialog.findViewById(R.id.tv_not_tips);
            ImageView iv_close = (ImageView) baseDialog.findViewById(R.id.iv_close);
            rvDevice.setLayoutManager(new LinearLayoutManager(cx, LinearLayoutManager.HORIZONTAL, false));
            rvDeviceAdapter = new RvDeviceBluetoothAdapter();
            rvDeviceAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    BleRssiDevice item = rvDeviceAdapter.getItem(position);
                    if (item.getBleName().contains("_1")) {
                        //连接并配网
                        connectDevice(item);
                    } else if (item.getBleName().contains("_0")) {

                    }

                }
            });
            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    baseDialog.dismiss();
                }
            });
            tv_not_tips.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    baseDialog.dismiss();
                    isShowBluetoothDeviceDialog();

                }
            });
            rvDevice.setAdapter(rvDeviceAdapter);
            baseDialog.show();
        } else {
            if (!baseDialog.isShowing()) {
                baseDialog.show();
            }
        }

    }

    /**
     * 关闭弹窗提示
     */
    private void isShowBluetoothDeviceDialog() {
        final IRokiDialog dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_26);
        dialog.setTitleText("关闭设备联网提示");
        dialog.setCancelable(false);
        dialog.show();
        dialog.setCancelBtn("始终", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtils.setBool("isShowBluetoothDevice", false);
                dialog.dismiss();
            }
        });
        dialog.setOkBtn("仅本次", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
//    @Subscribe
//    public void onEvent(DeviceFindEvent deviceFindEvent) {
//        DeviceInfo deviceInfo = deviceFindEvent.deviceInfo;
//        Log.e("yidao", "onEvent: DeviceFindEvent"+deviceInfo.guid );
//        addKettle(deviceInfo);
//
//    }
//    private void addKettle(final DeviceInfo devInfo) {
//        try {
//            devInfo.ownerId = Plat.accountService.getCurrentUserId();
//            if (Strings.isNullOrEmpty(devInfo.name)) {
//                DeviceType dt = DeviceTypeManager.getInstance().getDeviceType(devInfo.guid);
//                if (dt != null) {
//                    devInfo.name = dt.getName();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Log.e("yidao", "addKettle: "+devInfo.name);
//        Log.e("yidao", "addKettle: "+devInfo.guid);
//        Plat.deviceService.addWithBind(devInfo.guid, devInfo.name,
//                true, new VoidCallback() {
//
//                    @Override
//                    public void onSuccess() {
//                        EventUtils.postEvent(new DeviceEasylinkCompletedEvent(devInfo));
//                        // UIService.getInstance().returnHome();
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        } finally {
//                            UIService.getInstance().popBack().popBack().popBack();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                    }
//                });
//
//    }

    public void onSend(BleRssiDevice device) {
        BluetoothGattCharacteristic writeChar = null;
        BluetoothGattCharacteristic readChar = null;
        for (int i = 0; i < gattServices.size(); i++) {
            if (gattServices.get(i).getCharacteristic(UUID.fromString(UuidUtils.uuid16To128("fed7"))) != null) {
                writeChar = gattServices.get(i).getCharacteristic(UUID.fromString(UuidUtils.uuid16To128("fed7")));
            }

            if (gattServices.get(i).getCharacteristic(UUID.fromString(UuidUtils.uuid16To128("fed8"))) != null) {
                readChar = gattServices.get(i).getCharacteristic(UUID.fromString(UuidUtils.uuid16To128("fed8")));
            }
        }
        if (writeChar != null) {
            Map<String, String> wifiInfoMap = new HashMap<String, String>();
            wifiInfoMap.put("userid", Plat.accountService.getCurrentUserId() + "");
            wifiInfoMap.put("apptype", "RKIOS");
            wifiInfoMap.put("appid", CommonService.getInstance().getAppId());
            wifiInfoMap.put("ssid", "物联研究院");
            wifiInfoMap.put("pwd", "rokitest2021");

            Gson gson = new Gson();
            String wifiInfoGson = gson.toJson(wifiInfoMap);
            Log.e("onEvent", "onSend: " + wifiInfoGson);
            final BluetoothGattCharacteristic read = readChar;
            /**
             * 给设备写入消息
             */
            ble.writeByUuid(device, wifiInfoGson.getBytes(), writeChar.getService().getUuid(), writeChar.getUuid(), new BleWriteCallback<BleRssiDevice>() {
                @Override
                public void onWriteSuccess(BleRssiDevice device, BluetoothGattCharacteristic characteristic) {
                    Log.e("onEvent", "onWriteSuccess: ");
                    if (read != null) {
                        Log.e("onEvent", "read: ");
                        ble.readByUuid(device, read.getService().getUuid(), read.getUuid(), new BleReadCallback<BleRssiDevice>() {
                            @Override
                            public void onReadSuccess(BleRssiDevice dedvice, BluetoothGattCharacteristic characteristic) {
                                super.onReadSuccess(dedvice, characteristic);
                                Log.e("onEvent", "onReadSuccess: ");
                            }

                            @Override
                            public void onReadFailed(BleRssiDevice device, int failedCode) {
                                super.onReadFailed(device, failedCode);
                                Log.e("onEvent", "onReadFailed: ");
                            }
                        });
                    }
                }

                @Override
                public void onWriteFailed(BleRssiDevice device, int failedCode) {
                    Log.e("onEvent", "onWriteFailed: ");
                }
            });
        }
    }

    private void addDeviceaddDevice(String guid) {
        DeviceInfo info = new DeviceInfo();
        info.ownerId = Plat.accountService.getCurrentUserId();
        info.name = DeviceTypeManager.getInstance().getDeviceType(
                guid).getName();
        info.guid = guid;
        Plat.deviceService.addWithBind(info.guid, info.name,
                true, new VoidCallback() {

                    @Override
                    public void onSuccess() {
                        ToastUtils.showShort("添加完成");
                        EventUtils.postEvent(new DeviceEasylinkCompletedEvent(info));
                        UIService.getInstance().returnHome();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.showThrowable(t);
                    }
                });

    }

    private MultiTransformation options = new MultiTransformation<>(new CenterCrop(),
            new RoundedCornersTransformation(999, 0));

    @Subscribe
    public void onEvent(FloatHelperEvent event) {
        try {
            View view;
            IFloatWindow recipeCook = FloatWindow.get("RecipeCook");
            if (event.pageKey == 99) {
                if (event.isHiden) {
                    FloatWindow.get("RecipeCook").hide();
                } else {
                    FloatWindow.get("RecipeCook").show();
                }
                return;
            }
            if (recipeCook != null) {
                view = recipeCook.getView();
                ImageView ivFloat = (ImageView) view.findViewById(R.id.iv_float);
                GlideApp.with(cx)
                        .load(event.url)
                        .placeholder(R.mipmap.icon_recipe_default)
                        .error(R.mipmap.icon_recipe_default)
                        .priority(Priority.HIGH)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .apply(RequestOptions.bitmapTransform(options))
                        .into(ivFloat);
            } else {
                view = activity.getLayoutInflater().inflate(R.layout.float_window, null);
                ImageView ivFloat = (ImageView) view.findViewById(R.id.iv_float);
                LinearLayout ll_float = (LinearLayout) view.findViewById(R.id.ll_float);
                GlideApp.with(cx)
                        .load(event.url)
                        .placeholder(R.mipmap.icon_recipe_default)
                        .error(R.mipmap.icon_recipe_default)
                        .priority(Priority.HIGH)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .apply(RequestOptions.bitmapTransform(options))
                        .into(ivFloat);

                FloatWindow
                        .with(MobApp.getInstance())
                        .setView(view)
                        .setTag("RecipeCook")
                        .setWidth(220)
                        .setHeight(220)
                        .setX(Screen.width)                                   //设置控件初始位置
                        .setY(Screen.height, 0.3f)
                        .setFilter(true, MainActivity.class)
                        .setMoveType(MoveType.slide)
                        .setDesktopShow(true)//显示桌面
                        .setViewStateListener(new ViewStateListener() {
                            @Override
                            public void onPositionUpdate(int x, int y) {
                                Log.d("TAG", "onPositionUpdate: x=" + x + " y=" + y);
                                ll_float.setBackgroundResource(R.drawable.shape_bg_f5f5f8_999dp);
                            }

                            @Override
                            public void onShow() {
                                Log.d("TAG", "onShow");
                            }

                            @Override
                            public void onHide() {
                                Log.d("TAG", "onHide");
                            }

                            @Override
                            public void onDismiss() {
                                Log.d("TAG", "onDismiss");
                            }

                            @Override
                            public void onMoveAnimStart() {
                                Log.d("TAG", "onMoveAnimStart");
                            }

                            @Override
                            public void onMoveAnimEnd() {
                                Log.d("TAG", "onMoveAnimEnd");
                                if(FloatWindow.get("RecipeCook")==null){
                                   return;
                                }
                                float x= FloatWindow.get("RecipeCook").getX();
                                if (x == 0) {
                                    ll_float.setBackgroundResource(R.drawable.shape_bg_f5f5f8_left);
                                }else{
                                    ll_float.setBackgroundResource(R.drawable.shape_bg_f5f5f8_right);
                                }
                            }

                            @Override
                            public void onBackToDesktop() {
                                Log.d("TAG", "onBackToDesktop");
                            }
                        })
                        .build();
                FloatWindow.get("RecipeCook").show();

            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (event.form == 0) {
                        RecipeNoDeviceActivity.start(getActivity(), event.id, event.bd, "", event.step, event.url);
                    } else if (event.form == 1) {
                        RecipeActivity.start(getActivity(), event.id, event.bd, event.guid, event.step, event.url);
                    } else if (event.form == 2) {
                        RecipeRRQZActivity.start(getActivity(), event.id, event.bd, event.guid, event.step, event.url);
                    } else if (event.form == 4) {
                        com.robam.roki.ui.page.recipedetail.RecipeDetailPage.show(event.id, event.pageKey);
                    } else if (event.form == 5) {
                        UIService.getInstance().postPage(PageKey.RandomRecipe);
                    }
                    try {
                        FloatWindow.destroy("RecipeCook");
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
            });

        } catch (Exception e) {
            e.getMessage();
        }
    }

    private ViewStateListener mViewStateListener = new ViewStateListener() {
        @Override
        public void onPositionUpdate(int x, int y) {
            Log.d("TAG", "onPositionUpdate: x=" + x + " y=" + y);
        }

        @Override
        public void onShow() {
            Log.d("TAG", "onShow");
        }

        @Override
        public void onHide() {
            Log.d("TAG", "onHide");
        }

        @Override
        public void onDismiss() {
            Log.d("TAG", "onDismiss");
        }

        @Override
        public void onMoveAnimStart() {
            Log.d("TAG", "onMoveAnimStart");
        }

        @Override
        public void onMoveAnimEnd() {
            Log.d("TAG", "onMoveAnimEnd");
        }

        @Override
        public void onBackToDesktop() {
            Log.d("TAG", "onBackToDesktop");
        }
    };
}

