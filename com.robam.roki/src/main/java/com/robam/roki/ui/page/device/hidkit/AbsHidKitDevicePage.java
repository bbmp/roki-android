package com.robam.roki.ui.page.device.hidkit;

import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.events.DeviceNameChangeEvent;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.MainFunc;
import com.legent.plat.pojos.device.ModelMap;
import com.legent.plat.pojos.device.OtherFunc;
import com.legent.services.RestfulService;
import com.legent.ui.UIService;
import com.legent.utils.FileUtils;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.HidKitStatusChangedEvent;
import com.robam.common.events.TheUpgradeHidKitEvent;
import com.robam.common.pojos.device.hidkit.AbsHidKit;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.model.bean.HidKitUpdateBean;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.DividerItemDecoration;
import com.robam.roki.ui.adapter.HidKitOtherFuncAdapter;
import com.robam.roki.ui.page.device.DeviceCatchFilePage;
import com.robam.roki.utils.AlarmDataUtils;
import com.robam.roki.utils.DialogUtil;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author: lixin
 * @email: lx86@myroki.com
 * @date: 2020/10/16.
 * @PS:藏宝盒主界面
 */
public class AbsHidKitDevicePage<HidKit extends AbsHidKit> extends DeviceCatchFilePage {


    HidKit mHidKit;
    String mDeviceCategory;
    @InjectView(R.id.iv_bg)
    ImageView ivBg;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_device_model_name)
    TextView tvDeviceModelName;
    @InjectView(R.id.iv_device_more)
    ImageView ivDeviceMore;
    @InjectView(R.id.iv_wifi_res)
    ImageView ivWifiRes;
    @InjectView(R.id.tv_wifi_name)
    TextView tvWifiName;
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    @InjectView(R.id.layout_bg)
    FrameLayout layoutBg;
    protected String mViewBackgroundImg;
    protected HidKitOtherFuncAdapter mHidKitOtherFuncAdapter;
    protected List<DeviceConfigurationFunctions> mMainFuncDeviceConfigurationFunctions;
    protected List<DeviceConfigurationFunctions> mDeviceConfigurationFunctions;
    protected List<DeviceConfigurationFunctions> mBackgroundFuncDeviceConfigurationFunctions;
    @InjectView(R.id.iv_voice)
    ImageView ivVoice;
    @InjectView(R.id.iv_voice_add)
    ImageView ivVoiceAdd;
    @InjectView(R.id.seek_bar)
    SeekBar seekBar;
    private String[] deviceParam;
    private String KC306_downloadUrl = "http://roki.oss-cn-hangzhou.aliyuncs.com/KC306.json";
    private String KM310_downloadUrl = "http://roki.oss-cn-hangzhou.aliyuncs.com/KM310.json";
    private String KC306_fileName = "KC306.json";
    private String KM310_fileName = "KM310.json";
//private String downloadUrl = "https://roki-test.oss-cn-qingdao.aliyuncs.com/KC306.json";//测试环境

    public static String kc306Json;

    private IRokiDialog dialog;
    private IRokiDialog downDialog;
    private Timer timer;
    private TimerTask timerTask;
    private IRokiDialog dialogCompletion;
    private IRokiDialog dialogFailed;
    private String versionNum;
    private String mDesc;
    private int the_upgrade_val;
    private boolean connected;


    //    @Subscribe
