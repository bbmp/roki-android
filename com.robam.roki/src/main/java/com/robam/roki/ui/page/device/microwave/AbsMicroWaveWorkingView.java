package com.robam.roki.ui.page.device.microwave;

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
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.TimeUtils;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.microwave.MicroWaveModel;
import com.robam.common.pojos.device.microwave.MicroWaveStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.MicroWaveFireUtils;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/7/12.
 */

public class AbsMicroWaveWorkingView extends FrameLayout {

    Context cx;
    @InjectView(R.id.tv_mode_value)
    TextView mTvModeValue;
    @InjectView(R.id.tv_time_value)
    TextView mTvTimeValue;
    @InjectView(R.id.tv_model)
    TextView mTvModel;
    @InjectView(R.id.tv_time)
    TextView mTvTime;
    @InjectView(R.id.rl_up)
    RelativeLayout mRlUp;
    @InjectView(R.id.iv_run_down)
    ImageView mIvRunDown;
    @InjectView(R.id.iv_run_up)
    ImageView mIvRunUp;
    @InjectView(R.id.ll_run_animation)
    RelativeLayout mLlRunAnimation;
    @InjectView(R.id.tv_work_state_name)
    TextView mTvWorkStateName;
    @InjectView(R.id.tv_work_dec)
    TextView mTvWorkDec;
    @InjectView(R.id.imageView9)
    ImageView mImageView9;
    @InjectView(R.id.fl_run_and_stop)
    FrameLayout mFlRunAndStop;
    @InjectView(R.id.left_img)
    ImageView mLeftImg;
    @InjectView(R.id.left_name)
    TextView mLeftName;
    @InjectView(R.id.fl_left)
    FrameLayout mFlLeft;
    @InjectView(R.id.finish_img)
    ImageView mFinishImg;
    @InjectView(R.id.finish_name)
    TextView mFinishName;
    @InjectView(R.id.fl_run_stop)
    FrameLayout mFlRunStop;
    AbsMicroWave microWave;
    List<DeviceConfigurationFunctions> mHideFuncList;
    @InjectView(R.id.tv_weight_value)
    TextView mTvWeightValue;
    @InjectView(R.id.tv_weight)
    TextView mTvWeight;
    @InjectView(R.id.btn_one)
    Button mBtnOne;
    @InjectView(R.id.btn_two)
    Button mBtnTwo;
    @InjectView(R.id.btn_there)
    Button mBtnThere;
    @InjectView(R.id.ll_mult)
    LinearLayout mLlMult;
    @InjectView(R.id.ll_btn)
    LinearLayout mLlBtn;
    private List<DeviceConfigurationFunctions> mRunSubViewList;

    Map<String, String> mRunUpDataMap = new HashMap<>();
    private String mValue;
    private String mModeValue;
    private String mSetTime;
    private String mTime;

    public AbsMicroWaveWorkingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.cx = context;
        initView();
    }

    public AbsMicroWaveWorkingView(Context context, AbsMicroWave microWave, List<DeviceConfigurationFunctions> hideFuncList) {
        super(context);
        this.cx = context;
        this.microWave = microWave;
        mHideFuncList = hideFuncList;
        initView();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventUtils.regist(this);
    }

    public void initView() {
        View view = LayoutInflater.from(cx).inflate(R.layout.abs_microwave_working_page, this, true);
        ScreenAdapterTools.getInstance().loadView(view);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
        initData();
        updateStatus(microWave);
        try {
            startAnimation();
            //上面布局展示
        } catch (Exception e) {
            e.printStackTrace();
        }

        numStep();
    }

    private void numStep() {
        if (microWave.step != 0) {
            mLlBtn.setVisibility(View.VISIBLE);
            mBtnOne.setVisibility(View.VISIBLE);
            mBtnTwo.setVisibility(View.VISIBLE);
            mBtnThere.setVisibility(View.VISIBLE);

        } else if (microWave.step == 0) {
            mLlBtn.setVisibility(View.INVISIBLE);
            mBtnOne.setVisibility(View.INVISIBLE);
            mBtnTwo.setVisibility(View.INVISIBLE);
            mBtnThere.setVisibility(View.INVISIBLE);
        }
        switch (microWave.step) {
            case 1:
                mBtnOne.setAlpha(1);
                mBtnOne.setText("1");
                mBtnOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn_bg));
                mBtnTwo.setAlpha(0.4f);
                mBtnTwo.setText("2");
                mBtnTwo.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                mBtnThere.setAlpha(0.4f);
                mBtnThere.setText("3");
                mBtnThere.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                break;
            case 2:
                mBtnOne.setAlpha(0.4f);
                mBtnOne.setText("1");
                mBtnOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                mBtnTwo.setAlpha(1);
                mBtnTwo.setText("2");
                mBtnTwo.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn_bg));
                mBtnThere.setAlpha(0.4f);
                mBtnThere.setText("3");
                mBtnThere.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                break;
            case 3:
                mBtnOne.setAlpha(0.4f);
                mBtnOne.setText("1");
                mBtnOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                mBtnTwo.setAlpha(0.4f);
                mBtnTwo.setText("2");
                mBtnTwo.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn));
                mBtnThere.setAlpha(1f);
                mBtnThere.setText("3");
                mBtnThere.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_c906_other_btn_bg));
                break;
        }

    }

    private void initData() {
        if (mHideFuncList == null || mHideFuncList.size() == 0) return;

        for (int i = 0; i < mHideFuncList.size(); i++) {
            mRunSubViewList = mHideFuncList.get(i)
                    .subView
                    .subViewModelMap
                    .subViewModelMapSubView
                    .deviceConfigurationFunctions;
        }
        if (mRunSubViewList != null && mRunSubViewList.size() > 0) {
            for (int i = 0; i < mRunSubViewList.size(); i++) {
                String functionCode = mRunSubViewList.get(i).functionCode;
                if ("leftUnit".equals(functionCode) || "rightUnit".equals(functionCode)) {
                    mRunUpDataMap.put(functionCode, mRunSubViewList.get(i).functionParams);
                }
                if ("leftUnit".equals(functionCode)) {
                    mTvModel.setText(mRunSubViewList.get(i).functionName);
                }
                if ("rightUnit".equals(functionCode)) {
                    mTvTime.setText(mRunSubViewList.get(i).functionName);
                }
                if ("finsh".equals(functionCode)) {
                    LogUtils.i("20181109", "functionName" + mRunSubViewList.get(i).functionName);
                    mFinishName.setText(mRunSubViewList.get(i).functionName);
                    Glide.with(cx).load(mRunSubViewList.get(i).backgroundImg).into(mFinishImg);
                }
            }
        }
    }

    public void updateStatus(AbsMicroWave microWave) {
        this.microWave = microWave;
        String leftUnitParams = mRunUpDataMap.get("leftUnit");
        String rightUnitParams = mRunUpDataMap.get("rightUnit");
        LogUtils.i("20180930", " rightUnitParams:" + rightUnitParams);
        try {
            JSONObject leftUnitJson = new JSONObject(leftUnitParams);
            JSONObject modelJson = (JSONObject) leftUnitJson.get(String.valueOf(microWave.mode));
            mValue = (String) modelJson.get("value");
            mModeValue = (String) modelJson.get("mode");
            mTvWeight.setText(mValue);
            if (microWave.mode == MicroWaveModel.Barbecue || microWave.mode == MicroWaveModel.ComibineHeating
                    || microWave.mode == MicroWaveModel.MicroWave || microWave.mode == MicroWaveModel.CleanAir) {
                String resultFire = MicroWaveFireUtils.fireTransition(microWave.power);
                mTvWeightValue.setText(resultFire);
            } else {
                mTvWeightValue.setText(String.valueOf(microWave.weight));
            }
            mTvModeValue.setText(mModeValue);
            mSetTime = TimeUtils.secToHourMinSec(microWave.setTime);
            mTvTimeValue.setText(mSetTime);
//            JSONObject rightUnitJson = new JSONObject(rightUnitParams);
            mTime = TimeUtils.secToHourMinSec(microWave.time);
//            JSONObject min = (JSONObject) rightUnitJson.get("min");
//            String timeValue = (String) min.get("value");
//            mTvTime.setText(timeValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        numStep();
        if (microWave.state == MicroWaveStatus.Run) {
            runMode();
        } else if (microWave.state == MicroWaveStatus.Pause) {
            pauseMode();
        }
    }

    private void complete() {

    }

    private void runMode() {
        mTvWorkStateName.setText("工作剩余时间");
        mTvWorkStateName.setTextSize(18);
        mTvWorkDec.setTextSize(36);
        mTvWorkDec.setText(mTime);
    }

    private void pauseMode() {
        mTvWorkStateName.setText(cx.getString(R.string.device_stop));
        mTvWorkStateName.setTextSize(36);
        mTvWorkDec.setTextSize(18);
        mTvWorkDec.setText("工作剩余时间:" + mTime);
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
            mIvRunDown.startAnimation(circleRotateDown);
        }
        if (circleRotateUp == null) {
            circleRotateUp = AnimationUtils.loadAnimation(getContext(), R.anim.device_model_circle_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotateUp.setInterpolator(lin);
            mIvRunUp.startAnimation(circleRotateUp);
        }
    }

    /**
     * 关闭动画和烟雾效果
     */
    public void stopAnimation() {
        if (circleRotateDown != null) {
            circleRotateDown.cancel();
            circleRotateDown = null;
            mIvRunDown.clearAnimation();
        }
        if (circleRotateUp != null) {
            circleRotateUp.cancel();
            circleRotateUp = null;
            mIvRunUp.clearAnimation();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventUtils.unregist(this);
        stopAnimation();
    }

    @OnClick(R.id.fl_left)
    public void onMFlLeftClicked() {
    }

    IRokiDialog closedialog = null;

    @OnClick(R.id.fl_run_stop)
    public void onMFlRunStopClicked() {

        closedialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        closedialog.setTitleText(R.string.close_work);
        closedialog.setContentText(R.string.is_close_work);
        closedialog.show();

        closedialog.setOkBtn(R.string.ok_btn, new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (closedialog.isShow()) {
                    closedialog.dismiss();
                    short endStatus = 4;
                    if (microWave.state == MicroWaveStatus.Run) {
                        endStatus = 4;
                    } else if (microWave.state == MicroWaveStatus.Pause) {
                        endStatus = 1;
                    }
                    microWave.setMicroWaveState(endStatus, new VoidCallback() {
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

        closedialog.setCancelBtn(R.string.can_btn, new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (closedialog != null && closedialog.isShow()) {
                    closedialog.dismiss();
                }
            }
        });

    }

    public void closeAllDialog() {
        if (closedialog != null && closedialog.isShow()) {
            closedialog.dismiss();
        }
    }
}
