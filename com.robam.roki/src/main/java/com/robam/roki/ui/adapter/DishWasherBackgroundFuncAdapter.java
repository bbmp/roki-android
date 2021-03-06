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
import com.robam.common.events.DisherWasherStatusChangeEvent;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.page.device.dishWasher.DishWasherName;
import com.robam.roki.utils.AlarmDataUtils;

import java.util.List;


/**
 * Created by 14807 on 2018/4/8.
 */

public class DishWasherBackgroundFuncAdapter extends BaseAdapter {

    private AbsDishWasher absDishWasher;
    private LayoutInflater mInflater;
    private Context cx;
    private List<DeviceConfigurationFunctions> mDeviceConfigurationFunctions;

    short LackSaltStatus;
    short LackRinseStatus;
    short AbnormalAlarmStatus;
    String wxappletParams;

    public DishWasherBackgroundFuncAdapter(Context context, List<DeviceConfigurationFunctions> deviceConfigurationFunctionses
            , AbsDishWasher absDishWasher, String wxappletParams) {
        this.cx = context;
        this.mDeviceConfigurationFunctions = deviceConfigurationFunctionses;

        for (int i = 0; i < mDeviceConfigurationFunctions.size(); i++) {
            String functionCode = mDeviceConfigurationFunctions.get(i).functionCode;
            LogUtils.i("20200619","functionCode:::"+functionCode);
        }
        this.absDishWasher = absDishWasher;
        this.mInflater = LayoutInflater.from(context);
        this.wxappletParams = wxappletParams;


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
        // ???????????????
        DishWasherBackgroundFuncViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new DishWasherBackgroundFuncViewHolder();
            // ???xml??????????????????java??????
            convertView = mInflater.inflate(R.layout.item_dish_washer_backgroundfunc_page, null);
            // ??????????????????????????????
            viewHolder.mItemView = (LinearLayout) convertView.findViewById(R.id.itemView);
            viewHolder.mIvModelImg = (ImageView) convertView.findViewById(R.id.iv_model_img);
            viewHolder.mTvModelName = (TextView) convertView.findViewById(R.id.tv_model_name);
            // ??????????????????
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (DishWasherBackgroundFuncViewHolder) convertView.getTag();
        }
        // ??????????????????????????????
        if ("alert".equals(mDeviceConfigurationFunctions.get(position).functionType)) {
            viewHolder.mTvModelName.setText(mDeviceConfigurationFunctions.get(position).functionName);
            Glide.with(cx)
                    .load(mDeviceConfigurationFunctions.get(position).backgroundImg)
//                    .crossFade()
                    .into(viewHolder.mIvModelImg);
            viewHolder.mItemView.setTag(mDeviceConfigurationFunctions.get(position).functionCode);
        }

        viewHolder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //???????????????
                if (DishWasherName.softSaltWater.equals(mDeviceConfigurationFunctions.get(position).functionCode)) {
                    String title = mDeviceConfigurationFunctions.get(position).functionName;
                    List<DeviceConfigurationFunctions> deviceConfigurationFunctions = mDeviceConfigurationFunctions.get(position)
                            .subView
                            .subViewModelMap
                            .subViewModelMapSubView
                            .deviceConfigurationFunctions;
                    if (deviceConfigurationFunctions == null || deviceConfigurationFunctions.size() == 0)
                        return;
                    String params = null;
                    String url = null;
                    for (int i = 0; i < deviceConfigurationFunctions.size(); i++) {
                        if ("descText".equals(deviceConfigurationFunctions.get(i).functionCode)) {
                            params = deviceConfigurationFunctions.get(i).functionParams;
                        }
                        if ("buyLink".equals(deviceConfigurationFunctions.get(i).functionCode)) {
                            url = deviceConfigurationFunctions.get(i).functionParams;
                        }
                    }
                    Bundle bd = new Bundle();
                    bd.putSerializable(PageArgumentKey.Bean, absDishWasher);
                    bd.putSerializable(PageArgumentKey.Url, url);
                    bd.putSerializable(PageArgumentKey.title, title);
                    bd.putSerializable(PageArgumentKey.text, params);
                    bd.putSerializable(PageArgumentKey.tag, "2");
                    bd.putSerializable(PageArgumentKey.wxparams, wxappletParams);
                    UIService.getInstance().postPage(PageKey.SpecialState, bd);

                    //???????????????
                } else if (DishWasherName.bleaching.equals(mDeviceConfigurationFunctions.get(position).functionCode)) {
                    String title = mDeviceConfigurationFunctions.get(position).functionName;
                    List<DeviceConfigurationFunctions> deviceConfigurationFunctions = mDeviceConfigurationFunctions.get(position)
                            .subView
                            .subViewModelMap
                            .subViewModelMapSubView
                            .deviceConfigurationFunctions;
                    if (deviceConfigurationFunctions == null || deviceConfigurationFunctions.size() == 0)
                        return;
                    String params = null;
                    String url = null;
                    for (int i = 0; i < deviceConfigurationFunctions.size(); i++) {
                        if ("descText".equals(deviceConfigurationFunctions.get(i).functionCode)) {
                            params = deviceConfigurationFunctions.get(i).functionParams;
                        }
                        if ("buyLink".equals(deviceConfigurationFunctions.get(i).functionCode)) {
                            url = deviceConfigurationFunctions.get(i).functionParams;
                        }
                    }
                    Bundle bd = new Bundle();
                    bd.putSerializable(PageArgumentKey.Bean, absDishWasher);
                    bd.putSerializable(PageArgumentKey.Url, url);
                    bd.putSerializable(PageArgumentKey.title, title);
                    bd.putSerializable(PageArgumentKey.text, params);
                    bd.putSerializable(PageArgumentKey.tag, "3");
                    bd.putSerializable(PageArgumentKey.wxparams, wxappletParams);
                    UIService.getInstance().postPage(PageKey.SpecialState, bd);
                    //??????
                } else if (DishWasherName.hydropenia.equals(mDeviceConfigurationFunctions.get(position).functionCode)) {
                    AlarmDataUtils.dishWasherAlarm(absDishWasher, absDishWasher.alarmId);

                }

            }
        });
        if ((DishWasherName.hydropenia).equals(mDeviceConfigurationFunctions.get(position).functionCode)) {
            if (AbnormalAlarmStatus == (short) 1) {
                viewHolder.mItemView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mItemView.setVisibility(View.GONE);
            }
        } else if ((DishWasherName.softSaltWater).equals(mDeviceConfigurationFunctions.get(position).functionCode)) {
            if (LackSaltStatus == (short) 1) {
                viewHolder.mItemView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mItemView.setVisibility(View.GONE);
            }
        } else if ((DishWasherName.bleaching).equals(mDeviceConfigurationFunctions.get(position).functionCode)) {
            if (LackRinseStatus == (short) 1) {
                viewHolder.mItemView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mItemView.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    public class DishWasherBackgroundFuncViewHolder {
        ImageView mIvModelImg;
        TextView mTvModelName;
        LinearLayout mItemView;

    }


    //@Subscribe
    //public void onEvent(DisherWasherStatusChangeEvent event) {
    //    if (absDishWasher == null || !Objects.equal(absDishWasher.getID(), event.pojo.getID()))
    //        return;
    //    LackSaltStatus = absDishWasher.LackSaltStatus;//????????????
    //    LackRinseStatus = absDishWasher.LackRinseStatus;//??????????????????
    //    AbnormalAlarmStatus = absDishWasher.AbnormalAlarmStatus;//????????????
    //    this.notifyDataSetChanged();
    //}


}


