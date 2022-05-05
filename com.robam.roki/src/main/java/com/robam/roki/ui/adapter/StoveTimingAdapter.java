package com.robam.roki.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.StoveAlarmEvent;
import com.robam.common.events.StoveStatusChangedEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.common.pojos.dictionary.StoveAlarm;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.model.bean.StoveTimingParams;
import com.robam.roki.model.helper.HelperStoveData;
import com.robam.roki.utils.AlarmDataUtils;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.JsonUtils;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.roki.utils.StringConstantsUtil;
import com.robam.roki.utils.ToolUtils;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;

import static com.robam.common.pojos.device.Stove.Stove.StoveHead.LEFT_ID;
import static com.robam.common.pojos.device.Stove.Stove.StoveHead.RIGHT_ID;

/**
 * Created by 14807 on 2018/5/22.
 */

public class StoveTimingAdapter extends RecyclerView.Adapter<StoveTimingViewHolder> {

    Stove mStove;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<DeviceConfigurationFunctions> mDeviceConfigurationFunctions;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    short leftStatus;
    short rightStatus;
    private List<Integer> mLeftTimeList;
    private List<Integer> mRightTimeList;
    private final int LEFT_TIMING = 0;//左定时
    private final int RIGHT_TIMING = 1;//右定时
    private IRokiDialog mLeftTimeDialog;
    private IRokiDialog mRightTimingDialog;
    private String mDefaultMinLeft;
    private String mDefaultMinRight;
    //    public Tracker tracker;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case LEFT_TIMING:
                    leftTimingData((String) msg.obj);
                    break;
                case RIGHT_TIMING:
                    rightTimingData((String) msg.obj);
                    break;
            }
        }
    };


    @Subscribe
    public void onEvent(StoveStatusChangedEvent event) {
        if (mStove == null || !Objects.equal(mStove.getID(), event.pojo.getID()))
            return;
        leftStatus = mStove.leftHead.status;
        rightStatus = mStove.rightHead.status;
//        LogUtils.i("20180523", "leftStatus:" + leftStatus + " rightStatus:" + rightStatus);
        notifyDataSetChanged();

    }

    public StoveTimingAdapter(Context context, Stove stove,
                              List<DeviceConfigurationFunctions> deviceConfigurationFunctions,
                              OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mContext = context;
        mStove = stove;
        mDeviceConfigurationFunctions = deviceConfigurationFunctions;
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        mInflater = LayoutInflater.from(context);
        EventUtils.regist(this);
    }

    @Override
    public StoveTimingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_stove_quickly_page, parent, false);
        ScreenAdapterTools.getInstance().loadView(view);
        StoveTimingViewHolder stoveTimingViewHolder = new StoveTimingViewHolder(view);
        stoveTimingViewHolder.iv_stove_button_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonRight(v);
            }
        });

        stoveTimingViewHolder.iv_stove_button_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonLeft(v);
            }
        });
        return stoveTimingViewHolder;
    }

    @Override
    public void onBindViewHolder(StoveTimingViewHolder holder, int position) {
        for (int i = 0; i < mDeviceConfigurationFunctions.size(); i++) {
            String functionParams = mDeviceConfigurationFunctions.get(i).functionParams;
            String functionCode = mDeviceConfigurationFunctions.get(i).functionCode;

            if ("timedLeftHeadOff".equals(functionCode)) {
                if (mStove.leftHead.status == StoveStatus.Off || mStove.leftHead.status == StoveStatus.StandyBy) {
                    holder.iv_stove_button_left.setImageResource(R.mipmap.img_time_off_fire);
                } else if (mStove.leftHead.status != StoveStatus.Off && mStove.leftHead.time != 0 ||
                        mStove.leftHead.status != StoveStatus.StandyBy && mStove.leftHead.time != 0) {
                    Glide.with(mContext).load(mDeviceConfigurationFunctions.get(i).backgroundImg).into(holder.iv_stove_button_left);
                } else if (mStove.leftHead.status != StoveStatus.Off && mStove.leftHead.time == 0
                        || mStove.leftHead.status != StoveStatus.StandyBy && mStove.leftHead.time == 0) {
                    Glide.with(mContext).load(mDeviceConfigurationFunctions.get(i).backgroundImgH).into(holder.iv_stove_button_left);
                }
                holder.iv_stove_button_left.setTag(R.id.tag_stove_timing_btn_left, functionCode);
                if (!TextUtils.isEmpty(functionParams)) {
                    try {
                        StoveTimingParams timingParams = com.legent.utils.JsonUtils.json2Pojo(functionParams, StoveTimingParams.class);
                        mLeftTimeList = timingParams.getParam().getOff().getValue();
                        mDefaultMinLeft = timingParams.getParam().getDefaultmin().getValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (mStove.leftHead.time == 0 || mStove.leftHead.status == StoveStatus.Off || mStove.leftHead.status == StoveStatus.StandyBy) {
                    holder.tv_fire_time_left.setVisibility(View.GONE);
                } else if (mStove.leftHead.time > 0 && mStove.leftHead.status == StoveStatus.Working) {
                    holder.tv_fire_time_left.setVisibility(View.VISIBLE);
                    holder.tv_fire_time_left.setText(com.legent.utils.TimeUtils.secToHourMinSec(mStove.leftHead.time));
                }

            } else if ("timedRightHeadOff".equals(functionCode)) {
                if (mStove.rightHead.status == StoveStatus.Off || mStove.rightHead.status == StoveStatus.StandyBy) {
                    holder.iv_stove_button_right.setImageResource(R.mipmap.img_time_off_fire);

                } else if (mStove.rightHead.status != StoveStatus.Off && mStove.rightHead.time != 0 ||
                        mStove.rightHead.status != StoveStatus.StandyBy && mStove.rightHead.time != 0) {
                    Glide.with(mContext).load(mDeviceConfigurationFunctions.get(i).backgroundImg).into(holder.iv_stove_button_right);

                } else if (mStove.rightHead.status != StoveStatus.Off && mStove.rightHead.time == 0 ||
                        mStove.rightHead.status != StoveStatus.StandyBy && mStove.rightHead.time == 0) {
                    Glide.with(mContext).load(mDeviceConfigurationFunctions.get(i).backgroundImgH).into(holder.iv_stove_button_right);
                }
                holder.iv_stove_button_right.setTag(R.id.tag_stove_timing_btn_right, functionCode);
                if (!TextUtils.isEmpty(functionParams)) {
                    try {
                        StoveTimingParams timingParams = com.legent.utils.JsonUtils.json2Pojo(functionParams, StoveTimingParams.class);
                        mRightTimeList = timingParams.getParam().getOff().getValue();
                        mDefaultMinRight = timingParams.getParam().getDefaultmin().getValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (mStove.rightHead.time == 0 || mStove.rightHead.status == StoveStatus.Off || mStove.rightHead.status == StoveStatus.StandyBy) {
                    holder.tv_fire_time_right.setVisibility(View.GONE);
                } else if (mStove.rightHead.time > 0 && mStove.rightHead.status == StoveStatus.Working) {
                    holder.tv_fire_time_right.setVisibility(View.VISIBLE);
                    holder.tv_fire_time_right.setText(com.legent.utils.TimeUtils.secToHourMinSec(mStove.rightHead.time));
                }
            } else if ("timedLeftHeadGrear".equals(functionCode)) {
                if (!TextUtils.isEmpty(functionParams)) {
                    String value;
                    String tips;
                    if (IRokiFamily.R9B37.equals(mStove.getDt())) {
                        value = JsonUtils.getValue(functionParams, mStove.leftHead.status);
                        tips = JsonUtils.getTips(functionParams, mStove.leftHead.status);
                    } else {
                        value = JsonUtils.getValue(functionParams, mStove.leftHead.level);
                        tips = JsonUtils.getTips(functionParams, mStove.leftHead.level);
                    }
                    holder.tv_fire_left.setText(value);
                    holder.tv_fire_desc_left.setText(tips);
                    if (mStove.leftHead.status == StoveStatus.Off || mStove.leftHead.status == StoveStatus.StandyBy) {
                        holder.tv_fire_left.setTextColor(mContext.getResources().getColor(R.color.c64));
                        Glide.with(mContext).load(mDeviceConfigurationFunctions.get(i).backgroundImg).into(holder.iv_fire_left);
                    } else {
                        holder.tv_fire_left.setTextColor(mContext.getResources().getColor(R.color.c31));
                        Glide.with(mContext).load(mDeviceConfigurationFunctions.get(i).backgroundImgH).into(holder.iv_fire_left);
                    }
                    holder.tv_stove_left_name.setText(mDeviceConfigurationFunctions.get(i).functionName);
                }
            } else if ("timedRightHeadGrear".equals(functionCode)) {
                if (!TextUtils.isEmpty(functionParams)) {
                    String value;
                    String tips;
                    if (IRokiFamily.R9B37.equals(mStove.getDt())) {
                        value = JsonUtils.getValue(functionParams, mStove.rightHead.status);
                        tips = JsonUtils.getTips(functionParams, mStove.rightHead.status);
                    } else {
                        value = JsonUtils.getValue(functionParams, mStove.rightHead.level);
                        tips = JsonUtils.getTips(functionParams, mStove.rightHead.level);
                    }

                    holder.tv_fire_right.setText(value);
                    holder.tv_fire_desc_right.setText(tips);
                    if (mStove.rightHead.status == StoveStatus.Off || mStove.rightHead.status == StoveStatus.StandyBy) {
                        holder.tv_fire_right.setTextColor(mContext.getResources().getColor(R.color.c64));
                        Glide.with(mContext).load(mDeviceConfigurationFunctions.get(i).backgroundImg).into(holder.iv_fire_right);
                    } else {
                        holder.tv_fire_right.setTextColor(mContext.getResources().getColor(R.color.c31));
                        Glide.with(mContext).load(mDeviceConfigurationFunctions.get(i).backgroundImgH).into(holder.iv_fire_right);
                    }
                    holder.tv_stove_right_name.setText(mDeviceConfigurationFunctions.get(i).functionName);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private void buttonLeft(View v) {

        short alarmId = mStove.getHeadById(LEFT_ID).alarmId;
        short ihId = mStove.getHeadById(LEFT_ID).ihId;
        if (alarmId!=255) {
            AlarmDataUtils.onStoveAlarmEvent(mStove, new StoveAlarmEvent(mStove, mStove.getHeadById(ihId), alarmId).alarm);
            return;
        }

        String tag = v.getTag(R.id.tag_stove_timing_btn_left).toString();
        Stove.StoveHead head = null;
        if ("auxiliaryLeftHeadOff".equals(tag)) {
            head = mStove.leftHead;
        } else {
            head = mStove.rightHead;
        }
        LogUtils.i("20180702", " leftHead:" + mStove.leftHead.level);
        if (mStove.leftHead.status == StoveStatus.Off || mStove.leftHead.status == StoveStatus.StandyBy) {
            ToastUtils.showShort(R.string.stove_not_open_fire);
        } else if (mStove.leftHead.time != 0 && mStove.leftHead.status == StoveStatus.Working) {
            final IRokiDialog leftDialog = RokiDialogFactory.createDialogByType(mContext, DialogUtil.DIALOG_TYPE_10);
            leftDialog.setTitleText(R.string.device_stove_off_timing);
            leftDialog.setContentText(mContext.getString(R.string.device_stove_off_desc));
            final Stove.StoveHead finalHead = head;
            leftDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    leftDialog.dismiss();
                    Preconditions.checkNotNull(finalHead);
                    setLeftStatus(finalHead);
                }
            });
            leftDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    leftDialog.dismiss();
                }
            });
            leftDialog.show();

        } else if (mStove.leftHead.time == 0 && mStove.leftHead.status == StoveStatus.Working) {
            LogUtils.i("20180703", " mLeftTimeList:" + mLeftTimeList.size());
            if (mLeftTimeList != null && mLeftTimeList.size() > 0) {
                mLeftTimeDialog = RokiDialogFactory.createDialogByType(mContext, DialogUtil.DIALOG_TYPE_03);
                List<String> timeData = HelperStoveData.getTimeData(mLeftTimeList);
                LogUtils.i("20180703", " timeData:" + timeData);
                mLeftTimeDialog.setWheelViewData(null, timeData, null, false, 0, (short) (Short.parseShort(mDefaultMinLeft) - 1), 0, null, new OnItemSelectedListenerCenter() {
                    @Override
                    public void onItemSelectedCenter(String contentCenter) {
                        Message msg = mHandler.obtainMessage();
                        msg.what = LEFT_TIMING;
                        msg.obj = contentCenter;
                        mHandler.sendMessage(msg);
                    }
                }, null);
                mLeftTimeDialog.show();
            }
        }

    }


    private void buttonRight(View v) {
        short alarmId = mStove.getHeadById(RIGHT_ID).alarmId;
        short ihId = mStove.getHeadById(RIGHT_ID).ihId;
        if (alarmId!=255) {
            AlarmDataUtils.onStoveAlarmEvent(mStove, new StoveAlarmEvent(mStove, mStove.getHeadById(ihId), alarmId).alarm);
            return;
        }
        String tag = v.getTag(R.id.tag_stove_timing_btn_right).toString();
        Stove.StoveHead head = null;
        if ("auxiliaryRightHeadOff".equals(tag)) {
            head = mStove.rightHead;
        } else {
            head = mStove.leftHead;
        }
        if (mStove.rightHead.status == StoveStatus.Off || mStove.rightHead.status == StoveStatus.StandyBy) {
            ToastUtils.showShort(R.string.stove_not_open_fire);
        } else if (mStove.rightHead.time != 0 && mStove.rightHead.status == StoveStatus.Working) {
            final IRokiDialog rightDialog = RokiDialogFactory.createDialogByType(mContext, DialogUtil.DIALOG_TYPE_10);
            rightDialog.setTitleText(R.string.device_stove_off_timing);
            rightDialog.setContentText(mContext.getString(R.string.device_stove_off_desc));
            final Stove.StoveHead finalHead = head;
            rightDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rightDialog.dismiss();
                    Preconditions.checkNotNull(finalHead);
                    setRightStatus(finalHead);
                }
            });
            rightDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rightDialog.dismiss();
                }
            });
            rightDialog.show();
        } else if (mStove.rightHead.time == 0 && mStove.rightHead.status == StoveStatus.Working) {
            if (mRightTimeList != null && mRightTimeList.size() > 0) {
                mRightTimingDialog = RokiDialogFactory.createDialogByType(mContext, DialogUtil.DIALOG_TYPE_03);
                List<String> timeData = HelperStoveData.getTimeData(mRightTimeList);
                mRightTimingDialog.setWheelViewData(null, timeData, null, false, 0, (short) (Short.parseShort(mDefaultMinRight) - 1), 0, null, new OnItemSelectedListenerCenter() {
                    @Override
                    public void onItemSelectedCenter(String contentCenter) {
                        Message msg = mHandler.obtainMessage();
                        msg.what = RIGHT_TIMING;
                        msg.obj = contentCenter;
                        mHandler.sendMessage(msg);
                    }
                }, null);
                mRightTimingDialog.show();
            }
        }
    }

    private void setLeftStatus(Stove.StoveHead head) {
        if (!checkConnection()) return;
        mStove.setStoveShutdown(LEFT_ID, (short) 0, new VoidCallback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void setRightStatus(Stove.StoveHead head) {
        if (!checkConnection()) return;
        mStove.setStoveShutdown(Stove.StoveHead.RIGHT_ID, (short) 0, new VoidCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void leftTimingData(final String leftTimeList) {

        if (leftTimeList.contains(StringConstantsUtil.STRING_MINUTES)) {
            final String removeString = RemoveManOrsymbolUtil.getRemoveString(leftTimeList);
            mLeftTimeDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLeftTimeDialog.dismiss();
                    setTimeShutDownLeft(Short.parseShort(removeString));
                }
            });
        }

    }

    private void rightTimingData(final String rightTimeList) {

        if (rightTimeList.contains(StringConstantsUtil.STRING_MINUTES)) {
            final String removeString = RemoveManOrsymbolUtil.getRemoveString(rightTimeList);
            mRightTimingDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRightTimingDialog.dismiss();
                    setTimeShutDownRight(Short.parseShort(removeString));
                }
            });
        }

    }

    void setTimeShutDownLeft(final short seconds) {
        if (mStove!=null) {

            ToolUtils.logEvent(mStove.getDt(), "定时关火:左:" + seconds, "roki_设备");
        }
        mStove.setStoveShutdown(mStove.leftHead.ihId, (short) (seconds * (short) 60), new VoidCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.showShort(R.string.device_stove_countDown_timing);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showShort(t.getMessage());
            }
        });
    }

    void setTimeShutDownRight(final short seconds) {
        if (mStove!=null) {
            ToolUtils.logEvent(mStove.getDt(), "定时关火:右:" + seconds, "roki_设备");
        }
        mStove.setStoveShutdown(mStove.rightHead.ihId, (short) (seconds * (short) 60), new VoidCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.showShort(R.string.device_stove_countDown_timing);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showShort(t.getMessage());
            }
        });
    }

    boolean checkConnection() {
        if (!mStove.isConnected()) {
            ToastUtils.showLong(R.string.device_connected);
            return false;
        } else {
            return true;
        }
    }

}

