package com.robam.roki.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.robam.common.events.FanCleanNoticEvent;
import com.robam.common.events.FanOilCupCleanEvent;
import com.robam.common.events.FanStatusChangedEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;

import java.io.Serializable;
import java.util.List;

import static com.legent.ContextIniter.cx;


/**
 * Created by 14807 on 2018/4/8.
 */

public class FanBackgroundFuncAdapter extends BaseAdapter {

    private AbsFan mAbsFan;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<DeviceConfigurationFunctions> mDeviceConfigurationFunctions;
    private List<DeviceConfigurationFunctions> mDeviceTotalConfigurationFunctions;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    private boolean mClean = false;
    private boolean mOilCup = false;
    public static boolean isNewProtocol;
    public static boolean timeRemindingIsSend;


    public FanBackgroundFuncAdapter(Context context, List<DeviceConfigurationFunctions> deviceConfigurationFunctionses
            , AbsFan fan, OnRecyclerViewItemClickListener onRecyclerViewItemClickListener, List<DeviceConfigurationFunctions> deviceConfigurationFunctions) {
        this.mContext = context;
        this.mDeviceConfigurationFunctions = deviceConfigurationFunctionses;
        this.mDeviceTotalConfigurationFunctions = deviceConfigurationFunctions;
        this.mAbsFan = fan;
        this.mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        this.mInflater = LayoutInflater.from(context);
        EventUtils.regist(this);

    }

    @Subscribe
    public void onEvent(FanStatusChangedEvent event) {
        if (mAbsFan == null || !Objects.equal(mAbsFan.getID(), event.pojo.getID()))
            return;
        mClean = mAbsFan.clean;
        mOilCup = mAbsFan.oilCup != 0;
        this.notifyDataSetChanged();

    }

    @Subscribe
    public void onEvent(FanCleanNoticEvent event) {
        this.notifyDataSetChanged();
    }

