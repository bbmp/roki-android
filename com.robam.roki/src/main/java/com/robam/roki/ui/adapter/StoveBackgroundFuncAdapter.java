package com.robam.roki.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.EventUtils;
import com.legent.utils.StringUtils;
import com.robam.common.events.StoveStatusChangedEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.model.bean.StoveBackgroundFunParams;
import com.robam.roki.utils.DeviceJsonToBeanUtils;
import com.robam.roki.utils.StoveLevelUtils;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;


/**
 * Created by 14807 on 2018/5/18.
 */

public class StoveBackgroundFuncAdapter extends RecyclerView.Adapter<StoveBackgroundFuncViewHolder> {


    private LayoutInflater mInflater;
    private Context mContext;
    private List<DeviceConfigurationFunctions> mDeviceConfigurationFunctions;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    private Stove mStove;
    private StoveBackgroundFunParams mParams;
    private short mLeftLevel;
    private short mRightLevel;
    private boolean mLeftOn;
    private boolean mRightOn;

    public StoveBackgroundFuncAdapter(Context context, Stove stove, List<DeviceConfigurationFunctions> deviceConfigurationFunctionses
            , OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mContext = context;
        mStove = stove;
        mDeviceConfigurationFunctions = deviceConfigurationFunctionses;
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        mInflater = LayoutInflater.from(context);
        EventUtils.regist(this);

    }

    @Subscribe
    public void onEvent(StoveStatusChangedEvent event) {
        if (mStove == null || !Objects.equal(mStove.getID(), event.pojo.getID())) return;
        this.notifyDataSetChanged();
    }