class StoveTimingViewHolder extends RecyclerView.ViewHolder {

    TextView tv_stove_left_name;
    TextView tv_fire_left;
    TextView tv_stove_right_name;
    TextView tv_fire_right;
    TextView tv_fire_desc_right;
    TextView tv_fire_desc_left;
    TextView tv_fire_time_left;
    TextView tv_fire_time_right;
    ImageView iv_fire_left;
    ImageView iv_fire_right;
    ImageView iv_stove_button_left;
    ImageView iv_stove_button_right;

    public StoveTimingViewHolder(View itemView) {
        super(itemView);
        tv_stove_left_name = itemView.findViewById(R.id.tv_stove_left_name);
        tv_fire_left = itemView.findViewById(R.id.tv_fire_left);
        tv_fire_desc_left = itemView.findViewById(R.id.tv_fire_desc_left);
        tv_stove_right_name = itemView.findViewById(R.id.tv_stove_right_name);
        tv_fire_right = itemView.findViewById(R.id.tv_fire_right);
        tv_fire_desc_right = itemView.findViewById(R.id.tv_fire_desc_right);
        tv_fire_time_left = itemView.findViewById(R.id.tv_fire_time_left);
        tv_fire_time_right = itemView.findViewById(R.id.tv_fire_time_right);
        iv_fire_left = itemView.findViewById(R.id.iv_fire_left);
        iv_stove_button_left = itemView.findViewById(R.id.iv_stove_button_left);
        iv_fire_right = itemView.findViewById(R.id.iv_fire_right);
        iv_stove_button_right = itemView.findViewById(R.id.iv_stove_button_right);

    }
}