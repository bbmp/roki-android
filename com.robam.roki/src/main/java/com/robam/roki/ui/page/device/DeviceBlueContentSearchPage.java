package com.robam.roki.ui.page.device;

import static com.legent.utils.api.WifiUtils.BlueConnectTimeOut;
import static com.legent.utils.api.WifiUtils.BlueScanTimeOut;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.events.PageBackEvent;
import com.legent.services.TaskService;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter3.RvDeviceBlueSearchAdapter;
import com.robam.roki.ui.extension.GlideApp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 搜索蓝牙设备
 */
public class DeviceBlueContentSearchPage extends BasePage {


    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_device_model_name)
    TextView tvDeviceModelName;
    @InjectView(R.id.img_search)
    ImageView imgSearch;
    @InjectView(R.id.tv_search)
    TextView tvSearch;
    @InjectView(R.id.tv_search_tips)
    TextView tvSearchTips;
    @InjectView(R.id.tv_try_again)
    Button tvTryAgain;
    @InjectView(R.id.ll_fail_page)
    LinearLayout llFailPage;
    @InjectView(R.id.rl_device_list)
    RecyclerView rlDeviceList;
    @InjectView(R.id.tv_add_tips)
    TextView tvAddTips;

    private String mSsid;
    private String mPwd;
    String dt = null;

    //    private List<BluetoothGattService> gattServices = new ArrayList<>();//蓝牙服务
    private List<BleDevice> bleRssiDevices = new ArrayList<>(); //搜索到的蓝牙列表   rssi为信号强度
    private String SearchKey = "ROBAM_";

    //获取设备联网列表
//    List<List<DeviceItemList>> sumDeviceList = new ArrayList<List<DeviceItemList>>();

    /**
     * 显示设备
     */
    private RvDeviceBlueSearchAdapter rvDeviceAdapter;

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
                            if (bleRssiDevices.size() == 0) {
                                BleManager.getInstance().cancelScan();
                                if (tvTryAgain != null) {
                                    rvDeviceAdapter.notifyDataSetChanged();
                                    tvTryAgain.setVisibility(View.VISIBLE);
                                    tvAddTips.setVisibility(View.GONE);
                                    tvSearch.setText("未搜索到设备");
                                    imgSearch.setImageResource(R.mipmap.icon_search_fail);
                                }
                            }else{
                                startCountdown(BlueConnectTimeOut);
                                scanDevice();
                            }
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
        View view = inflater.inflate(R.layout.page_blue_content_search, container, false);
        ButterKnife.inject(this, view);
        Bundle bb = getArguments();
        if (bb != null) {
            mSsid = bb.getString(PageArgumentKey.Ssid);
            mPwd = bb.getString(PageArgumentKey.Pwd);
            dt = bb.getString(PageArgumentKey.Guid);
            SearchKey = dt == null ? SearchKey : dt;
        }

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

        rlDeviceList.setLayoutManager(new LinearLayoutManager(cx, LinearLayoutManager.HORIZONTAL, false));
        rvDeviceAdapter = new RvDeviceBlueSearchAdapter();
        rvDeviceAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                BleDevice item = rvDeviceAdapter.getItem(position);

                Bundle bundle = new Bundle();
//                    bundle.putString(PageArgumentKey.wifiName, name);
                bundle.putString(PageArgumentKey.Ssid, mSsid);
                bundle.putString(PageArgumentKey.Pwd, mPwd);
//                    bundle.putString(PageArgumentKey.Guid, dt);
                bundle.putParcelable("BleRssiDevice", item);

                UIService.getInstance().postPage(PageKey.DeviceBlueContent, bundle);
            }
        });
        rlDeviceList.setAdapter(rvDeviceAdapter);

        startCountdown(BlueConnectTimeOut);
        scanDevice();

        return view;
    }

    private void scanDevice() {
//        Glide.with(cx).asGif().load(R.drawable.blue_search_device)
//                .into(imgSearch);
        try {

            GlideApp.with(getContext())
                    .asGif()
                    .load(R.drawable.blue_search_device)
                    .into(imgSearch);

            BleManager.getInstance().cancelScan();

            BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                    .setAutoConnect(false)      // 连接时的autoConnect参数，可选，默认false
                    .setScanTimeOut(BlueScanTimeOut)              // 扫描超时时间，可选，默认10秒
                    .build();
            BleManager.getInstance().initScanRule(scanRuleConfig);
            bleRssiDevices.clear();
            if(rvDeviceAdapter!=null){
                rvDeviceAdapter.getData().clear();
                rvDeviceAdapter.notifyDataSetChanged();
            }
            tvAddTips.setVisibility(View.GONE);
            tvDeviceModelName.setText("查找设备");
            tvSearch.setText("搜索中");
            tvTryAgain.setVisibility(View.GONE);

            BleManager.getInstance().scan(new BleScanCallback() {
                @Override
                public void onScanStarted(boolean success) {

                }

                @Override
                public void onLeScan(BleDevice bleDevice) {
                    if (bleDevice.getName() != null && bleDevice.getName().contains(SearchKey) && bleDevice.getName().contains("_1")) {
                        Log.d("20220401", "onLeScan:  " + bleDevice.getName() + "  Rssi:" + bleDevice.getRssi());
                        if (bleRssiDevices.isEmpty()) {
                            if (bleDevice.getRssi() > -80) {
                                tvDeviceModelName.setText("选择设备");
                                tvAddTips.setVisibility(View.VISIBLE);
                                bleRssiDevices.add(bleDevice);
                                if (rvDeviceAdapter != null) {
                                    rvDeviceAdapter.addData(bleDevice);
//                                rvDeviceAdapter.addData(setBlueDeviceIcon(bleDevice));
                                }
                            }
                        }
                        boolean isExist = false;
                        for (int i = 0; i < bleRssiDevices.size(); i++) {
                            if (bleRssiDevices.get(i).getMac().equals(bleDevice.getMac())) {
                                isExist = true;
                                break;
                            }
                        }
                        if (bleDevice.getRssi() > -80 && !isExist) {
                            bleRssiDevices.add(bleDevice);
                            if (rvDeviceAdapter != null) {
                                rvDeviceAdapter.addData(bleDevice);
//                            rvDeviceAdapter.addData(setBlueDeviceIcon(bleDevice));
                            }
                        }
                    }
                }

                @Override
                public void onScanning(BleDevice bleDevice) {

                }

                @Override
                public void onScanFinished(List<BleDevice> scanResultList) {
                    LogUtils.i("20220401", "scanResultList:" + scanResultList.size());
                    List<BleDevice> scanDevices = new ArrayList<>();
                    for (int i = 0; i < scanResultList.size(); i++) {
                        BleDevice bd = scanResultList.get(i);
                        if (bd != null && bd.getName() != null && bd.getName().contains("ROBAM_") && bd.getName().contains("_1")) {
                            scanDevices.add(bd);
                        }
                    }
                    if (scanDevices.isEmpty()) {
                            bleRssiDevices.clear();
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
                    scanDevice();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
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
    @OnClick({R.id.iv_back, R.id.tv_try_again})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                UIService.getInstance().popBack();
                break;
            case R.id.tv_try_again:
                startCountdown(BlueConnectTimeOut);
                scanDevice();
                tvDeviceModelName.setText("查找设备");
                tvSearch.setText("搜索中");
                tvTryAgain.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        BleManager.getInstance().cancelScan();
        BleManager.getInstance().destroy();
    }

    @Subscribe
    public void onEvent(PageBackEvent event) {
        if ("DeviceBlueContentPage".equals(event.getPageName())){
            scanDevice();
        }
    }
}
