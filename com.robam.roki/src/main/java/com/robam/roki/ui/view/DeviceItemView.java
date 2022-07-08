package com.robam.roki.ui.view;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.pojos.device.AbsDevice;
import com.legent.plat.pojos.dictionary.DeviceType;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.robam.common.Utils;
import com.robam.common.events.FanStatusChangedEvent;
import com.robam.common.events.StoveStatusChangedEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.FanStatus;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DeviceItemView extends FrameLayout {

    @InjectView(R.id.layout)
    RelativeLayout layout;
    @InjectView(R.id.txtTitle)
    TextView txtTitle;
    @InjectView(R.id.txtDesc)
    TextView txtDesc;
    @InjectView(R.id.imgDevice)
    ImageView imgDevice;
    AbsDevice device;

    public void setImageDevice(int img_deviceId) {
        imgDevice.setImageResource(img_deviceId);
    }

    public void setTxtDeviceName(String deviceName) {
        txtTitle.setText(deviceName);
    }

    public void setTxtDeviceDesc(String deviceDesc) {
        txtDesc.setText(deviceDesc);
    }


    public DeviceItemView(Context context) {
        super(context);
        init(context, null);
    }

    public DeviceItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DeviceItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventUtils.regist(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventUtils.unregist(this);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        layout.setSelected(selected);
    }


    @Subscribe
    public void onEvent(FanStatusChangedEvent event) {
        if (!Objects.equal(event.pojo.getID(), device.getID()))
            return;

        boolean isOn = event.pojo.isConnected() && event.pojo.status != FanStatus.Off;
        this.setSelected(isOn);
    }

    @Subscribe
    public void onEvent(StoveStatusChangedEvent event) {
        if (!Objects.equal(event.pojo.getID(), device.getID()))
            return;

        boolean isOnLeft = event.pojo.leftHead.status != StoveStatus.Off;
        boolean isOnRight = event.pojo.rightHead.status != StoveStatus.Off;

        this.setSelected(event.pojo.isConnected() && (isOnLeft || isOnRight));
    }


    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_device_item,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
    }

    void loadData(AbsDevice device) {

        this.device = device;
        if (device == null)
            return;

        DeviceType dt = DeviceTypeManager.getInstance().getDeviceType(device.getID());
        txtTitle.setText(dt.getName());
        if (dt.tag != null) {
            txtDesc.setText(dt.tag);
        }

        if (device instanceof AbsFan) {
            imgDevice.setImageResource(R.mipmap.ic_device_header_fan);
        } else if (device instanceof Stove) {
            imgDevice.setImageResource(R.mipmap.ic_device_header_stove);
        }
    }


    @OnClick(R.id.layout)
    public void onClick() {
        if (device == null) return;
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, device.getID());
        String pageKey = null;
        if (device instanceof AbsFan) {
            if (DeviceTypeManager.getInstance().isInDeviceType(device.getGuid(), IRokiFamily.R8700))
                pageKey = PageKey.DeviceFan8700;
            else if (DeviceTypeManager.getInstance().isInDeviceType(device.getGuid(), IRokiFamily.R66A2))
                pageKey = PageKey.DeviceFan66A2;
            else if (DeviceTypeManager.getInstance().isInDeviceType(device.getGuid(), IRokiFamily._66A2H))
                pageKey = PageKey.DeviceFan66A2H;
            else if (DeviceTypeManager.getInstance().isInDeviceType(device.getGuid(), IRokiFamily.R8229))
                pageKey = PageKey.DeviceFan8229;
            else if (DeviceTypeManager.getInstance().isInDeviceType(device.getGuid(), IRokiFamily.R5910))
                pageKey = PageKey.DeviceFan5910;
            else if(DeviceTypeManager.getInstance().isInDeviceType(device.getGuid(),  IRokiFamily._8230S))
                pageKey=PageKey.DeviceFan8230s;
            else if (DeviceTypeManager.getInstance().isInDeviceType(device.getGuid(), IRokiFamily.R5610))
                //pageKey = PageKey.DeviceSideFan;  //转向侧式油烟机 zhaiyuanyi
                pageKey = PageKey.DeviceFan5610;
            else
                pageKey = PageKey.DeviceFan;
        } else if (device instanceof Stove) {
            pageKey = PageKey.DeviceStove;
        }
        UIService.getInstance().postPage(pageKey, bd);
    }


}
