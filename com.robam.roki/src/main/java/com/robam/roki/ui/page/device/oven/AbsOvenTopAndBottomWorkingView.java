package com.robam.roki.ui.page.device.oven;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robam.roki.ui.page.device.oven.AbsOvenBasePage.isCompleteBottom;
import static com.robam.roki.ui.page.device.oven.AbsOvenBasePage.isCompleteTop;

/**
 * 035烤箱 工作页面
 */
public class AbsOvenTopAndBottomWorkingView extends FrameLayout {

    @InjectView(R.id.fl_run_and_stop)
    FrameLayout flRunAndStop;

    @InjectView(R.id.rl_run_animation)
    RelativeLayout rlRunAnimation;

    @InjectView(R.id.iv_run_down)
    ImageView ivRunDown;

    @InjectView(R.id.iv_run_up)
    ImageView ivRunUp;

    @InjectView(R.id.tv_work_state_name)
    TextView tvWorkStateName;

    @InjectView(R.id.tv_work_dec)
    TextView tvWorkDec;


    @InjectView(R.id.fl_run_stop_below)
    FrameLayout flRunStopBelow;

    @InjectView(R.id.iv_run_down_below)
    ImageView ivRunDownBelow;

    @InjectView(R.id.iv_run_up_below)
    ImageView ivRunUpBelow;

    @InjectView(R.id.tv_work_state_name_below)
    TextView tvWorkStateNameBelow;

    @InjectView(R.id.rotate)
    FrameLayout rotate;

    @InjectView(R.id.rotate_img)
    ImageView rotateImg;

    @InjectView(R.id.rotate_li)
    ImageView rotateLi;

    @InjectView(R.id.rotate_name)
    TextView rotateName;

    @InjectView(R.id.oven_complete)
    FrameLayout ovenComplete;

    @InjectView(R.id.fl_run_stop)
    FrameLayout flRunStop;

    @InjectView(R.id.finish_img)
    ImageView finishImg;

    @InjectView(R.id.finish_name)
    TextView finishName;

    @InjectView(R.id.tv_model_top)
    TextView tvModelTop;

    @InjectView(R.id.tv_temp_top)
    TextView tvTempTop;

    @InjectView(R.id.tv_time_top)
    TextView tvTimeTop;

    @InjectView(R.id.tv_model_bottom)
    TextView tvModelBottom;

    @InjectView(R.id.tv_temp_bottom)
    TextView tvTempBottom;

    @InjectView(R.id.tv_time_bottom)
    TextView tvTimeBottom;

    @InjectView(R.id.tv_work_dec_below)
    TextView tvWorkDecBelow;

    @InjectView(R.id.rl_normal_show_top)
    RelativeLayout rlNormalShowTop;

    @InjectView(R.id.rl_normal_show_below)
    RelativeLayout rlNormalShowBelow;

    @InjectView(R.id.tv_unused)
    TextView tvUnused;

    @InjectView(R.id.tv_unused_below)
    TextView tvUnusedBelow;


    @InjectView(R.id.rl_cookbook_mode)
    RelativeLayout cookbookMode;

    @InjectView(R.id.tv_cookbook_mode)
    TextView tvCookbookMode;

    @InjectView(R.id.rl_cookbook_mode_bottom)
    RelativeLayout rlCookbookModeBottom;
    @InjectView(R.id.tv_cookbook_mode_bottom)
    TextView tvCookbookModeBottom;


    Context cx;
    AbsOven oven;

    private List<DeviceConfigurationFunctions> bgFunList;
    private List<DeviceConfigurationFunctions> otherList;
    private List<DeviceConfigurationFunctions> subFunList;
    boolean isShow = false;


    private List<DeviceConfigurationFunctions> funShow;
    Animation circleRotateDown;


    Animation circleRotateUp;

    IRokiDialog closedialog = null;
    private String topName;
    private String bottomName;
    private String param;
    private JSONObject localRecipe;
    String modeParams;
    String cookBookParams;


    //开始/暂停
    @InjectView(R.id.fl_pause_and_run)
    LinearLayout llPauseAndRun;
    @InjectView(R.id.iv_pause_and_run)
    ImageView ivPauseAndRun;
    @InjectView(R.id.tv_pause_and_run)
    TextView tvPauseAndRun;


