package com.robam.roki.ui.page;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.robam.common.events.SteriAlarmEvent;
import com.robam.common.events.SteriStatusChangedEvent;
import com.robam.common.pojos.device.Sterilizer.Steri826;
import com.robam.common.pojos.device.Sterilizer.SteriStatus;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.SterilizerAnimationUtil;
import com.robam.roki.ui.view.SlideUnlockView;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.roki.utils.StringConstantsUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static android.content.Context.VIBRATOR_SERVICE;


public class DeviceSterilizer826Page extends BasePage {

    Steri826 steri;
    @InjectView(R.id.iv_dft)
    ImageView mIvDft;
    @InjectView(R.id.tv_dft_name)
    TextView mTvDftName;
    @InjectView(R.id.disconnectHintView)
    LinearLayout mDisconnectHintView;
    @InjectView(R.id.ll_empty)
    LinearLayout mLlEmpty;
    @InjectView(R.id.rl_tem)
    RelativeLayout mRlTem;
    @InjectView(R.id.rl_hum)
    RelativeLayout mRlHum;
    @InjectView(R.id.rl_germ)
    RelativeLayout mRlGerm;
    @InjectView(R.id.rl_ozone)
    RelativeLayout mRlOzone;
    @InjectView(R.id.ll_animation)
    LinearLayout mLlAnimation;
    @InjectView(R.id.tv_steri_tem)
    TextView mTvSteriTem;
    @InjectView(R.id.tv_steri_hum)
    TextView mTvSteriHum;
    @InjectView(R.id.tv_steri_germ)
    TextView mTvSteriGerm;
    @InjectView(R.id.tv_steri_ozone)
    TextView mTvSteriOzone;
    @InjectView(R.id.iv_reaction_sterilize)
    ImageView mIvReactionSterilize;
    @InjectView(R.id.tv_reaction_sterilize)
    TextView mTvReactionSterilize;
    @InjectView(R.id.rl_reaction_sterilize)
    RelativeLayout mRlReactionSterilize;
    @InjectView(R.id.tv_sterilize)
    TextView mTvSterilize;
    @InjectView(R.id.iv_sterilize)
    ImageView mIvSterilize;
    @InjectView(R.id.rl_sterilize)
    RelativeLayout mRlSterilize;
    @InjectView(R.id.tv_warm_dish)
    TextView mTvWarmDish;
    @InjectView(R.id.iv_warm_dish)
    ImageView mIvWarmDish;
    @InjectView(R.id.rl_warm_dish)
    RelativeLayout mRlWarmDish;
    @InjectView(R.id.iv_rapid_sterilize)
    ImageView mIvRapidSterilize;
    @InjectView(R.id.tv_rapid_sterilize)
    TextView mTvRapidSterilize;
    @InjectView(R.id.rl_rapid_sterilize)
    RelativeLayout mRlRapidSterilize;
    @InjectView(R.id.iv_stoving)
    ImageView mIvStoving;
    @InjectView(R.id.tv_stoving)
    TextView mTvStoving;
    @InjectView(R.id.rl_tv_stoving)
    RelativeLayout mRlTvStoving;
    @InjectView(R.id.rl_all_model)
    RelativeLayout mRlAllModel;
    @InjectView(R.id.tv_steri_time_hour)
    TextView mTvSteriTimeHour;
    @InjectView(R.id.tv_steri_time_point)
    TextView mTvSteriTimePoint;
    @InjectView(R.id.tv_steri_time_minute)
    TextView mTvSteriTimeMinute;
    @InjectView(R.id.tv_steri)
    TextView mTvSteri;
    @InjectView(R.id.tv_done)
    TextView mTvDone;
    @InjectView(R.id.fl_running)
    FrameLayout mFlRunning;
    @InjectView(R.id.rl_running)
    RelativeLayout mRlRunning;
    @InjectView(R.id.iv_child_lock)
    ImageView mIvChildLock;
    @InjectView(R.id.ll_child_lock)
    LinearLayout mLlChildLock;
    @InjectView(R.id.sterilize_switch)
    ImageView mSterilizeSwitch;
    @InjectView(R.id.rl_switch)
    RelativeLayout mRlSwitch;
    @InjectView(R.id.fl_sterilizer_main)
    LinearLayout mFlSterilizerMain;
    SterilizerAnimationUtil animationUtil;
    public static boolean alarm = false;
    @InjectView(R.id.sterilizer_switch_run)
    Button mSterilizerSwitchRun;
    @InjectView(R.id.iv_running)
    ImageView mIvRunning;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_intelligent_setting)
    TextView mTvIntelligentSetting;
    @InjectView(R.id.tv_model)
    TextView mTvModel;
    @InjectView(R.id.roki_lock_view)
    SlideUnlockView mRokiLockView;
    @InjectView(R.id.rl_lock_or_time_view)
    RelativeLayout mRlLockOrTimeView;
    @InjectView(R.id.tv_time_hour)
    TextView mTvTimeHour;
    @InjectView(R.id.tv_time_min)
    TextView mTvTimeMin;
    @InjectView(R.id.tv_time_point)
    TextView mTvTimePoint;
    @InjectView(R.id.tv_open_Intelligent_detection)
    TextView mTvOpenIntelligentDetection;
    @InjectView(R.id.tv_center_model)
    TextView mTvCenterModel;
    @InjectView(R.id.tv_open_text)
    TextView mTvOpenText;
    private IRokiDialog iRokiOrderDialog;
    private IRokiDialog iRokiWarmDishDialog;
    private IRokiDialog mClosedialog;
    private IRokiDialog mDialogToast;
    private final int white = 0xffffffff;
    private final int grey = 0xff575757;
    private Animation circleRotate;
    private Timer mTimer;
    private MyTask mMyTask;
    private Vibrator mVibrator;
    private ScreenOnOffReceiver mReceiver;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
