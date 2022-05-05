package com.robam.roki.ui.page.device.cook;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.cook.AbsCooker;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.dialog.AbsSettingDialog;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.TestDatas;
import com.robam.roki.utils.TimeUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/6/6.
 */

public class CookerWorkingView extends FrameLayout {

    @InjectView(R.id.co_run_down)
    ImageView coRunDown;
    @InjectView(R.id.co_run_up)
    ImageView coRunUp;
    @InjectView(R.id.ll_run_animation)
    RelativeLayout llRunAnimation;
    @InjectView(R.id.tv_work_state_name)
    TextView tvWorkStateName;
    @InjectView(R.id.tv_work_dec)
    TextView tvWorkDec;
    @InjectView(R.id.fl_run_and_stop)
    FrameLayout flRunAndStop;
    @InjectView(R.id.co_work_time)
    TextView coWorkTime;
    @InjectView(R.id.co_work_tempture)
    TextView coWorkTempture;
    @InjectView(R.id.co_tempture_just)
    RelativeLayout coTemptureJust;
    @InjectView(R.id.co_work_huoli)
    TextView coWorkHuoli;
    @InjectView(R.id.co_gear)
    RelativeLayout coGear;
    @InjectView(R.id.co_work_timer)
    TextView coWorkTimer;
    @InjectView(R.id.co_timer)
    RelativeLayout coTimer;
    @InjectView(R.id.co_finish)
    RelativeLayout coFinish;
    @InjectView(R.id.tempture_bg)
    ImageView temptureBg;
    @InjectView(R.id.fire_bg)
    ImageView fireBg;
    @InjectView(R.id.timer_bg)
    ImageView timerBg;

    Context cx;
    List<DeviceConfigurationFunctions> otherList;

    @InjectView(R.id.cofinish)
    ImageView cofinish;
    @InjectView(R.id.cofinishName)
    TextView cofinishName;


    private void onAction(int code) {
        switch (code) {
            case 0:
            case 1:
                upDateFireUi();
                break;
            case 2:
                upDateTemptureUi();
                break;
        }
    }

    private void action(int code) {
        if (code>4){
            int num = PreferenceUtils.getInt("code",1);
            switch (num){
                case 1:
                    upDateActionUi("煮");
                    break;
                case 2:
                    upDateActionUi("煎");
                    break;
                case 3:
                    upDateActionUi("炸");
                    break;
            }
        }else{
            PreferenceUtils.setInt("code",code);
            switch (code) {
                case 1:
                    upDateActionUi("煮");
                    break;
                case 2:
                    upDateActionUi("煎");
                    break;
                case 3:
                    upDateActionUi("炸");
                    break;
                case 4:
                    upDateActionUi("解冻中");
                    break;
            }
        }
    }


    Animation circleRotateDown;
    Animation circleRotateUp;

