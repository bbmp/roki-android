package com.robam.roki.ui.page.device;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IAppType;
import com.legent.plat.events.AbsDeviceInfoEvent;
import com.legent.plat.events.DeviceFindEvent;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.pojos.dictionary.DeviceType;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.EventUtils;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.WifiAutoConnectManager;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.api.WifiUtils;
import com.robam.common.events.DeviceEasylinkCompletedEvent;
import com.robam.roki.R;
import com.robam.roki.listener.IUdpServiceListener;
import com.robam.roki.model.bean.CodeBean;
import com.robam.roki.service.UdpService;
import com.robam.roki.ui.PageArgumentKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robam.roki.ui.view.networkoptimization.WifiConnectPage.isContent;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/4/13.
 * PS: Udp配网页面.
 */
public class DeviceUdpContentPage extends BasePage {
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    @InjectView(R.id.tv_device_add_succeed_and_failed)
    TextView mTvDeviceAddSucceedAndFailed;
    @InjectView(R.id.tv_device_transfer_succeed_and_failed)
    TextView mTvDeviceTransferSucceedAndFailed;
    @InjectView(R.id.tv_device_link_network_succeed_and_failed)
    TextView mTvDeviceLinkNetworkSucceedAndFailed;
    @InjectView(R.id.tv_device_bind_account_succeed_and_failed)
    TextView mTvDeviceBindAccountSucceedAndFailed;
    @InjectView(R.id.iv_device_transfer_succeed_and_failed)
    ImageView mIvDeviceTransferSucceedAndFailed;
    @InjectView(R.id.ll_device_transfer_succeed_and_failed)
    LinearLayout mLlDeviceTransferSucceedAndFailed;
    @InjectView(R.id.iv_device_link_network_succeed_and_failed)
    ImageView mIvDeviceLinkNetworkSucceedAndFailed;
    @InjectView(R.id.ll_device_link_network_succeed_and_failed)
    LinearLayout mLlDeviceLinkNetworkSucceedAndFailed;
    @InjectView(R.id.iv_device_bind_account_succeed_and_failed)
    ImageView mIvDeviceBindAccountSucceedAndFailed;
    @InjectView(R.id.ll_device_bind_account_succeed_and_failed)
    LinearLayout mLlDeviceBindAccountSucceedAndFailed;
    private String mSsid;
    private String mPwd;

    Thread sendThread;
    Thread reciverThread;

    private boolean isReciver = true;
    private int tag = 9;
    private WifiAutoConnectManager mWifiAutoConnectManager;


    @SuppressLint("HandlerLeak")
    MyHandler mHandler = new MyHandler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //向设备传输信息成功
                case 0:
                    tag = 0;
//                    ConnectBeforeWifi();
                    deviceTransferSucceed();
                    break;
                //向设备传输信息失败
                case 1:
                    tag = 1;
                    deviceTransferFailed();
                    break;
                //设备绑定账号成功
                //添加设备成功
                case 2:
                    tag = 2;
                    deviceBindAccountSucceed((DeviceInfo) msg.obj);
                    break;
                //设备绑定账号失败
                //添加设备失败
                case 3:
                    tag = 3;
                    deviceBindAccountFailed();
                    break;


                //超时3分钟 提示联网失败
                case 4:
                    stopTimer();
                    switch (tag) {
                        case 0:
                            //设备连接网络失败
                            deviceConnectFailed();
                            break;
                        case 1:
                            deviceTransferFailed();
                            break;
                        case 3:
                            deviceBindAccountFailed();
                            break;
                        case 9:
                            //向设备传输信息失败
                            deviceTransferFailed();
                            break;
                        default:
                            break;
                    }


                    break;
                default:
                    break;

            }
        }
    };
