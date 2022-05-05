package com.robam.roki.ui.view.networkoptimization;

import android.content.Context;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legent.ui.UIService;
import com.legent.ui.ext.popoups.Dp2PxUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.NetworkUtils;
import com.robam.common.pojos.DeviceGroupList;
import com.robam.common.pojos.DeviceItemList;
import com.robam.roki.R;
import com.robam.roki.ui.PageKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoudingjun on 2016/12/12.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    Context context;
    ImageView arrow;
    LayoutInflater mInflater;
    String[][] child;
    List<DeviceGroupList> groupList = new ArrayList<DeviceGroupList>();
    List<List<DeviceItemList>> deviceList = new ArrayList<List<DeviceItemList>>();
    TextView devicename;
    TextView textGroup;

    ExpandableListAdapter(Context context, List<DeviceGroupList> groupList, List<List<DeviceItemList>> deviceList) {
        this.context = context;
        this.groupList = groupList;
        this.deviceList = deviceList;
        //设置二级菜单的行数和列数
        child = new String[groupList.size()][1];
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    //取得用于显示给定分组的视图. 这个方法仅返回分组的视图对象
    @Override
    public View getGroupView(int groupPosition, final boolean isExpanded,
                             View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View expandableGroupView = null;
        if (groupPosition == groupList.size()) {
            expandableGroupView = mInflater.inflate(R.layout.expandablelistview_bottom, null);
            Button button = expandableGroupView.findViewById(R.id.btn_cant_find);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIService.getInstance().postPage(PageKey.CantfindDevice);
                }
            });

        } else {
            expandableGroupView = new ExpandableGroupView(context);
            arrow = expandableGroupView.findViewById(R.id.arrow);
            if (isExpanded) {
                arrow.setImageResource(R.mipmap.bottom_arrow);
            } else {
                arrow.setImageResource(R.mipmap.right_arrow);
            }
            LogUtils.i("20170214","grouplist::"+groupList.get(groupPosition));
            ((ExpandableGroupView) expandableGroupView).setDevice(groupList.get(groupPosition));
        }
        return expandableGroupView;
    }

    //取得指定分组的ID.该组ID必须在组中是唯一的.必须不同于其他所有ID（分组及子项目的ID）.
    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    //取得分组数
    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return groupList.size() + 1;
    }

    //取得与给定分组关联的数据
    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return groupList.get(groupPosition);
    }

    //取得指定分组的子元素数.
    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
      //  LogUtils.i("20170214","child:"+child[groupPosition].length);
        return child[groupPosition].length;
    }

    //取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            mViewChild = new ViewChild();
            convertView = mInflater.inflate(R.layout.expandablelistview_child, null);
            mViewChild.gridView = convertView.findViewById(R.id.gridChildView);
            convertView.setTag(mViewChild);
        } else {
            mViewChild = (ViewChild) convertView.getTag();
        }
        //LogUtils.i("20170215","groupPosition:"+groupPosition);
        GridChildViewAdapter gridChildViewAdapter = new GridChildViewAdapter(context, deviceList.get(groupPosition),groupList.get(groupPosition).getDeviceName());
       // LogUtils.i("20170215","deviceList.get(groupPosition)"+deviceList.get(groupPosition));
        mViewChild.gridView.setAdapter(gridChildViewAdapter);
        return convertView;
    }

    //取得给定分组中给定子视图的ID. 该组ID必须在组中是唯一的.必须不同于其他所有ID（分组及子项目的ID）.
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return child[groupPosition][childPosition];
    }

    ViewChild mViewChild;

    static class ViewChild {
        GridView gridView;
    }
}
