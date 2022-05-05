package com.robam.roki.ui.view.networkoptimization;

import static com.robam.softap.Constants.FEEDBACK_DEVICE_ALREADY_CONFIG;
import static com.robam.softap.Constants.FEEDBACK_DEVICE_OTHER_STATUS_CODE;
import static com.robam.softap.Constants.FEEDBACK_SEND_DATA_SUCCESS_CODE;
import static com.robam.softap.Constants.MXHCIP_LIGHT_WIFI_SSID;
import static com.robam.softap.Constants.SOCKET_ACCEPT_TIME_OUT;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceFindEvent;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.pojos.dictionary.DeviceType;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.NetworkUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.api.WifiUtils;
import com.robam.common.events.DeviceEasylinkCompletedEvent;
import com.robam.roki.R;
import com.robam.roki.ui.dialog.TipDialog;
import com.robam.roki.ui.form.MainActivity;
import com.robam.softap.NetworkConfig;
import com.robam.softap.PermissionUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SoftAPConnectConfirmPage extends BasePage {
    @InjectView(R.id.btn_connect)
    TextView btnConnect;
    @InjectView(R.id.myprogressbar)
    ProgressBar myProgressbar;
    @InjectView(R.id.tv_connect_tip)
    TextView tvConnectTip;
    @InjectView(R.id.img_top_bg)
    ImageView imgTopBg;
    @InjectView(R.id.tv_connect_ing)
    TextView tvConnectIng;

    public static final String TAG = "yidao";
    WifiBroadcastReceiver wifiReceiver;
    private List<ScanResult> mWifiList = new ArrayList<>(); //wifi列表
    NetworkConfig mNetworkConfig;
    WifiManager mWifiManager;
    private String mSsid, mPwd, mGuid;
    private int mFeedbackCode = -1;
    CountDownTimer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_device_connect_wifi_confirm, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        initWiFiManager();
        wifiReceiver = new WifiBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//监听wifi是开关变化的状态
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//监听wifiwifi连接状态广播
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);//监听wifi列表变化（开启一个热点或者关闭一个热点）
        cx.registerReceiver(wifiReceiver, filter);

        mSsid = getArguments().getString("mSsid");
        mPwd = getArguments().getString("mPwd");
        mGuid = getArguments().getString("mGuid");

        PermissionUtil.requestEach(getActivity(), new PermissionUtil.OnPermissionListener() {
            @Override
            public void onSucceed() {
                scanWifiInfo();
            }

            @Override
            public void onFailed(boolean showAgain) {

            }
        }, PermissionUtil.LOCATION);
        //启动softap
