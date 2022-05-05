package com.robam.roki.ui.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.EventUtils;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.TimeUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.DeviceTimeRemindEvent;
import com.robam.common.events.RikaStatusChangedEvent;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaModeName;
import com.robam.common.pojos.device.rika.RikaModel;
import com.robam.common.pojos.device.rika.RikaStatus;
import com.robam.common.util.AlarmManagerUtil;
import com.robam.roki.R;
import com.robam.roki.broadcast.DeviceTimeToRemindReceiver;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.model.bean.FanDeviceRemindSoup;
import com.robam.roki.model.helper.HelperRikaData;
import com.robam.roki.ui.dialog.RikaFanTimeRemindDialog;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.TestDatas;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by 14807 on 2018/1/24.
 * 主功能区域适配器
 */

public class OtherFuncAdapter extends RecyclerView.Adapter<OtherFuncViewHolder> {

    private static int sterilizerStatus;
    private LayoutInflater mInflater;
    private Context mContext;
    AbsRika mRika;
    List<DeviceConfigurationFunctions> mDatas;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    private short mRikaFanPower;
    private short mStoveHeadLeftPower;
    private short mStoveHeadRightPower;
    private short mSteamRunModel;
    private short mSteamOvenRunModel;
    private short mSteamWorkStatus;
    private short mSteamOvenWorkStatus;
    private int mSteamWorkRemainingTime;
    private int mSteamOvenWorkRemainingTime;
    public static String SOUP_REMIND = "rika_soup_remind";//计时提醒本地存储key
    Timer timer_remind;
    int count_remind;//计时提醒倒计时

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    remind();
                    break;
            }
        }
    };
    private short mSterilWorkStatus;
    private short mSterilDoorLockStatus;
    private int mSterilWorkTimeLeft;
    private FanDeviceRemindSoup mFanDeviceRemindSoup;
    private IRokiDialog mRemindSoupNoticeDialog;

    @Subscribe
    public void onEvent(RikaStatusChangedEvent event) {
        if (mRika == null || !Objects.equal(mRika.getID(), event.pojo.getID()))
            return;
        mRikaFanPower = event.pojo.rikaFanPower;
        mStoveHeadLeftPower = event.pojo.stoveHeadLeftPower;
        mStoveHeadRightPower = event.pojo.stoveHeadRightPower;
        mSteamRunModel = event.pojo.steamRunModel;
        mSteamOvenRunModel = event.pojo.steamOvenRunModel;
        mSteamWorkStatus = event.pojo.steamWorkStatus;
        mSteamOvenWorkStatus = event.pojo.steamOvenWorkStatus;
        mSteamWorkRemainingTime = event.pojo.steamWorkRemainingTime;
        mSteamOvenWorkRemainingTime = event.pojo.steamOvenTimeWorkRemaining;
        mSterilWorkStatus = event.pojo.sterilWorkStatus;
        mSterilDoorLockStatus = event.pojo.sterilDoorLockStatus;
        mSterilWorkTimeLeft = event.pojo.sterilWorkTimeLeft;
        mRika = event.pojo;

        LogUtils.i("20180412", " mRikaFanPower:" + mRikaFanPower + " mStoveHeadLeftPower:" + mStoveHeadLeftPower +
                " mStoveHeadRightPower:" + mStoveHeadRightPower + " mSteamRunModel:" + mSteamRunModel);
        this.notifyDataSetChanged();
    }

    @Subscribe
    public void onEvent(DeviceTimeRemindEvent event) {
        resumeView();
    }

    public OtherFuncAdapter(Context context, AbsRika rika, List<DeviceConfigurationFunctions> data,
                            OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mContext = context;
        mDatas = data;
        mRika = rika;
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        mInflater = LayoutInflater.from(context);
        EventUtils.regist(this);
        resumeView();
    }

    @Override
    public OtherFuncViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        View view = mInflater.inflate(R.layout.item_otherfunc_page, parent, false);
        ScreenAdapterTools.getInstance().loadView(view);
        OtherFuncViewHolder otherFuncViewHolder = new OtherFuncViewHolder(mContext, view);
        otherFuncViewHolder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRecyclerViewItemClickListener.onItemClick(v);
                itemClickListener(v);
            }
        });
        return otherFuncViewHolder;
    }

    private void itemClickListener(View v) {
        String tag = v.getTag().toString();
        if ("timeReminding".equals(tag)) {
            soupRemind(v);
        }
    }

    @Override
    public void onBindViewHolder(OtherFuncViewHolder holder, int position) {
        String count_remind_time = TimeUtils.secToHourMinSec(count_remind);
        String sterilWorkTimeLeft = TimeUtils.secToHourMinSec(mSterilWorkTimeLeft);
        Glide.with(mContext).load(mDatas.get(position).backgroundImg)
                .transition(DrawableTransitionOptions.with(new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()))
                .into(holder.mImageView);
        holder.mTvWorkName.setVisibility(View.GONE);
        holder.mTvName.setVisibility(View.VISIBLE);
        holder.mTvDesc.setVisibility(View.VISIBLE);
        holder.mLlDefaultText.setVisibility(View.VISIBLE);
        if (mDatas != null && mDatas.size() > 0) {
            if (RikaModeName.SMOKE_AIR_VOLUME.equals(mDatas.get(position).functionCode)) {
                holder.mTvName.setText(mDatas.get(position).functionName);

                if (mRikaFanPower != 0) {
//                    holder.mTvName.setText(R.string.fan_run_open);
//                    holder.mStateShow.setBackground(cx.getResources().getDrawable(R.drawable.shape_rika_round_yellow_dot));
                    holder.mTvWorkName.setText(R.string.fan_run_open);
                    holder.mTvWorkName.setVisibility(View.VISIBLE);
                    holder.mLlDefaultText.setVisibility(View.GONE);
                    holder.mStateShow.setBackground(mContext.getResources().getDrawable(R.drawable.shape_rika_round_yellow_dot));
                    holder.startAnimation();
                } else {
                    holder.mTvName.setText(mDatas.get(position).functionName);
                    holder.mTvDesc.setText(mDatas.get(position).msg);
                    holder.mTvWorkName.setVisibility(View.GONE);
                    holder.mLlDefaultText.setVisibility(View.VISIBLE);
                    holder.mStateShow.setBackground(null);
                    holder.stopAnimation();
                }
            } else if (TextUtils.equals(RikaModeName.STEAMING_ROAST_MODE, mDatas.get(position).functionCode)) {
                //蒸烤模式
                if (mSteamOvenRunModel != RikaModel.SteamOven.NO_MOEL && mSteamOvenWorkStatus == RikaStatus.STEAMOVEN_RUN) {
                    holder.startAnimation();
                    if (mRika.steamOvenRunModel == 19) {
                        holder.mTvName.setText("保温中");
                    }else {
                        holder.mTvName.setText(TimeUtils.secToHourMinSec(mSteamOvenWorkRemainingTime) + " 后完成工作");
                    }
                    holder.mTvDesc.setVisibility(View.GONE);
                    holder.mStateShow.setBackground(mContext.getResources().getDrawable(R.drawable.shape_rika_round_yellow_dot));
                } else if (mSteamOvenRunModel != RikaModel.SteamOven.NO_MOEL && mSteamOvenWorkStatus == RikaStatus.STEAMOVEN_STOP) {
                    holder.startAnimation();
                    if (mRika.steamOvenRunModel == 19) {
                        holder.mTvName.setText("保温暂停中");
                    }else {
                        holder.mTvName.setText("暂停中剩余"+TimeUtils.secToHourMinSec(mSteamOvenWorkRemainingTime));
                    }
                    holder.mTvDesc.setVisibility(View.GONE);
                    holder.mStateShow.setBackground(mContext.getResources().getDrawable(R.drawable.shape_rika_round_red_dot));
                } else if (mSteamOvenRunModel != RikaModel.SteamOven.NO_MOEL && mSteamOvenWorkStatus == RikaStatus.STEAMOVEN_PREHEAT) {
                    holder.startAnimation();
                    holder.mTvName.setText("预热中"+mRika.steamOvenWorkTemp + "℃");
                    holder.mTvDesc.setVisibility(View.GONE);
                    holder.mStateShow.setBackground(mContext.getResources().getDrawable(R.drawable.shape_rika_round_green_dot));
                } else {
                    holder.stopAnimation();
                    holder.mTvName.setText(mDatas.get(position).functionName);
                    holder.mTvName.setVisibility(View.VISIBLE);
                    holder.mTvDesc.setText(mDatas.get(position).msg);
                    holder.mStateShow.setBackground(null);
                }
            } else if (TextUtils.equals(RikaModeName.LOCAL_COOKBOOK, mDatas.get(position).functionCode)) {
                //定制菜谱
                holder.mTvName.setText(mDatas.get(position).functionName);
                holder.mTvDesc.setText(mDatas.get(position).msg);
                holder.mStateShow.setVisibility(View.GONE);
            } else if (TextUtils.equals(RikaModeName.CLEANING_AND_DISINFECTION_NEW, mDatas.get(position).functionCode)) {
                holder.mTvName.setText(mDatas.get(position).functionName);
                holder.mTvDesc.setText(mDatas.get(position).msg);
                holder.mStateShow.setVisibility(View.GONE);
            } else if (TextUtils.equals(RikaModeName.SMOKE_COOKER_STEAMING_ROAST_LINKAGE, mDatas.get(position).functionCode)) {
                holder.mTvName.setText(mDatas.get(position).functionName);
                holder.mTvDesc.setText(mDatas.get(position).msg);
                holder.mStateShow.setVisibility(View.GONE);
            } else if (RikaModeName.STEAMING_MODE.equals(mDatas.get(position).functionCode)) {
                holder.mStateShow.setVisibility(View.GONE);
                String time = TimeUtils.secToHourMinSec(mSteamWorkRemainingTime);
                if (mSteamRunModel != RikaModel.Steame.NO_MOEL && mSteamWorkStatus == RikaStatus.STEAM_RUN) {
//                    holder.mTvName.setText(time + cx.getString(R.string.rika_finish_work));
//                    holder.mTvDesc.setText(R.string.rika_work_dec);
                    holder.mTvWorkName.setText(time + mContext.getString(R.string.rika_finish_work));
                    holder.mTvWorkName.setVisibility(View.VISIBLE);
                    holder.mLlDefaultText.setVisibility(View.GONE);
                    holder.mStateShow.setBackground(mContext.getResources().getDrawable(R.drawable.shape_rika_round_yellow_dot));
                    holder.startAnimation();
                } else if (mSteamRunModel != RikaModel.Steame.NO_MOEL && mSteamWorkStatus == RikaStatus.STEAM_STOP) {
//                    holder.mTvName.setText(time + cx.getString(R.string.rika_steam_work_stop));
//                    holder.mTvDesc.setText(R.string.rika_steam_work_stop_dec);
                    holder.mTvWorkName.setText(mContext.getString(R.string.rika_steam_work_stop_center) + time);
                    holder.mTvWorkName.setVisibility(View.VISIBLE);
                    holder.mLlDefaultText.setVisibility(View.GONE);
                    holder.mStateShow.setBackground(mContext.getResources().getDrawable(R.drawable.shape_rika_round_red_dot));
                    holder.startAnimation();
                } else if (mSteamWorkStatus == RikaStatus.STEAM_PREHEAT) {
                    holder.startAnimation();
//                    holder.mTvName.setText(mRika.steamWorkTemp + "℃" + cx.getString(R.string.device_preheating));
//                    holder.mTvDesc.setText(R.string.rika_steam_work_preheating_dec);
                    holder.mTvWorkName.setText(mContext.getString(R.string.device_preheating) + mRika.steamWorkTemp + "℃");
                    holder.mTvWorkName.setVisibility(View.VISIBLE);
                    holder.mLlDefaultText.setVisibility(View.GONE);
                    holder.mStateShow.setBackground(mContext.getResources().getDrawable(R.drawable.shape_rika_round_green_dot));
                } else {
                    holder.stopAnimation();
                    holder.mTvWorkName.setVisibility(View.GONE);
                    holder.mLlDefaultText.setVisibility(View.VISIBLE);
                    holder.mTvName.setText(mDatas.get(position).functionName);
                    holder.mTvDesc.setText(mDatas.get(position).msg);
                    holder.mStateShow.setBackground(null);
                }
            } else if (RikaModeName.TIME_REMINDING.equals(mDatas.get(position).functionCode)) {
                if (count_remind > 0) {
                    holder.startAnimation();
//                    holder.mTvName.setText("0" + count_remind_time + cx.getString(R.string.fan_complete_timing));
//                    holder.mTvDesc.setText(cx.getString(R.string.fan_complete_timing_desc));
                    holder.mTvWorkName.setText(count_remind_time + mContext.getString(R.string.fan_complete_timing));
                    holder.mTvWorkName.setVisibility(View.VISIBLE);
                    holder.mLlDefaultText.setVisibility(View.GONE);
                    holder.mStateShow.setBackground(mContext.getResources().getDrawable(R.drawable.shape_rika_round_yellow_dot));
                } else {
                    holder.stopAnimation();
                    holder.mTvWorkName.setVisibility(View.GONE);
                    holder.mLlDefaultText.setVisibility(View.VISIBLE);
                    holder.mTvName.setText(mDatas.get(position).functionName);
                    holder.mTvDesc.setText(mDatas.get(position).msg);
                    holder.mStateShow.setBackground(null);
                }
            } else if (RikaModeName.OIL_NETWORK_DETECTION.equals(mDatas.get(position).functionCode)) {
                holder.mTvName.setText(mDatas.get(position).functionName);
                holder.mTvDesc.setText(mDatas.get(position).msg);
                holder.mStateShow.setVisibility(View.GONE);
            } else if (RikaModeName.SMOKE_COOKER_STEAMING_LINKAGE.equals(mDatas.get(position).functionCode)) {
                holder.mTvName.setText(mDatas.get(position).functionName);
                holder.mTvDesc.setText(mDatas.get(position).msg);
                holder.mStateShow.setVisibility(View.GONE);
            } else if (RikaModeName.STEAM_COOKBOOK.equals(mDatas.get(position).functionCode)) {
                holder.mTvName.setText(mDatas.get(position).functionName);
                holder.mTvDesc.setText(mDatas.get(position).msg);
                holder.mStateShow.setVisibility(View.GONE);
            } else if (RikaModeName.CLEANING_AND_DISINFECTION.equals(mDatas.get(position).functionCode)) {
                if (mSterilWorkStatus == RikaStatus.STERIL_NOT || mSterilWorkStatus == RikaStatus.STERIL_ON || mSterilWorkStatus == RikaStatus.STERIL_OFF) {
                    holder.mTvName.setText(mDatas.get(position).functionName);
                    holder.mTvDesc.setText(mDatas.get(position).msg);
                    holder.mLlDefaultText.setVisibility(View.VISIBLE);
                    holder.mTvWorkName.setVisibility(View.GONE);
                    holder.mStateShow.setVisibility(View.GONE);
                    holder.stopAnimation();
                } else if (mSterilWorkStatus == RikaStatus.STERIL_DISIDFECT) {
//                    holder.mTvName.setText("0" + sterilWorkTimeLeft + cx.getString(R.string.sterilizer_complete_disinfect));
//                    holder.mTvDesc.setText(R.string.device_sterilizer_open_disinfection_mode);
                    sterilizerStatus = RikaStatus.STERIL_DISIDFECT;
                    holder.mTvWorkName.setVisibility(View.VISIBLE);
                    holder.mLlDefaultText.setVisibility(View.GONE);
                    holder.mTvWorkName.setText(sterilWorkTimeLeft + mContext.getString(R.string.sterilizer_complete_disinfect));
                    holder.mStateShow.setBackground(mContext.getResources().getDrawable(R.drawable.shape_rika_round_yellow_dot));
                    holder.startAnimation();
                } else if (mSterilWorkStatus == RikaStatus.STERIL_CLEAN) {
//                    holder.mTvName.setText("0" + sterilWorkTimeLeft + cx.getString(R.string.sterilizer_complete_clean));
//                    holder.mTvDesc.setText(R.string.sterilizer_open_clean_mode);
                    sterilizerStatus = RikaStatus.STERIL_CLEAN;
                    holder.mTvWorkName.setVisibility(View.VISIBLE);
                    holder.mLlDefaultText.setVisibility(View.GONE);
                    holder.mTvWorkName.setText(sterilWorkTimeLeft + mContext.getString(R.string.sterilizer_complete_clean));
                    holder.mStateShow.setBackground(mContext.getResources().getDrawable(R.drawable.shape_rika_round_yellow_dot));
                    holder.startAnimation();
                } else if (mSterilWorkStatus == RikaStatus.STERIL_DRYING) {
//                    holder.mTvName.setText("0" + sterilWorkTimeLeft + cx.getString(R.string.sterilizer_complete_drying));
//                    holder.mTvDesc.setText(R.string.sterilizer_open_drying_mode);
                    sterilizerStatus = RikaStatus.STERIL_DRYING;
                    holder.mTvWorkName.setVisibility(View.VISIBLE);
                    holder.mLlDefaultText.setVisibility(View.GONE);
                    holder.mTvWorkName.setText(sterilWorkTimeLeft + mContext.getString(R.string.sterilizer_complete_drying));
                    holder.mStateShow.setBackground(mContext.getResources().getDrawable(R.drawable.shape_rika_round_yellow_dot));
                    holder.startAnimation();
                } else if (mSterilWorkStatus == RikaStatus.STERIL_PRE) {
//                    holder.mTvName.setText("0" + sterilWorkTimeLeft + cx.getString(R.string.sterilizer_complete_pre));
//                    holder.mTvDesc.setText(R.string.sterilizer_open_pre_mode);
                    sterilizerStatus = RikaStatus.STERIL_PRE;
                    holder.mTvWorkName.setVisibility(View.VISIBLE);
                    holder.mLlDefaultText.setVisibility(View.GONE);
                    holder.mTvWorkName.setText(sterilWorkTimeLeft + mContext.getString(R.string.sterilizer_complete_pre));
                    holder.mStateShow.setBackground(mContext.getResources().getDrawable(R.drawable.shape_rika_round_yellow_dot));
                    holder.startAnimation();
                } else if (mSterilWorkStatus == RikaStatus.STERIL_DEGERMING) {
//                    holder.mTvName.setText("0" + sterilWorkTimeLeft + cx.getString(R.string.sterilizer_complete_degerming));
//                    holder.mTvDesc.setText(R.string.sterilizer_open_degerming_mode);
                    sterilizerStatus = RikaStatus.STERIL_DEGERMING;
                    holder.mTvWorkName.setVisibility(View.VISIBLE);
                    holder.mLlDefaultText.setVisibility(View.GONE);
                    holder.mTvWorkName.setText(sterilWorkTimeLeft + mContext.getString(R.string.sterilizer_complete_degerming));
                    holder.mStateShow.setBackground(mContext.getResources().getDrawable(R.drawable.shape_rika_round_yellow_dot));
                    holder.startAnimation();
                } else if (mSterilWorkStatus == RikaStatus.STERIL_APPOINATION) {
                    sterilizerStatus = RikaStatus.STERIL_APPOINATION;
                    holder.mTvWorkName.setVisibility(View.VISIBLE);
                    holder.mLlDefaultText.setVisibility(View.GONE);
                    holder.mTvWorkName.setText(sterilWorkTimeLeft + mContext.getString(R.string.sterilizer_complete_pre_dis));
                    holder.mStateShow.setBackground(mContext.getResources().getDrawable(R.drawable.shape_rika_round_yellow_dot));
                    holder.startAnimation();
                } else if (mSterilWorkStatus == RikaStatus.STERIL_APPOINATION_DRYING) {
                    sterilizerStatus = RikaStatus.STERIL_APPOINATION_DRYING;
                    holder.mTvWorkName.setVisibility(View.VISIBLE);
                    holder.mLlDefaultText.setVisibility(View.GONE);
                    holder.mTvWorkName.setText(sterilWorkTimeLeft + mContext.getString(R.string.sterilizer_complete_pre_dry));
                    holder.mStateShow.setBackground(mContext.getResources().getDrawable(R.drawable.shape_rika_round_yellow_dot));
                    holder.startAnimation();
                } else if (mSterilWorkStatus == RikaStatus.STERIL_APPOINATION_CLEAN) {
                    sterilizerStatus = RikaStatus.STERIL_APPOINATION_CLEAN;
                    holder.mTvWorkName.setVisibility(View.VISIBLE);
                    holder.mLlDefaultText.setVisibility(View.GONE);
                    holder.mTvWorkName.setText(sterilWorkTimeLeft + mContext.getString(R.string.sterilizer_complete_pre_clean));
                    holder.mStateShow.setBackground(mContext.getResources().getDrawable(R.drawable.shape_rika_round_yellow_dot));
                    holder.startAnimation();
                } else if (mSterilWorkStatus == RikaStatus.STERIL_COER_DISIDFECT) {
                    sterilizerStatus = RikaStatus.STERIL_COER_DISIDFECT;
                    holder.mTvWorkName.setVisibility(View.VISIBLE);
                    holder.mLlDefaultText.setVisibility(View.GONE);
                    holder.mTvWorkName.setText(sterilWorkTimeLeft + mContext.getString(R.string.sterilizer_complete_coer_disidfect));
                    holder.mStateShow.setBackground(mContext.getResources().getDrawable(R.drawable.shape_rika_round_yellow_dot));
                    holder.startAnimation();
                } else if (mSterilWorkStatus == RikaStatus.STERIL_ALARM) {
                    holder.mStateShow.setBackground(mContext.getResources().getDrawable(R.drawable.shape_rika_round_red_dot));
                    holder.mTvWorkName.setVisibility(View.VISIBLE);
                    holder.mLlDefaultText.setVisibility(View.GONE);

                    String content = HelperRikaData.getAlarmContent(mContext, sterilizerStatus);
                    holder.mTvWorkName.setText(sterilWorkTimeLeft + content);
                    holder.startAnimation();
                }
            } else if (RikaModeName.SMOKE_STOVE_ELIMINATION_LINKAGE.equals(mDatas.get(position).functionCode)) {
                holder.mTvName.setText(mDatas.get(position).functionName);
                holder.mTvDesc.setText(mDatas.get(position).msg);
                holder.mStateShow.setVisibility(View.GONE);
            } else if (RikaModeName.APPOINTMENT_DISINFECTION.equals(mDatas.get(position).functionCode)) {
                holder.mTvName.setText(mDatas.get(position).functionName);
                holder.mTvDesc.setText(mDatas.get(position).msg);
                holder.mStateShow.setVisibility(View.GONE);
            } else {

            }
            holder.mItemView.setTag(mDatas.get(position).functionCode);
            holder.mItemView.setTag(R.id.tag_fan_other_func_key, mDatas.get(position).functionParams);
        }

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    private void resumeView() {
        if (PreferenceUtils.containKey(SOUP_REMIND)) {
            String time = PreferenceUtils.getString(SOUP_REMIND, "none");
            String[] strs = null;
            if (!"none".equals(time)) {
                strs = time.split(":");
                long tt = Long.valueOf(strs[0]) + Long.valueOf(strs[1]);
                if (System.currentTimeMillis() > tt) {
                    PreferenceUtils.remove(SOUP_REMIND);
                    count_remind = 0;
                    switchCountDown_remind(false);
                } else {
                    short down = (short) ((tt - System.currentTimeMillis()) / 1000);
                    count_remind = down;
                    switchCountDown_remind(true);
                }
            }
        }
        if (!mRika.isConnected()) return;
    }

    private void remind() {

        try {
            if (count_remind < 0) return;
            if (count_remind == 0) {
                LogUtils.i("20190427", "remind:");
                boolean isCountdown = PreferenceUtils.getBool("isCountdown", false);
                LogUtils.i("20190427", "remind_isCountdown:" + isCountdown);
                if (isCountdown) {
                    if (mRemindSoupNoticeDialog == null) {
                        mRemindSoupNoticeDialog = RokiDialogFactory.createDialogByType(mContext, DialogUtil.DIALOG_TYPE_16);
                        mRemindSoupNoticeDialog.setTitleText(R.string.fan_remind_timing);
                        mRemindSoupNoticeDialog.setContentText(R.string.device_time_remind_desc);
                        mRemindSoupNoticeDialog.setOkBtn(R.string.fan_Know, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mRemindSoupNoticeDialog.dismiss();
                            }
                        });
                        mRemindSoupNoticeDialog.show();
                        switchCountDown_remind(false);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            count_remind--;
            notifyDataSetChanged();
        }
    }

    /**
     * 开启或关闭煲汤提醒倒计时
     *
     * @param countdown true:开启，false：关闭
     */
    void switchCountDown_remind(boolean countdown) {
        if (timer_remind != null) {
            timer_remind.cancel();
            timer_remind = null;
        }
        if (countdown) {
            PreferenceUtils.setBool("isCountdown", true);
            timer_remind = new Timer();
            timer_remind.schedule(new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(2);
                }
            }, 0, 1000);

        } else {
            if (PreferenceUtils.containKey(SOUP_REMIND)) {
                PreferenceUtils.remove(SOUP_REMIND);
            }
            PreferenceUtils.remove("isCountdown");
            count_remind = 0;
        }
    }

    /**
     * 关闭煲汤提醒广播
     */
    private void stopBroadcastReceiver() {
        AlarmManager alarmManager = AlarmManagerUtil.getInstance(mContext);
        Intent intent = new Intent(mContext, DeviceTimeToRemindReceiver.class);
        intent.setAction("DeviceTimeToRemindReceiver");
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    /**
     * 开启计时提醒广播
     *
     * @param time
     * @param timestr
     */
    private void startBroadcastReceiver(long time, String timestr) {
        AlarmManager alarmManager = AlarmManagerUtil.getInstance(mContext);
        Intent intent = new Intent(mContext, DeviceTimeToRemindReceiver.class);
        intent.setAction("DeviceTimeToRemindReceiver");
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }

    private void soupRemind(View view) {
        if (!mRika.isConnected()) {
            ToastUtils.showShort(R.string.device_connected);
            return;
        }
        String tag = view.getTag(R.id.tag_fan_other_func_key).toString();
        if (!PreferenceUtils.containKey(SOUP_REMIND))
            try {
                mFanDeviceRemindSoup = JsonUtils.json2Pojo(tag, FanDeviceRemindSoup.class);
                if (mFanDeviceRemindSoup == null) return;
                List<Integer> hour = mFanDeviceRemindSoup.getParam().getHour().getValue();
                final List<Integer> minute = mFanDeviceRemindSoup.getParam().getMinute().getValue();
                List<Integer> newMinute = TestDatas.createRikaData(minute);
                int hourDefault = mFanDeviceRemindSoup.getParam().getHourDefault().getValue();
                int minuteDefault = mFanDeviceRemindSoup.getParam().getMinuteDefault().getValue();
                if (hour == null || minute == null) return;
                LogUtils.i("20181119", "hourDefault:" + hourDefault + " minuteDefault:" + minuteDefault);
                RikaFanTimeRemindDialog rikaFanTimeRemindDialog = new RikaFanTimeRemindDialog(mContext, hour,
                        minute, hourDefault, minuteDefault, newMinute);
                rikaFanTimeRemindDialog.show(rikaFanTimeRemindDialog);
                rikaFanTimeRemindDialog.setListener(new RikaFanTimeRemindDialog.PickListener() {
                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onConfirm(int index1, int index2) {
                        sendData(index1, index2);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        else {

            final IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(mContext, DialogUtil.DIALOG_TYPE_10);
            dialogByType.setTitleText(R.string.fan_remind_timing);
            dialogByType.setContentText(R.string.device_stove_off_desc);
            dialogByType.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogByType.dismiss();
                    switchCountDown_remind(false);
                    stopBroadcastReceiver();
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

    private void sendData(int hour, int min) {

        long time = hour * 60 * 60 * 1000 + min * 60 * 1000;
        long time1 = System.currentTimeMillis();
        PreferenceUtils.setString(SOUP_REMIND, time1 + ":" + time);
        count_remind = (short) (time / 1000);
        switchCountDown_remind(true);
        startBroadcastReceiver(time1 + time, time1 + ":" + time);
    }

    // 广播接收器
    public class UpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "广播事件收到", Toast.LENGTH_LONG).show();
            switchCountDown_remind(false);
        }
    }
}

class OtherFuncViewHolder extends RecyclerView.ViewHolder {

    ImageView mImageView;
    TextView mTvDesc;
    TextView mTvName;
    TextView mTvWorkName;
    LinearLayout mItemView;
    LinearLayout mLlDefaultText;
    ImageView mStateShow;
    AlphaAnimation mAlphaAnimation;
    Context mContext;

    public OtherFuncViewHolder(Context context, View itemView) {
        super(itemView);
        this.setIsRecyclable(false);
        mContext = context;
        mTvName = itemView.findViewById(R.id.tv_name);
        mTvWorkName = itemView.findViewById(R.id.tv_work_name);
        mLlDefaultText = itemView.findViewById(R.id.ll_default_text);
        mImageView = itemView.findViewById(R.id.iv_view);
        mTvDesc = itemView.findViewById(R.id.tv_desc);
        mItemView = itemView.findViewById(R.id.itemView);
        mStateShow = itemView.findViewById(R.id.iv_state_show);
    }

    public void startAnimation() {

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
        if (mAlphaAnimation != null) {
            mAlphaAnimation.cancel();
            mAlphaAnimation = null;
        }
    }

}
