package com.robam.roki.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.StoveStatusChangedEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.JsonUtils;
import com.robam.roki.utils.ToolUtils;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;


//import com.google.android.gms.analytics.Tracker;

/**
 * Created by 14807 on 2018/5/22.
 */

public class StoveQuicklyAdapter extends RecyclerView.Adapter<StoveQuicklyViewHolder> {

    Stove mStove;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<DeviceConfigurationFunctions> mDeviceConfigurationFunctions;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;


    @Subscribe
    public void onEvent(StoveStatusChangedEvent event) {
        if (mStove == null || !Objects.equal(mStove.getID(), event.pojo.getID()))
            return;
        short rightTime = mStove.rightHead.time;
        short leftTime = mStove.leftHead.time;
        LogUtils.i("20180523", "leftTime:" + leftTime + " rightTime:" + rightTime);
        notifyDataSetChanged();
    }

    public StoveQuicklyAdapter(Context context, Stove stove,
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
    public StoveQuicklyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_stove_quickly_page, parent, false);
        ScreenAdapterTools.getInstance().loadView(view);
        StoveQuicklyViewHolder stoveQuicklyViewHolder = new StoveQuicklyViewHolder(view);
        stoveQuicklyViewHolder.iv_stove_button_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                buttonRight(v);
            }
        });

        stoveQuicklyViewHolder.iv_stove_button_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonLeft(v);
            }
        });
        return stoveQuicklyViewHolder;
    }

    @Override
    public void onBindViewHolder(StoveQuicklyViewHolder holder, int position) {
        for (int i = 0; i < mDeviceConfigurationFunctions.size(); i++) {
            String functionParams = mDeviceConfigurationFunctions.get(i).functionParams;
            String functionCode = mDeviceConfigurationFunctions.get(i).functionCode;
            if ("auxiliaryLeftHeadOff".equals(functionCode)) {
                if (mStove.leftHead.status == StoveStatus.Off || mStove.leftHead.status == StoveStatus.StandyBy) {
                    Glide.with(mContext).load(mDeviceConfigurationFunctions.get(i).backgroundImg).into(holder.iv_stove_button_left);
                } else {
                    Glide.with(mContext).load(mDeviceConfigurationFunctions.get(i).backgroundImgH).into(holder.iv_stove_button_left);
                }

                holder.iv_stove_button_left.setTag(R.id.tag_stove_quickly_btn_left, functionCode);
            } else if ("auxiliaryRightHeadOff".equals(functionCode)) {
                if (mStove.rightHead.status == StoveStatus.Off || mStove.rightHead.status == StoveStatus.StandyBy) {
                    Glide.with(mContext).load(mDeviceConfigurationFunctions.get(i).backgroundImg).into(holder.iv_stove_button_right);
                } else {
                    Glide.with(mContext).load(mDeviceConfigurationFunctions.get(i).backgroundImgH).into(holder.iv_stove_button_right);
                }
                holder.iv_stove_button_right.setTag(R.id.tag_stove_quickly_btn_right, functionCode);
            } else if ("auxiliaryLeftHeadGear".equals(functionCode)) {
                if (functionParams != null) {
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
            } else if ("auxiliaryRightHeadGrear".equals(functionCode)) {

                if (functionParams != null) {
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

    //快速关火左
    private void buttonLeft(View v) {
        String tag = v.getTag(R.id.tag_stove_quickly_btn_left).toString();
        Stove.StoveHead head = null;
        if ("auxiliaryLeftHeadOff".equals(tag)) {
            head = mStove.leftHead;
        }
        LogUtils.i("20180702", " leftHead:" + mStove.leftHead.level);


        if (mStove.leftHead.level == 0) {
            ToastUtils.showShort(R.string.stove_not_open_fire);
        } else {

            final IRokiDialog leftDialog = RokiDialogFactory.createDialogByType(mContext, DialogUtil.DIALOG_TYPE_10);
            leftDialog.setTitleText(R.string.device_stove_off_heat);
            leftDialog.setContentText(mContext.getString(R.string.device_stove_off_gear));
            final Stove.StoveHead finalHead = head;
            leftDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mStove != null) {
                        ToolUtils.logEvent(mStove.getDt(), "快速关火:左", "roki_设备");
                    }

                    leftDialog.dismiss();
                    Preconditions.checkNotNull(finalHead);
                    setStatus(finalHead);

                }
            });
            leftDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    leftDialog.dismiss();
                }
            });
            leftDialog.show();
        }

    }

    //快速关火右
    private void buttonRight(View v) {
        String tag = v.getTag(R.id.tag_stove_quickly_btn_right).toString();
        Stove.StoveHead head = null;
        if ("auxiliaryRightHeadOff".equals(tag)) {
            head = mStove.rightHead;
        }
        if (mStove.rightHead.level == 0) {
            ToastUtils.showShort(R.string.stove_not_open_fire);
        } else {
            final IRokiDialog rightDialog = RokiDialogFactory.createDialogByType(mContext, DialogUtil.DIALOG_TYPE_10);
            rightDialog.setTitleText(R.string.device_stove_off_heat);
            rightDialog.setContentText(mContext.getString(R.string.device_stove_off_gear));
            final Stove.StoveHead finalHead = head;
            rightDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mStove != null) {
                        ToolUtils.logEvent(mStove.getDt(), "快速关火:右", "roki_设备");
                    }
                    rightDialog.dismiss();
                    Preconditions.checkNotNull(finalHead);
                    setStatus(finalHead);

                }
            });
            rightDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rightDialog.dismiss();
                }
            });
            rightDialog.show();
        }

    }

    private void setStatus(Stove.StoveHead head) {
        if (!checkConnection()) return;
        if (!checkIsPowerOn(head)) return;
        short status = (head.status == StoveStatus.Off) ? StoveStatus.StandyBy
                : StoveStatus.Off;
        mStove.setStoveStatus(false, head.ihId, status, new VoidCallback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    boolean checkIsPowerOn(Stove.StoveHead head) {
        if (head.status != StoveStatus.Off)
            return true;
        else {
            if (!mStove.isLock) {
                ToastUtils.showLong(R.string.device_stove_isLock);
            }
            return false;
        }
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

class StoveQuicklyViewHolder extends RecyclerView.ViewHolder {

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


    public StoveQuicklyViewHolder(View itemView) {
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