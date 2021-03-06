package com.robam.roki.ui.view.networkoptimization;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.common.base.Strings;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.PrefsKey;
import com.legent.plat.io.device.ICookerLink;
import com.legent.plat.io.device.IDeviceFinder;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.pojos.dictionary.DeviceType;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.api.WifiUtils;
import com.robam.common.events.DeviceEasylinkCompletedEvent;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.PermissionsUtils;
import com.robam.roki.utils.ToolUtils;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by zhoudingjun on 2016/12/14.
 * ??????wifi??????
 */

public class WifiConnectPage extends BasePage {
    public EditText edtWifiName;
    public EditText edtPwd;
    @InjectView(R.id.btn_connect)
    TextView btn_connect;
    @InjectView(R.id.imgPwd)
    ImageView imgPwd;
    @InjectView(R.id.img_back)
    ImageView mImgBack;
    @InjectView(R.id.relPwd)
    LinearLayout mRelPwd;

    private boolean checked = true;
    WIFIConnectFailPage wfp = new WIFIConnectFailPage();
    // ?????????????????????????????????????????????
    public final MyHandler handler = new MyHandler(this);
    private IDeviceFinder mFinder;
    private ICookerLink cookerLink;
    private IRokiDialog mDialogByType;
    public static boolean isContent = true;

