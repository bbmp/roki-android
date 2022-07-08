package com.robam.roki.ui.adapter;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.TimeUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.base.BaseDialog;
import com.robam.common.events.FanStatusChangedEvent;
import com.robam.common.events.KitchenCleanEvent;
import com.robam.common.events.KitchenCleanGearChangeEvent;
import com.robam.common.pojos.FanStatusComposite;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.FanStatus;
import com.robam.common.util.AlarmManagerUtil;
import com.robam.common.util.NotificationManagerUtil;
import com.robam.roki.R;
import com.robam.roki.broadcast.Fan8700alarmReceiver;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.model.bean.FanDeviceRemindSoup;
import com.robam.roki.model.bean.FanKitchenCleanParams;
import com.robam.roki.model.bean.FanMainParams;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;
import com.robam.roki.ui.activity3.device.fan.FanLinkageActivity;
import com.robam.roki.ui.activity3.device.fan.adapter.RvPickerAdapter;
import com.robam.roki.ui.dialog.AbsFanTimingDialog;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.mdialog.PickerLayoutManager;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.TestDatas;
import com.robam.roki.utils.ToolUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.legent.ContextIniter.cx;
import static com.robam.roki.ui.adapter.FanBackgroundFuncAdapter.timeRemindingIsSend;


/**
 * Created by 14807 on 2018/1/24.
 * 烟机主功能区域适配器
 */

