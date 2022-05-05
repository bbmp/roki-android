package com.robam.roki.ui.view;


import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.legent.VoidCallback;
import com.robam.common.pojos.device.Sterilizer.ISterilizer;
import com.robam.common.pojos.device.Sterilizer.Steri829;
import com.robam.common.pojos.device.Sterilizer.SteriStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.SterilizerAnimationUtil;
import com.robam.roki.ui.UIListeners;
import com.robam.roki.ui.page.DeviceSterilizerPage;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.roki.utils.StringConstantsUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by zhaiyuanyi on 15/10/20.
 */
public class SteriCtr829View extends FrameLayout implements UIListeners.ISteriCtrView {
    Steri829 steri;
    SterilizerAnimationUtil animationUtil;
    //警告弹框资源图片
    int[] imgs = {R.mipmap.img_steri_wran_door, R.mipmap.img_steri_wran_tem};
    @InjectView(R.id.tv_order_btn)
    TextView order;
    @InjectView(R.id.tv_stoving_btn)
    TextView stoving;
    @InjectView(R.id.tv_clean_btn)
    TextView clean;
    @InjectView(R.id.tv_sterilizer_btn)
    TextView sterilizer;
    @InjectView(R.id.rl_tem)
    RelativeLayout rlTem;
    @InjectView(R.id.tv_steri_tem)
    TextView tv_steri_tem;
    @InjectView(R.id.sterilizer_switch)
    CheckBox steriSwitch;
    @InjectView(R.id.tv_steri)
    TextView tvSteriRunning;
    @InjectView(R.id.tv_steri_time_hour)
    TextView txtHour;
    @InjectView(R.id.tv_steri_time_minute)
    TextView txtMinute;
    @InjectView(R.id.tv_steri_time_point)
    TextView txtPoint;
    @InjectView(R.id.ll_empty)
    LinearLayout imgEmpty;
    @InjectView(R.id.ll_animation)
    LinearLayout llAnimation;

    @InjectView(R.id.rl_germ)
    RelativeLayout rlGerm;
    @InjectView(R.id.tv_steri_germ)
    TextView tv_steri_germ;

    @InjectView(R.id.rl_hum)
    RelativeLayout rlHum;
    @InjectView(R.id.tv_steri_hum)
    TextView tv_steri_hum;

