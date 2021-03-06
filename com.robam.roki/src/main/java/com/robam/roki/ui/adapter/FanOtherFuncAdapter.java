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

import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.robam.roki.ui.dialog.AbsFanTimingDialog;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.NoDoubleClickUtils;
import com.robam.roki.utils.TestDatas;
import com.robam.roki.utils.ToolUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.robam.roki.ui.adapter.FanBackgroundFuncAdapter.timeRemindingIsSend;


/**
 * Created by 14807 on 2018/1/24.
 * ??????????????????????????????
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
    public static String SOUP_REMIND;//????????????????????????key
    public static String CLEAN_REMIND;////????????????????????????key
    public static String IS_LOCAL_START_REMIND;//???????????????????????????????????????
    Timer timer_clean;//?????????????????????
    Timer timer_remind;//?????????????????????

    int count_clean;//?????????????????????
    int count_remind;//?????????????????????

    private short min;

    boolean SIGN = true;//??????
    private AbsFanTimingDialog absFanTimingDialog;
    boolean isCountdown = false;
    private IRokiDialog remindSoupNoticeDialog;

    FanStatusComposite fanStatusComposite = new FanStatusComposite();

    boolean isFeelPower = false;

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
                                            ToolUtils.logEvent(fan.getDt(), "????????????:???", "roki_??????");
                                            break;
                                        case 2:
                                            ToolUtils.logEvent(fan.getDt(), "????????????:???", "roki_??????");
                                            break;
                                        case 3:
                                            ToolUtils.logEvent(fan.getDt(), "????????????:???", "roki_??????");
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
                                ToolUtils.logEvent(fan.getDt(), "????????????:???", "roki_??????");
                                break;
                            case 2:
                                ToolUtils.logEvent(fan.getDt(), "????????????:???", "roki_??????");
                                break;
                            case 3:
                                ToolUtils.logEvent(fan.getDt(), "????????????:???", "roki_??????");
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

            if ("8235S".equals(fan.getDt())||"R68A0".equals(fan.getDt())||"68A0S".equals(fan.getDt())) {
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
        //????????????????????????????????????????????????
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
        //??????????????????168?????? ???????????? ??????????????? ??????????????????????????????
        if (timeRemindingIsSend){
            short pollingTime = fan.periodicallyRemainingTime;
            //?????????????????????????????????
            if (remindSoupNoticeDialog != null && remindSoupNoticeDialog.isShow()) {
                pollingTime = 0;
            }
            if(isCountdown && count_remind == -1){
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

    @Subscribe
    public void onEvent(KitchenCleanGearChangeEvent event) {
        if (!fan.isConnected()) return;
        switchCountDown_clean(false, PreferenceUtils.getInt("dialogClean", -1));
    }

    //??????????????????
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

                NotificationManager notificationManager = NotificationManagerUtil.getInstance(mContext);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
                mBuilder.setContentTitle("ROKI????????????")//?????????????????????
                        .setContentText("?????????????????????????????????????????????") //???????????????????????????
                        .setTicker("????????????") //?????????????????????????????????????????????????????????
                        .setWhen(System.currentTimeMillis())//???????????????????????????????????????????????????????????????????????????????????????
                        .setChannelId(mContext.getPackageName()) //???????????????Android 8.0??? ??????????????????
                        .setPriority(Notification.PRIORITY_DEFAULT) //????????????????????????
                        .setOngoing(false)//ture??????????????????????????????????????????????????????????????????????????????????????????,??????????????????(???????????????)??????????????????????????????,??????????????????(?????????????????????,????????????,??????????????????)
                        .setDefaults(Notification.DEFAULT_ALL)//???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????defaults?????????????????????
                        //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND ???????????? // requires VIBRATE permission
                        .setSmallIcon(R.mipmap.ic_recipe_roki_logo);//???????????????ICON
                Intent it = new Intent(mContext, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, it, 0);
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
            FanOtherFuncViewHolder fanOtherFuncViewHolder = new FanOtherFuncViewHolder(mContext, view);
            //?????????????????????????????????
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
            //?????? ??? ??? ??????????????????
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
            //??????????????????????????? ??????????????????
            fanOtherFuncViewHolder.setIsRecyclable(false);
            String count_clean_time = TimeUtils.secToHourMinSec((short) count_clean);
            if (mDates != null && mDates.size() > 0) {
                Glide.with(mContext).load(mDates.get(position).backgroundImg).transition(withCrossFade()).into(fanOtherFuncViewHolder.mImageView);
                if ("kitchenCleanup".equals(mDates.get(position).functionCode)) {
                    StringBuilder cleanTime = new StringBuilder(count_clean_time);
                    cleanTime.append(mContext.getString(R.string.fan_complete_clean)).append(mDates.get(position).functionName);

                    if (count_clean > 0) {
                        fanOtherFuncViewHolder.startAnimation();
                        fanOtherFuncViewHolder.mLlDefaultText.setVisibility(View.GONE);
                        fanOtherFuncViewHolder.mTvRunWorkText.setVisibility(View.VISIBLE);
                        fanOtherFuncViewHolder.mTvRunWorkText.setText(cleanTime);

                    } else {
                        fanOtherFuncViewHolder.stopAnimation();
                        fanOtherFuncViewHolder.mLlDefaultText.setVisibility(View.VISIBLE);
                        fanOtherFuncViewHolder.mTvRunWorkText.setVisibility(View.GONE);
                        fanOtherFuncViewHolder.mTvName.setText(mDates.get(position).functionName);
                        fanOtherFuncViewHolder.mTvDesc.setText(mDates.get(position).msg);
                    }
                    //????????????
                } else if ("timeReminding".equals(mDates.get(position).functionCode)) {

                    StringBuilder remind_time = null;
                    if (timeRemindingIsSend) {

                        if (fan.periodicallyRemainingTime > 0) {
                            remind_time = new StringBuilder(count_remind_time);
                            remind_time.append(mContext.getString(R.string.fan_complete_clean)).append(mDates.get(position).functionName);
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
                            remind_time.append(mContext.getString(R.string.fan_complete_clean)).append(mDates.get(position).functionName);
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
                    //????????????
                } else if ("OTP".equals(mDates.get(position).functionCode)) {
                    fanOtherFuncViewHolder.mTvName.setText(mDates.get(position).functionName);
                    fanOtherFuncViewHolder.mTvDesc.setText(mDates.get(position).msg);
                    //????????????
                } else if ("cleanLock".equals(mDates.get(position).functionCode)) {
                    fanOtherFuncViewHolder.mTvName.setText(mDates.get(position).functionName);
                    fanOtherFuncViewHolder.mTvDesc.setText(mDates.get(position).msg);
                } else if ("gestureControl".equals(mDates.get(position).functionCode)) {
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
                        Glide.with(mContext).load(mDates.get(position).backgroundImgH).transition(withCrossFade()).
                                into(mainFuncViewHolder.mIvModelImg);
                    } else {
                        Glide.with(mContext).load(mDates.get(position).backgroundImg).transition(withCrossFade()).
                                into(mainFuncViewHolder.mIvModelImg);
                    }
                } else if ("decoct".equals(mDates.get(position).functionCode)) {
                    if (mLevel == AbsFan.PowerLevel_3) {
                        Glide.with(mContext).load(mDates.get(position).backgroundImgH).transition(withCrossFade()).
                                into(mainFuncViewHolder.mIvModelImg);
                    } else {
                        Glide.with(mContext).load(mDates.get(position).backgroundImg).transition(withCrossFade()).
                                into(mainFuncViewHolder.mIvModelImg);
                    }
                } else if ("stew".equals(mDates.get(position).functionCode)) {
                    if (mLevel == AbsFan.PowerLevel_1 || mLevel == AbsFan.PowerLevel_2) {
                        Glide.with(mContext).load(mDates.get(position).backgroundImgH).transition(withCrossFade()).
                                into(mainFuncViewHolder.mIvModelImg);
                    } else {
                        Glide.with(mContext).load(mDates.get(position).backgroundImg).transition(withCrossFade()).
                                into(mainFuncViewHolder.mIvModelImg);
                    }
                } else if ("smokeFeeling".equals(mDates.get(position).functionCode)) {
                    LogUtils.i("20191026123456", mDates.get(position).backgroundImg);
                    if (isFeelPower) {
                        Glide.with(mContext).load(mDates.get(position).backgroundImgH).transition(withCrossFade()).
                                into(mainFuncViewHolder.mIvModelImg);
                    } else {
                        Glide.with(mContext).load(mDates.get(position).backgroundImg).transition(withCrossFade()).
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
                //???
                case "fry":

                    if((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                        // ???????????????????????????lastClickTime???????????????????????????
                        lastClickTime = curClickTime;

                        LogUtils.i("20200610","?????????");

                        if (fan != null) {
                            ToolUtils.logEvent(fan.getDt(), "???", "roki_??????");
                        }
                        String fry = fanMainParams.getParam().getPower().getValue();
                        setLevel(Short.parseShort(fry), position, "fry");
                        EventUtils.postEvent(new KitchenCleanGearChangeEvent());
                    }



                    break;
                //???
                case "decoct":
                    if((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                        // ???????????????????????????lastClickTime???????????????????????????
                        lastClickTime = curClickTime;

                        LogUtils.i("20200610","?????????");

                        if (fan != null) {
                            ToolUtils.logEvent(fan.getDt(), "???", "roki_??????");
                        }

                        String decoct = fanMainParams.getParam().getPower().getValue();
                        setLevel(Short.parseShort(decoct), position, "decoct");
                        EventUtils.postEvent(new KitchenCleanGearChangeEvent());

                    }


                    break;
                //???
                case "stew":
                    if((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                        // ???????????????????????????lastClickTime???????????????????????????
                        lastClickTime = curClickTime;

                        LogUtils.i("20200610","?????????");

                        if (fan != null) {
                            ToolUtils.logEvent(fan.getDt(), "???", "roki_??????");
                        }

                        String stew = fanMainParams.getParam().getPower().getValue();
                        setLevel(Short.parseShort(stew), position, "stew");
                        EventUtils.postEvent(new KitchenCleanGearChangeEvent());

                    }

                    break;
                //????????????
                case "smokeFeeling":
                    if (fan != null) {
                        ToolUtils.logEvent(fan.getDt(), "????????????", "roki_??????");
                    }

                    //???????????? ??????????????????????????????????????????????????? ????????????????????? ?????????????????? 2019???12???31??? 11:51:58
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
     * ??????????????????
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

                if (fan.level == 2 && sing.equals("stew")|| fan.level == 1 && sing.equals("stew")){
                    setLevel_0(position);
                }else{
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
     * ????????????????????????
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

    //item??????????????????
    private void itemEventClick(View view) {
        String tag = view.getTag().toString();
        Integer pos = (Integer) view.getTag(R.id.tag_fan_other_func_key_pos);

        //????????????
        if ("kitchenCleanup".equals(tag)) {
            dialogClean(pos);
            //????????????
        } else if ("timeReminding".equals(tag)) {
            soupRemind(view, pos);
            //???????????? (????????????)
        } else if ("ventilation".equals(tag)) {
            ventilation();
            //????????????
        } else if ("smokeStoveLinkage".equals(tag)) {
            smokeStoveLinkage();
            //????????????
        } else if ("oilCupworkDetection".equals(tag)) {
            String title = null;
            for (int i = 0; i < mDates.size(); i++) {
                if ("oilCupworkDetection".equals(mDates.get(i).functionCode)) {
                    title = mDates.get(i).subView.title;
                }
            }
            oilCup(title);
            //????????????
        } else if ("oilNetworkDetection".equals(tag)) {
            String title = null;
            for (int i = 0; i < mDates.size(); i++) {
                if ("oilNetworkDetection".equals(mDates.get(i).functionCode)) {
                    title = mDates.get(i).subView.title;
                }
            }
            oilDetection(title);
            //????????????
        } else if ("hotOil".equals(tag)) {
            hotOil();
            //?????????
        } else if ("dryCleaning".equals(tag)) {
            dryCleaning();
            //????????????
        } else if ("OTP".equals(tag)) {
            otp();
            //????????????
        } else if ("cleanLock".equals(tag)) {
            cleanLock();
            //????????????
        }else if ("gestureControl".equals(tag)) {
            String title = null;
            for (int i = 0; i < mDates.size(); i++) {
                if ("gestureControl".equals(mDates.get(i).functionCode)) {
                    title = mDates.get(i).subView.title;
                }
            }
            gestureControl(title);

        }
    }

    //????????????
    private void cleanLock() {
        if (!fan.isConnected()) {
            DeviceOfflinePrompt();
            return;
        }

        final IRokiDialog dialog = RokiDialogFactory.createDialogByType(mContext, DialogUtil.DIALOG_TYPE_10);
        dialog.setTitleText("????????????");
        dialog.setContentText("?????????????????????????????????");
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

    //????????????
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
        if (PreferenceUtils.containKey(CLEAN_REMIND)) {//????????????
            String time = PreferenceUtils.getString(CLEAN_REMIND, "none");
            String[] strs = null;
            if (!"none".equals(time)) {
                strs = time.split(":");
                long tt = Long.valueOf(strs[0]) + Long.valueOf(strs[1]);
                if (System.currentTimeMillis() > tt) {//??????????????????????????????????????????
                    PreferenceUtils.remove(CLEAN_REMIND);//????????????
                    count_clean = 0;
                } else {//???????????????
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

    //????????????
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

    //????????????
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

    //???????????????
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


    //??????????????????
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


    //????????????????????????
    // 5915????????????
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

    //????????????
    private void soupRemind(View view, final Integer pos) {
        if (!fan.isConnected()) {
            DeviceOfflinePrompt();
            return;
        }
        String tag = view.getTag(R.id.tag_fan_other_func_key).toString();
        try {
            if (!PreferenceUtils.containKey(SOUP_REMIND)) {
                List<String> listButton = TestDatas.createDialogText("??????", "??????");

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
                        //???????????? ?????????
                        final long time = (long) (index1 * 60 * 60 * 1000 + index2 * 60 * 1000);
                        //???????????? ?????????
                        final long time1 = System.currentTimeMillis();
                        if (fan != null) {
                            ToolUtils.logEvent(fan.getDt(), "????????????:" + index1 + ":" + index2, "roki_??????");
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
                        //????????????168?????? 5916S?????? ?????????????????????
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
                //?????????????????????
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


    //????????????????????????????????????
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



    //????????????
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

    public void closeTask(){
        if (timer_remind != null) {
            timer_remind.cancel();
            timer_remind = null;
        }
        if (mHolder != null) {
            mHolder.stopAnimation();
        }
    }

    //????????????
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


    //????????????????????????
    private void stopBroadcastReceiver() {
        AlarmManager alarmManager = AlarmManagerUtil.getInstance(mContext);
        Intent intent = new Intent(mContext, Fan8700alarmReceiver.class);
        intent.setAction("Fan8700alarmReceiver");
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    //????????????????????????
    private void startBroadcastReceiver(long time) {
        AlarmManager alarmManager = AlarmManagerUtil.getInstance(mContext);
        Intent intent = new Intent(mContext, Fan8700alarmReceiver.class);
        intent.setAction("Fan8700alarmReceiver");
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
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
            dialogByType.setContentText(mContext.getString(R.string.device_close_work));
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

    //??????????????????
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

    //???????????????
    void cancelCountDown_clean() {
        switchCountDown_clean(false, PreferenceUtils.getInt("dialogClean", -1));
    }

    // ???????????????
    void beginCountDown_clean(long msgtime, int pos) {
        long time = System.currentTimeMillis();
        PreferenceUtils.setString(CLEAN_REMIND, time + ":" + msgtime * 60 * 1000);
        count_clean = (short) (msgtime * 60);
        switchCountDown_clean(true, pos);
    }

    //????????????????????????????????????
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

    Context mContext;
    ImageView mImageView;
    TextView mTvDesc;
    TextView mTvName;
    TextView mTvRunWorkText;
    LinearLayout mItemView;
    LinearLayout mLlDefaultText;
    ImageView mStateShow;
    AlphaAnimation mAlphaAnimation;

    public FanOtherFuncViewHolder(Context context, View itemView) {
        super(itemView);

        mContext = context;
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
            mAlphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(mContext, R.anim.device_rika_dot_alpha);
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



