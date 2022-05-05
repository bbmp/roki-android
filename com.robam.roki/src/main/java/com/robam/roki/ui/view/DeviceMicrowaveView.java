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
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.pojos.device.AbsDevice;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.robam.common.Utils;
import com.robam.common.events.MicroWaveStatusChangedEvent;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.microwave.MicroWaveStatus;
import com.robam.common.ui.UiHelper;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rent on 2016/06/10.
 */
public class DeviceMicrowaveView extends FrameLayout {

    AbsDevice device;

    @InjectView(R.id.relOven)
    RelativeLayout relOven;
    @InjectView(R.id.imgDevice)
    ImageView imgDevice;
    @InjectView(R.id.txtTitle)
    TextView txtTitle;
    @InjectView(R.id.txtDesc)
    TextView txtDesc;

    public DeviceMicrowaveView(Context context) {
        super(context);
        init(context, null);
    }

    public DeviceMicrowaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DeviceMicrowaveView(Context context, AttributeSet attrs, int defStyle) {
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

    void init(Context cx, AttributeSet attrs) {
        View view = LayoutInflater.from(cx).inflate(R.layout.view_device_oven,
                this, true);
        device = Utils.getDefaultMicrowave();


        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
//            setType(device != null);
            txtTitle.setText("微波炉");
            txtDesc.setText("Microwave");
        }
        //判断有无微波炉设备
        if (device != null)
            setType(true);
        else
            setType(false);


    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (device == null || !Objects.equal(device.getID(), event.device.getID()))
            return;
        boolean isConnect = event.isConnected;
        setStatu(isConnect);
    }

    @Subscribe
    public void onEvent(MicroWaveStatusChangedEvent event) {
        if (!Objects.equal(event.pojo.getID(), device.getID()))
            return;
        boolean isOn = event.pojo.isConnected() && (event.pojo.state == MicroWaveStatus.Run || event.pojo.state == MicroWaveStatus.Pause);
        setStatu(isOn);
    }

    @OnClick
    public void onClick() {
        /*if(true){
            UIService.getInstance().postPage(PageKey.DeviceMicrowave);return;
        }*/
        DeviceTypeManager dm = DeviceTypeManager.getInstance();
        if (UiHelper.checkAuthWithDialog(getContext(), PageKey.UserLogin)) {
            device = Utils.getDefaultMicrowave();
            if (device != null) {
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, device.getID());
                if (((AbsMicroWave) device).state == MicroWaveStatus.Pause || ((AbsMicroWave) device).state == MicroWaveStatus.Run) {
                    if(((AbsMicroWave) device).step==0)
                        UIService.getInstance().postPage(PageKey.DeviceMicrowaveNormalWorking, bd);
                    else
                        UIService.getInstance().postPage(PageKey.DeviceMicrowaveLinkageWorking, bd);
                    //ToastUtils.show("运行好啊", Toast.LENGTH_SHORT);
                } else {
                    UIService.getInstance().postPage(PageKey.DeviceMicrowave, bd);
                }
            } else {
                UIService.getInstance().postPage(PageKey.DeviceAddByEasylink);
            }
        }
    }

    public void setType(boolean hasDevice) {
        relOven.setBackgroundColor(getResources().getColor(hasDevice ? R.color.c01 : R.color.White));
        imgDevice.setImageResource(hasDevice ? R.mipmap.ic_device_microwave_home_write : R.mipmap.img_device_oven_add);
        txtTitle.setTextColor(getResources().getColor(hasDevice ? R.color.White : R.color.home_bg));
        txtDesc.setTextColor(getResources().getColor(hasDevice ? R.color.White : R.color.home_bg));
    }

    public void setStatu(boolean isOn) {
        relOven.setBackgroundColor(isOn ? getResources().getColor(R.color.home_bg) : getResources().getColor(R.color.c01));
    }


}

