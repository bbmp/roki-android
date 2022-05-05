package com.robam.roki.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;

import com.robam.roki.R;
import com.robam.roki.model.bean.DeviceOvenDiyParams;
import com.robam.roki.ui.wheel.WheelView;

import java.util.List;

/**
 * Created by Administrator on 2019/9/11.
 */

public class DiyParamAdapter extends WheelView.WheelAdapter<TimeViewHolder> {
    private List<DeviceOvenDiyParams> deviceList;
    private String str;

    public DiyParamAdapter(List<DeviceOvenDiyParams> deviceList, String str) {
        this.deviceList = deviceList;
        this.str = str;
    }

    private int pos = 0;


    @Override
    public int getItemCount() {
        return (deviceList != null && deviceList.size() > 0) ? deviceList.size() : 0;
    }

    @Override
    public TimeViewHolder onCreateViewHolder(LayoutInflater inflater, int viewType) {
        return new TimeViewHolder(inflater.inflate(R.layout.timeview_item, null, false));
    }

    @Override
    public void onBindViewHolder(TimeViewHolder holder, int position) {

        if (str != null) {
            holder.tv.setText(deviceList.get(position).getValue());
            if (position == pos) {
                holder.tv.setTextColor(Color.parseColor("#000000"));
            } else {
                holder.tv.setTextColor(Color.GRAY);
            }
        } else {
            holder.tv.setText(deviceList.get(position).getValue());
            if (position == pos) {
                holder.tv.setTextColor(Color.parseColor("#000000"));
            } else {
                holder.tv.setTextColor(Color.GRAY);
            }
        }

    }

    @Override
    public void getSelect(int pos) {
        this.pos = pos;
        notifyDataSetChanged();

    }
}
