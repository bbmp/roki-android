package com.robam.roki.ui.page;

import static com.robam.roki.ui.page.SelectThemeDetailPage.TYPE_THEME_BANNER;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.google.android.material.tabs.TabLayout;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;

import com.legent.VoidCallback;
import com.legent.events.BlueLoginSuccessEvent;
import com.legent.events.PageChangedEvent;
import com.legent.plat.Plat;
import com.legent.plat.events.BlueCloseEvent;
import com.legent.plat.events.ChatNewMsgEvent;
import com.legent.plat.events.FloatHelperEvent;
import com.legent.plat.events.MessageEvent;
import com.legent.plat.events.UserLoginEvent;
import com.legent.plat.events.UserLogoutEvent;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.pojos.dictionary.DeviceType;
import com.legent.plat.services.DeviceService;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.ui.UIService;
import com.legent.ui.ext.adapters.ExtPageAdapter;
import com.legent.ui.ext.utils.StatusBarCompat;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.qrcode.ScanQrActivity;
import com.robam.base.BaseDialog;
import com.robam.common.events.DeviceEasylinkCompletedEvent;
import com.robam.common.events.NewBieGuideEvent;
import com.robam.common.events.ReturnDeviceViewEvent;
import com.robam.common.util.StatusBarUtils;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.FormKey;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.UIListeners;
import com.robam.roki.ui.adapter3.RvDeviceBluetoothAdapter;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.form.RecipeActivity;
import com.robam.roki.ui.form.RecipeNoDeviceActivity;
import com.robam.roki.ui.form.RecipeRRQZActivity;
import com.robam.roki.ui.page.login.helper.CmccLoginHelper;
import com.robam.roki.ui.page.mine.HomeMineView;
import com.robam.roki.ui.view.HomeDeviceView;
import com.robam.roki.ui.view.HomeRecipeView32;
import com.robam.roki.ui.view.HomeTabView;
import com.robam.roki.utils.DateUtil;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.PermissionsUtils;
import com.robam.roki.utils.UploadLogFileUtil;
import com.robam.roki.utils.bubble.BubbleDialog;
import com.robam.widget.layout.NoScrollViewPager;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.IFloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.com.heaton.blelibrary.ble.utils.Utils;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import skin.support.content.res.SkinCompatResources;
import skin.support.utils.SkinStatusBarUtils;

/**
 * Created by sylar on 15/6/4.
 */
public class HomePage extends AbsDUIPage {

    static public final int TAB_DEVICE = 0;
    static public final int TAB_RECIPE = 1;
    static public final int TAB_IP = 2;
    static public final int TAB_PERSONAL = 3;
//    @InjectView(R.id.iv_mask)
//    ImageView mIvMask;
    Adapter adapter;
    @InjectView(R.id.pager)
    NoScrollViewPager pager;
//    @InjectView(R.id.tabView)
//    HomeTabView tabView;
    @InjectView(R.id.rl_dot_container)
    RelativeLayout rlDotContainer;
    @InjectView(R.id.user_dot)
    TextView userDot;
    @InjectView(R.id.tabLayout)
    TabLayout tabLayout;

    /**
     * '
     * 用户主页View
     */
    private HomeMineView homeMineView;
    private HomeRecipeView32 homeRecipeView32;

    private List<BleDevice> bleRssiDevices = new ArrayList<>(); //搜索到的蓝牙列表   rssi为信号强度
    private List<BluetoothGattService> gattServices = new ArrayList<>();//蓝牙服务
    /**
     * 蓝牙设备adapter
     */
    private RvDeviceBluetoothAdapter rvDeviceAdapter;
    /**
     * dialog
     */
    private BaseDialog baseDialog;
    private BaseDialog blueSetDialog;
    //获取设备联网列表
//    List<List<DeviceItemList>> sumDeviceList = new ArrayList<List<DeviceItemList>>();

    /*
     *每5秒重启一次蓝牙扫描，蓝牙名字需要重启才能变化_0,_1


    /**
     * 蓝牙搜索回调
     */
    private Timer mTimer;
    private TimerTask mTimerTask;
    private boolean firstScan = true;

