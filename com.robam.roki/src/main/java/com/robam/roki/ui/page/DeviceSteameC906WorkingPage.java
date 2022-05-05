package com.robam.roki.ui.page;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
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
import com.robam.roki.ui.PageKey;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.roki.utils.StringConstantsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/19.
 */

public class DeviceSteameC906WorkingPage extends BasePage {


    AbsSteameOvenOne steameOvenC906;
    String guid;
    short from;//来源 1 从主目录，0从菜谱控制
    Timer mTimer;
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
    @InjectView(R.id.tv_working_switch)
    TextView mTvWorkingSwitch;
    @InjectView(R.id.ll_working_switch)
    LinearLayout mLlWorkingSwitch;
    @InjectView(R.id.v_working_view2)
    View mVWorkingView2;
    @InjectView(R.id.tv_title)
    TextView mTvTitle;
    @InjectView(R.id.v_working_view1)
    View mVWorkingView1;
    private boolean training_lock = false;
    private Animation circleRotate;
    private IRokiDialog mTempAndTimeDialog;
    private final int Time_Refresh = 0;
    private short mTime;//倒计时
    private  List<String> mStringTimeList = new ArrayList<String>();
    private List<String> mStringTempList = new ArrayList<String>();

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case Time_Refresh:
                    TimeCountdown();
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
    //设置设备运行数据
    private void setDeviceRunData(String str) {
        if (str.contains(StringConstantsUtil.STRING_DEGREE_CENTIGRADE)) {
            String removetTempString = RemoveManOrsymbolUtil.getRemoveString(str);
            mStringTempList.add(removetTempString);
        }
        if (str.contains(StringConstantsUtil.STRING_MINUTES)) {
            String removeTimeString = RemoveManOrsymbolUtil.getRemoveString(str);
            mStringTimeList.add(removeTimeString);
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
                            n,new VoidCallback() {
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
        if (mTime == 0) return;
        short min = (short) (mTime / 60);
        short sec = (short) (mTime % 60);
        try {
            if (sec < 10)
                mTvWorkingRealTime.setText(min + ":0" + sec);
            else
                mTvWorkingRealTime.setText(min + ":" + sec);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        from = bd == null ? 0 : bd.getShort("from");
        steameOvenC906 = Plat.deviceService.lookupChild(guid);
        View view = inflater.inflate(R.layout.page_device_steam_c906_working, null, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Subscribe
    public void onEvent(SteamOvenOneStatusChangedEvent event) {
        LogUtils.i("20180627","event::"+event.pojo.worknStatus);
//        if (steameOvenC906 == null || !Objects.equal(steameOvenC906.getID(), event.pojo.getID()) || mTimer != null || training_lock)
//            return;


        if (steameOvenC906.worknStatus == SteamOvenOneWorkStatus.PreHeat) {
            setPreHeatModel();
        } else if (steameOvenC906.worknStatus == SteamOvenOneWorkStatus.Working) {
            setWorkMode();
        } else if (steameOvenC906.powerOnStatus == SteamOvenOnePowerOnStatus.Pause) {
            setPauseMolde();
        } else if (steameOvenC906.powerOnStatus == SteamOvenOnePowerOnStatus.Order) {
            setOrderMolde();
        } else if (steameOvenC906.powerStatus == SteamOvenOnePowerStatus.Off ||
                steameOvenC906.powerStatus == SteamOvenOnePowerStatus.On) {
            if (isRunningForeground())
                back();
        }
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (steameOvenC906 == null || !Objects.equal(steameOvenC906.getID(), event.device.getID()))
            return;
        if (!event.isConnected && isRunningForeground()) {
            back();
        }

    }

    @Subscribe
    public void onEvent(SteamOvenOneWorkFinishEvent event) {
//        if (event.finish != 0) return;
        training_lock = true;

        stopAnimation();
        mIvWorkingImgFinish.setVisibility(View.VISIBLE);
        if (!isRunningForeground()) {
            training_lock = false;
            return;
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                back();
            }
        }, 2500);
    }


    /**
     * 预热模式
     */
    private void setPreHeatModel() {
        LogUtils.i("20171013","----------setPreHeatModel----------");
        mIvWorkingImgPauseic1.setVisibility(View.GONE);
        mIvWorkingImgPauseic2.setVisibility(View.GONE);
        mIvWorkingImgCircle.setVisibility(View.VISIBLE);
        mIvWorkingImgPause.setVisibility(View.GONE);

        mTvWorkingSettemp.setText(steameOvenC906.setTemp + "");//设置温度
        mTvWorkingSettime.setText(steameOvenC906.setTime + "");//设置时间
        mTvWorkingRealTemp.setText(steameOvenC906.temp + "");//温度

        startAnimation();
        mIvWorkingImgCircledown.setVisibility(View.VISIBLE);
        mTvWorkingCircledown.setVisibility(View.GONE);
        mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_oven026work_preheating);
        mTvWorkingCircleabove.setText("预热中");
        setTimeBySeconds();
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
        stopAnimation();
        LogUtils.i("20171013","----------setPauseMolde----------");
        mIvWorkingImgPauseic1.setVisibility(View.VISIBLE);
        mIvWorkingImgPauseic2.setVisibility(View.VISIBLE);
        mIvWorkingImgCircle.setVisibility(View.GONE);
        mIvWorkingImgPause.setVisibility(View.VISIBLE);
        mTvWorkingSettemp.setText(steameOvenC906.setTemp + "");//设置温度
        mTvWorkingSettime.setText(steameOvenC906.setTime + "");//设置时间
        mTvWorkingRealTemp.setText(steameOvenC906.temp + "");//温度
//        mTvWorkingRealTime.setText(steameOvenC906.time + "");//时间
        mIvWorkingImgCircledown.setVisibility(View.VISIBLE);
        mTvWorkingCircledown.setVisibility(View.GONE);
        initModel();

    }

    /**
     * 工作模式
     */
    void setWorkMode() {
        light_change();
        LogUtils.i("20171013","----------setWorkMode----------");
        mIvWorkingImgPauseic1.setVisibility(View.GONE);
        mIvWorkingImgPauseic2.setVisibility(View.GONE);
        mIvWorkingImgCircle.setVisibility(View.VISIBLE);
        mIvWorkingImgPause.setVisibility(View.GONE);
        mTvWorkingSettemp.setText(steameOvenC906.setTemp + "");//设置温度
        mTvWorkingSettime.setText(steameOvenC906.setTime + "");//设置时间
        mTvWorkingRealTemp.setText(steameOvenC906.temp + "");//温度
//        mTvWorkingRealTime.setText(steameOvenC906.time + "");//时间
        startAnimation();
        mIvWorkingImgCircledown.setVisibility(View.VISIBLE);
        mTvWorkingCircledown.setVisibility(View.GONE);
        initModel();
        if (mTimer == null || (steameOvenC906.leftTime-mTime>3)||(steameOvenC906.leftTime-mTime<0)){
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;//时间暂停
            }
            setTimeBySeconds();
        }
    }

    //设置倒计时
    private void setTimeBySeconds() {
        mTime = steameOvenC906.leftTime;
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(Time_Refresh);
            }
        }, 0, 1000);

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
            case SteamOvenOneModel.FAXIAO://发酵
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_906steam_work_faxiao_white);
                mTvWorkingCircleabove.setText("发酵");
                break;
            case SteamOvenOneModel.GAOWENZHENG://高温蒸
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_906steam_work_gaowen_white);
                mTvWorkingCircleabove.setText("高温蒸");
                break;
            case SteamOvenOneModel.XIANNENZHENG://强力蒸
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_906steam_work_qiangli_white);
                mTvWorkingCircleabove.setText("强力蒸");
                break;
            case SteamOvenOneModel.YINGYANGZHENG://营养蒸
                mIvWorkingImgCircledown.setImageResource(R.mipmap.ic_906steam_work_qiangli_white);
                mTvWorkingCircleabove.setText("营养蒸");
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

    public boolean isRunningForeground() {
        ActivityManager am = (ActivityManager) cx.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        return currentPackageName != null && currentPackageName.equals(getActivity().getPackageName());
    }

    private void back() {
        if (from == 1) {
            UIService.getInstance().returnHome();
            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, steameOvenC906.getID());
            UIService.getInstance().postPage(PageKey.DeviceSteameOvenC906Home, bd);
        } else {
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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        back();
        return true;
    }


    //暂停时调节时间温度
    private void Pause_TempAndTimeSet() {

        mTempAndTimeDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_04);

        switch (steameOvenC906.workModel){
            case SteamOvenOneModel.FAXIAO:
                initModeData(getListTemp(SteamOvenOneModel.FAXIAO), 3,getListTime(SteamOvenOneModel.FAXIAO), 45);
             break;
            case SteamOvenOneModel.YINGYANGZHENG:
                initModeData(getListTemp(SteamOvenOneModel.YINGYANGZHENG), 4,getListTime(SteamOvenOneModel.YINGYANGZHENG), 15);
                break;
            case SteamOvenOneModel.XIANNENZHENG:
                initModeData(getListTemp(SteamOvenOneModel.XIANNENZHENG), 0,getListTime(SteamOvenOneModel.XIANNENZHENG), 15);
                break;
            case SteamOvenOneModel.GAOWENZHENG:
                initModeData(getListTemp(SteamOvenOneModel.GAOWENZHENG), 14,getListTime(SteamOvenOneModel.GAOWENZHENG), 15);
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
        },null, new OnItemSelectedListenerRear() {
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
        if (model == SteamOvenOneModel.YINGYANGZHENG || model == SteamOvenOneModel.XIANNENZHENG
                || model == SteamOvenOneModel.GAOWENZHENG || model == SteamOvenOneModel.FAXIAO) {
            for (int i = 5; i <= 120; i++) {
                list.add(i + StringConstantsUtil.STRING_MINUTES);
            }
        }
        return list;
    }
    //获取温度
    private List<String> getListTemp(short model) {

        List<String> list = Lists.newArrayList();
        if (model == SteamOvenOneModel.YINGYANGZHENG ) {
            for (int i = 96; i <= 105; i++) {
                list.add(i + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        } else if (model == SteamOvenOneModel.XIANNENZHENG) {
            for (int i = 90; i <= 95; i++) {
                list.add(i+ StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        }else if (model == SteamOvenOneModel.GAOWENZHENG){
            for (int i = 106; i <= 150; i++) {
                list.add(i+ StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        }else if (model == SteamOvenOneModel.FAXIAO){
            for (int i = 35; i <= 60; i++) {
                list.add(i+ StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        }
        return list;

    }


    private boolean checkConnection() {
        if (steameOvenC906.isConnected()) {
            return true;
        } else {
            ToastUtils.show("网络通讯异常", Toast.LENGTH_SHORT);
            return false;
        }
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        back();
    }

    @OnClick(R.id.fl_working_middle)
    public void onViewClicked() {
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
        final IRokiDialog closedialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        closedialog.setTitleText(R.string.close_work);
        closedialog.setContentText(R.string.is_close_work);
        closedialog.show();
        closedialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closedialog.dismiss();
                steameOvenC906.setSteameOvenStatus(SteamOvenOnePowerStatus.Off,SteamOvenOnePowerOnStatus.NoStatus,null);
                UIService.getInstance().returnHome();
            }
        });
        closedialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closedialog.isShow()) {
                    closedialog.dismiss();
                }
            }
        });
    }





}
