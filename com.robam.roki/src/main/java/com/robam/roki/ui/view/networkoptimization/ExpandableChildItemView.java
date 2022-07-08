package com.robam.roki.ui.view.networkoptimization;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.graphic.ImageUtils;
import com.robam.common.Utils;
import com.robam.common.pojos.DeviceItemList;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhoudingjun on 2016/12/9.
 */

public class ExpandableChildItemView extends FrameLayout {
    ImageView img_device;
    TextView deviceName;
    DeviceItemList deviceItemList;
    String groupName;
    String strDeviceName;
    AbsFan fan;

    public ExpandableChildItemView(Context context, DeviceItemList deviceItemList, String groupName) {
        this(context, null, deviceItemList, groupName);
    }

    public ExpandableChildItemView(Context context, AttributeSet attrs, DeviceItemList deviceItemList, String groupName) {
        this(context, attrs, 0, deviceItemList, groupName);
        init(context, attrs, deviceItemList, groupName);
    }

    public ExpandableChildItemView(Context context, AttributeSet attrs, int defStyle, DeviceItemList deviceItemList, String groupName) {
        super(context, attrs, defStyle);
        init(context, attrs, deviceItemList, groupName);
    }

    void init(Context cx, AttributeSet attrs, DeviceItemList deviceItemList, String groupName) {
        this.deviceItemList = deviceItemList;
        this.groupName = groupName;
        //  LogUtils.i("20170214","deviceItemList:"+deviceItemList.getName());

        View view = LayoutInflater.from(cx).inflate(R.layout.expandablechilditemview,
                this, true);
        ButterKnife.inject(this, view);
        img_device = view.findViewById(R.id.img_device);
        deviceName = view.findViewById(R.id.txt_devicename);
        fan = Utils.getDefaultFan();
        LogUtils.i("20170804", "fan::" + fan);
        refresh();
    }

    private void refresh() {
        if (deviceItemList.getIconUrl() == null) {
            img_device.setVisibility(GONE);
        } else {
            ImageUtils.displayImage(deviceItemList.getIconUrl(), img_device);
        }
        String str = deviceItemList.displayType;
        //  LogUtils.i("20170603","str:::"+str);
//        if ("R".equals(str.substring(0,1))){
//            deviceName.setText(str.substring(1));
//        } else{
        deviceName.setText(str);
//        }

    }

    @OnClick(R.id.lin_deviceitem)
    public void onClickDeviceitem() {
        if (!TextUtils.isEmpty(deviceName.getText().toString())) {
            strDeviceName = deviceName.getText().toString();
        } else {
            return;
        }
        /*if (strDeviceName.contains("R9700") || strDeviceName.contains("R8230")|| strDeviceName.contains("R5910")||strDeviceName.contains("8230S")) {
            UIService.getInstance().postPage(PageKey.DeviceScan);
        } else if (strDeviceName.contains("找不到")) {
            UIService.getInstance().postPage(PageKey.CantfindDevice);
        } else {
            SwitchToWiFiConnectPage();
        }*/
     /*   if ("9W70".equals(strDeviceName)||"9B39".equals(strDeviceName)){
            if (fan==null){
                ToastUtils.show("请先添加油烟机", Toast.LENGTH_SHORT);
                return;
            }
        }*/
        if ("YYJ02".equals(deviceItemList.getDp())) {//有屏
            Bundle bundle = new Bundle();
            bundle.putString("NetImgUrl", deviceItemList.getNetImgUrl());
            bundle.putString("displayType", deviceItemList.displayType);
            UIService.getInstance().postPage(PageKey.DeviceScan, bundle);
        } else if ("YYJ01".equals(deviceItemList.getDp())) {//无屏
            SwitchToWiFiConnectPage();
        } else if (strDeviceName.contains("找不到")) {
            UIService.getInstance().postPage(PageKey.CantfindDevice);
        } else {
            SwitchToWiFiConnectPage();
        }
    }

    private void SwitchToWiFiConnectPage() {
        Bundle bundle = new Bundle();
        bundle.putString("NetImgUrl", deviceItemList.getNetImgUrl());
        bundle.putString("NetTips", deviceItemList.getNetTips());
        bundle.putString("displayType", deviceItemList.displayType);
        bundle.putString("chnName", groupName);
        bundle.putString("strDeviceName", strDeviceName);
        LogUtils.i("20170802", "strDeviceName::" + strDeviceName);
        LogUtils.i("20170227", "NetTips:" + deviceItemList.getNetTips());

       /* if ("9W70".equals(strDeviceName)||"9B39".equals(strDeviceName)){
            UIService.getInstance().postPage(PageKey.DeviceZJWifiConnect, bundle);
        }else{*/
        UIService.getInstance().postPage(PageKey.DeviceWifiConnect, bundle);
        //}


    }
}