    private void timeLoop() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (BleManager.getInstance().isBlueEnable()) {
                    if (firstScan) {
                        firstScan = false;
                    } else {
                        BleManager.getInstance().cancelScan();
                        BleManager.getInstance().destroy();
                    }
                    checkBlueStatus();
                }
            }
        };
        mTimer.schedule(mTimerTask, 15 * 1000, 15 * 1000);
    }


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
//                if (tabView != null) {
//                    tabView.setBackgroundDrawable(null);
//                }
//
//                if (tabView != null) {
//                    tabView.setOnTabSelectedCallback(tabCallback);
//                    tabView.selectTab(TAB_DEVICE);
//                }
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
    protected void initView() {
        super.initView();
        tabLayout.setSelectedTabIndicatorHeight(0);
        TabLayout.Tab deviceTab = tabLayout.newTab();

        View deviceView = LayoutInflater.from(getContext()).inflate(R.layout.view_home_tab_item, null);
        ImageView deviceImage = deviceView.findViewById(R.id.imgTab);
        deviceImage.setImageResource(R.drawable.ic_home_tab_device);
        TextView deviceTv = deviceView.findViewById(R.id.txtTab);
        deviceTv.setText(R.string.home_tab_device);
        deviceTab.setCustomView(deviceView);
        tabLayout.addTab(deviceTab);

        TabLayout.Tab recipeTab = tabLayout.newTab();

        View recipeView = LayoutInflater.from(getContext()).inflate(R.layout.view_home_tab_item, null);
        ImageView recipeImage = recipeView.findViewById(R.id.imgTab);
        recipeImage.setImageResource(R.drawable.ic_home_tab_recipe);
        TextView recipeTv = recipeView.findViewById(R.id.txtTab);
        recipeTv.setText(R.string.home_tab_recipe);
        recipeTab.setCustomView(recipeView);
        tabLayout.addTab(recipeTab);

        TabLayout.Tab mineTab = tabLayout.newTab();

        View mineView = LayoutInflater.from(getContext()).inflate(R.layout.view_home_tab_item, null);
        ImageView mineImage = mineView.findViewById(R.id.imgTab);
        mineImage.setImageResource(R.drawable.ic_home_tab_personal);
        TextView mineTv = mineView.findViewById(R.id.txtTab);
        mineTv.setText(R.string.home_tab_personal);
        mineTab.setCustomView(mineView);
        tabLayout.addTab(mineTab);
    }

    @Override
    protected void initData() {
        adapter = new Adapter();
        adapter.loadViews(buildViews());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(adapter.getCount());
//        if (tabView != null) {
//            tabView.setOnTabSelectedCallback(tabCallback);
//            tabView.selectTab(TAB_RECIPE);
//        }
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        pager.setCurrentItem(TAB_RECIPE);
        //日志上传
        try {
            String currentDate = DateUtil.getCurrentDate();
            boolean bool = PreferenceUtils.getBool(currentDate, false);
            if (!bool && Plat.accountService.isLogon()) {
                UploadLogFileUtil.uploadLog(activity);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        if (!BleManager.getInstance().isBlueEnable()) {
//            ToastUtils.showShort("请打开蓝牙！");
            openBlueset();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int selfPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
            if (selfPermission == 0) {
                checkBlueStatus();
                timeLoop();
            } else {
                PermissionsUtils.checkPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION, PermissionsUtils.CODE_BLUE_TOOTH);
            }
        } else {
            checkBlueStatus();
            timeLoop();
        }
        PreferenceUtils.setBool("isShowBluetoothDevice", true);

        //获取设备联网列表
//        RokiRestHelper.getNetworkDeviceInfoRequest("roki", null, null, new Callback<List<DeviceGroupList>>() {
//            @Override
//            public void onSuccess(List<DeviceGroupList> deviceGroupLists) {
//                for (int i = 0; i < deviceGroupLists.size(); i++) {
//                    sumDeviceList.add(deviceGroupLists.get(i).getDeviceItemLists());
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//            }
//        });



    }


    /**
     * 检测蓝牙是否打开
     */
    private void checkBlueStatus() {

        if (!BleManager.getInstance().isBlueEnable()) {
            openBlueset();

        } else {
            checkGpsStatus();
        }

    }

    private void openBlueset() {
        if (blueSetDialog == null) {
            blueSetDialog = new BaseDialog(cx);
            blueSetDialog.setContentView(R.layout.dialog_open_bluetooth);
            blueSetDialog.setCanceledOnTouchOutside(true);
            blueSetDialog.setGravity(Gravity.CENTER_VERTICAL);
            blueSetDialog.setWidth(activity.getWindowManager().getDefaultDisplay().getWidth());
            TextView tv_cancel = (TextView) blueSetDialog.findViewById(R.id.tv_cancel);
            TextView tv_set = (TextView) blueSetDialog.findViewById(R.id.tv_set);

            blueSetDialog.setCancelable(false);

            tv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    blueSetDialog.dismiss();

                }
            });
            tv_set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                    activity.startActivity(intent);
                    blueSetDialog.dismiss();
                }
            });
            blueSetDialog.show();
        } else {
            if (!blueSetDialog.isShowing()) {
                blueSetDialog.show();
            }
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
            /**
             * 蓝牙搜索回调
             */
            BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                    .setAutoConnect(false)      // 连接时的autoConnect参数，可选，默认false
                    .setScanTimeOut(8000)              // 扫描超时时间，可选，默认10秒
                    .build();
            BleManager.getInstance().initScanRule(scanRuleConfig);
            BleManager.getInstance().scan(new com.clj.fastble.callback.BleScanCallback() {
                @Override
                public void onScanStarted(boolean success) {
                }

                @Override
                public void onLeScan(com.clj.fastble.data.BleDevice bleDevice) {
                    if (bleDevice.getName() != null) {
                        Log.d("20220105", "onLeScan:  " + bleDevice.getName() + "  Rssi:" + bleDevice.getRssi() + "  mac:" + bleDevice.getMac());
                    }
                    if (!isHomePage) {
                        return;
                    }
                    if (bleDevice.getName() != null && bleDevice.getName().contains("ROBAM_") && bleDevice.getName().contains("_0")) {
                        Log.d("20220105", "ROBAM_onLeScan:  " + bleDevice.getName() + "  Rssi:" + bleDevice.getRssi());
                        if (bleRssiDevices.isEmpty()) {
                            if (bleDevice.getRssi() > -80) {
                                bleRssiDevices.add(bleDevice);
                                deviceBluetoothDialog();
                                if (rvDeviceAdapter != null) {
                                    rvDeviceAdapter.addData(bleDevice);
//                                    rvDeviceAdapter.addData(setBlueDeviceIcon(bleDevice));
                                }
                            }
                        } else {
                            boolean isExist = false;
                            for (int i = 0; i < bleRssiDevices.size(); i++) {
                                if (bleRssiDevices.get(i).getMac().equals(bleDevice.getMac())) {
                                    isExist = true;
                                    break;
                                }
                            }
                            if (bleDevice.getRssi() > -80 && !isExist) {
                                bleRssiDevices.add(bleDevice);
                                deviceBluetoothDialog();
                                if (rvDeviceAdapter != null) {
                                    rvDeviceAdapter.addData(bleDevice);

                                }
                            }
                        }
                    }
                }

                @Override
                public void onScanning(com.clj.fastble.data.BleDevice bleDevice) {

                }

                @Override
                public void onScanFinished(List<BleDevice> scanResultList) {
                    LogUtils.i("20220321", "scanResultList:" + scanResultList.size());
                    List<BleDevice> scanDevices = new ArrayList<>();
                    for (int i = 0; i < scanResultList.size(); i++) {
                        BleDevice bd;
                        if (i<scanResultList.size()) {
                            bd= scanResultList.get(i);
                        }else{
                            return;
                        }
                        if (bd != null && bd.getName() != null && bd.getName().contains("ROBAM_") && bd.getName().contains("_0")) {
                            scanDevices.add(bd);
                        }
                    }
                    if (bleRssiDevices.isEmpty()) {
                        return;
                    }
                    if (scanDevices.isEmpty()) {
                        if (scanCountdis >= 2) {
                            bleRssiDevices.clear();
                            if (baseDialog != null && baseDialog.isShowing()) {
                                baseDialog.dismiss();
                                scanCountdis = 0;
                            }
                        } else {
                            scanCountdis++;
                        }
                        return;
                    }
                    for (int i = bleRssiDevices.size() - 1; i > 0; i--) {
                        boolean isEx = false;
                        for (int j = 0; j < scanDevices.size(); j++) {
                            if (bleRssiDevices.get(i).getMac().equals(scanDevices.get(j).getMac())) {
                                isEx = true;
                            }
                            if (j == scanDevices.size() - 1) {
                                if (!isEx) {
                                    if (rvDeviceAdapter != null)
                                        rvDeviceAdapter.remove(bleRssiDevices.get(i));
                                    rvDeviceAdapter.notifyDataSetChanged();
                                    bleRssiDevices.remove(i);
                                }
                                isEx = false;
                            }
                        }
                    }
                    if (bleRssiDevices.isEmpty() && scanDevices.isEmpty()) {
                        LogUtils.i("20220321", "bleRssiDevices:" + bleRssiDevices.size() + "   scanDevices:" + scanDevices);
                        if (baseDialog != null && baseDialog.isShowing()) {
                            baseDialog.dismiss();
                        }
                    }
                }
            });
        }
    }

    int scanCountdis = 0;
