package com.robam.roki.ui.page;

import android.os.Bundle;

import androidx.annotation.Nullable;
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
import com.robam.roki.utils.DialogUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/19.
 */

public class DeviceOtherC906WorkingPage extends BasePage {


    AbsSteameOvenOne steameOvenC906;
    String guid;
    short from;//来源 1 从主目录，0从菜谱控制
//    Timer mTimer;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_title)
    TextView mTvTitle;
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
    @InjectView(R.id.btn_one)
    Button mBtnOne;
    @InjectView(R.id.btn_two)
    Button mBtnTwo;
    @InjectView(R.id.btn_there)
    Button mBtnThere;
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
    @InjectView(R.id.iv_water)
    ImageView mIvWater;
    @InjectView(R.id.tv_title_status)
    TextView mTvTitleStatus;
    private boolean training_lock = false;
    private Animation circleRotate;
    private final int Time_Refresh = 0;
    private short mTime;//倒计时
    private IRokiDialog mToastDialog;
    private int mAlarmSing;
    private IRokiDialog mClosedialog;


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
//        LogUtils.i("20171127", "SteamOvenOneWorkFinishEvent" + event.finish);
//
//        if (event.finish != 0) return;
        training_lock = true;

        stopAnimation();
        mIvWorkingImgFinish.setVisibility(View.VISIBLE);
        /*if (!isRunningForeground()) {
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
            steamOvenOneAlarmStatus(i,alarm, guid);
        }

    }

    private void steamOvenOneAlarmStatus(int i, short alarm, String guid) {
        if (alarm != 0){
            LogUtils.i("20171128"," i:"+ i +" alarm" + alarm);
            switch (i) {

                case 4:
                case 5:
                case 6:
                    mAlarmSing = i;
                    break;


            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        from = bd == null ? 0 : bd.getShort("from");
//        event = (SteamOvenOneStatusChangedEvent) (bd == null ? 0 : bd.getSerializable("event"));
        steameOvenC906 = Plat.deviceService.lookupChild(guid);
        View view = inflater.inflate(R.layout.page_device_other_c906_working, null, false);
        mToastDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
        ButterKnife.inject(this, view);
        return view;
    }


    /**
     * 预热模式
     */
    private void setPreHeatModel() {
        light_change();
        mTvTitleStatus.setText(R.string.c906_other_working);
        mIvWorkingImgCircle.setVisibility(View.VISIBLE);
        mIvWorkingImgPause.setVisibility(View.GONE);
        mIvWater.setVisibility(View.GONE);
        startAnimation();
        mIvWorkingImgCircledown.setVisibility(View.VISIBLE);
        mTvWorkingCircledown.setVisibility(View.GONE);
        mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_oven026work_preheating);
        mTvWorkingCircleabove.setText(R.string.device_preheating);
        initModel();
        numStep();

        if (steameOvenC906.leftTime <= 2) {
            InEndMode();//完成模式
        }

    }


    /**
     * 预约模式
     */
    void setOrderMolde() {
        light_change();
        startAnimation();
    }

    /**
     * 暂停模式
     */
    void setPauseMolde() {
        light_change();
        water_change();
        stopAnimation();
        mIvWater.setVisibility(View.VISIBLE);
        mTvTitleStatus.setText(R.string.device_stop);
        mIvWorkingImgCircle.setVisibility(View.GONE);
        mIvWorkingImgPause.setVisibility(View.VISIBLE);
        mIvWorkingImgCircledown.setVisibility(View.VISIBLE);
        mTvWorkingCircledown.setVisibility(View.GONE);
        numStep();

    }

    /**
     * 工作模式
     */
    void setWorkMode() {
        numStep();
        light_change();
        mIvWater.setVisibility(View.GONE);
        mTvTitleStatus.setText(R.string.c906_other_working);
        mIvWorkingImgCircle.setVisibility(View.VISIBLE);
        mIvWorkingImgPause.setVisibility(View.GONE);
        startAnimation();
        mIvWorkingImgCircledown.setVisibility(View.VISIBLE);
        mTvWorkingCircledown.setVisibility(View.GONE);
        initModel();


        if (steameOvenC906.multiSumStep == 0) {
            if (steameOvenC906.leftTime <= 2) {
                InEndMode();//完成模式
            }
        } else if (steameOvenC906.multiSumStep == steameOvenC906.SectionOfTheStep) {
            if (steameOvenC906.leftTime <= 2) {
                InEndMode();//完成模式
            }
        }
    }

    //多段烹饪步鄹
    private void numStep() {

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
        mIvWorkingImgCircle.setImageResource(R.mipmap.img_steamoven_finish);
        mTvWorkingCircleabove.setText(cx.getString(R.string.device_finish));
        mIvWorkingImgCircledown.setVisibility(View.GONE);
        try {
            Thread.sleep(2600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            UIService.getInstance().popBack();
        }
    }


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
            case SteamOvenOneModel.CHUGO://除垢
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_c906_work_chugou);
                mTvWorkingCircleabove.setText(cx.getString(R.string.device_steamOvenOne_name_chugou));
                break;
            case SteamOvenOneModel.GANZAO://干燥
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_c906_work_ganzao);
                mTvWorkingCircleabove.setText(cx.getString(R.string.device_steamOvenOne_name_ganzao));
                break;
            case SteamOvenOneModel.QINGJIE://清洁
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_c906_work_clean);
                mTvWorkingCircleabove.setText(cx.getString(R.string.device_steamOvenOne_name_clean));
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

    /**
     * 关闭动画和烟雾效果
     */
    private void stopAnimation() {
        if (circleRotate != null)
            circleRotate.cancel();
        circleRotate = null;
        mIvWorkingImgCircle.clearAnimation();
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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        back();
        return true;
    }

    void water_change() {
        if (steameOvenC906.WaterStatus == 1) {
            IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
            dialogByType.setContentText(R.string.device_water_good);
            dialogByType.setToastShowTime(DialogUtil.LENGTH_LONG);
            dialogByType.show();
            return;
        }
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

        if (steameOvenC906.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus ||
                steameOvenC906.powerOnStatus == SteamOvenOnePowerOnStatus.Pause) {
            if (mToastDialog != null) {
                mToastDialog.setContentText(R.string.device_c906_model_stop_not);
                mToastDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
                mToastDialog.show();
                return;
            }
            steameOvenC906.setSteamOvenOneStatusControl(SteamOvenOnePowerOnStatus.Pause, null);
        } else if (steameOvenC906.powerOnStatus == SteamOvenOnePowerOnStatus.Pause) {
            steameOvenC906.setSteamOvenOneStatusControl(SteamOvenOnePowerOnStatus.WorkingStatus, null);
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
                LogUtils.i("20171208","mAlarmSing:" + mAlarmSing);
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
