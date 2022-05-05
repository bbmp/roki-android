package com.robam.roki.ui.page.device.steamovenone;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.TimeUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.NewSteamOvenOneAlarm2Event;
import com.robam.common.events.SteamOvenOneAlarmEvent;
import com.robam.common.events.SteamOvenOneAlarmEvent2;
import com.robam.common.events.SteamOvenOneWaterChangesEvent;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneModel;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneWorkStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.utils.DateUtil;
import com.robam.roki.utils.DialogUtil;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by Dell on 2018/7/12.
 */

public class AbsSteamOvenWorking610View extends FrameLayout {

    Context cx;
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
    @InjectView(R.id.btn_one)
    Button mBtnOne;
    @InjectView(R.id.btn_two)
    Button mBtnTwo;
    @InjectView(R.id.btn_there)
    Button mBtnThere;
    @InjectView(R.id.ll_btn)
    LinearLayout mLlBtn;
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
    @InjectView(R.id.finish_img)
    ImageView finishImg;
    @InjectView(R.id.finish_name)
    TextView finishName;
    @InjectView(R.id.fl_run_stop)
    FrameLayout flRunStop;
    @InjectView(R.id.normal_show)
    FrameLayout normalShow;
    @InjectView(R.id.exp_show)
    FrameLayout expShow;
    @InjectView(R.id.exp_model_content)
    TextView expModelContent;
    @InjectView(R.id.exp_temp_content1)
    TextView expTempContent1;
    @InjectView(R.id.exp_temp_content2)
    TextView expTempContent2;
    @InjectView(R.id.exp_time_content)
    TextView expTimeContent;
    @InjectView(R.id.exp_model)
    TextView expModel;
    @InjectView(R.id.exp_temp1)
    TextView expTemp1;
    @InjectView(R.id.exp_temp2)
    TextView expTemp2;
    @InjectView(R.id.exp_time)
    TextView expTime;
    //    @InjectView(R.id.ll)
//    RelativeLayout ll;
    @InjectView(R.id.left_img)
    ImageView mLeftImg;
    @InjectView(R.id.left_name)
    TextView mLeftName;
    @InjectView(R.id.fl_left)
    FrameLayout mFlLeft;
    @InjectView(R.id.local_recipe)
    TextView localRecipe;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_local_recipe)
    TextView tvLocalRecipe;
    @InjectView(R.id.fl_pause)
    FrameLayout flPause;
    @InjectView(R.id.iv_pause)
    ImageView ivPause;
    @InjectView(R.id.tv_pause)
    TextView tvPause;
    @InjectView(R.id.oven_complete)
    FrameLayout ovenComplete;
    @InjectView(R.id.fl_auto_step)
    FrameLayout flAutoStep;
    @InjectView(R.id.tv_auto_step)
    TextView tvAutoStep;

    private List<DeviceConfigurationFunctions> bgFunList;
    private List<DeviceConfigurationFunctions> subFunList;
    private List<DeviceConfigurationFunctions> ovenRunListDown;
    private List<DeviceConfigurationFunctions> steamRunListDown;
    String localRecipeParams;

    AbsSteameOvenOne steameOvenOne;
    boolean isSpecial;

    public AbsSteamOvenWorking610View(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.cx = context;
        initView();
    }

    public AbsSteamOvenWorking610View(Context context, List<DeviceConfigurationFunctions> bgFunList, AbsSteameOvenOne steameOvenOne) {
        super(context);
        this.cx = context;
        this.bgFunList = bgFunList;
        this.steameOvenOne = steameOvenOne;
        initView();
    }

    public AbsSteamOvenWorking610View(Context context, List<DeviceConfigurationFunctions> bgFunList, AbsSteameOvenOne steameOvenOne, String localRecipeParams) {
        super(context);
        this.cx = context;
        this.bgFunList = bgFunList;
        this.steameOvenOne = steameOvenOne;
        this.localRecipeParams = localRecipeParams;
        initView();
    }

    public AbsSteamOvenWorking610View(Context context, List<DeviceConfigurationFunctions> bgFunList, AbsSteameOvenOne steameOvenOne, String localRecipeParams, boolean isSpecial) {
        super(context);
        this.cx = context;
        this.bgFunList = bgFunList;
        this.steameOvenOne = steameOvenOne;
        this.localRecipeParams = localRecipeParams;
        this.isSpecial = isSpecial;
        initView();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventUtils.regist(this);
    }

    @Subscribe
    public void onEvent(SteamOvenOneWaterChangesEvent event) {

        if (steameOvenOne == null || !Objects.equal(steameOvenOne.getID(), event.steameOvenOne.getID())) {
            return;
        }
        short waterStatus = event.steameOvenOne.SteameOvenWaterChanges;
        LogUtils.i("202010231738", "waterStatus::" + waterStatus);
        if (waterStatus == 1) {
            ToastUtils.showLong(R.string.device_alarm_water_out);
        }

    }

    public void initView() {
        View view = LayoutInflater.from(cx).inflate(R.layout.abs_steamoven_610_working_page, this, true);
        ScreenAdapterTools.getInstance().loadView(view);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
        try {
            startAnimation();
            //上面布局展示
            if (steameOvenOne.workModel == SteamOvenOneWorkStatus.NoStatus) return;
            //是否是本地自动菜谱模式
            tvLocalRecipe.setText("");
            if (steameOvenOne.AutoRecipeModeValue == 0) {
                if (steameOvenOne.workModel == SteamOvenOneModel.EXP) {
                    expShow();
                } else {
                    normalShow();
                }

            } else {
                localAutoRecipe();
            }

            //下面布局展示
            updateStatus(steameOvenOne, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void closeAllDialog() {
        if (closedialog != null && closedialog.isShow()) {
            closedialog.dismiss();
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

    private void expShow() {
        numStep();
        for (int i = 0; i < bgFunList.size(); i++) {
            if ("runTimeUpView".equals(bgFunList.get(i).functionCode)) {
                subFunList = bgFunList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
            } else if ("roastRunTimeDownView".equals(bgFunList.get(i).functionCode)) {
                ovenRunListDown = bgFunList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
            }
        }

        for (int i = 0; i < ovenRunListDown.size(); i++) {
            if ("waterTank".equals(ovenRunListDown.get(i).functionCode)) {
                if (steameOvenOne.WaterStatus == 0) {
                    Glide.with(cx).load(ovenRunListDown.get(i).backgroundImg).into(mLeftImg);
                } else {
                    Glide.with(cx).load(ovenRunListDown.get(i).backgroundImg).into(mLeftImg);
                }
                mLeftName.setText(ovenRunListDown.get(i).functionName);
            } else if ("finish".equals(ovenRunListDown.get(i).functionCode)) {
                Glide.with(cx).load(ovenRunListDown.get(i).backgroundImg).into(finishImg);
                finishName.setText(ovenRunListDown.get(i).functionName);
            } else if ("wait".equals(ovenRunListDown.get(i).functionCode)) {
                if ("预约中".equals(tvWorkDec.getText().toString())) {
                    Glide.with(cx).load(ovenRunListDown.get(i).backgroundImg).into(ivPause);
                    tvPause.setText("立即启动");
                } else {
                    if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
                        Glide.with(cx).load(ovenRunListDown.get(i).backgroundImg).into(ivPause);
                        tvPause.setText(ovenRunListDown.get(i).functionName);
                    } else if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.Pause) {
                        Glide.with(cx).load(R.drawable.start_610).into(ivPause);
                        tvPause.setText("继续");
                    }
                }
            }
        }
        tvLocalRecipe.setText("");
        tvAutoStep.setText("");
        normalShow.setVisibility(View.INVISIBLE);
        expShow.setVisibility(View.VISIBLE);
        flPause.setVisibility(VISIBLE);
//        mLlBtn.setVisibility(INVISIBLE);
    }


    private String code2Name(String mode) throws JSONException {
        String name = "";
        for (int i = 0; i < subFunList.size(); i++) {
            if ("model".equals(subFunList.get(i).functionCode)) {
                String params = subFunList.get(i).functionParams;
                JSONObject jsonObject = new JSONObject(params);
                JSONObject param = jsonObject.getJSONObject("param");
                name = param.getJSONObject(mode).getString("value");
            }
        }
        return name;

    }


    private void normalShow() {
        try {
            numStep();
            if (steameOvenOne.AutoRecipeModeValue == 0) {
                numStepOhterType();
            }
            expShow.setVisibility(View.INVISIBLE);
            normalShow.setVisibility(View.VISIBLE);

//            if (SteamOvenOneModel.GANZAO == steameOvenOne.workModel
//                    || SteamOvenOneModel.QINGJIE == steameOvenOne.workModel
//                    || SteamOvenOneModel.CHUGO == steameOvenOne.workModel) {
//
//                normalShow.setVisibility(INVISIBLE);
//                localRecipe.setVisibility(VISIBLE);
//                if (steameOvenOne.workModel == SteamOvenOneModel.GANZAO) {
//                    flAutoStep.setVisibility(INVISIBLE);
//                    mFlLeft.setVisibility(INVISIBLE);
//                    if (TextUtils.equals(steameOvenOne.getDt(), "CQ926")){
//                        flPause.setVisibility(GONE);
//                    } else if (TextUtils.equals(steameOvenOne.getDt(), "DB610")){
//                        flPause.setVisibility(GONE);
//                    }else {
//                        flPause.setVisibility(VISIBLE);
//                    }
//                } else if (steameOvenOne.workModel == SteamOvenOneModel.CHUGO) {
//                    mFlLeft.setVisibility(INVISIBLE);
//                    flPause.setVisibility(GONE);
//                } else if (steameOvenOne.workModel == SteamOvenOneModel.QINGJIE) {
//                    mFlLeft.setVisibility(INVISIBLE);
//                    flPause.setVisibility(GONE);
//                } else {
//                    mFlLeft.setVisibility(VISIBLE);
//                }
//            } else {
            if (steameOvenOne.workModel == SteamOvenOneModel.CHUGO) {
                mFlLeft.setVisibility(INVISIBLE);
                normalShow.setVisibility(View.INVISIBLE);
                flPause.setVisibility(GONE);
                flRunStop.setVisibility(GONE);
                localRecipe.setVisibility(VISIBLE);
                localRecipe.setText("除垢");
            } else {
                flAutoStep.setVisibility(INVISIBLE);
                localRecipe.setVisibility(INVISIBLE);
                normalShow.setVisibility(VISIBLE);
                mFlLeft.setVisibility(VISIBLE);
                flPause.setVisibility(VISIBLE);
            }

//            }

            if (SteamOvenOneModel.FAXIAO == steameOvenOne.workModel
                    || SteamOvenOneModel.XIANNENZHENG == steameOvenOne.workModel
                    || SteamOvenOneModel.YINGYANGZHENG == steameOvenOne.workModel
                    || SteamOvenOneModel.GAOWENZHENG == steameOvenOne.workModel
                    || SteamOvenOneModel.GANZAO == steameOvenOne.workModel
                    || SteamOvenOneModel.BAOWEN == steameOvenOne.workModel
                    || SteamOvenOneModel.CHUGO == steameOvenOne.workModel) {
                for (int i = 0; i < bgFunList.size(); i++) {
                    if ("runTimeUpView".equals(bgFunList.get(i).functionCode)) {
                        subFunList = bgFunList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                    } else if ("steamRunTimeDownView".equals(bgFunList.get(i).functionCode)) {
                        steamRunListDown = bgFunList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                    }
                }

            } else {
                for (int i = 0; i < bgFunList.size(); i++) {
                    if ("runTimeUpView".equals(bgFunList.get(i).functionCode)) {
                        subFunList = bgFunList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                    } else if ("roastRunTimeDownView".equals(bgFunList.get(i).functionCode)) {
                        ovenRunListDown = bgFunList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                    }
                }
//                tvLocalRecipe.setText(code2Name(String.valueOf(steameOvenOne.workModel)));

            }


            for (int i = 0; i < subFunList.size(); i++) {
                if ("model".equals(subFunList.get(i).functionCode)) {
                    tvModel.setText(subFunList.get(i).functionName);
                    //tvTitle.setText(subFunList.get(i).functionName);
                } else if ("temperature".equals(subFunList.get(i).functionCode)) {
                    tvTemp.setText(subFunList.get(i).functionName);
                } else if ("remainingTime".equals(subFunList.get(i).functionCode)) {
                    tvTime.setText(subFunList.get(i).functionName);
                }
            }

            if (steamRunListDown != null && steamRunListDown.size() > 0) {
                for (int i = 0; i < steamRunListDown.size(); i++) {
                    if ("finish".equals(steamRunListDown.get(i).functionCode)) {
                        Glide.with(cx).load(steamRunListDown.get(i).backgroundImg).into(finishImg);
                        finishName.setText(steamRunListDown.get(i).functionName);
                    } else if ("waterTank".equals(steamRunListDown.get(i).functionCode)) {

                        if (steameOvenOne.WaterStatus == 0) {
                            Glide.with(cx).load(steamRunListDown.get(i).backgroundImg).into(mLeftImg);
                        } else {
                            Glide.with(cx).load(steamRunListDown.get(i).backgroundImg).into(mLeftImg);
                        }
                        mLeftName.setText(steamRunListDown.get(i).functionName);
                    } else if ("wait".equals(steamRunListDown.get(i).functionCode)) {
                        if ("预约中".equals(tvWorkDec.getText().toString())) {
                            Glide.with(cx).load(ovenRunListDown.get(i).backgroundImg).into(ivPause);
                            tvPause.setText("立即启动");
                        } else {
                            if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
                                Glide.with(cx).load(steamRunListDown.get(i).backgroundImg).into(ivPause);
                                tvPause.setText(steamRunListDown.get(i).functionName);
                            } else if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.Pause) {
                                Glide.with(cx).load(steamRunListDown.get(i).backgroundImgH).into(ivPause);
                                tvPause.setText("继续");
                            }
                        }
                    }
                }
            }

            if (ovenRunListDown != null && ovenRunListDown.size() > 0) {
                for (int i = 0; i < ovenRunListDown.size(); i++) {
                    if ("waterTank".equals(ovenRunListDown.get(i).functionCode)) {
                        if (steameOvenOne.WaterStatus == 0) {
                            Glide.with(cx).load(ovenRunListDown.get(i).backgroundImg).into(mLeftImg);
                        } else {
                            Glide.with(cx).load(ovenRunListDown.get(i).backgroundImgH).into(mLeftImg);
                        }
                        mLeftName.setText(ovenRunListDown.get(i).functionName);
                    } else if ("finish".equals(ovenRunListDown.get(i).functionCode)) {
                        Glide.with(cx).load(ovenRunListDown.get(i).backgroundImg).into(finishImg);
                        finishName.setText(ovenRunListDown.get(i).functionName);
                    } else if ("wait".equals(ovenRunListDown.get(i).functionCode)) {

                        if ("预约中".equals(tvWorkDec.getText().toString())) {
                            Glide.with(cx).load(ovenRunListDown.get(i).backgroundImgH).into(ivPause);
                            tvPause.setText("立即启动");
                        } else {
//                            Glide.with(cx).load(ovenRunListDown.get(i).backgroundImg).into(ivPause);
//                            tvPause.setText(ovenRunListDown.get(i).functionName);
                            if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
                                Glide.with(cx).load(ovenRunListDown.get(i).backgroundImg).into(ivPause);
                                tvPause.setText(ovenRunListDown.get(i).functionName);
                            } else if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.Pause) {
                                Glide.with(cx).load(ovenRunListDown.get(i).backgroundImgH).into(ivPause);
                                tvPause.setText("继续");
                            }
                        }
                    }

                }
            }
            LogUtils.i("202010221403", "workModel0:::" + steameOvenOne.workModel);
            showDescalingText();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showDescalingText() {
        LogUtils.i("202010221403", "workModel1:::" + steameOvenOne.workModel);
        if (steameOvenOne.workModel != SteamOvenOneModel.CHUGO && steameOvenOne.workModel != SteamOvenOneModel.QINGJIE) {

            LogUtils.i("202010221403", "workModel2:::" + steameOvenOne.workModel);
            return;
        }
        try {
            tvAutoStep.setVisibility(View.VISIBLE);
            LogUtils.i("202012071426", "steameOvenOne.CpStepValue:::" + steameOvenOne.CpStepValue);
            for (int i = 0; i < subFunList.size(); i++) {
                if ("model".equals(subFunList.get(i).functionCode)) {
                    String params = subFunList.get(i).functionParams;
                    JSONObject jsonObject = new JSONObject(params);
                    JSONObject param = jsonObject.getJSONObject("param");
                    JSONObject obj = param.getJSONObject(String.valueOf(steameOvenOne.workModel));
                    String functionParams = obj.getString("functionParams");
                    JSONObject object = new JSONObject(functionParams);
                    JSONArray multiSegment = object.getJSONArray("multiSegment");
                    for (int j = 0; j < multiSegment.length(); j++) {
                        //第一阶段
                        if (steameOvenOne.CpStepValue == 1) {
                            flAutoStep.setVisibility(View.VISIBLE);
                            tvAutoStep.setText(multiSegment.optString(0) == null ? "" : multiSegment.optString(0));
                            tvWorkDec.setVisibility(View.VISIBLE);
                            tvWorkDec.setText("工作中");
//                            tvWorkDec.setTextSize(36);


                            mLlBtn.setVisibility(View.VISIBLE);
                            mBtnOne.setVisibility(View.INVISIBLE);
                            mBtnTwo.setVisibility(View.INVISIBLE);
                            mBtnThere.setVisibility(View.INVISIBLE);

                            descalingModeShow(steameOvenOne.CpStepValue);
                            //第一阶段完成
                        } else if (steameOvenOne.CpStepValue == 11) {
                            flAutoStep.setVisibility(View.VISIBLE);
                            tvAutoStep.setText(multiSegment.optString(3) == null ? "" : multiSegment.optString(3));

                            tvWorkDec.setVisibility(View.VISIBLE);
//                            tvWorkDec.setTextSize(15);
                            tvWorkDec.setText("暂停中");

                            mLlBtn.setVisibility(View.VISIBLE);
                            mBtnOne.setVisibility(View.INVISIBLE);
                            mBtnTwo.setVisibility(View.INVISIBLE);
                            mBtnThere.setVisibility(View.INVISIBLE);
                            descalingModeShow(steameOvenOne.CpStepValue);
                            //第二阶段
                        } else if (steameOvenOne.CpStepValue == 2) {

                            mLlBtn.setVisibility(View.VISIBLE);
                            flAutoStep.setVisibility(View.VISIBLE);
                            tvAutoStep.setText(multiSegment.optString(1) == null ? "" : multiSegment.optString(1));

                            tvWorkDec.setVisibility(View.VISIBLE);
                            tvWorkDec.setText("工作中");
//                            tvWorkDec.setTextSize(36);
                            mBtnOne.setVisibility(View.VISIBLE);
                            mBtnTwo.setVisibility(View.INVISIBLE);
                            mBtnThere.setVisibility(View.VISIBLE);
                            descalingModeShow(steameOvenOne.CpStepValue);
                            //第二阶段完成
                        } else if (steameOvenOne.CpStepValue == 21) {

                            mLlBtn.setVisibility(View.VISIBLE);
                            flAutoStep.setVisibility(View.VISIBLE);
                            tvAutoStep.setText(multiSegment.optString(3) == null ? "" : multiSegment.optString(3));

                            tvWorkDec.setVisibility(View.VISIBLE);
//                            tvWorkDec.setTextSize(36);
                            tvWorkDec.setText("暂停中");


                            mBtnOne.setVisibility(View.VISIBLE);
                            mBtnTwo.setVisibility(View.INVISIBLE);
                            mBtnThere.setVisibility(View.VISIBLE);
                            descalingModeShow(steameOvenOne.CpStepValue);
                            //第三阶段
                        } else if (steameOvenOne.CpStepValue == 3) {
                            mLlBtn.setVisibility(View.VISIBLE);
                            flAutoStep.setVisibility(View.VISIBLE);
                            tvAutoStep.setText(multiSegment.optString(2) == null ? "" : multiSegment.optString(2));

                            tvWorkDec.setVisibility(View.VISIBLE);
                            tvWorkDec.setText("工作中");
//                            tvWorkDec.setTextSize(36);
                            mBtnOne.setVisibility(View.VISIBLE);
                            mBtnTwo.setVisibility(View.VISIBLE);
                            mBtnThere.setVisibility(View.VISIBLE);
                            descalingModeShow(steameOvenOne.CpStepValue);
                        } else {
                            LogUtils.i("202012071426", "steameOvenOne.CpStepValue::::" + steameOvenOne.CpStepValue);
                            ovenComplete.setVisibility(VISIBLE);
                            flAutoStep.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.i("202010221403", "error:::" + e.getMessage());
        }
    }


    private void descalingModeShow(short status) {
        mLlBtn.setVisibility(View.VISIBLE);
        switch (status) {
            case 1:
            case 11:
                if (steameOvenOne.multiSumStep == 2) {
                    mBtnOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn_bg));
                    mBtnOne.setAlpha(1);
                    mBtnOne.setText("1");
                    mBtnThere.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                    mBtnThere.setAlpha(0.4f);
                    mBtnThere.setText("2");
                    mBtnTwo.setVisibility(INVISIBLE);
                    mBtnOne.setVisibility(VISIBLE);
                    mBtnThere.setVisibility(VISIBLE);
                } else {
                    mBtnOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn_bg));
                    mBtnOne.setAlpha(1);
                    mBtnOne.setText("1");
                    mBtnTwo.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                    mBtnTwo.setAlpha(0.4f);
                    mBtnTwo.setText("2");
                    mBtnThere.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                    mBtnThere.setAlpha(0.4f);
                    mBtnThere.setText("3");
                    mBtnTwo.setVisibility(VISIBLE);
                    mBtnOne.setVisibility(VISIBLE);
                    mBtnThere.setVisibility(VISIBLE);
                }
                break;
            case 2:
            case 21:
                if (steameOvenOne.multiSumStep == 2) {
                    mBtnTwo.setVisibility(INVISIBLE);
                    mBtnOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                    mBtnOne.setAlpha(0.4f);
                    mBtnOne.setText("1");
                    mBtnThere.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn_bg));
                    mBtnThere.setAlpha(0.4f);
                    mBtnThere.setText("2");
                    mBtnTwo.setVisibility(INVISIBLE);
                    mBtnOne.setVisibility(VISIBLE);
                    mBtnThere.setVisibility(VISIBLE);
                } else {
                    mBtnTwo.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn_bg));
                    mBtnTwo.setAlpha(1);
                    mBtnTwo.setText("2");
                    mBtnOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                    mBtnOne.setAlpha(0.4f);
                    mBtnOne.setText("1");
                    mBtnThere.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                    mBtnThere.setAlpha(0.4f);
                    mBtnThere.setText("3");
                    mBtnTwo.setVisibility(VISIBLE);
                    mBtnOne.setVisibility(VISIBLE);
                    mBtnThere.setVisibility(VISIBLE);
                }
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


    //本地自动菜谱模式
    private void localAutoRecipe() {
        tvAutoStep.setVisibility(View.GONE);
        mLlBtn.setVisibility(View.INVISIBLE);
        mFlLeft.setVisibility(VISIBLE);
        expShow.setVisibility(View.INVISIBLE);
        normalShow.setVisibility(View.INVISIBLE);
        localRecipe.setVisibility(VISIBLE);
        //flPause.setVisibility(VISIBLE);
        try {
            JSONObject jsonObject = new JSONObject(localRecipeParams);
            JSONObject obj = jsonObject.getJSONObject(steameOvenOne.AutoRecipeModeValue + "");
            String pKey = obj.getString("pKey");
            String value = obj.getString("value");
//            tvLocalRecipe.setText(pKey + " " + value);
            localRecipe.setText(pKey + " " + value);


            for (int i = 0; i < bgFunList.size(); i++) {
                if ("runTimeUpView".equals(bgFunList.get(i).functionCode)) {
                    subFunList = bgFunList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                } else if ("steamRunTimeDownView".equals(bgFunList.get(i).functionCode)) {
                    steamRunListDown = bgFunList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                }
            }


            if (steamRunListDown != null && steamRunListDown.size() > 0) {
                for (int i = 0; i < steamRunListDown.size(); i++) {
                    if ("finish".equals(steamRunListDown.get(i).functionCode)) {
                        Glide.with(cx).load(steamRunListDown.get(i).backgroundImg).into(finishImg);
                        finishName.setText(steamRunListDown.get(i).functionName);
                    } else if ("waterTank".equals(steamRunListDown.get(i).functionCode)) {
                        if (steameOvenOne.WaterStatus == 0) {
                            Glide.with(cx).load(steamRunListDown.get(i).backgroundImg).into(mLeftImg);
                        } else {
                            Glide.with(cx).load(steamRunListDown.get(i).backgroundImgH).into(mLeftImg);
                        }
                        mLeftName.setText(steamRunListDown.get(i).functionName);

                    } else if ("wait".equals(steamRunListDown.get(i).functionCode)) {
                        Glide.with(cx).load(steamRunListDown.get(i).backgroundImg).into(ivPause);
                        tvPause.setText(steamRunListDown.get(i).functionName);
                        if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
                            Glide.with(cx).load(steamRunListDown.get(i).backgroundImg).into(ivPause);
                            tvPause.setText(steamRunListDown.get(i).functionName);
                        } else if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.Pause) {
                            Glide.with(cx).load(steamRunListDown.get(i).backgroundImgH).into(ivPause);
                            tvPause.setText("继续");
                        }
                    }


                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    protected void completeWork() {
        complete();
    }


    public void updateStatus(AbsSteameOvenOne steameOvenOne, boolean completeSign) {
        if (this.steameOvenOne != null && !this.steameOvenOne.getGuid().getGuid().equals(steameOvenOne.getGuid().getGuid())){
            return;
        }
        this.steameOvenOne = steameOvenOne;
        if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.AlarmStatus) {
            return;
        }
        if (completeSign) {
            complete();
            return;
        }
        if (steameOvenOne.AutoRecipeModeValue == 0) {
            if (steameOvenOne.workModel == SteamOvenOneModel.EXP) {
                expShow();
                commonExpMode();
            } else {
                normalShow();
                commonMode();
            }
        } else {
            localAutoRecipe();
        }

        if (steameOvenOne.worknStatus == SteamOvenOneWorkStatus.PreHeat &&
                steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
            heatMode();
        } else if (steameOvenOne.worknStatus == SteamOvenOneWorkStatus.Working &&
                steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
            runMode();
        } else if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.Pause &&
                steameOvenOne.powerStatus == SteamOvenOnePowerStatus.On) {
            LogUtils.i("202010231621", steameOvenOne.powerOnStatus + "");
            pauseMode();
        } else if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.Order) {
            orderMode();
        }

    }

    //工作中
    private void runMode() {
        mFlLeft.setVisibility(View.INVISIBLE);
//        if (SteamOvenOneModel.GANZAO == steameOvenOne.workModel ) {
//            if (SteamOvenOneModel.GANZAO == steameOvenOne.workModel) {
//                flPause.setVisibility(View.VISIBLE);
////                tvWorkStateName.setTextSize(20);
//                tvWorkStateName.setVisibility(View.VISIBLE);
//                tvWorkStateName.setText("工作剩余时间");
//            } else {
////                tvWorkStateName.setTextSize(36);
//                tvWorkStateName.setVisibility(View.VISIBLE);
//                JSONObject obj = null;
//                try {
//                    obj = new JSONObject(subFunList.get(0).functionParams);
//                    String param = (String) obj.getJSONObject("param").getJSONObject(steameOvenOne.workModel + "").get("value");
//                    tvWorkStateName.setText(param);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            flPause.setVisibility(View.GONE);
//            ovenComplete.setVisibility(View.INVISIBLE);
//            flRunStop.setVisibility(View.VISIBLE);
//            String minSec = TimeUtils.secToHourMinSec(steameOvenOne.unShortLeftTime);
////            tvWorkDec.setTextSize(36);
//            tvWorkDec.setText(minSec);
//
//        } else if (SteamOvenOneModel.CHUGO == steameOvenOne.workModel || SteamOvenOneModel.QINGJIE == steameOvenOne.workModel) {
//            ovenComplete.setVisibility(View.INVISIBLE);
//            flPause.setVisibility(View.GONE);
//            flRunStop.setVisibility(View.VISIBLE);
//            tvWorkStateName.setVisibility(GONE);
//        } else {
        if (steameOvenOne.workModel == SteamOvenOneModel.CHUGO) {
            mFlLeft.setVisibility(INVISIBLE);
            normalShow.setVisibility(View.INVISIBLE);
            flPause.setVisibility(GONE);
            localRecipe.setVisibility(VISIBLE);
            localRecipe.setText("除垢");
            flRunStop.setVisibility(GONE);
            ovenComplete.setVisibility(View.INVISIBLE);
//            flRunStop.setVisibility(View.VISIBLE);
            tvWorkStateName.setVisibility(View.VISIBLE);
            tvWorkDec.setVisibility(VISIBLE);
//            tvWorkStateName.setText("工作剩余时间");
//            String minSec = TimeUtils.secToHourMinSec(steameOvenOne.unShortLeftTime);
//            tvWorkDec.setText(minSec);

            tvWorkDec.setText("工作中");
            tvWorkStateName.setText("");

        } else {
            ovenComplete.setVisibility(View.INVISIBLE);
            flPause.setVisibility(View.VISIBLE);
            flRunStop.setVisibility(View.VISIBLE);
            tvWorkStateName.setVisibility(View.VISIBLE);
            tvWorkDec.setVisibility(VISIBLE);
            tvWorkStateName.setText("工作剩余时间");
            String minSec = TimeUtils.secToHourMinSec(steameOvenOne.unShortLeftTime);
            tvWorkDec.setText(minSec);
        }
//        ovenComplete.setVisibility(View.INVISIBLE);
//        flPause.setVisibility(View.VISIBLE);
//        flRunStop.setVisibility(View.VISIBLE);
//        tvWorkStateName.setVisibility(View.VISIBLE);
//        tvWorkStateName.setText("工作剩余时间");
//        String minSec = TimeUtils.secToHourMinSec(steameOvenOne.unShortLeftTime);
//        tvWorkDec.setText(minSec);
//        }
    }


    //完成
    private void complete() {
        tvWorkStateName.setVisibility(View.INVISIBLE);
        flRunStop.setVisibility(View.INVISIBLE);
//        tvWorkDec.setTextSize(36);
        tvWorkDec.setText("完成");
        mFlLeft.setVisibility(View.GONE);
        flPause.setVisibility(View.GONE);

        if (SteamOvenOneModel.CHUGO == steameOvenOne.workModel || SteamOvenOneModel.QINGJIE == steameOvenOne.workModel) {
            flAutoStep.setVisibility(INVISIBLE);
        }
        ovenComplete.setVisibility(View.VISIBLE);
        stopAnimation();
    }

    private void commonMode() {
        tvTempContent.setText(steameOvenOne.setTemp + "℃");
        if (steameOvenOne.setTimeH > 0) {
            byte low = (byte) steameOvenOne.setTime;
            byte high = (byte) steameOvenOne.setTimeH;
            byte[] bytes = new byte[2];
            bytes[0] = high;
            bytes[1] = low;
            String time = StringUtils.bytes2Hex(bytes).trim();
            time = time.replace(" ", "");
            tvTimeContent.setText(new BigInteger(time, 16) + "min");
        } else {
            tvTimeContent.setText(steameOvenOne.setTime + "min");
        }
        JSONObject obj = null;
        try {
            obj = new JSONObject(subFunList.get(0).functionParams);
            String param = (String) obj.getJSONObject("param").getJSONObject(steameOvenOne.workModel + "").get("value");
            tvModelContent.setText(param);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void commonExpMode() {
        expTempContent1.setText(steameOvenOne.setTemp + "℃");
        expTempContent2.setText(steameOvenOne.setTempDownValue + "℃");
        expTimeContent.setText(steameOvenOne.setTime + "min");
        JSONObject obj = null;
        try {
            obj = new JSONObject(subFunList.get(0).functionParams);
            String param = (String) obj.getJSONObject("param").getJSONObject(steameOvenOne.workModel + "").get("value");
            expModelContent.setText(param);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //暂停
    private void pauseMode() {
        if (SteamOvenOneModel.CHUGO == steameOvenOne.workModel || steameOvenOne.workModel == SteamOvenOneModel.QINGJIE) {
            mFlLeft.setVisibility(View.GONE);
            flPause.setVisibility(View.GONE);
            flRunStop.setVisibility(View.GONE);
        } else {
            mFlLeft.setVisibility(View.VISIBLE);
            flPause.setVisibility(View.VISIBLE);
            flRunStop.setVisibility(View.VISIBLE);
        }
        ovenComplete.setVisibility(View.INVISIBLE);
        tvWorkStateName.setVisibility(View.VISIBLE);

        tvWorkStateName.setText("暂停中");
        String minSec = TimeUtils.secToHourMinSec(steameOvenOne.unShortLeftTime);
        if (steameOvenOne.workModel == SteamOvenOneModel.QINGJIE || steameOvenOne.workModel == SteamOvenOneModel.CHUGO) {
            tvWorkDec.setVisibility(VISIBLE);
            tvWorkDec.setText("暂停中");
            tvWorkStateName.setVisibility(INVISIBLE);

        } else {
            tvWorkDec.setVisibility(VISIBLE);
            tvWorkDec.setText(minSec);
        }

    }

    //预热
    private void heatMode() {
        ovenComplete.setVisibility(View.INVISIBLE);
        if (SteamOvenOneModel.GANZAO == steameOvenOne.workModel || SteamOvenOneModel.CHUGO == steameOvenOne.workModel) {
            flPause.setVisibility(View.GONE);
            flRunStop.setVisibility(View.GONE);
        } else {
            flPause.setVisibility(View.VISIBLE);
            flRunStop.setVisibility(View.VISIBLE);
        }

        tvWorkDec.setVisibility(View.VISIBLE);
        tvWorkStateName.setVisibility(VISIBLE);
//        tvWorkStateName.setTextSize(20);
        tvWorkDec.setText("预热中");
//        tvWorkDec.setTextSize(36);
        tvWorkStateName.setText(steameOvenOne.temp + "℃");
        mFlLeft.setVisibility(View.INVISIBLE);
        if (SteamOvenOneModel.CHUGO == steameOvenOne.workModel){
            tvWorkDec.setText("工作中");
            tvWorkStateName.setText("");
        }
    }

    //预约
    private void orderMode() {
        ovenComplete.setVisibility(View.INVISIBLE);
        if (SteamOvenOneModel.GANZAO == steameOvenOne.workModel || SteamOvenOneModel.CHUGO == steameOvenOne.workModel) {
            flPause.setVisibility(View.GONE);
        } else {
            flPause.setVisibility(View.VISIBLE);
        }
        mFlLeft.setVisibility(View.GONE);
        flRunStop.setVisibility(View.VISIBLE);
        tvWorkDec.setVisibility(View.VISIBLE);
        tvWorkStateName.setVisibility(VISIBLE);
//        tvWorkStateName.setTextSize(36);
        tvWorkDec.setText("预约中");
        tvPause.setText("立即开始");
        Glide.with(cx).load(R.drawable.start_610).into(ivPause);
//        tvWorkDec.setTextSize(18);
        if (steameOvenOne.order_left_time_hour >= 0 && steameOvenOne.order_left_time_hour <= 9) {
            long data = DateUtil.currentTimestampLong();
//            long l = data + steameOvenOne.ordertime_hour * 3600000 + steameOvenOne.ordertime_min * 60000;
//            long l = data + steameOvenOne.order_left_time_hour * 3600000 + steameOvenOne.order_left_time_min * 60000;
//            String time = DateUtil.getFormatedDateTime(DateUtil.PATTERN, l);
            if (steameOvenOne.order_left_time_min >= 0 && steameOvenOne.order_left_time_min <= 9) {

                tvWorkStateName.setText("剩余时间" + "0" + steameOvenOne.order_left_time_hour + ":" + "0" + steameOvenOne.order_left_time_min + ":00");
//                tvWorkStateName.setText("开始时间" + time);
            } else {
                tvWorkStateName.setText("剩余时间" + "0" + steameOvenOne.order_left_time_hour + ":" + steameOvenOne.order_left_time_min+ ":00");
//                tvWorkStateName.setText("开始时间" + time);
            }
        } else {
            long data = DateUtil.currentTimestampLong();
//            long l = data + steameOvenOne.ordertime_hour * 3600000 + steameOvenOne.ordertime_min * 60000;
//            long l = data + steameOvenOne.order_left_time_hour * 3600000 + steameOvenOne.order_left_time_min * 60000;
//            String time = DateUtil.getFormatedDateTime(DateUtil.PATTERN, l);
            if (steameOvenOne.order_left_time_min > 9) {
                tvWorkStateName.setText("剩余时间" + steameOvenOne.order_left_time_hour + ":" + steameOvenOne.order_left_time_min+ ":00");
//                tvWorkStateName.setText("开始时间" + time);
            } else {
                tvWorkStateName.setText("剩余时间" + steameOvenOne.order_left_time_hour + ":" + "0" + steameOvenOne.order_left_time_min+ ":00");
//                tvWorkStateName.setText("开始时间" + time);
            }

        }
    }

    IRokiDialog closedialog = null;


    @OnClick(R.id.fl_run_stop)
    public void finishWork() {
        LogUtils.i("2020092201", "steameOvenOne.powerStatus::" + steameOvenOne.powerStatus);
        closedialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        closedialog.setTitleText(R.string.close_work);
        closedialog.setContentText(R.string.is_close_work);
        closedialog.show();

        closedialog.setOkBtn(R.string.ok_btn, new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (closedialog.isShow()) {
                    closedialog.dismiss();
                    if (steameOvenOne.powerStatus == SteamOvenOnePowerOnStatus.Order || steameOvenOne.powerStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
                        steameOvenOne.setSteameOvenStatus(SteamOvenOnePowerStatus.On, SteamOvenOnePowerOnStatus.OperatingState, null);
                    } else {
                        steameOvenOne.setSteameOvenStatus(SteamOvenOnePowerStatus.Off, SteamOvenOnePowerOnStatus.OperatingState, null);
                    }
                }
            }
        });

        closedialog.setCancelBtn(R.string.can_btn, new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (closedialog != null && closedialog.isShow()) {
                    closedialog.dismiss();
                }
            }
        });
    }

    @OnClick(R.id.fl_left)
    public void onViewClicked() {
//        if (isSpecial) {
//            if (steameOvenOne.WaterStatus == 1 && steameOvenOne.workModel >= 10 && SteamOvenOneModel.CHUGO != steameOvenOne.workModel) {
//                ToastUtils.showShort(R.string.device_alarm_water_out);
//                return;
//            }
//        } else {
        if (steameOvenOne.WaterStatus == 1) {
            ToastUtils.showShort(R.string.device_alarm_water_out);
            return;
        }
//        }


        if (steameOvenOne.WaterStatus == 0) {
            steameOvenOne.setSteameOvenOneWaterPop((short) 1, null);
        }

    }

    //多段烹饪步鄹(610)
    private void numStep() {
        if (steameOvenOne.workModel == SteamOvenOneModel.CHUGO) {
            mLlBtn.setVisibility(View.INVISIBLE);
            return;
        }
        if (steameOvenOne.MultiStepCookingStepsValue != 0) {
            mLlBtn.setVisibility(View.VISIBLE);
            if (steameOvenOne.MultiStepCookingStepsValue == 2) {
                mBtnOne.setVisibility(View.VISIBLE);
                mBtnTwo.setVisibility(View.INVISIBLE);
                mBtnThere.setVisibility(View.VISIBLE);
            } else if (steameOvenOne.MultiStepCookingStepsValue == 3) {
                mBtnOne.setVisibility(View.VISIBLE);
                mBtnTwo.setVisibility(View.VISIBLE);
                mBtnThere.setVisibility(View.VISIBLE);
            }
        } else {
//            if (steameOvenOne.workModel != SteamOvenOneModel.CHUGO) {
                mLlBtn.setVisibility(View.INVISIBLE);
//                mBtnOne.setVisibility(View.INVISIBLE);
//                mBtnTwo.setVisibility(View.INVISIBLE);
//                mBtnThere.setVisibility(View.INVISIBLE);
//            }

        }
        if (steameOvenOne.MultiStepCookingStepsValue == 2) {
            mBtnTwo.setVisibility(View.INVISIBLE);
            switch (steameOvenOne.MultiStepCurrentStepsValue) {
                case 1:
                    mBtnOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn_bg));
                    mBtnOne.setAlpha(1);
                    mBtnOne.setText("1");
                    mBtnThere.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                    mBtnThere.setAlpha(0.4f);
                    mBtnThere.setText("2");
                    break;
                case 2:
                    mBtnOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                    mBtnOne.setAlpha(0.4f);
                    mBtnOne.setText("1");
                    mBtnThere.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn_bg));
                    mBtnThere.setAlpha(1);
                    mBtnThere.setText("2");
                    break;
            }
        } else if (steameOvenOne.MultiStepCookingStepsValue == 3) {
            switch (steameOvenOne.MultiStepCurrentStepsValue) {

                case 1:
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


    //多段烹饪步骤（其他机型）
    private void numStepOhterType() {
        if (steameOvenOne.MultiStepCookingStepsValue != 0) {
            mLlBtn.setVisibility(View.VISIBLE);
            if (steameOvenOne.MultiStepCookingStepsValue == 1) {
                mBtnOne.setVisibility(View.INVISIBLE);
                mBtnTwo.setVisibility(View.INVISIBLE);
                mBtnThere.setVisibility(View.INVISIBLE);
            } else if (steameOvenOne.MultiStepCookingStepsValue == 2) {
                mBtnOne.setVisibility(View.VISIBLE);
                mBtnTwo.setVisibility(View.INVISIBLE);
                mBtnThere.setVisibility(View.VISIBLE);
            } else if (steameOvenOne.MultiStepCookingStepsValue == 3) {
                mBtnOne.setVisibility(View.VISIBLE);
                mBtnTwo.setVisibility(View.VISIBLE);
                mBtnThere.setVisibility(View.VISIBLE);
            }
        } else {
            if (steameOvenOne.workModel != SteamOvenOneModel.CHUGO) {
                mLlBtn.setVisibility(View.INVISIBLE);
                mBtnOne.setVisibility(View.INVISIBLE);
                mBtnTwo.setVisibility(View.INVISIBLE);
                mBtnThere.setVisibility(View.INVISIBLE);
            }


        }
        if (steameOvenOne.MultiStepCookingStepsValue == 2) {
            mBtnTwo.setVisibility(View.INVISIBLE);
            switch (steameOvenOne.MultiStepCurrentStepsValue) {
                case 1:
                    mBtnOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn_bg));
                    mBtnOne.setAlpha(1);
                    mBtnOne.setText("1");
                    mBtnThere.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                    mBtnThere.setAlpha(0.4f);
                    mBtnThere.setText("2");
                    break;
                case 2:
                    mBtnOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                    mBtnOne.setAlpha(0.4f);
                    mBtnOne.setText("1");
                    mBtnThere.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn_bg));
                    mBtnThere.setAlpha(1);
                    mBtnThere.setText("2");
                    break;
            }
        } else if (steameOvenOne.MultiStepCookingStepsValue == 3) {
            switch (steameOvenOne.MultiStepCurrentStepsValue) {

                case 1:
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


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventUtils.unregist(this);
        stopAnimation();
    }


    //暂停工作
    @OnClick(R.id.fl_pause)
    public void onPauseClickView() {
//        if (isSpecial) {
//            if (steameOvenOne.WaterStatus == 1 && (steameOvenOne.workModel > 12 && steameOvenOne.workModel < 23)) {
//                ToastUtils.showShort(R.string.device_alarm_water_out);
//                return;
//            }
//        }
//        if (steameOvenOne.doorStatusValue == 1 ) {
//            ToastUtils.show("门未关好，请检查并确认关好门", Toast.LENGTH_SHORT);
//            return;
//        }
        //工作中--> 暂停
        if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
            steameOvenOne.setSteameOvenStatus2(SteamOvenOnePowerStatus.On, SteamOvenOnePowerOnStatus.Pause, null);
            //暂停中-->开始
        } else if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.Pause) {
            if (isSpecial) {
                if (steameOvenOne.workModel == 13
                        || steameOvenOne.workModel == 14
                        || steameOvenOne.workModel == 15
                        || steameOvenOne.workModel == 16
                        || steameOvenOne.workModel == 20
                        || steameOvenOne.workModel == 21
                        || steameOvenOne.workModel == 22
                ) {
                    if (steameOvenOne.WaterStatus == 1 ) {
                        ToastUtils.showShort(R.string.device_alarm_water_out);
                        return;
                    }
                    if (steameOvenOne.alarm == 16 ) {
                        ToastUtils.showShort("水箱缺水，请加水");
                        return;
                    }
                }
            }
            if (steameOvenOne.doorStatusValue == 1  && steameOvenOne.workModel != 19) {
                ToastUtils.show("门未关好，请检查并确认关好门", Toast.LENGTH_SHORT);
                return;
            }
            steameOvenOne.setSteameOvenStatus2(SteamOvenOnePowerStatus.On, SteamOvenOnePowerOnStatus.WorkingStatus, null);
        } else if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.Order) {
            if (isSpecial) {
                if (steameOvenOne.WaterStatus == 1 && (steameOvenOne.workModel > 12 && steameOvenOne.workModel < 23)) {
                    ToastUtils.showShort(R.string.device_alarm_water_out);
                    return;
                }
            }
            if (steameOvenOne.doorStatusValue == 1 && steameOvenOne.workModel != 19) {
                ToastUtils.show("门未关好，请检查并确认关好门", Toast.LENGTH_SHORT);
                return;
            }
            steameOvenOne.setSteameOvenStatus2(SteamOvenOnePowerStatus.On, SteamOvenOnePowerOnStatus.WorkingStatus, null);
        }


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
//                    mAlarmSing = i;
                    break;
            }
        }
    }
    @Subscribe
    public void onEvent(NewSteamOvenOneAlarm2Event event) {

        if (steameOvenOne == null || !Objects.equal(steameOvenOne.getID(), event.steameOvenOne.getID())) {
            return;
        }
         steameOvenOne = event.steameOvenOne;

//        short alarms = event.alarmId;
//        LogUtils.i("202012071057","alarms::"+alarms);
//        AlarmDataUtils.steamOvenOneAlarmStatus(steameOvenOne, alarms);
    }

    public void inVisible(){
        if (tvWorkStateName != null && tvWorkDec != null){
            tvWorkDec.setVisibility(GONE);
            tvWorkStateName.setVisibility(GONE);
        }
    }
}