//    private AbsBleDevice setBlueDeviceIcon(BleDevice bleDevice){
//        AbsBleDevice absBleDevice = new AbsBleDevice(bleDevice.getDevice());
//        String tag= bleDevice.getName().substring(6,11);
//        for (int i = 0; i < sumDeviceList.size(); i++) {
//            List<DeviceItemList> detailList = sumDeviceList.get(i);
//            for (int j = 0; j < detailList.size(); j++) {
//                if (detailList.get(j).getName().contains(tag)) {
//                    absBleDevice.iconUrl=detailList.get(j).iconUrl;
//                }
//            }
//
//        }
//        return absBleDevice;
//    }

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
        BleManager.getInstance().cancelScan();
        BleManager.getInstance().destroy();
        //关闭定时任务
        if (mTimer != null) mTimer.cancel();

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
//        tabView.selectTab(TAB_DEVICE);
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

    @Subscribe
    public void onEvent(BlueCloseEvent event) {
        BleManager.getInstance().cancelScan();
        BleManager.getInstance().destroy();
        if (mTimer != null) mTimer.cancel();
    }

    List<View> buildViews() {
        List<View> views = Lists.newArrayList();
        views.add(new HomeDeviceView(cx, activity));
        homeRecipeView32 = new HomeRecipeView32(cx, activity);
        views.add(homeRecipeView32);
        homeMineView = new HomeMineView(cx, activity);
        views.add(homeMineView);
        return views;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventUtils.postEvent(new MessageEvent());
    }

    HomeTabView.OnTabSelectedCallback tabCallback = new HomeTabView.OnTabSelectedCallback() {
        @Override
        public void onTabSelected(final int tabIndex) {
//            if (tabView != null) {
//                tabView.setBackgroundDrawable(null);
//            }
            if (pager != null) {
                pager.setCurrentItem(tabIndex, true);
            }
//            if (Build.VERSION.SDK_INT >= 21) {
//                View decorView = activity.getWindow().getDecorView();
//                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//                activity.getWindow().setStatusBarColor(getResources().getColor(R.color.white));
//                StatusBarUtils.setTextDark(getContext(), true);
//            }
            switch (tabIndex) {
                case TAB_DEVICE:
//                  setRootBgRes(R.color.main_background);
//                    tabView.setBackgroundResource(R.color.white);
//                    String newBie = PreferenceUtils.getString("newBie", null);
//                    LogUtils.i("20180823", " newBie:" + newBie);
//                    if (Plat.accountService.isLogon() && DeviceService.getInstance().queryAll().size() > 0) {
//                        mIvMask.setVisibility(View.GONE);
//                        PreferenceUtils.setString("newBie", "new");
//                    } else if (newBie == null && DeviceService.getInstance().queryAll().size() == 0) {
//                        mIvMask.setVisibility(View.VISIBLE);
//                        PreferenceUtils.setString("newBie", "new");
//                    }
                    break;
                case TAB_RECIPE:
//                   setRootBgRes(R.color.main_background);
//                    tabView.setBackgroundResource(R.color.white);
//                    mIvMask.setVisibility(View.GONE);
//                    homeRecipeView3.upData();
                    break;
                case TAB_IP:
                    //R.color.White_90
//                    tabView.setBackgroundResource(R.color.white);
//                    mIvMask.setVisibility(View.GONE);
                    break;
                case TAB_PERSONAL:
                    //R.color.White_90
//                    tabView.setBackgroundResource(R.color.white);
//                    mIvMask.setVisibility(View.GONE);

//                    if (Build.VERSION.SDK_INT >= 21) {
//                        View decorView = activity.getWindow().getDecorView();
//                        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//                        activity.getWindow().setStatusBarColor(getResources().getColor(R.color.white));
//                        StatusBarUtils.setTextDark(getContext(), true);
//                    }
                    homeMineView.getUser();
                    break;
                default:
                    break;
            }
        }
    };


