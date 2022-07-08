package com.robam.roki.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.graphic.ImageUtils;
import com.robam.common.events.FanCleanNoticEvent;
import com.robam.common.events.RikaStatusChangedEvent;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaModel;
import com.robam.common.pojos.device.rika.RikaStatus;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.ui.extension.GlideApp;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import static android.R.attr.id;
import static android.R.attr.value;
import static com.legent.ContextIniter.cx;


/**
 * Created by 14807 on 2018/4/8.
 */

public class BackgroundFuncAdapter extends RecyclerView.Adapter<BackgroundFuncViewHolder> {


    private LayoutInflater mInflater;
    private Context mContext;
    private List<DeviceConfigurationFunctions> mDeviceConfigurationFunctions;

    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    private short mRikaFanPower;
    private short mStoveHeadLeftPower;
    private short mStoveHeadRightPower;
    private short mSteamRunModel;
    private short mSteamOvenRunModel;
    private short mSterilWorkStatus;
    AbsRika mRika;
    DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();


    public BackgroundFuncAdapter(Context context, AbsRika rika, List<DeviceConfigurationFunctions> deviceConfigurationFunctionses
            , OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mContext = context;
        mRika = rika;
        mDeviceConfigurationFunctions = deviceConfigurationFunctionses;
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        mInflater = LayoutInflater.from(context);
        EventUtils.regist(this);

    }

    @Subscribe
    public void onEvent(RikaStatusChangedEvent event) {
        if (mRika == null || !Objects.equal(mRika.getID(), event.pojo.getID())) {
            return;
        }
        String id = event.pojo.getID();
        LogUtils.i("20180412", " ID:" + id);
        mRikaFanPower = event.pojo.rikaFanPower;
        mStoveHeadLeftPower = event.pojo.stoveHeadLeftPower;
        mStoveHeadRightPower = event.pojo.stoveHeadRightPower;
        mSteamRunModel = event.pojo.steamRunModel;
        mSteamOvenRunModel = event.pojo.steamOvenRunModel;
        mSterilWorkStatus = event.pojo.sterilWorkStatus;
        mRika = event.pojo;
        LogUtils.i("20180412", " mRikaFanPower:" + mRikaFanPower + " mStoveHeadLeftPower:" + mStoveHeadLeftPower +
                " mStoveHeadRightPower:" + mStoveHeadRightPower + " mSteamRunModel:" + mSteamRunModel);
        this.notifyDataSetChanged();
    }

