package com.robam.roki.ui.dialog;

import static com.legent.plat.constant.IPlatRokiFamily.R9B010;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.Plat;
import com.legent.plat.constant.IPlatRokiFamily;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.StoveStatusChangedEvent;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.roki.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by yinwei on 2017/12/26.
 */

public class DeviceSelectStoveHeadDialog extends AbsDialog {

    @InjectView(R.id.img_left_header)
    ImageView img_left_header;
    @InjectView(R.id.img_right_header)
    ImageView img_right_header;
    @InjectView(R.id.tv_intellect_tip)
    TextView tv_intellect_tip;
    @InjectView(R.id.left_stove)
    RelativeLayout left_stove;
    Stove stove;

    public interface PickNewDeviceSelectLister {
        void onConfirm(int stoveHead);
    }

    private DeviceSelectStoveHeadDialog.PickNewDeviceSelectLister lister;

    public void setPickNewDeviceSelectLister(DeviceSelectStoveHeadDialog.PickNewDeviceSelectLister lister) {
        this.lister = lister;
    }

    public static DeviceSelectStoveHeadDialog dlg;

    Context cx;

    public DeviceSelectStoveHeadDialog(Context context) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.cx = context;
    }

    @Override
    protected int getViewResId() {
        return R.layout.dialog_stovehead_select;
    }


    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    @OnClick(R.id.left_stove)
    public void onClickLeft() {
        if (stove != null && stove.getDt().equals(IPlatRokiFamily.R9B010)) {
            ToastUtils.show("非智能炉头不支持此功能", Toast.LENGTH_SHORT);
            dlg.dismiss();
            return;
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                this.cancel();
            }
        }, 1500);
        if (lister != null) {
            lister.onConfirm(0);
        }
        dlg.dismiss();
    }

    @OnClick(R.id.right_stove)
    public void onClickRight() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                this.cancel();
            }
        }, 1500);
        if (lister != null) {
            lister.onConfirm(1);
        }
        dlg.dismiss();
    }

    @OnClick(R.id.cannel)
    public void onClickCannel() {
        dlg.dismiss();
    }

    public void updateDialog(Stove stove) {
        this.stove = stove;
        if (stove.leftHead.status == 2) {
            img_left_header.setImageResource(R.mipmap.ic_left_on);
        } else {
            img_left_header.setImageResource(R.mipmap.ic_left_off);
        }
        if (stove.rightHead.status == 2) {
            img_right_header.setImageResource(R.mipmap.ic_right_on);
        } else {
            img_right_header.setImageResource(R.mipmap.ic_right_off);
        }
        if (stove.getDt().equals(IPlatRokiFamily.R9B010)) {
            tv_intellect_tip.setVisibility(View.VISIBLE);
            left_stove.setEnabled(false);
        }

    }

    static public DeviceSelectStoveHeadDialog show(Context cx) {
        dlg = new DeviceSelectStoveHeadDialog(cx);
        Window win = dlg.getWindow();
        WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
        int screenHeight = wm.getDefaultDisplay().getHeight();
        win.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = screenHeight / 2;
        win.setAttributes(wl);
        dlg.show();
        return dlg;
    }

}
