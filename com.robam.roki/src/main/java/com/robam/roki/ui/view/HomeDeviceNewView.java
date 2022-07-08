package com.robam.roki.ui.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.common.eventbus.Subscribe;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectedNoticEvent;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.events.DeviceLoadCompletedEvent;
import com.legent.plat.events.DeviceSelectedEvent;
import com.legent.plat.events.UserLoginEvent;
import com.legent.plat.events.UserLogoutEvent;
import com.legent.plat.services.DeviceService;
import com.legent.ui.UIService;
import com.legent.ui.ext.views.TitleBar;
import com.legent.utils.EventUtils;
import com.legent.utils.qrcode.ScanQrActivity;
import com.robam.common.Utils;
import com.robam.common.events.DeviceDeleteEvent;
import com.robam.common.events.DeviceEasylinkCompletedEvent;
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.events.SteamOvenStatusChangedEvent;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Sterilizer.AbsSterilizer;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.ui.UiHelper;
import com.robam.roki.R;
import com.robam.roki.service.AppService;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.UIListeners;
import com.robam.roki.ui.view.networkoptimization.NetWorkStateUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeDeviceNewView extends FrameLayout implements UIListeners.IRefresh {
    @InjectView(R.id.titleView)
    TitleBar titleBar;//标题栏

    @InjectView(R.id.rel_homedevice)
    RelativeLayout rel_homedevice;

    @InjectView(R.id.disconnectHintView)
    LinearLayout disconnectHintView;

    @InjectView(R.id.pull_refresh_scrollview)
    PullToRefreshScrollView pullRefreshScrollview;

    private UIRefreshBroadcastReceiver receiver;
    //定义界面刷新常量
    protected static final String ACTION = "com.robam.roki.senduirefreshcommand";

    public HomeDeviceNewView(Context context) {
        super(context);
        init(context, null);
    }

    public HomeDeviceNewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HomeDeviceNewView(Context context, AttributeSet attrs, int defStyle) {
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
        onRefresh();
    }

    @Subscribe
    public void onEvent(UserLogoutEvent event) {
        refreshHomeBackground();
        onRefresh();
    }

    @Subscribe
    public void onEvent(DeviceEasylinkCompletedEvent event) {
        AppService.getInstance().init(Plat.app);
        onRefresh();
    }

    @Subscribe
    public void onEvent(DeviceConnectedNoticEvent event) {
        onRefresh();
    }

    @Subscribe
    public void onEvent(DeviceSelectedEvent event) {
        onRefresh();
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {

        String guid = event.device.getID();
        if (Utils.isFan(guid)) {
            onRefreshConnection();
        }
    }

    @Subscribe
    public void onEvent(DeviceDeleteEvent event) {

    }

    @Subscribe
    public void onEvent(SteamOvenStatusChangedEvent event) {
        if (event.pojo == null) {
            return;
        }
        if (event.pojo instanceof AbsSteamoven) {
            refreshSteamView(event.pojo.status);
        } else {
            return;
        }
    }

    @Subscribe
    public void onEvent(OvenStatusChangedEvent event) {
        boolean hasOven = (event.pojo != null);

        if (event.pojo == null) {
            return;
        }
        if (event.pojo instanceof AbsOven) {
            refreshOvenView(event.pojo.status);
        } else {
            return;
        }
    }

    void init(Context cx, AttributeSet attrs) {
        View view = LayoutInflater.from(cx).inflate(R.layout.view_home_device, this, true);
        if (Plat.accountService.isLogon() && DeviceService.getInstance().queryAll().size() > 0) {
            if (!view.isInEditMode()) {
                ButterKnife.inject(this, view);
                rel_homedevice.setBackgroundResource(0);
                setTitleBar();
                disconnectHintView.setVisibility(View.GONE);
                pullRefreshScrollview.setVisibility(VISIBLE);
                pullRefreshScrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                pullRefreshScrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
                    @Override
                    public void onRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
                        refreshWhenPull();
                    }
                });
                onRefresh();
            }
        } else {
            if (!view.isInEditMode()) {
                ButterKnife.inject(this, view);
                pullRefreshScrollview.setVisibility(GONE);
                rel_homedevice.setBackgroundResource(R.mipmap.lwbj);
                disconnectHintView.setVisibility(View.GONE);
                setTitleBar();
            }
        }

    }

    void setTitleBar() {
        titleBar.setTitle("厨电");
        ImageView icon_deviceadd = TitleBar.newTitleIconView(getContext(), R.mipmap.ic_device_add_night, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetWorkStateUtils.isNetworkConnected(getContext())) {
                    UIService.getInstance().postPage(PageKey.UserLogin);
                    return;
                }
                if (!NetWorkStateUtils.isWifi(getContext())) {
                    UIService.getInstance().postPage(PageKey.SettingWifi);
                    return;
                }
                if (UiHelper.checkAuthWithDialog(getContext(), PageKey.UserLogin)) {
                    UIService.getInstance().postPage(PageKey.DeviceAdd);
                }
            }
        });
        titleBar.replaceRight(icon_deviceadd);
        ImageView icon_scan = TitleBar.newTitleIconView(getContext(), R.mipmap.ic_device_scan, new OnClickListener() {
            private final static int SCANNIN_GREQUEST_CODE = 100;

            @Override
            public void onClick(View view) {
                if (UiHelper.checkAuthWithDialog(getContext(), PageKey.UserLogin)) {
                    Activity atv = UIService.getInstance().getTop().getActivity();
                    Intent intent = new Intent();
                    intent.setClass(atv, ScanQrActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    atv.startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                }

            }
        });
        titleBar.replaceLeft(icon_scan);
    }

    void refreshWhenPull() {
        AppService.getInstance().init(Plat.app);
        pullRefreshScrollview.onRefreshComplete();
    }

    @Subscribe
    public void onEvent(DeviceLoadCompletedEvent event) {
        refreshHomeBackground();
    }

    private void refreshHomeBackground() {
        if (DeviceService.getInstance().queryAll().size() > 0) {
            rel_homedevice.setBackgroundResource(0);
        } else {
            rel_homedevice.setBackgroundResource(R.mipmap.lwbj);
        }
    }

    @Override
    public void onRefresh() {
        if (DeviceService.getInstance().queryAll().size() > 0) {
            pullRefreshScrollview.setVisibility(VISIBLE);
            onRefreshConnection();
            onFanViewFresh();
            onSteriViewFresh();
            onSteamViewFresh();
            onOvenViewFresh();
            onMicrowaveFresh();
        } else {
            disconnectHintView.setVisibility(View.GONE);
            pullRefreshScrollview.setVisibility(GONE);
        }
    }

    //判断微波炉是否存在
    private void onMicrowaveFresh() {
        AbsMicroWave microWave = Utils.getDefaultMicrowave();
        boolean hasMicroWave = (microWave != null);
    }

    //判断蒸汽炉是否存在
    private void onSteamViewFresh() {
        AbsSteamoven steam = Utils.getDefaultSteam();
        boolean hasSteam = (steam != null);
    }

    //判断烤箱是否存在
    private void onOvenViewFresh() {
        AbsOven oven = Utils.getDefaultOven();
        boolean hasOven = (oven != null);
    }


    //油烟机存在并断网，显示断网提示
    void onRefreshConnection() {
        AbsFan fan = Utils.getDefaultFan();
        boolean isDisconnected = fan != null && !fan.isConnected();
        disconnectHintView.setVisibility(isDisconnected
                ? View.VISIBLE
                : View.GONE);
    }

    //判断油烟机电磁炉是否存在
    public void onFanViewFresh() {
//        AbsFan fan = Utils.getDefaultFan();
//        boolean hasDevice = (fan != null);
//        addView.setVisibility(!hasDevice ? VISIBLE : GONE);
//        devicesLayout.setVisibility(hasDevice ? VISIBLE : GONE);
//        if (!hasDevice) return;
//        fanView.loadData(fan);
//        Stove stove = Utils.getDefaultStove();
//        boolean hasStove = stove != null;
//        stoveView.setVisibility(hasStove ? VISIBLE : GONE);
//        if (hasStove) {
//            stoveView.loadData(stove);
//        }
    }

    //判断消毒柜会否存在
    private void onSteriViewFresh() {
        AbsSterilizer sterilizer = Utils.getDefaultSterilizer();
        boolean hasSterilizer = (sterilizer != null);
        if (!hasSterilizer) return;
    }

    void refreshSteamView(short status) {

    }

    void refreshOvenView(short status) {
    }


    //定义一个内部类广播接收者，接受删除设备之后刷新指令
    public class UIRefreshBroadcastReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            onRefresh();
        }
    }

}
