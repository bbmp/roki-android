package com.robam.roki.ui.page.device.steamovenone;

import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.TimeUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SteamOvenOneWaterChangesEvent;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneModel;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneWorkStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.utils.DialogUtil;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robam.roki.R.id.fl_left;

/**
 * Created by Dell on 2018/7/12.
 */

public class AbsSteamOvenWorking906View extends FrameLayout {

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
    @InjectView(R.id.ll_mult)
    LinearLayout llMult;
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
    @InjectView(R.id.ll)
    RelativeLayout ll;
    @InjectView(R.id.left_img)
    ImageView mLeftImg;
    @InjectView(R.id.left_name)
    TextView mLeftName;
    @InjectView(fl_left)
    FrameLayout mFlLeft;


    private List<DeviceConfigurationFunctions> bgFunList;
    private List<DeviceConfigurationFunctions> subFunList;
    private List<DeviceConfigurationFunctions> ovenRunListDown;
    private List<DeviceConfigurationFunctions> steamRunListDown;

    AbsSteameOvenOne steameOvenOne;

    public AbsSteamOvenWorking906View(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.cx = context;
        initView();
    }

    public AbsSteamOvenWorking906View(Context context, List<DeviceConfigurationFunctions> bgFunList, AbsSteameOvenOne steameOvenOne) {
        super(context);
        this.cx = context;
        this.bgFunList = bgFunList;
        this.steameOvenOne = steameOvenOne;
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
        ToastUtils.showLong(R.string.device_alarm_water_out);
    }

    public void initView() {
        View view = LayoutInflater.from(cx).inflate(R.layout.abs_steamoven_906_working_page, this, true);
        ScreenAdapterTools.getInstance().loadView(view);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
        try {
            startAnimation();
            //上面布局展示
            if (steameOvenOne.workModel == SteamOvenOneWorkStatus.NoStatus) return;
            if (steameOvenOne.workModel == SteamOvenOneModel.EXP) {
                expShow();
            } else {
                normalShow();
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
                    Glide.with(cx).load(ovenRunListDown.get(i).backgroundImgH).into(mLeftImg);
                }
                mLeftName.setText(ovenRunListDown.get(i).functionName);
            } else if ("finish".equals(ovenRunListDown.get(i).functionCode)) {
                Glide.with(cx).load(ovenRunListDown.get(i).backgroundImg).into(finishImg);
                finishName.setText(ovenRunListDown.get(i).functionName);
            }
        }

