package com.robam.roki.ui.page.device.sterilizer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Sterilizer.AbsSterilizer;
import com.robam.common.pojos.device.Sterilizer.Steri826;
import com.robam.common.pojos.device.Sterilizer.Steri829;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/10/17.
 */

public class SterilizerWorkingView extends FrameLayout {

    Context cx;
    AbsSterilizer sterilizer;
    @InjectView(R.id.txt_mode)
    TextView txtMode;
    @InjectView(R.id.mode_txt)
    TextView modeTxt;
    @InjectView(R.id.ser_work_show)
    LinearLayout serWorkShow;
    @InjectView(R.id.ll)
    RelativeLayout ll;
    @InjectView(R.id.param_show_work)
    SterilizerParamShowView paramShowWork;
    @InjectView(R.id.par_work_show)
    RelativeLayout parWorkShow;
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
    @InjectView(R.id.oven_complete)
    FrameLayout ovenComplete;
    @InjectView(R.id.finish_img)
    ImageView finishImg;
    @InjectView(R.id.finish_name)
    TextView finishName;
    @InjectView(R.id.fl_run_stop)
    FrameLayout flRunStop;
    @InjectView(R.id.tv_msg)
    TextView tvMsg;


    private List<DeviceConfigurationFunctions> bgFunList;
    private List<DeviceConfigurationFunctions> subFunList;
    private List<DeviceConfigurationFunctions> funShow;

    Steri826 steri826 = null;
    Steri829 steri829 = null;
    private String safeLocked;
    private String safeLocking;
    private int hasNofinishStatus = 0;

    public SterilizerWorkingView(Context context, List<DeviceConfigurationFunctions> bgFunList, AbsSterilizer sterilizer) {
        super(context);
        this.cx = context;
        this.bgFunList = bgFunList;
        this.sterilizer = sterilizer;
        initView();
    }

    public SterilizerWorkingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SterilizerWorkingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(cx).inflate(R.layout.abs_devcie_sterilizer_module, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
        for (int i = 0; i < bgFunList.size(); i++) {
            if ("runtimeUp".equals(bgFunList.get(i).functionCode)) {
                subFunList = bgFunList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
            } else if ("runtimeDown".equals(bgFunList.get(i).functionCode)) {
                funShow = bgFunList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
            }
        }
        normalShow();
        for (int i = 0; i < funShow.size(); i++) {
            if ("vchip".equals(funShow.get(i).functionCode)) {
                //暂时做在本地
                Glide.with(cx).load(funShow.get(i).backgroundImg).into(rotateImg);
                rotateName.setText(funShow.get(i).functionName);
            } else if ("finish".equals(funShow.get(i).functionCode)) {
                Glide.with(cx).load(funShow.get(i).backgroundImg).into(finishImg);
                finishName.setText(funShow.get(i).functionName);
            }
        }
        startAnimation();
        // setLockListener();
    }

