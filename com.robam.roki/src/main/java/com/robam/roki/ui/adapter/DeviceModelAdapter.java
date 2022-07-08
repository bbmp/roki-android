package com.robam.roki.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.LogUtils;
import com.legent.utils.graphic.ImageUtils;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;

import java.util.List;


/**
 * Created by 14807 on 2018/2/6.
 * 模式适配器
 */

public class DeviceModelAdapter extends RecyclerView.Adapter<DeviceSelectModelViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<DeviceConfigurationFunctions> mDeviceSeletModelList;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;


    public DeviceModelAdapter(Context context, List<DeviceConfigurationFunctions> deviceSeletModelList,
                              OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mContext = context;
        mDeviceSeletModelList = deviceSeletModelList;
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public DeviceSelectModelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.device_model_item, parent, false);
        DeviceSelectModelViewHolder deviceSelectModelViewHoder = new DeviceSelectModelViewHolder(view);
        deviceSelectModelViewHoder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRecyclerViewItemClickListener.onItemClick(v);
            }
        });
        return deviceSelectModelViewHoder;
    }

    @Override
    public void onBindViewHolder(DeviceSelectModelViewHolder holder, int position) {

        if (mDeviceSeletModelList != null && mDeviceSeletModelList.size() > 0) {
            ImageUtils.displayImage(mDeviceSeletModelList.get(position).backgroundImg, holder.img);
            holder.name.setText(mDeviceSeletModelList.get(position).functionName);
            holder.itemView.setTag(mDeviceSeletModelList.get(position).functionCode);
        }
    }

    @Override
    public int getItemCount() {
        return mDeviceSeletModelList.size();
    }
}


class DeviceSelectModelViewHolder extends RecyclerView.ViewHolder {

    public ImageView img;
    public TextView name;

    public DeviceSelectModelViewHolder(View itemView) {
        super(itemView);
        img = itemView.findViewById(R.id.iv_model);
        name = itemView.findViewById(R.id.tv_model_name);
    }
}
