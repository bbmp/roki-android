package com.robam.roki.ui.page.device;

import static com.legent.utils.api.WifiUtils.BlueConnectTimeOut;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.alibaba.fastjson.JSON;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleMtuChangedCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IAppType;
import com.legent.plat.events.DeviceFindEvent;
import com.legent.plat.events.PageBackEvent;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.pojos.dictionary.DeviceType;
import com.legent.plat.services.CommonService;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.services.TaskService;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.views.DotLineView;
import com.legent.utils.EventUtils;
import com.legent.utils.WifiAutoConnectManager;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.DeviceEasylinkCompletedEvent;
import com.robam.common.ui.BleRssiDevice;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.PermissionsUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.com.heaton.blelibrary.ble.Ble;
import cn.com.heaton.blelibrary.ble.callback.BleConnectCallback;
import cn.com.heaton.blelibrary.ble.callback.BleReadCallback;
import cn.com.heaton.blelibrary.ble.callback.BleScanCallback;
import cn.com.heaton.blelibrary.ble.callback.BleWriteCallback;
import cn.com.heaton.blelibrary.ble.utils.Utils;
import cn.com.heaton.blelibrary.ble.utils.UuidUtils;
import skin.support.content.res.SkinCompatResources;

/**
 * 蓝牙配网界面
 */
public class DeviceBlueContentPage extends BasePage {
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    @InjectView(R.id.img_blue_phone)
    ImageView imgBluePhone;
    @InjectView(R.id.img_blue_wifi)
    ImageView imgBlueWifi;

    @InjectView(R.id.ll_center)
    LinearLayout llCenter;
    @InjectView(R.id.img_blue_device)
    ImageView imgBlueDevice;
    @InjectView(R.id.rl_connet_step)
    LinearLayout rlConnetStep;
    @InjectView(R.id.img_blue_device_big)
    ImageView imgBlueDeviceBig;
    @InjectView(R.id.img_blue_wifi_slant)
    ImageView imgBlueWifiSlant;
    @InjectView(R.id.rl_connet_device)
    LinearLayout rlConnetDevice;
    @InjectView(R.id.img_setp_one)
    ImageView imgSetpOne;
    @InjectView(R.id.ing_setp_line_one)
    DotLineView ingSetpLineOne;
    @InjectView(R.id.img_setp_two)
    ImageView imgSetpTwo;
    @InjectView(R.id.ing_setp_line_two)
    DotLineView ingSetpLineTwo;
    @InjectView(R.id.img_setp_three)
    ImageView imgSetpThree;
    @InjectView(R.id.tv_setp_one)
    TextView tvSetpOne;
    @InjectView(R.id.tv_setp_two)
    TextView tvSetpTwo;
    @InjectView(R.id.tv_setp_three)
    TextView tvSetpThree;
    @InjectView(R.id.tv_try_again)
    TextView tv_try_again;
    @InjectView(R.id.tv_reset)
    TextView tv_reset;
    @InjectView(R.id.ll_fail_page)
    LinearLayout ll_fail_page;
    @InjectView(R.id.ll_connect_page)
    LinearLayout ll_connect_page;

    private String mSsid;
    private String mPwd;

    private List<BluetoothGattService> gattServices = new ArrayList<>();//蓝牙服务
    private BleDevice bleRssiDevice = null;

    protected ScheduledFuture<?> mFuture;//计时器
    protected int mRemainTime;

