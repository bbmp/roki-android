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
import com.legent.utils.api.DisplayUtils;
import com.robam.roki.R;

import java.util.List;

/**
 * Created by Dell on 2018/7/6.
 */

public class OvenGrid035Apater extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<DeviceConfigurationFunctions> mainList;
    Context cx;
    String dt;
    public OvenGrid035Apater(Context cx, List<DeviceConfigurationFunctions> modeList,String dt){
        this.cx = cx;
        this.mainList = modeList;
        this.dt = dt;
        this.mLayoutInflater = LayoutInflater.from(cx);
    }

    @Override
    public int getCount() {
        return mainList.size()<0 ? 0:mainList.size();
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
            if ("CQ915".equals(dt)) {
                //没缓存
                convertView = mLayoutInflater.inflate(R.layout.mode_steam_oven_915_show, null);
            }else {
                convertView = mLayoutInflater.inflate(R.layout.mode_oven_035_show, null);
            }

            holder = new ViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.ivl);
            holder.tv = (TextView) convertView.findViewById(R.id.tvnel);
            convertView.setTag(holder);
        } else {
            //有缓存
            holder = (ViewHolder) convertView.getTag();
        }
        //绑定viewholder数据
        String functionName = mainList.get(position).functionName;
        Glide.with(cx).load(mainList.get(position).backgroundImg).into(holder.iv);
        if (functionName.contains("n")) {
            String[] split = functionName.split("n");
            String frontText;
            String afterText = null;
            if (0 < split.length) {
                String front = split[0];
                frontText = front.substring(0, front.length() - 1);
                if (1 < split.length) {
                    afterText = split[1];
                }
                String name = frontText + "\n" + afterText;
                holder.tv.setText(name);
            }
        } else {
            holder.tv.setText(functionName);
        }
        return convertView;
    }


    public interface OnClickTag{
        void onClickGrid();
    }

    public OnClickTag onClickTagLister;

    public void setOnClickTagLister(OnClickTag onClickTagLister) {
        this.onClickTagLister = onClickTagLister;
    }

    class ViewHolder {
        ImageView iv;
        TextView tv;
    }

}
