package com.robam.roki.ui.page.device.cook;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.robam.common.events.CookerStatusChangeEvent;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.device.cook.AbsCooker;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.TipDialog;
import com.robam.roki.utils.DialogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2019/1/29.
 * 固件升级页
 */

public class CookerUpdateDevicePage extends BasePage {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_device_model_name)
    TextView tvDeviceModelName;

    AbsCooker absCooker;
    String mGuid;
    String mac;

    @InjectView(R.id.version_show)
    TextView versionShow;

    @InjectView(R.id.update_step)
    FrameLayout updateStep;
    @InjectView(R.id.update_txt)
    TextView updateTxt;
    @InjectView(R.id.concal)
    TextView concal;
    @InjectView(R.id.confirm)
    TextView confirm;
    @InjectView(R.id.update_tip)
    FrameLayout updateTip;


    int step = 0;
    @InjectView(R.id.update_finish)
    ImageView updateFinish;

    String targetVersion;
    Timer timer;


    List<String> listStep = new ArrayList<>();
    @InjectView(R.id.img_update)
    ImageView imgUpdate;
    @InjectView(R.id.current_status)
    RelativeLayout currentStatus;

    @InjectView(R.id.check_show1)
    ImageView checkShow1;
    @InjectView(R.id.step_show1)
    TextView stepShow1;
    @InjectView(R.id.check_show2)
    ImageView checkShow2;
    @InjectView(R.id.step_show2)
    TextView stepShow2;
    @InjectView(R.id.check_show3)
    ImageView checkShow3;
    @InjectView(R.id.step_show3)
    TextView stepShow3;
    @InjectView(R.id.cooker_circle)
    ImageView cookerCircle;

    /*private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    try{

                    }catch (Exception e){

                    }
                    break;
                case 2:
                    try{

                    }catch (Exception e){

                    }
                    break;
            }
        }
    };*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cooker_update_device, container, false);
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        ButterKnife.inject(this, view);
        currentStatus = view.findViewById(R.id.current_status);
        absCooker = Plat.deviceService.lookupChild(mGuid);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {
        mac = absCooker.getBid();
        LogUtils.i("20190130", "mac::" + mac + " mGuid::" + mGuid);
        checkVersion();
    }

    private Reponses.CheckChickenResponse checkResponse;
    boolean isLast = true;

    private void checkVersion() {
        RokiRestHelper.checkVersionFireChicken(mac, mGuid, new Callback<Reponses.CheckChickenResponse>() {

            @Override
            public void onSuccess(Reponses.CheckChickenResponse checkChickenResponse) {
                if (checkChickenResponse != null) {
                    LogUtils.i("20190129", "chl:::" + checkChickenResponse.toString());
                    currentStatus.setVisibility(View.VISIBLE);
                    if (checkChickenResponse.payLoad.version.equals(checkChickenResponse.payLoad.newVersion)) {
                        updateFinish.setVisibility(View.VISIBLE);
                        versionShow.setText("已是最新版本");
                        updateTip.setVisibility(View.GONE);
                    } else {
                        checkResponse = checkChickenResponse;
                        isLast = false;
                        startCountdown();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20190129", "t:::" + t.getMessage());
            }
        });
    }

    //通知升级
    private void toNotify(String targetVersion) {
        RokiRestHelper.toUpdate(mac, mGuid, targetVersion, new Callback<Reponses.UpdateDeviceResponse>() {

            @Override
            public void onSuccess(Reponses.UpdateDeviceResponse updateDeviceResponse) {
                LogUtils.i("20190313", "up:::" + updateDeviceResponse.rc + " " + updateDeviceResponse.mgs);
                if (updateDeviceResponse != null) {
                    if (updateDeviceResponse.rc != 0) {
                        dialogShow(updateDeviceResponse.mgs, "知道了");
                    }
                    startCountdown();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20190313", "error::" + t.getMessage());
            }
        });
    }


    private void startAnimation() {
        cookerCircle.setVisibility(View.VISIBLE);
        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);//闪烁时间间隔
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        //iv.setAnimation(animation);
        cookerCircle.startAnimation(animation);
    }

    private void stopAnimation() {
        cookerCircle.clearAnimation();
        cookerCircle.setVisibility(View.GONE);
    }


    void startCountdown() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                LogUtils.i("20190318", "here is run");
                getCookerStatus();
            }
        };
        timer.schedule(task, 2000);
    }


    String payLoad;

    //获取设备的状态
    private void getCookerStatus() {
        RokiRestHelper.checkCookerStatus(mGuid, new Callback<Reponses.CookerStatusResponse>() {
            @Override
            public void onSuccess(Reponses.CookerStatusResponse cookerStatusResponse) {
                if (cookerStatusResponse != null) {
                    LogUtils.i("20190314", "payload::" + cookerStatusResponse.payload + " " + cookerStatusResponse.rc);
                    startCountdown();
                    if (cookerStatusResponse.payload == null) {
                        try {
                            updateStep.setVisibility(View.GONE);
                            currentStatus.setVisibility(View.VISIBLE);
                            stopAnimation();
                            targetVersion = checkResponse.payLoad.newVersion;
                            updateFinish.setVisibility(View.GONE);
                            versionShow.setText("当前版本号" + checkResponse.payLoad.version);
                            updateTip.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            payLoad = cookerStatusResponse.payload;
                            currentStatus.setVisibility(View.GONE);
                            updateStep.setVisibility(View.VISIBLE);
                            updateTip.setVisibility(View.GONE);
                            dealStep(payLoad);
                            startAnimation();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20190129", "t11:::" + t.getMessage());
            }
        });
    }


    @Subscribe
    public void onEvent(CookerStatusChangeEvent event) {
        if (absCooker == null || !Objects.equal(absCooker.getID(), event.pojo.getID()))
            return;
        if (!PageKey.CookerUpdateDevice.equals(UIService.getInstance().getTop().getCurrentPageKey())) {
            return;
        }
        this.absCooker = event.pojo;
    }

    private void dealStep(String str) {
        switch (str) {
            case "1":
                break;
            case "2":
                checkShow1.setImageResource(R.drawable.cooker_update_dot);
                stepShow1.setTextColor(Color.parseColor("#2c2c2c"));
                checkShow2.setImageResource(R.drawable.cooker_update_dot_gray);
                stepShow2.setTextColor(Color.parseColor("#a1a1a1"));
                checkShow3.setImageResource(R.drawable.cooker_update_dot_gray);
                stepShow3.setTextColor(Color.parseColor("#a1a1a1"));
                break;
            case "3":
                checkShow1.setImageResource(R.mipmap.cooker_step_show);
                stepShow1.setTextColor(Color.parseColor("#2c2c2c"));
                checkShow2.setImageResource(R.drawable.cooker_update_dot);
                stepShow2.setTextColor(Color.parseColor("#2c2c2c"));
                checkShow3.setImageResource(R.drawable.cooker_update_dot_gray);
                stepShow3.setTextColor(Color.parseColor("#a1a1a1"));
                break;
            case "4":
                checkShow1.setImageResource(R.mipmap.cooker_step_show);
                stepShow1.setTextColor(Color.parseColor("#2c2c2c"));
                checkShow2.setImageResource(R.mipmap.cooker_step_show);
                stepShow2.setTextColor(Color.parseColor("#2c2c2c"));
                checkShow3.setImageResource(R.mipmap.cooker_step_show);
                stepShow3.setTextColor(Color.parseColor("#2c2c2c"));
                stopCountdown();
                break;
            case "18":
                checkShow1.setImageResource(R.mipmap.cooker_update_fail);
                stepShow1.setTextColor(Color.parseColor("#2c2c2c"));
                checkShow2.setImageResource(R.drawable.cooker_update_dot_gray);
                stepShow2.setTextColor(Color.parseColor("#a1a1a1"));
                checkShow3.setImageResource(R.drawable.cooker_update_dot_gray);
                stepShow3.setTextColor(Color.parseColor("#a1a1a1"));
                break;
            case "20":
                checkShow1.setImageResource(R.mipmap.cooker_step_show);
                stepShow1.setTextColor(Color.parseColor("#2c2c2c"));
                checkShow2.setImageResource(R.mipmap.cooker_step_show);
                stepShow2.setTextColor(Color.parseColor("#2c2c2c"));
                checkShow3.setImageResource(R.mipmap.cooker_update_fail);
                stepShow3.setTextColor(Color.parseColor("#2c2c2c"));
                break;
            default:
                break;
        }
    }


    void stopCountdown() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        stopCountdown();
        rokiDialog = null;
        tipDialog = null;
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        UIService.getInstance().popBack();
        stopCountdown();
        rokiDialog = null;
        tipDialog = null;
    }

    IRokiDialog rokiDialog = null;

    @OnClick({R.id.concal, R.id.confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.concal:
                UIService.getInstance().popBack();
                break;
            case R.id.confirm:
                //升级处理函数
                cookerStatusShow();
                break;
        }
    }

    TipDialog tipDialog = null;

    private void toUpdate(String tar) {
        toNotify(tar);
    }


    private void cookerStatusShow() {
        if (!absCooker.isConnected()) {
            dialogShow("设备离线状态无法升级固件版本，请将设备联网再升级", "知道了");
            return;
        }
        if (absCooker.powerStatus == 0) {
            dialogShow("设备处于关机状态，请先开机再升级", "知道了");
            return;
        }
        if (absCooker.powerStatus == 3) {
            dialogShow("设备工作中，无法升级固件版本，请结束工作再升级", "知道了");
            return;
        }


        rokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_00);
        rokiDialog.setTitleText("升级需要一段时间才能完成，请耐心等待。升级时请不要操作电磁炉。");
        rokiDialog.setOkBtn("去升级", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toUpdate(targetVersion);
                rokiDialog.dismiss();
            }
        });
        rokiDialog.setCancelBtn("稍后再试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rokiDialog.dismiss();
            }
        });
        rokiDialog.show();
    }


    private void dialogShow(String str1, String str2) {
        tipDialog = new TipDialog(cx, str1, str2);
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
