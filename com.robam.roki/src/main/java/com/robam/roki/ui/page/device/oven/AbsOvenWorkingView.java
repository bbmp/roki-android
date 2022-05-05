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
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.utils.AlarmDataUtils;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/7/12.
 */

public class AbsOvenWorkingView extends FrameLayout {

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
    ImageView btnOne;
    @InjectView(R.id.btn_two)
    ImageView btnTwo;
    @InjectView(R.id.btn_there)
    ImageView btnThere;
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
    @InjectView(R.id.fl_run_and_stop)
    FrameLayout flRunAndStop;
    @InjectView(R.id.rotate_img)
    ImageView rotateImg;
    @InjectView(R.id.rotate_name)
    TextView rotateName;
    @InjectView(R.id.rotate)
    FrameLayout rotate;
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

    @InjectView(R.id.localcookbook)
    FrameLayout localCookBook;

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
    @InjectView(R.id.rotate_li)
    ImageView rotateLi;
    @InjectView(R.id.oven_complete)
    FrameLayout ovenComplete;

    @InjectView(R.id.localcookbook_mode)
    TextView localCookBookMode;

    //开始/暂停
    @InjectView(R.id.fl_pause_and_run)
    FrameLayout flPauseAndRun;
    @InjectView(R.id.iv_pause_and_run)
    ImageView ivPauseAndRun;
    @InjectView(R.id.tv_pause_and_run)
    TextView tvPauseAndRun;


    private List<DeviceConfigurationFunctions> bgFunList;
    private List<DeviceConfigurationFunctions> otherList;
    private List<DeviceConfigurationFunctions> subFunList;
    private List<DeviceConfigurationFunctions> funShow;

    AbsOven oven;
    boolean isHasRotate = false;
    boolean isShow = false;
    private JSONObject localRecipe;
    private String mGuid;
    boolean is035 = false;
    private String backgroundImg;
    private String modeParams;
    private String cookBookParams;
    private boolean is082A = false, islocalRecipe = false;

