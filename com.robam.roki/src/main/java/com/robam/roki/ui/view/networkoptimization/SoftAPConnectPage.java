package com.robam.roki.ui.view.networkoptimization;

import static com.robam.softap.Constants.FEEDBACK_DEVICE_ALREADY_CONFIG;
import static com.robam.softap.Constants.FEEDBACK_DEVICE_OTHER_STATUS_CODE;
import static com.robam.softap.Constants.FEEDBACK_SEND_DATA_SUCCESS_CODE;
import static com.robam.softap.Constants.MXHCIP_LIGHT_WIFI_PASSWORD;
import static com.robam.softap.Constants.MXHCIP_LIGHT_WIFI_SSID;
import static com.robam.softap.Constants.WIFI_ENCRYPT_OPEN;
import static com.robam.softap.Constants.WIFI_ENCRYPT_WEP;
import static com.robam.softap.Constants.WIFI_ENCRYPT_WPA;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.PrefsKey;
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
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.TipDialog;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.PermissionsUtils;
import com.robam.softap.NetworkConfig;
import com.robam.softap.PermissionUtil;
import com.robam.softap.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SoftAPConnectPage extends BasePage {
    @InjectView(R.id.btn_connect)
    TextView btnConnect;
    @InjectView(R.id.imgPwd)
    ImageView imgPwd;
    @InjectView(R.id.edt_wifiName)
    EditText etWiFiName;
    @InjectView(R.id.edtPwd)
    EditText etWiFiPwd;
    public static final String TAG = "yidao";

    private List<ScanResult> mWifiList = new ArrayList<>(); //wifi列表
    NetworkConfig mNetworkConfig;
    WifiManager mWifiManager;
    private boolean checked = true;
    private String mSsid, mPwd, mGuid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_device_connect_wifi, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        etWiFiPwd.setTypeface(Typeface.DEFAULT);
        initWiFiManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int selfPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
            if (selfPermission == 0) {
                getWifiName();
            } else {
                PermissionsUtils.checkPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION, PermissionsUtils.CODE_WIFI_SSID);
            }
        } else {
            getWifiName();
        }
    }

    @SuppressLint("WifiManagerLeak")
    private void initWiFiManager() {
        mWifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
    }

    @OnClick(R.id.imgPwd)
    public void onClickPwd() {
        if (TextUtils.isEmpty(etWiFiPwd.getText())) return;
        if (checked) {
            etWiFiPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imgPwd.setImageResource(R.mipmap.img_yan_close);
        } else {
            etWiFiPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imgPwd.setImageResource(R.mipmap.yanjing);
        }
        checked = !checked;
    }

    @OnClick(R.id.btn_connect)
    public void onSoftAP() {
        Log.d(TAG, "onSoftAP: ");
        if (TextUtils.isEmpty(etWiFiPwd.getText()) || TextUtils.isEmpty(etWiFiName.getText())) {
            ToastUtils.show("WiFi名称和密码不能为空", 2000);
            return;
        }
        if (Utils.getSSID(cx).contains(MXHCIP_LIGHT_WIFI_SSID)) {
            mWifiManager.disconnect();
        }
        Bundle bundle = new Bundle();
        bundle.putString("mSsid", etWiFiName.getText().toString());
        bundle.putString("mPwd", etWiFiPwd.getText().toString());
        bundle.putString("mGuid", mGuid);
        UIService.getInstance().postPage(PageKey.WifiSoftapConnectConfirm, bundle);

//        PermissionUtil.requestEach(getActivity(), new PermissionUtil.OnPermissionListener() {
//            @Override
//            public void onSucceed() {
//                openWifi();
//            }
//
//            @Override
//            public void onFailed(boolean showAgain) {
//
//            }
//        }, PermissionUtil.LOCATION);
    }

    @OnClick(R.id.img_back)
    public void onMImgBackClicked() {
        UIService.getInstance().popBack();
    }

    private void openWifi() {
        //授权成功后打开wifi
        boolean isExisted = scanWifiInfo();
        if (isExisted) {
            Bundle bundle = new Bundle();
            bundle.putString("mSsid", etWiFiName.getText().toString());
            bundle.putString("mPwd", etWiFiPwd.getText().toString());
            bundle.putString("mGuid", mGuid);
            UIService.getInstance().postPage(PageKey.WifiSoftapConnectConfirm, bundle);
        } else {
            TipDialog tipDialog = new TipDialog(cx, "请先开启需要连接的设备热点", "知道了");
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
        }
    }

    /**
     * 扫描附近wifi,
     */
    private boolean scanWifiInfo() {
        mWifiManager.setWifiEnabled(true);
        mWifiManager.startScan();
        mWifiList.clear();
        mWifiList = mWifiManager.getScanResults();
        Log.d(TAG, "scanWifiInfo: mWifiList.size() = " + mWifiList.size());
        if (mWifiList == null || mWifiList.size() == 0) {
            return false;
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
        boolean isExisted = false;
        for (ScanResult scanResult : mWifiList) {
            Log.d(TAG, "scanWifiInfo: scanResult.SSID = " + scanResult.SSID);
            if (scanResult.SSID.contains(MXHCIP_LIGHT_WIFI_SSID)) {
                mGuid = "5915T" + scanResult.SSID.substring(12);
                while (mGuid.length() < 17) {
                    mGuid = mGuid + "0";
                }
                isExisted = true;
                mPwd = MXHCIP_LIGHT_WIFI_PASSWORD;

                break;
            }
        }
        return isExisted;

    }
    private void getWifiName() {
        if (!WifiUtils.isLocServiceEnable(getContext()) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ToastUtils.show("请开启手机的位置服务", Toast.LENGTH_SHORT);
            return;
        }
        String ssid = WifiUtils.getSSIDByNetworkId(Plat.app);

        if (ssid == null && ssid.contains("unknown ssid")) {
            etWiFiName.setText("");
        } else {
            etWiFiName.setText(ssid);
        }
        if (ssid != null) {
            String pwd = PreferenceUtils.getString(ssid, null);
            etWiFiPwd.setText(pwd);
            if (Strings.isNullOrEmpty(pwd)) {
                etWiFiPwd.requestFocus();
            }
        }
    }


}
