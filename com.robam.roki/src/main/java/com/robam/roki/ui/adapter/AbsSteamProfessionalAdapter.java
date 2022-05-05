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

import java.util.List;

public class AbsSteamProfessionalAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<DeviceConfigurationFunctions> mainList;
    Context cx;

    public AbsSteamProfessionalAdapter(List<DeviceConfigurationFunctions> mainList, Context cx) {
        this.mainList = mainList;
        this.cx = cx;
        this.mLayoutInflater = LayoutInflater.from(cx);
    }

    @Override
    public int getCount() {
        return mainList.size() < 0 ? 0 : mainList.size();
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
            //没缓存
            convertView = mLayoutInflater.inflate(R.layout.item_professional, null);
            holder = new ViewHolder();
            holder.iv = convertView.findViewById(R.id.ivl);
            holder.tv = convertView.findViewById(R.id.tvnel);
            convertView.setTag(holder);
        } else {
            //有缓存
            holder = (ViewHolder) convertView.getTag();
        }
        //绑定viewholder数据
        holder.tv.setText(mainList.get(position).functionName);
        Glide.with(cx).load(mainList.get(position).backgroundImg).into(holder.iv);
        return convertView;
    }


    public interface OnClickTag {
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
