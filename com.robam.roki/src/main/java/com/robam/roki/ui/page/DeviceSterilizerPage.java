package com.robam.roki.ui.page;



import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.legent.ui.ext.views.TitleBar;
import com.robam.common.events.SteriAlarmEvent;
import com.robam.common.events.SteriStatusChangedEvent;
import com.robam.common.pojos.device.Sterilizer.ISterilizer;
import com.robam.common.pojos.device.Sterilizer.Steri829;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.UIListeners;
import com.robam.roki.ui.view.SteriCtr829View;
import com.robam.roki.utils.DialogUtil;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 消毒柜
 */
public class DeviceSterilizerPage extends HeadPage {
    Steri829 sterilizer;
    UIListeners.ISteriCtrView ctrView;
    @InjectView(R.id.disconnectHintView)
    LinearLayout disconnectHintView;
    @InjectView(R.id.fl_sterilizer_main)
    FrameLayout sterilizerMain;
    private boolean isOn;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        isOn = bd == null ? null : bd.getBoolean("layout");
        sterilizer = Plat.deviceService.lookupChild(guid);
        View view = inflater.inflate(R.layout.page_device_sterilizer, container, false);
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
        if (sterilizer == null || !Objects.equal(sterilizer.getID(), event.device.getID()))
            return;
        disconnectHintView.setVisibility(event.isConnected ? View.INVISIBLE : View.VISIBLE);
    }

    public static boolean alarm = false;

    @Subscribe
    public void onEvent(SteriStatusChangedEvent event) {
        if (sterilizer == null || !Objects.equal(sterilizer.getID(), event.pojo.getID()))
            return;
        onRefresh();
    }

    @Subscribe
    public void onEvent(SteriAlarmEvent event) {
        if (sterilizer == null || !Objects.equal(sterilizer.getID(), event.absSterilizer.getID()))
            return;
        if (Plat.DEBUG)
            Log.i("sterilizer","alarm"+event.alarm);
        setAlarmDialog(event.alarm);
    }

    /**
     * 设置报警弹出框
     */
    private void setAlarmDialog(short alarmId){
        switch (alarmId){
            case 0://门控报警
                IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_09);
                dialogByType.setToastShowTime(DialogUtil.LENGTH_CENTER);
                dialogByType.setContentText(R.string.device_alarm_gating_content);
                dialogByType.show();
                break;
            default:
                break;
        }
    }

    void initView() {
        disconnectHintView.setVisibility(View.INVISIBLE);
        //if (DeviceTypeManager.getInstance().isInDeviceType(sterilizer.getGuid(), IRokiFamily.RR829)) {
        //增加829的view
        ctrView = new SteriCtr829View(cx);
        //}
        setCtrView();
        Preconditions.checkNotNull(ctrView, "invalid fan, no matched view");
        ctrView.attachSteri(sterilizer);
        sterilizerMain.addView((View) ctrView);
        //消毒柜智能设定按钮
        setTitleBar();
        onRefresh();
    }

    private void setTitleBar() {
        TextView txtSmart = TitleBar.newTitleTextView(getActivity(), "智能设定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sterilizer != null){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PageArgumentKey.steri829, sterilizer);
                    UIService.getInstance().postPage(PageKey.SteriSmartParams,bundle);
                }
            }
        });
        txtSmart.setTextColor(getResources().getColor(R.color.c11));
        getTitleBar().replaceRight(txtSmart);
    }

    private void setCtrView() {
        CheckBox cbSwitch = ((View) ctrView).findViewById(R.id.sterilizer_switch);
        TextView tvOrder = ((View) ctrView).findViewById(R.id.tv_order_btn);
        TextView tvStoving = ((View) ctrView).findViewById(R.id.tv_stoving_btn);
        TextView tvClean = ((View) ctrView).findViewById(R.id.tv_clean_btn);
        final TextView tvSterilizer = ((View) ctrView).findViewById(R.id.tv_sterilizer_btn);
        cbSwitch.setChecked(isOn);
        tvOrder.setSelected(isOn);
        tvStoving.setSelected(isOn);
        tvClean.setSelected(isOn);
        tvSterilizer.setSelected(isOn);
        if (tvSterilizer.isSelected()) {
            tvSterilizer.setTextColor(Color.parseColor("#ffffff"));

        } else {
            tvSterilizer.setTextColor(Color.parseColor("#575757"));
        }

    }

    void onRefresh() {
        disconnectHintView.setVisibility(sterilizer.isConnected() ? View.INVISIBLE : View.VISIBLE);
        if (sterilizer == null)
            return;
        ctrView.onRefresh();
    }

}