    private void normalShow() {
        try {
            for (int i = 0; i < subFunList.size(); i++) {
                if ("mode".equals(subFunList.get(i).functionCode)) {
                    txtMode.setText(subFunList.get(i).functionName);
                    String msg = subFunList.get(i).msg;
                    JSONObject jsonObject = new JSONObject(msg);
                    safeLocked = jsonObject.getString("safeLocked");
                    safeLocking = jsonObject.getString("safeLocking");
                    hasNofinishStatus = jsonObject.getInt("hasNofinishStatus");

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void upStatusSter(AbsSterilizer sterilizer) {


        if (IRokiFamily.RR829.equals(sterilizer.getDt())) {
            this.steri829 = (Steri829) sterilizer;
            upDate829Status(steri829);
        } else {
            this.steri826 = (Steri826) sterilizer;
            LogUtils.i("2020060204","steri826.work_left_time_l::----:"+steri826.work_left_time_l);
            upData826Status(steri826);

        }
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    run829Mode(steri829);
                    break;
                default:
                    break;
            }
        }
    };


    private void upDate829Status(Steri829 steri829) {
        initData829View(steri829);
        mode829Show(steri829);
        switch (steri829.status) {
            case 2://消毒
            case 3://保洁
            case 4://烘干
            case 7://快速杀菌
            case 10://暖碟
            case 8://智能检测
            case 9://感应杀菌
                Message msg = Message.obtain();
                msg.what = 1;
                handler.sendMessage(msg);
                break;
            case 5://预约
                order829Show(steri829);
                break;
            default:
                break;
        }
    }


    private void initData829View(Steri829 steri829) {
        paramShowWork.upDataView(steri829.temp, steri829.hum, steri829.ozone, steri829.germ);
    }

    private void upData826Status(Steri826 steri826) {
        initData826View(steri826);
        mode826Show(steri826);
        showMsg(steri826);

        if (hasNofinishStatus == 8) {
            flRunStop.setVisibility(INVISIBLE);
        } else {
            flRunStop.setVisibility(VISIBLE);
        }

        switch (steri826.status) {
            case 2://消毒
            case 3://保洁
            case 4://烘干
            case 7://快速杀菌
            case 9://感应杀菌
            case 10://暖碟
            case 11://母婴
            case 12://安全锁定
                runMode(steri826);
                break;
            case 5://预约
                order826Show(steri826);
                break;
            case 8://智能检测
                checkShow();
                break;
            default:
                break;
        }



    }

    private void showMsg(Steri826 steri826) {
        if (steri826.steriSecurityLock == 1) {
            tvMsg.setVisibility(VISIBLE);
            tvMsg.setText(safeLocking);
        }else{
            tvMsg.setVisibility(INVISIBLE);
        }
    }


    private void mode826Show(Steri826 steri826) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(subFunList.get(0).functionParams);
            String param = (String) obj.getJSONObject("param").getJSONObject(steri826.status + "").get("value");
            modeTxt.setText(param);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void mode829Show(Steri829 steri829) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(subFunList.get(0).functionParams);
            String param = (String) obj.getJSONObject("param").getJSONObject(steri829.status + "").get("value");
            modeTxt.setText(param);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initData826View(Steri826 steri826) {
        LogUtils.i("2020060202","steri826:::"+steri826);
        paramShowWork.upDataView(steri826.temp, steri826.hum, steri826.ozone, steri826.germ);
        String temperature = null;
        String humidity = null;
        String ozone = null;
        String bacteria = null;
        for (int i = 0; i < bgFunList.size(); i++) {
            if (bgFunList.get(i).functionCode.equals("temperature")) {
                temperature = bgFunList.get(i).functionName;
            }
            if (bgFunList.get(i).functionCode.equals("humidity")) {
                humidity = bgFunList.get(i).functionName;
            }
            if (bgFunList.get(i).functionCode.equals("ozone")) {
                ozone = bgFunList.get(i).functionName;
            }
            if (bgFunList.get(i).functionCode.equals("bacteria")) {
                bacteria = bgFunList.get(i).functionName;
            }
        }
        LogUtils.i("2020060202","temperature:::"+temperature);
        LogUtils.i("2020060202","humidity:::"+humidity);
        LogUtils.i("2020060202","ozone:::"+ozone);
        LogUtils.i("2020060202","bacteria:::"+bacteria);
        paramShowWork.upDataViewName(temperature, humidity, ozone, bacteria);
    }

    //工作中
    private void runMode(Steri826 steri826) {
        //消除臭氧
        if (steri826.status == 12) {
            tvMsg.setText(safeLocked);
            tvWorkStateName.setText("安全锁定剩余时间");
            rotate.setVisibility(VISIBLE);
            flRunStop.setVisibility(INVISIBLE);
        } else {

            tvWorkStateName.setText("工作剩余时间");
            rotate.setVisibility(VISIBLE);
            flRunStop.setVisibility(VISIBLE);
        }
        tvWorkStateName.setVisibility(VISIBLE);
        tvWorkStateName.setTextSize(20);

        tvWorkDec.setTextSize(40);
        LogUtils.i("2020060204","steri826.work_left_time_l:::"+steri826.work_left_time_l);
        String str = TimeUtils.getTime(steri826.work_left_time_l);
        tvWorkDec.setText(str);

        ovenComplete.setVisibility(INVISIBLE);
    }

    private void run829Mode(Steri829 steri829) {
        tvWorkStateName.setVisibility(VISIBLE);
        tvWorkStateName.setTextSize(20);
        tvWorkStateName.setText("工作剩余时间");
        tvWorkDec.setTextSize(40);
        String str = TimeUtils.timeforStr(steri829.work_left_time_l);
        tvWorkDec.setText(str);
        flRunStop.setVisibility(VISIBLE);
        rotate.setVisibility(VISIBLE);
        ovenComplete.setVisibility(INVISIBLE);
    }


    private void order829Show(Steri829 steri829) {
        //预约
        tvWorkStateName.setVisibility(View.VISIBLE);
        tvWorkStateName.setTextSize(40);
        tvWorkStateName.setText("预约中");
        tvWorkDec.setTextSize(20);
        tvWorkDec.setText("预约" + steri829.work_left_time_h + "小时" + steri829.work_left_time_l + "分钟后\n开始工作");
        flRunStop.setVisibility(VISIBLE);
        rotate.setVisibility(VISIBLE);
        ovenComplete.setVisibility(INVISIBLE);
    }

    private void checkShow() {
        //监测
        tvWorkStateName.setVisibility(View.VISIBLE);
        tvWorkStateName.setTextSize(35);
        tvWorkStateName.setText("监测中");
        tvWorkDec.setTextSize(18);
        tvWorkDec.setText("实时监测消毒柜内状态\n适时开启感应杀菌");
        rotate.setVisibility(INVISIBLE);
    }


    private void order826Show(Steri826 steri826) {
        //预约
        tvWorkStateName.setVisibility(View.VISIBLE);
        tvWorkStateName.setTextSize(40);
        tvWorkStateName.setText("预约中");
        tvWorkDec.setTextSize(20);


        int hours = (int) Math.floor((steri826.steriReminderTime) / 60);
        int minute = (steri826.steriReminderTime) % 60;

        tvWorkDec.setText("预约" + hours + "小时" + minute + "分钟后\n开始工作");
        flRunStop.setVisibility(VISIBLE);
        rotate.setVisibility(VISIBLE);
        ovenComplete.setVisibility(INVISIBLE);
    }

    protected void completeWork() {
        complete();
    }

    //完成
    private void complete() {
        tvWorkStateName.setVisibility(View.INVISIBLE);
        tvWorkDec.setTextSize(45);
        tvWorkDec.setText("完成");
        flRunStop.setVisibility(INVISIBLE);
        rotate.setVisibility(INVISIBLE);
        ovenComplete.setVisibility(VISIBLE);
    }


    public void closeAllDialog() {
        if (closedialog != null && closedialog.isShow()) {
            closedialog.dismiss();
        }
    }

    //童锁
    @OnClick(R.id.rotate)
    public void openClock() {
        if (steri826 == null) return;
        if (steri826.status != 0) {
            if (steri826.isChildLock == 0) {
                steri826.setSteriLock((short) 1, new VoidCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
            }
        }
    }




    IRokiDialog closedialog = null;

    @OnClick(R.id.fl_run_stop)
    public void finishWork() {
        if (steri826 != null && steri826.isChildLock == 1) {
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
                    sendOff();
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

    private void sendOff() {
        if (steri829 != null) {
            steri829.setSteriPower((short) 1, new VoidCallback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFailure(Throwable t) {
                    ToastUtils.show("结束失败", Toast.LENGTH_SHORT);
                }
            });
        } else {
            steri826.setSteriPower((short) 1, (short) 0, (short) 0, new VoidCallback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFailure(Throwable t) {
                    ToastUtils.show("结束失败", Toast.LENGTH_SHORT);
                }
            });
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


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

}
