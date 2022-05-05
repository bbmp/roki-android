package com.robam.roki.ui.adapter;

import android.content.Context;
import android.os.Bundle;
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
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.robam.common.events.RikaFanCleanNoticEvent;
import com.robam.common.events.RikaStatusChangedEvent;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.io.Serializable;
import java.util.List;


/**
 * Created by 14807 on 2018/4/8.
 */

public class MainFuncAdapter extends BaseAdapter {


    AbsRika mRika;
    String mTitle;
    private LayoutInflater mInflater;
    private Context mContext;
    private boolean mClean = false;
    private List<DeviceConfigurationFunctions> mDeviceConfigurationFunctions;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;


    public MainFuncAdapter(Context context, AbsRika rika, String title, List<DeviceConfigurationFunctions> deviceConfigurationFunctionses
            , OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mContext = context;
        mRika = rika;
        mTitle = title;
        mDeviceConfigurationFunctions = deviceConfigurationFunctionses;
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        mInflater = LayoutInflater.from(context);
        EventUtils.regist(this);

    }

    @Subscribe
    public void onEvent(RikaStatusChangedEvent event) {
        if (mRika == null || !Objects.equal(mRika.getID(), event.pojo.getID())) return;
        mRika = event.pojo;
        this.notifyDataSetChanged();
    }


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
    public View getView(int position, View convertView, ViewGroup parent) {
        // 声明内部类
        MainFuncViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new MainFuncViewHolder();
            // 给xml布局文件创建java对象
            convertView = mInflater.inflate(R.layout.item_fan_backgroundfunc_page, null);
            ScreenAdapterTools.getInstance().loadView(convertView);
            // 指向布局文件内部组件
            viewHolder.mItemView = convertView.findViewById(R.id.itemView);
            viewHolder.mIvModelImg = convertView.findViewById(R.id.iv_model_img);
            viewHolder.mTvModelName = convertView.findViewById(R.id.tv_model_name);

            // 增加额外变量
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MainFuncViewHolder) convertView.getTag();
        }
        // 获取数据显示在各组件
        viewHolder.mTvModelName.setText(mDeviceConfigurationFunctions.get(position).functionName);
        Glide.with(mContext)
                .load(mDeviceConfigurationFunctions.get(position).backgroundImg)
//                .crossFade()
                .into(viewHolder.mIvModelImg);
        viewHolder.mItemView.setTag(mDeviceConfigurationFunctions.get(position).functionCode);
        // 详情按钮，添加点击事件
        if (null != mDeviceConfigurationFunctions && mDeviceConfigurationFunctions.size() > 0) {

            // 获取数据显示在各组件
            viewHolder.mTvModelName.setText(mDeviceConfigurationFunctions.get(position).functionName);
            Glide.with(mContext)
                    .load(mDeviceConfigurationFunctions.get(position).backgroundImg)
//                    .crossFade()
                    .into(viewHolder.mIvModelImg);
            if ("oilNetworkDetection".equals(mDeviceConfigurationFunctions.get(position).functionCode)) {
                if (mRika.cleaningUseTime > 3600) {//3600M == 60H
                    viewHolder.mItemView.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.mItemView.setVisibility(View.GONE);
                }
            } else if ("clearOilCup".equals(mDeviceConfigurationFunctions.get(position).functionCode)) {
                String dp = mRika.getDp();
                if ("RIKAZ".equals(dp)) {
                    if (mRika.steamRemoveOilySoiled == 1) {
                        viewHolder.mItemView.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.mItemView.setVisibility(View.GONE);
                    }
                } else if ("RIKAX".equals(dp)) {
                    if (mRika.sterilRemoveOilySoiled == 1) {
                        viewHolder.mItemView.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.mItemView.setVisibility(View.GONE);
                    }
                } else if ("RIKAY".equals(dp)) {
                    if (mRika.steamOvenCleanOil == 1) {
                        viewHolder.mItemView.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.mItemView.setVisibility(View.GONE);
                    }
                }
            }
            viewHolder.mItemView.setTag(mDeviceConfigurationFunctions.get(position).functionCode);
        }


        // 详情按钮，添加点击事件
        final MainFuncViewHolder finalViewHolder = viewHolder;
        viewHolder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnRecyclerViewItemClickListener.onItemClick(view);
            }
        });
        return convertView;
    }

    class MainFuncViewHolder {

        ImageView mIvModelImg;
        TextView mTvModelName;
        LinearLayout mItemView;

    }
}



