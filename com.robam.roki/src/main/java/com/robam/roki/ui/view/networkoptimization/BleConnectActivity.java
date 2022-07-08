package com.robam.roki.ui.view.networkoptimization;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceFindEvent;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.pojos.dictionary.DeviceType;
import com.legent.plat.services.CommonService;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.DeviceEasylinkCompletedEvent;
import com.robam.common.ui.BleRssiDevice;
import com.robam.roki.R;
import com.robam.roki.ui.activity3.AppActivity;
import com.robam.roki.utils.PermissionsUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.heaton.blelibrary.ble.Ble;
import cn.com.heaton.blelibrary.ble.callback.BleConnectCallback;
import cn.com.heaton.blelibrary.ble.callback.BleMtuCallback;
import cn.com.heaton.blelibrary.ble.callback.BleReadCallback;
import cn.com.heaton.blelibrary.ble.callback.BleScanCallback;
import cn.com.heaton.blelibrary.ble.callback.BleWriteCallback;
import cn.com.heaton.blelibrary.ble.model.BleDevice;
import cn.com.heaton.blelibrary.ble.model.ScanRecord;
import cn.com.heaton.blelibrary.ble.utils.Utils;
import cn.com.heaton.blelibrary.ble.utils.UuidUtils;

public class BleConnectActivity extends AppActivity {
    private Ble<BleRssiDevice> ble = Ble.getInstance();
    private List<BleRssiDevice> bleRssiDevices = new ArrayList<>(); //搜索到的蓝牙列表   rssi为信号强度
    private List<BluetoothGattService> gattServices = new ArrayList<>();//蓝牙服务
    /**
     * 蓝牙搜索回调
     */
    private BleScanCallback<BleRssiDevice> scanCallback = new BleScanCallback<BleRssiDevice>() {
        @Override
        public void onLeScan(final BleRssiDevice device, int rssi, byte[] scanRecord) {
            synchronized (ble.getLocker()) {
                for (int i = 0; i < bleRssiDevices.size(); i++) {
                    BleRssiDevice rssiDevice = bleRssiDevices.get(i);
                    if (TextUtils.equals(rssiDevice.getBleAddress(), device.getBleAddress())){
                        return;
                    }
                }
                device.setScanRecord(ScanRecord.parseFromBytes(scanRecord));
                Log.e("yidao", device.getBleName() +"");
                if (device.getBleName() != null && device.getBleName().contains("ROBAM_RC906")) {
                    bleRssiDevices.add(device);
                }
            }
        }

        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };

    @Subscribe
    public void onEvent(DeviceFindEvent deviceFindEvent) {
        Log.e("onEvent", "onEvent: DeviceFindEventDeviceFindEvent" );
        DeviceInfo deviceInfo = deviceFindEvent.deviceInfo;
        addKettle(deviceInfo);

    }

    @OnClick({R.id.connect})
    public void onConnect() {
        if (bleRssiDevices.size() == 0) {
            return;
        }
        /**
         * 连接蓝牙
         */
        ble.connect(bleRssiDevices.get(0), new BleConnectCallback<BleRssiDevice>() {
            @Override
            public void onConnectionChanged(BleRssiDevice device) {
                if (device.isConnected()) {
                    ToastUtils.showShort("已连接");
                } else if (device.isConnecting()){
                    ToastUtils.showShort("连接中...");
                } else if (device.isDisconnected()){
                    ToastUtils.showShort("未连接");
                }
            }

            @Override
            public void onServicesDiscovered(BleRssiDevice device, final BluetoothGatt gatt) {
                super.onServicesDiscovered(device, gatt);
                gatt.requestMtu(200);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gattServices.clear();
                        gattServices.addAll(gatt.getServices());
                    }
                });
            }
        });
    }


    @OnClick({R.id.send})
    public void onSend() {
        if (bleRssiDevices.size() == 0) {
            return;
        }
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
            wifiInfoMap.put("userid", Plat.accountService.getCurrentUserId()+"");
            wifiInfoMap.put("apptype", "RKIOS");
            wifiInfoMap.put("appid", CommonService.getInstance().getAppId());
            wifiInfoMap.put("ssid", "物联研究院");
            wifiInfoMap.put("pwd", "rokitest2021");

            String wifiInfoGson = JSON.toJSONString(wifiInfoMap);
            Log.e("onEvent", "onSend: "+wifiInfoGson );
            final BluetoothGattCharacteristic read = readChar;
            /**
             * 给设备写入消息
             */

            ble.writeByUuid(bleRssiDevices.get(0), wifiInfoGson.getBytes(), writeChar.getService().getUuid(), writeChar.getUuid(), new BleWriteCallback<BleRssiDevice>() {
                @Override
                public void onWriteSuccess(BleRssiDevice device, BluetoothGattCharacteristic characteristic) {
                    Log.e("onEvent", "onWriteSuccess: " );
                    if (read != null) {
                        Log.e("onEvent", "read: " );
                        ble.readByUuid(bleRssiDevices.get(0), read.getService().getUuid(), read.getUuid(), new BleReadCallback<BleRssiDevice>() {
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
                    Log.e("onEvent", "onWriteFailed: " );
                }
            });
        }
    }

//    @Override
//    protected void onCreate(Bundle savedState) {
//        super.onCreate(savedState);
//        setContentView(R.layout.activity_ble_connect);
//        ButterKnife.inject(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            int selfPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
//            if (selfPermission == 0) {
//                checkBlueStatus();
//            } else {
//                PermissionsUtils.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, PermissionsUtils.CODE_BLUE_TOOTH);
//            }
//        } else {
//            checkBlueStatus();
//        }
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ble_connect;
    }

    @Override
    protected void initView() {
        ButterKnife.inject(this);
        EventUtils.regist(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int selfPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (selfPermission == 0) {
                checkBlueStatus();
            } else {
                PermissionsUtils.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, PermissionsUtils.CODE_BLUE_TOOTH);
            }
        } else {
            checkBlueStatus();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        EventUtils.unregist(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (ble != null && ble.isScanning()) {
            ble.stopScan();
        }
        super.onBackPressed();
    }

    private void rescan() {
        if (ble != null && !ble.isScanning()) {
            bleRssiDevices.clear();
            ble.startScan(scanCallback);
        }
    }

    //检查蓝牙是否支持及打开
    private void checkBlueStatus() {
        if (!ble.isSupportBle(this)) {
            finish();
        }
        if (!ble.isBleEnable()) {
            ToastUtils.showShort("请打开蓝牙！");
        }else {
            checkGpsStatus();
        }
    }

    private void checkGpsStatus(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !Utils.isGpsOpen(BleConnectActivity.this)){
            new AlertDialog.Builder(BleConnectActivity.this)
                    .setTitle("提示")
                    .setMessage("为了更精确的扫描到Bluetooth LE设备,请打开GPS定位")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            BleConnectActivity.this.startActivityForResult(intent, PermissionsUtils.CODE_WIFI_SSID);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create()
                    .show();
        }else {
            ble.startScan(scanCallback);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (PermissionsUtils.CODE_BLUE_TOOTH == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    checkBlueStatus();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == Ble.REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
        } else if (requestCode == Ble.REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            ble.startScan(scanCallback);
        }else if (requestCode == PermissionsUtils.CODE_WIFI_SSID){

        }
        super.onActivityResult(requestCode, resultCode, data);
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
                            UIService.getInstance().popBack().popBack().popBack();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });

    }
}