    String guid = null;
    String dt = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bd) {
        Bundle bb = getArguments();
        if (bb != null) {
            guid = bb.getString(PageArgumentKey.Guid);
            dt = bb.getString("displayType");
        }

        View view = inflater.inflate(R.layout.view_device_connect_wifi, container, false);
        edtWifiName = view.findViewById(R.id.edt_wifiName);
        edtPwd = view.findViewById(R.id.edtPwd);
        edtPwd.setTypeface(Typeface.DEFAULT);
        LogUtils.i("20180707", "guid:" + guid);
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

        edtWifiName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ToastUtils.show(R.string.show_wifi_name, Toast.LENGTH_SHORT);
            }
        });
        ButterKnife.inject(this, view);
        return view;
    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if(!hidden){
//            getWifiName();
//        }
//    }

    private void getWifiName() {
        if (!WifiUtils.isLocServiceEnable(getContext()) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ToastUtils.show("??????????????????????????????", Toast.LENGTH_SHORT);
            return;
        }
        String ssid = WifiUtils.getSSIDByNetworkId(Plat.app);

        if (ssid == null && ssid.contains("unknown ssid")) {
            edtWifiName.setText("");
        } else {
            edtWifiName.setText(ssid);
        }
        if (ssid != null) {
            String pwd = PreferenceUtils.getString(ssid, null);
            edtPwd.setText(pwd);
            if (Strings.isNullOrEmpty(pwd)) {
                edtPwd.requestFocus();
            }
        }
    }





    @OnClick(R.id.img_back)
    public void onMImgBackClicked() {
        UIService.getInstance().popBack();
    }

    @OnClick(R.id.edtPwd)
    public void onMEdtPwdClicked() {
        edtPwd.setCursorVisible(true);
        mRelPwd.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_wifipwd_bg));
    }


    static class MyHandler extends Handler {
        WeakReference<WifiConnectPage> WifiConnectPage;

        MyHandler(WifiConnectPage wifiConnectPage) {
            WifiConnectPage = new WeakReference<WifiConnectPage>(wifiConnectPage);
        }

        @Override
        public void handleMessage(Message msg) {
            final WifiConnectPage wifiConnectPage = WifiConnectPage.get();
            switch (msg.what) {
                case 0:
                    wifiConnectPage.setWifiList(wifiConnectPage, msg);
                    break;
                //guid ==null
                case 1:
                    wifiConnectPage.easylink();
                    break;
                //?????????guid????????? ????????????
                case 2:
                    wifiConnectPage.smartConnect();
                    break;
                case 3:
                    wifiConnectPage.udpSmartConnect();
                    break;
                default:
                    break;
            }
        }
    }


    //????????????wifi
    public void setWifiList(final WifiConnectPage wifiConnectPage, Message msg) {
        final ScanResult wifisr = (ScanResult) msg.obj;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                wifiConnectPage.edtWifiName.setText(wifisr.SSID);
            }
        });

    }

    //??????
    @OnClick(R.id.btn_connect)
    public void onClickBtn_Connect() {
        isContent = true;
        if (dt != null) {
            if ("C21-01R".equals(dt)) {
//                handler.sendEmptyMessage(2);
                handler.sendEmptyMessage(3);
                edtPwd.setCursorVisible(false);
                mRelPwd.setBackground(getResources().getDrawable(R.drawable.shape_wifi_name_bg));
                return;
            } else if ("C21-02R".equals(dt)) {
                handler.sendEmptyMessage(3);
                edtPwd.setCursorVisible(false);
                mRelPwd.setBackground(getResources().getDrawable(R.drawable.shape_wifi_name_bg));
                return;
            } else if ("C21-03R".equals(dt)) {
                handler.sendEmptyMessage(3);
                edtPwd.setCursorVisible(false);
                mRelPwd.setBackground(getResources().getDrawable(R.drawable.shape_wifi_name_bg));
                return;
            } else if ("KC306".equals(dt)){
                handler.sendEmptyMessage(3);
                edtPwd.setCursorVisible(false);
                mRelPwd.setBackground(getResources().getDrawable(R.drawable.shape_wifi_name_bg));
                return;
            }
        }
        if (guid != null) {
            if ("KC306".equals(guid)){
                handler.sendEmptyMessage(3);
                edtPwd.setCursorVisible(false);
                mRelPwd.setBackground(getResources().getDrawable(R.drawable.shape_wifi_name_bg));
            }else {
                handler.sendEmptyMessage(2);
                edtPwd.setCursorVisible(false);
                mRelPwd.setBackground(getResources().getDrawable(R.drawable.shape_wifi_name_bg));
            }
        } else {
            handler.sendEmptyMessage(1);
            edtPwd.setCursorVisible(false);
            mRelPwd.setBackground(getResources().getDrawable(R.drawable.shape_wifi_name_bg));
        }
    }

    //????????????
    @OnClick(R.id.imgPwd)
    public void onClickPwd() {
        if (TextUtils.isEmpty(edtPwd.getText())) return;
        if (checked) {
            edtPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imgPwd.setImageResource(R.mipmap.img_yan_close);
        } else {
            edtPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imgPwd.setImageResource(R.mipmap.yanjing);
        }
        checked = !checked;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void easylink() {
        final String ssid = edtWifiName.getText().toString();
        final String pwd = edtPwd.getText().toString();
        if (TextUtils.isEmpty(ssid)) {
            ToastUtils.show(getString(R.string.roki_error_no_wifi), Toast.LENGTH_SHORT);
            return;
        }
        PreferenceUtils.setString(PrefsKey.Ssid, ssid);
        PreferenceUtils.setString(ssid, pwd);
        mFinder = Plat.dcMqtt.getDeviceFinder();
        mDialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_06);
        mDialogByType.setInitTaskData(1, 0, 1850);
        mDialogByType.setCanceledOnTouchOutside(false);
        mDialogByType.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //ToastUtils.show("????????????",Toast.LENGTH_SHORT);
                mFinder.stop();
            }
        });
        mDialogByType.show();
        LogUtils.i("checkConnect", "ssid:" + ssid + " pwd:" + pwd);
        //????????????????????????wifi??????????????????guid
        mFinder.start(ssid, pwd, 1000 * 180, new Callback<DeviceInfo>() {

            @Override
            public void onSuccess(DeviceInfo result) {
                if (mDialogByType.isShow()) {
                    mDialogByType.dismiss();
                }

                PreferenceUtils.setString(ssid, pwd);
                LogUtils.i("20180201", "result:" + result);
                addKettle(result);
                if (guid == null) {
                    return;
                }
                if (guid.equals("")) {
                    return;
                }
                IDevice iDevice = Plat.deviceService.lookupChild(guid);
                if (iDevice == null) {
                    return;
                }
                ToolUtils.logEvent(iDevice.getDt(), "????????????", "roki_??????");

            }

            @Override
            public void onFailure(Throwable t) {

                if (mDialogByType.isShow()) {
                    mDialogByType.dismiss();
                    UIService.getInstance().postPage(PageKey.WIFIConnectFailPage);
                }


                if (guid == null) {
                    return;
                }
                if (guid.equals("")) {
                    return;
                }
                IDevice iDevice = Plat.deviceService.lookupChild(guid);
                if (iDevice == null) {
                    return;
                }
                ToolUtils.logEvent(iDevice.getDt(), "????????????", "roki_??????");
            }


        });


    }

    private void udpSmartConnect() {
        final String ssid = edtWifiName.getText().toString();
        final String pwd = edtPwd.getText().toString();
        if (TextUtils.isEmpty(ssid)) {
            ToastUtils.show(getString(R.string.roki_error_no_wifi), Toast.LENGTH_SHORT);
            return;
        }
        String name = Thread.currentThread().getName();
        LogUtils.i("20201029", "main:" + name);
        LogUtils.i("20201029", "Ssid:" + ssid);
        LogUtils.i("20201029", "Guid:" + guid);
        LogUtils.i("20201029", "pwd:" + pwd);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PreferenceUtils.setString(PrefsKey.Ssid, ssid);
                PreferenceUtils.setString(ssid, pwd);
                Bundle bundle = new Bundle();
                bundle.putString(PageArgumentKey.wifiName, name);
                bundle.putString(PageArgumentKey.Ssid, ssid);
                bundle.putString(PageArgumentKey.Pwd, pwd);
                bundle.putString(PageArgumentKey.Guid, dt);
                if ("KC306".equals(dt)|| "KC306".equals(guid)){
                    UIService.getInstance().postPage(PageKey.DeviceUdpHidKitContent, bundle);
                }else if ("C21-01R".equals(dt) || "C21-02R".equals(dt) || "C21-03R".equals(dt) ){
                    UIService.getInstance().postPage(PageKey.DeviceUdpContent, bundle);
                }
            }
        });

    }

    private void smartConnect() {
        final String ssid = edtWifiName.getText().toString();
        final String pwd = edtPwd.getText().toString();
        if (TextUtils.isEmpty(ssid)) {
            ToastUtils.show(getString(R.string.roki_error_no_wifi), Toast.LENGTH_SHORT);
            return;
        }
        PreferenceUtils.setString(PrefsKey.Ssid, ssid);
        PreferenceUtils.setString(ssid, pwd);
        cookerLink = new CookerNetLink();
        mDialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_06);
        mDialogByType.setInitTaskData(1, 0, 1850);
        mDialogByType.setCanceledOnTouchOutside(false);
        mDialogByType.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //ToastUtils.show("????????????",Toast.LENGTH_SHORT);
