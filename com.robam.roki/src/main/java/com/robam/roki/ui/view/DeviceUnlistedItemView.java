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
import com.robam.common.events.SteriStatusChangedEvent;
import com.robam.common.pojos.device.Sterilizer.SteriStatus;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DeviceUnlistedItemView extends FrameLayout {

    DeviceType type;
    @InjectView(R.id.layout)
    RelativeLayout layout;
    @InjectView(R.id.txtTitle)
    TextView txtTitle;
    @InjectView(R.id.txtDesc)
    TextView txtDesc;
    @InjectView(R.id.imgDevice)
    ImageView imgDevice;

    AbsDevice device;


    public DeviceUnlistedItemView(Context context) {
        super(context);
        init(context, null);
    }

    public DeviceUnlistedItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DeviceUnlistedItemView(Context context, AttributeSet attrs, int defStyle) {
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
    public void onEvent(SteriStatusChangedEvent event) {
        if (!Objects.equal(event.pojo.getID(), device.getID()))
            return;

        boolean isOn = event.pojo.isConnected() && event.pojo.status != SteriStatus.Off;
        this.setSelected(isOn);
    }


    void init(Context cx, AttributeSet attrs) {

        View view = LayoutInflater.from(cx).inflate(R.layout.view_device_unlisted_item,
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

    }

    public void loadData(DeviceType devType) {
        if (devType == null) return;
        txtTitle.setText(devType.getName());
        type = devType;
        if (devType.tag != null) {
            txtDesc.setText(devType.tag);
        }
    }

    @OnClick(R.id.layout)
    public void onClick() {
        if (device == null) return;
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, device.getID());
        boolean layoutSelected = layout.isSelected();
        bd.putBoolean("layout",layoutSelected);
        UIService.getInstance().postPage(PageKey.DeviceSterilizer, bd);
    }


}
