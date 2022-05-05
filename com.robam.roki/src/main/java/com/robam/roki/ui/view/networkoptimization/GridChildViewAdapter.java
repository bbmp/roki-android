package com.robam.roki.ui.view.networkoptimization;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.robam.common.pojos.DeviceItemList;

import java.util.List;

/**
 * Created by zhoudingjun on 2016/12/8.
 */


public class GridChildViewAdapter extends BaseAdapter {
    private Context cx;
    private List<DeviceItemList> listitem;
    private String groupName;

    public GridChildViewAdapter(Context context, List<DeviceItemList> listitem,String groupName) {
        this.cx = context;
        this.listitem = listitem;
        this.groupName=groupName;
       /* LogUtils.i("20170214","listItem:"+listitem.size());
        LogUtils.i("20170214","groupName:"+groupName);*/
    }
    @Override
    public int getCount() {
        return listitem.size();
    }

    @Override
    public Object getItem(int position) {
        return listitem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            DeviceItemList device = listitem.get(position);
            vh.view = new ExpandableChildItemView(cx,device,groupName);
            convertView = vh.view;
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    static class ViewHolder {
        ExpandableChildItemView view;
    }

}