//                cookerLink.stop();
            }
        });
        mDialogByType.show();


        LogUtils.i("checkConnect", "ssid:" + ssid + " pwd:" + pwd);

        cookerLink.start(ssid, pwd, 1000 * 180, new Callback<DeviceInfo>() {

            @Override
            public void onSuccess(DeviceInfo result) {
                if (mDialogByType.isShow()) {
                    mDialogByType.dismiss();
                }

                PreferenceUtils.setString(ssid, pwd);
                LogUtils.i("20180201", "result:" + result);
                addKettle(result);
            }

            @Override
            public void onFailure(Throwable t) {

                LogUtils.i("20180201", "t:" + t);
                if (mDialogByType.isShow()) {
                    mDialogByType.dismiss();
                    UIService.getInstance().postPage(PageKey.WIFIConnectFailPage);
                }
            }
        });

    }

    void addKettle(final DeviceInfo devInfo) {

        try {
            devInfo.ownerId = Plat.accountService.getCurrentUserId();
            if (Strings.isNullOrEmpty(devInfo.name)) {
                DeviceType dt = DeviceTypeManager.getInstance().getDeviceType(
                        devInfo.guid);
                if (dt != null) {
                    devInfo.name = dt.getName();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Plat.deviceService.addWithBind(devInfo.guid, devInfo.name,
                true, new VoidCallback() {

                    @Override
                    public void onSuccess() {
                        ToastUtils.showShort(R.string.add_device_failure);
                        EventUtils.postEvent(new DeviceEasylinkCompletedEvent(devInfo));
                        UIService.getInstance().returnHome();
//                        UIService.getInstance().popBack().popBack().popBack();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.showThrowable(t);
                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (PermissionsUtils.CODE_WIFI_SSID == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    getWifiName();
                }
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}