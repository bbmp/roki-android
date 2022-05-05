package com.robam.roki.ui.page;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback2;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.robam.common.events.MicroWaveAlarmEvent;
import com.robam.common.events.MicroWaveStatusChangedEvent;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.common.pojos.device.microwave.MicroWaveM526;
import com.robam.common.pojos.device.microwave.MicroWaveModel;
import com.robam.common.pojos.device.microwave.MicroWaveStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.BlackPromptDialog;
import com.robam.roki.ui.dialog.BlackPromptDialog526;
import com.robam.roki.ui.dialog.Micro526PauseSettingDialog;
import com.robam.roki.ui.dialog.MicrowaveProfessionalDialog;
import com.robam.roki.utils.DialogUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/1.
 */

public class DeviceMicrowave526NormalWorkingPage extends BasePage {
    @InjectView(R.id.mic_nw_txtTem)
    TextView mic_nw_txtTem;
    @InjectView(R.id.topleft_img)
    ImageView topleft_img;
    @InjectView(R.id.topleft_unit)
    TextView topleft_unit;
    @InjectView(R.id.mic_nw_txtCurrentWeight)//当前份量
            TextView mic_nw_txtCurrentWeight;
    @InjectView(R.id.mic_nw_txtCurrentTime)//当前时间
            TextView mic_nw_txtCurrentTime;
    @InjectView(R.id.mic_nw_rotate_img)//中间旋转圆圈
            ImageView mic_nw_rotate_img;
    @InjectView(R.id.mic_nw_middle_ll)//中间部分linearlayout
            LinearLayout mic_nw_middle_ll;
    @InjectView(R.id.mic_nw_middle_ll_worktype1)//工作文字1
            TextView mic_nw_middle_ll_worktype1;
    @InjectView(R.id.mic_nw_middle_ll_img)//中间图片
            ImageView mic_nw_middle_ll_img;
    @InjectView(R.id.mic_nw_middle_ll_worktype2)//工作文字2
            TextView mic_nw_middle_ll_worktype2;
    @InjectView(R.id.mic_nw_imgTimeReset)
            ImageView mic_nw_imgTimeReset;
    private Animation circleRotate;
    private MicroWaveM526 microWave;
   // private MicroWaveM509 microWave;
    private short time;//倒计时
    private final int Time_Refresh = 1;
    private Timer timer;//定时器
    private boolean lock = true;//锁定
    private boolean isFirst = true;//页面是否首次初始化
    private View contentView;
    private String selectFlag="true";
    private IRokiDialog rokigatingdialog;
    private IRokiDialog rokiCountDowndialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        selectFlag=bd.getString(PageArgumentKey.selectflag);
        LogUtils.i("20170527","selectFlag:"+selectFlag);
        microWave = Plat.deviceService.lookupChild(guid);
        contentView = inflater.inflate(R.layout.page_device_microwave526_normal_working,
                container, false);
        ButterKnife.inject(this, contentView);
        initView();
        rokigatingdialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
        return contentView;
    }

    private void initView() {
        Bundle bd = getArguments();
        String type = bd.getString(PageArgumentKey.MicroWavePageArg);
        if ("1".equals(type))//火力单位
            setLeftTop();
        //InWorkingMode();
        if (MicrowaveProfessionalDialog.dlg != null && MicrowaveProfessionalDialog.dlg.isShowing()) {
            MicrowaveProfessionalDialog.dlg.dismiss();
            MicrowaveProfessionalDialog.dlg = null;
        }
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == Time_Refresh) {
                if (time == 0) {
                }
                short min = (short) (time / 60);
                short sec = (short) (time % 60);
                try {
                    if (sec < 10){
                        mic_nw_txtCurrentTime.setText(min + ":0" + sec);
                        mic_nw_txtCurrentTime.invalidate();
                    } else{
                        mic_nw_txtCurrentTime.setText(min + ":" + sec);
                        mic_nw_txtCurrentTime.invalidate();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (time == 0) {
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        return;
                    }
                    time--;
                }
            }
        }
    };
    Dialog dialogPause;
    @OnClick(R.id.mic_nw_middle_ll)
    public void OnClickPauseOrRun() {
        if (lock) return;
        rokiCountDowndialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_07);
        // short cmd = 0;
        if (microWave.state == MicroWaveStatus.Run) {
            // cmd = 3;
            microWave.setMicroWaveState((short) 3, new VoidCallback() {
                @Override
                public void onSuccess() {
                    InPauseMode();
                }
                @Override
                public void onFailure(Throwable t) {
                    Log.i("cmd", "onFailure");
                }
            });

        } else if (microWave.state == MicroWaveStatus.Pause) {

            if (rokiCountDowndialog.isShow() || rokiCountDowndialog == null) return;
                rokiCountDowndialog.show();

                rokiCountDowndialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    microWave.setMicroWaveState((short) 2, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            //InWorkingMode();
                        }
                        @Override
                        public void onFailure(Throwable t) {
                            Log.i("cmd", "onFailure");
                        }
                    });
                }
            });

        } else {
            return;
        }
      /*  final short temp = cmd;
        microWave.setMicroWaveState(cmd, new VoidCallback() {
            @Override
            public void onSuccess() {
                if (temp == 3)
                    InPauseMode();
                else
                    InWorkingMode();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("cmd", "onFailure");
            }
        });*/
    }

    /**
     * 结束操作
     */
    @OnClick(R.id.mic_nw_linSwitch)
    public void OnClickEnd() {
        if (microWave.doorState == 1)return;
        if (lock) return;
        final IRokiDialog closedialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        closedialog.setTitleText(R.string.close_work);
        closedialog.setContentText(R.string.is_close_work);
        closedialog.show();
        closedialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closedialog.dismiss();
                short endStatus = 4;
                if (microWave.state == MicroWaveStatus.Run) {
                    endStatus = 4;
                } else if (microWave.state == MicroWaveStatus.Pause) {
                    endStatus = 1;
                }
                microWave.setMicroWaveState( endStatus, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        if ("true".equals(selectFlag)){
                        }else{
                            Bundle bd=new Bundle();
                            bd.putString(PageArgumentKey.Guid,microWave.getID());
                            UIService.getInstance().postPage(PageKey.DeviceMicrowave526Main,bd);
                        }
                    }
                    @Override
                    public void onFailure(Throwable t) {}
                });

            }
        });
        closedialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closedialog.isShow()){
                    closedialog.dismiss();
                }
            }
        });

    }

    /**
     * 返回
     */
    @OnClick(R.id.imgreturn)
    public void OnReturnClick() {
        if ("true".equals(selectFlag)){
            if (UIService.getInstance().getPage(PageKey.DeviceMicrowave526Main) != null) {
                UIService.getInstance().popBack();
                UIService.getInstance().popBack();
            }
        }else{
            UIService.getInstance().popBack();
        }
    }

    @Subscribe
    public void OnEvent(MicroWaveStatusChangedEvent event) {
        if (!UIService.getInstance().getTop().getCurrentPageKey().equals(PageKey.DeviceMicrowave526NormalWorking)) {
            return;
        }
        LogUtils.i("20180627","event::"+event.pojo.state);
        if (microWave.mode == MicroWaveModel.Barbecue || microWave.mode == MicroWaveModel.MicroWave
                || microWave.mode == MicroWaveModel.ComibineHeating||microWave.mode == MicroWaveModel.CleanAir)
            setLeftTop();
        if (microWave.mode == MicroWaveModel.Milk || microWave.mode == MicroWaveModel.Bread){
            setRightTop();
        }
        switch (microWave.state) {
            case MicroWaveStatus.Run:
                if (dialogPause!=null&&dialogPause.isShowing()){
                    dialogPause.dismiss();
                }
                InWorkingMode();
                break;
            case MicroWaveStatus.Pause:
                InPauseMode();
                break;
            case MicroWaveStatus.Alarm:
                break;
            case MicroWaveStatus.Wait:
                if (dialogPause!=null&&dialogPause.isShowing()){
                    dialogPause.dismiss();
                }

                if (time == 0) {
                    InEndMode();//完成模式
                    time = -100;
                    lock = true;
                    backToParent();
                } else if (time == -100) {//完成返回
                    backToParent();
                } else {//中途返回
                    backToParent();
                }
                break;
            case MicroWaveStatus.Setting://中途返回
                backToParent();
                break;
            default:
                break;
        }
        lock = false;
        if (microWave.doorState == 0) {
            if (rokigatingdialog != null && rokigatingdialog.isShow()){
                rokigatingdialog.dismiss();
                rokigatingdialog=null;
            }
        } else if (microWave.doorState == 1) {
            if (rokigatingdialog != null && rokigatingdialog.isShow()) {
                return;
            } else {
                rokigatingdialog.setContentText(R.string.device_alarm_gating_content);
                rokigatingdialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
                rokigatingdialog.show();

            }
        }
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (microWave == null || !Objects.equal(microWave.getID(), event.device.getID()))
            return;
        backToParent();
        //getFragmentManager().popBackStack();
    }

    /**
     * 报警
     */
    @Subscribe
    public void onEvent(MicroWaveAlarmEvent event) {
        if (event.alarm == 0) return;
        IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_09);
        if (dialogByType.isShow())return;
        dialogByType.setContentText(R.string.device_alarm_gating_content);
        dialogByType.setToastShowTime(DialogUtil.LENGTH_CENTER);
        dialogByType.show();
    }

    /**
     * 工作模式
     */
    private void InWorkingMode() {
        mic_nw_txtCurrentWeight.setVisibility(View.VISIBLE);
        if (microWave.mode == MicroWaveModel.Barbecue || microWave.mode == MicroWaveModel.MicroWave
                || microWave.mode == MicroWaveModel.ComibineHeating||microWave.mode == MicroWaveModel.CleanAir) {
            mic_nw_txtCurrentWeight.setText(ChangeFire(microWave.power) + "");
        } else
            mic_nw_txtCurrentWeight.setText(microWave.weight + "");
            mic_nw_txtCurrentTime.setVisibility(View.VISIBLE);
        if (timer == null || (microWave.time-time>3)||(microWave.time-time<0)){
            if (timer != null) {
                timer.cancel();
                timer = null;//时间暂停
            }

            /*if (PageKey.MICRO_CD_FLAG){
                CountDownDialog.start((Activity) getContext());
                PageKey.MICRO_CD_FLAG=false;
                delay3s();
            }*/
            setTimeBySeconds();

        }

        mic_nw_imgTimeReset.setVisibility(View.GONE);
        mic_nw_middle_ll_worktype2.setVisibility(View.GONE);
        mic_nw_middle_ll_worktype1.setVisibility(View.VISIBLE);
        mic_nw_middle_ll_worktype1.setText(getModelText().string);
        mic_nw_middle_ll_img.setVisibility(View.VISIBLE);
        mic_nw_middle_ll_img.setImageResource(getModelText().res);
        mic_nw_rotate_img.setImageResource(R.mipmap.img_order_run);
        setAnimation();
    }

    //延时3秒倒计时
    private void delay3s(){
        TimerTask task = new TimerTask(){
            public void run(){
                setTimeBySeconds();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 3000);
    }

    Micro526PauseSettingDialog microPauseSeting;
    /**
     * 暂停模式
     */
    private void InPauseMode() {

        LogUtils.i("uuuu","quwei:"+microWave.mode);
        if (timer != null) {
            timer.cancel();
            timer = null;//时间暂停
        }
        if (circleRotate != null) {
            circleRotate.cancel();
            circleRotate = null;
        }

        mic_nw_rotate_img.clearAnimation();
        mic_nw_rotate_img.setImageResource(R.mipmap.img_oven_pause);
        mic_nw_rotate_img.bringToFront();
       /* if (microWave.mode == MicroWaveModel.Barbecue || microWave.mode == MicroWaveModel.MicroWave
                || microWave.mode == MicroWaveModel.ComibineHeating){

            if (microWave.mode==MicroWaveModel.MicroWave&&("去味".equals(microClean)||"热饭热菜".equals(microQuickHeat))){
                mic_nw_imgTimeReset.setVisibility(View.GONE);
            }else{
                mic_nw_imgTimeReset.setVisibility(View.VISIBLE);
                mic_nw_imgTimeReset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.show("点我设置时间", Toast.LENGTH_SHORT);
                        Helper.newMicro526PauseSettingDialog(cx, new Callback2<MicroWaveWheelMsg>() {
                            @Override
                            public void onCompleted(MicroWaveWheelMsg microWaveWheelMsg) {
                                Log.i("20170417", "microWaveWheelMsg:" + microWaveWheelMsg.getTime());
                                LogUtils.i("20170417", "aa:" + microWave.mode + "  bb:" + microWave.power);
                                ToastUtils.show("拿到了时间",Toast.LENGTH_SHORT);
                                microWave.setMicroWaveProModeHeat(microWave.mode, microWaveWheelMsg.getTime(), microWave.power, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                        LogUtils.i("20170504","sss");
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        LogUtils.i("20170504","vvv");
                                    }
                                });
                            }
                        },microWave.mode);
                    }
                });
            }

        }*/

        if (lock) {//首次进入页面轮训结果若是暂停状态，设置时间和重量或火力
            mic_nw_txtCurrentWeight.setVisibility(View.VISIBLE);
            if (microWave.mode == MicroWaveModel.Barbecue || microWave.mode == MicroWaveModel.MicroWave
                    || microWave.mode == MicroWaveModel.ComibineHeating || microWave.mode == MicroWaveModel.CleanAir){
                mic_nw_txtCurrentWeight.setText(ChangeFire(microWave.power) + "");
            }
            else
                mic_nw_txtCurrentWeight.setText(microWave.weight + "");
            mic_nw_txtCurrentTime.setVisibility(View.VISIBLE);
            short min = (short) (microWave.time / 60);
            short sec = (short) (microWave.time % 60);
            if (sec < 10){
                mic_nw_txtCurrentTime.setText(min + ":0" + sec);
                mic_nw_txtCurrentTime.invalidate();
            } else{
                mic_nw_txtCurrentTime.setText(min + ":" + sec);
                mic_nw_txtCurrentTime.invalidate();
            }

        }
    }

    /**
     * 完成模式
     */
    private void InEndMode() {
        if (timer != null) {
            timer.cancel();
            timer = null;//时间暂停
        }
        if (circleRotate != null) {
            circleRotate.cancel();
            circleRotate = null;
        }
       // startBroadcastReceiver(microWave.time);
        mic_nw_imgTimeReset.setVisibility(View.GONE);
        mic_nw_rotate_img.clearAnimation();
        mic_nw_rotate_img.setImageResource(R.mipmap.img_steamoven_finish);
        mic_nw_middle_ll_worktype1.setVisibility(View.GONE);
        mic_nw_middle_ll_worktype2.setVisibility(View.VISIBLE);
        mic_nw_middle_ll_img.setVisibility(View.GONE);
        PageKey.MICRO_CD_FLAG=true;
    }

    /**
     * 更改为火力单位
     */
    private void setLeftTop() {
        topleft_unit.setText("档");
        mic_nw_txtTem.setText("火力");
        topleft_img.setImageResource(R.mipmap.ic_device_microwave_linksetting_fire_write);
    }

    /**
     * 面包，煲汤，份数
     */
    private void setRightTop(){
        topleft_unit.setText("份");
        mic_nw_txtTem.setText("份量");
        topleft_img.setImageResource(R.mipmap.ic_device_microwave_normal_working_weighit_white);
    }



    /**
     * 设置倒计时
     */
    private void setTimeBySeconds() {
        time = microWave.time;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(Time_Refresh);
            }
        }, 0, 1000);
    }

    /**
     * 设置动画
     */
    private void setAnimation() {
        if (circleRotate == null) {
            circleRotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_oven_circle_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotate.setInterpolator(lin);
            mic_nw_rotate_img.startAnimation(circleRotate);
        }
    }

    /**
     * 获取具体模式名称
     */
    private ModelInfo getModelText() {
        ModelInfo info = new ModelInfo();
        int res = 0;
        switch (microWave.mode) {
            case MicroWaveModel.Meat:
                info.string = "黑椒牛排";
                info.res = R.mipmap.ic_device_microwave526_normal_working_beefmeet_white;
                break;
            case MicroWaveModel.Checken:
                info.string = "香烤全鸡";
                info.res = R.mipmap.ic_device_microwave526_normal_working_chicken_white;
                break;
            case MicroWaveModel.Kebab:
                info.string = "风味肉串";
                info.res = R.mipmap.ic_device_microwave526_normal_working_meat;
                break;
            case MicroWaveModel.Rice:
                info.string = "煮饭";
                info.res = R.mipmap.ic_device_microwave526_normal_working_porridge;
                break;
            case MicroWaveModel.Porridge:
                info.string = "煮粥";
                info.res = R.mipmap.ic_device_microwave526_normal_working_cook;
                break;
            case MicroWaveModel.Milk:
                info.string = "煲汤";
                info.res = R.mipmap.ic_device_microwave526_normal_working_baotang;
                break;
            case MicroWaveModel.Bread:
                info.string = "早餐面包";
                info.res = R.mipmap.ic_device_microwave526_normal_working_bread;
                break;
            case MicroWaveModel.Vegetables:
                info.string = "炒时蔬";
                info.res = R.mipmap.ic_device_microwave526_normal_working_vegetables;
                break;
            case MicroWaveModel.Fish:
                info.string = "蒸鱼";
                info.res = R.mipmap.ic_device_microwave526_normal_working_fish;
                break;
            case MicroWaveModel.Barbecue:
                info.string = "烧烤";
                info.res = R.mipmap.ic_device_microwave526_normal_working_barbecue;
                break;
            case MicroWaveModel.MicroWave:
                info.string = "微波";
                info.res = R.mipmap.ic_device_microwave526_normal_working_microwave;
                break;
            case MicroWaveModel.ComibineHeating:
                info.string = "组合加热";
                info.res = R.mipmap.ic_device_microwave526_normal_working_combinedheating;
                break;
            case MicroWaveModel.HeatingAgain:
                info.string = "再加热";
                info.res = R.mipmap.ic_device_microwave526_normal_working_heatingagain;
                break;
            case MicroWaveModel.Unfreeze:
                info.string = "解冻";
                info.res = R.mipmap.ic_device_microwave526_normal_working_unfreeze;
                break;
            case MicroWaveModel.CleanAir:
                info.string = "去味";
                info.res = R.mipmap.ic_device_microwave526_normal_working_clean;
                break;
            default:
                break;
        }
        return info;
    }

    /**
     * 更改 烧烤和组合火力值
     */
    private short ChangeFire(short value) {
        if (microWave.mode == MicroWaveModel.Barbecue) {
            switch (microWave.power) {
                case 7:
                    value = (short) 6;
                    break;
                case 8:
                    value = (short) 4;
                    break;
                case 9:
                    value = (short) 2;
                    break;
                default:
                    break;
            }
        } else if (microWave.mode == MicroWaveModel.ComibineHeating) {
            switch (microWave.power) {
                case 10:
                    value = (short) 6;
                    break;
                case 11:
                    value = (short) 4;
                    break;
                case 12:
                    value = (short) 2;
                    break;
                default:
                    break;
            }
        }
        return value;
    }

    private boolean shut = false;

    private void backToParent() {
        if (shut) return;
        if (UIService.getInstance().getPage(PageKey.DeviceMicrowave526Main) != null) {
            UIService.getInstance().popBack();
        } else {
            UIService.getInstance().returnHome();
            Bundle bundle = new Bundle();
            bundle.putString(PageArgumentKey.Guid, microWave.getID());
            UIService.getInstance().postPage(PageKey.DeviceMicrowave526Main, bundle);
        }
        shut = true;
    }

    /**
     * 用于模式选择，封装模式名称和模式图标
     */
    class ModelInfo {
        String string;
        int res;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ("true".equals(selectFlag)){
                UIService.getInstance().popBack();
                UIService.getInstance().popBack();
                LogUtils.i("20170527","1");
            }else{
                UIService.getInstance().popBack();
                LogUtils.i("20170527","2");
            }
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        ButterKnife.reset(this);
       // stopBroadcastReceiver();
    }

   /* private void startBroadcastReceiver(long time) {
        AlarmManager alarmManager = AlarmManagerUtil.getInstance(cx);
        Intent intent = new Intent(cx, Micro526alarmReceiver.class);
        intent.setAction("Micro526alarmReceiver");
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(cx, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }

    private void stopBroadcastReceiver() {
        AlarmManager alarmManager = AlarmManagerUtil.getInstance(cx);
        Intent intent = new Intent(cx, Micro526alarmReceiver.class);
        intent.setAction("Micro526alarmReceiver");
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(cx, 0, intent, 0);
        alarmManager.cancel(pendingIntent);
    }*/

}
