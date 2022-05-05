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
import com.legent.ui.UIService;
import com.robam.common.Utils;
import com.robam.common.events.SteamOvenStatusChangedEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.common.ui.UiHelper;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rosicky on 15/12/29.
 */
public class DeviceSteamView extends FrameLayout {

    AbsDevice device;

    @InjectView(R.id.relSteam)
    RelativeLayout relSteam;
    @InjectView(R.id.imgDevice)
    ImageView imgDevice;
    @InjectView(R.id.txtTitle)
    TextView txtTitle;
    @InjectView(R.id.txtDesc)
    TextView txtDesc;

    public DeviceSteamView(Context context) {
        super(context);
        init(context, null);
    }

    public DeviceSteamView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DeviceSteamView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {
        View view = LayoutInflater.from(cx).inflate(R.layout.view_device_steam_oven,
                this, true);

        device = Utils.getDefaultSteam();


        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
//            setType(device != null);
        }
    }

    @Subscribe
    public void onEvent(SteamOvenStatusChangedEvent event) {
        if (!Objects.equal(event.pojo.getID(), device.getID()))
            return;

        boolean isOn = event.pojo.isConnected() && event.pojo.status != SteamStatus.Off;
        relSteam.setBackgroundColor(getResources().getColor(isOn ? R.color.home_bg : R.color.c01));
    }

    @OnClick
    public void onClick() {
        if (UiHelper.checkAuthWithDialog(getContext(), PageKey.UserLogin)) {
            device = Utils.getDefaultSteam();
            if (device != null) {
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, device.getID());
                if (((AbsSteamoven) device).status == SteamStatus.Working || ((AbsSteamoven) device).status == SteamStatus.Pause) {
                    if (IRokiFamily.RS209.equals(device.getDt()))
                        UIService.getInstance().postPage(PageKey.DeviceSteamWorking, bd);
                    else if (IRokiFamily.RS226.equals(device.getDt()))
                        UIService.getInstance().postPage(PageKey.DeviceSteam226, bd);
                } else {
                    if (IRokiFamily.RS209.equals(device.getDt()))
                        UIService.getInstance().postPage(PageKey.DeviceSteamOven, bd);
                    else if (IRokiFamily.RS226.equals(device.getDt()))
                        UIService.getInstance().postPage(PageKey.DeviceSteam226, bd);
                }
            } else {
                UIService.getInstance().postPage(PageKey.DeviceAddByEasylink);
            }
        }
    }

    public void setType(boolean hasDevice) {
        relSteam.setBackgroundColor(getResources().getColor(hasDevice ? R.color.c01 : R.color.White));
        imgDevice.setImageResource(hasDevice ? R.mipmap.ic_home_device_zql : R.mipmap.img_device_add);
        txtTitle.setTextColor(getResources().getColor(hasDevice ? R.color.White : R.color.home_bg));
        txtDesc.setTextColor(getResources().getColor(hasDevice ? R.color.White : R.color.home_bg));
    }

    public void setStatus(short status) {
        setType(true);
        switch (status) {
            case SteamStatus.Working:
                relSteam.setBackgroundColor(getResources().getColor(R.color.home_bg));
                break;
            case SteamStatus.Off:
                relSteam.setBackgroundColor(getResources().getColor(R.color.c01));
                break;
            case SteamStatus.On:
                relSteam.setBackgroundColor(getResources().getColor(R.color.home_bg));
                break;
            case SteamStatus.Pause:
                relSteam.setBackgroundColor(getResources().getColor(R.color.home_bg));
                break;
            case SteamStatus.Wait:
                relSteam.setBackgroundColor(getResources().getColor(R.color.c01));
                break;
            case SteamStatus.PreHeat:
                relSteam.setBackgroundColor(getResources().getColor(R.color.home_bg));
                break;
            default:
                break;
        }
    }
}
