package com.robam.roki.ui.view;

import android.content.Context;
import android.content.res.Resources;
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
import com.legent.utils.LogUtils;
import com.robam.common.Utils;
import com.robam.common.events.SteamOvenStatusChangedEvent;
import com.robam.common.pojos.device.WaterPurifier.WaterPurifierStatus;
import com.robam.common.ui.UiHelper;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rent on 16/05/25.
 */
public class DeviceWaterPurifierView extends FrameLayout {

    AbsDevice device;

    @InjectView(R.id.relSteam)
    RelativeLayout relSteam;
    @InjectView(R.id.imgDevice)
    ImageView imgDevice;
    @InjectView(R.id.txtTitle)
    TextView txtTitle;
    @InjectView(R.id.txtDesc)
    TextView txtDesc;
    Resources resources;

    public DeviceWaterPurifierView(Context context) {
        super(context);
        init(context, null);
    }

    public DeviceWaterPurifierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DeviceWaterPurifierView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {
        View view = LayoutInflater.from(cx).inflate(R.layout.view_device_purifier,
                this, true);

        device = Utils.getDefaultWaterPurifier();
        resources=getResources();

        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
//            setType(device != null);
        }
    }



    @Subscribe
    public void onEvent(SteamOvenStatusChangedEvent event) {
        LogUtils.i("2017-1-21event","event"+event.toString());
        if (!Objects.equal(event.pojo.getID(), device.getID()))
            return;
        boolean isOn = event.pojo.isConnected() && (event.pojo.status != WaterPurifierStatus.Wait||event.pojo.status != WaterPurifierStatus.Off);
        relSteam.setBackgroundColor(resources.getColor(isOn ? R.color.home_bg : R.color.c16));
    }

    @OnClick
    public void onClick() {
        DeviceTypeManager dm = DeviceTypeManager.getInstance();
        if (UiHelper.checkAuthWithDialog(getContext(), PageKey.UserLogin)) {
            device = Utils.getDefaultWaterPurifier();
            LogUtils.i("devicewater","device:"+device);
            if (device != null) {
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, device.getID());
                UIService.getInstance().postPage(PageKey.DeviceWaterPurifiy,bd);
            } else {
                UIService.getInstance().postPage(PageKey.DeviceAddByEasylink);
            }
        }
    }

    public void setType(boolean hasDevice) {
        relSteam.setBackgroundColor(resources.getColor(hasDevice ? R.color.c16 : R.color.White));
        imgDevice.setImageResource(hasDevice ? R.mipmap.ic_home_device_zql : R.mipmap.img_device_add);
        txtTitle.setTextColor(resources.getColor(hasDevice ? R.color.White : R.color.home_bg));
        txtDesc.setTextColor(resources.getColor(hasDevice ? R.color.White : R.color.home_bg));
    }

    public void setStatus(short status) {
        setType(true);
        switch (status) {
            case WaterPurifierStatus.On:
                relSteam.setBackgroundColor(resources.getColor(R.color.home_bg));
                break;
            case WaterPurifierStatus.Off:
                relSteam.setBackgroundColor(resources.getColor(R.color.c16));
                break;
            case WaterPurifierStatus.Purify:
                relSteam.setBackgroundColor(resources.getColor(R.color.home_bg));
                break;
            case WaterPurifierStatus.Wash:
                relSteam.setBackgroundColor(resources.getColor(R.color.home_bg));
                break;
            case WaterPurifierStatus.Wait:
                relSteam.setBackgroundColor(resources.getColor(R.color.c16));
                break;
            case WaterPurifierStatus.AlarmStatus:
                relSteam.setBackgroundColor(resources.getColor(R.color.home_bg));
                break;
            default:
                break;
        }
    }
}