    @InjectView(R.id.rl_ozone)
    RelativeLayout rlOzone;
    @InjectView(R.id.tv_steri_ozone)
    TextView tv_steri_ozone;
    @InjectView(R.id.rl_running)
    RelativeLayout rlRunning;
    @InjectView(R.id.rl_switch)
    RelativeLayout rlSwitch;
    @InjectView(R.id.fl_running)
    FrameLayout fl_running;
    @InjectView(R.id.tv_done)
    TextView tv_done;
    Context context;
    private IRokiDialog mClosedialog;
    private IRokiDialog mDialogToast;
    private IRokiDialog mStovingDialog;
    List<String> mListStoving = Lists.newArrayList();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String s = msg.what % 2 == 0 ? "" : ":";
            txtPoint.setText(s);
        }
    };
    private IRokiDialog iRokiOrderDialog;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    setDeviceOrderRunData((String) msg.obj);
                    break;
                case 2:
                    setStovingModelData((String)msg.obj);
                    break;
            }
        }
    };




    public SteriCtr829View(Context context) {
        super(context);
        init(context, null);
    }

    public SteriCtr829View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {
        this.context = cx;
        View view = LayoutInflater.from(cx).inflate(R.layout.view_829,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
            animationUtil = new SterilizerAnimationUtil(cx, rlTem, rlHum, rlGerm, rlOzone);
            animationUtil.setAnimation();
            setPoint();
        }
        mClosedialog = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_10);
        mDialogToast = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_09);
    }

    @Override
    public void attachSteri(ISterilizer steri) {
        Preconditions.checkState(steri instanceof Steri829, "attachFan error:not 829");
        this.steri = (Steri829) steri;
    }

    @Override
    public void onRefresh() {
        if (steri == null)
            return;
        //断开连接
        if (!steri.isConnected()) {
            steriSwitch.setClickable(false);
            steriSwitch.setEnabled(false);
        } else {
            steriSwitch.setClickable(true);
            steriSwitch.setEnabled(true);
        }
        //消毒柜警告状态判断
        //JudgeWran();
        //设置控制按钮状态
        setRunningState();
        //设置消毒时间
        setRunningTime();
        initData();
    }

    //电源开关
    @OnClick(R.id.sterilizer_switch)
    public void onClickSwitch(View v) {

        if (deviceNotConnectdToast()) return;

         if (steri.status == 0 && steri.isConnected()){
               boolean selected = ((CheckBox) v).isChecked();
               setBtnSelected(selected);
               setStatus(selected);
        }else if (steri.status == 1){
             setStatus(false);
        }else if (steri.status == 6){
            if (steri.AlarmStautus == 0 && !DeviceSterilizerPage.alarm) {
                gatingAlarmHint();
            }
            return;
        }
    }

    //预约消毒控制
    @OnClick(R.id.tv_order_btn)
    public void onClickOrderRunning() {
        deviceListenerAndToast(steri.status);
        if (steri.status == 6) {
            gatingAlarmHint();
        }
        if (steri.status == 1) {
            onStartOrderClock();
        }

    }

    //暖烘控制
    @OnClick(R.id.tv_stoving_btn)
    public void onClickStoveRunning() {
        deviceListenerAndToast(steri.status);
        if (steri.status == 6) {
            gatingAlarmHint();
        }
        if (steri.status == 1) {
            onStartDryingClock();
        }

    }

    //快洁控制
    @OnClick(R.id.tv_clean_btn)
    public void onClickCleanRunning() {
        deviceListenerAndToast(steri.status);
        if (steri.status == 6) {
            gatingAlarmHint();
        }
        if (steri.status == 1) {
            steri.setSteriClean((short) 60, new VoidCallback() {
                @Override
                public void onSuccess() {
                }
                @Override
                public void onFailure(Throwable t) {
                }
            });
        }
    }

    //消毒控制
    @OnClick(R.id.tv_sterilizer_btn)
    public void onClickSteriRunning() {
        deviceListenerAndToast(steri.status);
        if (steri.status == 6) {
            gatingAlarmHint();
        }
        if (steri.status == 1) {
            steri.setSteriDisinfect((short) 150, new VoidCallback() {
                @Override
                public void onSuccess() {
                }
                @Override
                public void onFailure(Throwable t) {
                }
            });
        }
    }

    //门控报警提示
    private void gatingAlarmHint() {
        mDialogToast.setToastShowTime(DialogUtil.LENGTH_CENTER);
        mDialogToast.setContentText(R.string.device_alarm_gating_content);
        mDialogToast.show();
    }

    //停止工作
    @OnClick(R.id.fl_running)
    public void onClickStopWorking(View v) {
//        stopWorking();
    }


    //停止工作并断开电源
    @OnClick(R.id.sterilizer_switch_run)
    public void onClickTakeOff(View v) {
        stopWorking(true);
    }

    //停止工作
    private void stopWorking(final boolean lean) {
        mClosedialog.setTitleText(R.string.close_work);
        mClosedialog.setContentText(R.string.is_close_work);
        mClosedialog.setOkBtn(R.string.ok_btn, new OnClickListener() {
            @Override
            public void onClick(View view) {
                mClosedialog.dismiss();
                setStatus(lean);
            }
        });
        mClosedialog.setCancelBtn(R.string.can_btn, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClosedialog.isShow()){
                    mClosedialog.dismiss();
                }
            }
        });
        mClosedialog.show();
    }

    private void initData() {
        short temp = steri.temp;
        short germ = steri.germ;
        short hum = steri.hum;
        short ozone = steri.ozone;
        tv_steri_tem.setText(String.valueOf(temp));
        tv_steri_germ.setText(String.valueOf(germ));
        tv_steri_hum.setText(String.valueOf(hum));
        tv_steri_ozone.setText(String.valueOf(ozone));
    }

    private void setRunningState() {
        if (steri.status == 2 || steri.status == 3 || steri.status == 4 || steri.status == 5) {
            llAnimation.setVisibility(VISIBLE);
            imgEmpty.setVisibility(GONE);
            rlRunning.setVisibility(VISIBLE);
            rlSwitch.setVisibility(GONE);
            fl_running.setBackground(context.getResources().getDrawable(R.mipmap.img_order_run));
            txtHour.setVisibility(VISIBLE);
            txtMinute.setVisibility(VISIBLE);
            txtPoint.setVisibility(VISIBLE);
            tvSteriRunning.setVisibility(VISIBLE);
            tv_done.setVisibility(GONE);
            if (steri.status == 2)
                tvSteriRunning.setText("消毒中");
            if (steri.status == 3)
                tvSteriRunning.setText("快洁中");
            if (steri.status == 4)
                tvSteriRunning.setText("暖烘中");
            if (steri.status == 5)
                tvSteriRunning.setText("预约计时中");
            if (PageKey.STERI_CD_FLAG) {
                IRokiDialog rokiCountDownDialog = RokiDialogFactory.createDialogByType(getContext(),DialogUtil.DIALOG_TYPE_07);
                rokiCountDownDialog.show();
                PageKey.STERI_CD_FLAG = false;
            }
        } else {
            if (steri.status == 1) {
                setBtnSelected(true);
                steriSwitch.setChecked(true);
            } else if (steri.status == 0){
                setBtnSelected(false);
                steriSwitch.setChecked(false);
            }
            PageKey.STERI_CD_FLAG = true;
            if (steri.status == 0 || steri.status == 1) {
                if (steri.oldstatus == 2 || steri.oldstatus == 3 || steri.oldstatus == 4
                        || steri.oldstatus == 5) {
                    fl_running.setBackground(context.getResources().getDrawable(R.mipmap.img_oven_finish));
                    txtHour.setVisibility(GONE);
                    txtMinute.setVisibility(GONE);
                    txtPoint.setVisibility(GONE);
                    tvSteriRunning.setVisibility(GONE);
                    tv_done.setVisibility(VISIBLE);
                    return;
                }
            }
            llAnimation.setVisibility(GONE);
            imgEmpty.setVisibility(VISIBLE);
            rlRunning.setVisibility(GONE);
            rlSwitch.setVisibility(VISIBLE);
        }
    }

    private void setRunningTime() {
        int time = 0;
        if (steri.status == 5) {
            time = (steri.work_left_time_l + steri.work_left_time_h) * 60;
        } else {
            time = steri.work_left_time_l + steri.work_left_time_h;
        }
        String hour = time / 60 < 10 ? "0" + time / 60 : String.valueOf(time / 60);
        String minute = time % 60 == 0 ? "00" : time % 60 < 10 ? "0" + time % 60 : String.valueOf(time % 60);
        txtHour.setText(hour);
        txtMinute.setText(minute);
    }

    public void setBtnSelected(boolean btnSelected) {
        order.setSelected(btnSelected);
        stoving.setSelected(btnSelected);
        clean.setSelected(btnSelected);
        sterilizer.setSelected(btnSelected);
        if (sterilizer.isSelected()) {
            sterilizer.setTextColor(Color.parseColor("#ffffff"));
        } else {
            sterilizer.setTextColor(Color.parseColor("#575757"));
        }
    }
    //监听设备并作出提示语句
    private void deviceListenerAndToast(short status) {
        if (deviceNotConnectdToast()) return;
        if (status == 0){
            mDialogToast.setContentText(R.string.open_device);
            mDialogToast.setToastShowTime(DialogUtil.LENGTH_CENTER);
            mDialogToast.show();
        }
    }

    //设备是否在线提醒
    private boolean deviceNotConnectdToast() {
        if (!steri.isConnected()){
            if (null != mDialogToast){
                mDialogToast.setContentText(R.string.device_connected);
                mDialogToast.setToastShowTime(DialogUtil.LENGTH_CENTER);
                mDialogToast.show();
            }
            return true;
        }
        return false;
    }


    public void setStatus(boolean witchStatus) {
        short status = witchStatus ? SteriStatus.On : SteriStatus.Off;
        steri.setSteriPower(status, new VoidCallback() {
            @Override
            public void onSuccess() {}
            @Override
            public void onFailure(Throwable t) {}
        });
    }

    //设置暖烘模式
    void onStartDryingClock() {

        mStovingDialog = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_03);
        mStovingDialog.setWheelViewData(null, getStovingModelList(), null, false, 0, 3, 0, null, new OnItemSelectedListenerCenter() {
            @Override
            public void onItemSelectedCenter(String contentCenter) {
                Message message = mHandler.obtainMessage();
                message.what = 2;
                message.obj = contentCenter;
                mHandler.sendMessage(message);
            }
        },null);
        mStovingDialog.setCanceledOnTouchOutside(true);
        mStovingDialog.show();
    }

    //设置暖烘数据类型
    private void setStovingModelData(String data) {

        if (data.contains(StringConstantsUtil.STRING_MINUTES)){
            String newData = RemoveManOrsymbolUtil.getRemoveString(data);
            mListStoving.add(newData);
        }
        mStovingDialog.setOkBtn(R.string.ok_btn, new OnClickListener() {
            @Override
            public void onClick(View v) {
                mStovingDialog.dismiss();
                steri.setSteriDrying(Short.parseShort(String.valueOf(mListStoving.get(mListStoving.size()-1))),
                        new VoidCallback() {
                    @Override
                    public void onSuccess() {}
                    @Override
                    public void onFailure(Throwable t) {}
                });
            }
        });

    }

    protected List<String> getListTime() {
        List<String> listime = Lists.newArrayList();

            for (int i = 1; i <= 24; i++) {
                listime.add(i + StringConstantsUtil.STRING_HOUR);
            }
        return listime;
    }

    private List<String> getStovingModelList(){
        List<String> listStov = Lists.newArrayList();
        String stov80 = "烘干 " + 80 + StringConstantsUtil.STRING_MINUTES;
        String stov60 = "烘干 " + 60 + StringConstantsUtil.STRING_MINUTES;
        String wenDie = "温碟 " + 10 + StringConstantsUtil.STRING_MINUTES;
        String nuanDie = "暖碟 " + 15 + StringConstantsUtil.STRING_MINUTES;
        String reDie = "热蝶 " + 20 + StringConstantsUtil.STRING_MINUTES;
        listStov.add(stov80);
        listStov.add(stov60);
        listStov.add(wenDie);
        listStov.add(nuanDie);
        listStov.add(reDie);
        return listStov;
    }

    //设置预约倒计时
    void onStartOrderClock() {
        iRokiOrderDialog = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_03);
        iRokiOrderDialog.setWheelViewData(null, getListTime(), null, false, 0, 11, 0, null, new OnItemSelectedListenerCenter() {
            @Override
            public void onItemSelectedCenter(String contentCenter) {
                Message message = mHandler.obtainMessage();
                message.what = 1;
                message.obj = contentCenter;
                mHandler.sendMessage(message);
            }
        },null);

        iRokiOrderDialog.show();

    }

    //设置预约时间
    private void setDeviceOrderRunData(String str) {
        final String removeString = RemoveManOrsymbolUtil.getRemoveString(str);
        iRokiOrderDialog.setOkBtn(R.string.ok_btn, new OnClickListener() {
            @Override
            public void onClick(View v) {
                iRokiOrderDialog.dismiss();
                steri.SetSteriReserveTime(Short.parseShort(removeString), new VoidCallback() {
                    @Override
                    public void onSuccess() {}
                    @Override
                    public void onFailure(Throwable t) {}
                });

            }
        });
        iRokiOrderDialog.setCancelBtn(R.string.can_btn, new OnClickListener() {
            @Override
            public void onClick(View v) {}
        });

    }

    //时间显示闪烁
    private void setPoint() {
        Timer timer = new Timer();
        MyTask myTask = new MyTask();
        timer.schedule(myTask, 0, 1000);
    }

    class MyTask extends TimerTask {
        //时间显示中间两点闪烁标志位
        int i = 0;

        @Override
        public void run() {
            if (rlRunning.getVisibility() == GONE)
                return;
            Message message = handler.obtainMessage();
            message.what = i;
            handler.sendMessage(message);
            i++;
        }
    }

}

