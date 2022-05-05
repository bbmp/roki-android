package com.robam.roki.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.EventUtils;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.robam.common.events.FanStatusChangedEvent;
import com.robam.common.events.KitchenCleanGearChangeEvent;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.model.bean.FanMainParams;

import java.util.List;


/**
 * Created by 14807 on 2018/4/8.
 */

public class FanMainFuncAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<DeviceConfigurationFunctions> mDeviceConfigurationFunctions;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    AbsFan mAbsFan;
    private short mLevel;


    public FanMainFuncAdapter(Context context, List<DeviceConfigurationFunctions> deviceConfigurationFunctionses
            , AbsFan fan, OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mContext = context;
        mDeviceConfigurationFunctions = deviceConfigurationFunctionses;
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        mAbsFan = fan;
        mInflater = LayoutInflater.from(context);
        EventUtils.regist(this);

    }


    @Subscribe
    public void onEvent(FanStatusChangedEvent event) {
        if (mAbsFan == null || !Objects.equal(mAbsFan.getID(), event.pojo.getID())) return;
        LogUtils.i("20180529", " event:" + event.pojo);
        mLevel = event.pojo.level;
        this.notifyDataSetChanged();
    }

//    @Subscribe
//    public void onEvent(DeviceConnectionChangedEvent event) {
//        if (mAbsFan == null || !Objects.equal(mAbsFan.getID(), event.device.getID()))
//            return;
//        mLevel = 0;
//        notifyDataSetChanged();
//    }

    @Override
    public int getCount() {
        return mDeviceConfigurationFunctions.size();
    }

    @Override
    public Object getItem(int position) {
        return mDeviceConfigurationFunctions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LogUtils.i("20180530", " getView:");
        // 声明内部类
        FanMainFuncViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new FanMainFuncViewHolder();
            // 给xml布局文件创建java对象
            convertView = mInflater.inflate(R.layout.item_fan_mainfunc_page, null);
            // 指向布局文件内部组件
            viewHolder.mItemView = convertView.findViewById(R.id.itemView);
            viewHolder.mIvModelImg = convertView.findViewById(R.id.iv_model_img);
            viewHolder.mTvModelName = convertView.findViewById(R.id.tv_model_name);
            // 增加额外变量
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FanMainFuncViewHolder) convertView.getTag();
        }
        // 获取数据显示在各组件
        if (mDeviceConfigurationFunctions != null && mDeviceConfigurationFunctions.size() > 0) {
            viewHolder.mTvModelName.setText(mDeviceConfigurationFunctions.get(position).functionName);
            if ("fry".equals(mDeviceConfigurationFunctions.get(position).functionCode)) {
                if (mLevel > AbsFan.PowerLevel_3 && mLevel <= AbsFan.PowerLevel_6) {
                    Glide.with(mContext)
                            .load(mDeviceConfigurationFunctions.get(position).backgroundImgH)
//                            .crossFade()
                            .into(viewHolder.mIvModelImg);
                } else {
                    Glide.with(mContext)
                            .load(mDeviceConfigurationFunctions.get(position).backgroundImg)
//                            .crossFade()
                            .into(viewHolder.mIvModelImg);
                }
            } else if ("decoct".equals(mDeviceConfigurationFunctions.get(position).functionCode)) {
                if (mLevel == AbsFan.PowerLevel_3) {
                    Glide.with(mContext)
                            .load(mDeviceConfigurationFunctions.get(position).backgroundImgH)
//                            .crossFade()
                            .into(viewHolder.mIvModelImg);
                } else {
                    Glide.with(mContext)
                            .load(mDeviceConfigurationFunctions.get(position).backgroundImg)
//                            .crossFade()
                            .into(viewHolder.mIvModelImg);
                }
            } else if ("stew".equals(mDeviceConfigurationFunctions.get(position).functionCode)) {
                if (mLevel == AbsFan.PowerLevel_1 || mLevel == AbsFan.PowerLevel_2) {
                    Glide.with(mContext)
                            .load(mDeviceConfigurationFunctions.get(position).backgroundImgH)
//                            .crossFade()
                            .into(viewHolder.mIvModelImg);
                } else {
                    Glide.with(mContext)
                            .load(mDeviceConfigurationFunctions.get(position).backgroundImg)
//                            .crossFade()
                            .into(viewHolder.mIvModelImg);
                }
            }
            viewHolder.mItemView.setTag(mDeviceConfigurationFunctions.get(position));
            // 详情按钮，添加点击事件
            viewHolder.mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnRecyclerViewItemClickListener.onItemClick(view);
                    ItemEvent(view);
                }
            });

        }
        return convertView;
    }


    private void ItemEvent(View v) {
        DeviceConfigurationFunctions tag = (DeviceConfigurationFunctions) v.getTag();
//        FanMainParams fanMainParams = DeviceJsonToBeanUtils.JsonToObject(tag.functionParams, FanMainParams.class);
        try {
            FanMainParams  fanMainParams = JsonUtils.json2Pojo(tag.functionParams, FanMainParams.class);

            switch (tag.functionCode) {
                case "fry":
                    String fry = fanMainParams.getParam().getPower().getValue();
                    setLevel(Short.parseShort(fry));
                    LogUtils.i("20180606", " fry:" + fry);
                    EventUtils.postEvent(new KitchenCleanGearChangeEvent());
                    break;
                case "decoct":
                    String decoct = fanMainParams.getParam().getPower().getValue();
                    setLevel(Short.parseShort(decoct));
                    LogUtils.i("20180606", " decoct:" + decoct);
                    EventUtils.postEvent(new KitchenCleanGearChangeEvent());
                    break;

                case "stew":
                    String stew = fanMainParams.getParam().getPower().getValue();
                    setLevel(Short.parseShort(stew));
                    LogUtils.i("20180606", " stew:" + stew);
                    EventUtils.postEvent(new KitchenCleanGearChangeEvent());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 判断是否断网
     */
    boolean isConnected() {
        return mAbsFan.isConnected();
    }

    /**
     * 设置烟灶档位
     */
    void setLevel(final short level) {

        if (!mAbsFan.isConnected()) return;
        if (mAbsFan.level == level) {
            setLevel_0();
        } else {
            mAbsFan.setFanLevel(level, new VoidCallback() {
                @Override
                public void onSuccess() {
                    mLevel = level;
                    notifyDataSetChanged();
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        }

    }

    /**
     * 设置烟灶档位关闭
     */
    void setLevel_0() {
        mAbsFan.setFanStatus(AbsFan.PowerLevel_0, new VoidCallback() {
            @Override
            public void onSuccess() {
                LogUtils.i("20180530", "-----------烟机关闭-------------");
                mLevel = 0;
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}

class FanMainFuncViewHolder {

    ImageView mIvModelImg;
    TextView mTvModelName;
    LinearLayout mItemView;

}


