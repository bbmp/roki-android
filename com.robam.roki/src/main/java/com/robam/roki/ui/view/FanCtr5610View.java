package com.robam.roki.ui.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Preconditions;
import com.legent.VoidCallback;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.Fan8229;
import com.robam.common.pojos.device.fan.FanStatus;
import com.robam.common.pojos.device.fan.IFan;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.CircularProgressDrawable;
import com.robam.roki.ui.UIListeners;
import com.robam.roki.ui.dialog.HoodStopWorkTipDialog;
import com.robam.roki.utils.ToolUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FanCtr5610View extends FrameLayout implements UIListeners.IFanCtrView {
    private Context mContext;

    Fan8229 fan;

    @InjectView(R.id.gearLow)
    DeviceFanGearView gearLow;
    @InjectView(R.id.gearHigh)
    DeviceFanGearView gearHigh;
    @InjectView(R.id.imgLight)
    ImageView imgLight;
    @InjectView(R.id.img_logo)
    FrameLayout img_logo;

    //工作动画
    private ImageView gearLowImg;
    private ImageView gearHighImg;
    private Animation operatingAnim;
    //油网动画
    @InjectView(R.id.you_wang_dh)
    ImageView ywImg;
    //首次进入界面的老板logo
    @InjectView(R.id.init_logo)
    ImageView initlogo;
    //首次进入界面的老板logo背景
    @InjectView(R.id.img_5610)
    ImageView img_5610;

    //开机动画
    @InjectView(R.id.kj_dh)
    FrameLayout kjDh;
    @InjectView(R.id.iv_drawable)
    ImageView ivDrawable;
    @InjectView(R.id.img)
    ImageView kjImg;
    CircularProgressDrawable drawable;
    Animator currentAnimation;
    Animation imgPushOut;
    Animation imgPushIn;
    Animation kjimgPushOut;
    Animation kjimgPushOut_notchange;
    //老板图标从小到大动画
    Animator logoSmallToLargeAnimation;
    //延时关机
    @InjectView(R.id.delay_dh)
    FrameLayout delaydh;

    //延时关机中间老板log颜色和动画控制
    @InjectView(R.id.img_delay)
    ImageView img_delay;
    //控制动画画圈确保只执行一次
    private boolean isDrawCicleOnce = false;
    //控制老板log从小到大动画只执行一次
    private boolean isSmallToLargeAnimationOnce = false;

    public static final String BROADCAST_ACTION = "com.robam.roki.sendclenlockcommand";
    private MyBroadcastReceiver mBroadcastReceiver;
    private Dialog mYouWDialog;//油网清洗dialog
    private boolean mDelay_RefreshOnce;
    private boolean stop6_mflag;//停止爆炒模式动画播放
    private boolean stop0_mflag;//停止待机状态动画播放
    private boolean Clean_mLock;//清洁状态锁定

    //定义一个内部类广播接收者，接受油烟机发过来的指令
    public class MyBroadcastReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            short flag = intent.getExtras().getShort("flag");
            if (flag == 1) {
                ywImg.setVisibility(GONE);
                kjDh.setVisibility(GONE);
                img_logo.setVisibility(VISIBLE);
                mYouWDialog = new HoodStopWorkTipDialog.Builder(context).build();
                mYouWDialog.show();
                setCleanLockUnclick();
                OnPowerOffStatus();
                mDelay_RefreshOnce = false;
            } else if (flag == 0) {
                if (mYouWDialog != null)
                    mYouWDialog.dismiss();
                setCleanLockClick();
            }

        }
    }

    public FanCtr5610View(Context context) {
        super(context);
        this.mContext = context;
        init(context, null);
    }

    public FanCtr5610View(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(context, attrs);

    }

    void init(Context cx, AttributeSet attrs) {
        View view = LayoutInflater.from(cx).inflate(R.layout.view_5610, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
            //初始化动画
            initAnimotion(cx);
        }
        //动态注册接收广播
        mBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BROADCAST_ACTION);
        //动态注册UIRefreshBroadcastReceiver
        getContext().registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //当页面结束后销毁注册的广播
        getContext().unregisterReceiver(mBroadcastReceiver);
    }

    //油烟机首次操作
    @OnClick(R.id.img_logo)
    public void onClickLogo() {
        if (!checkConnection()) {
            return;
        }
        short status = FanStatus.On;
        //setStatus(status);
        //开机动画
        //OnPowerOn();
        ywImg.setVisibility(GONE);
        setFirstLevel(AbsFan.PowerLevel_1);
    }

    //油烟机延时关机
    @OnClick(R.id.kj_dh)
    public void onClickKjLogo() {
        if (!checkConnection()) {
            return;
        }
        if (true) {
            onClickDelayLogo();
            return;
        }
        short status = FanStatus.DelayShutdown;
        setStatus(status);
        //进入延时关机状态
        //OndelayStatus();
        isSmallToLargeAnimationOnce = false;
        isDrawCicleOnce = false;
        Toast.makeText(mContext, R.string.hood_stopworking_delay, Toast.LENGTH_SHORT).show();
    }

    //油烟机关机操作
    @OnClick(R.id.delay_dh)
    public void onClickDelayLogo() {
        //回到初始状态
        if (!checkConnection()) {
            return;
        }
        short status = FanStatus.Off;
        setStatus(status);
        //关机状态动作
        OnPowerOffStatus();
        isSmallToLargeAnimationOnce = false;
        isDrawCicleOnce = false;
    }

    //根据档位判断呼吸灯颜色（中间老板log是红色还是白色）
    private void judgeLogColor(ImageView logo) {

        if (fan.level == AbsFan.PowerLevel_6 || fan.level == AbsFan.PowerLevel_3) {
            logo.setImageResource(R.mipmap.img_robam_red);
        } else if (fan.status == 0) {
            logo.setImageResource(R.mipmap.img_robam_black);
        } else {
            logo.setImageResource(R.mipmap.img_robam_white);
        }


    }

    //根据档位判断呼吸灯是否闪烁
    public void judgeBreathingLampTwinkle() {
        if ((fan.status == 1) && (fan.level > 0)) {
            kjImg.clearAnimation();
        }
    }


    @OnClick(R.id.imgLight)
    public void onClickLight() {
        setLight();
    }

    @OnClick(R.id.gearLow)
    public void onClickLow() {
        if (fan.level == AbsFan.PowerLevel_1)
            setLevel(AbsFan.PowerLevel_0);
        else
            setLevel(AbsFan.PowerLevel_1);
    }

    @OnClick(R.id.gearHigh)
    public void onClickHigh() {
        if (fan.level == AbsFan.PowerLevel_6 || fan.level == AbsFan.PowerLevel_3)
            setLevel(AbsFan.PowerLevel_0);
        else
            setLevel(AbsFan.PowerLevel_6);
    }


    @Override
    public void attachFan(IFan fan) {
        Preconditions.checkState(fan instanceof Fan8229, "attachFan error:not 8700");
        this.fan = (Fan8229) fan;
    }


    //每隔一段时间去查询烟机状态
    @Override
    public void onRefresh() {
        Log.i("FanCtr5610---->",
                "status:"+fan.status + " level:" + fan.level+" light:"+fan.light);
        if (fan == null)
            return;
        if (fan.status == 0||fan.status==5) {//关机
            OnPowerOffStatus();
            mDelay_RefreshOnce = false;
        }else if (fan.status == 1) {//开机
            OnPowerOn();
            mDelay_RefreshOnce = false;
        }else if (fan.status == 2) {//延迟关机
            if (!mDelay_RefreshOnce) {
                OndelayStatus();
                mDelay_RefreshOnce = true;
            }
            imgLight.setSelected(fan.light);
            //return;
        }else if(fan.status==4){//
            gearLow.setVisibility(GONE);
            gearHigh.setVisibility(GONE);
            imgLight.setSelected(fan.light);
            return;
        }
        imgLight.setSelected(fan.light);
        if ((fan.status == 1) && (fan.level == AbsFan.PowerLevel_0)) {
            return;
        }
        showLevel(fan.level);
    }

    //开机状态
    private void OnPowerOn() {
        img_logo.setVisibility(GONE);
        kjDh.setVisibility(VISIBLE);
        kjImg.setVisibility(VISIBLE);
        judgeLogColor(kjImg);

        if(fan.level== AbsFan.PowerLevel_0){
            gearLow.setVisibility(VISIBLE);
            gearLow.setEnabled(true);
            gearHigh.setVisibility(VISIBLE);
            gearHigh.setEnabled(true);
            //开启延时关机呼吸灯动画
            if(!stop0_mflag){
                kjImg.clearAnimation();
                DelayImgReceyle();
                stop0_mflag=true;
            }
        }else{
            gearLow.setVisibility(VISIBLE);
            gearLow.setEnabled(true);
            gearHigh.setVisibility(VISIBLE);
            gearHigh.setEnabled(true);
        }
        if (!isSmallToLargeAnimationOnce == true) {
            logoSmallToLargeAnimation.start();
            isSmallToLargeAnimationOnce = true;
        }
        if (!isDrawCicleOnce == true) {
            currentAnimation.start();
            isDrawCicleOnce = true;
        }
    }

    //关机状态动作
    private void OnPowerOffStatus() {
        img_delay.clearAnimation();
        kjImg.clearAnimation();
        delaydh.setVisibility(GONE);
        kjDh.setVisibility(GONE);
        gearLow.setVisibility(GONE);
        gearHigh.setVisibility(GONE);
        img_logo.setVisibility(VISIBLE);

        isSmallToLargeAnimationOnce = false;
        isDrawCicleOnce = false;
        DelayImgReceyleCancel();
    }

    //进入延时关机状态动作
    private void OndelayStatus() {
        LayoutInflater inflater = LayoutInflater
                .from(getContext());
        View view = inflater.inflate(R.layout.toast_textcolor_view,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        //String delay_time=String.valueOf(PreferenceUtils.getInt(PageArgumentKey.ShutDownDelay,1));
        String delay_time= "稍后";
        Toast toast = Toast.makeText(mContext,
                delay_time+"关机",
                Toast.LENGTH_SHORT);
        TextView tv= view.findViewById(R.id.text);
        tv.setText(delay_time+"关机");
        toast.setGravity(Gravity.CENTER, 0, 100);
        toast.setView(view);
        toast.show();
        kjImg.clearAnimation();
        kjImg.setVisibility(GONE);
        kjDh.setVisibility(GONE);

        delaydh.setVisibility(VISIBLE);
        judgeLogColor(img_delay);
        gearLow.setVisibility(VISIBLE);
        gearLow.setEnabled(false);
        gearHigh.setVisibility(VISIBLE);
        gearHigh.setEnabled(false);
        //开启延时关机呼吸灯动画
        img_delay.clearAnimation();
        img_delay.startAnimation(imgPushOut);
        DelayImgReceyleCancel();
    }


    void showLevel(int level) {
        gearLow.setSelected(false);
        gearHigh.setSelected(false);
        gearLow.setTitle("弱", "档");
        gearHigh.setTitle("爆", "炒");

        if (level == AbsFan.PowerLevel_0) {
            //关闭工作动画
            gearLowImg.clearAnimation();
            gearHighImg.clearAnimation();
        }

        boolean isLow = level < AbsFan.PowerLevel_3;//弱档 1或2
        boolean isLevel3 = level == AbsFan.PowerLevel_3;//强档3
        if(level!= AbsFan.PowerLevel_0)
        {
            gearLow.setSelected(isLow);
        }else{
            gearLow.setSelected(false);gearHigh.setSelected(false);
        }
        gearHigh.setSelected(!isLow);
        //设置工作动画
        //setAnimotion(isLow);
        //if(isLow)
        //爆档
        if (!isLow && level == AbsFan.PowerLevel_6) {
            Log.i("showlevel0",String.valueOf(level));
            stop6_mflag=false;
            stop0_mflag=false;
            kjImg.setVisibility(VISIBLE);
            kjImg.clearAnimation();
            kjImg.startAnimation(kjimgPushOut);
        }else if(isLow){
            Log.i("showlevel1",String.valueOf(isLow));
            DelayImgReceyleCancel();
            kjimgPushOut.cancel();
            kjImg.clearAnimation();
            stop6_mflag=true;
            stop0_mflag=false;
            //kjImg.setAnimation(kjimgPushOut_notchange);
        }
        if (!isLow && isLevel3) {
            kjimgPushOut.cancel();
            kjImg.clearAnimation();
            //kjImg.setAnimation(kjimgPushOut_notchange);
            stop0_mflag=false;
            stop6_mflag=true;
            DelayImgReceyleCancel();
            gearHigh.setTitle("强", "档");
        }
        /*Log.i("img_logo",String.valueOf(img_logo.getVisibility()));
        Log.i("img_5610",String.valueOf(img_5610.getVisibility()));
        Log.i("initlogo",String.valueOf(initlogo.getVisibility()));
        Log.i("kjDh",String.valueOf(kjDh.getVisibility()));
        Log.i("ivDrawable",String.valueOf(ivDrawable.getVisibility()));
        Log.i("kjImg",String.valueOf(kjImg.getVisibility()));
        Log.i("delaydh",String.valueOf(delaydh.getVisibility()));
        Log.i("img_delay",String.valueOf(img_delay.getVisibility()));*/
    }

    void setLight() {
//        if (!checkConnection())
//            return;

        fan.setFanLight(!fan.light, new VoidCallback() {

            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });
    }

    void setLevel(final int level) {
//        if (!checkConnection())
//            return;

        fan.setFanLevel((short) level, new VoidCallback() {

            @Override
            public void onSuccess() {
                //showLevel(level);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });
    }

    void setFirstLevel(final int level) {
        Log.i("rent0001",level+"");
//        if (!checkConnection())
//            return;
        fan.setFanLevel((short) level, new VoidCallback() {

            @Override
            public void onSuccess() {
                if (!fan.light) {
                    setLight();
                }
                //showLevel(level);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });
    }

    public void setAnimotion(boolean isLow) {
        if (isLow) {
            //开启弱档动画
            if (operatingAnim != null)
                gearLowImg.startAnimation(operatingAnim);
            gearHighImg.clearAnimation();
            kjImg.clearAnimation();
        } else {
            //开启强档动画
            if (operatingAnim != null)
                Log.i("当前档位为", "" + fan.level);
            gearHighImg.startAnimation(operatingAnim);
            gearLowImg.clearAnimation();
            kjImg.clearAnimation();
        }
    }

    private void initAnimotion(Context cx) {
        //工作动画初始化
        initWorkDh(cx);
        //油网动画初始化
        //画圈动画
        initDrawCircleDh(cx);
        //开机动画
        initKjDh(cx);


    }

    boolean checkConnection() {
        if (!fan.isConnected()) {
            ToastUtils.showShort(R.string.fan_invalid_error);
            return false;
        } else {
            return true;
        }
    }


    private void initWorkDh(Context cx) {
        operatingAnim = AnimationUtils.loadAnimation(cx, R.anim.fan_run_rotate);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        gearLowImg = gearLow.getImg();
        gearHighImg = gearHigh.getImg();
    }


    //当处于清洗锁定状态时，按钮不可以被点击
    private void setCleanLockUnclick() {
        img_logo.setEnabled(false);
        kjDh.setEnabled(false);
        delaydh.setEnabled(false);
        gearLow.setEnabled(false);
        gearHigh.setEnabled(false);
    }

    //清洗结束，解锁按钮
    private void setCleanLockClick() {
        img_logo.setVisibility(VISIBLE);
        kjDh.setVisibility(VISIBLE);
        kjDh.setEnabled(true);
        delaydh.setEnabled(true);
        img_logo.setEnabled(true);
    }

    //初始化画圆动画
    private void initDrawCircleDh(Context context) {
        drawable = new CircularProgressDrawable.Builder()
                .setRingWidth(2)
                .setOutlineColor(getResources().getColor(R.color.c01))
                .setRingColor(getResources().getColor(R.color.home_bg))
                .setCenterColor(getResources().getColor(R.color.Black_80))
                .create();
        ivDrawable.setImageDrawable(drawable);
        logoSmallToLargeAnimation = logoSmallToLarge();
        currentAnimation = prepareStyle3Animation();
        currentAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // kjImg.startAnimation(imgPushIn);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    //初始化开机动画
    private void initKjDh(final Context context) {
        imgPushOut = AnimationUtils.loadAnimation(context, R.anim.push_out);
        imgPushOut.setAnimationListener(imgPushOut_Listeren);
        //圆圈从小到大
        imgPushIn = AnimationUtils.loadAnimation(context, R.anim.push_in);
        imgPushIn.setAnimationListener(imgPushIn_Listeren);

        kjimgPushOut= AnimationUtils.loadAnimation(context, R.anim.push_out);
        kjimgPushOut.setAnimationListener(kjimgPushOut_Listeren);
        kjimgPushOut_notchange=AnimationUtils.loadAnimation(context, R.anim.push_nochange);
        kjimgPushOut_notchange.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    Animation.AnimationListener kjimgPushOut_Listeren=new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            kjImg.clearAnimation();
            if(stop6_mflag)
                return;
            if (kjimgPushOut == null)
                kjimgPushOut = AnimationUtils.loadAnimation(getContext(), R.anim.push_out);
            kjimgPushOut.setAnimationListener(this);
            kjImg.startAnimation(kjimgPushOut);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
    Animation.AnimationListener imgPushOut_Listeren=new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            img_delay.clearAnimation();
            if (imgPushIn == null)
                imgPushIn = AnimationUtils.loadAnimation(getContext(), R.anim.push_in);
            imgPushIn.setAnimationListener(imgPushIn_Listeren);
            img_delay.startAnimation(imgPushIn);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
    Animation.AnimationListener imgPushIn_Listeren= new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            img_delay.clearAnimation();
            if (imgPushOut == null)
                imgPushOut = AnimationUtils.loadAnimation(getContext(), R.anim.push_out);
            imgPushOut.setAnimationListener(imgPushOut_Listeren);
            img_delay.startAnimation(imgPushOut);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };


    private Animator prepareStyle3Animation() {
        ObjectAnimator invertedProgress = ObjectAnimator.ofFloat(drawable, CircularProgressDrawable.PROGRESS_PROPERTY, 0f, 1.0f);
        invertedProgress.setDuration(8000);
        invertedProgress.setInterpolator(new OvershootInterpolator());
        return invertedProgress;
    }

    //老板图标从小到大变化
    private Animator logoSmallToLarge() {
        Animator invertedCircle = ObjectAnimator.ofFloat(drawable, CircularProgressDrawable.CIRCLE_SCALE_PROPERTY, 0.75f, 0.85f);
        invertedCircle.setDuration(8000);
        invertedCircle.setStartDelay(800);
        invertedCircle.setInterpolator(new OvershootInterpolator());
        return invertedCircle;
    }


    void setStatus(short status) {

        if (!checkConnection())
            return;

        fan.setFanStatus(status, new VoidCallback() {

            @Override
            public void onSuccess() {
                onRefresh();
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });
    }

    private Timer timer;
    private int index = 0;
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.getData().getInt("type")==1){
                switch (msg.getData().getInt("index")) {
                    case 1:
                        index++;Log.i("handleMessage","-->"+index+"++");
                        kjImg.clearAnimation();
                        kjImg.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_out));
                        break;
                    case 2:
                        index++;Log.i("handleMessage","-->"+index+"++");
                        kjImg.clearAnimation();
                        kjImg.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_in));
                        break;
                }
            }
            super.handleMessage(msg);
        }
    };
    private void DelayImgReceyle() {
        timer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                    if (index == 2) {
                        index = 0;
                    }
                    index++;Log.i("runrun","-->"+index+"++");
                    Message message = new Message();
                    message.what = index;
                    Bundle bundle=new Bundle();
                    bundle.putInt("type",1);
                    bundle.putInt("index",index);
                    message.setData(bundle);
                    handler.sendMessage(message);
            }
        };
        timer.schedule(task, 0, 4000); // 延时0ms后执行，4000ms执行一次
    }

    private void DelayImgReceyleCancel(){
        if (timer != null) {
            timer.cancel();// 退出计时器
        }
        timer = null;
    }


}
