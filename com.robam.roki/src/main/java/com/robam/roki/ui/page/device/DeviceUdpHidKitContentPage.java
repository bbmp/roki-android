package com.robam.roki.ui.page.device;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IAppType;
import com.legent.plat.events.DeviceFindEvent;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.pojos.dictionary.DeviceType;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.ui.IForm;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.EventUtils;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.WifiAutoConnectManager;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.api.WifiUtils;
import com.robam.common.events.DeviceEasylinkCompletedEvent;
import com.robam.common.io.cloud.IRokiRestService;
import com.robam.roki.R;
import com.robam.roki.listener.IUdpServiceListener;
import com.robam.roki.model.bean.CodeBean;
import com.robam.roki.service.UdpService;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.mdialog.MessageDialog;
import com.robam.roki.ui.view.networkoptimization.WIFIConnectFailPage;
import com.robam.roki.ui.widget.base.BaseDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robam.roki.ui.view.networkoptimization.WifiConnectPage.isContent;

//import android.net.wifi.WifiNetworkSpecifier;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2019/4/13.
 * PS: Udp配网页面.
 */
public class DeviceUdpHidKitContentPage extends BasePage {
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.img_bg)
    ImageView imgBg;

    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    @InjectView(R.id.ll_message)
    RelativeLayout llMessage;


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
    @InjectView(R.id.tv_common)
    TextView tvCommon;
    @InjectView(R.id.tv_common_2)
    TextView tvCommon2;
    @InjectView(R.id.btn_common)
    Button btnCommon;
    @InjectView(R.id.img_dot)
    ImageView imgDot;
    private String mSsid;
    private String wifiName;
    private String mPwd;
    private String mGuid;

    Thread sendThread;
    Thread reciverThread;
    String ssidContent = null;
    private boolean isReciver = false;
    private int tag = 9;
    private WifiAutoConnectManager mWifiAutoConnectManager;
    private AnimationDrawable mAnimationDrawable;
    private WifiManager mWifiManager;

    @SuppressLint("HandlerLeak")
    MyHandler mHandler = new MyHandler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);


            switch (msg.what) {
                //向设备传输信息成功
                case 0:
                    tag = 0;
//                    ConnectBeforeWifi();
                    deviceTransferSucceed();
                    mHandler.sendEmptyMessageDelayed(999, 2000);
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
                case 999:
                    connectWifi();
                    break;
                default:
                    break;

            }

        }
    };

    /**
     * 第二步：设备连接网络失败
     */

    private void deviceConnectFailed() {
        if (mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
            mAnimationDrawable = null;
        }
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
            setingText2();
        }


    }

    /**
     * 第三步：设备绑定账号失败
     */
    private void deviceBindAccountFailed() {
        if (mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
            mAnimationDrawable = null;
        }
        if (mLlDeviceBindAccountSucceedAndFailed != null) {

            mLlDeviceBindAccountSucceedAndFailed.setVisibility(View.VISIBLE);
        }
        if (mIvDeviceBindAccountSucceedAndFailed != null) {

            mIvDeviceBindAccountSucceedAndFailed.setImageResource(R.mipmap.ic_content_faite);
        }
        if (mTvDeviceBindAccountSucceedAndFailed != null) {

            mTvDeviceBindAccountSucceedAndFailed.setText(R.string.device_account_bind_failed_text);
            setingText2();
        }


    }

    /**
     * 第三步：绑定账号成功 设备添加成功
     */
    private void deviceBindAccountSucceed(DeviceInfo devInfo) {
        if (mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
            mAnimationDrawable = null;
        }
        if (mLlDeviceBindAccountSucceedAndFailed == null) {
            return;
        }
        mLlDeviceBindAccountSucceedAndFailed.setVisibility(View.VISIBLE);
        mIvDeviceBindAccountSucceedAndFailed.setImageResource(R.mipmap.ic_content_succ);
        mTvDeviceBindAccountSucceedAndFailed.setText(R.string.device_account_bind_succeed_text);
        ToastUtils.showShort(R.string.add_device_failure);
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


    private void deviceTransferFailed() {
        if (mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
            mAnimationDrawable = null;
        }
        if (mLlDeviceTransferSucceedAndFailed != null) {

            mLlDeviceTransferSucceedAndFailed.setVisibility(View.VISIBLE);
        }
        if (mIvDeviceTransferSucceedAndFailed != null) {

            mIvDeviceTransferSucceedAndFailed.setImageResource(R.mipmap.ic_content_faite);
        }
        if (mTvDeviceTransferSucceedAndFailed != null) {

            mTvDeviceTransferSucceedAndFailed.setText(R.string.device_transmit_failed_text);

//            tvCommon.setText("您的设备已发出WIFI信号，请在手机设置界面链接WIFI。");
        }
        if (mLlDeviceLinkNetworkSucceedAndFailed != null) {

            mLlDeviceLinkNetworkSucceedAndFailed.setVisibility(View.INVISIBLE);
        }
        if (mLlDeviceBindAccountSucceedAndFailed != null) {

            mLlDeviceBindAccountSucceedAndFailed.setVisibility(View.INVISIBLE);
        }
//        setingText();
    }

    /**
     * 第一步：向设备传输信息成功
     */
    private void deviceTransferSucceed() {

        if (mLlDeviceTransferSucceedAndFailed != null) {

            mLlDeviceTransferSucceedAndFailed.setVisibility(View.VISIBLE);
        }
        if (mIvDeviceTransferSucceedAndFailed != null) {

            mIvDeviceTransferSucceedAndFailed.setImageResource(R.mipmap.ic_content_succ);
        }

        if (mTvDeviceTransferSucceedAndFailed != null) {

            mTvDeviceTransferSucceedAndFailed.setText(R.string.device_transmit_succeed_text);
        }
        if (mLlDeviceLinkNetworkSucceedAndFailed != null) {

            mLlDeviceLinkNetworkSucceedAndFailed.setVisibility(View.VISIBLE);
        }

        if (mIvDeviceLinkNetworkSucceedAndFailed != null) {
            mIvDeviceLinkNetworkSucceedAndFailed.setImageResource(R.mipmap.ic_content_current);
        }
        if (mTvDeviceLinkNetworkSucceedAndFailed != null) {

            mTvDeviceLinkNetworkSucceedAndFailed.setText(R.string.device_link_network_text);
        }
        if (mLlDeviceBindAccountSucceedAndFailed != null) {

            mLlDeviceBindAccountSucceedAndFailed.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * 接收到设备发送过来的mqtt
     */
    @Subscribe
    public void onEvent(DeviceFindEvent deviceFindEvent) {
        if (isReciver) {
            mIvDeviceLinkNetworkSucceedAndFailed.setImageResource(R.mipmap.ic_content_succ);
            mTvDeviceLinkNetworkSucceedAndFailed.setText(R.string.device_link_network_succeed_text);
            DeviceInfo deviceInfo = deviceFindEvent.deviceInfo;
            addKettle(deviceInfo);
//        } else {
//            ToastUtils.show("连网超时，请重试！", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mSsid = bundle.getString(PageArgumentKey.Ssid);
            wifiName = bundle.getString(PageArgumentKey.Ssid);
            mPwd = bundle.getString(PageArgumentKey.Pwd);
            mGuid = bundle.getString(PageArgumentKey.Guid);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_udp_hid_kit_content, container, false);
        ButterKnife.inject(this, view);
        mWifiManager = (WifiManager) cx.getSystemService(Context.WIFI_SERVICE);
        mWifiAutoConnectManager = new WifiAutoConnectManager(mWifiManager);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        tvCommon.setVisibility(View.GONE);
//        btnCommon.setVisibility(View.GONE);
//        initData();
        setingText();
        //开始计时
//        startTimer();
        imgDot.setBackgroundResource(R.drawable.animation_hid_kit_dot);
        mAnimationDrawable = (AnimationDrawable) imgDot.getBackground();
        if (mAnimationDrawable != null) {
            mAnimationDrawable.start();
        }
    }

    /**
     * 向设备发送upd数据报文
     */
    private void initData() {
        startTimer();
        isReciver = true;
        tvCommon.setVisibility(View.GONE);
        btnCommon.setVisibility(View.GONE);
        llMessage.setVisibility(View.VISIBLE);
        imgBg.setImageResource(R.mipmap.ic_hid_kit_center);
        mLlDeviceTransferSucceedAndFailed.setVisibility(View.VISIBLE);
        mIvDeviceTransferSucceedAndFailed.setImageResource(R.mipmap.ic_content_current);
        mTvDeviceTransferSucceedAndFailed.setText(R.string.device_transmit_msg_text);
        long userId = Plat.accountService.getCurrentUserId();
        String appid = Plat.appGuid;
        LogUtils.i("20190418", "appid:" + appid);
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

                while (isContent) {

                    udpService.receive(new IUdpServiceListener() {
                        @Override
                        public void onDeviceResult(String result) {
                            try {
                                CodeBean codeBean = JsonUtils.json2Pojo(result, CodeBean.class);
                                LogUtils.i("20201030", "code:" + codeBean.getCode());
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

    @Override
    public void onResume() {
        super.onResume();

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
        isReciver = false;
        mHandler.removeCallbacks(sendThread);
        mHandler.removeCallbacks(reciverThread);
        if (sendThread != null) {
            sendThread = null;
        }
        if (reciverThread != null) {
            reciverThread = null;
        }
        mWifiAutoConnectManager.useTag = false;
        ButterKnife.reset(this);

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        UIService.getInstance().popBack();
    }


    /**
     * 连上之前的wifi
     */
    private void connectWifi() {
        mWifiAutoConnectManager.useTag = false;
        String ssid = mSsid;
        String pwd = mPwd;
        Log.e("yidao", "connectWifi: 重连吧" + ssid + "  " + pwd);
        if (ssid == null) {
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
            if (System.currentTimeMillis() - mLastActionTime > 120000) {
                removeTimer();
            }
//            List<ScanResult> scanList = WifiUtils.getScanResults(cx);
//              120000
//            if (null != scanList && 0 < scanList.size()) {
//                for (ScanResult scanResult : scanList) {
//                    final String ssid = scanResult.SSID;
//                    if (ssid.contains("KC306")) {
//                        ssidContent = ssid;
//                        LogUtils.i("20201029", "ssid:" + ssid);
//                        mWifiAutoConnectManager.connect(ssidContent, "roki123456",
//                                WifiAutoConnectManager.WifiCipherType.WIFICIPHER_WPA);
////                        }
//                        continue;
//                    }
//                }
//            }
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

    private void setingText() {
        if (tvCommon == null) {
            return;
        }
        if (ssidContent == null) {
            ssidContent = "ROKI-KC306-xxxx";
        }

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("您的设备已发出WI-FI信号，请在手机界面链接WI-FI。\n\n" +
                "名称：" + ssidContent +
                "\n密码：" + "roki123456" + "\n\n" +  //  "\t\t复制\n\n" +
                "成功连接该设备WI-FI后返回此界面 ");
//        spannableStringBuilder.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(@NonNull View widget) {
//                copyUrl();
//            }
//
//            @Override
//            public void updateDrawState(@NonNull TextPaint ds) {
//                super.updateDrawState(ds);
//                ds.setColor(Color.parseColor("#EFCE17"));
//                ds.setUnderlineText(false);
//            }
//        }, 33 + ssidContent.length() + 5 + 10, 33 + ssidContent.length() + 5 + 10 + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvCommon.setText(spannableStringBuilder);

        tvCommon.setVisibility(View.VISIBLE);
        btnCommon.setVisibility(View.VISIBLE);
        btnCommon.setText("确认连接");
        btnCommon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWifiManager == null
                        || !getWifiSSID(cx).contains("ROKI-KC306")
//                        || mWifiManager.getConnectionInfo() == null
//                        || mWifiManager.getConnectionInfo().getSSID() == null
//                        || !mWifiManager.getConnectionInfo().getSSID().contains("ROKI-KC306")
                ) {
                    new MessageDialog.Builder(getActivity())
                            // 标题可以不用填写
                            .setTitle("提示")
                            // 内容必须要填写
                            .setMessage("您的手机当前未连接到ROKI-\nKC306热点，请先连接成功\n后，再进行下一步")
                            // 确定按钮文本
                            .setConfirm("知道了")
                            // 设置 null 表示不显示取消按钮
                            .setCancel(null)
                            // 设置点击按钮后不关闭对话框
                            //.setAutoDismiss(false)
                            .setListener(new MessageDialog.OnListener() {

                                @Override
                                public void onConfirm(BaseDialog dialog) {

                                }

                                @Override
                                public void onCancel(BaseDialog dialog) {
                                }
                            })
                            .show();
                } else {
                    initData();
                }
            }
        });
//        }

        tvCommon.setEnabled(true);
        tvCommon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                copyUrl();
            }
        });
    }

    private void setingText2() {
        if (tvCommon2 == null) {
            return;
        }
//        tvCommon.setGravity(Gravity.CENTER_HORIZONTAL);
//        tvCommon.setWidth(300);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(
                "需要您检查后重新进行联网操作\n\n" +
                        "1、检查设备是否进入了配网状态\t\t\t\n" +
                        "2、是否输入了正确的WI-FI密码\t\t\t\n" +
                        "3、您家的路由器是否是在2.4G频段");
        tvCommon2.setText(spannableStringBuilder);
        tvCommon2.setVisibility(View.VISIBLE);
        tvCommon2.setEnabled(false);
        btnCommon.setVisibility(View.VISIBLE);
        btnCommon.setText("返回");
        btnCommon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIService.getInstance().popBack();
            }
        });
    }

    //将连接copy到剪切板
    private void copyUrl() {

//        if (!TextUtils.isEmpty(mPwd)) {
        ClipboardManager cm = (ClipboardManager) cx.getSystemService(Context.CLIPBOARD_SERVICE);
// 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", "roki123456");
// 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        ToastUtils.showShort("复制成功");

//        }
    }

    private static final String WIFISSID_UNKNOW = "<unknown ssid>";

    public String getWifiSSID(Context context) {
        /*
         *  先通过 WifiInfo.getSSID() 来获取
         */
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        String wifiId = info != null ? info.getSSID() : null;
        String result = wifiId != null ? wifiId.trim() : null;
        if (!TextUtils.isEmpty(result)) {
            // 部分机型上获取的 ssid 可能会带有 引号
            if (result.charAt(0) == '"' && result.charAt(result.length() - 1) == '"') {
                result = result.substring(1, result.length() - 1);
            }
        }
        // 如果上面通过 WifiInfo.getSSID() 来获取到的是 空或者 <unknown ssid>，则使用 networkInfo.getExtraInfo 获取
        if (TextUtils.isEmpty(result) || WIFISSID_UNKNOW.equalsIgnoreCase(result.trim())) {
            NetworkInfo networkInfo = getNetworkInfo(context);
            if (networkInfo.isConnected()) {
                if (networkInfo.getExtraInfo() != null) {
                    result = networkInfo.getExtraInfo().replace("\"", "");
                }
            }
        }
        // 如果获取到的还是 空或者 <unknown ssid>，则遍历 wifi 列表来获取
        if (TextUtils.isEmpty(result) || WIFISSID_UNKNOW.equalsIgnoreCase(result.trim())) {
            result = getSSIDByNetworkId(context);
        }
        return result;
    }

    public NetworkInfo getNetworkInfo(Context context) {
        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (null != connectivityManager) {
                return connectivityManager.getActiveNetworkInfo();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     *  遍历wifi列表来获取
     */
    private String getSSIDByNetworkId(Context context) {
        String ssid = WIFISSID_UNKNOW;
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int networkId = wifiInfo.getNetworkId();
            @SuppressLint("MissingPermission") List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration wifiConfiguration : configuredNetworks) {
                if (wifiConfiguration.networkId == networkId) {
                    ssid = wifiConfiguration.SSID;
                    break;
                }
            }
        }
        return ssid;
    }


}