    @Subscribe
    public void onEvent(FanOilCupCleanEvent event) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // 声明内部类
        FanBackgroundFuncViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new FanBackgroundFuncViewHolder();
            // 给xml布局文件创建java对象
            convertView = mInflater.inflate(R.layout.item_fan_backgroundfunc_page, null);
            // 指向布局文件内部组件
            viewHolder.mItemView = convertView.findViewById(R.id.itemView);
            viewHolder.mIvModelImg = convertView.findViewById(R.id.iv_model_img);
            viewHolder.mTvModelName = convertView.findViewById(R.id.tv_model_name);
            // 增加额外变量
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FanBackgroundFuncViewHolder) convertView.getTag();
        }
        // 获取数据显示在各组件
        if ("alert".equals(mDeviceConfigurationFunctions.get(position).functionType)) {
            viewHolder.mTvModelName.setText(mDeviceConfigurationFunctions.get(position).functionName);
            Glide.with(cx)
                    .load(mDeviceConfigurationFunctions.get(position).backgroundImg)
//                    .crossFade()
                    .into(viewHolder.mIvModelImg);
            viewHolder.mItemView.setTag(mDeviceConfigurationFunctions.get(position).functionCode);
        } else if ("hide".equals(mDeviceConfigurationFunctions.get(position).functionType)) {
            short hide = Short.parseShort(mDeviceConfigurationFunctions.get(position).functionParams);
            isNewProtocol = hide != 0;

        }else if ("displayOnly".equals(mDeviceConfigurationFunctions.get(position).functionType)) {
            viewHolder.mTvModelName.setText(mDeviceConfigurationFunctions.get(position).functionName);
            Glide.with(cx)
                    .load(mDeviceConfigurationFunctions.get(position).backgroundImg)
//                    .crossFade()
                    .into(viewHolder.mIvModelImg);
            viewHolder.mItemView.setTag(mDeviceConfigurationFunctions.get(position).functionCode);
        }
        //是否发送168计时提醒指令
        if ("timeRemindingIsSend".equals(mDeviceConfigurationFunctions.get(position).functionCode) &&
                "1".equals(mDeviceConfigurationFunctions.get(position).functionParams)) {
            timeRemindingIsSend = true;
        } else {
            timeRemindingIsSend = false;
        }



        viewHolder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("oilNetworkState".equals(mDeviceConfigurationFunctions.get(position).functionCode)) {

                    String title = mDeviceConfigurationFunctions.get(position).functionName;
                    List<DeviceConfigurationFunctions> deviceConfigurationFunctions =
                            mDeviceConfigurationFunctions.get(position)
                                    .subView
                                    .subViewModelMap
                                    .subViewModelMapSubView
                                    .deviceConfigurationFunctions;
                    mOnRecyclerViewItemClickListener.onItemClick(view);
                    if (deviceConfigurationFunctions == null)
                        return;
                    String dismantling = null;
                    String url = null;
                    for (int i = 0; i < deviceConfigurationFunctions.size(); i++) {
                        if ("oilNetDismant".equals(deviceConfigurationFunctions.get(i).functionCode)) {
                            dismantling = deviceConfigurationFunctions.get(i).functionName;
                            url = deviceConfigurationFunctions.get(i).subViewName;
                        }
                    }
                    if (TextUtils.equals(mAbsFan.getDp(), "8236S")) {
                        Bundle bd = new Bundle();
                        bd.putSerializable(PageArgumentKey.title, title);
                        bd.putSerializable(PageArgumentKey.List, (Serializable) mDeviceTotalConfigurationFunctions);
                        bd.putSerializable(PageArgumentKey.Bean, mAbsFan);
                        bd.putSerializable(PageArgumentKey.tag, "background");
                        UIService.getInstance().postPage(PageKey.DeviceFanOilDetection, bd);
                        return;
                    }
                    Bundle bd = new Bundle();
                    bd.putSerializable(PageArgumentKey.Url, url);
                    bd.putSerializable(PageArgumentKey.title, title);
                    bd.putSerializable(PageArgumentKey.dismantling, dismantling);
                    bd.putSerializable(PageArgumentKey.Bean, mAbsFan);
                    bd.putSerializable(PageArgumentKey.tag, "background");
                    UIService.getInstance().postPage(PageKey.DeviceFanOilDetection, bd);


                } else if ("oilCupReminding".equals(mDeviceConfigurationFunctions.get(position).functionCode)) {
                    String title = mDeviceConfigurationFunctions.get(position).functionName;
                    List<DeviceConfigurationFunctions> deviceConfigurationFunctions =
                            mDeviceConfigurationFunctions.get(position)
                                    .subView
                                    .subViewModelMap
                                    .subViewModelMapSubView
                                    .deviceConfigurationFunctions;
                    mOnRecyclerViewItemClickListener.onItemClick(view);
                    if (deviceConfigurationFunctions == null || deviceConfigurationFunctions.size() == 0)
                        return;
                    String dismantling = null;
                    String url = null;
                    for (int i = 0; i < deviceConfigurationFunctions.size(); i++) {
                        if ("oilCupDismant".equals(deviceConfigurationFunctions.get(i).functionCode)) {
                            dismantling = deviceConfigurationFunctions.get(i).functionName;
                            url = deviceConfigurationFunctions.get(i).subViewName;
                        }
                    }

                    Bundle bd = new Bundle();
                    bd.putSerializable(PageArgumentKey.Bean, mAbsFan);
                    bd.putSerializable(PageArgumentKey.Url, url);
                    bd.putSerializable(PageArgumentKey.title, title);
                    bd.putSerializable(PageArgumentKey.dismantling, dismantling);
                    bd.putSerializable(PageArgumentKey.tag, "background");
                    UIService.getInstance().postPage(PageKey.DeviceFanOilCup, bd);

                }

            }
        });

        if ("oilNetworkState".equals(mDeviceConfigurationFunctions.get(position).functionCode)) {

            if (mAbsFan.clean) {
                viewHolder.mItemView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mItemView.setVisibility(View.GONE);
            }

        } else if ("oilCupReminding".equals(mDeviceConfigurationFunctions.get(position).functionCode)) {
            if (mAbsFan.oilCup == 1) {
                viewHolder.mItemView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mItemView.setVisibility(View.GONE);
            }
        }else if ("OverTem".equals(mDeviceConfigurationFunctions.get(position).functionCode)){
            if (mAbsFan.overTempProtectStatus==1&&mAbsFan.smartSmokeStatus==1) {
                viewHolder.mItemView.setVisibility(View.VISIBLE);
            }else {
                viewHolder.mItemView.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    public class FanBackgroundFuncViewHolder {
        ImageView mIvModelImg;
        TextView mTvModelName;
        LinearLayout mItemView;

    }
}