        normalShow.setVisibility(View.INVISIBLE);
        expShow.setVisibility(View.VISIBLE);
    }

    private void normalShow() {
        numStep();
        expShow.setVisibility(View.INVISIBLE);
        normalShow.setVisibility(View.VISIBLE);

        if (SteamOvenOneModel.GANZAO == steameOvenOne.workModel || SteamOvenOneModel.QINGJIE == steameOvenOne.workModel
                || SteamOvenOneModel.CHUGO == steameOvenOne.workModel) {
            normalShow.setVisibility(INVISIBLE);
        } else {
            normalShow.setVisibility(VISIBLE);
        }

        if (SteamOvenOneModel.FAXIAO == steameOvenOne.workModel || SteamOvenOneModel.XIANNENZHENG == steameOvenOne.workModel
                || SteamOvenOneModel.YINGYANGZHENG == steameOvenOne.workModel
                || SteamOvenOneModel.GAOWENZHENG == steameOvenOne.workModel) {
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
        }

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
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void completeWork() {
        complete();
    }

    public void updateStatus(AbsSteameOvenOne steameOvenOne, boolean completeSign) {
        this.steameOvenOne = steameOvenOne;
        if (completeSign) {
            complete();
        }
        if (steameOvenOne.workModel == SteamOvenOneModel.EXP) {
            expShow();
            commonExpMode();
        } else {
            normalShow();
            commonMode();
        }

        if (steameOvenOne.worknStatus == SteamOvenOneWorkStatus.PreHeat &&
                steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
            heatMode();
        } else if (steameOvenOne.worknStatus == SteamOvenOneWorkStatus.Working &&
                steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
            runMode();
        } else if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.Pause &&
                steameOvenOne.powerStatus == SteamOvenOnePowerStatus.On) {
            pauseMode();
        } else if (steameOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.Order) {
            orderMode();
        }

    }

    //工作中
    private void runMode() {


        if (SteamOvenOneModel.GANZAO == steameOvenOne.workModel || SteamOvenOneModel.QINGJIE == steameOvenOne.workModel
                || SteamOvenOneModel.CHUGO == steameOvenOne.workModel) {
            mFlLeft.setVisibility(View.GONE);
            flRunStop.setVisibility(View.VISIBLE);
            tvWorkStateName.setTextSize(36);
            tvWorkStateName.setVisibility(View.VISIBLE);
            tvWorkDec.setTextSize(20);
            JSONObject obj = null;
            try {
                obj = new JSONObject(subFunList.get(0).functionParams);
                String param = (String) obj.getJSONObject("param").getJSONObject(steameOvenOne.workModel + "").get("value");
                LogUtils.i("20180712", "param::" + param);
                tvWorkStateName.setText(param);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String minSec = TimeUtils.secToHourMinSec(steameOvenOne.leftTime);
//        String str = TimeUtils.timeforStr(steameOvenOne.leftTime);
            tvWorkDec.setText(minSec);
        } else {
            mFlLeft.setVisibility(View.GONE);
            flRunStop.setVisibility(View.VISIBLE);
            tvWorkStateName.setTextSize(20);
            tvWorkStateName.setVisibility(View.VISIBLE);
            tvWorkStateName.setText("工作剩余时间");
            tvWorkDec.setTextSize(36);
            String minSec = TimeUtils.secToHourMinSec(steameOvenOne.leftTime);
//        String str = TimeUtils.timeforStr(steameOvenOne.leftTime);
            tvWorkDec.setText(minSec);
        }


    }

    //完成
    private void complete() {
        tvWorkStateName.setVisibility(View.INVISIBLE);
        flRunStop.setVisibility(View.INVISIBLE);
        tvWorkDec.setTextSize(36);
        tvWorkDec.setText("完成");
        mFlLeft.setVisibility(View.VISIBLE);

    }

    private void commonMode() {
        tvTempContent.setText(steameOvenOne.setTemp + "℃");
        tvTimeContent.setText(steameOvenOne.setTime + "min");
        JSONObject obj = null;
        try {
            obj = new JSONObject(subFunList.get(0).functionParams);
            String param = (String) obj.getJSONObject("param").getJSONObject(steameOvenOne.workModel + "").get("value");
            LogUtils.i("20180712", "param::" + param);
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
        tvWorkStateName.setVisibility(View.VISIBLE);
        flRunStop.setVisibility(View.VISIBLE);
        tvWorkStateName.setTextSize(36);
        tvWorkStateName.setText("暂停中");
        String minSec = TimeUtils.secToHourMinSec(steameOvenOne.leftTime);
//        String str = TimeUtils.timeforStr(steameOvenOne.leftTime);
        tvWorkDec.setTextSize(20);
        tvWorkDec.setText(minSec);
        if (steameOvenOne.workModel == SteamOvenOneModel.GANZAO || steameOvenOne.workModel == SteamOvenOneModel.QINGJIE
                || steameOvenOne.workModel == SteamOvenOneModel.CHUGO) {
            mFlLeft.setVisibility(View.GONE);
        } else {
            mFlLeft.setVisibility(View.VISIBLE);
        }

    }

    //预热
    private void heatMode() {
        mFlLeft.setVisibility(View.GONE);
        flRunStop.setVisibility(View.VISIBLE);
        tvWorkStateName.setVisibility(View.VISIBLE);
        tvWorkStateName.setTextSize(20);
        tvWorkStateName.setText("预热中");
        tvWorkDec.setTextSize(36);
        tvWorkDec.setText(steameOvenOne.temp + "℃");
    }

    //预约
    private void orderMode() {
        mFlLeft.setVisibility(View.GONE);
        flRunStop.setVisibility(View.VISIBLE);
        tvWorkStateName.setVisibility(View.VISIBLE);
        tvWorkStateName.setTextSize(36);
        tvWorkStateName.setText("预约中");
        tvWorkDec.setTextSize(18);
        if (steameOvenOne.ordertime_hour >= 0 && steameOvenOne.ordertime_hour <= 9) {
            if (steameOvenOne.ordertime_min >= 0 && steameOvenOne.ordertime_min <= 9) {
                tvWorkDec.setText("预约开始时间" + "0" + steameOvenOne.ordertime_hour + ":" + "0" + steameOvenOne.ordertime_min);
            } else {
                tvWorkDec.setText("预约开始时间" + "0" + steameOvenOne.ordertime_hour + ":" + steameOvenOne.ordertime_min);
            }
        } else {
            if (steameOvenOne.ordertime_min > 9) {
                tvWorkDec.setText("预约开始时间" + steameOvenOne.ordertime_hour + ":" + steameOvenOne.ordertime_min);
            } else {
                tvWorkDec.setText("预约开始时间" + steameOvenOne.ordertime_hour + ":" + "0" + steameOvenOne.ordertime_min);
            }

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
            public void onClick(View view) {
                if (closedialog.isShow()) {
                    closedialog.dismiss();
                    if (steameOvenOne.powerStatus == SteamOvenOnePowerOnStatus.Order || steameOvenOne.powerStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
                        steameOvenOne.setSteameOvenStatus(SteamOvenOnePowerStatus.On, SteamOvenOnePowerOnStatus.OperatingState, null);
                    } else
                        steameOvenOne.setSteameOvenStatus(SteamOvenOnePowerStatus.Off, SteamOvenOnePowerOnStatus.OperatingState, null);
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

    @OnClick(fl_left)
    public void onViewClicked() {

        if (steameOvenOne.WaterStatus == 1){
            ToastUtils.showShort(R.string.device_alarm_water_open);
            return;
        }
        if (steameOvenOne.WaterStatus == 0) {
            steameOvenOne.setSteameOvenOneWaterPop((short) 1, new VoidCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        }

    }

    //多段烹饪步鄹
    private void numStep() {
        if (steameOvenOne.multiSumStep != 0) {
            mLlBtn.setVisibility(View.VISIBLE);
            if (steameOvenOne.multiSumStep == 2) {
                mBtnOne.setVisibility(View.VISIBLE);
                mBtnTwo.setVisibility(View.INVISIBLE);
                mBtnThere.setVisibility(View.VISIBLE);
            } else if (steameOvenOne.multiSumStep == 3) {
                mBtnOne.setVisibility(View.VISIBLE);
                mBtnTwo.setVisibility(View.VISIBLE);
                mBtnThere.setVisibility(View.VISIBLE);
            }
        } else if (steameOvenOne.multiSumStep == 0) {
            mLlBtn.setVisibility(View.INVISIBLE);
            mBtnOne.setVisibility(View.INVISIBLE);
            mBtnTwo.setVisibility(View.INVISIBLE);
            mBtnThere.setVisibility(View.INVISIBLE);
        }
        if (steameOvenOne.multiSumStep == 2) {
            mBtnTwo.setVisibility(View.INVISIBLE);
            switch (steameOvenOne.SectionOfTheStep) {
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
        } else if (steameOvenOne.multiSumStep == 3) {
            switch (steameOvenOne.SectionOfTheStep) {

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


}