//                    setDeviceDisinfectionData((String) msg.obj);
                    break;
                case 2:
//                    setStovingModelData((String) msg.obj);
                    break;
                case 3:
                    setWarmDishData((String) msg.obj);
                    break;
                case 4:
                    setRapidSterilizationData((String) msg.obj);
                    break;
                case 5:
                    setDryingData((String) msg.obj);
                    break;
                default:
                    if (msg.arg1 == 1) {
                        String s = msg.what % 2 == 0 ? "" : ":";
                        mTvSteriTimePoint.setText(s);
                    }
                    break;
            }
        }
    };


    @Subscribe
    public void onEvent(SteriStatusChangedEvent event) {

        LogUtils.i("20171214","steri:"+steri + " event:"+event.pojo.isConnected());

        if (steri == null || !Objects.equal(steri.getID(), event.pojo.getID()))
            return;
        steri = (Steri826) event.pojo;
        setRunningState(event.pojo.status);
        initData();
        setPoint();
        onRefresh(event.pojo.isConnected());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        steri = Plat.deviceService.lookupChild(guid);
        View view = inflater.inflate(R.layout.page_device_826_sterilizer, container, false);
        ButterKnife.inject(this, view);
        initView();
        // 注册屏幕锁屏的广播
        registScreenOffReceiver();
        // 获取系统振动器服务
        mVibrator = (Vibrator) cx.getSystemService(VIBRATOR_SERVICE);
        setLockListener();
        return view;
    }

    private void setLockListener() {
        // 设置滑动解锁-解锁的监听
        mRokiLockView.setOnUnLockListener(new SlideUnlockView.OnUnLockListener() {
            @Override
            public void setUnLocked(boolean unLock) {
                // 如果是true，证明解锁
                if (unLock) {
                    // 启动震动器 100ms
                    mVibrator.vibrate(100);
                    // 重置一下滑动解锁的控件
                    mRokiLockView.reset();

                    steri.setSteriLock((short) 0, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            if (mLlChildLock != null) {
                                mRlLockOrTimeView.setVisibility(View.GONE);
                                if (steri.status == 0 || steri.status == 1) {
                                    mRlAllModel.setVisibility(View.VISIBLE);
                                    mRlRunning.setVisibility(View.GONE);
                                } else {
                                    mRlAllModel.setVisibility(View.GONE);
                                    mRlRunning.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                        }
                    });
                }
            }
        });
    }


    /**
     * 注册一个屏幕锁屏的广播
     */
    private void registScreenOffReceiver() {
        // TODO Auto-generated method stub
        mReceiver = new ScreenOnOffReceiver();
        // 创建一个意图过滤器
        IntentFilter filter = new IntentFilter();
        // 添加屏幕锁屏的广播
        filter.addAction("android.intent.action.SCREEN_OFF");
        // 在代码里边来注册广播
        cx.registerReceiver(mReceiver, filter);

    }

    /**
     * 开启动画
     */
    void startAnimation() {
        if (circleRotate == null) {
            circleRotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_oven_circle_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotate.setInterpolator(lin);
            mIvRunning.startAnimation(circleRotate);
        }
    }

    /**
     * 关闭动画和烟雾效果
     */
    private void stopAnimation() {
        if (circleRotate != null)
            circleRotate.cancel();
        circleRotate = null;
        mIvRunning.clearAnimation();
    }

    //------------------------------------------------------------------------- ui event---------------------------------------------------------------------

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (steri == null || !Objects.equal(steri.getID(), event.device.getID()))
            return;
        LogUtils.i("20171214","isConnected:"+ steri.isConnected());
        onRefresh(event.isConnected);
        if (!event.isConnected){
            setRunningState((short) 0);
            mSterilizeSwitch.setImageResource(R.mipmap.img_sterilizer_826_switch_off);
        }

    }


    @Subscribe
    public void onEvent(SteriAlarmEvent event) {
        if (steri == null || !Objects.equal(steri.getID(), event.absSterilizer.getID()))
            return;
        setAlarmDialog(event.alarm);
    }


    void initView() {
        mClosedialog = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_10);
        mDialogToast = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_09);
        mDisconnectHintView.setVisibility(View.INVISIBLE);
        mRlLockOrTimeView.setVisibility(View.GONE);
        animationUtil = new SterilizerAnimationUtil(cx, mRlTem, mRlHum, mRlGerm, mRlOzone);
        animationUtil.setAnimation();
        onRefresh(steri.isConnected());
    }

    void onRefresh(boolean lean) {
        mDisconnectHintView.setVisibility(lean ? View.INVISIBLE : View.VISIBLE);
        if (steri == null)
            return;
    }

    //感应杀菌
    @OnClick(R.id.rl_reaction_sterilize)
    public void onMRlReactionSterilizeClicked() {
        deviceListenerAndToast(steri.status);
        if (alarmStatus())return;
        if (doorToast())return;
        if (steri.status == 1) {
            setReactionSterilize();
        }
    }


    //消毒
    @OnClick(R.id.rl_sterilize)
    public void onMRlSterilizeClicked() {
        deviceListenerAndToast(steri.status);
        if (alarmStatus())return;
        if (doorToast())return;
        if (steri.status == 1) {
            setDeviceDisinfectionData();
        }
    }

    //暖碟
    @OnClick(R.id.rl_warm_dish)
    public void onMRlWarmDishClicked() {
        deviceListenerAndToast(steri.status);
        if (alarmStatus())return;
        if (doorToast())return;
        if (steri.status == 1) {
            onStartWarmDishClock();
        }
    }


    //快洁
    @OnClick(R.id.rl_rapid_sterilize)
    public void onMRlRapidSterilizeClicked() {
        deviceListenerAndToast(steri.status);
        if (alarmStatus())return;
        if (doorToast())return;
        if (steri.status == 1) {
            onStartRapidSterilizationClock();
        }
    }

    //烘干
    @OnClick(R.id.rl_tv_stoving)
    public void onMRlTvStovingClicked() {
        deviceListenerAndToast(steri.status);
        if (alarmStatus())return;
        if (doorToast())return;
        if (steri.status == 1) {
            onStartDryingClock();
        }

    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        UIService.getInstance().popBack();
    }

    //童锁
    @OnClick(R.id.ll_child_lock)
    public void onMLlChildLockClicked() {
        deviceListenerAndToast(steri.status);
        if (alarmStatus())return;
        if (steri.status != 0) {
            if (steri.isChildLock == 0) {
                steri.setSteriLock((short) 1, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        if (mLlChildLock != null) {
                            mRlLockOrTimeView.setVisibility(View.VISIBLE);
                            mRlRunning.setVisibility(View.GONE);
                            mRlAllModel.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        LogUtils.i("20171205", "t:" + t);
                    }
                });
            }
        }
    }

    @OnClick(R.id.sterilizer_switch_run)
    public void onMSterilizerSwitchRunClicked() {
        stopWorking(true);
    }

    @OnClick(R.id.rl_lock_or_time_view)
    public void onViewClicked() {

        if (steri.isChildLock == 1 && steri.status != 0) {
            IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
            dialogByType.setContentText(R.string.remove_child_lock);
            dialogByType.setToastShowTime(DialogUtil.LENGTH_CENTER);
            dialogByType.show();
        }
    }

    @OnClick(R.id.tv_intelligent_setting)
    public void onMTvIntelligentSettingClicked() {
        if (steri != null){
            Bundle bundle = new Bundle();
            bundle.putSerializable(PageArgumentKey.steri826, steri);
            UIService.getInstance().postPage(PageKey.SteriSmart826Params,bundle);
        }

    }

    //开关
    @OnClick(R.id.sterilize_switch)
    public void onMSterilizeSwitchClicked() {

        if (deviceNotConnectdToast()) return;
        if (steri.status == 0 && steri.isConnected()) {
            setBtnSelected(true);
            setStatus(true);
        } else if (steri.status == 1 || steri.status == 6) {
            setStatus(false);
        }
    }

    //门控提醒
    private boolean doorToast() {
        if (steri.doorLock == 0){
            mDialogToast.setToastShowTime(DialogUtil.LENGTH_CENTER);
            mDialogToast.setContentText(R.string.device_alarm_gating_content);
            mDialogToast.show();
            return true;
        }
        return false;
    }

    //报警状态
    private boolean alarmStatus(){
        if (steri.status == 6){
            mDialogToast.setToastShowTime(DialogUtil.LENGTH_CENTER);
            mDialogToast.setContentText(R.string.device_alarm_close_content);
            mDialogToast.show();
            return true;
        }
       return false;
    }



    //开关控制
    public void setStatus(boolean witchStatus) {
        short status = witchStatus ? SteriStatus.On : SteriStatus.Off;
        steri.setSteriPower(status, new VoidCallback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }


    //童锁打开时状态设置
    private void lockModelStatusSetting() {
        switch (steri.status) {
            case 2:
                mTvModel.setText(R.string.device_sterilizer_text);
                mTvModel.setVisibility(View.VISIBLE);
                mTvTimeHour.setVisibility(View.VISIBLE);
                mTvTimeMin.setVisibility(View.VISIBLE);
                mTvTimePoint.setVisibility(View.VISIBLE);
                mTvOpenIntelligentDetection.setVisibility(View.GONE);
                break;
            case 3:
                mTvModel.setText(R.string.device_sterilizer_clean_keeping_text);
                break;
            case 4:
                mTvModel.setText(R.string.device_sterilizer_stoving_text);
                mTvModel.setVisibility(View.VISIBLE);
                mTvTimeHour.setVisibility(View.VISIBLE);
                mTvTimeMin.setVisibility(View.VISIBLE);
                mTvTimePoint.setVisibility(View.VISIBLE);
                mTvOpenIntelligentDetection.setVisibility(View.GONE);
                break;
            case 5:
                mTvModel.setText(R.string.device_sterilizer_yuyue);
                break;
            case 7:
                mTvModel.setText(R.string.device_sterilizer_rapid_sterilize_center_text);
                mTvModel.setVisibility(View.VISIBLE);
                mTvTimeHour.setVisibility(View.VISIBLE);
                mTvTimeMin.setVisibility(View.VISIBLE);
                mTvTimePoint.setVisibility(View.VISIBLE);
                mTvOpenIntelligentDetection.setVisibility(View.GONE);
                break;
            case 8:
                mTvModel.setVisibility(View.GONE);
                mTvTimeHour.setVisibility(View.GONE);
                mTvTimeMin.setVisibility(View.GONE);
                mTvTimePoint.setVisibility(View.GONE);
                mTvOpenIntelligentDetection.setVisibility(View.VISIBLE);
                mTvOpenIntelligentDetection.setText(R.string.device_sterilizer_open_Intelligent_detection);
                break;
            case 9:
                mTvModel.setText(R.string.device_sterilizer_Induction_center_sterilization);
                mTvModel.setVisibility(View.VISIBLE);
                mTvTimeHour.setVisibility(View.VISIBLE);
                mTvTimeMin.setVisibility(View.VISIBLE);
                mTvTimePoint.setVisibility(View.VISIBLE);
                mTvOpenIntelligentDetection.setVisibility(View.GONE);
                break;
            case 10:
               /* mTvModel.setVisibility(View.GONE);
                mTvTimeHour.setVisibility(View.GONE);
                mTvTimeMin.setVisibility(View.GONE);
                mTvTimePoint.setVisibility(View.GONE);
                mTvOpenIntelligentDetection.setVisibility(View.VISIBLE);
                mTvOpenIntelligentDetection.setText(R.string.device_sterilizer_warm_dish_center_text);*/
                mTvModel.setText(R.string.device_sterilizer_warm_dish_center_text);
                mTvModel.setVisibility(View.VISIBLE);
                mTvTimeHour.setVisibility(View.VISIBLE);
                mTvTimeMin.setVisibility(View.VISIBLE);
                mTvTimePoint.setVisibility(View.VISIBLE);
                mTvOpenIntelligentDetection.setVisibility(View.GONE);
                break;
        }
    }

    //设置感应杀菌
    private void setReactionSterilize() {
        steri.setSteriPower((short) 8, (short) 0,(short) 0, new VoidCallback() {
            @Override
            public void onSuccess() {
                IRokiDialog rokiCountDownDialog = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_07);
                rokiCountDownDialog.show();
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20171205", "t:" + t);
            }
        });
    }

    //设置暖碟参数
    private void onStartWarmDishClock() {

        if (iRokiWarmDishDialog != null) {
            iRokiWarmDishDialog = null;
        }
        if (iRokiWarmDishDialog == null) {
            iRokiWarmDishDialog = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_14);
        }

        iRokiWarmDishDialog.setWheelViewData(null, getWarmDishListData(), null, false, 0, 0, 0, null, new OnItemSelectedListenerCenter() {
            @Override
            public void onItemSelectedCenter(String contentCenter) {
                Message message = mHandler.obtainMessage();
                message.what = 3;
                message.obj = contentCenter;
                mHandler.sendMessage(message);
            }
        }, null);
        iRokiWarmDishDialog.show();
    }

    //设置快速杀菌参数
    private void onStartRapidSterilizationClock() {
        if (iRokiOrderDialog != null) {
            iRokiOrderDialog = null;
        }
        if (iRokiOrderDialog == null) {
            iRokiOrderDialog = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_03);
        }

        iRokiOrderDialog.setWheelViewData(null, getRapidSterilizationListData(), null, false, 0, 0, 0, null, new OnItemSelectedListenerCenter() {
            @Override
            public void onItemSelectedCenter(String contentCenter) {
                Message message = mHandler.obtainMessage();
                message.what = 4;
                message.obj = contentCenter;
                mHandler.sendMessage(message);
            }
        }, null);
        iRokiOrderDialog.show();
    }

    //设置烘干参数
    private void onStartDryingClock() {
        if (iRokiOrderDialog != null) {
            iRokiOrderDialog = null;
        }
        if (iRokiOrderDialog == null) {
            iRokiOrderDialog = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_03);
        }

        iRokiOrderDialog.setWheelViewData(null, getDryingListData(), null, false, 0, 0, 0, null, new OnItemSelectedListenerCenter() {
            @Override
            public void onItemSelectedCenter(String contentCenter) {
                Message message = mHandler.obtainMessage();
                message.what = 5;
                message.obj = contentCenter;
                mHandler.sendMessage(message);
            }
        }, null);
        iRokiOrderDialog.show();
    }


    //设置消毒时间
    private void setDeviceDisinfectionData() {
        steri.setSteriPower((short) 2, (short) 130,(short) 0, new VoidCallback() {
            @Override
            public void onSuccess() {
                IRokiDialog rokiCountDownDialog = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_07);
                rokiCountDownDialog.show();
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });


    }

    //设置暖碟温度
    private void setWarmDishData(String str) {
        if (TextUtils.isEmpty(str)) return;
        final String removeString = RemoveManOrsymbolUtil.getRemoveString(str);

        iRokiWarmDishDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRokiWarmDishDialog.dismiss();
                steri.setSteriPower((short) 10, Short.parseShort(removeString), (short) 0, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        IRokiDialog rokiCountDownDialog = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_07);
                        rokiCountDownDialog.show();
                        LogUtils.i("20171214","-----setWarmDishData-----:"+ steri.status);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        LogUtils.i("20171205", "t:" + t);
                    }
                });

            }
        });
        iRokiWarmDishDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    //设置快速杀菌数据
    private void setRapidSterilizationData(String str) {
        if (TextUtils.isEmpty(str)) return;
        final String removeString = RemoveManOrsymbolUtil.getRemoveString(str);

        iRokiOrderDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRokiOrderDialog.dismiss();
                steri.setSteriPower((short) 7, Short.parseShort(removeString),(short) 0, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        IRokiDialog rokiCountDownDialog = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_07);
                        rokiCountDownDialog.show();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });

            }
        });
        iRokiOrderDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    //设置烘干数据
    private void setDryingData(String str) {

        if (TextUtils.isEmpty(str)) return;
        final String removeString = RemoveManOrsymbolUtil.getRemoveString(str);
        final short data = Short.parseShort(removeString);
        iRokiOrderDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRokiOrderDialog.dismiss();
                steri.setSteriPower((short) 4,data,(short) 0, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        IRokiDialog rokiCountDownDialog = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_07);
                        rokiCountDownDialog.show();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });

            }
        });
        iRokiOrderDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


    //消毒数据
    protected List<String> getListTime() {
        List<String> listime = Lists.newArrayList();
        int i = 130;
        listime.add(i + StringConstantsUtil.STRING_MIN);

        return listime;
    }

    //暖碟数据
   /* protected List<String> getWarmDishListData() {
        List<String> warmDishList = Lists.newArrayList();
        String wenWarmDish = cx.getString(R.string.device_warm_dish_wen) + 37
                + StringConstantsUtil.STRING_DEGREE_CENTIGRADE;
        String reWarmDish = cx.getString(R.string.device_warm_dish_re) + 50
                + StringConstantsUtil.STRING_DEGREE_CENTIGRADE;
        warmDishList.add(wenWarmDish);
        warmDishList.add(reWarmDish);
        return warmDishList;
    }*/

    protected List<String> getWarmDishListData() {
        List<String> warmDishList = Lists.newArrayList();
        String wenWarmDish = 10 + StringConstantsUtil.STRING_MINUTES;
        String reWarmDish =  20 + StringConstantsUtil.STRING_MINUTES;
        warmDishList.add(wenWarmDish);
        warmDishList.add(reWarmDish);
        return warmDishList;
    }

    //快洁数据
    public List<String> getRapidSterilizationListData() {
        List<String> rapidSteriList = Lists.newArrayList();

        String rapidItem01 = 3 + cx.getString(R.string.dialog_minutes_text);
        String rapidItem30 = 15 + cx.getString(R.string.dialog_minutes_text);
        rapidSteriList.add(rapidItem01);
        rapidSteriList.add(rapidItem30);
        return rapidSteriList;
    }

    //烘干数据
    public List<String> getDryingListData() {
        List<String> dryingList = Lists.newArrayList();
        String dryingItem30 = 30 + cx.getString(R.string.dialog_minutes_text);
        String dryingItem60 = 60 + cx.getString(R.string.dialog_minutes_text);
        String dryingItem80 = 80 + cx.getString(R.string.dialog_minutes_text);
        dryingList.add(dryingItem30);
        dryingList.add(dryingItem60);
        dryingList.add(dryingItem80);
        return dryingList;
    }


    long slge = -1;

    //运行状态显示
    private void setRunningState(short state) {
    LogUtils.i("20171207","steri.isChildLock：" +steri.isChildLock);
        if (state == 2 || state == 3 || state == 4 || state == 5
                || state == 7 || state == 8 || state == 9 || state == 10) {
            startAnimation();
           if (iRokiOrderDialog != null && iRokiOrderDialog.isShow()){
               iRokiOrderDialog.dismiss();
           }
            if (iRokiWarmDishDialog != null && iRokiWarmDishDialog.isShow()){
                iRokiWarmDishDialog.dismiss();
            }
            slge++;
            if (slge >= 1) {
                setRunningTime(steri.work_left_time_l);
                mRlAllModel.setVisibility(View.GONE);
                mRlRunning.setVisibility(View.VISIBLE);
                setBtnSelected(true);
                mSterilizeSwitch.setVisibility(View.GONE);
                mSterilizerSwitchRun.setVisibility(View.VISIBLE);
                mTvDone.setVisibility(View.GONE);
                if (steri.isChildLock == 1) {
                    lockModelStatusSetting();//童锁打开时设置
                    mRlRunning.setVisibility(View.GONE);
                    mRokiLockView.setVisibility(View.VISIBLE);
                    mRlLockOrTimeView.setVisibility(View.VISIBLE);
                    mLlChildLock.setVisibility(View.GONE);
                    mSterilizerSwitchRun.setVisibility(View.GONE);
                    mSterilizeSwitch.setVisibility(View.GONE);
                } else {
                    mRlRunning.setVisibility(View.VISIBLE);
                    mRokiLockView.setVisibility(View.GONE);
                    mTvModel.setVisibility(View.GONE);
                    mRlLockOrTimeView.setVisibility(View.GONE);
                    mLlChildLock.setVisibility(View.VISIBLE);
                    mSterilizerSwitchRun.setVisibility(View.VISIBLE);
                    mSterilizeSwitch.setVisibility(View.GONE);

                }

                if (steri.status == 2) {
                    mTvSteri.setText(cx.getString(R.string.device_sterilizer_text));
                    mTvSteri.setVisibility(View.VISIBLE);
                    mTvSteriTimeHour.setVisibility(View.VISIBLE);
                    mTvSteriTimeMinute.setVisibility(View.VISIBLE);
                    mTvSteriTimePoint.setVisibility(View.VISIBLE);
                    mTvCenterModel.setVisibility(View.GONE);
                    mTvOpenText.setVisibility(View.GONE);
                } else if (steri.status == 3) {
                    mTvSteri.setText(cx.getString(R.string.device_sterilizer_fast_clean));
                    mTvSteri.setVisibility(View.VISIBLE);
                    mTvCenterModel.setVisibility(View.GONE);
                    mTvOpenText.setVisibility(View.GONE);
                } else if (steri.status == 4) {
                    mTvSteri.setText(cx.getString(R.string.device_sterilizer_stoving_text));
                    mTvCenterModel.setVisibility(View.GONE);
                    mTvSteri.setVisibility(View.VISIBLE);
                    mTvSteriTimeHour.setVisibility(View.VISIBLE);
                    mTvSteriTimeMinute.setVisibility(View.VISIBLE);
                    mTvSteriTimePoint.setVisibility(View.VISIBLE);
                    mTvOpenText.setVisibility(View.GONE);
                } else if (steri.status == 5) {
                    mTvSteri.setText(cx.getString(R.string.device_sterilizer_yuyue));
                    mTvOpenText.setVisibility(View.GONE);
                    mTvSteri.setVisibility(View.VISIBLE);
                    mTvCenterModel.setVisibility(View.GONE);
                } else if (steri.status == 7) {
                    mTvSteri.setVisibility(View.VISIBLE);
                    mTvCenterModel.setVisibility(View.GONE);
                    mTvSteriTimeHour.setVisibility(View.VISIBLE);
                    mTvSteriTimeMinute.setVisibility(View.VISIBLE);
                    mTvSteriTimePoint.setVisibility(View.VISIBLE);
                    mTvOpenText.setVisibility(View.GONE);
                    mTvSteri.setText(cx.getString(R.string.device_sterilizer_rapid_sterilize_center_text));
                } else if (steri.status == 8) {
                    mTvSteriTimeHour.setVisibility(View.GONE);
                    mTvSteriTimeMinute.setVisibility(View.GONE);
                    mTvSteriTimePoint.setVisibility(View.GONE);
                    mTvSteri.setVisibility(View.GONE);
                    mTvCenterModel.setVisibility(View.VISIBLE);
                    mTvOpenText.setVisibility(View.VISIBLE);
                    mTvCenterModel.setText(cx.getString(R.string.device_sterilizer_Intelligent_detection));
                } else if (steri.status == 9) {
                    mTvSteri.setText(cx.getString(R.string.device_sterilizer_Induction_center_sterilization));
                    mTvOpenText.setVisibility(View.GONE);
                    mTvSteri.setVisibility(View.VISIBLE);
                    mTvSteriTimeHour.setVisibility(View.VISIBLE);
                    mTvSteriTimeMinute.setVisibility(View.VISIBLE);
                    mTvSteriTimePoint.setVisibility(View.VISIBLE);
                    mTvCenterModel.setVisibility(View.GONE);
                } else if (steri.status == 10) {
                   /* mTvOpenText.setVisibility(View.GONE);
                    mTvSteriTimeHour.setVisibility(View.GONE);
                    mTvSteriTimeMinute.setVisibility(View.GONE);
                    mTvSteriTimePoint.setVisibility(View.GONE);
                    mTvSteri.setVisibility(View.GONE);
                    mTvCenterModel.setVisibility(View.VISIBLE);
                    mTvCenterModel.setText(cx.getString(R.string.device_sterilizer_warm_dish_center_text));*/
                    mTvSteri.setText(cx.getString(R.string.device_sterilizer_warm_dish_center_text));
                    mTvCenterModel.setVisibility(View.GONE);
                    mTvSteri.setVisibility(View.VISIBLE);
                    mTvSteriTimeHour.setVisibility(View.VISIBLE);
                    mTvSteriTimeMinute.setVisibility(View.VISIBLE);
                    mTvSteriTimePoint.setVisibility(View.VISIBLE);
                    mTvOpenText.setVisibility(View.GONE);
                }
            }

        } else {
            stopAnimation();
            if (state == 1 || state == 6) {
                slge = -1;
//                LogUtils.i("20171205", "slge--status == 1:" + slge);
                setBtnSelected(true);
//                mSterilizeSwitch.setChecked(true);
                if (mClosedialog != null && mClosedialog.isShow()){
                    mClosedialog.dismiss();
                }
                if (steri.isChildLock == 1) {

                    if (iRokiOrderDialog != null && iRokiOrderDialog.isShow()){
                        iRokiOrderDialog.dismiss();
                    }
                    if (iRokiWarmDishDialog != null && iRokiWarmDishDialog.isShow()){
                        iRokiWarmDishDialog.dismiss();
                    }

                    mTvModel.setVisibility(View.GONE);
                    mTvTimeHour.setVisibility(View.GONE);
                    mTvTimePoint.setVisibility(View.GONE);
                    mTvTimeMin.setVisibility(View.GONE);
                    mRlRunning.setVisibility(View.GONE);
                    mRlAllModel.setVisibility(View.GONE);
                    mLlChildLock.setVisibility(View.GONE);
                    mSterilizerSwitchRun.setVisibility(View.GONE);
                    mRokiLockView.setVisibility(View.VISIBLE);
                    mRlLockOrTimeView.setVisibility(View.VISIBLE);
                    mSterilizeSwitch.setVisibility(View.GONE);
                    mTvOpenIntelligentDetection.setVisibility(View.GONE);
                    mTvOpenText.setVisibility(View.GONE);

                } else {
                    mRlRunning.setVisibility(View.GONE);
                    mRlLockOrTimeView.setVisibility(View.GONE);
                    mRlAllModel.setVisibility(View.VISIBLE);
                    mLlChildLock.setVisibility(View.VISIBLE);
                    mSterilizerSwitchRun.setVisibility(View.GONE);
                    mSterilizeSwitch.setVisibility(View.VISIBLE);
                    mTvOpenText.setVisibility(View.GONE);
                }
            } else {
                slge = -1;
                if (mClosedialog != null && mClosedialog.isShow()){
                    mClosedialog.dismiss();
                }
                if (iRokiOrderDialog != null && iRokiOrderDialog.isShow()){
                    iRokiOrderDialog.dismiss();
                }
                if (iRokiWarmDishDialog != null && iRokiWarmDishDialog.isShow()){
                    iRokiWarmDishDialog.dismiss();
                }

                mSterilizerSwitchRun.setVisibility(View.GONE);
                setBtnSelected(false);
                mRlRunning.setVisibility(View.GONE);
                mRlLockOrTimeView.setVisibility(View.GONE);
                mRokiLockView.setVisibility(View.GONE);
                mRlAllModel.setVisibility(View.VISIBLE);
                mSterilizeSwitch.setVisibility(View.VISIBLE);
                mTvOpenText.setVisibility(View.GONE);
                mLlChildLock.setVisibility(View.VISIBLE);


            }
            if (steri.status == 0 || steri.status == 1) {
                if (steri.oldstatus == 2 || steri.oldstatus == 3 || steri.oldstatus == 4
                        || steri.oldstatus == 5 || steri.oldstatus == 7 || steri.oldstatus == 8
                        || steri.oldstatus == 9 || steri.oldstatus == 10) {
                    return;
                }
            }


        }
    }

    //时间设置
    private void setRunningTime(int second) {
        int h = 0;
        int d = 0;
        int s = 0;
        int temp = second % 3600;
        if (second > 3600) {
            h = second / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            d = second / 60;
            if (second % 60 != 0) {
                s = second % 60;
            }
        }
        mTvSteriTimeHour.setText("0" + h);
        mTvTimeHour.setText("0" + h);
        String min = String.valueOf(d + 1);
        if (min.length() == 1) {
            mTvSteriTimeMinute.setText("0" + min);
            mTvTimeMin.setText("0" + min);
        } else {
            mTvSteriTimeMinute.setText(min);
            mTvTimeMin.setText(min);
        }

    }

    //温度，湿度，细菌，臭氧
    private void initData() {
        short temp = steri.temp;
        short germ = steri.germ;
        short hum = steri.hum;
        short ozone = steri.ozone;
        mTvSteriTem.setText(String.valueOf(temp));
        mTvSteriGerm.setText(String.valueOf(germ));
        mTvSteriHum.setText(String.valueOf(hum));
        mTvSteriOzone.setText(String.valueOf(ozone));
    }


    //选中的状态
    private void setBtnSelected(boolean selected) {

        if (selected) {
            mIvReactionSterilize.setImageResource(R.mipmap.img_steamoven_circle_open_small);
            mIvSterilize.setImageResource(R.mipmap.img_steamoven_circle_open_small);
            mIvWarmDish.setImageResource(R.mipmap.img_steamoven_circle_open_small);
            mIvRapidSterilize.setImageResource(R.mipmap.img_steamoven_circle_open_small);
            mIvStoving.setImageResource(R.mipmap.img_steamoven_circle_open_small);
            mIvChildLock.setImageResource(R.mipmap.ic_child_lock_white);

            mTvReactionSterilize.setTextColor(white);
            mTvSterilize.setTextColor(white);
            mTvWarmDish.setTextColor(white);
            mTvStoving.setTextColor(white);
            mTvRapidSterilize.setTextColor(white);
            mSterilizeSwitch.setImageResource(R.mipmap.img_sterilizer_826_switch_on);

        } else {
            mIvReactionSterilize.setImageResource(R.mipmap.img_sterictr_min_circle_grey);
            mIvSterilize.setImageResource(R.mipmap.img_sterictr_min_circle_grey);
            mIvWarmDish.setImageResource(R.mipmap.img_sterictr_min_circle_grey);
            mIvRapidSterilize.setImageResource(R.mipmap.img_sterictr_min_circle_grey);
            mIvStoving.setImageResource(R.mipmap.img_sterictr_min_circle_grey);
            mIvChildLock.setImageResource(R.mipmap.ic_child_lock_grey);
            mTvReactionSterilize.setTextColor(grey);
            mTvSterilize.setTextColor(grey);
            mTvWarmDish.setTextColor(grey);
            mTvStoving.setTextColor(grey);
            mTvRapidSterilize.setTextColor(grey);
            mSterilizeSwitch.setImageResource(R.mipmap.img_sterilizer_826_switch_on);

        }


    }

    /**
     * 设置报警弹出框
     */
    private void setAlarmDialog(short alarmId) {

        LogUtils.i("20171219","alarmId:"+alarmId);
        switch (alarmId) {
            case 0://门控报警
                IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_09);
                dialogByType.setToastShowTime(DialogUtil.LENGTH_CENTER);
                dialogByType.setContentText(R.string.device_alarm_gating_content);
                dialogByType.show();
                break;
            default:
                break;
        }
    }

    //监听设备并作出提示语句
    private void deviceListenerAndToast(short status) {
        if (deviceNotConnectdToast()) return;
        if (status == 0) {
            mDialogToast.setContentText(R.string.open_device);
            mDialogToast.setToastShowTime(DialogUtil.LENGTH_CENTER);
            mDialogToast.show();
        }

    }

    //设备是否在线提醒
    private boolean deviceNotConnectdToast() {
        LogUtils.i("20171121", "isConnected:" + steri.isConnected());
        if (!steri.isConnected()) {
            if (null != mDialogToast) {
                mDialogToast.setContentText(R.string.device_connected);
                mDialogToast.setToastShowTime(DialogUtil.LENGTH_CENTER);
                mDialogToast.show();
            }
            return true;
        }
        return false;
    }

    //报警提示
    private void gatingAlarmHint() {
        mDialogToast.setToastShowTime(DialogUtil.LENGTH_CENTER);
        mDialogToast.setContentText(R.string.device_alarm_close_content);
        mDialogToast.show();
        return;
    }


    //停止工作
    private void stopWorking(final boolean lean) {
        mClosedialog.setTitleText(R.string.close_work);
        mClosedialog.setContentText(R.string.is_close_work);
        mClosedialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClosedialog.dismiss();
                setStatus(lean);
            }
        });
        mClosedialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClosedialog.isShow()) {
                    mClosedialog.dismiss();
                }
            }
        });
        mClosedialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        if (mTimer != null) {
            mTimer.cancel();
        }
        if (mMyTask != null) {
            mMyTask.cancel();
        }
        cx.unregisterReceiver(mReceiver);
        if (mReceiver != null) {
            mReceiver = null;
        }

    }

    //时间显示闪烁
    private void setPoint() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mMyTask == null) {
            mMyTask = new MyTask();
        }
        mTimer.schedule(mMyTask, 0, 1000);
    }


    class MyTask extends TimerTask {
        //时间显示中间两点闪烁标志位
        int i = 0;

        @Override
        public void run() {
            Message message = mHandler.obtainMessage();
            message.what = i;
            message.arg1 = 1;
            mHandler.sendMessage(message);
            i++;
        }
    }

    class ScreenOnOffReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 关屏的操作
            if ("android.intent.action.SCREEN_OFF".equals(action)) {
                // 当手机关屏时，我们同时也锁屏
                mRokiLockView.setVisibility(View.VISIBLE);
            }
        }
    }

}
