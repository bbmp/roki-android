package com.robam.roki.ui.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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

import androidx.recyclerview.widget.RecyclerView;

import com.aispeech.kernel.Abs;
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
import com.robam.common.events.IntegStoveStatusChangedEvent;
import com.robam.common.events.IntegratedStoveStatusChangedEvent;
import com.robam.common.events.RikaStatusChangedEvent;
import com.robam.common.pojos.device.integratedStove.AbsIntegratedStove;
import com.robam.common.pojos.device.integratedStove.IntegStoveModel;
import com.robam.common.pojos.device.integratedStove.IntegStoveStatus;
import com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum;
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
import com.robam.roki.ui.page.device.integratedStove.SteamOvenHelper;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.TestDatas;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.legent.ContextIniter.cx;

/**
 * Created by 14807 on 2018/1/24.
 * 主功能区域适配器
 */

public class OtherFunc2Adapter extends RecyclerView.Adapter<OtherFunc2ViewHolder> {

    private static int sterilizerStatus;
    private LayoutInflater mInflater;
    private Context mContext;
    AbsIntegratedStove mIntegratedStove;
    List<DeviceConfigurationFunctions> mDatas;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    private short fan_gear;

    private short workState;

    private int totalRemainSeconds;
    private int totalRemainSeconds2;
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
    public void replceData(IntegStoveStatusChangedEvent event) {
        if (mIntegratedStove == null || !Objects.equal(mIntegratedStove.getID(), event.pojo.getID())) {
            return;
        }
        fan_gear = event.pojo.fan_gear;
        workState = event.pojo.workState;
        totalRemainSeconds = event.pojo.totalRemainSeconds;
        totalRemainSeconds2 = event.pojo.totalRemainSecondsH;
        mSterilWorkStatus = event.pojo.sterilWorkStatus;
        mSterilDoorLockStatus = event.pojo.sterilDoorLockStatus;
        mSterilWorkTimeLeft = event.pojo.sterilWorkTimeLeft;
        mIntegratedStove = event.pojo;

        this.notifyDataSetChanged();
    }

    @Subscribe
    public void onEvent(DeviceTimeRemindEvent event) {
        resumeView();
    }

    public OtherFunc2Adapter(Context context, AbsIntegratedStove absIntegratedStove, List<DeviceConfigurationFunctions> data,
                             OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mContext = context;
        mDatas = data;
        mIntegratedStove = absIntegratedStove;
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        mInflater = LayoutInflater.from(context);
        EventUtils.regist(this);
        resumeView();
    }