    @Override
    public StoveBackgroundFuncViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_stove_backgroundfunc_page, parent, false);
        ScreenAdapterTools.getInstance().loadView(view);
        StoveBackgroundFuncViewHolder stoveBackgroundFuncViewHolder = new StoveBackgroundFuncViewHolder(mContext, view);
        //快速关火 的监听
        stoveBackgroundFuncViewHolder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRecyclerViewItemClickListener.onItemClick(v);
            }
        });
        return stoveBackgroundFuncViewHolder;
    }

    @Override
    public void onBindViewHolder(StoveBackgroundFuncViewHolder holder, int position) {
        if (mDeviceConfigurationFunctions != null && mDeviceConfigurationFunctions.size() > 0) {

            String functionParams = mDeviceConfigurationFunctions.get(position).functionParams;
            String functionCode = mDeviceConfigurationFunctions.get(position).functionCode;
            mParams = DeviceJsonToBeanUtils.JsonToObject
                    (functionParams, StoveBackgroundFunParams.class);
            if ("leftHeadStatue".equals(functionCode)) {
                if (IRokiFamily.R9B37.equals(mStove.getDt())) {//灶具9b37根据状态取值
                    short leftStatus = mStove.leftHead.status;
                    String stoveStatus = StoveLevelUtils.getStoveLevel(leftStatus, mParams);
                    holder.mTvModelName.setText(stoveStatus);
                    if (leftStatus == 0 || leftStatus == 1) {
                        Glide.with(mContext)
                                .load(mDeviceConfigurationFunctions.get(position).backgroundImg)
//                                .crossFade()
                                .into(holder.mIvModelImg);
                    } else {
                        Glide.with(mContext)
                                .load(mDeviceConfigurationFunctions.get(position)
                                        .backgroundImgH)
//                                .crossFade()
                                .into(holder.mIvModelImg);
                    }
                } else {//其它灶具类型根据档位取值
                    mLeftLevel = mStove.leftHead.level;
                    String stoveLevel = StoveLevelUtils.getStoveLevel(mLeftLevel, mParams);
                    holder.mTvModelName.setText(stoveLevel);
                    if (mStove.leftHead.workTime != 0) {
                        mLeftOn = true;
                        holder.mTvModelTime.setVisibility(View.VISIBLE);
                        if (mLeftLevel == 0) {
                            holder.mTvModelTime.setText("--");
                        } else {
                            holder.mTvModelTime.setText(StringUtils.secondToTime(mStove.leftHead.workTime));
                        }
                    } else {
                        mLeftOn = false;
                        if (!mRightOn) {
                            holder.mTvModelTime.setVisibility(View.GONE);
                        } else {
                            holder.mTvModelTime.setVisibility(View.VISIBLE);
                        }
                        holder.mTvModelTime.setText("--");
                    }
                    if (mLeftLevel == 0) {
                        Glide.with(mContext)
                                .load(mDeviceConfigurationFunctions.get(position)
                                        .backgroundImg)
//                                .crossFade()
                                .into(holder.mIvModelImg);
                    } else {
                        Glide.with(mContext)
                                .load(mDeviceConfigurationFunctions.get(position)
                                        .backgroundImgH)
//                                .crossFade()
                                .into(holder.mIvModelImg);
                    }
                }

            } else if ("rightHeadStatue".equals(functionCode)) {

                if (IRokiFamily.R9B37.equals(mStove.getDt())) {
                    short rightStatus = mStove.rightHead.status;
                    String stoveStatus = StoveLevelUtils.getStoveLevel(rightStatus, mParams);
                    holder.mTvModelName.setText(stoveStatus);
                    if (rightStatus == 0 || rightStatus == 1) {
                        Glide.with(mContext)
                                .load(mDeviceConfigurationFunctions.get(position)
                                        .backgroundImg)
//                                .crossFade()
                                .into(holder.mIvModelImg);
                    } else {
                        Glide.with(mContext)
                                .load(mDeviceConfigurationFunctions.get(position)
                                        .backgroundImgH)
//                                .crossFade()
                                .into(holder.mIvModelImg);
                    }
                } else {
                    mRightLevel = mStove.rightHead.level;
                    String stoveLevel = StoveLevelUtils.getStoveLevel(mRightLevel, mParams);
                    holder.mTvModelName.setText(stoveLevel);
                    if (mStove.rightHead.workTime != 0) {
                        mRightOn = true;
                        holder.mTvModelTime.setVisibility(View.VISIBLE);
                        if (mRightLevel == 0) {
                            holder.mTvModelTime.setText("--");
                        } else {
                            holder.mTvModelTime.setText(StringUtils.secondToTime(mStove.rightHead.workTime));
                        }
                    } else {
                        mRightOn = false;
                        if (!mLeftOn) {
                            holder.mTvModelTime.setVisibility(View.GONE);
                        } else {
                            holder.mTvModelTime.setVisibility(View.VISIBLE);
                        }
                        holder.mTvModelTime.setText("--");
                    }
                    if (mRightLevel == 0) {
                        Glide.with(mContext)
                                .load(mDeviceConfigurationFunctions.get(position)
                                        .backgroundImg)
//                                .crossFade()
                                .into(holder.mIvModelImg);
                    } else {
                        Glide.with(mContext)
                                .load(mDeviceConfigurationFunctions.get(position)
                                        .backgroundImgH)
//                                .crossFade()
                                .into(holder.mIvModelImg);
                    }
                }

            }

        }
    }

    @Override
    public int getItemCount() {
        return mDeviceConfigurationFunctions.size();
    }


}

class StoveBackgroundFuncViewHolder extends RecyclerView.ViewHolder {

    Animation mAnimation;
    ImageView mIvModelImg;
    TextView mTvModelName;
    TextView mTvModelTime;
    LinearLayout mItemView;
    Context mContext;

    public StoveBackgroundFuncViewHolder(Context context, View itemView) {
        super(itemView);

        mContext = context;
        mItemView = itemView.findViewById(R.id.itemView);
        mIvModelImg = itemView.findViewById(R.id.iv_model_img);
        mTvModelName = itemView.findViewById(R.id.tv_model_name);
        mTvModelTime = itemView.findViewById(R.id.tv_model_time);
        mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.device_rika_clockwise_rotate);

    }

    public void startAnimation() {

        mAnimation = null;
        if (mAnimation == null) {
            mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.device_rika_clockwise_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            mAnimation.setInterpolator(lin);
            mIvModelImg.startAnimation(mAnimation);
        }
    }

    public void stopAnimation() {
        if (mAnimation != null) {
            mAnimation.cancel();
            mIvModelImg.clearAnimation();
            mAnimation = null;
        }
    }

}
