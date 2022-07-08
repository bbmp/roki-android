package com.robam.roki.ui.page;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
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
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
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
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.roki.utils.StringConstantsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/19.
 */

public class DeviceOvenC906WorkingPage extends BasePage {


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
    @InjectView(R.id.iv_water)
    ImageView mIvWater;
    @InjectView(R.id.tv_min)
    TextView mTvMin;
    private boolean training_lock = false;
    LayoutInflater inflater;
    View contentView;
    Animation circleRotate;
    private IRokiDialog mTempAndTimeDialog;
    private final int Time_Refresh = 0;
    private short mTime;//倒计时
    private List<String> mStringTimeList = new ArrayList<String>();
    private List<String> mStringTempList = new ArrayList<String>();
    private int mAlarmSing;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case Time_Refresh:
//                    TimeCountdown();
                    break;
                case 1:
                    setDeviceRunData((String) msg.obj);
                    break;
                case 2:
                    setDeviceRunData((String) msg.obj);
                    break;
            }


        }
    };
    private IRokiDialog mRokiDialog;
    private IRokiDialog mClosedialog;


    //设置设备运行数据
    private void setDeviceRunData(String str) {
        if (str.contains(StringConstantsUtil.STRING_DEGREE_CENTIGRADE)) {
            String removetTempString = RemoveManOrsymbolUtil.getRemoveString(str);
            mStringTempList.add(removetTempString);
        }
        if (str.contains(StringConstantsUtil.STRING_MINUTES) || str.equals("--")) {
            String removeTimeString = RemoveManOrsymbolUtil.getRemoveString(str);
            if (steameOvenC906.workModel == SteamOvenOneModel.KUAISUYURE ){
                mStringTimeList.add("90");
            }else if (steameOvenC906.workModel == SteamOvenOneModel.BAOWEN ){
                mStringTimeList.add("120");
            }else {
                mStringTimeList.add(removeTimeString);
            }

        }
        mTempAndTimeDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTempAndTimeDialog.isShow()) {
                    mTempAndTimeDialog.dismiss();
                    short n = (short) 0;
                    steameOvenC906.setSteameOvenOneRunMode(steameOvenC906.workModel,
                            Short.valueOf(mStringTimeList.get(mStringTimeList.size() - 1)),
                            Short.valueOf(mStringTempList.get(mStringTempList.size() - 1)),
                            n, new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                }
                            });
                }
            }
        });
    }

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
            if (steameOvenC906.leftTime == 0) {
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

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        from = bd == null ? 0 : bd.getShort("from");
//        LogUtils.i("20171128", "guid:" + guid);
//        LogUtils.i("20171128", "appGuid:" + Plat.appGuid);
        steameOvenC906 = Plat.deviceService.lookupChild(guid);
        this.inflater = inflater;
        contentView = inflater.inflate(R.layout.page_device_oven_c906_working,
                container, false);
        ButterKnife.inject(this, contentView);
        mRokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
        return contentView;
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
        if (!steameOvenC906.isConnected()) {
            back();
        }
    }

    @Subscribe
    public void onEvent(SteamOvenOneWorkFinishEvent event) {
       /* LogUtils.i("20180116", "--event:" + event.finish);
        if (event.finish != 0) return;
        stopAnimation();
        mIvWorkingImgCircle.clearAnimation();
        mIvWorkingImgCircle.setVisibility(View.GONE);
        mIvWorkingImgPause.setVisibility(View.GONE);
        mLlWorkingMidcontent.setVisibility(View.GONE);
        mIvWorkingImgFinish.setVisibility(View.VISIBLE);
        mTvWorkingCircleabove.setText(R.string.device_finish);*/

       /* Timer mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
               UIService.getInstance().popBack();
            }
        }, 3000);*/
    }

    @Subscribe
    public void onEvent(SteamOvenOneAlarmEvent event) {
        AbsSteameOvenOne steameOvenOne = event.steameOvenOne;
        String guid = steameOvenOne.getGuid().getGuid();
        short[] alarms = event.alarms;
        for (int i = 0; i < alarms.length; i++) {
            short alarm = alarms[i];
            steamOvenOneAlarmStatus(i,alarm, guid);
        }

    }

    /**
     * 开启动画
     */
    void startAnimation() {
        if (circleRotate == null) {
            circleRotate = AnimationUtils.loadAnimation(cx, R.anim.device_oven_circle_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotate.setInterpolator(lin);
            mIvWorkingImgCircle.startAnimation(circleRotate);
        }
    }

    /**
     * 关闭动画
     */
    private void stopAnimation() {
        if (circleRotate != null){
            circleRotate.cancel();
            circleRotate = null;
            mIvWorkingImgCircle.clearAnimation();
        }
    }

    private void steamOvenOneAlarmStatus(int i, short alarm, String guid) {
        if (alarm != 0){
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
        startAnimation();
        light_change();
//        water_change();
        if (mTempAndTimeDialog != null && mTempAndTimeDialog.isShow()){
            mTempAndTimeDialog.dismiss();
        }
        mIvWorkingImgPauseic1.setVisibility(View.GONE);
        mIvWorkingImgPauseic2.setVisibility(View.GONE);
        mIvWorkingImgCircle.setVisibility(View.VISIBLE);
        mIvWorkingImgPause.setVisibility(View.GONE);
        mIvWater.setVisibility(View.GONE);
        mTvWorkingSettemp.setText(steameOvenC906.setTemp + "");//设置温度
        mTvWorkingSettime.setText(steameOvenC906.setTime + "");//设置时间
        mTvWorkingRealTemp.setText(steameOvenC906.temp + "");//温度
        TimeCountdown();
        mIvWorkingImgCircledown.setVisibility(View.VISIBLE);
        mTvWorkingCircledown.setVisibility(View.GONE);
        mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_oven026work_preheating);
        mTvWorkingCircleabove.setText(R.string.device_preheating);
       /* if (steameOvenC906.workModel == SteamOvenOneModel.KUAISUYURE ||
                steameOvenC906.workModel == SteamOvenOneModel.BAOWEN) {
            mTvMin.setText("--");
        } else {
            mTvMin.setText("min");
        }*/
        multiNumStep();
        /*if (mTimer == null || (steameOvenC906.leftTime > 3) || (steameOvenC906.leftTime < 0)) {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;//时间暂停
            }
            setTimeBySeconds();
        }*/
        /*if (steameOvenC906.leftTime <= 1) {
            InEndMode();//完成模式
        }*/

    }

    /**
     * 预约模式
     */
    void setOrderMolde() {
        startAnimation();
        light_change();
        mIvWorkingImgPauseic1.setVisibility(View.GONE);
        mIvWorkingImgPauseic2.setVisibility(View.GONE);
        mIvWorkingImgCircle.setVisibility(View.VISIBLE);
        mIvWorkingImgPause.setVisibility(View.GONE);
        /*if (steameOvenC906.workModel == SteamOvenOneModel.JIEDONG ||
                steameOvenC906.workModel == SteamOvenOneModel.ZHENGQISHAJUN) {
            mIvWater.setVisibility(View.GONE);
        } else {
            mIvWater.setVisibility(View.VISIBLE);
        }*/
        mTvWorkingSettemp.setText(steameOvenC906.setTemp + "");//设置温度
        mTvWorkingSettime.setText(steameOvenC906.setTime + "");//设置时间
        mTvWorkingRealTemp.setText(steameOvenC906.temp + "");//温度
        TimeCountdown();
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
        /*if (mTimer == null || (steameOvenC906.leftTime > 3) || (steameOvenC906.leftTime  < 0)) {
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
        if (steameOvenC906.multiSumStep == 0) {
            if (steameOvenC906.workModel == SteamOvenOneModel.JIEDONG || steameOvenC906.workModel
                    == SteamOvenOneModel.ZHENGQISHAJUN || steameOvenC906.modelType == 1) {
                mIvWorkingImgPauseic1.setVisibility(View.GONE);
                mIvWorkingImgPauseic2.setVisibility(View.GONE);
//                mIvWater.setVisibility(View.GONE);
            } else {
                mIvWorkingImgPauseic1.setVisibility(View.VISIBLE);
                mIvWorkingImgPauseic2.setVisibility(View.VISIBLE);

            }

        } else if (steameOvenC906.multiSumStep != 0) {
            mIvWorkingImgPauseic1.setVisibility(View.GONE);
            mIvWorkingImgPauseic2.setVisibility(View.GONE);
            //多端时蒸模式显示水箱
            if (steameOvenC906.workModel == SteamOvenOneModel.XIANNENZHENG || steameOvenC906.workModel ==
                    SteamOvenOneModel.YINGYANGZHENG || steameOvenC906.workModel == SteamOvenOneModel.ZHENGQISHAJUN ||
                    steameOvenC906.workModel == SteamOvenOneModel.GAOWENZHENG || steameOvenC906.workModel == SteamOvenOneModel.FAXIAO) {
                mIvWater.setVisibility(View.VISIBLE);
            } else {
                mIvWater.setVisibility(View.GONE);
            }
        }

        /*if (steameOvenC906.workModel == SteamOvenOneModel.KUAISUYURE ||
                steameOvenC906.workModel == SteamOvenOneModel.BAOWEN) {
            mTvMin.setText("--");
        } else {
            mTvMin.setText("min");
        }*/
        mIvWater.setVisibility(View.VISIBLE);
        mIvWorkingImgCircle.setVisibility(View.GONE);
        mIvWorkingImgPause.setVisibility(View.VISIBLE);
        mTvWorkingSettemp.setText(steameOvenC906.setTemp + "");//设置温度
        mTvWorkingSettime.setText(steameOvenC906.setTime + "");//设置时间
        mTvWorkingRealTemp.setText(steameOvenC906.temp + "");//温度
        TimeCountdown();
//        mTvWorkingRealTime.setText(steameOvenC906.time + "");//时间
        mIvWorkingImgCircledown.setVisibility(View.VISIBLE);
        mTvWorkingCircledown.setVisibility(View.GONE);
        initModel();
        multiNumStep();
        /*if (mTimer == null || (steameOvenC906.leftTime > 3) || (steameOvenC906.leftTime < 0)) {
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
//        water_change();
        startAnimation();
        if (mTempAndTimeDialog != null && mTempAndTimeDialog.isShow()){
            mTempAndTimeDialog.dismiss();
        }
        mIvWorkingImgPauseic1.setVisibility(View.GONE);
        mIvWorkingImgPauseic2.setVisibility(View.GONE);
        mIvWorkingImgCircle.setVisibility(View.VISIBLE);
        mIvWorkingImgPause.setVisibility(View.GONE);
        mIvWater.setVisibility(View.GONE);
        mTvWorkingSettemp.setText(steameOvenC906.setTemp + "");//设置温度
        mTvWorkingSettime.setText(steameOvenC906.setTime + "");//设置时间
        mTvWorkingRealTemp.setText(steameOvenC906.temp + "");//温度
//        mTvWorkingRealTime.setText(steameOvenC906.leftTime + "");//时间
        TimeCountdown();
        mIvWorkingImgCircledown.setVisibility(View.VISIBLE);
        mTvWorkingCircledown.setVisibility(View.GONE);
        initModel();
        multiNumStep();
        /*if (mTimer == null || (steameOvenC906.leftTime > 3) || (steameOvenC906.leftTime < 0)) {
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
            if (steameOvenC906.multiSumStep == 2) {
                mBtnOne.setVisibility(View.VISIBLE);
                mBtnTwo.setVisibility(View.INVISIBLE);
                mBtnThere.setVisibility(View.VISIBLE);
            } else if (steameOvenC906.multiSumStep == 3) {
                mBtnOne.setVisibility(View.VISIBLE);
                mBtnTwo.setVisibility(View.VISIBLE);
                mBtnThere.setVisibility(View.VISIBLE);
            }
        } else if (steameOvenC906.multiSumStep == 0) {
            mBtnOne.setVisibility(View.INVISIBLE);
            mBtnTwo.setVisibility(View.INVISIBLE);
            mBtnThere.setVisibility(View.INVISIBLE);
        }
        if (steameOvenC906.multiSumStep == 2) {
            mBtnTwo.setVisibility(View.INVISIBLE);
            switch (steameOvenC906.SectionOfTheStep) {
                case 1:
                    mBtnOne.setTextColor(getResources().getColor(R.color.white));
                    mBtnOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn_bg));
                    mBtnThere.setTextColor(getResources().getColor(R.color.gray));
                    mBtnThere.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                    mBtnThere.setText("2");
                    break;
                case 2:
                    mBtnOne.setTextColor(getResources().getColor(R.color.gray));
                    mBtnOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                    mBtnThere.setTextColor(getResources().getColor(R.color.white));
                    mBtnThere.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn_bg));
                    mBtnThere.setText("2");
                    break;
            }
        } else if (steameOvenC906.multiSumStep == 3) {
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
                    mBtnTwo.setBackgroundColor(getResources().getColor(R.color.c11));
                    mBtnOne.setTextColor(getResources().getColor(R.color.gray));
                    mBtnOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                    mBtnThere.setTextColor(getResources().getColor(R.color.gray));
                    mBtnThere.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                    break;
                case 3:
                    mBtnThere.setTextColor(getResources().getColor(R.color.white));
                    mBtnThere.setBackgroundColor(getResources().getColor(R.color.c11));
                    mBtnOne.setTextColor(getResources().getColor(R.color.gray));
                    mBtnOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                    mBtnTwo.setTextColor(getResources().getColor(R.color.gray));
                    mBtnTwo.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                    break;
            }
        }

    }

    private void InEndMode() {
        stopAnimation();
        mIvWorkingImgCircle.setVisibility(View.GONE);
        mIvWater.setVisibility(View.GONE);
        mLlWorkingMidcontent.setVisibility(View.GONE);
        mIvWorkingImgPause.setVisibility(View.GONE);
        mTvWorkingRealTime.setText("0:00");
        mTvWorkingFinish.setVisibility(View.VISIBLE);
        mIvWorkingImgFinish.setVisibility(View.VISIBLE);
        mTvWorkingCircleabove.setText(R.string.device_finish);
        try {
            Thread.sleep(2500);
        } catch (Exception e) {
                    e.printStackTrace();
        }finally {
                    UIService.getInstance().popBack();
        }


    }

    //设置倒计时
   /* private void setTimeBySeconds() {
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

    void water_change() {
        if (steameOvenC906.WaterStatus == 1) {
            IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
            dialogByType.setContentText(R.string.device_water_good);
            dialogByType.setToastShowTime(DialogUtil.LENGTH_CENTER);
            dialogByType.show();
            return;
        }
    }

    void initModel() {
        switch (steameOvenC906.workModel) {
            //烤箱
            case SteamOvenOneModel.KUAISUYURE://快速预热
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_906ovenwork_kuaisuyure_white);
                mTvWorkingCircleabove.setText(cx.getString(R.string.device_steamOvenOne_name_kuaisuyure));
                break;
            case SteamOvenOneModel.KUAIRE://快热
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_906ovenwork_kuaire_white);
                mTvWorkingCircleabove.setText(cx.getString(R.string.device_steamOvenOne_name_kuaire));
                break;
            case SteamOvenOneModel.FENGPEIKAO://风焙烤
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_906ovenwork_fengbeikao);
                mTvWorkingCircleabove.setText(cx.getString(R.string.device_steamOvenOne_name_fengpeikao));
                break;
            case SteamOvenOneModel.PEIKAO://焙烤
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_906ovenwork_beikao);
                mTvWorkingCircleabove.setText(cx.getString(R.string.device_steamOvenOne_name_peikao));
                break;
            case SteamOvenOneModel.FENGSHAIKAO://风扇烤
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_906ovenwork_fengshankao);
                mTvWorkingCircleabove.setText(cx.getString(R.string.device_steamOvenOne_name_fengshankao));
                break;
            case SteamOvenOneModel.SHAOKAO://烤烧
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_026ovenwork_kaoshao_white);
                mTvWorkingCircleabove.setText(cx.getString(R.string.device_steamOvenOne_name_kaoshao));
                break;
            case SteamOvenOneModel.QIANGSHAOKAO://强烤烧
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_906ovenwork_qiangshaokao);
                mTvWorkingCircleabove.setText(cx.getString(R.string.device_steamOvenOne_name_qiangshaokao));
                break;
            case SteamOvenOneModel.JIANKAO://煎烤
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_906ovenwork_jiankao_white);
                mTvWorkingCircleabove.setText(cx.getString(R.string.device_steamOvenOne_name_jiankao));
                break;
            case SteamOvenOneModel.DIJIARE://底加热
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_906ovenwork_dijiare_white);
                mTvWorkingCircleabove.setText(cx.getString(R.string.device_steamOvenOne_name_dijiare));
                break;
            case SteamOvenOneModel.EXP://EXP
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_906ovenwork_exp);
                mTvWorkingCircleabove.setText(cx.getString(R.string.device_steamOvenOne_name_exp));
                break;
            case SteamOvenOneModel.GUOSHUHONGGAN://果蔬烘干
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_906ovenwork_guoshuhong_white);
                mTvWorkingCircleabove.setText(cx.getString(R.string.device_steamOvenOne_name_guoshuhonggan));
                break;
            case SteamOvenOneModel.BAOWEN://保温
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_906ovenwork_baowen_white);
                mTvWorkingCircleabove.setText(cx.getString(R.string.device_steamOvenOne_name_baowen));
                break;
            //蒸箱部分
            case SteamOvenOneModel.FAXIAO://发酵
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_906steam_work_fajiao_white);
                mTvWorkingCircleabove.setText(cx.getString(R.string.device_steamOvenOne_name_faXiao));
                break;
            case SteamOvenOneModel.GAOWENZHENG://高温蒸
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_906steam_work_gaowen_white);
                mTvWorkingCircleabove.setText(cx.getString(R.string.device_steamOvenOne_name_gaoWenZhen));
                break;
            case SteamOvenOneModel.XIANNENZHENG://鲜嫩蒸
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_906steam_work_xiannenzheng_01);
                mTvWorkingCircleabove.setText(cx.getString(R.string.device_steamOvenOne_name_xianNenZheng));
                break;
            case SteamOvenOneModel.YINGYANGZHENG://营养蒸
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_906steam_work_xiannenzheng);
                mTvWorkingCircleabove.setText(cx.getString(R.string.device_steamOvenOne_name_yingYangZheng));
                break;
            //辅助部分
            case SteamOvenOneModel.JIEDONG://解冻
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_906steam_work_jiedong_white);
                mTvWorkingCircleabove.setText(R.string.device_steamOvenOne_name_jiedong);
                break;
            case SteamOvenOneModel.ZHENGQISHAJUN://杀菌
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_906steam_work_shajun_white);
                mTvWorkingCircleabove.setText(R.string.device_steamOvenOne_name_shajun);
                break;
        }
    }




   /* public boolean isRunningForeground() {
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
            try {
                Thread.sleep(1000);
                UIService.getInstance().popBack();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            UIService.getInstance().popBack();
            UIService.getInstance().popBack();
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        if (mTempAndTimeDialog != null && mTempAndTimeDialog.isShow()){
            mTempAndTimeDialog.dismiss();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
        }
        return true;
    }

    //暂停时调节时间温度
    private void Pause_TempAndTimeSet() {

        mTempAndTimeDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_04);

        switch (steameOvenC906.workModel) {
            case SteamOvenOneModel.KUAISUYURE:
                initModeData(getListTemp(SteamOvenOneModel.KUAISUYURE), 130, getListTime(SteamOvenOneModel.KUAISUYURE), 0);
                break;
            case SteamOvenOneModel.KUAIRE:
                initModeData(getListTemp(SteamOvenOneModel.KUAIRE), 150, getListTime(SteamOvenOneModel.KUAIRE), 29);
                break;
            case SteamOvenOneModel.FENGPEIKAO:
                initModeData(getListTemp(SteamOvenOneModel.FENGPEIKAO), 150, getListTime(SteamOvenOneModel.FENGPEIKAO), 29);
                break;
            case SteamOvenOneModel.PEIKAO:
                initModeData(getListTemp(SteamOvenOneModel.PEIKAO), 110, getListTime(SteamOvenOneModel.PEIKAO), 29);
                break;
            case SteamOvenOneModel.FENGSHAIKAO:
                initModeData(getListTemp(SteamOvenOneModel.FENGSHAIKAO), 170, getListTime(SteamOvenOneModel.FENGSHAIKAO), 29);
                break;
            case SteamOvenOneModel.SHAOKAO:
                initModeData(getListTemp(SteamOvenOneModel.SHAOKAO), 130, getListTime(SteamOvenOneModel.SHAOKAO), 29);
                break;
            case SteamOvenOneModel.QIANGSHAOKAO:
                initModeData(getListTemp(SteamOvenOneModel.QIANGSHAOKAO), 130, getListTime(SteamOvenOneModel.QIANGSHAOKAO), 29);
                break;
            case SteamOvenOneModel.JIANKAO:
                initModeData(getListTemp(SteamOvenOneModel.JIANKAO), 100, getListTime(SteamOvenOneModel.JIANKAO), 29);
                break;
            case SteamOvenOneModel.DIJIARE:
                initModeData(getListTemp(SteamOvenOneModel.DIJIARE), 110, getListTime(SteamOvenOneModel.DIJIARE), 29);
                break;
            case SteamOvenOneModel.EXP:
                initModeData(getListTemp(SteamOvenOneModel.EXP), 110, getListTime(SteamOvenOneModel.EXP), 19);
                break;
            case SteamOvenOneModel.GUOSHUHONGGAN:
                initModeData(getListTemp(SteamOvenOneModel.GUOSHUHONGGAN), 10, getListTime(SteamOvenOneModel.GUOSHUHONGGAN), 29);
                break;
            case SteamOvenOneModel.BAOWEN:
                initModeData(getListTemp(SteamOvenOneModel.BAOWEN), 20, getListTime(SteamOvenOneModel.BAOWEN), 0);
                break;
            //蒸箱部分
            case SteamOvenOneModel.FAXIAO:
                initModeData(getListTemp(SteamOvenOneModel.FAXIAO), 3, getListTime(SteamOvenOneModel.FAXIAO), 59);
                break;
            case SteamOvenOneModel.YINGYANGZHENG:
                initModeData(getListTemp(SteamOvenOneModel.YINGYANGZHENG), 4, getListTime(SteamOvenOneModel.YINGYANGZHENG), 19);
                break;
            case SteamOvenOneModel.XIANNENZHENG:
                initModeData(getListTemp(SteamOvenOneModel.XIANNENZHENG), 0, getListTime(SteamOvenOneModel.XIANNENZHENG), 19);
                break;
            case SteamOvenOneModel.GAOWENZHENG:
                initModeData(getListTemp(SteamOvenOneModel.GAOWENZHENG), 14, getListTime(SteamOvenOneModel.GAOWENZHENG), 19);
                break;

        }
    }

    private void initModeData(List<String> listTemp, int tempIndex, List<String> listTime, int timeIndex) {
        if (listTemp == null || listTime == null) return;
        mTempAndTimeDialog.setWheelViewData(listTemp, null, listTime, false, tempIndex, 0, timeIndex, new OnItemSelectedListenerFrone() {
            @Override
            public void onItemSelectedFront(String contentFront) {
                Message msg = mHandler.obtainMessage();
                msg.obj = contentFront;
                msg.what = 1;
                mHandler.sendMessage(msg);

            }
        }, null, new OnItemSelectedListenerRear() {
            @Override
            public void onItemSelectedRear(String contentRear) {
                Message msg = mHandler.obtainMessage();
                msg.obj = contentRear;
                msg.what = 2;
                mHandler.sendMessage(msg);
            }

        });

        mTempAndTimeDialog.setCanceledOnTouchOutside(true);
        mTempAndTimeDialog.show();

    }


    //获取时间
    private List<String> getListTime(short model) {
        List<String> list = Lists.newArrayList();
        if (model == SteamOvenOneModel.KUAISUYURE) {
            String i = "--";
            list.add(i);
        } else if (model == SteamOvenOneModel.KUAIRE || model == SteamOvenOneModel.FENGPEIKAO || model == SteamOvenOneModel.PEIKAO
                || model == SteamOvenOneModel.FENGSHAIKAO || model == SteamOvenOneModel.SHAOKAO || model == SteamOvenOneModel.QIANGSHAOKAO
                || model == SteamOvenOneModel.DIJIARE || model == SteamOvenOneModel.JIANKAO || model == SteamOvenOneModel.GUOSHUHONGGAN
                ) {
            for (int i = 1; i <= 120; i++) {
                list.add(i + StringConstantsUtil.STRING_MINUTES);
            }
        } else if (model == SteamOvenOneModel.YINGYANGZHENG || model == SteamOvenOneModel.XIANNENZHENG
                || model == SteamOvenOneModel.GAOWENZHENG || model == SteamOvenOneModel.FAXIAO) {
            for (int i = 1; i <= 120; i++) {
                list.add(i + StringConstantsUtil.STRING_MINUTES);
            }
        } else if (model == SteamOvenOneModel.JIEDONG) {
            for (int i = 1; i <= 120; i++) {
                list.add(i + StringConstantsUtil.STRING_MINUTES);
            }
        } else if (model == SteamOvenOneModel.ZHENGQISHAJUN) {
            int i = 20;
            list.add(i + StringConstantsUtil.STRING_MINUTES);
        } else if (model == SteamOvenOneModel.BAOWEN) {
            String i = "--";
            list.add(i);
        }
        return list;

    }

    //获取温度
    private List<String> getListTemp(short model) {

        List<String> list = Lists.newArrayList();

        if (model == SteamOvenOneModel.BAOWEN) {
            for (int i = 40; i <= 90; i++) {
                list.add(i + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        } else if (model == SteamOvenOneModel.KUAISUYURE || model == SteamOvenOneModel.JIANKAO || model == SteamOvenOneModel.KUAIRE || model == SteamOvenOneModel.FENGPEIKAO || model == SteamOvenOneModel.PEIKAO
                || model == SteamOvenOneModel.FENGSHAIKAO || model == SteamOvenOneModel.SHAOKAO || model == SteamOvenOneModel.QIANGSHAOKAO) {
            for (int i = 50; i <= 230; i++) {
                list.add(i + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        } else if (model == SteamOvenOneModel.DIJIARE) {
            for (int i = 50; i <= 180; i++) {
                list.add(i + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        } else if (model == SteamOvenOneModel.EXP) {
            for (int i = 50; i <= 200; i++) {
                list.add(i + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        } else if (model == SteamOvenOneModel.GUOSHUHONGGAN) {
            for (int i = 50; i <= 80; i++) {
                list.add(i + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        } else if (model == SteamOvenOneModel.YINGYANGZHENG) {
            for (int i = 96; i <= 105; i++) {
                list.add(i + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        } else if (model == SteamOvenOneModel.XIANNENZHENG) {
            for (int i = 90; i <= 95; i++) {
                list.add(i + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        } else if (model == SteamOvenOneModel.GAOWENZHENG) {
            for (int i = 106; i <= 150; i++) {
                list.add(i + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        } else if (model == SteamOvenOneModel.FAXIAO) {
            for (int i = 35; i <= 60; i++) {
                list.add(i + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        } else if (model == SteamOvenOneModel.JIEDONG) {
            for (int i = 40; i <= 80; i++) {
                list.add(i + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        } else if (model == SteamOvenOneModel.ZHENGQISHAJUN) {
            int i = 100;
            list.add(i + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
        }
        return list;


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
                public void onSuccess() {
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        }
    }


    @OnClick(R.id.fl_working_middle)
    public void onViewClicked() {

        if (steameOvenC906.powerOnStatus == SteamOvenOnePowerOnStatus.Order){
            if (mRokiDialog != null){
                mRokiDialog.setContentText(R.string.device_steamOvenOne_name_model_not_content);
                mRokiDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
                mRokiDialog.show();
            }
        }

        if (steameOvenC906.WaterStatus == 1) {

            if (steameOvenC906.workModel == SteamOvenOneModel.KUAIRE || steameOvenC906.workModel == SteamOvenOneModel.KUAISUYURE
                    || steameOvenC906.workModel == SteamOvenOneModel.FENGSHAIKAO || steameOvenC906.workModel == SteamOvenOneModel.FENGPEIKAO
                    || steameOvenC906.workModel == SteamOvenOneModel.PEIKAO || steameOvenC906.workModel == SteamOvenOneModel.SHAOKAO
                    || steameOvenC906.workModel == SteamOvenOneModel.QIANGSHAOKAO || steameOvenC906.workModel == SteamOvenOneModel.GUOSHUHONGGAN
                    || steameOvenC906.workModel == SteamOvenOneModel.BAOWEN || steameOvenC906.workModel == SteamOvenOneModel.DIJIARE
                    || steameOvenC906.workModel == SteamOvenOneModel.GUOSHUHONGGAN || steameOvenC906.workModel == SteamOvenOneModel.JIANKAO){

//                if (mRokiDialog != null){
//                    mRokiDialog.setContentText(R.string.device_alarm_water_out);
//                    mRokiDialog.setToastShowTime(DialogUtil.LENGTH_LONG);
//                    mRokiDialog.show();
//                }
                if (steameOvenC906.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
                    steameOvenC906.setSteamOvenOneStatusControl(SteamOvenOnePowerOnStatus.Pause, null);
                } else if (steameOvenC906.powerOnStatus == SteamOvenOnePowerOnStatus.Pause) {
                    steameOvenC906.setSteamOvenOneStatusControl(SteamOvenOnePowerOnStatus.WorkingStatus, null);
                }
            }else {
                if (mRokiDialog != null){
                    mRokiDialog.setContentText(R.string.device_alarm_water_out);
                    mRokiDialog.setToastShowTime(DialogUtil.LENGTH_LONG);
                    mRokiDialog.show();
                    return;
                }

                if (steameOvenC906.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
                    steameOvenC906.setSteamOvenOneStatusControl(SteamOvenOnePowerOnStatus.Pause, null);
                } else if (steameOvenC906.powerOnStatus == SteamOvenOnePowerOnStatus.Pause) {
                    steameOvenC906.setSteamOvenOneStatusControl(SteamOvenOnePowerOnStatus.WorkingStatus, null);
                }
            }


        }else {

            if (steameOvenC906.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
                steameOvenC906.setSteamOvenOneStatusControl(SteamOvenOnePowerOnStatus.Pause, null);
            } else if (steameOvenC906.powerOnStatus == SteamOvenOnePowerOnStatus.Pause) {
                steameOvenC906.setSteamOvenOneStatusControl(SteamOvenOnePowerOnStatus.WorkingStatus, null);
            }
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
        light();
    }

    private void light() {
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
                if (mAlarmSing == 4 || mAlarmSing == 5 || mAlarmSing == 6){
                    steameOvenC906.setSteameOvenStatus(SteamOvenOnePowerStatus.Off, SteamOvenOnePowerOnStatus.OperatingState, null);
                }else {
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


}
