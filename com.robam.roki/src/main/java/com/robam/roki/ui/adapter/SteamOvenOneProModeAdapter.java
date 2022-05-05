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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Dell on 2018/7/6.
 */

public class SteamOvenOneProModeAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<DeviceConfigurationFunctions> mainList;
    Context cx;
    String dt;
    String imgType;
    private String paramType;

    public SteamOvenOneProModeAdapter(Context cx, List<DeviceConfigurationFunctions> modeList, String dt, String imgType) {
        this.cx = cx;
        this.mainList = modeList;
        this.dt = dt;
        this.imgType = imgType;
        this.mLayoutInflater = LayoutInflater.from(cx);
        initData();
    }

    private void initData() {
        try {
            if (!"".equals(imgType)) {
                JSONObject jsonObject = new JSONObject(imgType);
                JSONObject imgType = jsonObject.optJSONObject("imgType");
                paramType = imgType.optString("value");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            if ("CQ915".equals(dt)) {
                convertView = mLayoutInflater.inflate(R.layout.mode_steam_oven_915_show, null);
            } else if ("CQ908".equals(dt)) {
                convertView = mLayoutInflater.inflate(R.layout.mode_steam_oven_one_pro_show, null);
            } else {
                convertView = mLayoutInflater.inflate(R.layout.mode_oven_pro_show, null);
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
        holder.tv.setText(mainList.get(position).functionName);

        if ("".equals(imgType)) {
            Glide.with(cx).load(mainList.get(position).backgroundImg).into(holder.iv);
        } else {
            if (!"".equals(paramType)) {
                if ("backgroundImgH".equals(paramType)) {
                    Glide.with(cx).load(mainList.get(position).backgroundImgH).into(holder.iv);
                } else {
                    Glide.with(cx).load(mainList.get(position).backgroundImg).into(holder.iv);
                }
            } else {
                Glide.with(cx).load(mainList.get(position).backgroundImg).into(holder.iv);
            }


        }

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