//        startSoftAp();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wifiReceiver != null) {
            cx.unregisterReceiver(wifiReceiver);
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (mNetworkConfig != null) {
            mNetworkConfig.stopSoftAp();
        }

    }

    @SuppressLint("WifiManagerLeak")
    private void initWiFiManager() {
        mWifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
    }


    @OnClick(R.id.btn_connect)
    public void onSoftAP() {
        if (!getWifiName()) {
            TipDialog tipDialog = new TipDialog(cx, "您的手机当前未连接到ROKI-OHOS-XXXX热点，请先连接成功后，再进行下一步", "知道了");
            tipDialog.setSetOkOnClickLister(new TipDialog.SetOkOnClickLister() {
                @Override
                public void confirm() {
                    if (tipDialog.isShowing()) {
                        tipDialog.dismiss();
                    }
                }
            });
            tipDialog.setCanceledOnTouchOutside(false);
            tipDialog.show();
            return;
        }
        startSoftAp();
        scanWifiInfo();

        btnConnect.setVisibility(View.GONE);
        myProgressbar.setVisibility(View.VISIBLE);
        tvConnectTip.setVisibility(View.VISIBLE);
        tvConnectIng.setText(getResources().getString(R.string.device_softap_link_text_ing));
        Glide.with(cx).load(R.mipmap.connect).into(imgTopBg);
//        int number = (int) (Math.random() * 20 * 1000) + 10000;
//        Log.d("随机数：", number + "");
        int number = SOCKET_ACCEPT_TIME_OUT;
        timer = new CountDownTimer(number, 1) {
            @Override
            public void onTick(final long millisUntilFinished) {
                myProgressbar.post(new Runnable() {
                    @Override
                    public void run() {
                        int prog = (int) (number - millisUntilFinished);
                        int intP = (int) (prog * 100 / number);
                        myProgressbar.setProgress(intP);
                        if (!TextUtils.isEmpty(mGuid) && mFeedbackCode == 0 && intP > 30) {
                            myProgressbar.setProgress(100);
                            timer.cancel();
                            addDevice(mGuid);
                        }
                    }
                });
            }

            @Override
            public void onFinish() {
                btnConnect.setVisibility(View.VISIBLE);
                myProgressbar.setVisibility(View.GONE);
                tvConnectTip.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(mGuid) && mFeedbackCode == 0) {
                    addDevice(mGuid);
                } else {
                    connectFailShow();
                }
            }
        };
        timer.start();

    }

    @OnClick(R.id.img_back)
    public void onMImgBackClicked() {
        UIService.getInstance().popBack();
    }


    //自己配置从设备WiFi名拼接配置guid后，添加设备
    private void addDevice(String guid) {
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
                        Log.d("服务异常", t.toString());
                        connectFailShow();
                    }
                });

    }

    /**
     * 扫描附近wifi,
     */
    private void scanWifiInfo() {
        mWifiManager.setWifiEnabled(true);
        mWifiManager.startScan();
        mWifiList.clear();
        mWifiList = mWifiManager.getScanResults();
        Log.d(TAG, "scanWifiInfo: mWifiList.size() = " + mWifiList.size());
        if (mWifiList == null || mWifiList.size() == 0) {
            return;
        }
        NetworkConfig.IWiFiDiscoveryCallBack iWiFiDiscoveryCallBack = new NetworkConfig.IWiFiDiscoveryCallBack() {
            @Override
            public void onDiscoverySuccess() {
                Log.d(TAG, "onDiscoverySuccess: scanResult.SSID============ ");

            }

            @Override
            public void onDiscoveryFail() {
                if (mNetworkConfig != null) {
                    mNetworkConfig.stopSoftAp();
                }
            }
        };
        for (ScanResult scanResult : mWifiList) {
            Log.d(TAG, "scanWifiInfo: scanResult.SSID = " + scanResult.SSID);
            if (scanResult.SSID.contains(MXHCIP_LIGHT_WIFI_SSID)) {
                mGuid = "5915T" + scanResult.SSID.substring(12);
                while (mGuid.length() < 17) {
                    mGuid = mGuid + "0";
                }
                break;
            }
        }
    }

    public class WifiBroadcastReceiver extends BroadcastReceiver {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {

            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {

                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                switch (state) {
                    /**
                     * WIFI_STATE_DISABLED    WLAN已经关闭
                     * WIFI_STATE_DISABLING   WLAN正在关闭
                     * WIFI_STATE_ENABLED     WLAN已经打开
                     * WIFI_STATE_ENABLING    WLAN正在打开
                     * WIFI_STATE_UNKNOWN     未知
                     */
                    case WifiManager.WIFI_STATE_DISABLED: {
                        Log.i(TAG, "已经关闭");
                        break;
                    }
                    case WifiManager.WIFI_STATE_DISABLING: {
                        Log.i(TAG, "正在关闭");
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLED: {
                        Log.i(TAG, "已经打开");
//
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLING: {
                        Log.i(TAG, "正在打开");
                        break;
                    }
                    case WifiManager.WIFI_STATE_UNKNOWN: {
                        Log.i(TAG, "未知状态");
                        break;
                    }
                }
            } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                Log.i(TAG, "--NetworkInfo--" + info.toString());
                if (NetworkInfo.State.DISCONNECTED == info.getState()) {//wifi没连接上
                    Log.i(TAG, "wifi没连接上");
                } else if (NetworkInfo.State.CONNECTED == info.getState()) {//wifi连接上了
                    Log.i(TAG, "wifi连接上了" + WifiUtils.getSSIDByNetworkId(Plat.app));
                    LogUtils.i("获取网关信息", "wifi连接上了ip==========" + NetworkUtils.getLocalIp());
                    LogUtils.i("获取网关信息", "wifi连接上了Macip==========" + NetworkUtils.getMacByIp(NetworkUtils.getLocalIp()));

                } else if (NetworkInfo.State.CONNECTING == info.getState()) {//正在连接
                    Log.i(TAG, "wifi正在连接");
                }
            } else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
                Log.i(TAG, "网络列表变化了");
                scanWifiInfo();
            }

            boolean success = intent.getBooleanExtra(
                    WifiManager.EXTRA_RESULTS_UPDATED, false);
            Log.d(TAG, "onReceive: success = " + success);

        }
    }

    public void startSoftAp() {
        mNetworkConfig = new NetworkConfig();
        NetworkConfig.IDiscoveryDeviceListener iDiscoveryDeviceListener = new NetworkConfig.IDiscoveryDeviceListener() {
            @Override
            public void onDiscoverySoftAp() {
                Log.d(TAG, "onDiscoverySoftAp: ");
//                toastDiscoveryInfo("onDiscoverySoftAp");
            }

            @Override
            public void onServerSocketAccept() {
//                toastDiscoveryInfo("onServerSocketAccept");

            }

            @Override
            public void onAcceptedDeviceReadyData() {
//                toastDiscoveryInfo("onAcceptedDeviceReadyData");

            }

            @Override
            public void onSendDataToDevice(int sendStatus) {
//                toastDiscoveryInfo("onSendDataToDevice" + String.valueOf(sendStatus));
            }

            @Override
            public void onFetchDataFromDeviceFeedback(int feedbackCode) {
                if (feedbackCode == FEEDBACK_SEND_DATA_SUCCESS_CODE) {
                    mFeedbackCode = feedbackCode;
                    Log.d("feedbackCode", "配网成功了=========== feedbackCode：" + feedbackCode);
//                    myProgressbar.setProgress(100);
//                    timer.onFinish();
//                    addDevice(mGuid);

                } else if (feedbackCode == FEEDBACK_DEVICE_ALREADY_CONFIG) {

                } else if (feedbackCode == FEEDBACK_DEVICE_OTHER_STATUS_CODE) {

                } else {

                }
                mNetworkConfig.stopSoftAp();
            }

            @Override
            public void onDiscoveryFail(String reason) {
                connectFailShow();
                mNetworkConfig.stopSoftAp();
            }
        };
        mNetworkConfig.setIDiscoverySoftApListener(iDiscoveryDeviceListener);
        mNetworkConfig.startSoftAp(getActivity(), mSsid, mPwd, iDiscoveryDeviceListener);
    }

    private boolean getWifiName() {

        String ssid = WifiUtils.getSSIDByNetworkId(Plat.app);

        if (ssid == null && ssid.contains("unknown ssid")) {
            return false;
        } else if (ssid.contains(MXHCIP_LIGHT_WIFI_SSID)) {
            return true;
        }
        return false;
    }

    //配网失败
    private void connectFailShow() {
        //失败图片
        imgTopBg.setImageResource(R.mipmap.soft_ap_wifti_fail);
        tvConnectIng.setText(cx.getString(R.string.device_softap_link_fail_text));
        btnConnect.setText("重新配网");
        btnConnect.setVisibility(View.VISIBLE);
        myProgressbar.setVisibility(View.GONE);
        tvConnectTip.setVisibility(View.GONE);
    }

}
