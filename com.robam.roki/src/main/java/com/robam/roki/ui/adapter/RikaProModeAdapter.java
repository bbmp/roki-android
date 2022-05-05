package com.robam.roki.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.roki.R;
import com.robam.roki.model.bean.RikaFunctionParams;

import java.util.List;

public class RikaProModeAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private RikaFunctionParams params;
    Context cx;
    String dt;
    String imgType;
    private String paramType;

    public RikaProModeAdapter(Context cx, RikaFunctionParams params, String dt) {
        this.cx = cx;
        this.params = params;
        this.dt = dt;
        this.imgType = imgType;
        this.mLayoutInflater = LayoutInflater.from(cx);
    }

    @Override
    public int getCount() {
        return params == null ? 0 : params.modeList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.mode_oven_pro_show, null);
            holder = new ViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.ivl);
            holder.tv = (TextView) convertView.findViewById(R.id.tvnel);
            convertView.setTag(holder);
        } else {
            //有缓存
            holder = (RikaProModeAdapter.ViewHolder) convertView.getTag();
        }
        holder.tv.setText(params.modeList.get(position).mode.title);
        Glide.with(cx).load(params.modeList.get(position).mode.image).into(holder.iv);
        return convertView;
    }

    class ViewHolder {
        ImageView iv;
        TextView tv;
    }
}