public class FanOtherFuncAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int MAIN_VIEW = 1;
    public static final int OTHER_VIEW = 2;

    private LayoutInflater mInflater;
    private Context mContext;
    private List<DeviceConfigurationFunctions> mDates;
    private AbsFan fan;
    private IRokiDialog mRokiDialog;
    private short mLevel;
    private short smokeStatus;
    public static String SOUP_REMIND;//计时提醒本地存储key
    public static String CLEAN_REMIND;////厨房净化本地存储key
    public static String IS_LOCAL_START_REMIND;//是否是当前设备开启的倒计时
    Timer timer_clean;//厨房净化倒计时
    Timer timer_remind;//煲汤提醒倒计时

    int count_clean;//厨房净化倒计时
    int count_remind;//煲汤提醒倒计时

    private short min;

    boolean SIGN = true;//标记
    private AbsFanTimingDialog absFanTimingDialog;
    boolean isCountdown = false;
    private IRokiDialog remindSoupNoticeDialog;

    FanStatusComposite fanStatusComposite = new FanStatusComposite();

    boolean isFeelPower = false;
    private RvPickerAdapter rvPickerAdapter;

    public FanOtherFuncAdapter(Context context, List<DeviceConfigurationFunctions> dates, AbsFan fan) {
        mContext = context;
        mDates = dates;
        this.fan = fan;
        mInflater = LayoutInflater.from(context);
        EventUtils.regist(this);
        initView();
    }


    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if ("5010S".equals(fan.getDt())) {
                        return;
                    }
                    kitchenClean(msg.arg1);
                    break;
                case 2:
                    remind(msg.arg1);
                    break;
                case 3:
                    kitchenCleanSelectData((String) msg.obj, msg.arg1);
                    break;
            }
        }
    };

    private void kitchenCleanSelectData(String date, final int pos) {
        if (TextUtils.isEmpty(date)) return;
        short time = 0;
        short power = 0;
        List<DeviceConfigurationFunctions> kitchenData = getKitchenData();
        if (kitchenData == null || kitchenData.size() == 0) return;
        for (int i = 0; i < kitchenData.size(); i++)
            if (date.equals(kitchenData.get(i).functionName)) {
                String functionParams = kitchenData.get(i).functionParams;
                try {
                    FanKitchenCleanParams kitchenCleanParams = JsonUtils.json2Pojo(functionParams, FanKitchenCleanParams.class);
                    time = (short) kitchenCleanParams.getParam().getMinute().getValue();
                    power = (short) kitchenCleanParams.getParam().getPower().getValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        if (mRokiDialog != null) {
            final short finalTime = time;
            final short finalPower = power;
            mRokiDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRokiDialog.dismiss();
                    if (fan == null) {
                        return;
                    }
                    if (isFeelPower) {
                        fanStatusComposite.FanFeelPower = (short) 0;
                        fan.setFanSmartSmoke(fanStatusComposite, (short) 1, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                notifyItemChanged(3);
                                if (fan != null) {
                                    switch (finalPower) {
                                        case 1:
                                            ToolUtils.logEvent(fan.getDt(), "厨房净化:小", "roki_设备");
                                            break;
                                        case 2:
                                            ToolUtils.logEvent(fan.getDt(), "厨房净化:中", "roki_设备");
                                            break;
                                        case 3:
                                            ToolUtils.logEvent(fan.getDt(), "厨房净化:大", "roki_设备");
                                            break;
                                    }
                                }
                                setCleaningLevel(finalPower, finalTime, pos);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                LogUtils.i("201910261111", t.getMessage());
                            }
                        });
                        return;
                    }

                    if (fan != null) {
                        switch (finalPower) {
                            case 1:
                                ToolUtils.logEvent(fan.getDt(), "厨房净化:小", "roki_设备");
                                break;
                            case 2:
                                ToolUtils.logEvent(fan.getDt(), "厨房净化:中", "roki_设备");
                                break;
                            case 3:
                                ToolUtils.logEvent(fan.getDt(), "厨房净化:大", "roki_设备");
                                break;
                        }
                    }
                    setCleaningLevel(finalPower, finalTime, pos);
                }
            });
            mRokiDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

    @Subscribe
    public void onEvent(KitchenCleanEvent event) {
        initView();
    }


    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (fan == null || !Objects.equal(fan.getID(), event.device.getID()))
            return;
        if (event.isConnected) {
            fan = (AbsFan) event.device;

        }
    }

    @Subscribe
    public void onEvent(FanStatusChangedEvent event) {
        if (fan == null || !Objects.equal(fan.getID(), event.pojo.getID()))
            return;
        fan = event.pojo;
        mLevel = fan.level;
        smokeStatus = fan.smartSmokeStatus;

        if (PreferenceUtils.containKey(CLEAN_REMIND)) {
            if (mLevel == 0 || mLevel != 1) {
                count_clean = 0;
            }

            if ("8235S".equals(fan.getDt()) || "R68A0".equals(fan.getDt()) || "68A0S".equals(fan.getDt())) {
                if (fan.timeWork == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            PreferenceUtils.remove(CLEAN_REMIND);
                            PreferenceUtils.remove("dialogClean");
                            count_clean = 0;
                            notifyDataSetChanged();
                        }
                    }, 500);
                }
            }


        }
        //做了一大堆只为关机时不刷新改状态
        if (mLevel > 0) {
            SIGN = true;
            notifyItemRangeChanged(0, 3);
        }

        if (fan.status == FanStatus.Off && SIGN || fan.status == FanStatus.On && SIGN) {
            if (mLevel == 0) {
                notifyItemRangeChanged(0, 3);
                SIGN = false;
            }
        }

        if (smokeStatus == 0) {
            isFeelPower = false;
            notifyItemChanged(3, 1);

        } else {
            isFeelPower = true;
            notifyItemChanged(3, 1);
        }
        //判断是否发送168指令 如果发送 走这个判断 其他计时都是本地判断
        if (timeRemindingIsSend) {
            short pollingTime = fan.periodicallyRemainingTime;
            //轮询时间和本地时间相等
            if (remindSoupNoticeDialog != null && remindSoupNoticeDialog.isShow()) {
                pollingTime = 0;
            }
            if (isCountdown && count_remind == -1) {
                pollingTime = 0;
            }

            if (pollingTime == 0) {
                closeCountDown();
            } else {
                if (count_remind == 0 || count_remind == -1 || count_remind > 56) {
                    PreferenceUtils.setInt("soupRemind", 4);
                    long time1 = System.currentTimeMillis();
                    long time = pollingTime * 60 * 1000;
                    PreferenceUtils.setString(SOUP_REMIND, time1 + ":" + time);
                    boolean isCountdownFan = PreferenceUtils.getBool("isCountdownFan", false);
                    if (!isCountdownFan) {
                        count_remind = (short) (pollingTime * 60);
                        startBroadcastReceiver(time1 + time);
                        LogUtils.i("20200325666", "55555----time1 + time:::" + time1 + time);
                    }
                }
                startCountDown(4);
            }
        }
        if (count_remind > 0) {
            PreferenceUtils.setBool("isCountdownFan", true);
        }
    }

    public void setFan(AbsFan fan) {
        this.fan = fan;
        notifyItemChanged(0);
    }

    @Subscribe
    public void onEvent(KitchenCleanGearChangeEvent event) {
        if (!fan.isConnected()) return;
        switchCountDown_clean(false, PreferenceUtils.getInt("dialogClean", -1));
    }

    //结束计时提醒
    private void remind(int pos) {
        if (count_remind < 0) return;
        if (count_remind == 0) {
            isCountdown = PreferenceUtils.getBool("isCountdownFan", false);
            LogUtils.i("20200325666", "isCountdown:" + isCountdown);
            if (isCountdown) {
                if (remindSoupNoticeDialog != null && remindSoupNoticeDialog.isShow()) {
                    return;
                }
                remindSoupNoticeDialog = RokiDialogFactory.createDialogByType(mContext, DialogUtil.DIALOG_TYPE_16);
                remindSoupNoticeDialog.setTitleText(R.string.fan_remind_timing);
                remindSoupNoticeDialog.setContentText(R.string.device_time_remind_desc);
                remindSoupNoticeDialog.setOkBtn(R.string.fan_Know, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (timeRemindingIsSend) {
                            fan.setTimingRemind((short) 0, (short) 0, new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    //switchCountDown_remind(false, PreferenceUtils.getInt("soupRemind", -1));

                                }

                                @Override
                                public void onFailure(Throwable t) {
                                }
                            });

                        }
                        closeCountDown();
                        isCountdown = false;
                        remindSoupNoticeDialog.dismiss();
                    }
                });
                remindSoupNoticeDialog.show();

                NotificationManager notificationManager = NotificationManagerUtil.getInstance(cx);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(cx);
                mBuilder.setContentTitle("ROKI智能烹饪")//设置通知栏标题
                        .setContentText("计时时间已到，请注意查看关火！") //设置通知栏显示内容
                        .setTicker("计时提醒") //通知首次出现在通知栏，带上升动画效果的
                        .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                        .setChannelId(cx.getPackageName()) //必须添加（Android 8.0） 【唯一标识】
                        .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                        .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                        .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                        //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                        .setSmallIcon(R.mipmap.ic_recipe_roki_logo);//设置通知小ICON
                Intent it = new Intent(cx, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(cx, 0, it, 0);
                mBuilder.setContentIntent(pendingIntent);
                Notification notification = mBuilder.build();
                notification.flags = Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(1, notification);
            }
        }

        try {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            count_remind--;
//            if (mHolder != null) {
//                mHolder.stopAnimation();
//            }
            if (pos != -1) {
                notifyItemChanged(pos);
            }
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        if (OTHER_VIEW == viewType) {
            View view = mInflater.inflate(R.layout.item_fan_otherfunc_page, parent, false);
            FanOtherFuncViewHolder fanOtherFuncViewHolder = new FanOtherFuncViewHolder(view);
            //烟机下面横条的点击事件
            fanOtherFuncViewHolder.mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemEventClick(v);
                }
            });
            return fanOtherFuncViewHolder;
        } else if (MAIN_VIEW == viewType) {
            View view;
            if (mDates.size() <= 3) {
                view = mInflater.inflate(R.layout.item_fan_mainfunc_page, parent, false);
            } else {
                view = mInflater.inflate(R.layout.item_fan_mainfunc_size_page, parent, false);
            }
            MainFuncViewHolder mainFuncViewHolder = new MainFuncViewHolder(view);
            //烟机 炒 煎 炖的点击事件
            mainFuncViewHolder.mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ItemEvent(v);
                }
            });

            return mainFuncViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof FanOtherFuncViewHolder) {
            FanOtherFuncViewHolder fanOtherFuncViewHolder = (FanOtherFuncViewHolder) holder;
            String count_remind_time = null;
            if (timeRemindingIsSend) {
                short periodicallyRemainingTime = fan.periodicallyRemainingTime;
                if (periodicallyRemainingTime == 0) {
                    if (min != 0) {
                        count_remind_time = String.valueOf(TimeUtils.minToHourMin(min));
                    }
                } else {
                    count_remind_time = TimeUtils.minToHourMin(periodicallyRemainingTime);
                }

            } else {
                count_remind_time = TimeUtils.secToHourMinSec(count_remind);
            }
            //解决滑动错乱的问题 直接关闭复用
            fanOtherFuncViewHolder.setIsRecyclable(false);
            String count_clean_time;
            count_clean_time = TimeUtils.secToHourMinSec((short) count_clean);
            //新增 设备返回的计时
            int count_clean_time2 = fan.ventilationRemainingTime;
            if (fan.getDt().equals("5010S")) {
//                count_clean = fan.ventilationRemainingTime ;
                count_clean_time = TimeUtils.secToHourMinSec(count_clean_time2);
            }

            if (mDates != null && mDates.size() > 0) {
                Glide.with(cx).load(mDates.get(position).backgroundImg).transition(withCrossFade()).into(fanOtherFuncViewHolder.mImageView);
                fanOtherFuncViewHolder.mTvName.setText(mDates.get(position).functionName);
                fanOtherFuncViewHolder.mTvDesc.setText(mDates.get(position).msg);
                if ("kitchenCleanup".equals(mDates.get(position).functionCode)) {
                    StringBuilder cleanTime;

                    cleanTime = new StringBuilder(count_clean_time);

                    cleanTime.append(cx.getString(R.string.fan_complete_clean)).append(mDates.get(position).functionName);

                    if (count_clean > 0 || (fan.getDt().equals("5010S") && count_clean_time2 > 0)) {
                        if (!cleanTime.toString().contains("00:00:00")) {
                            fanOtherFuncViewHolder.startAnimation();
                            fanOtherFuncViewHolder.mLlDefaultText.setVisibility(View.GONE);
                            fanOtherFuncViewHolder.mTvRunWorkText.setVisibility(View.VISIBLE);

                            fanOtherFuncViewHolder.mTvRunWorkText.setText(cleanTime);
                        }

                    } else {
                        fanOtherFuncViewHolder.stopAnimation();
                        fanOtherFuncViewHolder.mLlDefaultText.setVisibility(View.VISIBLE);
                        fanOtherFuncViewHolder.mTvRunWorkText.setVisibility(View.GONE);
                        fanOtherFuncViewHolder.mTvName.setText(mDates.get(position).functionName);
                        fanOtherFuncViewHolder.mTvDesc.setText(mDates.get(position).msg);
                    }
                    //计时提醒
                } else if ("timeReminding".equals(mDates.get(position).functionCode)) {

                    StringBuilder remind_time = null;
                    if (timeRemindingIsSend) {

                        if (fan.periodicallyRemainingTime > 0) {
                            remind_time = new StringBuilder(count_remind_time);
                            remind_time.append(cx.getString(R.string.fan_complete_clean)).append(mDates.get(position).functionName);
                            fanOtherFuncViewHolder.mTvRunWorkText.setText(remind_time);

                            fanOtherFuncViewHolder.startAnimation();
                            fanOtherFuncViewHolder.mLlDefaultText.setVisibility(View.GONE);
                            fanOtherFuncViewHolder.mTvRunWorkText.setVisibility(View.VISIBLE);
                        } else {
                            fanOtherFuncViewHolder.stopAnimation();
                            fanOtherFuncViewHolder.mLlDefaultText.setVisibility(View.VISIBLE);
                            fanOtherFuncViewHolder.mTvRunWorkText.setVisibility(View.GONE);
                            fanOtherFuncViewHolder.mTvName.setText(mDates.get(position).functionName);
                            fanOtherFuncViewHolder.mTvDesc.setText(mDates.get(position).msg);
                        }

                    } else {
                        if (count_remind > 0) {
                            remind_time = new StringBuilder(count_remind_time);
                            remind_time.append(cx.getString(R.string.fan_complete_clean)).append(mDates.get(position).functionName);
                            fanOtherFuncViewHolder.mTvRunWorkText.setText(remind_time);

                            fanOtherFuncViewHolder.startAnimation();
                            fanOtherFuncViewHolder.mLlDefaultText.setVisibility(View.GONE);
                            fanOtherFuncViewHolder.mTvRunWorkText.setVisibility(View.VISIBLE);
                        } else {
                            fanOtherFuncViewHolder.stopAnimation();
                            fanOtherFuncViewHolder.mLlDefaultText.setVisibility(View.VISIBLE);
                            fanOtherFuncViewHolder.mTvRunWorkText.setVisibility(View.GONE);
                            fanOtherFuncViewHolder.mTvName.setText(mDates.get(position).functionName);
                            fanOtherFuncViewHolder.mTvDesc.setText(mDates.get(position).msg);
                        }
                    }

                } else if ("ventilation".equals(mDates.get(position).functionCode)) {
                    fanOtherFuncViewHolder.mTvName.setText(mDates.get(position).functionName);
                    fanOtherFuncViewHolder.mTvDesc.setText(mDates.get(position).msg);
                } else if ("smokeStoveLinkage".equals(mDates.get(position).functionCode)) {
                    fanOtherFuncViewHolder.mTvName.setText(mDates.get(position).functionName);
                    fanOtherFuncViewHolder.mTvDesc.setText(mDates.get(position).msg);
                } else if ("oilNetworkDetection".equals(mDates.get(position).functionCode)) {
                    fanOtherFuncViewHolder.mTvName.setText(mDates.get(position).functionName);
                    fanOtherFuncViewHolder.mTvDesc.setText(mDates.get(position).msg);
                } else if ("oilCupworkDetection".equals(mDates.get(position).functionCode)) {
                    fanOtherFuncViewHolder.mTvName.setText(mDates.get(position).functionName);
                    fanOtherFuncViewHolder.mTvDesc.setText(mDates.get(position).msg);
                } else if ("hotOil".equals(mDates.get(position).functionCode)) {
                    fanOtherFuncViewHolder.mTvName.setText(mDates.get(position).functionName);
                    fanOtherFuncViewHolder.mTvDesc.setText(mDates.get(position).msg);
                } else if ("dryCleaning".equals(mDates.get(position).functionCode)) {
                    fanOtherFuncViewHolder.mTvName.setText(mDates.get(position).functionName);
                    fanOtherFuncViewHolder.mTvDesc.setText(mDates.get(position).msg);
                    //过温保护
                } else if ("OTP".equals(mDates.get(position).functionCode)) {
                    fanOtherFuncViewHolder.mTvName.setText(mDates.get(position).functionName);
                    fanOtherFuncViewHolder.mTvDesc.setText(mDates.get(position).msg);
                    //清洗锁定
                } else if ("cleanLock".equals(mDates.get(position).functionCode)) {
                    fanOtherFuncViewHolder.mTvName.setText(mDates.get(position).functionName);
                    fanOtherFuncViewHolder.mTvDesc.setText(mDates.get(position).msg);
                } else if ("gestureControl".equals(mDates.get(position).functionCode)) {
                    fanOtherFuncViewHolder.mTvName.setText(mDates.get(position).functionName);
                    fanOtherFuncViewHolder.mTvDesc.setText(mDates.get(position).msg);
                } else if ("smokeStoveAutoPower".equals(mDates.get(position).functionCode)) {
                    fanOtherFuncViewHolder.mTvName.setText(mDates.get(position).functionName);
                    fanOtherFuncViewHolder.mTvDesc.setText(mDates.get(position).msg);
                }
            }
            fanOtherFuncViewHolder.mItemView.setTag(mDates.get(position).functionCode);
            fanOtherFuncViewHolder.mItemView.setTag(R.id.tag_fan_other_func_key, mDates.get(position).functionParams);
            fanOtherFuncViewHolder.mItemView.setTag(R.id.tag_fan_other_func_key_pos, position);
        } else if (holder instanceof MainFuncViewHolder) {
            MainFuncViewHolder mainFuncViewHolder = (MainFuncViewHolder) holder;
            mainFuncViewHolder.setIsRecyclable(false);
            if (mDates != null && mDates.size() > 0) {
                mainFuncViewHolder.mTvModelName.setText(mDates.get(position).functionName);
                if ("fry".equals(mDates.get(position).functionCode)) {
                    if (mLevel > AbsFan.PowerLevel_3 && mLevel <= AbsFan.PowerLevel_6) {
                        Glide.with(cx).load(mDates.get(position).backgroundImgH).transition(withCrossFade()).
                                into(mainFuncViewHolder.mIvModelImg);
                    } else {
                        Glide.with(cx).load(mDates.get(position).backgroundImg).transition(withCrossFade()).
                                into(mainFuncViewHolder.mIvModelImg);
                    }
                } else if ("decoct".equals(mDates.get(position).functionCode)) {
                    if (mLevel == AbsFan.PowerLevel_3) {
                        Glide.with(cx).load(mDates.get(position).backgroundImgH).transition(withCrossFade()).
                                into(mainFuncViewHolder.mIvModelImg);
                    } else {
                        Glide.with(cx).load(mDates.get(position).backgroundImg).transition(withCrossFade()).
                                into(mainFuncViewHolder.mIvModelImg);
                    }
                } else if ("stew".equals(mDates.get(position).functionCode)) {
                    if (mLevel == AbsFan.PowerLevel_1 || mLevel == AbsFan.PowerLevel_2) {
                        Glide.with(cx).load(mDates.get(position).backgroundImgH).transition(withCrossFade()).
                                into(mainFuncViewHolder.mIvModelImg);
                    } else {
                        Glide.with(cx).load(mDates.get(position).backgroundImg).transition(withCrossFade()).
                                into(mainFuncViewHolder.mIvModelImg);
                    }
                } else if ("smokeFeeling".equals(mDates.get(position).functionCode)) {
                    LogUtils.i("20191026123456", mDates.get(position).backgroundImg);
                    if (isFeelPower) {
                        Glide.with(cx).load(mDates.get(position).backgroundImgH).transition(withCrossFade()).
                                into(mainFuncViewHolder.mIvModelImg);
                    } else {
                        Glide.with(cx).load(mDates.get(position).backgroundImg).transition(withCrossFade()).
                                into(mainFuncViewHolder.mIvModelImg);
                    }


                }
            }
            mainFuncViewHolder.mItemView.setTag(mDates.get(position));
            mainFuncViewHolder.mItemView.setTag(R.id.tag_fan_main_func_key, position);
        }

    }

    @Override
    public int getItemCount() {
        return mDates.size() == 0 ? 0 : mDates.size();
    }

    @Override
    public int getItemViewType(int position) {

        String functionCode = mDates.get(position).functionCode;
        if ("fry".equals(functionCode) || "decoct".equals(functionCode) || "stew".equals(functionCode)
                || "smokeFeeling".equals(functionCode)) {
            return MAIN_VIEW;
        } else {
            return OTHER_VIEW;
        }
    }

    private static final int MIN_CLICK_DELAY_TIME = 500;
    private static long lastClickTime;

    private void ItemEvent(View v) {
        if (!fan.isConnected()) {
            DeviceOfflinePrompt();
            return;
        }
        long curClickTime = System.currentTimeMillis();
        DeviceConfigurationFunctions tag = (DeviceConfigurationFunctions) v.getTag();
        int position = (int) v.getTag(R.id.tag_fan_main_func_key);
        try {
            FanMainParams fanMainParams = JsonUtils.json2Pojo(tag.functionParams, FanMainParams.class);

            switch (tag.functionCode) {
                //炒
                case "fry":


                    if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                        // 超过点击间隔后再将lastClickTime重置为当前点击时间
                        lastClickTime = curClickTime;

                        LogUtils.i("20200610", "点击炒");

                        if (fan != null) {
                            ToolUtils.logEvent(fan.getDt(), "炒", "roki_设备");
                        }
                        String fry = fanMainParams.getParam().getPower().getValue();
                        setLevel(Short.parseShort(fry), position, "fry");
                        EventUtils.postEvent(new KitchenCleanGearChangeEvent());
                    }


                    break;
                //煎
                case "decoct":
                    if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                        // 超过点击间隔后再将lastClickTime重置为当前点击时间
                        lastClickTime = curClickTime;

                        LogUtils.i("20200610", "点击煎");

                        if (fan != null) {
                            ToolUtils.logEvent(fan.getDt(), "煎", "roki_设备");
                        }

                        String decoct = fanMainParams.getParam().getPower().getValue();
                        setLevel(Short.parseShort(decoct), position, "decoct");
                        EventUtils.postEvent(new KitchenCleanGearChangeEvent());

                    }


                    break;
                //炖
                case "stew":
                    if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                        // 超过点击间隔后再将lastClickTime重置为当前点击时间
                        lastClickTime = curClickTime;

                        LogUtils.i("20200610", "点击炖");

                        if (fan != null) {
                            ToolUtils.logEvent(fan.getDt(), "炖", "roki_设备");
                        }

                        String stew = fanMainParams.getParam().getPower().getValue();
                        setLevel(Short.parseShort(stew), position, "stew");
                        EventUtils.postEvent(new KitchenCleanGearChangeEvent());

                    }

                    break;
                //自动烟感
                case "smokeFeeling":
                    if (fan != null) {
                        ToolUtils.logEvent(fan.getDt(), "自动烟感", "roki_设备");
                    }

                    //逻辑更改 下发智能烟感之前不用判断是否开关机 以及关机状态下 下发关机指令 2019年12月31日 11:51:58
                    setFanFeelPower();

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setFanFeelPower() {
        if (isFeelPower) {
            fanStatusComposite.FanFeelPower = (short) 0;
            setFanSmartSmoke();
        } else {
            fanStatusComposite.FanFeelPower = (short) 1;
            setFanSmartSmoke();
        }
    }

    private void setFanSmartSmoke() {
        fan.setFanSmartSmoke(fanStatusComposite, (short) 1, new VoidCallback() {
            @Override
            public void onSuccess() {
                notifyItemChanged(3);
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("201910261111", t.getMessage());
            }
        });
    }


    /**
     * 设置烟灶档位
     */
    void setLevel(final short level, final int position, String sing) {
        if (!fan.isConnected()) {
            DeviceOfflinePrompt();
            return;
        }


//        if (NoDoubleClickUtils.isDoubleClick()) {
//            LogUtils.i("20200610","isDoubleClick");
//            return;
//        }
        if (fan.level == level || fan.level == AbsFan.PowerLevel_1
                || fan.level == AbsFan.PowerLevel_4
                || fan.level == AbsFan.PowerLevel_5) {

            if (count_clean != 0 && fan.level == AbsFan.PowerLevel_1) {

                if (fan.level == 2 && sing.equals("stew") || fan.level == 1 && sing.equals("stew")) {
                    setLevel_0(position);
                } else {
                    fan.setFanLevel(level, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            mLevel = level;
                            fan.level = level;
                            notifyItemRangeChanged(0, 3);
                            LogUtils.i("20200610", "success");
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            LogUtils.i("20200610", " onFailure:" + t);
                        }
                    });

                }


                return;
            }

            if (fan.level >= 4 && fan.level <= 6 && sing.equals("fry")
                    || fan.level == 3 && sing.equals("decoct")
                    || fan.level == 2 && sing.equals("stew")
                    || fan.level == 1 && sing.equals("stew")) {
                setLevel_0(position);

            } else {
                fan.setFanLevel(level, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        mLevel = level;
                        fan.level = level;
                        notifyItemRangeChanged(0, 3);
                        LogUtils.i("20200610", "success");
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        LogUtils.i("20200610", " onFailure:" + t);
                    }
                });
            }


        } else {
            fan.setFanLevel(level, new VoidCallback() {
                @Override
                public void onSuccess() {
                    mLevel = level;
                    fan.level = level;
                    notifyItemRangeChanged(0, 3);
                    LogUtils.i("20200610", "success");
                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("20200610", " onFailure:" + t);
                }
            });
        }
    }

    /**
     * 设置烟灶档位关闭
     */
    void setLevel_0(final int position) {
        fan.setFanStatus(AbsFan.PowerLevel_0, new VoidCallback() {
            @Override
            public void onSuccess() {
                LogUtils.i("20200610", "success");
                mLevel = 0;
                fan.level = 0;
                notifyItemChanged(position);
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20200610", t.getMessage());
            }
        });
    }

    //item时间点击处理
    private void itemEventClick(View view) {
        String tag = view.getTag().toString();
        Integer pos = (Integer) view.getTag(R.id.tag_fan_other_func_key_pos);

        //厨房净化
        if ("kitchenCleanup".equals(tag)) {
            dialogClean(pos);
            //计时提醒
        } else if ("timeReminding".equals(tag)) {
            soupRemind(view, pos);
            //通风换气 (假日模式)
        } else if ("ventilation".equals(tag)) {
            ventilation();
            //烟灶联动
        } else if ("smokeStoveLinkage".equals(tag)) {
            smokeStoveLinkage();
            //油杯检测
        } else if ("oilCupworkDetection".equals(tag)) {
            String title = null;
            for (int i = 0; i < mDates.size(); i++) {
                if ("oilCupworkDetection".equals(mDates.get(i).functionCode)) {
                    title = mDates.get(i).subView.title;
                }
            }
            oilCup(title);
            //油网检测
        } else if ("oilNetworkDetection".equals(tag)) {
            String title = null;
            for (int i = 0; i < mDates.size(); i++) {
                if ("oilNetworkDetection".equals(mDates.get(i).functionCode)) {
                    title = mDates.get(i).subView.title;
                }
            }
            oilDetection(title);
            //健康热油
        } else if ("hotOil".equals(tag)) {
            hotOil();
            //防干烧
        } else if ("dryCleaning".equals(tag)) {
            dryCleaning();
            //过温保护
        } else if ("OTP".equals(tag)) {
            otp();
            //清洗锁定
        } else if ("cleanLock".equals(tag)) {
            cleanLock();
            //手势控制
        } else if ("gestureControl".equals(tag)) {
            String title = null;
            for (int i = 0; i < mDates.size(); i++) {
                if ("gestureControl".equals(mDates.get(i).functionCode)) {
                    title = mDates.get(i).subView.title;
                }
            }
            gestureControl(title);

        } else if ("smokeStoveAutoPower".equals(tag)) {
            toCruise();
        } else if ("smokeLinkage".equals(tag)) {
            //新增联动功能 5010S开始
            linkage();
        }else if ("setLinkageTime".equals(tag)) {
            selectTime();
        }
    }

    //清洗锁定
    private void cleanLock() {
        if (!fan.isConnected()) {
            DeviceOfflinePrompt();
            return;
        }

        final IRokiDialog dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        dialog.setTitleText("清洗锁定");
        dialog.setContentText("是否确认打开清洗锁定？");
        dialog.show();
        dialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        dialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    //过温保护
    private void otp() {
        if (!fan.isConnected()) {
            DeviceOfflinePrompt();
            return;
        }
        Bundle bd = new Bundle();
        bd.putSerializable(PageArgumentKey.Bean, fan);
        bd.putSerializable(PageArgumentKey.List, (Serializable) mDates);
        UIService.getInstance().postPage(PageKey.DeviceFanOTP, bd);

    }

    private void gestureControl(String title) {
        if (!fan.isConnected()) {
            DeviceOfflinePrompt();
            return;
        }
        Bundle bd = new Bundle();
        bd.putSerializable(PageArgumentKey.title, title);
        bd.putSerializable(PageArgumentKey.List, (Serializable) mDates);
        bd.putSerializable(PageArgumentKey.Bean, fan);
        bd.putSerializable(PageArgumentKey.tag, "other");
        UIService.getInstance().postPage(PageKey.DeviceFanGestureControlPage, bd);
    }

    void initView() {
        if (fan == null) return;
        SOUP_REMIND = fan.getGuid().toString();
        CLEAN_REMIND = fan.getGuid().toString() + "CLEAN";
        IS_LOCAL_START_REMIND = fan.getGuid().toString() + "IS_LOCAL";
        if (PreferenceUtils.containKey(CLEAN_REMIND)) {//厨房净化
            String time = PreferenceUtils.getString(CLEAN_REMIND, "none");
            String[] strs = null;
            if (!"none".equals(time)) {
                strs = time.split(":");
                long tt = Long.valueOf(strs[0]) + Long.valueOf(strs[1]);
                if (System.currentTimeMillis() > tt) {//判断预设时间是否超过当前时间
                    PreferenceUtils.remove(CLEAN_REMIND);//清空记录
                    count_clean = 0;
                } else {//重新倒计时
                    short down = (short) ((tt - System.currentTimeMillis()) / 1000);
                    count_clean = down;
                    switchCountDown_clean(true, PreferenceUtils.getInt("dialogClean", -1));
                }
            }
        }
        if (PreferenceUtils.containKey(SOUP_REMIND)) {
            String time = PreferenceUtils.getString(SOUP_REMIND, "none");
            String[] strs = null;
            if (!"none".equals(time)) {
                strs = time.split(":");
                long tt = Long.valueOf(strs[0]) + Long.valueOf(strs[1]);
                if (System.currentTimeMillis() > tt) {
                    PreferenceUtils.remove(SOUP_REMIND);
                    count_remind = 0;
                    switchCountDown_remind(false, PreferenceUtils.getInt("soupRemind", -1));
                } else {
                    short down = (short) ((tt - System.currentTimeMillis()) / 1000);
                    count_remind = down;
                    switchCountDown_remind(true, PreferenceUtils.getInt("soupRemind", -1));
                }
            }
        }
        if (!fan.isConnected()) return;
    }

    //油网检测
    private void oilDetection(String title) {
        if (!fan.isConnected()) {
            DeviceOfflinePrompt();
            return;
        }
        Bundle bd = new Bundle();
        bd.putSerializable(PageArgumentKey.title, title);
        bd.putSerializable(PageArgumentKey.List, (Serializable) mDates);
        bd.putSerializable(PageArgumentKey.Bean, fan);
        bd.putSerializable(PageArgumentKey.tag, "other");
        UIService.getInstance().postPage(PageKey.DeviceFanOilDetection, bd);
    }

    private void oilCup(String title) {
        if (!fan.isConnected()) {
            DeviceOfflinePrompt();
            return;
        }
        Bundle bd = new Bundle();
        bd.putSerializable(PageArgumentKey.title, title);
        bd.putSerializable(PageArgumentKey.List, (Serializable) mDates);
        bd.putSerializable(PageArgumentKey.Bean, fan);
        bd.putSerializable(PageArgumentKey.tag, "other");
        UIService.getInstance().postPage(PageKey.DeviceFanOilCup, bd);
    }

    //健康热油
    private void hotOil() {
        if (!fan.isConnected()) {
            DeviceOfflinePrompt();
            return;
        }
        Bundle bd = new Bundle();
        bd.putSerializable(PageArgumentKey.Bean, fan);
        bd.putSerializable(PageArgumentKey.List, (Serializable) mDates);
        UIService.getInstance().postPage(PageKey.DeviceFanHotOil, bd);
    }

    //防干烧跳转
    private void dryCleaning() {
        if (!fan.isConnected()) {
            DeviceOfflinePrompt();
            return;
        }
        Bundle bd = new Bundle();
        bd.putSerializable(PageArgumentKey.Bean, fan);
        bd.putSerializable(PageArgumentKey.List, (Serializable) mDates);
        UIService.getInstance().postPage(PageKey.DeviceFanDryCleaning, bd);
    }

    /**
     * 联动功能 新增
     */
    private void linkage() {
        if (!fan.isConnected()) {
            DeviceOfflinePrompt();
            return;
        }
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, fan.getGuid().getGuid());
        for (DeviceConfigurationFunctions mDate : mDates) {
            if(mDate.functionCode.equals("smokeLinkage")){
                bd.putSerializable(DeviceBaseFuntionActivity.FUNCTION, mDate);
            }
        }
        Intent intent = new Intent(cx, FanLinkageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(DeviceBaseFuntionActivity.BUNDLE , bd);
        cx.startActivity(intent);
    }
    /**
     * 选择蒸烤一体机
     */
    public void selectTime(){
        BaseDialog delayedShutDialog = new BaseDialog(mContext);
        delayedShutDialog.setContentView(R.layout.dialog_device_delayed_shut);
        delayedShutDialog.setCanceledOnTouchOutside(true);
        delayedShutDialog.setGravity(Gravity.BOTTOM);
        delayedShutDialog.setWidth(((AppCompatActivity)mContext).getWindowManager().getDefaultDisplay().getWidth());
        RecyclerView rvDevice = (RecyclerView) delayedShutDialog.findViewById(R.id.rv_device);

        PickerLayoutManager manager = new PickerLayoutManager.Builder(mContext)
//                .setMaxItem(3)
                .setScale(0.3f)
                .setOnPickerListener(new PickerLayoutManager.OnPickerListener() {
                    @Override
                    public void onPicked(RecyclerView recyclerView, int position) {
                        Log.e("position" , "---------"+position);
                        rvPickerAdapter.setIndex(position);
                    }
                })
                .build();
        rvDevice.setLayoutManager(manager);
        //设置功能区块间距
//        rvDevice.addItemDecoration(new VerticalItemDecoration(18, mContext));
        //device adapter
         rvPickerAdapter = new RvPickerAdapter(mContext);
        rvDevice.setAdapter(rvPickerAdapter);
        manager.scrollToPosition(Integer.MAX_VALUE / 2);
        rvPickerAdapter.setIndex(Integer.MAX_VALUE / 2);
        //设置数据
        rvPickerAdapter.addItem(1);
        rvPickerAdapter.addItem(2);
        rvPickerAdapter.addItem(3);
        rvPickerAdapter.addItem(4);
        rvPickerAdapter.addItem(5);
        delayedShutDialog.show();


        delayedShutDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.btn_cancel){
                    delayedShutDialog.dismiss();
                }else if (view.getId() == R.id.btn_complete){
                    delayedShutDialog.dismiss();
                    fan.setDelayedShut(rvPickerAdapter.getItem(rvPickerAdapter.getIndex()), new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            ToastUtils.showShort("设置成功");

                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
                }
            }
        } ,R.id.btn_complete, R.id.btn_cancel);
    }

    //烟灶联动跳转
    private void smokeStoveLinkage() {
        if (!fan.isConnected()) {
            DeviceOfflinePrompt();
            return;
        }
        Bundle bd = new Bundle();
        bd.putSerializable(PageArgumentKey.List, (Serializable) mDates);
        bd.putSerializable(PageArgumentKey.Bean, fan);

        if ("5916S".equals(fan.getDt()) || "8236S".equals(fan.getDt())) {
            UIService.getInstance().postPage(PageKey.DeviceFanStoveLinkage2, bd);
        } else {
            UIService.getInstance().postPage(PageKey.DeviceFanStoveLinkage, bd);
        }

    }

    /**
     * 巡航跳转
     */
    private void toCruise() {
        if (!fan.isConnected()) {
            DeviceOfflinePrompt();
            return;
        }
        Bundle bd = new Bundle();
        bd.putSerializable(PageArgumentKey.List, (Serializable) mDates);
        bd.putSerializable(PageArgumentKey.Bean, fan);
        UIService.getInstance().postPage(PageKey.DeviceFanCruisePage, bd);
    }

    //其他通风换气跳转
    // 5915假日模式
    private void ventilation() {
        if (!fan.isConnected()) {
            DeviceOfflinePrompt();
            return;
        }
        Bundle bd = new Bundle();
        bd.putSerializable(PageArgumentKey.List, (Serializable) mDates);
        bd.putSerializable(PageArgumentKey.Bean, fan);

        if ("5916S".equals(fan.getDt()) || "8236S".equals(fan.getDt())) {
            UIService.getInstance().postPage(PageKey.DeviceFanVentilation2, bd);
        } else {
            UIService.getInstance().postPage(PageKey.DeviceFanVentilation, bd);
        }

    }

    FanOtherFuncViewHolder mHolder;

    //计时提醒
    private void soupRemind(View view, final Integer pos) {
        if (!fan.isConnected()) {
            DeviceOfflinePrompt();
            return;
        }
        String tag = view.getTag(R.id.tag_fan_other_func_key).toString();
        try {
            if (!PreferenceUtils.containKey(SOUP_REMIND)) {
                List<String> listButton = TestDatas.createDialogText("小时", "分钟");

                FanDeviceRemindSoup fanDeviceRemindSoup = JsonUtils.json2Pojo(tag, FanDeviceRemindSoup.class);
                if (fanDeviceRemindSoup == null) return;
                List<Integer> hour = fanDeviceRemindSoup.getParam().getHour().getValue();
                final List<Integer> minute = fanDeviceRemindSoup.getParam().getMinute().getValue();
                int hourDefault = fanDeviceRemindSoup.getParam().getHourDefault().getValue();
                int minuteDefault = fanDeviceRemindSoup.getParam().getMinuteDefault().getValue();


                absFanTimingDialog = new AbsFanTimingDialog(mContext, hour, generateModelWheelData(minute.get(1), minute.get(0)), listButton, hourDefault, minuteDefault);
                absFanTimingDialog.show(absFanTimingDialog);
                absFanTimingDialog.setListener(new AbsFanTimingDialog.PickListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onConfirm(int index1, int index2) {
                        //计时提醒 毫秒数
                        final long time = (long) (index1 * 60 * 60 * 1000 + index2 * 60 * 1000);
                        //当前时间 毫秒数
                        final long time1 = System.currentTimeMillis();
                        if (fan != null) {
                            ToolUtils.logEvent(fan.getDt(), "计时提醒:" + index1 + ":" + index2, "roki_设备");
                        }
                        min = 0;
                        if (index1 == 0) {
                            if (index2 != 0) {
                                min = (short) index2;
                            }
                        } else {
                            if (index2 != 0) {
                                min = (short) (index1 * 60 + index2);
                            } else {
                                min = (short) (index1 * 60);
                            }
                        }
                        //是否发送168指令 5916S发送 其他烟机不发送
                        if (timeRemindingIsSend) {

                            fan.setTimingRemind((short) 1, min, new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    PreferenceUtils.setInt(IS_LOCAL_START_REMIND, pos);
                                    startRemind(time, time1, pos);
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    t.printStackTrace();

                                }
                            });
                        } else {
                            startRemind(time, time1, pos);
                            LogUtils.i("20200325666", "else_setTimingRemind:" + time);
                        }


                    }
                });
            } else {
                //取消倒计时弹框
                final IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(mContext, DialogUtil.DIALOG_TYPE_10);
                dialogByType.setTitleText(R.string.fan_remind_timing);
                dialogByType.setContentText(R.string.fan_can_countdown2);
                dialogByType.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stopBroadcastReceiver();

                        if (timeRemindingIsSend) {
                            fan.setTimingRemind((short) 0, (short) 0, new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    //switchCountDown_remind(false, PreferenceUtils.getInt("soupRemind", -1));

                                }


                                @Override
                                public void onFailure(Throwable t) {
                                }
                            });

                        } else {
                            switchCountDown_remind(false, PreferenceUtils.getInt("soupRemind", -1));
                        }
                        dialogByType.dismiss();

                    }
                });
                dialogByType.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogByType.dismiss();
                    }
                });
                dialogByType.show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void startRemind(long time, long time1, Integer pos) {
        PreferenceUtils.setString(SOUP_REMIND, time1 + ":" + time);
        PreferenceUtils.setInt("soupRemind", pos);
        count_remind = (short) (time / 1000);
        //switchCountDown_remind(true, pos);
        startBroadcastReceiver(time1 + time);
        startCountDown(pos);

    }


    private List<Integer> generateModelWheelData(int max, int start) {
        List<Integer> list = Lists.newArrayList();
        for (int i = start; i <= max; i++) {
            list.add(i);
        }
        return list;
    }


    //开启或关闭计时提醒倒计时
    void switchCountDown_remind(boolean countdown, final Integer pos) {
        if (timer_remind != null) {
            timer_remind.cancel();
            timer_remind = null;
        }
        if (countdown) {
            PreferenceUtils.setBool("isCountdownFan", true);
            timer_remind = new Timer();
            timer_remind.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message msg = mHandler.obtainMessage();
                    msg.arg1 = pos;
                    msg.what = 2;
                    mHandler.sendMessage(msg);
                }
            }, 0, 1000);
        } else {
            if (PreferenceUtils.containKey(SOUP_REMIND)) {
                PreferenceUtils.remove(SOUP_REMIND);
                PreferenceUtils.remove("soupRemind");
            }
            count_remind = 0;
            if (mHolder != null) {
                mHolder.stopAnimation();
            }
            //closeCountDown();
            PreferenceUtils.remove("isCountdownFan");
            notifyDataSetChanged();
        }
    }


    //开启计时
    void startCountDown(final Integer pos) {
        if (timer_remind != null) {
            timer_remind.cancel();
            timer_remind = null;
        }
        timer_remind = new Timer();
        timer_remind.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = mHandler.obtainMessage();
                msg.arg1 = pos;
                msg.what = 2;
                mHandler.sendMessage(msg);

            }
        }, 0, 1000);
    }

    public void closeTask() {
        if (timer_remind != null) {
            timer_remind.cancel();
            timer_remind = null;
        }
        if (mHolder != null) {
            mHolder.stopAnimation();
        }
    }

    //关闭计时
    void closeCountDown() {

        try {
            if (timer_remind != null) {
                timer_remind.cancel();
                timer_remind = null;
            }
            count_remind = -1;
            PreferenceUtils.remove("isCountdownFan");
            if (PreferenceUtils.containKey(SOUP_REMIND)) {
                PreferenceUtils.remove(SOUP_REMIND);
                PreferenceUtils.remove("soupRemind");
            }
            if (PreferenceUtils.containKey(IS_LOCAL_START_REMIND)) {
                PreferenceUtils.remove(IS_LOCAL_START_REMIND);
            }

            if (mHolder != null) {
                mHolder.stopAnimation();
            }
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //关闭计时提醒广播
    private void stopBroadcastReceiver() {
        AlarmManager alarmManager = AlarmManagerUtil.getInstance(cx);
        Intent intent = new Intent(cx, Fan8700alarmReceiver.class);
        intent.setAction("Fan8700alarmReceiver");
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(cx, 0, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    //开启计时提醒广播
    private void startBroadcastReceiver(long time) {
        AlarmManager alarmManager = AlarmManagerUtil.getInstance(cx);
        Intent intent = new Intent(cx, Fan8700alarmReceiver.class);
        intent.setAction("Fan8700alarmReceiver");
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(cx, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }

    public List<DeviceConfigurationFunctions> getKitchenData() {
        List<DeviceConfigurationFunctions> kitchenList = Lists.newArrayList();
        for (int i = 0; i < mDates.size(); i++) {
            if ("kitchenCleanup".equals(mDates.get(i).functionCode)) {
                DeviceConfigurationFunctions functions = mDates.get(i);
                List<DeviceConfigurationFunctions> subList = functions.subView.subViewModelMap.
                        subViewModelMapSubView.deviceConfigurationFunctions;
                if (subList != null && subList.size() > 0) {
                    for (int j = 0; j < subList.size(); j++) {
                        DeviceConfigurationFunctions kitchenFunctions = subList.get(j);
                        kitchenList.add(kitchenFunctions);
                    }
                }
            }
        }
        return kitchenList;
    }

    private void dialogClean(final Integer pos) {
        PreferenceUtils.setInt("dialogClean", pos);
        if (!fan.isConnected()) {
            DeviceOfflinePrompt();
            return;
        }
        if (fan.getDt().equals("5010S")) {
            if (fan.ventilationRemainingTime == 0) {
                mRokiDialog = RokiDialogFactory.createDialogByType(mContext, DialogUtil.DIALOG_TYPE_15);
                List<DeviceConfigurationFunctions> kitchenData = getKitchenData();
                ArrayList<String> names = Lists.newArrayList();
                for (int i = 0; i < kitchenData.size(); i++) {
                    String functionName = kitchenData.get(i).functionName;
                    names.add(functionName);
                }
                mRokiDialog.setWheelViewData(null, names, null, kitchenData, false, 0, 0, 0, null, new OnItemSelectedListenerCenter() {
                    @Override
                    public void onItemSelectedCenter(String contentCenter) {
                        Message msg = mHandler.obtainMessage();
                        msg.what = 3;
                        msg.arg1 = pos;
                        msg.obj = contentCenter;
                        mHandler.sendMessage(msg);
                    }
                }, null);

                mRokiDialog.show();
            } else {
                final IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(mContext, DialogUtil.DIALOG_TYPE_10);
                dialogByType.setTitleText(R.string.close_work);
                dialogByType.setContentText(cx.getString(R.string.device_close_work));
                dialogByType.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogByType.dismiss();
                        fan.setFanStatus(AbsFan.PowerLevel_0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onFailure(Throwable t) {
                            }
                        });
                    }
                });
                dialogByType.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogByType.dismiss();
                    }
                });
                dialogByType.show();
            }
            return;
        }

        if (!PreferenceUtils.containKey(CLEAN_REMIND)) {
            mRokiDialog = RokiDialogFactory.createDialogByType(mContext, DialogUtil.DIALOG_TYPE_15);
            List<DeviceConfigurationFunctions> kitchenData = getKitchenData();
            ArrayList<String> names = Lists.newArrayList();
            for (int i = 0; i < kitchenData.size(); i++) {
                String functionName = kitchenData.get(i).functionName;
                names.add(functionName);
            }
            mRokiDialog.setWheelViewData(null, names, null, kitchenData, false, 0, 0, 0, null, new OnItemSelectedListenerCenter() {
                @Override
                public void onItemSelectedCenter(String contentCenter) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = 3;
                    msg.arg1 = pos;
                    msg.obj = contentCenter;
                    mHandler.sendMessage(msg);
                }
            }, null);

            mRokiDialog.show();
        } else {
            final IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(mContext, DialogUtil.DIALOG_TYPE_10);
            dialogByType.setTitleText(R.string.close_work);
            dialogByType.setContentText(cx.getString(R.string.device_close_work));
            dialogByType.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogByType.dismiss();
                    fan.setFanStatus(AbsFan.PowerLevel_0, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onFailure(Throwable t) {
                        }
                    });
                }
            });
            dialogByType.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogByType.dismiss();
                }
            });
            dialogByType.show();
        }


    }

    private void DeviceOfflinePrompt() {
        ToastUtils.showLong(R.string.device_connected);
    }

    //开启厨房净化
    void setCleaningLevel(short level, final short time, final int pos) {

        fan.setFanTimeWork(level, time, new VoidCallback() {
            @Override
            public void onSuccess() {
                if (time == 0 && PreferenceUtils.containKey(CLEAN_REMIND)) {
                    cancelCountDown_clean();
                    return;
                }
                if (time != 0) {
                    beginCountDown_clean(time, pos);
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    //结束倒计时
    void cancelCountDown_clean() {
        switchCountDown_clean(false, PreferenceUtils.getInt("dialogClean", -1));
    }

    // 开始倒计时
    void beginCountDown_clean(long msgtime, int pos) {
        long time = System.currentTimeMillis();
        PreferenceUtils.setString(CLEAN_REMIND, time + ":" + msgtime * 60 * 1000);
        count_clean = (short) (msgtime * 60);
        switchCountDown_clean(true, pos);
    }

    //开启或关闭厨房净化倒计时
    void switchCountDown_clean(boolean countdown, final int pos) {

        if (timer_clean != null) {
            timer_clean.cancel();
            timer_clean = null;
        }
        if (countdown) {

            timer_clean = new Timer();
            timer_clean.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message msg = mHandler.obtainMessage();
                    msg.arg1 = pos;
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                }
            }, 0, 1000);

        } else {
            if (PreferenceUtils.containKey(CLEAN_REMIND)) {
                PreferenceUtils.remove(CLEAN_REMIND);
                PreferenceUtils.remove("dialogClean");
            }
            count_clean = 0;
            notifyDataSetChanged();

        }
    }

    private void kitchenClean(int pos) {
        if (count_clean < 0) return;

        if (count_clean == 0) {
            switchCountDown_clean(false, pos);
            fan.setFanStatus(AbsFan.PowerLevel_0, new VoidCallback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        }
        count_clean--;
        notifyDataSetChanged();
    }


    public void closeEventUtils() {
        EventUtils.unregist(this);
    }
}

class FanOtherFuncViewHolder extends RecyclerView.ViewHolder {

    ImageView mImageView;
    TextView mTvDesc;
    TextView mTvName;
    TextView mTvRunWorkText;
    LinearLayout mItemView;
    LinearLayout mLlDefaultText;
    ImageView mStateShow;
    AlphaAnimation mAlphaAnimation;

    public FanOtherFuncViewHolder(View itemView) {
        super(itemView);

        mTvName = (TextView) itemView.findViewById(R.id.tv_name);
        mTvRunWorkText = (TextView) itemView.findViewById(R.id.tv_run_work_text);
        mImageView = (ImageView) itemView.findViewById(R.id.iv_view);
        mTvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
        mItemView = (LinearLayout) itemView.findViewById(R.id.itemView);
        mLlDefaultText = (LinearLayout) itemView.findViewById(R.id.ll_default_text);
        mStateShow = (ImageView) itemView.findViewById(R.id.iv_state_show);
    }

    public void startAnimation() {
        mStateShow.setImageResource(R.drawable.shape_rika_round_yellow_dot);
        mAlphaAnimation = null;
        if (mAlphaAnimation == null) {
            mAlphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(cx, R.anim.device_rika_dot_alpha);
            LinearInterpolator lin = new LinearInterpolator();
            mAlphaAnimation.setInterpolator(lin);
            mStateShow.startAnimation(mAlphaAnimation);
            mStateShow.setVisibility(View.VISIBLE);
        }
    }

    public void stopAnimation() {
        mStateShow.setVisibility(View.GONE);
        mStateShow.setImageResource(0);
        if (mAlphaAnimation != null) {
            mAlphaAnimation.cancel();
            mAlphaAnimation = null;
        }
    }


}

class MainFuncViewHolder extends RecyclerView.ViewHolder {

    ImageView mIvModelImg;
    TextView mTvModelName;
    LinearLayout mItemView;

    public MainFuncViewHolder(View itemView) {
        super(itemView);
        mItemView = (LinearLayout) itemView.findViewById(R.id.itemView);
        mIvModelImg = (ImageView) itemView.findViewById(R.id.iv_model_img);
        mTvModelName = (TextView) itemView.findViewById(R.id.tv_model_name);
    }


}



