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
import com.legent.plat.services.DeviceTypeManager;
import com.legent.ui.UIService;
import com.robam.common.Utils;
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.common.ui.UiHelper;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by linxiaobin on 2015/12/30.
 */
public class DeviceOvenView extends FrameLayout {

    AbsDevice device;

    @InjectView(R.id.relOven)
    RelativeLayout relOven;
    @InjectView(R.id.imgDevice)
    ImageView imgDevice;
    @InjectView(R.id.txtTitle)
    TextView txtTitle;
    @InjectView(R.id.txtDesc)
    TextView txtDesc;

    public DeviceOvenView(Context context) {
        super(context);
        init(context, null);
    }

    public DeviceOvenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DeviceOvenView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {
        View view = LayoutInflater.from(cx).inflate(R.layout.view_device_oven,
                this, true);

        device = Utils.getDefaultOven();


        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
//            setType(device != null);
        }
    }

    @Subscribe
    public void onEvent(OvenStatusChangedEvent event) {
        if (!Objects.equal(event.pojo.getID(), device.getID()))
            return;

        boolean isOn = event.pojo.isConnected() && event.pojo.status != OvenStatus.Off;
        relOven.setBackgroundColor(getResources().getColor(isOn ? R.color.home_bg : R.color.c01));
    }

    @OnClick
    public void onClick() {
        DeviceTypeManager dm = DeviceTypeManager.getInstance();
        if (UiHelper.checkAuthWithDialog(getContext(), PageKey.UserLogin)) {
            device = Utils.getDefaultOven();
            if (device != null) {
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, device.getID());
                if (((AbsOven) device).status == OvenStatus.Working || ((AbsOven) device).status == OvenStatus.Pause) {
                    if (IRokiFamily.RR039.equals(device.getDt()))
                        UIService.getInstance().postPage(PageKey.DeviceOvenWorking039, bd);
                    else if (IRokiFamily.RR026.equals(device.getDt()) || IRokiFamily.RR016.equals(device.getDt())) {
                        UIService.getInstance().postPage(PageKey.DeviceOven026, bd);
                    }
                } else {
                    if (IRokiFamily.RR039.equals(device.getDt()))
                        UIService.getInstance().postPage(PageKey.DeviceOven039, bd);
                    else if (IRokiFamily.RR026.equals(device.getDt()) || IRokiFamily.RR016.equals(device.getDt())) {
                        UIService.getInstance().postPage(PageKey.DeviceOven026, bd);
                    }
                }
            } else {
                UIService.getInstance().postPage(PageKey.DeviceAddByEasylink);
            }
        }
    }

    public void setType(boolean hasDevice) {
        relOven.setBackgroundColor(getResources().getColor(hasDevice ? R.color.c01 : R.color.White));
        imgDevice.setImageResource(hasDevice ? R.mipmap.ic_device_oven_home : R.mipmap.img_device_oven_add);
        txtTitle.setTextColor(getResources().getColor(hasDevice ? R.color.White : R.color.home_bg));
        txtDesc.setTextColor(getResources().getColor(hasDevice ? R.color.White : R.color.home_bg));
    }

    public void setStatus(short status) {
        if (OvenStatus.Off == status || OvenStatus.Wait == status) {
            relOven.setBackgroundColor(getResources().getColor(R.color.c01));
        } else {
            relOven.setBackgroundColor(getResources().getColor(R.color.home_bg));
        }
    }
}

