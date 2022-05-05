package com.robam.roki.ui.page.device.steam;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamMode;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.utils.AlarmDataUtils;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robam.common.util.FanLockUtils.mGuid;
import static com.robam.roki.R.id.oven_complete;

/**
 * Created by Dell on 2018/8/29.
 */

public class SteamWorkingView extends FrameLayout {
    Context cx;
    AbsSteamoven steam;
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
    @InjectView(R.id.normal_show)
    FrameLayout normalShow;
    @InjectView(R.id.ll)
    RelativeLayout ll;
    @InjectView(R.id.iv_run_down)
    ImageView ivRunDown;
    @InjectView(R.id.iv_run_up)
    ImageView ivRunUp;
    @InjectView(R.id.ll_run_animation)
    RelativeLayout llRunAnimation;
    @InjectView(R.id.tv_work_state_name)
    TextView tvWorkStateName;
    @InjectView(R.id.tv_work_dec)
    TextView tvWorkDec;
    @InjectView(R.id.imageView9)
    ImageView imageView9;
    @InjectView(R.id.fl_run_and_stop)
    FrameLayout flRunAndStop;
    @InjectView(R.id.rotate_img)
    ImageView rotateImg;
    @InjectView(R.id.rotate_name)
    TextView rotateName;
    @InjectView(R.id.rotate)
    FrameLayout rotate;
    @InjectView(oven_complete)
    FrameLayout ovenComplete;
    @InjectView(R.id.finish_img)
    ImageView finishImg;
    @InjectView(R.id.finish_name)
    TextView finishName;
    @InjectView(R.id.fl_run_stop)
    FrameLayout flRunStop;
    @InjectView(R.id.fl_wait_and_pause)
    FrameLayout flWaitAndPause;
    @InjectView(R.id.iv_wait_and_pause)
    ImageView ivWaitAndPause;
    @InjectView(R.id.tv_wait_and_pause)
    TextView tvWaitAndPause;

    @InjectView(R.id.fl_localcookbook)
    FrameLayout fllocalcookbook;
    @InjectView(R.id.localcookbook_mode)
    TextView localcookbook_mode;

    @InjectView(R.id.fl_auto_step)
    FrameLayout flAutoStep;
    @InjectView(R.id.tv_auto_step)
    TextView tvAutoStep;

    @InjectView(R.id.btn_one)
    Button mBtnOne;
    @InjectView(R.id.btn_two)
    Button mBtnTwo;
    @InjectView(R.id.btn_there)
    Button mBtnThere;
    @InjectView(R.id.ll_btn)
    FrameLayout mLlBtn;


    private List<DeviceConfigurationFunctions> bgFunList;
    private List<DeviceConfigurationFunctions> otherList;
    private List<DeviceConfigurationFunctions> subFunList;
    private List<DeviceConfigurationFunctions> funShow;
    private JSONObject localRecipeJson;

    public SteamWorkingView(Context context, List<DeviceConfigurationFunctions> bgFunList, AbsSteamoven steam) {
        super(context);
        this.cx = context;
        this.bgFunList = bgFunList;
        this.steam = steam;
        initView();
    }

    public SteamWorkingView(Context context, List<DeviceConfigurationFunctions> bgFunList, List<DeviceConfigurationFunctions> otherList, AbsSteamoven steam) {
        super(context);
        this.cx = context;
        this.bgFunList = bgFunList;
        this.otherList = otherList;
        this.steam = steam;
        initView();
    }