//    @OnClick(R.id.iv_mask)
//    public void onViewClicked() {
//        mIvMask.setVisibility(View.GONE);
//        EventUtils.postEvent(new NewBieGuideEvent());
//    }


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
        } else if (PermissionsUtils.CODE_BLUE_TOOTH == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    timeLoop();
                }
            }
        }

    }


    /**
     * 显示设备
     */
    private BleDevice item = null;

    private void deviceBluetoothDialog() {
        if (!PreferenceUtils.getBool("isShowBluetoothDevice", true) || !isHomePage) {
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
            Button tv_scan_again = (Button) baseDialog.findViewById(R.id.tv_scan_again);
            ImageView iv_close = (ImageView) baseDialog.findViewById(R.id.iv_close);
            rvDevice.setLayoutManager(new LinearLayoutManager(cx, LinearLayoutManager.HORIZONTAL, false));
            rvDeviceAdapter = new RvDeviceBluetoothAdapter();

            rvDeviceAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    item = rvDeviceAdapter.getItem(position);
//                    connectDevice(item);
                    if (!Plat.accountService.isLogon()) {
                        CmccLoginHelper.getInstance().login();
                        baseDialog.dismiss();
                        return;
                    }
                    Bundle bd = new Bundle();
                    bd.putString(PageArgumentKey.WIFITYPE, FormKey.HOME_WIFITYPE_BLUE);
                    bd.putParcelable("BleRssiDevice", item);
                    UIService.getInstance().postPage(PageKey.WifiConnect, bd);
//                    PreferenceUtils.setBool("isShowBluetoothDevice", false);
//                    if (mTimer != null) mTimer.cancel();
//                    BleManager.getInstance().cancelScan();
//                    BleManager.getInstance().destroy();
                    baseDialog.dismiss();

                }
            });
            baseDialog.setCancelable(false);
            baseDialog.addOnDismissListener(new BaseDialog.OnDismissListener() {
                @Override
                public void onDismiss(BaseDialog baseDialog) {
                    bleRssiDevices.clear();
                    rvDeviceAdapter.getData().clear();
                    rvDeviceAdapter.notifyDataSetChanged();
                }
            });
            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreferenceUtils.setBool("isShowBluetoothDevice", false);
