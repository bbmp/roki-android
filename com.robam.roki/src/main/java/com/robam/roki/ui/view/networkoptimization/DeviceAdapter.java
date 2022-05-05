package com.robam.roki.ui.view.networkoptimization;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.legent.utils.LogUtils;
import com.robam.common.pojos.DeviceGroupList;
import com.robam.common.pojos.DeviceItemList;

import java.util.ArrayList;
import java.util.List;

public class DeviceAdapter extends BaseAdapter {

    private Context cx;
    private List<DeviceItemList> deviceItemList = new ArrayList<DeviceItemList>();
    private List<DeviceItemList> deviceItemsumList = new ArrayList<DeviceItemList>();
    private List<DeviceGroupList> groupList = new ArrayList<DeviceGroupList>();

    public DeviceAdapter(Context cx) {
        this.cx = cx;
    }

    public void loadData(List<List<DeviceItemList>> deviceList) {
        this.deviceItemsumList.clear();
        notifyDataSetChanged();
        for (int i = 0; i < deviceList.size(); i++) {
            LogUtils.i("20170214device","deviceList"+deviceList.get(i).toString());
            deviceItemList = deviceList.get(i);
            for (int j = 0; j < deviceItemList.size(); j++) {
                if (!deviceItemList.get(j).getName().contains("找不到")) {
                    deviceItemsumList.add(deviceItemList.get(j));
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return deviceItemsumList.size();
    }

    @Override
    public Object getItem(int position) {
        return deviceItemsumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        vh = new ViewHolder();
        vh.view = new ExpandableChildItemView(cx, deviceItemsumList.get(position), deviceItemsumList.get(position).tag );
        convertView = vh.view;
        return convertView;
    }

    static class ViewHolder {
        ExpandableChildItemView view;
    }
}