    public void initView() {
        try {
            View view = LayoutInflater.from(cx).inflate(R.layout.abs_devcie_module_steam, this, true);
            if (!view.isInEditMode()) {
                ButterKnife.inject(this, view);
            }
            for (int i = 0; i < bgFunList.size(); i++) {
                if ("runTimeUpView".equals(bgFunList.get(i).functionCode)) {
                    subFunList = bgFunList.get(i)
                            .subView
                            .subViewModelMap
                            .subViewModelMapSubView
                            .deviceConfigurationFunctions;
                } else if ("runTimeDownView".equals(bgFunList.get(i).functionCode)) {
                    funShow = bgFunList.get(i)
                            .subView
                            .subViewModelMap
                            .subViewModelMapSubView
                            .deviceConfigurationFunctions;
                }
            }
            for (int i = 0; i < otherList.size(); i++) {
                if ("localCookbook".equals(otherList.get(i).functionCode)) {
                    List<DeviceConfigurationFunctions> deviceConfigurationFunctions = otherList.get(i)
                            .subView
                            .subViewModelMap
                            .subViewModelMapSubView
                            .deviceConfigurationFunctions;

                    for (int i1 = 0; i1 < deviceConfigurationFunctions.size(); i1++) {
                        if (deviceConfigurationFunctions.get(i1).functionCode.equals("cookBookTop")) {
                            String functionParams = deviceConfigurationFunctions.get(i1).functionParams;
                            localRecipeJson = new JSONObject(functionParams);
                        }

                    }

                }
            }
            if (steam.steamAutoRecipeModeValue != 0) {
                localRecipe();
            } else {
                if (steam.mode == 20) {
                    Descaling();
                } else {
                    normalShow();
                }

            }

            showRunDown();
            startAnimation();
            updateStatus(steam);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showRunDown() {
        for (int i = 0; i < funShow.size(); i++) {
            if ("waterTank".equals(funShow.get(i).functionCode)) {

                if (steam.mode != SteamMode.SteamClean) {
                    Glide.with(cx).load(funShow.get(i).backgroundImg).into(rotateImg);
                    rotateName.setText(funShow.get(i).functionName);
                }

            } else if ("finish".equals(funShow.get(i).functionCode)) {
                Glide.with(cx).load(funShow.get(i).backgroundImg).into(finishImg);
                finishName.setText(funShow.get(i).functionName);
            } else if ("wait".equals(funShow.get(i).functionCode)) {
                if (steam.mode != SteamMode.SteamClean) {
                    if (steam.status == SteamStatus.Pause) {
                        Glide.with(cx).load(funShow.get(i).backgroundImgH).into(ivWaitAndPause);
                        tvWaitAndPause.setText("继续");
                    } else {
                        Glide.with(cx).load(funShow.get(i).backgroundImg).into(ivWaitAndPause);
                        tvWaitAndPause.setText(funShow.get(i).functionName);
                    }
                }

            }
        }
    }

    private void normalShow() {
        fllocalcookbook.setVisibility(INVISIBLE);
        ll.setVisibility(View.VISIBLE);
        try {
            for (int i = 0; i < subFunList.size(); i++) {
                if ("model".equals(subFunList.get(i).functionCode)) {
                    tvModel.setText(subFunList.get(i).functionName);
                } else if ("temperature".equals(subFunList.get(i).functionCode)) {
                    tvTemp.setText(subFunList.get(i).functionName);
                } else if ("remainingTime".equals(subFunList.get(i).functionCode)) {
                    tvTime.setText(subFunList.get(i).functionName);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void localRecipe() {
        ll.setVisibility(View.GONE);
        fllocalcookbook.setVisibility(View.VISIBLE);
        try {
            if (localRecipeJson != null) {
                JSONObject jsonObject = localRecipeJson.getJSONObject(steam.steamAutoRecipeModeValue + "");
                String pKey = jsonObject.getString("pKey");
                String name = jsonObject.getString("value");
                localcookbook_mode.setText(pKey + "   " + name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void Descaling() {
        fllocalcookbook.setVisibility(View.VISIBLE);
        ll.setVisibility(View.GONE);
        JSONObject obj = null;
        try {
            if (subFunList.size() == 0) {
                return;
            }
            obj = new JSONObject(subFunList.get(0).functionParams);
            String param = (String) obj.getJSONObject("param").getJSONObject(steam.mode + "").get("value");
            localcookbook_mode.setText(param);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    IRokiDialog closedialog = null;

    @OnClick(R.id.fl_run_stop)
    public void finishWork() {

        closedialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        closedialog.setTitleText(R.string.close_work);
        closedialog.setContentText(R.string.is_close_work);
        closedialog.show();
        closedialog.setOkBtn(R.string.ok_btn, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closedialog.isShow()) {
                    closedialog.dismiss();
                    if ("RS209".equals(steam.getDt()) || steam.status == SteamStatus.Order) {
                        send209Off();
                    } else {
                        sendCommonOn();
                    }
                }
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


    @OnClick(R.id.fl_wait_and_pause)
    public void runAndStop() {
        if (steam.doorState == 0) {
            ToastUtils.show("门未关好，请先关好箱门", Toast.LENGTH_SHORT);
            return;
        }
        if(alarmDialog()){
            return;
        }
        if (steam.waterboxstate == 0) {
            ToastUtils.show("水箱已弹出", Toast.LENGTH_SHORT);
            return;
        }
        if (steam.status == SteamStatus.Pause) {
            steam.setSteamStatus(SteamStatus.Working, new VoidCallback() {
                @Override
                public void onSuccess() {
                    LogUtils.i("2020071501", "success");
                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("2020071501", t.getMessage());
                }
            });
        } else {
            steam.setSteamStatus(SteamStatus.Pause, new VoidCallback() {
                @Override
                public void onSuccess() {
                    LogUtils.i("2020071501", "success");
                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("2020071501", t.getMessage());
                }
            });


        }

    }
    private boolean alarmDialog() {
        AbsSteamoven device = Plat.deviceService.lookupChild(mGuid);
        if (device.status == SteamStatus.AlarmStatus) {
            AlarmDataUtils.SteamAlarmStatus( steam,steam.alarm);
            return true;
        }
        return false;
    }
    private void send209Off() {
        steam.setSteamStatus(SteamStatus.Off, new VoidCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show("指令下发失败", Toast.LENGTH_SHORT);
            }
        });
    }

    private void sendCommonOn() {
        steam.setSteamStatus(SteamStatus.On, new VoidCallback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show("指令下发失败", Toast.LENGTH_SHORT);
            }
        });
    }

    @OnClick(R.id.rotate)
    public void tanWater() {
        if (IRokiFamily.RS228.equals(steam.getDt()) && (steam.status == SteamStatus.Working || steam.status == SteamStatus.PreHeat ||
                steam.status == SteamStatus.Order)) {
            ToastUtils.show("工作中无法操作水箱", Toast.LENGTH_SHORT);
            return;
        }

        if (IRokiFamily.RS228.equals(steam.getDt())) {
            if (steam.mode == 21 || steam.mode == 20) {//干燥21，除垢20
                ToastUtils.show("该状态无法操作水箱", Toast.LENGTH_SHORT);
                return;
            }

        }


        if (steam.waterboxstate == 0) {
            ToastUtils.show("水箱已弹出", Toast.LENGTH_SHORT);
            return;
        }

        steam.setSteamWaterTankPOP(new VoidCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.show("水箱已弹出", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show("弹出失败", Toast.LENGTH_SHORT);
            }
        });
    }

    //水箱状态变化
    private void tankStatusChange(short status) {
        for (int i = 0; i < funShow.size(); i++) {
            if ("waterTank".equals(funShow.get(i).functionCode)) {
                //暂时做在本地
                if (status == 1) {
                    Glide.with(cx).load(funShow.get(i).backgroundImg).into(rotateImg);
                } else {
                    Glide.with(cx).load(funShow.get(i).backgroundImgH).into(rotateImg);
                }

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


    public void closeAllDialog() {
        if (closedialog != null && closedialog.isShow()) {
            closedialog.dismiss();
        }
        LogUtils.i("202011131154","steam.status:::"+steam.status);
        flAutoStep.setVisibility(View.GONE);
    }

    public void updateStatus(AbsSteamoven steam) {

        this.steam = steam;
        tvWorkDec.setVisibility(INVISIBLE);
        ovenComplete.setVisibility(INVISIBLE);
        mLlBtn.setVisibility(View.GONE);
        showRunDown();
        if (steam.steamAutoRecipeModeValue != 0) {
            localRecipe();
        } else {
            if (steam.mode == 20) {
                Descaling();
            } else {
                commonMode();
                normalShow();
            }
        }

        if (steam.mode != SteamMode.SteamClean) {
            tankStatusChange(steam.waterboxstate);
        }
        LogUtils.i("202010271001", "steam.status:::" + steam.status);
        switch (steam.status) {
            case SteamStatus.Pause://暂停
                pauseMode();
                break;
            case SteamStatus.Working://运行
                runMode();
                break;
            case SteamStatus.Order://预约
                orderMode();
                break;
            case SteamStatus.PreHeat://预热
                heatMode();
                break;
            default:
                break;
        }

        LogUtils.i("202010271001", "steam.descaleModeStageValue:::" + steam.descaleModeStageValue);
        showDescalingText();
    }


    private void commonMode() {
        tvTempContent.setText(steam.tempSet + "℃");
        tvTimeContent.setText(steam.timeSet + "min");
        JSONObject obj = null;
        try {
            if (subFunList.size() == 0) {
                return;
            }
            obj = new JSONObject(subFunList.get(0).functionParams);
            String param = (String) obj.getJSONObject("param").getJSONObject(steam.mode + "").get("value");
            tvModelContent.setText(param);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //预约
    private void orderMode() {
        tvWorkDec.setVisibility(VISIBLE);
        tvWorkStateName.setVisibility(View.VISIBLE);
        tvWorkStateName.setTextSize(40);
        tvWorkStateName.setText("预约中");
        tvWorkDec.setTextSize(20);
        String strHour;
        String strMin;
        if (steam.orderTime_hour < 10) {
            strHour = "0" + steam.orderTime_hour;
        } else {
            strHour = steam.orderTime_hour + "";
        }
        if (steam.orderTime_min < 10) {
            strMin = "0" + steam.orderTime_min;
        } else {
            strMin = steam.orderTime_min + "";
        }
        tvWorkDec.setText("预约开始时间：" + strHour + ":" + strMin);
    }


    //暂停
    private void pauseMode() {
        LogUtils.i("202010271001", "暂停:::" + steam.status);
        flWaitAndPause.setVisibility(VISIBLE);
        flRunStop.setVisibility(VISIBLE);
        rotate.setVisibility(VISIBLE);
        if (steam.mode == 20) {
            tvWorkDec.setVisibility(INVISIBLE);
        } else {
            tvWorkDec.setVisibility(VISIBLE);
        }
        tvWorkStateName.setVisibility(View.VISIBLE);
        tvWorkStateName.setTextSize(40);
        tvWorkStateName.setText("暂停中");
        String str = TimeUtils.timeToStr(steam.time);
        tvWorkDec.setTextSize(25);
        tvWorkDec.setText(str);
    }

    //预热
    private void heatMode() {
        flWaitAndPause.setVisibility(VISIBLE);
        flRunStop.setVisibility(VISIBLE);
        rotate.setVisibility(INVISIBLE);
        tvWorkDec.setVisibility(VISIBLE);
        tvWorkStateName.setVisibility(View.VISIBLE);
        tvWorkStateName.setTextSize(20);
        tvWorkStateName.setText("预热中");
        tvWorkDec.setTextSize(40);
        tvWorkDec.setText(steam.temp + "℃");
    }

    //工作中
    private void runMode() {
        flWaitAndPause.setVisibility(VISIBLE);
        flRunStop.setVisibility(VISIBLE);
        rotate.setVisibility(INVISIBLE);
        tvWorkDec.setVisibility(VISIBLE);
        tvWorkStateName.setVisibility(VISIBLE);
        tvWorkStateName.setTextSize(20);
        tvWorkStateName.setText("工作剩余时间");
        tvWorkDec.setTextSize(40);
        String str = null;
        LogUtils.i("202011051152","steam.SetTime2_value:::"+steam.SetTime2_value);
        LogUtils.i("202011051152","steam.time:::"+steam.time);
        if (steam.SetTime2_value>0) {
            short time = steam.time;
            short setTime2_value = steam.SetTime2_value;

            int i = time & 0xff;//低8位
            int i1 = (setTime2_value & 0xff00) >> 8;//高8位
            int i2 = i | (i1 << 8);
            str = TimeUtils.timeToStr((short) i2);
        }else{
            str = TimeUtils.timeToStr(steam.time);
        }

        tvWorkDec.setText(str);


    }

    protected void completeWork() {
        complete();
    }

    //完成
    private void complete() {
        tvWorkDec.setVisibility(VISIBLE);
        tvWorkStateName.setVisibility(View.INVISIBLE);
        tvWorkDec.setTextSize(45);
        tvWorkDec.setText("完成");
        flRunStop.setVisibility(INVISIBLE);
        rotate.setVisibility(INVISIBLE);
        ovenComplete.setVisibility(VISIBLE);
        flWaitAndPause.setVisibility(INVISIBLE);
        flAutoStep.setVisibility(INVISIBLE);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();

    }

    private void showDescalingText() {
        if (!TextUtils.equals(steam.getDt(), "SQ235")&&!TextUtils.equals(steam.getDt(), "Q282A")) {
            return;
        }
        if (steam.mode != SteamMode.SteamClean) {
            return;
        }
        rotate.setVisibility(INVISIBLE);
        flWaitAndPause.setVisibility(INVISIBLE);
        if (steam.mode == 20) {
            tvWorkDec.setVisibility(INVISIBLE);
        } else {
            tvWorkDec.setVisibility(VISIBLE);
        }
        try {
            for (int i = 0; i < subFunList.size(); i++) {
                if ("model".equals(subFunList.get(i).functionCode)) {
                    String params = subFunList.get(i).functionParams;
                    JSONObject jsonObject = new JSONObject(params);
                    JSONObject param = jsonObject.getJSONObject("param");
                    JSONObject obj = param.getJSONObject(String.valueOf(steam.mode));
                    String functionParams = obj.getString("functionParams");
                    JSONObject object = new JSONObject(functionParams);
                    JSONArray multiSegment = object.getJSONArray("multiSegment");
                    for (int j = 0; j < multiSegment.length(); j++) {
                        //第一阶段
                        if (steam.descaleModeStageValue == 1) {
                            flAutoStep.setVisibility(View.VISIBLE);
                            tvAutoStep.setText(multiSegment.optString(0) == null ? "" : multiSegment.optString(0));
                            //tvWorkStateName.setVisibility(INVISIBLE);
                            //tvWorkDec.setVisibility(View.VISIBLE);
                            //tvWorkDec.setText("工作中");
                            //tvWorkDec.setTextSize(36);

                            mLlBtn.setVisibility(View.VISIBLE);
                            mBtnOne.setVisibility(View.INVISIBLE);
                            mBtnTwo.setVisibility(View.INVISIBLE);
                            mBtnThere.setVisibility(View.INVISIBLE);

                            descalingModeShow(steam.descaleModeStageValue);


                            switch (steam.status) {

                                case SteamStatus.Working://运行
                                    tvWorkStateName.setVisibility(INVISIBLE);
                                    tvWorkDec.setVisibility(View.VISIBLE);
                                    tvWorkDec.setText("工作中");
                                    tvWorkDec.setTextSize(36);
                                    break;

                                default:
                                    break;
                            }
                            //第一阶段完成
                        } else if (steam.descaleModeStageValue == 11) {
                            flAutoStep.setVisibility(View.VISIBLE);
                            tvAutoStep.setText(multiSegment.optString(3) == null ? "" : multiSegment.optString(3));
                            //tvWorkStateName.setVisibility(INVISIBLE);
                            //tvWorkDec.setVisibility(View.VISIBLE);
                            //tvWorkDec.setTextSize(36);
                            //tvWorkDec.setText("完成");
                            mLlBtn.setVisibility(View.VISIBLE);
                            mBtnOne.setVisibility(View.INVISIBLE);
                            mBtnTwo.setVisibility(View.INVISIBLE);
                            mBtnThere.setVisibility(View.INVISIBLE);
                            descalingModeShow(steam.descaleModeStageValue);
                            switch (steam.status) {

                                case SteamStatus.Working://运行
                                    tvWorkStateName.setVisibility(INVISIBLE);
                                    tvWorkDec.setVisibility(View.VISIBLE);
                                    tvWorkDec.setTextSize(36);
                                    tvWorkDec.setText("暂停");
                                    break;

                                default:
                                    break;
                            }

                            //第二阶段
                        } else if (steam.descaleModeStageValue == 2) {

                            flAutoStep.setVisibility(View.VISIBLE);
                            tvAutoStep.setText(multiSegment.optString(1) == null ? "" : multiSegment.optString(1));
                            //tvWorkStateName.setVisibility(INVISIBLE);
                            //tvWorkDec.setVisibility(View.VISIBLE);
                            //tvWorkDec.setText("工作中");
                            //tvWorkDec.setTextSize(36);
                            mBtnOne.setVisibility(View.VISIBLE);
                            mBtnTwo.setVisibility(View.INVISIBLE);
                            mBtnThere.setVisibility(View.VISIBLE);
                            descalingModeShow(steam.descaleModeStageValue);

                            switch (steam.status) {
                                case SteamStatus.Working://运行
                                    tvWorkStateName.setVisibility(INVISIBLE);
                                    tvWorkDec.setVisibility(View.VISIBLE);
                                    tvWorkDec.setText("工作中");
                                    tvWorkDec.setTextSize(36);
                                    break;
                                default:
                                    break;
                            }
                            //第二阶段完成
                        } else if (steam.descaleModeStageValue == 21) {

                            flAutoStep.setVisibility(View.VISIBLE);
                            tvAutoStep.setText(multiSegment.optString(3) == null ? "" : multiSegment.optString(3));
                            //tvWorkStateName.setVisibility(INVISIBLE);
                            //tvWorkDec.setVisibility(View.VISIBLE);
                            //tvWorkDec.setTextSize(36);
                            //tvWorkDec.setText("完成");

                            mBtnOne.setVisibility(View.VISIBLE);
                            mBtnTwo.setVisibility(View.INVISIBLE);
                            mBtnThere.setVisibility(View.VISIBLE);
                            descalingModeShow(steam.descaleModeStageValue);

                            switch (steam.status) {

                                case SteamStatus.Working://运行
                                    tvWorkStateName.setVisibility(INVISIBLE);
                                    tvWorkDec.setVisibility(View.VISIBLE);
                                    tvWorkDec.setTextSize(36);
                                    tvWorkDec.setText("暂停");
                                    break;

                                default:
                                    break;
                            }
                            //第三阶段
                        } else if (steam.descaleModeStageValue == 3) {
                            flAutoStep.setVisibility(View.VISIBLE);
                            tvAutoStep.setText(multiSegment.optString(2) == null ? "" : multiSegment.optString(2));
                            //tvWorkStateName.setVisibility(INVISIBLE);
                            //tvWorkDec.setVisibility(View.VISIBLE);
                            //tvWorkDec.setText("工作中");
                            //tvWorkDec.setTextSize(36);
                            mBtnOne.setVisibility(View.VISIBLE);
                            mBtnTwo.setVisibility(View.VISIBLE);
                            mBtnThere.setVisibility(View.VISIBLE);
                            descalingModeShow(steam.descaleModeStageValue);

                            switch (steam.status) {
                                case SteamStatus.Working://运行
                                    tvWorkStateName.setVisibility(INVISIBLE);
                                    tvWorkDec.setVisibility(View.VISIBLE);
                                    tvWorkDec.setText("工作中");
                                    tvWorkDec.setTextSize(36);
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            flAutoStep.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void descalingModeShow(short status) {
        mLlBtn.setVisibility(View.VISIBLE);
        mBtnOne.setVisibility(View.VISIBLE);
        mBtnTwo.setVisibility(View.VISIBLE);
        mBtnThere.setVisibility(View.VISIBLE);
        switch (status) {
            case 1:
            case 11:
                mBtnOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn_bg));
                mBtnOne.setAlpha(1);
                mBtnOne.setText("1");
                mBtnTwo.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                mBtnTwo.setAlpha(0.4f);
                mBtnTwo.setText("2");
                mBtnThere.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                mBtnThere.setAlpha(0.4f);
                mBtnThere.setText("3");
                break;
            case 2:
            case 21:
                mBtnTwo.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn_bg));
                mBtnTwo.setAlpha(1);
                mBtnTwo.setText("2");
                mBtnOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                mBtnOne.setAlpha(0.4f);
                mBtnOne.setText("1");
                mBtnThere.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                mBtnThere.setAlpha(0.4f);
                mBtnThere.setText("3");
                break;
            case 3:
                mBtnThere.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn_bg));
                mBtnThere.setAlpha(1);
                mBtnThere.setText("3");
                mBtnOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                mBtnOne.setAlpha(0.4f);
                mBtnOne.setText("1");
                mBtnTwo.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                mBtnTwo.setAlpha(0.4f);
                mBtnTwo.setText("2");
                break;
        }


    }


}