    @Override
    public OtherFunc2ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        View view = mInflater.inflate(R.layout.item_otherfunc_page, parent, false);
        ScreenAdapterTools.getInstance().loadView(view);
        OtherFunc2ViewHolder otherFuncViewHolder = new OtherFunc2ViewHolder(view);
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
    public void onBindViewHolder(OtherFunc2ViewHolder holder, int position) {
        String count_remind_time = TimeUtils.secToHourMinSec(count_remind);
        String sterilWorkTimeLeft = TimeUtils.secToHourMinSec(mSterilWorkTimeLeft);
        Glide.with(cx).load(mDatas.get(position).backgroundImg)
                .transition(DrawableTransitionOptions.with(new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()))
                .into(holder.mImageView);
        holder.mTvWorkName.setVisibility(View.GONE);
        holder.mTvName.setVisibility(View.VISIBLE);
        holder.mTvDesc.setVisibility(View.VISIBLE);
        holder.mLlDefaultText.setVisibility(View.VISIBLE);
        if (mDatas != null && mDatas.size() > 0) {
            if (RikaModeName.SMOKE_AIR_VOLUME.equals(mDatas.get(position).functionCode)) {
                holder.mTvName.setText(mDatas.get(position).functionName);

                if (fan_gear != 0) {
//                    holder.mTvName.setText(R.string.fan_run_open);
//                    holder.mStateShow.setBackground(cx.getResources().getDrawable(R.drawable.shape_rika_round_yellow_dot));
                    holder.mTvWorkName.setText(R.string.fan_run_open);
                    holder.mTvWorkName.setVisibility(View.VISIBLE);
                    holder.mLlDefaultText.setVisibility(View.GONE);
                    holder.mStateShow.setBackground(cx.getResources().getDrawable(R.drawable.shape_rika_round_green_dot));
                    holder.startAnimation();
                } else {
                    holder.mTvName.setText(mDatas.get(position).functionName);
                    holder.mTvDesc.setText(mDatas.get(position).msg);
                    holder.mTvWorkName.setVisibility(View.GONE);
                    holder.mLlDefaultText.setVisibility(View.VISIBLE);
                    holder.mStateShow.setBackground(null);
                    holder.stopAnimation();
                }
            } else if (TextUtils.equals(IntegStoveModel.STEAMING_ROAST_MODE, mDatas.get(position).functionCode)) {
                SteamOvenModeEnum match = SteamOvenModeEnum.match(mIntegratedStove.mode);
                if (match!=null && match == SteamOvenModeEnum.CHUGOU){
                    if (workState == IntegStoveStatus.workState_preheat_time_out || workState == IntegStoveStatus.workState_work_time_out) {
                        holder.startAnimation();
                        holder.mTvName.setText("暂停中");
                        holder.mTvDesc.setVisibility(View.GONE);
                        holder.mStateShow.setBackground(cx.getResources().getDrawable(R.drawable.shape_rika_round_red_dot));
                    }else if (workState == IntegStoveStatus.workState_work){
                        holder.startAnimation();
                        holder.mTvName.setText("除垢中" );
                        holder.mTvDesc.setVisibility(View.GONE);
                        holder.mStateShow.setBackground(cx.getResources().getDrawable(R.drawable.shape_rika_round_green_dot));
                    }else {
                        holder.stopAnimation();
                        holder.mTvName.setText(mDatas.get(position).functionName);
                        holder.mTvName.setVisibility(View.VISIBLE);
                        holder.mTvDesc.setText(mDatas.get(position).msg);
                        holder.mStateShow.setBackground(null);
                    }
                }else {


                //蒸烤模式
                if (workState == IntegStoveStatus.workState_order) {
                    holder.startAnimation();
                    holder.mTvName.setText("预约中");

                    holder.mTvDesc.setVisibility(View.GONE);
                    holder.mStateShow.setBackground(cx.getResources().getDrawable(R.drawable.shape_rika_round_yellow_dot));
                } else if (workState == IntegStoveStatus.workState_preheat) {
                    holder.startAnimation();
                    holder.mTvName.setText("预热中" + mIntegratedStove.curTemp + "℃");
                    holder.mTvDesc.setVisibility(View.GONE);
                    holder.mStateShow.setBackground(cx.getResources().getDrawable(R.drawable.shape_rika_round_yellow_dot));
                } else if (workState == IntegStoveStatus.workState_preheat_time_out || workState == IntegStoveStatus.workState_work_time_out) {
                    holder.startAnimation();
                    holder.mTvName.setText("暂停中剩余" + TimeUtils.secToHourMinSec(totalRemainSeconds + totalRemainSeconds2 * 256));
                    holder.mTvDesc.setVisibility(View.GONE);
                    holder.mStateShow.setBackground(cx.getResources().getDrawable(R.drawable.shape_rika_round_red_dot));
                }else if (workState == IntegStoveStatus.workState_work){
                    holder.startAnimation();
                    holder.mTvName.setText("工作中剩余" + TimeUtils.secToHourMinSec(totalRemainSeconds + totalRemainSeconds2 * 256));
                    holder.mTvDesc.setVisibility(View.GONE);
                    holder.mStateShow.setBackground(cx.getResources().getDrawable(R.drawable.shape_rika_round_green_dot));
                }else {
                    holder.stopAnimation();
                    holder.mTvName.setText(mDatas.get(position).functionName);
                    holder.mTvName.setVisibility(View.VISIBLE);
                    holder.mTvDesc.setText(mDatas.get(position).msg);
                    holder.mStateShow.setBackground(null);
                }
                }
            } else if ("multiStepModel".equals(mDatas.get(position).functionCode)){
                holder.mTvName.setText(mDatas.get(position).functionName);
                holder.mTvDesc.setText(mDatas.get(position).msg);
                holder.mStateShow.setVisibility(View.GONE);
                Glide.with(cx).load(mDatas.get(position).backgroundImg)
                        .placeholder(R.drawable.ic_multi_img)
                        .error(R.drawable.ic_multi_img)
                        .transition(DrawableTransitionOptions.with(new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()))
                        .into(holder.mImageView);
            }else {
                holder.mTvName.setText(mDatas.get(position).functionName);
                holder.mTvDesc.setText(mDatas.get(position).msg);
                holder.mStateShow.setVisibility(View.GONE);
                Glide.with(cx).load(mDatas.get(position).backgroundImg)
                        .transition(DrawableTransitionOptions.with(new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()))
                        .into(holder.mImageView);
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
        if (!mIntegratedStove.isConnected()) {
            return;
        }
    }

    private void remind() {

        try {
            if (count_remind < 0) {
                return;
            }
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
        AlarmManager alarmManager = AlarmManagerUtil.getInstance(cx);
        Intent intent = new Intent(cx, DeviceTimeToRemindReceiver.class);
        intent.setAction("DeviceTimeToRemindReceiver");
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(cx, 0, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    /**
     * 开启计时提醒广播
     *
     * @param time
     * @param timestr
     */
    private void startBroadcastReceiver(long time, String timestr) {
        AlarmManager alarmManager = AlarmManagerUtil.getInstance(cx);
        Intent intent = new Intent(cx, DeviceTimeToRemindReceiver.class);
        intent.setAction("DeviceTimeToRemindReceiver");
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(cx, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }

    private void soupRemind(View view) {
        if (!mIntegratedStove.isConnected()) {
            ToastUtils.showShort(R.string.device_connected);
            return;
        }
        String tag = view.getTag(R.id.tag_fan_other_func_key).toString();
        if (!PreferenceUtils.containKey(SOUP_REMIND)) {
            try {
                mFanDeviceRemindSoup = JsonUtils.json2Pojo(tag, FanDeviceRemindSoup.class);
                if (mFanDeviceRemindSoup == null) {
                    return;
                }
                List<Integer> hour = mFanDeviceRemindSoup.getParam().getHour().getValue();
                final List<Integer> minute = mFanDeviceRemindSoup.getParam().getMinute().getValue();
                List<Integer> newMinute = TestDatas.createRikaData(minute);
                int hourDefault = mFanDeviceRemindSoup.getParam().getHourDefault().getValue();
                int minuteDefault = mFanDeviceRemindSoup.getParam().getMinuteDefault().getValue();
                if (hour == null || minute == null) {
                    return;
                }
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
        } else {

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

class OtherFunc2ViewHolder extends RecyclerView.ViewHolder {

    ImageView mImageView;
    TextView mTvDesc;
    TextView mTvName;
    TextView mTvWorkName;
    LinearLayout mItemView;
    LinearLayout mLlDefaultText;
    ImageView mStateShow;
    AlphaAnimation mAlphaAnimation;

    public OtherFunc2ViewHolder(View itemView) {
        super(itemView);
        this.setIsRecyclable(false);
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
            mAlphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(cx, R.anim.device_rika_dot_alpha);
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