//
//    private void ConnectBeforeWifi() {
//        WifiManager  mWIFIManager = (WifiManager) cx.getSystemService(Context.WIFI_SERVICE);
//        String ssid = PreferenceUtils.getString(PrefsKey.Ssid, "");
//        String pwd = PreferenceUtils.getString(PrefsKey.SsidPwd, "");
//        Log.d("2019年5月29日14:08:01",ssid+"==="+pwd);
//        //获取网络安全性
//        int type = WifiUtils.getCipherType(scanResultLists.get(i));
//        WifiConfiguration configuration = WifiAutoConnectManager.configWifiInfo(cx, ssid, pwd, type);
//        int netId = configuration.networkId;
//        if (netId == -1) {
//            netId = mWIFIManager.addNetwork(configuration);
//        }
//        mWIFIManager.enableNetwork(netId, true);
//    }

    /**
     * 第二步：设备连接网络失败
     */

    private void deviceConnectFailed() {
        if (mLlDeviceTransferSucceedAndFailed != null) {

            mLlDeviceTransferSucceedAndFailed.setVisibility(View.VISIBLE);
        }
        if (mLlDeviceLinkNetworkSucceedAndFailed != null) {

            mLlDeviceLinkNetworkSucceedAndFailed.setVisibility(View.VISIBLE);
        }
        if (mIvDeviceLinkNetworkSucceedAndFailed != null) {

            mIvDeviceLinkNetworkSucceedAndFailed.setImageResource(R.mipmap.ic_content_faite);
        }
        if (mTvDeviceLinkNetworkSucceedAndFailed != null) {

            mTvDeviceLinkNetworkSucceedAndFailed.setText("设备连接网络失败");
        }

        if (mTvDeviceAddSucceedAndFailed != null) {

            mTvDeviceAddSucceedAndFailed.setText("设备连接网络失败");
        }
    }

    /**
     * 第三步：设备绑定账号失败
     */
    private void deviceBindAccountFailed() {
        if (mLlDeviceBindAccountSucceedAndFailed != null) {

            mLlDeviceBindAccountSucceedAndFailed.setVisibility(View.VISIBLE);
        }
        if (mIvDeviceBindAccountSucceedAndFailed != null) {

            mIvDeviceBindAccountSucceedAndFailed.setImageResource(R.mipmap.ic_content_faite);
        }
        if (mTvDeviceBindAccountSucceedAndFailed != null) {

            mTvDeviceBindAccountSucceedAndFailed.setText(R.string.device_account_bind_failed_text);
        }
        if (mTvDeviceAddSucceedAndFailed != null) {

            mTvDeviceAddSucceedAndFailed.setText(R.string.device_link_failed_result_text);
        }

    }

    /**
     * 第三步：绑定账号成功 设备添加成功
     */
    private void deviceBindAccountSucceed(DeviceInfo devInfo) {
        if (mLlDeviceBindAccountSucceedAndFailed == null) return;
        mLlDeviceBindAccountSucceedAndFailed.setVisibility(View.VISIBLE);
        mIvDeviceBindAccountSucceedAndFailed.setImageResource(R.mipmap.ic_content_succ);
        mTvDeviceBindAccountSucceedAndFailed.setText(R.string.device_account_bind_succeed_text);
        mTvDeviceAddSucceedAndFailed.setText(R.string.device_link_succeed_result_text);
        ToastUtils.showShort(R.string.add_device_failure);
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


    private void deviceTransferFailed() {
        if (mLlDeviceTransferSucceedAndFailed != null) {

            mLlDeviceTransferSucceedAndFailed.setVisibility(View.VISIBLE);
        }
        if (mIvDeviceTransferSucceedAndFailed != null) {

            mIvDeviceTransferSucceedAndFailed.setImageResource(R.mipmap.ic_content_faite);
        }
        if (mTvDeviceTransferSucceedAndFailed != null) {

            mTvDeviceTransferSucceedAndFailed.setText(R.string.device_transmit_failed_text);
        }
        if (mLlDeviceLinkNetworkSucceedAndFailed != null) {

            mLlDeviceLinkNetworkSucceedAndFailed.setVisibility(View.INVISIBLE);
        }
        if (mLlDeviceBindAccountSucceedAndFailed != null) {

            mLlDeviceBindAccountSucceedAndFailed.setVisibility(View.INVISIBLE);
        }
        if (mTvDeviceAddSucceedAndFailed != null) {

            mTvDeviceAddSucceedAndFailed.setText(R.string.device_link_failed_result_text);
        }
    }

    /**
     * 第一步：向设备传输信息成功
     */
    private void deviceTransferSucceed() {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (mLlDeviceTransferSucceedAndFailed!=null) {

                mLlDeviceTransferSucceedAndFailed.setVisibility(View.VISIBLE);
            }
            if (mIvDeviceTransferSucceedAndFailed!=null) {

                mIvDeviceTransferSucceedAndFailed.setImageResource(R.mipmap.ic_content_succ);
            }

            if (mTvDeviceTransferSucceedAndFailed!=null) {

                mTvDeviceTransferSucceedAndFailed.setText(R.string.device_transmit_succeed_text);
            }
            if (mLlDeviceLinkNetworkSucceedAndFailed!=null) {

                mLlDeviceLinkNetworkSucceedAndFailed.setVisibility(View.VISIBLE);
            }

            if (mIvDeviceLinkNetworkSucceedAndFailed!=null) {
                mIvDeviceLinkNetworkSucceedAndFailed.setImageResource(R.mipmap.ic_content_current);
            }
            if (mTvDeviceLinkNetworkSucceedAndFailed!=null) {

                mTvDeviceLinkNetworkSucceedAndFailed.setText(R.string.device_link_network_text);
            }
            if (mLlDeviceBindAccountSucceedAndFailed!=null) {

                mLlDeviceBindAccountSucceedAndFailed.setVisibility(View.INVISIBLE);
            }
            if (mTvDeviceAddSucceedAndFailed!=null) {

                mTvDeviceAddSucceedAndFailed.setText(R.string.device_link_possible_router_text);
            }

        }
    }


    @Subscribe
    public void onEvent(AbsDeviceInfoEvent event) {
//        if (isReciver) {
//            mIvDeviceLinkNetworkSucceedAndFailed.setImageResource(R.mipmap.ic_content_succ);
//            mTvDeviceLinkNetworkSucceedAndFailed.setText(R.string.device_link_network_succeed_text);
//            DeviceInfo deviceInfo = event.deviceInfo;
//            addKettle(deviceInfo);
//        }else{
//            ToastUtils.show("连网超时，请重试！",Toast.LENGTH_SHORT);
//        }

    }

    /**
     * 接收到设备发送过来的mqtt
     */
    @Subscribe
    public void onEvent(DeviceFindEvent deviceFindEvent) {
//        if (isReciver) {
            mIvDeviceLinkNetworkSucceedAndFailed.setImageResource(R.mipmap.ic_content_succ);
            mTvDeviceLinkNetworkSucceedAndFailed.setText(R.string.device_link_network_succeed_text);
            DeviceInfo deviceInfo = deviceFindEvent.deviceInfo;
            addKettle(deviceInfo);
//        } else {
//            ToastUtils.show("连网超时，请重试！", Toast.LENGTH_SHORT);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mSsid = bundle.getString(PageArgumentKey.Ssid);
            mPwd = bundle.getString(PageArgumentKey.Pwd);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_udp_content, container, false);
        ButterKnife.inject(this, view);
        WifiManager wifiManager = (WifiManager) cx.getSystemService(Context.WIFI_SERVICE);
        mWifiAutoConnectManager = new WifiAutoConnectManager(wifiManager);
        initData();
        //开始计时
        startTimer();
        return view;
    }

    /**
     * 向设备发送upd数据报文
     */
    private void initData() {
        mLlDeviceTransferSucceedAndFailed.setVisibility(View.VISIBLE);
        mIvDeviceTransferSucceedAndFailed.setImageResource(R.mipmap.ic_content_current);
        mTvDeviceTransferSucceedAndFailed.setText(R.string.device_transmit_msg_text);
        long userId = Plat.accountService.getCurrentUserId();
        String appid = Plat.appGuid;
        LogUtils.i("20190417", "appid:" + appid);
        String newAppId = appid.substring(5, appid.length());
        Map<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("apptype", IAppType.RKDRD);
        stringStringMap.put("userid", String.valueOf(userId));
        stringStringMap.put("appid", newAppId);
        stringStringMap.put("ssid", mSsid);
        stringStringMap.put("pwd", mPwd);
        String data1 = "";
        try {
            data1 = JsonUtils.pojo2Json(stringStringMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String data = data1;
        final UdpService udpService = new UdpService();


        sendThread = new Thread(new Runnable() {
            @Override
            public void run() {

//                LogUtils.i("20190523", "sendThread _Thread");
//
//                if (isContent) {
//                    ToastUtils.show("isContent===连上了",Toast.LENGTH_SHORT);
//                }else{
//                    ToastUtils.show("isContent===没连上",Toast.LENGTH_SHORT);
//                }
                try {

                    while (isContent) {
                        Thread.sleep(1000);
                        udpService.send(data);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        sendThread.start();


        reciverThread = new Thread(new Runnable() {
            @Override
            public void run() {

                LogUtils.i("20190523", "reciverThread_Thread");
                while (isContent) {

                    udpService.receive(new IUdpServiceListener() {
                        @Override
                        public void onDeviceResult(String result) {
                            try {
                                CodeBean codeBean = JsonUtils.json2Pojo(result, CodeBean.class);
                                LogUtils.i("20190417", "code:" + codeBean.getCode());
                                if (0 == codeBean.getCode()) {
                                    mHandler.sendEmptyMessage(0);
                                } else {
                                    mHandler.sendEmptyMessage(1);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            isContent = false;
                            udpService.releaseUdp();
                        }
                    });
                }
            }
        });
        reciverThread.start();


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
                        Message message = mHandler.obtainMessage();
                        message.what = 2;
                        message.obj = devInfo;
                        mHandler.sendMessage(message);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        mHandler.sendEmptyMessage(3);
                        ToastUtils.showThrowable(t);
                    }
                });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isReciver = true;
        mHandler.removeCallbacks(sendThread);
        mHandler.removeCallbacks(reciverThread);
        if (sendThread != null) {
            sendThread = null;
        }
        if (reciverThread != null) {
            reciverThread = null;
        }
        ButterKnife.reset(this);


    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        UIService.getInstance().popBack();
    }


//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mHandler.removeCallbacks(sendThread);
//        mHandler.removeCallbacks(reciverThread);
//        if (sendThread != null) {
//            sendThread = null;
//        }
//        if (reciverThread != null) {
//            reciverThread = null;
//        }
//
//    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_BACK:
////                connectWifi();
//                break;
//            case KeyEvent.KEYCODE_MENU:
//                break;
//            default:
//                break;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    /**
     * 连上之前的wifi
     */
    private void connectWifi() {
        String ssid = WifiUtils.getCurrentSsid(Plat.app);
        String pwd = PreferenceUtils.getString(ssid, null);
        if (ssid == null) {
            return;
        }
        if (Strings.isNullOrEmpty(pwd)) {
            return;
        }
        //连接该wifi
        mWifiAutoConnectManager.connect(ssid, pwd,
                WifiAutoConnectManager.WifiCipherType.WIFICIPHER_WPA);

    }


    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            //240s结束计时 关闭弹框
            if (System.currentTimeMillis() - mLastActionTime > 240000) {
                removeTimer();
            }
        }
    }


    private Timer mTimer;
    private TimerTask mTimerTask;
    private long mLastActionTime;

    //开始计时
    private void startTimer() {
        mTimer = new Timer(true);
        mTimerTask = new MyTimerTask();
        //延时1000ms后执行，1000ms检查一次
        mTimer.schedule(mTimerTask, 0, 1000);
        //初始化上次操作时间为登录成功的时间
        mLastActionTime = System.currentTimeMillis();
    }

    //倒计时完毕的操作
    private void removeTimer() {
        Message message = new Message();
        message.what = 4;
        mHandler.sendMessage(message);
    }

    // 停止计时任务
    private void stopTimer() {
        isReciver = false;
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }


}
