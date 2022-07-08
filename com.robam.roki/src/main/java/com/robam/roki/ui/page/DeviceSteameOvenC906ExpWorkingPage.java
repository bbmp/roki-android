package com.robam.roki.ui.page;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.robam.common.events.SteamOvenOneAlarmEvent;
import com.robam.common.events.SteamOvenOneStatusChangedEvent;
import com.robam.common.events.SteamOvenOneWorkFinishEvent;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneModel;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneWorkStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.dialog.SteamOvenC906ExpPauseSettingDialog;
import com.robam.roki.utils.DialogUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/19.
 */

public class DeviceSteameOvenC906ExpWorkingPage extends BasePage {


    AbsSteameOvenOne steameOvenC906;
    String guid;
    short from;//来源 1 从主目录，0从菜谱控制
//    Timer mTimer;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_working_settemp)
    TextView mTvWorkingSettemp;
    @InjectView(R.id.tv_working_settemp_text)
    TextView mTvWorkingSettempText;
    @InjectView(R.id.iv_working_img_pauseic1)
    ImageView mIvWorkingImgPauseic1;
    @InjectView(R.id.tv_working_settime)
    TextView mTvWorkingSettime;
    @InjectView(R.id.iv_working_img_pauseic2)
    ImageView mIvWorkingImgPauseic2;
    @InjectView(R.id.ll_working_pause)
    LinearLayout mLlWorkingPause;
    @InjectView(R.id.ll_working_view3)
    LinearLayout mLlWorkingView3;
    @InjectView(R.id.ll_working_view4)
    LinearLayout mLlWorkingView4;
    @InjectView(R.id.tv_working_real_temp)
    TextView mTvWorkingRealTemp;
    @InjectView(R.id.tv_working_real_time)
    TextView mTvWorkingRealTime;
    @InjectView(R.id.iv_working_img_circle)
    ImageView mIvWorkingImgCircle;
    @InjectView(R.id.tv_working_circleabove)
    TextView mTvWorkingCircleabove;
    @InjectView(R.id.iv_working_img_circledown)
    ImageView mIvWorkingImgCircledown;
    @InjectView(R.id.tv_working_circledown)
    TextView mTvWorkingCircledown;
    @InjectView(R.id.ll_working_midcontent)
    LinearLayout mLlWorkingMidcontent;
    @InjectView(R.id.iv_working_img_pause)
    ImageView mIvWorkingImgPause;
    @InjectView(R.id.iv_working_img_finish)
    ImageView mIvWorkingImgFinish;
    @InjectView(R.id.tv_working_finish)
    TextView mTvWorkingFinish;
    @InjectView(R.id.fl_working_middle)
    FrameLayout mFlWorkingMiddle;
    @InjectView(R.id.iv_working_img_light_circle)
    ImageView mIvWorkingImgLightCircle;
    @InjectView(R.id.iv_working_img_light)
    ImageView mIvWorkingImgLight;
    @InjectView(R.id.fr_working_light)
    FrameLayout mFrWorkingLight;
    @InjectView(R.id.iv_working_img_switch)
    ImageView mIvWorkingImgSwitch;
    @InjectView(R.id.ll_working_switch)
    LinearLayout mLlWorkingSwitch;
    @InjectView(R.id.v_working_view2)
    View mVWorkingView2;
    @InjectView(R.id.tv_title)
    TextView mTvTitle;
    @InjectView(R.id.v_working_view1)
    View mVWorkingView1;
    @InjectView(R.id.btn_one)
    Button mBtnOne;
    @InjectView(R.id.btn_two)
    Button mBtnTwo;
    @InjectView(R.id.btn_there)
    Button mBtnThere;
    @InjectView(R.id.tv_working_setDownTemp)
    TextView mTvWorkingSetDownTemp;
    @InjectView(R.id.tv_working_setDownTemp_text)
    TextView mTvWorkingSetDownTempText;
    @InjectView(R.id.tv_working_real_down_temp)
    TextView mTvWorkingRealDownTemp;
    @InjectView(R.id.iv_working_img_pauseic3)
    ImageView mIvWorkingImgPauseic3;
    @InjectView(R.id.iv_water)
    ImageView mIvWater;
    private boolean training_lock = false;
    private Animation circleRotate;
    SteamOvenC906ExpPauseSettingDialog mSteamOvenC906Dialog;
    private final int Time_Refresh = 0;
    private short mTime;//倒计时
    private int mAlarmSing;
    private IRokiDialog mRokiDialog;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case Time_Refresh:
//                    TimeCountdown();
                    break;
            }


        }
    };
    private IRokiDialog mClosedialog;


    //时间倒计时
    private void TimeCountdown() {
        if (steameOvenC906.leftTime == 0) return;
        short min = (short) (steameOvenC906.leftTime / 60);
        short sec = (short) (steameOvenC906.leftTime % 60);
        try {
            if (sec < 10)
                mTvWorkingRealTime.setText(min + ":0" + sec);
            else
                mTvWorkingRealTime.setText(min + ":" + sec);
        } catch (Exception e) {
            e.printStackTrace();
        } /*finally {
            if (mTime == 0) {
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer = null;
                }
                return;
            }
            if (steameOvenC906.worknStatus == SteamOvenOneWorkStatus.Working) {
                mTime--;
            }
        }*/
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        from = bd == null ? 0 : bd.getShort("from");
        steameOvenC906 = Plat.deviceService.lookupChild(guid);
        View view = inflater.inflate(R.layout.page_device_oven_c906_exp_working, null, false);
        mRokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
        ButterKnife.inject(this, view);
        return view;
    }

    @Subscribe
    public void onEvent(SteamOvenOneStatusChangedEvent event) {
        if (steameOvenC906 == null || !Objects.equal(steameOvenC906.getID(), event.pojo.getID()))
            return;
        if (event.pojo.workState == SteamOvenOneWorkStatus.PreHeat &&
                event.pojo.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
            setPreHeatModel();
        } else if (event.pojo.workState == SteamOvenOneWorkStatus.Working &&
                event.pojo.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
            setWorkMode();
        } else if (event.pojo.powerOnStatus == SteamOvenOnePowerOnStatus.Pause &&
                event.pojo.powerState == SteamOvenOnePowerStatus.On) {
            setPauseMolde();
        } else if (event.pojo.powerOnStatus == SteamOvenOnePowerOnStatus.Order) {
            setOrderMolde();
        } else if (event.pojo.powerState == SteamOvenOnePowerStatus.Off) {
            back();
        } else if (event.pojo.powerState == SteamOvenOnePowerStatus.On && !steameOvenC906.isConnected()) {
            back();
        } else if (event.pojo.powerOnStatus == SteamOvenOnePowerOnStatus.AlarmStatus) {
            setPauseMolde();
        }else {
            back();
        }
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (steameOvenC906 == null || !Objects.equal(steameOvenC906.getID(), event.device.getID()))
            return;
        if (!event.isConnected) {
            back();
        }

    }

    @Subscribe
    public void onEvent(SteamOvenOneWorkFinishEvent event) {
//        if (event.finish != 0) return;
        training_lock = true;

        stopAnimation();
        mIvWorkingImgFinish.setVisibility(View.VISIBLE);
       /* if (!isRunningForeground()) {
            training_lock = false;
            return;
        }*/
       /* mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                back();
            }
        }, 2500);*/
    }

    @Subscribe
    public void onEvent(SteamOvenOneAlarmEvent event) {
        AbsSteameOvenOne steameOvenOne = event.steameOvenOne;
        String guid = steameOvenOne.getGuid().getGuid();
        short[] alarms = event.alarms;
        for (int i = 0; i < alarms.length; i++) {
            short alarm = alarms[i];
            steamOvenOneAlarmStatus(i, alarm, guid);
        }

    }

    private void steamOvenOneAlarmStatus(int i, short alarm, String guid) {
        if (alarm != 0) {
            LogUtils.i("20171128", " i:" + i + " alarm" + alarm);
            switch (i) {
                case 4:
                case 5:
                case 6:
                    mAlarmSing = i;
                    break;
            }
        }
    }


    /**
     * 预热模式
     */
    private void setPreHeatModel() {
        light_change();
        if (mSteamOvenC906Dialog != null && mSteamOvenC906Dialog.isShowing()) {
            mSteamOvenC906Dialog.dismiss();
        }
        mIvWater.setVisibility(View.GONE);
        mIvWorkingImgPauseic1.setVisibility(View.GONE);
        mIvWorkingImgPauseic2.setVisibility(View.GONE);
        mIvWorkingImgPauseic3.setVisibility(View.GONE);
        mIvWorkingImgCircle.setVisibility(View.VISIBLE);
        mIvWorkingImgPause.setVisibility(View.GONE);

        mTvWorkingSettemp.setText(steameOvenC906.setTemp + "");//设置温度
        mTvWorkingSetDownTemp.setText(steameOvenC906.setTempDownValue + "");//设置下温度
        mTvWorkingSettime.setText(steameOvenC906.setTime + "");//设置时间
        TimeCountdown();
        mTvWorkingRealTemp.setText(steameOvenC906.temp + "");//温度
        mTvWorkingRealDownTemp.setText(steameOvenC906.currentTempDownValue + "");

        startAnimation();
        mIvWorkingImgCircledown.setVisibility(View.VISIBLE);
        mTvWorkingCircledown.setVisibility(View.GONE);
        mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_oven026work_preheating);
        mTvWorkingCircleabove.setText(R.string.device_preheating);
        multiNumStep();
       /* if (mTimer == null || (steameOvenC906.leftTime > 3) || (steameOvenC906.leftTime < 0)) {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;//时间暂停
            }
            setTimeBySeconds();
        }*/
        if (steameOvenC906.leftTime <= 2) {
            InEndMode();//完成模式
        }

    }

    /**
     * 预约模式
     */
    void setOrderMolde() {
        startAnimation();
        light_change();
        mIvWater.setVisibility(View.GONE);
        mIvWorkingImgPauseic1.setVisibility(View.GONE);
        mIvWorkingImgPauseic2.setVisibility(View.GONE);
        mIvWorkingImgCircle.setVisibility(View.VISIBLE);
        mIvWorkingImgPause.setVisibility(View.GONE);

        mTvWorkingSettemp.setText(steameOvenC906.setTemp + "");//设置温度
        mTvWorkingSetDownTemp.setText(steameOvenC906.setTempDownValue + "");//设置下温度
        mTvWorkingSettime.setText(steameOvenC906.setTime + "");//设置时间
        TimeCountdown();
        mTvWorkingRealTemp.setText(steameOvenC906.temp + "");//温度
        mTvWorkingRealDownTemp.setText(steameOvenC906.currentTempDownValue + "");
        mIvWorkingImgCircledown.setVisibility(View.GONE);
        mTvWorkingCircledown.setVisibility(View.VISIBLE);

        mTvWorkingCircleabove.setText(cx.getString(R.string.device_steamOvenOne_order_start_time));
        short ordertime_hour = steameOvenC906.ordertime_hour;
        short ordertime_min = steameOvenC906.ordertime_min;
        String stroth = String.valueOf(ordertime_hour);
        String strotm = String.valueOf(ordertime_min);
        if (stroth.length() == 1 && strotm.length() == 1) {
            mTvWorkingCircledown.setText("0" + steameOvenC906.ordertime_hour + ":0" + steameOvenC906.ordertime_min);
        } else if (stroth.length() == 1 && strotm.length() == 2) {
            mTvWorkingCircledown.setText("0" + steameOvenC906.ordertime_hour + ":" + steameOvenC906.ordertime_min);
        } else if (stroth.length() == 2 && strotm.length() == 1) {
            mTvWorkingCircledown.setText(steameOvenC906.ordertime_hour + ":0" + steameOvenC906.ordertime_min);
        } else {
            mTvWorkingCircledown.setText(steameOvenC906.ordertime_hour + ":" + steameOvenC906.ordertime_min);
        }
        /*if (mTimer == null || (steameOvenC906.leftTime - mTime > 3) || (steameOvenC906.leftTime - mTime < 0)) {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;//时间暂停
            }
            setTimeBySeconds();
        }*/
    }

    /**
     * 暂停模式
     */
    void setPauseMolde() {
        light_change();
        stopAnimation();

        if (steameOvenC906.alarm == 64 && SteamOvenOnePowerOnStatus.AlarmStatus == steameOvenC906.powerOnStatus){
            mRokiDialog.setContentText(R.string.device_alarm_water_out);
            mRokiDialog.setToastShowTime(DialogUtil.LENGTH_LONG);
            mRokiDialog.show();
        }
        mIvWater.setVisibility(View.VISIBLE);
        mIvWorkingImgPauseic1.setVisibility(View.VISIBLE);
        mIvWorkingImgPauseic2.setVisibility(View.VISIBLE);
        mIvWorkingImgPauseic3.setVisibility(View.VISIBLE);
        mIvWorkingImgCircle.setVisibility(View.GONE);
        mIvWorkingImgPause.setVisibility(View.VISIBLE);
        mTvWorkingSettemp.setText(steameOvenC906.setTemp + "");//设置温度
        mTvWorkingSetDownTemp.setText(steameOvenC906.setTempDownValue + "");//设置下温度
        mTvWorkingSettime.setText(steameOvenC906.setTime + "");//设置时间
        TimeCountdown();
        mTvWorkingRealTemp.setText(steameOvenC906.temp + "");//温度
        mTvWorkingRealDownTemp.setText(steameOvenC906.currentTempDownValue + "");
        mIvWorkingImgCircledown.setVisibility(View.VISIBLE);
        mTvWorkingCircledown.setVisibility(View.GONE);
        initModel();
        multiNumStep();
       /* if (mTimer == null || (steameOvenC906.leftTime - mTime > 3) || (steameOvenC906.leftTime - mTime < 0)) {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;//时间暂停
            }
            setTimeBySeconds();
        }*/
    }

    /**
     * 工作模式
     */
    void setWorkMode() {
        light_change();
        if (mSteamOvenC906Dialog != null && mSteamOvenC906Dialog.isShowing()) {
            mSteamOvenC906Dialog.dismiss();
        }
        mIvWater.setVisibility(View.GONE);
        mIvWorkingImgPauseic1.setVisibility(View.GONE);
        mIvWorkingImgPauseic2.setVisibility(View.GONE);
        mIvWorkingImgPauseic3.setVisibility(View.GONE);
        mIvWorkingImgCircle.setVisibility(View.VISIBLE);
        mIvWorkingImgPause.setVisibility(View.GONE);
        mTvWorkingSettemp.setText(steameOvenC906.setTemp + "");//设置温度
        mTvWorkingSetDownTemp.setText(steameOvenC906.setTempDownValue + "");//设置下温度
        mTvWorkingSettime.setText(steameOvenC906.setTime + "");//设置时间
        TimeCountdown();
        mTvWorkingRealTemp.setText(steameOvenC906.temp + "");//温度
        mTvWorkingRealDownTemp.setText(steameOvenC906.currentTempDownValue + "");
        startAnimation();
        mIvWorkingImgCircledown.setVisibility(View.VISIBLE);
        mTvWorkingCircledown.setVisibility(View.GONE);
        initModel();
        multiNumStep();
        /*if (mTimer == null || (steameOvenC906.leftTime - mTime > 3) || (steameOvenC906.leftTime - mTime < 0)) {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;//时间暂停
            }
            setTimeBySeconds();
        }*/

        if (steameOvenC906.multiSumStep == 0) {
            if (steameOvenC906.leftTime <= 3) {
                InEndMode();//完成模式
            }
        } else if (steameOvenC906.multiSumStep == steameOvenC906.SectionOfTheStep) {
            if (steameOvenC906.leftTime <= 3) {
                InEndMode();//完成模式
            }
        }
    }

    //多端烹饪步鄹
    private void multiNumStep() {
        if (steameOvenC906.multiSumStep != 0) {
            mBtnOne.setVisibility(View.VISIBLE);
            mBtnTwo.setVisibility(View.VISIBLE);
            mBtnThere.setVisibility(View.VISIBLE);
        }

        switch (steameOvenC906.SectionOfTheStep) {
            case 1:
                mBtnOne.setTextColor(getResources().getColor(R.color.white));
                mBtnOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn_bg));
                mBtnTwo.setTextColor(getResources().getColor(R.color.gray));
                mBtnTwo.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                mBtnThere.setTextColor(getResources().getColor(R.color.gray));
                mBtnThere.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                break;
            case 2:
                mBtnTwo.setTextColor(getResources().getColor(R.color.white));
                mBtnTwo.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn_bg));
                mBtnOne.setTextColor(getResources().getColor(R.color.gray));
                mBtnOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                mBtnThere.setTextColor(getResources().getColor(R.color.gray));
                mBtnThere.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                break;
            case 3:
                mBtnThere.setTextColor(getResources().getColor(R.color.white));
                mBtnThere.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn_bg));
                mBtnTwo.setTextColor(getResources().getColor(R.color.gray));
                mBtnTwo.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                mBtnOne.setTextColor(getResources().getColor(R.color.gray));
                mBtnOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                mBtnThere.setTextColor(getResources().getColor(R.color.gray));
                break;
        }
    }


    private void InEndMode() {
        stopAnimation();
        mIvWorkingImgCircle.setImageResource(R.mipmap.img_steamoven_finish);
        mTvWorkingCircleabove.setText(cx.getString(R.string.device_finish));
        mIvWorkingImgCircledown.setVisibility(View.GONE);
        mTvWorkingRealTime.setText("0:00");
        try {
            Thread.sleep(3600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            UIService.getInstance().popBack();
        }
    }

    /*//设置倒计时
    private void setTimeBySeconds() {
        mTime = steameOvenC906.leftTime;
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(Time_Refresh);
            }
        }, 0, 1000);

    }*/

    //灯控
    void light_change() {
        if (steameOvenC906.light == 1) {
            mIvWorkingImgLightCircle.setImageResource(R.mipmap.ic_count_stove_on);
            mIvWorkingImgLight.setImageResource(R.mipmap.ic_fan8700_light_yellow);
        } else {
            mIvWorkingImgLightCircle.setImageResource(R.mipmap.img_steamoven_circle_open_small);
            mIvWorkingImgLight.setImageResource(R.mipmap.ic_fan8700_light_white);
        }
    }

    void initModel() {
        switch (steameOvenC906.workModel) {
            case SteamOvenOneModel.EXP://EXP
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_906ovenwork_exp);
                mTvWorkingCircleabove.setText(cx.getString(R.string.device_steamOvenOne_name_exp));
                break;
        }
    }

    /**
     * 开启动画
     */
    void startAnimation() {
        if (circleRotate == null) {
            circleRotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_oven_circle_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotate.setInterpolator(lin);
            mIvWorkingImgCircle.startAnimation(circleRotate);

        }
    }

    /*public boolean isRunningForeground() {
        ActivityManager am = (ActivityManager) cx.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (currentPackageName != null && currentPackageName.equals(getActivity().getPackageName())) {
            return true;
        }
        return false;
    }*/

    private void back() {
        if (mClosedialog != null && mClosedialog.isShow()){
            mClosedialog.dismiss();
        }
        if (from == 1) {
            UIService.getInstance().popBack();

        } else {
            UIService.getInstance().popBack();
            UIService.getInstance().popBack();
        }
    }

    /**
     * 关闭动画和烟雾效果
     */
    private void stopAnimation() {
        if (circleRotate != null)
            circleRotate.cancel();
        circleRotate = null;
        mIvWorkingImgCircle.clearAnimation();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        if (mSteamOvenC906Dialog != null && mSteamOvenC906Dialog.isShowing()) {
            mSteamOvenC906Dialog.dismiss();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        back();
        return true;
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        UIService.getInstance().popBack();
        back();
    }


    @OnClick(R.id.iv_water)
    public void onMIvWaterClicked() {
        if (steameOvenC906.WaterStatus == 0) {
            steameOvenC906.setSteameOvenOneWaterPop((short) 1, new VoidCallback() {
                @Override
                public void onSuccess() {}
                @Override
                public void onFailure(Throwable t) {}
            });
        }
    }

    @OnClick(R.id.fl_working_middle)
    public void onMFlWorkingMiddleClicked() {
//        if (steameOvenC906.WaterStatus == 1) {
//            if (mRokiDialog != null){
//                mRokiDialog.setContentText(R.string.device_alarm_water_out);
//                mRokiDialog.setToastShowTime(DialogUtil.LENGTH_LONG);
//                mRokiDialog.show();
//                return;
//            }
//        }
        if (steameOvenC906.powerOnStatus == SteamOvenOnePowerOnStatus.Order){
            if (mRokiDialog != null){
                mRokiDialog.setContentText(R.string.device_steamOvenOne_name_model_not_content);
                mRokiDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
                mRokiDialog.show();
            }
        }

        if (steameOvenC906.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
            steameOvenC906.setSteamOvenOneStatusControl(SteamOvenOnePowerOnStatus.Pause, null);
        } else if (steameOvenC906.powerOnStatus == SteamOvenOnePowerOnStatus.Pause) {
            steameOvenC906.setSteamOvenOneStatusControl(SteamOvenOnePowerOnStatus.WorkingStatus, null);
        }
    }

    @OnClick(R.id.ll_working_pause)
    public void onMLlWorkingPauseClicked() {

        if (mIvWorkingImgPauseic1.getVisibility() == View.VISIBLE) {
            Pause_TempAndTimeSet();
        }

    }

    @OnClick(R.id.fr_working_light)
    public void onMFrWorkingLightClicked() {

        if (steameOvenC906.light == 1) {
            steameOvenC906.setLightControl((short) 0, new VoidCallback() {
                @Override
                public void onSuccess() {
                    mIvWorkingImgLightCircle.setImageResource(R.mipmap.img_steamoven_circle_open_small);
                    mIvWorkingImgLight.setImageResource(R.mipmap.ic_fan8700_light_white);
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        } else {
            steameOvenC906.setLightControl((short) 1, new VoidCallback() {
                @Override
                public void onSuccess() {
                    mIvWorkingImgLightCircle.setImageResource(R.mipmap.ic_count_stove_on);
                    mIvWorkingImgLight.setImageResource(R.mipmap.ic_fan8700_light_yellow);
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        }
    }

    @OnClick(R.id.ll_working_switch)
    public void onMLlWorkingSwitchClicked() {
        mClosedialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        mClosedialog.setTitleText(R.string.close_work);
        mClosedialog.setContentText(R.string.is_close_work);
        mClosedialog.show();
        mClosedialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClosedialog.dismiss();
                if (mAlarmSing == 4 || mAlarmSing == 5 || mAlarmSing == 6) {
                    steameOvenC906.setSteameOvenStatus(SteamOvenOnePowerStatus.Off, SteamOvenOnePowerOnStatus.OperatingState, null);
                } else {
                    steameOvenC906.setSteameOvenStatus(SteamOvenOnePowerStatus.On, SteamOvenOnePowerOnStatus.OperatingState, null);
                }
                back();
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
    }


    //时间调节
    private void Pause_TempAndTimeSet() {
        mSteamOvenC906Dialog = new SteamOvenC906ExpPauseSettingDialog(cx, steameOvenC906.workModel, new SteamOvenC906ExpPauseSettingDialog.PickListener() {

            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(Integer uptemp, Integer downtemp, Integer min) {
                if (steameOvenC906.workModel == SteamOvenOneModel.EXP) {
                    steameOvenC906.setSteameOvenOneRunMode(SteamOvenOneModel.EXP, Short.valueOf(String.valueOf(min)),
                            Short.valueOf(String.valueOf(uptemp)), (short) 0, Short.valueOf(String.valueOf(downtemp)),
                            (short) 0, (short) 0, null);

                    mSteamOvenC906Dialog.dismiss();
                }
            }
        });
        Window win = mSteamOvenC906Dialog.getWindow();
        win.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(wl);
        mSteamOvenC906Dialog.show();
    }


}