    @Override
    public BackgroundFuncViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_backgroundfunc_page, parent, false);
        ScreenAdapterTools.getInstance().loadView(view);
        BackgroundFuncViewHolder backgroundFuncViewHolder = new BackgroundFuncViewHolder(view);
        backgroundFuncViewHolder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRecyclerViewItemClickListener.onItemClick(v);
            }
        });
        return backgroundFuncViewHolder;
    }

    @Override
    public void onBindViewHolder(BackgroundFuncViewHolder holder, int position) {
        try {
            if (mDeviceConfigurationFunctions != null && mDeviceConfigurationFunctions.size() > 0) {
//                ImageUtils.displayImage(mDeviceConfigurationFunctions.get(position).backgroundImg, holder.mIvModelImg);
                String functionParams = mDeviceConfigurationFunctions.get(position).functionParams;
                String functionCode = mDeviceConfigurationFunctions.get(position).functionCode;
                if ("fanGear".equals(functionCode)) {
                    if (functionParams != null) {
                        JSONObject jsonObject = new JSONObject(functionParams);
                        JSONObject param = (JSONObject) jsonObject.get("param");
                        JSONObject paramValue = (JSONObject) param.get(String.valueOf(mRikaFanPower));
                        String value = (String) paramValue.get("value");
                        holder.mTvModelName.setText(value);
                        if (mRikaFanPower == 0) {
                            Glide.with(cx)
                                    .load(mDeviceConfigurationFunctions.get(position).backgroundImg)
//                                    .crossFade()
                                    .into(holder.mIvModelImg);
                        } else {
                            Glide.with(cx)
                                    .asGif()
                                    .load(mDeviceConfigurationFunctions.get(position).backgroundImgH)
                                    .into(holder.mIvModelImg);
                        }
                    }
                } else if ("leftHeadGear".equals(functionCode)) {
                    JSONObject jsonObject = new JSONObject(functionParams);
                    JSONObject param = (JSONObject) jsonObject.get("param");
                    JSONObject paramValue = (JSONObject) param.get(String.valueOf(mStoveHeadLeftPower));
                    String value = (String) paramValue.get("value");
                    LogUtils.i("20180412", " value:" + value);
                    holder.mTvModelName.setText(value);
                    if (mStoveHeadLeftPower == 0){
                        Glide.with(cx).load(mDeviceConfigurationFunctions.get(position)
                                .backgroundImg)
                                .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
//                                .crossFade()
                                .into(holder.mIvModelImg);
                    }else {
                        GlideApp.with(cx)
                                .load(mDeviceConfigurationFunctions.get(position).backgroundImgH)
//                                .asGif()

                                .into(holder.mIvModelImg);
                    }
                } else if ("rightHeadGear".equals(functionCode)) {
                    JSONObject jsonObject = new JSONObject(functionParams);
                    JSONObject param = (JSONObject) jsonObject.get("param");
                    JSONObject paramValue = (JSONObject) param.get(String.valueOf(mStoveHeadRightPower));
                    String value = (String) paramValue.get("value");
                    holder.mTvModelName.setText(value);
                    if (mStoveHeadRightPower == 0){
                        Glide.with(cx).load(mDeviceConfigurationFunctions.get(position)
                                .backgroundImg)
//                                .crossFade()
                                .into(holder.mIvModelImg);
                    }else {
                        Glide.with(cx)
                                .asGif()
                                .load(mDeviceConfigurationFunctions.get(position).backgroundImgH)
                                .into(holder.mIvModelImg);
                    }
                } else if ("steamedModel".equals(functionCode)) {
                    JSONObject jsonObject = new JSONObject(functionParams);
                    JSONObject param = (JSONObject) jsonObject.get("param");
                    if (mSteamRunModel == -1){
                        mSteamRunModel = 0;
                    }
                    JSONObject paramValue = (JSONObject) param.get(String.valueOf(mSteamRunModel));
                    String value = (String) paramValue.get("value");
                    holder.mTvModelName.setText(value);
                    if (mSteamRunModel == RikaModel.Steame.NO_MOEL || mRika.steamWorkStatus == RikaStatus.STEAM_ON) {
                        Glide.with(cx)
                                .load(mDeviceConfigurationFunctions.get(position)
                                        .backgroundImg)
//                                .crossFade()
                                .into(holder.mIvModelImg);
                    }else {
                        Glide.with(cx)
                                .asGif()
                                .load(mDeviceConfigurationFunctions.get(position).backgroundImgH)
                                .into(holder.mIvModelImg);
                    }
                } else if ("zkyStatus".equals(functionCode)) {
                    JSONObject jsonObject = new JSONObject(functionParams);
                    JSONObject param = (JSONObject) jsonObject.get("param");
                    if (mSteamOvenRunModel == 255) {
                        mSteamOvenRunModel = 0;
                    }
                    JSONObject paramValue = (JSONObject) param.get(String.valueOf(mSteamOvenRunModel));
                    String value = (String) paramValue.get("value");
                    holder.mTvModelName.setText(value);
                    if (mSteamOvenRunModel == 0 || mSteamOvenRunModel == 255 || mRika.steamOvenWorkStatus == RikaStatus.STEAMOVEN_ON){
                        Glide.with(cx).load(R.mipmap.bg_rika_device)
                                .transition(DrawableTransitionOptions.with(new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()))
                                .into(holder.mIvModelImg);
                    }else {
                        Glide.with(cx).asGif().load(R.mipmap.bg_rika_device_gif)
                                .into(holder.mIvModelImg);
                    }
                } else if ("disinfectionModel".equals(functionCode)){
                    JSONObject jsonObject = new JSONObject(functionParams);
                    JSONObject param = (JSONObject) jsonObject.get("param");
                    if (mSterilWorkStatus == -2){
                        mSterilWorkStatus = 0;
                    }
                    JSONObject paramValue = (JSONObject) param.get(String.valueOf(mSterilWorkStatus));
                    String value = (String) paramValue.get("value");
                    holder.mTvModelName.setText(value);
                    Glide.with(cx).load(mDeviceConfigurationFunctions.get(position)
                            .backgroundImg)
//                            .crossFade()
                            .into(holder.mIvModelImg);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return mDeviceConfigurationFunctions.size();
    }
}

class BackgroundFuncViewHolder extends RecyclerView.ViewHolder {

    ImageView mIvModelImg;
    TextView mTvModelName;
    LinearLayout mItemView;

    public BackgroundFuncViewHolder(View itemView) {
        super(itemView);
        mItemView = (LinearLayout) itemView.findViewById(R.id.itemView);
        mIvModelImg = (ImageView) itemView.findViewById(R.id.iv_model_img);
        mTvModelName = (TextView) itemView.findViewById(R.id.tv_model_name);

    }

}