    public AbsOvenWorkingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.cx = context;
        initView();
    }

    public AbsOvenWorkingView(Context context, List<DeviceConfigurationFunctions> bgFunList, List<DeviceConfigurationFunctions> otherList, AbsOven oven) {
        super(context);
        this.cx = context;
        this.bgFunList = bgFunList;
        this.otherList = otherList;
        this.oven = oven;
        initView();
    }

    public AbsOvenWorkingView(Context context, List<DeviceConfigurationFunctions> bgFunList, List<DeviceConfigurationFunctions> otherList, AbsOven oven, String modeParams, String cookBookParams) {
        super(context);
        this.cx = context;
        this.bgFunList = bgFunList;
        this.otherList = otherList;
        this.oven = oven;
        this.modeParams = modeParams;
        this.cookBookParams = cookBookParams;
        initView();
    }

    public AbsOvenWorkingView(Context context, List<DeviceConfigurationFunctions> bgFunList, List<DeviceConfigurationFunctions> otherList, AbsOven oven, String modeParams, String cookBookParams, String mGuid) {
        super(context);
        this.cx = context;
        this.bgFunList = bgFunList;
        this.otherList = otherList;
        this.oven = oven;
        this.modeParams = modeParams;
        this.cookBookParams = cookBookParams;
        this.mGuid = mGuid;
        initView();
    }

    public void initView() {
        View view = LayoutInflater.from(cx).inflate(R.layout.abs_oven_working_page, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }

        try {
            for (int i = 0; i < bgFunList.size(); i++) {
                if ("runTimeUpView".equals(bgFunList.get(i).functionCode)) {
                    //运行时上页面
                    subFunList = bgFunList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                } else if ("runTimeDownView".equals(bgFunList.get(i).functionCode)) {
                    //运行时下页面
                    funShow = bgFunList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                }
                if (bgFunList.get(i).deviceType.equals("Q082A")) {
                    is082A = true;
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

            //上面布局展示
            //EXP模式
            if (oven.runP == 9) {
                expShow();
            }
            if (oven.runP == 0) {
                localCookbookMode();
            } else {
                normalShow();
                commonMode();
            }

            //当前步骤
            //多段的总数
            showMoreStage(oven.currentStageValue, oven.moreTotalValue);
            showRunDown();


            if (oven.autoMode != 0) {
                rotate.setVisibility(INVISIBLE);
            }


            if (isHasRotate) {
                rotateStatus();
            }

            ovenComplete.setVisibility(View.INVISIBLE);
            flRunStop.setVisibility(VISIBLE);
            startAnimation();
            LogUtils.i("20200316", "----111----");
            updateStatus(oven);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i("2020031304", e.getMessage());
        }
    }

    private void showRunDown() {
        //下面布局展示
        for (int i = 0; i < funShow.size(); i++) {
            //旋转烤
            if ("rotatRoasting".equals(funShow.get(i).functionCode)) {
                isHasRotate = true;
                //暂时做在本地
                rotateName.setText(funShow.get(i).functionName);
                if (!"clickable".equals(funShow.get(i).functionType)) {
                    is035 = true;
                    backgroundImg = funShow.get(i).backgroundImg;
                    Glide.with(cx).load(backgroundImg).into(rotateImg);
                }
                //结束
            } else if ("finish".equals(funShow.get(i).functionCode)) {
                Glide.with(cx).load(funShow.get(i).backgroundImg).into(finishImg);
                finishName.setText(funShow.get(i).functionName);//结束
            } else if ("wait".equals(funShow.get(i).functionCode)) {
                LogUtils.i("20200709000", "wait");
                if (oven.status == OvenStatus.Pause) {
                    Glide.with(cx).load(funShow.get(i).backgroundImgH).into(ivPauseAndRun);
                    tvPauseAndRun.setText("继续");

                } else {
                    Glide.with(cx).load(funShow.get(i).backgroundImg).into(ivPauseAndRun);
                    tvPauseAndRun.setText(funShow.get(i).functionName);
                }


            }
        }
    }

    protected void closeAllDialog() {
        if (closedialog != null && closedialog.isShow()) {
            closedialog.dismiss();
        }
    }

    //当前步骤
    //多段的总数
    private void showMoreStage(short stage, short moreTotal) {
        if (moreTotal != 0) {
            llMult.setVisibility(View.VISIBLE);
        } else {
            llMult.setVisibility(View.INVISIBLE);
        }
        if (moreTotal == 2) {
            btnThere.setVisibility(View.GONE);
        } else if (moreTotal == 3) {
            btnThere.setVisibility(View.VISIBLE);
        }

        switch (stage) {
            case 1:
                btnOne.setImageResource(R.mipmap.img_device_oven028_morecook_one_yellow);
                btnTwo.setImageResource(R.mipmap.img_device_oven028_morecook_two);
                btnThere.setImageResource(R.mipmap.img_device_oven028_morecook_three);
                break;
            case 2:
                btnOne.setImageResource(R.mipmap.img_device_oven028_morecook_one);
                btnTwo.setImageResource(R.mipmap.img_device_oven028_morecook_two_yellow);
                btnThere.setImageResource(R.mipmap.img_device_oven028_morecook_three);
                break;
            case 3:
                btnOne.setImageResource(R.mipmap.img_device_oven028_morecook_one);
                btnTwo.setImageResource(R.mipmap.img_device_oven028_morecook_two);
                btnThere.setImageResource(R.mipmap.img_device_oven028_morecook_three_yellow);
                break;
            default:
                break;
        }
    }


    Animation circleRotateDown;
    Animation circleRotateUp;
    Animation imgRotate;

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

    boolean flag = false;

    void startRotateAnimation() {
        flag = true;
        if (imgRotate == null) {
            imgRotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            imgRotate.setInterpolator(lin);
            rotateLi.startAnimation(imgRotate);
        }
    }

    void stopRotateAnimation() {
        flag = false;
        if (imgRotate != null) {
            imgRotate.cancel();
            imgRotate = null;
            rotateLi.clearAnimation();
        }
    }


    private void expShow() {
        tvWorkStateName.setVisibility(VISIBLE);
        normalShow.setVisibility(View.INVISIBLE);
        expShow.setVisibility(View.VISIBLE);
        localCookBook.setVisibility(INVISIBLE);
        expModelContent.setText("EXP");

        //上温度
        expTempContent1.setText(oven.setTemp + "℃");
        //下温度（EXP模式才有该值）
        expTempContent2.setText(oven.setTempDownValue + "℃");

        expTimeContent.setText(oven.setTime + "min");
//        if (is082A) {
//            expModelContent.setTextColor(cx.getResources().getColor(R.color.c41));
//            expTempContent1.setTextColor(cx.getResources().getColor(R.color.c41));
//            expTempContent2.setTextColor(cx.getResources().getColor(R.color.c41));
//            expTimeContent.setTextColor(cx.getResources().getColor(R.color.c41));
//            expModel.setTextColor(cx.getResources().getColor(R.color.c41));
//            expTemp1.setTextColor(cx.getResources().getColor(R.color.c41));
//            expTemp2.setTextColor(cx.getResources().getColor(R.color.c41));
//            expTime.setTextColor(cx.getResources().getColor(R.color.c41));
//        }
    }

    //其他模式
    private void normalShow() {
        tvWorkStateName.setVisibility(VISIBLE);
        expShow.setVisibility(View.INVISIBLE);
        normalShow.setVisibility(View.VISIBLE);
        localCookBook.setVisibility(INVISIBLE);
        try {
            for (int i = 0; i < subFunList.size(); i++) {
                LogUtils.i("20191210000", subFunList.get(i).toString());
                if ("model".equals(subFunList.get(i).functionCode)) {
                    tvModel.setText(subFunList.get(i).functionName);
                } else if ("temperature".equals(subFunList.get(i).functionCode)) {
                    tvTemp.setText(subFunList.get(i).functionName);
                } else if ("remainingTime".equals(subFunList.get(i).functionCode)) {
                    tvTime.setText(subFunList.get(i).functionName);
                }
                if (is082A) {
                    tvModel.setTextColor(cx.getResources().getColor(R.color.c41));
                    tvTemp.setTextColor(cx.getResources().getColor(R.color.c41));
                    tvTime.setTextColor(cx.getResources().getColor(R.color.c41));
                    tvModelContent.setTextColor(cx.getResources().getColor(R.color.c41));
                    tvTempContent.setTextColor(cx.getResources().getColor(R.color.c41));
                    tvTimeContent.setTextColor(cx.getResources().getColor(R.color.c41));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //自动菜谱的专业模式
    private void localCookbookMode() {
        tvWorkStateName.setVisibility(VISIBLE);
        expShow.setVisibility(View.INVISIBLE);
        normalShow.setVisibility(View.INVISIBLE);
        localCookBook.setVisibility(VISIBLE);
        try {
            if (localRecipe != null) {
                JSONObject jsonObject = localRecipe.getJSONObject(oven.autoMode + "");
                String pKey = jsonObject.getString("pKey");
                String name = jsonObject.getString("value");
                localCookBookMode.setText(pKey + "   " + name);
                islocalRecipe = true;
                if (is082A) {
                    if (jsonObject.has("hasRotate") && "true".equals(jsonObject.getString("hasRotate"))) {
                        rotate.setVisibility(VISIBLE);
                    } else {
                        rotate.setVisibility(GONE);
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

    public void updateStatus(AbsOven oven) {
        this.oven = oven;
        LogUtils.i("20200930", "oven.status:::" + oven.status);
        showRunDown();
        showMoreStage(oven.currentStageValue, oven.moreTotalValue);
        flRunStop.setVisibility(VISIBLE);
        ovenComplete.setVisibility(View.INVISIBLE);
        flPauseAndRun.setVisibility(VISIBLE);
        islocalRecipe = false;
        if (oven.runP == 9) {
            expShow();
        } else if (oven.runP == 0) {
            localCookbookMode();
        } else {
            normalShow();
            commonMode();
        }
        if (isHasRotate) {
            rotateStatus();
        }
        switch (oven.status) {
            case 3://暂停
                pauseMode();
                break;
            case 4://运行
                runMode();
                break;
            case 7://预约
                orderMode();
                break;
            case 9://预热
                heatMode();
                break;
            default:
                break;
        }
    }

    //烤叉的状态
    private void rotateStatus() {
        // 是EXP模式 不是旋转烤且不是自动模式
        if (oven.runP == 9 || !isShow && oven.autoMode == 0) {
            rotate.setVisibility(INVISIBLE);
            return;
        } else if (oven.autoMode != 0) {
            if ("".equals(cookBookParams)) {
                rotate.setVisibility(INVISIBLE);
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(cookBookParams);
                    boolean aFalse = false;
                    if (oven != null) {
                        JSONObject obj = jsonObject.getJSONObject(String.valueOf(oven.autoMode));
                        String hasRotate = obj.getString("hasRotate");
                        aFalse = hasRotate.equals("true");
                    }

                    if (aFalse) {
                        rotate.setVisibility(VISIBLE);
                    } else {
                        rotate.setVisibility(INVISIBLE);
                        return;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtils.i("20200316", e.getMessage());
                }
            }

        } else {
            rotate.setVisibility(VISIBLE);

        }
        //烤叉旋转
        if (oven.revolve == 1) {

            rotateImg.setImageResource(R.mipmap.oven_module_rotate_yellow);
            rotateLi.setImageResource(R.mipmap.oven_module_li_yellow);

            LogUtils.i("20200316", "13::::" + flag);
            if (!flag) {
                startRotateAnimation();
            }

        } else {
            LogUtils.i("20200316", "14:::" + oven.revolve);
            if (flag) {
                LogUtils.i("20200316", "15:::" + flag);
                stopRotateAnimation();
            }

            if (!is035) {
                LogUtils.i("20200316", "16:::" + is035);
                rotateImg.setImageResource(R.mipmap.oven_module_rotate);
                rotateLi.setImageResource(R.mipmap.oven_module_rotate_nei);
            } else {
                LogUtils.i("20200316", "17:::" + is035);
                rotateLi.setImageDrawable(null);
                Glide.with(cx).load(backgroundImg).into(rotateImg);

            }


        }
    }

    @OnClick(R.id.rotate)
    public void onClickRotate() {
        //警报时是否弹出？
        if (alarmDialog()) {
            return;
        }
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
        if (is082A && islocalRecipe) {
            ToastUtils.show("旋转烤不可操作", Toast.LENGTH_SHORT);
            return;
        }
        if (oven.autoMode != 0) {
            if (is035) {
                ToastUtils.show("旋转烤不可操作", Toast.LENGTH_SHORT);
            }

            return;
        }


        if (oven.status != OvenStatus.Pause) {
            if (is035) {

                ToastUtils.show("工作开始后，旋转烤不可再设置", Toast.LENGTH_SHORT);

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

    private boolean alarmDialog() {
        if (is082A) {
            AbsOven deviceOven = Plat.deviceService.lookupChild(mGuid);
            if (deviceOven.status == OvenStatus.AlarmStatus) {
                AlarmDataUtils.ovenAlarmStatus(oven.alarm, oven);
                return true;
            }
        }
        return false;
    }

    //工作中
    private void runMode() {
        tvWorkStateName.setTextSize(20);
        tvWorkStateName.setText("工作剩余时间");
        tvWorkDec.setTextSize(40);
        String str = "";
        if (is082A) {
            str = com.legent.utils.TimeUtils.secToHourMinSec(oven.hTime);
        } else {
            str = TimeUtils.timeToStr(oven.time);
        }
        tvWorkDec.setText(str);
    }

    //完成
    private void complete() {
        tvWorkStateName.setVisibility(View.INVISIBLE);
        tvWorkDec.setTextSize(45);
        tvWorkDec.setText("完成");
        flRunStop.setVisibility(INVISIBLE);
        rotate.setVisibility(INVISIBLE);
        flPauseAndRun.setVisibility(INVISIBLE);
        ovenComplete.setVisibility(VISIBLE);
    }


    private void commonMode() {
        tvTempContent.setText(oven.setTemp + "℃");
//        tvTimeContent.setText(oven.setTime + "min");

        if (oven.setTimeHValve > 0) {
            byte low = (byte) oven.setTime;
            byte high = (byte) oven.setTimeHValve;
            byte[] bytes = new byte[2];
            bytes[0] = high;
            bytes[1] = low;
            String time = StringUtils.bytes2Hex(bytes).trim();
            time = time.replace(" ", "");
            tvTimeContent.setText(new BigInteger(time, 16) + "min");
        } else {
            tvTimeContent.setText(oven.setTime + "min");
        }

        if (oven.autoMode != 0) {
            tvModelContent.setText("P" + oven.autoMode);
            return;
        }
        JSONObject obj = null;
        try {
            obj = new JSONObject(subFunList.get(0).functionParams);

            String param = (String) obj.getJSONObject("param").getJSONObject(oven.runP + "").get("value");
            String str = (String) obj.getJSONObject("param").getJSONObject(oven.runP + "").get("hasRotate");
            isShow = "true".equals(str);
            tvModelContent.setText(param);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //暂停
    private void pauseMode() {
        tvWorkStateName.setVisibility(View.VISIBLE);
        tvWorkStateName.setTextSize(40);
        tvWorkStateName.setText("暂停中");
        tvWorkDec.setTextSize(25);
        String str = "";
        if (is082A) {
            str = com.legent.utils.TimeUtils.secToHourMinSec(oven.hTime);
        } else {
            str = TimeUtils.timeToStr(oven.time);
        }
        tvWorkDec.setText(str);

    }

    //预热
    private void heatMode() {
        tvWorkStateName.setVisibility(View.VISIBLE);
        tvWorkStateName.setTextSize(20);
        tvWorkStateName.setText("预热中");
        tvWorkDec.setTextSize(40);
        tvWorkDec.setText(oven.temp + "℃");
    }

    //预约
    private void orderMode() {
        tvWorkStateName.setVisibility(View.VISIBLE);
        tvWorkStateName.setTextSize(40);
        tvWorkStateName.setText("预约中");
        tvWorkDec.setTextSize(20);
        String strHour = null;
        String strMin = null;
        if (oven.orderTime_hour < 10) {
            strHour = "0" + oven.orderTime_hour;
        } else {
            strHour = oven.orderTime_hour + "";
        }
        if (oven.orderTime_min < 10) {
            strMin = "0" + oven.orderTime_min;
        } else {
            strMin = oven.orderTime_min + "";
        }
        tvWorkDec.setText("预约开始时间：" + strHour + ":" + strMin);
    }

    IRokiDialog closedialog = null;

    @OnClick(R.id.fl_run_stop)
    public void finishWork() {
        //警报时是否弹出？
        if (alarmDialog()) {
            return;
        }
        closedialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        closedialog.setTitleText(R.string.close_work);
        closedialog.setContentText(R.string.is_close_work);
        closedialog.show();
        closedialog.setOkBtn(R.string.ok_btn, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closedialog.isShow()) {
                    closedialog.dismiss();
                    if (oven.status == OvenStatus.Order) {
                        oven.setOvenStatus(OvenStatus.Off, null);
                    } else if (oven.status == OvenStatus.AlarmStatus) {
                        oven.setOvenStatus(OvenStatus.Off, null);
                    } else {
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


    @OnClick(R.id.fl_pause_and_run)
    public void pauseWork() {
        //警报时是否弹出？
        if (alarmDialog()) {
            return;
        }
        if (oven.status == OvenStatus.Pause) {
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

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }


}