    /**
     * 开启动画
     */
    void startAnimation() {
        if (circleRotateDown == null) {
            circleRotateDown = AnimationUtils.loadAnimation(getContext(), R.anim.device_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotateDown.setInterpolator(lin);
            coRunDown.startAnimation(circleRotateDown);
        }

        if (circleRotateUp == null) {
            circleRotateUp = AnimationUtils.loadAnimation(getContext(), R.anim.device_model_circle_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotateUp.setInterpolator(lin);
            coRunUp.startAnimation(circleRotateUp);
        }
    }

    /**
     * 关闭动画和烟雾效果
     */
    public void stopAnimation() {
        if (circleRotateDown != null) {
            circleRotateDown.cancel();
            circleRotateDown = null;
            coRunDown.clearAnimation();
        }
        if (circleRotateUp != null) {
            circleRotateUp.cancel();
            circleRotateUp = null;
            coRunUp.clearAnimation();
        }
    }


    private void upDateActionUi(String modeName) {
        tvWorkStateName.setText(R.string.co_action_mode);
        tvWorkDec.setText(modeName);
        temptureBg.setImageResource(R.mipmap.ic_cooker_tempture_white);
        fireBg.setImageResource(R.mipmap.ic_cooker_huoli_white);
        if (absCooker.timerPower == 0) {
            timerBg.setImageResource(R.mipmap.ic_cooker_timer_white);
        } else {
            timerBg.setImageResource(R.mipmap.ic_cooker_timer_yellow);
        }
    }


    private void upDateTemptureUi() {
        tvWorkStateName.setText(R.string.co_all_tempture);
        tvWorkDec.setText(absCooker.currentTempture + "℃");
        Glide.with(cx).load(otherList.get(0).backgroundImgH).into(temptureBg);
        Glide.with(cx).load(otherList.get(1).backgroundImg).into(fireBg);
       /* temptureBg.setImageResource(R.mipmap.ic_cooker_tempture_yellow);
        fireBg.setImageResource(R.mipmap.ic_cooker_huoli_white);*/
        if (absCooker.timerPower == 0) {
            Glide.with(cx).load(otherList.get(2).backgroundImg).into(timerBg);
            //  timerBg.setImageResource(R.mipmap.ic_cooker_timer_white);
        } else {
            Glide.with(cx).load(otherList.get(2).backgroundImgH).into(timerBg);
            // timerBg.setImageResource(R.mipmap.ic_cooker_timer_yellow);
        }
    }

    private void upDateFireUi() {
        tvWorkStateName.setText(R.string.co_current_fire);
        tvWorkDec.setText(absCooker.currentFire + "档");
        Glide.with(cx).load(otherList.get(0).backgroundImg).into(temptureBg);
        Glide.with(cx).load(otherList.get(1).backgroundImgH).into(fireBg);
       /* temptureBg.setImageResource(R.mipmap.ic_cooker_tempture_white);
        fireBg.setImageResource(R.mipmap.ic_cooker_huoli_yellow);*/
        if (absCooker.timerPower == 0) {
            Glide.with(cx).load(otherList.get(2).backgroundImg).into(timerBg);
            //  timerBg.setImageResource(R.mipmap.ic_cooker_timer_white);
        } else {
            Glide.with(cx).load(otherList.get(2).backgroundImgH).into(timerBg);
            // timerBg.setImageResource(R.mipmap.ic_cooker_timer_yellow);
        }
    }

    public CookerWorkingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.cx = context;
        initView(context, attrs);
    }

    public CookerWorkingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    AbsCooker absCooker;

    public CookerWorkingView(Context context, AbsCooker absCooker, List<DeviceConfigurationFunctions> otherList) {
        super(context);
        this.cx = context;
        this.absCooker = absCooker;
        this.otherList = otherList;
        initView(context, null);
    }

    IRokiDialog irokiFinish;

    private void initView(Context cx, AttributeSet attrs) {
        View view = LayoutInflater.from(cx).inflate(R.layout.device_cooker_working_show, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
        irokiFinish = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        init();
    }

    private void init() {
        if (otherList == null) return;
        Glide.with(cx).load(otherList.get(0).backgroundImg).into(temptureBg);
        Glide.with(cx).load(otherList.get(1).backgroundImg).into(fireBg);
        Glide.with(cx).load(otherList.get(3).backgroundImg).into(cofinish);
        coWorkTempture.setText(otherList.get(0).functionName);
        coWorkHuoli.setText(otherList.get(1).functionName);
        coWorkTimer.setText(otherList.get(2).functionName);
        cofinishName.setText(otherList.get(3).functionName);
       /* temptureBg.setImageResource(R.mipmap.ic_cooker_tempture_white);
        fireBg.setImageResource(R.mipmap.ic_cooker_huoli_white);*/
        LogUtils.i("20180611", "cooker---do");
        if (absCooker != null) {
            if (absCooker.mode == 1) {
                upDateFireUi();
            } else {
                upDateTemptureUi();
            }
        }
        UpDateTimeShow(absCooker.heatTime);
        startAnimation();
    }

    private void UpDateTimeShow(short time) {
        String timeStr = TimeUtils.getTime(time);
        coWorkTime.setText(timeStr);
    }

    //更新UI
    public void upDateUi(int code, int mode,AbsCooker absCooker) {
        LogUtils.i("20180806","code:"+code+" mode:"+mode);
        this.absCooker = absCooker;
        UpDateTimeShow(absCooker.heatTime);
        switch (mode) {
            case 0:
                onAction(code);
                break;
            case 1:
                action(code);
                break;
            default:
                break;
        }
    }

    public void closeDialog(){
        if (absSettingDialog!=null&&absSettingDialog.isShowing()){
            absSettingDialog.dismiss();
        }
    }

    AbsSettingDialog absSettingDialog;

    int index = 0;
    @OnClick({R.id.co_tempture_just, R.id.co_gear, R.id.co_timer, R.id.co_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.co_tempture_just:
              //  ToastUtils.show("温度调整", Toast.LENGTH_SHORT);
                List<Integer> listTempture = TestDatas.createTempDatas(absCooker.currentTempture);
                for (int i = 0; i <listTempture.size() ; i++) {
                    if (absCooker.currentTempture == listTempture.get(i)){
                        index = i;
                    }
                }
                setDialog(0, listTempture, index, "℃","取消");
                break;
            case R.id.co_gear:
               // ToastUtils.show("火力调整", Toast.LENGTH_SHORT);
                List<Integer> listFire = TestDatas.createFire();
                setDialog(1, listFire, 4, "火力","取消");
                break;
            case R.id.co_timer:
              //  ToastUtils.show("定时关机", Toast.LENGTH_SHORT);
                List<Integer> listMin = TestDatas.createTimeDatas();
                String str = null;
                if (absCooker.timerPower==0){
                    str = "取消";
                }else{
                    str = "关闭定时";
                }
                setDialog(2, listMin, 19, "分钟",str);
                break;
            case R.id.co_finish:
              //  ToastUtils.show("结束", Toast.LENGTH_SHORT);
                irokiFinish.setTitleText(getContext().getString(R.string.co_finish));
                irokiFinish.setContentText("确定结束当前工作？");
                irokiFinish.setOkBtn("确定", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (irokiFinish != null && irokiFinish.isShow()) {
                            irokiFinish.dismiss();
                        }
                        sendFinishCommand((short) 1);
                    }
                });
                irokiFinish.setCancelBtn("取消", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (irokiFinish != null && irokiFinish.isShow()) {
                            irokiFinish.dismiss();
                        }
                    }
                });
                irokiFinish.show();
                break;
        }
    }

    //mode用于标记温度，火力，关机
    private void setDialog(final int mode, List<Integer> listTempture, int num, String str,String btn1) {
        List<String> listButton = TestDatas.createButtonText(str, btn1, "确定", null);
        absSettingDialog = new AbsSettingDialog<Integer>(cx, listTempture, listButton, num);
        absSettingDialog.show(absSettingDialog);
        absSettingDialog.setListener(new AbsSettingDialog.PickListener() {
            @Override
            public void onCancel() {
                if (absCooker.timerPower==0){

                }else {
                    if (mode==2){
                        setModeDeal(3,0);
                    }
                }
            }

            @Override
            public void onConfirm(Object index) {
                LogUtils.i("20180609", "index::" + (int) index);
                setModeDeal(mode, (int) index);
            }
        });
    }

    private void setModeDeal(int mode, int index) {
        switch (mode) {
            case 0:
                sendTempCommand(index);
                break;
            case 1:
                sendFireCommand(index);
                break;
            case 2:
                sendTimerCommand((short) 1,index);
                break;
            case 3:
                sendTimerCommand((short) 0,index);
                break;
            default:
                break;
        }
    }

    private void sendTempCommand(int index) {
        LogUtils.i("20180809", "index::" + index);
        absCooker.setCookerTempTure((short) index, new VoidCallback() {
            @Override
            public void onSuccess() {
                //ToastUtils.show("发送成功", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show("发送失败", Toast.LENGTH_SHORT);
            }
        });
    }

    private void sendFireCommand(int index) {
        absCooker.setCookerFire((short) index, new VoidCallback() {
            @Override
            public void onSuccess() {
                //ToastUtils.show("发送成功", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show("发送失败", Toast.LENGTH_SHORT);
            }
        });
    }

    private void sendTimerCommand(short timerStatus,int index) {
        absCooker.setTimerOff(timerStatus, index, new VoidCallback() {
            @Override
            public void onSuccess() {
                //ToastUtils.show("发送成功", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show("发送失败", Toast.LENGTH_SHORT);
            }
        });
    }

    private void sendFinishCommand(short status) {
        absCooker.setCookerWorkStatus(status, new VoidCallback() {
            @Override
            public void onSuccess() {
                //ToastUtils.show("发送成功", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show("发送失败", Toast.LENGTH_SHORT);
            }
        });
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }
}
