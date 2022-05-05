package com.robam.roki.ui.view;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.ui.ext.dialogs.DialogHelper;
import com.legent.ui.ext.dialogs.NumberDialog;
import com.legent.utils.EventUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.FanTimingCompletedEvent;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.Fan8229;
import com.robam.common.pojos.device.fan.IFan;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.ui.UIListeners;
import com.robam.roki.utils.ToolUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

/**
 * Created by zhaiyuanyi on 15/10/20.
 */
public class FanCtr8229View extends FrameLayout implements UIListeners.IFanCtrView {
    Fan8229 fan;

    @InjectView(R.id.imgLight)
    ImageView imgLight;

    @InjectView(R.id.img_clock_m)
    ImageView img_clock_m;

    @InjectView(R.id.tvCountdownTime_m)
    TextView CountdownTime_m;

    @InjectViews({R.id.gear1, R.id.gear2, R.id.gear3, R.id.gear6})
    List<DeviceFanGearView> gearViews;

    boolean flag_dialogshow = false;

    public FanCtr8229View(Context context) {
        super(context);
        init(context, null);
    }

    public FanCtr8229View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventUtils.regist(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventUtils.unregist(this);
    }

    @Subscribe
    public void onEvent(FanTimingCompletedEvent event) {
        if (event.flag_countend == 0)                   //倒计时结束，烟机端报警，事件上报
        {
            //是否取消报警
            if (flag_dialogshow==false) {
                onCancelAlarm();
            }else{
                return;
            }

        }

    }

    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_8229,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);

            gearViews.get(0).setTag(AbsFan.PowerLevel_1);
            gearViews.get(1).setTag(AbsFan.PowerLevel_2);
            gearViews.get(2).setTag(AbsFan.PowerLevel_3);
            gearViews.get(3).setTag(AbsFan.PowerLevel_6);

//            img_clock_m.setOnTouchListener(new OnTouchListenerWithAntiShake(img_clock_m, new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //启动设置倒计时
//                    onStartClock();
//                }
//            }));
//            CountdownTime_m.setOnTouchListener(new OnTouchListenerWithAntiShake(CountdownTime_m, new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //停止倒计时
//                    onStopClock();
//                }
//            }));

            changeClockViewStatus((short) 0);
        }


    }

    @OnClick(R.id.imgLight)
    public void onClickLight() {
        setLight();
    }

    @OnClick(R.id.divClock)
    public void onClickClock() {
        if (fan.timeWork>0) {

            onStopClock();

        }else {

            onStartClock();
        }

    }



    @OnClick({R.id.gear1, R.id.gear2, R.id.gear3, R.id.gear6})
    public void onClickGear(View v) {
        short level = (Short) v.getTag();
        if (level == fan.level) {
            setLevel(AbsFan.PowerLevel_0);
        } else {
            setLevel(level);
        }
    }

    @Override
    public void attachFan(IFan fan) {
        this.fan = (Fan8229) fan;
    }

    @Override
    public void onRefresh() {
        int showtime = fan.timeWork ;
        if (fan == null)
            return;
        imgLight.setSelected(fan.light);
        showLevel(fan.level);

        changeClockViewStatus(showtime);


    }

    void showLevel(int level) {
        for (DeviceFanGearView v : gearViews) {
            v.setSelected(false);
        }

        if (level == AbsFan.PowerLevel_0)
            return;

        for (DeviceFanGearView v : gearViews) {
            if ((Short) v.getTag() == level) {
                v.setSelected(true);
                break;
            }
        }

    }

    void setLight() {
//        if (!checkConnection())
//            return;

        fan.setFanLight(!fan.light, new VoidCallback() {

            @Override
            public void onSuccess() {
                imgLight.setSelected(fan.light);
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
                showLevel(level);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });

    }

    boolean checkConnection() {
        if (!fan.isConnected()) {
            ToastUtils.showShort(R.string.fan_invalid_error);
            return false;
        } else {
            return true;
        }
    }

    //设置倒计时
    void onStartClock() {
        final short currentFanlevel = fan.level;
        String title = "设置倒计时";
        NumberDialog.show(getContext(), title, 0, 60, 30,
                new NumberDialog.NumberSeletedCallback() {

                    @Override
                    public void onNumberSeleted(final int value) {
                        fan.setFanTimeWork(currentFanlevel, (short) value, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                //changeClockViewStatus((short)value);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.showThrowable(t);
                            }
                        });
                    }
                });
    }

    //停止倒计时
    void onStopClock() {

            String message = "确定要关闭倒计时吗？";
            final short currentFanlevel = fan.level;
            DialogHelper.newDialog_OkCancel(getContext(), null, message,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dlg, int witch) {
                            if (witch == DialogInterface.BUTTON_POSITIVE) {
                                fan.setFanTimeWork(currentFanlevel, (short) 0, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onFailure(Throwable t) {

                                    }
                                });
                            }
                        }
                    }).show();
    }

    void onCancelAlarm() {
        final short currentFanlevel = fan.level;
        flag_dialogshow =true;
        String message = "时间到，请关闭提醒";
        DialogHelper.newDialog_OkCancel(getContext(), null, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == DialogInterface.BUTTON_POSITIVE) {
                    flag_dialogshow = false;
                    fan.setFanTimeWork(currentFanlevel, (short) 0, new VoidCallback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
                }else if (i==DialogInterface.BUTTON_NEGATIVE)
                {
                    flag_dialogshow = false;
                }
            }
        }).show();
    }


    private void showFinishDialog() {

        this.post(new Runnable() {

            @Override
            public void run() {
                String message = "倒计时结束";
                DialogHelper.newOKDialog(getContext(), null, message, null)
                        .show();
            }
        });

    }

    void changeClockViewStatus(int time) {
        //currentClockTime = time;
        boolean isClock = time > 0;
        img_clock_m.setSelected(isClock);
        CountdownTime_m.setVisibility(isClock ? VISIBLE : GONE);

        String strTime = String.valueOf(time);//UiHelper.second2String(time);
        CountdownTime_m.setText(strTime+" 分钟");
    }

}