    public AbsOvenTopAndBottomWorkingView(Context context, AttributeSet attrs, String modeParams, String cookBookParams) {
        super(context, attrs);
        this.cx = context;
        this.modeParams = modeParams;
        this.cookBookParams = cookBookParams;
        initView();

    }

    public AbsOvenTopAndBottomWorkingView(Context context, List<DeviceConfigurationFunctions> bgFunList,
                                          List<DeviceConfigurationFunctions> otherList, AbsOven oven, String modeParams, String cookBookParams) {
        super(context);
        this.cx = context;
        this.bgFunList = bgFunList;
        this.otherList = otherList;
        this.oven = oven;
        this.modeParams = modeParams;
        this.cookBookParams = cookBookParams;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(cx).inflate(R.layout.abs_oven_top_bottom_working_page, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }

        try {
            for (int i = 0; i < bgFunList.size(); i++) {
                //运行时上页面
                if ("runTimeUpView".equals(bgFunList.get(i).functionCode)) {
                    subFunList = bgFunList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                    //运行时下页面
                } else if ("runTimeDownView".equals(bgFunList.get(i).functionCode)) {
                    funShow = bgFunList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
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
                            localRecipe = new JSONObject(functionParams);
                        }
                    }
                }
            }


            startAnimation();
            updateStatus(oven, modeParams, cookBookParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void runDownView() {
        //下面布局展示
        for (int i = 0; i < funShow.size(); i++) {
            if ("rotatRoasting".equals(funShow.get(i).functionCode)) {
                if (isShow) {
                    rotate.setVisibility(VISIBLE);
                } else {
                    rotate.setVisibility(INVISIBLE);
                }
                rotateName.setText(funShow.get(i).functionName);
                Glide.with(cx).load(funShow.get(i).backgroundImg).into(rotateImg);

            } else if ("finish".equals(funShow.get(i).functionCode)) {

                Glide.with(cx).load(funShow.get(i).backgroundImg).into(finishImg);
                finishName.setText(funShow.get(i).functionName);

            } else if ("wait".equals(funShow.get(i).functionCode)) {

                if (oven.status == OvenStatus.Pause || oven.status2Values == OvenStatus.Pause) {
                    Glide.with(cx).load(funShow.get(i).backgroundImgH).into(ivPauseAndRun);
                    tvPauseAndRun.setText("继续");
                } else {
                    Glide.with(cx).load(funShow.get(i).backgroundImg).into(ivPauseAndRun);
                    tvPauseAndRun.setText(funShow.get(i).functionName);
                }


            }
        }
    }

    private void startAnimation() {
        if (circleRotateDown == null) {
            circleRotateDown = AnimationUtils.loadAnimation(getContext(), R.anim.device_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotateDown.setInterpolator(lin);
            ivRunDown.startAnimation(circleRotateDown);
            ivRunDownBelow.startAnimation(circleRotateDown);

        }

        if (circleRotateUp == null) {
            circleRotateUp = AnimationUtils.loadAnimation(getContext(), R.anim.device_model_circle_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotateUp.setInterpolator(lin);
            ivRunUp.startAnimation(circleRotateUp);
            ivRunUpBelow.startAnimation(circleRotateUp);

        }
    }

    public void updateStatus(AbsOven oven, String modeParams, String cookBookParams) {
        this.oven = oven;

        LogUtils.i("20200805", "updateStatus:  status:" + oven.status + " status2Values:" + oven.status2Values);
        runDownView();
        try {
            JSONObject obj = new JSONObject(subFunList.get(0).functionParams);
            param = (String) obj.getJSONObject("param").getJSONObject(oven.runP + "").get("value");
            String str = (String) obj.getJSONObject("param").getJSONObject(oven.runP + "").get("hasRotate");
            //分区模式
            if (param.contains("，")) {
                String[] split = param.split("，");
                topName = split[0];
                bottomName = split[1];
            }

            isShow = "true".equals(str);

            //是自动模式 是EXP模式 不是旋转烤
            if (oven.autoMode != 0 || oven.runP == 9 || !isShow) {
                rotate.setVisibility(INVISIBLE);
            } else {
                rotate.setVisibility(VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.i("2020031102", "333:::");
        //先判断模式 在判断状态
        String leftTimeTop = TimeUtils.timeToStr(oven.time);
        String leftTimeBottom = TimeUtils.timeToStr(oven.LeftTime2Value);
        //自动菜谱模式
        if (oven.runP == 0) {
            autoRecipeMode(leftTimeTop, leftTimeBottom, oven, cookBookParams);
        } else {
            LogUtils.i("2020031102", "444:::");
            //正常模式
            normalMode(leftTimeTop, leftTimeBottom, oven, modeParams);
        }


    }


    //自动菜谱模式
    public void autoRecipeMode(String topTime, String bottomTime, AbsOven oven, String cookBookParams) {
        this.oven = oven;
        JSONObject jsonObject = null;
        try {
            String pKey = null;
            String name = null;
            if (localRecipe != null) {
                jsonObject = localRecipe.getJSONObject(oven.autoMode + "");
                pKey = jsonObject.getString("pKey");
                name = jsonObject.getString("value");
            }
            JSONObject object = new JSONObject(cookBookParams);
            JSONObject autoModeObj = object.getJSONObject(String.valueOf(oven.autoMode));
            String isCombination = autoModeObj.getString("isCombination");
            //组合 分层
            if (isCombination.equals("true")) {
                String isOnlyTop = autoModeObj.getString("isOnlyTop");
                String isOnlyBottem = autoModeObj.getString("isOnlyBottem");

                //上层
                if ("true".equals(isOnlyTop) && "false".equals(isOnlyBottem)) {
                    //显示上层“P25 巴西烤肉”字样
                    cookbookMode.setVisibility(VISIBLE);
                    //隐藏下层“P25 巴西烤肉”字样
                    rlCookbookModeBottom.setVisibility(INVISIBLE);
                    //赋值 “P25 巴西烤肉”字样
                    tvCookbookMode.setText(pKey + name);
                    setTopVisibleData();


                    switch (oven.status) {
                        case 1:

                            tvWorkStateName.setVisibility(View.GONE);
                            tvWorkDec.setTextSize(40);
                            tvWorkDec.setText("完成");
                            rotate.setVisibility(INVISIBLE);
                            ovenComplete.setVisibility(VISIBLE);
                            flRunStop.setVisibility(INVISIBLE);
                            ivPauseAndRun.setVisibility(INVISIBLE);
                            break;

                        //暂停
                        case 3:
                            tvWorkStateName.setVisibility(View.VISIBLE);
                            tvWorkStateName.setTextSize(14);
                            tvWorkStateName.setText("暂停中");
                            tvWorkDec.setTextSize(16);
                            tvWorkDec.setText(topTime);

                            break;
                        //运行
                        case 4:
                            tvWorkStateName.setVisibility(View.VISIBLE);
                            tvWorkStateName.setTextSize(14);
                            tvWorkStateName.setText("工作剩余时间");
                            tvWorkDec.setTextSize(16);
                            tvWorkDec.setText(topTime);

                            break;
                        //预热
                        case 9:

                            tvWorkStateName.setVisibility(View.VISIBLE);
                            tvWorkStateName.setTextSize(14);
                            tvWorkStateName.setText("预热中");
                            tvWorkDec.setTextSize(30);
                            tvWorkDec.setText(oven.temp + "℃");
                            flRunStop.setVisibility(VISIBLE);

                            break;
                    }


                }

                //下层
                if ("false".equals(isOnlyTop) && "true".equals(isOnlyBottem)) {

                    //隐藏上层“P25 巴西烤肉”字样
                    cookbookMode.setVisibility(INVISIBLE);
                    //显示 下层 “P25 巴西烤肉”字样
                    rlCookbookModeBottom.setVisibility(VISIBLE);
                    //赋值 下层“P25 巴西烤肉”字样
                    tvCookbookModeBottom.setText(pKey + name);
                    setBottomVisibleData();

                    switch (oven.status2Values) {
                        case 1:
                            tvWorkStateNameBelow.setVisibility(View.GONE);
                            tvWorkDecBelow.setTextSize(40);
                            tvWorkDecBelow.setText("完成");
                            rotate.setVisibility(INVISIBLE);
                            ovenComplete.setVisibility(VISIBLE);

                            flRunStop.setVisibility(INVISIBLE);
                            break;
                        case 3:
                            tvWorkStateNameBelow.setVisibility(View.VISIBLE);
                            tvWorkStateNameBelow.setTextSize(14);
                            tvWorkStateNameBelow.setText("暂停中");
                            tvWorkDecBelow.setTextSize(16);
                            tvWorkDecBelow.setText(bottomTime);
                            break;
                        case 4:
                            tvWorkStateNameBelow.setVisibility(View.VISIBLE);
                            tvWorkStateNameBelow.setTextSize(14);
                            tvWorkStateNameBelow.setText("工作剩余时间");
                            tvWorkDecBelow.setTextSize(16);
                            tvWorkDecBelow.setText(bottomTime);
                            break;

                        case 9:
                            tvWorkStateNameBelow.setVisibility(View.VISIBLE);
                            tvWorkStateNameBelow.setTextSize(14);
                            tvWorkStateNameBelow.setText("预热中");
                            tvWorkDecBelow.setTextSize(30);
                            tvWorkDecBelow.setText(oven.TempValue + "℃");
                            break;

                    }


                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //正常模式
    public void normalMode(String topTime, String bottomTime, AbsOven oven, String modeParams) {
        this.oven = oven;
        try {
            JSONObject object = new JSONObject(modeParams);
            LogUtils.i("2020031102", "modeParams:::" + modeParams);
            JSONObject param = object.getJSONObject("param");
            JSONObject runModeObj = param.getJSONObject(String.valueOf(oven.runP));

            String isOnlyTop = runModeObj.getString("isOnlyTop");
            String isOnlyBottem = runModeObj.getString("isOnlyBottem");
            LogUtils.i("2020031102", "555:::" + "isOnlyTop:::" + isOnlyTop + "  isOnlyBottem:::" + isOnlyBottem);
            //上层
            if ("true".equals(isOnlyTop) && "false".equals(isOnlyBottem)) {
                LogUtils.i("2020031102", "666:::");
                topStatus(topTime, oven);
            }

            //下层
            if ("false".equals(isOnlyTop) && "true".equals(isOnlyBottem)) {
                bottomStatus(bottomTime, oven);
            }
            //组合
            if ("false".equals(isOnlyTop) && "false".equals(isOnlyBottem)) {
                topAndBottomStatus(topTime, bottomTime, oven);
            }


        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.i("2020031102", e.getMessage());
        }


    }


    private void topAndBottomStatus(String topTime, String bottomTime, AbsOven oven) {
        this.oven = oven;
        rlCookbookModeBottom.setVisibility(INVISIBLE);
        cookbookMode.setVisibility(INVISIBLE);
        short setTemp = oven.setTemp;//上温度
        short setTime = oven.setTime;//上时间
        short setTemp2Value = oven.SetTemp2Value;//下温度
        short setTime2Value = oven.SetTime2Value;//下时间
        top(topTime, setTemp, setTime, oven);
        bottom(bottomTime, setTemp2Value, setTime2Value, oven);
        LogUtils.i("20200805", "topAndBottomStatus:  status:" + oven.status + " status2Values:" + oven.status2Values);
        if (oven.status == 1 && oven.status2Values == 1) {
            LogUtils.i("20200805", "-----------------");
            flRunStop.setVisibility(INVISIBLE);
            llPauseAndRun.setVisibility(INVISIBLE);
            //隐藏 “请带手套拿取，小心高温烫伤。”字样
            ovenComplete.setVisibility(VISIBLE);
        } else {
            flRunStop.setVisibility(VISIBLE);
            ovenComplete.setVisibility(INVISIBLE);
            llPauseAndRun.setVisibility(VISIBLE);
        }


        //显示 上层 模式温度时间模块
        rlNormalShowTop.setVisibility(VISIBLE);
        //显示 下层 模式温度时间模块
        rlNormalShowBelow.setVisibility(VISIBLE);
        //显示 上层 橙色动画
        flRunAndStop.setVisibility(VISIBLE);
        //显示下层橙色动画
        flRunStopBelow.setVisibility(VISIBLE);
        //隐藏上层 “未使用”字样
        tvUnused.setVisibility(INVISIBLE);
        //隐藏 下层 “未使用”字样
        tvUnusedBelow.setVisibility(INVISIBLE);

    }

    private void bottom(String bottomTime, short setTemp2Value, short setTime2Value, AbsOven oven) {
        this.oven = oven;
        switch (oven.status2Values) {
            case 1:
//                if (oven.status== OvenStatus.On) {
//                    ovenComplete.setVisibility(VISIBLE);
//                }

                tvWorkStateNameBelow.setVisibility(View.GONE);
                tvWorkDecBelow.setTextSize(40);
                tvWorkDecBelow.setText("完成");

                tvModelBottom.setText(bottomName);
                tvTempBottom.setText(setTemp2Value + "℃");
                tvTimeBottom.setText(setTime2Value + "min");

                rotate.setVisibility(INVISIBLE);
                break;
            //暂停
            case 3:
                tvWorkStateNameBelow.setVisibility(View.VISIBLE);
                tvWorkStateNameBelow.setTextSize(14);
                tvWorkStateNameBelow.setText("暂停中");
                tvWorkDecBelow.setTextSize(16);
                tvWorkDecBelow.setText(bottomTime);

                tvModelBottom.setText(bottomName);
                tvTempBottom.setText(setTemp2Value + "℃");
                tvTimeBottom.setText(setTime2Value + "min");
                break;
            //运行
            case 4:
                tvWorkStateNameBelow.setVisibility(View.VISIBLE);
                tvWorkStateNameBelow.setTextSize(14);
                tvWorkStateNameBelow.setText("工作剩余时间");
                tvWorkDecBelow.setTextSize(16);
                tvWorkDecBelow.setText(bottomTime);


                tvModelBottom.setText(bottomName);
                tvTempBottom.setText(setTemp2Value + "℃");
                tvTimeBottom.setText(setTime2Value + "min");
                break;
            //预热
            case 9:
                tvWorkStateNameBelow.setVisibility(View.VISIBLE);
                tvWorkStateNameBelow.setTextSize(14);
                tvWorkStateNameBelow.setText("预热中");

                tvWorkDecBelow.setTextSize(30);
                tvWorkDecBelow.setText(oven.TempValue + "℃");

                tvModelBottom.setText(bottomName);
                tvTempBottom.setText(setTemp2Value + "℃");
                tvTimeBottom.setText(setTime2Value + "min");
                break;
        }
    }

    private void top(String topTime, short setTemp, short setTime, AbsOven oven) {
        this.oven = oven;
        LogUtils.i("201912172", "oven.status:::" + oven.status);
        switch (oven.status) {
            case 1:
//                if(oven.status2Values==OvenStatus.On){
//                    ovenComplete.setVisibility(VISIBLE);
//                }

                tvWorkStateName.setVisibility(View.GONE);
                tvWorkDec.setTextSize(40);
                tvWorkDec.setText("完成");
                rotate.setVisibility(INVISIBLE);
                tvModelTop.setText(topName);
                tvTempTop.setText(setTemp + "℃");
                tvTimeTop.setText(setTime + "min");
                break;

            //暂停
            case 3:
                tvWorkStateName.setVisibility(View.VISIBLE);
                tvWorkStateName.setTextSize(14);
                tvWorkStateName.setText("暂停中");

                tvWorkDec.setTextSize(20);
                tvWorkDec.setText(topTime);

                tvModelTop.setText(topName);
                tvTempTop.setText(setTemp + "℃");
                tvTimeTop.setText(setTime + "min");

                break;
            //运行
            case 4:
                tvWorkStateName.setVisibility(View.VISIBLE);
                tvWorkStateName.setTextSize(14);
                tvWorkStateName.setText("工作剩余时间");
                tvWorkDec.setTextSize(16);
                tvWorkDec.setText(topTime);

                tvModelTop.setText(topName);
                tvTempTop.setText(setTemp + "℃");
                tvTimeTop.setText(setTime + "min");

                break;
            //预热
            case 9:

                tvWorkStateName.setVisibility(View.VISIBLE);
                tvWorkStateName.setTextSize(14);
                tvWorkStateName.setText("预热中");
                tvWorkDec.setTextSize(30);
                tvWorkDec.setText(oven.temp + "℃");

                tvModelTop.setText(topName);
                tvTempTop.setText(setTemp + "℃");
                tvTimeTop.setText(setTime + "min");

                break;
        }
    }

    private void bottomStatus(String str, AbsOven oven) {
        this.oven = oven;
        short setTemp2Value = oven.SetTemp2Value;//下温度
        short setTime2Value = oven.SetTime2Value;//下时间

        switch (oven.status2Values) {
            case 1:
                //隐藏“结束”按钮
                flRunStop.setVisibility(INVISIBLE);
                //显示 “请带手套拿取，小心高温烫伤。”字样
                //ovenComplete.setVisibility(VISIBLE);


                break;
            case 3://暂停
                //下腔
                tvWorkStateNameBelow.setVisibility(View.VISIBLE);
                tvWorkStateNameBelow.setTextSize(30);
                tvWorkStateNameBelow.setText("暂停中");
                tvWorkDecBelow.setTextSize(16);
                tvWorkDecBelow.setText(str);

                tvModelBottom.setText(param);
                tvTempBottom.setText(setTemp2Value + "℃");
                tvTimeBottom.setText(setTime2Value + "min");

                setBottomVisibleData();
                break;
            case 4://运行
                tvWorkStateNameBelow.setVisibility(View.VISIBLE);
                tvWorkStateNameBelow.setTextSize(14);
                tvWorkStateNameBelow.setText("工作剩余时间");
                tvWorkDecBelow.setTextSize(16);
                tvWorkDecBelow.setText(str);

                tvModelBottom.setText(param);
                tvTempBottom.setText(setTemp2Value + "℃");
                tvTimeBottom.setText(setTime2Value + "min");
                setBottomVisibleData();


                break;

            case 9://预热
                tvWorkStateNameBelow.setVisibility(View.VISIBLE);
                tvWorkStateNameBelow.setTextSize(14);
                tvWorkStateNameBelow.setText("预热中");

                tvWorkDecBelow.setTextSize(16);
                tvWorkDecBelow.setText(oven.TempValue + "℃");

                tvModelBottom.setText(param);
                tvTempBottom.setText(setTemp2Value + "℃");
                tvTimeBottom.setText(setTime2Value + "min");

                setBottomVisibleData();
                break;
            default:
                break;
        }
    }

    private void topStatus(String topTime, AbsOven oven) {
        this.oven = oven;

        short setTemp = oven.setTemp;//上温度
        short setTime = oven.setTime;//上时间
        switch (oven.status) {
            case 1:
                flRunStop.setVisibility(INVISIBLE);
                //显示 “请带手套拿取，小心高温烫伤。”字样
                ovenComplete.setVisibility(VISIBLE);


                break;

            case 3://暂停
                tvWorkStateName.setVisibility(View.VISIBLE);
                tvWorkStateName.setTextSize(30);
                tvWorkStateName.setText("暂停中");

                tvWorkDec.setTextSize(14);
                tvWorkDec.setText(topTime);
                tvModelTop.setText(param);
                tvTempTop.setText(setTemp + "℃");
                tvTimeTop.setText(setTime + "min");

                setTopVisibleData();
                break;
            case 4://运行
                tvWorkStateName.setVisibility(View.VISIBLE);
                tvWorkStateName.setTextSize(14);
                tvWorkStateName.setText("工作剩余时间");
                tvWorkDec.setTextSize(16);
                tvWorkDec.setText(topTime);
                tvModelTop.setText(param);
                tvTempTop.setText(setTemp + "℃");
                tvTimeTop.setText(setTime + "min");
                setTopVisibleData();

                break;

            case 9://预热
                tvWorkStateName.setVisibility(View.VISIBLE);
                tvWorkStateName.setTextSize(14);
                tvWorkStateName.setText("预热中");
                tvWorkDec.setTextSize(30);
                tvWorkDec.setText(oven.temp + "℃");

                tvModelTop.setText(param);
                tvTempTop.setText(setTemp + "℃");
                tvTimeTop.setText(setTime + "min");
                setTopVisibleData();
                break;
            default:
                break;
        }
    }


    private void setBottomVisibleData() {

        if (oven.runP == 0) {
            //自动菜谱
            //隐藏 正常模式的 上层 模式温度时间模块
            rlNormalShowTop.setVisibility(INVISIBLE);
            //隐藏 正常模式的 下层 模式温度时间模块
            rlNormalShowBelow.setVisibility(INVISIBLE);
        } else {
            //正常模式

            //隐藏 正常模式的 上层 模式温度时间模块
            rlNormalShowTop.setVisibility(INVISIBLE);
            //显示 正常模式的 下层 模式温度时间模块
            rlNormalShowBelow.setVisibility(VISIBLE);

            //隐藏上层“P25 巴西烤肉”字样
            cookbookMode.setVisibility(INVISIBLE);
            //显示 下层 “P25 巴西烤肉”字样
            rlCookbookModeBottom.setVisibility(INVISIBLE);
        }
        //隐藏上层的 橙色圆圈动画
        flRunAndStop.setVisibility(INVISIBLE);
        //显示下层的 橙色圆圈动画
        flRunStopBelow.setVisibility(VISIBLE);
        // 显示上层的 “未使用”字样
        tvUnused.setVisibility(VISIBLE);
        // 隐藏下层的“未使用”字样
        tvUnusedBelow.setVisibility(INVISIBLE);

        //隐藏 “请带手套拿取，小心高温烫伤。”字样
        ovenComplete.setVisibility(INVISIBLE);
        //显示右下角 “结束”按钮
        flRunStop.setVisibility(VISIBLE);
    }


    private void setTopVisibleData() {
        if (oven.runP == 0) {
            //自动菜谱

            //隐藏 正常模式的 上层 模式温度时间模块
            rlNormalShowTop.setVisibility(INVISIBLE);
            //隐藏 正常模式的 下层 模式温度时间模块
            rlNormalShowBelow.setVisibility(INVISIBLE);
        } else {
            //正常模式

            //显示 正常模式的 上层 模式温度时间模块
            rlNormalShowTop.setVisibility(VISIBLE);
            //隐藏 正常模式的 下层 模式温度时间模块
            rlNormalShowBelow.setVisibility(INVISIBLE);


            //隐藏上层“P25 巴西烤肉”字样
            cookbookMode.setVisibility(INVISIBLE);
            //显示 下层 “P25 巴西烤肉”字样
            rlCookbookModeBottom.setVisibility(INVISIBLE);
        }


        //显示上层的 橙色圆圈动画
        flRunAndStop.setVisibility(VISIBLE);
        //隐藏下层的 橙色圆圈动画
        flRunStopBelow.setVisibility(INVISIBLE);
        // 隐藏上层的 “未使用”字样
        tvUnused.setVisibility(INVISIBLE);
        // 显示下层的“未使用”字样
        tvUnusedBelow.setVisibility(VISIBLE);

        //隐藏 “请带手套拿取，小心高温烫伤。”
        ovenComplete.setVisibility(INVISIBLE);
        //显示右下角“结束”按钮
        flRunStop.setVisibility(VISIBLE);

    }


    //点击完成
    @OnClick(R.id.fl_run_stop)
    public void onClick() {
        closedialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        closedialog.setTitleText(R.string.close_work);
        closedialog.setContentText(R.string.is_close_work);
        closedialog.show();
        closedialog.setOkBtn(R.string.ok_btn, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closedialog.isShow()) {
                    closedialog.dismiss();
                    if (oven.status == OvenStatus.Order || oven.status2Values == OvenStatus.Order) {
                        LogUtils.i("2020031309", "oven.status:::11:::" + oven.status);
                        oven.setOvenStatus(OvenStatus.Off, null);
                    } else if (oven.status == OvenStatus.AlarmStatus) {
                        LogUtils.i("2020031309", "oven.status:::22:::" + oven.status);
                        oven.setOvenStatus(OvenStatus.Off, null);
                    } else {
                        LogUtils.i("2020031309", "oven.status:::44:::" + oven.status);
                        oven.setOvenStatus(OvenStatus.On, null);
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

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

    //关闭动画和烟雾效果
    public void stopAnimation() {
        if (circleRotateDown != null) {
            circleRotateDown.cancel();
            circleRotateDown = null;
            ivRunDown.clearAnimation();
            ivRunDownBelow.clearAnimation();
        }
        if (circleRotateUp != null) {
            circleRotateUp.cancel();
            circleRotateUp = null;
            ivRunUp.clearAnimation();
            ivRunUpBelow.clearAnimation();
        }
    }


    //点击旋转烤
    @OnClick(R.id.rotate)
    public void onClickRotate() {
        if (oven.status == OvenStatus.Order) {
            ToastUtils.show(cx.getString(R.string.oven_module_order), Toast.LENGTH_SHORT);
            return;
        }

        if (!"RQ035".equals(oven.getDt())) {
            if (oven.status == OvenStatus.Pause) {
                ToastUtils.show("暂停不可操作", Toast.LENGTH_SHORT);
                return;
            }
        }

        short state = 0;
        if (oven.revolve == 1) {
            state = 0;
        } else {
            state = 1;
        }
        oven.setOvenSpitRotateLightControl(state, oven.light, (short) 0, new VoidCallback() {

            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    //上层工作完成
    public void completeTopWork() {
        LogUtils.i("20200805", "completeTopWork:  status:" + oven.status + " status2Values:" + oven.status2Values);
        LogUtils.i("20200805", "isCompleteTop:" + isCompleteTop + " isCompleteBottom:" + isCompleteBottom);
        tvWorkStateName.setVisibility(View.GONE);
        tvWorkDec.setTextSize(40);
        tvWorkDec.setText("完成");
        rotate.setVisibility(INVISIBLE);

        //单上层
        if (oven.runP == 16 || oven.runP == 17 || oven.runP == 18 || oven.runP == 19) {
            if (isCompleteTop) {
                ovenComplete.setVisibility(VISIBLE);
                llPauseAndRun.setVisibility(INVISIBLE);
                flRunStop.setVisibility(INVISIBLE);
            } else {
                ovenComplete.setVisibility(INVISIBLE);
                llPauseAndRun.setVisibility(VISIBLE);
                flRunStop.setVisibility(VISIBLE);
            }
        } else {
            if (isCompleteTop && isCompleteBottom) {
                ovenComplete.setVisibility(VISIBLE);
                llPauseAndRun.setVisibility(INVISIBLE);
                flRunStop.setVisibility(INVISIBLE);
            }

        }


    }

    //下层工作完成
    public void completeBottomWork() {
        LogUtils.i("20200805", "completeBottomWork:  status:" + oven.status + " status2Values:" + oven.status2Values);
        tvWorkStateNameBelow.setVisibility(View.GONE);
        tvWorkDecBelow.setTextSize(40);
        tvWorkDecBelow.setText("完成");
        rotate.setVisibility(INVISIBLE);
        //单下层
        if (oven.runP == 20 || oven.runP == 21) {
            if (isCompleteBottom) {
                ovenComplete.setVisibility(VISIBLE);
                llPauseAndRun.setVisibility(INVISIBLE);
                flRunStop.setVisibility(INVISIBLE);
            } else {
                ovenComplete.setVisibility(INVISIBLE);
                llPauseAndRun.setVisibility(VISIBLE);
                flRunStop.setVisibility(VISIBLE);
            }
        } else {
            if (isCompleteTop && isCompleteBottom) {
                ovenComplete.setVisibility(VISIBLE);
                llPauseAndRun.setVisibility(INVISIBLE);
                flRunStop.setVisibility(INVISIBLE);
            }
        }

    }

    @OnClick(R.id.fl_pause_and_run)
    public void pauseWork() {
        LogUtils.i("20200709000", "oven.status:::" + oven.status);

        if (oven.status2Values == OvenStatus.Pause || oven.status == OvenStatus.Pause) {
            oven.setOvenStatus(OvenStatus.Working, new VoidCallback() {
                @Override
                public void onSuccess() {
                    LogUtils.i("20200709000", "oven.work:::success");
                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("20200709000", t.getMessage());
                }
            });
        } else {
            oven.setOvenStatus(OvenStatus.Pause, new VoidCallback() {
                @Override
                public void onSuccess() {
                    LogUtils.i("20200709000", "oven.pause:::success");
                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("20200709000", t.getMessage());
                }
            });
        }


    }


}