//                    ToastUtils.show("本次不再弹出", Toast.LENGTH_SHORT);
                    baseDialog.dismiss();
                    BleManager.getInstance().cancelScan();
                    BleManager.getInstance().destroy();
                    //关闭定时任务
                    if (mTimer != null) mTimer.cancel();
                }
            });
            tv_not_tips.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    baseDialog.dismiss();
                    isShowBluetoothDeviceDialog();

                }
            });
            tv_scan_again.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bleRssiDevices.clear();
                    gattServices.clear();
                    rvDeviceAdapter.getData().clear();
                    rvDeviceAdapter.notifyDataSetChanged();
                    checkGpsStatus();
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
//        Log.e("yidao", "onEvent: DeviceFindEvent" + deviceInfo.guid);
//        if(Plat.accountService.isLogon()){
//            addKettle(deviceInfo);
//        }
//    }

    private void addKettle(final DeviceInfo devInfo) {
        try {
            devInfo.ownerId = Plat.accountService.getCurrentUserId();
            if (Strings.isNullOrEmpty(devInfo.name)) {
                DeviceType dt = DeviceTypeManager.getInstance().getDeviceType(devInfo.guid);
                if (dt != null) {
                    devInfo.name = dt.getName();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("yidao", "addKettle: " + devInfo.name);
        Log.e("yidao", "addKettle: " + devInfo.guid);
        Plat.deviceService.addWithBind(devInfo.guid, devInfo.name,
                true, new VoidCallback() {

                    @Override
                    public void onSuccess() {
                        EventUtils.postEvent(new DeviceEasylinkCompletedEvent(devInfo));
                    }

                    @Override
                    public void onFailure(Throwable t) {
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
                                if (FloatWindow.get("RecipeCook") == null) {
                                    return;
                                }
                                float x = FloatWindow.get("RecipeCook").getX();
                                if (x == 0) {
                                    ll_float.setBackgroundResource(R.drawable.shape_bg_f5f5f8_left);
                                } else {
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

    private boolean isHomePage = true;

    //页面切换事件可根据pageKey做判断
    @Subscribe
    public void onEvent(PageChangedEvent pageChangedEvent) {
        LogUtils.i("20220224", " pageKey:" + pageChangedEvent.pageKey);
        if (pageChangedEvent.pageKey.equals("dialogshow")||pageChangedEvent.pageKey.equals("dialogdismiss")) {
            return;
        }
        if (pageChangedEvent.pageKey.equals("Home")) {
            isHomePage = true;
        } else {
            isHomePage = false;
        }
    }

    @Subscribe
    public void onEvent(BlueLoginSuccessEvent loginSuccessEvent) {
        if (loginSuccessEvent.loginSuccess && item != null) {
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.WIFITYPE, FormKey.HOME_WIFITYPE_BLUE);
            bd.putParcelable("BleRssiDevice", item);
            UIService.getInstance().postPage(PageKey.WifiConnect, bd);
//            PreferenceUtils.setBool("isShowBluetoothDevice", false);
//            if (mTimer != null) mTimer.cancel();
//            BleManager.getInstance().cancelScan();
//            BleManager.getInstance().destroy();
            if (baseDialog != null && baseDialog.isShowing()) {
                baseDialog.dismiss();
            }
        }
    }

}

