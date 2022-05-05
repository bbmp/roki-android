package com.robam.roki.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.legent.plat.pojos.device.AbsDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.robam.common.ui.UiHelper;
import com.robam.roki.R;
import com.robam.roki.model.DeviceWorkMsg;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rosicky on 15/12/9.
 */
public class SteamOvenStartWorkDialog extends AbsDialog {

    @InjectView(R.id.txtOne)
    TextView txtOne;
    @InjectView(R.id.txtTwo)
    TextView txtTwo;
    @InjectView(R.id.start)
    TextView start;

    AbsDevice deivce;

    DeviceWorkMsg deviceWorkMsg = null;

    public SteamOvenStartWorkDialog(Context context) {
        super(context, R.style.Theme_Dialog_HorziFullScreen);
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_start_work;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    public void setText(String one, String two) {
        if (one != null) txtOne.setText(one);
        if (two != null) txtTwo.setText(two);
    }

    public void setMsg(DeviceWorkMsg msg) {
        deviceWorkMsg = msg;
    }
    public void setDevice(AbsDevice deivce) {
        this.deivce = deivce;
    }

    static public void show(Context cx, DeviceWorkMsg msg, AbsDevice device) {
        SteamOvenStartWorkDialog dlg = new SteamOvenStartWorkDialog(cx);
        Window win = dlg.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.alpha = 0.6f;
        win.setAttributes(lp);
        String one = "", two = "";
        if (msg.getType().equals("自洁")) {
            one = "请保持水箱水量，并在凹状加热盘内滴些食醋";
            two = "*工作10分钟后即可轻松去除水垢";
        } else if (msg.getType().equals("杀菌")) {
            one = "即将开启60分钟的高温蒸汽杀菌模式";
            two = "*请保持水箱水量足够";
        }
        dlg.setMsg(msg);
        dlg.setDevice(device);
        dlg.setText(one, two);
        dlg.show();
    }

    @OnClick(R.id.start)
    public void onClickStart() {
        dismiss();
        if (UiHelper.checkAuthWithDialog(getContext(), PageKey.UserLogin)) {
            Bundle bundle = new Bundle();
            if (deviceWorkMsg != null) {
                bundle.putSerializable("msg", deviceWorkMsg);
            }
            bundle.putString(PageArgumentKey.Guid, deivce.getID());
            UIService.getInstance().postPage(PageKey.DeviceSteamWorking, bundle);
        }
    }

}
