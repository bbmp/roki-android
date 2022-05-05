package com.robam.roki.ui.page.device.dishWasher;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.TypedValue;
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
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.EventUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.DishWasherAlarmEvent;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.common.pojos.device.dishWasher.DishWasherStatus;
import com.robam.common.pojos.device.dishWasher.DishWasherWorkMode;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.utils.AlarmDataUtils;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AbsDishWasherWorkingView extends FrameLayout {
    @InjectView(R.id.tv_model)
    TextView tvModel;
    @InjectView(R.id.tv_temp)
    TextView tvTemp;
    @InjectView(R.id.tv_time)
    TextView tvTime;
    @InjectView(R.id.tv_model_content)
    TextView tvModelContent;
    @InjectView(R.id.tv_temp_content)
    TextView tvTempContent;
    @InjectView(R.id.tv_time_content)
    TextView tvTimeContent;

    @InjectView(R.id.fl_run_and_stop)
    FrameLayout flRunAndStop;
    @InjectView(R.id.ll_run_animation)
    RelativeLayout llRunAnimation;
    @InjectView(R.id.iv_run_down)
    ImageView ivRunDown;
    @InjectView(R.id.iv_run_up)
    ImageView ivRunUp;

    @InjectView(R.id.fl_data_show)
    FrameLayout flDataShow;
    @InjectView(R.id.tv_use_water)
    TextView tvUseWater;
    @InjectView(R.id.tv_use_water_show)
    TextView tvUseWaterShow;
    @InjectView(R.id.tv_use_power)
    TextView tvUsePower;
    @InjectView(R.id.tv_use_power_show)
    TextView tvUsePowerShow;

    @InjectView(R.id.fl_run_stop)
    FrameLayout flRunStop;
    @InjectView(R.id.finish_img)
    ImageView finishImg;
    @InjectView(R.id.finish_name)
    TextView finishName;

    @InjectView(R.id.fl_pause_continue)
    FrameLayout flPauseContinue;
    @InjectView(R.id.iv_pause_continue)
    ImageView ivPauseContinue;
    @InjectView(R.id.tv_pause_continue)
    TextView tvPauseContinue;

    @InjectView(R.id.tv_show_top)
    TextView tvShowTop;
    @InjectView(R.id.tv_show_center)
    TextView tvShowCenter;
    @InjectView(R.id.tv_show_bottom)
    TextView tvShowBottom;
    @InjectView(R.id.tv_ventilation_desc)
    TextView tvVentilationDesc;
    @InjectView(R.id.rl_ventilation_desc)
    RelativeLayout rlVentilationDesc;
    @InjectView(R.id.normal_show)
    FrameLayout normalShow;
    Context cx;
    AbsDishWasher washer;
    private List<DeviceConfigurationFunctions> hideList;
    private List<DeviceConfigurationFunctions> subFunList;
    private List<DeviceConfigurationFunctions> funShow;


    Animation circleRotateDown;
    Animation circleRotateUp;
    IRokiDialog closedialog = null;
    private String params;

    public AbsDishWasherWorkingView(@NonNull Context context) {
        super(context);
    }

    public AbsDishWasherWorkingView(Context context, List<DeviceConfigurationFunctions> hideList, AbsDishWasher washer) {
        super(context);
        this.cx = context;
        this.hideList = hideList;
        this.washer = washer;
        initView();
        EventUtils.regist(this);
    }


    private void initView() {
        View view = LayoutInflater.from(cx).inflate(R.layout.abs_dish_washer_working_page, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }

        try {
            for (int i = 0; i < hideList.size(); i++) {
                if ("runTimeUp".equals(hideList.get(i).functionCode)) {
                    subFunList = hideList.get(i)
                            .subView
                            .subViewModelMap
                            .subViewModelMapSubView
                            .deviceConfigurationFunctions;
                } else if ("runTimeDown".equals(hideList.get(i).functionCode)) {
                    funShow = hideList.get(i)
                            .subView
                            .subViewModelMap
                            .subViewModelMapSubView
                            .deviceConfigurationFunctions;
                }
            }
            //上面布局展示
            showUpView();
            //下面布局展示
            showBottomView(washer);
            updateStatus(washer);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showBottomView(AbsDishWasher washer) {
        for (int i = 0; i < funShow.size(); i++) {
            if (DishWasherName.suspend.equals(funShow.get(i).functionCode)) {
                if (washer.powerStatus == DishWasherStatus.working) {
                    String backgroundImg = funShow.get(i).backgroundImg;
                    String functionName = funShow.get(i).functionName;
                    Glide.with(cx).load(backgroundImg).into(ivPauseContinue);
                    tvPauseContinue.setText(functionName);
                }

            } else if (DishWasherName.goonWorking.equals(funShow.get(i).functionCode)) {
                if (washer.powerStatus == DishWasherStatus.pause) {
                    String backgroundImg = funShow.get(i).backgroundImg;
                    String functionName = funShow.get(i).functionName;
                    Glide.with(cx).load(backgroundImg).into(ivPauseContinue);
                    tvPauseContinue.setText(functionName);
                }
            } else if (DishWasherName.stop.equals(funShow.get(i).functionCode)) {
                String backgroundImg = funShow.get(i).backgroundImg;
                String functionName = funShow.get(i).functionName;
                Glide.with(cx).load(backgroundImg).into(finishImg);
                finishName.setText(functionName);
            } else if (DishWasherName.waterConsumption.equals(funShow.get(i).functionCode)) {
                String functionName = funShow.get(i).functionName;
                tvUseWater.setText(functionName);
            } else if (DishWasherName.electricityConsumption.equals(funShow.get(i).functionCode)) {
                String functionName = funShow.get(i).functionName;
                tvUsePower.setText(functionName);

            }
        }
    }

    private void showUpView() {
        try {
            for (int i = 0; i < subFunList.size(); i++) {
                if (DishWasherName.mode.equals(subFunList.get(i).functionCode)) {
                    tvModel.setText(subFunList.get(i).functionName);
                    params = subFunList.get(i).functionParams;
                } else if (DishWasherName.additionalFunction.equals(subFunList.get(i).functionCode)) {
                    tvTemp.setText(subFunList.get(i).functionName);
                } else if (DishWasherName.workTime.equals(subFunList.get(i).functionCode)) {
                    tvTime.setText(subFunList.get(i).functionName);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void closeAllDialog() {
        if (closedialog != null && closedialog.isShow()) {
            closedialog.dismiss();
        }
    }


    /**
     * 开启动画
     */
    void startAnimation() {
        if (circleRotateDown == null) {
            circleRotateDown = AnimationUtils.loadAnimation(getContext(), R.anim.device_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotateDown.setInterpolator(lin);
            ivRunDown.startAnimation(circleRotateDown);
        }

        if (circleRotateUp == null) {
            circleRotateUp = AnimationUtils.loadAnimation(getContext(), R.anim.device_model_circle_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotateUp.setInterpolator(lin);
            ivRunUp.startAnimation(circleRotateUp);
        }
    }

    /**
     * 关闭动画和烟雾效果
     */
    public void stopAnimation() {
        if (circleRotateDown != null) {
            circleRotateDown.cancel();
            circleRotateDown = null;
            ivRunDown.clearAnimation();
        }
        if (circleRotateUp != null) {
            circleRotateUp.cancel();
            circleRotateUp = null;
            ivRunUp.clearAnimation();
        }
    }


    public void updateStatus(AbsDishWasher washer) {
        this.washer = washer;
        switch (washer.powerStatus) {
            case DishWasherStatus.working://工作中
                appointmentJudgment(washer);
                break;
            case DishWasherStatus.pause://暂停中
                pauseStatus(washer);
                break;
            case DishWasherStatus.end://完成
                completeStatus();
                break;
            case DishWasherStatus.off://关机
                break;
            default:
                break;
        }


    }


    public void updateCompleteState(short powerConsumption, short waterConsumption) {
        tvUseWaterShow.setText(String.valueOf((double) waterConsumption / 10));
        tvUsePowerShow.setText(String.valueOf((double) powerConsumption / 100));
    }


    public void appointmentJudgment(AbsDishWasher washer) {

        switch (washer.AppointmentSwitchStatus) {
            //预约状态关
            case DishWasherStatus.appointmentSwitchOff:
                workingStatus(washer);
                break;
            //预约状态开
            case DishWasherStatus.appointmentSwitchOn:
                appointmentStatus(washer);
                break;
            default:
                break;

        }

    }

    //工作状态
    public void workingStatus(AbsDishWasher washer) {
        try {
            startAnimation();

            tvShowTop.setVisibility(View.VISIBLE);
            tvShowCenter.setVisibility(View.VISIBLE);

            flDataShow.setVisibility(View.INVISIBLE);

            tvShowTop.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tvShowCenter.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            tvShowBottom.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);


            //自动换气
            if (washer.DishWasherWorkMode == DishWasherWorkMode.Ventilation) {
                JSONObject jsonObject = new JSONObject(params);
                String desc = jsonObject.getJSONObject(washer.DishWasherWorkMode + "").getString("desc");
                normalShow.setVisibility(View.INVISIBLE);
                rlVentilationDesc.setVisibility(View.VISIBLE);
                tvVentilationDesc.setText(desc);
                tvShowBottom.setVisibility(View.INVISIBLE);
                flPauseContinue.setVisibility(View.VISIBLE);
                flRunStop.setVisibility(View.VISIBLE);
                tvShowTop.setText("工作剩余时间");
                tvShowCenter.setText(String.format("%s分钟", washer.DishWasherRemainingWorkingTime));
                showBottomView(washer);
                //等待自动换气
            } else if (washer.DishWasherWorkMode == DishWasherWorkMode.VentilationWait) {
                JSONObject jsonObject = new JSONObject(params);
                String desc = jsonObject.getJSONObject(washer.DishWasherWorkMode + "").getString("desc");
                normalShow.setVisibility(View.INVISIBLE);
                rlVentilationDesc.setVisibility(View.VISIBLE);
                tvVentilationDesc.setText(desc);
                tvShowBottom.setVisibility(View.VISIBLE);
                flPauseContinue.setVisibility(View.INVISIBLE);
                flRunStop.setVisibility(View.VISIBLE);
                flRunAndStop.setVisibility(View.VISIBLE);
                tvShowTop.setText("洗碗机将在");
                tvShowCenter.setText(String.format("%s分钟", washer.DishWasherRemainingWorkingTime));
                tvShowBottom.setText("后为您自动换气");
                finishName.setText("取消自动");
            } else {
                //工作中
                flRunStop.setVisibility(View.VISIBLE);
                flPauseContinue.setVisibility(View.VISIBLE);
                normalShow.setVisibility(View.VISIBLE);
                rlVentilationDesc.setVisibility(View.INVISIBLE);
                //模式名称
                tvModelContent.setText(code2Name(washer) != null ? code2Name(washer) : "");
                tvTempContent.setText(additionalShow(washer));
                tvShowTop.setText("工作剩余时间");
                //工作剩余时间
                tvShowCenter.setText(String.format("%s分钟", washer.DishWasherRemainingWorkingTime));
                //工作时长
                tvTimeContent.setText(String.format("%s分钟", washer.SetWorkTimeValue));
                tvShowBottom.setVisibility(View.VISIBLE);
                tvShowBottom.setText(String.format("实时水温：%s℃", (double) washer.CurrentWaterTemperatureValue / 10));
                showBottomView(washer);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //模式Code转模式名称
    public String code2Name(AbsDishWasher washer) throws JSONException {
        String modeName;
        if (!"".equals(params)) {
            JSONObject jsonObject = new JSONObject(params);
            modeName = jsonObject.getJSONObject(washer.DishWasherWorkMode + "").getString("value");
            return modeName;
        } else {
            return "";
        }

    }

    //附加功能
    public String additionalShow(AbsDishWasher washer) {
        String totalShow = "";
        String lowerLayerWasher = washer.LowerLayerWasher == (short) 0 ? "" : "下层洗";
        String enhancedDryStatus = washer.EnhancedDryStatus == (short) 0 ? "" : "加强干燥";
        String autoVentilation = washer.AutoVentilation == (short) 0 ? "" : "自动换气";

        if (!"".equals(lowerLayerWasher)) {
            totalShow = lowerLayerWasher;

        } else if (!"".equals(enhancedDryStatus)) {
            totalShow = enhancedDryStatus;

        }
        if (!"".equals(autoVentilation)) {
            if (!"".equals(totalShow)) {
                totalShow = totalShow + "、" + autoVentilation;

            } else {
                totalShow = autoVentilation;

            }

        }
        if (totalShow.equals("")) {
            totalShow = "--";

        }
        return totalShow;


    }

    //预约状态
    public void appointmentStatus(AbsDishWasher washer) {
        try {

            startAnimation();
            normalShow.setVisibility(View.VISIBLE);
            rlVentilationDesc.setVisibility(View.INVISIBLE);

            tvShowTop.setVisibility(View.VISIBLE);
            tvShowCenter.setVisibility(View.VISIBLE);
            tvShowBottom.setVisibility(View.VISIBLE);
            flDataShow.setVisibility(View.INVISIBLE);
            flPauseContinue.setVisibility(View.INVISIBLE);

            tvShowTop.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            tvShowCenter.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tvShowBottom.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            tvShowTop.setText("预约中");
            String str = TimeUtils.min2HourM(washer.AppointmentRemainingTime);
            tvShowCenter.setText(String.format("设备将在%s后", str));
            tvShowBottom.setText("开始工作");


            tvModelContent.setText(code2Name(washer) != null ? code2Name(washer) : "");
            tvTempContent.setText(additionalShow(washer));
            tvTimeContent.setText(String.format("%s分钟", washer.SetWorkTimeValue));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //暂停状态
    public void pauseStatus(AbsDishWasher washer) {
        try {
            startAnimation();


            if (washer.DishWasherWorkMode == 9) {
                normalShow.setVisibility(View.INVISIBLE);
                rlVentilationDesc.setVisibility(View.VISIBLE);
                tvShowBottom.setVisibility(View.INVISIBLE);
                JSONObject jsonObject = new JSONObject(params);
                String desc = jsonObject.getJSONObject(washer.DishWasherWorkMode + "").getString("desc");
                normalShow.setVisibility(View.INVISIBLE);
                tvVentilationDesc.setText(desc);
            } else {

                tvShowBottom.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                normalShow.setVisibility(View.VISIBLE);
                rlVentilationDesc.setVisibility(View.INVISIBLE);
                if (washer.AppointmentSwitchStatus == DishWasherStatus.appointmentSwitchOn) {
                    tvShowBottom.setVisibility(View.INVISIBLE);
                } else {
                    tvShowBottom.setVisibility(View.VISIBLE);
                    tvShowBottom.setText(String.format("实时水温：%s℃", (double) washer.CurrentWaterTemperatureValue / 10));
                }

            }

            flRunStop.setVisibility(View.VISIBLE);
            flDataShow.setVisibility(View.INVISIBLE);
            tvShowTop.setVisibility(View.VISIBLE);
            tvShowCenter.setVisibility(View.VISIBLE);


            tvShowTop.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            tvShowCenter.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);



            //预约状态
            if (washer.AppointmentSwitchStatus == DishWasherStatus.appointmentSwitchOn) {
                flPauseContinue.setVisibility(View.INVISIBLE);
                tvShowTop.setText("预约暂停中");
                String str = TimeUtils.min2HourM(washer.AppointmentRemainingTime);
                tvShowCenter.setText(str);
            } else {
                flPauseContinue.setVisibility(View.VISIBLE);
                tvShowTop.setText("暂停中");
                tvShowCenter.setText(String.format("%s分钟", washer.DishWasherRemainingWorkingTime));
            }

            tvModelContent.setText(code2Name(washer) != null ? code2Name(washer) : "");
            tvTempContent.setText(additionalShow(washer));
            tvTimeContent.setText(String.format("%s分钟", washer.SetWorkTimeValue));

            showBottomView(washer);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //完成状态
    public void completeStatus() {
        try {
            stopAnimation();
            normalShow.setVisibility(View.VISIBLE);
            rlVentilationDesc.setVisibility(View.INVISIBLE);

            tvShowTop.setVisibility(View.INVISIBLE);
            tvShowCenter.setVisibility(View.VISIBLE);
            tvShowBottom.setVisibility(View.INVISIBLE);
            flPauseContinue.setVisibility(View.INVISIBLE);
            flDataShow.setVisibility(View.VISIBLE);
            tvShowCenter.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            tvShowCenter.setText("完成");

            tvModelContent.setText(code2Name(washer) != null ? code2Name(washer) : "");
            tvTempContent.setText(additionalShow(washer));
            tvTimeContent.setText(String.format("%s分钟", washer.SetWorkTimeValue));
            flRunStop.setVisibility(View.INVISIBLE);
            tvUseWaterShow.setText(String.valueOf((double) washer.waterConsumption / 10));
            tvUsePowerShow.setText(String.valueOf((double) washer.powerConsumption / 100));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @OnClick({R.id.fl_pause_continue, R.id.fl_run_stop})
    public void OnClickView(View view) {
        switch (view.getId()) {
            case R.id.fl_pause_continue:
                if (childLock()) {
                    if (washer.alarmId == 0) {
                        pauseAndContinueWork();
                    } else {
                        AlarmDataUtils.dishWasherAlarm(washer, washer.alarmId);
                    }
                }
                break;
            case R.id.fl_run_stop:
                if (childLock()) {
                    finishWork();
                }
                break;
        }
    }


    private boolean childLock() {
        if (washer.StoveLock == 1) {
            return false;
        } else {
            return true;
        }
    }

    private void finishWork() {
        closedialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        closedialog.setTitleText(R.string.close_work);
        closedialog.setContentText(R.string.is_close_work);
        closedialog.show();
        closedialog.setOkBtn(R.string.ok_btn, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closedialog.isShow()) {
                    closedialog.dismiss();
                }
                washer.setDishWasherStatusControl(DishWasherStatus.off, null);
            }
        });
        closedialog.setCancelBtn(R.string.can_btn, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closedialog.isShow()) {
                    closedialog.dismiss();
                }
            }
        });
    }

    private void pauseAndContinueWork() {
        if (washer.powerStatus == DishWasherStatus.working) {
            washer.setDishWasherStatusControl(DishWasherStatus.pause, null);
        } else {
            washer.setDishWasherStatusControl(DishWasherStatus.working, null);
        }
    }


    @Subscribe
    public void onEvent(DishWasherAlarmEvent event) {
        if (washer == null || !Objects.equal(washer.getID(), event.washer.getID())) {
            return;
        }
        this.washer = event.washer;
    }

}
