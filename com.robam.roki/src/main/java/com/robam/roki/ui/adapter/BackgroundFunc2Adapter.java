package com.robam.roki.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.robam.common.events.IntegStoveStatusChangedEvent;
import com.robam.common.events.IntegratedStoveStatusChangedEvent;
import com.robam.common.pojos.device.integratedStove.AbsIntegratedStove;
import com.robam.common.pojos.device.integratedStove.IntegStoveModel;
import com.robam.common.pojos.device.integratedStove.IntegStoveStatus;
import com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum;
import com.robam.common.pojos.device.rika.RikaModel;
import com.robam.common.pojos.device.rika.RikaStatus;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.ui.extension.GlideApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;



/**
 * Created by 14807 on 2018/4/8.
 */

public class BackgroundFunc2Adapter extends RecyclerView.Adapter<BackgroundFunc2ViewHolder> {


    private LayoutInflater mInflater;
    private Context mContext;
    private List<DeviceConfigurationFunctions> mDeviceConfigurationFunctions;

    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    AbsIntegratedStove mIntegratedStove;
    DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();


    public BackgroundFunc2Adapter(Context context, AbsIntegratedStove absIntegratedStove, List<DeviceConfigurationFunctions> deviceConfigurationFunctionses
            , OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mContext = context;
        mIntegratedStove = absIntegratedStove;
        mDeviceConfigurationFunctions = deviceConfigurationFunctionses;
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        mInflater = LayoutInflater.from(context);

    }

//    @Subscribe
//    public void onEvent(IntegStoveStatusChangedEvent event) {
//        if (mIntegratedStove == null || !Objects.equal(mIntegratedStove.getID(), event.pojo.getID())) {
//            return;
//        }
//        String id = event.pojo.getID();
//        LogUtils.i("20180412", " ID:" + id);
//        this.mIntegratedStove = event.pojo ;
//        this.notifyDataSetChanged();
//    }

    public void replaceIntegratedStove(AbsIntegratedStove mIntegratedStove) {
        this.mIntegratedStove = mIntegratedStove;
        notifyDataSetChanged();
    }

