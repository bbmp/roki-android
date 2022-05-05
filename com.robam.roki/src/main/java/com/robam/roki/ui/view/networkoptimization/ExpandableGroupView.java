package com.robam.roki.ui.view.networkoptimization;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.DeviceGroupList;
import com.robam.roki.R;

/**
 * Created by zhoudingjun on 2016/12/9.
 */

public class ExpandableGroupView extends FrameLayout {
    ImageView img_device;
    TextView textGroup;
    TextView textGroupEnglishName;
    DeviceGroupList groupListBean;
    Context mContext;

    public ExpandableGroupView(Context context) {
        this(context, null);
    }

    public ExpandableGroupView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    public ExpandableGroupView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {
        View view = LayoutInflater.from(cx).inflate(R.layout.expandablelistview_groups,
                this, true);
        mContext = cx;
        img_device = view.findViewById(R.id.img_device);
        textGroup = view.findViewById(R.id.textGroup);
        textGroupEnglishName = view.findViewById(R.id.textGroupEnglishName);
    }

    public void setDevice(DeviceGroupList groupListBean) {
        this.groupListBean = groupListBean;
        refresh();
    }

    private void refresh() {
//        img_device.setImageResource(groupListBean.getImage_device());
        Glide.with(mContext)
                .load(groupListBean.iconUrl)
                .into(img_device);
        textGroup.setText(groupListBean.getDeviceName());
        LogUtils.i("20170214","deviceName:"+groupListBean.getDeviceName());
        textGroupEnglishName.setText(groupListBean.getDeviceEnglishName());
    }

}
