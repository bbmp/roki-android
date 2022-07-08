package com.robam.roki.ui.view.networkoptimization;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleMtuChangedCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IAppType;
import com.legent.plat.events.DeviceFindEvent;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.pojos.dictionary.DeviceType;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.utils.EventUtils;
import com.legent.utils.api.ToastUtils;
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
import cn.com.heaton.blelibrary.ble.model.ScanRecord;
import cn.com.heaton.blelibrary.ble.utils.Utils;
import cn.com.heaton.blelibrary.ble.utils.UuidUtils;

public class BleConnectTestActivity extends AppActivity {
    private String TAG = "蓝牙连接日志";
    List<BluetoothDevice> bluetoothDeviceList = new ArrayList<>();
    private List<BluetoothGattService> gattServices = new ArrayList<>();//蓝牙服务
    private List<BleRssiDevice> bleRssiDevices = new ArrayList<>(); //搜索到的蓝牙列表   rssi为信号强度

    List<BleDevice> resultList = new ArrayList<>();
    private Ble<BleRssiDevice> ble = Ble.getInstance();

    /**
     * 蓝牙搜索回调
     */
    private cn.com.heaton.blelibrary.ble.callback.BleScanCallback<BleRssiDevice> scanCallback = new cn.com.heaton.blelibrary.ble.callback.BleScanCallback<BleRssiDevice>() {
        @Override
        public void onLeScan(final BleRssiDevice device, int rssi, byte[] scanRecord) {
            synchronized (ble.getLocker()) {
                for (int i = 0; i < bleRssiDevices.size(); i++) {
                    BleRssiDevice rssiDevice = bleRssiDevices.get(i);
                    if (TextUtils.equals(rssiDevice.getBleAddress(), device.getBleAddress())) {
                        return;
                    }
                }
                device.setScanRecord(ScanRecord.parseFromBytes(scanRecord));
                Log.e("yidao", device.getBleName() + "");
                if (device.getBleName() != null && device.getBleName().contains("ROBAM_")) {
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ble_connect;
    }

    private BluetoothDevice bluetoothDevice = null;

    @Override
    protected void initView() {
//        BleScanUtils.getGetInstance().startScanBle(this);
//        BleScanUtils.getGetInstance().setOnListener(bluetoothDevices -> {
//            Log.e(TAG, "onCreate: 附近设备：" + bluetoothDevices.size());
//            for (BluetoothDevice bd : bluetoothDevices){
//                if(bd.getName()!=null){
//                    Log.e(TAG, "onCreate: " + bd.getName());
//                    if(bd.getName().contains("_1")){
//                        bluetoothDevice=bd;
//                    }
//                }
//            }
//            return null;
//        });


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
        if (BleManager.getInstance().isBlueEnable()) {
            ToastUtils.show("蓝牙已开启", Toast.LENGTH_SHORT);
            BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
//                    .setServiceUuids()      // 只扫描指定的服务的设备，可选
//                    .setDeviceName(true, names)         // 只扫描指定广播名的设备，可选
//                    .setDeviceMac(mac)                  // 只扫描指定mac的设备，可选
                    .setAutoConnect(false)      // 连接时的autoConnect参数，可选，默认false
                    .setScanTimeOut(10000)              // 扫描超时时间，可选，默认10秒
                    .build();
            BleManager.getInstance().initScanRule(scanRuleConfig);
        } else {
            ToastUtils.show("蓝牙未开启", Toast.LENGTH_SHORT);
            //通过蓝牙适配器直接打开蓝牙。
            BleManager.getInstance().enableBluetooth();
        }

    }


    @OnClick({R.id.connect})
    public void onConnect() {

//        ble.startScan(scanCallback);
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {

            }

            @Override
            public void onScanning(BleDevice bleDevice) {
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
//                resultList.addAll(scanResultList);
                resultList.clear();
                for (int i = 0; i < scanResultList.size(); i++) {
                    if (scanResultList.get(i).getName() != null && scanResultList.get(i).getName().contains("ROBAM_")) {
                        resultList.add(scanResultList.get(i));

                        Log.d(TAG, scanResultList.get(i).getName());

                    }
                }
                ToastUtils.show("蓝牙扫描完成", Toast.LENGTH_SHORT);

            }
        });
    }

    @OnClick({R.id.send})
    public void onSend() {
        //停止扫描
//        mClient.stopSearch();

        BleDevice bleDevice = null;
        for (int i = 0; i < resultList.size(); i++) {
            if (resultList.get(i).getName().contains("_1")) {
                bleDevice = resultList.get(i);
            }
        }

        if (bleDevice != null) {
            BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
                @Override
                public void onStartConnect() {
                    ToastUtils.show("开始连接蓝牙", Toast.LENGTH_SHORT);
                }

                @Override
                public void onConnectFail(BleDevice bleDevice, BleException e) {
                    ToastUtils.show("连接蓝牙失败" + e.getDescription(), Toast.LENGTH_SHORT);
                }

                @Override
                public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                    ToastUtils.show("连接蓝成功", Toast.LENGTH_SHORT);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(gatt.requestMtu(200)){
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
                            Log.d(TAG, "设置MTU失败");
                        }

                        @Override
                        public void onMtuChanged(int mtu) {
                            // 设置MTU成功，并获得当前设备传输支持的MTU值
                            Log.d(TAG, "设置MTU成功mtu:" + mtu);
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
                    gattServices.clear();
                    gattServices.addAll(bluetoothGatt.getServices());

                }

                @Override
                public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                }
            });
        }

    }

    public void onSend(BleDevice bleDevice) {
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
            wifiInfoMap.put("apptype", IAppType.RKDRD);
            String appid = Plat.appGuid;
            String newAppId = appid.substring(5, appid.length());
            wifiInfoMap.put("appid", newAppId);
            wifiInfoMap.put("ssid", "yanjiusuo");
            wifiInfoMap.put("pwd", "rokitest2021");
//            wifiInfoMap.put("ssid", "DKB001");
//            wifiInfoMap.put("pwd", "robamDKB");

            String wifiInfoGson = JSON.toJSONString(wifiInfoMap);
            Log.e(TAG, "onSend: " + wifiInfoGson);
//            final BluetoothGattCharacteristic read = readChar;
            /**
             * 给设备写入消息
             */
//            BleRssiDevice writeBleRssiDevice = new BleRssiDevice(bleDevice.getMac(), bleDevice.getName());
//            writeBleRssiDevice.setScanRecord(ScanRecord.parseFromBytes(bleDevice.getScanRecord()));
//            writeBleRssiDevice.setRssi(bleDevice.getRssi());
//            ble.writeByUuid(writeBleRssiDevice, wifiInfoGson.getBytes(), writeChar.getService().getUuid(), writeChar.getUuid(), new BleWriteCallback<BleRssiDevice>() {
//                @Override
//                public void onWriteSuccess(BleRssiDevice device, BluetoothGattCharacteristic characteristic) {
//                    Log.e("onEvent", "onWriteSuccess: ");
//                    if (read != null) {
//                        Log.e("onEvent", "read: ");
//                        ble.readByUuid(device, read.getService().getUuid(), read.getUuid(), new BleReadCallback<BleRssiDevice>() {
//                            @Override
//                            public void onReadSuccess(BleRssiDevice dedvice, BluetoothGattCharacteristic characteristic) {
//                                super.onReadSuccess(dedvice, characteristic);
//                                Log.e("onEvent", "onReadSuccess: ");
//
//                            }
//
//                            @Override
//                            public void onReadFailed(BleRssiDevice device, int failedCode) {
//                                super.onReadFailed(device, failedCode);
//                                Log.e("onEvent", "onReadFailed: ");
//                            }
//                        });
//                    }
//                }
//
//                @Override
//                public void onWriteFailed(BleRssiDevice device, int failedCode) {
//                    Log.e("onEvent", "onWriteFailed: ");
//                }
//            });


            BleManager.getInstance().write(
                    bleDevice,
                    writeChar.getService().getUuid().toString(),
//                    UuidUtils.uuid16To128("fd00"),
                    writeChar.getUuid().toString(),
//                    UuidUtils.uuid16To128("fed7"),
                    wifiInfoGson.getBytes(),
                    false,
                    new com.clj.fastble.callback.BleWriteCallback() {
                        @Override
                        public void onWriteSuccess(int i, int i1, byte[] bytes) {
                            Log.d(TAG, "onWriteSuccess");
//                            resultList.get(0).getName().substring("_");
                            String strGuid = bleDevice.getName().substring(6, bleDevice.getName().length() - 2);
                            Log.d(TAG, "strGuid:" + strGuid);
//                            addKettle(strGuid);
                        }

                        @Override
                        public void onWriteFailure(BleException e) {

                        }
                    });

        }
    }

    @Subscribe
    public void onEvent(DeviceFindEvent deviceFindEvent) {
        Log.e("onEvent", "onEvent: DeviceFindEventDeviceFindEvent");
        DeviceInfo deviceInfo = deviceFindEvent.deviceInfo;
//        addKettle(deviceInfo);
    }

    private void addKettle(final String strGuid) {

        DeviceType dt = DeviceTypeManager.getInstance().getDeviceType(strGuid);

        Plat.deviceService.addWithBind(strGuid, dt.getName(),
                true, new VoidCallback() {

                    @Override
                    public void onSuccess() {
                        // UIService.getInstance().returnHome();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
//                            UIService.getInstance().popBack().popBack().popBack();
                            Log.d(TAG, "添加成功");
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });

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
        super.onBackPressed();
    }


    //检查蓝牙是否支持及打开
    private void checkBlueStatus() {
//        if (!ble.isSupportBle(this)) {
//            finish();
//        }
//        if (!ble.isBleEnable()) {
//            ToastUtils.showShort("请打开蓝牙！");
//        }else {
//            checkGpsStatus();
//        }
    }

    private void checkGpsStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !Utils.isGpsOpen(BleConnectTestActivity.this)) {
            new AlertDialog.Builder(BleConnectTestActivity.this)
                    .setTitle("提示")
                    .setMessage("为了更精确的扫描到Bluetooth LE设备,请打开GPS定位")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            BleConnectTestActivity.this.startActivityForResult(intent, PermissionsUtils.CODE_WIFI_SSID);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create()
                    .show();
        } else {
//            ble.startScan(scanCallback);
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
//            ble.startScan(scanCallback);
        } else if (requestCode == PermissionsUtils.CODE_WIFI_SSID) {

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