    @Override
    public BackgroundFunc2ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_backgroundfunc_page, parent, false);
        BackgroundFunc2ViewHolder backgroundFuncViewHolder = new BackgroundFunc2ViewHolder(view);
        backgroundFuncViewHolder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRecyclerViewItemClickListener.onItemClick(v);
            }
        });
        return backgroundFuncViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(BackgroundFunc2ViewHolder holder, int position) {
        try {
            if (mDeviceConfigurationFunctions != null && mDeviceConfigurationFunctions.size() > 0) {
//                ImageUtils.displayImage(mDeviceConfigurationFunctions.get(position).backgroundImg, holder.mIvModelImg);
                String functionParams = mDeviceConfigurationFunctions.get(position).functionParams;
                String functionCode = mDeviceConfigurationFunctions.get(position).functionCode;
                if ("fanGear".equals(functionCode)) {
                    if (functionParams != null) {
                        JSONObject jsonObject = new JSONObject(functionParams);
                        JSONObject param = (JSONObject) jsonObject.get("param");
                        JSONObject paramValue = (JSONObject) param.get(String.valueOf(mIntegratedStove.fan_gear));
                        String value = (String) paramValue.get("value");
                        holder.mTvModelName.setText(value);
                        if (mIntegratedStove.fan_gear == 0) {
                            Glide.with(mContext)
                                    .load(mDeviceConfigurationFunctions.get(position).backgroundImg)
                                    .into(holder.mIvModelImg);
                        } else {
                            Glide.with(mContext)
                                    .asGif()
                                    .load(R.drawable.ic_fan_gear_ac)
//                                    .placeholder(R.drawable.ic_fan_fear)
//                                    .error(R.drawable.ic_fan_fear)
                                    .into(holder.mIvModelImg);
                        }
                    }
                } else if ("leftHeadGear".equals(functionCode)) {
                    JSONObject jsonObject = new JSONObject(functionParams);
                    JSONObject param = (JSONObject) jsonObject.get("param");
                    JSONObject paramValue = (JSONObject) param.get(String.valueOf(mIntegratedStove.stove_powerState));
                    String value = (String) paramValue.get("value");
                    LogUtils.i("20180412", " value:" + value);
                    holder.mTvModelName.setText(value);
                    if (mIntegratedStove.stove_powerState == IntegStoveStatus.powerState_off) {
                        Glide.with(mContext).load(mDeviceConfigurationFunctions.get(position)
                                .backgroundImg)
                                .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
//                                .crossFade()
                                .into(holder.mIvModelImg);
                    } else {
                        GlideApp.with(mContext)
                                .load(mDeviceConfigurationFunctions.get(position).backgroundImgH)
//                                .asGif()

                                .into(holder.mIvModelImg);
                    }
                } else if ("rightHeadGear".equals(functionCode)) {
                    JSONObject jsonObject = new JSONObject(functionParams);
                    JSONObject param = (JSONObject) jsonObject.get("param");
                    JSONObject paramValue = (JSONObject) param.get(String.valueOf(mIntegratedStove.stove_powerState2));
                    String value = (String) paramValue.get("value");
                    holder.mTvModelName.setText(value);
                    if (mIntegratedStove.stove_powerState2 == IntegStoveStatus.powerState_off) {
                        Glide.with(mContext).load(mDeviceConfigurationFunctions.get(position)
                                .backgroundImg)
//                                .crossFade()
                                .into(holder.mIvModelImg);
                    } else {
                        Glide.with(mContext)
                                .asGif()
                                .load(mDeviceConfigurationFunctions.get(position).backgroundImgH)
                                .into(holder.mIvModelImg);
                    }
                } else if ("steamedModel".equals(functionCode)) {

                } else if ("zkyStatus".equals(functionCode)) {

                    if (mIntegratedStove.workState != IntegStoveStatus.workState_free) {
                        if (mIntegratedStove.recipeId == 0) {
                            JSONObject jsonObject = new JSONObject(functionParams);
                            JSONObject param = (JSONObject) jsonObject.get("param");

                            JSONObject paramValue = (JSONObject) param.get(String.valueOf(mIntegratedStove.mode));
                            String value = (String) paramValue.get("value");
                            holder.mTvModelName.setText(value);

                            if (SteamOvenModeEnum.match(mIntegratedStove.mode) == SteamOvenModeEnum.NO_MOEL) {
                                Glide.with(mContext).load(R.mipmap.bg_rika_device)
                                        .transition(DrawableTransitionOptions.with(new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()))
                                        .into(holder.mIvModelImg);
                            } else {
                                Glide.with(mContext).asGif().load(R.mipmap.bg_rika_device_gif)
                                        .into(holder.mIvModelImg);
                            }
                        } else {
                            Glide.with(mContext).asGif().load(R.mipmap.bg_rika_device_gif)
                                    .into(holder.mIvModelImg);
                            holder.mTvModelName.setText("P" + mIntegratedStove.recipeId);
                        }
                    }else {
                        Glide.with(mContext).load(R.mipmap.bg_rika_device)
                                .transition(DrawableTransitionOptions.with(new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()))
                                .into(holder.mIvModelImg);
                        holder.mTvModelName.setText("无模式");
                    }
                } else if ("disinfectionModel".equals(functionCode)) {

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return Math.min(mDeviceConfigurationFunctions.size(), 4);
    }
}

class BackgroundFunc2ViewHolder extends RecyclerView.ViewHolder {

    ImageView mIvModelImg;
    TextView mTvModelName;
    LinearLayout mItemView;

    public BackgroundFunc2ViewHolder(View itemView) {
        super(itemView);
        mItemView = (LinearLayout) itemView.findViewById(R.id.itemView);
        mIvModelImg = (ImageView) itemView.findViewById(R.id.iv_model_img);
        mTvModelName = (TextView) itemView.findViewById(R.id.tv_model_name);

    }

}