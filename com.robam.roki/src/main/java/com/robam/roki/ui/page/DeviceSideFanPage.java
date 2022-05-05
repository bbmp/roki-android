package com.robam.roki.ui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.legent.ui.ext.views.TitleBar;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.FanStatusChangedEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.FanStatus;
import com.robam.common.pojos.device.fan.IFan;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.UIListeners;
import com.robam.roki.ui.view.FanCtr5610View;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sylar on 15/6/8.
 */
public class DeviceSideFanPage extends HeadPage {


    AbsFan fan;
    UIListeners.IFanCtrView ctrView;

    @InjectView(R.id.imgHead)
    ImageView imgHead;
    @InjectView(R.id.txtTitle)
    TextView txtTitle;
    @InjectView(R.id.txtDesc)
    TextView txtDesc;
    @InjectView(R.id.divMain)
    FrameLayout divMain;
    @InjectView(R.id.disconnectHintView)
    LinearLayout disconnectHintView;

    @Override
    protected View onCreateContentView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {

        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        fan = Plat.deviceService.lookupChild(guid);

        View view = layoutInflater.inflate(R.layout.page_device_fan_side, viewGroup, false);
        ButterKnife.inject(this, view);
        initView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    //------------------------------------------------------------------------- ui event---------------------------------------------------------------------

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (fan == null || !Objects.equal(fan.getID(), event.device.getID()))
            return;

        disconnectHintView.setVisibility(event.isConnected ? View.INVISIBLE : View.VISIBLE);
    }

    @Subscribe
    public void onEvent(FanStatusChangedEvent event) {
        if (fan == null || !Objects.equal(fan.getID(), event.pojo.getID()))
            return;

        onRefresh();
    }

//    @OnClick(R.id.switchView)
//    public void onClickSwitch() {
//
//        short status = (fan.status == FanStatus.Off) ? FanStatus.On
//                : FanStatus.Off;
//        setStatus(status);
//    }

    void initView() {
        //油烟机智能设定按钮
        setTitleBar();
        disconnectHintView.setVisibility(View.INVISIBLE);

//        IFan f = null;
//        String model = f.getFanModel();
//        if (Objects.equal(model, IRokiFamily.R9700)) {
//        } else {
//        }

        if (DeviceTypeManager.getInstance().isInDeviceType(fan.getGuid(), IRokiFamily.R5610)) {
            ctrView = new FanCtr5610View(cx);
        }else{
//            可以在此增加其他侧吸油烟机的view
        }

        Preconditions.checkNotNull(ctrView, "invalid fan, no matched view");
        ctrView.attachFan(fan);
        divMain.addView((View) ctrView);

        onRefresh();
    }
    private void setTitleBar() {
        TextView txtSmart = TitleBar.newTitleTextView(getActivity(), "智能设定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIService.getInstance().postPage(PageKey.SmartParams);
            }
        });
        txtSmart.setTextColor(getResources().getColor(R.color.c11));
        getTitleBar().replaceRight(txtSmart);
    }
    void onRefresh() {

        disconnectHintView.setVisibility(fan != null && !fan.isConnected()
                ? View.VISIBLE
                : View.INVISIBLE);

        if (fan == null)
            return;

        boolean isOn = fan.isConnected() && fan.status != FanStatus.Off;
        //switchView.setStatus(isOn);
        ctrView.onRefresh();
    }

    void setStatus(short status) {

        if (!checkConnection())
            return;

        fan.setFanStatus(status, new VoidCallback() {

            @Override
            public void onSuccess() {
                onRefresh();
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });
    }

    boolean checkConnection() {
        if (!fan.isConnected()) {
            ToastUtils.showShort(R.string.fan_invalid_error);
            return false;
        } else {
            return true;
        }
    }

}