    /**
     * 开始计时
     */
    protected void startCountdown(final int needTime) {
        if (mFuture != null)
            return;
        mRemainTime = needTime;
        mFuture = TaskService.getInstance().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Log.i("20171108", "startCountdown mRemainTime " + mRemainTime);
                if (mRemainTime <= 0) {
                    stopCountdown();
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            setPageUI(4);
                        }
                    });
                }
                mRemainTime--;

            }
        }, 500, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * 结束计时
     */
    protected void stopCountdown() {
        Log.i("20171108", "stopCountdown " + mFuture);
        if (mFuture != null) {
            mFuture.cancel(true);
            mFuture = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_blue_content, container, false);
        ButterKnife.inject(this, view);
        Bundle bb = getArguments();
        if (bb != null) {
            bleRssiDevice = (BleDevice) bb.getParcelable("BleRssiDevice");
            mSsid = bb.getString(PageArgumentKey.Ssid);
            mPwd = bb.getString(PageArgumentKey.Pwd);
        }
        if (bleRssiDevice != null) {
            connectDevice(bleRssiDevice);
        }
        return view;
    }

    public void connectDevice(BleDevice bleDevice) {
        try {
            if (bleDevice != null) {
                startCountdown(BlueConnectTimeOut);
                BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
                    @Override
                    public void onStartConnect() {
//                    ToastUtils.show("开始连接蓝牙", Toast.LENGTH_SHORT);
                        Log.d("20220105", "开始连接蓝牙  ");

                        setPageUI(2);
                    }

                    @Override
                    public void onConnectFail(BleDevice bleDevice, BleException e) {
//                    ToastUtils.show("连接蓝牙失败" + e.getDescription(), Toast.LENGTH_SHORT);
                        Log.d("20220105", "连接蓝牙失败  ");

//                    setPageUI(4);
                    }

                    @Override
                    public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
//                    ToastUtils.show("连接蓝成功", Toast.LENGTH_SHORT);
                        Log.d("20220105", "连接蓝成功  ");

                        setPageUI(3);
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (gatt.requestMtu(200)) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
//                        onSend(bleDevice);
                        }
                        BleManager.getInstance().setMtu(bleDevice, 200, new BleMtuChangedCallback() {
                            @Override
                            public void onSetMTUFailure(BleException exception) {
                                // 设置MTU失败
                                Log.d("20220105", "设置MTU失败  ");

//                            setPageUI(4);
                            }

                            @Override
                            public void onMtuChanged(int mtu) {
                                // 设置MTU成功，并获得当前设备传输支持的MTU值
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                onSend(bleDevice);
                            }
                        });
                        BluetoothGatt bluetoothGatt = BleManager.getInstance().getBluetoothGatt(bleDevice);
//                    bluetoothGatt.requestMtu(200);
                        if (gattServices != null && bluetoothGatt != null) {
                            gattServices.clear();
                            gattServices.addAll(bluetoothGatt.getServices());
                        }
                    }

                    @Override
                    public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onSend(BleDevice device) {
        BluetoothGattCharacteristic writeChar = null;
        for (int i = 0; i < gattServices.size(); i++) {
            if (gattServices.get(i).getCharacteristic(UUID.fromString(UuidUtils.uuid16To128("fed7"))) != null) {
                writeChar = gattServices.get(i).getCharacteristic(UUID.fromString(UuidUtils.uuid16To128("fed7")));
            }
        }
        if (writeChar != null) {
            Map<String, String> wifiInfoMap = new HashMap<String, String>();
            wifiInfoMap.put("userid", Plat.accountService.getCurrentUserId() + "");
            wifiInfoMap.put("apptype", IAppType.RKDRD);
            String appid = Plat.appGuid;
            String newAppId = appid.substring(5, appid.length());
            wifiInfoMap.put("appid", newAppId);
            wifiInfoMap.put("ssid", mSsid);
            wifiInfoMap.put("pwd", mPwd);
            String wifiInfoGson = JSON.toJSONString(wifiInfoMap);
            /**
             * 给设备写入消息
             */
            BleManager.getInstance().write(
                    device,
                    writeChar.getService().getUuid().toString(),
                    writeChar.getUuid().toString(),
                    wifiInfoGson.getBytes(),
                    false,
                    new com.clj.fastble.callback.BleWriteCallback() {
                        @Override
                        public void onWriteSuccess(int i, int i1, byte[] bytes) {
                            Log.d("20220105", "写入成功  ");
                        }

                        @Override
                        public void onWriteFailure(BleException e) {
                            Log.d("20220105", "写入失败  ");

//                            setPageUI(4);
                        }
                    });
        }
    }

    private void setPageUI(int step) {
        switch (step) {
            case 1:
                ingSetpLineOne.setDotColor(SkinCompatResources.getColor(getContext(), R.color.circle_color_progress));
                ingSetpLineTwo.setDotColor(SkinCompatResources.getColor(getContext(), R.color.circle_color_progress));
//                imgSetpThree.setImageResource(R.mipmap.icon_blue_connect_wait);
                rlConnetStep.setVisibility(View.VISIBLE);
                rlConnetDevice.setVisibility(View.GONE);
                break;
            case 2:
                imgSetpOne.setImageResource(R.mipmap.icon_blue_connect_success);
                ingSetpLineOne.setDotColor(Color.parseColor("#ff61acff"));
//                imgSetpTwo.setImageResource(R.mipmap.icon_blue_connect_ing);
                imgBlueWifi.setVisibility(View.VISIBLE);
//                ingSetpLineTwo
//                imgSetpThree
                break;
            case 3:
                imgSetpOne.setImageResource(R.mipmap.icon_blue_connect_success);
                ingSetpLineOne.setDotColor(Color.parseColor("#ff61acff"));
                imgSetpTwo.setImageResource(R.mipmap.icon_blue_connect_success);
                ingSetpLineTwo.setDotColor(Color.parseColor("#ff61acff"));
//                imgSetpThree.setImageResource(R.mipmap.icon_blue_connect_ing);
                rlConnetStep.setVisibility(View.GONE);
                rlConnetDevice.setVisibility(View.VISIBLE);
                break;
            case 4:
                ll_fail_page.setVisibility(View.VISIBLE);
                ll_connect_page.setVisibility(View.GONE);
                break;
        }

    }

    @Subscribe
    public void onEvent(DeviceFindEvent deviceFindEvent) {
        Log.e("onEvent", "onEvent: DeviceFindEventDeviceFindEvent");
        DeviceInfo deviceInfo = deviceFindEvent.deviceInfo;
        addKettle(deviceInfo);

    }

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
        Plat.deviceService.addWithBind(devInfo.guid, devInfo.name,
                true, new VoidCallback() {

                    @Override
                    public void onSuccess() {
                        EventUtils.postEvent(new DeviceEasylinkCompletedEvent(devInfo));
                        // UIService.getInstance().returnHome();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            UIService.getInstance().returnHome();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        BleManager.getInstance().cancelScan();
        BleManager.getInstance().destroy();
        stopCountdown();
    }

    @OnClick({R.id.iv_back, R.id.tv_try_again, R.id.tv_reset})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                UIService.getInstance().popBack();
                break;
            case R.id.tv_try_again:
                setPageUI(1);
                ll_fail_page.setVisibility(View.GONE);
                ll_connect_page.setVisibility(View.VISIBLE);
                if (bleRssiDevice != null) {
                    connectDevice(bleRssiDevice);
                }
                break;
            case R.id.tv_reset:
                UIService.getInstance().popBack().popBack();
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventUtils.postEvent(new PageBackEvent("DeviceBlueContentPage"));
    }
}