//    public void onEvent(NewVersionHidKitEvent event) {
//        PreferenceUtils.setBool(PageArgumentKey.isShow, false);
//        LogUtils.i("20201110", "newVersion_val:" + event.newVersion_val);
//        downJsonVersion(event.newVersion_val);
//    }
    @Subscribe
    public void onEvent(DeviceNameChangeEvent event) {
        if (mGuid.equals(event.device.getGuid().getGuid())) {
            String name = event.device.getName();
            tvDeviceModelName.setText(name);
        }
    }

    @Subscribe
    public void onEvent(TheUpgradeHidKitEvent event) {
        the_upgrade_val = event.the_upgrade_val;
        LogUtils.i("20201111", "the_upgrade_val:" + the_upgrade_val);
        switchStatus(the_upgrade_val);
    }

    @Subscribe
    public void onEvent(HidKitStatusChangedEvent event) {
        if (mHidKit == null || !Objects.equal(mHidKit.getID(), event.pojo.getID()))
            return;
        mHidKit = (HidKit) event.pojo;
        connected = mHidKit.isConnected();
        wifiState();
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (event.device.getID().equals(mGuid)) {
            connected = event.isConnected;
            wifiState();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        mDeviceCategory = bd == null ? null : bd.getString(PageArgumentKey.deviceCategory);
        LogUtils.i("20201030", "mGuid:" + mGuid);
        mHidKit = Plat.deviceService.lookupChild(mGuid);
        LogUtils.i("20201030", "mHidKit:" + mHidKit);
        View view = inflater.inflate(R.layout.page_hidkit, container, false);
        ButterKnife.inject(this, view);
        downJsonVersion();
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initListener();
        deviceParam = new String[]{mHidKit.getGuid().getGuid()};
    }

    private void downJsonVersion() {
        String downloadUrl = "", fileName = "";
        if (mGuid.contains("KC306")) {
            downloadUrl = KC306_downloadUrl;
            fileName = KC306_fileName;
        } else if (mGuid.contains("KM310")) {
            downloadUrl = KM310_downloadUrl;
            fileName = KM310_fileName;
        }
        RestfulService.getInstance().downFile(downloadUrl, fileName, new Callback<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                LogUtils.i("20201111", " uri:" + uri);
                if (uri != null) {
                    kc306Json = AlarmDataUtils.getFileFromSD(uri.getPath());
                    try {
                        HidKitUpdateBean hidKitUpdateBean = JsonUtils.json2Pojo(kc306Json, HidKitUpdateBean.class);
                        versionNum = hidKitUpdateBean.getVersion().getValue();
                        final String desc = hidKitUpdateBean.getDesc().getValue();
                        mDesc = desc;
                        LogUtils.i("20201111", " versionNum:" + versionNum);
                        LogUtils.i("20201111", " desc:" + desc);
                        FileUtils.deleteFile(uri.getPath());
                        int version = mHidKit.getVersion();
                        boolean bool = PreferenceUtils.getBool(PageArgumentKey.isShow, false);
                        long saveTime = PreferenceUtils.getLong(PageArgumentKey.time, 0);
                        long timeMillis = System.currentTimeMillis();
                        long time = timeMillis - saveTime;
                        LogUtils.i("20201111", " time:" + time);
                        long longHours = time / (60 * 60 * 1000); //根据时间差来计算小时数
                        LogUtils.i("20201111", " longHours:" + longHours);
                        if (!mHidKit.isConnected()) {
                            ToastUtils.showShort(R.string.oven_dis_con);
                            return;
                        }
                        if ((version < Integer.parseInt(versionNum))) {
                            dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_DISCOVER_NEW_VERSION);
                            dialog.setContentText(desc);
                            dialog.setTitleText(R.string.dialog_discover_new_version);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setCancelBtn(R.string.dialog_ignore_text, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            });
                            dialog.setOkBtn(R.string.dialog_update_text, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    downDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_DOWN_NEW_VERSION);
                                    downDialog.setTitleText(R.string.dialog_discover_new_version);
                                    downDialog.setContentText(desc);
                                    downDialog.setCanceledOnTouchOutside(false);
                                    downDialog.show();


                                    if (!connected) {
                                        ToastUtils.showShort(R.string.oven_dis_con);
                                        return;
                                    }

                                    mHidKit.setHidKitStatusCombined((short) 1, (short) 2, (short) 1, (short) 1, new VoidCallback() {
                                        @Override
                                        public void onSuccess() {
                                            mHandler.sendEmptyMessage(100);
                                            startUpdateTask();
                                            LogUtils.i("20201111", " onSuccess:");
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                            stopUpdateTask();
                                            LogUtils.i("20201111", " onFailure:" + t);
                                        }
                                    });

                                }
                            });
                            if (bool) {
                                if (time >= 604800000L) {
                                    dialog.show();
                                }
                            } else {
                                dialog.show();
                            }
                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    long currentTimeMillis = System.currentTimeMillis();
                                    PreferenceUtils.setLong(PageArgumentKey.time, currentTimeMillis);
                                    PreferenceUtils.setBool(PageArgumentKey.isShow, true);
                                }
                            });
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20190614", " t:" + t.toString());
                mHandler.sendEmptyMessage(2);
            }
        });

    }


    private void initListener() {
        seekBar.setMax(99);
        wifiState();
        if (!mHidKit.isConnected()) {
            seekBar.setProgress(50);
        } else {
            seekBar.setProgress(mHidKit.volume);
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LogUtils.i("20201024", "progress:" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (!connected) {
                    ToastUtils.showShort(R.string.oven_dis_con);
                    seekBar.setProgress(50);
                    return;
                }

                mHidKit.setHidKitStatusCombined((short) 1, (short) 1, (short) 1, (short)
                        seekBar.getProgress(), new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.showShort(R.string.device_volume_fader);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.showShort(R.string.device_volume_fader_failed);
                    }
                });
                seekBar.invalidate();
            }
        });

    }

    @Override
    protected void setDataToView(Reponses.DeviceResponse obj) {
        if (obj == null) return;

        try {
            if (mDevice instanceof AbsHidKit) {
                mHidKit = (HidKit) mDevice;
            }

            ModelMap modelMap = obj.modelMap;
//            tvDeviceModelName.setText(obj.title);
            tvDeviceModelName.setText(mHidKit.getName() == null ||  mHidKit.getName().equals(mHidKit.getCategoryName())? mHidKit.getDispalyType() : mHidKit.getName());
            mViewBackgroundImg = obj.viewBackgroundImg;
            LogUtils.i("20180815", " mViewBackgroundImg:" + mViewBackgroundImg);
            Glide.with(cx).load(mViewBackgroundImg).into(ivBg);
            if (mHidKit != null && mHidKit.ssid != null) {
                tvWifiName.setText(decodeUtf8Str(mHidKit.ssid));
            }
            if (!mHidKit.isConnected()) {
                tvWifiName.setText(R.string.oven_dis_con);
                tvWifiName.setTextColor(Color.parseColor("#888888"));
                ivWifiRes.setImageResource(R.mipmap.wifi_img_no);
            }
            //WIFI链接状态
//            BackgroundFunc backgroundFunc = modelMap.backgroundFunc;
//            mBackgroundFuncDeviceConfigurationFunctions = backgroundFunc.deviceConfigurationFunctions;
//            String backgroundImgWifi = mBackgroundFuncDeviceConfigurationFunctions.get(0).backgroundImg;
//            Glide.with(cx).load(backgroundImgWifi).into(ivWifiRes);

            //底部列表区
            OtherFunc otherFunc = modelMap.otherFunc;
            mDeviceConfigurationFunctions = otherFunc.deviceConfigurationFunctions;
            if (mHidKitOtherFuncAdapter == null) {
                mHidKitOtherFuncAdapter = new HidKitOtherFuncAdapter(cx,
                        mDeviceConfigurationFunctions, new OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view) {
                        otherFuncItemEvent(view);
                    }
                });
            }
            recyclerview.setAdapter(mHidKitOtherFuncAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                    (cx, LinearLayoutManager.VERTICAL, false);
            recyclerview.setLayoutManager(linearLayoutManager);
            recyclerview.addItemDecoration(new DividerItemDecoration(cx, DividerItemDecoration.VERTICAL_LIST));

            //声音区域
            MainFunc mainFunc = modelMap.mainFunc;
            mMainFuncDeviceConfigurationFunctions = mainFunc.deviceConfigurationFunctions;

            DeviceConfigurationFunctions functions = mMainFuncDeviceConfigurationFunctions.get(0);
            String backgroundImg = functions.backgroundImg;
            Glide.with(cx).load(backgroundImg).into(ivVoiceAdd);
            String backgroundImgH = functions.backgroundImgH;
            Glide.with(cx).load(backgroundImgH).into(ivVoice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Unicode转中文方法
    private static String decodeUtf8Str(String xstr) throws UnsupportedEncodingException {

        return URLDecoder.decode(xstr.replaceAll("\\\\x", "%"), "utf-8");
    }

    private void otherFuncItemEvent(View view) {

        DeviceConfigurationFunctions functions = (DeviceConfigurationFunctions) view.getTag(R.id.tag_hid_kit_other_func_key);
        String code = functions.functionCode;
        String functionParams = functions.functionParams;
        LogUtils.i("20201026", "functionParams:" + functionParams);
        if ("onlyAtHome".equals(code)) {
            Bundle bundleOnlyAtHome = new Bundle();
            bundleOnlyAtHome.putString(PageArgumentKey.Guid, mGuid);
            bundleOnlyAtHome.putSerializable(PageArgumentKey.List, (Serializable) mDeviceConfigurationFunctions);
            UIService.getInstance().postPage(PageKey.SmartHome, bundleOnlyAtHome);
        } else if ("storyVideo".equals(code) || "reminders".equals(code) || "knowledge".equals(code)
                || "lifeAssistant".equals(code)) {
            Bundle bundleStoryVideo = new Bundle();
            bundleStoryVideo.putSerializable(PageArgumentKey.Bean, functions);
            UIService.getInstance().postPage(PageKey.HidKitHomeOther, bundleStoryVideo);
        }else {
            Bundle bundleStoryVideo = new Bundle();
            bundleStoryVideo.putSerializable(PageArgumentKey.Bean, functions);
            UIService.getInstance().postPage(PageKey.HidKitHomeOther, bundleStoryVideo);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.iv_back)
    public void onIvBackClicked() {
        UIService.getInstance().popBack();
    }

    @OnClick(R.id.iv_device_more)
    public void onIvDeviceMoreClicked() {
//        if (TextUtils.isEmpty(versionNum)) return;
        Bundle bundle = new Bundle();
        bundle.putSerializable(PageArgumentKey.Guid, mGuid);
        bundle.putString(PageArgumentKey.versionNum, versionNum);
        UIService.getInstance().postPage(PageKey.HidKitDeviceMore, bundle);
    }

    @Override
    protected void updateTask() {
        super.updateTask();
        switchStatus(3);
    }

//    private void getUpdate() {
//        Plat.deviceService.getDeviceUpgrdeStatus(deviceParam, new Callback<Map<String, Integer>>() {
//            @Override
//            public void onSuccess(Map<String, Integer> stringIntegerMap) {
//                for (Integer status : stringIntegerMap.values()) {
//                    switchStatus(status);
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                LogUtils.i("20201110", "t:" + t);
//            }
//        });
//    }

    int i = 0;

    private void switchStatus(Integer status) {
        LogUtils.i("20201111", "status:" + status);
        switch (status) {
            case 0://完成
                stopUpdateTask();
                if (downDialog != null && downDialog.isShow()) {
                    downDialog.dismiss();
                }
                if (null == dialogCompletion) {
                    dialogCompletion = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_UPDATE_COMPLETION);
                }
                if (null != dialogCompletion && !dialogCompletion.isShow()) {
                    dialogCompletion.setContentText(R.string.dialog_update_completion);
                    dialogCompletion.setCanceledOnTouchOutside(false);
                    dialogCompletion.setOkBtn(R.string.dialog_affirm_text, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogCompletion.dismiss();
                        }
                    });
                    dialogCompletion.show();
                }

                break;
            case 1://失败
            case 2:
                if (downDialog != null && downDialog.isShow()) {
                    downDialog.dismiss();
                }
                if (null == dialogFailed) {
                    dialogFailed = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_UPDATE_FAILED);
                }
                if (null != dialogFailed && !dialogFailed.isShow()) {
                    dialogFailed.setCanceledOnTouchOutside(false);
                    dialogFailed.setTitleText(R.string.dialog_update_failed);
                    dialogFailed.setContentText(R.string.dialog_update_content_failed);
                    dialogFailed.setCancelBtn(R.string.dialog_close_text, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LogUtils.i("20201110", "onClick:");
                            stopUpdateTask();
                            dialogFailed.dismiss();
                        }
                    });
                    dialogFailed.setOkBtn(R.string.dialog_try_again_text, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogFailed.dismiss();
                            stopUpdateTask();
//                            startUpdateTask();
                            downJsonVersion();
                            i = 0;
                        }
                    });
                    dialogFailed.show();
                }

                break;
            case 3://升级中...
                if (downDialog != null && downDialog.isShow()) {
                    if (i >= 100) {
                        downDialog.setProgress(99);
                    } else {
                        downDialog.setProgress(i);
                    }
                    if (i >= 300) {
                        //三分钟算超时
                        mHandler.sendEmptyMessage(0);
                    } else {
                        i += 1;
                    }
                }
                break;
            case 4:
                if (downDialog != null && downDialog.isShow()) {
                    i = 100;
                    downDialog.setProgress(i);
                }
                break;
        }
    }

    /**
     * 开始更新任务
     */
    private void startUpdateTask() {

        if (dialog != null && dialog.isShow()) {
            dialog.dismiss();
        }

        if (null == timer) {
            timer = new Timer();
        }
        if (null == timerTask) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(100);
                }
            };
        }
        timer.schedule(timerTask, 0, 600);

    }

    /**
     * 停止更新任务
     */
    private void stopUpdateTask() {

        if (null != timer) {
            timer.cancel();
            timer = null;
        }
        if (null != timerTask) {
            timerTask.cancel();
            timerTask = null;
        }

    }

    private void wifiState() {

        if (!connected) {
            seekBar.setProgress(50);
            tvWifiName.setText(R.string.oven_dis_con);
            tvWifiName.setTextColor(Color.parseColor("#888888"));
            ivWifiRes.setImageResource(R.mipmap.wifi_img_no);
        } else {
            short rssi = mHidKit.rssi;
            try {
                tvWifiName.setText(decodeUtf8Str(mHidKit.ssid));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            tvWifiName.setTextColor(Color.parseColor("#171717"));
            if (rssi >= 75) {
                ivWifiRes.setImageResource(R.mipmap.wifi_img_four);
            } else if (rssi >= 50) {
                ivWifiRes.setImageResource(R.mipmap.wifi_img_three);
            } else if (rssi >= 25) {
                ivWifiRes.setImageResource(R.mipmap.wifi_img_two);
            } else {
                ivWifiRes.setImageResource(R.mipmap.wifi_img_one);
            }
        }
    }
}
